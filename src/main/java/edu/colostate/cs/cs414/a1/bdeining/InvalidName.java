package edu.colostate.cs.cs414.a1.bdeining;

/** An exception that is thrown when an invalid name field is detected. */
public class InvalidName extends Exception {

  /**
   * Creates an InvalidName with a given message.
   *
   * @param message - the message tied to the exception
   */
  public InvalidName(String message) {
    super(message);
  }
}
