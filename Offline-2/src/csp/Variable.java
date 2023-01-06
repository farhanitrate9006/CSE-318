package csp;

import java.util.ArrayList;
import java.util.Objects;

public class Variable
{
    private Position pos;
    private ArrayList<Integer> domain;
    public ArrayList<Variable> affectedNeighbours;

    public Variable(int row, int col, int maxVal)
    {
        pos = new Position(row, col);
        domain = new ArrayList<>();

        for(int i=1; i<=maxVal; i++)
            domain.add(i);
    }

    public void printDom() {
        System.out.println("domain");
        System.out.println(pos.getRow() + " " + pos.getCol());
        System.out.println(domain);
    }

    public void addValue(int val) { domain.add(val); }

    public void removeValue(int val) { domain.remove(Integer.valueOf(val)); }

    public int getDomainSize() { return domain.size(); }

    public int getDomainValue(int index) { return domain.get(index); }

    public ArrayList<Integer> getDomain() {
        return domain;
    }

    public void setDomain(ArrayList<Integer> domain) {
        this.domain = domain;
    }

    public Position getPos() { return pos; }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Variable variable = (Variable) o;
        return pos.equals(variable.pos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pos);
    }
}
