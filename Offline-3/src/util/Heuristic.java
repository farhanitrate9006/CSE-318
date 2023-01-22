package util;

import comparator.DegreeComparator;
import comparator.EnrollmentComparator;
import entity.Course;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Heuristic
{
    private final int heuristicType;
    private ArrayList<Course> courses;
    private int indexToReturn;
    private static final int SEED = 5;

    public Heuristic(int heuristicType, ArrayList<Course> courses)
    {
        this.heuristicType = heuristicType;
        this.courses = courses;
        this.indexToReturn = 0;
        setup();
    }

    private void setup()
    {
        switch(heuristicType)
        {
            case 1:
                Collections.sort(courses, new DegreeComparator());
                break;
            case 3:
                Collections.sort(courses, new EnrollmentComparator());
                break;
            case 4:
                Collections.shuffle(courses, new Random(SEED));
                break;
        }
    }

    public Course getNextUnassigned()
    {
        if(heuristicType == 2)
            return solveBySaturationDegree();
        return courses.get(indexToReturn++);
    }

    public Course solveBySaturationDegree()
    {
        int saturationDegree, unassignedDegree;
        int maxSaturationDegree = -1;
        int maxUnassignedDegree = -1;
        Course courseToReturn = null;

        for(Course course : courses)
        {
            saturationDegree = course.getSaturationDegree();
            unassignedDegree = course.getUnassignedNeighbourDegree();
            if(course.getTimeSlot() == -1 &&
                    (saturationDegree > maxSaturationDegree ||
                        (saturationDegree == maxSaturationDegree && unassignedDegree > maxUnassignedDegree)))
            {
                courseToReturn = course;
                maxSaturationDegree = course.getSaturationDegree();
                maxUnassignedDegree = course.getUnassignedNeighbourDegree();
            }
        }

        return courseToReturn;
    }
}
