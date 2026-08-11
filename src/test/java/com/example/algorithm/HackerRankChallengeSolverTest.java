package com.example.algorithm;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HackerRankChallengeSolverTest {

    private static final int TOTAL_QUESTIONS = 100;
    private final AtomicInteger passed = new AtomicInteger();

    @TestFactory
    Stream<DynamicTest> acceptanceScenarios() {
        return Stream.of(
                scenario("Q001 arraySum", () -> assertEquals(6L, HackerRankChallengeSolver.arraySum(new int[]{1, -2, 3, 4}))),
                scenario("Q002 countEvens", () -> assertEquals(3, HackerRankChallengeSolver.countEvens(new int[]{0, 3, -4, 7, 8}))),
                scenario("Q003 maxElement", () -> assertEquals(-2, HackerRankChallengeSolver.maxElement(new int[]{-9, -2, -11}))),
                scenario("Q004 reverseString", () -> assertEquals("8avaJ", HackerRankChallengeSolver.reverseString("Java8"))),
                scenario("Q005 isPalindrome", () -> assertTrue(HackerRankChallengeSolver.isPalindrome("A man, a plan, a canal: Panama"))),
                scenario("Q006 countVowels", () -> assertEquals(6, HackerRankChallengeSolver.countVowels("OpenAI Java"))),
                scenario("Q007 charFrequency", () -> assertEquals(3, HackerRankChallengeSolver.charFrequency("banana", 'a'))),
                scenario("Q008 stableUnique", () -> assertArrayEquals(new int[]{4, 2, 1}, HackerRankChallengeSolver.stableUnique(new int[]{4, 2, 4, 1, 2}))),
                scenario("Q009 secondLargest", () -> assertEquals(3, HackerRankChallengeSolver.secondLargest(new int[]{5, 1, 5, 3}))),
                scenario("Q010 runningSum", () -> assertArrayEquals(new long[]{3, 2, 6}, HackerRankChallengeSolver.runningSum(new int[]{3, -1, 4}))),
                scenario("Q011 rangeDifference", () -> assertEquals(13L, HackerRankChallengeSolver.rangeDifference(new int[]{10, -3, 7, 7}))),
                scenario("Q012 grade", () -> assertEquals('B', HackerRankChallengeSolver.grade(83))),
                scenario("Q013 isLeapYear", () -> assertTrue(HackerRankChallengeSolver.isLeapYear(2000))),
                scenario("Q014 fizzBuzz", () -> assertEquals(List.of("1", "2", "Fizz", "4", "Buzz"), HackerRankChallengeSolver.fizzBuzz(5))),
                scenario("Q015 gcd", () -> assertEquals(6L, HackerRankChallengeSolver.gcd(-24, 18))),
                scenario("Q016 lcm", () -> assertEquals(36L, HackerRankChallengeSolver.lcm(12, 18))),
                scenario("Q017 factorial", () -> assertEquals(BigInteger.valueOf(120), HackerRankChallengeSolver.factorial(5))),
                scenario("Q018 fibonacci", () -> assertEquals(55L, HackerRankChallengeSolver.fibonacci(10))),
                scenario("Q019 isPrime", () -> assertTrue(HackerRankChallengeSolver.isPrime(29))),
                scenario("Q020 countPrimes", () -> assertEquals(4, HackerRankChallengeSolver.countPrimes(10))),
                scenario("Q021 digitSum", () -> assertEquals(15, HackerRankChallengeSolver.digitSum(-5028))),
                scenario("Q022 reverseInt", () -> assertEquals(-21, HackerRankChallengeSolver.reverseInt(-120))),
                scenario("Q023 isArmstrong", () -> assertTrue(HackerRankChallengeSolver.isArmstrong(153))),
                scenario("Q024 binaryToDecimal", () -> assertEquals(45L, HackerRankChallengeSolver.binaryToDecimal("101101"))),
                scenario("Q025 decimalToBinary", () -> assertEquals("1010", HackerRankChallengeSolver.decimalToBinary(10))),
                scenario("Q026 rotateRightOnce", () -> assertArrayEquals(new int[]{4, 1, 2, 3}, HackerRankChallengeSolver.rotateRightOnce(new int[]{1, 2, 3, 4}))),
                scenario("Q027 leftRotate", () -> assertArrayEquals(new int[]{3, 4, 5, 1, 2}, HackerRankChallengeSolver.leftRotate(new int[]{1, 2, 3, 4, 5}, 7))),
                scenario("Q028 mergeSorted", () -> assertArrayEquals(new int[]{1, 2, 4, 4, 4, 9}, HackerRankChallengeSolver.mergeSorted(new int[]{1, 4, 4}, new int[]{2, 4, 9}))),
                scenario("Q029 uniqueIntersection", () -> assertArrayEquals(new int[]{4, 9}, HackerRankChallengeSolver.uniqueIntersection(new int[]{4, 9, 5, 4}, new int[]{9, 4, 9, 8}))),
                scenario("Q030 missingNumber", () -> assertEquals(2, HackerRankChallengeSolver.missingNumber(new int[]{3, 0, 1}))),
                scenario("Q031 hasPairWithSum", () -> assertTrue(HackerRankChallengeSolver.hasPairWithSum(new int[]{2, 7, 11, 15}, 9))),
                scenario("Q032 balancedParentheses", () -> assertTrue(HackerRankChallengeSolver.balancedParentheses("(()())"))),
                scenario("Q033 firstUniqueIndex", () -> assertEquals(1, HackerRankChallengeSolver.firstUniqueIndex("swiss"))),
                scenario("Q034 wordCount", () -> assertEquals(3, HackerRankChallengeSolver.wordCount("  Java\t makes   sense  "))),
                scenario("Q035 longestWord", () -> assertEquals("modern", HackerRankChallengeSolver.longestWord("code in modern Java"))),
                scenario("Q036 areAnagrams", () -> assertTrue(HackerRankChallengeSolver.areAnagrams("Dormitory", "Dirty room"))),
                scenario("Q037 caesarCipher", () -> assertEquals("Cheud-493", HackerRankChallengeSolver.caesarCipher("Zebra-493", 3))),
                scenario("Q038 diagonalDifference", () -> assertEquals(2L, HackerRankChallengeSolver.diagonalDifference(new int[][]{{1, 2, 3}, {4, 5, 6}, {9, 8, 9}}))),
                scenario("Q039 transpose", () -> assertMatrixEquals(new int[][]{{1, 4}, {2, 5}, {3, 6}}, HackerRankChallengeSolver.transpose(new int[][]{{1, 2, 3}, {4, 5, 6}}))),
                scenario("Q040 rowSums", () -> assertArrayEquals(new long[]{3, 1, 10}, HackerRankChallengeSolver.rowSums(new int[][]{{1, 2}, {-3, 4}, {5, 5}}))),
                scenario("Q041 borderSum", () -> assertEquals(40L, HackerRankChallengeSolver.borderSum(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}))),
                scenario("Q042 ticTacToeWinner", () -> assertEquals('X', HackerRankChallengeSolver.ticTacToeWinner(new char[][]{{'X', 'X', 'X'}, {'O', '.', 'O'}, {'.', '.', '.'}}))),
                scenario("Q043 to24Hour", () -> assertEquals("19:05:45", HackerRankChallengeSolver.to24Hour("07:05:45PM"))),
                scenario("Q044 dayOfYear", () -> assertEquals(61, HackerRankChallengeSolver.dayOfYear(2024, 3, 1))),
                scenario("Q045 temperatureStats", () -> assertArrayEquals(new double[]{20, 24, 22}, HackerRankChallengeSolver.temperatureStats(new double[]{20, 24, 22}))),
                scenario("Q046 runLengthEncode", () -> assertEquals("a3b2c1", HackerRankChallengeSolver.runLengthEncode("aaabbc"))),
                scenario("Q047 validUsername", () -> assertTrue(HackerRankChallengeSolver.validUsername("java_dev8"))),
                scenario("Q048 staircase", () -> assertEquals("  #\n ##\n###", HackerRankChallengeSolver.staircase(3))),
                scenario("Q049 medianOdd", () -> assertEquals(5, HackerRankChallengeSolver.medianOdd(new int[]{7, 1, 3, 9, 5}))),
                scenario("Q050 smallestMode", () -> assertEquals(1, HackerRankChallengeSolver.smallestMode(new int[]{4, 1, 2, 2, 1}))),
                scenario("Q051 longestUniqueSubstring", () -> assertEquals(3, HackerRankChallengeSolver.longestUniqueSubstring("abcabcbb"))),
                scenario("Q052 groupAnagrams", () -> assertEquals(List.of(List.of("eat", "tea", "ate"), List.of("tan")), HackerRankChallengeSolver.groupAnagrams(new String[]{"eat", "tea", "tan", "ate"}))),
                scenario("Q053 topKFrequent", () -> assertArrayEquals(new int[]{4, 1}, HackerRankChallengeSolver.topKFrequent(new int[]{4, 4, 1, 1, 2, 4, 2}, 2))),
                scenario("Q054 mergeIntervals", () -> assertMatrixEquals(new int[][]{{1, 6}, {8, 12}}, HackerRankChallengeSolver.mergeIntervals(new int[][]{{1, 3}, {2, 6}, {8, 10}, {10, 12}}))),
                scenario("Q055 insertInterval", () -> assertMatrixEquals(new int[][]{{1, 2}, {5, 12}}, HackerRankChallengeSolver.insertInterval(new int[][]{{1, 2}, {5, 7}, {9, 12}}, new int[]{6, 10}))),
                scenario("Q056 productExceptSelf", () -> assertArrayEquals(new long[]{24, 12, 8, 6}, HackerRankChallengeSolver.productExceptSelf(new int[]{1, 2, 3, 4}))),
                scenario("Q057 threeSum", () -> assertEquals(List.of(List.of(-1, -1, 2), List.of(-1, 0, 1)), HackerRankChallengeSolver.threeSum(new int[]{-1, 0, 1, 2, -1, -4}))),
                scenario("Q058 maxSubarraySum", () -> assertEquals(6L, HackerRankChallengeSolver.maxSubarraySum(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4}))),
                scenario("Q059 maxProductSubarray", () -> assertEquals(6L, HackerRankChallengeSolver.maxProductSubarray(new int[]{2, 3, -2, 4}))),
                scenario("Q060 lisLength", () -> assertEquals(4, HackerRankChallengeSolver.lisLength(new int[]{10, 9, 2, 5, 3, 7, 101, 18}))),
                scenario("Q061 minCoins", () -> assertEquals(3, HackerRankChallengeSolver.minCoins(new int[]{1, 2, 5}, 11))),
                scenario("Q062 coinChangeWays", () -> assertEquals(4L, HackerRankChallengeSolver.coinChangeWays(new int[]{1, 2, 5}, 5))),
                scenario("Q063 knapsack01", () -> assertEquals(9L, HackerRankChallengeSolver.knapsack01(new int[]{2, 3, 4}, new int[]{4, 5, 7}, 5))),
                scenario("Q064 editDistance", () -> assertEquals(3, HackerRankChallengeSolver.editDistance("horse", "ros"))),
                scenario("Q065 lcsLength", () -> assertEquals(3, HackerRankChallengeSolver.lcsLength("abcde", "ace"))),
                scenario("Q066 wordBreak", () -> assertTrue(HackerRankChallengeSolver.wordBreak("applepenapple", List.of("apple", "pen")))),
                scenario("Q067 decodeWays", () -> assertEquals(3, HackerRankChallengeSolver.decodeWays("226"))),
                scenario("Q068 spiralOrder", () -> assertArrayEquals(new int[]{1, 2, 3, 6, 9, 8, 7, 4, 5}, HackerRankChallengeSolver.spiralOrder(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}))),
                scenario("Q069 rotateClockwise", () -> { int[][] matrix = {{1, 2}, {3, 4}}; HackerRankChallengeSolver.rotateClockwise(matrix); assertMatrixEquals(new int[][]{{3, 1}, {4, 2}}, matrix); }),
                scenario("Q070 searchSortedMatrix", () -> assertArrayEquals(new int[]{2, 1}, HackerRankChallengeSolver.searchSortedMatrix(new int[][]{{1, 4, 7}, {2, 5, 9}, {3, 6, 12}}, 6))),
                scenario("Q071 countIslands", () -> assertEquals(3, HackerRankChallengeSolver.countIslands(new char[][]{{'1', '1', '0'}, {'0', '1', '0'}, {'1', '0', '1'}}))),
                scenario("Q072 shortestGridPath", () -> assertEquals(4, HackerRankChallengeSolver.shortestGridPath(new int[][]{{0, 0, 1}, {1, 0, 0}, {1, 1, 0}}))),
                scenario("Q073 connectedComponents", () -> assertEquals(2, HackerRankChallengeSolver.connectedComponents(5, new int[][]{{0, 1}, {1, 2}, {3, 4}}))),
                scenario("Q074 hasUndirectedCycle", () -> assertTrue(HackerRankChallengeSolver.hasUndirectedCycle(4, new int[][]{{0, 1}, {1, 2}, {2, 0}, {2, 3}}))),
                scenario("Q075 topologicalSort", () -> assertArrayEquals(new int[]{0, 1, 2, 3}, HackerRankChallengeSolver.topologicalSort(4, new int[][]{{0, 2}, {1, 2}, {1, 3}}))),
                scenario("Q076 shortestPaths", () -> assertArrayEquals(new long[]{0, 3, 2, 6}, HackerRankChallengeSolver.shortestPaths(4, new int[][]{{0, 1, 5}, {0, 2, 2}, {2, 1, 1}, {1, 3, 3}}, 0))),
                scenario("Q077 isValidBST", () -> {
                    var root = tree(5, tree(1), tree(7, tree(4), tree(8)));
                    assertFalse(HackerRankChallengeSolver.isValidBST(root));
                }),
                scenario("Q078 zigzagLevelOrder", () -> {
                    var root = tree(3, tree(9), tree(20, tree(15), tree(7)));
                    assertEquals(List.of(List.of(3), List.of(20, 9), List.of(15, 7)), HackerRankChallengeSolver.zigzagLevelOrder(root));
                }),
                scenario("Q079 lowestCommonAncestor", () -> { var root = tree(3, tree(5), tree(1)); assertSame(root, HackerRankChallengeSolver.lowestCommonAncestor(root, root.left, root.right)); }),
                scenario("Q080 kthSmallest", () -> assertEquals(2, HackerRankChallengeSolver.kthSmallest(tree(3, tree(1, null, tree(2)), tree(4)), 2))),
                scenario("Q081 cycleEntry", () -> { var a = node(3); var b = node(2); var c = node(0); var d = node(-4); a.next = b; b.next = c; c.next = d; d.next = b; assertSame(b, HackerRankChallengeSolver.cycleEntry(a)); }),
                scenario("Q082 reorderList", () -> { var head = list(1, 2, 3, 4, 5); HackerRankChallengeSolver.reorderList(head); assertArrayEquals(new int[]{1, 5, 2, 4, 3}, listValues(head)); }),
                scenario("Q083 runLruCache", () -> assertArrayEquals(new int[]{1, -1}, HackerRankChallengeSolver.runLruCache(2, new int[][]{{1, 1, 1}, {1, 2, 2}, {2, 1, 0}, {1, 3, 3}, {2, 2, 0}}))),
                scenario("Q084 runMinStack", () -> assertArrayEquals(new int[]{-3, 0, -2}, HackerRankChallengeSolver.runMinStack(new int[][]{{1, -2}, {1, 0}, {1, -3}, {4, 0}, {2, 0}, {3, 0}, {4, 0}}))),
                scenario("Q085 evaluateRpn", () -> assertEquals(9, HackerRankChallengeSolver.evaluateRpn(new String[]{"2", "1", "+", "3", "*"}))),
                scenario("Q086 medianOfSortedArrays", () -> assertEquals(2.5, HackerRankChallengeSolver.medianOfSortedArrays(new int[]{1, 2}, new int[]{3, 4}))),
                scenario("Q087 mergeKLists", () -> assertArrayEquals(new int[]{1, 1, 2, 3, 4, 4, 5, 6}, listValues(HackerRankChallengeSolver.mergeKLists(new HackerRankChallengeSolver.ListNode[]{list(1, 4, 5), list(1, 3, 4), list(2, 6)})))),
                scenario("Q088 wordLadderLength", () -> assertEquals(5, HackerRankChallengeSolver.wordLadderLength("hit", "cog", List.of("hot", "dot", "dog", "lot", "log", "cog")))),
                scenario("Q089 minWindow", () -> assertEquals("BANC", HackerRankChallengeSolver.minWindow("ADOBECODEBANC", "ABC"))),
                scenario("Q090 regexMatch", () -> assertTrue(HackerRankChallengeSolver.regexMatch("aab", "c*a*b"))),
                scenario("Q091 wildcardMatch", () -> assertTrue(HackerRankChallengeSolver.wildcardMatch("adceb", "*a*b"))),
                scenario("Q092 totalNQueens", () -> assertEquals(2L, HackerRankChallengeSolver.totalNQueens(4))),
                scenario("Q093 solveSudoku", () -> { char[][] board = sudoku(); assertTrue(HackerRankChallengeSolver.solveSudoku(board)); assertEquals("534678912", new String(board[0])); }),
                scenario("Q094 trapRainWater", () -> assertEquals(4L, HackerRankChallengeSolver.trapRainWater(new int[][]{{1, 4, 3, 1, 3, 2}, {3, 2, 1, 3, 2, 4}, {2, 3, 3, 2, 3, 1}}))),
                scenario("Q095 largestRectangle", () -> assertEquals(10L, HackerRankChallengeSolver.largestRectangle(new int[]{2, 1, 5, 6, 2, 3}))),
                scenario("Q096 maxFlow", () -> assertEquals(5L, HackerRankChallengeSolver.maxFlow(4, new int[][]{{0, 1, 3}, {0, 2, 2}, {1, 2, 1}, {1, 3, 2}, {2, 3, 4}}, 0, 3))),
                scenario("Q097 minCostMaxFlow", () -> assertArrayEquals(new long[]{3, 12}, HackerRankChallengeSolver.minCostMaxFlow(4, new int[][]{{0, 1, 2, 1}, {0, 2, 1, 5}, {1, 2, 1, 0}, {1, 3, 1, 3}, {2, 3, 2, 1}}, 0, 3))),
                scenario("Q098 processRangeQueries", () -> assertArrayEquals(new long[]{10, 19}, HackerRankChallengeSolver.processRangeQueries(new long[]{1, 2, 3, 4}, new long[][]{{2, 0, 3, 0}, {1, 1, 2, 5}, {2, 1, 3, 0}}))),
                scenario("Q099 kthInRanges", () -> assertArrayEquals(new int[]{3, 2}, HackerRankChallengeSolver.kthInRanges(new int[]{5, 1, 2, 3, 4}, new int[][]{{1, 4, 3}, {0, 2, 2}}))),
                scenario("Q100 dynamicConnectivity", () -> {
                    boolean[] actual = HackerRankChallengeSolver.dynamicConnectivity(
                            4, new int[][]{{1, 0, 1}, {1, 1, 2}, {3, 0, 2}, {2, 1, 2}, {3, 0, 2}});
                    assertArrayEquals(new boolean[]{true, false}, actual);
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

    private static HackerRankChallengeSolver.TreeNode tree(int value) { return tree(value, null, null); }
    private static HackerRankChallengeSolver.TreeNode tree(int value, HackerRankChallengeSolver.TreeNode left, HackerRankChallengeSolver.TreeNode right) {
        var node = new HackerRankChallengeSolver.TreeNode(value); node.left = left; node.right = right; return node;
    }
    private static HackerRankChallengeSolver.ListNode node(int value) { return new HackerRankChallengeSolver.ListNode(value); }
    private static HackerRankChallengeSolver.ListNode list(int... values) {
        HackerRankChallengeSolver.ListNode head = null, tail = null;
        for (int value : values) { var next = node(value); if (head == null) head = next; else tail.next = next; tail = next; }
        return head;
    }
    private static int[] listValues(HackerRankChallengeSolver.ListNode head) {
        var values = new java.util.ArrayList<Integer>();
        for (var current = head; current != null; current = current.next) values.add(current.val);
        return values.stream().mapToInt(Integer::intValue).toArray();
    }
    private static char[][] sudoku() {
        return new char[][]{{'5','3','.','.','7','.','.','.','.'},{'6','.','.','1','9','5','.','.','.'},{'.','9','8','.','.','.','.','6','.'},{'8','.','.','.','6','.','.','.','3'},{'4','.','.','8','.','3','.','.','1'},{'7','.','.','.','2','.','.','.','6'},{'.','6','.','.','.','.','2','8','.'},{'.','.','.','4','1','9','.','.','5'},{'.','.','.','.','8','.','.','7','9'}};
    }

    @FunctionalInterface
    private interface CheckedAssertion { void run() throws Exception; }
}
