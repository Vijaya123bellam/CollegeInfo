package com.collegeinfo.servlet;

import com.collegeinfo.db.DBConnection;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.ServletException;
import java.io.*;
import java.sql.*;

@WebServlet("/addEnrollment")
public class AddEnrollmentServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int enrollmentId = Integer.parseInt(request.getParameter("enrollment_id"));
        int studentId = Integer.parseInt(request.getParameter("student_id"));
        int courseId = Integer.parseInt(request.getParameter("course_id"));
        String semester = request.getParameter("semester");
        String grade = request.getParameter("grade");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection conn = DBConnection.getConnection()) {

            // Check Student exists
            PreparedStatement checkStudent = conn.prepareStatement("SELECT * FROM Student WHERE student_id = ?");
            checkStudent.setInt(1, studentId);
            ResultSet rsStu = checkStudent.executeQuery();
            if (!rsStu.next()) {
                out.println("<h3>Student ID <b>" + studentId + "</b> not found.</h3>");
                out.println("<p>Please <a href='addStudent.html'>add the student</a> first.</p>");
                out.println("<p><a href='index.html'>Back to Dashboard</a></p>");
                return;
            }

            // Check Course exists
            PreparedStatement checkCourse = conn.prepareStatement("SELECT * FROM Course WHERE course_id = ?");
            checkCourse.setInt(1, courseId);
            ResultSet rsCourse = checkCourse.executeQuery();
            if (!rsCourse.next()) {
                out.println("<h3>Course ID <b>" + courseId + "</b> not found.</h3>");
                out.println("<p>Please <a href='addCourse.html'>add the course</a> first.</p>");
                out.println("<p><a href='index.html'>Back to Dashboard</a></p>");
                return;
            }

            // Insert Enrollment
            PreparedStatement insert = conn.prepareStatement(
                "INSERT INTO Enrollment (enrollment_id, student_id, course_id, semester, grade) VALUES (?, ?, ?, ?, ?)"
            );
            insert.setInt(1, enrollmentId);
            insert.setInt(2, studentId);
            insert.setInt(3, courseId);
            insert.setString(4, semester);
            insert.setString(5, grade);

            int rows = insert.executeUpdate();

            if (rows > 0) {
                out.println("<h3>Enrollment added successfully.</h3>");
            } else {
                out.println("<h3>Failed to add enrollment.</h3>");
            }

            out.println("<p><a href='index.html'>Back to Dashboard</a></p>");

        } catch (Exception e) {
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
            out.println("<p><a href='index.html'>Back to Dashboard</a></p>");
            e.printStackTrace();
        }
    }
}
