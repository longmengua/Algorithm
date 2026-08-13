package com.example.algorithm;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * 業務模組分配與獎懲計算規則
 *
 * 設計目的：
 * 在確保每個模組都有主責與備援的前提下，兼顧人員適配度、件數公平、
 * 加權工作量與獎懲合理性，讓團隊人力得到較有效的運用。
 *
 * 一、模組覆蓋
 *
 * 1. 每個模組固定配置 1 位主責，以及 ModuleRule 設定的 1～2 位備援。
 *
 * 2. 同一個模組的主責與備援必須由不同成員擔任，避免休假時無人接手。
 *
 * 二、件數平衡
 *
 * 3. 每位成員的「主責＋備援」總件數，最大與最小最多相差 1。
 *
 * 4. 每位成員的主責數，最大與最小最多相差 1。
 *
 * 5. 每位成員的備援總數，最大與最小最多相差 1。
 *
 * 以上三項都是硬性限制，不會因難易度或負載最佳化而被破壞。
 *
 * 三、人選分配
 *
 * 6. 每位成員須將所有模組由易到難排序：
 *
 *    - 名次 1：自己認為最容易處理。
 *    - 名次 19：自己認為最困難處理。
 *
 * 7. 分配時會優先選擇「自己認為該模組較容易」的成員。
 *    模組權重、加級倍率或角色責任比例越高，人選適配度的重要性也越高。
 *
 * 8. 例如某個模組的團隊平均難度接近 19，而某位成員將它排在第 16 名，
 *    代表該成員雖然也認為它困難，但相較其他成員仍較有能力處理。
 *    因此，在件數平衡、主備不可重複等限制都符合時，
 *    該成員會有較高機會被安排為主責或備援，但不代表一定會被選中。
 *
 * 9. 完成初始分配後，程式會嘗試交換同類角色的人選：
 *
 *    - 主責只能與其他主責交換。
 *    - 備援只能與其他備援交換。
 *
 *    交換的目的是降低高難度、高權重工作過度集中於少數人的情況，
 *    同時維持總件數、主責數與備援數的平衡。
 *
 * 四、獎懲計算
 *
 * 10. 模組的共同難度，取所有成員難度名次的平均值：
 *
 *     共同難度 = 全體成員對該模組的難度名次合計 ÷ 成員數
 *
 *     平均名次越大，代表團隊普遍認為該模組越困難。
 *
 * 11. 模組的獎懲權重計算方式：
 *
 *     獎懲權重 = 基礎權重 × 加級倍率 × 共同難度
 *
 *     因此：
 *
 *     - 大家普遍認為困難的模組，獎懲權重較高。
 *     - 大家普遍認為簡單的模組，獎懲權重較低。
 *     - 業務重要性較高的模組，可透過基礎權重或加級倍率提高比重。
 *
 * 12. 模組獎懲權重會再依角色責任比例分配：
 *
 *     - 1 位主責＋1 位備援：預設為 70%／30%。
 *     - 1 位主責＋2 位備援：預設為 60%／25%／15%。
 *
 *     增加備援不會增加該模組的總獎懲，只會重新分配同一份權重。
 *
 * 五、排序原則
 *
 * 13. 每位成員提供的排序本身是主觀判斷；彙整全體成員的排序後，
 *     可以形成較具代表性的團隊共識，降低由單一成員決定所產生的偏差。
 *
 *     這是一種相對客觀的分析方式，但不是絕對客觀的難度測量。
 *
 * 14. 請依自己實際理解、維護經驗與處理難度誠實排序，
 *     不需要考慮自己是否想承接該模組，也不要為了影響分配結果而調整名次。
 */
public class App {

    /**
     * Person 改為一般類別，不再使用 enum。
     * 新成員只要在 main 建立 Person、加入 people 並提供完整排序即可。
     */
    public static final class Person {
        /** 穩定且不可重複的成員代碼，例如 M1；演算法用它判斷是否為同一人。 */
        private final String id;

        /** 報表顯示名稱，例如 Hugo；不參與任何計算。 */
        private final String displayName;

        public Person(String id, String displayName) {
            if (isBlank(id)) {
                throw new IllegalArgumentException("成員 id 不可空白。");
            }
            if (isBlank(displayName)) {
                throw new IllegalArgumentException("成員名稱不可空白。");
            }
            this.id = id;
            this.displayName = displayName;
        }

        public String getId() {
            return id;
        }

