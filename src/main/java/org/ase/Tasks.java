package org.ase;

public class Tasks {
    private String title;
    private String description;

    public Tasks(String title, String description) {
        this.title = title;
        this.description = description;
    }

    // Getter und Setter
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Tasks [title=" + title + ", description=" + description + "]";
    }
}
