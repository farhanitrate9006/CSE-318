package entity;

import java.util.ArrayList;

public class Student
{
    private ArrayList<Course> enrolledCourses;

    public Student() {
        enrolledCourses = new ArrayList<>();
    }

    public void addCourse(Course course) {
        enrolledCourses.add(course);
    }
}
