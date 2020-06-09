import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se1app.datatypes.EventStatus;
import se1app.datatypes.TimeType;
import se1app.entities.*;
import se1app.persistency.DatabaseConfig;
import se1app.persistency.H2Database;
import se1app.repositories.EventRepository;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class EventRepositoryTest {

  private H2Database _db;


  /** Setup the in-memory database. */
  @BeforeEach
  public void setupDatabase() {
    H2Database.configure(new DatabaseConfig() {{
      initInMemory = true;
      annotatedClasses = Arrays.asList(User.class, Neighborhood.class, Event.class);
    }});
    _db = H2Database.getInstance();
  }


  /** Tear down the in-memory database. */
  @AfterEach()
  public void dropDatabase() {
    _db.stop();
  }


  /** Create three entries. */
  private static void createTestEvents() {
    Neighborhood neighborhood = new Neighborhood("Altona", 22769, "Hamburg", "Deutschland");
    Neighborhood neighborhood2 = new Neighborhood("Altona", 22769, "Hamburg", "Deutschland");
    Neighborhood neighborhood3 = new Neighborhood("Altona", 22769, "Hamburg", "Deutschland");
    User user = new User(new Date(80, Calendar.FEBRUARY,1), "Test", "Hallo", "test@test.de", "Teststraße 5", neighborhood);
    User user1 = new User(new Date(80, Calendar.FEBRUARY,1), "Tesadsadst", "Haasdsadllo", "test@test.de", "Teststraße 5", neighborhood);
    EventRepository.createEvent(user, "Waddup", new Date(11,11,4), new TimeType(15,25,17,30), EventStatus.EVENT_PLANNED, neighborhood);
    EventRepository.createEvent(user, "Hey", new Date(11,11,4), new TimeType(15,30,17,30), EventStatus.EVENT_PLANNED, neighborhood2);
    EventRepository.createEvent(user1, "Yo", new Date(11,11,4), new TimeType(15,30,17,30), EventStatus.EVENT_PLANNED, neighborhood3);

  }


  /** Test event creation. */
  @Test
  public void eventCreationTest() {
    assertEquals(0, EventRepository.getAllEvents().size());
    createTestEvents();
    assertEquals(3, EventRepository.getAllEvents().size());
  }


  /** Test event query by ID. */
  @Test
  public void eventQueryTest() {
    createTestEvents();
    var event2 = EventRepository.getEventById(2);
    assertEquals("Hey", event2.getEventName());
  }


  /** Test event deletion. */
  @Test
  public void eventDeletionTest() {
    createTestEvents();
    EventRepository.deleteEventById(3);
    assertEquals(2, EventRepository.getAllEvents().size());
    var event3 = EventRepository.getEventById(3);
    assertEquals(null, event3);
  }
}
