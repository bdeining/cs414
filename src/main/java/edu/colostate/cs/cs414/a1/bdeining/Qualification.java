package edu.colostate.cs.cs414.a1.bdeining;

import java.util.HashSet;
import java.util.Set;

public class Qualification {

    private String description;

    private Set<Worker> workers;

    private Set<Project> projects;

    /**
     * Creates a new instance of qualification and sets its description, if the description is valid. A valid
     * description is a non-empty string that consists of at least one non-blank character.
     *
     * @param d – the description of the qualification.
     * @throws InvalidDescription - if description is not a non-empty string that consists of at least one
     * non-blank character.
     * @throws NullPointerException - if description is null.
     */
    public Qualification(String d) throws InvalidDescription {
        ValidationUtils.validateDescription(d);

        this.description = d;
        workers = new HashSet<>();
        projects = new HashSet<>();
    }

    @Override
    public int hashCode() {
        return 0;
    }

    /**
     * This operation is needed by JUnit. Note that the parameter of this method is of type Object, i.e., not
     * equals(c : Qualification), etc. Two Qualification instances are equal iff their
     * descriptions match. Note that it is good practice to override the hashCode method when equals is
     * overridden.
     *
     * @param obj - the reference object with which to compare.
     * @return true if this object is the same as the o argument; false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Qualification)) {
            return false;
        }

        return description.equals(((Qualification) obj).description);
    }

    @Override
    public String toString() {
        return description;
    }

    /**
     * Adds the worker w to the Set of workers having the current qualification.
     *
     * @param w - the worker
     * @throws NullPointerException - if w is null
     */
    public void addWorker(Worker w) {
        if (w == null) {
            throw new NullPointerException("Worker is null");
        }

        workers.add(w);
    }

    /**
     * Adds the project p to the Set of projects requiring the current qualification.
     *
     * @param p - the project
     * @throws NullPointerException - if p is null
     */
    public void addProject(Project p) {
        if (p == null) {
            throw new NullPointerException("Project is null");
        }

        projects.add(p);
    }
}
