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
@Table(name = "feed")
public class Feed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedUserId")
    private int _feedUserId;

    @Column(name = "feedContentText")
    private String _feedContentText;

    @Column(name = "feedContentPicture")
    private String _feedContentPicture;

    @Column(name = "feedContentDate")
    private Date _feedContentDate;

    @ManyToMany(mappedBy = "_feed ", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Neighborhood> _neighborhood;


    /**
     * Create a new neighborhood.
     *
     * @param Name         neighborhood  name.
     * @param lastName     neighborhood last name.
     * @param emailAddress E-mail address of the neighborhood.
     * @@hrows InvalidEmailException Thrown if input is no valid e-mail.
     */
    public Feed(int feedUserId, String feedContentText, String feedContentPicture, Date feedContentDate,) {
        _feedUserId = feedUserId;
        _feedContentText = feedContentText;
        _feedContentPicture = feedContentPicture;
        _feedContentDate = feedContentDate;
    }


    /**
     * Empty constructor for Hibernate.
     */
    Feed() {
    }


    /**
     * Output the properties of the neighborhood.
     *
     * @return The neighborhood's attributes concatenated as string.
     */
    @Override
    public String toString() {
        var str = "[" + _id + "] " + _feedUserId + " " + _feedContentText + " " + _feedContentPicture + ", " + _feedContentDate;
        return str;
    }


    /**
     * Get the neighborhood identifier.
     *
     * @return The neighborhood ID.
     */
    public int getFeedUserId() {
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
        this._neighborhoodPostalcode=neighborhoodPostalcode;
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
