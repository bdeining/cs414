package edu.colostate.cs.cs414.a1.bdeining;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
public class CompanyTest {

  public static final String HELPFUL_QUALIFICATION = "helpful qualification";

  public static final String UNHELPFUL_QUALIFICATION = "unhelpful qualification";

  public static final String WORKER_NAME = "Bob";

  public static final String PROJECT_NAME = "aProject";

  private static final String COMPANY_NAME = "A Company";

  private Company company;

  @BeforeEach
  public void beforeTest() throws InvalidName {
    company = new Company("A Company");
  }

  @Test
  public void testEmptyCompanyName() {
    assertThrows(InvalidName.class, () -> new Company(""));
  }

  @Test
  public void testNullCompanyName() {
    assertThrows(NullPointerException.class, () -> new Company(null));
  }

  @Test
  public void testCompanyNameBlankCharacter() {
    assertThrows(InvalidName.class, () -> new Company(" "));
  }

  @Test
  public void testGetName() {
    assertEquals(company.getName(), COMPANY_NAME);
  }

  @Test
  public void testAddWorker() {
    Worker worker = mock(Worker.class);
    boolean result = company.addToAvailableWorkerPool(worker);
    assertTrue(result);
    assertEquals(company.getAvailableWorkers().size(), 1);
  }

  @Test
  public void testAddWorkerNullWorker() {
    assertThrows(NullPointerException.class, () -> company.addToAvailableWorkerPool(null));
  }

  @Test
  public void testAddWorkerWithAssignedProject() {
    Worker worker = mock(Worker.class);
    when(worker.getProjects()).thenReturn(Collections.singleton(mock(Project.class)));
    boolean result = company.addToAvailableWorkerPool(worker);
    assertFalse(result);
    assertEquals(company.getAvailableWorkers().size(), 0);
  }

  @Test
  public void testCompaniesEqual() throws InvalidName {
    Company anotherCompany = new Company("A Company");
    assertEquals(company, anotherCompany);
  }

  @Test
  public void testCompaniesNotEqual() throws InvalidName {
    Company anotherCompany = new Company("Another Company");
    assertNotEquals(company, anotherCompany);
  }

  @Test
  public void testCompaniesNotEqualNotACompany() {
    assertNotEquals(company, "a string");
  }

  @Test
  public void testCreateProject() throws Exception {
    Set<Qualification> qualifications = Collections.singleton(mock(Qualification.class));
    Project project = company.createProject(PROJECT_NAME, qualifications, ProjectSize.LARGE);
    assertNotNull(project);
    assertEquals(project.getStatus(), ProjectStatus.PLANNED);
    assertEquals(project.getSize(), ProjectSize.LARGE);
    assertEquals(project.getName(), PROJECT_NAME);
    assertTrue(company.getProjects().contains(project));
  }

  @Test
  public void testCreateProjectInvalidProjectName() {
    Set<Qualification> qualifications = Collections.singleton(mock(Qualification.class));
    assertThrows(
        InvalidName.class, () -> company.createProject("", qualifications, ProjectSize.LARGE));
  }

  @Test
  public void testCreateProjectNullProjectName() {
    Set<Qualification> qualifications = Collections.singleton(mock(Qualification.class));
    assertThrows(
        NullPointerException.class,
        () -> company.createProject(null, qualifications, ProjectSize.LARGE));
  }

  @Test
  public void testCreateProjectNullQualificationSet() {
    assertThrows(
        NullPointerException.class,
        () -> company.createProject(PROJECT_NAME, null, ProjectSize.LARGE));
  }

  @Test
  public void testCreateProjectEmptyQualificationSet() {
    assertThrows(
        InvalidQualification.class,
        () -> company.createProject(PROJECT_NAME, Collections.emptySet(), ProjectSize.LARGE));
  }

  @Test
  public void testAssign() throws Exception {
    Qualification qualification = new Qualification(HELPFUL_QUALIFICATION);
    Set<Qualification> qualifications = Collections.singleton(qualification);
    Project project = company.createProject(PROJECT_NAME, qualifications, ProjectSize.LARGE);
    Worker worker = new Worker(WORKER_NAME, qualifications);
    company.addToAvailableWorkerPool(worker);
    boolean result = company.assign(worker, project);
    assertTrue(result);
    assertTrue(company.getAssignedWorkers().contains(worker));
    assertTrue(project.getWorkers().contains(worker));
    assertFalse(company.getUnassignedWorkers().contains(worker));
  }

