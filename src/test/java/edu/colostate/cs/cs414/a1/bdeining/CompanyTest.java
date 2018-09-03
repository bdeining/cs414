package edu.colostate.cs.cs414.a1.bdeining;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;


@RunWith(JUnitPlatform.class)
public class CompanyTest {

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
        assertThrows(NullPointerException.class, () -> new Company( null));
    }

    @Test
    public void testCompanyNameBlankCharacter() {
        assertThrows(InvalidName.class, () -> new Company( "The\\sCompany"));
    }

    @Test
    public void testGetName() throws InvalidName {
       String companyName = "Amazon";
       Company company = new Company(companyName);
       assertEquals(company.getName(), companyName);
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
        Project project = company.createProject("aProject", qualifications, ProjectSize.LARGE);
        assertNotNull(project);
        assertEquals(project.getStatus(), ProjectStatus.PLANNED);
        assertEquals(project.getSize(), ProjectSize.LARGE);
        assertEquals(project.getName(), "aProject");
        assertTrue(company.getProjects().contains(project));
    }

    @Test
    public void testCreateProjectInvalidProjectName() {
        Set<Qualification> qualifications = Collections.singleton(mock(Qualification.class));
        assertThrows(InvalidName.class, () -> company.createProject("", qualifications, ProjectSize.LARGE));
    }

    @Test
    public void testCreateProjectNullProjectName() {
        Set<Qualification> qualifications = Collections.singleton(mock(Qualification.class));
        assertThrows(NullPointerException.class, () -> company.createProject(null, qualifications, ProjectSize.LARGE));
    }

    @Test
    public void testCreateProjectNullQualificationSet() {
        assertThrows(NullPointerException.class, () -> company.createProject("aProject", null, ProjectSize.LARGE));
    }

    @Test
    public void testCreateProjectEmptyQualificationSet() {
        assertThrows(InvalidQualification.class, () -> company.createProject("aProject", Collections.emptySet(), ProjectSize.LARGE));
    }

    @Test
    public void testAssign() throws Exception {
        Set<Qualification> qualifications = Collections.singleton(mock(Qualification.class));
        Project project = company.createProject("aProject", qualifications, ProjectSize.LARGE);
        Worker worker = mock(Worker.class);
        company.addToAvailableWorkerPool(worker);
        boolean result = company.assign(worker, project);
        assertTrue(result);
        assertTrue(company.getAssignedWorkers().contains(worker));
        assertTrue(project.getWorkers().contains(worker));
        assertFalse(company.getUnassignedWorkers().contains(worker));
    }

    @Test
    public void testAssignUnavailableWorker() throws Exception {
        Set<Qualification> qualifications = Collections.singleton(mock(Qualification.class));
        Project project = company.createProject("aProject", qualifications, ProjectSize.LARGE);
        Worker worker = mock(Worker.class);
        boolean result = company.assign(worker, project);
        assertFalse(result);
    }

    @Test
    public void testAssignAlreadyAssignedWorker() throws Exception {
        Set<Qualification> qualifications = Collections.singleton(mock(Qualification.class));
        Project project = company.createProject("aProject", qualifications, ProjectSize.LARGE);
        Worker worker = mock(Worker.class);
        company.addToAvailableWorkerPool(worker);
        company.assign(worker, project);
        boolean result = company.assign(worker, project);
        assertFalse(result);
    }

    @Test
    public void testAssignActiveProject() throws Exception {
        Set<Qualification> qualifications = Collections.singleton(mock(Qualification.class));
        Project project = company.createProject("aProject", qualifications, ProjectSize.LARGE);
        Worker worker = mock(Worker.class);
        company.addToAvailableWorkerPool(worker);
        project.setStatus(ProjectStatus.ACTIVE);
        boolean result = company.assign(worker, project);
        assertFalse(result);
    }

    @Test
    public void testAssignFinishedProject() throws Exception {
        Set<Qualification> qualifications = Collections.singleton(mock(Qualification.class));
        Project project = company.createProject("aProject", qualifications, ProjectSize.LARGE);
        Worker worker = mock(Worker.class);
        company.addToAvailableWorkerPool(worker);
        project.setStatus(ProjectStatus.FINISHED);
        boolean result = company.assign(worker, project);
        assertFalse(result);
    }

    @Test
    public void testAssignOverloadedWorker() throws Exception {
        Set<Qualification> qualifications = Collections.singleton(mock(Qualification.class));
        Project project = company.createProject("aProject", qualifications, ProjectSize.LARGE);
        Worker worker = mock(Worker.class);
        when(worker.willOverload(any(Project.class))).thenReturn(true);
        company.addToAvailableWorkerPool(worker);
        boolean result = company.assign(worker, project);
        assertFalse(result);
    }


