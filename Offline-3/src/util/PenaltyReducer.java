package util;

import entity.Course;
import java.util.ArrayList;
import java.util.Random;

public class PenaltyReducer
{
    private static final int MIN_RUN_KEMPE = 1000;
    private static final int MIN_RUN_PAIR_SWAP = 10000;
    private static final int SEED = 5;
    Random random;

    private final ArrayList<Course> courses;
    private final PenaltyCalculator pc;

    public PenaltyReducer(ArrayList<Course> courses, PenaltyCalculator pc)
    {
        this.courses = courses;
        this.pc = pc;

        this.random = new Random(SEED);
    }
    public void kempeChain()
    {
        for(int i=0; i<MIN_RUN_KEMPE; i++)
            kempeChainUtil();
    }

    private void kempeChainUtil()
    {
        double penalty = pc.avgPenalty();

        Course current = courses.get(random.nextInt(courses.size()));
        int currentSlot = current.getTimeSlot();
        ArrayList<Course> adjacentCourses = current.getAdjacentCourses();
        int size = adjacentCourses.size();
        if(size == 0)
            return;
        int neighbourSlot = adjacentCourses.get(random.nextInt(size)).getTimeSlot();
        dfs(current, neighbourSlot);

        kempeChainSlotSwap(currentSlot, neighbourSlot);
        double changedPenalty = pc.avgPenalty();
        if(changedPenalty > penalty)
            kempeChainSlotSwap(currentSlot, neighbourSlot);

        // resetting
        for(Course course : courses)
            if(course.getDfsStatus() == 2)
                course.setDfsStatus(0);
    }

    private void dfs(Course current, int neighbourSlot)
    {
        current.setDfsStatus(1);
        ArrayList<Course> adjacentCourses = current.getAdjacentCourses();
        for(Course adjacent : adjacentCourses)
            if(adjacent.getDfsStatus() == 0 && adjacent.getTimeSlot() == neighbourSlot)
                dfs(adjacent, current.getTimeSlot());
        current.setDfsStatus(2);
    }

    private void kempeChainSlotSwap(int currentSlot, int neighbourSlot)
    {
        for(Course course : courses)
        {
            if(course.getDfsStatus() == 2)
            {
                if(course.getTimeSlot() == currentSlot)
                    course.setTimeSlot(neighbourSlot);
                else
                    course.setTimeSlot(currentSlot);
            }
        }
    }

    public void pairSwap()
    {
        for(int i=0; i<MIN_RUN_PAIR_SWAP; i++)
            pairSwapUtil();
    }

    private void pairSwapUtil()
    {
        Course c1 = courses.get(random.nextInt(courses.size()));
        int firstSlot = c1.getTimeSlot();
        ArrayList<Course> adjacentC1 = c1.getAdjacentCourses();

        Course c2 = courses.get(random.nextInt(courses.size()));
        int secondSlot = c2.getTimeSlot();
        ArrayList<Course> adjacentC2 = c2.getAdjacentCourses();

        for(Course neighbour : adjacentC1)
            if(neighbour.getTimeSlot() == secondSlot)
                return;

        for(Course neighbour : adjacentC2)
            if(neighbour.getTimeSlot() == firstSlot)
                return;

        double penalty = pc.avgPenalty();

        // applying pair swap
        c1.setTimeSlot(secondSlot);
        c2.setTimeSlot(firstSlot);
        double changedPenalty = pc.avgPenalty();
        if(changedPenalty > penalty) // reverting pair swap
        {
            c1.setTimeSlot(firstSlot);
            c2.setTimeSlot(secondSlot);
        }
    }
}
