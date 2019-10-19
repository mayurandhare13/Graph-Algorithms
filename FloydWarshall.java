import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/* Floyd Warshall algorithm is an All-Pairs Shortest Path (APSP)
time complexity O(V^3) which is ideal for couple hundred nodes.
optimal way to represent graph is Adjacency matrix 2D
*/
class FloydWarshall {

    private int n;
    private double[][] dp;
    private Integer[][] next;
    private boolean isSolved;
    private static final int NEGATIVE_CYCLE = -1;


    public FloydWarshall(double[][] matrix) {
        n = matrix.length;
        dp = new double[n][n];
        next = new Integer[n][n];

        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                if(matrix[i][j] != Double.POSITIVE_INFINITY)
                    next[i][j] = j;                     // for reconstructing path
                dp[i][j] = matrix[i][j];
            }
        }
    }


    private void runFloydWarshall() {
        if(isSolved)
            return;

        for(int k = 0; k < n; k++) {
            for(int i = 0; i < n; i++) {
                for(int j = 0; j < n; j++) {
                    if(dp[i][k] + dp[k][j] < dp[i][j]) {
                        dp[i][j] = dp[i][k] + dp[k][j];     // calculate low cost path
                        next[i][j] = next[i][k];
                    }
                }
            }
        }

        // second pass to detect -ve cycle
        for(int k = 0; k < n; k++) {
            for(int i = 0; i < n; i++) {
                for(int j = 0; j < n; j++) {
                    if(dp[i][k] + dp[k][j] < dp[i][j]) {
                        dp[i][j] = Double.NEGATIVE_INFINITY;
                        next[i][j] = NEGATIVE_CYCLE;
                    }
                }
            }
        }

        isSolved = true;
    }


    private List<Integer> reconstructPath(int start, int end) {
        this.runFloydWarshall();

        List<Integer> path = new ArrayList<>();
        if(dp[start][end] == Double.POSITIVE_INFINITY)
            return path;

        int at = start;
        for(; at != end; at = next[at][end]) {
            if(at == NEGATIVE_CYCLE) {
                path.clear();
                return null;
            }
            path.add(at);
        }

        if(next[at][end] == NEGATIVE_CYCLE) {
            path.clear();
            return null;
        }

        path.add(end);
        return path;
    }


    private static double[][] initGraph(int n) {
        double[][] matrix = new double[n][n];
        for(int i = 0; i < n; i++) {
            Arrays.fill(matrix[i], Double.POSITIVE_INFINITY);
            matrix[i][i] = 0;   // self edge cost = 0
        }

        return matrix;
    }

    public static void main(String[] args) {
        final int n = 7;
        double[][] m = initGraph(n);

        m[0][1] = 2;
        m[0][2] = 5;
        m[0][6] = 10;
        m[1][2] = 2;
        m[1][4] = 11;
        m[2][6] = 2;
        m[6][5] = 11;
        m[4][5] = 1;
        m[5][4] = -2;

        FloydWarshall fw = new FloydWarshall(m); //∞
        fw.runFloydWarshall();

        System.out.println("Shortest Path Costs");
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) 
                System.out.printf("node %d --> %d == %.2f\n", i, j, fw.dp[i][j]);
        } 

        // Reconstructs the shortest paths from all nodes to every other nodes.
        System.out.println("Shortest Path");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                List<Integer> path = fw.reconstructPath(i, j);
                String str;
                if (path == null) {
                    str = ": \u221E solutions! (negative cycle)";
                } else if (path.size() == 0) {
                    str = String.format(": doesn't have route");
                } else {
                    str = String.join( " -> ", path.stream().map(Object::toString).collect(Collectors.toList()));
                str = " : [" + str + "]";
                }

                System.out.printf("node %d to %d %s\n", i, j, str);
            }
        }
    }
}


/*
OUTPUT
---------------------------
Shortest Path Costs
node 0 --> 0 == 0.00
node 0 --> 1 == 2.00
node 0 --> 2 == 4.00
node 0 --> 3 == Infinity
node 0 --> 4 == -Infinity
node 0 --> 5 == -Infinity
node 0 --> 6 == 6.00
node 1 --> 0 == Infinity
node 1 --> 1 == 0.00
node 1 --> 2 == 2.00
node 1 --> 3 == Infinity
node 1 --> 4 == -Infinity
node 1 --> 5 == -Infinity
node 1 --> 6 == 4.00
node 2 --> 0 == Infinity
node 2 --> 1 == Infinity
node 2 --> 2 == 0.00
node 2 --> 3 == Infinity
node 2 --> 4 == -Infinity
node 2 --> 5 == -Infinity
node 2 --> 6 == 2.00
                    ....


Shortest Path
node 0 to 0  : [0]
node 0 to 1  : [0 -> 1]
node 0 to 2  : [0 -> 1 -> 2]
node 0 to 3 : doesn't have route
node 0 to 4 : ∞ solutions! (negative cycle)
node 0 to 5 : ∞ solutions! (negative cycle)
node 0 to 6  : [0 -> 1 -> 2 -> 6]
node 1 to 0 : doesn't have route
node 1 to 1  : [1]
node 1 to 2  : [1 -> 2]
node 1 to 3 : doesn't have route
node 1 to 4 : ∞ solutions! (negative cycle)
node 1 to 5 : ∞ solutions! (negative cycle)
node 1 to 6  : [1 -> 2 -> 6]
node 2 to 0 : doesn't have route
node 2 to 1 : doesn't have route
node 2 to 2  : [2]
node 2 to 3 : doesn't have route
node 2 to 4 : ∞ solutions! (negative cycle)
node 2 to 5 : ∞ solutions! (negative cycle)
node 2 to 6  : [2 -> 6]
                        ...
*/