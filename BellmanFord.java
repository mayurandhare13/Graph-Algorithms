import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/* Bellman Ford algorithm is Single Source Shortest Path (SSSP) algorithm
for graph with `negative` edge weights.
It's not ideal for Single Source Shortest Path(use Dijkstra's). So, use BF only when Dijkstra's algorithm fails
time complexity: O((E+V)*log(V))

1. Set every entry in dist[] to +inf
2. Set dist[start] = 0
3. Relax each edge V-1 times
4. In second pass, if node updates to better value. then set the cost to -inf
*/


class BellmanFord {

    // Directed Edges
    private static class Edge {
        private int from, to, cost;

        public Edge(int from, int to, int cost) {
            this.from = from;
            this.to = to;
            this.cost = cost;
        }
    } 


    private List<List<Edge>> graph;
    private int numOfNodes;
    private Double[] dist; 

    public BellmanFord (List<List<Edge>> graph, int n) {
        if(graph == null)
            throw new IllegalArgumentException("Graph cannot be empty");
        
        this.graph = graph;
        this.numOfNodes = n;
    }


    // set start node dist: 0 and other nodes to +inf
    // similar approach as BFS / Dijkstra
    public Double[] bellmanFord(int start) {
        
        dist = new Double[this.numOfNodes];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        dist[start] = 0.0;


        // apply relaxation
        for(int i = 0; i < numOfNodes - 1; i++) {
            for(List<Edge> edges : graph) {
                for(Edge edge : edges) {
                    if(dist[edge.from] + edge.cost < dist[edge.to])
                        dist[edge.to] = dist[edge.from] + edge.cost;
                }
            }
        }

        // second pass to detect -ve cycle
        for(int i = 0; i < numOfNodes - 1; i++) {
            for(List<Edge> edges : graph) {
                for(Edge edge : edges) {
                    if(dist[edge.from] + edge.cost < dist[edge.to])
                        dist[edge.to] = Double.NEGATIVE_INFINITY;
                }
            }
        }

        return dist;
    }


    private static void addEdge(List<List<Edge>> graph, int from, int to, int cost) {
        graph.get(from).add(new Edge(from, to, cost));
    }


    private static List<List<Edge>> initGraph(int n) {
        List<List<Edge>> graph = new ArrayList<>(n);

        for(int i = 0; i < n; i++)
            graph.add(new ArrayList<Edge>());
        
        return graph;
    }



    public static void main(String[] args) {
        final int V = 9, start = 0;
        List<List<Edge>> graph = initGraph(V);
        addEdge(graph, 0, 1, 1);
        addEdge(graph, 1, 2, 1);
        addEdge(graph, 2, 4, 1);
        addEdge(graph, 4, 3, -3);
        addEdge(graph, 3, 2, 1);
        addEdge(graph, 1, 5, 4);
        addEdge(graph, 1, 6, 4);
        addEdge(graph, 5, 6, 5);
        addEdge(graph, 6, 7, 4);
        addEdge(graph, 5, 7, 3);
        
        BellmanFord bf = new BellmanFord(graph, V);
        Double[] dist = bf.bellmanFord(start);

        for(int i = 0; i < V; i++)
            System.out.printf("Cost: node %d -> %d == %.2f\n", start, i, dist[i]);
    }
}

/*
OUTPUT
------------------------------
Cost: node 0 -> 0 == 0.00
Cost: node 0 -> 1 == 1.00
Cost: node 0 -> 2 == -Infinity
Cost: node 0 -> 3 == -Infinity
Cost: node 0 -> 4 == -Infinity
Cost: node 0 -> 5 == 5.00
Cost: node 0 -> 6 == 5.00
Cost: node 0 -> 7 == 8.00
Cost: node 0 -> 8 == Infinity
*/