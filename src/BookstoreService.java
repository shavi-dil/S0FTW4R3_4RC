import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookstoreService {
    private final BookstoreStorage storage;

    public BookstoreService(BookstoreStorage storage) {
        this.storage = storage;
    }

    public User authenticate(String username, String password) {
        if (username == null || password == null || username.isBlank() || password.isBlank()) {
            return null;
        }
        return storage.findUserByUsername(username)
                .filter(user -> user.getPassword().equals(password))
                .orElse(null);
    }

    public Customer registerCustomer(String username, String password, String email, String fullName, String address) {
        if (username == null || username.isBlank() || password == null || password.isBlank()
                || email == null || email.isBlank() || fullName == null || fullName.isBlank()
                || address == null || address.isBlank()) {
            throw new IllegalArgumentException("All registration fields are required.");
        }
        if (storage.findUserByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists.");
        }
        return (Customer) storage.addCustomer(username, password, email, fullName, address);
    }

    public List<Book> searchBooks(String query) {
        String normalized = query == null ? "" : query.trim().toLowerCase();
        return storage.getBooks().stream()
                .filter(book -> normalized.isEmpty()
                        || book.getTitle().toLowerCase().contains(normalized)
                        || book.getAuthor().toLowerCase().contains(normalized)
                        || Integer.toString(book.getId()).contains(normalized)
                        || book.getCategory().getName().toLowerCase().contains(normalized))
                .collect(Collectors.toList());
    }

    public Book findBook(int bookId) {
        return storage.findBookById(bookId).orElse(null);
    }

    public void addBookToCart(Cart cart, int bookId, int quantity) {
        if (cart == null) {
            throw new IllegalArgumentException("Cart cannot be null.");
        }
        Book book = findBook(bookId);
        if (book == null) {
            throw new IllegalArgumentException("Book not found.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        if (book.getStock() < quantity) {
            throw new IllegalArgumentException("Not enough stock available.");
        }
        cart.addItem(book, quantity);
    }

    public Order checkout(Cart cart, Customer customer, String deliveryAddress) {
        if (cart == null || cart.isEmpty()) {
            throw new IllegalStateException("Cart is empty.");
        }
        if (customer == null) {
            throw new IllegalArgumentException("A valid customer must checkout.");
        }
        if (deliveryAddress == null || deliveryAddress.isBlank()) {
            throw new IllegalArgumentException("Delivery address cannot be blank.");
        }
        List<OrderItem> items = new ArrayList<>();
        for (CartItem cartItem : cart.getItems()) {
            Book bookInStock = findBook(cartItem.getBook().getId());
            if (bookInStock == null) {
                throw new IllegalStateException("A book in the cart is no longer available.");
            }
            if (bookInStock.getStock() < cartItem.getQuantity()) {
                throw new IllegalStateException("Insufficient stock for " + bookInStock.getTitle() + ".");
            }
            items.add(new OrderItem(bookInStock, cartItem.getQuantity()));
        }

        Order order = new Order(storage.nextOrderId(), customer, items, deliveryAddress);
        order.setPayment(new Payment(storage.nextPaymentId(), order.getTotalAmount(), "COMPLETED", LocalDateTime.now()));
        order.setShipment(new Shipment(storage.nextShipmentId(), order));
        order.setStatus("CONFIRMED");

        for (OrderItem item : items) {
            item.getBook().reduceStock(item.getQuantity());
        }

        storage.addOrder(order);
        cart.clear();
        return order;
    }

    public List<Order> getOrdersForCustomer(Customer customer) {
        return storage.getOrders().stream()
                .filter(order -> order.getCustomer().getId() == customer.getId())
                .collect(Collectors.toList());
    }

    public List<Order> getAllOrders() {
        return storage.getOrders();
    }

    public void addBook(String title, String author, double price, int stock, Category category, String description) {
        if (title == null || title.isBlank() || author == null || author.isBlank()
                || price <= 0 || stock < 0 || category == null) {
            throw new IllegalArgumentException("Invalid book data.");
        }
        storage.addBook(title, author, price, stock, category, description == null ? "" : description);
    }

    public void updateBook(int bookId, String title, String author, double price, int stock, Category category, String description) {
        Book book = findBook(bookId);
        if (book == null) {
            throw new IllegalArgumentException("Book not found.");
        }
        if (title != null && !title.isBlank()) {
            book.setTitle(title);
        }
        if (author != null && !author.isBlank()) {
            book.setAuthor(author);
        }
        if (price > 0) {
            book.setPrice(price);
        }
        if (stock >= 0) {
            book.setStock(stock);
        }
        if (category != null) {
            book.setCategory(category);
        }
        if (description != null) {
            book.setDescription(description);
        }
    }

    public void deleteBook(int bookId) {
        Book book = findBook(bookId);
        if (book == null) {
            throw new IllegalArgumentException("Book not found.");
        }
        storage.removeBook(book);
    }

    public List<Category> getCategories() {
        return storage.getCategories();
    }

    public Category getCategoryById(int categoryId) {
        return storage.getCategories().stream()
                .filter(category -> category.getId() == categoryId)
                .findFirst()
                .orElse(null);
    }

    public void updateOrderStatus(int orderId, String status) {
        Order order = storage.findOrderById(orderId).orElse(null);
        if (order == null) {
            throw new IllegalArgumentException("Order not found.");
        }
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("Status cannot be blank.");
        }
        order.setStatus(status);
        if (order.getShipment() != null) {
            order.getShipment().setStatus(status);
        }
    }

    public int getNextInvoiceId() {
        return storage.nextInvoiceId();
    }
}
