import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se1app.datatypes.EventStatus;
import se1app.datatypes.TimeType;
import se1app.entities.*;
import se1app.persistency.DatabaseConfig;
import se1app.persistency.H2Database;
import se1app.repositories.EventRepository;
import se1app.repositories.NeighborhoodRepository;
import se1app.repositories.UserRepository;

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

    EventRepository.createEvent("SE 1", new Date(11,11,4), new TimeType(15,25,17,30), EventStatus.EVENT_PLANNED);
    EventRepository.createEvent("AD", new Date(11,11,4), new TimeType(15,30,17,30), EventStatus.EVENT_PLANNED);
    EventRepository.createEvent("BWL II", new Date(11,11,4), new TimeType(15,30,17,30), EventStatus.EVENT_PLANNED);

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
    assertEquals("AD", event2.getEventName());
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
