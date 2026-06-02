import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class BookstoreStorage {
    private final List<Category> categories = new ArrayList<>();
    private final List<Book> books = new ArrayList<>();
    private final List<User> users = new ArrayList<>();
    private final List<Order> orders = new ArrayList<>();

    private int nextBookId = 1;
    private int nextUserId = 1;
    private int nextOrderId = 1;
    private int nextPaymentId = 1;
    private int nextShipmentId = 1;
    private int nextInvoiceId = 1;

    public BookstoreStorage() {
        initializeSampleData();
    }

    public List<Category> getCategories() {
        return Collections.unmodifiableList(categories);
    }

    public List<Book> getBooks() {
        return Collections.unmodifiableList(books);
    }

    public List<User> getUsers() {
        return Collections.unmodifiableList(users);
    }

    public List<Order> getOrders() {
        return Collections.unmodifiableList(orders);
    }

    public Optional<Book> findBookById(int id) {
        return books.stream().filter(book -> book.getId() == id).findFirst();
    }

    public Optional<User> findUserByUsername(String username) {
        return users.stream().filter(user -> user.getUsername().equalsIgnoreCase(username)).findFirst();
    }

    public Optional<Order> findOrderById(int id) {
        return orders.stream().filter(order -> order.getId() == id).findFirst();
    }

    public Book addBook(String title, String author, double price, int stock, Category category, String description) {
        Book book = new Book(nextBookId++, title, author, price, stock, category, description);
        books.add(book);
        return book;
    }

    public void removeBook(Book book) {
        books.remove(book);
    }

    public User addCustomer(String username, String password, String email, String fullName, String address) {
        Customer customer = new Customer(nextUserId++, username, password, email, fullName, address);
        users.add(customer);
        return customer;
    }

    public User addAdmin(String username, String password, String email, String fullName, String address) {
        Admin admin = new Admin(nextUserId++, username, password, email, fullName, address);
        users.add(admin);
        return admin;
    }

    public Order addOrder(Order order) {
        orders.add(order);
        return order;
    }

    public int nextOrderId() {
        return nextOrderId++;
    }

    public int nextPaymentId() {
        return nextPaymentId++;
    }

    public int nextShipmentId() {
        return nextShipmentId++;
    }

    public int nextInvoiceId() {
        return nextInvoiceId++;
    }

    private void initializeSampleData() {
        categories.add(new Category(1, "Fiction"));
        categories.add(new Category(2, "Non-Fiction"));
        categories.add(new Category(3, "Science"));
        categories.add(new Category(4, "Self-Help"));

        books.add(new Book(nextBookId++, "Harry Potter", "J.K. Rowling", 25.99, 10, categories.get(0), "A magical adventure of a young wizard."));
        books.add(new Book(nextBookId++, "Atomic Habits", "James Clear", 22.50, 8, categories.get(3), "Transform your life through tiny habits."));
        books.add(new Book(nextBookId++, "The Alchemist", "Paulo Coelho", 18.00, 12, categories.get(0), "A philosophical novel about personal journey."));
        books.add(new Book(nextBookId++, "Clean Code", "Robert Martin", 45.00, 5, categories.get(2), "A guide to writing better code."));
        books.add(new Book(nextBookId++, "AI Basics", "Tom Smith", 30.00, 6, categories.get(2), "Introduction to artificial intelligence."));
        books.add(new Book(nextBookId++, "Sapiens", "Yuval Noah Harari", 28.00, 9, categories.get(1), "A brief history of humankind."));
        books.add(new Book(nextBookId++, "Thinking, Fast and Slow", "Daniel Kahneman", 35.00, 7, categories.get(1), "Psychology of human behavior."));

        addAdmin("admin", "admin123", "admin@bookstore.com", "Admin User", "123 Admin Street");
        addCustomer("john_doe", "password123", "john@email.com", "John Doe", "456 Main Street");
    }
}
