package com.collegeinfo.servlet;

import com.collegeinfo.db.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

@WebServlet("/addDepartment")
public class AddDepartmentServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int deptId = Integer.parseInt(request.getParameter("dept_id"));
        String name = request.getParameter("name");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO Department VALUES (?, ?)");
            ps.setInt(1, deptId);
            ps.setString(2, name);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                out.println("<h3>Department added successfully!</h3>");
                out.println("<a href='inde.html'>Add Student Details</a>");
            } else {
                out.println("<h3>Failed to add department.</h3>");
                out.println("<p>Please <a href='index.html'>Back to DashBoard</a> first.</p>");
            }

        } catch (Exception e) {
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
        }
    }
}
