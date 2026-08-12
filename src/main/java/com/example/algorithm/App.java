package com.example.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class App {

    public static void main(String[] args) {
        // ==================== 可調整參數：開始 ====================

        List<String> people = Arrays.asList(
                "Hugo", "Jacky.I", "Knox", "Meachel",
                "Tomoto", "Ultraman", "kingstar"
        );

        List<Module> modules = Arrays.asList(
                module("资金帐务",      19, 19, 19, 19, 19, 19, 19),
                module("钱包相关",      17, 15, 17, 17, 16, 17, 19),
                module("法币相关",      17, 12, 18, 18, 17, 16, 19),
                module("福利中心",      15, 16, 17, 15, 15, 15, 15),
                module("个人中心",       5,  5,  4,  5,  2,  4,  9),
                module("Web3 / DEX",   18, 18, 19, 18, 18, 18, 19),
                module("三方服务",      12, 14, 16, 13,  6,  9, 10),
                module("报表对接",       3,  2,  3,  2,  3,  2,  5),
                module("经纪人系统",    10,  4, 13, 10, 11,  5, 10),
                module("后台营运",       2,  1,  8,  7,  7,  1,  8),
                module("理财宝",        11, 10, 16,  9, 12,  8,  1),
                module("HiChat",        9,  8, 10,  7, 10, 10,  3),
                module("合约推送",      15, 11, 14, 15, 14, 12, 19),
                module("风控中心管理",  15, 17, 18, 17,  8, 13, 16),
                module("风控策略管理",  17, 13, 19, 14, 13, 14, 16),
                module("风控权限治理",   6,  3,  2,  3,  9, 11, 19),
                module("风控配置中心",   3,  4,  2,  4,  5,  3, 19),
                module("風控報表",      10,  9, 15, 10,  1,  6, 19),
                module("风控工单",       7,  7,  7,  7,  4,  7, 19)
        );

        double primaryShare = 0.40; // 主要負責人佔比
        double secondShare  = 0.30; // 第二負責人佔比
        double thirdShare   = 0.30; // 第三負責人佔比

        int maxPrimaryModulesPerPerson = 3; // 每人最多主責幾個模組

        // ==================== 可調整參數：結束 ====================

        Config config = new Config(
                people,
                modules,
                primaryShare,
                secondShare,
                thirdShare,
                maxPrimaryModulesPerPerson
        );

        Result result = solve(config);
        printResult(config, result);
    }

    /**
     * 全域最佳化：
     * 1. 每個模組配置主責、第二負責、第三負責各一人，三人不可重複。
     * 2. 每人的主責模組數不可超過 maxPrimaryModulesPerPerson。
     * 3. 最小化所有模組三位負責人的原始評分總和。
     *
     * 現行規則只有「主責數」受限制，因此當某人為主責時，該模組另外兩位
     * 一定可直接選其餘人中分數最低的兩位；再以動態規劃決定全域主責配置。
     */
    public static Result solve(Config config) {
        validate(config);

        int personCount = config.people.size();
        int moduleCount = config.modules.size();
        int base = config.maxPrimaryModulesPerPerson + 1;

        long[] placeValue = new long[personCount];
        placeValue[0] = 1L;
        for (int i = 1; i < personCount; i++) {
            placeValue[i] = Math.multiplyExact(placeValue[i - 1], base);
        }

        Choice[][] choices = new Choice[moduleCount][personCount];
        for (int m = 0; m < moduleCount; m++) {
            for (int primary = 0; primary < personCount; primary++) {
                choices[m][primary] = bestChoiceForPrimary(config, m, primary);
            }
        }

        List<Map<Long, DpNode>> layers = new ArrayList<Map<Long, DpNode>>();
        Map<Long, DpNode> current = new HashMap<Long, DpNode>();
        current.put(0L, new DpNode(0L, 0L, -1L, -1));
        layers.add(current);

        for (int moduleIndex = 0; moduleIndex < moduleCount; moduleIndex++) {
            Map<Long, DpNode> next = new HashMap<Long, DpNode>();

            for (Map.Entry<Long, DpNode> entry : current.entrySet()) {
                long state = entry.getKey();
                DpNode previous = entry.getValue();

                for (int primary = 0; primary < personCount; primary++) {
                    int count = getCount(state, placeValue[primary], base);
                    if (count >= config.maxPrimaryModulesPerPerson) {
                        continue;
                    }

                    Choice choice = choices[moduleIndex][primary];
                    long nextState = state + placeValue[primary];
                    DpNode candidate = new DpNode(
                            previous.totalScore + choice.responsibleScore,
                            previous.totalPrimaryScore + choice.primaryScore,
                            state,
                            primary
                    );

                    DpNode old = next.get(nextState);
                    if (old == null || isBetter(candidate, old)) {
                        next.put(nextState, candidate);
                    }
                }
            }

            if (next.isEmpty()) {
                throw new IllegalStateException("找不到可行解，請提高每人主責上限或增加成員。");
            }

            current = next;
            layers.add(current);
        }

        long bestState = -1L;
        DpNode bestNode = null;
        for (Map.Entry<Long, DpNode> entry : current.entrySet()) {
            if (bestNode == null
                    || isBetter(entry.getValue(), bestNode)
                    || (sameCost(entry.getValue(), bestNode)
                    && isMoreBalanced(entry.getKey(), bestState, placeValue, base))) {
                bestState = entry.getKey();
                bestNode = entry.getValue();
            }
        }

        Assignment[] assignments = new Assignment[moduleCount];
        long state = bestState;
        for (int m = moduleCount - 1; m >= 0; m--) {
            DpNode node = layers.get(m + 1).get(state);
            Choice choice = choices[m][node.primary];
            assignments[m] = new Assignment(
                    m,
                    choice.primary,
                    choice.second,
                    choice.third,
                    choice.responsibleScore
            );
            state = node.previousState;
        }

        return calculateShares(config, assignments, bestNode.totalScore);
    }

    private static Choice bestChoiceForPrimary(Config config, int moduleIndex, int primary) {
        Module module = config.modules.get(moduleIndex);
        List<Integer> candidates = new ArrayList<Integer>();

        for (int person = 0; person < config.people.size(); person++) {
            if (person != primary) {
                candidates.add(person);
            }
        }

        Collections.sort(candidates, new Comparator<Integer>() {
            @Override
            public int compare(Integer a, Integer b) {
                int byScore = Integer.compare(module.scores[a], module.scores[b]);
                return byScore != 0 ? byScore : Integer.compare(a, b);
            }
        });

        int lowerScorePerson = candidates.get(0);
        int otherPerson = candidates.get(1);

        // 較高的角色佔比交給評分較低的人；若佔比相同則依成員順序固定結果。
        int second;
        int third;
        if (config.secondShare >= config.thirdShare) {
            second = lowerScorePerson;
            third = otherPerson;
        } else {
            second = otherPerson;
            third = lowerScorePerson;
        }

        long responsibleScore = (long) module.scores[primary]
                + module.scores[second]
                + module.scores[third];

        return new Choice(
                primary,
                second,
                third,
                responsibleScore,
                module.scores[primary]
        );
    }

    private static Result calculateShares(
            Config config,
            Assignment[] assignments,
            long minimumTotalScore
    ) {
        long denominator = 0L;

        for (Assignment assignment : assignments) {
            Module module = config.modules.get(assignment.moduleIndex);
            long moduleTotal = 0L;
            for (int score : module.scores) {
                moduleTotal += score;
            }
            assignment.numerator = moduleTotal - assignment.responsibleScore;
            denominator += assignment.numerator;
        }

        if (denominator <= 0L) {
            throw new IllegalStateException("模組佔比分母必須大於 0，請檢查評分資料。");
        }

        int personCount = config.people.size();
        double[] workload = new double[personCount];
        int[] primaryCount = new int[personCount];
        int[] secondCount = new int[personCount];
        int[] thirdCount = new int[personCount];

        for (Assignment assignment : assignments) {
            assignment.moduleShare = (double) assignment.numerator / denominator;
            assignment.primaryWorkload = assignment.moduleShare * config.primaryShare;
            assignment.secondWorkload = assignment.moduleShare * config.secondShare;
            assignment.thirdWorkload = assignment.moduleShare * config.thirdShare;

            workload[assignment.primary] += assignment.primaryWorkload;
            workload[assignment.second] += assignment.secondWorkload;
            workload[assignment.third] += assignment.thirdWorkload;

            primaryCount[assignment.primary]++;
            secondCount[assignment.second]++;
            thirdCount[assignment.third]++;
        }

        return new Result(
                Arrays.asList(assignments),
                minimumTotalScore,
                denominator,
                workload,
                primaryCount,
                secondCount,
                thirdCount
        );
    }

    private static void printResult(Config config, Result result) {
        System.out.println("===== 最佳模組分配 =====");
        System.out.println("業務模組\t主責\t第二負責\t第三負責\t負責人評分總和\t分子\t模組佔比");

        for (Assignment a : result.assignments) {
            Module module = config.modules.get(a.moduleIndex);
            System.out.printf(
                    Locale.ROOT,
                    "%s\t%s\t%s\t%s\t%d\t%d\t%s%n",
                    module.name,
                    config.people.get(a.primary),
                    config.people.get(a.second),
                    config.people.get(a.third),
                    a.responsibleScore,
                    a.numerator,
                    percent(a.moduleShare)
            );
        }

        System.out.println();
        System.out.println("最小評分總和：" + result.minimumTotalScore);
        System.out.println("模組佔比分母：" + result.denominator);

        System.out.println();
        System.out.println("===== 個人工作量 =====");
        System.out.println("成員\t主責數\t第二負責數\t第三負責數\t總模組數\t個人佔比");

        double totalWorkload = 0.0;
        for (int person = 0; person < config.people.size(); person++) {
            int totalModules = result.primaryCount[person]
                    + result.secondCount[person]
                    + result.thirdCount[person];
            totalWorkload += result.workload[person];

            System.out.printf(
                    Locale.ROOT,
                    "%s\t%d\t%d\t%d\t%d\t%s%n",
                    config.people.get(person),
                    result.primaryCount[person],
                    result.secondCount[person],
                    result.thirdCount[person],
                    totalModules,
                    percent(result.workload[person])
            );
        }

        System.out.println("個人佔比合計：" + percent(totalWorkload));
    }

    private static boolean isBetter(DpNode candidate, DpNode old) {
        if (candidate.totalScore != old.totalScore) {
            return candidate.totalScore < old.totalScore;
        }
        return candidate.totalPrimaryScore < old.totalPrimaryScore;
    }

    private static boolean sameCost(DpNode a, DpNode b) {
        return a.totalScore == b.totalScore
                && a.totalPrimaryScore == b.totalPrimaryScore;
    }

    private static boolean isMoreBalanced(
            long candidateState,
            long oldState,
            long[] placeValue,
            int base
    ) {
        if (oldState < 0L) {
            return true;
        }

        int candidateMax = 0;
        int oldMax = 0;
        int candidateSquares = 0;
        int oldSquares = 0;

        for (long place : placeValue) {
            int candidateCount = getCount(candidateState, place, base);
            int oldCount = getCount(oldState, place, base);
            candidateMax = Math.max(candidateMax, candidateCount);
            oldMax = Math.max(oldMax, oldCount);
            candidateSquares += candidateCount * candidateCount;
            oldSquares += oldCount * oldCount;
        }

        if (candidateMax != oldMax) {
            return candidateMax < oldMax;
        }
        if (candidateSquares != oldSquares) {
            return candidateSquares < oldSquares;
        }
        return candidateState < oldState;
    }

    private static int getCount(long state, long placeValue, int base) {
        return (int) ((state / placeValue) % base);
    }

    private static String percent(double value) {
        return String.format(Locale.ROOT, "%.2f%%", value * 100.0);
    }

    private static Module module(String name, int... scores) {
        return new Module(name, scores);
    }

    private static void validate(Config config) {
        if (config.people == null || config.people.size() < 3) {
            throw new IllegalArgumentException("至少需要 3 位成員。");
        }
        if (config.modules == null || config.modules.isEmpty()) {
            throw new IllegalArgumentException("至少需要 1 個業務模組。");
        }
        if (config.maxPrimaryModulesPerPerson <= 0) {
            throw new IllegalArgumentException("每人主責上限必須大於 0。");
        }
        if ((long) config.people.size() * config.maxPrimaryModulesPerPerson
                < config.modules.size()) {
            throw new IllegalArgumentException("主責名額不足：成員數 × 每人主責上限小於模組數。");
        }

        double shareTotal = config.primaryShare + config.secondShare + config.thirdShare;
        if (config.primaryShare < 0.0 || config.secondShare < 0.0 || config.thirdShare < 0.0) {
            throw new IllegalArgumentException("角色佔比不可為負數。");
        }
        if (Math.abs(shareTotal - 1.0) > 1e-9) {
            throw new IllegalArgumentException("主責、第二負責、第三負責佔比必須加總為 1.0。");
        }

        long stateCount = 1L;
        int base = config.maxPrimaryModulesPerPerson + 1;
        for (int i = 0; i < config.people.size(); i++) {
            stateCount = Math.multiplyExact(stateCount, base);
        }

        for (Module module : config.modules) {
            if (module.scores == null || module.scores.length != config.people.size()) {
                throw new IllegalArgumentException(
                        "模組「" + module.name + "」的評分數量必須等於成員數。"
                );
            }
            for (int score : module.scores) {
                if (score < 0) {
                    throw new IllegalArgumentException("評分不可為負數：" + module.name);
                }
            }
        }
    }

    public static final class Config {
        final List<String> people;
        final List<Module> modules;
        final double primaryShare;
        final double secondShare;
        final double thirdShare;
        final int maxPrimaryModulesPerPerson;

        Config(
                List<String> people,
                List<Module> modules,
                double primaryShare,
                double secondShare,
                double thirdShare,
                int maxPrimaryModulesPerPerson
        ) {
            this.people = people;
            this.modules = modules;
            this.primaryShare = primaryShare;
            this.secondShare = secondShare;
            this.thirdShare = thirdShare;
            this.maxPrimaryModulesPerPerson = maxPrimaryModulesPerPerson;
        }
    }

    public static final class Module {
        final String name;
        final int[] scores;

        Module(String name, int[] scores) {
            this.name = name;
            this.scores = scores;
        }
    }

    public static final class Result {
        final List<Assignment> assignments;
        final long minimumTotalScore;
        final long denominator;
        final double[] workload;
        final int[] primaryCount;
        final int[] secondCount;
        final int[] thirdCount;

        Result(
                List<Assignment> assignments,
                long minimumTotalScore,
                long denominator,
                double[] workload,
                int[] primaryCount,
                int[] secondCount,
                int[] thirdCount
        ) {
            this.assignments = assignments;
            this.minimumTotalScore = minimumTotalScore;
            this.denominator = denominator;
            this.workload = workload;
            this.primaryCount = primaryCount;
            this.secondCount = secondCount;
            this.thirdCount = thirdCount;
        }
    }

    public static final class Assignment {
        final int moduleIndex;
        final int primary;
        final int second;
        final int third;
        final long responsibleScore;
        long numerator;
        double moduleShare;
        double primaryWorkload;
        double secondWorkload;
        double thirdWorkload;

        Assignment(
                int moduleIndex,
                int primary,
                int second,
                int third,
                long responsibleScore
        ) {
            this.moduleIndex = moduleIndex;
            this.primary = primary;
            this.second = second;
            this.third = third;
            this.responsibleScore = responsibleScore;
        }
    }

    private static final class Choice {
        final int primary;
        final int second;
        final int third;
        final long responsibleScore;
        final long primaryScore;

        Choice(
                int primary,
                int second,
                int third,
                long responsibleScore,
                long primaryScore
        ) {
            this.primary = primary;
            this.second = second;
            this.third = third;
            this.responsibleScore = responsibleScore;
            this.primaryScore = primaryScore;
        }
    }

    private static final class DpNode {
        final long totalScore;
        final long totalPrimaryScore;
        final long previousState;
        final int primary;

        DpNode(
                long totalScore,
                long totalPrimaryScore,
                long previousState,
                int primary
        ) {
            this.totalScore = totalScore;
            this.totalPrimaryScore = totalPrimaryScore;
            this.previousState = previousState;
            this.primary = primary;
        }
    }
}