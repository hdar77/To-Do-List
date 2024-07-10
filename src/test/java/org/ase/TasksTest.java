package org.ase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;
import org.junit.jupiter.api.Test;

// why checkstyle is annoying sometimes
// Intellij converts the three static imports above to an .* import which causes a checkstyle
// violation.

// import static org.junit.jupiter.api.Assertions.*;

/** Tests for the {@link Tasks} class. */
public class TasksTest {

  @Test
  public void testTaskCreation() {
    UUID id = UUID.randomUUID();
    Tasks task = new Tasks(id, "Test Task", false);

    assertEquals(id, task.getId());
    assertEquals("Test Task", task.getDescription());
    assertFalse(task.isCompleted());
  }

  @Test
  public void testTaskSetDescription() {
    Tasks task = new Tasks(UUID.randomUUID(), "Old Description", false);
    task.setDescription("New Description");

    assertEquals("New Description", task.getDescription());
  }

  @Test
  public void testTaskComplete() {
    Tasks task = new Tasks(UUID.randomUUID(), "Test Task", false);
    task.setCompleted(true);

    assertTrue(task.isCompleted());
  }

  @Test
  public void testToStringMethod() {
    // Create a task with known values
    UUID id = UUID.randomUUID();
    String description = "Sample Task";
    boolean completed = false;

    Tasks task = new Tasks(id, description, completed);

    // Expected string representation
    String expectedString =
        "Tasks{id=" + id + ", description='" + description + "', completed=" + completed + "}";

    // Verify the toString() method
    assertEquals(
        expectedString, task.toString(), "toString method didnt display expected string properly.");
  }
}
