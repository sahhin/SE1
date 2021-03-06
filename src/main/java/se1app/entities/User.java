package se1app.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import se1app.datatypes.AdressType;
import se1app.datatypes.EmailType;
import se1app.exceptions.InvalidEmailException;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import java.util.Date;


/**
 * Representation of a User.
 */
@Entity
@Table(name = "user")
@JsonIgnoreProperties(value = {"neighborhood"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int _id;

    @Column(name = "userFirstName")
    private String _userFirstName;

    @Column(name = "userLastName")
    private String _userLastName;

    @AttributeOverride(name = "_email", column = @Column(name = "userEmail"))
    private EmailType _userEmail;

    @AttributeOverride(name = "_adress", column = @Column(name = "userAdress"))
    private AdressType _userAdress;

    @OneToMany(mappedBy = "_user", cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private List<Event> _events;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name = "neighborhood_id")
    public Neighborhood _neighborhood;

    /**
     * Create a new user.
     *
     * @param firstName  user first name.
     * @param lastName   user last name.
     * @param userEmail  E-mail address of the user.
     * @param userAdress adress of the user.
     * @throws InvalidEmailException Thrown if input is no valid e-mail.
     */
    public User(String firstName, String lastName, String userEmail, String userAdress) throws InvalidEmailException {
        _userFirstName = firstName;
        _userLastName = lastName;
        _userEmail = new EmailType(userEmail);
        _userAdress = new AdressType(userAdress);
        _events = new ArrayList<Event>();
    }

    /**
     * Empty constructor for Hibernate.
     */
    User() {
    }

    /**
     * Get the user identifier.
     *
     * @return The user ID.
     */
    public int getId() {
        return _id;
    }

    /**
     * set a neighborhood fot the User
     *
     * @param neighborhood the neighborhood
     */
    public void setNeighborhood(Neighborhood neighborhood) {
        this._neighborhood = neighborhood;
    }

    /**
     * get the neighborhood
     *
     * @return neighborhood
     */
    public Neighborhood getNeighborhood() {
        return _neighborhood;
    }

    /**
     * Get the user's first name.
     * @return First name of the user.
     */
    public String getFirstName() {
        return _userFirstName;
    }


    /**
     * Get the user's last name.
     * @return Last name of the user.
     */
    public String getLastName() {
        return _userLastName;
    }


    /**
     * Get the user's e-mail address.
     * @return E-mail address of the user.
     */
    public EmailType getEmailAddress() {
        return _userEmail;
    }

    /**
     * Get the user's e-mail address.
     * @return E-mail address of the user.
     */
    public AdressType getCustAdress() {
        return _userAdress;
    }


    /**
     * Set a new first name.
     * @param newFirstName The new first name.
     */
    public void setFirstName(String newFirstName) {
        _userFirstName = newFirstName;
    }


    /**
     * Set a new last name.
     * @param newLastName The new last name.
     */
    public void setLastName(String newLastName) {
        _userLastName = newLastName;
    }


    /**
     * Set a new e-mail address.
     * @param newEmailAddress The new e-mail address.
     */
    public void setEmailAddress(EmailType newEmailAddress) {
        _userEmail = newEmailAddress;
    }

    /**
     * Set a new e-mail address.
     * @param newAddress The new e-mail address.
     */
    public void setAddress(AdressType newAddress) {
        _userAdress = newAddress;
    }

}
