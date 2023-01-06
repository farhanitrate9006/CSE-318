package csp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class CSP_Solver
{
    private int boardSize;
    private boolean forwardChecking;
    private Variable_Order_Heuristic voh;
    private CSP csp;
    private HashMap<Variable, Integer> assignment;
    //private ArrayList<Variable> affectedNeighbours;
    public static long backtracks = 0;

    public CSP_Solver(int boardSize, boolean forwardChecking, int heuristicChoice, CSP csp, HashMap<Variable, Integer> assignment)
    {
        this.boardSize = boardSize;
        this.forwardChecking = forwardChecking;
        this.csp = csp;
        this.assignment = assignment;
        voh = new Variable_Order_Heuristic(heuristicChoice, csp, assignment);
    }

    public boolean solve()
    {
        if(isAssignmentCompleted())
            return true;

        //System.out.println("hello");
        Variable var = voh.getNextVariable();
        //System.out.println("var: " + var.getPos().getRow() + " " + var.getPos().getCol());
        ArrayList<Integer> prevDomain = (ArrayList<Integer>) var.getDomain().clone();
        //System.out.println("outside loop");

        while(true)
        {
            int val = getNextVal(var);
            //System.out.println("var: " + var.getPos().getRow() + " " + var.getPos().getCol() + " " + val);
            if(val == 0)
            {
                //System.out.println("here 0");
                var.setDomain(prevDomain);
                backtracks++;
                return false;
            }

            assignment.put(var, val);
            if(csp.satisfyConstraints(var, assignment))
            {
                boolean neighboursOkay = csp.checkNeighbours(forwardChecking, var, val);
                if(!neighboursOkay)
                    handleVar(var, val);
                if(!forwardChecking || neighboursOkay)
                {
                    if(solve())
                    {
                        //System.out.println("here 0.5");
                        break;
                    }
                    else
                    {
                        //System.out.println("here 1");
                        handleVar(var, val);
                        for(Variable v : var.affectedNeighbours)
                            v.addValue(val);
                    }
                }
            }
            else
            {
                //System.out.println("here 2");
                handleVar(var, val);
            }
        }

        //System.out.println("here 3");
        return true;
    }

    private void handleVar(Variable var, int val)
    {
        var.removeValue(val);
        assignment.put(var, 0);
        //if(forwardChecking);
    }

    private boolean isAssignmentCompleted()
    {
        for(int i : assignment.values())
            if(i == 0)
                return false;
        return true;
    }

    private int getNextVal(Variable var)
    {
        int minClash = Integer.MAX_VALUE;
        int selectedVal = 0;

        for(int val : var.getDomain())
        {
            int clashesForVal = csp.clashes(var, val);
            if(clashesForVal < minClash)
            {
                minClash = clashesForVal;
                selectedVal = val;
            }
        }

        return selectedVal;
    }
}