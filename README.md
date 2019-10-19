# Graph-Algorithms

- [Depth First Search (Iterative and Recursive)](./DFS.java)
- [Breadth First Search](./BFS.java) using BSF, find the shortest path
- [Topological Sort](./TopologicalSort.java) using TopSort, find shortest path from node `u --> v`
- [Dijkstra's Algorithm](./Dijkstra.java) find Shortest Path and cost in Positive weight Directed graph
- [Bellman Ford](./BellmanFord.java) find shortest path in Negative Cycle graph
- [Floyd Warshall](./FloydWarshall.java) All Pairs Shortest Path (APSP)
- [Tarjans Strongly Connected Component](./TarjansSCC.java)

|                            | BFS                | Dijkstra's   | Bellman Ford | Floyd Warshall   |
|----------------------------|--------------------|--------------|--------------|------------------|
| __Complexity__             | O(V+E)             | O(E*log(V))  | O((V+E)log(V)) | O(V<sup>3</sup>) |
| __Recommended Graph Size__ | Large              | Large/Medium | Medium/Small | Small            |
| __Good for APSP__          | only on unweighted | Ok           | Bad          | Yes              |
| __Detect Negative Cycle__  | No                 | No           | Yes          | Yes              |
| __SP on weighted edges__   | Incorrect SP ans   | Best         | Works        | Bad in general   |
| __SP on unweighted edges__ | Best               | Ok           | Bad          | Bad in general   |