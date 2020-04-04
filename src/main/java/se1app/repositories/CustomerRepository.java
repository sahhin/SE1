package se1app.repositories;

import java.util.List;
import se1app.entities.Customer;
import se1app.exceptions.InvalidEmailException;
import se1app.persistency.H2Database;


/** Repository class to load and save customers from/to the database. */
public class CustomerRepository {


  /** Create a new customer and save them in the database.
   * @param firstName The customers first name.
   * @param lastName The customers last name.
   * @param emailAddress The e-mail address.
   * @return The customer object or 'null' if creation failed. */
  public static Customer createCustomer(String firstName, String lastName, String emailAddress) {
    try {
      var customer = new Customer(firstName, lastName, emailAddress);
      saveCustomer(customer);
      return customer;
    }
    catch (InvalidEmailException ex) {
      System.err.println("Customer creation failed: \n -> "+ex);
      return null;
    }
  }


  /** Save (=update) an existing customer to the database.
   * @param customer The customer to update. */
  public static void saveCustomer(Customer customer) {
    var session = H2Database.getInstance().getSession();
    var transaction = session.beginTransaction();
    session.save(customer);
    transaction.commit();
  }


  /** Get all customers from the database.
   * @return A list with customers. May be empty if the database contains no customers. */
  public static List<Customer> getAllCustomers() {
    var session = H2Database.getInstance().getSession();
    var customers = session.createQuery("FROM Customer", Customer.class).getResultList();
    return customers;
  }


  /** Get a customer by its identifier.
   * @param customerId Customer identifier.
   * @return The customer or 'null' if not found. */
  public static Customer getCustomerById(int customerId) {
    var session = H2Database.getInstance().getSession();
    var customer = session.get(Customer.class, customerId);
    return customer;
  }


  /** Print a table of all customers to the console. This
   * method is not really a repository function, it's more
   * intended to be a helper during debugging and development. */
  public static void printCustomerTable() {
    var customers = getAllCustomers();
    System.out.println("Customers in database");
    System.out.println("---------------------------------------");
    for (var customer : customers) {
      System.out.println(customer);
    }
    System.out.println("---------------------------------------");
  }
}
