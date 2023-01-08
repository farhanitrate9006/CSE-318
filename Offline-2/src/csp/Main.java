package csp;

import java.util.HashMap;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);

        // variables which decides heuristic type & solve method
        final int heuristicType = 1;
        final boolean forwardChecking = true;

        int initialNonEmpty = 0; // non-zero cells of board in given input
        int N = sc.nextInt(); // board dimension

        // instantiating necessary objects
        CSP csp = new CSP(N);
        HashMap<Position, Integer> givenVariables = new HashMap<>();
        HashMap<Variable, Integer> assignment = new HashMap<>();
        int[][] solution = new int[N][N];

        // input taking & processing in nested loop
        for(int i=0; i<N; i++)
        {
            for(int j=0; j<N; j++)
            {
                int cellVal = sc.nextInt(); // value for a particular cell
                if(cellVal == 0)
                {
                    initialNonEmpty++;
                    Variable var = new Variable(i, j, N); // each empty cell will be represented one variable
                    csp.addVariable(var);
                    assignment.put(var, 0); // variable currently contains 0

                    // each cell belongs to one row and one col
                    // each row/col is represented one constraint object
                    // hence each variable will belong to two constraint objects
                    csp.modifyConstraints(i, var);
                    csp.modifyConstraints(N+j, var);
                }
                else {
                    givenVariables.put(new Position(i, j), cellVal); // this list will be used to prune domains of empty cells
                    solution[i][j] = cellVal;
                }
            }
        }

        // domain pruning of empty cells
        for(Position pos : givenVariables.keySet())
            csp.modifyVariableDomain(pos, givenVariables.get(pos));

        // ### Solution begins ### //
        long start = System.nanoTime();
        new CSP_Solver(N, forwardChecking, heuristicType, csp, assignment).solve();

        // filling board
        for(Variable v : assignment.keySet())
            solution[v.getPos().getRow()][v.getPos().getCol()] = assignment.get(v);

        long end = System.nanoTime();
        long time = (long) ((end-start) * Math.pow(10, -6));
        // ### Solution ends ### //

        // printing logs
        long backtracks = CSP_Solver.backtracks;
        long nodes = backtracks + initialNonEmpty + 1;
        System.out.println("#Nodes: " + nodes);
        System.out.println("#BT: " + backtracks);
        System.out.println("Time: " + time + " ms");

        // printing solution
        for(int i=0; i<N; i++)
        {
            for(int j = 0; j < N; j++)
                System.out.print(solution[i][j] + " ");
            System.out.println();
        }
    }
}

/*
3
1 0 3
0 0 0
0 3 1

=====

5
0 4 1 0 5
1 0 0 0 0
3 0 2 0 0
0 3 0 2 1
5 0 0 4 0

2 4 1 3 5
1 2 4 5 3
3 5 2 1 4
4 3 5 2 1
5 1 3 4 2
*/