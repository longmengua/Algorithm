# Algorithm project rules

## Source of truth

`src/main/java/com/example/algorithm/HackerRankChallengeSolver.java` is the
authoritative exercise contract. Do not replace it with a generic dispatcher,
`Object...` API, a second catalogue, or HackerRank problem-title mappings.

## Challenge format

- Keep exactly 100 public static exercises in `HackerRankChallengeSolver`:
  Q001–Q050 Easy, Q051–Q085 Medium, and Q086–Q100 Hard.
- Keep the existing method names, parameter types, return types, nested
  `ListNode`/`TreeNode` types, and question numbering stable. Changing a
  signature requires updating its matching test in the same change.
- Each exercise JavaDoc must be written in Traditional Chinese and include:
  question number and difficulty, a concise task definition, at least one
  concrete input/output example, and explicit constraints or complexity goals.
- Solutions must use only the Java standard library and target Java 21. Do not
  add an external dependency merely to solve a challenge.
- Preserve the intended side effects: `rotateClockwise`, `reorderList`, and
  `solveSudoku` mutate their supplied structure; all other methods must not
  mutate input unless their JavaDoc explicitly says so.
- Keep `UnsupportedOperationException("TODO Qxxx")` until a question is
  deliberately implemented. Do not return placeholder values to make a test
  appear to pass.

## Test rules

- `src/test/java/com/example/algorithm/HackerRankChallengeSolverTest.java` is
  the only acceptance suite for these exercises.
- It must call the strongly typed solver methods directly; never test a text
  catalogue or a generic id-based router.
- Maintain exactly one representative acceptance test per Q001–Q100, with
  test names containing the corresponding Q number. Add edge-case tests only
  when they do not obscure the 100-question pass-rate total.
- Expected values must come from the JavaDoc example or an unambiguous scenario
  consistent with its contract. Arrays, lists, matrices, linked lists, and
  trees must be compared structurally; linked-list/tree identity must be
  checked when the contract requires it.
- The suite must print `Pass rate: passed/100` after execution and must fail
  until every acceptance test passes. `mvn test` is the command used to check
  progress.
