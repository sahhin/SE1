package se1app;

import java.util.Arrays;
import se1app.entities.*;
import se1app.facade.Webserver;
import se1app.persistency.*;
import se1app.repositories.CustomerRepository;


/** Main class of our application, containing the entry point and setup. */
public class Application {

  private H2Database _dbServer;  // Database server (h2 and Hibernate).
  private Webserver _webServer;  // Web server (Javalin framework).


  /** Create the application. */
  public Application() {
    _dbServer = H2Database.getInstance();
    _webServer = new Webserver(7000, null);
  }


  /** Start the web server and listen for client connections.
   * This a blocking call, running until shutdown is signaled. */
  public void enterListeningMode() {
    _webServer.start();
  }


  /** Stop the application. This kills all server processes. */
  public void stop() {
    _dbServer.stop();
    _webServer.stop();
  }


  /** Fill an empty database with some test data. */
  public void insertTestData() {
    CustomerRepository.createCustomer("Horst", "Müller", "thehorst_64@gmail.com");
    CustomerRepository.createCustomer("Anna", "Smith", "a.smith@outlook.uk");
    CustomerRepository.createCustomer("Marlene", "Schulze", "ms83@bremen-online.de");
    CustomerRepository.createCustomer("Anton", "Smirnov", "asmi-89@inbox.ru");
    var anna = CustomerRepository.getCustomerById(2);
    anna.orderItems(Arrays.asList("fish", "chips"));
    CustomerRepository.saveCustomer(anna);
    var anton = CustomerRepository.getCustomerById(4);
    anton.orderItems(Arrays.asList("pirog", "beef", "salad"));
    anton.orderItems(Arrays.asList("vodka"));
    CustomerRepository.saveCustomer(anton);
  }




  // --------------------------------------------------------------------------

  /** Program main entry point.
   * @param args Command line arguments. Not used here! */
  public static void main(String[] args) {

    // Configure the database connection.
    H2Database.configure(new DatabaseConfig() {{
      dbName = "./customers";
      startWebserver = true;
      annotatedClasses = Arrays.asList(Customer.class, Order.class);
      //showSqlQueries = true;
    }});


    // Create application.
    var application = new Application();

    // Register shutdown hook.
    Runtime.getRuntime().addShutdownHook(new Thread( () -> {
      application.stop();
    }));

    // If this is an empty database, populate tables with test data.
    if (CustomerRepository.getAllCustomers().size() == 0) {
      System.out.println("Empty database found! Filling it with test data!");
      application.insertTestData();
    }

    // Print all customers to console.
    CustomerRepository.printCustomerTable();

    // All manual setup done up to here.
    // Start the web server and listen for connections until shutdown is ordered.
    application.enterListeningMode();
  }
}


// Hibernate: https://www.marcobehler.com/guides/java-databases-jdbc-hibernate-spring-data#_java_orm_frameworks_hibernate_jpa_and_more
// H2 database: http://www.h2database.com/html/features.html
// Javalin UI Tutorial: https://javalin.io/tutorials/simple-frontends-with-javalin-and-vue
// Mapping Attributnamen -> Datenbank: https://www.baeldung.com/hibernate-naming-strategy
