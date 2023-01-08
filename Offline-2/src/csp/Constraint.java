package csp;

import java.util.ArrayList;
import java.util.HashMap;

// this class either represents a row or a col
// it will check value consistency (no value shall be duplicated) in that particular row/col
public class Constraint
{
    private final int boardSize;
    private ArrayList<Variable> scope; // variables of that row/col

    public Constraint(int boardSize)
    {
        this.boardSize = boardSize;
        scope = new ArrayList<>();
    }

    public void addVariable(Variable v) {
        scope.add(v);
    }

    public boolean holds(int val, HashMap<Variable, Integer> assignment)
    {
        int existCount = 0;
        for(Variable v : scope)
        {
            if(assignment.get(v) == val)
            {
                if(existCount == 1) // value found twice in that row/col
                {
                    existCount++;
                    break;
                }
                existCount++;
            }
        }
        return existCount == 1;
    }

    // required for vah-2
    public int forwardDegree(HashMap<Variable, Integer> assignment)
    {
        int degree = 0;
        for(Variable v : scope)
        if(assignment.get(v) == 0)
            degree++;
        return degree;
    }

    // required for value-ordering-heuristic
    public int clashes(Variable var, int val)
    {
        int clashCount = 0;
        for(Variable v : scope)
            if(v != var && v.getDomain().contains(val))
                clashCount++;
        return clashCount;
    }

    public boolean checkNeighbours(boolean forwardChecking, Variable var, int val)
    {
        boolean failure = false;

        for(Variable v : scope)
        {
            if(v != var && v.getDomain().contains(val))
            {
                v.removeValue(val);
                var.affectedNeighbours.add(v);
                if(forwardChecking && v.getDomainSize() == 0)
                {
                    failure = true;
                    break;
                }
            }
        }

        if(failure)
        {
            for(Variable v : var.affectedNeighbours)
                v.addValue(val);
            return false;
        }

        return true;
    }
}
