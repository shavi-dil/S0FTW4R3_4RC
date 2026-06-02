import java.time.LocalDateTime;

public class Shipment {
    private final int id;
    private final Order order;
    private String status;
    private LocalDateTime shipmentDate;
    private final String trackingNumber;

    public Shipment(int id, Order order) {
        this.id = id;
        this.order = order;
        this.status = "PENDING";
        this.trackingNumber = "TRK-" + order.getId() + "-" + System.currentTimeMillis();
    }

    public int getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        if ("SHIPPED".equalsIgnoreCase(status) || "DELIVERED".equalsIgnoreCase(status)) {
            this.shipmentDate = LocalDateTime.now();
        }
    }

    public LocalDateTime getShipmentDate() {
        return shipmentDate;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }
}
