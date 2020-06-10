import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se1app.datatypes.EventStatus;
import se1app.datatypes.TimeType;
import se1app.entities.Event;
import se1app.entities.Neighborhood;
import se1app.entities.User;
import se1app.persistency.DatabaseConfig;
import se1app.persistency.H2Database;
import se1app.repositories.EventRepository;
import se1app.repositories.NeighborhoodRepository;
import se1app.repositories.UserRepository;

import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class UserRepositoryTest {

    private H2Database _db;


    /**
     * Setup the in-memory database.
     */
    @BeforeEach
    public void setupDatabase() {
        H2Database.configure(new DatabaseConfig() {{
            initInMemory = true;
            annotatedClasses = Arrays.asList(User.class, Neighborhood.class, Event.class);
        }});
        _db = H2Database.getInstance();
    }


    /**
     * Tear down the in-memory database.
     */
    @AfterEach()
    public void dropDatabase() {
        _db.stop();
    }


    /**
     * Create three entries.
     */
    private static void createTestUsers() {
        UserRepository.createUser( "Homer J.", "Simpson", "simpson@sector7g.com", "Evergreen Terrace 742");
        UserRepository.createUser("Frank", "Rosin", "f.rosin@kabel1.de", "Rosins Restaurants 5");
        UserRepository.createUser("Rick", "Sanchez", "pickle@rick.com", "Dimension C 132");
    }


    /**
     * Test user creation.
     */
    @Test
    public void userCreationTest() {
        assertEquals(0, UserRepository.getAllUsers().size());
        createTestUsers();
        assertEquals(3, UserRepository.getAllUsers().size());
    }


    /**
     * Test user query by ID.
     */
    @Test
    public void userQueryTest() {
        createTestUsers();
        var user2 = UserRepository.getUserById(2);
        assertEquals("Frank", user2.getFirstName());
    }


    /**
     * Test user deletion.
     */
    @Test
    public void userDeletionTest() {
        createTestUsers();
        UserRepository.deleteUserById(3);
        assertEquals(2, UserRepository.getAllUsers().size());
        var user3 = UserRepository.getUserById(3);
        assertEquals(null, user3);
    }
}
