package se1app.datatypes;


import se1app.exceptions.InvalidAdressException;

import javax.persistence.Embeddable;


/** Data type for an address compliant to RFC 5322. */
@Embeddable
public class AdressType {

  private static final String adressRegex =
          "^\\w+(?:[^\\!\\\"\\§\\$\\%\\&\\/\\(\\)\\=\\?\\`\\´\\@\\€\\<\\>\\|\\,\\.\\-\\µ\\+\\#\\;\\:\\'\\*\\~]*)[0-9]$";
  private String _adress;


  /** Create an address data type.
   * @param adress The input to transform to an address.
   * @throws InvalidAdressException Thrown if input is no valid adress. */
  public AdressType(String adress) throws InvalidAdressException {
    if (isValid(adress)) _adress = adress;
    else {
      throw new InvalidAdressException(adress);
    }
  }


  /** Empty constructor for Hibernate. */
  public AdressType() {}


  /** Get the address.
   * @return The address. */
  public String getAdress() {
    return _adress;
  }


  /** Check if a given string is a RFC 5322 compliant address.
   * @param adress The string to check.
   * @return 'true' if the input is a valid adress, 'false' otherwise. */
  public static boolean isValid(String adress) {
    return adress.matches(adressRegex);
  }
}
