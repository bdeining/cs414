package edu.colostate.cs.cs414.a1.bdeining;

import java.util.HashSet;
import java.util.Set;

public class Company {

  private String name;

  private Set<Worker> availableWorkers;

  private Set<Worker> assignedWorkers;

  private Set<Worker> unassignedWorkers;

  private Set<Project> projects;

  /**
   * Creates a company instance and sets its name. A valid name is a non-empty string that consists
   * of at least one non-blank character.
   *
   * @param name - the name of the company
   * @throws InvalidName when the name is empty or contains one non-blank character
   * @throws NullPointerException when the name is null
   */
  public Company(String name) throws InvalidName {
    ValidationUtils.validateName(name);
    this.name = name;
    this.availableWorkers = new HashSet<>();
    this.assignedWorkers = new HashSet<>();
    this.unassignedWorkers = new HashSet<>();
    this.projects = new HashSet<>();
  }

  public String getName() {
    return name;
  }

  public Set<Worker> getAvailableWorkers() {
    return availableWorkers;
  }

  public Set<Worker> getAssignedWorkers() {
    return assignedWorkers;
  }

  public Set<Worker> getUnassignedWorkers() {
    return unassignedWorkers;
  }

  public Set<Project> getProjects() {
    return projects;
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + name.hashCode();
    return result;
  }

