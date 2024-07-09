package org.ase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/** Tests for the TasksManager class. */
public class TasksManagerTest {

  private TasksManager tasksManager;
  private TasksFileHandler tasksFileHandler;

  @BeforeEach
  public void setUp() {
    tasksFileHandler = Mockito.mock(TasksFileHandler.class);
    List<Tasks> initialTasks = new ArrayList<>();
    initialTasks.add(new Tasks(UUID.randomUUID(), "Test Task 1", false));
    initialTasks.add(new Tasks(UUID.randomUUID(), "Test Task 2", true));

    when(tasksFileHandler.readTasksFromFile()).thenReturn(initialTasks);
    tasksManager = new TasksManager(tasksFileHandler);
  }

  @Test
  public void testAddTask() {
    Tasks newTask = new Tasks(UUID.randomUUID(), "New Task", false);
    tasksManager.addTask(newTask);

    verify(tasksFileHandler).writeTasksToFile(any());
  }

  @Test
  public void testGetAllTasks() {
    List<Tasks> tasks = tasksManager.getAllTasks();
    assertEquals(2, tasks.size()); // Adjust based on actual setup
  }

  @Test
  public void testUpdateTask() {
    UUID taskId = tasksManager.getAllTasks().get(0).getId();
    Tasks updatedTask = tasksManager.updateTask(taskId, "Updated Task Description");

    assertNotNull(updatedTask);
    assertEquals("Updated Task Description", updatedTask.getDescription());
  }

  @Test
  public void testGetTaskById() {
    UUID taskId = tasksManager.getAllTasks().get(0).getId();
    Tasks task = tasksManager.getTaskById(taskId);
    assertNotNull(task);
  }

  @Test
  public void testDeleteTask() {
    UUID taskId = tasksManager.getAllTasks().get(0).getId();
    tasksManager.deleteTask(taskId);
    List<Tasks> tasks = tasksManager.getAllTasks();
    assertEquals(1, tasks.size()); // Adjust based on actual setup
  }
}
