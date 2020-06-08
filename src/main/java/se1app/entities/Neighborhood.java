package se1app.entities;

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

//    @Column(name = "neighborhoodMemberId")
//    private List<Integer> _neighborhoodMemberId;

    @OneToMany(mappedBy = "_neighborhood", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<User> _user;

    @OneToMany(mappedBy = "_neighborhood",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Event> _event;


    /**
     * Create a new neighborhood.
     *
     * @throws InvalidEmailException Thrown if input is no valid e-mail.
     */
    public Neighborhood(String neighborhoodName, int neighborhoodPostalcode, String neighborhoodCity, String neighborhoodCountry) {
        _neighborhoodName = neighborhoodName;
        _neighborhoodPostalcode = neighborhoodPostalcode;
        _neighborhoodCity = neighborhoodCity;
        _neighborhoodCountry = neighborhoodCountry;
//        _neighborhoodMemberId = new ArrayList<Integer>();
        _user = new ArrayList<User>();
        _event = new ArrayList<Event>();

    }


    /**
     * Empty constructor for Hibernate.
     */
    Neighborhood() {

    }


    /**
     * Output the properties of the neighborhood.
     *
     * @return The neighborhood's attributes concatenated as string.
     */
    @Override
    public String toString() {
        var str = "[" + _neighborhoodId + "] " + _neighborhoodName + " " + _neighborhoodPostalcode + ", " + _neighborhoodCity + " " + _neighborhoodCountry;
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


//    /**
//     * Get all oders of this neighborhood.
//     *
//     * @return List of orders.
//     */
//    public List<Integer> getNeighborhoodMembers() {
//        return _neighborhoodMemberId;
//    }

    public int getNeighborhoodPostalcode() {
        return _neighborhoodPostalcode;
    }

    public String getNeighborhoodCountry() {
        return _neighborhoodCountry;
    }

    public String getNeighborhoodCity() {
        return _neighborhoodCity;
    }

//setter

//    public void setNeighborhoodMembers(List<Integer> neighborhoodMemberId) {
//        _neighborhoodMemberId.addAll(neighborhoodMemberId);
//    }

    public void setNeighborhoodPostalcode(int neighborhoodPostalcode) {
        this._neighborhoodPostalcode = neighborhoodPostalcode;
    }

    public void setNeighborhoodCity(String neighborhoodCity) {
        this._neighborhoodCity = neighborhoodCity;
    }

    public void setNeighborhoodCountry(String neighborhoodCountry) {
        this._neighborhoodCountry = neighborhoodCountry;
    }
    public void setNeighborhoodName(String neighborhoodName) {
        this._neighborhoodName = neighborhoodName;
    }

}
