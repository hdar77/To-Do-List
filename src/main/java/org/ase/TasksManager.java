import java.util.ArrayList;

public class TaskManager {
    private ArrayList<Task> tasks;

    public TaskManager() {
        tasks = new ArrayList<>();
    }

    public void addTask(Task task) {
        tasks.add(task);
        System.out.println("Aufgabe hinzugefügt: " + task.getDescription());
    }

    public void removeTask(int index) {
        if (index >= 0 && index < tasks.size()) {
            System.out.println("Aufgabe entfernt: " + tasks.remove(index).getDescription());
        } else {
            System.out.println("Ungültiger Index.");
        }
    }

    public void showTasks() {
        if (tasks.isEmpty()) {
            System.out.println("Keine Aufgaben in der Liste.");
        } else {
            System.out.println("Aktuelle Aufgaben:");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println((i + 1) + ". " + tasks.get(i).getDescription());
            }
        }
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }
}
