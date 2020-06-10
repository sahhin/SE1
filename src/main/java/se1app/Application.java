package se1app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import se1app.datatypes.EventStatus;
import se1app.datatypes.TimeType;
import se1app.entities.*;
import se1app.facade.Webserver;
import se1app.persistency.*;
import se1app.repositories.EventRepository;
import se1app.repositories.NeighborhoodRepository;
import se1app.repositories.UserRepository;
import se1app.usecases.EventUseCase;


/**
 * Main class of our application, containing the entry point and setup.
 */
public class Application {

    private H2Database _dbServer;  // Database server (h2 and Hibernate).
    private Webserver _webServer;  // Web server (Javalin framework).


    /**
     * Create the application.
     */
    public Application() {
        _dbServer = H2Database.getInstance();
        _webServer = new Webserver(7000, "src/main/resources/webroot");
    }


    /**
     * Start the web server and listen for client connections.
     * This a blocking call, running until shutdown is signaled.
     */
    public void enterListeningMode() {
        _webServer.start();
    }


    /**
     * Stop the application. This kills all server processes.
     */
    public void stop() {
        _dbServer.stop();
        _webServer.stop();
    }


    /**
     * Fill an empty database with some test data.
     */
    public void insertTestData() {
        NeighborhoodRepository.createNeighborhood("Altona2", 22769, "Hamburg", "Deutschland");
        UserRepository.createUser("Sahin", "Tekes", "stekes@haw.de", "Strasse 5");
        EventRepository.createEvent("Testevent", new Date(2020, 1, 1), new TimeType(15, 15, 15, 51), EventStatus.EVENT_PLANNED);
        EventUseCase.setOrganizer(1, 1);
        EventUseCase.setNeighborhood(1, 1);
        UserRepository.getUserById(1).setNeighborhood(NeighborhoodRepository.getNeighborhoodById(1));

        UserRepository.createUser("Frank", "Rosin", "tomatenundschalotten@weisswein.de", "Rosinstraße 5");
        UserRepository.createUser("Homer", "Simpson", "hsimpson@sector7g.de", "Evergreen Terrace 742");
        UserRepository.createUser("Gordon", "Shumway", "alf@melmac.de", "Melmac 20215487");

        UserRepository.getUserById(2).setNeighborhood(NeighborhoodRepository.getNeighborhoodById(1));
        UserRepository.getUserById(3).setNeighborhood(NeighborhoodRepository.getNeighborhoodById(1));
        UserRepository.getUserById(4).setNeighborhood(NeighborhoodRepository.getNeighborhoodById(1));

        List<User> users = new ArrayList<>();
        users.add(UserRepository.getUserById(2));
        users.add(UserRepository.getUserById(3));
        users.add(UserRepository.getUserById(4));
        EventUseCase.addParticipants(users, 1);

    }


    // --------------------------------------------------------------------------

    /**
     * Program main entry point.
     *
     * @param args Command line arguments. Not used here!
     */
    public static void main(String[] args) {

        // Configure the database connection.
        H2Database.configure(new DatabaseConfig() {{
            dbName = "./neighborino";
            annotatedClasses = Arrays.asList(User.class, Neighborhood.class, Event.class);
            startWebserver = true;
            showSqlQueries = true;
        }});


        // Create application.
        var application = new Application();

        // Register shutdown hook.
        Runtime.getRuntime().addShutdownHook(new Thread(application::stop));

        // If this is an empty database, populate tables with test data.
        if (UserRepository.getAllUsers().size() == 0) {
            System.out.println("Empty database found! Filling it with test data!");
            application.insertTestData();
        }

//        // Print all customers to console.
//        UserRepository.printUserTable();

        // All manual setup done up to here.
        // Start the web server and listen for connections until shutdown is ordered.
        application.enterListeningMode();
    }
}


// Hibernate: https://www.marcobehler.com/guides/java-databases-jdbc-hibernate-spring-data#_java_orm_frameworks_hibernate_jpa_and_more
// H2 database: http://www.h2database.com/html/features.html
// Javalin UI Tutorial: https://javalin.io/tutorials/simple-frontends-with-javalin-and-vue
// Mapping Attributnamen -> Datenbank: https://www.baeldung.com/hibernate-naming-strategy

// Testdatengenerierung
// -----------------------------------
// - https://www.fakenamegenerator.com/gen-random-gr-gr.php
// - https://www.mockaroo.com
