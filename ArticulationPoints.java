import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/* Bridge & Articulation Points: weak links, bottleneck or vulnerabilities in graph

Conditions for Articulation Points:
1) Number of outgoing edges > 1
2)  i. if their is a bridge
   ii. if their exists cycle

 time complexity O(V+E)
*/

class ArticulationPoints {


    private List<List<Integer>> graph;
    private int n, id, outEdgeCount;
    private int[] ids, lows;
    private boolean[] visited;
    Set<Integer> articulationPoints;
    private boolean isSolved;


    public ArticulationPoints (List<List<Integer>> graph, int n) {
        if(graph == null)
            throw new IllegalArgumentException("Graph cannot be null");
        
        this.graph = graph;
        this.n = n;
    }


    private Set<Integer> findArticulationPoints() {
        if(isSolved)
            return articulationPoints;
        
        id = 0;
        lows = new int[n];  // low links
        ids = new int[n];   // ids to compare
        visited = new boolean[n];
        articulationPoints = new HashSet<>();

        for(int i = 0; i < n; i++) {
            if(!visited[i]) {
                outEdgeCount = 0;
                dfs(i, i, -1); // -1: parent | their is no prev node
                if(outEdgeCount > 1) 
                    articulationPoints.add(i);
                else if(articulationPoints.contains(i)) 
                    articulationPoints.remove(i);
            }
        }

        isSolved = true;
        return articulationPoints;
    }


    private void dfs(int root, int at, int parent) {
        if(root == parent)
            outEdgeCount++;

        visited[at] = true;
        lows[at] = ids[at] = ++id;

        for(Integer to : graph.get(at)) {
            if(to == parent)    // as this is undirected graph | their is edge to parent node
                continue;
            if(!visited[to]) {
                dfs(root, to, at);
                lows[at] = Math.min(lows[at], lows[to]); //
                
                if(ids[at] < lows[to])              // bridge case
                    articulationPoints.add(at);
                if(ids[at] == lows[to])             // cycle case
                    articulationPoints.add(at);
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
        
        final int n = 9;
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

        ArticulationPoints ap = new ArticulationPoints(graph, n);
        Set<Integer> artPoints = ap.findArticulationPoints();

        System.out.println("Articulation Points are:- " + artPoints);
    }
}


/*
OUTPUT
------------------
Articulation Points are:- [2, 3, 5]
*/