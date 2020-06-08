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
@Table(name = "neighborhood")
public class Neighborhood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "neighborhoodId")
    private int _neighborhoodId;

    @Column(name = "neighborhoodName")
    private String _neighborhoodName;

    @Column(name = "neighborhoodPostalcode")
    private int _neighborhoodPostalcode;

    @Column(name = "neighborhoodCity")
    private String _neighborhoodCity;

    @Column(name = "neighborhoodCountry")
    private String _neighborhoodCountry;

    @Column(name = "neighborhoodMemberId")
    private List<int> _neighborhoodMemberId = new ArrayList<int>();

    @ManyToOne(mappedBy = "_neighborhood", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<User> _user;

    @ManyToOne(mappedBy = "_neighborhood ", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Event> _event;

    @ManyToMany(mappedBy = "_neighborhood ", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Feed> _feed;


    /**
     * Create a new neighborhood.
     *
     * @param Name         neighborhood  name.
     * @param lastName     neighborhood last name.
     * @param emailAddress E-mail address of the neighborhood.
     * @@hrows InvalidEmailException Thrown if input is no valid e-mail.
     */
    public Neighborhood(String neighborhoodName, int neighborhoodPostalcode, String neighborhoodCity, String neighborhoodCountry, List<int> neighborhoodMemberId) {
        _neighborhoodName = neighborhoodName;
        _neighborhoodPostalcode = neighborhoodPostalcode;
        _neighborhoodCity = neighborhoodCity;
        _neighborhoodCountry = neighborhoodCountry;
        _neighborhoodMemberId = neighborhoodMemberId;
    }


    /**
     * Empty constructor for Hibernate.
     */
    Neighborhood() {
        _neighborhoodMemberId = new ArrayList<int>();
    }


    /**
     * Output the properties of the neighborhood.
     *
     * @return The neighborhood's attributes concatenated as string.
     */
    @Override
    public String toString() {
        var str = "[" + _id + "] " + _neighborhoodId + " " + _neighborhoodName + " " + _neighborhoodPostalcode + ", " + _neighborhoodCity + " " + _neighborhoodCountry + ", " + _neighborhoodMemberId;
        if (_user.size() > 0) {
            str += ", User: (";
            for (var i = 0; i < _user.size(); i++) {
                str += "#" + _user.get(i).getId();
                if (i < _user.size() - 1) str += ", ";
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
    public int getNeighborhoodId() {
        return _neighborhoodId;
    }


    /**
     * Get the neighborhood's first name.
     *
     * @return First name of the neighborhood.
     */
    public String getNeighborhoodName() {
        return _neighborhoodName;
    }


    /**
     * Get all oders of this neighborhood.
     *
     * @return List of orders.
     */
    public List<int> getNeighborhoodMembers() {
        return _neighborhoodMemberId;
    }

    public String getNeighborhoodPostalcode() {
        return _neighborhoodPostalcode;
    }

    public String getNeighborhoodCity() {
        return _neighborhoodCity;
    }

    public String getNeighborhoodCountry() {
        return _neighborhoodCountry;
    }

    public String getNeighborhoodCity() {
        return _neighborhoodCity;
    }

//setter

    public void setNeighborhoodMembers(int neighborhoodMemberId) {
        _neighborhoodMemberId.add
    }

    public void setNeighborhoodPostalcode(int neighborhoodPostalcode) {
        this._neighborhoodPostalcode = neighborhoodPostalcode;
    }

    public void setNeighborhoodCity(String neighborhoodCity) {
        return _neighborhoodCity = neighborhoodCity;
    }

    public void setNeighborhoodCountry(String neighborhoodCountry) {
        return _neighborhoodCountry;
    }

    public void setNeighborhoodCity(String neighborhoodCity) {
        return _neighborhoodCity;
    }
}
