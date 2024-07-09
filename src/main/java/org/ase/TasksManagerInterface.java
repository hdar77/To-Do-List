package org.ase;

import java.util.List;

/** The TasksManagerInterface defines the contract for managing tasks. */
public interface TasksManagerInterface {
  void addTask(Tasks task);

  List<Tasks> getAllTasks();

  Tasks getTaskById(String id);

  void updateTask(Tasks task);

  void deleteTask(String id);
}
