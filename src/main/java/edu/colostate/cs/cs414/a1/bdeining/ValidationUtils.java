package edu.colostate.cs.cs414.a1.bdeining;

import java.util.Set;

public class ValidationUtils {

  public static void validateName(String name) throws InvalidName {
    if (name == null) {
      throw new NullPointerException("Name is null");
    }

    if (name.equals("") || name.trim().isEmpty()) {
      throw new InvalidName("Name contained a blank character");
    }
  }

  public static void validateDescription(String description) throws InvalidDescription {
    if (description == null) {
      throw new NullPointerException("Description is null");
    }

    if (description.equals("") || description.trim().isEmpty()) {
      throw new InvalidDescription("Description contained a blank character");
    }
  }

  public static void validateQualificationSet(Set<Qualification> qs) throws InvalidQualification {
    if (qs == null) {
      throw new NullPointerException("");
    }

    if (qs.size() == 0) {
      throw new InvalidQualification("Qualification set is empty");
    }
  }
}
