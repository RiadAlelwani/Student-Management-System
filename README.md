\# 📚 Student Management System



A Java-based desktop application for managing students, teachers, courses, enrollments, and academic reports.  

Built with \*\*Swing GUI\*\*, layered architecture (Presentation, Application, Domain, Infrastructure), and \*\*JDBC\*\* for database operations.



---



\## 🎯 Features



\- 🧑‍🎓 Manage Students (Add, Edit, Delete, View)

\- 👨‍🏫 Manage Teachers and Departments

\- 📚 Manage Courses and Semesters

\- 📝 Enroll Students in Courses

\- 📊 Generate Detailed Academic Reports (GPA, registrations, course-teacher reports, etc.)

\- 💾 Persistent storage using JDBC with MySQL or SQLite

\- 🔔 Real-time updates across GUI using Observer Pattern



---



\## 🧱 Project Structure



```bash

SMS/

├── src/

│   ├── presentation/        # Swing GUI components

│   ├── application/         # Service layer (business logic)

│   ├── domain/              # Core entities (Student, Course, Teacher, etc.)

│   ├── infrastructure/      # Observers, Calculators, utilities

│   └── persistence/         # DAOs and database access

├── db/                      # SQL scripts (e.g., student\_mgmt.sql)

├── .gitignore

└── README.md



