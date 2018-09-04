package edu.colostate.cs.cs414.a1.bdeining;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WorkerTest {

  private static final String NAME = "Bob";

  private Company company;

  private Worker worker;

  private Qualification qualification;

  private Set<Qualification> qualificationSet;

  @BeforeEach
  public void setup() throws Exception {
    qualification = new Qualification("somethingHelpful");
    qualificationSet = new HashSet<>();
    qualificationSet.add(qualification);
    worker = new Worker(NAME, qualificationSet);
    company = new Company("Amazon");
    worker.setCompany(company);
  }

  @Test
  public void testCreateWorkerNullName() {
    assertThrows(NullPointerException.class, () -> new Worker(null, qualificationSet));
  }

  @Test
  public void testCreateWorkerEmptyName() {
    assertThrows(InvalidName.class, () -> new Worker("", qualificationSet));
  }

  @Test
  public void testCreateWorkerBlankName() {
    assertThrows(InvalidName.class, () -> new Worker("b\\sob", qualificationSet));
  }

  @Test
  public void testCreateWorkerEmptyQualifications() {
    assertThrows(InvalidQualification.class, () -> new Worker(NAME, Collections.emptySet()));
  }

  @Test
  public void testCreateWorkerNullQualifications() {
    assertThrows(NullPointerException.class, () -> new Worker(NAME, null));
  }

  @Test
  public void testName() {
    assertEquals(worker.getName(), NAME);
  }

  @Test
  public void testDefaultSalary() {
    assertEquals(worker.getSalary(), 0);
  }

  @Test
  public void testSalary() {
    double salary = 100.0;
    worker.setSalary(salary);
    assertEquals(worker.getSalary(), salary);
  }

  @Test
  public void testGetQualifications() {
    assertEquals(worker.getQualification().size(), 1);
    assertTrue(worker.getQualification().contains(qualification));
  }

  @Test
  public void testAddQualification() throws Exception {
    Qualification anotherQualification = new Qualification("anotherQualification");
    boolean result = worker.addQualification(anotherQualification);
    assertTrue(result);
    assertEquals(worker.getQualification().size(), 2);
    assertTrue(worker.getQualification().contains(anotherQualification));
    assertTrue(worker.getQualification().contains(qualification));
  }

  @Test
  public void testAddQualificationNullQualification() {
    assertThrows(NullPointerException.class, () -> worker.addQualification(null));
  }

  @Test
  public void testHashCode() {
    assertNotEquals(worker.hashCode(), 0);
  }

  @Test
  public void testEquals() throws Exception {
    Worker anotherWorker = new Worker(NAME, qualificationSet);
    assertEquals(worker, anotherWorker);
  }

  @Test
  public void testEqualsNotEquals() throws Exception {
    Worker anotherWorker = new Worker("somebody else", qualificationSet);
    assertNotEquals(worker, anotherWorker);
  }

  @Test
  public void testEqualsNotWorker() {
    assertNotEquals(worker, 11);
  }

  @Test
  public void testToString() {
    assertTrue(worker.toString().contains(NAME));
  }

  @Test
  public void testGetCompany() {
    assertEquals(company, worker.getCompany());
  }

  @Test
  public void testAddProject() throws Exception {
    Project project =
        new Project("project", ProjectSize.LARGE, ProjectStatus.ACTIVE, qualificationSet);
    worker.addProject(project);
    assertEquals(worker.getProjects().size(), 1);
    assertTrue(worker.getProjects().contains(project));
  }

  @Test
  public void testGetProjectLoad() throws Exception {
    Project project =
        new Project("project", ProjectSize.LARGE, ProjectStatus.ACTIVE, qualificationSet);
    worker.addProject(project);
    assertEquals(worker.getProjectLoad(ProjectSize.LARGE), 1);
  }
}
