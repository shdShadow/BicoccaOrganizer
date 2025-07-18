package shdShadow.BicoccaOrganizer.CoursesWindow;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Course {
    public enum ExamStatus{
        PASSED,
        NOT_PASSED,
        STUDYING,
        NOT_DEFINED
    }
    //this is going to be a SHA?
    private String name;
    private String url;
    private ExamStatus examStatus;
    private String customName;

    public String getName() {
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getUrl() {
        return url;
    }
    
    public ExamStatus getExamStatus() {
        return examStatus;
    }
    public void setExamStatus(ExamStatus examStatus) {
        this.examStatus = examStatus;
    }
    public String getCustomName() {
        return customName;
    }
    public void setCustomName(String customName){
        this.customName = customName;
    }
    public Course(String name, String url){
        this.name  = name;
        this.url = url;
        this.examStatus = ExamStatus.NOT_DEFINED;
        this.customName = "";
    }
}