  @Test
  public void testAssignUnavailableWorker() throws Exception {
    Qualification qualification = new Qualification(HELPFUL_QUALIFICATION);
    Set<Qualification> qualifications = Collections.singleton(qualification);
    Project project = company.createProject(PROJECT_NAME, qualifications, ProjectSize.LARGE);
    Worker worker = new Worker(WORKER_NAME, qualifications);
    boolean result = company.assign(worker, project);
    assertFalse(result);
  }

  @Test
  public void testAssignAlreadyAssignedWorker() throws Exception {
    Qualification qualification = new Qualification(HELPFUL_QUALIFICATION);
    Set<Qualification> qualifications = Collections.singleton(qualification);
    Project project = company.createProject(PROJECT_NAME, qualifications, ProjectSize.LARGE);
    Worker worker = new Worker(WORKER_NAME, qualifications);
    company.addToAvailableWorkerPool(worker);
    boolean result = company.assign(worker, project);
    assertTrue(result);
    result = company.assign(worker, project);
    assertFalse(result);
  }

  @Test
  public void testAssignActiveProject() throws Exception {
    Qualification qualification = new Qualification(HELPFUL_QUALIFICATION);
    Set<Qualification> qualifications = Collections.singleton(qualification);
    Project project = company.createProject(PROJECT_NAME, qualifications, ProjectSize.LARGE);
    Worker worker = new Worker(WORKER_NAME, qualifications);
    company.addToAvailableWorkerPool(worker);
    project.setStatus(ProjectStatus.ACTIVE);
    boolean result = company.assign(worker, project);
    assertFalse(result);
  }

  @Test
  public void testAssignFinishedProject() throws Exception {
    Qualification qualification = new Qualification(HELPFUL_QUALIFICATION);
    Set<Qualification> qualifications = Collections.singleton(qualification);
    Project project = company.createProject(PROJECT_NAME, qualifications, ProjectSize.LARGE);
    Worker worker = new Worker(WORKER_NAME, qualifications);
    company.addToAvailableWorkerPool(worker);
    project.setStatus(ProjectStatus.FINISHED);
    boolean result = company.assign(worker, project);
    assertFalse(result);
  }

  @Test
  public void testAssignOverloadedWorker() throws Exception {
    Qualification qualification = new Qualification(HELPFUL_QUALIFICATION);
    Set<Qualification> qualifications = Collections.singleton(qualification);
    Project project = company.createProject(PROJECT_NAME, qualifications, ProjectSize.LARGE);
    Project project2 = company.createProject(PROJECT_NAME + "2", qualifications, ProjectSize.LARGE);
    Project project3 = company.createProject(PROJECT_NAME + "3", qualifications, ProjectSize.LARGE);
    Project project4 = company.createProject(PROJECT_NAME + "4", qualifications, ProjectSize.LARGE);
    Project project5 = company.createProject(PROJECT_NAME + "5", qualifications, ProjectSize.LARGE);
    Worker worker = new Worker(WORKER_NAME, qualifications);
    company.addToAvailableWorkerPool(worker);
    boolean result = company.assign(worker, project);
    assertTrue(result);
    result = company.assign(worker, project2);
    assertTrue(result);
    result = company.assign(worker, project3);
    assertTrue(result);
    result = company.assign(worker, project4);
    assertTrue(result);
    project.setStatus(ProjectStatus.ACTIVE);
    project2.setStatus(ProjectStatus.ACTIVE);
    project3.setStatus(ProjectStatus.ACTIVE);
    project4.setStatus(ProjectStatus.ACTIVE);
    result = company.assign(worker, project5);
    assertFalse(result);
  }

  @Test
  public void testAssignUnhelpfulWorker() throws Exception {
    Qualification qualification = new Qualification(HELPFUL_QUALIFICATION);
    Qualification workerQualification = new Qualification(UNHELPFUL_QUALIFICATION);
    Set<Qualification> qualifications = Collections.singleton(qualification);
    Set<Qualification> workerQualifications = Collections.singleton(workerQualification);
    Project project = company.createProject(PROJECT_NAME, qualifications, ProjectSize.LARGE);
    Worker worker = new Worker(WORKER_NAME, workerQualifications);
    company.addToAvailableWorkerPool(worker);
    boolean result = company.assign(worker, project);
    assertFalse(result);
  }

  @Test
  public void testAssignNullWorker() {
    assertThrows(NullPointerException.class, () -> company.assign(null, mock(Project.class)));
  }

  @Test
  public void testAssignNullProject() {
    assertThrows(NullPointerException.class, () -> company.assign(mock(Worker.class), null));
  }

