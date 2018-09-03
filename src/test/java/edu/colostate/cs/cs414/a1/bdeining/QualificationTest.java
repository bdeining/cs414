package edu.colostate.cs.cs414.a1.bdeining;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
public class QualificationTest {

  private static final String DESCRIPTION = "description";

  private Qualification qualification;

  @BeforeEach
  public void beforeTest() throws InvalidDescription {
    qualification = new Qualification(DESCRIPTION);
  }

  @Test
  public void testInitializeNullDescription() {
    assertThrows(NullPointerException.class, () -> new Qualification(null));
  }

  @Test
  public void testInitializeBlankDescription() {
    assertThrows(InvalidDescription.class, () -> new Qualification("desscr\\sption"));
  }

  @Test
  public void testToString() {
    assertEquals(qualification.toString(), DESCRIPTION);
  }

  @Test
  public void testAddWorkerNullWorker() {
    assertThrows(NullPointerException.class, () -> qualification.addWorker(null));
  }

  @Test
  public void testAddProjectNullProject() {
    assertThrows(NullPointerException.class, () -> qualification.addProject(null));
  }

  @Test
  public void testEquals() throws Exception {
    Qualification anotherQualification = new Qualification(DESCRIPTION);
    assertEquals(anotherQualification, qualification);
  }

  @Test
  public void testNotEquals() throws Exception {
    Qualification anotherQualification = new Qualification("a different qualification");
    assertNotEquals(anotherQualification, qualification);
  }

  @Test
  public void testNotEqualsNotAQualification() {
    assertNotEquals(qualification, 12);
  }

  @Test
  public void testHashCode() {
    qualification.addProject(mock(Project.class));
    qualification.addWorker(mock(Worker.class));
    assertNotEquals(qualification.hashCode(), 0);
  }
}
