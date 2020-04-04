package se1app.persistency;

import java.util.List;


/** Database connector configuration. */
public class DatabaseConfig {

  // Connection and H2 settings.
  public String host = "localhost";
  public String dbName = "./test";
  public String user = "sa";
  public String password = "";
  public boolean startWebserver = false;

  // Hibernate settings.
  public boolean showSqlQueries = false;
  public List<Class<?>> annotatedClasses = null;
}
