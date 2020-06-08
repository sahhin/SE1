package se1app.entities;

import se1app.datatypes.EventStatus;
import se1app.datatypes.TimeType;
import se1app.exceptions.InvalidEmailException;

import java.util.Date;
import java.util.List;
import javax.persistence.*;


/**
 * Representation of a neighborhood.
 */
@Entity
@Table(name = "event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int _id;

    @Column(name = "eventName")
    private String _eventName;

    @Column(name = "eventDate")
    private Date _eventDate;

    @Column(name = "eventTime")
    private TimeType _eventTime;

    @Column(name = "eventParticipantId")
    @ElementCollection
    private List<User> _eventUser;

    @Column(name = "eventStatusId")
    private EventStatus _eventStatusId;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User _user;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "neighborhood_id")
    private Neighborhood _neighborhood;


    /**
     * Create a new neighborhood.
     *
     * @throws InvalidEmailException Thrown if input is no valid e-mail.
     */
    public Event(User user, String eventName, Date eventDate, TimeType eventTime, EventStatus eventStatusId, Neighborhood neighborhood) {
        _eventName = eventName;
        _eventDate = eventDate;
        _eventTime = eventTime;
//        _eventParticipantId = new ArrayList<>();
        _eventStatusId = eventStatusId;
        _user = user;
        _neighborhood = neighborhood;
    }


    /**
     * Empty constructor for Hibernate.
     */
    Event() {
    }

    /**
     *
     * @return
     */
    public int getEventId() {
        return _id;
    }


    /**
     * Get the neighborhood's first name.
     *
     * @return First name of the neighborhood.
     */
    public String getEventName() {
        return _eventName;
    }

    public Date getEventDate() {
        return _eventDate;
    }

    public TimeType getEventTime() {
        return _eventTime;
    }

    public EventStatus getEventStatusId() {
        return _eventStatusId;
    }


//setter

    public void setEventName(String eventName) {
        this._eventName = eventName;
    }

    public void setEventDate(Date eventDate) {
        this._eventDate = eventDate;
    }


    public void setEventTime(TimeType eventTime) {
        this._eventTime = eventTime;
    }

    public void inviteUsers(User user) {
        this._eventUser.add(user);
    }

    public void setEventStatusId(EventStatus eventStatusId) {
        this._eventStatusId = eventStatusId;
    }

    public User getUser() {
        return _user;
    }

}
