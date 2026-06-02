import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Invoice {
    private final int id;
    private final Order order;
    private final LocalDateTime generatedDate;
    private final String invoiceNumber;

    public Invoice(int id, Order order) {
        this.id = id;
        this.order = order;
        this.generatedDate = LocalDateTime.now();
        this.invoiceNumber = "INV-" + order.getId() + "-" + System.currentTimeMillis();
    }

    public String generateInvoiceText() {
        StringBuilder sb = new StringBuilder();
        sb.append("========== INK AND LANTERN BOOKS ==========\n");
        sb.append("Invoice: ").append(invoiceNumber).append("\n");
        sb.append("Date: ").append(generatedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).append("\n");
        sb.append("Customer: ").append(order.getCustomer().getFullName()).append("\n");
        sb.append("Email: ").append(order.getCustomer().getEmail()).append("\n");
        sb.append("Delivery: ").append(order.getDeliveryAddress()).append("\n");
        sb.append("------------------------------------------\n");
        for (OrderItem item : order.getItems()) {
            sb.append(String.format("%s x%d = $%.2f\n", item.getBook().getTitle(), item.getQuantity(), item.getSubtotal()));
        }
        sb.append("------------------------------------------\n");
        sb.append(String.format("Total: $%.2f\n", order.getTotalAmount()));
        sb.append("Status: ").append(order.getStatus()).append("\n");
        sb.append("Thank you for shopping with us!\n");
        return sb.toString();
    }
}
