package shdShadow.BicoccaOrganizer.CoursesWindow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

    public List<Course> getCourses(){
        return Collections.unmodifiableList(courses);
    }

    public void SerializeAcademicYears() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(this);
        System.out.println(json);
    }
}
