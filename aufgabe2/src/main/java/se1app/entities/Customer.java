package se1app.entities;

import se1app.datatypes.AdressType;
import se1app.datatypes.EmailType;
import se1app.exceptions.InvalidEmailException;
import java.util.List;
import java.util.ArrayList;
import javax.persistence.*;


/** Representation of a customer. */
@Entity
@Table(name="customers")
public class Customer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int _id;

  @AttributeOverride(name="_adress", column=@Column(name="cust_adress"))
  private AdressType _custAdress;

  @AttributeOverride(name="_email", column=@Column(name="email_address"))
  private EmailType _emailAddress;

  @Column(name="last_name")
  private String _lastName;

  @Column(name="first_name")
  private String _firstName;

  @OneToMany(mappedBy="_customer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<Order> _orders;


  /** Create a new customer.
   * @param firstName Customer first name.
   * @param lastName Customer last name.
   * @param emailAddress E-mail address of the customer.
   * @@hrows InvalidEmailException Thrown if input is no valid e-mail. */
  public Customer(String firstName, String lastName, String emailAddress, String address) throws InvalidEmailException {
    _firstName = firstName;
    _lastName = lastName;
    _emailAddress = new EmailType(emailAddress);
    _custAdress = new AdressType(address);
    _orders = new ArrayList<Order>();
  }


  /** Empty constructor for Hibernate. */
  Customer() {}


  /** Output the properties of the customer.
   * @return The customer's attributes concatenated as string. */
  @Override
  public String toString() {
    var str = "["+_id+"] "+_firstName+" "+_lastName+", "+_emailAddress.getEmail()+", "+_custAdress.getAdress();
    if (_orders.size() > 0) {
      str += ", Orders: (";
      for (var i = 0; i < _orders.size(); i++) {
        str += "#"+_orders.get(i).getId();
        if (i < _orders.size()-1) str += ", ";
      }
      str += ")";
    }
    return str;
  }


  /** Get the customer identifier.
   * @return The customer ID. */
  public int getId() {
    return _id;
  }


  /** Get the customer's first name.
   * @return First name of the customer. */
  public String getFirstName() {
    return _firstName;
  }


  /** Get the customer's last name.
   * @return Last name of the customer. */
  public String getLastName() {
    return _lastName;
  }


  /** Get the customer's e-mail address.
   * @return E-mail address of the customer. */
  public EmailType getEmailAddress() {
    return _emailAddress;
  }

  /** Get the customer's e-mail address.
   * @return E-mail address of the customer. */
  public AdressType getCustAdress() {
    return _custAdress;
  }


  /** Get all oders of this customer.
   * @return List of orders. */
  public List<Order> getOrders() {
    return _orders;
  }


  /** Set a new first name.
   * @param newFirstName The new first name. */
  public void setFirstName(String newFirstName) {
    _firstName = newFirstName;
  }


  /** Set a new last name.
   * @param newLastName The new last name. */
  public void setLastName(String newLastName) {
    _lastName = newLastName;
  }


  /** Set a new e-mail address.
   * @param newEmailAddress The new e-mail address. */
  public void setEmailAddress(EmailType newEmailAddress) {
    _emailAddress = newEmailAddress;
  }

  /** Set a new e-mail address.
   * @param newAddress The new e-mail address. */
  public void setAddress(AdressType newAddress) {
    _custAdress = newAddress;
  }
}
