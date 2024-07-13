package org.ase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/** Tests for the {@link TasksManager} class. */
class TasksManagerTest {

  /** define objects of task manager and file handler. */
  private TasksManager tasksManager;

  private TasksFileHandler tasksFileHandler;

  /** setup method to initialize the tasks manager and file handler */
  @BeforeEach
  public void setUp() {
    tasksFileHandler = Mockito.mock(TasksFileHandler.class);
    List<Tasks> initialTasks = new ArrayList<>();
    initialTasks.add(new Tasks(UUID.randomUUID(), "Test Task 1", false));
    initialTasks.add(new Tasks(UUID.randomUUID(), "Test Task 2", true));

    when(tasksFileHandler.readTasksFromFile()).thenReturn(initialTasks);
    tasksManager = new TasksManager(tasksFileHandler);
  }

  /** test for adding a task */
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

  /** test for updating a task */
  @Test
  public void testUpdateTask() {
    UUID taskId = tasksManager.getAllTasks().get(0).getId();
    Tasks updatedTask = tasksManager.updateTask(taskId, "Updated Task Description", false);

    assertNotNull(updatedTask);
    assertEquals("Updated Task Description", updatedTask.getDescription());
  }

  /** test for trying to update a non-existing task */
  @Test
  public void testUpdateNonExistingTask() {
    // Generate a random UUID that does not correspond to any existing task
    UUID nonExistingTaskId = UUID.randomUUID();

    // Attempt to update a non-existing task
    Tasks updatedTask =
        tasksManager.updateTask(nonExistingTaskId, "Non-Existent Task Description", false);

    // Verify that the updateTask method returns null for a non-existing task
    assertNull(updatedTask, "Expected updateTask method to return null for a non-existing task.");

    // Verify the writeTasksToFile method was not called
    verify(tasksFileHandler, never()).writeTasksToFile(anyList());
  }

  /** test for getting task by id */
  @Test
  public void testGetTaskById() {
    UUID taskId = tasksManager.getAllTasks().get(0).getId();
    Tasks task = tasksManager.getTaskById(taskId);
    assertNotNull(task);
  }

  /** test for task deletion */
  @Test
  public void testDeleteTask() {
    UUID taskId = tasksManager.getAllTasks().get(0).getId();
    tasksManager.deleteTask(taskId);
    List<Tasks> tasks = tasksManager.getAllTasks();
    assertEquals(1, tasks.size()); // Adjust based on actual setup
  }
}
