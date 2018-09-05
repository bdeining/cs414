package edu.colostate.cs.cs414.a1.bdeining;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
public class ProjectTest {

  private static final String PROJECT_NAME = "A Project";

  private Qualification qualification;

  private Set<Qualification> qualificationSet;

  private Project project;

  @BeforeEach
  public void setup() throws Exception {
    qualification = new Qualification("a Qualification");
    qualificationSet = new HashSet<>();
    qualificationSet.add(qualification);
    project = new Project(PROJECT_NAME, ProjectSize.LARGE, ProjectStatus.PLANNED, qualificationSet);
  }

  @Test
  public void testCreateProjectNullName() {
    assertThrows(
        NullPointerException.class,
        () -> new Project(null, ProjectSize.LARGE, ProjectStatus.PLANNED, qualificationSet));
  }

  @Test
  public void testCreateProjectEmptyName() {
    assertThrows(
        InvalidName.class,
        () -> new Project("", ProjectSize.LARGE, ProjectStatus.PLANNED, qualificationSet));
  }

  @Test
  public void testCreateProjectNullQualifications() {
    assertThrows(
        NullPointerException.class,
        () -> new Project(PROJECT_NAME, ProjectSize.LARGE, ProjectStatus.PLANNED, null));
  }

  @Test
  public void testCreateProjectEmptyQualifications() {
    assertThrows(
        InvalidQualification.class,
        () -> new Project(PROJECT_NAME, ProjectSize.LARGE, ProjectStatus.PLANNED, new HashSet<>()));
  }

  @Test
  public void testProjectsEqual() throws Exception {
    Project anotherProject =
        new Project(PROJECT_NAME, ProjectSize.LARGE, ProjectStatus.PLANNED, qualificationSet);
    assertEquals(project, anotherProject);
  }

  @Test
  public void testProjectsNotEqual() throws Exception {
    Project anotherProject =
        new Project(
            "A different project", ProjectSize.LARGE, ProjectStatus.PLANNED, qualificationSet);
    assertNotEquals(project, anotherProject);
  }

  @Test
  public void testProjectsNotEqualNotAProject() {
    assertNotEquals(project, "a string");
  }

  @Test
  public void testToString() {
    assertTrue(project.toString().contains(PROJECT_NAME));
    assertTrue(project.toString().contains(project.getStatus().toString()));
  }

  @Test
  public void testNoMissingQualifications() throws Exception {
    Worker worker = new Worker("Bob", qualificationSet);
    project.getWorkers().add(worker);
    assertTrue(project.missingQualifications().isEmpty());
  }

  @Test
  public void testMissingQualifications() throws Exception {
    Qualification qualification = new Qualification("somethingUnhelpful");
    Worker worker = new Worker("Bob", Collections.singleton(qualification));
    project.getWorkers().add(worker);
    assertFalse(project.missingQualifications().isEmpty());
    assertTrue(project.missingQualifications().contains(this.qualification));
  }

  @Test
  public void testIsHelpful() throws Exception {
    Worker worker = new Worker("Bob", qualificationSet);
    assertTrue(project.isHelpful(worker));
  }

  @Test
  public void testIsNotHelpful() throws Exception {
    Qualification qualification = new Qualification("somethingUnhelpful");
    Worker worker = new Worker("Bob", Collections.singleton(qualification));
    assertFalse(project.isHelpful(worker));
  }

  @Test
  public void testIsHelpfulNullWorker() {
    assertThrows(NullPointerException.class, () -> project.isHelpful(null));
  }

  @Test
  public void testAddWorker() throws Exception {
    Worker worker = new Worker("Bob", qualificationSet);
    project.getWorkers().add(worker);
    assertTrue(project.getWorkers().contains(worker));
  }

  @Test
  public void testAddQualification() throws Exception {
    Qualification qualification = new Qualification("somethingMoreHelpful");
    Worker worker = new Worker("Bob", Collections.singleton(qualification));
    project.addQualification(qualification);
    assertTrue(project.isHelpful(worker));
  }

  @Test
  public void testAddQualificationNullQualification() {
    assertThrows(NullPointerException.class, () -> project.addQualification(null));
  }
}
