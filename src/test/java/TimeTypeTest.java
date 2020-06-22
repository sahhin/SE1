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
            "15,15,16,11"
    } )
    public void createTimeFromStringSuccess(String time) {
        String[] timearr = time.split(",");
        new TimeType(timearr[0],timearr[1],timearr[2],timearr[3]);
    }


    /**
     * Negative time  creation test.
     **/
    @ParameterizedTest
    @ValueSource(strings = {
            "154,145,164,114"
    })
    public void createTimeFromStringFail(String time) {
        String[] timearr = time.split(",");
        Assertions.assertThrows(
                InvalidTimeException.class,
                () ->  new TimeType(timearr[0],timearr[1],timearr[2],timearr[3]),
                "'" + Integer.parseInt(timearr[0]) + ":"+Integer.parseInt(timearr[1]) +"- "+ Integer.parseInt(timearr[2]) +":" +Integer.parseInt(timearr[3]) + "' is no valid time!"
        );
    }
}
