package org.ase;

import java.util.UUID;

/** Represents a task with an ID, description, and completion status. */
public class Tasks {
  private final UUID id;
  private String description;
  private boolean completed;

  /**
   * Creates a new task.
   *
   * @param id the unique identifier of the task
   * @param description the description of the task
   * @param completed the completion status of the task
   */
  public Tasks(UUID id, String description, boolean completed) {
    this.id = id;
    this.description = description;
    this.completed = completed;
  }

  public UUID getId() {
    return id;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean isCompleted() {
    return completed;
  }

  public void setCompleted(boolean completed) {
    this.completed = completed;
  }

  @Override
  public String toString() {
    return "Tasks{"
        + "id="
        + id.toString()
        + ", description='"
        + description
        + '\''
        + ", completed="
        + completed
        + '}';
  }
}
