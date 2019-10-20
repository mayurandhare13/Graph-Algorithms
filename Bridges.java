import java.util.ArrayList;
import java.util.List;

/* Bridge & Articulation Points: weak links, bottleneck or vulnerabilities in graph
 DFS labelling nodes with increasing id as we go.
 Keep track of id and 'low-link' value.
 Bridges will be found at --> id of the edge we are coming from < low link value of edge going to || id(edge.from) < low_link(edge.to)
 Low link value = smallest/lowest id reachable from that node when doing DFS including itself.

 time complexity O(V+E)
*/

class Bridges {

    static class Pair {
        private int node1, node2;
        public Pair(int node1, int node2) {
            this.node1 = node1;
            this.node2 = node2;
        }
    }

    private List<List<Integer>> graph;
    private int n, id;
    private int[] ids, lows;
    private boolean[] visited;
    private boolean isSolved;
    private List<Pair> bridges;


    public Bridges(List<List<Integer>> graph, int n) {
        if(graph == null)
            throw new IllegalArgumentException("Graph cannot be null");
        
        this.graph = graph;
        this.n = n;
    }


    private List<Pair> findBridges() {
        if(isSolved)
            return bridges;
        
        id = 0;
        lows = new int[n];
        ids = new int[n];
        visited = new boolean[n];

        bridges = new ArrayList<>();

        for(int i = 0; i < n; i++) {
            if(!visited[i])
                dfs(i, -1); // -1: parent | their is no prev node
        }

        isSolved = true;
        return bridges;
    }


    private void dfs(int at, int parent) {
        visited[at] = true;
        lows[at] = ids[at] = ++id;

        for(Integer to : graph.get(at)) {
            if(to == parent)    // as this is undirected graph | their is edge to parent node
                continue;
            if(!visited[to]) {
                dfs(to, at);
                lows[at] = Math.min(lows[at], lows[to]); //
                if(ids[at] < lows[to]) {
                    bridges.add(new Pair(at, to));
                }
            }
            else {
                lows[at] = Math.min(lows[at], ids[to]); //
            }
        }
    }


    private static List<List<Integer>> initGraph (int n) {
        List<List<Integer>> graph = new ArrayList<>(n);
        for(int i = 0; i < n; i++)
            graph.add(new ArrayList<>());
        
        return graph;
    }

    // undirected edges
    private static void addEdge(List<List<Integer>> graph, int from, int to) {
        graph.get(from).add(to);
        graph.get(to).add(from);
    }

    public static void main(String[] args) {
        
        int n = 9;
        List<List<Integer>> graph = initGraph(n);

        addEdge(graph, 0, 1);
        addEdge(graph, 0, 2);
        addEdge(graph, 1, 2);
        addEdge(graph, 2, 3);
        addEdge(graph, 3, 4);
        addEdge(graph, 2, 5);
        addEdge(graph, 5, 6);
        addEdge(graph, 6, 7);
        addEdge(graph, 7, 8);
        addEdge(graph, 8, 5);

        Bridges b = new Bridges(graph, n);
        List<Pair> bridges = b.findBridges();

        System.out.println("Bridge between nodes are:- ");
        for(Pair p : bridges) {
            System.out.printf("nodes [%d - %d]\n", p.node1, p.node2);
        }
    }
}


/*
OUTPUT
------------------
Bridge between nodes are:- 
nodes [3 - 4]
nodes [2 - 3]
nodes [2 - 5]
*/