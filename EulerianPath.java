/* Eulerian Path is path which travels every edges only once.
Eulerian Circuit is path which starts and ends at same vertex covering all edges once.
-----------------------------------------------------------------------------------------------------
|                   | Eulerian Circuit              | Eulerian Path
-----------------------------------------------------------------------------------------------------
Undirected graph    | every vertex has even degrees | even degrees OR exactly 
                    |                               | 2 vertices has odd degrees
--------------------|-------------------------------|-------------------------------------------------
Directed graph      | every vertec has even         | AT MOST one vertex (outdegree - indegree == 1)
                    | indegrees and outdegrees      | AND one vertex (indegree - outdegree == 1)
                    |                               | AND every other has even indegree and outdegree
--------------------|-------------------------------|-------------------------------------------------
*/


import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class EulerianPath {

    private final int n;
    private int edgeCount;
    private int[] in, out;
    private List<Integer> path;
    private List<List<Integer>> graph;


    public EulerianPath(List<List<Integer>> graph) {
        if(graph == null)
            throw new IllegalArgumentException("graph cannot be null");
        
        this.graph = graph;
        this.n = graph.size();
        this.path = new LinkedList<>();
    }


    private int[] getEulerianPath() {
        setDegrees();
        if(!hasEulerianPath())
            return null;

        dfs(findStartNode());

        if(path.size() != edgeCount + 1)
            return null;
        
        int[] res = new int[this.edgeCount + 1];
        for(int i = 0; !path.isEmpty(); i++)
            res[i] = path.remove(0);

        return res;
    }


    private void dfs(int at) {
        while(out[at] != 0) {
            int next = graph.get(at).get(--out[at]);
            dfs(next);
        }
        this.path.add(0, at);
    }


    private int findStartNode() {
        int startNode = 0;

        for(int i = 0; i < n; i++) {
            if(out[i] - in[i] == 1) {
                startNode = i;
                break;
            }
            else
                startNode = i;
        }

        return startNode;
    }


    private void setDegrees() {
        in = new int[this.n];
        out = new int[this.n];
        edgeCount = 0;

        for(int from = 0; from < n; from++) {
            for(int to : this.graph.get(from)) {
                in[to]++;
                out[from]++;
                edgeCount++;
            }
        }
    }


    private boolean hasEulerianPath() {
        if(edgeCount == 0) { return false; }

        int startNodes = 0;
        int endNodes = 0;
        // AT MOST one vertex has (outdegree - indegree == 1) AND (indegree - outdegree == 1)
        for(int i = 0; i < n; i++) {
            if(out[i] - in[i] > 1 || in[i] - out[i] > 1)
                return false;
            
            else if(out[i] - in[i] == 1)
                startNodes++;
            else if(in[i] - out[i] == 1)
                endNodes++;
        }

        return (startNodes == 0 && endNodes == 0) || (startNodes == 1 && endNodes == 1);
    }

    
    public static List<List<Integer>> initGraph(int n) {
        List<List<Integer>> graph = new ArrayList<>(n);
        for(int i = 0; i < n; i++)
            graph.add(new ArrayList<>());
        
        return graph;
    }

    public static void addEdge(List<List<Integer>> graph, int from, int to) {
        graph.get(from).add(to);
    }

    public static void main(String[] args) {
        int n = 7;
        List<List<Integer>> graph = initGraph(n);

        addEdge(graph, 1, 2);
        addEdge(graph, 1, 3);
        addEdge(graph, 2, 2);
        addEdge(graph, 2, 4);
        addEdge(graph, 2, 4);
        addEdge(graph, 3, 1);
        addEdge(graph, 3, 2);
        addEdge(graph, 3, 5);
        addEdge(graph, 4, 3);
        addEdge(graph, 4, 6);
        addEdge(graph, 5, 6);
        addEdge(graph, 6, 3);

    EulerianPath e = new EulerianPath(graph);

    System.out.println(Arrays.toString(e.getEulerianPath()));
    }
}


/* 
OUTPUT
--------------
[1, 3, 5, 6, 3, 2, 4, 3, 1, 2, 2, 4, 6]
*/