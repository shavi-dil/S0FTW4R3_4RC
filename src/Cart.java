import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Handles selected books before checkout.
 */
public class Cart {
    private final List<CartItem> items = new ArrayList<>();

    public void addBook(Book book, int quantity) {
        if (book == null) {
            throw new IllegalArgumentException("Book is required.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        if (quantity > book.getStock()) {
            throw new IllegalArgumentException("Only " + book.getStock() + " copies are available.");
        }

        for (CartItem item : items) {
            if (item.getBook().getId() == book.getId()) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        items.add(new CartItem(book, quantity));
    }

    public void updateQuantity(Book book, int quantity) {
        for (CartItem item : items) {
            if (item.getBook().getId() == book.getId()) {
                item.setQuantity(quantity);
                return;
            }
        }
    }

    public void removeBook(Book book) {
        Iterator<CartItem> iterator = items.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getBook().getId() == book.getId()) {
                iterator.remove();
                return;
            }
        }
    }

    public void clear() {
        items.clear();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public List<CartItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public double getSubtotal() {
        double total = 0;
        for (CartItem item : items) {
            total += item.getLineTotal();
        }
        return total;
    }

    public double getTax() {
        return getSubtotal() * 0.10;
    }

    public double getShipping() {
        return isEmpty() ? 0 : 7.99;
    }

    public double getGrandTotal() {
        return getSubtotal() + getTax() + getShipping();
    }
}
