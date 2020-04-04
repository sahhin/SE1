package se1app.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;


/** An order contains a set of items. */
@Entity
@Table(name="orders")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int _id;

  @ManyToOne
  @JoinColumn(name="customer_id", nullable=false)
  private Customer _customer;

  @ElementCollection
  @Column(name="items")
  private List<String> _items; //To do: Model items of order as separate types!



  /** Create a new order which is empty. */
  public Order() {
    _items = new ArrayList<String>();
  }


  /** Create an order with an existing set of items.
   * @param customer Reference to the customer that did this order.
   * @param items List of items of this order. */
  public Order(Customer customer, List<String> items) {
    _customer = customer;
    _items = items;
  }


  /** Add an item to this order.
   * @param item The item to add. */
  public void addItem(String item) {
    _items.add(item);
  }


  /** Get the order identifier.
   * @return The order ID. */
  public int getId() {
    return _id;
  }


  /** Get all items of this order.
   * @return List of items. */
  public List<String> getItems() {
    return _items;
  }


  /** Get the customer that did this order.
   * @return The customer. */
  public Customer getCustomer() {
    return _customer;
  }
}
