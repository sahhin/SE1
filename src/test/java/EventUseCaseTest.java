import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se1app.entities.*;
import se1app.persistency.DatabaseConfig;
import se1app.persistency.H2Database;
import se1app.repositories.CustomerRepository;
import se1app.repositories.EventRepository;
import se1app.usecases.EventUseCase;
import se1app.usecases.OrderUseCase;

import java.util.ArrayList;
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
      Neighborhood neighborhood = new Neighborhood("Altona", 22769, "Hamburg", "Deutschland");
      User user = new User(new Date(80,1,1), "Test", "Hallo", "test@test.de", "Teststraße 5", neighborhood);
      User user2 = new User(new Date(80,1,1), "Testian", "Testmann", "test@test.de", "Teststraße 5", neighborhood);
      EventRepository.createEvent(user, "Waddup", new Date(11,11,11),"11:11", "11:12", 12, 0, neighborhood);
      EventRepository.createEvent(user, "Hey", new Date(11,11,11),"11:11", "11:12", 12, 0, neighborhood);
      EventRepository.createEvent(user, "Yo", new Date(11,11,11),"11:11", "11:12", 12, 0, neighborhood);
    }


    /** Tear down the in-memory database. */
    @AfterEach()
    public void dropDatabase() {
      _db.stop();
    }


    /** Order a single item (success test #1). */
    @Test
    public void inviteUserToEvent() {
      Neighborhood neighborhood = new Neighborhood("Altona", 22769, "Hamburg", "Deutschland");
      User user2 = new User(new Date(80,1,1), "Testian", "Testmann", "test@test.de", "Teststraße 5", neighborhood);
      assertTrue(EventUseCase.inviteUser(user2.getId(), 1));
    }

    /** Query an non-existing user (failure test #1). */
    @Test
    public void createEventInvalidUserId() {
      assertFalse(EventUseCase.inviteUser(-12, 1));
    }


    /** Supply 'null' as item list (failure test #2). */
    @Test
    public void createEventInvalidEvent() {
      assertFalse(EventUseCase.inviteUser(1, -12));
    }
}
