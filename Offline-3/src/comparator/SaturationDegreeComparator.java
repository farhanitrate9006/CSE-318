package comparator;

import entity.Course;
import java.util.Comparator;

public class SaturationDegreeComparator implements Comparator<Course> {
    @Override
    public int compare(Course course1, Course course2) {
        if(course2.getSaturationDegree() != course1.getSaturationDegree())
            return course2.getSaturationDegree() - course1.getSaturationDegree();
        return course2.getDegree() - course1.getDegree();
    }
}

