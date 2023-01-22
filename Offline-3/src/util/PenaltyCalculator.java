package util;

import entity.Student;

import java.util.ArrayList;

public class PenaltyCalculator
{
    private final int penaltyType;
    private final ArrayList<Student> students;

    public PenaltyCalculator(int penaltyType, ArrayList<Student> students)
    {
        this.penaltyType = penaltyType;
        this.students = students;
    }

    public double avgPenalty()
    {
        switch(penaltyType)
        {
            case 1:
                return avgExpPenalty();
            case 2:
                return avgLinearPenalty();
        }
        return -1;
    }

    private double avgLinearPenalty()
    {
        int totalPenalty = 0;
        for(Student student : students)
            totalPenalty += student.linearPenalty();
        return (totalPenalty * 1.0)/students.size();
    }

    private double avgExpPenalty()
    {
        int totalPenalty = 0;
        for(Student student : students)
            totalPenalty += student.expPenalty();
        return (totalPenalty * 1.0)/students.size();
    }
}
