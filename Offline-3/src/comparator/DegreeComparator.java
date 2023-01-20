package comparator;

import entity.Course;
import java.util.Comparator;

public class DegreeComparator implements Comparator<Course> {
    @Override
    public int compare(Course course1, Course course2) {
        return course2.getDegree() - course1.getDegree();
    }
}