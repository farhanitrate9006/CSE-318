package csp;

import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int heuristicType = 4;
        int initialNonEmpty = 0;

        int N = sc.nextInt();
        CSP csp = new CSP(N);
        HashMap<Position, Integer> givenVariables = new HashMap<>();
        HashMap<Variable, Integer> assignment = new HashMap<>();
        int[][] solution = new int[N][N];

        for(int i=0; i<N; i++)
        {
            for(int j=0; j<N; j++)
            {
                int cellVal = sc.nextInt();
                if(cellVal == 0)
                {
                    Variable v = new Variable(i, j, N);
                    csp.addVariable(v);
                    csp.modifyConstraints(i, v);
                    csp.modifyConstraints(N+j, v);
                    assignment.put(v, 0);
                    initialNonEmpty++;
                }
                else {
                    givenVariables.put(new Position(i, j), cellVal);
                    solution[i][j] = cellVal;
                }
            }
        }

        //csp.checkVarDom();

        for(Position pos : givenVariables.keySet()) {
            //System.out.println("mao");
            csp.modifyVariableDomain(pos, givenVariables.get(pos));
        }

        //System.out.println("size: " + assignment.size());

        long start = System.nanoTime();
        new CSP_Solver(N, false, heuristicType, csp, assignment).solve();
        for(Variable v : assignment.keySet())
        {
            //System.out.println(v.getPos().getRow() + " " + v.getPos().getCol() + " " + assignment.get(v));
            solution[v.getPos().getRow()][v.getPos().getCol()] = assignment.get(v);
        }
        long end = System.nanoTime();
        long time = (long) ((end-start) * Math.pow(10, -6));
        System.out.println("Time: " + time + " ms");
        long backtracks = CSP_Solver.backtracks;
        long nodes = backtracks + initialNonEmpty;
        System.out.println("#BT: " + backtracks);
        System.out.println("#Nodes: " + nodes);

        //System.out.println("Solution");
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