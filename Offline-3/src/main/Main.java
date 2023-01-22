package main;

import entity.Course;
import entity.Student;
import util.Solver;

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
        {
            String FILE_TO_PROCESS = FILE_NAMES[i];
            System.out.println("===== " + FILE_TO_PROCESS + " =====");
            processCourseFile(FILE_TO_PROCESS);
            processStudentFile(FILE_TO_PROCESS);
            //for(int j=2; j<3; j++)
            for(int j=1; j<5; j++)
            {
                Solver solver = new Solver(j, courses, students);
                System.out.println(solver.findTimeTable());
                //checkConflict();
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

    private static void checkConflict()
    {
        boolean flag = false;
        for(Course course : courses)
        {
            ArrayList<Course> adjacent = course.getAdjacentCourses();
            for(Course c : adjacent)
            {
                if(course.getTimeSlot() == c.getTimeSlot())
                {
                    flag = true;
                    break;
                }
            }
            if(flag)
                break;
        }
        System.out.println(flag);
    }
}

/*
===== car-f-92 =====
34
28
35
43
===== car-s-91 =====
36
31
36
47
===== kfu-s-93 =====
21
20
21
27
===== tre-s-92 =====
22
21
22
29
===== yor-f-83 =====
24
22
23
27
*/