import java.util.Arrays;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.equalTo;
import se1app.entities.Customer;
import se1app.entities.Order;
import se1app.facade.Webserver;
import se1app.persistency.DatabaseConfig;
import se1app.persistency.H2Database;
import se1app.repositories.CustomerRepository;


public class CustomerControllerTest {

  private H2Database _db;
  private Webserver _webserver;


  /** Setup the in-memory database and webserver. */
  @BeforeEach
  public void setup() {
    H2Database.configure(new DatabaseConfig() {
      {
        initInMemory = true;
        annotatedClasses = Arrays.asList(Customer.class, Order.class);
      }
    });
    _db = H2Database.getInstance();
    CustomerRepository.createCustomer("Max", "Mustermann", "max-mustermann123@web.de");
    _webserver = new Webserver(7001, null);
    _webserver.start();
    RestAssured.port = 7001;
    RestAssured.basePath = "api/";
  }


  /** Tear down the in-memory database and webserver. */
  @AfterEach()
  public void teardown() {
    _db.stop();
    _webserver.stop();
  }


  // ---------------------------------------
  // Documentation: http://rest-assured.io
  // ---------------------------------------

  @Test
  public void getCustomersTest() {
    given().
    when().
    get("/customers").
    then().
    statusCode(200).
    body("lastName", hasItems("Mustermann"));  // collection contains
  }


  @Test
  public void getSpecificCustomerSuccessTest() {
    given().
    when().
    get("/customers/1").
    then().
    statusCode(200).
    body("lastName", equalTo("Mustermann"));  // single entry
  }


  @Test
  public void getSpecificCustomerFailureTest() {
    given().
    when().
    get("/customers/12").
    then().
    statusCode(404);
  }


  @Test
  public void deleteCustomerTest() {
    given().
    when().
    delete("/customers/1").
    then().
    statusCode(200); // deletion O.K.
    given().
    when().
    get("/customers/1").
    then().
    statusCode(404); // and afterwards no longer present!
  }


  @Test
  void createCustomerSuccess() {
    given().
    contentType(ContentType.JSON).
    body(new Customer("Maren", "Musterfrau", "mm@gmail.com")).
    when().
    post("/customers").
    then().
    statusCode(201);
    given().
    when().
    get("/customers/2").
    then().
    statusCode(200).
    body("lastName", equalTo("Musterfrau"));
  }
}
