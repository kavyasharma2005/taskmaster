package com.todo.todoapp;  

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    // ── GET all tasks ──────────────────────────────────────────
    @GetMapping
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // ── GET one task by id ─────────────────────────────────────
    @GetMapping("/{id}")
    public Task getTask(@PathVariable int id) {
        return taskRepository.findById(id).orElse(null);
    }

    // ── POST add a task ────────────────────────────────────────
    @PostMapping
    public Task addTask(@RequestBody Task task) {
        return taskRepository.save(task);
    }

    // ── PUT update a task ──────────────────────────────────────
    @PutMapping("/{id}")
    public Task updateTask(@PathVariable int id, @RequestBody Task updated) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task != null) {
            task.setName(updated.getName());
            task.setCompleted(updated.isCompleted());
            return taskRepository.save(task);
        }
        return null;
    }

    // ── DELETE a task ──────────────────────────────────────────
    @DeleteMapping("/{id}")
    public String deleteTask(@PathVariable int id) {
        taskRepository.deleteById(id);
        return "Task " + id + " deleted!";
    }
}