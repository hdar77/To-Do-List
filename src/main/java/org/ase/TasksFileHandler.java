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

/** Handles reading and writing tasks to a file. */
public class TasksFileHandler {
  private final String filePath;

  /**
   * Creates a new file handler.
   *
   * @param filePath the path to the file where tasks are stored
   */
  public TasksFileHandler(String filePath) {
    this.filePath = filePath;
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
      e.printStackTrace(); // TODO: Replace with proper logging
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
      e.printStackTrace(); // TODO: Replace with proper logging
    }
  }

  private Tasks parseTask(String line) {
    String[] parts = line.split(",");
    UUID id = UUID.fromString(parts[0]);
    String description = parts[1];
    boolean isCompleted = Boolean.parseBoolean(parts[2]);
    return new Tasks(id, description, isCompleted);
  }
}