        public String getDisplayName() {
            return displayName;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Person)) {
                return false;
            }
            Person that = (Person) other;
            return id.equals(that.id);
        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    public enum BusinessModule {
        Biz1("资金帐务"),
        Biz2("钱包相关"),
        Biz3("法币相关"),
        Biz4("福利中心"),
        Biz5("个人中心"),
        Biz6("Web3 / DEX"),
        Biz7("三方服务"),
        Biz8("报表对接"),
        Biz9("经纪人系统"),
        Biz10("后台营运"),
        Biz11("理财宝"),
        Biz12("HiChat"),
        Biz13("合约推送"),
        Biz14("风控中心管理"),
        Biz15("风控策略管理"),
        Biz16("风控权限治理"),
        Biz17("风控配置中心"),
        Biz18("風控報表"),
        Biz19("风控工单");

        /** 報表顯示名稱；enum 本身（Biz1、Biz2……）才是程式內的穩定識別值。 */
        private final String displayName;

        BusinessModule(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    public static void main(String[] args) {
        // 自動測試模式不使用下方真實名單，只驗證不同人數下的分配限制。
        if (args.length == 1 && "--self-test".equals(args[0])) {
            runDynamicHeadcountSelfTest();
            return;
        }

        // ==================== 可調整資料：開始 ====================

        // ---------- 1. 成員名單 ----------
        // 第一個參數是不可重複的 id，第二個參數是輸出時顯示的姓名。
        Person m1 = new Person("M1", "Hugo");
        Person m2 = new Person("M2", "Jacky.I");
        Person m3 = new Person("M3", "Knox");
        Person m4 = new Person("M4", "Meachel");
        Person m5 = new Person("M5", "Tomoto");
        Person m6 = new Person("M6", "Ultraman");
        Person m7 = new Person("M7", "kingstar");

        /*
         * 人數完全由這份清單決定，不綁死方案 A／B／C。
         * 方案 B 若有 9 人，只需再加入 m8、m9，並在下方提供兩人的完整排序。
         */
        List<Person> people = Arrays.asList(m1, m2, m3, m4, m5, m6, m7);

        // ---------- 2. 每位成員的主觀難易度排序 ----------
        // index 0 代表此人認為最簡單（名次 1）；最後一個代表最難（名次 19）。
        // 每位成員必須把全部模組各放一次，不可缺少、重複或包含 null。
        // 此排序有兩個用途：
        //   A. 分配時優先找「認為該模組較容易」的人。
        //   B. 彙總所有人的名次，計算該模組的共同難度與獎懲權重。
        Map<Person, BusinessModule[]> rankingsByPerson =
                new LinkedHashMap<Person, BusinessModule[]>();

        rankingsByPerson.put(m1, ranking(
                BusinessModule.Biz10,
                BusinessModule.Biz17,
                BusinessModule.Biz8,
                BusinessModule.Biz5,
                BusinessModule.Biz16,
                BusinessModule.Biz19,
                BusinessModule.Biz18,
                BusinessModule.Biz12,
                BusinessModule.Biz9,
                BusinessModule.Biz11,
                BusinessModule.Biz7,
                BusinessModule.Biz6,
                BusinessModule.Biz4,
                BusinessModule.Biz14,
                BusinessModule.Biz13,
                BusinessModule.Biz15,
                BusinessModule.Biz3,
                BusinessModule.Biz2,
                BusinessModule.Biz1
        ));

        rankingsByPerson.put(m2, ranking(
                BusinessModule.Biz10,
                BusinessModule.Biz8,
                BusinessModule.Biz9,
                BusinessModule.Biz17,
                BusinessModule.Biz5,
                BusinessModule.Biz13,
                BusinessModule.Biz19,
                BusinessModule.Biz12,
                BusinessModule.Biz18,
                BusinessModule.Biz11,
                BusinessModule.Biz16,
                BusinessModule.Biz3,
                BusinessModule.Biz7,
                BusinessModule.Biz15,
                BusinessModule.Biz2,
                BusinessModule.Biz4,
                BusinessModule.Biz14,
                BusinessModule.Biz6,
                BusinessModule.Biz1
        ));

        rankingsByPerson.put(m3, ranking(
                BusinessModule.Biz14,
                BusinessModule.Biz5,
                BusinessModule.Biz10,
                BusinessModule.Biz16,
                BusinessModule.Biz8,
                BusinessModule.Biz17,
                BusinessModule.Biz19,
                BusinessModule.Biz18,
                BusinessModule.Biz7,
                BusinessModule.Biz12,
                BusinessModule.Biz13,
                BusinessModule.Biz9,
                BusinessModule.Biz3,
                BusinessModule.Biz15,
                BusinessModule.Biz4,
                BusinessModule.Biz11,
                BusinessModule.Biz2,
                BusinessModule.Biz6,
                BusinessModule.Biz1
        ));

        rankingsByPerson.put(m4, ranking(
                BusinessModule.Biz8,
                BusinessModule.Biz17,
                BusinessModule.Biz5,
                BusinessModule.Biz10,
                BusinessModule.Biz19,
                BusinessModule.Biz12,
                BusinessModule.Biz16,
                BusinessModule.Biz18,
                BusinessModule.Biz11,
                BusinessModule.Biz9,
                BusinessModule.Biz13,
                BusinessModule.Biz7,
                BusinessModule.Biz14,
                BusinessModule.Biz4,
                BusinessModule.Biz3,
                BusinessModule.Biz15,
                BusinessModule.Biz2,
                BusinessModule.Biz6,
                BusinessModule.Biz1
        ));

        rankingsByPerson.put(m5, ranking(
                BusinessModule.Biz4,
                BusinessModule.Biz5,
                BusinessModule.Biz16,
                BusinessModule.Biz19,
                BusinessModule.Biz8,
                BusinessModule.Biz10,
                BusinessModule.Biz17,
                BusinessModule.Biz14,
                BusinessModule.Biz18,
                BusinessModule.Biz7,
                BusinessModule.Biz12,
                BusinessModule.Biz9,
                BusinessModule.Biz13,
                BusinessModule.Biz11,
                BusinessModule.Biz15,
                BusinessModule.Biz1,
                BusinessModule.Biz2,
                BusinessModule.Biz3,
                BusinessModule.Biz6
        ));

        rankingsByPerson.put(m6, ranking(
                BusinessModule.Biz18,
                BusinessModule.Biz8,
                BusinessModule.Biz5,
                BusinessModule.Biz9,
                BusinessModule.Biz17,
                BusinessModule.Biz7,
                BusinessModule.Biz10,
                BusinessModule.Biz19,
                BusinessModule.Biz11,
                BusinessModule.Biz16,
                BusinessModule.Biz6,
                BusinessModule.Biz12,
                BusinessModule.Biz14,
                BusinessModule.Biz13,
                BusinessModule.Biz4,
                BusinessModule.Biz15,
                BusinessModule.Biz3,
                BusinessModule.Biz2,
                BusinessModule.Biz1
        ));

        rankingsByPerson.put(m7, ranking(
                BusinessModule.Biz10,
                BusinessModule.Biz11,
                BusinessModule.Biz8,
                BusinessModule.Biz12,
                BusinessModule.Biz17,
                BusinessModule.Biz5,
                BusinessModule.Biz18,
                BusinessModule.Biz19,
                BusinessModule.Biz7,
                BusinessModule.Biz16,
                BusinessModule.Biz9,
                BusinessModule.Biz13,
                BusinessModule.Biz15,
                BusinessModule.Biz2,
                BusinessModule.Biz3,
                BusinessModule.Biz4,
                BusinessModule.Biz14,
                BusinessModule.Biz6,
                BusinessModule.Biz1
        ));

        // ---------- 3. 模組權重、倍率與備援人數 ----------
        /*
         * 先給全部模組相同預設值：
         * - 基礎權重 1.0
         * - 加級倍率 1.0
         * - 1 位主責 70%、1 位備援 30%
         */
        Map<BusinessModule, ModuleRule> moduleRules = defaultModuleRules(
                ModuleRule.withOneBackup(1.0, 1.0)
        );

        /*
         * 預設全部模組都是一位備援，因此目前共有 19 主責 + 19 備援 = 38 件。
         * 若某個模組要提高權重、倍率並改成兩位備援，可解除下方註解：
         *
         * moduleRules.put(
         *         BusinessModule.Biz1,
         *         ModuleRule.withTwoBackups(1.50, 1.20)
         * );
         *
         * 兩位備援的預設責任比例為：主責 60%、備援1 25%、備援2 15%。
         */

        // ---------- 4. 全域分配政策 ----------
        /*
         * 允許人數範圍：7～11 人。
         * 負載平衡強度：
         * 0.0 代表只在乎難易度順位；數字越大，越願意用稍差的順位換取平均負載。
         * maxSwapIterations 是最後最多嘗試套用幾次最佳交換。
         *
         * 注意：總件數、主責數、備援數各自差最多 1，都是硬限制，
         * 與 loadBalanceStrength 無關；
         * loadBalanceStrength 只控制難度／權重換算後的負載要多平均。
         */
        AllocationPolicy policy = new AllocationPolicy(7, 11, 0.25, 10_000);

        // ==================== 可調整資料：結束 ====================

        Config config = Config.fromRankings(
                people,
                rankingsByPerson,
                moduleRules,
                policy
        );
        Result result = solve(config);
        printResult(config, result);
    }

    /**
     * 驗證同一套演算法可直接支援不同人數。
     * 執行：java App.java --self-test
     */
    private static void runDynamicHeadcountSelfTest() {
        for (int headcount = 3; headcount <= 12; headcount++) {
            List<Person> people = new ArrayList<Person>();
            Map<Person, BusinessModule[]> rankings =
                    new LinkedHashMap<Person, BusinessModule[]>();

            for (int index = 0; index < headcount; index++) {
                Person person = new Person(
                        "T" + (index + 1),
                        "Test" + (index + 1)
                );
                people.add(person);
                rankings.put(person, rotatedRanking(index));
            }

            Map<BusinessModule, ModuleRule> rules = defaultModuleRules(
                    ModuleRule.withOneBackup(1.0, 1.0)
            );

            // 情境一：全部模組都是一位備援（19 主責 + 19 備援 = 38 角色）。
            assertThreeCountBalances(
                    solve(Config.fromRankings(
                            people,
                            rankings,
                            rules,
                            new AllocationPolicy(1.0, 2_000)
                    )),
                    headcount,
                    "全部一位備援"
            );

            // 情境二：部分模組配置兩位備援，驗證備援總數不能整除人數時也成立。
            rules.put(BusinessModule.Biz1, ModuleRule.withTwoBackups(1.5, 1.2));
            rules.put(BusinessModule.Biz2, ModuleRule.withTwoBackups(1.3, 1.1));

            assertThreeCountBalances(
                    solve(Config.fromRankings(
                            people,
                            rankings,
                            rules,
                            new AllocationPolicy(1.0, 2_000)
                    )),
                    headcount,
                    "混合一至兩位備援"
            );
        }
        System.out.println(
                "動態人數 3～12 人、兩種備援情境，"
                        + "總數／主責／備援三項平衡測試：PASS"
        );
    }

    private static void assertThreeCountBalances(
            Result result,
            int headcount,
            String scenario
    ) {
        if (result.totalCountBalance.observedMaximum
                - result.totalCountBalance.observedMinimum > 1
                || result.primaryCountBalance.observedMaximum
                - result.primaryCountBalance.observedMinimum > 1
                || result.backupCountBalance.observedMaximum
                - result.backupCountBalance.observedMinimum > 1) {
            throw new IllegalStateException(
                    "動態人數三項件數平衡測試失敗："
                            + headcount + " 人，情境=" + scenario + "。"
            );
        }
    }

    private static BusinessModule[] rotatedRanking(int offset) {
        BusinessModule[] modules = BusinessModule.values();
        BusinessModule[] result = new BusinessModule[modules.length];
        for (int index = 0; index < modules.length; index++) {
            result[index] = modules[(index + offset) % modules.length];
        }
        return result;
    }

    /**
     * 一次聯合分配所有主責與備援角色：
     *
     * 1. 每個模組依 ModuleRule 建立 1 個主責及 1～2 個備援角色。
     * 2. module-person gate 容量為 1，保證同模組不會重複使用同一人。
     * 3. person-sink 容量保證每人「主責＋全部備援」總件數只能是
     *    floor(總角色數 / 人數) 或 ceil(總角色數 / 人數)。
     * 4. 以主責／備援交換，讓主責數與備援數也各自落在 floor～ceil。
     * 5. 最後只交換同類角色的人選，在不破壞三項件數限制下改善負載。
     *
     * 最小成本流階段對加權難度成本是全域最佳；後續的負載交換是局部改善。
     */
    public static Result solve(Config config) {
        validate(config);

        JointAssignmentResult joint = assignAllRoles(config);
        List<Assignment> assignments = joint.assignments;

        /*
         * 第一階段只硬性平衡總件數；這裡再用「一份主責 ↔ 一份備援」交換，
         * 在不改變任何人的總件數下，讓主責數與備援數也各自只差 1。
         */
        int roleBalanceSwapCount = balancePrimaryAndBackupCounts(
                config,
                assignments
        );

        // 主／備件數達標後，才進行不破壞三項硬限制的加權負載改善。
        double objectiveBefore = objective(config, assignments);
        int loadBalanceSwapCount = improveWeightedBalance(config, assignments);
        double objectiveAfter = objective(config, assignments);

        return calculateResult(
                config,
                assignments,
                joint.totalRoleCount,
                roleBalanceSwapCount,
                loadBalanceSwapCount,
                objectiveBefore,
                objectiveAfter
        );
    }

    private static JointAssignmentResult assignAllRoles(Config config) {
        // M：模組數；P：實際成員數。P 完全取自 people.size()，因此可動態增減。
        int moduleCount = config.modules.size();
        int personCount = config.people.size();

        // 一個 RoleSlot 代表一份必須有人承接的責任，例如 Biz1-主責或 Biz1-備援2。
        List<RoleSlot> roleSlots = new ArrayList<RoleSlot>();

        for (int module = 0; module < moduleCount; module++) {
            ModuleRule rule = config.modules.get(module).rule;
            for (int role = 0; role < rule.roleCount(); role++) {
                roleSlots.add(new RoleSlot(module, role));
            }
        }

        // K：全部角色數。例如 19 主責 + 19 備援1 + 2 備援2 = 40。
        int totalRoleCount = roleSlots.size();

        // 硬性件數平衡：每人只能拿 floor(K/P) 或 ceil(K/P) 份角色。
        // 例如 K=40、P=7 時，每人只能是 5 或 6 件。
        int minimumPerPerson = totalRoleCount / personCount;
        int maximumPerPerson = (totalRoleCount + personCount - 1) / personCount;

        /*
         * 最小成本流網路的節點排列：
         *
         * source
         *   -> role node（每份主責／備援角色）
         *   -> gate-in -> gate-out（每個 module-person 配對）
         *   -> person node（每位成員）
         *   -> sink
         *
         * gate 容量設為 1，讓同一個人不能在同一模組同時擔任主責與備援。
         */
        int source = 0;
        int roleNodeStart = 1;
        int gateInStart = roleNodeStart + totalRoleCount;
        int gateCount = moduleCount * personCount;
        int gateOutStart = gateInStart + gateCount;
        int personNodeStart = gateOutStart + gateCount;
        int sink = personNodeStart + personCount;

        // 建立殘餘網路；choiceEdges 用來在演算法結束後讀回「角色選中了誰」。
        MinCostFlow network = new MinCostFlow(sink + 1);
        FlowEdge[][] choiceEdges = new FlowEdge[totalRoleCount][personCount];

        // 每份角色可能產生的最大難度成本之和，用來製造絕對優先的件數平衡成本。
        long maximumPreferenceTotal = 0L;

        for (int roleSlotIndex = 0; roleSlotIndex < totalRoleCount; roleSlotIndex++) {
            RoleSlot roleSlot = roleSlots.get(roleSlotIndex);
            int roleNode = roleNodeStart + roleSlotIndex;
            network.addEdge(source, roleNode, 1, 0L);

            // source -> role 容量 1：這份角色只能也必須指派一次。
            long maximumCostForRole = 0L;
            for (int person = 0; person < personCount; person++) {
                int gateIndex = roleSlot.moduleIndex * personCount + person;
                int gateIn = gateInStart + gateIndex;
                long cost = weightedDifficultyCost(
                        config,
                        roleSlot.moduleIndex,
                        person,
                        roleSlot.roleIndex
                );
                // role -> module-person gate 的成本越小，表示此人越適合該角色。
                choiceEdges[roleSlotIndex][person] = network.addEdge(
                        roleNode,
                        gateIn,
                        1,
                        cost
                );
                maximumCostForRole = Math.max(maximumCostForRole, cost);
            }
            maximumPreferenceTotal = safeAddCost(
                    maximumPreferenceTotal,
                    maximumCostForRole
            );
        }

        // 每個 module-person gate 最多通過一份角色，主責與備援因此不重複。
        for (int module = 0; module < moduleCount; module++) {
            for (int person = 0; person < personCount; person++) {
                int gateIndex = module * personCount + person;
                int gateIn = gateInStart + gateIndex;
                int gateOut = gateOutStart + gateIndex;
                network.addEdge(gateIn, gateOut, 1, 0L);
                network.addEdge(gateOut, personNodeStart + person, 1, 0L);
            }
        }

        /*
         * 每人前 minimumPerPerson 份走零成本容量；第 maximum 份需付平衡成本。
         * 平衡成本大於全部難度成本之和，所以演算法一定先滿足所有人的最小數，
         * 最終每人只能是 minimum 或 maximum。
         */
        long balanceCost = safeAddCost(maximumPreferenceTotal, 1L);
        for (int person = 0; person < personCount; person++) {
            int personNode = personNodeStart + person;
            if (minimumPerPerson > 0) {
                network.addEdge(personNode, sink, minimumPerPerson, 0L);
            }
            if (maximumPerPerson > minimumPerPerson) {
                network.addEdge(personNode, sink, 1, balanceCost);
            }
        }

        // 要求網路送出 K 單位流量，等價於所有 K 份角色都完成分配。
        FlowResult flow = network.minCostMaxFlow(source, sink, totalRoleCount);
        if (flow.flow != totalRoleCount) {
            throw new IllegalStateException(
                    "找不到符合總件數平衡及主備不重複限制的完整分配。"
            );
        }

        List<Assignment> assignments = new ArrayList<Assignment>();
        int[] countByPerson = new int[personCount];

        // 從最終殘餘容量還原每份角色的實際人選。
        for (int roleSlotIndex = 0; roleSlotIndex < totalRoleCount; roleSlotIndex++) {
            RoleSlot roleSlot = roleSlots.get(roleSlotIndex);
            int selectedPerson = -1;

            for (int person = 0; person < personCount; person++) {
                if (choiceEdges[roleSlotIndex][person].flow() == 1) {
                    if (selectedPerson >= 0) {
                        throw new IllegalStateException("同一角色被分配給多位成員。");
                    }
                    selectedPerson = person;
                }
            }

            if (selectedPerson < 0) {
                throw new IllegalStateException("角色未分配完成。");
            }
            assignments.add(new Assignment(
                    roleSlot.moduleIndex,
                    roleSlot.roleIndex,
                    selectedPerson
            ));
            countByPerson[selectedPerson]++;
        }

        for (int count : countByPerson) {
            if (count < minimumPerPerson || count > maximumPerPerson) {
                throw new IllegalStateException("每人總件數未落在 floor～ceil 範圍。");
            }
        }

        return new JointAssignmentResult(
                assignments,
                totalRoleCount
        );
    }

    private static long weightedDifficultyCost(
            Config config,
            int moduleIndex,
            int personIndex,
            int roleIndex
    ) {
        ModuleData module = config.modules.get(moduleIndex);

        /*
         * contribution 是這份角色真正代表的責任量：
         *   （基礎權重 × 加級倍率 × 全員平均難度）× 角色責任比例
         *
         * 同一模組中，主責的 responsibilityShare 通常比備援高，
         * 所以把主責交給不熟悉的人會產生更高的成本。
         */
        double contribution = module.difficultyAdjustedWeight()
                * module.rule.responsibilityShare(roleIndex);

        // 將名次 1～模組數映射到 0～1，名次越前面（越簡單）成本越低。
        double normalizedRank = normalizedRank(
                module.ranks[personIndex],
                config.modules.size()
        );
        // 最小成本流使用 long；乘一百萬保留 double 成本的小數精度。
        double rawCost = contribution * normalizedRank * 1_000_000.0;

        if (!Double.isFinite(rawCost) || rawCost < 0.0
                || rawCost > Long.MAX_VALUE / 1024.0) {
            throw new IllegalArgumentException("權重過大，無法安全建立成本矩陣。");
        }
        return Math.round(rawCost);
    }

    private static long safeAddCost(long left, long right) {
        // 成本加總前先檢查，避免 long 溢位後變成負數而破壞最佳化結果。
        if (right > 0L && left > Long.MAX_VALUE / 8L - right) {
            throw new IllegalArgumentException("成本數值過大。");
        }
        return left + right;
    }

    /**
     * 在「每人總件數已平衡」的前提下，同時平衡主責數與備援數。
     *
     * 一次主責／備援互換的效果：
     * - 主責原持有人：主責 -1、備援 +1、總件數不變。
     * - 備援原持有人：主責 +1、備援 -1、總件數不變。
     *
     * 所以只要把每人的主責數調到相容目標，備援數也會同步達標。
     */
    private static int balancePrimaryAndBackupCounts(
            Config config,
            List<Assignment> assignments
    ) {
        int personCount = config.people.size();
        int[] totalCounts = countAssignmentsByPerson(
                assignments,
                personCount,
                AssignmentCategory.ALL
        );
        int[] primaryCounts = countAssignmentsByPerson(
                assignments,
                personCount,
                AssignmentCategory.PRIMARY
        );

        int totalPrimaryCount = sum(primaryCounts);
        int totalBackupCount = assignments.size() - totalPrimaryCount;
        int primaryMinimum = totalPrimaryCount / personCount;
        int primaryMaximum = ceilingDivide(totalPrimaryCount, personCount);
        int backupMinimum = totalBackupCount / personCount;
        int backupMaximum = ceilingDivide(totalBackupCount, personCount);

        /*
         * targetPrimaryCounts 同時滿足：
         *   primaryMinimum <= targetPrimary <= primaryMaximum
         *   backupMinimum  <= totalCount - targetPrimary <= backupMaximum
         * 且全體 targetPrimary 合計仍等於全部主責數。
         */
        int[] targetPrimaryCounts = chooseCompatiblePrimaryTargets(
                totalCounts,
                primaryCounts,
                totalPrimaryCount,
                primaryMinimum,
                primaryMaximum,
                backupMinimum,
                backupMaximum
        );

        int swapCount = 0;
        while (!Arrays.equals(primaryCounts, targetPrimaryCounts)) {
            boolean progressed = false;

            for (int sourcePerson = 0; sourcePerson < personCount; sourcePerson++) {
                if (primaryCounts[sourcePerson]
                        <= targetPrimaryCounts[sourcePerson]) {
                    continue;
                }

                boolean[] visited = new boolean[personCount];
                int pathSwapCount = transferOnePrimaryAlongPath(
                        config,
                        assignments,
                        primaryCounts,
                        targetPrimaryCounts,
                        sourcePerson,
                        visited
                );

                if (pathSwapCount > 0) {
                    swapCount += pathSwapCount;
                    progressed = true;
                    break;
                }
            }

            if (!progressed) {
                throw new IllegalStateException(
                        "無法在維持同模組人員不重複的條件下，"
                                + "把主責與備援件數調整到最大落差 1。"
                );
            }
        }

        return swapCount;
    }

    /**
     * 動態規劃選出每人的主責目標數，並盡量貼近目前分配以減少交換次數。
     */
    private static int[] chooseCompatiblePrimaryTargets(
            int[] totalCounts,
            int[] currentPrimaryCounts,
            int requiredPrimaryTotal,
            int primaryMinimum,
            int primaryMaximum,
            int backupMinimum,
            int backupMaximum
    ) {
        int personCount = totalCounts.length;
        int infinity = Integer.MAX_VALUE / 4;
        int[][] minimumChange = new int[personCount + 1][requiredPrimaryTotal + 1];
        int[][] selectedPrimary = new int[personCount + 1][requiredPrimaryTotal + 1];

        for (int person = 0; person <= personCount; person++) {
            Arrays.fill(minimumChange[person], infinity);
            Arrays.fill(selectedPrimary[person], -1);
        }
        minimumChange[0][0] = 0;

        for (int person = 0; person < personCount; person++) {
            int smallestAllowedPrimary = Math.max(
                    primaryMinimum,
                    totalCounts[person] - backupMaximum
            );
            int largestAllowedPrimary = Math.min(
                    primaryMaximum,
                    totalCounts[person] - backupMinimum
            );

            if (smallestAllowedPrimary > largestAllowedPrimary) {
                throw new IllegalStateException(
                        "總件數與主／備件數的 floor～ceil 限制彼此不相容。"
                );
            }

            for (int used = 0; used <= requiredPrimaryTotal; used++) {
                if (minimumChange[person][used] == infinity) {
                    continue;
                }
                for (int target = smallestAllowedPrimary;
                     target <= largestAllowedPrimary;
                     target++) {
                    int nextUsed = used + target;
                    if (nextUsed > requiredPrimaryTotal) {
                        continue;
                    }

                    int candidateChange = minimumChange[person][used]
                            + Math.abs(currentPrimaryCounts[person] - target);
                    if (candidateChange < minimumChange[person + 1][nextUsed]) {
                        minimumChange[person + 1][nextUsed] = candidateChange;
                        selectedPrimary[person + 1][nextUsed] = target;
                    }
                }
            }
        }

        if (minimumChange[personCount][requiredPrimaryTotal] == infinity) {
            throw new IllegalStateException(
                    "找不到能同時讓總件數、主責數、備援數落差不超過 1 的目標。"
            );
        }

        int[] targets = new int[personCount];
        int remaining = requiredPrimaryTotal;
        for (int person = personCount; person > 0; person--) {
            int target = selectedPrimary[person][remaining];
            if (target < 0) {
                throw new IllegalStateException("主責目標回溯失敗。");
            }
            targets[person - 1] = target;
            remaining -= target;
        }
        return targets;
    }

    /**
     * 將一個「主責名額」從 sourcePerson 沿人員路徑轉移給尚未達標的人。
     * 中繼人員會先收到一份主責、再交出一份主責，所以最終件數不變。
     * 深度最多等於人數，避免循環。
     */
    private static int transferOnePrimaryAlongPath(
            Config config,
            List<Assignment> assignments,
            int[] primaryCounts,
            int[] targetPrimaryCounts,
            int sourcePerson,
            boolean[] visited
    ) {
        visited[sourcePerson] = true;
        List<PrimaryTransferCandidate> candidates =
                new ArrayList<PrimaryTransferCandidate>();

        for (Assignment primary : assignments) {
            if (primary.roleIndex != 0 || primary.personIndex != sourcePerson) {
                continue;
            }

            for (Assignment backup : assignments) {
                int destinationPerson = backup.personIndex;
                if (backup.roleIndex == 0
                        || destinationPerson == sourcePerson
                        || visited[destinationPerson]
                        || !canSwap(assignments, primary, backup)) {
                    continue;
                }

                double preferenceDelta = preferenceSwapDelta(
                        config,
                        primary,
                        backup
                );
                candidates.add(new PrimaryTransferCandidate(
                        primary,
                        backup,
                        destinationPerson,
                        primaryCounts[destinationPerson]
                                < targetPrimaryCounts[destinationPerson],
                        preferenceDelta
                ));
            }
        }

        // 優先直接補足缺額；同類候選再選擇對難度適配傷害最小的交換。
        Collections.sort(candidates, new Comparator<PrimaryTransferCandidate>() {
            @Override
            public int compare(
                    PrimaryTransferCandidate left,
                    PrimaryTransferCandidate right
            ) {
                if (left.destinationNeedsPrimary != right.destinationNeedsPrimary) {
                    return left.destinationNeedsPrimary ? -1 : 1;
                }
                return Double.compare(left.preferenceDelta, right.preferenceDelta);
            }
        });

        for (PrimaryTransferCandidate candidate : candidates) {
            Assignment primary = candidate.primary;
            Assignment backup = candidate.backup;
            int destinationPerson = candidate.destinationPerson;

            swapAssignedPeople(primary, backup);
            primaryCounts[sourcePerson]--;
            primaryCounts[destinationPerson]++;

            if (primaryCounts[destinationPerson]
                    <= targetPrimaryCounts[destinationPerson]) {
                return 1;
            }

            int downstreamSwapCount = transferOnePrimaryAlongPath(
                    config,
                    assignments,
                    primaryCounts,
                    targetPrimaryCounts,
                    destinationPerson,
                    visited
            );
            if (downstreamSwapCount > 0) {
                return downstreamSwapCount + 1;
            }

            // 此路徑走不通，完整還原人選與計數後再試下一條。
            swapAssignedPeople(primary, backup);
            primaryCounts[sourcePerson]++;
            primaryCounts[destinationPerson]--;
        }

        visited[sourcePerson] = false;
        return 0;
    }

    private static double preferenceSwapDelta(
            Config config,
            Assignment first,
            Assignment second
    ) {
        int firstPerson = first.personIndex;
        int secondPerson = second.personIndex;
        double before = preferencePenalty(config, first, firstPerson)
                + preferencePenalty(config, second, secondPerson);
        double after = preferencePenalty(config, first, secondPerson)
                + preferencePenalty(config, second, firstPerson);
        return after - before;
    }

    private static void swapAssignedPeople(Assignment first, Assignment second) {
        int temporary = first.personIndex;
        first.personIndex = second.personIndex;
        second.personIndex = temporary;
    }

    private static int[] countAssignmentsByPerson(
            List<Assignment> assignments,
            int personCount,
            AssignmentCategory category
    ) {
        int[] counts = new int[personCount];
        for (Assignment assignment : assignments) {
            boolean included = category == AssignmentCategory.ALL
                    || (category == AssignmentCategory.PRIMARY
                    && assignment.roleIndex == 0)
                    || (category == AssignmentCategory.BACKUP
                    && assignment.roleIndex > 0);
            if (included) {
                counts[assignment.personIndex]++;
            }
        }
        return counts;
    }

    private static int ceilingDivide(int numerator, int denominator) {
        return (numerator + denominator - 1) / denominator;
    }

    private static int sum(int[] values) {
        int total = 0;
        for (int value : values) {
            total += value;
        }
        return total;
    }

    /**
     * 交換兩份同類角色的成員，因此每人的總件數、主責數、備援數都不變。
     * 主責只能與主責交換；備援1與備援2可彼此交換。
     * 交換前也會確認不會讓同一個人同時出現在同一模組兩次。
     */
    private static int improveWeightedBalance(
            Config config,
            List<Assignment> assignments
    ) {
        // 0 表示停用第三階段的負載改善；三項件數硬限制仍會照常執行。
        if (config.policy.loadBalanceStrength <= 0.0
                || config.policy.maxSwapIterations == 0) {
            return 0;
        }

        // loads[p] 是第 p 人目前承擔的「難度調整後責任量」，不是單純件數。
        double[] loads = calculateLoadUnits(config, assignments);
        double averageLoad = totalRewardWeight(config) / config.people.size();
        int applied = 0;
        final double epsilon = 1.0e-12;

        // 每一輪找出能讓綜合目標下降最多的一組交換，直到沒有改善或達上限。
        while (applied < config.policy.maxSwapIterations) {
            double bestDelta = -epsilon;
            int bestLeft = -1;
            int bestRight = -1;

            for (int left = 0; left < assignments.size(); left++) {
                Assignment first = assignments.get(left);

                for (int right = left + 1; right < assignments.size(); right++) {
                    Assignment second = assignments.get(right);

                    boolean firstIsPrimary = first.roleIndex == 0;
                    boolean secondIsPrimary = second.roleIndex == 0;

                    if (first.personIndex == second.personIndex
                            || firstIsPrimary != secondIsPrimary
                            || !canSwap(assignments, first, second)) {
                        continue;
                    }

                    // delta < 0 表示交換後更好；數值越小，改善越大。
                    double delta = swapObjectiveDelta(
                            config,
                            first,
                            second,
                            loads,
                            averageLoad
                    );

                    if (delta < bestDelta) {
                        bestDelta = delta;
                        bestLeft = left;
                        bestRight = right;
                    }
                }
            }

            if (bestLeft < 0) {
                break;
            }

            // 只互換人選，不更換 moduleIndex／roleIndex，因此角色仍完整且總件數不變。
            Assignment first = assignments.get(bestLeft);
            Assignment second = assignments.get(bestRight);
            int firstPerson = first.personIndex;
            int secondPerson = second.personIndex;
            double firstUnits = responsibilityUnits(config, first);
            double secondUnits = responsibilityUnits(config, second);

            loads[firstPerson] = loads[firstPerson] - firstUnits + secondUnits;
            loads[secondPerson] = loads[secondPerson] - secondUnits + firstUnits;
            swapAssignedPeople(first, second);
            applied++;
        }

        return applied;
    }

    private static boolean canSwap(
            List<Assignment> assignments,
            Assignment first,
            Assignment second
    ) {
        // 若交換後某人會在同一模組出現第二次（例如同時主責與備援），就禁止交換。
        for (Assignment other : assignments) {
            if (other == first || other == second) {
                continue;
            }
            if (other.moduleIndex == first.moduleIndex
                    && other.personIndex == second.personIndex) {
                return false;
            }
            if (other.moduleIndex == second.moduleIndex
                    && other.personIndex == first.personIndex) {
                return false;
            }
        }
        return true;
    }

    private static double swapObjectiveDelta(
            Config config,
            Assignment first,
            Assignment second,
            double[] loads,
            double averageLoad
    ) {
        int firstPerson = first.personIndex;
        int secondPerson = second.personIndex;
        double firstUnits = responsibilityUnits(config, first);
        double secondUnits = responsibilityUnits(config, second);

        // 第一部分：交換前後，人選對模組的主觀難度適配成本差。
        double preferenceBefore = preferencePenalty(config, first, firstPerson)
                + preferencePenalty(config, second, secondPerson);
        double preferenceAfter = preferencePenalty(config, first, secondPerson)
                + preferencePenalty(config, second, firstPerson);

        double firstLoadAfter = loads[firstPerson] - firstUnits + secondUnits;
        double secondLoadAfter = loads[secondPerson] - secondUnits + firstUnits;

        // 第二部分：交換前後，兩人的難度加權負載偏離平均值多少。
        double balanceBefore = square(loads[firstPerson] - averageLoad)
                + square(loads[secondPerson] - averageLoad);
        double balanceAfter = square(firstLoadAfter - averageLoad)
                + square(secondLoadAfter - averageLoad);

        // loadBalanceStrength 越大，負載差距在綜合目標中的影響越強。
        double balanceDelta = config.policy.loadBalanceStrength
                * (balanceAfter - balanceBefore)
                / averageLoad;

        return (preferenceAfter - preferenceBefore) + balanceDelta;
    }

    private static Result calculateResult(
            Config config,
            List<Assignment> assignments,
            int totalRoleCount,
            int roleBalanceSwapCount,
            int loadBalanceSwapCount,
            double objectiveBefore,
            double objectiveAfter
    ) {
        // 固定輸出順序：先 Biz1～Biz19，再依主責、備援1、備援2排列。
        Collections.sort(assignments, new Comparator<Assignment>() {
            @Override
            public int compare(Assignment left, Assignment right) {
                int byModule = Integer.compare(left.moduleIndex, right.moduleIndex);
                if (byModule != 0) {
                    return byModule;
                }
                return Integer.compare(left.roleIndex, right.roleIndex);
            }
        });

        int personCount = config.people.size();
        int moduleCount = config.modules.size();
        // denominator 是所有模組「共同難度調整後權重」的合計，作為百分比分母。
        double denominator = totalRewardWeight(config);

        // 以下陣列都以 personIndex 當索引，長度等於實際人數。
        double[] loadUnits = new double[personCount];
        double[] workloadShare = new double[personCount];

        // roleCountByPerson[p][0/1/2] 分別是第 p 人的主責／備援1／備援2件數。
        int[][] roleCountByPerson =
                new int[personCount][config.maximumRoleCount];

        // totalAssignmentCountByPerson[p] 是主責與所有備援加總後的總件數。
        int[] totalAssignmentCountByPerson = new int[personCount];

        // 兩個矩陣只用於驗證同模組人員與角色沒有重複。
        boolean[][] assigned = new boolean[moduleCount][personCount];
        boolean[][] roleSeen = new boolean[moduleCount][config.maximumRoleCount];
        int[] assignmentCountByModule = new int[moduleCount];
        double weightedDifficultyPenalty = 0.0;

        for (Assignment assignment : assignments) {
            ModuleData module = config.modules.get(assignment.moduleIndex);
            ModuleRule rule = module.rule;

            if (assigned[assignment.moduleIndex][assignment.personIndex]) {
                throw new IllegalStateException("同模組出現重複人員。");
            }
            if (roleSeen[assignment.moduleIndex][assignment.roleIndex]) {
                throw new IllegalStateException("同模組出現重複角色。");
            }
            assigned[assignment.moduleIndex][assignment.personIndex] = true;
            roleSeen[assignment.moduleIndex][assignment.roleIndex] = true;
            assignmentCountByModule[assignment.moduleIndex]++;

            // 保存輸出欄位，避免列印時重複計算。
            assignment.difficultyRank = module.ranks[assignment.personIndex];

            // 模組占全部獎懲池的比例；同模組所有角色看到的 moduleShare 相同。
            assignment.moduleShare = module.difficultyAdjustedWeight() / denominator;

            // 主責／備援在該模組內的責任比例，例如 0.60／0.25／0.15。
            assignment.responsibilityShare = rule.responsibilityShare(
                    assignment.roleIndex
            );

            // 此人最終的獎懲比例 = 模組占比 × 角色責任比例。
            assignment.personalShare = assignment.moduleShare
                    * assignment.responsibilityShare;

            // loadUnits 保留尚未除以全體分母的原始責任量，方便比較與算標準差。
            assignment.loadUnits = module.difficultyAdjustedWeight()
                    * assignment.responsibilityShare;

            loadUnits[assignment.personIndex] += assignment.loadUnits;
            workloadShare[assignment.personIndex] += assignment.personalShare;
            roleCountByPerson[assignment.personIndex][assignment.roleIndex]++;
            totalAssignmentCountByPerson[assignment.personIndex]++;
            weightedDifficultyPenalty += preferencePenalty(
                    config,
                    assignment,
                    assignment.personIndex
            );
        }

        for (int module = 0; module < moduleCount; module++) {
            int expected = config.modules.get(module).rule.roleCount();
            if (assignmentCountByModule[module] != expected) {
                throw new IllegalStateException(
                        "模組「" + config.modules.get(module).businessModule.getDisplayName()
                                + "」預期 " + expected + " 位人員，實際為 "
                                + assignmentCountByModule[module] + "。"
                );
            }
            for (int role = 0; role < expected; role++) {
                if (!roleSeen[module][role]) {
                    throw new IllegalStateException("模組缺少角色：" + roleLabel(role));
                }
            }
        }

        int[] primaryCountByPerson = new int[personCount];
        int[] backupCountByPerson = new int[personCount];
        for (int person = 0; person < personCount; person++) {
            primaryCountByPerson[person] = roleCountByPerson[person][0];
            for (int role = 1; role < config.maximumRoleCount; role++) {
                backupCountByPerson[person] += roleCountByPerson[person][role];
            }
        }

        /*
         * 三項都是硬性驗證：每個人都只能落在各自的 floor～ceil 範圍。
         * CountBalance.create() 也會驗證合計、實際最大落差與理論範圍。
         */
        CountBalance totalCountBalance = CountBalance.create(
                "總件數",
                totalRoleCount,
                totalAssignmentCountByPerson
        );
        CountBalance primaryCountBalance = CountBalance.create(
                "主責數",
                moduleCount,
                primaryCountByPerson
        );
        CountBalance backupCountBalance = CountBalance.create(
                "備援數",
                totalRoleCount - moduleCount,
                backupCountByPerson
        );

        // 全體共同休假容錯取所有模組備援數的最小值。
        // 例如大多數模組 1 備援、少數 2 備援，整體保證仍是任意 1 人缺席。
        int guaranteedAbsenceTolerance = Integer.MAX_VALUE;
        for (ModuleData module : config.modules) {
            guaranteedAbsenceTolerance = Math.min(
                    guaranteedAbsenceTolerance,
                    module.rule.backupCount()
            );
        }

        if (guaranteedAbsenceTolerance < 1) {
            throw new IllegalStateException("所有模組至少需要一位備援才能通過休假覆蓋檢查。");
        }

        // 標準差僅用來觀察難度加權負載分散程度，不是硬性限制。
        double averageLoad = denominator / personCount;
        double variance = 0.0;
        double loadUnitTotal = 0.0;
        double workloadShareTotal = 0.0;
        for (double load : loadUnits) {
            variance += square(load - averageLoad);
            loadUnitTotal += load;
        }
        for (double share : workloadShare) {
            workloadShareTotal += share;
        }
        variance /= personCount;

        if (Math.abs(loadUnitTotal - denominator) > 1.0e-9
                || Math.abs(workloadShareTotal - 1.0) > 1.0e-9) {
            throw new IllegalStateException("責任比例合計檢查失敗。");
        }
        if (objectiveAfter > objectiveBefore + 1.0e-9) {
            throw new IllegalStateException("負載交換不應使綜合目標值變差。");
        }

        return new Result(
                Collections.unmodifiableList(new ArrayList<Assignment>(assignments)),
                totalCountBalance,
                primaryCountBalance,
                backupCountBalance,
                denominator,
                loadUnits,
                workloadShare,
                roleCountByPerson,
                weightedDifficultyPenalty,
                Math.sqrt(variance),
                guaranteedAbsenceTolerance,
                roleBalanceSwapCount,
                loadBalanceSwapCount,
                objectiveBefore,
                objectiveAfter
        );
    }

    private static double objective(Config config, List<Assignment> assignments) {
        /*
         * 綜合目標 = 人選不適配成本
         *          + loadBalanceStrength × 負載平方差 / 平均負載
         *
         * 第一項越小，代表角色越常交給認為該模組簡單的人；
         * 第二項越小，代表高難度／高權重工作沒有過度集中。
         */
        double preference = 0.0;
        for (Assignment assignment : assignments) {
            preference += preferencePenalty(
                    config,
                    assignment,
                    assignment.personIndex
            );
        }

        double totalWeight = totalRewardWeight(config);
        double averageLoad = totalWeight / config.people.size();
        double[] loads = calculateLoadUnits(config, assignments);
        double squaredDeviation = 0.0;
        for (double load : loads) {
            squaredDeviation += square(load - averageLoad);
        }

        return preference + config.policy.loadBalanceStrength
                * squaredDeviation / averageLoad;
    }

    private static double preferencePenalty(
            Config config,
            Assignment assignment,
            int candidatePerson
    ) {
        // 某角色若改由 candidatePerson 承接時，產生的加權難度成本。
        ModuleData module = config.modules.get(assignment.moduleIndex);
        return responsibilityUnits(config, assignment)
                * normalizedRank(
                module.ranks[candidatePerson],
                config.modules.size()
        );
    }

    private static double normalizedRank(int rank, int moduleCount) {
        // rank=1 -> 0.0；rank=moduleCount -> 1.0。
        if (moduleCount <= 1) {
            return 0.0;
        }
        return (double) (rank - 1) / (moduleCount - 1);
    }

    private static double responsibilityUnits(Config config, Assignment assignment) {
        // 一份角色的原始責任量，不包含百分比正規化。
        ModuleData module = config.modules.get(assignment.moduleIndex);
        return module.difficultyAdjustedWeight()
                * module.rule.responsibilityShare(assignment.roleIndex);
    }

    private static double[] calculateLoadUnits(
            Config config,
            List<Assignment> assignments
    ) {
        // 將每份角色的責任量累加到其負責人。
        double[] loads = new double[config.people.size()];
        for (Assignment assignment : assignments) {
            loads[assignment.personIndex] += responsibilityUnits(config, assignment);
        }
        return loads;
    }

    private static double totalRewardWeight(Config config) {
        // 所有模組共同難度調整後權重的合計；也是獎懲占比的分母。
        double total = 0.0;
        for (ModuleData module : config.modules) {
            total += module.difficultyAdjustedWeight();
        }
        return total;
    }

    private static void printResult(Config config, Result result) {
        System.out.println("===== 模組責任與休假覆蓋 =====");

        /*
         * 模組表欄位：
         * - 有效權重：baseWeight × gradeMultiplier，尚未納入大家的難度評價。
         * - 共同難度：所有成員對該模組名次的平均值，越大代表普遍越難。
         * - 獎懲權重：有效權重 × 共同難度。
         * - 獎懲占比：該模組獎懲權重 / 全部模組獎懲權重。
         * - 名稱後的 #N：該成員自己給此模組的難度名次。
         */
        List<String[]> moduleRows = new ArrayList<String[]>();
        for (int moduleIndex = 0; moduleIndex < config.modules.size(); moduleIndex++) {
            ModuleData module = config.modules.get(moduleIndex);
            Assignment primary = findAssignment(result.assignments, moduleIndex, 0);
            List<String> backups = new ArrayList<String>();

            for (int role = 1; role < module.rule.roleCount(); role++) {
                Assignment backup = findAssignment(result.assignments, moduleIndex, role);
                backups.add(personWithRank(config, backup));
            }

            moduleRows.add(new String[] {
                    module.businessModule.getDisplayName(),
                    decimal(module.rule.baseWeight),
                    decimal(module.rule.gradeMultiplier),
                    decimal(module.rule.effectiveWeight()),
                    decimal(module.consensusDifficulty),
                    decimal(module.difficultyAdjustedWeight()),
                    percent(module.difficultyAdjustedWeight()
                            / result.totalRewardWeight),
                    personWithRank(config, primary),
                    join(backups, "、"),
                    module.rule.backupCount() + " 人"
            });
        }

        printTable(
                new String[] {
                        "業務模組",
                        "基礎權重",
                        "加級倍率",
                        "有效權重",
                        "共同難度",
                        "獎懲權重",
                        "獎懲佔比",
                        "主責（難度序）",
                        "備援（難度序）",
                        "可同時缺席"
                },
                new Alignment[] {
                        Alignment.LEFT,
                        Alignment.RIGHT,
                        Alignment.RIGHT,
                        Alignment.RIGHT,
                        Alignment.RIGHT,
                        Alignment.RIGHT,
                        Alignment.RIGHT,
                        Alignment.LEFT,
                        Alignment.LEFT,
                        Alignment.RIGHT
                },
                moduleRows
        );

        System.out.println();
        System.out.println("===== 每人總件數／主責／備援平衡 =====");

        // 三列都是獨立硬限制；不代表每人的難度加權獎懲占比也必須相同。
        List<String[]> roleRows = new ArrayList<String[]>();
        addCountBalanceRow(roleRows, config, result.totalCountBalance);
        addCountBalanceRow(roleRows, config, result.primaryCountBalance);
        addCountBalanceRow(roleRows, config, result.backupCountBalance);
        printTable(
                new String[] {"平衡單位", "該類總數", "每人件數範圍", "實際件數"},
                new Alignment[] {
                        Alignment.LEFT,
                        Alignment.RIGHT,
                        Alignment.RIGHT,
                        Alignment.LEFT
                },
                roleRows
        );

        System.out.println();
        System.out.println("===== 個人加權工作量／獎勵佔比 =====");

        /*
         * 個人表欄位：
         * - 總件數：主責數 + 全部備援數，最大與最小最多差 1。
         * - 負載單位：所有角色難度調整後責任量的原始加總。
         * - 個人占比：負載單位 / 全部獎懲權重，所有人合計必須為 100%。
         */
        List<String[]> personRows = new ArrayList<String[]>();
        double totalShare = 0.0;

        for (int person = 0; person < config.people.size(); person++) {
            int primaryCount = result.primaryCountBalance.countByPerson[person];
            int backupCount = result.backupCountBalance.countByPerson[person];
            totalShare += result.workloadShare[person];

            personRows.add(new String[] {
                    config.people.get(person).getDisplayName(),
                    String.valueOf(primaryCount),
                    String.valueOf(backupCount),
                    String.valueOf(result.totalCountBalance.countByPerson[person]),
                    decimal(result.loadUnits[person]),
                    percent(result.workloadShare[person]),
                    assignedModuleNames(config, result, person, true),
                    assignedModuleNames(config, result, person, false)
            });
        }

        printTable(
                new String[] {
                        "成員",
                        "主責數",
                        "備援數",
                        "總件數",
                        "負載單位",
                        "個人佔比",
                        "主責模組",
                        "備援模組"
                },
                new Alignment[] {
                        Alignment.LEFT,
                        Alignment.RIGHT,
                        Alignment.RIGHT,
                        Alignment.RIGHT,
                        Alignment.RIGHT,
                        Alignment.RIGHT,
                        Alignment.LEFT,
                        Alignment.LEFT
                },
                personRows
        );

        System.out.println();
        System.out.println("===== 驗證摘要 =====");
        /*
         * 摘要指標：
         * - 負載標準差：越小代表難度加權責任越平均，但不要求等於 0。
         * - 加權難度懲罰：越小代表越常由「覺得該模組簡單」的人承接。
         * - 硬平衡交換：為讓主責／備援也各自只差 1 而套用的交換。
         * - 負載改善交換：三項件數達標後，為改善加權負載而套用的交換。
         * - 綜合目標值：負載改善前 -> 改善後；後者不應大於前者。
         */
        System.out.println(
                "目前／允許人數：" + config.people.size() + "／"
                        + config.policy.minimumHeadcount + "～"
                        + (config.policy.maximumHeadcount == Integer.MAX_VALUE
                        ? "不限" : config.policy.maximumHeadcount)
        );
        System.out.println("難度調整後獎懲權重合計：" + decimal(result.totalRewardWeight));
        System.out.println("個人佔比合計：" + percent(totalShare));
        printCountBalanceSummary(result.totalCountBalance);
        printCountBalanceSummary(result.primaryCountBalance);
        printCountBalanceSummary(result.backupCountBalance);
        System.out.println(
                "所有模組共同保證可承受任意同時缺席："
                        + result.guaranteedAbsenceTolerance + " 人（PASS）"
        );
        System.out.println("負載標準差（單位）：" + decimal(result.loadStandardDeviation));
        System.out.println("加權難度懲罰：" + decimal(result.weightedDifficultyPenalty));
        System.out.println("主責／備援硬平衡交換次數：" + result.roleBalanceSwapCount);
        System.out.println("加權負載改善交換次數：" + result.loadBalanceSwapCount);
        System.out.println(
                "綜合目標值：" + decimal(result.objectiveBefore)
                        + " -> " + decimal(result.objectiveAfter)
        );
    }

    private static void addCountBalanceRow(
            List<String[]> rows,
            Config config,
            CountBalance balance
    ) {
        rows.add(new String[] {
                balance.label,
                String.valueOf(balance.totalCount),
                balance.minimumPerPerson + "～" + balance.maximumPerPerson,
                countsByPerson(config, balance.countByPerson)
        });
    }

    private static void printCountBalanceSummary(CountBalance balance) {
        System.out.println(
                "每人" + balance.label + "："
                        + balance.observedMinimum + "～"
                        + balance.observedMaximum
                        + "，最大落差 "
                        + (balance.observedMaximum - balance.observedMinimum)
                        + "（PASS）"
        );
    }

    private static Assignment findAssignment(
            List<Assignment> assignments,
            int moduleIndex,
            int roleIndex
    ) {
        for (Assignment assignment : assignments) {
            if (assignment.moduleIndex == moduleIndex
                    && assignment.roleIndex == roleIndex) {
                return assignment;
            }
        }
        throw new IllegalStateException("找不到模組角色指派。");
    }

    private static String personWithRank(Config config, Assignment assignment) {
        // 例如 "Hugo (#3)" 表示 Hugo 將該模組排為第 3 簡單。
        return config.people.get(assignment.personIndex).getDisplayName()
                + " (#" + assignment.difficultyRank + ")";
    }

    private static String countsByPerson(Config config, int[] counts) {
        List<String> values = new ArrayList<String>();
        for (int person = 0; person < counts.length; person++) {
            values.add(config.people.get(person).getDisplayName() + "=" + counts[person]);
        }
        return join(values, "、");
    }

    private static String assignedModuleNames(
            Config config,
            Result result,
            int person,
            boolean primary
    ) {
        List<String> names = new ArrayList<String>();
        for (Assignment assignment : result.assignments) {
            if (assignment.personIndex != person) {
                continue;
            }
            if (primary && assignment.roleIndex == 0) {
                names.add(config.modules.get(assignment.moduleIndex)
                        .businessModule.getDisplayName());
            } else if (!primary && assignment.roleIndex > 0) {
                names.add(
                        config.modules.get(assignment.moduleIndex)
                                .businessModule.getDisplayName()
                                + "(" + roleLabel(assignment.roleIndex) + ")"
                );
            }
        }
        return join(names, "、");
    }

    /**
     * 輸出支援中英文混排的等寬表格。
     * headers 是欄名、alignments 指定靠左／靠右、rows 是實際資料列。
     */
    private static void printTable(
            String[] headers,
            Alignment[] alignments,
            List<String[]> rows
    ) {
        if (headers.length == 0 || headers.length != alignments.length) {
            throw new IllegalArgumentException("表格標題與對齊設定數量不一致。");
        }

        int[] widths = new int[headers.length];
        for (int column = 0; column < headers.length; column++) {
            widths[column] = displayWidth(headers[column]);
        }

        for (String[] row : rows) {
            if (row.length != headers.length) {
                throw new IllegalArgumentException("表格資料欄位數量不一致。");
            }
            for (int column = 0; column < row.length; column++) {
                widths[column] = Math.max(widths[column], displayWidth(row[column]));
            }
        }

        printBorder('┌', '┬', '┐', widths);
        printTableRow(headers, alignments, widths);
        printBorder('├', '┼', '┤', widths);
        for (String[] row : rows) {
            printTableRow(row, alignments, widths);
        }
        printBorder('└', '┴', '┘', widths);
    }

    private static void printBorder(char left, char middle, char right, int[] widths) {
        StringBuilder line = new StringBuilder();
        line.append(left);
        for (int column = 0; column < widths.length; column++) {
            if (column > 0) {
                line.append(middle);
            }
            appendRepeated(line, '─', widths[column] + 2);
        }
        line.append(right);
        System.out.println(line.toString());
    }

    private static void printTableRow(
            String[] cells,
            Alignment[] alignments,
            int[] widths
    ) {
        StringBuilder line = new StringBuilder();
        line.append('│');

        for (int column = 0; column < cells.length; column++) {
            String cell = cells[column] == null ? "" : cells[column];
            int padding = widths[column] - displayWidth(cell);

            line.append(' ');
            if (alignments[column] == Alignment.RIGHT) {
                appendRepeated(line, ' ', padding);
            }
            line.append(cell);
            if (alignments[column] == Alignment.LEFT) {
                appendRepeated(line, ' ', padding);
            }
            line.append(' ').append('│');
        }
        System.out.println(line.toString());
    }

    private static int displayWidth(String value) {
        // Java 的 String.length() 無法反映終端顯示寬度；中文字通常占兩格。
        int width = 0;
        for (int offset = 0; offset < value.length();) {
            int codePoint = value.codePointAt(offset);
            offset += Character.charCount(codePoint);

            int type = Character.getType(codePoint);
            if (type == Character.NON_SPACING_MARK
                    || type == Character.ENCLOSING_MARK
                    || type == Character.COMBINING_SPACING_MARK) {
                continue;
            }
            width += isWideCodePoint(codePoint) ? 2 : 1;
        }
        return width;
    }

    private static boolean isWideCodePoint(int codePoint) {
        // 常見 CJK、全形字元及 emoji 的 Unicode 範圍，終端通常顯示為兩格寬。
        return codePoint >= 0x1100
                && (codePoint <= 0x115F
                || codePoint == 0x2329
                || codePoint == 0x232A
                || (codePoint >= 0x2E80 && codePoint <= 0xA4CF && codePoint != 0x303F)
                || (codePoint >= 0xAC00 && codePoint <= 0xD7A3)
                || (codePoint >= 0xF900 && codePoint <= 0xFAFF)
                || (codePoint >= 0xFE10 && codePoint <= 0xFE19)
                || (codePoint >= 0xFE30 && codePoint <= 0xFE6F)
                || (codePoint >= 0xFF00 && codePoint <= 0xFF60)
                || (codePoint >= 0xFFE0 && codePoint <= 0xFFE6)
                || (codePoint >= 0x1F300 && codePoint <= 0x1FAFF)
                || (codePoint >= 0x20000 && codePoint <= 0x3FFFD));
    }

    private static String join(List<String> values, String separator) {
        StringBuilder joined = new StringBuilder();
        for (int index = 0; index < values.size(); index++) {
            if (index > 0) {
                joined.append(separator);
            }
            joined.append(values.get(index));
        }
        return joined.toString();
    }

    private static void appendRepeated(StringBuilder target, char character, int count) {
        for (int index = 0; index < count; index++) {
            target.append(character);
        }
    }

    private static String roleLabel(int roleIndex) {
        return roleIndex == 0 ? "主責" : "備援" + roleIndex;
    }

    private static String percent(double value) {
        return String.format(Locale.ROOT, "%.2f%%", value * 100.0);
    }

    private static String decimal(double value) {
        return String.format(Locale.ROOT, "%.4f", value);
    }

    private static double square(double value) {
        return value * value;
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private enum Alignment {
        LEFT,
        RIGHT
    }

    /** 計數工具要統計全部角色、只統計主責，或只統計所有備援。 */
    private enum AssignmentCategory {
        ALL,
        PRIMARY,
        BACKUP
    }

    private static BusinessModule[] ranking(BusinessModule... modulesFromEasyToHard) {
        // 只為了讓 main 中的排序資料更容易閱讀；不改變陣列內容。
        return modulesFromEasyToHard;
    }

    private static Map<BusinessModule, ModuleRule> defaultModuleRules(
            ModuleRule defaultRule
    ) {
        // 先替全部 enum 模組填入同一預設規則，再由 main 對特定模組覆寫。
        Map<BusinessModule, ModuleRule> result =
                new LinkedHashMap<BusinessModule, ModuleRule>();
        for (BusinessModule module : BusinessModule.values()) {
            result.put(module, defaultRule);
        }
        return result;
    }

    private static void validate(Config config) {
        // 驗證屬於硬防線：資料錯誤時立即失敗，不讓演算法產生看似正常的錯誤結果。
        if (config == null) {
            throw new IllegalArgumentException("Config 不可為 null。");
        }
        if (config.people.size() < 2) {
            throw new IllegalArgumentException("至少需要 2 位成員。");
        }
        if (config.people.size() < config.policy.minimumHeadcount
                || config.people.size() > config.policy.maximumHeadcount) {
            throw new IllegalArgumentException(
                    "目前人數為 " + config.people.size()
                            + "，允許範圍為 " + config.policy.minimumHeadcount
                            + "～" + config.policy.maximumHeadcount + "。"
            );
        }
        if (config.modules.isEmpty()) {
            throw new IllegalArgumentException("至少需要 1 個業務模組。");
        }

        for (ModuleData module : config.modules) {
            if (module.ranks.length != config.people.size()) {
                throw new IllegalArgumentException(
                        "模組「" + module.businessModule.getDisplayName()
                                + "」的排名數量必須等於成員數。"
                );
            }
            if (module.rule.roleCount() > config.people.size()) {
                throw new IllegalArgumentException(
                        "模組「" + module.businessModule.getDisplayName()
                                + "」需要 " + module.rule.roleCount()
                                + " 位不同人員，但目前只有 " + config.people.size() + " 人。"
                );
            }
            for (int rank : module.ranks) {
                if (rank <= 0 || rank > config.modules.size()) {
                    throw new IllegalArgumentException("難度名次超出有效範圍。");
                }
            }
        }
    }

    /**
     * 模組規則：
     * - effectiveWeight = baseWeight * gradeMultiplier
     * - responsibilityShares[0] 是主責，其後依序是備援 1、備援 2
     * - 所有責任比例總和必須為 1.0
     */
    public static final class ModuleRule {
        /** 模組本身的業務重要度；1.0 代表一般基準。 */
        final double baseWeight;

        /** 額外加級倍率；例如 1.20 代表在基礎權重上再乘 1.2。 */
        final double gradeMultiplier;

        /**
         * 模組內各角色的責任比例：index 0=主責、1=備援1、2=備援2。
         * 長度只能是 2 或 3，且全部元素合計必須為 1.0。
         */
        final double[] responsibilityShares;

        private ModuleRule(
                double baseWeight,
                double gradeMultiplier,
                double[] responsibilityShares
        ) {
            if (!Double.isFinite(baseWeight) || baseWeight <= 0.0) {
                throw new IllegalArgumentException("基礎權重必須是有限正數。");
            }
            if (!Double.isFinite(gradeMultiplier) || gradeMultiplier <= 0.0) {
                throw new IllegalArgumentException("加級倍率必須是有限正數。");
            }
            if (responsibilityShares == null
                    || responsibilityShares.length < 2
                    || responsibilityShares.length > 3) {
                throw new IllegalArgumentException(
                        "每個模組必須有一位主責，並配置一至兩位備援。"
                );
            }

            double total = 0.0;
            for (double share : responsibilityShares) {
                if (!Double.isFinite(share) || share <= 0.0) {
                    throw new IllegalArgumentException("責任比例必須是有限正數。");
                }
                total += share;
            }
            if (Math.abs(total - 1.0) > 1.0e-9) {
                throw new IllegalArgumentException("同模組責任比例合計必須為 1.0。");
            }

            this.baseWeight = baseWeight;
            this.gradeMultiplier = gradeMultiplier;
            if (!Double.isFinite(baseWeight * gradeMultiplier)) {
                throw new IllegalArgumentException("有效權重超出可表示範圍。");
            }
            this.responsibilityShares = Arrays.copyOf(
                    responsibilityShares,
                    responsibilityShares.length
            );
        }

        public static ModuleRule withOneBackup(
                double baseWeight,
                double gradeMultiplier
        ) {
            // 一位備援的預設分攤：主責 70%、備援 30%。
            return of(baseWeight, gradeMultiplier, 0.70, 0.30);
        }

        public static ModuleRule withTwoBackups(
                double baseWeight,
                double gradeMultiplier
        ) {
            // 兩位備援的預設分攤：主責 60%、第一備援 25%、第二備援 15%。
            return of(baseWeight, gradeMultiplier, 0.60, 0.25, 0.15);
        }

        public static ModuleRule of(
                double baseWeight,
                double gradeMultiplier,
                double... responsibilityShares
        ) {
            // 自訂責任比例時使用，例如 of(1.0, 1.0, 0.55, 0.30, 0.15)。
            return new ModuleRule(
                    baseWeight,
                    gradeMultiplier,
                    responsibilityShares
            );
        }

        double effectiveWeight() {
            // 尚未納入共同難度的業務權重。
            return baseWeight * gradeMultiplier;
        }

        int roleCount() {
            // 包含主責，例如 [0.70, 0.30] 的角色數是 2。
            return responsibilityShares.length;
        }

        int backupCount() {
            // 扣除 index 0 的主責後，就是備援人數。
            return responsibilityShares.length - 1;
        }

        boolean hasRole(int roleIndex) {
            return roleIndex >= 0 && roleIndex < responsibilityShares.length;
        }

        double responsibilityShare(int roleIndex) {
            if (!hasRole(roleIndex)) {
                throw new IllegalArgumentException("角色索引超出範圍。");
            }
            return responsibilityShares[roleIndex];
        }
    }

    public static final class AllocationPolicy {
        /** 允許執行分配的最少人數；目前正式設定為 7。 */
        final int minimumHeadcount;

        /** 允許執行分配的最多人數；目前正式設定為 11。 */
        final int maximumHeadcount;

        /**
         * 難度適配與加權負載平均之間的軟性取捨：
         * 0=只在乎誰覺得模組簡單；數值越大越重視負載平均。
         * 它不影響總件數、主責數、備援數各自差最多 1 的硬限制。
         */
        final double loadBalanceStrength;

        /** 第三階段最多套用多少次負載改善交換，防止極端資料運算過久。 */
        final int maxSwapIterations;

        public AllocationPolicy(double loadBalanceStrength, int maxSwapIterations) {
            // 便利建構式：不特別限制上限時使用；正式設定建議使用四參數版本。
            this(2, Integer.MAX_VALUE, loadBalanceStrength, maxSwapIterations);
        }

        public AllocationPolicy(
                int minimumHeadcount,
                int maximumHeadcount,
                double loadBalanceStrength,
                int maxSwapIterations
        ) {
            if (minimumHeadcount < 2 || maximumHeadcount < minimumHeadcount) {
                throw new IllegalArgumentException("人數範圍設定無效。");
            }
            if (!Double.isFinite(loadBalanceStrength) || loadBalanceStrength < 0.0) {
                throw new IllegalArgumentException("負載平衡強度必須是有限非負數。");
            }
            if (maxSwapIterations < 0) {
                throw new IllegalArgumentException("交換次數上限不可小於 0。");
            }
            this.minimumHeadcount = minimumHeadcount;
            this.maximumHeadcount = maximumHeadcount;
            this.loadBalanceStrength = loadBalanceStrength;
            this.maxSwapIterations = maxSwapIterations;
        }
    }

    public static final class Config {
        /** 按固定順序保存的實際成員；所有 personIndex 都指向這份清單。 */
        final List<Person> people;

        /** 由原始個人排序轉置而成的「逐模組資料」。 */
        final List<ModuleData> modules;

        /** 人數範圍、負載平衡強度與交換上限。 */
        final AllocationPolicy policy;

        /** 所有模組中的最大角色數；一主兩備時為 3，用來建立二維陣列。 */
        final int maximumRoleCount;

        private Config(
                List<Person> people,
                List<ModuleData> modules,
                AllocationPolicy policy,
                int maximumRoleCount
        ) {
            this.people = people;
            this.modules = modules;
            this.policy = policy;
            this.maximumRoleCount = maximumRoleCount;
        }

        static Config fromRankings(
                List<Person> peopleInput,
                Map<Person, BusinessModule[]> rankingsByPerson,
                Map<BusinessModule, ModuleRule> rulesByModule,
                AllocationPolicy policy
        ) {
            /*
             * 將比較適合人工輸入的 person -> 模組排序，轉換成演算法需要的
             * module -> 每人名次，同時計算每個模組的全員平均難度。
             */
            if (peopleInput == null || peopleInput.isEmpty()) {
                throw new IllegalArgumentException("成員清單不可為空。");
            }
            if (rankingsByPerson == null) {
                throw new IllegalArgumentException("難易度排序資料不可為 null。");
            }
            if (rulesByModule == null) {
                throw new IllegalArgumentException("模組規則不可為 null。");
            }
            if (policy == null) {
                throw new IllegalArgumentException("分配政策不可為 null。");
            }

            // 複製輸入，避免呼叫端之後修改原 List 影響演算法內部狀態。
            List<Person> people = new ArrayList<Person>(peopleInput);
            Set<String> ids = new HashSet<String>();
            // 驗證每人的排序是 BusinessModule.values() 的完整排列。
            for (Person person : people) {
                if (person == null || !ids.add(person.getId())) {
                    throw new IllegalArgumentException("成員不可為 null，且 id 不可重複。");
                }
            }

            List<BusinessModule> businessModules =
                    Arrays.asList(BusinessModule.values());

            if (rankingsByPerson.size() != people.size()
                    || !rankingsByPerson.keySet().containsAll(people)) {
                throw new IllegalArgumentException("每一位成員都必須提供一份完整排序。");
            }
            if (rulesByModule.size() != businessModules.size()
                    || !rulesByModule.keySet().containsAll(businessModules)) {
                throw new IllegalArgumentException("每個業務模組都必須提供 ModuleRule。");
            }

            for (Person person : people) {
                BusinessModule[] personRanking = rankingsByPerson.get(person);
                if (personRanking == null
                        || personRanking.length != businessModules.size()) {
                    throw new IllegalArgumentException(
                            "成員「" + person.getDisplayName()
                                    + "」的排序必須包含全部業務模組。"
                    );
                }

                boolean[] found = new boolean[businessModules.size()];
                for (BusinessModule module : personRanking) {
                    if (module == null) {
                        throw new IllegalArgumentException(
                                "成員「" + person.getDisplayName() + "」的排序不可包含 null。"
                        );
                    }
                    if (found[module.ordinal()]) {
                        throw new IllegalArgumentException(
                                "成員「" + person.getDisplayName() + "」重複排列模組「"
                                        + module.getDisplayName() + "」。"
                        );
                    }
                    found[module.ordinal()] = true;
                }
            }

            List<ModuleData> modules = new ArrayList<ModuleData>();
            int maximumRoleCount = 0;

            // 將資料由「每人一列」轉置為「每模組一列」。
            for (BusinessModule businessModule : businessModules) {
                ModuleRule rule = rulesByModule.get(businessModule);
                if (rule == null) {
                    throw new IllegalArgumentException(
                            "模組「" + businessModule.getDisplayName() + "」缺少規則。"
                    );
                }

                int[] ranks = new int[people.size()];
                for (int personIndex = 0; personIndex < people.size(); personIndex++) {
                    BusinessModule[] ranking = rankingsByPerson.get(people.get(personIndex));
                    int rank = -1;
                    for (int index = 0; index < ranking.length; index++) {
                        if (ranking[index] == businessModule) {
                            rank = index + 1;
                            break;
                        }
                    }
                    if (rank < 1) {
                        throw new IllegalStateException("完整性檢查失敗：" + businessModule);
                    }
                    ranks[personIndex] = rank;
                }

                modules.add(new ModuleData(businessModule, ranks, rule));
                maximumRoleCount = Math.max(maximumRoleCount, rule.roleCount());
            }

            return new Config(
                    Collections.unmodifiableList(people),
                    Collections.unmodifiableList(modules),
                    policy,
                    maximumRoleCount
            );
        }
    }

    private static final class ModuleData {
        /** 對應的業務模組。 */
        final BusinessModule businessModule;

        /** ranks[p] 是第 p 位成員對此模組的難度名次，1 最簡單、19 最難。 */
        final int[] ranks;

        /** 此模組的基礎權重、倍率及主備責任比例設定。 */
        final ModuleRule rule;

        /** 所有成員 ranks 的算術平均；越大代表大家普遍認為越難。 */
        final double consensusDifficulty;

        ModuleData(BusinessModule businessModule, int[] ranks, ModuleRule rule) {
            this.businessModule = businessModule;
            this.ranks = Arrays.copyOf(ranks, ranks.length);
            this.rule = rule;
            long totalRank = 0L;
            for (int rank : ranks) {
                totalRank += rank;
            }
            this.consensusDifficulty = (double) totalRank / ranks.length;
        }

        double difficultyAdjustedWeight() {
            // 最終模組獎懲權重 = 基礎權重 × 加級倍率 × 全員平均難度。
            return rule.effectiveWeight() * consensusDifficulty;
        }
    }

    /**
     * 一種件數維度的平衡摘要，例如「總件數」、「主責數」或「備援數」。
     * 每種維度都獨立使用 floor(該類總數 / 人數)～ceil(...) 當硬限制。
     */
    public static final class CountBalance {
        /** 顯示與錯誤訊息使用的名稱。 */
        final String label;

        /** 此維度需要分配的總件數。 */
        final int totalCount;

        /** 理論每人最少件數 floor(totalCount / 人數)。 */
        final int minimumPerPerson;

        /** 理論每人最多件數 ceil(totalCount / 人數)。 */
        final int maximumPerPerson;

        /** 最終結果中實際觀察到的最少件數。 */
        final int observedMinimum;

        /** 最終結果中實際觀察到的最多件數。 */
        final int observedMaximum;

        /** countByPerson[p] 是第 p 人在此維度的件數。 */
        final int[] countByPerson;

        private CountBalance(
                String label,
                int totalCount,
                int minimumPerPerson,
                int maximumPerPerson,
                int observedMinimum,
                int observedMaximum,
                int[] countByPerson
        ) {
            this.label = label;
            this.totalCount = totalCount;
            this.minimumPerPerson = minimumPerPerson;
            this.maximumPerPerson = maximumPerPerson;
            this.observedMinimum = observedMinimum;
            this.observedMaximum = observedMaximum;
            this.countByPerson = Arrays.copyOf(countByPerson, countByPerson.length);
        }

        static CountBalance create(
                String label,
                int expectedTotal,
                int[] countByPerson
        ) {
            if (countByPerson == null || countByPerson.length == 0) {
                throw new IllegalArgumentException(label + "的人員計數不可為空。");
            }

            int minimum = expectedTotal / countByPerson.length;
            int maximum = ceilingDivide(expectedTotal, countByPerson.length);
            int observedMinimum = Integer.MAX_VALUE;
            int observedMaximum = Integer.MIN_VALUE;
            int actualTotal = 0;

            for (int count : countByPerson) {
                if (count < minimum || count > maximum) {
                    throw new IllegalStateException(
                            label + "未落在 " + minimum + "～" + maximum
                                    + " 範圍，實際出現 " + count + "。"
                    );
                }
                actualTotal += count;
                observedMinimum = Math.min(observedMinimum, count);
                observedMaximum = Math.max(observedMaximum, count);
            }

            if (actualTotal != expectedTotal) {
                throw new IllegalStateException(
                        label + "合計錯誤：預期 " + expectedTotal
                                + "，實際 " + actualTotal + "。"
                );
            }
            if (observedMaximum - observedMinimum > 1) {
                throw new IllegalStateException(label + "最大與最小不可相差超過 1。");
            }

            return new CountBalance(
                    label,
                    expectedTotal,
                    minimum,
                    maximum,
                    observedMinimum,
                    observedMaximum,
                    countByPerson
            );
        }
    }

    /**
     * solve() 的完整輸出。欄位保留原始數值，printResult() 再負責轉成表格。
     */
    public static final class Result {
        /** 每一份「模組＋角色＋人選」的明細。 */
        final List<Assignment> assignments;

        /** 主責＋備援合計的 floor～ceil 平衡結果。 */
        final CountBalance totalCountBalance;

        /** 只計算主責的 floor～ceil 平衡結果。 */
        final CountBalance primaryCountBalance;

        /** 合併備援1、備援2後的 floor～ceil 平衡結果。 */
        final CountBalance backupCountBalance;

        /** 所有模組 difficultyAdjustedWeight() 的合計，也是獎懲占比分母。 */
        final double totalRewardWeight;

        /** loadUnits[p] 是第 p 人未正規化的難度加權責任量。 */
        final double[] loadUnits;

        /** workloadShare[p] 是第 p 人最終獎懲占比；全部成員合計為 1.0。 */
        final double[] workloadShare;

        /** roleCountByPerson[p][r] 是第 p 人擔任角色 r 的件數。 */
        final int[][] roleCountByPerson;

        /** 最終人選的加權難度不適配成本；越小代表人選越熟悉其模組。 */
        final double weightedDifficultyPenalty;

        /** 個人 loadUnits 的母體標準差；越小代表難度加權負載越接近。 */
        final double loadStandardDeviation;

        /** 所有模組共同保證可承受的同時缺席人數，取最少備援數。 */
        final int guaranteedAbsenceTolerance;

        /** 為滿足主責／備援硬性件數平衡而套用的交換次數。 */
        final int roleBalanceSwapCount;

        /** 件數全部達標後，為改善加權負載而套用的交換次數。 */
        final int loadBalanceSwapCount;

        /** 負載改善交換前的綜合目標值。 */
        final double objectiveBefore;

        /** 負載改善交換後的綜合目標值；應小於或等於 objectiveBefore。 */
        final double objectiveAfter;

        Result(
                List<Assignment> assignments,
                CountBalance totalCountBalance,
                CountBalance primaryCountBalance,
                CountBalance backupCountBalance,
                double totalRewardWeight,
                double[] loadUnits,
                double[] workloadShare,
                int[][] roleCountByPerson,
                double weightedDifficultyPenalty,
                double loadStandardDeviation,
                int guaranteedAbsenceTolerance,
                int roleBalanceSwapCount,
                int loadBalanceSwapCount,
                double objectiveBefore,
                double objectiveAfter
        ) {
            this.assignments = assignments;
            this.totalCountBalance = totalCountBalance;
            this.primaryCountBalance = primaryCountBalance;
            this.backupCountBalance = backupCountBalance;
            this.totalRewardWeight = totalRewardWeight;
            this.loadUnits = loadUnits;
            this.workloadShare = workloadShare;
            this.roleCountByPerson = roleCountByPerson;
            this.weightedDifficultyPenalty = weightedDifficultyPenalty;
            this.loadStandardDeviation = loadStandardDeviation;
            this.guaranteedAbsenceTolerance = guaranteedAbsenceTolerance;
            this.roleBalanceSwapCount = roleBalanceSwapCount;
            this.loadBalanceSwapCount = loadBalanceSwapCount;
            this.objectiveBefore = objectiveBefore;
            this.objectiveAfter = objectiveAfter;
        }
    }

    public static final class Assignment {
        /** 在 Config.modules 中的索引，指向哪個業務模組。 */
        final int moduleIndex;

        /** 0=主責、1=備援1、2=備援2。 */
        final int roleIndex;

        /** 在 Config.people 中的索引，表示這份角色目前分給誰；交換階段可修改。 */
        int personIndex;

        /** 最終負責人自己給此模組的難度名次，僅供輸出與檢查。 */
        int difficultyRank;

        /** 此模組占全部獎懲池的比例。 */
        double moduleShare;

        /** 此角色在模組內的責任比例，例如主責 0.60。 */
        double responsibilityShare;

        /** 此人因這份角色取得的最終比例 = moduleShare × responsibilityShare。 */
        double personalShare;

        /** 此角色尚未除以總分母的原始責任量。 */
        double loadUnits;

        Assignment(int moduleIndex, int roleIndex, int personIndex) {
            this.moduleIndex = moduleIndex;
            this.roleIndex = roleIndex;
            this.personIndex = personIndex;
        }
    }

    /** 主責件數平衡階段的一個候選「主責 ↔ 備援」交換。 */
    private static final class PrimaryTransferCandidate {
        /** 目前由來源人員持有、準備往外轉移的主責角色。 */
        final Assignment primary;

        /** 目前由目的地人員持有、準備反向交換的備援角色。 */
        final Assignment backup;

        /** 接收主責角色的人員索引。 */
        final int destinationPerson;

        /** 目的地目前是否低於它的主責目標；為 true 時優先直接補足。 */
        final boolean destinationNeedsPrimary;

        /** 交換後相較交換前增加多少難度不適配成本；越小越好。 */
        final double preferenceDelta;

        PrimaryTransferCandidate(
                Assignment primary,
                Assignment backup,
                int destinationPerson,
                boolean destinationNeedsPrimary,
                double preferenceDelta
        ) {
            this.primary = primary;
            this.backup = backup;
            this.destinationPerson = destinationPerson;
            this.destinationNeedsPrimary = destinationNeedsPrimary;
            this.preferenceDelta = preferenceDelta;
        }
    }

    private static final class RoleSlot {
        /** 這份待分配角色屬於哪個模組。 */
        final int moduleIndex;

        /** 這份待分配角色是主責或第幾備援。 */
        final int roleIndex;

        RoleSlot(int moduleIndex, int roleIndex) {
            this.moduleIndex = moduleIndex;
            this.roleIndex = roleIndex;
        }
    }

    private static final class JointAssignmentResult {
        /** 最小成本流解出的角色明細。 */
        final List<Assignment> assignments;

        /** K：主責與全部備援角色總數。 */
        final int totalRoleCount;

        JointAssignmentResult(
                List<Assignment> assignments,
                int totalRoleCount
        ) {
            this.assignments = assignments;
            this.totalRoleCount = totalRoleCount;
        }
    }

    private static final class FlowResult {
        /** 實際從 source 送到 sink 的流量；必須等於全部角色數才算完整。 */
        final int flow;

        /** 最小成本流的整數總成本；主要用於演算法完整性，不直接當獎懲分數。 */
        final long cost;

        FlowResult(int flow, long cost) {
            this.flow = flow;
            this.cost = cost;
        }
    }

    private static final class FlowEdge {
        /** 此邊指向的節點編號。 */
        final int to;

        /** 反向邊在目標節點 adjacency list 中的索引。 */
        final int reverseIndex;

        /** 每通過一單位流量要付出的成本；反向邊成本為其負值。 */
        final long cost;

        /** 建邊時的原始容量，用來計算最終到底流過多少。 */
        final int initialCapacity;

        /** 殘餘容量；演算法執行過程會遞減或被反向流恢復。 */
        int capacity;

        FlowEdge(
                int to,
                int reverseIndex,
                int capacity,
                long cost,
                int initialCapacity
        ) {
            this.to = to;
            this.reverseIndex = reverseIndex;
            this.capacity = capacity;
            this.cost = cost;
            this.initialCapacity = initialCapacity;
        }

        int flow() {
            // 正向邊已使用的流量 = 原始容量 - 目前殘餘容量。
            return initialCapacity - capacity;
        }
    }

    /**
     * Successive shortest augmenting path 最小成本最大流。
     * SPFA 可處理殘餘網路中的反向負成本邊；本題每次至少完成一個角色，
     * 19 個模組、最多 3 個角色的規模很小。
     */
    private static final class MinCostFlow {
        /** graph[u] 保存從節點 u 出發的所有殘餘邊。 */
        private final List<List<FlowEdge>> graph;

        MinCostFlow(int nodeCount) {
            graph = new ArrayList<List<FlowEdge>>(nodeCount);
            for (int node = 0; node < nodeCount; node++) {
                graph.add(new ArrayList<FlowEdge>());
            }
        }

        FlowEdge addEdge(int from, int to, int capacity, long cost) {
            if (capacity < 0 || cost < 0L) {
                throw new IllegalArgumentException("正向邊容量與成本不可為負數。");
            }

            // 每條正向邊都搭配一條初始容量 0、成本相反的反向邊，供後續撤銷舊選擇。
            FlowEdge forward = new FlowEdge(
                    to,
                    graph.get(to).size(),
                    capacity,
                    cost,
                    capacity
            );
            FlowEdge reverse = new FlowEdge(
                    from,
                    graph.get(from).size(),
                    0,
                    -cost,
                    0
            );
            graph.get(from).add(forward);
            graph.get(to).add(reverse);
            return forward;
        }

        FlowResult minCostMaxFlow(int source, int sink, int requiredFlow) {
            // 不斷找目前殘餘網路中成本最低的 source -> sink 路徑並增加流量。
            int nodeCount = graph.size();
            int totalFlow = 0;
            long totalCost = 0L;
            final long infinity = Long.MAX_VALUE / 4L;

            while (totalFlow < requiredFlow) {
                // distance／previousNode／previousEdge 共同記錄本輪最短路徑。
                long[] distance = new long[nodeCount];
                Arrays.fill(distance, infinity);
                int[] previousNode = new int[nodeCount];
                int[] previousEdge = new int[nodeCount];
                Arrays.fill(previousNode, -1);
                boolean[] inQueue = new boolean[nodeCount];
                ArrayDeque<Integer> queue = new ArrayDeque<Integer>();

                distance[source] = 0L;
                queue.addLast(source);
                inQueue[source] = true;

                // SPFA：殘餘網路含負成本反向邊，因此不能直接使用一般 Dijkstra。
                while (!queue.isEmpty()) {
                    int node = queue.removeFirst();
                    inQueue[node] = false;
                    List<FlowEdge> edges = graph.get(node);

                    for (int edgeIndex = 0; edgeIndex < edges.size(); edgeIndex++) {
                        FlowEdge edge = edges.get(edgeIndex);
                        if (edge.capacity <= 0) {
                            continue;
                        }

                        long candidate = addDistance(
                                distance[node],
                                edge.cost,
                                infinity
                        );
                        if (candidate < distance[edge.to]) {
                            distance[edge.to] = candidate;
                            previousNode[edge.to] = node;
                            previousEdge[edge.to] = edgeIndex;

                            if (!inQueue[edge.to]) {
                                queue.addLast(edge.to);
                                inQueue[edge.to] = true;
                            }
                        }
                    }
                }

                if (previousNode[sink] < 0) {
                    break;
                }

                // 找出本條路徑的瓶頸容量。
                int addedFlow = requiredFlow - totalFlow;
                // 套用流量：正向邊扣除容量，反向邊增加可撤銷容量。
                for (int node = sink; node != source; node = previousNode[node]) {
                    FlowEdge edge = graph.get(previousNode[node])
                            .get(previousEdge[node]);
                    addedFlow = Math.min(addedFlow, edge.capacity);
                }

                for (int node = sink; node != source; node = previousNode[node]) {
                    int from = previousNode[node];
                    FlowEdge edge = graph.get(from).get(previousEdge[node]);
                    FlowEdge reverse = graph.get(edge.to).get(edge.reverseIndex);
                    edge.capacity -= addedFlow;
                    reverse.capacity += addedFlow;
                }

                totalFlow += addedFlow;
                if (distance[sink] > 0L
                        && addedFlow > (Long.MAX_VALUE / 8L - totalCost)
                        / distance[sink]) {
                    throw new IllegalArgumentException("最小成本流總成本過大。");
                }
                totalCost += distance[sink] * addedFlow;
            }

            return new FlowResult(totalFlow, totalCost);
        }

        private static long addDistance(long left, long right, long infinity) {
            // 最短路徑成本相加的防溢位版本。
            if (left == infinity) {
                return infinity;
            }
            if (right > 0L && left > infinity - right) {
                return infinity;
            }
            if (right < 0L && left < -infinity - right) {
                return -infinity;
            }
            return left + right;
        }
    }
}
