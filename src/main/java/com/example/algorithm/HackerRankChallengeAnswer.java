package com.example.algorithm;

import java.util.*;

/**
 * Practice set: 30 medium questions (Q013-Q042) and
 * 20 hard questions (Q043-Q062).
 *
 * Each method is intentionally left unimplemented. Complexity hints describe
 * the target complexity of an intended best solution for this practice set.
 */
public final class HackerRankChallengeAnswer {

    private HackerRankChallengeAnswer() {
    }

    public static class ListNode {
        public int val;
        public ListNode next;

        public ListNode(int val) {
            this.val = val;
        }

        public ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    public static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;

        public TreeNode(int val) {
            this.val = val;
        }

        public TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    // ---------------------------------------------------------------------
    // Medium: Q013-Q042 (30 questions)
    // ---------------------------------------------------------------------

    /**
     * Q013 (Medium): Longest subarray with target sum.
     * Return the maximum length of a contiguous subarray whose sum equals k.
     * The array may contain positive, zero, and negative values.
     * Example: nums = [1, -1, 5, -2, 3], k = 3 -> 4.
     * Constraints: 1 <= nums.length <= 100000.
     * Complexity hint: Time O(n), extra space O(n).
     */
    public static int longestSubarraySumK(int[] nums, long k) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        // key：前綴和
        // value：該前綴和第一次出現的索引
        HashMap<Long, Integer> firstIndex = new HashMap<>();

        // 索引 -1 之前的總和是 0
        firstIndex.put(0L, -1);

        long prefixSum = 0L;
        int maxLength = 0;

        for (int i = 0; i < nums.length; i++) {
            prefixSum += nums[i];

            long needed = prefixSum - k;

            if (firstIndex.containsKey(needed)) {
                int startIndex = firstIndex.get(needed);
                int length = i - startIndex;

                maxLength = Math.max(maxLength, length);
            }

            // 只保存第一次出現的位置
            firstIndex.putIfAbsent(prefixSum, i);
        }

        return maxLength;
    }

    /**
     * Q014 (Medium): Product of array except self.
     * Return ans where ans[i] is the product of every value except nums[i].
     * Do not use division. Every result fits in a signed long.
     * Example: [1, 2, 3, 4] -> [24, 12, 8, 6].
     * Constraints: 2 <= nums.length <= 100000.
     * Complexity hint: Time O(n), extra space O(1), excluding the result array.
     */
    public static long[] productExceptSelf(int[] nums) {
        if (nums == null) {
            throw new IllegalArgumentException("nums must be not null");
        }

        long[] ans = new long[nums.length];

        // 第一輪：ans[i] 先存 nums[i] 左邊所有元素的乘積
        long prefix = 1L;
        for (int i = 0; i < nums.length; i++) {
            ans[i] = prefix;
            prefix *= nums[i];
        }

        // 第二輪：乘上 nums[i] 右邊所有元素的乘積
        long suffix = 1L;
        for (int i = nums.length - 1; i >= 0; i--) {
            ans[i] *= suffix;
            suffix *= nums[i];
        }
        return ans;
    }

