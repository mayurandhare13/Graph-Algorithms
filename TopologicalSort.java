import java.util.*;

class TopologicalSort {

    public static class Edge {
        int from, to, cost;

        public Edge(int from, int to, int cost) {
            this.from = from;
            this.to = to;
            this.cost = cost;
        }
    }

    static boolean topSortDone = false;
    static int[] order;
    private static int dfs(int i, int at, boolean[] visited, int[] order, List<List<Edge>> graph) {
        
        visited[at] = true;
        List<Edge> edges = graph.get(at);
        if(edges != null) {
            for(Edge edge : edges) {
                if(!visited[edge.to])
                    i = dfs(i, edge.to, visited, order, graph);
            }
        }

        
        order[i] = at;
        return i - 1;
    }


    private static int[] topSort(List<List<Edge>> graph, int numOfNodes) {
        order = new int[numOfNodes];
        boolean[] visited = new boolean[numOfNodes];

        int i = numOfNodes - 1;
        for(int at = 0; at < numOfNodes; at++) {
            if(!visited[at])
                i = dfs(i, at, visited, order, graph);
        }

        topSortDone = true;
        return order;
    }


    // topological sort is to find the shortest path
    // between two nodes in a Directed Acyclic Graph (DAG)
    private static Integer[] dagShortestPath(List<List<Edge>> graph, int numOfNodes, int startVertex) {
        if(!topSortDone)
            order = topSort(graph, numOfNodes);
        
        Integer[] dist = new Integer[numOfNodes];
        dist[startVertex] = 0;

        for(int i = 0; i < numOfNodes; i++) {
            int nodeIndex = order[i];
            if(dist[nodeIndex] != null) {
                List<Edge> edges = graph.get(nodeIndex);
                if(edges != null) {
                    for(Edge edge : edges) {
                        int newDist = dist[nodeIndex] + edge.cost;
                        
                        if(dist[edge.to] == null)
                            dist[edge.to] = newDist;
                        else
                            dist[edge.to] = Math.min(dist[edge.to], newDist);
                    }
                }
            }
        }

        return dist;
    }


    private static List<List<Edge>> initGraph(int n) {
        List<List<Edge>> graph = new ArrayList<>();
        for(int i = 0; i < n; i++)
            graph.add(new ArrayList<Edge>());
        
        return graph;
    }

    private static void addEdge(List<List<Edge>> graph, int from, int to, int cost) {
        graph.get(from).add(new Edge(from, to, cost));
    }
    public static void main(String[] args) {
        
        // Graph setup
        final int n = 7;
        List<List<Edge>> graph = initGraph(n);
        
        addEdge(graph, 0, 1, 3);
        addEdge(graph, 0, 2, 2);
        addEdge(graph, 0, 5, 3);
        addEdge(graph, 1, 3, 1);
        addEdge(graph, 1, 2, 6);
        addEdge(graph, 2, 3, 1);
        addEdge(graph, 2, 4, 10);
        addEdge(graph, 3, 4, 5);
        addEdge(graph, 5, 4, 7);

        int[] order = topSort(graph, n);

        // Output: [6, 0, 5, 1, 2, 3, 4]
        System.out.println(Arrays.toString(order));

        Integer[] distance = dagShortestPath(graph, n, 0);
        
        // shortest path 0 --> 4 == 8 || 0 -> 2 -> 3 -> 4
        System.out.printf("shortest path %d --> %d == %d \n", 0, 4, distance[4]);

        Integer[] distance2 = dagShortestPath(graph, n, 2);
        
        // shortest path 2 --> 4 == 6 || 2 -> 3 -> 4
        System.out.printf("shortest path %d --> %d == %d \n", 2, 4, distance2[4]);


        // No path exists -> Null
        System.out.println(distance2[6]);
    }
}