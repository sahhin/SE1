package se1app.datatypes;

import org.jetbrains.annotations.NotNull;


import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

public class DateTyp implements Comparable {
    private Date _time;
    private TimeTypes _startTime;
    private TimeTypes _endTime;

    public DateTyp(int year, int month, int day, int startHours, int startMinutes, int endHours, int endMinutes) {
        _time = new Date(year, month, day);
        _startTime = new TimeTypes(startHours, startMinutes);
        _endTime = new TimeTypes(endHours, endMinutes);

    }

    public int getYear() {
        return _time.getYear();
    }

    public int getMonth() {
        return _time.getMonth();
    }

    public int getDate() {
        return _time.getDay();
    }

    public TimeTypes getstartTime() {
        return _startTime;
    }

    public TimeTypes get_endTime() {
        return _endTime;
    }

    @Override
    public int compareTo(@NotNull Object o) {
        return _time.compareTo((java.util.Date) o);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DateTyp)) {
            return false;
        }
        DateTyp other = (DateTyp) o;
        return (this._time == null && other._time == null) || (this._time != null && this._time.equals(other._time));

    }
}

class TimeTypes {
    private int _hours;
    private int _minutes;

    public TimeTypes(int hours, int minutes) {
        _hours = hours;
        _minutes = minutes;
    }
}

