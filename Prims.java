import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/** Prim's Spanning Tree - Lazy Evaluation - Time complexity O(E*logE)
                        - Eager Evaluation - O(E*logV) using Indexed Priority Queue

    Travels all the vertices only once. 
    In Minimum Spanning Tree => # of Edges == # of vertices - 1
*/


public class Prims {

    static class Edge implements Comparable<Edge> {
        private int from, to, cost;

        public Edge(int from, int to, int cost) {
            this.from = from;
            this.to = to;
            this.cost = cost;
        }

        @Override
        public int compareTo(Edge other) {
            return this.cost - other.cost;
        }
    }

    private int n;
    private boolean mstExists;
    private boolean[] visited;
    private boolean isSolved;
    private List<List<Edge>> graph;
    private PriorityQueue<Edge> pq;

    private Long minCostSum = 0L;
    private Edge[] mstEdges;


    public Prims(List<List<Edge>> graph) {
        if(graph == null)
            throw new IllegalArgumentException("Graph should not be null");
        
        this.graph = graph;
        this.n = graph.size();
    }

    private Edge[] getMST() {
        if(!isSolved)
            solvePrims();

        return mstExists ? mstEdges : null;
    }

    private Long getMinCost() {
        if(!isSolved)
            solvePrims();
        
        return mstExists ? minCostSum : null;
    }

    private void solvePrims() {
        if(isSolved) { return; }

        int m = n - 1, edgeCount = 0;
        pq = new PriorityQueue<>();
        visited = new boolean[n];
        mstEdges = new Edge[m];

        addEdges(0);

        while(!pq.isEmpty() && m != edgeCount) {
            Edge edge = pq.poll();
            int nodeTo = edge.to;
            if(visited[nodeTo])
                continue;
            
            mstEdges[edgeCount++] = edge;
            minCostSum += edge.cost;
            addEdges(nodeTo);
        }

        mstExists = (m == edgeCount);
        isSolved = true;
    }


    private void addEdges(int nodeIndex) {
        visited[nodeIndex] = true;

        List<Edge> edges = this.graph.get(nodeIndex);
        for(Edge e : edges) {
            if(!visited[e.to])
                pq.offer(e);
        }
    }


    // if graph is dense then MUST use adjacency matrix
    public static List<List<Edge>> initGraph(int n) {
        List<List<Edge>> graph = new ArrayList<>(n);
        for(int i = 0; i < n; i++)
            graph.add(new ArrayList<>());

        return graph;
    }


    public static void connectEdge(List<List<Edge>> graph, int from, int to, int cost) {
        graph.get(from).add(new Edge(from, to, cost));
        graph.get(to).add(new Edge(to, from, cost));  // because of undirected graph
    }

    public static void main(String[] args) {
        int n = 7;
        List<List<Edge>> g = initGraph(n);

        connectEdge(g, 0, 1, 9);
        connectEdge(g, 0, 2, 0);
        connectEdge(g, 0, 3, 5);
        connectEdge(g, 0, 5, 7);
        connectEdge(g, 1, 3, -2);
        connectEdge(g, 1, 4, 3);
        connectEdge(g, 1, 6, 4);
        connectEdge(g, 2, 5, 6);
        connectEdge(g, 3, 5, 2);
        connectEdge(g, 3, 6, 3);
        connectEdge(g, 4, 6, 6);
        connectEdge(g, 5, 6, 1);

        Prims solver = new Prims(g);
        Long cost = solver.getMinCost();

        if (cost == null) {
            System.out.println("No MST does not exists");
        } else {
        System.out.println("MST cost: " + cost);
        for (Edge e : solver.getMST()) {
            System.out.println(String.format("from: %d, to: %d, cost: %d", e.from, e.to, e.cost));
        }
        }
    }
}

/** OUTPUT
---------------------------
MST cost: 9
from: 0, to: 2, cost: 0
from: 0, to: 3, cost: 5
from: 3, to: 1, cost: -2
from: 3, to: 5, cost: 2
from: 5, to: 6, cost: 1
from: 1, to: 4, cost: 3
 */