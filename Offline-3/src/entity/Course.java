package entity;

import java.util.ArrayList;
import java.util.Arrays;

public class Course
{
    private String courseId;
    private int totalEnrolled;
    private int saturationDegree;
    private int timeSlot;
    private ArrayList<Course> adjacentCourses;

    public Course(String courseId, int totalEnrolled)
    {
        this.courseId = courseId;
        this.totalEnrolled = totalEnrolled;

        this.timeSlot = -1;
        adjacentCourses = new ArrayList<>();
    }

    public void addAdjacent(Course course) {
        adjacentCourses.add(course);
    }

    public int getDegree() { return adjacentCourses.size(); }

    public void increaseDegree() { this.saturationDegree++; }

    public int getSaturationDegree() { return saturationDegree; }

    public int getTotalEnrolled() { return totalEnrolled; }

    public int getTimeSlot() { return timeSlot; }

    public void setTimeSlot(int timeSlot) { this.timeSlot = timeSlot; }

    public int[] getAdjacentCourseSlots()
    {
        int[] slotOccupied = new int[adjacentCourses.size()];
        for(int i=0; i<adjacentCourses.size(); i++)
            slotOccupied[i] = adjacentCourses.get(i).timeSlot;
        Arrays.sort(slotOccupied);
        return slotOccupied;
    }

    @Override
    public String toString() {
        return courseId + " " + totalEnrolled + " " + timeSlot;
    }
}
