import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import se1app.datatypes.AdressType;
import se1app.exceptions.InvalidAdressException;


public class AdresseTypeTest {


  /** Positive address creation test.
   * @param adresse address to test (automaticlly filled from value source). */
  @ParameterizedTest
  @ValueSource(strings = {
    "Holstenglacis 6",
    "Berliner Tor 7",
    "Platz der Republik 1",
    "Steindamm 58"
  })
  public void createAdressFromStringSuccess(String adresse) {
    new AdressType(adresse);
  }


  /** Negative address creation test.
   * @param adresse address to test (automaticlly filled from value source). */
  @ParameterizedTest
  @ValueSource(strings = {
    "Holstengla*+8",
    "Berlinter.Tor.7",
    "Platz @ Republik",
    "Stein,damm"
  })
  public void createAdressFromStringFail(String adresse) {
    Assertions.assertThrows(
      InvalidAdressException.class,
      () -> new AdressType(adresse),
      "'"+adresse+"' is no valid address!"
    );
  }
}
