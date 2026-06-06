/**
 * Represents one line item in the shopping cart.
 */
public class CartItem {
    private final Book book;
    private int quantity;

    public CartItem(Book book, int quantity) {
        this.book = book;
        this.quantity = quantity;
    }

    public Book getBook() { return book; }
    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        if (quantity > book.getStock()) {
            throw new IllegalArgumentException("Quantity cannot exceed available stock.");
        }
        this.quantity = quantity;
    }

    public double getLineTotal() {
        return book.getPrice() * quantity;
    }
}
