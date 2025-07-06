// DeleteEntityServlet.java
package com.collegeinfo.servlet;

import com.collegeinfo.db.DBConnection;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.ServletException;
import java.io.*;
import java.sql.*;

@WebServlet("/deleteEntity")
public class DeleteEntityServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String entity = request.getParameter("entity");
        int id = Integer.parseInt(request.getParameter("id"));

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection conn = DBConnection.getConnection()) {

            switch (entity) {
                case "department":
                    PreparedStatement stmt1 = conn.prepareStatement("UPDATE Student SET dept_id = NULL WHERE dept_id = ?");
                    stmt1.setInt(1, id);
                    stmt1.executeUpdate();

                    PreparedStatement stmt2 = conn.prepareStatement("UPDATE Faculty SET dept_id = NULL WHERE dept_id = ?");
                    stmt2.setInt(1, id);
                    stmt2.executeUpdate();

                    PreparedStatement stmt3 = conn.prepareStatement("UPDATE Course SET dept_id = NULL WHERE dept_id = ?");
                    stmt3.setInt(1, id);
                    stmt3.executeUpdate();

                    PreparedStatement deleteDept = conn.prepareStatement("DELETE FROM Department WHERE dept_id = ?");
                    deleteDept.setInt(1, id);
                    executeAndRespond(deleteDept, entity, id, out);
                    break;

                case "faculty":
                    PreparedStatement stmt4 = conn.prepareStatement("UPDATE Course SET faculty_id = NULL WHERE faculty_id = ?");
                    stmt4.setInt(1, id);
                    stmt4.executeUpdate();

                    PreparedStatement deleteFaculty = conn.prepareStatement("DELETE FROM Faculty WHERE faculty_id = ?");
                    deleteFaculty.setInt(1, id);
                    executeAndRespond(deleteFaculty, entity, id, out);
                    break;

                case "course":
                    PreparedStatement stmt5 = conn.prepareStatement("DELETE FROM Enrollment WHERE course_id = ?");
                    stmt5.setInt(1, id);
                    stmt5.executeUpdate();

                    PreparedStatement deleteCourse = conn.prepareStatement("DELETE FROM Course WHERE course_id = ?");
                    deleteCourse.setInt(1, id);
                    executeAndRespond(deleteCourse, entity, id, out);
                    break;

                case "student":
                    PreparedStatement stmt6 = conn.prepareStatement("DELETE FROM Enrollment WHERE student_id = ?");
                    stmt6.setInt(1, id);
                    stmt6.executeUpdate();

                    PreparedStatement deleteStudent = conn.prepareStatement("DELETE FROM Student WHERE student_id = ?");
                    deleteStudent.setInt(1, id);
                    executeAndRespond(deleteStudent, entity, id, out);
                    break;

                case "enrollment":
                    PreparedStatement deleteEnroll = conn.prepareStatement("DELETE FROM Enrollment WHERE enrollment_id = ?");
                    deleteEnroll.setInt(1, id);
                    executeAndRespond(deleteEnroll, entity, id, out);
                    break;

                default:
                    out.println("<h3 style='color:red;'>Invalid entity selected.</h3>");
                    break;
            }

        } catch (Exception e) {
            out.println("<h3 style='color:red;'>Error: " + e.getMessage() + "</h3>");
        }

        out.println("<p><a href='index.html'>Back to Dashboard</a></p>");
    }

    private void executeAndRespond(PreparedStatement stmt, String entity, int id, PrintWriter out) throws SQLException {
        int rows = stmt.executeUpdate();
        if (rows > 0) {
            out.println("<h3 style='color:green;'>" + entity + " with ID " + id + " deleted successfully.</h3>");
        } else {
            out.println("<h3 style='color:red;'>No record found with ID " + id + ".</h3>");
        }
    }
}
