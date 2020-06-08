package se1app.entities;

import se1app.datatypes.DateTyp;
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
    private DateTyp _eventDate;

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
    public Event(User user, String eventName, DateTyp eventDate, int eventStatusId, Neighborhood neighborhood) {
        _eventName = eventName;
        _eventDate = eventDate;
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

    public DateTyp getEventDate() {
        return _eventDate;
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

    public void setEventDate(DateTyp eventDate) {
        this._eventDate = eventDate;
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
