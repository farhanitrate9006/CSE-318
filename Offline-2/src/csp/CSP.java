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
        constraints = new Constraint[2*boardSize]; // N row, N col => 2N constraint objects

        for (int i = 0; i < 2*boardSize; i++)
            constraints[i] = new Constraint(boardSize);
    }

    public void addVariable(Variable v) {
        variables.add(v);
    }

    public void modifyConstraints(int index, Variable v) {
        constraints[index].addVariable(v);
    }

    public void modifyVariableDomain(Position pos, int cellVal)
    {
        for (Variable v : variables)
            if (v.getPos().getRow() == pos.getRow() || v.getPos().getCol() == pos.getCol())
                v.removeValue(cellVal);
    }

    public boolean satisfyConstraints(Variable v, int val, HashMap<Variable, Integer> assignment) {
        return constraints[v.getPos().getRow()].holds(val, assignment)
                && constraints[constraints.length / 2 + v.getPos().getCol()].holds(val, assignment);
    }

    // required for vah-2
    public int forwardDegree(Variable v, HashMap<Variable, Integer> assignment) {
        return constraints[v.getPos().getRow()].forwardDegree(assignment)
                + constraints[constraints.length / 2 + v.getPos().getCol()].forwardDegree(assignment);
    }

    // required for domain pruning of neighbours
    public boolean checkNeighbours(boolean forwardChecking, Variable var, int val)
    {
        var.affectedNeighbours = new ArrayList<>();
        return constraints[var.getPos().getRow()].checkNeighbours(forwardChecking, var, val)
                && constraints[constraints.length / 2 + var.getPos().getCol()].checkNeighbours(forwardChecking, var, val);
    }

    // required for value-ordering-heuristic
    public int clashes(Variable var, int val) {
        return constraints[var.getPos().getRow()].clashes(var, val)
                + constraints[constraints.length / 2 + var.getPos().getCol()].clashes(var, val);
    }
}
