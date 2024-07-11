package org.ase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

/**
 * test class for {@link TasksFileHandler} class to verify that tasks will be read from and written
 * to the tasks .txt-file properly.
 */
class TasksFileHandlerTest {

  private static final String TEST_FILE_PATH = "src/main/resources/tasks.txt";
  private TasksFileHandler tasksFileHandler;
  private Logger mockLogger;
  private List<Tasks> tasks;

  @BeforeEach
  void setUp() throws IOException {
    // Prepare test file
    Files.write(Paths.get(TEST_FILE_PATH), new byte[0]);
    tasksFileHandler = new TasksFileHandler(TEST_FILE_PATH);

    // Initialize tasks list
    tasks = new ArrayList<>();
    tasks.add(new Tasks(UUID.randomUUID(), "Task 1", false));
    tasks.add(new Tasks(UUID.randomUUID(), "Task 2", true));

    // Mock Logger
    mockLogger = mock(Logger.class);
    tasksFileHandler.myLogger = mockLogger;
  }

  @AfterEach
  void deleteTestFile() throws IOException {
    Files.deleteIfExists(Paths.get(TEST_FILE_PATH));
  }

  /** Tests writing tasks to a file. */
  @Test
  void testWriteTasksToFile() throws IOException {
    tasksFileHandler.writeTasksToFile(tasks);

    List<String> lines = Files.readAllLines(Paths.get(TEST_FILE_PATH));
    assertEquals(2, lines.size(), "Expected 2 lines in the file.");
    assertTrue(lines.get(0).contains("Task 1"), "Expected Task 1 in the file.");
    assertTrue(lines.get(1).contains("Task 2"), "Expected Task 2 in the file.");
  }

  /** Tests reading tasks from a file. */
  @Test
  void testReadTasksFromFile() throws IOException {
    try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(TEST_FILE_PATH))) {
      for (Tasks task : tasks) {
        writer.write(task.getId() + "," + task.getDescription() + "," + task.isCompleted());
        writer.newLine();
      }
    }

    List<Tasks> readTasks = tasksFileHandler.readTasksFromFile();
    assertEquals(2, readTasks.size(), "Expected 2 tasks to be read.");
    for (int i = 0; i < tasks.size(); i++) {
      assertEquals(tasks.get(i).getId(), readTasks.get(i).getId(), "Task IDs should match.");
      assertEquals(
          tasks.get(i).getDescription(),
          readTasks.get(i).getDescription(),
          "Task descriptions should match.");
      assertEquals(
          tasks.get(i).isCompleted(),
          readTasks.get(i).isCompleted(),
          "Task completion statuses should match.");
    }
  }

  /** Tests handling of non-existing file during write operation. */
  @Test
  void testWriteNotExistingTasksToFile() throws IOException {
    // Delete the test file if it exists
    Files.deleteIfExists(Paths.get(TEST_FILE_PATH));

    // Mock behavior to throw IOException
    BufferedWriter mockWriter = mock(BufferedWriter.class);
    doThrow(new IOException("Mocked IOException")).when(mockWriter).write(anyString());
    doThrow(new IOException("Mocked IOException")).when(mockWriter).newLine();

    // Spy on Files to return the mockWriter when newBufferedWriter is called
    try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
      mockedFiles
          .when(() -> Files.newBufferedWriter(any(Path.class), any(Charset.class)))
          .thenReturn(mockWriter);

      tasksFileHandler.writeTasksToFile(tasks);

      verify(mockLogger, times(1)).log(eq(Level.SEVERE), anyString(), any(IOException.class));
    }
  }

  /** Tests handling of non-existing file during read operation. */
  @Test
  void testReadNotExistingTasksToFile() throws IOException {
    Files.deleteIfExists(Paths.get(TEST_FILE_PATH));
    List<Tasks> readTasks = tasksFileHandler.readTasksFromFile();

    assertTrue(readTasks.isEmpty(), "Expected an empty list when file does not exist.");
    verify(mockLogger, times(1)).log(eq(Level.SEVERE), anyString(), any(IOException.class));
  }
}