    /**
     * Q015 (Medium): Merge overlapping intervals.
     * Merge every overlapping interval and return the disjoint intervals sorted
     * by start value. An interval [a, b] includes both endpoints.
     * Example: [[1,3],[2,6],[8,10],[15,18]] -> [[1,6],[8,10],[15,18]].
     * Constraints: 1 <= intervals.length <= 100000.
     * Complexity hint: Time O(n log n), space O(n) including the result.
     */
    public static int[][] mergeIntervals(int[][] intervals) {
        if (intervals.length == 0) return new int[0][];
        Arrays.sort(intervals, Comparator.comparingInt(interval -> interval[0]));
        List<int[]> merged = new ArrayList<>();
        int[] current = intervals[0].clone();
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] <= current[1]) current[1] = Math.max(current[1], intervals[i][1]);
            else {
                merged.add(current);
                current = intervals[i].clone();
            }
        }
        merged.add(current);
        return merged.toArray(int[][]::new);
    }

    /**
     * Q016 (Medium): Rotate a square matrix.
     * Rotate an n x n matrix 90 degrees clockwise in place.
     * Example: [[1,2],[3,4]] becomes [[3,1],[4,2]].
     * Constraints: 1 <= n <= 1000.
     * Complexity hint: Time O(n^2), extra space O(1).
     */
    public static void rotateMatrix(int[][] matrix) {
        if (matrix == null) {
            throw new IllegalArgumentException("matrix cannot be null");
        }

        int n = matrix.length;

        // 對角線轉置：將每個元素的列與欄互換。
        // col 從 row + 1 開始，避免同一對元素被交換兩次。
        for (int row = 0; row < n; row++) {
            for (int col = row + 1; col < n; col++) {
                int temp = matrix[row][col];
                matrix[row][col] = matrix[col][row];
                matrix[col][row] = temp;
            }
        }

        // 反轉每一列，完成 90 度順時針旋轉。
        for (int row = 0; row < n; row++) {
            for (int left = 0, right = n - 1; left < right; left++, right--) {
                int temp = matrix[row][left];
                matrix[row][left] = matrix[row][right];
                matrix[row][right] = temp;
            }
        }
    }

    /**
     * Q017 (Medium): Longest substring without repeated characters.
     * Return the length of the longest contiguous substring containing no
     * repeated character.
     * Example: "abcabcbb" -> 3.
     * Constraints: 0 <= s.length() <= 100000.
     * Complexity hint: Time O(n), extra space O(min(n, alphabet size)).
     */
    public static int longestUniqueSubstring(String s) {
        Map<Character, Integer> lastSeen = new HashMap<>();
        int best = 0, left = 0;
        for (int right = 0; right < s.length(); right++) {
            left = Math.max(left, lastSeen.getOrDefault(s.charAt(right), -1) + 1);
            lastSeen.put(s.charAt(right), right);
            best = Math.max(best, right - left + 1);
        }
        return best;
    }

    /**
     * Q018 (Medium): Group anagrams.
     * Group lowercase words that contain the same character counts. The order of
     * groups and the order inside each group do not matter.
     * Example: ["eat","tea","tan","ate","nat","bat"] ->
     *          [["eat","tea","ate"],["tan","nat"],["bat"]].
     * Constraints: 0 <= words.length <= 100000; words contain 'a' through 'z'.
     * Let L be the total number of characters across all words.
     * Complexity hint: Expected time O(L + n), space O(L + n) including output.
     */
    public static List<List<String>> groupAnagrams(String[] words) {
        Map<String, List<String>> groups = new HashMap<>();
        for (String word : words) {
            int[] counts = new int[26];
            for (int i = 0; i < word.length(); i++) counts[word.charAt(i) - 'a']++;
            groups.computeIfAbsent(Arrays.toString(counts), ignored -> new ArrayList<>()).add(word);
        }
        return new ArrayList<>(groups.values());
    }

    /**
     * Q019 (Medium): Top k frequent values.
     * Return the k values with the highest frequencies in any order.
     * The answer is guaranteed to be unique.
     * Example: nums = [1,1,1,2,2,3], k = 2 -> [1,2].
     * Constraints: 1 <= k <= number of distinct values; nums.length <= 100000.
     * Complexity hint: Expected time O(n), extra space O(n).
     */
    public static int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> frequency = new HashMap<>();
        for (int num : nums) frequency.merge(num, 1, Integer::sum);
        List<List<Integer>> buckets = new ArrayList<>(nums.length + 1);
        for (int i = 0; i <= nums.length; i++) buckets.add(new ArrayList<>());
        for (var entry : frequency.entrySet()) buckets.get(entry.getValue()).add(entry.getKey());
        int[] result = new int[k];
        for (int count = nums.length, index = 0; count >= 0 && index < k; count--) {
            for (int value : buckets.get(count)) {
                result[index++] = value;
                if (index == k) break;
            }
        }
        return result;
    }

    /**
     * Q020 (Medium): Kth largest value.
     * Return the kth largest array value by sorted position, not the kth distinct
     * value. You may modify nums.
     * Example: nums = [3,2,1,5,6,4], k = 2 -> 5.
     * Constraints: 1 <= k <= nums.length <= 100000.
     * Complexity hint: Expected time O(n), extra space O(1).
     */
    public static int kthLargest(int[] nums, int k) {
        int target = nums.length - k;
        int left = 0, right = nums.length - 1;
        while (left <= right) {
            int pivot = partition(nums, left, right);
            if (pivot == target) return nums[pivot];
            if (pivot < target) left = pivot + 1;
            else right = pivot - 1;
        }
        throw new IllegalArgumentException("k is out of range");
    }

    /**
     * Q021 (Medium): Validate a binary search tree.
     * Return true only if every node is strictly greater than all values in its
     * left subtree and strictly less than all values in its right subtree.
     * Example: [2,1,3] -> true; [5,1,4,null,null,3,6] -> false.
     * Constraints: 0 <= number of nodes <= 100000; values may be any int.
     * Let h be the tree height.
     * Complexity hint: Time O(n), extra space O(h).
     */
    public static boolean isValidBST(TreeNode root) {
        return isValidBST(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    /**
     * Q022 (Medium): Binary-tree level order traversal.
     * Return one list per depth, from the root level to the deepest level.
     * Example: [3,9,20,null,null,15,7] -> [[3],[9,20],[15,7]].
     * Constraints: 0 <= number of nodes <= 100000.
     * Let w be the maximum number of nodes on one level.
     * Complexity hint: Time O(n), extra space O(w), excluding the result.
     */
    public static List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) return result;
        Deque<TreeNode> queue = new ArrayDeque<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            List<Integer> level = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.remove();
                level.add(node.val);
                if (node.left != null) queue.add(node.left);
                if (node.right != null) queue.add(node.right);
            }
            result.add(level);
        }
        return result;
    }

    /**
     * Q023 (Medium): Lowest common ancestor in a BST.
     * Return the lowest node whose subtree contains both p and q. Both nodes are
     * present, are distinct, and node values are unique.
     * Example: root = [6,2,8,0,4,7,9], p = 2, q = 8 -> node 6.
     * Constraints: 2 <= number of nodes <= 100000.
     * Let h be the tree height.
     * Complexity hint: Time O(h), extra space O(1).
     */
    public static TreeNode lowestCommonAncestorBST(
            TreeNode root, TreeNode p, TreeNode q) {
        int low = Math.min(p.val, q.val), high = Math.max(p.val, q.val);
        while (root != null) {
            if (root.val < low) root = root.right;
            else if (root.val > high) root = root.left;
            else return root;
        }
        return null;
    }

    /**
     * Q024 (Medium): Linked-list cycle entry.
     * Return the node where a cycle begins, or null if the list has no cycle.
     * Do not change the list.
     * Example: 3 -> 2 -> 0 -> -4 -> node 2 -> ... returns node 2.
     * Constraints: 0 <= number of nodes <= 100000.
     * Complexity hint: Time O(n), extra space O(1).
     */
    public static ListNode cycleEntry(ListNode head) {
        ListNode slow = head, fast = head;
        do {
            if (fast == null || fast.next == null) return null;
            slow = slow.next;
            fast = fast.next.next;
        } while (slow != fast);
        for (slow = head; slow != fast; slow = slow.next, fast = fast.next) {
            // Move both pointers one step; they meet at the cycle entry.
        }
        return slow;
    }

    /**
     * Q025 (Medium): Reorder a linked list.
     * Change L0 -> L1 -> ... -> Ln into L0 -> Ln -> L1 -> Ln-1 -> ... in place.
     * Example: 1 -> 2 -> 3 -> 4 becomes 1 -> 4 -> 2 -> 3.
     * Constraints: 0 <= number of nodes <= 100000.
     * Complexity hint: Time O(n), extra space O(1).
     */
    public static void reorderList(ListNode head) {
        if (head == null || head.next == null) return;
        ListNode slow = head, fast = head;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        ListNode second = reverse(slow.next);
        slow.next = null;
        for (ListNode first = head; second != null;) {
            ListNode nextFirst = first.next, nextSecond = second.next;
            first.next = second;
            second.next = nextFirst;
            first = nextFirst;
            second = nextSecond;
        }
    }

    /**
     * Q026 (Medium): Add two numbers stored in linked lists.
     * Each list stores a non-negative integer in reverse digit order. Return the
     * sum in the same format. Neither input has leading zeroes except number 0.
     * Example: 2 -> 4 -> 3 plus 5 -> 6 -> 4 gives 7 -> 0 -> 8.
     * Constraints: 1 <= each list length <= 100000.
     * Complexity hint: Time O(max(m, n)); O(1) auxiliary space, excluding output.
     */
    public static ListNode addTwoNumbers(ListNode a, ListNode b) {
        ListNode dummy = new ListNode(0), tail = dummy;
        int carry = 0;
        while (a != null || b != null || carry != 0) {
            int sum = carry + (a == null ? 0 : a.val) + (b == null ? 0 : b.val);
            tail = tail.next = new ListNode(sum % 10);
            carry = sum / 10;
            if (a != null) a = a.next;
            if (b != null) b = b.next;
        }
        return dummy.next;
    }

    /**
     * Q027 (Medium): Course schedule.
     * There are numCourses courses numbered 0 through numCourses - 1. Each pair
     * [a,b] means b must be completed before a. Return whether all can finish.
     * Example: numCourses = 2, prerequisites = [[1,0]] -> true.
     * Constraints: 1 <= numCourses <= 100000; prerequisites.length <= 200000.
     * Let V be courses and E be prerequisite pairs.
     * Complexity hint: Time O(V + E), extra space O(V + E).
     */
    public static boolean canFinishCourses(int numCourses, int[][] prerequisites) {
        List<List<Integer>> next = new ArrayList<>(numCourses);
        int[] indegree = new int[numCourses];
        for (int i = 0; i < numCourses; i++) next.add(new ArrayList<>());
        for (int[] prerequisite : prerequisites) {
            next.get(prerequisite[1]).add(prerequisite[0]);
            indegree[prerequisite[0]]++;
        }
        Deque<Integer> queue = new ArrayDeque<>();
        for (int course = 0; course < numCourses; course++) if (indegree[course] == 0) queue.add(course);
        int completed = 0;
        while (!queue.isEmpty()) {
            int course = queue.remove();
            completed++;
            for (int dependent : next.get(course)) if (--indegree[dependent] == 0) queue.add(dependent);
        }
        return completed == numCourses;
    }

    /**
     * Q028 (Medium): Number of islands.
     * Count connected groups of '1' cells in a grid of '0' and '1'. Cells connect
     * only vertically and horizontally. You may modify grid.
     * Example: [[1,1,0],[1,0,0],[0,0,1]] -> 2.
     * Constraints: 1 <= rows, columns <= 1000; rows * columns <= 100000.
     * Complexity hint: Time O(rows * columns), worst-case extra space O(rows * columns).
     */
    public static int numberOfIslands(char[][] grid) {
        int islands = 0;
        for (int row = 0; row < grid.length; row++) for (int col = 0; col < grid[0].length; col++) {
            if (grid[row][col] == '1') {
                islands++;
                floodIsland(grid, row, col);
            }
        }
        return islands;
    }

    /**
     * Q029 (Medium): Shortest path in a binary matrix.
     * In a square grid, 0 is open and 1 is blocked. Return the length of the
     * shortest path from top-left to bottom-right using all eight directions, or
     * -1 if none exists. Path length counts visited cells. You may modify grid.
     * Example: [[0,1],[1,0]] -> 2.
     * Constraints: 1 <= n <= 500.
     * Complexity hint: Time O(n^2), extra space O(n^2).
     */
    public static int shortestPathBinaryMatrix(int[][] grid) {
        int n = grid.length;
        if (grid[0][0] != 0 || grid[n - 1][n - 1] != 0) return -1;
        Deque<int[]> queue = new ArrayDeque<>();
        queue.add(new int[]{0, 0, 1});
        grid[0][0] = 1;
        int[][] directions = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
        while (!queue.isEmpty()) {
            int[] cell = queue.remove();
            if (cell[0] == n - 1 && cell[1] == n - 1) return cell[2];
            for (int[] direction : directions) {
                int row = cell[0] + direction[0], col = cell[1] + direction[1];
                if (row >= 0 && row < n && col >= 0 && col < n && grid[row][col] == 0) {
                    grid[row][col] = 1;
                    queue.add(new int[]{row, col, cell[2] + 1});
                }
            }
        }
        return -1;
    }

    /**
     * Q030 (Medium): Coin change.
     * Return the fewest coins needed to make amount, or -1 if it is impossible.
     * Each positive denomination may be used any number of times.
     * Example: coins = [1,2,5], amount = 11 -> 3.
     * Constraints: 1 <= coins.length <= 100; 0 <= amount <= 100000.
     * Let c be the number of denominations.
     * Complexity hint: Time O(c * amount), extra space O(amount).
     */
    public static int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);
        dp[0] = 0;
        for (int value = 1; value <= amount; value++) {
            for (int coin : coins) if (coin <= value) dp[value] = Math.min(dp[value], dp[value - coin] + 1);
        }
        return dp[amount] > amount ? -1 : dp[amount];
    }

    /**
     * Q031 (Medium): House robber in a circle.
     * Return the largest amount that can be robbed when adjacent houses cannot
     * both be robbed and the first and last houses are adjacent.
     * Example: [2,3,2] -> 3; [1,2,3,1] -> 4.
     * Constraints: 1 <= nums.length <= 100000; nums[i] >= 0.
     * Complexity hint: Time O(n), extra space O(1).
     */
    public static long robCircular(int[] nums) {
        if (nums.length == 1) return nums[0];
        return Math.max(robRange(nums, 0, nums.length - 2), robRange(nums, 1, nums.length - 1));
    }

    /**
     * Q032 (Medium): Longest increasing subsequence.
     * Return the length of the longest strictly increasing subsequence. Selected
     * values do not need to be contiguous.
     * Example: [10,9,2,5,3,7,101,18] -> 4.
     * Constraints: 1 <= nums.length <= 100000.
     * Complexity hint: Time O(n log n), extra space O(n).
     */
    public static int longestIncreasingSubsequence(int[] nums) {
        int[] tails = new int[nums.length];
        int length = 0;
        for (int num : nums) {
            int index = Arrays.binarySearch(tails, 0, length, num);
            if (index < 0) index = -index - 1;
            tails[index] = num;
            if (index == length) length++;
        }
        return length;
    }

    /**
     * Q033 (Medium): Minimum path sum.
     * Starting at the top-left, move only right or down to reach the bottom-right.
     * Return the minimum possible sum of visited non-negative grid values.
     * Example: [[1,3,1],[1,5,1],[4,2,1]] -> 7.
     * Constraints: 1 <= rows * columns <= 100000; each value <= 100000.
     * Complexity hint: Time O(rows * columns), extra space O(min(rows, columns)).
     */
    public static long minimumPathSum(int[][] grid) {
        long[] dp = new long[grid[0].length];
        Arrays.fill(dp, Long.MAX_VALUE / 4);
        dp[0] = 0;
        for (int[] row : grid) {
            dp[0] += row[0];
            for (int col = 1; col < row.length; col++) dp[col] = Math.min(dp[col], dp[col - 1]) + row[col];
        }
        return dp[dp.length - 1];
    }

    /**
     * Q034 (Medium): Search a row-and-column sorted matrix.
     * Each row is sorted left to right and each column is sorted top to bottom.
     * Return whether target occurs in the matrix.
     * Example: [[1,4,7],[2,5,8],[3,6,9]], target = 6 -> true.
     * Constraints: 1 <= rows, columns <= 10000; rows * columns <= 100000.
     * Complexity hint: Time O(rows + columns), extra space O(1).
     */
    public static boolean searchSortedMatrix(int[][] matrix, int target) {
        int row = 0, col = matrix[0].length - 1;
        while (row < matrix.length && col >= 0) {
            if (matrix[row][col] == target) return true;
            if (matrix[row][col] > target) col--;
            else row++;
        }
        return false;
    }

    /**
     * Q035 (Medium): Search a rotated sorted array.
     * nums originally had strictly increasing unique values and was rotated at an
     * unknown pivot. Return target's index, or -1 if it is absent.
     * Example: nums = [4,5,6,7,0,1,2], target = 0 -> 4.
     * Constraints: 1 <= nums.length <= 100000.
     * Complexity hint: Time O(log n), extra space O(1).
     */
    public static int searchRotated(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        while (left <= right) {
            int middle = left + (right - left) / 2;
            if (nums[middle] == target) return middle;
            if (nums[left] <= nums[middle]) {
                if (nums[left] <= target && target < nums[middle]) right = middle - 1;
                else left = middle + 1;
            } else {
                if (nums[middle] < target && target <= nums[right]) left = middle + 1;
                else right = middle - 1;
            }
        }
        return -1;
    }

    /**
     * Q036 (Medium): Minimum in a rotated sorted array.
     * nums contains unique values and was formed by rotating a strictly increasing
     * array. Return its minimum value.
     * Example: [3,4,5,1,2] -> 1.
     * Constraints: 1 <= nums.length <= 100000.
     * Complexity hint: Time O(log n), extra space O(1).
     */
    public static int minimumInRotated(int[] nums) {
        int left = 0, right = nums.length - 1;
        while (left < right) {
            int middle = left + (right - left) / 2;
            if (nums[middle] > nums[right]) left = middle + 1;
            else right = middle;
        }
        return nums[left];
    }

    /**
     * Q037 (Medium): Three sum.
     * Return every unique triplet [a,b,c] whose values sum to zero. The order of
     * triplets and values inside them does not matter. You may sort nums in place.
     * Example: [-1,0,1,2,-1,-4] -> [[-1,-1,2],[-1,0,1]].
     * Constraints: 3 <= nums.length <= 5000.
     * Complexity hint: Time O(n^2), O(1) auxiliary space excluding result and
     *                  the sorting implementation's call stack/work buffer.
     */
    public static List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> result = new ArrayList<>();
        for (int first = 0; first < nums.length - 2; first++) {
            if (first > 0 && nums[first] == nums[first - 1]) continue;
            int left = first + 1, right = nums.length - 1;
            while (left < right) {
                long sum = (long) nums[first] + nums[left] + nums[right];
                if (sum < 0) left++;
                else if (sum > 0) right--;
                else {
                    result.add(List.of(nums[first], nums[left++], nums[right--]));
                    while (left < right && nums[left] == nums[left - 1]) left++;
                    while (left < right && nums[right] == nums[right + 1]) right--;
                }
            }
        }
        return result;
    }

    /**
     * Q038 (Medium): Count subarrays with target sum.
     * Return the number of contiguous subarrays whose sum equals k. The array may
     * contain positive, zero, and negative values.
     * Example: nums = [1,1,1], k = 2 -> 2.
     * Constraints: 1 <= nums.length <= 100000; answer fits in long.
     * Complexity hint: Expected time O(n), extra space O(n).
     */
    public static long countSubarraysSumK(int[] nums, long k) {
        Map<Long, Long> frequency = new HashMap<>();
        frequency.put(0L, 1L);
        long sum = 0, count = 0;
        for (int num : nums) {
            sum += num;
            count += frequency.getOrDefault(sum - k, 0L);
            frequency.merge(sum, 1L, Long::sum);
        }
        return count;
    }

    /**
     * Q039 (Medium): Minimum-size subarray sum.
     * nums contains only positive values. Return the minimum length of a contiguous
     * subarray whose sum is at least target, or 0 if none exists.
     * Example: target = 7, nums = [2,3,1,2,4,3] -> 2.
     * Constraints: 1 <= nums.length <= 100000; target > 0.
     * Complexity hint: Time O(n), extra space O(1).
     */
    public static int minimumSubarrayLength(long target, int[] nums) {
        long sum = 0;
        int best = Integer.MAX_VALUE;
        for (int left = 0, right = 0; right < nums.length; right++) {
            sum += nums[right];
            while (sum >= target) {
                best = Math.min(best, right - left + 1);
                sum -= nums[left++];
            }
        }
        return best == Integer.MAX_VALUE ? 0 : best;
    }

    /**
     * Q040 (Medium): Daily temperatures.
     * For each day, return how many days must pass before a warmer temperature.
     * Use 0 when no later day is warmer.
     * Example: [73,74,75,71,69,72,76,73] -> [1,1,4,2,1,1,0,0].
     * Constraints: 1 <= temperatures.length <= 100000.
     * Complexity hint: Time O(n), extra space O(n).
     */
    public static int[] dailyTemperatures(int[] temperatures) {
        int[] result = new int[temperatures.length];
        Deque<Integer> pending = new ArrayDeque<>();
        for (int day = 0; day < temperatures.length; day++) {
            while (!pending.isEmpty() && temperatures[pending.peek()] < temperatures[day]) {
                int earlier = pending.pop();
                result[earlier] = day - earlier;
            }
            pending.push(day);
        }
        return result;
    }

    /**
     * Q041 (Medium): Decode an encoded string.
     * Decode expressions of the form k[text], which may be nested. Input is valid,
     * contains no spaces, and each k is a positive integer.
     * Example: "3[a2[c]]" -> "accaccacc".
     * Constraints: encoded input length <= 100000; decoded length fits in memory.
     * Let n be encoded length and r be decoded length.
     * Complexity hint: Time O(n + r), extra space O(n + r) including the result.
     */
    public static String decodeString(String s) {
        Deque<Integer> counts = new ArrayDeque<>();
        Deque<StringBuilder> prefixes = new ArrayDeque<>();
        StringBuilder current = new StringBuilder();
        int count = 0;
        for (char ch : s.toCharArray()) {
            if (Character.isDigit(ch)) count = count * 10 + ch - '0';
            else if (ch == '[') {
                counts.push(count);
                prefixes.push(current);
                count = 0;
                current = new StringBuilder();
            } else if (ch == ']') {
                String repeated = current.toString().repeat(counts.pop());
                current = prefixes.pop().append(repeated);
            } else current.append(ch);
        }
        return current.toString();
    }

    /**
     * Q042 (Medium): Insert an interval.
     * intervals is sorted by start and contains no overlaps. Insert newInterval,
     * merging when necessary, and return a sorted non-overlapping array.
     * Example: intervals = [[1,3],[6,9]], newInterval = [2,5] -> [[1,5],[6,9]].
     * Constraints: 0 <= intervals.length <= 100000.
     * Complexity hint: Time O(n), O(1) auxiliary space excluding the O(n) result.
     */
    public static int[][] insertInterval(int[][] intervals, int[] newInterval) {
        List<int[]> result = new ArrayList<>();
        int index = 0;
        while (index < intervals.length && intervals[index][1] < newInterval[0]) result.add(intervals[index++]);
        int start = newInterval[0], end = newInterval[1];
        while (index < intervals.length && intervals[index][0] <= end) {
            start = Math.min(start, intervals[index][0]);
            end = Math.max(end, intervals[index++][1]);
        }
        result.add(new int[]{start, end});
        while (index < intervals.length) result.add(intervals[index++]);
        return result.toArray(int[][]::new);
    }

    // ---------------------------------------------------------------------
    // Hard: Q043-Q062 (20 questions)
    // ---------------------------------------------------------------------

    /**
     * Q043 (Hard): Median of two sorted arrays.
     * Return the median after conceptually combining two individually sorted
     * arrays. At least one array is non-empty.
     * Example: a = [1,3], b = [2] -> 2.0; a = [1,2], b = [3,4] -> 2.5.
     * Constraints: 0 <= m,n <= 100000; m + n >= 1.
     * Complexity hint: Time O(log(min(m, n))), extra space O(1).
     */
    public static double medianOfTwoSortedArrays(int[] a, int[] b) {
        if (a.length > b.length) return medianOfTwoSortedArrays(b, a);
        int totalLeft = (a.length + b.length + 1) / 2;
        for (int left = 0, right = a.length; left <= right;) {
            int cutA = left + (right - left) / 2, cutB = totalLeft - cutA;
            int aLeft = cutA == 0 ? Integer.MIN_VALUE : a[cutA - 1];
            int aRight = cutA == a.length ? Integer.MAX_VALUE : a[cutA];
            int bLeft = cutB == 0 ? Integer.MIN_VALUE : b[cutB - 1];
            int bRight = cutB == b.length ? Integer.MAX_VALUE : b[cutB];
            if (aLeft <= bRight && bLeft <= aRight) {
                int lower = Math.max(aLeft, bLeft);
                if ((a.length + b.length) % 2 == 1) return lower;
                return ((long) lower + Math.min(aRight, bRight)) / 2.0;
            }
            if (aLeft > bRight) right = cutA - 1;
            else left = cutA + 1;
        }
        throw new IllegalArgumentException("arrays must be sorted");
    }

    /**
     * Q044 (Hard): Trapping rain water.
     * Each non-negative value is a bar height of width 1. Return the total water
     * trapped after raining as a long.
     * Example: [0,1,0,2,1,0,1,3,2,1,2,1] -> 6.
     * Constraints: 1 <= height.length <= 100000.
     * Complexity hint: Time O(n), extra space O(1).
     */
    public static long trappedRainWater(int[] height) {
        int left = 0, right = height.length - 1, leftMax = 0, rightMax = 0;
        long water = 0;
        while (left < right) {
            if (height[left] <= height[right]) {
                leftMax = Math.max(leftMax, height[left]);
                water += leftMax - height[left++];
            } else {
                rightMax = Math.max(rightMax, height[right]);
                water += rightMax - height[right--];
            }
        }
        return water;
    }

    /**
     * Q045 (Hard): Largest rectangle in a histogram.
     * Return the largest rectangle area formed by adjacent bars, as a long.
     * Each bar has width 1 and a non-negative height.
     * Example: [2,1,5,6,2,3] -> 10.
     * Constraints: 1 <= heights.length <= 100000.
     * Complexity hint: Time O(n), extra space O(n).
     */
    public static long largestHistogramRectangle(int[] heights) {
        return largestHistogramRectangle(heights, heights.length);
    }

    /**
     * Q046 (Hard): Minimum window substring.
     * Return the shortest substring of s containing every character of t with at
     * least its required frequency. Return "" if no such window exists.
     * Example: s = "ADOBECODEBANC", t = "ABC" -> "BANC".
     * Constraints: 1 <= s.length(), t.length() <= 100000.
     * Let alphabet be the number of distinct representable characters.
     * Complexity hint: Time O(|s| + |t|), extra space O(alphabet).
     */
    public static String minimumWindowSubstring(String s, String t) {
        int[] need = new int[Character.MAX_VALUE + 1];
        for (int i = 0; i < t.length(); i++) need[t.charAt(i)]++;
        int missing = t.length(), bestStart = 0, bestLength = Integer.MAX_VALUE;
        for (int left = 0, right = 0; right < s.length(); right++) {
            if (need[s.charAt(right)]-- > 0) missing--;
            while (missing == 0) {
                if (right - left + 1 < bestLength) {
                    bestStart = left;
                    bestLength = right - left + 1;
                }
                if (++need[s.charAt(left++)] > 0) missing++;
            }
        }
        return bestLength == Integer.MAX_VALUE ? "" : s.substring(bestStart, bestStart + bestLength);
    }

    /**
     * Q047 (Hard): Longest valid parentheses.
     * s contains only '(' and ')'. Return the length of its longest contiguous
     * substring that forms valid parentheses.
     * Example: ")()())" -> 4.
     * Constraints: 0 <= s.length() <= 100000.
     * Complexity hint: Time O(n), extra space O(1).
     */
    public static int longestValidParentheses(String s) {
        int best = 0, open = 0, close = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') open++; else close++;
            if (open == close) best = Math.max(best, 2 * close);
            else if (close > open) open = close = 0;
        }
        open = close = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            if (s.charAt(i) == ')') close++; else open++;
            if (open == close) best = Math.max(best, 2 * open);
            else if (open > close) open = close = 0;
        }
        return best;
    }

    /**
     * Q048 (Hard): First missing positive.
     * Return the smallest positive integer absent from an unsorted array. You may
     * modify nums in place.
     * Example: [3,4,-1,1] -> 2; [7,8,9,11,12] -> 1.
     * Constraints: 1 <= nums.length <= 100000.
     * Complexity hint: Time O(n), extra space O(1).
     */
    public static int firstMissingPositive(int[] nums) {
        for (int index = 0; index < nums.length; index++) {
            while (nums[index] > 0 && nums[index] <= nums.length && nums[nums[index] - 1] != nums[index]) {
                int target = nums[index] - 1;
                int temp = nums[index];
                nums[index] = nums[target];
                nums[target] = temp;
            }
        }
        for (int index = 0; index < nums.length; index++) if (nums[index] != index + 1) return index + 1;
        return nums.length + 1;
    }

    /**
     * Q049 (Hard): Sliding-window maximum.
     * Return the maximum value in every contiguous window of exactly k elements.
     * Example: nums = [1,3,-1,-3,5,3,6,7], k = 3 -> [3,3,5,5,6,7].
     * Constraints: 1 <= k <= nums.length <= 100000.
     * Complexity hint: Time O(n), extra space O(k), excluding the result.
     */
    public static int[] slidingWindowMaximum(int[] nums, int k) {
        int[] result = new int[nums.length - k + 1];
        Deque<Integer> candidates = new ArrayDeque<>();
        for (int index = 0; index < nums.length; index++) {
            while (!candidates.isEmpty() && candidates.peekFirst() <= index - k) candidates.removeFirst();
            while (!candidates.isEmpty() && nums[candidates.peekLast()] <= nums[index]) candidates.removeLast();
            candidates.addLast(index);
            if (index >= k - 1) result[index - k + 1] = nums[candidates.peekFirst()];
        }
        return result;
    }

    /**
     * Q050 (Hard): Merge k sorted linked lists.
     * Merge all ascending linked lists into one ascending list. You may reuse and
     * relink the existing nodes.
     * Example: [1->4->5, 1->3->4, 2->6] -> 1->1->2->3->4->4->5->6.
     * Constraints: 0 <= k <= 10000; total nodes N <= 100000.
     * Complexity hint: Time O(N log k), extra space O(k).
     */
    public static ListNode mergeKSortedLists(ListNode[] lists) {
        PriorityQueue<ListNode> queue = new PriorityQueue<>(Comparator.comparingInt(node -> node.val));
        for (ListNode node : lists) if (node != null) queue.add(node);
        ListNode dummy = new ListNode(0), tail = dummy;
        while (!queue.isEmpty()) {
            tail = tail.next = queue.remove();
            if (tail.next != null) queue.add(tail.next);
        }
        return dummy.next;
    }

    /**
     * Q051 (Hard): Reverse nodes in k-sized groups.
     * Reverse every complete consecutive group of k list nodes. Leave a final
     * incomplete group unchanged. Do not change node values.
     * Example: 1->2->3->4->5, k = 2 -> 2->1->4->3->5.
     * Constraints: 0 <= number of nodes <= 100000; 1 <= k <= 100000.
     * Complexity hint: Time O(n), extra space O(1).
     */
    public static ListNode reverseKGroup(ListNode head, int k) {
        ListNode dummy = new ListNode(0, head), beforeGroup = dummy;
        while (true) {
            ListNode groupEnd = beforeGroup;
            for (int count = 0; count < k && groupEnd != null; count++) groupEnd = groupEnd.next;
            if (groupEnd == null) return dummy.next;
            ListNode afterGroup = groupEnd.next, previous = afterGroup, current = beforeGroup.next;
            while (current != afterGroup) {
                ListNode next = current.next;
                current.next = previous;
                previous = current;
                current = next;
            }
            ListNode oldStart = beforeGroup.next;
            beforeGroup.next = groupEnd;
            beforeGroup = oldStart;
        }
    }

    /**
     * Q052 (Hard): Serialize and deserialize a binary tree.
     * Design these two methods so every int-valued binary tree, including its
     * exact shape, survives a serialize/deserialize round trip. Do not use Java
     * object serialization. deserialize receives only strings made by serialize.
     * Constraints: 0 <= number of nodes <= 100000.
     * Complexity hint: O(n) time per operation; O(n) serialized/output size and
     *                  O(h) auxiliary traversal space, where h is tree height.
     */
    public static String serialize(TreeNode root) {
        StringBuilder data = new StringBuilder();
        serialize(root, data);
        return data.toString();
    }

    public static TreeNode deserialize(String data) {
        return deserialize(data.split(","), new int[]{0});
    }

    /**
     * Q053 (Hard): Binary-tree maximum path sum.
     * A path may start and end at any nodes but may not visit a node twice. Return
     * the greatest sum of values on a non-empty path as a long.
     * Example: [-10,9,20,null,null,15,7] -> 42.
     * Constraints: 1 <= number of nodes <= 100000; values may be negative.
     * Let h be the tree height.
     * Complexity hint: Time O(n), extra space O(h).
     */
    public static long maximumPathSum(TreeNode root) {
        long[] best = {Long.MIN_VALUE};
        maximumPathGain(root, best);
        return best[0];
    }

    /**
     * Q054 (Hard): Edit distance.
     * Return the minimum number of single-character insertions, deletions, and
     * replacements needed to change a into b.
     * Example: a = "horse", b = "ros" -> 3.
     * Constraints: 0 <= a.length(), b.length() <= 10000; m * n <= 10000000.
     * Complexity hint: Time O(m * n), extra space O(min(m, n)).
     */
    public static int editDistance(String a, String b) {
        if (a.length() < b.length()) return editDistance(b, a);
        int[] dp = new int[b.length() + 1];
        for (int col = 0; col <= b.length(); col++) dp[col] = col;
        for (int row = 1; row <= a.length(); row++) {
            int diagonal = dp[0];
            dp[0] = row;
            for (int col = 1; col <= b.length(); col++) {
                int above = dp[col];
                dp[col] = a.charAt(row - 1) == b.charAt(col - 1) ? diagonal : 1 + Math.min(diagonal, Math.min(dp[col], dp[col - 1]));
                diagonal = above;
            }
        }
        return dp[b.length()];
    }

    /**
     * Q055 (Hard): Regular-expression matching.
     * Match the entire text against a pattern where '.' matches any one character
     * and '*' matches zero or more copies of the preceding element. The pattern
     * is valid and '*' never appears first.
     * Example: text = "aab", pattern = "c*a*b" -> true.
     * Constraints: text.length(), pattern.length() <= 2000.
     * Complexity hint: Time O(m * n), extra space O(n), where n is pattern length.
     */
    public static boolean regexMatches(String text, String pattern) {
        boolean[] dp = new boolean[pattern.length() + 1];
        dp[0] = true;
        for (int patternIndex = 2; patternIndex <= pattern.length(); patternIndex++) if (pattern.charAt(patternIndex - 1) == '*') dp[patternIndex] = dp[patternIndex - 2];
        for (int textIndex = 1; textIndex <= text.length(); textIndex++) {
            boolean diagonal = dp[0];
            dp[0] = false;
            for (int patternIndex = 1; patternIndex <= pattern.length(); patternIndex++) {
                boolean previous = dp[patternIndex];
                char token = pattern.charAt(patternIndex - 1);
                if (token == '*') dp[patternIndex] = dp[patternIndex - 2] || (matches(text.charAt(textIndex - 1), pattern.charAt(patternIndex - 2)) && dp[patternIndex]);
                else dp[patternIndex] = matches(text.charAt(textIndex - 1), token) && diagonal;
                diagonal = previous;
            }
        }
        return dp[pattern.length()];
    }

    /**
     * Q056 (Hard): Count distinct subsequences.
     * Return how many distinct ways target can be formed by deleting zero or more
     * characters from source without changing the order of remaining characters.
     * The answer fits in a signed long.
     * Example: source = "rabbbit", target = "rabbit" -> 3.
     * Constraints: source.length(), target.length() <= 10000; product <= 10000000.
     * Complexity hint: Time O(m * n), extra space O(n), n = target.length().
     */
    public static long distinctSubsequences(String source, String target) {
        long[] dp = new long[target.length() + 1];
        dp[0] = 1;
        for (int sourceIndex = 0; sourceIndex < source.length(); sourceIndex--) {
            for (int targetIndex = target.length() - 1; targetIndex >= 0; targetIndex--) {
                if (source.charAt(sourceIndex) == target.charAt(targetIndex)) dp[targetIndex + 1] += dp[targetIndex];
            }
        }
        return dp[target.length()];
    }

    /**
     * Q057 (Hard): Burst balloons.
     * Bursting i earns leftValue * nums[i] * rightValue using the nearest unburst
     * neighbors; missing boundary neighbors have value 1. Return maximum coins.
     * Example: [3,1,5,8] -> 167.
     * Constraints: 1 <= nums.length <= 500; values are non-negative; answer fits long.
     * Complexity hint: Time O(n^3), extra space O(n^2).
     */
    public static long maximumBurstCoins(int[] nums) {
        long[] values = new long[nums.length + 2];
        values[0] = values[values.length - 1] = 1;
        for (int index = 0; index < nums.length; index++) values[index + 1] = nums[index];
        long[][] dp = new long[values.length][values.length];
        for (int length = 2; length < values.length; length++) {
            for (int left = 0; left + length < values.length; left++) {
                int right = left + length;
                for (int last = left + 1; last < right; last++) {
                    dp[left][right] = Math.max(dp[left][right], dp[left][last] + values[left] * values[last] * values[right] + dp[last][right]);
                }
            }
        }
        return dp[0][values.length - 1];
    }

    /**
     * Q058 (Hard): Maximal rectangle.
     * In a matrix containing only '0' and '1', return the area of the largest
     * axis-aligned rectangle containing only '1' cells.
     * Example: [[1,0,1,0,0],[1,0,1,1,1],[1,1,1,1,1],[1,0,0,1,0]] -> 6.
     * Constraints: 1 <= rows * columns <= 100000.
     * Complexity hint: Time O(rows * columns), extra space O(columns).
     */
    public static long maximalRectangle(char[][] matrix) {
        int[] heights = new int[matrix[0].length];
        long best = 0;
        for (char[] row : matrix) {
            for (int col = 0; col < row.length; col++) heights[col] = row[col] == '1' ? heights[col] + 1 : 0;
            best = Math.max(best, largestHistogramRectangle(heights, heights.length));
        }
        return best;
    }

    /**
     * Q059 (Hard): Longest increasing path in a matrix.
     * Move vertically or horizontally to a strictly larger value. Return the
     * greatest possible number of cells in a path; cells cannot wrap around.
     * Example: [[9,9,4],[6,6,8],[2,1,1]] -> 4.
     * Constraints: 1 <= rows * columns <= 100000.
     * Complexity hint: Time O(rows * columns), extra space O(rows * columns).
     */
    public static int longestIncreasingPath(int[][] matrix) {
        int rows = matrix.length, columns = matrix[0].length;
        int[][] outdegree = new int[rows][columns];
        Deque<int[]> leaves = new ArrayDeque<>();
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for (int row = 0; row < rows; row++) for (int col = 0; col < columns; col++) {
            for (int[] direction : directions) {
                int nextRow = row + direction[0], nextCol = col + direction[1];
                if (nextRow >= 0 && nextRow < rows && nextCol >= 0 && nextCol < columns && matrix[nextRow][nextCol] > matrix[row][col]) outdegree[row][col]++;
            }
            if (outdegree[row][col] == 0) leaves.add(new int[]{row, col});
        }
        int length = 0;
        while (!leaves.isEmpty()) {
            length++;
            for (int size = leaves.size(); size > 0; size--) {
                int[] cell = leaves.remove();
                for (int[] direction : directions) {
                    int previousRow = cell[0] + direction[0], previousCol = cell[1] + direction[1];
                    if (previousRow >= 0 && previousRow < rows && previousCol >= 0 && previousCol < columns && matrix[previousRow][previousCol] < matrix[cell[0]][cell[1]] && --outdegree[previousRow][previousCol] == 0) leaves.add(new int[]{previousRow, previousCol});
                }
            }
        }
        return length;
    }

    /**
     * Q060 (Hard): Word ladder.
     * Change beginWord into endWord one letter at a time. Every intermediate word
     * must be in wordList. Return the number of words in the shortest sequence,
     * or 0 if impossible. All words have equal length and lowercase letters.
     * Example: hit -> hot -> dot -> dog -> cog returns 5.
     * Constraints: wordList size N <= 50000; word length L <= 20.
     * Complexity hint: Time O(N * L^2), extra space O(N * L).
     */
    public static int wordLadderLength(
            String beginWord, String endWord, List<String> wordList) {
        Set<String> dictionary = new HashSet<>(wordList);
        if (!dictionary.contains(endWord)) return 0;
        Deque<String> queue = new ArrayDeque<>();
        queue.add(beginWord);
        dictionary.remove(beginWord);
        for (int length = 1; !queue.isEmpty(); length++) {
            for (int size = queue.size(); size > 0; size--) {
                char[] word = queue.remove().toCharArray();
                if (String.valueOf(word).equals(endWord)) return length;
                for (int index = 0; index < word.length; index++) {
                    char original = word[index];
                    for (char replacement = 'a'; replacement <= 'z'; replacement++) {
                        word[index] = replacement;
                        String candidate = String.valueOf(word);
                        if (dictionary.remove(candidate)) queue.add(candidate);
                    }
                    word[index] = original;
                }
            }
        }
        return 0;
    }

    /**
     * Q061 (Hard): Critical connections in a network.
     * An undirected connected graph has servers 0 through n - 1. Return every
     * edge whose removal disconnects the graph. Edge order and result order do
     * not matter.
     * Example: n = 4, edges = [[0,1],[1,2],[2,0],[1,3]] -> [[1,3]].
     * Constraints: 2 <= n <= 100000; n - 1 <= E <= 200000; no duplicate edges.
     * Complexity hint: Time O(V + E), extra space O(V + E).
     */
    public static List<List<Integer>> criticalConnections(
            int n, List<List<Integer>> edges) {
        List<List<Integer>> graph = new ArrayList<>(n);
        for (int node = 0; node < n; node++) graph.add(new ArrayList<>());
        for (List<Integer> edge : edges) {
            graph.get(edge.get(0)).add(edge.get(1));
            graph.get(edge.get(1)).add(edge.get(0));
        }
        int[] discovery = new int[n], low = new int[n];
        List<List<Integer>> bridges = new ArrayList<>();
        int[] time = {0};
        for (int node = 0; node < n; node++) if (discovery[node] == 0) findBridges(node, -1, graph, discovery, low, time, bridges);
        return bridges;
    }

    /**
     * Q062 (Hard): LFU cache.
     * Implement a fixed-capacity cache. get(key) returns its value or -1. put adds
     * or updates a key. When full, put evicts the least-frequently-used key; break
     * frequency ties by evicting the least-recently-used key. A successful get
     * and an update of an existing key both increase that key's frequency.
     * Constraints: capacity <= 100000; at most 200000 get/put calls.
     * Complexity hint: Average O(1) time per get and put; O(capacity) total space.
     */
    public static class LFUCache {

        private static final class Entry {
            final int key;
            int value;
            int frequency = 1;

            Entry(int key, int value) {
                this.key = key;
                this.value = value;
            }
        }

        private final int capacity;
        private int minimumFrequency;
        private final Map<Integer, Entry> entries = new HashMap<>();
        private final Map<Integer, LinkedHashSet<Entry>> entriesByFrequency = new HashMap<>();

        public LFUCache(int capacity) {
            this.capacity = capacity;
        }

        public int get(int key) {
            Entry entry = entries.get(key);
            if (entry == null) return -1;
            touch(entry);
            return entry.value;
        }

        public void put(int key, int value) {
            if (capacity == 0) return;
            Entry existing = entries.get(key);
            if (existing != null) {
                existing.value = value;
                touch(existing);
                return;
            }
            if (entries.size() == capacity) {
                LinkedHashSet<Entry> leastFrequent = entriesByFrequency.get(minimumFrequency);
                Entry evicted = leastFrequent.iterator().next();
                leastFrequent.remove(evicted);
                entries.remove(evicted.key);
            }
            Entry entry = new Entry(key, value);
            entries.put(key, entry);
            entriesByFrequency.computeIfAbsent(1, ignored -> new LinkedHashSet<>()).add(entry);
            minimumFrequency = 1;
        }

        private void touch(Entry entry) {
            LinkedHashSet<Entry> current = entriesByFrequency.get(entry.frequency);
            current.remove(entry);
            if (current.isEmpty() && minimumFrequency == entry.frequency) minimumFrequency++;
            entry.frequency++;
            entriesByFrequency.computeIfAbsent(entry.frequency, ignored -> new LinkedHashSet<>()).add(entry);
        }
    }

    private static int partition(int[] nums, int left, int right) {
        int pivot = nums[right];
        int next = left;
        for (int index = left; index < right; index++) {
            if (nums[index] <= pivot) {
                int temp = nums[next];
                nums[next++] = nums[index];
                nums[index] = temp;
            }
        }
        nums[right] = nums[next];
        nums[next] = pivot;
        return next;
    }

    private static boolean isValidBST(TreeNode node, long lower, long upper) {
        if (node == null) return true;
        return node.val > lower && node.val < upper
                && isValidBST(node.left, lower, node.val)
                && isValidBST(node.right, node.val, upper);
    }

    private static ListNode reverse(ListNode head) {
        ListNode previous = null;
        while (head != null) {
            ListNode next = head.next;
            head.next = previous;
            previous = head;
            head = next;
        }
        return previous;
    }

    private static void floodIsland(char[][] grid, int startRow, int startCol) {
        Deque<int[]> cells = new ArrayDeque<>();
        cells.add(new int[]{startRow, startCol});
        grid[startRow][startCol] = '0';
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        while (!cells.isEmpty()) {
            int[] cell = cells.remove();
            for (int[] direction : directions) {
                int row = cell[0] + direction[0], col = cell[1] + direction[1];
                if (row >= 0 && row < grid.length && col >= 0 && col < grid[0].length && grid[row][col] == '1') {
                    grid[row][col] = '0';
                    cells.add(new int[]{row, col});
                }
            }
        }
    }

    private static long robRange(int[] nums, int start, int end) {
        long previous = 0, current = 0;
        for (int index = start; index <= end; index++) {
            long next = Math.max(current, previous + nums[index]);
            previous = current;
            current = next;
        }
        return current;
    }

    private static long largestHistogramRectangle(int[] heights, int length) {
        Deque<Integer> increasing = new ArrayDeque<>();
        long best = 0;
        for (int index = 0; index <= length; index++) {
            int height = index == length ? 0 : heights[index];
            while (!increasing.isEmpty() && heights[increasing.peek()] > height) {
                int bar = increasing.pop();
                int left = increasing.isEmpty() ? -1 : increasing.peek();
                best = Math.max(best, (long) heights[bar] * (index - left - 1));
            }
            increasing.push(index);
        }
        return best;
    }

    private static void serialize(TreeNode node, StringBuilder data) {
        if (node == null) {
            data.append("#,");
            return;
        }
        data.append(node.val).append(',');
        serialize(node.left, data);
        serialize(node.right, data);
    }

    private static TreeNode deserialize(String[] values, int[] index) {
        String value = values[index[0]++];
        if (value.equals("#")) return null;
        TreeNode node = new TreeNode(Integer.parseInt(value));
        node.left = deserialize(values, index);
        node.right = deserialize(values, index);
        return node;
    }

    private static long maximumPathGain(TreeNode node, long[] best) {
        if (node == null) return 0;
        long left = Math.max(0, maximumPathGain(node.left, best));
        long right = Math.max(0, maximumPathGain(node.right, best));
        best[0] = Math.max(best[0], left + node.val + right);
        return node.val + Math.max(left, right);
    }

    private static boolean matches(char text, char token) {
        return token == '.' || token == text;
    }

    private static void findBridges(
            int node, int parent, List<List<Integer>> graph, int[] discovery, int[] low,
            int[] time, List<List<Integer>> bridges) {
        discovery[node] = low[node] = ++time[0];
        for (int neighbor : graph.get(node)) {
            if (neighbor == parent) continue;
            if (discovery[neighbor] == 0) {
                findBridges(neighbor, node, graph, discovery, low, time, bridges);
                low[node] = Math.min(low[node], low[neighbor]);
                if (low[neighbor] > discovery[node]) bridges.add(List.of(node, neighbor));
            } else low[node] = Math.min(low[node], discovery[neighbor]);
        }
    }
}
