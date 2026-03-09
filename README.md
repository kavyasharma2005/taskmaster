# TaskMaster 🚀

A full-stack task management web app with user authentication.

🌐 **Live:** https://taskmaster-k8g1.onrender.com

---

## Features
- 🔐 User registration & login (JWT authentication)
- ✅ Add, complete, and delete tasks
- 👤 Private tasks per user
- 💾 Persistent PostgreSQL database
- 🎨 Beautiful animated dark UI

---

## Tech Stack
| Layer | Technology |
|-------|-----------|
| Backend | Java 17, Spring Boot 4 |
| Security | Spring Security + JWT |
| Database | PostgreSQL (Render) |
| Frontend | HTML, CSS, JavaScript |
| Deployment | Docker + Render |

---

## API Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /auth/register | Register new user |
| POST | /auth/login | Login & get JWT token |
| GET | /tasks | Get all tasks |
| POST | /tasks | Create a task |
| PUT | /tasks/{id} | Update a task |
| DELETE | /tasks/{id} | Delete a task |

---

## Run Locally
```bash
git clone https://github.com/kavyasharma2005/taskmaster
cd taskmaster/todoapp
./mvnw spring-boot:run
```
Open http://localhost:8080

---

Made with ❤️ by Kavya Sharma
```