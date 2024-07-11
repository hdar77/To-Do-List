package org.ase;

import java.util.List;
import java.util.UUID;

/** Manages tasks by adding, updating, deleting, and retrieving tasks. */
public class TasksManager {
  private final TasksFileHandler tasksFileHandler;
  private List<Tasks> tasks;

  /**
   * Creates a new tasks manager.
   *
   * @param tasksFileHandler the file handler to read/write tasks
   */
  public TasksManager(TasksFileHandler tasksFileHandler) {
    this.tasksFileHandler = tasksFileHandler;
    this.tasks = tasksFileHandler.readTasksFromFile();
  }

  /**
   * Adds a new task and writes it to the file.
   *
   * @param task the task to add
   */
  public void addTask(Tasks task) {
    tasks.add(task);
    tasksFileHandler.writeTasksToFile(tasks);
  }

  /**
   * Retrieves all tasks.
   *
   * @return the list of tasks
   */
  public List<Tasks> getAllTasks() {
    return tasks;
  }

  /**
   * Retrieves a task by its ID.
   *
   * @param id the unique identifier of the task
   * @return the task with the specified ID, or null if not found
   */
  public Tasks getTaskById(UUID id) {
    return tasks.stream().filter(task -> task.getId().equals(id)).findFirst().orElse(null);
  }

  /**
   * Updates the description of a task and writes the change to the file.
   *
   * @param id the unique identifier of the task
   * @param newDescription the new description of the task
   * @return the updated task, or null if not found
   */
  public Tasks updateTask(UUID id, String newDescription, boolean isCompleted) {
    for (Tasks task : tasks) {
      if (task.getId().equals(id)) {
        task.setDescription(newDescription);
        task.setCompleted(isCompleted);
        tasksFileHandler.writeTasksToFile(tasks);
        return task;
      }
    }
    return null;
  }

  /**
   * Deletes a task by its ID and writes the change to the file.
   *
   * @param id the unique identifier of the task
   */
  public void deleteTask(UUID id) {
    tasks.removeIf(task -> task.getId().equals(id));
    tasksFileHandler.writeTasksToFile(tasks);
  }
}
