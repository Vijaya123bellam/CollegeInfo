package com.collegeinfo.servlet;

import com.collegeinfo.db.DBConnection;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.ServletException;
import java.io.*;
import java.sql.*;

@WebServlet("/updateCourse")
public class UpdateCourseServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int courseId = Integer.parseInt(request.getParameter("course_id"));
        String newName = request.getParameter("course_name");
        String newCreditsStr = request.getParameter("credits");
        String newDeptIdStr = request.getParameter("dept_id");
        String newFacultyIdStr = request.getParameter("faculty_id");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection conn = DBConnection.getConnection()) {

            // 1. Fetch existing course details
            PreparedStatement fetchStmt = conn.prepareStatement(
                "SELECT course_name, credits, dept_id, faculty_id FROM Course WHERE course_id = ?"
            );
            fetchStmt.setInt(1, courseId);
            ResultSet rs = fetchStmt.executeQuery();

            if (!rs.next()) {
                out.println("<h3 style='color:red;'>Course ID " + courseId + " not found.</h3>");
                out.println("<p><a href='index.html'>Back to Dashboard</a></p>");
                return;
            }

            String currentName = rs.getString("course_name");
            int currentCredits = rs.getInt("credits");
            int currentDeptId = rs.getInt("dept_id");
            int currentFacultyId = rs.getInt("faculty_id");

            // 2. Determine changes
            boolean isSameName = (newName == null || newName.trim().isEmpty() || newName.trim().equalsIgnoreCase(currentName));
            boolean isSameCredits = (newCreditsStr == null || newCreditsStr.trim().isEmpty() || Integer.parseInt(newCreditsStr) == currentCredits);
            boolean isSameDept = true;
            boolean isSameFaculty = true;

            Integer newDeptId = null;
            Integer newFacultyId = null;

            if (newDeptIdStr != null && !newDeptIdStr.trim().isEmpty()) {
                newDeptId = Integer.parseInt(newDeptIdStr);
                isSameDept = newDeptId == currentDeptId;

                if (!isSameDept) {
                    PreparedStatement checkDept = conn.prepareStatement("SELECT * FROM Department WHERE dept_id = ?");
                    checkDept.setInt(1, newDeptId);
                    ResultSet rsDept = checkDept.executeQuery();
                    if (!rsDept.next()) {
                        out.println("<h3 style='color:red;'>Department ID " + newDeptId + " not found in Department table.</h3>");
                        out.println("<p><a href='index.html'>Back to Dashboard</a></p>");
                        return;
                    }
                }
            }

            if (newFacultyIdStr != null && !newFacultyIdStr.trim().isEmpty()) {
                newFacultyId = Integer.parseInt(newFacultyIdStr);
                isSameFaculty = newFacultyId == currentFacultyId;

                if (!isSameFaculty) {
                    PreparedStatement checkFac = conn.prepareStatement("SELECT * FROM Faculty WHERE faculty_id = ?");
                    checkFac.setInt(1, newFacultyId);
                    ResultSet rsFac = checkFac.executeQuery();
                    if (!rsFac.next()) {
                        out.println("<h3 style='color:red;'>Faculty ID " + newFacultyId + " not found in Faculty table.</h3>");
                        out.println("<p><a href='index.html'>Back to Dashboard</a></p>");
                        return;
                    }
                }
            }

            // 3. If all values are the same
            if (isSameName && isSameCredits && isSameDept && isSameFaculty) {
                out.println("<h3 style='color:orange;'>You entered the same values as existing. No update performed.</h3>");
                out.println("<p><a href='index.html'>Back to Dashboard</a></p>");
                return;
            }

            // 4. Build update query dynamically
            StringBuilder sql = new StringBuilder("UPDATE Course SET ");
            boolean comma = false;

            if (!isSameName && newName != null && !newName.trim().isEmpty()) {
                sql.append("course_name = ?");
                comma = true;
            }
            if (!isSameCredits && newCreditsStr != null && !newCreditsStr.trim().isEmpty()) {
                if (comma) sql.append(", ");
                sql.append("credits = ?");
                comma = true;
            }
            if (!isSameDept && newDeptId != null) {
                if (comma) sql.append(", ");
                sql.append("dept_id = ?");
                comma = true;
            }
            if (!isSameFaculty && newFacultyId != null) {
                if (comma) sql.append(", ");
                sql.append("faculty_id = ?");
            }

            sql.append(" WHERE course_id = ?");

            PreparedStatement updateStmt = conn.prepareStatement(sql.toString());

            int i = 1;
            if (!isSameName && newName != null && !newName.trim().isEmpty()) {
                updateStmt.setString(i++, newName.trim());
            }
            if (!isSameCredits && newCreditsStr != null && !newCreditsStr.trim().isEmpty()) {
                updateStmt.setInt(i++, Integer.parseInt(newCreditsStr.trim()));
            }
            if (!isSameDept && newDeptId != null) {
                updateStmt.setInt(i++, newDeptId);
            }
            if (!isSameFaculty && newFacultyId != null) {
                updateStmt.setInt(i++, newFacultyId);
            }

            updateStmt.setInt(i, courseId);

            int rows = updateStmt.executeUpdate();
            if (rows > 0) {
                out.println("<h3>Course updated successfully.</h3>");
            } else {
                out.println("<h3 style='color:red;'>Update failed.</h3>");
            }

            out.println("<p><a href='index.html'>Back to Dashboard</a></p>");

        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h3 style='color:red;'>Error: " + e.getMessage() + "</h3>");
            out.println("<p><a href='index.html'>Back to Dashboard</a></p>");
        }
    }
}