  @Test
  public void testUnassign() throws Exception {
    Qualification qualification = new Qualification(HELPFUL_QUALIFICATION);
    Set<Qualification> qualifications = Collections.singleton(qualification);
    Project project = company.createProject(PROJECT_NAME, qualifications, ProjectSize.LARGE);
    Worker worker = new Worker(WORKER_NAME, qualifications);
    company.addToAvailableWorkerPool(worker);
    company.assign(worker, project);
    boolean result = company.unassign(worker, project);
    assertTrue(result);
    assertFalse(project.getWorkers().contains(worker));
    assertFalse(company.getAssignedWorkers().contains(worker));
  }

  @Test
  public void testUnassignSuspendProject() throws Exception {
    Qualification qualification = new Qualification(HELPFUL_QUALIFICATION);
    Set<Qualification> qualifications = Collections.singleton(qualification);
    Project project = company.createProject(PROJECT_NAME, qualifications, ProjectSize.LARGE);
    Worker worker = new Worker(WORKER_NAME, qualifications);
    company.addToAvailableWorkerPool(worker);
    company.assign(worker, project);
    project.setStatus(ProjectStatus.ACTIVE);
    boolean result = company.unassign(worker, project);
    assertTrue(result);
    assertFalse(project.getWorkers().contains(worker));
    assertFalse(company.getAssignedWorkers().contains(worker));
    assertEquals(project.getStatus(), ProjectStatus.SUSPENDED);
  }

  @Test
  public void testUnassignMultipleProjects() throws Exception {
    Qualification qualification = new Qualification(HELPFUL_QUALIFICATION);
    Set<Qualification> qualifications = Collections.singleton(qualification);
    Project project = company.createProject(PROJECT_NAME, qualifications, ProjectSize.LARGE);
    Project project2 = company.createProject(PROJECT_NAME + "2", qualifications, ProjectSize.LARGE);
    Worker worker = new Worker(WORKER_NAME, qualifications);
    company.addToAvailableWorkerPool(worker);
    company.assign(worker, project);
    company.assign(worker, project2);
    boolean result = company.unassign(worker, project);
    assertTrue(result);
    assertFalse(project.getWorkers().contains(worker));
    assertTrue(company.getAssignedWorkers().contains(worker));
  }

  @Test
  public void testUnassignWorkerNotOnProject() throws Exception {
    Qualification qualification = new Qualification(HELPFUL_QUALIFICATION);
    Set<Qualification> qualifications = Collections.singleton(qualification);
    Project project = company.createProject(PROJECT_NAME, qualifications, ProjectSize.LARGE);
    Worker worker = mock(Worker.class);
    company.addToAvailableWorkerPool(worker);
    boolean result = company.unassign(worker, project);
    assertFalse(result);
  }

  @Test
  public void testUnassignNullProject() {
    assertThrows(NullPointerException.class, () -> company.unassign(mock(Worker.class), null));
  }

  @Test
  public void testUnassignNullWorker() {
    assertThrows(NullPointerException.class, () -> company.unassign(null, mock(Project.class)));
  }

  @Test
  public void testHashCode() {
    assertNotEquals(company.hashCode(), 0);
  }

  @Test
  public void testUnassignAll() throws Exception {
    Qualification qualification = new Qualification(HELPFUL_QUALIFICATION);
    Set<Qualification> qualifications = Collections.singleton(qualification);
    Project project = company.createProject(PROJECT_NAME, qualifications, ProjectSize.LARGE);
    Project project2 = company.createProject(PROJECT_NAME + "2", qualifications, ProjectSize.LARGE);
    Worker worker = mock(Worker.class);
    Set<Project> projects = new HashSet<>();
    projects.add(project);
    projects.add(project2);
    company.addToAvailableWorkerPool(worker);
    company.assign(worker, project);
    company.assign(worker, project2);
    when(worker.getProjects()).thenReturn(projects);
    company.unassignAll(worker);
    assertTrue(project.getWorkers().isEmpty());
    assertTrue(project2.getWorkers().isEmpty());
    assertTrue(company.getUnassignedWorkers().contains(worker));
    assertFalse(company.getAssignedWorkers().contains(worker));
  }

  @Test
  public void testStart() throws Exception {
    Qualification qualification = new Qualification(HELPFUL_QUALIFICATION);
    Set<Qualification> qualifications = Collections.singleton(qualification);
    Project project = company.createProject(PROJECT_NAME, qualifications, ProjectSize.LARGE);
    Worker worker = new Worker(WORKER_NAME, qualifications);
    company.addToAvailableWorkerPool(worker);
    company.assign(worker, project);
    boolean result = company.start(project);
    assertTrue(result);
    assertEquals(project.getStatus(), ProjectStatus.ACTIVE);
  }

