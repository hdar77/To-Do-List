import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\nTo-Do-Liste:");
            System.out.println("1. Aufgabe hinzufügen");
            System.out.println("2. Aufgabe entfernen");
            System.out.println("3. Aufgaben anzeigen");
            System.out.println("4. Aufgaben speichern");
            System.out.println("5. Aufgaben laden");
            System.out.println("6. Beenden");
            System.out.print("Wähle eine Option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Konsumiere die Zeilenumbruch-Zeichen

            switch (choice) {
                case 1:
                    System.out.print("Gib die Aufgabe ein: ");
                    String taskDescription = scanner.nextLine();
                    taskManager.addTask(new Task(taskDescription));
                    break;
                case 2:
                    System.out.print("Gib den Index der zu entfernenden Aufgabe ein: ");
                    int index = scanner.nextInt();
                    taskManager.removeTask(index - 1);
                    break;
                case 3:
                    taskManager.showTasks();
                    break;
                case 4:
                    TaskFileHandler.saveTasksToFile(taskManager.getTasks());
                    break;
                case 5:
                    taskManager.setTasks(TaskFileHandler.loadTasksFromFile());
                    break;
                case 6:
                    exit = true;
                    break;
                default:
                    System.out.println("Ungültige Wahl. Bitte versuche es erneut.");
            }
        }

        scanner.close();
    }
}

