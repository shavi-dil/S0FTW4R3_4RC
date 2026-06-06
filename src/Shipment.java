/**
 * Represents shipment details for an order.
 */
public class Shipment {
    private String trackingId;
    private String status;

    public Shipment() {
        this.trackingId = "TRK-" + System.currentTimeMillis();
        this.status = "Preparing";
    }

    public String getTrackingId() { return trackingId; }
    public String getStatus() { return status; }

    public void setStatus(String status) {
        this.status = status;
    }
}
