import java.util.*;
import java.util.stream.Collectors;

/* Dijkstra's algorithm is Single Source Shortest Path (SSSP) algorithm
for graph with `non-negative` edge weights.
Algorithm acts in Greedy manner to select most promising node
*/


class Dijkstra {

    // Directed Edges
    private static class Edge {
        private int from, to, cost;

        public Edge(int from, int to, int cost) {
            this.from = from;
            this.to = to;
            this.cost = cost;
        }
    } 


    // keep track of next promising node to visit while running Dijkstra
    private static class Node {
        private int id;
        private double val;

        public Node(int id, double val) {
            this.id = id;
            this.val = val;
        }
    }


    // min heap | based on nodes weight/cost to get minimum cost of shortest path
    private Comparator<Node> comparator = new Comparator<Node>() {
        @Override
        public int compare(Node n1, Node n2) {
            return (n1.val - n2.val) > 0 ? +1 : -1;
        }
    };


    private List<List<Edge>> graph;
    private int numOfNodes;
    private Double[] dist; 
    private Integer[] prev;

    public Dijkstra(List<List<Edge>> graph, int n) {
        if(graph == null)
            throw new IllegalArgumentException("Graph cannot be empty");
        
        this.graph = graph;
        this.numOfNodes = n;
    }


    private List<Integer> reconstructPath(int start, int end) {

        List<Integer> path = new ArrayList<>();
        double distance = this.dijkstra(start, end);
        if(distance == Double.POSITIVE_INFINITY)
            return path;
        
        for(Integer at = end; at != null; at = this.prev[at])
            path.add(at);

        Collections.reverse(path);
        return path;
    }



    // set start node dist: 0 and other nodes to +inf
    // similar approach as BFS 
    public double dijkstra(int start, int end) {
        
        boolean[] visited = new boolean[numOfNodes];
        prev = new Integer[this.numOfNodes];
        
        dist = new Double[this.numOfNodes];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        dist[start] = 0.0;

        PriorityQueue<Node> pq = new PriorityQueue<>(2 * this.numOfNodes, this.comparator);
        pq.offer(new Node(start, 0));

        while(!pq.isEmpty()) {
            Node node = pq.poll();
            visited[node.id] = true;
            
            if(dist[node.id] < node.val)
                continue;
            
            List<Edge> edges = this.graph.get(node.id);
            if(edges != null) {
                for(Edge edge : edges) {
                    if(!visited[edge.to]) {
                        double newDist = dist[node.id] + edge.cost;
                        if(newDist < dist[edge.to]) {
                            dist[edge.to] = newDist;
                            prev[edge.to] = node.id;
                            pq.offer(new Node(edge.to, dist[edge.to]));
                        }
                    }
                }
            }

            if(node.id == end)
                return dist[end];
        }

        // Node is not reachable
        return Double.POSITIVE_INFINITY;
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


    private String formatPath(List<Integer> path) {
        return String.join("->", path.stream().map(Object::toString).collect(Collectors.toList()));
    }

    public static void main(String[] args) {
        final int n = 5;
        List<List<Edge>> graph = initGraph(n);
        addEdge(graph, 0, 1, 4);
        addEdge(graph, 0, 2, 1);
        addEdge(graph, 1, 3, 1);
        addEdge(graph, 2, 1, 2);
        addEdge(graph, 2, 3, 5);
        addEdge(graph, 3, 4, 3);

        Dijkstra d = new Dijkstra(graph, n);

        System.out.printf("total cost from %d to %d: %f\n", 0, 4, d.dijkstra(0, 4));

        List<Integer> path = d.reconstructPath(0, 4);
        System.out.printf("Path from %d to %d: %s", 3, 4, d.formatPath(path));
    }
}

/*
OUTPUT
total cost from 0 to 4: 7.000000
Path from 3 to 4: 0->2->1->3->4
*/