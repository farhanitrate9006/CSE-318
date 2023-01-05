package csp;

import java.util.HashMap;

public class Variable_Order_Heuristic
{
    private final int heuristicType;

    public Variable_Order_Heuristic(int heuristicType) {
        this.heuristicType = heuristicType;
    }

    public Variable getNextVariable(CSP csp, HashMap<Variable, Integer> assignment)
    {
        switch(heuristicType)
        {
            case 5:
                return getRandomVariable(assignment);
        }

        return null;
    }

    public Variable getRandomVariable(HashMap<Variable, Integer> assignment)
    {
        for(Variable v : assignment.keySet())
            if(assignment.get(v) == 0)
                return v;

        return null;
    }
}