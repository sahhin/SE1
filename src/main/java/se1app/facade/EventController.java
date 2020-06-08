package se1app.facade;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import se1app.datatypes.AdressType;
import se1app.datatypes.EmailType;
import se1app.entities.Event;
import se1app.entities.Neighborhood;
import se1app.entities.User;
import se1app.exceptions.InvalidEmailException;
import se1app.repositories.EventRepository;
import se1app.repositories.UserRepository;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * REST controller for the customer endpoint.
 */
public class EventController {


    /**
     * Create a new customer controller.
     *
     * @param server Javalin server handle to register REST endpoints.
     */
    public EventController(Javalin server) {
        server.get("/api/events", EventController::getAllEvents);
        server.get("/api/events/:event-id", EventController::getEventById);
        server.delete("/api/events/:event-id", EventController::deleteEventById);
        server.post("/api/events", EventController::createEvent);
        server.put("/api/events/:event-id", EventController::updateEvent);
    }


    /**
     * Get all customers.
     *
     * @param ctx HTTP context (request/response handle).
     */
    private static void getAllEvents(Context ctx) {
        var eventList = EventRepository.getAllEvents();
        ctx.json(eventList);
    }


    /**
     * Get a specific customer by URL parameter ID.
     *
     * @param ctx HTTP context (request/response handle).
     */
    private static void getEventById(Context ctx) {
        var event = fetchEvent(ctx, "getEventById");
        if (event != null) ctx.json(event);
    }


    /**
     * Delete a customer by URL parameter ID.
     *
     * @param ctx HTTP context (request/response handle).
     */
    private static void deleteEventById(Context ctx) {
        var event = fetchEvent(ctx, "deleteEventById");
        if (event != null) {
            EventRepository.deleteEventById(event.getEventId());
            ctx.res.setStatus(200);
        }
    }

    /**
     * Create a new customer with the data specified in request body.
     *
     * @param ctx HTTP context (request/response handle).
     */
    private static void createEvent(Context ctx) throws IOException {
        try {
            var jsonNode = new ObjectMapper().readTree(ctx.body());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Neighborhood neighborhood = new Neighborhood("Altona", 22769, "Hamburg", "Deutschland");
            User user = new User(new Date(80,1,1), "Test", "Hallo", "test@test.de", "Teststr.", neighborhood);
            Event events = new Event(user, "Test", new Date(2020, 1, 1), "15:00", "16:00", 12, 1,neighborhood);
            var savedEvent = EventRepository.createEvent(
                    user,
                    jsonNode.get("eventName").asText(),
                    sdf.parse(jsonNode.get("eventDate").toString()),
                    jsonNode.get("eventStartTime").asText(),
                    jsonNode.get("eventEndTime").asText(),
                    jsonNode.get("eventOrganizerId").asInt(),
                    jsonNode.get("eventStatusId").asInt(),
                    neighborhood
                    );
            if (savedEvent != null) ctx.res.setStatus(201); // 201 - Created (POST success)
            else ctx.res.setStatus(500);                       // 500 - Internal Server Error
        } catch (JsonProcessingException | ParseException ex) {
            var msg = "JSON parser exception: " + ex;
            System.err.println("[UserController] createEvent: " + msg);
            ctx.res.sendError(400, msg);
        }
    }


    /**
     * Update the data of a customer specified by URL parameter.
     *
     * @param ctx HTTP context (request/response handle).
     */
    private static void updateEvent(Context ctx) throws IOException {
        var event = fetchEvent(ctx, "updateEvent");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (event != null) {
            try {
                var jsonNode = new ObjectMapper().readTree(ctx.body());
                if (jsonNode.get("eventDate") != null) {
                    event.setEventDate(sdf.parse(jsonNode.get("eventDate").toString()));
                }
                if (jsonNode.get("eventName") != null) {
                    event.setEventName(jsonNode.get("eventName").asText());
                }
                if (jsonNode.get("eventStartTime") != null) {
                    event.setEventStartTime(jsonNode.get("eventStartTime").asText());
                }
                if (jsonNode.get("eventEndTime") != null) {
                    event.setEventEndTime(jsonNode.get("eventEndTime").asText());
                }
                if (jsonNode.get("eventStatusId") != null) {
                    event.setEventStatusId(jsonNode.get("eventStatusId").asInt());
                }
                if (jsonNode.get("eventOrganizerId") != null) {
                    event.setEventEndTime(jsonNode.get("eventEndTime").asText());
                }

                EventRepository.saveEvent(event);
                ctx.res.setStatus(200);
            } catch (JsonProcessingException | InvalidEmailException | ParseException ex) {
                System.err.println("[UserController] updateUser: " + ex);
                ctx.res.sendError(400, ex.toString());
            }
        }
    }


    /**
     * Helper function to parse a customer ID from the URL and retrieve it.
     * This method also performs HTTP-400 and 404 error handling.
     *
     * @param ctx          The web server context.
     * @param endpointDesc Optional description of endpoint for console error message. May be null!
     * @return The customer or 'null', if access failed (query invalid or customer not found).
     */
    private static Event fetchEvent(Context ctx, String endpointDesc) {
        try {
            try {
                var eventId = Integer.parseInt(ctx.pathParam("event-id"));
                var event = EventRepository.getEventById(eventId);
                if (event != null) return event;
                else {
                    var msg = "Event #" + eventId + " not found!";
                    if (endpointDesc != null) System.err.println("[EventController] " + endpointDesc + ": " + msg);
                    ctx.res.sendError(404, msg);
                }
            } catch (NumberFormatException ex) {
                var msg = "Failed to parse user identifier!";
                if (endpointDesc != null) System.err.println("[EventController] " + endpointDesc + ": " + msg);
                ctx.res.sendError(400, msg);
            }
        } catch (IOException ex) {
            System.err.println("[EventController] fetchEvent: IOException: " + ex);
        }
        return null;
    }
}
