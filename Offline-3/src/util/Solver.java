package util;

import entity.Course;
import java.util.ArrayList;

public class Solver
{
    private final ArrayList<Course> courses;
    private final Heuristic heuristic;
    private final int heuristicType;

    public Solver(int heuristicType, ArrayList<Course> courses)
    {
        this.courses = courses;
        this.heuristicType = heuristicType;
        this.heuristic = new Heuristic(heuristicType, courses);
    }

    private void resetSlots()
    {
        for(Course course : courses)
            course.setTimeSlot(-1);
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
            if(heuristicType == 2) // initializing necessary side effect for dsatur algo
            {
                ArrayList<Course> adjacentCourses = tempCourse.getAdjacentCourses();
                for(Course course : adjacentCourses) // the currently assigned slot should affect only unassigned neighbours
                    if(course.getTimeSlot() == -1)
                        course.addDiffColoredNeighbours(suitableSlot);
            }
        }

        return totalSlot;
    }

    private int findSuitableSlot(Course course)
    {
        int i, suitableSlot = 0;
        int[] adjacentCourseSlots = course.getAdjacentCourseSlots(); // getting sorted array of neighbour time slots

        // skipping unassigned neighbours since they do not have any impact on slot selection
        for(i=0; i<adjacentCourseSlots.length && adjacentCourseSlots[i] == -1; i++);

        /* the idea is like this:
         if neighbour time slots are: -1, -1, 1, 2, 4, 6
         I can choose 3/5/7
         choosing 7 may increase total slots which will harm the desire for greediness
         the way I am doing, I will choose slot 3 here */
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
}
