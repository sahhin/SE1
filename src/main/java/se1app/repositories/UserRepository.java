package se1app.repositories;

import java.util.Date;
import java.util.List;

import se1app.entities.Event;
import se1app.entities.Neighborhood;
import se1app.entities.User;
import se1app.exceptions.InvalidEmailException;
import se1app.persistency.H2Database;


/** Repository class to load and save users from/to the database. */
public class UserRepository {


  /**
   * Create a new user and save them in the database.
   * @param userBirthday Birthday of user
   * @param firstName First name of user
   * @param lastName Last name of user
   * @param userEmail Email adress of user
   * @param userAdress Adress of user
   * @param neighborhood Neighborhood in which user lives in
   * @return user object or null
   */
    public static User createUser(Date userBirthday, String firstName, String lastName, String userEmail, String userAdress, Neighborhood neighborhood) {
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


  /**
   * Save (=update) an existing user to the database.
   * @param user User object to be saved
   */
  public static void saveUser(User user) {
    var session = H2Database.getInstance().getSession();
    var transaction = session.beginTransaction();
    session.save(user);
    transaction.commit();
  }

  /** Get all users from the database.
   * @return A list with users. May be empty if the database contains no users. */
  public static List<User> getAllUsers() {
    var session = H2Database.getInstance().getSession();
    var user = session.createQuery("FROM User", User.class).getResultList();
    return user;
  }


  /** Get a user by its identifier.
   * @param userId  id of user.
   * @return The customer or 'null' if not found. */
  public static User getUserById(int userId) {
    var session = H2Database.getInstance().getSession();
    var user = session.get(User.class, userId);
    return user;
  }


  /** Delete a user from the database.
   * @param userId  id of user. */
  public static void deleteUserById(int userId) {
    var session = H2Database.getInstance().getSession();
    var user = session.get(User.class, userId);
    var transaction = session.beginTransaction();
    session.delete(user);
    transaction.commit();
  }

//
//  /** Print a table of all users to the console. This
//   * method is not really a repository function, it's more
//   * intended to be a helper during debugging and development. */
//  public static void printUserTable() {
//    var users = getAllUsers();
//    System.out.println("Users in database");
//    System.out.println("---------------------------------------");
//    for (var user : users) {
//      System.out.println(user);
//    }
//    System.out.println("---------------------------------------");
//  }
}
