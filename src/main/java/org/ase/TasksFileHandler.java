import java.io.*;
import java.util.ArrayList;

public class TaskFileHandler {
    private static final String FILE_NAME = "tasks.txt";

    public static void saveTasksToFile(ArrayList<Task> tasks) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Task task : tasks) {
                writer.write(task.getDescription());
                writer.newLine();
            }
            System.out.println("Aufgaben wurden gespeichert.");
        } catch (IOException e) {
            System.out.println("Fehler beim Speichern der Aufgaben: " + e.getMessage());
        }
    }

    public static ArrayList<Task> loadTasksFromFile() {
        ArrayList<Task> tasks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                tasks.add(new Task(line));
            }
            System.out.println("Aufgaben wurden geladen.");
        } catch (IOException e) {
            System.out.println("Fehler beim Laden der Aufgaben: " + e.getMessage());
        }
        return tasks;
    }
}
