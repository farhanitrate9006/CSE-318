package csp;

import java.util.ArrayList;
import java.util.HashMap;

public class Variable_Order_Heuristic
{
    private final int heuristicType;
    private CSP csp;
    private HashMap<Variable, Integer> assignment;

    public Variable_Order_Heuristic(int heuristicType, CSP csp, HashMap<Variable, Integer> assignment) {
        this.heuristicType = heuristicType;
        this.csp = csp;
        this.assignment = assignment;
    }

    public Variable getNextVariable()
    {
        switch(heuristicType)
        {
            case 1:
                return get_Vah_1(getUnassignedVars());
            case 2:
                return get_Vah_2(getUnassignedVars());
            case 3:
                return get_Vah_3(getUnassignedVars());
            case 4:
                return get_Vah_4(getUnassignedVars());
            case 5:
                return get_Vah_5();
        }

        return null;
    }

    private ArrayList<Variable> getUnassignedVars()
    {
        ArrayList<Variable> unassignedVars = new ArrayList<>();
        for(Variable v : assignment.keySet())
            if(assignment.get(v) == 0)
                unassignedVars.add(v);
        return unassignedVars;
    }

    private Variable get_Vah_1(ArrayList<Variable> unassignedVars)
    {
        int minDomain = Integer.MAX_VALUE;
        Variable selected = null;

        for(Variable v : unassignedVars)
        {
            if(v.getDomainSize() < minDomain)
            {
                minDomain = v.getDomainSize();
                selected = v;
            }
        }

        return selected;
    }

    private Variable get_Vah_2(ArrayList<Variable> unassignedVars)
    {
        int maxForwardDegree = -1;
        Variable selected = null;

        for(Variable v : unassignedVars)
        {
            int tempDegree = csp.forwardDegree(v, assignment);
            if(tempDegree > maxForwardDegree)
            {
                maxForwardDegree = tempDegree;
                selected = v;
            }
        }

        return selected;
    }

    private Variable get_Vah_3(ArrayList<Variable> unassignedVars)
    {
        int minDomain = Integer.MAX_VALUE;
        ArrayList<Variable> selectedVars = new ArrayList<>();

        for(Variable v : unassignedVars)
        {
            if(v.getDomainSize() < minDomain)
            {
                minDomain = v.getDomainSize();
                selectedVars.clear();
                selectedVars.add(v);
            }
            else if(v.getDomainSize() == minDomain)
                selectedVars.add(v);
        }

        if(selectedVars.size() == 1)
            return selectedVars.get(0);
        return get_Vah_2(selectedVars);
    }

    private Variable get_Vah_4(ArrayList<Variable> unassignedVars)
    {
        double minValue = Integer.MAX_VALUE;
        Variable selected = null;

        for(Variable v : unassignedVars)
        {
            double tempValue = (v.getDomainSize() * 1.0)/ (csp.forwardDegree(v, assignment) - 1);
            if(tempValue < minValue)
            {
                minValue = tempValue;
                selected = v;
            }
        }

        return selected;
    }

    private Variable get_Vah_5()
    {
        for(Variable v : assignment.keySet())
            if(assignment.get(v) == 0)
                return v;
        return null;
    }
}