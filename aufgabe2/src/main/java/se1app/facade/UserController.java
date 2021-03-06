package se1app.facade;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import se1app.datatypes.AdressType;
import se1app.datatypes.EmailType;
import se1app.exceptions.InvalidEmailException;
import se1app.repositories.CustomerRepository;

import java.io.IOException;


/**
 * REST controller for the customer endpoint.
 */
public class UserController {


    /**
     * Create a new customer controller.
     *
     * @param server Javalin server handle to register REST endpoints.
     */
    public UserController(Javalin server) {
        server.get("/api/users", UserController::getAllUsers);
        server.get("/api/users/:user-id", UserController::getUserById);
        server.delete("/api/users/:user-id", UserController::deleteUserById);
        server.post("/api/users", UserController::createUser);
        server.put("/api/users/:user-id", UserController::updateUser);
    }


    /**
     * Get all customers.
     *
     * @param ctx HTTP context (request/response handle).
     */
    private static void getAllUsers(Context ctx) {
        var userList = UserRepository.getAllUsers();
        ctx.json(userList);
    }


    /**
     * Get a specific customer by URL parameter ID.
     *
     * @param ctx HTTP context (request/response handle).
     */
    private static void getUserById(Context ctx) {
        var user = fetchUser(ctx, "getUserById");
        if (user != null) ctx.json(user);
    }


    /**
     * Delete a customer by URL parameter ID.
     *
     * @param ctx HTTP context (request/response handle).
     */
    private static void deleteUserById(Context ctx) {
        var user = fetchUser(ctx, "deleteCustomerById");
        if (user != null) {
            UserRepository.deleteUserById(user.getId());
            ctx.res.setStatus(200);
        }
    }

    /**
     * Create a new customer with the data specified in request body.
     *
     * @param ctx HTTP context (request/response handle).
     */
    private static void createUser(Context ctx) throws IOException {
        try {
            var jsonNode = new ObjectMapper().readTree(ctx.body());
            var savedUser = UserRepository.createUser(
                    jsonNode.get("userBirthday").asText(),
                    jsonNode.get("firstName").asText(),
                    jsonNode.get("lastName").asText(),
                    jsonNode.get("emailAddress").get("userEmail").asText(),
                    jsonNode.get("address").get("userAdress").asText()
            );
            if (savedUser != null) ctx.res.setStatus(201); // 201 - Created (POST success)
            else ctx.res.setStatus(500);                       // 500 - Internal Server Error
        } catch (JsonProcessingException ex) {
            var msg = "JSON parser exception: " + ex;
            System.err.println("[UserController] createUser: " + msg);
            ctx.res.sendError(400, msg);
        }
    }


    /**
     * Update the data of a customer specified by URL parameter.
     *
     * @param ctx HTTP context (request/response handle).
     */
    private static void updateUser(Context ctx) throws IOException {
        var user = fetchUser(ctx, "updateUser");
        if (user != null) {
            try {
                var jsonNode = new ObjectMapper().readTree(ctx.body());
                if (jsonNode.get("userBirthday") != null) {
                    user.setBirthday(jsonNode.get("userBirthday").asText());
                }
                if (jsonNode.get("firstName") != null) {
                    user.setFirstName(jsonNode.get("firstName").asText());
                }
                if (jsonNode.get("lastName") != null) {
                    user.setLastName(jsonNode.get("lastName").asText());
                }
                if (jsonNode.get("emailAddress") != null) {
                    var emailStr = jsonNode.get("emailAddress").get("userEmail").asText();
                    var newEmailAddress = new EmailType(emailStr);
                    user.setEmailAddress(newEmailAddress);
                }
                if (jsonNode.get("address") != null) {
                    var adressStr = jsonNode.get("address").get("userAdress").asText();
                    var newAddress = new AdressType(adressStr);
                    user.setAddress(newAddress);
                }
                UserRepository.saveUser(user);
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
    private static User fetchUser(Context ctx, String endpointDesc) {
        try {
            try {
                var userId = Integer.parseInt(ctx.pathParam("user-id"));
                var user = UserRepository.getUserById(userId);
                if (user != null) return user;
                else {
                    var msg = "User #" + userId + " not found!";
                    if (endpointDesc != null) System.err.println("[UserController] " + endpointDesc + ": " + msg);
                    ctx.res.sendError(404, msg);
                }
            } catch (NumberFormatException ex) {
                var msg = "Failed to parse user identifier!";
                if (endpointDesc != null) System.err.println("[UserController] " + endpointDesc + ": " + msg);
                ctx.res.sendError(400, msg);
            }
        } catch (IOException ex) {
            System.err.println("[UserController] fetchUser: IOException: " + ex);
        }
        return null;
    }
}
