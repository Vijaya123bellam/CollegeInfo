package com.collegeinfo.model;

public class Student {
    private int studentId;
    private String name;
    private String email;
    private int deptId;

    public Student() {}

    public Student(int studentId, String name, String email, int deptId) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.deptId = deptId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
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
