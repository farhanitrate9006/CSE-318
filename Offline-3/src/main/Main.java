package main;

import entity.Course;
import entity.Student;
import solver.Solver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main
{
    private static final String PATH_TO_DATASET = "dataset";
    private static ArrayList<Course> courses = new ArrayList<>();
    private static ArrayList<Student> students = new ArrayList<>();
    private static final String[] FILE_NAMES = {"car-f-92", "car-s-91", "kfu-s-93", "tre-s-92", "yor-f-83"};

    public static void main(String[] args) throws IOException
    {
        for(int i=0; i<FILE_NAMES.length; i++)
        //for(int i=2; i<3; i++)
        {
            String FILE_TO_PROCESS = FILE_NAMES[i];
            System.out.println("===== " + FILE_TO_PROCESS + " =====");
            processCourseFile(FILE_TO_PROCESS);
            processStudentFile(FILE_TO_PROCESS);
            Solver solver = new Solver(courses, students);
            solver.solveByLargestDegree();
            //for(Course course : courses)
                //System.out.print(course.getTimeSlot() + " ");
            solver.solveByLargestEnrollment();
            solver.solveByRandomOrdering();
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
            Course course = new Course(coursesInfo[0], Integer.parseInt(coursesInfo[1]));
            courses.add(course);
        }
    }

    private static void processStudentFile(String fileName) throws FileNotFoundException
    {
        File studentFile = new File(PATH_TO_DATASET + "\\" + fileName + ".stu");
        Scanner sc = new Scanner(studentFile);
        while(sc.hasNextLine())
        {
            Student student = new Student();
            String[] coursesForThisStudent = sc.nextLine().split(" ");

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

            int lastCourseIndex = Integer.parseInt(coursesForThisStudent[coursesForThisStudent.length - 1]) - 1;
            //System.out.println(lastCourseIndex);
            student.addCourse(courses.get(lastCourseIndex));
            students.add(student);
        }
    }
}

/*
===== car-f-92 =====
For degree: 36
For enrollment: 37
For random: 42
===== car-s-91 =====
For degree: 41
For enrollment: 41
For random: 48
===== kfu-s-93 =====
For degree: 46
For enrollment: 50
For random: 65
===== tre-s-92 =====
For degree: 23
For enrollment: 23
For random: 28
===== yor-f-83 =====
For degree: 24
For enrollment: 23
For random: 27
*/