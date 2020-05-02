package se1app.usecases;

import java.util.Arrays;
import java.util.List;
import se1app.entities.Order;
import se1app.repositories.CustomerRepository;


/** Use case class to place customer orders. */
public class OrderUseCase {


  /** Place an order with multiple items.
   * @param customerId Identifier of the customer.
   * @param items A list of items to order.
   * @return 'True' if the order was successfully placed. 'False' otherwise. */
  public static boolean orderItems(int customerId, List<String> items) {
    var customer = CustomerRepository.getCustomerById(customerId);
    if (customer != null && items != null && items.size() > 0) {
      var order = new Order(customer, items);
      customer.getOrders().add(order);
      CustomerRepository.saveCustomer(customer);
      return true;
    }
    return false;
  }


  /** Place an order for a single item.
   * @param customerId Identifier of the customer.
   * @param item The item to order.
   * @return 'True' if the order was successfully placed. 'False' otherwise. */
  public static boolean orderItem(int customerId, String item) {
    return orderItems(customerId, Arrays.asList(item));
  }
}
