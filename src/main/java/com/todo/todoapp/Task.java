package com.todo.todoapp;

import jakarta.persistence.*;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private boolean completed;

    // ── Constructors ───────────────────────────────────────────
    public Task() {}

    public Task(String name) {
        this.name = name;
        this.completed = false;
    }

    // ── Getters & Setters ──────────────────────────────────────
    public int getId() { return id; }
    public String getName() { return name; }
    public boolean isCompleted() { return completed; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}