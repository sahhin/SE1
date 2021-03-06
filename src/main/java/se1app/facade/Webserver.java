package se1app.facade;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.plugin.rendering.vue.VueComponent;

import java.nio.file.Paths;


/** The Javalin web server. */
public class Webserver {

  private Javalin _server;  // Javalin web server process.
  private int _port;        // Web server port.


  /** Create a new web server.
   * @param port Web server port.
   * @param htmlDir HTML directory for static content delivery. May be set to 'null' to disable it! */
  public Webserver(int port, String htmlDir) {
    _port = port;
    _server = Javalin.create(
      config -> {
        config.enableWebjars();
        config.enableCorsForAllOrigins();
        if (htmlDir != null) {
          var staticPath = Paths.get(System.getProperty("user.dir"), htmlDir).toString();
          config.addStaticFiles(staticPath, Location.EXTERNAL);
        }
        config.defaultContentType = "application/json";
        config.showJavalinBanner = false;
      }
    );
    defineRoutes();
  }


  /** Start the web server. */
  public void start() {
    _server.start(_port);
  }


  /** Stop the web server. */
  public void stop() {
    _server.stop();
  }


  /** Define backend routes (REST endpoints). */
  private void defineRoutes() {
    new EventController(_server);
    new UserController(_server);
    new NeighborhoodController(_server);
    _server.get("/", new VueComponent("<home-comp></home-comp>"));
    _server.get("/users", new VueComponent("<users-comp></users-comp>"));
    _server.get("/events", new VueComponent("<events-comp></events-comp>"));
    _server.get("/neighborhoods", new VueComponent("<neighborhoods-comp></neighborhoods-comp>"));
    _server.get("/about", new VueComponent("<about-comp></about-comp>"));
  }
}
