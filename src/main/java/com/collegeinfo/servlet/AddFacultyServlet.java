package com.collegeinfo.servlet;

import com.collegeinfo.db.DBConnection;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.ServletException;
import java.io.*;
import java.sql.*;

@WebServlet("/addFaculty")
public class AddFacultyServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int facultyId = Integer.parseInt(request.getParameter("faculty_id"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        int deptId = Integer.parseInt(request.getParameter("dept_id"));

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection conn = DBConnection.getConnection()) {

            // Check if department exists
            PreparedStatement checkDept = conn.prepareStatement("SELECT * FROM Department WHERE dept_id = ?");
            checkDept.setInt(1, deptId);
            ResultSet rs = checkDept.executeQuery();

            if (!rs.next()) {
                out.println("<h3>Department ID <b>" + deptId + "</b> not found.</h3>");
                out.println("<p>Please <a href='addDepartment.html'>add the department</a> first.</p>");
                return;
            }

            // Insert faculty
            PreparedStatement insert = conn.prepareStatement(
                "INSERT INTO Faculty (faculty_id, name, email, dept_id) VALUES (?, ?, ?, ?)"
            );
            insert.setInt(1, facultyId);
            insert.setString(2, name);
            insert.setString(3, email);
            insert.setInt(4, deptId);

            int rows = insert.executeUpdate();
            if (rows > 0) {
                out.println("<h3>Faculty added successfully.</h3>");
                out.println("<p>Please <a href='index.html'>Back to DashBoard</a> first.</p>");
            } else {
                out.println("<h3>Failed to add faculty.</h3>");
                out.println("<p>Please <a href='index.html'>Back to DashBoard</a> first.</p>");
            }

        } catch (Exception e) {
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
            e.printStackTrace();
        }
    }
}
