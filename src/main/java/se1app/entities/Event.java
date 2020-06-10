package se1app.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import se1app.datatypes.EventStatus;
import se1app.datatypes.TimeType;
import se1app.exceptions.InvalidEmailException;
import se1app.repositories.UserRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;


/**
 * Representation of a Event in a Neighborhood.
 */
@Entity
@Table(name = "event")
@JsonIgnoreProperties(value = { "neighborhood" })
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
    public List<User> _eventUser;

    @Column(name = "eventStatusId")
    private EventStatus _eventStatusId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User _user;

    @ManyToOne
    @JoinColumn(name = "neighborhood_id")
    public Neighborhood _neighborhood;


    /**
     * Create a new neighborhood.
     * @param  eventName the Title of the event
     * @param eventDate the date of the event
     * @param eventTime the time of the event
     * @param eventStatusId the status of the event
     * @throws InvalidEmailException Thrown if input is no valid e-mail.
     */
    public Event(String eventName, Date eventDate, TimeType eventTime, EventStatus eventStatusId) {
        _eventName = eventName;
        _eventDate = eventDate;
        _eventTime = eventTime;
        _eventUser = new ArrayList<>();
        _eventStatusId = eventStatusId;
    }

    /**
     * Empty constructor for Hibernate.
     */
    Event() {
    }

    /**
     * set a neighborhood where the event is
     * @param neighborhood the neighorhood
     */
    public void setNeighborhood(Neighborhood neighborhood){
        this._neighborhood = neighborhood;
    }

    /**
     * get the Event identifier.
     *
     * @return the id of the Event
     */
    public int getEventId() {
        return _id;
    }

    /**
     * Get the Event's name.
     *
     * @return name of the Event.
     */
    public String getEventName() {
        return _eventName;
    }

    /**
     * get the Date of the Event
     *
     * @return the Event Date
     */
    public Date getEventDate() {
        return _eventDate;
    }

    /**
     * get the Stard-End Time of the Event
     *
     * @return The Time
     */
    public TimeType getEventTime() {
        return _eventTime;
    }


    /**
     * get the Status of the Event
     *
     * @return the Status of the Event
     */

    public EventStatus getEventStatusId() {
        return _eventStatusId;
    }


//setter

    /**
     * set a new Name of the Event
     *
     * @param eventName the new Name
     */

    public void setEventName(String eventName) {
        this._eventName = eventName;
    }

    /**
     * set a new Date of the Event
     *
     * @param eventDate the new Date
     */

    public void setEventDate(Date eventDate) {
        this._eventDate = eventDate;
    }


    /**
     * set a new Time of the Event
     *
     * @param eventTime the new Time
     */
    public void setEventTime(TimeType eventTime) {
        this._eventTime = eventTime;
    }

    /**
     * invite a users to the event
     *
     * @param user the users
     */
    public void setOrganiser(User user) {
        this._user = user;
    }

    /**
     * add users to the event
     * @param users
     */
    public void addUsers(List<User> users){
        this._eventUser.addAll(users);
    }

    /**
     * set a new status of the Event
     *
     * @param eventStatusId the new Status
     */
    public void setEventStatusId(EventStatus eventStatusId) {
        this._eventStatusId = eventStatusId;
    }


}
