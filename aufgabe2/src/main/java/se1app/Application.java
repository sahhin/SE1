package se1app;

import java.util.Arrays;
import se1app.entities.*;
import se1app.facade.Webserver;
import se1app.persistency.*;
import se1app.repositories.CustomerRepository;
import se1app.usecases.OrderUseCase;


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
    public static User createUser(Date userBirthday, String firstName, String lastName, String userEmail, String userAdress) {

      UserRepository.createUser(new Date("01.01.1980"), "Horst", "Müller", "thehorst_64@gmail.com", "Berliner Tor 7");
      //    CustomerRepository.createCustomer("Anna", "Smith", "a.smith@outlook.uk", "Berliner Tor 7");
//    CustomerRepository.createCustomer("Marlene", "Schulze", "ms83@bremen-online.de", "Berliner Tor 7");
//    CustomerRepository.createCustomer("Anton", "Smirnov", "asmi-89@inbox.ru", "Berliner Tor 7");
//    OrderUseCase.orderItems(2, Arrays.asList("fish", "chips"));
//    OrderUseCase.orderItems(4, Arrays.asList("pirog", "beef", "salad"));
//    OrderUseCase.orderItems(4, Arrays.asList("vodka"));
  }




  // --------------------------------------------------------------------------

  /** Program main entry point.
   * @param args Command line arguments. Not used here! */
  public static void main(String[] args) {

    // Configure the database connection.
    H2Database.configure(new DatabaseConfig() {{
      dbName = "./users";
      annotatedClasses = Arrays.asList(User.class);
      //startWebserver = true;
      //showSqlQueries = true;
    }});


    // Create application.
    var application = new Application();

    // Register shutdown hook.
    Runtime.getRuntime().addShutdownHook(new Thread( () -> {
      application.stop();
    }));

    // If this is an empty database, populate tables with test data.
    if (CustomerRepository.getAllUsers().size() == 0) {
      System.out.println("Empty database found! Filling it with test data!");
      application.insertTestData();
    }

    // Print all customers to console.
    UserRepository.printCustomerTable();

    // All manual setup done up to here.
    // Start the web server and listen for connections until shutdown is ordered.
    application.enterListeningMode();
  }
}


// Hibernate: https://www.marcobehler.com/guides/java-databases-jdbc-hibernate-spring-data#_java_orm_frameworks_hibernate_jpa_and_more
// H2 database: http://www.h2database.com/html/features.html
// Javalin UI Tutorial: https://javalin.io/tutorials/simple-frontends-with-javalin-and-vue
// Mapping Attributnamen -> Datenbank: https://www.baeldung.com/hibernate-naming-strategy
