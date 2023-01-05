package csp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Constraint
{
    private int boardSize;
    private ArrayList<Variable> scope;

    public Constraint(int boardSize)
    {
        this.boardSize = boardSize;
        scope = new ArrayList<>();
    }

    public void addVariable(Variable v) {
        scope.add(v);
    }

    public boolean holds(HashMap<Variable, Integer> assignment)
    {
        Boolean[] check = new Boolean[boardSize + 1];
        Arrays.fill(check, Boolean.FALSE);

        for(Variable v : assignment.keySet())
        {
            if(scope.contains(v))
            {
                int cellVal = assignment.get(v);
                //System.out.println(v.getPos().getRow() + " " + v.getPos().getCol() + " " + cellVal);
                if(cellVal != 0 && check[cellVal])
                    return false;
                check[cellVal] = true;
            }
        }

        return true;
    }
}
