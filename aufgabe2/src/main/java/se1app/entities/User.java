package se1app.entities;

import se1app.datatypes.AdressType;
import se1app.datatypes.EmailType;
import se1app.exceptions.InvalidEmailException;
import java.util.List;
import java.util.ArrayList;
import javax.persistence.*;


/** Representation of a user. */
@Entity
@Table(name="user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private int _userId;

    @Column(name="userFirstName")
    private String _userFirstName;

    @Column(name="userLastName")
    private String _userLastName;

    @Column(name="userBirthday")
    private Date _userBirthday;

    @AttributeOverride(name="_email", column=@Column(name="userEmail"))
    private EmailType _userEmail;

    @AttributeOverride(name="_adress", column=@Column(name="userAdr ess"))
    private AdressType _userAdress;

    @ManyToOne(mappedBy="_user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Neighborhood> _neighborhood;

    @ManyToMany(mappedBy="_user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Event> _event;


    /** Create a new user.
     * @param firstName user first name.
     * @param lastName user last name.
     * @param emailAddress E-mail address of the user.
     * @@hrows InvalidEmailException Thrown if input is no valid e-mail. */
    public User(Date userBirthday, String firstName, String lastName, String userEmail, String userAdress) throws InvalidEmailException {
        _userFirstName = firstName;
        _userLastName = lastName;
        _userBirthday = userBirthday;
        _userEmail = new EmailType(userEmail);
        _userAdress = new AdressType(userAdress);
    }


    /** Empty constructor for Hibernate. */
    User() {}


    /** Output the properties of the user.
     * @return The user's attributes concatenated as string. */
    @Override
    public String toString() {
        var str = "["+_id+"] "+ _userId +" " +_userFirstName+" "+_userLastName+", " + _userBirthday + " " +_userEmail.getEmail()+", "+_userAdress.getAdress();
        if (_neighborhood.size() > 0) {
            str += ", Neighborhood: (";
            for (var i = 0; i < _neighborhood.size(); i++) {
                str += "#" + _neighborhood.get(i).getId();
                if (i < _neighborhood.size() - 1) str += ", ";
            }
            str += ")";
        }
        if (_event.size() > 0) {
            str += ", Events: (";
            for (var i = 0; i < _event.size() && _event.contains(this._userId); i++) {
                str += "#" + _event.get(i).getId();
                if (i < _event.size() - 1) str += ", ";
            }
            str += ")";
        }
        return str;
    }


    /** Get the user identifier.
     * @return The user ID. */
    public int getId() {
        return _id;
    }


    /** Get the user's first name.
     * @return First name of the user. */
    public String getFirstName() {
        return _userFirstName;
    }


    /** Get the user's last name.
     * @return Last name of the user. */
    public String getLastName() {
        return _userLastName;
    }


    /** Get the user's e-mail address.
     * @return E-mail address of the user. */
    public EmailType getEmailAddress() {
        return _userEmail;
    }

    /** Get the user's e-mail address.
     * @return E-mail address of the user. */
    public AdressType getCustAdress() {
        return _userAdress;
    }


    /** Get all oders of this user.
     * @return List of orders. */
    public List<Order> getOrders() {
        return _orders;
    }


    /** Set a new first name.
     * @param newFirstName The new first name. */
    public void setBirthday(Date newBirthday) {
        _userBirthday = newBirthday;
    }



    /** Set a new first name.
     * @param newFirstName The new first name. */
    public void setFirstName(String newFirstName) {
        _userFirstName = newFirstName;
    }


    /** Set a new last name.
     * @param newLastName The new last name. */
    public void setLastName(String newLastName) {
        _userLastName = newLastName;
    }


    /** Set a new e-mail address.
     * @param newEmailAddress The new e-mail address. */
    public void setEmailAddress(EmailType newEmailAddress) {
        _userEmail = newEmailAddress;
    }

    /** Set a new e-mail address.
     * @param newAddress The new e-mail address. */
    public void setAddress(AdressType newAddress) {
        _userAdress = newAddress;
    }
}
