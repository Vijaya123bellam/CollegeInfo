package com.collegeinfo.servlet;

import com.collegeinfo.db.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

@WebServlet("/addStudent")
public class AddStudentServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int studentId = Integer.parseInt(request.getParameter("student_id"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        int deptId = Integer.parseInt(request.getParameter("dept_id"));

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement checkDept = conn.prepareStatement("SELECT * FROM Department WHERE dept_id = ?");
            checkDept.setInt(1, deptId);
            ResultSet rs = checkDept.executeQuery();

            if (!rs.next()) {
                out.println("<h3>Department ID " + deptId + " not found.</h3>");
                out.println("<a href='addDepartment.html'>Add Department First</a>");
                return;
            }

            PreparedStatement ps = conn.prepareStatement("INSERT INTO Student(student_id, name, email, dept_id) VALUES (?, ?, ?, ?)");
            ps.setInt(1, studentId);
            ps.setString(2, name);
            ps.setString(3, email);
            ps.setInt(4, deptId);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                out.println("<h3>Student added successfully!</h3>");
                out.println("<a href='index.html'> Back to DashBoard</a>");
            } else {
                out.println("<h3>Failed to add student.</h3>");
                out.println("<p>Please <a href='index.html'>Back to DashBoard</a> first.</p>");
            }

        } catch (Exception e) {
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
        }
    }
}
