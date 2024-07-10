package org.ase;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.UUID;

/** Main application class to interact with the user. */
public class App {
  /** main method to trigger tasks menu. */
  public static void main(String[] args) {
    TasksFileHandler tasksFileHandler = new TasksFileHandler("src/main/resources/tasks.txt");
    TasksManager tasksManager = new TasksManager(tasksFileHandler);

    Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);

    while (true) {
      System.out.println("1. Add Task");
      System.out.println("2. View Tasks");
      System.out.println("3. Update Task");
      System.out.println("4. Delete Task");
      System.out.println("5. Exit");
      System.out.print("Choose an option: ");

      int option = scanner.nextInt();
      scanner.nextLine(); // consume the newline

      switch (option) {
        case 1:
          System.out.print("Enter task description: ");
          String description = scanner.nextLine();
          Tasks task = new Tasks(UUID.randomUUID(), description, false);
          tasksManager.addTask(task);
          break;
        case 2:
          tasksManager.getAllTasks().forEach(System.out::println);
          break;
        case 3:
          System.out.print("Enter task ID to update: ");
          UUID updateId = UUID.fromString(scanner.nextLine());
          System.out.print("Enter new task description: ");
          String newDescription = scanner.nextLine();
          tasksManager.updateTask(updateId, newDescription);
          break;
        case 4:
          System.out.print("Enter task ID to delete: ");
          UUID deleteId = UUID.fromString(scanner.nextLine());
          tasksManager.deleteTask(deleteId);
          break;
        case 5:
          return;
        default:
          System.out.println("Invalid option. Please try again.");
      }
    }
  }
}
