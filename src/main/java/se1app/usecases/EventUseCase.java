package se1app.usecases;

import se1app.entities.Neighborhood;
import se1app.entities.User;
import se1app.repositories.EventRepository;
import se1app.repositories.NeighborhoodRepository;
import se1app.repositories.UserRepository;

import java.util.List;


/** Use case class to place event orders. */
public class EventUseCase {

  /**
   * set the organizer of the event
   * @param userId Identifier of the user.
   * @param eventId Identifier of the event.
   * @return true or false
   */
  public static boolean setOrganizer(int userId, int eventId) {
    var user = UserRepository.getUserById(userId);
    var event = EventRepository.getEventById(eventId);
    if (user != null && event != null) {
      event.setOrganiser(user);
      EventRepository.saveEvent(event);
      return true;
    }
    return false;
  }

  /**
   * add users to the event
   * @param users the list of user
   * @param eventId the id of the event
   * @return true or false
   */
  public static boolean addParticipants(List<User> users, int eventId) {
    var event = EventRepository.getEventById(eventId);
    if (users != null && event != null && users.size() > 0) {
      event.addUsers(users);
      EventRepository.saveEvent(event);
      return true;
    }
    return false;
  }

  /**
   * set the neighborhood of the event
   * @param nId the id of the neighorhood
   * @param eventId the id of the event
   * @return true or false
   */
  public static boolean setNeighborhood(int nId, int eventId) {
    var n = NeighborhoodRepository.getNeighborhoodById(nId);
    var event = EventRepository.getEventById(eventId);
    if (n != null && event != null) {
      event.setNeighborhood(n);
      EventRepository.saveEvent(event);
      return true;
    }
    return false;
  }
}
