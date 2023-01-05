package csp;

import java.util.ArrayList;
import java.util.HashMap;

public class CSP
{
    private ArrayList<Variable> variables;
    private Constraint[] constraints;

    public CSP(int boardSize)
    {
        variables = new ArrayList<>();
        constraints = new Constraint[2*boardSize];

        for(int i=0; i < 2*boardSize; i++)
            constraints[i] = new Constraint(boardSize);
    }

    public void checkVarDom()
    {
        for(Variable var : variables)
        {
            System.out.println("var-print: " + var.getPos().getRow() + " " + var.getPos().getCol());
            var.printDom();
        }
    }

    public void addVariable(Variable v) {
        variables.add(v);
    }

    public void modifyConstraints(int index, Variable v) {
        constraints[index].addVariable(v);
    }

    public void modifyVariableDomain(Position pos, int cellVal)
    {
        //System.out.println("pos: " + pos.getRow() + " " + pos.getCol() + " " + cellVal);
        for(Variable v : variables)
        {
            if(v.getPos().getRow() == pos.getRow() || v.getPos().getCol() == pos.getCol())
                v.removeValue(cellVal);
        }
    }

    public boolean satisfyConstraints(Variable v, HashMap<Variable, Integer> assignment)
    {
        return constraints[v.getPos().getRow()].holds(assignment)
                && constraints[constraints.length / 2 + v.getPos().getCol()].holds(assignment);
    }
}
