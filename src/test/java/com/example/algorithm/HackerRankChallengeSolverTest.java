package com.example.algorithm;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HackerRankChallengeSolverTest {

    private static final int TOTAL_QUESTIONS = 50;
    private final AtomicInteger passed = new AtomicInteger();

    @TestFactory
    Stream<DynamicTest> acceptanceScenarios() {
        return Stream.of(
                scenario("Q013 longestSubarraySumK", () -> assertEquals(4, HackerRankChallengeSolver.longestSubarraySumK(new int[]{1, -1, 5, -2, 3}, 3))),
                scenario("Q014 productExceptSelf", () -> assertArrayEquals(new long[]{24, 12, 8, 6}, HackerRankChallengeSolver.productExceptSelf(new int[]{1, 2, 3, 4}))),
                scenario("Q015 mergeIntervals", () -> assertMatrixEquals(new int[][]{{1, 6}, {8, 10}, {15, 18}}, HackerRankChallengeSolver.mergeIntervals(new int[][]{{1, 3}, {2, 6}, {8, 10}, {15, 18}}))),
                scenario("Q016 rotateMatrix", () -> {
                    int[][] matrix = {{1, 2}, {3, 4}};
                    HackerRankChallengeSolver.rotateMatrix(matrix);
                    assertMatrixEquals(new int[][]{{3, 1}, {4, 2}}, matrix);
                }),
                scenario("Q017 longestUniqueSubstring", () -> assertEquals(3, HackerRankChallengeSolver.longestUniqueSubstring("abcabcbb"))),
                scenario("Q018 groupAnagrams", () -> assertEquals(
                        normalizeStringGroups(List.of(List.of("eat", "tea", "ate"), List.of("tan", "nat"), List.of("bat"))),
                        normalizeStringGroups(HackerRankChallengeSolver.groupAnagrams(new String[]{"eat", "tea", "tan", "ate", "nat", "bat"})))),
                scenario("Q019 topKFrequent", () -> {
                    int[] actual = HackerRankChallengeSolver.topKFrequent(new int[]{1, 1, 1, 2, 2, 3}, 2);
                    java.util.Arrays.sort(actual);
                    assertArrayEquals(new int[]{1, 2}, actual);
                }),
                scenario("Q020 kthLargest", () -> assertEquals(5, HackerRankChallengeSolver.kthLargest(new int[]{3, 2, 1, 5, 6, 4}, 2))),
                scenario("Q021 isValidBST", () -> assertTrue(HackerRankChallengeSolver.isValidBST(tree(2, tree(1), tree(3))))),
                scenario("Q022 levelOrder", () -> assertEquals(List.of(List.of(3), List.of(9, 20), List.of(15, 7)), HackerRankChallengeSolver.levelOrder(tree(3, tree(9), tree(20, tree(15), tree(7)))))),
                scenario("Q023 lowestCommonAncestorBST", () -> {
                    var root = tree(6, tree(2, tree(0), tree(4)), tree(8, tree(7), tree(9)));
                    assertSame(root, HackerRankChallengeSolver.lowestCommonAncestorBST(root, root.left, root.right));
                }),
                scenario("Q024 cycleEntry", () -> {
                    var a = node(3); var b = node(2); var c = node(0); var d = node(-4);
                    a.next = b; b.next = c; c.next = d; d.next = b;
                    assertSame(b, HackerRankChallengeSolver.cycleEntry(a));
                }),
                scenario("Q025 reorderList", () -> {
                    var head = list(1, 2, 3, 4, 5);
                    HackerRankChallengeSolver.reorderList(head);
                    assertArrayEquals(new int[]{1, 5, 2, 4, 3}, listValues(head));
                }),
                scenario("Q026 addTwoNumbers", () -> assertArrayEquals(new int[]{7, 0, 8}, listValues(HackerRankChallengeSolver.addTwoNumbers(list(2, 4, 3), list(5, 6, 4))))),
                scenario("Q027 canFinishCourses", () -> assertTrue(HackerRankChallengeSolver.canFinishCourses(2, new int[][]{{1, 0}}))),
                scenario("Q028 numberOfIslands", () -> assertEquals(2, HackerRankChallengeSolver.numberOfIslands(new char[][]{{'1', '1', '0'}, {'1', '0', '0'}, {'0', '0', '1'}}))),
                scenario("Q029 shortestPathBinaryMatrix", () -> assertEquals(2, HackerRankChallengeSolver.shortestPathBinaryMatrix(new int[][]{{0, 1}, {1, 0}}))),
                scenario("Q030 coinChange", () -> assertEquals(3, HackerRankChallengeSolver.coinChange(new int[]{1, 2, 5}, 11))),
                scenario("Q031 robCircular", () -> assertEquals(4L, HackerRankChallengeSolver.robCircular(new int[]{1, 2, 3, 1}))),
                scenario("Q032 longestIncreasingSubsequence", () -> assertEquals(4, HackerRankChallengeSolver.longestIncreasingSubsequence(new int[]{10, 9, 2, 5, 3, 7, 101, 18}))),
                scenario("Q033 minimumPathSum", () -> assertEquals(7L, HackerRankChallengeSolver.minimumPathSum(new int[][]{{1, 3, 1}, {1, 5, 1}, {4, 2, 1}}))),
                scenario("Q034 searchSortedMatrix", () -> assertTrue(HackerRankChallengeSolver.searchSortedMatrix(new int[][]{{1, 4, 7}, {2, 5, 8}, {3, 6, 9}}, 6))),
                scenario("Q035 searchRotated", () -> assertEquals(4, HackerRankChallengeSolver.searchRotated(new int[]{4, 5, 6, 7, 0, 1, 2}, 0))),
                scenario("Q036 minimumInRotated", () -> assertEquals(1, HackerRankChallengeSolver.minimumInRotated(new int[]{3, 4, 5, 1, 2}))),
                scenario("Q037 threeSum", () -> assertEquals(
                        normalizeGroups(List.of(List.of(-1, -1, 2), List.of(-1, 0, 1))),
                        normalizeGroups(HackerRankChallengeSolver.threeSum(new int[]{-1, 0, 1, 2, -1, -4})))),
                scenario("Q038 countSubarraysSumK", () -> assertEquals(2L, HackerRankChallengeSolver.countSubarraysSumK(new int[]{1, 1, 1}, 2))),
                scenario("Q039 minimumSubarrayLength", () -> assertEquals(2, HackerRankChallengeSolver.minimumSubarrayLength(7, new int[]{2, 3, 1, 2, 4, 3}))),
                scenario("Q040 dailyTemperatures", () -> assertArrayEquals(new int[]{1, 1, 4, 2, 1, 1, 0, 0}, HackerRankChallengeSolver.dailyTemperatures(new int[]{73, 74, 75, 71, 69, 72, 76, 73}))),
                scenario("Q041 decodeString", () -> assertEquals("accaccacc", HackerRankChallengeSolver.decodeString("3[a2[c]]"))),
                scenario("Q042 insertInterval", () -> assertMatrixEquals(new int[][]{{1, 5}, {6, 9}}, HackerRankChallengeSolver.insertInterval(new int[][]{{1, 3}, {6, 9}}, new int[]{2, 5}))),
                scenario("Q043 medianOfTwoSortedArrays", () -> assertEquals(2.5, HackerRankChallengeSolver.medianOfTwoSortedArrays(new int[]{1, 2}, new int[]{3, 4}))),
                scenario("Q044 trappedRainWater", () -> assertEquals(6L, HackerRankChallengeSolver.trappedRainWater(new int[]{0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1}))),
                scenario("Q045 largestHistogramRectangle", () -> assertEquals(10L, HackerRankChallengeSolver.largestHistogramRectangle(new int[]{2, 1, 5, 6, 2, 3}))),
                scenario("Q046 minimumWindowSubstring", () -> assertEquals("BANC", HackerRankChallengeSolver.minimumWindowSubstring("ADOBECODEBANC", "ABC"))),
                scenario("Q047 longestValidParentheses", () -> assertEquals(4, HackerRankChallengeSolver.longestValidParentheses(")()())"))),
                scenario("Q048 firstMissingPositive", () -> assertEquals(2, HackerRankChallengeSolver.firstMissingPositive(new int[]{3, 4, -1, 1}))),
                scenario("Q049 slidingWindowMaximum", () -> assertArrayEquals(new int[]{3, 3, 5, 5, 6, 7}, HackerRankChallengeSolver.slidingWindowMaximum(new int[]{1, 3, -1, -3, 5, 3, 6, 7}, 3))),
                scenario("Q050 mergeKSortedLists", () -> assertArrayEquals(new int[]{1, 1, 2, 3, 4, 4, 5, 6}, listValues(HackerRankChallengeSolver.mergeKSortedLists(new HackerRankChallengeSolver.ListNode[]{list(1, 4, 5), list(1, 3, 4), list(2, 6)})))),
                scenario("Q051 reverseKGroup", () -> assertArrayEquals(new int[]{2, 1, 4, 3, 5}, listValues(HackerRankChallengeSolver.reverseKGroup(list(1, 2, 3, 4, 5), 2)))),
                scenario("Q052 serializeDeserialize", () -> {
                    var root = tree(-1, tree(2), tree(3, tree(4), null));
                    assertTreeEquals(root, HackerRankChallengeSolver.deserialize(HackerRankChallengeSolver.serialize(root)));
                }),
                scenario("Q053 maximumPathSum", () -> assertEquals(42L, HackerRankChallengeSolver.maximumPathSum(tree(-10, tree(9), tree(20, tree(15), tree(7)))))),
                scenario("Q054 editDistance", () -> assertEquals(3, HackerRankChallengeSolver.editDistance("horse", "ros"))),
                scenario("Q055 regexMatches", () -> assertTrue(HackerRankChallengeSolver.regexMatches("aab", "c*a*b"))),
                scenario("Q056 distinctSubsequences", () -> assertEquals(3L, HackerRankChallengeSolver.distinctSubsequences("rabbbit", "rabbit"))),
                scenario("Q057 maximumBurstCoins", () -> assertEquals(167L, HackerRankChallengeSolver.maximumBurstCoins(new int[]{3, 1, 5, 8}))),
                scenario("Q058 maximalRectangle", () -> assertEquals(6L, HackerRankChallengeSolver.maximalRectangle(new char[][]{{'1', '0', '1', '0', '0'}, {'1', '0', '1', '1', '1'}, {'1', '1', '1', '1', '1'}, {'1', '0', '0', '1', '0'}}))),
                scenario("Q059 longestIncreasingPath", () -> assertEquals(4, HackerRankChallengeSolver.longestIncreasingPath(new int[][]{{9, 9, 4}, {6, 6, 8}, {2, 1, 1}}))),
                scenario("Q060 wordLadderLength", () -> assertEquals(5, HackerRankChallengeSolver.wordLadderLength("hit", "cog", List.of("hot", "dot", "dog", "lot", "log", "cog")))),
                scenario("Q061 criticalConnections", () -> assertEquals(List.of(List.of(1, 3)), normalizeEdges(HackerRankChallengeSolver.criticalConnections(4, List.of(List.of(0, 1), List.of(1, 2), List.of(2, 0), List.of(1, 3)))))),
                scenario("Q062 LFUCache", () -> {
                    var cache = new HackerRankChallengeSolver.LFUCache(2);
                    cache.put(1, 1); cache.put(2, 2);
                    assertEquals(1, cache.get(1));
                    cache.put(3, 3);
                    assertEquals(-1, cache.get(2));
                    assertEquals(3, cache.get(3));
                }));
    }

    @AfterAll
    void reportPassRate() {
        System.out.printf("Pass rate: %d/%d (%.1f%%)%n", passed.get(), TOTAL_QUESTIONS, passed.get() * 100.0 / TOTAL_QUESTIONS);
    }

    private DynamicTest scenario(String name, CheckedAssertion assertion) {
        return DynamicTest.dynamicTest(name, () -> {
            assertion.run();
            passed.incrementAndGet();
        });
    }

    private static void assertMatrixEquals(int[][] expected, int[][] actual) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) assertArrayEquals(expected[i], actual[i]);
    }

    private static void assertTreeEquals(HackerRankChallengeSolver.TreeNode expected, HackerRankChallengeSolver.TreeNode actual) {
        if (expected == null || actual == null) {
            assertEquals(expected, actual);
            return;
        }
        assertEquals(expected.val, actual.val);
        assertTreeEquals(expected.left, actual.left);
        assertTreeEquals(expected.right, actual.right);
    }

    private static List<List<Integer>> normalizeGroups(List<List<Integer>> groups) {
        return groups.stream()
                .map(group -> group.stream().sorted().toList())
                .sorted(Comparator.comparing(Object::toString))
                .toList();
    }

    private static List<List<String>> normalizeStringGroups(List<List<String>> groups) {
        return groups.stream()
                .map(group -> group.stream().sorted().toList())
                .sorted(Comparator.comparing(Object::toString))
                .toList();
    }

    private static List<List<Integer>> normalizeEdges(List<List<Integer>> edges) {
        return edges.stream()
                .map(edge -> edge.stream().sorted().toList())
                .sorted(Comparator.comparing(Object::toString))
                .toList();
    }

    private static HackerRankChallengeSolver.TreeNode tree(int value) {
        return tree(value, null, null);
    }

    private static HackerRankChallengeSolver.TreeNode tree(
            int value, HackerRankChallengeSolver.TreeNode left, HackerRankChallengeSolver.TreeNode right) {
        return new HackerRankChallengeSolver.TreeNode(value, left, right);
    }

    private static HackerRankChallengeSolver.ListNode node(int value) {
        return new HackerRankChallengeSolver.ListNode(value);
    }

    private static HackerRankChallengeSolver.ListNode list(int... values) {
        HackerRankChallengeSolver.ListNode head = null;
        HackerRankChallengeSolver.ListNode tail = null;
        for (int value : values) {
            var next = node(value);
            if (head == null) head = next;
            else tail.next = next;
            tail = next;
        }
        return head;
    }

    private static int[] listValues(HackerRankChallengeSolver.ListNode head) {
        var values = new ArrayList<Integer>();
        for (var current = head; current != null; current = current.next) values.add(current.val);
        return values.stream().mapToInt(Integer::intValue).toArray();
    }

    @FunctionalInterface
    private interface CheckedAssertion {
        void run() throws Exception;
    }
}
