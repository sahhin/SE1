package se1app.usecases;

import se1app.repositories.EventRepository;
import se1app.repositories.UserRepository;


/** Use case class to place customer orders. */
public class EventUseCase {


  /** Invite user to an event
   * @param userId Identifier of the customer.
   * @param items A list of items to order.
   * @return 'True' if the order was successfully placed. 'False' otherwise. */
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
