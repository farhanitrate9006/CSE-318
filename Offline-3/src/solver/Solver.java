package solver;

import comparator.DegreeComparator;
import comparator.EnrollmentComparator;
import entity.Course;
import entity.Student;

import java.util.ArrayList;
import java.util.Collections;

public class Solver
{
    private ArrayList<Course> courses;
    private ArrayList<Student> students;

    public Solver(ArrayList<Course> courses, ArrayList<Student> students)
    {
        this.courses = courses;
        this.students = students;
    }

    private void resetSlots()
    {
        for(Course course : courses)
            course.setTimeSlot(-1);
    }

    public void solveByLargestDegree()
    {
        resetSlots();
        Collections.sort(courses, new DegreeComparator());
        System.out.println("For degree: " + findTimeTable());
    }

    public void solveByLargestEnrollment()
    {
        resetSlots();
        Collections.sort(courses, new EnrollmentComparator());
        System.out.println("For enrollment: " + findTimeTable());
    }

    public void solveByRandomOrdering()
    {
        resetSlots();
        Collections.shuffle(courses);
        System.out.println("For random: " + findTimeTable());
    }

    private int findTimeTable()
    {
        int totalSlot = 0;
        for(Course course : courses)
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
            {
                if(suitableSlot == totalSlot)
                    suitableSlot = totalSlot++;
                course.setTimeSlot(suitableSlot);
            }
        }

        return totalSlot;
    }
}