  @Test
  public void testStartUnmetQualifications() throws Exception {
    Qualification qualification = new Qualification(HELPFUL_QUALIFICATION);
    Qualification workerQualification = new Qualification(UNHELPFUL_QUALIFICATION);
    Set<Qualification> qualifications = Collections.singleton(qualification);
    Set<Qualification> workerQualifications = Collections.singleton(workerQualification);
    Project project = company.createProject(PROJECT_NAME, workerQualifications, ProjectSize.LARGE);
    Worker worker = new Worker(WORKER_NAME, qualifications);
    company.addToAvailableWorkerPool(worker);
    company.assign(worker, project);
    boolean result = company.start(project);
    assertFalse(result);
    assertEquals(project.getStatus(), ProjectStatus.PLANNED);
  }

  @Test
  public void testStartFinishedProject() throws Exception {
    Qualification qualification = new Qualification(HELPFUL_QUALIFICATION);
    Set<Qualification> qualifications = Collections.singleton(qualification);
    Project project = company.createProject(PROJECT_NAME, qualifications, ProjectSize.LARGE);
    project.setStatus(ProjectStatus.FINISHED);
    boolean result = company.start(project);
    assertFalse(result);
  }

  @Test
  public void testStartActiveProject() throws Exception {
    Qualification qualification = new Qualification(HELPFUL_QUALIFICATION);
    Set<Qualification> qualifications = Collections.singleton(qualification);
    Project project = company.createProject(PROJECT_NAME, qualifications, ProjectSize.LARGE);
    project.setStatus(ProjectStatus.ACTIVE);
    boolean result = company.start(project);
    assertFalse(result);
  }

  @Test
  public void testStartNullProject() {
    assertThrows(NullPointerException.class, () -> company.start(null));
  }

  @Test
  public void testToString() {
    assertTrue(company.toString().contains(company.getName()));
  }

  @Test
  public void testFinish() throws Exception {
    Qualification qualification = new Qualification(HELPFUL_QUALIFICATION);
    Set<Qualification> qualifications = Collections.singleton(qualification);
    Project project = company.createProject(PROJECT_NAME, qualifications, ProjectSize.LARGE);
    Worker worker = new Worker(WORKER_NAME, qualifications);
    company.addToAvailableWorkerPool(worker);
    company.assign(worker, project);
    company.start(project);
    assertEquals(project.getStatus(), ProjectStatus.ACTIVE);
    company.finish(project);
    assertEquals(project.getStatus(), ProjectStatus.FINISHED);
  }

  @Test
  public void testFinishProjectFinished() throws Exception {
    Qualification qualification = new Qualification(HELPFUL_QUALIFICATION);
    Set<Qualification> qualifications = Collections.singleton(qualification);
    Project project = company.createProject(PROJECT_NAME, qualifications, ProjectSize.LARGE);
    project.setStatus(ProjectStatus.FINISHED);
    company.finish(project);
    assertEquals(project.getStatus(), ProjectStatus.FINISHED);
  }

  @Test
  public void testFinishWithAssignedWorkers() throws Exception {
    Qualification qualification = new Qualification(HELPFUL_QUALIFICATION);
    Set<Qualification> qualifications = Collections.singleton(qualification);
    Project project = company.createProject(PROJECT_NAME, qualifications, ProjectSize.LARGE);
    Project project2 = company.createProject(PROJECT_NAME + "2", qualifications, ProjectSize.LARGE);
    Worker worker = new Worker(WORKER_NAME, qualifications);
    company.addToAvailableWorkerPool(worker);
    company.assign(worker, project);
    company.assign(worker, project2);
    company.start(project);
    assertEquals(project.getStatus(), ProjectStatus.ACTIVE);
    company.finish(project);
    assertEquals(project.getStatus(), ProjectStatus.FINISHED);
    assertTrue(worker.getProjects().contains(project2));
    assertFalse(worker.getProjects().contains(project));
  }

  @Test
  public void testFinishWithAssignedWorkersAvailable() throws Exception {
    Qualification qualification = new Qualification(HELPFUL_QUALIFICATION);
    Set<Qualification> qualifications = Collections.singleton(qualification);
    Project project = company.createProject(PROJECT_NAME, qualifications, ProjectSize.LARGE);
    Worker worker = new Worker(WORKER_NAME, qualifications);
    company.addToAvailableWorkerPool(worker);
    company.assign(worker, project);
    company.start(project);
    assertEquals(project.getStatus(), ProjectStatus.ACTIVE);
    company.finish(project);
    assertEquals(project.getStatus(), ProjectStatus.FINISHED);
    assertFalse(worker.getProjects().contains(project));
    assertTrue(company.getAvailableWorkers().contains(worker));
  }

  @Test
  public void testFinishNullProject() {
    assertThrows(NullPointerException.class, () -> company.finish(null));
  }
}
