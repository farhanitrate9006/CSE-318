package comparator;

import entity.Course;
import java.util.Comparator;

public class SaturationDegreeComparator implements Comparator<Course> {
    @Override
    public int compare(Course c1, Course c2) {
        if(c2.getSaturationDegree() != c1.getSaturationDegree())
            return c2.getSaturationDegree() - c1.getSaturationDegree();
        return c2.getUnassignedNeighbourDegree() - c1.getUnassignedNeighbourDegree();
    }
}

