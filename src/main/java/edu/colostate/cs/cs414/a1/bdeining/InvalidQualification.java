package edu.colostate.cs.cs414.a1.bdeining;

/** An exception that is thrown when an invalid qualification field is detected. */
public class InvalidQualification extends Exception {

  /**
   * Creates an InvalidQualification with a given message.
   *
   * @param message - the message tied to the exception
   */
  public InvalidQualification(String message) {
    super(message);
  }
}
