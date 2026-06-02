import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private final int id;
    private final Customer customer;
    private final List<OrderItem> items;
    private final LocalDateTime orderDate;
    private final String deliveryAddress;
    private String status;
    private Payment payment;
    private Shipment shipment;

    public Order(int id, Customer customer, List<OrderItem> items, String deliveryAddress) {
        this.id = id;
        this.customer = customer;
        this.items = new ArrayList<>(items);
        this.deliveryAddress = deliveryAddress;
        this.status = "PENDING";
        this.orderDate = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<OrderItem> getItems() {
        return new ArrayList<>(items);
    }

    public double getTotalAmount() {
        double total = 0;
        for (OrderItem item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }
}
