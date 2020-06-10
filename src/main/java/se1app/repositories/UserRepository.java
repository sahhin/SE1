package se1app.repositories;

import java.util.Date;
import java.util.List;

import se1app.entities.Event;
import se1app.entities.Neighborhood;
import se1app.entities.User;
import se1app.exceptions.InvalidEmailException;
import se1app.persistency.H2Database;


/**
 * Repository class to load and save users from/to the database.
 */
public class UserRepository {


    /**
     * Create a new user and save them in the database.
     *
     * @param firstName  First name of user
     * @param lastName   Last name of user
     * @param userEmail  Email adress of user
     * @param userAdress Adress of user
     * @return user object or null
     */
    public static User createUser(String firstName, String lastName, String userEmail, String userAdress) {
        try {
            var user = new User(firstName, lastName, userEmail, userAdress);
            saveUser(user);
            return user;
        } catch (InvalidEmailException ex) {
            System.err.println("User creation failed: \n -> " + ex);
            return null;
        }
    }


    /**
     * Save (=update) an existing user to the database.
     *
     * @param user User object to be saved
     */
    public static void saveUser(User user) {
        var session = H2Database.getInstance().getSession();
        var transaction = session.beginTransaction();
        session.save(user);
        transaction.commit();
    }

    /**
     * Get all users from the database.
     *
     * @return A list with users. May be empty if the database contains no users.
     */
    public static List<User> getAllUsers() {
        var session = H2Database.getInstance().getSession();
        var user = session.createQuery("FROM User", User.class).getResultList();
        return user;
    }


    /**
     * Get a user by its identifier.
     *
     * @param userId id of user.
     * @return The customer or 'null' if not found.
     */
    public static User getUserById(int userId) {
        var session = H2Database.getInstance().getSession();
        var user = session.get(User.class, userId);
        return user;
    }


    /**
     * Delete a user from the database.
     *
     * @param userId id of user.
     */
    public static void deleteUserById(int userId) {
        var session = H2Database.getInstance().getSession();
        var user = session.get(User.class, userId);
        var transaction = session.beginTransaction();
        session.delete(user);
        transaction.commit();
    }

}
