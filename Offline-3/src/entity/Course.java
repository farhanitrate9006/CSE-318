package entity;

import java.util.ArrayList;

public class Course
{
    private String courseId;
    private int totalEnrolled;
    private int saturationDegree;
    private ArrayList<Course> adjacentCourses;

    public Course(String courseId, int totalEnrolled)
    {
        this.courseId = courseId;
        this.totalEnrolled = totalEnrolled;
        adjacentCourses = new ArrayList<>();
    }

    public void addAdjacent(Course course) {
        adjacentCourses.add(course);
    }

    public int getDegree() { return adjacentCourses.size(); }

    public void increaseDegree() { this.saturationDegree++; }

    public int getSaturationDegree() { return saturationDegree; }

    public int getTotalEnrolled() { return totalEnrolled; }

    @Override
    public String toString() {
        return courseId + " " + String.valueOf(totalEnrolled);
    }
}
