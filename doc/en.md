# Algorithm Core Concepts and Example Problems

These notes summarize common core algorithm concepts and representative problems for each concept. The goal is not to memorize complete solutions, but to recognize which tool to consider first based on the characteristics of a problem.

## Table of Contents

1. [HashMap / HashSet](#1-hashmap--hashset)
2. [Two Pointers](#2-two-pointers)
3. [Sliding Window](#3-sliding-window)
4. [Prefix Sum](#4-prefix-sum)
5. [Binary Search](#5-binary-search)
6. [Sorting and Intervals](#6-sorting-and-intervals)
7. [Stack](#7-stack)
8. [Monotonic Stack and Monotonic Queue](#8-monotonic-stack-and-monotonic-queue)
9. [Heap / PriorityQueue](#9-heap--priorityqueue)
10. [Fast and Slow Pointers](#10-fast-and-slow-pointers)
11. [Recursion and Backtracking](#11-recursion-and-backtracking)
12. [Tree DFS / BFS](#12-tree-dfs--bfs)
13. [Graph DFS / BFS](#13-graph-dfs--bfs)
14. [Topological Sort](#14-topological-sort)
15. [Union-Find](#15-union-find)
16. [Greedy](#16-greedy)
17. [Dynamic Programming](#17-dynamic-programming)
18. [Trie](#18-trie)
19. [Bit Manipulation](#19-bit-manipulation)
20. [Combining Core Concepts](#combining-core-concepts)
21. [Recommended Practice Order](#recommended-practice-order)

---

## 1. HashMap / HashSet

### Core Concept

Remember data that has appeared before so it can be looked up quickly later.

- `HashSet`: Records whether a value has appeared.
- `HashMap`: Records the index, frequency, or other information associated with a key.

Recognition cues: finding duplicates, pairing values, counting frequencies, checking whether something has appeared, and finding the earliest or most recent occurrence.

### Example 1: Two Sum

```text
nums = [2, 7, 11, 15], target = 9
Output: [0, 1]
```

When you encounter `7`, calculate the value currently needed:

```text
9 - 7 = 2
```

Then check whether `2` has appeared before. The key idea is to build a mapping from each number to its index.

### Example 2: Contains Duplicate

```text
nums = [1, 2, 3, 1]
Output: true
```

Use a `HashSet` to record numbers that have already been seen. If a number is already in the set before it is added, a duplicate has been found.

### Example 3: Longest Consecutive Sequence

```text
nums = [100, 4, 200, 1, 3, 2]
Output: 4
```

The longest sequence is `[1, 2, 3, 4]`. Use a `HashSet` to quickly determine whether the next integer exists.

---

## 2. Two Pointers

### Core Concept

Move two positions together to narrow the search space in a directed way, avoiding comparisons between every pair of elements.

Recognition cues: sorted arrays, two-value pairing, left-versus-right comparisons, and removing or merging elements in place.

### Example 1: Two Sum in a Sorted Array

```text
nums = [1, 2, 4, 6, 8], target = 10
Output: [2, 8] or [4, 6]
```

- Sum too small: Move the left pointer to the right.
- Sum too large: Move the right pointer to the left.

### Example 2: Valid Palindrome

```text
s = "racecar"
Output: true
```

Start the left and right pointers at opposite ends of the string and compare characters while moving toward the center.

### Example 3: Container With Most Water

```text
height = [1, 8, 6, 2, 5, 4, 8, 3, 7]
Output: 49
```

The left and right pointers form a container. Move the pointer at the shorter side each time, because only that choice can potentially produce a larger area.

---

## 3. Sliding Window

### Core Concept

Maintain a contiguous range: add new elements on the right and remove old elements on the left.

Recognition cues: contiguous subarrays or substrings, longest or shortest ranges, and fixed-length ranges.

### Example 1: Maximum Sum of a Fixed-Length Subarray

```text
nums = [2, 1, 5, 1, 3, 2], windowSize = 3
Output: 9
```

The length-`3` range `[5, 1, 3]` has a sum of `9`.

### Example 2: Longest Substring Without Repeating Characters

```text
s = "abcabcbb"
Output: 3
```

One valid answer is `"abc"`. When a duplicate character appears in the window, move the left boundary until the condition is satisfied again.

### Example 3: Minimum-Size Subarray With Sum at Least K

```text
nums = [2, 3, 1, 2, 4, 3], k = 7
Output: 2
```

The answer is `[4, 3]`. This standard sliding-window approach depends on the elements being positive, or on the problem having a monotonic property.

> If a range-sum problem may contain negative numbers, a standard sliding window will usually fail. Consider using prefix sums instead.

---

## 4. Prefix Sum

### Core Concept

First record the cumulative sum from the beginning through each position. The sum of a contiguous range equals the difference between two prefix sums.

```text
Sum of the range from left through right
= prefix[right + 1] - prefix[left]
```

Recognition cues: contiguous range sums, repeated range queries, arrays containing negative numbers, and sums equal to `k`.

### Example 1: Range Sum Query

```text
nums = [2, 3, 1, 4]
Query indices 1 through 3
Output: 8
```

The requested range is `[3, 1, 4]`, whose sum is `8`.

### Example 2: Number of Subarrays With Sum Equal to K

```text
nums = [1, 1, 1], k = 2
Output: 2
```

Use a prefix sum together with a `HashMap<prefix sum, frequency>`.

### Example 3: Longest Subarray With Sum Equal to K

```text
nums = [1, -1, 5, -2, 3], k = 3
Output: 4
```

The longest range is `[1, -1, 5, -2]`. Use a prefix sum together with a `HashMap<prefix sum, earliest index>`.

---

## 5. Binary Search

### Core Concept

Use sorting or a monotonic boundary to eliminate half of the impossible answers at each step.

Recognition cues: sorted data, an `O(log n)` requirement, the first or last position satisfying a condition, and the minimum feasible answer.

### Example 1: Find a Number in a Sorted Array

```text
nums = [1, 3, 5, 7, 9], target = 7
Output: 3
```

Compare the middle value with the target, then retain the half that may contain the answer.

### Example 2: Find the First Position Greater Than or Equal to Target

```text
nums = [1, 3, 3, 5, 8], target = 3
Output: 1
```

This searches for the left boundary with binary search.

### Example 3: Minimum Banana-Eating Speed

```text
piles = [3, 6, 7, 11], hours = 8
Output: 4
```

This problem does not apply binary search to array indices. Instead, it applies binary search to the possible speeds:

- Speed too slow: Increase the speed.
- Speed sufficient: Try a lower speed.

This technique is known as “binary search on the answer.”

---

## 6. Sorting and Intervals

### Core Concept

After sorting, related elements that were previously scattered become adjacent, allowing many global relationships to be reduced to comparisons between neighbors.

Recognition cues: overlapping intervals, merging intervals, scheduling, minimum differences, and activity selection.

### Example 1: Merge Intervals

```text
intervals = [[1,3], [2,6], [8,10]]
Output: [[1,6], [8,10]]
```

First sort by starting position, then determine whether the next interval overlaps with the current interval.

### Example 2: Insert a New Interval

```text
intervals = [[1,3], [6,9]]
newInterval = [2,5]
Output: [[1,5], [6,9]]
```

### Example 3: Remove the Minimum Number of Intervals to Eliminate Overlaps

```text
intervals = [[1,2], [2,3], [3,4], [1,3]]
Output: 1
```

The usual approach is to sort by ending position and then make greedy selections.

---

## 7. Stack

### Core Concept

A stack is a last-in, first-out structure. It is well suited to handling the most recently started task that has not yet been completed.

Recognition cues: matching parentheses, nested structures, undo operations, expressions, and returning along a path.

### Example 1: Valid Parentheses

```text
s = "([]{})"
Output: true
```

Push opening brackets onto the stack. Each closing bracket must match the opening bracket at the top of the stack.

### Example 2: Evaluate a Postfix Expression

```text
tokens = ["2", "1", "+", "3", "*"]
Output: 9
```

The calculation is `(2 + 1) × 3 = 9`.

### Example 3: Simplify a File Path

```text
path = "/home/user/../docs"
Output: "/home/docs"
```

When `..` is encountered, pop the previous directory from the stack.

---

## 8. Monotonic Stack and Monotonic Queue

### Core Concept

Keep data monotonically increasing or decreasing, while discarding older elements that can no longer be part of the answer.

Recognition cues: the next greater or smaller element, how long until a condition is met, and the maximum or minimum value in a sliding window.

### Example 1: Daily Temperatures

```text
temperatures = [73,74,75,71,69,72,76,73]
Output: [1,1,4,2,1,1,0,0]
```

For each day, find the first position to its right with a higher temperature.

### Example 2: Next Greater Element

```text
nums = [2, 1, 4, 3]
Output: [4, 4, -1, -1]
```

The monotonic stack stores positions that have not yet found their next greater value.

### Example 3: Sliding Window Maximum

```text
nums = [1,3,-1,-3,5,3,6,7], k = 3
Output: [3,3,5,5,6,7]
```

Use a monotonic queue to maintain the maximum value in the current window.

---

## 9. Heap / PriorityQueue

### Core Concept

Quickly retrieve the current minimum or maximum element without fully sorting all elements.

Recognition cues: the top `K` largest or smallest elements, dynamic maximum or minimum values, merging multiple sorted sequences, and scheduling.

### Example 1: Kth Largest Element

```text
nums = [3,2,1,5,6,4], k = 2
Output: 5
```

Maintain a min-heap of size `k`. At the end, the heap's root is the `k`th largest element.

### Example 2: Top K Frequent Elements

```text
nums = [1,1,1,2,2,3], k = 2
Output: [1,2]
```

Use a `HashMap` to count frequencies, then use a heap to select the top `k` elements.

### Example 3: Merge K Sorted Linked Lists

```text
[1,4,5]
[1,3,4]
[2,6]
```

Each time, use the heap to retrieve the smallest current node among all linked lists.

---

## 10. Fast and Slow Pointers

### Core Concept

Use two pointers that move at different speeds or maintain different distances to find a midpoint, cycle, or position at a specific distance.

Recognition cues: the midpoint of a linked list, a linked-list cycle, the entry point of a cycle, and a position measured from the end.

### Example 1: Find the Middle of a Linked List

```text
1 → 2 → 3 → 4 → 5
Output: 3
```

The slow pointer moves one step at a time, while the fast pointer moves two steps at a time.

### Example 2: Detect a Cycle in a Linked List

```text
1 → 2 → 3 → 4
        ↑     ↓
        ← ← ←
```

If a cycle exists, the fast pointer will eventually catch up with the slow pointer.

### Example 3: Find the Duplicate Number

```text
nums = [1,3,4,2,2]
Output: 2
```

Treat the array as a set of pointer relationships, then use Floyd's cycle-detection algorithm.

---

## 11. Recursion and Backtracking

### Core Concept

Make a choice, continue exploring, undo the choice, and then try another path.

Recognition cues: all permutations, combinations, subsets, all possible paths, and placing pieces on a board.

### Example 1: All Subsets

```text
nums = [1,2]
Output: [], [1], [2], [1,2]
```

For each element, there are two decisions: include it or exclude it.

### Example 2: All Permutations

```text
nums = [1,2,3]
Output: [1,2,3], [1,3,2], [2,1,3]...
```

At each level, choose one element that has not yet been used.

### Example 3: Combination Sum

```text
candidates = [2,3,6,7], target = 7
Output: [2,2,3], [7]
```

After choosing a number, continue exploring. Backtrack when the sum exceeds the target.

---

## 12. Tree DFS / BFS

### Core Concept

- DFS: Follow one branch all the way down before exploring another. It is well suited to subtree, depth, and path problems.
- BFS: Traverse one level at a time. It is well suited to level and distance problems.

### Example 1: Maximum Depth of a Binary Tree

```text
       3
      / \
     9  20
       /  \
      15   7

Output: 3
```

Use DFS: the current depth is `1 + the maximum depth of the left and right subtrees`.

### Example 2: Path Sum

Determine whether a root-to-leaf path exists whose node values sum to the target value. This type of problem is well suited to DFS.

### Example 3: Level-Order Traversal

```text
       3
      / \
     9  20
       /  \
      15   7

Output: [[3], [9,20], [15,7]]
```

Use BFS to process the tree one level at a time.

---

## 13. Graph DFS / BFS

### Core Concept

Traverse relationships between nodes. A graph may contain cycles, so a `visited` set is usually required to prevent repeated visits.

Recognition cues: cities and roads, friendships, network connectivity, map grids, and the minimum number of steps.

### Example 1: Number of Islands

```text
1 1 0
1 0 0
0 0 1

Output: 2
```

Whenever an unvisited piece of land is found, use DFS or BFS to traverse the entire island.

### Example 2: Determine Whether a Path Exists

```text
edges = [[0,1], [1,2], [2,3]]
start = 0, end = 3
Output: true
```

### Example 3: Shortest Path in a Binary Matrix

Travel from the top-left corner to the bottom-right corner. Cells containing `0` are traversable, while cells containing `1` are not. Because every step has the same cost, prioritize BFS.

---

## 14. Topological Sort

### Core Concept

At each step, process an item that currently has no unfinished prerequisites. An in-degree of `0` means the item can now be processed.

Recognition cues: course prerequisites, task dependencies, compilation order, and determining whether dependencies form a cycle.

### Example 1: Can All Courses Be Completed?

```text
Course 1 requires Course 0 to be completed first
prerequisites = [[1,0]]
Output: true
```

If the dependencies form a cycle, not all courses can be completed.

### Example 2: Output a Valid Course Order

```text
Dependencies: 0 → 1, 0 → 2, 1 → 3, 2 → 3
Possible output: [0,1,2,3]
```

### Example 3: Infer an Unknown Letter Order

Given words sorted according to an alien dictionary, infer the ordering relationships among the letters. This requires building a dependency graph and applying topological sort.

---

## 15. Union-Find

### Core Concept

Efficiently merge groups and determine whether two elements belong to the same group.

Main operations:

- `find(x)`: Find the representative of the group containing `x`.
- `union(a, b)`: Merge the groups containing `a` and `b`.

### Example 1: Friend Circles / Number of Provinces

```text
A knows B
B knows C
D is in a group by itself

Output: 2 groups
```

### Example 2: Find a Redundant Connection

```text
edges = [[1,2], [1,3], [2,3]]
Output: [2,3]
```

Before `[2,3]` is added, `2` and `3` are already connected, so this edge creates a cycle.

### Example 3: Accounts Merge

If different accounts share the same email address, treat them as belonging to the same person. Use email addresses to merge related accounts into groups.

---

## 16. Greedy

### Core Concept

Make the choice that is currently most advantageous at each step. You must be able to prove that this choice will not prevent a globally optimal answer.

Recognition cues: activity selection, minimizing intervals, resource allocation, and maintaining only the current best state at each step.

### Example 1: Assign Cookies

```text
Children's requirements: [1,2,3]
Cookie sizes: [1,1]
Output: 1
```

Use the smallest sufficient cookie, prioritizing the child with the smallest requirement.

### Example 2: Attend the Maximum Number of Activities

Given multiple `[start time, end time]` intervals, select the maximum number of non-overlapping activities. Sort by ending time and prioritize the activity that ends earliest.

### Example 3: Jump Game

```text
nums = [2,3,1,1,4]
Output: true
```

While traversing the array, maintain the farthest currently reachable position.

> A choice that “looks best” does not automatically justify a greedy approach. You must be able to prove that each local choice leads to a globally optimal solution.

---

## 17. Dynamic Programming

### Core Concept

A large problem can be divided into repeated smaller problems. First define the state and store the answers to smaller problems, then combine them to obtain the answer to the larger problem.

When learning dynamic programming, always ask four questions:

1. What does `dp[i]` or `dp[i][j]` represent?
2. Which previous states does it come from?
3. What are the initial values?
4. Which state should be returned at the end?

### Example 1: Climbing Stairs

```text
n = 4
Output: 5
```

State transition:

```java
dp[i] = dp[i - 1] + dp[i - 2];
```

### Example 2: House Robber

```text
nums = [2,7,9,3,1]
Output: 12
```

Adjacent houses cannot both be selected. At each step, compare robbing the current house with skipping it.

### Example 3: Coin Change

```text
coins = [1,2,5], amount = 11
Output: 3
```

The answer is `5 + 5 + 1`. Find the minimum number of coins needed to make up the target amount.

### Example 4: Longest Common Subsequence

```text
text1 = "abcde"
text2 = "ace"
Output: 3
```

The common subsequence is `"ace"`. This is a classic two-dimensional dynamic-programming problem.

---

## 18. Trie

### Core Concept

A trie is a tree designed specifically for storing strings. Multiple strings can share the same prefix.

Recognition cues: string prefixes, autocomplete, large numbers of dictionary lookups, and word searches on a board.

### Example 1: Implement a Trie

```java
insert("apple");
search("apple");     // true
startsWith("app");   // true
```

### Example 2: Search Suggestions

```text
Input prefix: "app"
Output: apple, application, apply
```

### Example 3: Word Search on a Board

Given a board of letters and a large collection of words, find which words can be formed on the board. The usual approach is Trie + DFS + backtracking.

---

## 19. Bit Manipulation

### Core Concept

Operate directly on the binary representation of integers.

Common operations:

```java
a & b   // AND: 1 only when both sides are 1
a | b   // OR: 1 when either side is 1
a ^ b   // XOR: 1 only when the two sides differ
a << 1  // Left shift
a >> 1  // Right shift
```

### Example 1: Single Number

```text
nums = [4,1,2,1,2]
Output: 4
```

Use these properties:

```text
x ^ x = 0
x ^ 0 = x
```

Paired numbers cancel each other out.

### Example 2: Count the Number of 1 Bits

```text
n = 11
Binary = 1011
Output: 3
```

### Example 3: Represent Subsets With Bits

```text
nums = [a,b,c]

000 → []
001 → [c]
010 → [b]
011 → [b,c]
...
111 → [a,b,c]
```

Each bit indicates whether the corresponding element is selected.

---

## Combining Core Concepts

A problem does not necessarily use only one core concept. Medium and Hard problems often combine multiple concepts.

| Problem | Combination of Core Concepts |
| --- | --- |
| Longest subarray with sum equal to K | Prefix Sum + HashMap |
| Sliding window maximum | Sliding Window + Monotonic Queue |
| Top K frequent elements | HashMap + Heap |
| Merge K sorted linked lists | Heap + Linked List |
| Word search on a board | Trie + DFS + Backtracking |
| Non-overlapping intervals | Sorting + Greedy |
| Course dependencies | Graph + Topological Sort |
| Median of two sorted arrays | Binary Search + Partition Position |

Difficulty can generally be understood as follows:

- **Easy**: Direct application of one core concept.
- **Medium**: A variation of one core concept, or a combination of two concepts.
- **Hard**: A less obvious starting point, a combination of multiple concepts, or complex edge cases.

---

## Quick Problem-Pattern Recognition Table

| Problem Characteristic | Consider First |
| --- | --- |
| Finding duplicates, pairing values, counting frequencies | HashMap / HashSet |
| Sorted array, two-value pairing | Two Pointers |
| Longest or shortest contiguous range | Sliding Window |
| Contiguous range sum with negative numbers | Prefix Sum |
| Sorted data or a monotonic boundary in the answer space | Binary Search |
| Overlapping intervals | Sorting |
| Parentheses, nesting, undo operations | Stack |
| Next greater or smaller element | Monotonic Stack |
| Maximum or minimum value in a window | Monotonic Queue |
| Top K largest or smallest elements | Heap |
| Linked-list cycle or midpoint | Fast and Slow Pointers |
| All combinations or permutations | Backtracking |
| Tree paths or depth | Tree DFS |
| Tree levels | Tree BFS |
| Graph connectivity | Graph DFS / BFS |
| Shortest path when every step has the same cost | BFS |
| Precedence dependencies | Topological Sort |
| Dynamically merging groups | Union-Find |
| A locally optimal choice that never needs to be reversed | Greedy |
| Optimum values, number of ways, repeated subproblems | Dynamic Programming |
| String prefixes | Trie |
| Binary states, cancellation of paired values | Bit Manipulation |

---

## Recommended Practice Order

In the first stage, master the basic patterns for arrays and strings:

```text
HashMap
→ Two Pointers
→ Sliding Window
→ Prefix Sum
→ Binary Search
→ Sorting and Intervals
→ Stack
```

In the second stage, learn structural traversal and search:

```text
Tree DFS / BFS
→ Graph DFS / BFS
→ Backtracking
→ Heap
→ Topological Sort
→ Union-Find
```

In the third stage, learn optimization techniques and advanced structures:

```text
Greedy
→ Dynamic Programming
→ Monotonic Stack / Monotonic Queue
→ Trie
→ Bit Manipulation
```

Recommended first set of practice problems:

1. Two Sum
2. Contains Duplicate
3. Two Sum in a Sorted Array
4. Valid Palindrome
5. Longest Substring Without Repeating Characters
6. Maximum Sum of a Fixed-Length Subarray
7. Longest Subarray With Sum Equal to K
8. Basic Binary Search
9. Merge Intervals
10. Valid Parentheses

On the first pass, the goal is not to memorize templates, but to be able to answer these questions after seeing a problem:

> What characteristics does this problem have? Which core concept is best suited to eliminating repeated work?
