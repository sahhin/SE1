package se1app.datatypes;

import se1app.exceptions.InvalidTimeException;

import javax.persistence.Embeddable;


/** Data type for a time compliant. */
@Embeddable
public class TimeType {

  private static final String timeRegex =
    "\\d?\\d:\\d\\d";
  private String _time;


  /** Create an time address data type.
   * @throws InvalidTimeException Thrown if input is no valid time. */
  public TimeType(String time) throws InvalidTimeException {
    if (isValid(time)) _time = time;
    else {
      throw new InvalidTimeException(time);
    }
  }


  /** Empty constructor for Hibernate. */
  public TimeType() {}


  /** Get the time address.
   * @return The time address. */
  public String getTime() {
    return _time;
  }


  /** Check if a given string is a RFC 5322 compliant time address.
   * @param time The string to check.
   * @return 'true' if the input is a valid time, 'false' otherwise. */
  public static boolean isValid(String time) {
    return time.matches(timeRegex);
  }
}

