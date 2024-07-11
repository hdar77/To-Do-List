package org.ase;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class AppTest {

  private static final String FILE_PATH = "src/main/resources/tasks.txt";

  @Mock private TasksFileHandler tasksFileHandler;

  @Mock private TasksManager tasksManager;

  private AutoCloseable closeable;
  private File tasksFile;
  private Logger myLogger;
  private ByteArrayOutputStream outputStream;

  @BeforeEach
  void setUp() throws IOException {
    tasksFile = Mockito.mock(File.class);
    myLogger = Mockito.mock(Logger.class);
    closeable = MockitoAnnotations.openMocks(this);

    Logger logger = Logger.getLogger(App.class.getName());
    logger.setLevel(Level.INFO);

    ConsoleHandler handler = new ConsoleHandler();
    handler.setFormatter(
        new java.util.logging.SimpleFormatter() {
          @Override
          public synchronized String format(java.util.logging.LogRecord lr) {
            return lr.getMessage() + "\n";
          }
        });
    handler.setLevel(Level.INFO);
    logger.addHandler(handler);

    // Ensure the file does not exist before each test
    Files.deleteIfExists(Paths.get(FILE_PATH));

    // Create the file
    tasksFile = new File(FILE_PATH);
    tasksFile.getParentFile().mkdirs();
    tasksFile.createNewFile();
    assertTrue(tasksFile.exists(), "File should exist.");
  }

  @AfterEach
  void deleteTestFile() throws Exception {
    // Clean up test file if it exists
    Files.deleteIfExists(Paths.get(FILE_PATH));
    closeable.close();
  }

  /** Test for the main method, simulating the add task option. */
  @Test
  void testAddTask() throws IOException {

    // Arrange sample task data
    String taskDescription = "Test Task";
    Path filePath = Paths.get(FILE_PATH);
    Files.write(filePath, new byte[0]);

    // add task and then exit program
    String input = "1\n" + taskDescription + "\n5\n";
    System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));

    App app = new App();

    // start main method
    app.main(new String[] {});

    // Check that the task is in the file
    String fileContent = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
    assertTrue(
        fileContent.contains(taskDescription),
        "Expected file to contain the task description: 'Test Task'.");
  }

  /** Test for the main method, simulating the view tasks option. */
  @Test
  void testViewTasks() throws IOException {
    // Arrange
    UUID taskId = UUID.randomUUID();
    String taskDescription = "Sample Task";
    String taskData = taskId + "," + taskDescription + "," + false;

    // Ensure the file exists
    Path filePath = Paths.get(FILE_PATH);
    if (!Files.exists(filePath)) {
      Files.createFile(filePath);
    }

    // Write the sample task to the file
    try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
      writer.write(taskData);
      writer.newLine();
    }

    // Prepare the input for viewing tasks
    String input = "2\n5\n"; // Choose option 2 to view tasks, then option 5 to exit
    System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));

    // Capture the output
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    System.setOut(printStream);

    // Act
    App.main(new String[] {});

    // Assert
    /*String output = outputStream.toString();
    String expectedTaskString = "Tasks{id=" + taskId + ", description='"
    + taskDescription + "',completed=false}";
    assertTrue(output.contains(expectedTaskString), "Expected output to contain the task: "
            + expectedTaskString + "\nActual output: " + output);*/

    // Verify the task is in the file
    String fileContent = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
    assertTrue(
        fileContent.contains(taskData),
        "Expected file to contain the task data: "
            + taskData
            + "\nActual file content: "
            + fileContent);

    // Reset System.in and System.out
    System.setIn(System.in);
    System.setOut(System.out);
  }

  /** Test for the main method, simulating the update task option. */
  @Test
  void testUpdateTask() throws IOException {
    // Arrange
    UUID taskId = UUID.randomUUID();
    String initialTaskDescription = "Initial Task";
    boolean initialTaskStatus = false;

    // Ensure the file is created and has a task
    if (!Files.exists(Paths.get(FILE_PATH))) {
      Files.createFile(Paths.get(FILE_PATH));
    }

    // Write the sample task to the file
    try (BufferedWriter writer =
        Files.newBufferedWriter(Paths.get(FILE_PATH), StandardCharsets.UTF_8)) {
      writer.write(taskId + "," + initialTaskDescription + "," + initialTaskStatus);
      writer.newLine();
    }

    // Prepare the input for updating the task
    String input =
        "3\n"
            + taskId.toString()
            + "\nUpdated Task\ntrue\n5\n"; // Choose option 3 to update task, provide the task ID,
    // new description, completion status, and choose option
    // 5 to exit
    InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
    System.setIn(inputStream);

    // Capture the output
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    System.setOut(printStream);

    // Act
    // Run the main method of the App class in a separate thread to handle Scanner input correctly
    Thread appThread =
        new Thread(
            () -> {
              try {
                App.main(new String[0]);
              } catch (Exception e) {
                fail("Application encountered an exception: " + e.getMessage());
              }
            });
    appThread.start();

    // Give enough time for the input to be processed and the task to be updated
    try {
      Thread.sleep(2000); // Adjust sleep time if necessary
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    // Stop the thread after the test
    appThread.interrupt();

    // Verify the task is updated in the file
    String fileContent =
        new String(Files.readAllBytes(Paths.get(FILE_PATH)), StandardCharsets.UTF_8);
    assertTrue(fileContent.contains(taskId.toString()), "Expected task ID to remain in the file");
    assertTrue(
        fileContent.contains("Updated Task"),
        "Expected task description to be updated in the file");
    assertTrue(
        fileContent.contains("true"), "Expected task completion status to be updated in the file");
  }

  /** Test for the main method, simulating the delete task option. */
  @Test
  void testDeleteTask() throws IOException {
    // Arrange
    UUID taskId = UUID.randomUUID();
    String taskDescription = "Sample Task";

    // Ensure the file is created
    if (!Files.exists(Paths.get(FILE_PATH))) {
      Files.createFile(Paths.get(FILE_PATH));
    }

    // Write the sample task to the file
    try (BufferedWriter writer =
        Files.newBufferedWriter(Paths.get(FILE_PATH), StandardCharsets.UTF_8)) {
      writer.write(taskId + "," + taskDescription + "," + false);
      writer.newLine();
    }

    // Prepare the input for deleting the task
    String input =
        "4\n"
            + taskId.toString()
            + "\n5\n"; // Choose option 4 to delete task, provide the task ID, and choose option 5
    // to exit
    InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
    System.setIn(inputStream);

    // Capture the output
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    System.setOut(printStream);

    // Act
    // Run the main method of the App class in a separate thread to handle Scanner input correctly
    Thread appThread =
        new Thread(
            () -> {
              try {
                App.main(new String[0]);
              } catch (Exception e) {
                fail("Application encountered an exception: " + e.getMessage());
              }
            });
    appThread.start();

    // Give enough time for the input to be processed and the task to be deleted
    try {
      Thread.sleep(2000); // Adjust sleep time if necessary
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    // Stop the thread after test
    appThread.interrupt();

    // Verify the task is not in the file anymore
    String fileContent =
        new String(Files.readAllBytes(Paths.get(FILE_PATH)), StandardCharsets.UTF_8);
    assertFalse(
        fileContent.contains(taskId.toString()), "Expected task to be deleted from the file");
  }

  /** Test for the main method, simulating the exit option. */
  @Test
  void testExit() {
    String input = "5\n";
    System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));

    TasksManager tasksManager = spy(new TasksManager(tasksFileHandler));
    App app = new App();

    app.main(new String[] {});

    // Verify no further interactions are made after exit
    verifyNoMoreInteractions(tasksManager);
  }

  /*@Test
  public void testFileCreationSuccess() throws IOException {
    Mockito.when(tasksFile.createNewFile()).thenReturn(true);

    File file = new File(FILE_PATH);
    // Call the method/function that contains the code snippet
    // Replace MyClass with the actual class name
    file.createNewFile();

    // Verify that the logger was called with the correct message
    //Mockito.verify(myLogger).info("File created: " + tasksFile.getPath());
  }*/

  /*@Test
  void testFileCreationFailureLogsSevere() throws IOException {
    // Arrange
    when(tasksFile.exists()).thenReturn(false); // Simulate that the file does not exist
    when(tasksFile.createNewFile()).thenReturn(false); // Simulate file creation failure

    // Act
    App.main(new String[] {}); // Call main method to trigger the file creation logic

    // Assert
    verify(myLogger, times(1)).severe(anyString()); // Verify that severe log was called
    verify(tasksFile).createNewFile(); // Verify that createNewFile was called

    // Capture and assert the log message
    ArgumentCaptor<String> logCaptor = ArgumentCaptor.forClass(String.class);
    verify(myLogger).severe(logCaptor.capture());
    String logMessage = logCaptor.getValue();
    assertTrue(
        logMessage.contains("Failed to create file: " + FILE_PATH),
        "Expected log message for file creation failure.");
  }*/

  /*@Test
  void testIOExceptionDuringFileCreationLogsSevere() throws IOException {
    // Arrange
    IOException ioException = new IOException("File creation failed");
    when(tasksFile.exists()).thenReturn(false); // Simulate that the file does not exist
    when(tasksFile.createNewFile())
        .thenThrow(ioException); // Simulate IOException during file creation

    // Act
    App.main(new String[] {}); // Call main method to trigger the file creation logic

    // Assert
    verify(myLogger, times(1))
        .log(
            eq(Level.SEVERE),
            anyString(),
            eq(ioException)); // Verify that severe log was called with IOException

    // Capture and assert the log message
    ArgumentCaptor<Throwable> throwableCaptor = ArgumentCaptor.forClass(Throwable.class);
    ArgumentCaptor<String> logMessageCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Level> levelCaptor = ArgumentCaptor.forClass(Level.class);
    verify(myLogger)
        .log(levelCaptor.capture(), logMessageCaptor.capture(), throwableCaptor.capture());

    assertEquals(Level.SEVERE, levelCaptor.getValue());
    assertTrue(
        logMessageCaptor
            .getValue()
            .contains("An error occurred while creating the file: " + FILE_PATH),
        "Expected log message for IOException.");
    assertEquals(ioException, throwableCaptor.getValue());
  }*/

  /* tests for cleanUpTasksFile(String filepath) -method */
  // TODO
}
