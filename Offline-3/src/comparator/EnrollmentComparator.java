package comparator;

import entity.Course;
import java.util.Comparator;

public class EnrollmentComparator implements Comparator<Course> {
    @Override
    public int compare(Course c1, Course c2) {
        return c2.getTotalEnrolled() - c1.getTotalEnrolled();
    }
}