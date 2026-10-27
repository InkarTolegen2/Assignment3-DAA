# Assignment 3. Maze Escape

Student ID:252971
Name:Ingkar Tolegen
Group:SE-2540
Maze size (rows x columns): 10 * 9

This file is the report. Fill it in as you go. How to start: `START_HERE.md`. The task: `docs/Assignment3.pdf`.

## Program output

The 13 lines your program prints on your data (`java -cp out Main`):

    Broken: 89
    Kept: 72
    Last broken wall: 128
    MST cost: 22855
    Heaviest broken wall: 698
    Path length: 27
    DFS visited: 67
    Dead ends: 26
    Zones: 10
    Largest zone: 15
    Is DAG: 0
    Flow stages: 6
    Fastest route cost: 2

My maze:

![maze](output/maze.png)

## Questions

Answer in your own words, two or three sentences each.

Mission 0. What do find and union do, and what makes them fast in your code?

`find(x)` returns the root of the set of x and, on the way, hangs every cell of the path directly under the root (path compression). `union(a, b)` takes the two roots, returns false if they are equal, otherwise puts the smaller tree under the larger one (union by size) and decreases the number of sets. Both tricks keep the trees almost flat, so one operation costs almost O(1) (inverse Ackermann).

Mission 1. Why is Broken always R*C - 1, and Kept always (R-1)*(C-1)?

At the start there are R*C sets. Every broken wall is a successful union and lowers the number of sets by one; the maze is connected, so we end with one set. That is exactly R*C - 1 unions. The grid has R*(C-1) + (R-1)*C inner walls = 2RC - R - C. Kept = 2RC - R - C - (RC - 1) = RC - R - C + 1 = (R-1)(C-1).

Mission 2. My MST cost from Kruskal and from Prim, and why they are equal:

Kruskal: <MST cost>, Prim: <MST cost> (write your number). All costs are different, so the minimum spanning tree is unique, and both algorithms build it (cut property: the cheapest wall that leaves a group of cells is always in the MST).

Mission 3. Why is there exactly one path between two cells? How did you avoid a stack overflow on a huge maze?

The broken walls connect all cells (so a path exists) and have R*C - 1 edges, so there is no cycle: the maze is a tree. If there were two different paths between two cells, they would make a cycle. DFS is written without recursion: my own array `stack` and an array `dir` with the next direction to try in every cell, so the depth of the walk does not use the call stack.

Mission 4. Why does the second pass of Kosaraju go on the reversed graph? Why do the slides have no cycle exactly when Zones equals the number of cells?

We start the second pass from the cell that finished last in the first pass; it lies in a source zone. On the original graph the DFS would leave this zone and take cells of other zones too. On the reversed graph no slide leaves the zone backwards, so the DFS reaches exactly the cells of this zone. A zone with two or more cells is a cycle, and a cycle always lies inside one zone. So no cycle means every zone has one cell, that is Zones = number of cells, and the other way round.

Mission 5. Why is it enough to go through the zones in the order of their numbers? What does this pass cost?

The numbers are a topological order: every slide between zones goes from a smaller number to a larger one. When we reach zone X, all slides into X come from zones before X, so dist[X] and stages[X] are already final and we only relax the slides that leave X. No priority queue is needed. The cost is O(n + m) (grouping the slides by zone and one pass over them).

## AI use

The syllabus requires you to say which AI tools you used and for what. Write the tool (ChatGPT, Copilot, other) and what you asked it: explain an idea, find an error, review code. Write "none" if you did not use any. If you took code or text from a book or a website, give the source here.

## Timing (Mission 7)

Milliseconds, 5 runs each, from `java -cp out Bench`.

| maze | Kruskal | Prim |
|---|---|---|
| 100 x 100 | 26 8 3 5 3 avg 9| 22 7 10 6 5 avg 10|
| 300 x 300 |  56 27 27 28 25  avg 33| 82 74 83 88 96  avg 85|
| 1000 x 1000 | 476 534 408 480 464  avg 472| 2391 2522 2581 2574 2338  avg 2481|

What do the numbers say about how the time grows? Two or three sentences.

## Complexity

n is the number of cells, m the number of walls (or slides). Fill in every algorithm you wrote.

| algorithm | time | space | assumptions |
|---|---|---|---|
| DSU find and union | O(alpha(n)) per operation, almost O(1) | O(n) | union by size + path compression |
| Kruskal | O(m log m) | O(n + m) | sorting the walls dominates; DSU is almost O(1) |
| Prim | O(m log m) | O(n + m) | binary heap; walls are pushed at most twice |
| DFS | O(n) | O(n) | the maze is a tree with n - 1 edges; iterative with an own stack |
| Kosaraju | O(n + m) | O(n + m) | two DFS passes, adjacency lists |
| Route in the zone graph | O(n + m) | O(n + m) | one pass in topological order, no priority queue |

## Before you submit

- data/walls.txt, data/slides.txt and output/maze.png are in the repository.
- `java tools/Check.java` shows all examples right.
- This README is filled in, the program output and the section AI use included.
- The work is pushed to main (at least one commit per mission) and the repository is public.
- The link is attached in Moodle.
