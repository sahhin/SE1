package se1app.datatypes;

import javax.persistence.Embeddable;
import se1app.exceptions.InvalidEmailException;


/** Data type for an e-mail address compliant to RFC 5322. */
@Embeddable
public class EmailType {

  private static final String emailRegex =
    "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
  private String _email;


  /** Create an e-mail address data type.
   * @param email The input to transform to an e-mail address.
   * @throws InvalidEmailException Thrown if input is no valid e-mail. */
  public EmailType(String email) throws InvalidEmailException {
    if (isValid(email)) _email = email;
    else {
      throw new InvalidEmailException(email);
    }
  }


  /** Empty constructor for Hibernate. */
  public EmailType() {}


  /** Get the e-mail address.
   * @return The e-mail address. */
  public String getEmail() {
    return _email;
  }


  /** Check if a given string is a RFC 5322 compliant e-mail address.
   * @param email The string to check.
   * @return 'true' if the input is a valid e-mail, 'false' otherwise. */
  public static boolean isValid(String email) {
    return email.matches(emailRegex);
  }
}
