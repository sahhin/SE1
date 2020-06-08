package se1app.facade;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import se1app.entities.Neighborhood;
import se1app.entities.User;
import se1app.exceptions.InvalidEmailException;
import se1app.repositories.NeighborhoodRepository;

import java.io.IOException;
import java.util.Date;


/**
 * REST controller for the customer endpoint.
 */
public class NeighborhoodController {


    /**
     * Create a new customer controller.
     *
     * @param server Javalin server handle to register REST endpoints.
     */
    public NeighborhoodController(Javalin server) {
        server.get("/api/neighborhood", NeighborhoodController::getAllNeighborhood);
        server.get("/api/neighborhood/:neighborhood-id", NeighborhoodController::getNeighborhoodById);
        server.delete("/api/neighborhood/:neighborhood-id", NeighborhoodController::deleteNeighborhoodById);
        server.post("/api/neighborhood", NeighborhoodController::createNeighborhood);
        server.put("/api/neighborhood/:neighborhood-id", NeighborhoodController::updateNeighborhood);
    }


    /**
     * Get all customers.
     *
     * @param ctx HTTP context (request/response handle).
     */
    private static void getAllNeighborhood(Context ctx) {
        var neighborhoodList = NeighborhoodRepository.getAllNeighborhoods();
        ctx.json(neighborhoodList);
    }


    /**
     * Get a specific customer by URL parameter ID.
     *
     * @param ctx HTTP context (request/response handle).
     */
    private static void getNeighborhoodById(Context ctx) {
        var neighborhood = fetchNeighborhood(ctx, "getNeighborhoodById");
        if (neighborhood != null) ctx.json(neighborhood);
    }


    /**
     * Delete a customer by URL parameter ID.
     *
     * @param ctx HTTP context (request/response handle).
     */
    private static void deleteNeighborhoodById(Context ctx) {
        var neighborhood = fetchNeighborhood(ctx, "deleteNeighborhoodById");
        if (neighborhood != null) {
            NeighborhoodRepository.deleteNeighborhoodById(neighborhood.getNeighborhoodId());
            ctx.res.setStatus(200);
        }
    }

    /**
     * Create a new customer with the data specified in request body.
     *
     * @param ctx HTTP context (request/response handle).
     */
    private static void createNeighborhood(Context ctx) throws IOException {
        try {
            var jsonNode = new ObjectMapper().readTree(ctx.body());
            Neighborhood neighborhood = new Neighborhood("Altona", 22769, "Hamburg", "Deutschland");
            User user = new User(new Date(80, 1, 1), "Test", "Hallo", "test@test.de", "Teststr.", neighborhood);
            var savedNeighborhood = NeighborhoodRepository.createNeighborhood(
                    jsonNode.get("neighborhoodName").asText(),
                    jsonNode.get("neighborhoodPostalcode").asInt(),
                    jsonNode.get("neighborhoodCity").asText(),
                    jsonNode.get("neighborhoodCountry").asText()

            );
            if (savedNeighborhood != null) ctx.res.setStatus(201); // 201 - Created (POST success)
            else ctx.res.setStatus(500);                       // 500 - Internal Server Error
        } catch (JsonProcessingException ex) {
            var msg = "JSON parser exception: " + ex;
            System.err.println("[NeighborhoodController] createNeighborhood: " + msg);
            ctx.res.sendError(400, msg);
        }
    }


    /**
     * Update the data of a customer specified by URL parameter.
     *
     * @param ctx HTTP context (request/response handle).
     */
    private static void updateNeighborhood(Context ctx) throws IOException {
        var neighborhood = fetchNeighborhood(ctx, "updateNeighborhood");
        if (neighborhood != null) {
            try {
                var jsonNode = new ObjectMapper().readTree(ctx.body());
                if (jsonNode.get("neighborhoodName") != null) {
                    neighborhood.setNeighborhoodName(jsonNode.get("neighborhoodName").asText());
                }
                if (jsonNode.get("neighborhoodPostalcode") != null) {
                    neighborhood.setNeighborhoodPostalcode(jsonNode.get("neighborhoodPostalcode").asInt());
                }
                if (jsonNode.get("neighborhoodCity") != null) {
                    neighborhood.setNeighborhoodCity(jsonNode.get("neighborhoodCity").asText());
                }
                if (jsonNode.get("neighborhoodCountry") != null) {
                    neighborhood.setNeighborhoodCountry(jsonNode.get("neighborhoodCountry").asText());
                }

                NeighborhoodRepository.saveNeighborhood(neighborhood);
                ctx.res.setStatus(200);
            } catch (JsonProcessingException | InvalidEmailException ex) {
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
    private static Neighborhood fetchNeighborhood(Context ctx, String endpointDesc) {
        try {
            try {
                var neighborhoodId = Integer.parseInt(ctx.pathParam("neighborhood-id"));
                var neighborhood = NeighborhoodRepository.getNeighborhoodById(neighborhoodId);
                if (neighborhood != null) return neighborhood;
                else {
                    var msg = "Neighborhood #" + neighborhoodId + " not found!";
                    if (endpointDesc != null) System.err.println("[NeighborhoodController] " + endpointDesc + ": " + msg);
                    ctx.res.sendError(404, msg);
                }
            } catch (NumberFormatException ex) {
                var msg = "Failed to parse user identifier!";
                if (endpointDesc != null) System.err.println("[NeighborhoodController] " + endpointDesc + ": " + msg);
                ctx.res.sendError(400, msg);
            }
        } catch (IOException ex) {
            System.err.println("[NeighborhoodController] fetchNeighborhood: IOException: " + ex);
        }
        return null;
    }
}
