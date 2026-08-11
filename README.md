# Algorithm

Java 21 的 Maven 基本專案。

## HackerRank-style practice catalogue

The 100 strongly typed exercises are in
[HackerRankChallengeSolver.java](src/main/java/com/example/algorithm/HackerRankChallengeSolver.java):

- 50 Easy
- 35 Medium
- 15 Hard

Every JavaDoc contains the task, an example, and constraints. Implement the
corresponding method directly; do not change its method signature.

The 100 matching acceptance scenarios are in
[HackerRankChallengeSolverTest.java](src/test/java/com/example/algorithm/HackerRankChallengeSolverTest.java).
It prints the current `Pass rate: passed/100` after every test run.

Project conventions for future AI-assisted edits are in [AGENTS.md](AGENTS.md).

## 指令

```bash
mvn test       # Run the 100 challenge acceptance scenarios
mvn package    # Build the project
mvn exec:java  # Run Hello, World!
```
