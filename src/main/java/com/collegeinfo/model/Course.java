package com.collegeinfo.model;

public class Course {
    private int courseId;
    private String courseName;
    private int credits;
    private int deptId;
    private Integer facultyId; // Nullable

    public Course() {}

    public Course(int courseId, String courseName, int credits, int deptId, Integer facultyId) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.credits = credits;
        this.deptId = deptId;
        this.facultyId = facultyId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public Integer getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(Integer facultyId) {
        this.facultyId = facultyId;
    }
}
