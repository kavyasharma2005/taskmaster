import java.util.ArrayList;
import java.util.Scanner;
import java.sql.*;

class Task {
    int id;
    String name;
    boolean completed;

    Task(int id, String name, boolean completed) {
        this.id = id;
        this.name = name;
        this.completed = completed;
    }

    public String toString() {
        if (completed) {
            return id + ". " + name + " ✔";
        } else {
            return id + ". " + name + " ✘";
        }
    }
}

public class ToDoApp {

    static final String DB = "jdbc:sqlite:tasks.db";

    // ── Load driver once when app starts ──────────────────────
    static void loadDriver() {
        try {
            Class.forName("org.sqlite.JDBC"); // ← manually load SQLite driver
            System.out.println("✅ SQLite driver loaded!");
        } catch (ClassNotFoundException e) {
            System.out.println("⚠ SQLite driver not found! Check your JAR file.");
        }
    }

    // ── Helper 1: print all tasks ──────────────────────────────
    static void printTasks(ArrayList<Task> tasks) {
        tasks.stream().forEach(t -> System.out.println(t));
    }

    // ── Helper 2: safely read a number ────────────────────────
    static int readNumber(Scanner sc) {
        try {
            int num = sc.nextInt();
            sc.nextLine();
            return num;
        } catch (Exception e) {
            sc.nextLine();
            return -1;
        }
    }

    // ── Helper 3: create table if it doesn't exist ────────────
    static void initDB() {
        String sql = """
                CREATE TABLE IF NOT EXISTS tasks (
                    id        INTEGER PRIMARY KEY AUTOINCREMENT,
                    name      TEXT    NOT NULL,
                    completed INTEGER DEFAULT 0
                )
                """;

        try (Connection conn = DriverManager.getConnection(DB);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("✅ Database ready!");
        } catch (SQLException e) {
            System.out.println("⚠ DB Error: " + e.getMessage());
        }
    }

    // ── Helper 4: load all tasks from database ─────────────────
    static ArrayList<Task> loadTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks";

        try (Connection conn = DriverManager.getConnection(DB);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id            = rs.getInt("id");
                String name       = rs.getString("name");
                boolean completed = rs.getInt("completed") == 1;
                tasks.add(new Task(id, name, completed));
            }

        } catch (SQLException e) {
            System.out.println("⚠ Could not load: " + e.getMessage());
        }

        return tasks;
    }

    // ── Helper 5: add a task ───────────────────────────────────
    static void addTask(String name) {
        String sql = "INSERT INTO tasks (name, completed) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(DB);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setInt(2, 0);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("⚠ Could not add: " + e.getMessage());
        }
    }

    // ── Helper 6: delete a task ────────────────────────────────
    static void deleteTask(int id) {
        String sql = "DELETE FROM tasks WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("⚠ Could not delete: " + e.getMessage());
        }
    }

    // ── Helper 7: mark task complete or incomplete ─────────────
    static void updateCompleted(int id, boolean completed) {
        String sql = "UPDATE tasks SET completed = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, completed ? 1 : 0);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("⚠ Could not update: " + e.getMessage());
        }
    }

    // ── Helper 8: edit a task name ─────────────────────────────
    static void editTask(int id, String newName) {
        String sql = "UPDATE tasks SET name = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newName);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("⚠ Could not edit: " + e.getMessage());
        }
    }

    // ── Main ───────────────────────────────────────────────────
    public static void main(String[] args) {

        loadDriver(); // ← load SQLite driver first
        initDB();     // ← create table if first time
        Scanner sc = new Scanner(System.in);

        while (true) {

            System.out.println("\n==== TO DO LIST ====");
            System.out.println("1. Add Task");
            System.out.println("2. View Tasks");
            System.out.println("3. Delete Task");
            System.out.println("4. Mark Task Complete");
            System.out.println("5. Mark Task Incomplete"); 
            System.out.println("6. Edit Task");
            System.out.println("7. Exit");
            System.out.print("Choose option: ");

            int choice = readNumber(sc);
            if (choice == -1) {
                System.out.println("⚠ Please enter a number!");
                continue;
            }

            if (choice == 1) {
                System.out.print("Enter task: ");
                String taskName = sc.nextLine().trim();
                if (taskName.isEmpty()) {
                    System.out.println("⚠ Task name cannot be empty!");
                } else {
                    addTask(taskName);
                    System.out.println("✅ Task added!");
                }
            }

            else if (choice == 2) {
                ArrayList<Task> tasks = loadTasks();
                if (tasks.isEmpty()) {
                    System.out.println("No tasks yet!");
                } else {
                    System.out.println("\n--- YOUR TASKS ---");
                    printTasks(tasks);
                    System.out.println("------------------");
                    long done = tasks.stream().filter(t -> t.completed).count();
                    System.out.println("✔ " + done + " done / " + (tasks.size() - done) + " remaining");
                    System.out.println("\n✔ Completed:");
                    tasks.stream()
                         .filter(t -> t.completed)
                         .forEach(System.out::println);
                }
            }

            else if (choice == 3) {
                ArrayList<Task> tasks = loadTasks();
                if (tasks.isEmpty()) {
                    System.out.println("No tasks to delete!");
                } else {
                    printTasks(tasks);
                    System.out.print("Enter task ID to delete: ");
                    int id = readNumber(sc);
                    if (id == -1) { System.out.println("⚠ Please enter a number!"); continue; }
                    deleteTask(id);
                    System.out.println("🗑 Task deleted!");
                }
            }

            else if (choice == 4) {
                ArrayList<Task> tasks = loadTasks();
                if (tasks.isEmpty()) {
                    System.out.println("No tasks available!");
                } else {
                    printTasks(tasks);
                    System.out.print("Enter task ID to mark complete: ");
                    int id = readNumber(sc);
                    if (id == -1) { System.out.println("⚠ Please enter a number!"); continue; }
                    updateCompleted(id, true);
                    System.out.println("🎉 Task marked complete!");
                }
            }

            else if (choice == 5) {
                ArrayList<Task> tasks = loadTasks();
                if (tasks.isEmpty()) {
                    System.out.println("No tasks available!");
                } else {
                    printTasks(tasks);
                    System.out.print("Enter task ID to mark incomplete: ");
                    int id = readNumber(sc);
                    if (id == -1) { System.out.println("⚠ Please enter a number!"); continue; }
                    updateCompleted(id, false);
                    System.out.println("↩ Task marked incomplete!");
                }
            }

            else if (choice == 6) {
                ArrayList<Task> tasks = loadTasks();
                if (tasks.isEmpty()) {
                    System.out.println("No tasks to edit!");
                } else {
                    printTasks(tasks);
                    System.out.print("Enter task ID to edit: ");
                    int id = readNumber(sc);
                    if (id == -1) { System.out.println("⚠ Please enter a number!"); continue; }
                    System.out.print("Enter new task name: ");
                    String newName = sc.nextLine().trim();
                    if (newName.isEmpty()) {
                        System.out.println("⚠ Task name cannot be empty!");
                    } else {
                        editTask(id, newName);
                        System.out.println("✏ Task updated!");
                    }
                }
            }

            else if (choice == 7) {
                System.out.println("👋 Goodbye!");
                break;
            }

            else {
                System.out.println("⚠ Invalid option! Choose 1-7.");
            }
        }

        sc.close();
    }
}