package se1app.repositories;

import java.util.Date;
import java.util.List;

import se1app.entities.Event;
import se1app.entities.Neighborhood;
import se1app.entities.User;
import se1app.exceptions.InvalidEmailException;
import se1app.persistency.H2Database;


/** Repository class to load and save customers from/to the database. */
public class UserRepository {


  /** Create a new customer and save them in the database.
   * @param firstName The customers first name.
   * @param lastName The customers last name.
   * @param userEmail The e-mail address.
   * @return The customer object or 'null' if creation failed. */
    public static User createUser(Date userBirthday, String firstName, String lastName, String userEmail, String userAdress, Neighborhood neighborhood, Event events) {
    try {
      var user = new User(userBirthday, firstName, lastName, userEmail, userAdress, neighborhood);
      saveUser(user);
      return user;
    }
    catch (InvalidEmailException ex) {
      System.err.println("User creation failed: \n -> "+ex);
      return null;
    }
  }


  /** Save (=update) an existing customer to the database.
   * @param user The customer to update. */
  public static void saveUser(User user) {
    var session = H2Database.getInstance().getSession();
    var transaction = session.beginTransaction();
    session.save(user);
    transaction.commit();
  }

  /** Get all customers from the database.
   * @return A list with customers. May be empty if the database contains no customers. */
  public static List<User> getAllUsers() {
    var session = H2Database.getInstance().getSession();
    var user = session.createQuery("FROM User", User.class).getResultList();
    return user;
  }


  /** Get a customer by its identifier.
   * @param userId  identifier.
   * @return The customer or 'null' if not found. */
  public static User getUserById(int userId) {
    var session = H2Database.getInstance().getSession();
    var user = session.get(User.class, userId);
    return user;
  }


  /** Delete a customer from the database.
   * @param userId  identifier. */
  public static void deleteUserById(int userId) {
    var session = H2Database.getInstance().getSession();
    var user = session.get(User.class, userId);
    var transaction = session.beginTransaction();
    session.delete(user);
    transaction.commit();
  }


  /** Print a table of all customers to the console. This
   * method is not really a repository function, it's more
   * intended to be a helper during debugging and development. */
  public static void printUserTable() {
    var users = getAllUsers();
    System.out.println("Users in database");
    System.out.println("---------------------------------------");
    for (var user : users) {
      System.out.println(user);
    }
    System.out.println("---------------------------------------");
  }
}