  /**
   * Two Company instances are equal iff their names match.
   *
   * @param obj - the reference object with which to compare.
   * @return true if this object is the same as the o argument; false otherwise.
   */
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Company)) {
      return false;
    }

    return (((Company) obj).name).equals(name);
  }

  /**
   * Returns a String concatenating company name, colon, number of available workers, colon, number
   * of projects carried out. For example, a company named ABC that has 20 available workers and 10
   * projects will result in the string ABC:20:10.
   *
   * @return a string representation of the object
   */
  @Override
  public String toString() {
    return String.format("%s:%s:%s", name, availableWorkers.size(), projects.size());
  }

  /**
   * Adds a worker w to the pool of available workers, if the worker is not already there and if the
   * worker has no assigned projects.
   *
   * @param w - the worker to be added
   * @return true if the worker was added; false otherwise.
   * @throws NullPointerException - if w is null
   */
  public boolean addToAvailableWorkerPool(Worker w) {
    if (w == null) {
      throw new NullPointerException("Worker was null");
    }

    if (w.getProjects().size() > 0) {
      return false;
    }

    return availableWorkers.add(w);
  }

  /**
   * Creates a new project with the given a valid name, qualifications and size, and marks the
   * project as PLANNED. A valid name is a non-empty string that consists of at least one non-blank
   * character. Adds the new project to the projects carried out by the company. Adds each
   * qualification in qs to the project’s qualifications.
   *
   * @param n - the name of the project.
   * @param qs - the set of qualifications required by the project
   * @param size – the size of the project
   * @return - the new project
   * @throws NullPointerException - if n or qs are null.
   * @throws InvalidName – if name is not a non-empty string that consists of at least one non-blank
   *     character.
   * @throws InvalidQualification if qs is empty.
   */
  public Project createProject(String n, Set<Qualification> qs, ProjectSize size)
      throws InvalidName, InvalidQualification {
    ValidationUtils.validateName(name);
    ValidationUtils.validateQualificationSet(qs);

    Project project = new Project(n, size, ProjectStatus.PLANNED, qs);
    projects.add(project);
    return project;
  }

  /**
   * Assigns a worker w to the project p. Only a worker from the pool of available workers can be
   * assigned to a project, as long as the worker is not already assigned to the same project. The
   * project must not be in the ACTIVE or FINISHED state. The worker should not get overloaded by
   * the assignment. The worker can be added iff the worker is helpful to the project. If all the
   * conditions are satisfied, (i) the assigned worker is added to the pool of assigned workers of
   * the company unless the worker is already present in that pool, and (ii) the worker is also
   * added to the project. This results in at least one previously unmet required qualification of
   * the project being met. Note that the same worker can be in both the available pool and assigned
   * pool of workers at the same time. However, the worker cannot be in the assigned pool if the
   * worker is not in the available pool. Think of the available pool as the pool of employed
   * workers.
   *
   * @param w - the worker to be assigned.
   * @param p - the project to assign.
   * @return true if the worker was assigned to the project; false otherwise.
   * @throws NullPointerException – if w or p are null
   */
  public boolean assign(Worker w, Project p) {
    if (w == null || p == null) {
      throw new NullPointerException(String.format("Worker or Project was null : %s, %s", w, p));
    }

    if (!availableWorkers.contains(w)) {
      return false;
    }

    if (p.getWorkers().contains(w)) {
      return false;
    }

    if (p.getStatus() == ProjectStatus.ACTIVE || p.getStatus() == ProjectStatus.FINISHED) {
      return false;
    }

    if (w.willOverload(p)) {
      return false;
    }

    if (!p.isHelpful(w)) {
      return false;
    }

    assignedWorkers.add(w);
    p.getWorkers().add(w);
    w.addProject(p);
    return true;
  }

  /**
   * Removes a worker w from the project p. A worker must have been assigned to a project to be
   * unassigned from it. If this was the only project for the worker, then this worker needs to be
   * removed from the pool of assigned workers of the company. If the qualification requirements of
   * an ACTIVE project are no longer met, that project is marked as SUSPENDED. A PLANNED or
   * SUSPENDED project remains in that state.
   *
   * @param w - the worker to be unassigned.
   * @param p - the project to unassign.
   * @return true if the worker was unassigned from the project; false otherwise.
   * @throws NullPointerException – if w or p are null.
   */
  public boolean unassign(Worker w, Project p) {
    if (w == null || p == null) {
      throw new NullPointerException(String.format("Worker or Project was null : %s, %s", w, p));
    }

    if (!p.getWorkers().contains(w)) {
      return false;
    }

    p.getWorkers().remove(w);
    w.getProjects().remove(p);

    if (w.getProjects().isEmpty()) {
      assignedWorkers.remove(w);
    }

    if (p.getStatus().equals(ProjectStatus.ACTIVE) && !p.missingQualifications().isEmpty()) {
      p.setStatus(ProjectStatus.SUSPENDED);
    }

    return true;
  }

  /**
   * Removes a worker w from all the projects that are assigned to the worker. It also removes the
   * worker from the pool of assigned workers of the company. The state of the affected projects
   * must be changed as needed.
   *
   * @param w - the worker to be unassigned.
   * @throws NullPointerException - if w is null
   */
  public void unassignAll(Worker w) {
    Set<Project> projects = w.getProjects();
    for (Project p : projects) {
      unassign(w, p);
    }
  }

  /**
   * Starts a PLANNED or SUSPENDED project as long as the project's qualification requirements are
   * all satisfied. The project shifts to an ACTIVE status. Otherwise, the project remains PLANNED
   * or SUSPENDED (i.e., as it was before the method was called).
   *
   * @param p – the project to be started.
   * @return true if the project is successfully started; false otherwise.
   * @throws NullPointerException - if p is null
   */
  public boolean start(Project p) {
    if (p == null) {
      throw new NullPointerException("Project was null");
    }

    if (p.getStatus().equals(ProjectStatus.ACTIVE)
        || p.getStatus().equals(ProjectStatus.FINISHED)) {
      return false;
    }

    if (!p.missingQualifications().isEmpty()) {
      return false;
    }

    p.setStatus(ProjectStatus.ACTIVE);
    return true;
  }

  /**
   * Marks an ACTIVE project as FINISHED. The project no longer has any assigned workers, so if a
   * worker was only involved in this project, the worker must be removed from the pool of assigned
   * workers of the company. A SUSPENDED or PLANNED project remains as it was.
   *
   * @param p - the project to be finished.
   * @return true if the project is successfully finished; false otherwise.
   * @throws NullPointerException - if p is null
   */
  public boolean finish(Project p) {
    if (p == null) {
      throw new NullPointerException("Project was null");
    }

    if (!p.getStatus().equals(ProjectStatus.ACTIVE)) {
      return false;
    }

    Set<Worker> workers = p.getWorkers();
    for (Worker w : workers) {
      w.getProjects().remove(p);
      if (w.getProjects().isEmpty()) {
        assignedWorkers.remove(w);
      }
    }

    p.setStatus(ProjectStatus.FINISHED);
    return true;
  }
}
