package se1app.persistency;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import static java.util.Map.entry;
import java.util.logging.Level;
import org.h2.tools.Server;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;


/** H2 database with Hibernate connector. */
public class H2Database {

  private Server _dbTcpServer;  // Main database server.
  private Server _dbWebServer;  // Auxiliary web server for browser-based access.
  private Session _session;     // Hibernate connection handler to perform SQL queries.

  private static boolean _isInitialized;               // Flag set to 'true' on successful initialization.
  private static DatabaseConfig _config = null;        // Configuration object with init parameters.
  private static H2Database _serverInstance = null;    // Reference to the singleton instance.


  /** Create a new H2 database with Hibernate session attached to it.
   * This constructor is private because of singleton pattern! */
  private H2Database() {
    if (_config == null) {
      configure(new DatabaseConfig()); // Initialize with default configuration.
    }
    String conString = "";
    try {
      if (_config.initInMemory) {
        conString = "jdbc:h2:mem:"+_config.dbName;
      }
      else {
        _dbTcpServer = Server.createTcpServer("-ifNotExists").start();
        System.out.println("[H2Database] "+_dbTcpServer.getStatus());
        if (_config.startWebserver) {
          _dbWebServer = Server.createWebServer().start();
          System.out.println("[H2Database] "+_dbWebServer.getStatus());
        }
        conString = "jdbc:h2:tcp://"+_config.host+":"+_dbTcpServer.getPort()+"/"+_config.dbName;
      }
    }
    catch (SQLException ex) {
      System.err.println("[H2Database] Failed to initialize database! \n"+ex);
    }

    // ----- Hibernate initialization -----

    // Hide all these nasty Hibernate startup messages.
    java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);

    // Create and configure Hibernate registry builder.
    var regBuilder = new StandardServiceRegistryBuilder();
    regBuilder.applySettings(Map.ofEntries(
      entry(Environment.DRIVER, "org.h2.Driver"),
      entry(Environment.DIALECT, "org.hibernate.dialect.H2Dialect"),
      entry(Environment.URL, conString),
      entry(Environment.USER, _config.user),
      entry(Environment.PASS, _config.password),
      entry(Environment.HBM2DDL_AUTO, "update"),
      entry(Environment.SHOW_SQL, Boolean.toString(_config.showSqlQueries))
    ));

    // Create registry and metadata sources.
    var registry = regBuilder.build();
    var sources = new MetadataSources(registry);
    if (_config.annotatedClasses != null) {
      for (Class<?> annotatedClass : _config.annotatedClasses) {
        sources.addAnnotatedClass(annotatedClass);
      }
    }

    // Create Metadata and session factory.
    var metadata = sources.buildMetadata();
    var sessionFactory = metadata.getSessionFactoryBuilder().build();
    _session = sessionFactory.openSession();

    _isInitialized = true;
  }


  /** Set a custom configuration for the database connector.
   * @param config Config object with database settings. */
  public static void configure(DatabaseConfig config) {
    _config = config;
  }


  /** Get an instance of the H2 database. The database connector is
   * implemented as a singleton and gets initialized on first access.
   * @return The database object or 'null' if an exception occured. */
  public static H2Database getInstance() {
    if (!_isInitialized) {
      _serverInstance = new H2Database();
    }
    return _serverInstance;
  }


  /** Get a Hibernate session to the H2 database.
   * @return The Hibernate session. May be 'null' on initialization failure. */
  public Session getSession() {
    return _session;
  }


  /** Shutdown all server threads of the database connector. */
  public void stop() {
    if (_session != null) {
      try { _session.close(); }
      catch (HibernateException ex) { /* */ }
    }
    if (_config.initInMemory) {
      try {
        var conn = DriverManager.getConnection("jdbc:h2:mem:"+_config.dbName, _config.user, _config.password);
        var stmt = conn.createStatement();
        stmt.execute("DROP ALL OBJECTS");
        conn.close();
      }
      catch (SQLException ex) {
        System.err.println("Failed to tear down in-memory database: "+ex);
      }
    }
    else {
      if (_dbTcpServer != null) _dbTcpServer.stop();
      if (_dbWebServer != null) _dbWebServer.stop();
      System.out.println("[H2Database] Connection closed and server threads stopped!");
    }
    _isInitialized = false;
  }
}
