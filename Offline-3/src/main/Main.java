package main;

import entity.Course;
import entity.Student;
import util.PenaltyCalculator;
import util.PenaltyReducer;
import util.Solver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main
{
    private static final String PATH_TO_DATASET = "dataset"; // input files
    private static final int PENALTY_TYPE = 1; // 1: exp 2: linear

    private static final ArrayList<Course> courses = new ArrayList<>();
    private static final ArrayList<Student> students = new ArrayList<>();

    private static final String[] FILE_NAMES = {"car-f-92", "car-s-91", "kfu-s-93", "tre-s-92", "yor-f-83"};
    private static final String[] HEURISTICS = {"degree", "saturation degree", "enrollment", "random"};

    public static void main(String[] args) throws IOException
    {
        for(int i = 4; i < FILE_NAMES.length; i++)
        {
            String FILE_TO_PROCESS = FILE_NAMES[i];
            System.out.println("===== " + FILE_TO_PROCESS + " =====");

            processCourseFile(FILE_TO_PROCESS);
            processStudentFile(FILE_TO_PROCESS);

            PenaltyCalculator pc = new PenaltyCalculator(PENALTY_TYPE, students);
            PenaltyReducer pr = new PenaltyReducer(courses, pc);

            // Calling time table solver and penalty reducer
            for(int j = 1; j <= HEURISTICS.length; j++)
            {
                System.out.println("=== For " + HEURISTICS[j - 1] + " ===");
                System.out.println("Slots: " + new Solver(j, courses).findTimeTable());
                System.out.printf("penalty: %.4f", pc.avgPenalty());
                System.out.println();

                // Applying kempe chain to reduce penalty. However, it will not reduce time slots
                pr.kempeChain();
                System.out.printf("penalty after running kempe chain: %.4f", pc.avgPenalty());
                System.out.println();

                // Applying pair swap to further reduce penalty. However, it will not reduce time slots
                pr.pairSwap();
                System.out.printf("penalty after running pair swap: %.4f", pc.avgPenalty());
                System.out.println();
            }
            courses.clear();
            students.clear();
        }
    }

    private static void processCourseFile(String fileName) throws FileNotFoundException
    {
        File courseFile = new File(PATH_TO_DATASET + "\\" + fileName + ".crs");
        Scanner sc = new Scanner(courseFile);
        while(sc.hasNextLine())
        {
            String[] coursesInfo = sc.nextLine().split(" ");
            Course course = new Course(Integer.parseInt(coursesInfo[1]));
            courses.add(course);
        }
    }

    private static void processStudentFile(String fileName) throws FileNotFoundException
    {
        File studentFile = new File(PATH_TO_DATASET + "\\" + fileName + ".stu");
        Scanner sc = new Scanner(studentFile);
        while(sc.hasNextLine())
        {
            String line = sc.nextLine();
            if(line.equals("")) // skipping empty lines
                continue;

            Student student = new Student();
            String[] coursesForThisStudent = line.split(" ");

            for(int i=0; i < coursesForThisStudent.length-1; i++)
            {
                int courseIndexI = Integer.parseInt(coursesForThisStudent[i]) - 1;
                student.addCourse(courses.get(courseIndexI));

                for(int j = i+1; j < coursesForThisStudent.length; j++)
                {
                    int courseIndexJ = Integer.parseInt(coursesForThisStudent[j]) - 1;
                    courses.get(courseIndexI).addAdjacent(courses.get(courseIndexJ));
                    courses.get(courseIndexJ).addAdjacent(courses.get(courseIndexI));
                }
            }

            // manually adding last course because loop did not cover this
            int lastCourseIndex = Integer.parseInt(coursesForThisStudent[coursesForThisStudent.length - 1]) - 1;
            student.addCourse(courses.get(lastCourseIndex));
            students.add(student);
        }
    }
}