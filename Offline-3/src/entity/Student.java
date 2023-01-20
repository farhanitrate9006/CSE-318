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

    public int linearPenalty()
    {
        int penalty = 0;
        for(int i=0; i < enrolledCourses.size()-1; i++)
        {
            for(int j = i+1; j < enrolledCourses.size(); j++)
            {
                int gap = Math.abs(enrolledCourses.get(i).getTimeSlot() - enrolledCourses.get(j).getTimeSlot());
                if(gap < 6)
                    penalty += 2*(5-gap);
            }
        }
        return penalty;
    }

    public int expPenalty()
    {
        int penalty = 0;
        for(int i=0; i < enrolledCourses.size()-1; i++)
        {
            for(int j = i+1; j < enrolledCourses.size(); j++)
            {
                int gap = Math.abs(enrolledCourses.get(i).getTimeSlot() - enrolledCourses.get(j).getTimeSlot());
                if(gap < 6)
                    penalty += Math.pow(2, 5-gap);
            }
        }
        return penalty;
    }
}
