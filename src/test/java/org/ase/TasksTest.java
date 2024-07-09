package org.ase;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import org.junit.jupiter.api.Test;

/** Tests for the Tasks class. */
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
}
