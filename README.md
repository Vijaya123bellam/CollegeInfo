 🎓 CollegeInfo Management System (Java Servlet + JDBC)

A dynamic web application to manage college data such as Students, Faculty, Departments, Courses, and Enrollments using Java Servlets and JDBC. It features full CRUD operations and smart foreign key validation.

---

## 📦 Download & Setup

> 📁 Download the ZIP file of the project and **extract (unzip)** it on your local system.

---

## 🛠 Requirements

- Java JDK 8 or higher
- Apache Tomcat 9 or 10
- MySQL Server
- MySQL Connector JAR (already included in `WEB-INF/lib`)
- Eclipse IDE (Dynamic Web Project)

---

## 🚀 How to Run the Project

### 1. Add Tomcat Server in Eclipse

- Open **Eclipse**
- Go to: `Window → Preferences → Server → Runtime Environments → Add`
- Select: `Apache Tomcat v9.0 or v10.0`
- Set your Tomcat installation directory

---

### 2. Import the Project into Eclipse

- `File → Import → Existing Projects into Workspace`
- Browse to your extracted `CollegeInfo` folder
- Make sure the project is recognized as a **Dynamic Web Project**
- Finish the import

---

### 3. Database Setup

- Open the database.txt file, copy the SQL queries inside it, and paste them into your MySQL database (using Workbench or the MySQL CLI) to execute and create all required tables.

###4. Check DB Credentials
Ensure your DB credentials are correctly set in:

com.collegeinfo.db.DBConnection.java
String url = "jdbc:mysql://localhost:3306/CollegeInfo";
String username = "root";
String password = "password"; // Replace with your MySQL password

#### 5. Run the Project
Right-click the project → Run As → Run on Server
Choose your configured Tomcat Server

Eclipse will deploy the project automatically
🔗 Access the Application
Open browser and go to:
http://localhost:8080/CollegeInfo/index.html

💡 Features
✅ Dynamic Web Application using Java Servlets + HTML + JDBC
✅ Modular CRUD for:
Department
Student
Faculty
Course
Enrollment



![Screenshot 2025-07-08 001204](https://github.com/user-attachments/assets/043f90e9-20ba-419a-b99d-7560d071a268)
