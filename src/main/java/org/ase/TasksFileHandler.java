package org.ase;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TasksFileHandler {
    public static void saveTasksToFile(List<Tasks> tasks, String fileName) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(tasks);
        }
    }

    public static List<Tasks> loadTasksFromFile(String fileName) throws IOException, ClassNotFoundException {
        List<Tasks> tasks;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            tasks = (List<Tasks>) ois.readObject();
        }
        return tasks;
    }
}


public class TasksFileHandler {

    public static void saveTasksToFile(List<Tasks> tasks, String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Tasks task : tasks) {
                writer.write(task.getTitle() + "," + task.getDescription());
                writer.newLine();
            }
        }
    }

    public static List<Tasks> loadTasksFromFile(String filename) throws IOException {
        List<Tasks> tasks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    Tasks task = new Tasks(parts[0], parts[1]);
                    tasks.add(task);
                }
            }
        }
        return tasks;
    }
}
