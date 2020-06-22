import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se1app.datatypes.EventStatus;
import se1app.datatypes.TimeType;
import se1app.entities.*;
import se1app.persistency.DatabaseConfig;
import se1app.persistency.H2Database;
import se1app.repositories.EventRepository;
import se1app.repositories.UserRepository;
import se1app.usecases.EventUseCase;

import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;


public class EventUseCaseTest {

  private H2Database _db;


    /** Setup the in-memory database. */
    @BeforeEach
    public void setupDatabase() {
      H2Database.configure(new DatabaseConfig() {{
        initInMemory = true;
        annotatedClasses = Arrays.asList(User.class, Neighborhood.class, Event.class);
      }});
      _db = H2Database.getInstance();
      UserRepository.createUser("Maria", "Testmann", "test@test.de", "Teststraße 5");
      EventRepository.createEvent("Gemeinsam Lernen", new Date(11,11,4), new TimeType("15","20","17","30"), EventStatus.EVENT_PLANNED);
      EventRepository.createEvent("Geburtstag Jeny", new Date(11,11,4), new TimeType("15","20","17","30"), EventStatus.EVENT_PLANNED);
      EventRepository.createEvent("Grillen", new Date(11,11,4), new TimeType("15","20","17","30"), EventStatus.EVENT_PLANNED);
    }


    /** Tear down the in-memory database. */
    @AfterEach()
    public void dropDatabase() {
      _db.stop();
    }


    /** Order a single item (success test #1). */
    @Test
    public void inviteUserToEvent() {
      UserRepository.createUser("Testian", "Testmann", "test@test.de", "Teststraße 5");

      assertTrue(EventUseCase.setOrganizer(UserRepository.getUserById(2).getId(), 1));
    }

    /** Query an non-existing event (failure test #1). */
    @Test
    public void createEventInvalidUserId() {
      assertFalse(EventUseCase.setOrganizer(-12, 1));
    }


    /** Supply 'null' as item list (failure test #2). */
    @Test
    public void createEventInvalidEvent() {
      assertFalse(EventUseCase.setOrganizer(1, -12));
    }
}
