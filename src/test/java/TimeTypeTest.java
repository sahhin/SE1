import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import se1app.datatypes.TimeType;
import se1app.exceptions.InvalidTimeException;


public class TimeTypeTest {


    /**
     * Positive time creation test.
     *
     */
    @ParameterizedTest
    @ValueSource(strings = {
            "15:00",
            "21:00",
            "18:45",
            "5:00"
    })
    public void createTimeFromStringSuccess(int startHours, int startMins, int endHours, int endMins) {
        new TimeType(startHours, startMins, endHours, endMins);
    }


    /**
     * Negative time  creation test.
     **/
    @ParameterizedTest
    @ValueSource(strings = {
            "15",
            "555:55",
            "abc",
            "drölf"
    })
    public void createTimeFromStringFail(int startHours, int startMins, int endHours, int endMins) {
        Assertions.assertThrows(
                InvalidTimeException.class,
                () ->  new TimeType(startHours, startMins, endHours, endMins),
                "'" + startHours + ":"+startMins +"- "+ endHours +":" +endMins + "' is no valid time!"
        );
    }
}
