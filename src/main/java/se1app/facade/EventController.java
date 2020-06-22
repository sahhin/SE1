package se1app.facade;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import se1app.datatypes.EventStatus;
import se1app.datatypes.TimeType;
import se1app.entities.Event;
import se1app.entities.User;
import se1app.exceptions.InvalidEmailException;
import se1app.persistency.H2Database;
import se1app.repositories.EventRepository;
import se1app.repositories.NeighborhoodRepository;
import se1app.repositories.UserRepository;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * REST controller for the event endpoint.
 */
public class EventController {


    /**
     * Create a new event controller.
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
     * Get all events.
     *
     * @param ctx HTTP context (request/response handle).
     */
    private static void getAllEvents(Context ctx) {
        var eventList = EventRepository.getAllEvents();
        var userList = UserRepository.getAllUsers();
        var neighborhoodList = NeighborhoodRepository.getAllNeighborhoods();
        List<Object> sections = new ArrayList<Object>();
        sections.add(eventList);
        sections.add(userList);
        sections.add(neighborhoodList);
        ctx.json(sections);
    }


    /**
     * Get a specific event by URL parameter ID.
     *
     * @param ctx HTTP context (request/response handle).
     */
    private static void getEventById(Context ctx) {
        var event = fetchEvent(ctx, "getEventById");
        if (event != null) ctx.json(event);
    }


    /**
     * Delete a event by URL parameter ID.
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
     * Create a new event with the data specified in request body.
     *
     * @param ctx HTTP context (request/response handle).
     */
    private static void createEvent(Context ctx) throws IOException {
        System.out.println(ctx.body());
        try {
            var jsonNode = new ObjectMapper().readTree(ctx.body());
            String[] times = jsonNode.get("eventTime").get("time").asText().split("-");

            var savedEvent = EventRepository.createEvent(
                    jsonNode.get("eventName").asText(),
                    new Date(),
                    new TimeType(
                            times[0].substring(0, times[0].indexOf(':')),
                            times[0].substring(times[0].indexOf(':') + 1).strip(),
                            times[1].substring(0, times[1].indexOf(':')).strip(),
                            times[1].substring(times[1].indexOf(':') + 1)
                    ),
                    EventStatus.EVENT_PLANNED
            );

            savedEvent.setNeighborhood(NeighborhoodRepository.getNeighborhoodById(jsonNode.get("_neighborhood").asInt()));
            savedEvent.setOrganiser(UserRepository.getUserById(jsonNode.get("_user").asInt()));

            String[] participants = jsonNode.get("_eventUser").asText().split(",");
//            System.out.println(jsonNode.get("_eventUser").asText());
            List<User> newParticipants = new ArrayList<User>();
            for (int i = 0; i < participants.length; i++) {
                newParticipants.add(UserRepository.getUserById(Integer.parseInt(participants[i])));
            }
            savedEvent.addUsers(newParticipants);
            EventRepository.saveEvent(savedEvent);
            if (savedEvent != null) ctx.res.setStatus(201); // 201 - Created (POST success)
            else ctx.res.setStatus(500);                       // 500 - Internal Server Error
        } catch (JsonProcessingException ex) {
            var session = H2Database.getInstance().getSession();
            var transaction = session.beginTransaction();
            transaction.rollback();
            var msg = "JSON parser exception: " + ex;
            System.err.println("[EventController] createEvent: " + msg);
            ctx.res.sendError(400, msg);
        }
    }


    /**
     * Update the data of a customer specified by URL parameter.
     *
     * @param ctx HTTP context (request/response handle).
     */
    private static void updateEvent(Context ctx) throws IOException {
        System.out.println(ctx.body());
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
//                "15:15 - 15:15"
                if (jsonNode.get("eventTime") != null) {
                    String[] eventTime = jsonNode.get("eventTime").asText().split("-");
                    event.setEventTime(new TimeType(
                            eventTime[0].substring(0, eventTime[0].indexOf(':')),
                            eventTime[0].substring(eventTime[0].indexOf(':') + 1).strip(),
                            eventTime[1].substring(0, eventTime[1].indexOf(':')).strip(),
                            eventTime[1].substring(eventTime[1].indexOf(':') + 1)));
                }
                if (jsonNode.get("eventStatusId") != null) {
                    event.setEventStatusId(EventStatus.valueOf(jsonNode.get("eventStatusId").asText()));
                }
                if (jsonNode.get("neighborhoodId") != null) {
                    event.setNeighborhood(NeighborhoodRepository.getNeighborhoodById(jsonNode.get("neighborhoodId").asInt()));
                }
                if (jsonNode.get("userId") != null) {
                    System.out.println(jsonNode.get("userId").asInt());
                    event.setOrganiser(UserRepository.getUserById(jsonNode.get("userId").asInt()));
                }
                EventRepository.saveEvent(event);
                ctx.res.setStatus(200);
            } catch (JsonProcessingException | InvalidEmailException | ParseException ex) {
                System.err.println("[EventController] updateEvent: " + ex);
                ctx.res.sendError(400, ex.toString());
            }
        }
    }


    /**
     * Helper function to parse a event ID from the URL and retrieve it.
     * This method also performs HTTP-400 and 404 error handling.
     *
     * @param ctx          The web server context.
     * @param endpointDesc Optional description of endpoint for console error message. May be null!
     * @return The event or 'null', if access failed (query invalid or customer not found).
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
                var msg = "Failed to parse event identifier!";
                if (endpointDesc != null) System.err.println("[EventController] " + endpointDesc + ": " + msg);
                ctx.res.sendError(400, msg);
            }
        } catch (IOException ex) {
            System.err.println("[EventController] fetchEvent: IOException: " + ex);
        }
        return null;
    }
}
