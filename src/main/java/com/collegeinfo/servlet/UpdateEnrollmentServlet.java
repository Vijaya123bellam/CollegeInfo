// UpdateEnrollmentServlet.java
package com.collegeinfo.servlet;

import com.collegeinfo.db.DBConnection;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.ServletException;
import java.io.*;
import java.sql.*;

@WebServlet("/updateEnrollment")
public class UpdateEnrollmentServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int enrollmentId = Integer.parseInt(request.getParameter("enrollment_id"));
        String newStudentIdStr = request.getParameter("student_id");
        String newCourseIdStr = request.getParameter("course_id");
        String newSemester = request.getParameter("semester");
        String newGrade = request.getParameter("grade");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection conn = DBConnection.getConnection()) {
            // Fetch existing record
            PreparedStatement fetchStmt = conn.prepareStatement("SELECT * FROM Enrollment WHERE enrollment_id = ?");
            fetchStmt.setInt(1, enrollmentId);
            ResultSet rs = fetchStmt.executeQuery();

            if (!rs.next()) {
                out.println("<h3 style='color:red;'>Enrollment ID " + enrollmentId + " not found.</h3>");
                out.println("<p><a href='index.html'>Back to Dashboard</a></p>");
                return;
            }

            int currentStudentId = rs.getInt("student_id");
            int currentCourseId = rs.getInt("course_id");
            String currentSemester = rs.getString("semester");
            String currentGrade = rs.getString("grade");

            Integer newStudentId = null;
            Integer newCourseId = null;

            boolean sameStudent = true, sameCourse = true;
            if (newStudentIdStr != null && !newStudentIdStr.isEmpty()) {
                newStudentId = Integer.parseInt(newStudentIdStr);
                sameStudent = (newStudentId == currentStudentId);
                if (!sameStudent) {
                    PreparedStatement checkStudent = conn.prepareStatement("SELECT * FROM Student WHERE student_id = ?");
                    checkStudent.setInt(1, newStudentId);
                    ResultSet rsStu = checkStudent.executeQuery();
                    if (!rsStu.next()) {
                        out.println("<h3 style='color:red;'>Student ID " + newStudentId + " not found.</h3>");
                        out.println("<p><a href='index.html'>Back to Dashboard</a></p>");
                        return;
                    }
                }
            }

            if (newCourseIdStr != null && !newCourseIdStr.isEmpty()) {
                newCourseId = Integer.parseInt(newCourseIdStr);
                sameCourse = (newCourseId == currentCourseId);
                if (!sameCourse) {
                    PreparedStatement checkCourse = conn.prepareStatement("SELECT * FROM Course WHERE course_id = ?");
                    checkCourse.setInt(1, newCourseId);
                    ResultSet rsCourse = checkCourse.executeQuery();
                    if (!rsCourse.next()) {
                        out.println("<h3 style='color:red;'>Course ID " + newCourseId + " not found.</h3>");
                        out.println("<p><a href='index.html'>Back to Dashboard</a></p>");
                        return;
                    }
                }
            }

            boolean sameSemester = newSemester == null || newSemester.trim().equalsIgnoreCase(currentSemester);
            boolean sameGrade = newGrade == null || newGrade.trim().equalsIgnoreCase(currentGrade);

            if ((sameStudent || newStudentId == null) &&
                (sameCourse || newCourseId == null) &&
                sameSemester &&
                sameGrade) {
                out.println("<h3 style='color:orange;'>You entered the same values as current. No update made.</h3>");
                out.println("<p><a href='index.html'>Back to Dashboard</a></p>");
                return;
            }

            // Build dynamic SQL
            StringBuilder sql = new StringBuilder("UPDATE Enrollment SET ");
            boolean needComma = false;

            if (newStudentId != null && !sameStudent) {
                sql.append("student_id = ?");
                needComma = true;
            }
            if (newCourseId != null && !sameCourse) {
                if (needComma) sql.append(", ");
                sql.append("course_id = ?");
                needComma = true;
            }
            if (newSemester != null && !sameSemester) {
                if (needComma) sql.append(", ");
                sql.append("semester = ?");
                needComma = true;
            }
            if (newGrade != null && !sameGrade) {
                if (needComma) sql.append(", ");
                sql.append("grade = ?");
            }

            sql.append(" WHERE enrollment_id = ?");

            PreparedStatement updateStmt = conn.prepareStatement(sql.toString());
            int i = 1;
            if (newStudentId != null && !sameStudent) updateStmt.setInt(i++, newStudentId);
            if (newCourseId != null && !sameCourse) updateStmt.setInt(i++, newCourseId);
            if (newSemester != null && !sameSemester) updateStmt.setString(i++, newSemester.trim());
            if (newGrade != null && !sameGrade) updateStmt.setString(i++, newGrade.trim());
            updateStmt.setInt(i, enrollmentId);

            int rows = updateStmt.executeUpdate();

            if (rows > 0) {
                out.println("<h3>Enrollment updated successfully.</h3>");
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
