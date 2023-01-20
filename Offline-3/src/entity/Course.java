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

    @Override
    public String toString() {
        return courseId + " " + String.valueOf(totalEnrolled);
    }
}
