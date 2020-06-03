import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se1app.entities.Customer;
import se1app.entities.Order;
import se1app.persistency.DatabaseConfig;
import se1app.persistency.H2Database;
import se1app.repositories.CustomerRepository;
import se1app.usecases.OrderUseCase;


public class OrderUseCaseTest {

  private H2Database _db;


    /** Setup the in-memory database. */
    @BeforeEach
    public void setupDatabase() {
      H2Database.configure(new DatabaseConfig() {{
        initInMemory = true;
        annotatedClasses = Arrays.asList(Customer.class, Order.class);
      }});
      _db = H2Database.getInstance();
      CustomerRepository.createCustomer("Max", "Mustermann", "max-mustermann123@web.de");
    }


    /** Tear down the in-memory database. */
    @AfterEach()
    public void dropDatabase() {
      _db.stop();
    }


    /** Order a single item (success test #1). */
    @Test
    public void createOrderWithOneItem() {
      assertTrue(OrderUseCase.orderItem(1, "cheesecake"));
      var orders = CustomerRepository.getCustomerById(1).getOrders();
      assertEquals(1, orders.size());
      assertEquals(1, orders.get(0).getItems().size());
    }


    /** Order multiple items (success test #2). */
    @Test
    public void createOrderWithManyItems() {
      assertTrue(OrderUseCase.orderItems(1, Arrays.asList("chocolate", "cookies", "peanuts")));
      var orders = CustomerRepository.getCustomerById(1).getOrders();
      assertEquals(1, orders.size());
      assertEquals(3, orders.get(0).getItems().size());
    }


    /** Query an non-existing user (failure test #1). */
    @Test
    public void createOrderInvalidUserId() {
      assertFalse(OrderUseCase.orderItem(-12, "something"));
    }


    /** Supply 'null' as item list (failure test #2). */
    @Test
    public void createOrderNullParameter() {
      assertFalse(OrderUseCase.orderItems(1, null));
    }


    /** Supply an empty item list (failure test #3). */
    @Test
    public void createOrderEmptyItemList() {
      assertFalse(OrderUseCase.orderItems(1, new ArrayList<String>()));
    }
}
