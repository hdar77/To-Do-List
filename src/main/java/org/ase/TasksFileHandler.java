package org.ase;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Handles reading and writing tasks to a file. */
public class TasksFileHandler {
  private final String filePath;
  public Logger myLogger = Logger.getLogger(TasksFileHandler.class.getName());

  /**
   * Creates a new file handler.
   *
   * @param filePath the path to the file where tasks are stored
   */
  public TasksFileHandler(String filePath) {
    this.filePath = filePath;
    myLogger.setLevel(Level.INFO);
  }

  /**
   * Reads tasks from the file.
   *
   * @return the list of tasks read from the file
   */
  public List<Tasks> readTasksFromFile() {
    List<Tasks> tasks = new ArrayList<>();
    try (BufferedReader br = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8)) {
      String line;
      while ((line = br.readLine()) != null) {
        Tasks task = parseTask(line);
        tasks.add(task);
      }
    } catch (IOException e) {
      myLogger.log(Level.SEVERE, "Failed to read tasks from file: " + filePath, e);
    }
    return tasks;
  }

  /**
   * Writes tasks to the file.
   *
   * @param tasks the list of tasks to write to the file
   */
  public void writeTasksToFile(List<Tasks> tasks) {
    try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(filePath), StandardCharsets.UTF_8)) {
      for (Tasks task : tasks) {
        bw.write(
            task.toString()); // Assume a proper toString() implementation for CSV representation
        bw.newLine();
      }
    } catch (IOException e) {
      myLogger.log(Level.SEVERE, "Failed to write tasks to file: " + filePath, e);
    }
  }

  private Tasks parseTask(String line) {
    String[] parts = line.split(",");
    if (parts.length != 3) {
      throw new IllegalArgumentException("Invalid task format: " + line);
    }
    UUID id = UUID.fromString(parts[0].trim()); // trim UUID string to 36 digits
    String description = parts[1].trim(); // trim description
    boolean isCompleted = Boolean.parseBoolean(parts[2].trim());
    return new Tasks(id, description, isCompleted);
  }
}
