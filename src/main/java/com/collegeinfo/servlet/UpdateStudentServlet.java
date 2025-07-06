package com.collegeinfo.servlet;

import com.collegeinfo.db.DBConnection;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.ServletException;
import java.io.*;
import java.sql.*;

@WebServlet("/updateStudent")
public class UpdateStudentServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int studentId = Integer.parseInt(request.getParameter("student_id"));
        String newName = request.getParameter("name");
        String newEmail = request.getParameter("email");
        String deptIdStr = request.getParameter("dept_id");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection conn = DBConnection.getConnection()) {

            // Step 1: Fetch current student data
            PreparedStatement fetchStmt = conn.prepareStatement("SELECT name, email, dept_id FROM Student WHERE student_id = ?");
            fetchStmt.setInt(1, studentId);
            ResultSet rs = fetchStmt.executeQuery();

            if (!rs.next()) {
                out.println("<h3 style='color:red;'>Student ID " + studentId + " not found.</h3>");
                out.println("<p><a href='index.html'>Back to Dashboard</a></p>");
                return;
            }

            String currentName = rs.getString("name");
            String currentEmail = rs.getString("email");
            int currentDeptId = rs.getInt("dept_id");

            // Step 2: Compare values
            boolean sameName = newName == null || newName.trim().equalsIgnoreCase(currentName);
            boolean sameEmail = newEmail == null || newEmail.trim().equalsIgnoreCase(currentEmail);
            boolean sameDept = true;

            Integer newDeptId = null;
            if (deptIdStr != null && !deptIdStr.trim().isEmpty()) {
                newDeptId = Integer.parseInt(deptIdStr.trim());
                sameDept = newDeptId == currentDeptId;

                // Check if dept_id exists
                if (!sameDept) {
                    PreparedStatement checkDept = conn.prepareStatement("SELECT * FROM Department WHERE dept_id = ?");
                    checkDept.setInt(1, newDeptId);
                    ResultSet rsDept = checkDept.executeQuery();
                    if (!rsDept.next()) {
                        out.println("<h3 style='color:red;'>Department ID " + newDeptId + " not found in Department table.</h3>");
                        out.println("<p><a href='addDepartment.html'>Add Department</a> | <a href='index.html'>Back to Dashboard</a></p>");
                        return;
                    }
                }
            }

            // Step 3: Check if all fields are same
            if ((newName == null || newName.isEmpty() || sameName) &&
                (newEmail == null || newEmail.isEmpty() || sameEmail) &&
                (deptIdStr == null || deptIdStr.isEmpty() || sameDept)) {

                out.println("<h3 style='color:orange;'>You entered the same values as current. No update made.</h3>");
                out.println("<p><a href='index.html'>Back to Dashboard</a></p>");
                return;
            }

            // Step 4: Build update query
            StringBuilder sql = new StringBuilder("UPDATE Student SET ");
            boolean hasChanges = false;

            if (newName != null && !newName.trim().isEmpty() && !sameName) {
                sql.append("name = ?");
                hasChanges = true;
            }
            if (newEmail != null && !newEmail.trim().isEmpty() && !sameEmail) {
                if (hasChanges) sql.append(", ");
                sql.append("email = ?");
                hasChanges = true;
            }
            if (newDeptId != null && !sameDept) {
                if (hasChanges) sql.append(", ");
                sql.append("dept_id = ?");
                hasChanges = true;
            }

            sql.append(" WHERE student_id = ?");

            if (!hasChanges) {
                out.println("<h3>No valid fields selected to update.</h3>");
                out.println("<p><a href='index.html'>Back to Dashboard</a></p>");
                return;
            }

            // Step 5: Execute update
            PreparedStatement updateStmt = conn.prepareStatement(sql.toString());
            int i = 1;
            if (newName != null && !newName.trim().isEmpty() && !sameName)
                updateStmt.setString(i++, newName.trim());

            if (newEmail != null && !newEmail.trim().isEmpty() && !sameEmail)
                updateStmt.setString(i++, newEmail.trim());

            if (newDeptId != null && !sameDept)
                updateStmt.setInt(i++, newDeptId);

            updateStmt.setInt(i, studentId);

            int rows = updateStmt.executeUpdate();

            if (rows > 0) {
                out.println("<h3>Student updated successfully.</h3>");
            } else {
                out.println("<h3 style='color:red;'>Update failed.</h3>");
            }

            out.println("<p><a href='index.html'>Back to Dashboard</a></p>");

        } catch (Exception e) {
            out.println("<h3 style='color:red;'>Error: " + e.getMessage() + "</h3>");
            out.println("<p><a href='index.html'>Back to Dashboard</a></p>");
            e.printStackTrace();
        }
    }
}
