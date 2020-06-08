package se1app.repositories;

import se1app.entities.Event;
import se1app.entities.Neighborhood;
import se1app.entities.User;
import se1app.exceptions.InvalidEmailException;
import se1app.persistency.H2Database;

import java.util.Date;
import java.util.List;


/**
 * Repository class to load and save neighborhoods from/to the database.
 */
public class NeighborhoodRepository {

  /**
   *
   * @param neighborhoodName Name of neighborhood
   * @param neighborhoodPostalcode Postal code of neighborhood
   * @param neighborhoodCity City of neighborhood
   * @param neighborhoodCountry Country of neighborhood
   * @return neighborhood or null
   */
  public static Neighborhood createNeighborhood(String neighborhoodName, int neighborhoodPostalcode, String neighborhoodCity, String neighborhoodCountry) {
        try {
            var neighborhood = new Neighborhood(neighborhoodName, neighborhoodPostalcode, neighborhoodCity, neighborhoodCountry);
            saveNeighborhood(neighborhood);
            return neighborhood;
        } catch (InvalidEmailException ex) {
            System.err.println("Event creation failed: \n -> " + ex);
            return null;
        }
    }


  /**
   * Save (=update) an existing neighborhood to the database.
   * @param neighborhood Neighborhood object to be saved
   */
  public static void saveNeighborhood(Neighborhood neighborhood) {
        var session = H2Database.getInstance().getSession();
        var transaction = session.beginTransaction();
        session.save(neighborhood);
        transaction.commit();
    }

    /**
     * Get all neighborhoods from the database.
     *
     * @return A list with neighborhoods. May be empty if the database contains no neighborhoods.
     */
    public static List<Neighborhood> getAllNeighborhoods() {
        var session = H2Database.getInstance().getSession();
        var neighborhood = session.createQuery("FROM Neighborhood", Neighborhood.class).getResultList();
        return neighborhood;
    }


  /**
   * Get a neighborhood by its identifier.
   * @param neighborhoodId Id of desired neighborhood
   * @return The desired neighborhood
   */
    public static Neighborhood getNeighborhoodById(int neighborhoodId) {
        var session = H2Database.getInstance().getSession();
        var neighborhood = session.get(Neighborhood.class, neighborhoodId);
        return neighborhood;
    }


    /**
     * Delete a neighborhood from the database.
     * @param neighborhoodId Id of desired neighborhood
     */
    public static void deleteNeighborhoodById(int neighborhoodId) {
        var session = H2Database.getInstance().getSession();
        var neighborhood = session.get(Neighborhood.class, neighborhoodId);
        var transaction = session.beginTransaction();
        session.delete(neighborhood);
        transaction.commit();
    }
}
