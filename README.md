
---

# 📌 README 3 — TaskTrekk (Workforce Management API)  

```markdown
# 🗂 TaskTrekk – Workforce Management API  

A workforce task management system that enables managers to create, assign, prioritize, and track tasks for employees.  
Originally developed as a **backend engineer challenge**, refactored into a **production-ready Spring Boot API**.  

---

## 🚀 Features  

- 👥 **Task Assignment & Reassignment** (no duplication, clean cancellation)  
- 📆 **Smart Daily Task View** (shows today’s and ongoing active tasks)  
- ⚡ **Task Priority Levels** (HIGH / MEDIUM / LOW)  
- 💬 **Comments & Activity History** (track lifecycle & team collaboration)  
- 🔐 **Thread-Safe Concurrency** with `ConcurrentHashMap`  
- 🛠 **Logging & Exception Handling** for production readiness  

---

## 🛠 Tech Stack  

- **Backend**: Java, Spring Boot, Gradle  
- **Libraries**: Lombok, MapStruct  
- **Data Store**: ConcurrentHashMap (thread-safe)  
- **Tools**: Postman, Git, IntelliJ  

---

## ⚙️ Setup  

1. Clone the repo:  
   ```bash
   git clone https://github.com/AnoushkaVyas24/TaskTrekk-TaskManagement.git
   cd TaskTrekk-TaskManagement

2. Run the project:
./gradlew bootRun

3. Access endpoints at: http://localhost:8080/api/tasks

---

## 📖 Example API Usage
1. Create a Task:
    POST /api/tasks

  {
    "title": "Visit Client A",
    "assignedTo": "Staff_101",
    "startDate": "2025-09-25",
    "dueDate": "2025-09-28",
    "priority": "HIGH"
  }

2. Fetch Active Tasks:
  GET /api/tasks/active

3. Add a Comment:
  POST /api/tasks/{taskId}/comments

  {
    "author": "Manager_01",
    "comment": "Please prioritize this task"
  }

**🌟 Skills Demonstrated**
-> API design & RESTful principles
-> Debugging & real-world bug fixes
-> Concurrency control with ConcurrentHashMap
-> Feature development (priority, history, comments)
-> Clean project structuring with Spring Boot
