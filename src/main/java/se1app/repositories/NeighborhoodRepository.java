package se1app.repositories;

import se1app.entities.Event;
import se1app.entities.Neighborhood;
import se1app.entities.User;
import se1app.exceptions.InvalidEmailException;
import se1app.persistency.H2Database;

import java.util.Date;
import java.util.List;


/** Repository class to load and save customers from/to the database. */
public class NeighborhoodRepository {


  /** Create a new customer and save them in the database.
   * @return The customer object or 'null' if creation failed. */
    public static Neighborhood createNeighborhood(String neighborhoodName, int neighborhoodPostalcode, String neighborhoodCity, String neighborhoodCountry) {
    try {
      var neighborhood = new Neighborhood(neighborhoodName, neighborhoodPostalcode, neighborhoodCity, neighborhoodCountry);
      saveNeighborhood(neighborhood);
      return neighborhood;
    }
    catch (InvalidEmailException ex) {
      System.err.println("Event creation failed: \n -> "+ex);
      return null;
    }
  }


  /** Save (=update) an existing customer to the database. */
  public static void saveNeighborhood(Neighborhood neighborhood) {
    var session = H2Database.getInstance().getSession();
    var transaction = session.beginTransaction();
    session.save(neighborhood);
    transaction.commit();
  }

  /** Get all customers from the database.
   * @return A list with customers. May be empty if the database contains no customers. */
  public static List<Neighborhood> getAllNeighborhoods() {
    var session = H2Database.getInstance().getSession();
    var neighborhood = session.createQuery("FROM Neighborhood", Neighborhood.class).getResultList();
    return neighborhood;
  }


  /** Get a customer by its identifier.
   * @return The customer or 'null' if not found. */
  public static Neighborhood getNeighborhoodById(int neighborhoodId) {
    var session = H2Database.getInstance().getSession();
    var neighborhood = session.get(Neighborhood.class, neighborhoodId);
    return neighborhood;
  }


  /** Delete a customer from the database. */
  public static void deleteNeighborhoodById(int neighborhoodId) {
    var session = H2Database.getInstance().getSession();
    var neighborhood = session.get(Neighborhood.class, neighborhoodId);
    var transaction = session.beginTransaction();
    session.delete(neighborhood);
    transaction.commit();
  }


  /** Print a table of all customers to the console. This
   * method is not really a repository function, it's more
   * intended to be a helper during debugging and development. */
  public static void printUserTable() {
    var neighborhoods = getAllNeighborhoods();
    System.out.println("Neighborhoods in database");
    System.out.println("---------------------------------------");
    for (var neighborhood : neighborhoods) {
      System.out.println(neighborhood);
    }
    System.out.println("---------------------------------------");
  }
}
