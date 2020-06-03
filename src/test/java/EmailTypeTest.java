import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import se1app.datatypes.EmailType;
import se1app.exceptions.InvalidEmailException;


public class EmailTypeTest {


  /** Positive e-mail address creation test.
   * @param email E-mail address to test (automaticlly filled from value source). */
  @ParameterizedTest
  @ValueSource(strings = {
    "the_champ@hotmail.de",
    "citizenl@outlook.com",
    "dburrows43@att.net",
    "babo-1234@haw-hamburg.de"
  })
  public void createEmailFromStringSuccess(String email) {
    new EmailType(email);
  }


  /** Negative e-mail address creation test.
   * @param email E-mail address to test (automaticlly filled from value source). */
  @ParameterizedTest
  @ValueSource(strings = {
    "mkyong@.com.my",
    "arathi@%*.com",
    "pizza123@.com",
    "jhardin,2002@gmail.com"
  })
  public void createEmailFromStringFail(String email) {
    Assertions.assertThrows(
      InvalidEmailException.class,
      () -> new EmailType(email),
      "'"+email+"' is no valid e-mail address!"
    );
  }
}
