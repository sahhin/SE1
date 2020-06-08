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
 * Repository class to load and save event from/to the database.
 */
public class EventRepository {


    /**
     *
     * @param user Organiser of event
     * @param eventName Name of event
     * @param eventDate Date of event
     * @param eventTime Start time and end time of event
     * @param eventStatusId Status of event. See EventStatus
     * @param neighborhood Neighborhood in which event takes place
     * @return Created event or null
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

    /**
     * Save (=update) an existing event to the database.
     * @param event Event to be saved
     */
    public static void saveEvent(Event event) {
        var session = H2Database.getInstance().getSession();
        var transaction = session.beginTransaction();
        session.save(event);
        transaction.commit();
    }

    /**
     * Get all event from the database.
     *
     * @return A list with events. May be empty if the database contains no events.
     */
    public static List<Event> getAllEvents() {
        var session = H2Database.getInstance().getSession();
        var event = session.createQuery("FROM Event", Event.class).getResultList();
        return event;
    }


    /**
     * Get a event by its identifier.
     * @param eventId Id of specific event
     * @return Desired event
     */
    public static Event getEventById(int eventId) {
        var session = H2Database.getInstance().getSession();
        var event = session.get(Event.class, eventId);
        return event;
    }


    /**
     * Delete an event from the database.
     * @param eventId Id of specific event
     */
    public static void deleteEventById(int eventId) {
        var session = H2Database.getInstance().getSession();
        var event = session.get(Event.class, eventId);
        var transaction = session.beginTransaction();
        event._neighborhood = null;
        event._user = null;
        session.delete(event);

        transaction.commit();
    }
}
