package util;

import entity.Course;
import entity.Student;
import java.util.ArrayList;

public class Solver
{
    private ArrayList<Course> courses;
    private ArrayList<Student> students;
    private Heuristic heuristic;
    private final int heuristicType;

    public Solver(int heuristicType, ArrayList<Course> courses, ArrayList<Student> students)
    {
        this.courses = courses;
        this.students = students;
        this.heuristicType = heuristicType;
        this.heuristic = new Heuristic(heuristicType, courses);
    }

    private void resetSlots()
    {
        for(Course course : courses)
            course.setTimeSlot(-1);
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

    private int findSuitableSlot(Course course)
    {
        int i, suitableSlot = 0;
        int[] adjacentCourseSlots = course.getAdjacentCourseSlots();

        for(i=0; i<adjacentCourseSlots.length && adjacentCourseSlots[i] == -1; i++);

        for(; i<adjacentCourseSlots.length; i++)
        {
            if(suitableSlot < adjacentCourseSlots[i])
            {
                course.setTimeSlot(suitableSlot);
                break;
            }
            else if(suitableSlot == adjacentCourseSlots[i])
                suitableSlot++;
        }

        if(course.getTimeSlot() == -1)
            course.setTimeSlot(suitableSlot);

        return suitableSlot;
    }

    public int findTimeTable()
    {
        resetSlots();

        int suitableSlot, totalSlot = 0;
        Course tempCourse;

        for(int i=0; i<courses.size(); i++)
        {
            tempCourse = heuristic.getNextUnassigned();
            suitableSlot = findSuitableSlot(tempCourse);
            if(suitableSlot == totalSlot)
                totalSlot++;
            if(heuristicType == 2)
            {
                ArrayList<Course> adjacentCourses = tempCourse.getAdjacentCourses();
                for(Course course : adjacentCourses)
                    if(course.getTimeSlot() == -1)
                        course.addDiffColoredNeighbours(suitableSlot);
            }
        }

        return totalSlot;
    }
}
