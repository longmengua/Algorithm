package com.example.algorithm;

import java.math.BigInteger;
import java.util.List;

/**
 * 100 題原創 HackerRank 風格 Java 8 練習題。
 *
 * 使用方式：每個 public static 方法就是一題，請用自己的實作取代
 * UnsupportedOperationException。題目依序為 Easy 50、Medium 35、Hard 15。
 */
public class HackerRankChallengeSolver {

    /** 鏈結串列題使用的節點，測試平台可改用自己的同等類型。 */
    public static final class ListNode {
        public int val;
        public ListNode next;

        public ListNode(int val) {
            this.val = val;
        }
    }

    /** 二元樹題使用的節點，測試平台可改用自己的同等類型。 */
    public static final class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;

        public TreeNode(int val) {
            this.val = val;
        }
    }

    // ========================= Easy 01-50 =========================

    /**
     * 題目 001（Easy）陣列總和
     * 題意：回傳整數陣列所有元素的總和，必須使用 long 累加。
     * 範例：[1, -2, 3, 4] -> 6
     * 限制：1 <= nums.length <= 100000。
     */
    public static long arraySum(int[] nums) {
        throw new UnsupportedOperationException("TODO Q001");
    }

    /**
     * 題目 002（Easy）偶數計數
     * 題意：計算陣列中的偶數數量，負偶數與 0 都要計入。
     * 範例：[0, 3, -4, 7, 8] -> 3
     * 限制：0 <= nums.length <= 100000。
     */
    public static int countEvens(int[] nums) {
        throw new UnsupportedOperationException("TODO Q002");
    }

    /**
     * 題目 003（Easy）最大元素
     * 題意：找出非空整數陣列中的最大值。
     * 範例：[-9, -2, -11] -> -2
     * 限制：1 <= nums.length <= 100000。
     */
    public static int maxElement(int[] nums) {
        throw new UnsupportedOperationException("TODO Q003");
    }

    /**
     * 題目 004（Easy）反轉字串
     * 題意：以 Java char 為單位反轉字串。
     * 範例："Java8" -> "8avaJ"
     * 限制：0 <= s.length() <= 100000。
     */
    public static String reverseString(String s) {
        throw new UnsupportedOperationException("TODO Q004");
    }

    /**
     * 題目 005（Easy）忽略格式的回文
     * 題意：忽略非英數字元與英文字母大小寫，判斷字串是否為回文。
     * 範例："A man, a plan, a canal: Panama" -> true
     * 限制：輸入只含 ASCII，長度不超過 100000。
     */
    public static boolean isPalindrome(String s) {
        throw new UnsupportedOperationException("TODO Q005");
    }

    /**
     * 題目 006（Easy）母音數量
     * 題意：計算 a、e、i、o、u 的數量，不分大小寫。
     * 範例："OpenAI Java" -> 6
     * 限制：0 <= s.length() <= 100000。
     */
    public static int countVowels(String s) {
        throw new UnsupportedOperationException("TODO Q006");
    }

    /**
     * 題目 007（Easy）指定字元頻率
     * 題意：計算 target 在字串中出現幾次，區分大小寫。
     * 範例：(\"banana\", 'a') -> 3
     * 限制：0 <= s.length() <= 100000。
     */
    public static int charFrequency(String s, char target) {
        throw new UnsupportedOperationException("TODO Q007");
    }

    /**
     * 題目 008（Easy）保序去重
     * 題意：移除重複整數，保留每個值第一次出現的順序。
     * 範例：[4, 2, 4, 1, 2] -> [4, 2, 1]
     * 限制：0 <= nums.length <= 100000。
     */
    public static int[] stableUnique(int[] nums) {
        throw new UnsupportedOperationException("TODO Q008");
    }

    /**
     * 題目 009（Easy）第二大相異值
     * 題意：回傳第二大的相異值；相異值少於兩個時回傳 null。
     * 範例：[5, 1, 5, 3] -> 3；[7, 7] -> null
     * 限制：1 <= nums.length <= 100000。
     */
    public static Integer secondLargest(int[] nums) {
        throw new UnsupportedOperationException("TODO Q009");
    }

    /**
     * 題目 010（Easy）前綴累加和
     * 題意：ans[i] 等於 nums[0..i] 的總和。
     * 範例：[3, -1, 4] -> [3, 2, 6]
     * 限制：0 <= nums.length <= 100000。
     */
    public static long[] runningSum(int[] nums) {
        throw new UnsupportedOperationException("TODO Q010");
    }

    /**
     * 題目 011（Easy）最大最小差
     * 題意：回傳非空陣列的最大值減最小值，結果使用 long。
     * 範例：[10, -3, 7, 7] -> 13
     * 限制：1 <= nums.length <= 100000。
     */
    public static long rangeDifference(int[] nums) {
        throw new UnsupportedOperationException("TODO Q011");
    }

    /**
     * 題目 012（Easy）成績等第
     * 題意：90+ 為 A、80+ 為 B、70+ 為 C、60+ 為 D，其餘為 F。
     * 範例：83 -> 'B'
     * 限制：0 <= score <= 100。
     */
    public static char grade(int score) {
        throw new UnsupportedOperationException("TODO Q012");
    }

    /**
     * 題目 013（Easy）閏年判斷
     * 題意：可被 400 整除，或可被 4 但不可被 100 整除，即為閏年。
     * 範例：2000 -> true；1900 -> false
     * 限制：1 <= year <= 9999。
     */
    public static boolean isLeapYear(int year) {
        throw new UnsupportedOperationException("TODO Q013");
    }

    /**
     * 題目 014（Easy）FizzBuzz 清單
     * 題意：產生 1..n；3 的倍數用 Fizz、5 的倍數用 Buzz、共同倍數用 FizzBuzz。
     * 範例：5 -> ["1", "2", "Fizz", "4", "Buzz"]
     * 限制：1 <= n <= 100000。
     */
    public static List<String> fizzBuzz(int n) {
        throw new UnsupportedOperationException("TODO Q014");
    }

    /**
     * 題目 015（Easy）最大公因數
     * 題意：回傳兩整數絕對值的最大公因數，規定 gcd(0, 0) = 0。
     * 範例：(-24, 18) -> 6
     * 限制：輸入與答案皆可放入 long。
     */
    public static long gcd(long a, long b) {
        throw new UnsupportedOperationException("TODO Q015");
    }

    /**
     * 題目 016（Easy）最小公倍數
     * 題意：回傳非負最小公倍數；任一參數為 0 時回傳 0。
     * 範例：(12, 18) -> 36
     * 限制：保證答案可放入 long。
     */
    public static long lcm(int a, int b) {
        throw new UnsupportedOperationException("TODO Q016");
    }

    /**
     * 題目 017（Easy）階乘
     * 題意：計算 n!，使用 BigInteger 避免溢位。
     * 範例：5 -> 120
     * 限制：0 <= n <= 2000。
     */
    public static BigInteger factorial(int n) {
        throw new UnsupportedOperationException("TODO Q017");
    }

    /**
     * 題目 018（Easy）第 n 個 Fibonacci 數
     * 題意：定義 F(0)=0、F(1)=1，回傳 F(n)。
     * 範例：10 -> 55
     * 限制：0 <= n <= 92。
     */
    public static long fibonacci(int n) {
        throw new UnsupportedOperationException("TODO Q018");
    }

    /**
     * 題目 019（Easy）質數判斷
     * 題意：判斷整數 n 是否為質數。
     * 範例：29 -> true；1 -> false
     * 限制：n 為任意 int。
     */
    public static boolean isPrime(int n) {
        throw new UnsupportedOperationException("TODO Q019");
    }

    /**
     * 題目 020（Easy）範圍內質數數量
     * 題意：計算 2..n 之間共有多少個質數。
     * 範例：10 -> 4
     * 限制：0 <= n <= 5000000。
     */
    public static int countPrimes(int n) {
        throw new UnsupportedOperationException("TODO Q020");
    }

    /**
     * 題目 021（Easy）數字各位總和
     * 題意：回傳整數絕對值的十進位各位數總和。
     * 範例：-5028 -> 15
     * 限制：n != Long.MIN_VALUE。
     */
    public static int digitSum(long n) {
        throw new UnsupportedOperationException("TODO Q021");
    }

    /**
     * 題目 022（Easy）反轉整數
     * 題意：反轉 32 位元有號整數；若結果溢位則回傳 0。
     * 範例：-120 -> -21；1534236469 -> 0
     * 限制：x 為任意 int。
     */
    public static int reverseInt(int x) {
        throw new UnsupportedOperationException("TODO Q022");
    }

    /**
     * 題目 023（Easy）Armstrong 數
     * 題意：若 n 等於每個十進位位數的「總位數次方」之和，回傳 true。
     * 範例：153 -> true，因為 1^3 + 5^3 + 3^3 = 153
     * 限制：0 <= n <= 1000000000。
     */
    public static boolean isArmstrong(int n) {
        throw new UnsupportedOperationException("TODO Q023");
    }

    /**
     * 題目 024（Easy）二進位字串轉十進位
     * 題意：將只含 0、1 的非空字串轉成 long。
     * 範例："101101" -> 45
     * 限制：1 <= bits.length() <= 63，且數值不超過 Long.MAX_VALUE。
     */
    public static long binaryToDecimal(String bits) {
        throw new UnsupportedOperationException("TODO Q024");
    }

    /**
     * 題目 025（Easy）十進位轉二進位
     * 題意：將非負 long 轉成不含前導零的二進位字串。
     * 範例：10 -> "1010"；0 -> "0"
     * 限制：0 <= n <= Long.MAX_VALUE。
     */
    public static String decimalToBinary(long n) {
        throw new UnsupportedOperationException("TODO Q025");
    }

    /**
     * 題目 026（Easy）向右旋轉一格
     * 題意：將陣列最後一個元素移到最前面；空陣列保持不變。
     * 範例：[1, 2, 3, 4] -> [4, 1, 2, 3]
     * 限制：0 <= nums.length <= 100000。
     */
    public static int[] rotateRightOnce(int[] nums) {
        throw new UnsupportedOperationException("TODO Q026");
    }

    /**
     * 題目 027（Easy）向左旋轉 k 格
     * 題意：循環向左旋轉 k 格，k 可以大於陣列長度。
     * 範例：([1, 2, 3, 4, 5], 7) -> [3, 4, 5, 1, 2]
     * 限制：0 <= k <= 10^18；空陣列保持不變。
     */
    public static int[] leftRotate(int[] nums, long k) {
        throw new UnsupportedOperationException("TODO Q027");
    }

    /**
     * 題目 028（Easy）合併兩個排序陣列
     * 題意：合併兩個非遞減排序陣列，結果也必須非遞減。
     * 範例：([1,4,4], [2,4,9]) -> [1,2,4,4,4,9]
     * 限制：兩陣列總長度不超過 200000。
     */
    public static int[] mergeSorted(int[] a, int[] b) {
        throw new UnsupportedOperationException("TODO Q028");
    }

    /**
     * 題目 029（Easy）兩陣列相異交集
     * 題意：回傳共同出現的相異值，並以遞增順序排列。
     * 範例：([4,9,5,4], [9,4,9,8]) -> [4,9]
     * 限制：兩陣列總長度不超過 200000。
     */
    public static int[] uniqueIntersection(int[] a, int[] b) {
        throw new UnsupportedOperationException("TODO Q029");
    }

    /**
     * 題目 030（Easy）缺少的數字
     * 題意：陣列含 0..n 中恰好 n 個相異數字，找出唯一缺少者。
     * 範例：[3, 0, 1] -> 2
     * 限制：0 <= n = nums.length <= 1000000。
     */
    public static int missingNumber(int[] nums) {
        throw new UnsupportedOperationException("TODO Q030");
    }

    /**
     * 題目 031（Easy）是否存在目標配對
     * 題意：判斷是否存在兩個不同索引，其元素總和等於 target。
     * 範例：([2,7,11,15], 9) -> true
     * 限制：0 <= nums.length <= 200000。
     */
    public static boolean hasPairWithSum(int[] nums, int target) {
        throw new UnsupportedOperationException("TODO Q031");
    }

    /**
     * 題目 032（Easy）單一括號平衡
     * 題意：字串只含 ( 與 )，判斷是否能依正確順序完全配對。
     * 範例："(()())" -> true；"())(" -> false
     * 限制：0 <= s.length() <= 1000000。
     */
    public static boolean balancedParentheses(String s) {
        throw new UnsupportedOperationException("TODO Q032");
    }

    /**
     * 題目 033（Easy）第一個不重複字元
     * 題意：回傳第一個只出現一次的字元索引；不存在回傳 -1。
     * 範例："swiss" -> 1
     * 限制：只含小寫英文字母，長度不超過 100000。
     */
    public static int firstUniqueIndex(String s) {
        throw new UnsupportedOperationException("TODO Q033");
    }

    /**
     * 題目 034（Easy）單字計數
     * 題意：以一個或多個空白字元分隔單字，忽略頭尾空白。
     * 範例："  Java\t makes   sense  " -> 3
     * 限制：0 <= s.length() <= 100000。
     */
    public static int wordCount(String s) {
        throw new UnsupportedOperationException("TODO Q034");
    }

    /**
     * 題目 035（Easy）最長單字
     * 題意：回傳最長單字；同長時回傳最先出現者，沒有單字回傳空字串。
     * 範例："code in modern Java" -> "modern"
     * 限制：輸入只含英文字母與空白，長度不超過 100000。
     */
    public static String longestWord(String s) {
        throw new UnsupportedOperationException("TODO Q035");
    }

    /**
     * 題目 036（Easy）字母異位詞
     * 題意：忽略英文字母大小寫與空白，判斷兩字串是否由相同字元重排而成。
     * 範例：(\"Dormitory\", \"Dirty room\") -> true
     * 限制：輸入為 ASCII，總長度不超過 200000。
     */
    public static boolean areAnagrams(String a, String b) {
        throw new UnsupportedOperationException("TODO Q036");
    }

    /**
     * 題目 037（Easy）Caesar Cipher
     * 題意：英文字母向後循環位移 k 格，保留大小寫，其他字元不變。
     * 範例：(\"Zebra-493\", 3) -> \"Cheud-493\"
     * 限制：0 <= k <= 10^9，字串長度不超過 100000。
     */
    public static String caesarCipher(String s, int k) {
        throw new UnsupportedOperationException("TODO Q037");
    }

    /**
     * 題目 038（Easy）矩陣對角線差
     * 題意：回傳正方形矩陣主、副對角線總和之差的絕對值。
     * 範例：[[1,2,3],[4,5,6],[9,8,9]] -> 2
     * 限制：矩陣為 n x n，1 <= n <= 1000。
     */
    public static long diagonalDifference(int[][] matrix) {
        throw new UnsupportedOperationException("TODO Q038");
    }

    /**
     * 題目 039（Easy）矩陣轉置
     * 題意：回傳 rows x cols 矩陣的轉置矩陣。
     * 範例：[[1,2,3],[4,5,6]] -> [[1,4],[2,5],[3,6]]
     * 限制：矩陣規則且總元素數不超過 1000000。
     */
    public static int[][] transpose(int[][] matrix) {
        throw new UnsupportedOperationException("TODO Q039");
    }

    /**
     * 題目 040（Easy）每列總和
     * 題意：回傳矩陣每一列的元素總和。
     * 範例：[[1,2],[-3,4],[5,5]] -> [3,1,10]
     * 限制：可為空矩陣，總元素數不超過 1000000。
     */
    public static long[] rowSums(int[][] matrix) {
        throw new UnsupportedOperationException("TODO Q040");
    }

    /**
     * 題目 041（Easy）矩陣邊框總和
     * 題意：計算矩陣最外圈元素總和，每個元素只能算一次。
     * 範例：[[1,2,3],[4,5,6],[7,8,9]] -> 40
     * 限制：1 <= rows, cols <= 1000。
     */
    public static long borderSum(int[][] matrix) {
        throw new UnsupportedOperationException("TODO Q041");
    }

    /**
     * 題目 042（Easy）井字棋勝者
     * 題意：棋盤含 X、O、.；回傳勝者 X 或 O，無勝者回傳 .。
     * 範例：[['X','X','X'],['O','.','O'],['.','.','.']] -> 'X'
     * 限制：固定 3 x 3，保證不會同時有兩位勝者。
     */
    public static char ticTacToeWinner(char[][] board) {
        throw new UnsupportedOperationException("TODO Q042");
    }

    /**
     * 題目 043（Easy）12 小時制轉 24 小時制
     * 題意：將 hh:mm:ssAM 或 hh:mm:ssPM 轉成 HH:mm:ss。
     * 範例："07:05:45PM" -> "19:05:45"
     * 限制：輸入格式與時間保證有效。
     */
    public static String to24Hour(String time) {
        throw new UnsupportedOperationException("TODO Q043");
    }

    /**
     * 題目 044（Easy）一年中的第幾天
     * 題意：回傳有效日期是該年的第幾天，1 月 1 日為第 1 天。
     * 範例：(2024, 3, 1) -> 61
     * 限制：1 <= year <= 9999，日期有效。
     */
    public static int dayOfYear(int year, int month, int day) {
        throw new UnsupportedOperationException("TODO Q044");
    }

    /**
     * 題目 045（Easy）溫度統計
     * 題意：依序回傳最低溫、最高溫、平均溫，平均值不需四捨五入。
     * 範例：[20.0, 24.0, 22.0] -> [20.0, 24.0, 22.0]
     * 限制：1 <= values.length <= 100000，所有值皆為有限數。
     */
    public static double[] temperatureStats(double[] values) {
        throw new UnsupportedOperationException("TODO Q045");
    }

    /**
     * 題目 046（Easy）簡易游程編碼
     * 題意：將連續相同字元編碼為「字元加次數」，單一字元也附上 1。
     * 範例："aaabbc" -> "a3b2c1"
     * 限制：只含英文字母，長度不超過 100000。
     */
    public static String runLengthEncode(String s) {
        throw new UnsupportedOperationException("TODO Q046");
    }

    /**
     * 題目 047（Easy）使用者名稱驗證
     * 題意：長度 5..20，首字元為英文字母，其餘只能是字母、數字或底線。
     * 範例："java_dev8" -> true；"8java" -> false
     * 限制：輸入只含 ASCII。
     */
    public static boolean validUsername(String username) {
        throw new UnsupportedOperationException("TODO Q047");
    }

    /**
     * 題目 048（Easy）階梯字串
     * 題意：建立高度 n 的右對齊 # 階梯；列間換行，結尾不加換行。
     * 範例：3 -> "  #\n ##\n###"
     * 限制：1 <= n <= 1000。
     */
    public static String staircase(int n) {
        throw new UnsupportedOperationException("TODO Q048");
    }

    /**
     * 題目 049（Easy）奇數長度陣列的中位數
     * 題意：回傳奇數長度陣列排序後位於正中央的值。
     * 範例：[7,1,3,9,5] -> 5
     * 限制：1 <= nums.length <= 100001，長度為奇數。
     */
    public static int medianOdd(int[] nums) {
        throw new UnsupportedOperationException("TODO Q049");
    }

    /**
     * 題目 050（Easy）最小眾數
     * 題意：回傳出現次數最多的整數；同頻率時回傳最小值。
     * 範例：[4,1,2,2,1] -> 1
     * 限制：1 <= nums.length <= 200000。
     */
    public static int smallestMode(int[] nums) {
        throw new UnsupportedOperationException("TODO Q050");
    }

    // ======================= Medium 51-85 ========================

    /**
     * 題目 051（Medium）最長無重複子字串
     * 題意：回傳不含重複字元的最長連續子字串長度。
     * 範例："abcabcbb" -> 3
     * 限制：輸入只含 ASCII，長度不超過 200000；期望 O(n)。
     */
    public static int longestUniqueSubstring(String s) {
        throw new UnsupportedOperationException("TODO Q051");
    }

    /**
     * 題目 052（Medium）字母異位詞分組
     * 題意：將異位詞放在同組；組內維持輸入順序，各組依首次出現順序排列。
     * 範例：["eat","tea","tan","ate"] -> [["eat","tea","ate"],["tan"]]
     * 限制：只含小寫字母，所有單字總長度不超過 200000。
     */
    public static List<List<String>> groupAnagrams(String[] words) {
        throw new UnsupportedOperationException("TODO Q052");
    }

    /**
     * 題目 053（Medium）前 k 個高頻元素
     * 題意：回傳頻率最高的 k 個相異值；同頻率時數值較小者優先。
     * 範例：([4,4,1,1,2,4,2], 2) -> [4,1]
     * 限制：1 <= k <= 相異值數量，陣列長度不超過 200000。
     */
    public static int[] topKFrequent(int[] nums, int k) {
        throw new UnsupportedOperationException("TODO Q053");
    }

    /**
     * 題目 054（Medium）合併重疊區間
     * 題意：合併所有重疊或端點相接的閉區間，依起點遞增回傳。
     * 範例：[[1,3],[2,6],[8,10],[10,12]] -> [[1,6],[8,12]]
     * 限制：區間數不超過 200000，且 start <= end。
     */
    public static int[][] mergeIntervals(int[][] intervals) {
        throw new UnsupportedOperationException("TODO Q054");
    }

    /**
     * 題目 055（Medium）插入區間
     * 題意：向已排序且互不重疊的閉區間插入新區間，合併必要部分。
     * 範例：([[1,2],[5,7],[9,12]], [6,10]) -> [[1,2],[5,12]]
     * 限制：區間數不超過 200000。
     */
    public static int[][] insertInterval(int[][] intervals, int[] newInterval) {
        throw new UnsupportedOperationException("TODO Q055");
    }

    /**
     * 題目 056（Medium）除自身外的乘積
     * 題意：ans[i] 為除 nums[i] 外所有元素乘積；不可使用除法。
     * 範例：[1,2,3,4] -> [24,12,8,6]
     * 限制：答案可放入 long；額外空間 O(1)，不計回傳陣列。
     */
    public static long[] productExceptSelf(int[] nums) {
        throw new UnsupportedOperationException("TODO Q056");
    }

    /**
     * 題目 057（Medium）三數和為零
     * 題意：找出所有值相異的三元組，使總和為 0；組內遞增，組間按字典序。
     * 範例：[-1,0,1,2,-1,-4] -> [[-1,-1,2],[-1,0,1]]
     * 限制：陣列長度不超過 5000；期望優於 O(n^3)。
     */
    public static List<List<Integer>> threeSum(int[] nums) {
        throw new UnsupportedOperationException("TODO Q057");
    }

    /**
     * 題目 058（Medium）最大連續子陣列和
     * 題意：回傳非空連續子陣列能得到的最大總和。
     * 範例：[-2,1,-3,4,-1,2,1,-5,4] -> 6
     * 限制：陣列長度不超過 1000000；期望 O(n)。
     */
    public static long maxSubarraySum(int[] nums) {
        throw new UnsupportedOperationException("TODO Q058");
    }

    /**
     * 題目 059（Medium）最大連續子陣列乘積
     * 題意：回傳非空連續子陣列能得到的最大乘積。
     * 範例：[2,3,-2,4] -> 6；[-2,0,-1] -> 0
     * 限制：所有中間值與答案可放入 long。
     */
    public static long maxProductSubarray(int[] nums) {
        throw new UnsupportedOperationException("TODO Q059");
    }

    /**
     * 題目 060（Medium）最長嚴格遞增子序列
     * 題意：回傳最長嚴格遞增子序列的長度，子序列不要求連續。
     * 範例：[10,9,2,5,3,7,101,18] -> 4
     * 限制：長度不超過 200000；要求 O(n log n)。
     */
    public static int lisLength(int[] nums) {
        throw new UnsupportedOperationException("TODO Q060");
    }

    /**
     * 題目 061（Medium）最少硬幣數
     * 題意：面額可無限使用，求湊出 amount 的最少硬幣數；無法湊出回傳 -1。
     * 範例：([1,2,5], 11) -> 3
     * 限制：0 <= amount <= 100000，面額皆為正整數。
     */
    public static int minCoins(int[] coins, int amount) {
        throw new UnsupportedOperationException("TODO Q061");
    }

    /**
     * 題目 062（Medium）硬幣組合數
     * 題意：每種硬幣可無限使用，計算湊出 amount 的組合數，順序不同不另計。
     * 範例：([1,2,5], 5) -> 4
     * 限制：0 <= amount <= 10000，保證答案可放入 long。
     */
    public static long coinChangeWays(int[] coins, int amount) {
        throw new UnsupportedOperationException("TODO Q062");
    }

    /**
     * 題目 063（Medium）0/1 背包
     * 題意：每件物品最多選一次，在總重量不超過 capacity 時求最大價值。
     * 範例：([2,3,4], [4,5,7], 5) -> 9
     * 限制：weights 與 values 等長，n * capacity <= 20000000。
     */
    public static long knapsack01(int[] weights, int[] values, int capacity) {
        throw new UnsupportedOperationException("TODO Q063");
    }

    /**
     * 題目 064（Medium）編輯距離
     * 題意：每次可插入、刪除或替換一個字元，求將 a 變成 b 的最少操作數。
     * 範例：(\"horse\", \"ros\") -> 3
     * 限制：兩字串長度乘積不超過 5000000。
     */
    public static int editDistance(String a, String b) {
        throw new UnsupportedOperationException("TODO Q064");
    }

    /**
     * 題目 065（Medium）最長共同子序列
     * 題意：回傳兩字串的最長共同子序列長度，子序列不要求連續。
     * 範例：(\"abcde\", \"ace\") -> 3
     * 限制：兩字串長度乘積不超過 10000000。
     */
    public static int lcsLength(String a, String b) {
        throw new UnsupportedOperationException("TODO Q065");
    }

    /**
     * 題目 066（Medium）單字拆分
     * 題意：判斷 s 能否切成字典中的單字，同一單字可重複使用；空字串回傳 true。
     * 範例：(\"applepenapple\", [\"apple\",\"pen\"]) -> true
     * 限制：s 長度不超過 5000，字典總字元數不超過 200000。
     */
    public static boolean wordBreak(String s, List<String> dictionary) {
        throw new UnsupportedOperationException("TODO Q066");
    }

    /**
     * 題目 067（Medium）數字訊息解碼
     * 題意：1..26 代表 A..Z；計算合法解碼數，答案對 1000000007 取模。
     * 範例："226" -> 3；"06" -> 0
     * 限制：字串只含數字，長度不超過 1000000。
     */
    public static int decodeWays(String digits) {
        throw new UnsupportedOperationException("TODO Q067");
    }

    /**
     * 題目 068（Medium）矩陣螺旋走訪
     * 題意：從左上角開始，依順時針螺旋順序回傳所有元素。
     * 範例：[[1,2,3],[4,5,6],[7,8,9]] -> [1,2,3,6,9,8,7,4,5]
     * 限制：矩陣規則，總元素數不超過 1000000。
     */
    public static int[] spiralOrder(int[][] matrix) {
        throw new UnsupportedOperationException("TODO Q068");
    }

    /**
     * 題目 069（Medium）原地旋轉正方形矩陣
     * 題意：將 n x n 矩陣原地順時針旋轉 90 度。
     * 範例：[[1,2],[3,4]] -> [[3,1],[4,2]]
     * 限制：1 <= n <= 1000，不可建立另一個 n x n 矩陣。
     */
    public static void rotateClockwise(int[][] matrix) {
        throw new UnsupportedOperationException("TODO Q069");
    }

    /**
     * 題目 070（Medium）搜尋排序矩陣
     * 題意：每列、每欄皆遞增；回傳 target 的 [row,col]，不存在回傳 [-1,-1]。
     * 範例：([[1,4,7],[2,5,9],[3,6,12]], 6) -> [2,1]
     * 限制：要求 O(rows + cols)。
     */
    public static int[] searchSortedMatrix(int[][] matrix, int target) {
        throw new UnsupportedOperationException("TODO Q070");
    }

    /**
     * 題目 071（Medium）島嶼數量
     * 題意：1 為陸地、0 為水，上下左右相鄰的陸地屬於同一島嶼。
     * 範例：[['1','1','0'],['0','1','0'],['1','0','1']] -> 3
     * 限制：總格數不超過 1000000；避免遞迴過深。
     */
    public static int countIslands(char[][] grid) {
        throw new UnsupportedOperationException("TODO Q071");
    }

    /**
     * 題目 072（Medium）障礙網格最短路
     * 題意：0 可通行、1 為障礙，只能上下左右移動；求左上到右下最少步數。
     * 範例：[[0,0,1],[1,0,0],[1,1,0]] -> 4
     * 限制：無法抵達回傳 -1，總格數不超過 1000000。
     */
    public static int shortestGridPath(int[][] grid) {
        throw new UnsupportedOperationException("TODO Q072");
    }

    /**
     * 題目 073（Medium）無向圖連通分量
     * 題意：節點為 0..n-1，回傳無向圖連通分量數，孤立節點也要計入。
     * 範例：(5, [[0,1],[1,2],[3,4]]) -> 2
     * 限制：n <= 200000，邊數不超過 300000。
     */
    public static int connectedComponents(int n, int[][] edges) {
        throw new UnsupportedOperationException("TODO Q073");
    }

    /**
     * 題目 074（Medium）無向圖是否有環
     * 題意：判斷無向圖是否存在環；自環算環，兩條平行邊也形成環。
     * 範例：(4, [[0,1],[1,2],[2,0],[2,3]]) -> true
     * 限制：n <= 200000，邊數不超過 300000。
     */
    public static boolean hasUndirectedCycle(int n, int[][] edges) {
        throw new UnsupportedOperationException("TODO Q074");
    }

    /**
     * 題目 075（Medium）字典序最小拓撲排序
     * 題意：回傳有向圖字典序最小的拓撲順序；有環時回傳空陣列。
     * 範例：(4, [[0,2],[1,2],[1,3]]) -> [0,1,2,3]
     * 限制：n <= 200000，邊數不超過 300000。
     */
    public static int[] topologicalSort(int n, int[][] edges) {
        throw new UnsupportedOperationException("TODO Q075");
    }

    /**
     * 題目 076（Medium）非負權重最短路
     * 題意：有向邊為 [from,to,weight]，求 source 到各點最短距離；不可達為 -1。
     * 範例：(4, [[0,1,5],[0,2,2],[2,1,1],[1,3,3]], 0) -> [0,3,2,6]
     * 限制：權重非負，n <= 200000，邊數不超過 300000。
     */
    public static long[] shortestPaths(int n, int[][] edges, int source) {
        throw new UnsupportedOperationException("TODO Q076");
    }

    /**
     * 題目 077（Medium）驗證二元搜尋樹
     * 題意：判斷是否為嚴格 BST；左子樹值都小於節點，右子樹值都大於節點。
     * 範例：層序 [5,1,7,null,null,4,8] -> false
     * 限制：最多 200000 個節點，節點值為任意 int。
     */
    public static boolean isValidBST(TreeNode root) {
        throw new UnsupportedOperationException("TODO Q077");
    }

    /**
     * 題目 078（Medium）二元樹之字形層序
     * 題意：第一層左到右、第二層右到左，交替回傳每層節點值。
     * 範例：層序 [3,9,20,null,null,15,7] -> [[3],[20,9],[15,7]]
     * 限制：最多 200000 個節點。
     */
    public static List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        throw new UnsupportedOperationException("TODO Q078");
    }

    /**
     * 題目 079（Medium）二元樹最低共同祖先
     * 題意：在一般二元樹中回傳 p、q 的最低共同祖先，兩節點保證存在且不同。
     * 範例：根 3 的左右子為 5、1，LCA(5,1) -> 節點 3
     * 限制：最多 200000 個節點，以節點物件身分而非數值判定。
     */
    public static TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        throw new UnsupportedOperationException("TODO Q079");
    }

    /**
     * 題目 080（Medium）BST 第 k 小元素
     * 題意：在嚴格 BST 中回傳第 k 小的值，k 從 1 開始。
     * 範例：層序 [3,1,4,null,2]，k=2 -> 2
     * 限制：1 <= k <= 節點數，最多 200000 個節點。
     */
    public static int kthSmallest(TreeNode root, int k) {
        throw new UnsupportedOperationException("TODO Q080");
    }

    /**
     * 題目 081（Medium）鏈結串列環入口
     * 題意：有環時回傳環開始節點，無環回傳 null；不得修改串列。
     * 範例：3 -> 2 -> 0 -> -4 -> 回到 2，答案為值 2 的節點。
     * 限制：最多 1000000 個節點；額外空間 O(1)。
     */
    public static ListNode cycleEntry(ListNode head) {
        throw new UnsupportedOperationException("TODO Q081");
    }

    /**
     * 題目 082（Medium）重排鏈結串列
     * 題意：將 L0 -> L1 -> ... -> Ln 原地改成 L0 -> Ln -> L1 -> Ln-1 -> ...。
     * 範例：1 -> 2 -> 3 -> 4 -> 5 變成 1 -> 5 -> 2 -> 4 -> 3
     * 限制：最多 200000 個節點；額外空間 O(1)。
     */
    public static void reorderList(ListNode head) {
        throw new UnsupportedOperationException("TODO Q082");
    }

    /**
     * 題目 083（Medium）LRU 快取函式版
     * 題意：操作 [1,key,value] 是 put，[2,key,0] 是 get；回傳所有 get 的結果，
     *       找不到回傳 -1。讀取或寫入後該 key 成為最近使用，超量淘汰最久未使用者。
     * 範例：capacity=2，[[1,1,1],[1,2,2],[2,1,0],[1,3,3],[2,2,0]] -> [1,-1]
     * 限制：最多 500000 次操作；每次操作平均 O(1)。
     */
    public static int[] runLruCache(int capacity, int[][] operations) {
        throw new UnsupportedOperationException("TODO Q083");
    }

    /**
     * 題目 084（Medium）最小值堆疊函式版
     * 題意：操作 [1,x] 為 push、[2,0] 為 pop、[3,0] 為 top、[4,0] 為 getMin；
     *       依序回傳 top 與 getMin 的結果。
     * 範例：[[1,-2],[1,0],[1,-3],[4,0],[2,0],[3,0],[4,0]] -> [-3,0,-2]
     * 限制：操作皆有效；每次操作必須 O(1)。
     */
    public static int[] runMinStack(int[][] operations) {
        throw new UnsupportedOperationException("TODO Q084");
    }

    /**
     * 題目 085（Medium）逆波蘭表示式求值
     * 題意：運算子為 +、-、*、/；整數除法向零截斷。
     * 範例：["2","1","+","3","*"] -> 9
     * 限制：輸入有效，所有中間結果皆在 int 範圍。
     */
    public static int evaluateRpn(String[] tokens) {
        throw new UnsupportedOperationException("TODO Q085");
    }

    // ========================= Hard 86-100 =========================

    /**
     * 題目 086（Hard）兩排序陣列中位數
     * 題意：回傳兩個非遞減陣列合併後的中位數，但不可真的合併全部元素。
     * 範例：([1,3], [2]) -> 2.0；([1,2], [3,4]) -> 2.5
     * 限制：總長度至少 1；要求 O(log(min(a.length, b.length)))。
     */
    public static double medianOfSortedArrays(int[] a, int[] b) {
        throw new UnsupportedOperationException("TODO Q086");
    }

    /**
     * 題目 087（Hard）合併 k 個排序鏈結串列
     * 題意：合併 k 條非遞減鏈結串列，可重用原節點。
     * 範例：[1->4->5, 1->3->4, 2->6] -> 1->1->2->3->4->4->5->6
     * 限制：總節點數 N <= 1000000；目標 O(N log k)。
     */
    public static ListNode mergeKLists(ListNode[] lists) {
        throw new UnsupportedOperationException("TODO Q087");
    }

    /**
     * 題目 088（Hard）單字階梯最短長度
     * 題意：每步改一個字母且新字必須在字典中；回傳最短序列長度，無解為 0。
     * 範例：(\"hit\", \"cog\", [\"hot\",\"dot\",\"dog\",\"lot\",\"log\",\"cog\"]) -> 5
     * 限制：單字等長，最多 50000 個，每字長度不超過 20。
     */
    public static int wordLadderLength(String beginWord, String endWord, List<String> words) {
        throw new UnsupportedOperationException("TODO Q088");
    }

    /**
     * 題目 089（Hard）最小覆蓋子字串
     * 題意：找出 s 中包含 t 所有字元與重複次數的最短連續子字串；無解為空字串。
     * 範例：(\"ADOBECODEBANC\", \"ABC\") -> \"BANC\"
     * 限制：輸入為 ASCII；要求 O(s.length + t.length)。
     */
    public static String minWindow(String s, String t) {
        throw new UnsupportedOperationException("TODO Q089");
    }

    /**
     * 題目 090（Hard）正規表示式比對
     * 題意：. 配對任一字元，* 表示前一元素零次或多次；需完整配對整個字串。
     * 範例：(\"aab\", \"c*a*b\") -> true
     * 限制：模式合法且不以 * 開頭，兩者長度乘積不超過 20000000。
     */
    public static boolean regexMatch(String s, String pattern) {
        throw new UnsupportedOperationException("TODO Q090");
    }

    /**
     * 題目 091（Hard）萬用字元比對
     * 題意：? 配對一個字元，* 配對任意長度字串；需完整配對整個字串。
     * 範例：(\"adceb\", \"*a*b\") -> true
     * 限制：長度各不超過 200000；不得建立完整二維 DP 表。
     */
    public static boolean wildcardMatch(String s, String pattern) {
        throw new UnsupportedOperationException("TODO Q091");
    }

    /**
     * 題目 092（Hard）N 皇后解法數
     * 題意：在 n x n 棋盤放 n 個皇后，使任兩個不在同列、同欄或同對角線。
     * 範例：4 -> 2；1 -> 1
     * 限制：1 <= n <= 15；建議使用位元遮罩回溯。
     */
    public static long totalNQueens(int n) {
        throw new UnsupportedOperationException("TODO Q092");
    }

    /**
     * 題目 093（Hard）數獨求解器
     * 題意：原地填滿 9 x 9 數獨，空格為 .；有解回傳 true，無解須還原並回傳 false。
     * 範例：輸入第一列 53..7....，成功後第一列可能為 534678912。
     * 限制：初始數字不衝突；測資可能需要有效剪枝。
     */
    public static boolean solveSudoku(char[][] board) {
        throw new UnsupportedOperationException("TODO Q093");
    }

    /**
     * 題目 094（Hard）二維接雨水
     * 題意：水會從矩形邊界流出，計算高度圖最多可儲存的總水量。
     * 範例：[[1,4,3,1,3,2],[3,2,1,3,2,4],[2,3,3,2,3,1]] -> 4
     * 限制：高度非負，總格數不超過 1000000。
     */
    public static long trapRainWater(int[][] heightMap) {
        throw new UnsupportedOperationException("TODO Q094");
    }

    /**
     * 題目 095（Hard）直方圖最大矩形
     * 題意：每根柱寬為 1，找出連續柱子能構成的最大矩形面積。
     * 範例：[2,1,5,6,2,3] -> 10
     * 限制：柱數不超過 1000000；要求 O(n)。
     */
    public static long largestRectangle(int[] heights) {
        throw new UnsupportedOperationException("TODO Q095");
    }

    /**
     * 題目 096（Hard）最大流
     * 題意：有向邊為 [from,to,capacity]，求 source 到 sink 的最大流；平行邊容量累加。
     * 範例：(4, [[0,1,3],[0,2,2],[1,2,1],[1,3,2],[2,3,4]], 0, 3) -> 5
     * 限制：n <= 5000，邊數不超過 50000；建議使用 Dinic。
     */
    public static long maxFlow(int n, int[][] edges, int source, int sink) {
        throw new UnsupportedOperationException("TODO Q096");
    }

    /**
     * 題目 097（Hard）最小費用最大流
     * 題意：邊為 [from,to,capacity,cost]；先最大化流量，再最小化費用，回傳 [流量,費用]。
     * 範例：(4, [[0,1,2,1],[0,2,1,5],[1,2,1,0],[1,3,1,3],[2,3,2,1]], 0, 3) -> [3,12]
     * 限制：費用可為負，但不存在可無限降費用的負環；答案可放入 long。
     */
    public static long[] minCostMaxFlow(int n, int[][] edges, int source, int sink) {
        throw new UnsupportedOperationException("TODO Q097");
    }

    /**
     * 題目 098（Hard）區間加值與區間總和
     * 題意：[1,l,r,d] 對閉區間加 d；[2,l,r,0] 查閉區間總和；回傳所有查詢答案。
     * 範例：values=[1,2,3,4]，[[2,0,3,0],[1,1,2,5],[2,1,3,0]] -> [10,19]
     * 限制：陣列與操作數各不超過 200000；每次操作 O(log n)。
     */
    public static long[] processRangeQueries(long[] values, long[][] operations) {
        throw new UnsupportedOperationException("TODO Q098");
    }

    /**
     * 題目 099（Hard）子陣列第 k 小值查詢
     * 題意：查詢 [left,right,k]，回傳該閉區間排序後第 k 小的值。
     * 範例：nums=[5,1,2,3,4]，queries=[[1,4,3],[0,2,2]] -> [3,2]
     * 限制：n、查詢數各不超過 200000；目標 O((n+q) log n)。
     */
    public static int[] kthInRanges(int[] nums, int[][] queries) {
        throw new UnsupportedOperationException("TODO Q099");
    }

    /**
     * 題目 100（Hard）離線動態連通性
     * 題意：圖起初無邊；[1,u,v] 新增、[2,u,v] 移除、[3,u,v] 詢問是否連通。
     * 範例：(4, [[1,0,1],[1,1,2],[3,0,2],[2,1,2],[3,0,2]]) -> [true,false]
     * 限制：n、操作數各不超過 200000；建議時間線段樹搭配可回滾 DSU。
     */
    public static boolean[] dynamicConnectivity(int n, int[][] operations) {
        throw new UnsupportedOperationException("TODO Q100");
    }
}