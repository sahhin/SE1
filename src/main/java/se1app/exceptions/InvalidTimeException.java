package se1app.exceptions;


/** Exception to be thrown when a time parser failure occured. */
public class InvalidTimeException extends IllegalArgumentException {

  private static final long serialVersionUID = 8510499809729660355L;


  /** Create a new exception.
   * @param invalidTime The input which was parsed as invalid time. */
  public InvalidTimeException(String invalidTime) {
    super("'"+invalidTime+"' is no valid time!");
  }
}
