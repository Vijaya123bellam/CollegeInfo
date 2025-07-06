package com.collegeinfo.servlet;

import com.collegeinfo.db.DBConnection;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.ServletException;
import java.io.*;
import java.sql.*;

@WebServlet("/updateDepartment")
public class UpdateDepartmentServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int deptId = Integer.parseInt(request.getParameter("dept_id"));
        String newName = request.getParameter("name").trim();

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection conn = DBConnection.getConnection()) {

            // Check if department exists
            PreparedStatement checkStmt = conn.prepareStatement("SELECT name FROM Department WHERE dept_id = ?");
            checkStmt.setInt(1, deptId);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                out.println("<h3>Department ID " + deptId + " not found.</h3>");
                out.println("<p><a href='index.html'>Back to Dashboard</a></p>");
                return;
            }

            String existingName = rs.getString("name").trim();

            // Check if new name is same as existing
            if (existingName.equalsIgnoreCase(newName)) {
                out.println("<h3 style='color:red;'>You entered the same department name. No changes made.</h3>");
                out.println("<p><a href='index.html'>Back to Dashboard</a></p>");
                return;
            }

            // Perform update
            PreparedStatement updateStmt = conn.prepareStatement("UPDATE Department SET name = ? WHERE dept_id = ?");
            updateStmt.setString(1, newName);
            updateStmt.setInt(2, deptId);

            int rows = updateStmt.executeUpdate();

            if (rows > 0) {
                out.println("<h3>Department name updated successfully from <b>" + existingName + "</b> to <b>" + newName + "</b>.</h3>");
            } else {
                out.println("<h3>Failed to update department.</h3>");
            }

            out.println("<p><a href='index.html'>Back to Dashboard</a></p>");

        } catch (Exception e) {
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
            out.println("<p><a href='index.html'>Back to Dashboard</a></p>");
            e.printStackTrace();
        }
    }
}
