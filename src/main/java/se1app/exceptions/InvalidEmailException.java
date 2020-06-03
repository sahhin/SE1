package se1app.exceptions;


/** Exception to be thrown when a e-mail address parser failure occured. */
public class InvalidEmailException extends IllegalArgumentException {

  private static final long serialVersionUID = 8510499809729660355L;


  /** Create a new exception.
   * @param invalidAddress The input which was parsed as invalid mail address. */
  public InvalidEmailException(String invalidAddress) {
    super("'"+invalidAddress+"' is no valid e-mail address!");
  }
}