/*    @Test TODO
    public void testAssignUnhelpfulWorker() throws Exception {
        Set<Qualification> qualifications = Collections.singleton(mock(Qualification.class));
        Project project = company.createProject("aProject", qualifications, ProjectSize.LARGE);
        Worker worker = mock(Worker.class);

        company.addToAvailableWorkerPool(worker);
        boolean result = company.assign(worker, project);
        assertFalse(result);
    }*/


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
        Set<Qualification> qualifications = Collections.singleton(mock(Qualification.class));
        Project project = company.createProject("aProject", qualifications, ProjectSize.LARGE);
        Worker worker = mock(Worker.class);
        company.addToAvailableWorkerPool(worker);
        company.assign(worker, project);
        boolean result =  company.unassign(worker, project);
        assertTrue(result);
        assertFalse(project.getWorkers().contains(worker));
        assertFalse(company.getAssignedWorkers().contains(worker));
    }

    @Test
    public void testUnassignMultipleProjects() throws Exception {
        Set<Qualification> qualifications = Collections.singleton(mock(Qualification.class));
        Project project = company.createProject("aProject", qualifications, ProjectSize.LARGE);
        Project project2 = company.createProject("aProject2", qualifications, ProjectSize.LARGE);
        Worker worker = mock(Worker.class);
        Set<Project> projects = new HashSet<>();
        projects.add(project);
        projects.add(project2);
        company.addToAvailableWorkerPool(worker);
        company.assign(worker, project);
        company.assign(worker, project2);
        when(worker.getProjects()).thenReturn(projects);
        boolean result =  company.unassign(worker, project);
        assertTrue(result);
        assertFalse(project.getWorkers().contains(worker));
        assertTrue(company.getAssignedWorkers().contains(worker));
    }

    @Test
    public void testUnassignWorkerNotOnProject() throws Exception {
        Set<Qualification> qualifications = Collections.singleton(mock(Qualification.class));
        Project project = company.createProject("aProject", qualifications, ProjectSize.LARGE);
        Worker worker = mock(Worker.class);
        company.addToAvailableWorkerPool(worker);
        boolean result =  company.unassign(worker, project);
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
        Set<Qualification> qualifications = Collections.singleton(mock(Qualification.class));
        Project project = company.createProject("aProject", qualifications, ProjectSize.LARGE);
        Project project2 = company.createProject("aProject2", qualifications, ProjectSize.LARGE);
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
    }

    @Test
    public void testStart() throws Exception {
        Set<Qualification> qualifications = Collections.singleton(mock(Qualification.class));
        Project project = company.createProject("aProject", qualifications, ProjectSize.LARGE);
        boolean result = company.start(project);
        assertTrue(result);
        assertEquals(project.getStatus(), ProjectStatus.ACTIVE);
    }

    @Test
    public void testStartFinishedProject() throws Exception {
        Set<Qualification> qualifications = Collections.singleton(mock(Qualification.class));
        Project project = company.createProject("aProject", qualifications, ProjectSize.LARGE);
        project.setStatus(ProjectStatus.FINISHED);
        boolean result = company.start(project);
        assertFalse(result);
    }

    @Test
    public void testStartNullProject() {
        assertThrows(NullPointerException.class, () -> company.start(null));
    }

}
