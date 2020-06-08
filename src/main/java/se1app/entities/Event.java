package se1app.entities;

import se1app.datatypes.EventStatus;
import se1app.datatypes.TimeType;
import se1app.exceptions.InvalidEmailException;

import java.util.ArrayList;
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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    public User _user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "neighborhood_id")
    public Neighborhood _neighborhood;


    /**
     * Create a new neighborhood.
     *
     * @throws InvalidEmailException Thrown if input is no valid e-mail.
     */
    public Event(User user, String eventName, Date eventDate, TimeType eventTime, EventStatus eventStatusId, Neighborhood neighborhood) {
        _eventName = eventName;
        _eventDate = eventDate;
        _eventTime = eventTime;
        _eventUser = new ArrayList<>();
        _eventStatusId = eventStatusId;
        _user = user;
        _neighborhood = neighborhood;
    }

    public void setN(Neighborhood n){
        this._neighborhood=null;
    }

    /**
     * Empty constructor for Hibernate.
     */
    Event() {
    }

    /**
     * get the Event identifier.
     *
     * @return the id of the Event
     */
    public int getEventId() {
        return _id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null ) {
            return false;
        }
        if (!(o instanceof Event)){
            return false;
        }
        Event object = (Event) o;
        return this.getEventId() == object.getEventId();
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

    public int get_neighborhood_id(){
        return this._neighborhood.getNeighborhoodId();
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
    public void inviteUsers(User user) {
        this._eventUser.add(user);
    }

    /**
     * set a new status of the Event
     *
     * @param eventStatusId the new Status
     */
    public void setEventStatusId(EventStatus eventStatusId) {
        this._eventStatusId = eventStatusId;
    }

    /**
     * get the organizer user of the Event
     *
     * @return
     */
    public User getUser() {
        return _user;
    }

}
