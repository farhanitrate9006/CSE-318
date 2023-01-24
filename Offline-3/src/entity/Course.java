package entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Course
{
    private final int totalEnrolled;
    private int timeSlot;
    private final ArrayList<Course> adjacentCourses;
    private final HashSet<Integer> diffColoredNeighbours; // required for dsatur algo
    private int dfsStatus; // 0: not visited 1: running 2: visit done

    public Course(int totalEnrolled)
    {
        this.totalEnrolled = totalEnrolled;
        this.timeSlot = -1;
        this.adjacentCourses = new ArrayList<>();
        this.diffColoredNeighbours = new HashSet<>();
        this.dfsStatus = 0;
    }

    public void addAdjacent(Course course) {
        adjacentCourses.add(course);
    }

    public int getDegree() { return adjacentCourses.size(); }

    // required for dsatur tie breaking
    public int getUnassignedNeighbourDegree()
    {
        int count = 0;
        for(Course course : adjacentCourses)
            if(course.timeSlot == -1)
                count++;
        return count;
    }

    public int getTotalEnrolled() { return totalEnrolled; }

    public int getTimeSlot() { return timeSlot; }

    public void setTimeSlot(int timeSlot) { this.timeSlot = timeSlot; }

    // below two methods are required for dsatur algo
    public void addDiffColoredNeighbours(int timeSlot) { diffColoredNeighbours.add(timeSlot); }

    public int getSaturationDegree() { return diffColoredNeighbours.size(); }

    // below two methods are required for kempe chain
    public int getDfsStatus() { return dfsStatus; }

    public void setDfsStatus(int dfsStatus) { this.dfsStatus = dfsStatus; }

    // returning sorted time slots of neighbour courses
    public int[] getAdjacentCourseSlots()
    {
        int[] slotOccupied = new int[adjacentCourses.size()];
        for(int i=0; i<adjacentCourses.size(); i++)
            slotOccupied[i] = adjacentCourses.get(i).timeSlot;
        Arrays.sort(slotOccupied);
        return slotOccupied;
    }

    public ArrayList<Course> getAdjacentCourses() { return adjacentCourses; }
}
