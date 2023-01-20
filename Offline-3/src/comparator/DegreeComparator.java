package comparator;

import entity.Course;
import java.util.Comparator;

public class DegreeComparator implements Comparator<Course> {
    @Override
    public int compare(Course c1, Course c2) {
        return c2.getDegree() - c1.getDegree();
    }
}