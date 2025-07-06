package com.collegeinfo.servlet;

import com.collegeinfo.db.DBConnection;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.ServletException;
import java.io.*;
import java.sql.*;

@WebServlet("/searchData")
public class SearchServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String searchType = request.getParameter("searchType");
        String value = request.getParameter("searchValue");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection conn = DBConnection.getConnection()) {

            out.println("<h2>Search Results</h2>");
            out.println("<table border='1' cellpadding='8'>");

            switch (searchType) {

                case "student_id":
                    PreparedStatement studentStmt = conn.prepareStatement(
                        "SELECT s.student_id, s.name, s.email, d.name AS dept_name, e.semester, e.grade " +
                        "FROM Student s " +
                        "LEFT JOIN Department d ON s.dept_id = d.dept_id " +
                        "LEFT JOIN Enrollment e ON s.student_id = e.student_id " +
                        "WHERE s.student_id = ?"
                    );
                    studentStmt.setInt(1, Integer.parseInt(value));
                    ResultSet rsStudent = studentStmt.executeQuery();

                    out.println("<tr><th>ID</th><th>Name</th><th>Email</th><th>Department</th><th>Semester</th><th>Grade</th></tr>");
                    while (rsStudent.next()) {
                        out.println("<tr><td>" + rsStudent.getInt("student_id") +
                                "</td><td>" + rsStudent.getString("name") +
                                "</td><td>" + rsStudent.getString("email") +
                                "</td><td>" + rsStudent.getString("dept_name") +
                                "</td><td>" + rsStudent.getString("semester") +
                                "</td><td>" + rsStudent.getString("grade") + "</td></tr>");
                    }
                    break;

                case "dept_id":
                    PreparedStatement deptStmt = conn.prepareStatement(
                        "SELECT * FROM Department WHERE dept_id = ?"
                    );
                    deptStmt.setInt(1, Integer.parseInt(value));
                    ResultSet rsDept = deptStmt.executeQuery();

                    out.println("<tr><th>Dept ID</th><th>Department Name</th></tr>");
                    while (rsDept.next()) {
                        out.println("<tr><td>" + rsDept.getInt("dept_id") +
                                "</td><td>" + rsDept.getString("name") + "</td></tr>");
                    }
                    break;

                case "faculty_id":
                    PreparedStatement facStmt = conn.prepareStatement(
                        "SELECT f.faculty_id, f.name, f.email, d.name AS dept_name " +
                        "FROM Faculty f LEFT JOIN Department d ON f.dept_id = d.dept_id WHERE f.faculty_id = ?"
                    );
                    facStmt.setInt(1, Integer.parseInt(value));
                    ResultSet rsFac = facStmt.executeQuery();

                    out.println("<tr><th>Faculty ID</th><th>Name</th><th>Email</th><th>Department</th></tr>");
                    while (rsFac.next()) {
                        out.println("<tr><td>" + rsFac.getInt("faculty_id") +
                                "</td><td>" + rsFac.getString("name") +
                                "</td><td>" + rsFac.getString("email") +
                                "</td><td>" + rsFac.getString("dept_name") + "</td></tr>");
                    }
                    break;

                case "course_id":
                    PreparedStatement courseStmt = conn.prepareStatement(
                        "SELECT c.course_id, c.course_name, c.credits, d.name AS dept_name, f.name AS faculty_name " +
                        "FROM Course c " +
                        "LEFT JOIN Department d ON c.dept_id = d.dept_id " +
                        "LEFT JOIN Faculty f ON c.faculty_id = f.faculty_id " +
                        "WHERE c.course_id = ?"
                    );
                    courseStmt.setInt(1, Integer.parseInt(value));
                    ResultSet rsCourse = courseStmt.executeQuery();

                    out.println("<tr><th>Course ID</th><th>Name</th><th>Credits</th><th>Department</th><th>Faculty</th></tr>");
                    while (rsCourse.next()) {
                        out.println("<tr><td>" + rsCourse.getInt("course_id") +
                                "</td><td>" + rsCourse.getString("course_name") +
                                "</td><td>" + rsCourse.getInt("credits") +
                                "</td><td>" + rsCourse.getString("dept_name") +
                                "</td><td>" + rsCourse.getString("faculty_name") + "</td></tr>");
                    }
                    break;

                case "enrollment_id":
                    PreparedStatement enrollStmt = conn.prepareStatement(
                        "SELECT e.enrollment_id, s.name AS student_name, c.course_name, e.semester, f.name AS faculty_name " +
                        "FROM Enrollment e " +
                        "LEFT JOIN Student s ON e.student_id = s.student_id " +
                        "LEFT JOIN Course c ON e.course_id = c.course_id " +
                        "LEFT JOIN Faculty f ON c.faculty_id = f.faculty_id " +
                        "WHERE e.enrollment_id = ?"
                    );
                    enrollStmt.setInt(1, Integer.parseInt(value));
                    ResultSet rsEnroll = enrollStmt.executeQuery();

                    out.println("<tr><th>Enroll ID</th><th>Student</th><th>Course</th><th>Semester</th><th>Faculty</th></tr>");
                    while (rsEnroll.next()) {
                        out.println("<tr><td>" + rsEnroll.getInt("enrollment_id") +
                                "</td><td>" + rsEnroll.getString("student_name") +
                                "</td><td>" + rsEnroll.getString("course_name") +
                                "</td><td>" + rsEnroll.getString("semester") +
                                "</td><td>" + rsEnroll.getString("faculty_name") + "</td></tr>");
                    }
                    break;


                default:
                    out.println("<tr><td colspan='5'>Invalid search type.</td></tr>");
            }

            out.println("</table>");
            out.println("<br><a href='index.html'>Back to Dashboard</a>");

        } catch (Exception e) {
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
            out.println("<p><a href='index.html'>Back to Dashboard</a></p>");
            e.printStackTrace();
        }
    }
}
