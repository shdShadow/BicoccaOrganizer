package shdShadow.BicoccaOrganizer.CoursesWindow;

public class Course {
    private String name;
    private String url;
    private Boolean examPassed;
    private String customName;
    public String getName() {
        return name;
    }
    public String getUrl() {
        return url;
    }
    public Boolean getExamPassed() {
        return examPassed;
    }
    public void setExamPassed(Boolean examPassed) {
        this.examPassed = examPassed;
    }
    public String getCustomName() {
        return customName;
    }
    public void setCustomName(String customName) {
        this.customName = customName;
    }
    public Course(String name, String url){
        this.name  = name;
        this.url = url;
        this.examPassed = false;
        this.customName = "";
    }
}
