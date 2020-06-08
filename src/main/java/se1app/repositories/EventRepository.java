package se1app.repositories;

import se1app.datatypes.EventStatus;
import se1app.datatypes.TimeType;
import se1app.entities.Event;
import se1app.entities.Neighborhood;
import se1app.entities.User;
import se1app.exceptions.InvalidEmailException;
import se1app.persistency.H2Database;

import java.util.Date;
import java.util.List;


/**
 * Repository class to load and save customers from/to the database.
 */
public class EventRepository {


    /**
     * Create a new customer and save them in the database.
     *
     * @return The customer object or 'null' if creation failed.
     */
    public static Event createEvent(User user, String eventName, Date eventDate, TimeType eventTime, EventStatus eventStatusId, Neighborhood neighborhood) {
        try {
            var event = new Event(user, eventName, eventDate, eventTime, eventStatusId, neighborhood);
            saveEvent(event);
            return event;
        } catch (InvalidEmailException ex) {
            System.err.println("Event creation failed: \n -> " + ex);
            return null;
        }
    }

    public static void inviteUser(int userId, int eventId) {

    }


    /**
     * Save (=update) an existing customer to the database.
     */
    public static void saveEvent(Event event) {
        var session = H2Database.getInstance().getSession();
        var transaction = session.beginTransaction();
        session.save(event);
        transaction.commit();
    }

    /**
     * Get all customers from the database.
     *
     * @return A list with customers. May be empty if the database contains no customers.
     */
    public static List<Event> getAllEvents() {
        var session = H2Database.getInstance().getSession();
        var event = session.createQuery("FROM Event", Event.class).getResultList();
        return event;
    }


    /**
     * Get a customer by its identifier.
     *
     * @return The customer or 'null' if not found.
     */
    public static Event getEventById(int eventId) {
        var session = H2Database.getInstance().getSession();
        var event = session.get(Event.class, eventId);
        return event;
    }


    /**
     * Delete a customer from the database.
     */
    public static void deleteEventById(int eventId) {
        var session = H2Database.getInstance().getSession();
        var event = session.get(Event.class, eventId);
        var transaction = session.beginTransaction();
        session.delete(event);
        transaction.commit();
    }


    /**
     * Print a table of all customers to the console. This
     * method is not really a repository function, it's more
     * intended to be a helper during debugging and development.
     */
    public static void printUserTable() {
        var events = getAllEvents();
        System.out.println("Events in database");
        System.out.println("---------------------------------------");
        for (var event : events) {
            System.out.println(event);
        }
        System.out.println("---------------------------------------");
    }
}
