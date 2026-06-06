import java.text.DecimalFormat;

/**
 * Generates an invoice/receipt summary for an order.
 */
public class Invoice {
    private final Order order;

    public Invoice(Order order) {
        this.order = order;
    }

    public String buildReceiptText() {
        DecimalFormat df = new DecimalFormat("0.00");
        StringBuilder sb = new StringBuilder();
        sb.append("Ink and Lantern Books - Invoice and Receipt\n");
        sb.append("----------------------------------------------------\n");
        sb.append("Invoice No: INV-").append(order.getOrderNumber()).append("\n");
        sb.append("Order No: ").append(order.getOrderNumber()).append("\n");
        sb.append("Date: ").append(order.getCreatedAtText()).append("\n");
        sb.append("Customer: ").append(order.getCustomer().getName()).append("\n");
        sb.append("Email: ").append(order.getCustomer().getEmail()).append("\n");
        sb.append("Delivery Address: ").append(order.getDeliveryAddress()).append("\n");
        sb.append("Tracking ID: ").append(order.getShipment().getTrackingId()).append("\n\n");
        sb.append("Items:\n");
        for (CartItem item : order.getItems()) {
            sb.append("- ").append(item.getBook().getTitle())
                    .append(" x ").append(item.getQuantity())
                    .append(" = $").append(df.format(item.getLineTotal())).append("\n");
        }
        sb.append("\nSubtotal: $").append(df.format(order.getSubtotal()));
        sb.append("\nGST 10%: $").append(df.format(order.getTax()));
        sb.append("\nShipping: $").append(df.format(order.getShipping()));
        sb.append("\nGrand Total: $").append(df.format(order.getGrandTotal()));
        sb.append("\nPayment Status: Processed successfully (simulation only)");
        sb.append("\nOrder Status: ").append(order.getStatus());
        sb.append("\nShipment Status: ").append(order.getShipment().getStatus());
        return sb.toString();
    }
}
