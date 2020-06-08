package se1app.facade;

import java.io.IOException;
import java.util.ArrayList;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import se1app.datatypes.AdressType;
import se1app.datatypes.EmailType;
import se1app.entities.Customer;
import se1app.exceptions.InvalidEmailException;
import se1app.repositories.CustomerRepository;
import se1app.usecases.OrderUseCase;


/** REST controller for the customer endpoint. */
public class CustomerController {


  /** Create a new customer controller.
   * @param server Javalin server handle to register REST endpoints. */
  public CustomerController(Javalin server) {
    server.get("/api/customers", CustomerController::getAllCustomers);
    server.get("/api/customers/:customer-id", CustomerController::getCustomerById);
    server.delete("/api/customers/:customer-id", CustomerController::deleteCustomerById);
    server.post("/api/customers", CustomerController::createCustomer);
    server.put("/api/customers/:customer-id", CustomerController::updateCustomer);
    server.post("/api/customers/:customer-id/orders", CustomerController::placeOrderForCustomer);
  }


  /** Get all customers.
   * @param ctx HTTP context (request/response handle). */
  private static void getAllCustomers(Context ctx) {
    var customerList = CustomerRepository.getAllCustomers();
    ctx.json(customerList);
  }


  /** Get a specific customer by URL parameter ID.
   * @param ctx HTTP context (request/response handle). */
  private static void getCustomerById(Context ctx) {
    var customer = fetchCustomer(ctx, "getCustomerById");
    if (customer != null) ctx.json(customer);
  }


  /** Delete a customer by URL parameter ID.
   * @param ctx HTTP context (request/response handle). */
  private static void deleteCustomerById(Context ctx) {
    var customer = fetchCustomer(ctx, "deleteCustomerById");
    if (customer != null) {
      CustomerRepository.deleteCustomerById(customer.getId());
      ctx.res.setStatus(200);
    }
  }


  /** Create a new customer with the data specified in request body.
   * @param ctx HTTP context (request/response handle). */
  private static void createCustomer(Context ctx) throws IOException {
    try {
      var jsonNode = new ObjectMapper().readTree(ctx.body());
      var savedCustomer = CustomerRepository.createCustomer(
        jsonNode.get("firstName").asText(),
        jsonNode.get("lastName").asText(),
        jsonNode.get("emailAddress").get("email").asText(),
        jsonNode.get("address").get("custAdress").asText()
      );
      if (savedCustomer != null) ctx.res.setStatus(201); // 201 - Created (POST success)
      else ctx.res.setStatus(500);                       // 500 - Internal Server Error
    }
    catch (JsonProcessingException ex) {
      var msg = "JSON parser exception: "+ex;
      System.err.println("[CustomerController] createCustomer: "+msg);
      ctx.res.sendError(400, msg);
    }
  }


  /** Update the data of a customer specified by URL parameter.
   * @param ctx HTTP context (request/response handle). */
  private static void updateCustomer(Context ctx) throws IOException {
    var customer = fetchCustomer(ctx, "updateCustomer");
    if (customer != null) {
      try {
        var jsonNode = new ObjectMapper().readTree(ctx.body());
        if (jsonNode.get("firstName") != null) {
          customer.setFirstName(jsonNode.get("firstName").asText());
        }
        if (jsonNode.get("lastName") != null) {
          customer.setLastName(jsonNode.get("lastName").asText());
        }
        if (jsonNode.get("emailAddress") != null) {
          var emailStr = jsonNode.get("emailAddress").get("email").asText();
          var newEmailAddress = new EmailType(emailStr);
          customer.setEmailAddress(newEmailAddress);
        }
        if (jsonNode.get("address") != null) {
          var adressStr = jsonNode.get("address").get("custAdress").asText();
          var newAddress = new AdressType(adressStr);
          customer.setAddress(newAddress);
        }
        CustomerRepository.saveCustomer(customer);
        ctx.res.setStatus(200);
      }
      catch (JsonProcessingException | InvalidEmailException ex) {
        System.err.println("[CustomerController] updateCustomer: "+ex);
        ctx.res.sendError(400, ex.toString());
      }
    }
  }


  /** Place a new order for a customer.
   * @param ctx HTTP context (request/response handle). */
  private static void placeOrderForCustomer(Context ctx) throws IOException {
    var customer = fetchCustomer(ctx, "placeOrderForCustomer");
    if (customer != null) {
      try {
        var jsonNode = new ObjectMapper().readTree(ctx.body());
        var items = new ArrayList<String>();
        for (var item : jsonNode.get("items")) items.add(item.asText());
        OrderUseCase.orderItems(customer.getId(), items);
        ctx.res.setStatus(200);
      }
      catch (JsonProcessingException ex) {
        System.err.println("[CustomerController] placeOrderForCustomer: "+ex);
        ctx.res.sendError(400, ex.toString());
      }
    }
  }


  /** Helper function to parse a customer ID from the URL and retrieve it.
   *  This method also performs HTTP-400 and 404 error handling.
   * @param ctx The web server context.
   * @param endpointDesc Optional description of endpoint for console error message. May be null!
   * @return The customer or 'null', if access failed (query invalid or customer not found). */
  private static Customer fetchCustomer(Context ctx, String endpointDesc) {
    try {
      try {
        var customerId = Integer.parseInt(ctx.pathParam("customer-id"));
        var customer = CustomerRepository.getCustomerById(customerId);
        if (customer != null) return customer;
        else {
          var msg = "Customer #"+customerId+" not found!";
          if (endpointDesc != null) System.err.println("[CustomerController] "+endpointDesc+": "+msg);
          ctx.res.sendError(404, msg);
        }
      }
      catch (NumberFormatException ex) {
        var msg = "Failed to parse customer identifier!";
        if (endpointDesc != null) System.err.println("[CustomerController] "+endpointDesc+": "+msg);
        ctx.res.sendError(400, msg);
      }
    }
    catch (IOException ex) {
      System.err.println("[CustomerController] fetchCustomer: IOException: "+ex);
    }
    return null;
  }
}
