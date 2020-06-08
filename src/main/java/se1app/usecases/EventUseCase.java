package se1app.usecases;

import se1app.repositories.EventRepository;
import se1app.repositories.UserRepository;


/** Use case class to place event orders. */
public class EventUseCase {

  /**
   *
   * @param userId Identifier of the user.
   * @param eventId Identifier of the event.
   * @return
   */
  public static boolean inviteUser(int userId, int eventId) {
    var user = UserRepository.getUserById(userId);
    var event = EventRepository.getEventById(eventId);
    if (user != null && event != null) {
      event.inviteUsers(user);
      EventRepository.saveEvent(event);
      return true;
    }
    return false;
  }
}
