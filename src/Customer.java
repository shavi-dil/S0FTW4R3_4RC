import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a bookstore customer account.
 */
public class Customer extends User {
    private String phone;
    private String address;
    private final List<Order> orders = new ArrayList<>();

    public Customer(String name, String email, String password, String phone, String address) {
        super(name, email, password);
        this.phone = phone;
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public List<Order> getOrders() {
        return Collections.unmodifiableList(orders);
    }

    public void updateProfile(String name, String phone, String address) {
        setName(name);
        this.phone = phone;
        this.address = address;
    }

    public void addOrder(Order order) {
        orders.add(order);
    }
}
