import java.util.*;

class DFS {
    
    static class Edge {
        private int from, to, cost;
        
        public Edge(int from, int to, int cost) {
            this.from = from;
            this.to = to;
            this.cost = cost;
        }
    }



    private static int dfsIterative(Map<Integer, List<Edge>> graph, int start, int n) {
        int count = 0;
        boolean[] visited = new boolean[n];
        Stack<Integer> stack = new Stack<>();
        stack.push(start);

        while(!stack.isEmpty()) {
            int node = stack.pop();
            
            if(!visited[node]) {
                ++count;
                visited[node] = true;
                List<Edge> edges = graph.get(node);
                if(edges != null) {
                    for(Edge adj : graph.get(node)) {
                        if(!visited[adj.to])
                            stack.push(adj.to);
                    }
                }
            }
        }

        return count;
    }


    private static int dfsRecursive(Map<Integer, List<Edge>> graph, boolean[] visited, int start) {
        
        if(visited[start])
            return 0;
        
        visited[start] = true;
        int count = 1;

        List<Edge> edges = graph.get(start);

        if(edges != null) {
            for(Edge edge : edges) 
                count += dfsRecursive(graph, visited, edge.to);
        }

        return count;
    }


    private static void addDirectedEdge(Map<Integer, List<Edge>> graph, int from, int to, int cost) {
        
        List<Edge> edges = graph.get(from);

        if(edges == null)
            graph.put(from, new ArrayList<Edge>());
        
        graph.get(from).add(new Edge(from, to, cost));
    }
    
    public static void main(String[] args) {

        int numOfVertices = 5;
        int startVertex = 0;
        Map<Integer, List<Edge>> graph = new HashMap<>();
        addDirectedEdge(graph, 0, 1, 4);
        addDirectedEdge(graph, 0, 2, 5);
        addDirectedEdge(graph, 1, 2, -2);
        addDirectedEdge(graph, 1, 3, 7);
        addDirectedEdge(graph, 2, 3, 5);
        addDirectedEdge(graph, 2, 2, 1); // Self loop

        int count = DFS.dfsIterative(graph, startVertex, numOfVertices);
        System.out.println("all vertices from node: " + startVertex + " --> " + count);


        int count2 = DFS.dfsRecursive(graph, new boolean[numOfVertices], 2);
        System.out.println("all vertices from node: " + 2 + " --> " + count2);
    }
}

/*
OUTPUT
---------------------------------
all vertices from node: 0 --> 4
all vertices from node: 2 --> 2
*/