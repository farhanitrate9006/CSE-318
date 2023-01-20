package main;

import entity.Course;
import entity.Student;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main
{
    private static String PATH_TO_DATASET = "dataset";
    private static String FILE_TO_PROCESS = "yor-f-83";
    private static ArrayList<Course> courses = new ArrayList<>();
    private static ArrayList<Student> students = new ArrayList<>();

    public static void main(String[] args) throws IOException
    {
        processCourseFile(FILE_TO_PROCESS);
        processStudentFile(FILE_TO_PROCESS);
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
            student.addCourse(courses.get(lastCourseIndex));
            students.add(student);
        }
    }
}
