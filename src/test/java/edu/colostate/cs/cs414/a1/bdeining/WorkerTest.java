package edu.colostate.cs.cs414.a1.bdeining;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javafx.util.Pair;
import org.junit.BeforeClass;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class WorkerTest {

  private static final String NAME = "Bob";

  private static Company company;

  private static Worker worker;

  private static Qualification qualification;

  private static Set<Qualification> qualificationSet;

  @DataPoints
  public static Pair<Project[], Integer>[] projectLoads() {
    try {
      Set<Qualification> qualificationSet = Collections.singleton(qualification);

      Project[] projects =
          new Project[] {
            new Project("project", ProjectSize.LARGE, ProjectStatus.ACTIVE, qualificationSet),
            new Project("project2", ProjectSize.MEDIUM, ProjectStatus.ACTIVE, qualificationSet),
            new Project("project3", ProjectSize.SMALL, ProjectStatus.ACTIVE, qualificationSet),
            new Project("project4", ProjectSize.LARGE, ProjectStatus.ACTIVE, qualificationSet)
          };
      Pair<Project[], Integer> pair = new Pair<>(projects, 9);

      Project[] projects2 =
          new Project[] {
            new Project("project", ProjectSize.LARGE, ProjectStatus.ACTIVE, qualificationSet),
            new Project("project4", ProjectSize.LARGE, ProjectStatus.ACTIVE, qualificationSet)
          };
      Pair<Project[], Integer> pair2 = new Pair<>(projects2, 6);

      Project[] projects3 =
          new Project[] {
            new Project("project", ProjectSize.LARGE, ProjectStatus.ACTIVE, qualificationSet),
            new Project("project2", ProjectSize.LARGE, ProjectStatus.ACTIVE, qualificationSet),
            new Project("project3", ProjectSize.LARGE, ProjectStatus.ACTIVE, qualificationSet),
            new Project("project4", ProjectSize.LARGE, ProjectStatus.ACTIVE, qualificationSet),
            new Project("project5", ProjectSize.LARGE, ProjectStatus.ACTIVE, qualificationSet)
          };
      Pair<Project[], Integer> pair3 = new Pair<>(projects3, 15);

      return new Pair[] {pair, pair2, pair3};
    } catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
  }

  @BeforeClass
  public static void setup() throws Exception {
    qualification = new Qualification("somethingHelpful");
    qualificationSet = new HashSet<>();
    qualificationSet.add(qualification);
    worker = new Worker(NAME, qualificationSet);
    company = new Company("Amazon");
    worker.setCompany(company);
  }

  @Theory
  public void testCreateWorkerNullName() {
    assertThrows(NullPointerException.class, () -> new Worker(null, qualificationSet));
  }

  @Theory
  public void testCreateWorkerEmptyName() {
    assertThrows(InvalidName.class, () -> new Worker("", qualificationSet));
  }

  @Theory
  public void testCreateWorkerBlankName() {
    assertThrows(InvalidName.class, () -> new Worker("b\\sob", qualificationSet));
  }

  @Theory
  public void testCreateWorkerEmptyQualifications() {
    assertThrows(InvalidQualification.class, () -> new Worker(NAME, Collections.emptySet()));
  }

  @Theory
  public void testCreateWorkerNullQualifications() {
    assertThrows(NullPointerException.class, () -> new Worker(NAME, null));
  }

  @Theory
  public void testName() {
    assertEquals(worker.getName(), NAME);
  }

  @Theory
  public void testDefaultSalary() {
    assertEquals(worker.getSalary(), 0);
  }

  @Theory
  public void testSalary() {
    double salary = 100.0;
    worker.setSalary(salary);
    assertEquals(worker.getSalary(), salary);
  }

  @Theory
  public void testGetQualifications() {
    assertEquals(worker.getQualification().size(), 1);
    assertTrue(worker.getQualification().contains(qualification));
  }

  @Theory
  public void testAddQualification() throws Exception {
    Qualification qualification = new Qualification("somethingHelpful");

    Set<Qualification> qualificationSet = new HashSet<>();
    qualificationSet.add(qualification);

    Worker worker = new Worker(NAME, qualificationSet);

    Qualification anotherQualification = new Qualification("anotherQualification");
    boolean result = worker.addQualification(anotherQualification);
    assertTrue(result);
    assertEquals(worker.getQualification().size(), 2);
    assertTrue(worker.getQualification().contains(anotherQualification));
    assertTrue(worker.getQualification().contains(qualification));
  }

  @Theory
  public void testAddQualificationNullQualification() {
    assertThrows(NullPointerException.class, () -> worker.addQualification(null));
  }

  @Theory
  public void testHashCode() {
    assertNotEquals(worker.hashCode(), 0);
  }

  @Theory
  public void testEquals() throws Exception {
    Worker anotherWorker = new Worker(NAME, qualificationSet);
    assertEquals(worker, anotherWorker);
  }

  @Theory
  public void testEqualsNotEquals() throws Exception {
    Worker anotherWorker = new Worker("somebody else", qualificationSet);
    assertNotEquals(worker, anotherWorker);
  }

  @Theory
  public void testEqualsNotWorker() {
    assertNotEquals(worker, 11);
  }

  @Theory
  public void testToString() {
    assertTrue(worker.toString().contains(NAME));
  }

  @Theory
  public void testGetCompany() {
    assertEquals(company, worker.getCompany());
  }

  @Theory
  public void testAddProject() throws Exception {
    Qualification qualification = new Qualification("somethingHelpful");

    Set<Qualification> qualificationSet = Collections.singleton(qualification);

    Worker worker = new Worker(NAME, qualificationSet);

    Project project =
        new Project("project", ProjectSize.LARGE, ProjectStatus.ACTIVE, qualificationSet);
    worker.addProject(project);
    assertEquals(worker.getProjects().size(), 1);
    assertTrue(worker.getProjects().contains(project));
  }

  @Theory
  public void testAddProjectNullProject() {
    assertThrows(NullPointerException.class, () -> worker.addProject(null));
  }

  @Theory
  public void testWillOverload(Pair<Project[], Integer> pair) throws Exception {
    Qualification qualification = new Qualification("somethingHelpful");

    Set<Qualification> qualificationSet = Collections.singleton(qualification);

    Worker worker = new Worker(NAME, qualificationSet);
    for (Project project : pair.getKey()) {
      worker.addProject(project);
    }

    Project lastProject = pair.getKey()[pair.getKey().length - 1];
    if (pair.getValue() > 12) {
      assertTrue(worker.willOverload(lastProject));
    } else {
      assertFalse(worker.willOverload(lastProject));
    }
  }
}
