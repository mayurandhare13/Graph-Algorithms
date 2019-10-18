import java.util.*;
import java.util.stream.Collectors;

class BFS {

    public static class Edge {
        int from, to, cost;

        public Edge(int from, int to, int cost) {
            this.from = from;
            this.to = to;
            this.cost = cost;
        }
    }

    private int n;
    private List<List<Edge>> graph;
    private Integer[] prev;


    public BFS(List<List<Edge>> graph) {
        if(graph == null)
            throw new IllegalArgumentException("Graph should not be Null");
        this.graph = graph;
        this.n = graph.size();
        prev = new Integer[n];
    }


    private List<Integer> reconstructPath(int start, int end) {
        
        List<Integer> path = new ArrayList<>();

        for(Integer at = end; at != null; at = prev[at]) {
            path.add(at);
        }

        Collections.reverse(path);
        
        if(path.get(0) != start)
            path.clear();
        
        return path;
    }



    // Loop through all edges attached to this node. Mark nodes as visited once they're
    // in the queue. This will prevent having duplicate nodes in the queue and speedup the BFS.
    // In DFS, we first insert into stack, and at the beginning of while we check if it is visited
    // which is same as BFS. as operation is opposite to BFS. So, its actually same. 
    // As `what node` we are going to explore first we visit it then only. 
    private void bfsIterative(int start) {
        boolean[] visited = new boolean[n];
        Deque<Integer> queue = new ArrayDeque<>(n);
        
        queue.offer(start);
        visited[start] = true;

        while(!queue.isEmpty()) {
            int node = queue.poll();
            List<Edge> edges = graph.get(node);
            for(Edge edge : edges) {
                if(!visited[edge.to]) {
                    visited[edge.to] = true;
                    queue.offer(edge.to);
                    prev[edge.to] = node;   // use to reconstruct path | saving parent node for every node
                }
            }
            
        }
    }


    private static List<List<Edge>> initGraph(int n) {
        List<List<Edge>> graph = new ArrayList<>();
        for(int i = 0; i < n; i++)
            graph.add(new ArrayList<>());
        
        return graph;
    }


    private static void addEdge(List<List<Edge>> graph, int from, int to) {
        graph.get(from).add(new Edge(from, to, 1));
    }


    private String formatPath(List<Integer> path) {
        return String.join("->", path.stream().map(Object::toString).collect(Collectors.toList()));
    }


    public static void main(String[] args) {
        final int n = 13;
        List<List<Edge>> graph = initGraph(n);

        addEdge(graph, 0, 7);
        addEdge(graph, 9, 0);
        addEdge(graph, 0, 11);
        addEdge(graph, 7, 11);
        addEdge(graph, 7, 6);
        addEdge(graph, 7, 3);
        addEdge(graph, 6, 5);
        addEdge(graph, 3, 4);
        addEdge(graph, 2, 3);
        addEdge(graph, 2, 12);
        addEdge(graph, 12, 8);
        addEdge(graph, 8, 1);
        addEdge(graph, 1, 10);
        addEdge(graph, 10, 9);
        addEdge(graph, 9, 8);

        BFS bfs = new BFS(graph);
        int start = 10, end = 5;
        
        bfs.bfsIterative(start);
        List<Integer> path = bfs.reconstructPath(10, 5);
        System.out.printf("The shortest path from %d to %d is: [%s]\n", start, end, bfs.formatPath(path));
    }
}

/*
OUTPUT
------------
The shortest path from 10 to 5 is: [10->9->0->7->6->5]
*/