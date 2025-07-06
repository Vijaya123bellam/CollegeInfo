package com.collegeinfo.model;

public class Faculty {
    private int facultyId;
    private String name;
    private String email;
    private int deptId;

    public Faculty() {}

    public Faculty(int facultyId, String name, String email, int deptId) {
        this.facultyId = facultyId;
        this.name = name;
        this.email = email;
        this.deptId = deptId;
    }

    public int getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(int facultyId) {
        this.facultyId = facultyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }
}
