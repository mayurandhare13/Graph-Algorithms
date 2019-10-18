import java.util.*;
import java.lang.IllegalArgumentException;

class TarjansSCC {

    private List<List<Integer>> graph;
    private boolean isSolved = false;
    private int n, sccCount, id;
    private int[] ids, lows;
    private boolean[] onStack; 
    private Deque<Integer> stack;
    private final int UNVISITED = -1;

    public TarjansSCC(List<List<Integer>> graph) {
        if(graph == null)
            throw new IllegalArgumentException("Graph cannot be Null");

        this.graph = graph;
        this.n = graph.size();
    }


    private int[] getSCC() {
        if(!isSolved)
            solve();
        
        return lows;
    }


    private void solve() {
        ids = new int[n];           // ids --> ID of vertex
        lows = new int[n];          // lows --> low link of vertex to get SCC
        onStack = new boolean[n];   // if vertex we are visiting is on stack then only get the low link
        stack = new ArrayDeque<>(); // keep track of vertex we are visiting | to avoid cycle
        Arrays.fill(ids, UNVISITED);

        for(int i = 0; i < n; i++) {
            if(ids[i] == UNVISITED)
                dfs(i);
        }

        isSolved = true;
    }


    private void dfs(int at) {
        stack.push(at);
        onStack[at] = true;
        ids[at] = lows[at] = id++;

        for(int to : graph.get(at)) {
            if(ids[to] == UNVISITED)
                dfs(to);
            
            if(onStack[to])
                lows[at] = Math.min(lows[at], lows[to]);
        }

        // On recursive callback, if we're at the root node (start of SCC)
        // empty the seen stack until back to root.

        if(ids[at] == lows[at]) {
            for(int node = stack.pop(); ; node = stack.pop()) {
                onStack[node] = false;
                lows[node] = ids[at];
                
                if(node == at)
                    break;
            }
            ++sccCount;
        }
    }


    // graph with adjacency list
    private static List<List<Integer>> initGraph(int n) {
        List<List<Integer>> graph = new ArrayList<>(n);
        
        for(int i = 0; i < n; i++)
            graph.add(new ArrayList<>());

        return graph;
    }


    // add directed edge `from` --> `to` vertex
    private static void addEdge(List<List<Integer>> graph, int from, int to) {
        graph.get(from).add(to);
    }

    public static void main(String[] args) {
        
        int n = 8;
        List<List<Integer>> graph = initGraph(n);

        addEdge(graph, 6, 0);
        addEdge(graph, 6, 2);
        addEdge(graph, 3, 4);
        addEdge(graph, 6, 4);
        addEdge(graph, 2, 0);
        addEdge(graph, 0, 1);
        addEdge(graph, 4, 5);
        addEdge(graph, 5, 6);
        addEdge(graph, 3, 7);
        addEdge(graph, 7, 5);
        addEdge(graph, 1, 2);
        addEdge(graph, 7, 3);
        addEdge(graph, 5, 0);


        TarjansSCC tarjansSCC = new TarjansSCC(graph);
        int[] scc = tarjansSCC.getSCC();

        System.out.println("number of SCC:- " + tarjansSCC.sccCount);

        Map<Integer, List<Integer>> multiMap = new HashMap<>();

        for(int i = 0; i < n; i++) {
            if(!multiMap.containsKey(scc[i]))
                multiMap.put(scc[i], new ArrayList<>());
            
            multiMap.get(scc[i]).add(i);
        }

        // Strongly Connected Component List
        for(List<Integer> cc : multiMap.values()) 
            System.out.println("Nodes:- " + cc);
    }
}