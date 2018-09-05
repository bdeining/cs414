package edu.colostate.cs.cs414.a1.bdeining;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Worker {

  private String name;

  private double salary;

  private Set<Qualification> qualifications;

  private Company company;

  private Set<Project> projects;

  /**
   * Creates a new worker with the given a valid name and qualifications. A name is valid if it is a
   * non-empty string that consists of at least one non-blank character. Default salary is 0.
   *
   * @param name - the name of the worker.
   * @param qs - the worker’s qualifications.
   * @throws NullPointerException – if name or qs are null.
   * @throws InvalidName – if name is not a non-empty string that consists of at least one non-blank
   *     character.
   * @throws InvalidQualification – if qs is empty.
   */
  public Worker(String name, Set<Qualification> qs) throws InvalidName, InvalidQualification {
    ValidationUtils.validateName(name);
    ValidationUtils.validateQualificationSet(qs);

    this.name = name;
    this.salary = 0;
    this.projects = new HashSet<>();
    this.qualifications = qs;
    this.projects = new HashSet<>();
  }

  public String getName() {
    return name;
  }

  public double getSalary() {
    return salary;
  }

  public void setSalary(double salary) {
    this.salary = salary;
  }

  public Set<Qualification> getQualification() {
    return qualifications;
  }

  /**
   * Adds the qualification q to the set of qualifications of the worker.
   *
   * @param q – the qualification to be added.
   * @return true if the qualification was added; false otherwise.
   * @throws NullPointerException - if q is null
   */
  public boolean addQualification(Qualification q) {
    if (q == null) {
      throw new NullPointerException("Qualification was null");
    }

    return qualifications.add(q);
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + name.hashCode();
    return result;
  }

  /**
   * Two Worker instances are equal iff their names match. Note that it is good practice to override
   * the hashCode method when equals is overridden.
   *
   * @param obj - the reference object with which to compare.
   * @return true if this object is the same as the o argument; false otherwise.
   */
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Worker)) {
      return false;
    }

    return this.getName().equals(((Worker) obj).getName());
  }

  /**
   * Returns a String that concatenates the name, colon, number of projects, colon, number of
   * qualifications, colon, salary. For example, a worker named "Nick", working on 2 projects, and
   * having 10 qualifications and a salary of 10000 will result in the string Nick:2:10:10000.
   *
   * @return a string representation of the object.
   */
  @Override
  public String toString() {
    return String.format("%s:%s:%s:%s", name, projects.size(), qualifications.size(), salary);
  }

  /** @return */
  private int computeCurrentLoad() {
    final AtomicInteger currentLoad = new AtomicInteger(0);
    projects
            .stream()
            .filter(project -> project.getStatus().equals(ProjectStatus.ACTIVE))
            .forEach(project -> currentLoad.addAndGet(getProjectLoad(project.getSize())));

    return currentLoad.get();
  }

  /**
   * @param size
   * @return
   */
  private int getProjectLoad(ProjectSize size) {

    switch (size) {
    case LARGE:
      return 3;

    case MEDIUM:
      return 2;

    case SMALL:
      return 1;

      default:
        return 0;
    }
  }

  /**
   * Verifies if the worker will overload by assigning him to a project p. A constraint for the
   * entire system is that no worker should ever be overloaded. To determine overloading, consider
   * all the ACTIVE projects of the worker. If adding a new project p to the current project set of
   * the worker makes (3*numberOfLargeProjects + 2*numberOfMediumProjects + numberOfSmall Projects)
   * greater than 12 when p becomes active, then the worker will be overloaded.
   *
   * @param p
   * @return true if a worker will be overloaded when assigned to the project p, false otherwise.
   */
  public boolean willOverload(Project p) {
    int currentLoad = computeCurrentLoad();
    int tentativeSize = getProjectLoad(p.getSize());

    return (currentLoad + tentativeSize) > 12;
  }

  public Set<Project> getProjects() {
    return projects;
  }

  /**
   * @param p
   * @return
   * @throws NullPointerException
   */
  public boolean addProject(Project p) {
    if (p == null) {
      throw new NullPointerException("Project was null");
    }

    return projects.add(p);
  }

  public void setCompany(Company c) {
    this.company = c;
  }

  public Company getCompany() {
    return this.company;
  }
}
