public class OrderItem {
    private final Book book;
    private final int quantity;

    public OrderItem(Book book, int quantity) {
        this.book = book;
        this.quantity = quantity;
    }

    public Book getBook() {
        return book;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getSubtotal() {
        return book.getPrice() * quantity;
    }
}
