package se1app.entities;

import se1app.datatypes.AdressType;
import se1app.datatypes.EmailType;
import se1app.exceptions.InvalidEmailException;

import java.util.List;
import java.util.ArrayList;
import javax.persistence.*;


/**
 * Representation of a neighborhood.
 */
@Entity
@Table(name = "event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "eventId")
    private int _eventId;

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
    private List<int> _eventParticipantId;

    @Column(name = "eventStatusId")
    private int _eventStatusId;

    @ManyToMany(mappedBy = "_event ", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<User> _user;

    @ManyToOne(mappedBy = "_event ", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Neighborhood> _neighborhood;


    /**
     * Create a new neighborhood.
     *
     * @param Name         neighborhood  name.
     * @param lastName     neighborhood last name.
     * @param emailAddress E-mail address of the neighborhood.
     * @@hrows InvalidEmailException Thrown if input is no valid e-mail.
     */
    public Feed(String eventName, Date eventDate, String eventStartTime, String eventEndTime, int eventOrganizerId, List<int> eventParticipantId, int eventStatusId, Date feedContentDate,) {
        _eventName = eventName;
        _eventDate = eventDate;
        _eventStartTime = eventStartTime;
        _eventEndTime = eventEndTime;
        _eventOrganizerId = eventOrganizerId;
        _eventParticipantId = eventParticipantId;
        _eventStatusId = eventStatusId;
    }


    /**
     * Empty constructor for Hibernate.
     */
    Event() {
    }


    /**
     * Output the properties of the neighborhood.
     *
     * @return The neighborhood's attributes concatenated as string.
     */
    @Override
    public String toString() {
        var str = "[" + _id + "] " + _eventId + " " + _eventName + " " + _eventDate + ", " + _eventStartTime + ", " + _eventEndTime + ", " + _eventOrganizerId + " " +_eventStatusId;
        if (_user.size() > 0) {
            str += ", Users: (";
            for (var i = 0; i < _user.size(); i++) {
                str += "#"+_user.get(i).getId();
                if (i < _user.size()-1) str += ", ";
            }
            str += ")";
        }
        if (_neighborhood.size() > 0) {
            str += ", Neighborhood: (";
            for (var i = 0; i < _neighborhood.size(); i++) {
                str += "#"+_neighborhood.get(i).getId();
                if (i < _neighborhood.size()-1) str += ", ";
            }
            str += ")";
        }
        return str;
    }

    /**
     * Get the neighborhood identifier.
     *
     * @return The neighborhood ID.
     */
    public int getEventId() {
        return _eventId;
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
    public List<int> getEventParticipantId() {
        return _eventParticipantId;
    }
    public int getEventStatusId() {
        return _eventStatusId;
    }
    

//setter

    public void setEventName(String eventName) {
        this._eventName = eventName;
    }
    public Date setEventDate(Date eventDate) {
        this._eventDate = eventDate;
    }
    public void setEventStartTime(String eventStartTime) {
        this._eventStartTime=eventStartTime;
    }
    public void setEventEndTime(String eventEndTime) {
        this._eventEndTime=eventEndTime;
    }
    public void setEventOrganizerId(int eventOrganizerId) {
        this._eventOrganizerId=eventOrganizerId;
    }
    public void setEventParticipantId(List<int> eventParticipantId) {
        this._eventParticipantId=eventParticipantId;
    }
    public void setEventStatusId(int eventStatusId) {
        this._eventStatusId=eventStatusId;
    }
}
