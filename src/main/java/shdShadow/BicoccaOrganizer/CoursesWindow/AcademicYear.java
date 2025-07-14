package shdShadow.BicoccaOrganizer.CoursesWindow;

import java.util.ArrayList;
import java.util.List;

public class AcademicYear {
    private String name;
    private List<Course> courses;

    public AcademicYear(String name){
        this.name = name;
        this.courses = new ArrayList<Course>();
    }

    public void addCourse(Course c){
        this.courses.add(c);
    }

    public String getName(){
        return this.name;
    }
}
