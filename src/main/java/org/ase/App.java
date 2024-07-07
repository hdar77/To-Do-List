package org.ase;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        TasksManager tasksManager = new TasksManager();
        Scanner scanner = new Scanner(System.in);

        // Beispiel: Abfragen und Hinzufügen von Aufgaben
        boolean addMoreTasks = true;
        while (addMoreTasks) {
            System.out.print("Geben Sie den Titel der Aufgabe ein (oder 'exit' zum Beenden): ");
            String title = scanner.nextLine();

            if ("exit".equalsIgnoreCase(title.trim())) {
                addMoreTasks = false;
                break;
            }

            System.out.print("Geben Sie eine Beschreibung für die Aufgabe ein: ");
            String description = scanner.nextLine();

            tasksManager.addTask(title, description); // Hier wird der Fehler verursacht

            System.out.println("Aufgabe hinzugefügt.");
            System.out.print("Möchten Sie eine weitere Aufgabe hinzufügen? (ja/nein): ");
            String choice = scanner.nextLine();
            if (!"ja".equalsIgnoreCase(choice.trim())) {
                addMoreTasks = false;
            }
        }

        // Aufgaben anzeigen
        System.out.println("Aktuelle Aufgaben:");
        tasksManager.displayTasks();

        // Aufgaben speichern und laden (optional, wenn TasksFileHandler verwendet wird)
        try {
            TasksFileHandler.saveTasksToFile(tasksManager.getTasks(), "tasks.txt");
            List<Tasks> loadedTasks = TasksFileHandler.loadTasksFromFile("tasks.txt");
            System.out.println("Geladene Aufgaben:");
            for (Tasks task : loadedTasks) {
                System.out.println(task);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Scanner schließen, um Ressourcen freizugeben
            scanner.close();
        }
    }
}
