package se1app.entities;

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

    @Column(name = "eventStartTime")
    private String _eventStartTime;

    @Column(name = "eventEndTime")
    private String _eventEndTime;

    @Column(name = "eventOrganizerId")
    private int _eventOrganizerId;

    @Column(name = "eventParticipantId")
    @ElementCollection
    private List<User> _eventUser;

    @Column(name = "eventStatusId")
    private int _eventStatusId;

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
    public Event(User user, String eventName, Date eventDate, String eventStartTime, String eventEndTime, int eventOrganizerId, int eventStatusId, Neighborhood neighborhood) {
        _eventName = eventName;
        _eventDate = eventDate;
        _eventStartTime = eventStartTime;
        _eventEndTime = eventEndTime;
        _eventOrganizerId = eventOrganizerId;
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
     * Get the neighborhood identifier.
     *
     * @return The neighborhood ID.
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

    public String getEventStartTime() {
        return _eventStartTime;
    }

    public String getEventEndTime() {
        return _eventEndTime;
    }

    public int getEventOrganizerId() {
        return _eventOrganizerId;
    }

//    public List<Integer> getEventParticipantId() {
//        return _eventParticipantId;
//    }

    public int getEventStatusId() {
        return _eventStatusId;
    }


//setter

    public void setEventName(String eventName) {
        this._eventName = eventName;
    }

    public void setEventDate(Date eventDate) {
        this._eventDate = eventDate;
    }

    public void setEventStartTime(String eventStartTime) {
        this._eventStartTime = eventStartTime;
    }

    public void setEventEndTime(String eventEndTime) {
        this._eventEndTime = eventEndTime;
    }

    public void setEventOrganizerId(int eventOrganizerId) {
        this._eventOrganizerId = eventOrganizerId;
    }

    public void inviteUsers(User user) {
        this._eventUser.add(user);
    }

    public void setEventStatusId(int eventStatusId) {
        this._eventStatusId = eventStatusId;
    }

    public User getUser() {
        return _user;
    }

}
