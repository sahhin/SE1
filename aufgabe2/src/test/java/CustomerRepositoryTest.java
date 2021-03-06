import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Arrays;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se1app.entities.Customer;
import se1app.entities.Order;
import se1app.persistency.DatabaseConfig;
import se1app.persistency.H2Database;
import se1app.repositories.CustomerRepository;


public class CustomerRepositoryTest {

  private H2Database _db;


  /** Setup the in-memory database. */
  @BeforeEach
  public void setupDatabase() {
    H2Database.configure(new DatabaseConfig() {{
      initInMemory = true;
      annotatedClasses = Arrays.asList(Customer.class, Order.class);
    }});
    _db = H2Database.getInstance();
  }


  /** Tear down the in-memory database. */
  @AfterEach()
  public void dropDatabase() {
    _db.stop();
  }


  /** Create three entries. */
  private static void createTestCustomers() {
    CustomerRepository.createCustomer("Antonia", "Aalglatt", "aa@mail.com", "Berliner Tor 7");
    CustomerRepository.createCustomer("Bernd", "Bratwurst", "bb@mail.com", "Berliner Tor 7");
    CustomerRepository.createCustomer("Charlie", "Chaos", "cc@mail.com", "Berliner Tor 7");
  }


  /** Test customer creation. */
  @Test
  public void customerCreationTest() {
    assertEquals(0, CustomerRepository.getAllCustomers().size());
    createTestCustomers();
    assertEquals(3, CustomerRepository.getAllCustomers().size());
  }


  /** Test customer query by ID. */
  @Test
  public void customerQueryTest() {
    createTestCustomers();
    var customer2 = CustomerRepository.getCustomerById(2);
    assertEquals("Bernd", customer2.getFirstName());
  }


  /** Test customer deletion. */
  @Test
  public void customerDeletionTest() {
    createTestCustomers();
    CustomerRepository.deleteCustomerById(3);
    assertEquals(2, CustomerRepository.getAllCustomers().size());
    var customer3 = CustomerRepository.getCustomerById(3);
    assertEquals(null, customer3);
  }
}
