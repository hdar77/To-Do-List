package org.ase;

import java.util.ArrayList;
import java.util.List;

public class TasksManager {
    private List<Tasks> tasks;

    public TasksManager() {
        this.tasks = new ArrayList<>();
    }

    public void addTask(String title, String description) {
        Tasks newTask = new Tasks(title, description);
        tasks.add(newTask);
    }

    public void displayTasks() {
        for (Tasks task : tasks) {
            System.out.println(task.getTitle() + ": " + task.getDescription());
        }
    }

    public List<Tasks> getTasks() {
        return tasks;
    }
}
