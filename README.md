# 🎯 Taskmaster

A full-stack Todo application built from scratch with Java and Spring Boot.

## 🚀 Tech Stack
- Java 17
- Spring Boot 4.0
- H2 Database
- Spring Data JPA
- HTML/CSS/JavaScript
- Maven

## ✨ Features
- Add, delete, edit tasks
- Mark tasks complete/incomplete
- REST API with full CRUD
- Clean responsive UI
- CLI version with SQLite

## 📁 Project Structure
- `src/` → Spring Boot REST API + HTML frontend
- `cli-app/` → Java CLI version with SQLite database

## 📡 API Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /tasks | Get all tasks |
| POST | /tasks | Add a task |
| PUT | /tasks/{id} | Update a task |
| DELETE | /tasks/{id} | Delete a task |

## 🛠️ How to Run
```bash
git clone https://github.com/kavyasharma2005/taskmaster.git
cd taskmaster
.\mvnw.cmd spring-boot:run
```
Open `http://localhost:8080` 🎉

## 👩‍💻 Author
**Kavya Sharma** — [@kavyasharma2005](https://github.com/kavyasharma2005)
