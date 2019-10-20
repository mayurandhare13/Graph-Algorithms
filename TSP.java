/* TSP using Dynamic Programming
time complexity O(n^2 * 2^n)
Credit to William Fiset - Travelling Salesman Problem | Dynamic Programming | Graph Theory (https://www.youtube.com/watch?v=cY4HiiFHO1o&list=PLDV1Zeh2NRsDGO4--qE8yH72HFL1Km93P&index=17)
*/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class TSP {

    private final int N, start;
    private final double[][] distance;
    private List<Integer> tour = new ArrayList<>();
    private double minTourCost = Double.POSITIVE_INFINITY;
    private boolean isSolved = false;


    public TSP(int start, double[][] distance) {
        N = distance.length;

        if(N != distance[0].length)
            throw new IllegalStateException("Matrix must be Square");
        if(N > 32)
            throw new IllegalArgumentException("too much computation to handle");

        this.start = start;
        this.distance = distance;
    }

    private List<Integer> getTour() {
        if(!isSolved)
            runTSP();
        
        return tour;
    }

    private double getTourCost() {
        if(!isSolved)
            runTSP();
        
        return minTourCost;
    }

    private void runTSP() {

        if(isSolved)
            return;

        //all bits position is 1 means  all vertices are visited
        final int END_STATE = (1 << N) - 1; 
        Double[][] memo = new Double[N][1 << N];


        // setup
        for(int end = 0; end < N; end++) {
            if(end == start)
                continue;
            memo[end][(1 << start) | (1 << end)] = distance[start][end];
            // cost from 0 to 3 == (1 << 0) | (1 << 3) == 1001
            // if 0 is start and 1 to (n-1) is end
        }


        // r -> number of nodes in partial tour
        for(int r = 3; r <= N; r++) {
            for(int subset : combinations(r, N)) {
                if(notIn(start, subset))
                    continue;
                
                for(int next = 0; next < N; next++) {
                    if(next == start || notIn(next, subset))
                        continue;

                    int subsetWithoutNext = subset ^ (1 << next);
                    double minDist = Double.POSITIVE_INFINITY;
                    // to look up for best possible partial tour value when next node is not yet in subset
                    for(int end = 0; end < N; end++) {
                        if(end == start || end == next || notIn(end, subset))
                            continue;
                        double newdist = memo[end][subsetWithoutNext] + distance[end][next];
                        if(newdist < minDist)
                            minDist = newdist;
                    }
                    memo[next][subset] = minDist;
                }
            }
        }


        // calculate min tour cost
        for(int i = 0; i < N; i++) {
            if(i == start)
                continue;
            double tourCost = memo[i][END_STATE] + distance[i][start];
            if(tourCost < minTourCost)
                minTourCost = tourCost;
        }


        // find optimal tour
        int lastIndex = start;
        int state = END_STATE;
        tour.add(start);

        for(int i = 1; i < N; i++) {
            int index = -1;     // index of best next node
            for(int j = 0; j < N; j++) {
                if(j == start || notIn(j, state))
                    continue;
                if(index == -1)
                    index = j;
                
                double prevDist = memo[index][state] + distance[index][lastIndex];
                double newDist = memo[j][state] + distance[j][lastIndex];

                if(newDist < prevDist)
                    index = j;
            }

            tour.add(index);
            // flip bit | as we already select this node for tour
            state = state ^ (1 << index);   
            lastIndex = index;
        }

        tour.add(start);
        Collections.reverse(tour);

        isSolved = true;
    }

/**     
 * combinations(r, N) -> generates `r` bits set to 1 in size N
 * these are meant to represent subset of visited nodes.
 * 
 * @param r number of bits set to 1
 * @param n size of bit set
 * @return combinations(3, 4) -> {(1011), (1110), (1101), (0111)}
 */
    private List<Integer> combinations(int r, int n) {
        List<Integer> subsets = new ArrayList<>();
        combinations(0, 0, r, n, subsets);
        return subsets;
    }

    private void combinations(int set, int at, int r, int n, List<Integer> subsets) {
        int elementsLeftToPick = n - at;
        if(elementsLeftToPick <  r)
            return;
        
        if(r == 0) {
            subsets.add(set);
            return;
        }
        for(int i = at; i < n; i++) {
            set ^= (1 << i);    // set = set | (1 << i)

            combinations(set, i + 1, r - 1, n, subsets);

            set ^= (1 << i);    // set = set & ~(1 << i)
        }
    }

    private boolean notIn(int el, int subset) {
        return ((1 << el) & subset) == 0;
    }


    public static void main(String[] args) {
        // Create adjacency matrix
        int n = 6;
        double[][] distanceMatrix = new double[n][n];
        for (double[] row : distanceMatrix) Arrays.fill(row, 10000);
        distanceMatrix[5][0] = 10;
        distanceMatrix[1][5] = 12;
        distanceMatrix[4][1] = 2;
        distanceMatrix[2][4] = 4;
        distanceMatrix[3][2] = 6;
        distanceMatrix[0][3] = 8;

        int startNode = 4;
        TSP solver = new TSP(startNode, distanceMatrix);

        System.out.println("Tour: " + solver.getTour());

        System.out.println("Tour cost: " + solver.getTourCost());
    }
}


/*
OUTPUT
-------------
Tour: [0, 3, 2, 4, 1, 5, 0]
Tour cost: 42.0
*/