import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se1app.entities.Event;
import se1app.entities.Neighborhood;
import se1app.entities.User;
import se1app.persistency.DatabaseConfig;
import se1app.persistency.H2Database;
import se1app.repositories.NeighborhoodRepository;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class NeighborhoodRepositoryTest {

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
  private static void createTestNeighborhoods() {
    NeighborhoodRepository.createNeighborhood("Altona", 22769, "Hamburg", "Deutschland");
    NeighborhoodRepository.createNeighborhood("Wandsbek", 20535, "Hamburg", "Deutschland");
    NeighborhoodRepository.createNeighborhood("Bergedorf", 21029, "Hamburg", "Deutschland");
  }


  /** Test neighborhood creation. */
  @Test
  public void neighborhoodCreationTest() {
    assertEquals(0, NeighborhoodRepository.getAllNeighborhoods().size());
    createTestNeighborhoods();
    assertEquals(3, NeighborhoodRepository.getAllNeighborhoods().size());
  }


  /** Test neighborhood query by ID. */
  @Test
  public void neighborhoodQueryTest() {
    createTestNeighborhoods();
    var neighborhood2 = NeighborhoodRepository.getNeighborhoodById(2);
    assertEquals("Wandsbek", neighborhood2.getNeighborhoodName());
  }


  /** Test neighborhood deletion. */
  @Test
  public void neighborhoodDeletionTest() {
    createTestNeighborhoods();
    NeighborhoodRepository.deleteNeighborhoodById(3);
    assertEquals(2, NeighborhoodRepository.getAllNeighborhoods().size());
    var neighborhood3 = NeighborhoodRepository.getNeighborhoodById(3);
    assertEquals(null, neighborhood3);
  }
}
