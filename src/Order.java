import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a confirmed customer order.
 */
public class Order {
    private static int nextOrderNumber = 1001;

    private final String orderNumber;
    private final Customer customer;
    private final List<CartItem> items;
    private final LocalDateTime createdAt;
    private String status;
    private String deliveryAddress;
    private final Shipment shipment;
    private final double subtotal;
    private final double tax;
    private final double shipping;
    private final double grandTotal;

    public Order(Customer customer, Cart cart, String deliveryAddress) {
        this.orderNumber = "ORD-" + nextOrderNumber++;
        this.customer = customer;
        this.items = new ArrayList<>();
        for (CartItem item : cart.getItems()) {
            this.items.add(new CartItem(item.getBook(), item.getQuantity()));
            item.getBook().reduceStock(item.getQuantity());
        }
        this.createdAt = LocalDateTime.now();
        this.status = "Payment Processed";
        this.deliveryAddress = deliveryAddress;
        this.shipment = new Shipment();
        this.subtotal = cart.getSubtotal();
        this.tax = cart.getTax();
        this.shipping = cart.getShipping();
        this.grandTotal = cart.getGrandTotal();
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getTax() {
        return tax;
    }

    public double getShipping() {
        return shipping;
    }

    public double getGrandTotal() {
        return grandTotal;
    }

    public String getCreatedAtText() {
        return createdAt.format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"));
    }
}
