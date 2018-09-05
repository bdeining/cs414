package edu.colostate.cs.cs414.a1.bdeining;

/** An exception that is thrown when an invalid description field is detected. */
public class InvalidDescription extends Exception {

  /**
   * Creates an InvalidDescription with a given message.
   *
   * @param message - the message tied to the exception
   */
  public InvalidDescription(String message) {
    super(message);
  }
}
