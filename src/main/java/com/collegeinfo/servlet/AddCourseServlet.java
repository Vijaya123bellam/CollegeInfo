package com.collegeinfo.servlet;

import com.collegeinfo.db.DBConnection;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.ServletException;
import java.io.*;
import java.sql.*;

@WebServlet("/addCourse")
public class AddCourseServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int courseId = Integer.parseInt(request.getParameter("course_id"));
        String courseName = request.getParameter("course_name");
        int credits = Integer.parseInt(request.getParameter("credits"));
        int deptId = Integer.parseInt(request.getParameter("dept_id"));
        String facultyIdParam = request.getParameter("faculty_id");

        Integer facultyId = null;
        if (facultyIdParam != null && !facultyIdParam.trim().isEmpty()) {
            facultyId = Integer.parseInt(facultyIdParam);
        }

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection conn = DBConnection.getConnection()) {

            // 1. Check department
            PreparedStatement checkDept = conn.prepareStatement("SELECT * FROM Department WHERE dept_id = ?");
            checkDept.setInt(1, deptId);
            ResultSet rsDept = checkDept.executeQuery();

            if (!rsDept.next()) {
                out.println("<h3>Department ID <b>" + deptId + "</b> not found.</h3>");
                out.println("<p>Please <a href='addDepartment.html'>add the department</a> first.</p>");
                out.println("<p><a href='index.html'>Back to Dashboard</a></p>");
                return;
            }

            // 2. If faculty_id is provided, check faculty
            if (facultyId != null) {
                PreparedStatement checkFaculty = conn.prepareStatement("SELECT * FROM Faculty WHERE faculty_id = ?");
                checkFaculty.setInt(1, facultyId);
                ResultSet rsFac = checkFaculty.executeQuery();

                if (!rsFac.next()) {
                    out.println("<h3>Faculty ID <b>" + facultyId + "</b> not found.</h3>");
                    out.println("<p>Please <a href='addFaculty.html'>add the faculty</a> first.</p>");
                    out.println("<p><a href='index.html'>Back to Dashboard</a></p>");
                    return;
                }
            }

            // 3. Insert course
            String sql = "INSERT INTO Course (course_id, course_name, credits, dept_id, faculty_id) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, courseId);
            ps.setString(2, courseName);
            ps.setInt(3, credits);
            ps.setInt(4, deptId);

            if (facultyId != null) {
                ps.setInt(5, facultyId);
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }

            int rows = ps.executeUpdate();

            if (rows > 0) {
                out.println("<h3>Course added successfully.</h3>");
            } else {
                out.println("<h3>Failed to add course.</h3>");
            }
            out.println("<p><a href='index.html'>Back to Dashboard</a></p>");

        } catch (Exception e) {
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
            out.println("<p><a href='index.html'>Back to Dashboard</a></p>");
            e.printStackTrace();
        }
    }
}
