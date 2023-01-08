package csp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class CSP_Solver
{
    private final int boardSize;
    private final boolean forwardChecking;
    public static long backtracks = 0;

    private Variable_Order_Heuristic voh;
    private CSP csp;
    private HashMap<Variable, Integer> assignment;

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

        Variable var = voh.getNextVariable();

        // Saving previous value of domain. If this branch fails, domain will be needed to revert back
        ArrayList<Integer> prevDomain = (ArrayList<Integer>) var.getDomain().clone();

        while(true)
        {
            int val = getNextVal(var);

            if(val == 0) // Domain became null. Hence, invalid assignment... backtracking to parent
            {
                var.setDomain(prevDomain);
                backtracks++;
                return false;
            }

            assignment.put(var, val);
            if(csp.satisfyConstraints(var, val, assignment))
            {
                boolean neighboursOkay = csp.checkNeighbours(forwardChecking, var, val);
                if(!neighboursOkay) // Neighbours are not okay with this particular value. Now, other values of domain need to be checked
                    handleVar(var, val);
                if(!forwardChecking || neighboursOkay)
                {
                    if(solve()) // Successful assignment. Hence, no need to check other values of domain
                        break;
                    else // Branch failed at some point. Hence, invalid assignment. Taking necessary compensation
                    {
                        handleVar(var, val);
                        for(Variable v : var.affectedNeighbours)
                            v.addValue(val);
                    }
                }
            }
            else // Does not satisfy constraints. Hence, invalid assignment
                handleVar(var, val);
        }

        return true;
    }

    // Necessary compensation for invalid assignment
    private void handleVar(Variable var, int val)
    {
        var.removeValue(val); // This val leads to failure. Hence, removing it from domain
        assignment.put(var, 0);
    }

    // If any of the cell still contains zero, assignment is incomplete
    private boolean isAssignmentCompleted()
    {
        for(int i : assignment.values())
            if(i == 0)
                return false;
        return true;
    }

    // least-constraining-value heuristic has been applied
    // the value which makes the minimum clashes with other variables domain, will be selected
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