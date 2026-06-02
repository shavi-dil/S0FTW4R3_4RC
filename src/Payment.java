import java.time.LocalDateTime;

public class Payment {
    private final int id;
    private final double amount;
    private String status;
    private final LocalDateTime transactionDate;

    public Payment(int id, double amount, String status, LocalDateTime transactionDate) {
        this.id = id;
        this.amount = amount;
        this.status = status;
        this.transactionDate = transactionDate;
    }

    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }
}
