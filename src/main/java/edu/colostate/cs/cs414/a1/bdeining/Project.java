package edu.colostate.cs.cs414.a1.bdeining;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Project {

    private String name;

    private ProjectSize projectSize;

    private ProjectStatus projectStatus;

    private Set<Worker> workers;

    private Set<Qualification> qualifications;

    /**
     * Creates an instance of a project given a valid name, size, status, and qualifications. A name is valid if it
     * is a non-empty string that consists of at least one non-blank character.
     *
     * @param name – the name of the project.
     * @param size – the size of the project.
     * @param status – the status of the project.
     * @param qs – the qualifications required by the project.
     * @throws NullPointerException – if name or qs are null.
     * @throws InvalidName – if name is not a non-empty string that consists of at least one non-blank character.
     * @throws InvalidQualification – if qs is empty.
     */
    public Project(String name, ProjectSize size, ProjectStatus status, Set<Qualification> qs) throws InvalidName, InvalidQualification {
        ValidationUtils.validateName(name);
        ValidationUtils.validateQualificationSet(qs);

        this.name = name;
        this.projectSize = size;
        this.projectStatus = status;
        this.workers = new HashSet<>();
        this.qualifications = qs;
    }

    public String getName() {
        return name;
    }

    public ProjectSize getSize() {
        return projectSize;
    }

    public ProjectStatus getStatus() {
        return projectStatus;
    }

    public void setStatus(ProjectStatus status) {
        this.projectStatus = status;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + name.hashCode();
        result = 31 * result + workers.hashCode();
        result = 31 * result + projectSize.hashCode();
        result = 31 * result + projectStatus.hashCode();
        result = 31 * result + qualifications.hashCode();
        return result;
    }

    /**
     * This operation is needed by JUnit. Note that the parameter of this method is of type Object, i.e., not
     * equals(c : Project), etc. Two Project instances are equal iff their descriptions match. Note
     * that it is good practice to override the hashCode method when equals is overridden.
     *
     * @param obj – the reference object with which to compare.
     * @return true if this object is the same as the o argument; false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Project)) {
            return false;
        }

        return this.name.equals(((Project) obj).name);
    }

    /**
     * Returns a String that concatenates the name of the project, colon, number of assigned workers,
     * colon, status. For example, a project named "CS5Anniv" using 10 assigned workers and status
     * PLANNED will result in CS5Anniv:10:PLANNED. In the string, status is in upper case (as shown in
     * the UML class diagram).
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return String.format("%s:%s:%s", name, getWorkers().size(), getStatus());
    }

    /**
     * Returns the qualifications that are not met by comparing the qualifications required by the project and
     * those that are met by the workers who are assigned to the project. An empty set (not a null set) is
     * returned when all the qualification requirements are met.
     *
     * @return the Set of project qualifications not met by its assigned workers
     */
    public Set<Qualification> missingQualifications() {
        Set<Qualification> unMetQualifications = new HashSet<>();

        Set<Qualification> workerQualifications = workers.stream().flatMap(worker -> worker.getQualification().stream()).collect(Collectors.toSet());

        for (Qualification qualification : qualifications) {
            if (!workerQualifications.contains(qualification)) {
                unMetQualifications.add(qualification);
            }
        }

        return unMetQualifications;
    }

    /**
     * Verifies that at least one of the missing qualification requirements of a project is satisfied by the worker
     * w.
     *
     * @param w - the worker to be analyzed.
     * @return true if at least one of the missing qualification requirements of a project is satisfied by the worker,
     * false otherwise.
     * @throws NullPointerException – if w is null.
     */
    public boolean isHelpful(Worker w) {
        if (w == null) {
            throw new NullPointerException("Worker is null");
        }

        Set<Qualification> unmetQualifications = missingQualifications();
        for (Qualification qualification : w.getQualification()) {
            if (unmetQualifications.contains(qualification)) {
                return true;
            }
        }

        return false;
    }

    public Set<Worker> getWorkers() {
        return workers;
    }

    /**
     * Adds a qualification q to the set of required qualifications of the project.
     *
     * @param q – the qualification to be added.
     * @return true if the qualification is added, false otherwise.
     */
    public boolean addQualification(Qualification q) {
        if (q == null) {
            throw new NullPointerException("Qualification was null");
        }

        return qualifications.add(q);
    }


}
