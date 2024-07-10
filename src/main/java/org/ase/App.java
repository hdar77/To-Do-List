package org.ase;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.UUID;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/** Main application class to interact with the user. */
public class App {
  private static final Logger logger = Logger.getLogger(App.class.getName());

  /** main method to trigger tasks menu. */
  public static void main(String[] args) {
    // Set logging level to INFO
    logger.setLevel(Level.INFO);

    // Remove existing handlers to avoid duplicate log entries
    Logger rootLogger = Logger.getLogger("");
    for (var handler : rootLogger.getHandlers()) {
      rootLogger.removeHandler(handler);
    }

    // Create a custom console handler to format log output without metadata
    ConsoleHandler handler = new ConsoleHandler();
    handler.setFormatter(
        new SimpleFormatter() {
          @Override
          public synchronized String format(java.util.logging.LogRecord lr) {
            return lr.getMessage() + "\n";
          }
        });
    handler.setLevel(Level.INFO);
    logger.addHandler(handler);

    final String filePath = "src/main/resources/tasks.txt";
    File tasksFile = new File(filePath);

    // Check if file exists, if not, create it
    if (!tasksFile.exists()) {
      try {
        if (tasksFile.createNewFile()) {
          logger.info("File created: " + filePath);
        } else {
          logger.severe("Failed to create file: " + filePath);
        }
      } catch (IOException e) {
        logger.log(Level.SEVERE, "An error occurred while creating the file: " + filePath, e);
      }
    } else {
      // Verify and clean up the file
      cleanUpTasksFile(filePath);
    }

    TasksFileHandler tasksFileHandler = new TasksFileHandler(filePath);
    TasksManager tasksManager = new TasksManager(tasksFileHandler);

    Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);

    while (true) {
      logger.info("1. Add Task");
      logger.info("2. View Tasks");
      logger.info("3. Update Task");
      logger.info("4. Delete Task");
      logger.info("5. Exit");
      logger.info("Choose an option: ");

      int option = scanner.nextInt();
      scanner.nextLine(); // consume the newline

      switch (option) {
        case 1:
          logger.info("Enter task description: ");
          String description = scanner.nextLine();
          Tasks task = new Tasks(UUID.randomUUID(), description, false);
          tasksManager.addTask(task);
          logger.info("Task added.");
          break;
        case 2:
          tasksManager.getAllTasks().forEach(t -> logger.info(t.toString()));
          break;
        case 3:
          logger.info("Enter task ID to update: ");
          UUID updateId = UUID.fromString(scanner.nextLine());
          logger.info("Enter new task description: ");
          String newDescription = scanner.nextLine();
          tasksManager.updateTask(updateId, newDescription);
          logger.info("Task updated.");
          break;
        case 4:
          logger.info("Enter task ID to delete: ");
          UUID deleteId = UUID.fromString(scanner.nextLine());
          tasksManager.deleteTask(deleteId);
          logger.info("Task deleted.");
          break;
        case 5:
          logger.info("Exiting application.");
          return;
        default:
          logger.info("Invalid option. Please try again.");
      }
    }
  }

  private static void cleanUpTasksFile(String filePath) {
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath));
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath + ".tmp"))) {
      String line;
      while ((line = reader.readLine()) != null) {
        try {
          // Attempt to parse the task to ensure it's valid
          String[] parts = line.split(",");
          if (parts.length == 3) {
            UUID.fromString(parts[0].trim());
            writer.write(line);
            writer.newLine();
          }
        } catch (IllegalArgumentException e) {
          // Skip invalid lines
        }
      }
    } catch (IOException e) {
      logger.log(Level.SEVERE, "An error occurred while cleaning up the file: " + filePath, e);
    }

    // Replace the old file with the cleaned up file
    File originalFile = new File(filePath);
    File tempFile = new File(filePath + ".tmp");
    if (originalFile.delete()) {
      tempFile.renameTo(originalFile);
    } else {
      logger.severe("Failed to replace the original file with the cleaned up file.");
    }
  }
}
