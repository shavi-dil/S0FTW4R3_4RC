import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

// ==================== CATEGORY CLASS ====================
class Category {
    private int id;
    private String name;

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }
}

// ==================== BOOK CLASS ====================
class Book {
    private int id;
    private String title;
    private String author;
    private double price;
    private int stock;
    private Category category;
    private String description;

    public Book(int id, String title, String author, double price, int stock, Category category, String description) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.description = description;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public Category getCategory() { return category; }
    public String getDescription() { return description; }

    public void reduceStock(int quantity) {
        stock -= quantity;
    }

    public void increaseStock(int quantity) {
        stock += quantity;
    }
}

// ==================== USER CLASS ====================
class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String address;
    private boolean isAdmin;

    public User(int id, String username, String password, String email, String fullName, String address, boolean isAdmin) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
        this.address = address;
        this.isAdmin = isAdmin;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public String getFullName() { return fullName; }
    public String getAddress() { return address; }
    public boolean isAdmin() { return isAdmin; }

    public void setEmail(String email) { this.email = email; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setAddress(String address) { this.address = address; }
}

// ==================== PAYMENT CLASS ====================
class Payment {
    private int id;
    private double amount;
    private String status; // PENDING, COMPLETED, FAILED
    private LocalDateTime transactionDate;

    public Payment(int id, double amount, String status, LocalDateTime transactionDate) {
        this.id = id;
        this.amount = amount;
        this.status = status;
        this.transactionDate = transactionDate;
    }

    public int getId() { return id; }
    public double getAmount() { return amount; }
    public String getStatus() { return status; }
    public LocalDateTime getTransactionDate() { return transactionDate; }

    public void setStatus(String status) { this.status = status; }
}

// ==================== INVOICE CLASS ====================
class Invoice {
    private int id;
    private Order order;
    private LocalDateTime generatedDate;
    private String invoiceNumber;

    public Invoice(int id, Order order, LocalDateTime generatedDate) {
        this.id = id;
        this.order = order;
        this.generatedDate = generatedDate;
        this.invoiceNumber = "INV-" + order.getId() + "-" + System.currentTimeMillis();
    }

    public int getId() { return id; }
    public Order getOrder() { return order; }
    public LocalDateTime getGeneratedDate() { return generatedDate; }
    public String getInvoiceNumber() { return invoiceNumber; }

    public String generateInvoiceText() {
        StringBuilder sb = new StringBuilder();
        sb.append("════════════════════════════════════════\n");
        sb.append("        INK AND LANTERN BOOKS\n");
        sb.append("              INVOICE\n");
        sb.append("════════════════════════════════════════\n\n");
        sb.append("Invoice #: ").append(invoiceNumber).append("\n");
        sb.append("Date: ").append(generatedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).append("\n\n");
        
        sb.append("Customer: ").append(order.getCustomer().getFullName()).append("\n");
        sb.append("Address: ").append(order.getDeliveryAddress()).append("\n");
        sb.append("Email: ").append(order.getCustomer().getEmail()).append("\n\n");

        sb.append("─────────────────────────────────────────\n");
        sb.append("ITEMS:\n");
        sb.append("─────────────────────────────────────────\n");

        for (OrderItem item : order.getItems()) {
            sb.append(String.format("%-30s %3d x $%7.2f = $%7.2f\n",
                    item.getBook().getTitle(),
                    item.getQuantity(),
                    item.getBook().getPrice(),
                    item.getSubtotal()));
        }

        sb.append("─────────────────────────────────────────\n");
        sb.append(String.format("SUBTOTAL:                           $%7.2f\n", order.getTotalAmount()));
        sb.append(String.format("TAX (10%%):                          $%7.2f\n", order.getTotalAmount() * 0.1));
        sb.append(String.format("TOTAL:                              $%7.2f\n", order.getTotalAmount() * 1.1));
        sb.append("════════════════════════════════════════\n");
        sb.append("Order Status: ").append(order.getStatus()).append("\n");
        sb.append("Thank you for your purchase!\n");

        return sb.toString();
    }
}

// ==================== SHIPMENT CLASS ====================
class Shipment {
    private int id;
    private Order order;
    private String status; // PENDING, SHIPPED, IN_TRANSIT, DELIVERED
    private LocalDateTime shipmentDate;
    private String trackingNumber;

    public Shipment(int id, Order order) {
        this.id = id;
        this.order = order;
        this.status = "PENDING";
        this.shipmentDate = null;
        this.trackingNumber = "TRK-" + order.getId() + "-" + System.currentTimeMillis();
    }

    public int getId() { return id; }
    public Order getOrder() { return order; }
    public String getStatus() { return status; }
    public LocalDateTime getShipmentDate() { return shipmentDate; }
    public String getTrackingNumber() { return trackingNumber; }

    public void setStatus(String status) { this.status = status; }
    public void setShipmentDate(LocalDateTime date) { this.shipmentDate = date; }
}

// ==================== ORDER ITEM CLASS ====================
class OrderItem {
    private Book book;
    private int quantity;

    public OrderItem(Book book, int quantity) {
        this.book = book;
        this.quantity = quantity;
    }

    public Book getBook() { return book; }
    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSubtotal() {
        return book.getPrice() * quantity;
    }
}

// ==================== ORDER CLASS ====================
class Order {
    private int id;
    private User customer;
    private ArrayList<OrderItem> items;
    private double totalAmount;
    private String status; // PENDING, CONFIRMED, PROCESSING, SHIPPED, DELIVERED, CANCELLED
    private LocalDateTime orderDate;
    private String deliveryAddress;
    private Payment payment;
    private Shipment shipment;

    public Order(int id, User customer, ArrayList<OrderItem> items, String deliveryAddress) {
        this.id = id;
        this.customer = customer;
        this.items = new ArrayList<>(items);
        this.deliveryAddress = deliveryAddress;
        this.status = "PENDING";
        this.orderDate = LocalDateTime.now();
        this.totalAmount = calculateTotal();
    }

    private double calculateTotal() {
        double total = 0;
        for (OrderItem item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    public int getId() { return id; }
    public User getCustomer() { return customer; }
    public ArrayList<OrderItem> getItems() { return items; }
    public double getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public Payment getPayment() { return payment; }
    public Shipment getShipment() { return shipment; }

    public void setStatus(String status) { this.status = status; }
    public void setPayment(Payment payment) { this.payment = payment; }
    public void setShipment(Shipment shipment) { this.shipment = shipment; }
}

// ==================== CART CLASS ====================
class Cart {
    private ArrayList<OrderItem> items = new ArrayList<>();

    public void addBook(Book book, int quantity) {
        for (OrderItem item : items) {
            if (item.getBook().getId() == book.getId()) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        items.add(new OrderItem(book, quantity));
    }

    public void removeItem(int index) {
        if (index >= 0 && index < items.size()) {
            items.remove(index);
        }
    }

    public ArrayList<OrderItem> getItems() {
        return items;
    }

    public double getTotal() {
        double total = 0;
        for (OrderItem item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    public void clearCart() {
        items.clear();
    }
}


// ==================== MAIN APPLICATION CLASS ====================
public class OnlineBookstoreApp extends JFrame {
    private ArrayList<Book> books = new ArrayList<>();
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Order> orders = new ArrayList<>();
    private ArrayList<Category> categories = new ArrayList<>();
    private Cart cart = new Cart();
    private User currentUser = null;
    private int nextUserId = 1;
    private int nextOrderId = 1;
    private int nextShipmentId = 1;
    private int nextPaymentId = 1;
    private int nextInvoiceId = 1;

    private JTable bookTable;
    private JTable cartTable;
    private DefaultTableModel bookModel;
    private DefaultTableModel cartModel;
    private CardLayout cardLayout;
    private JPanel cardPanel;

    // Color scheme
    // Color scheme (navy-inspired bookstore theme)
    private static final Color BG_COLOR = new Color(250, 247, 242);        // Warm Cream #FAF7F2 (main background)
    private static final Color SECONDARY_BG = new Color(241, 236, 230);    // Soft Beige #F1ECE6 (secondary background)
    private static final Color PRIMARY_COLOR = new Color(8, 35, 70);      // Navy #082346 (navbar / buttons)
    private static final Color SECONDARY_COLOR = new Color(42, 76, 118);   // Slate Navy #2A4C76 (secondary)
    private static final Color ACCENT_COLOR = new Color(123, 163, 212);    // Soft Sky Blue #7BA3D4 (highlights/prices)
    private static final Color CARD_COLOR = new Color(255, 255, 255);      // White #FFFFFF (cards/panels)
    private static final Color TEXT_COLOR = new Color(47, 47, 47);         // Soft Dark Grey #2F2F2F (main text)
    private static final Color SECONDARY_TEXT = new Color(122, 122, 122);  // Grey #7A7A7A (secondary text)
    private static final Color BUTTON_COLOR = PRIMARY_COLOR;               // Buttons use primary navy blue
    private static final Color BUTTON_HOVER = SECONDARY_COLOR;             // Slightly softer navy on hover
    private static final Color ERROR_COLOR = new Color(217, 124, 108);     // Muted Coral #D97C6C (errors)
    private static final Color SUCCESS_COLOR = new Color(123, 174, 127);   // Sage Green #7BAE7F (success)

    public OnlineBookstoreApp() {
        setTitle("📚 INK AND LANTERN BOOKS - Online Bookstore");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setBackground(BG_COLOR);

        UIManager.put("TabbedPane.background", BG_COLOR);
        UIManager.put("Panel.background", BG_COLOR);
        UIManager.put("Button.background", BUTTON_COLOR);
        UIManager.put("Button.foreground", Color.WHITE);

        initializeData();
        createUI();
    }

    private void initializeData() {
        // Load categories
        categories.add(new Category(1, "Fiction"));
        categories.add(new Category(2, "Non-Fiction"));
        categories.add(new Category(3, "Science"));
        categories.add(new Category(4, "Self-Help"));

        // Load sample books with categories
        books.add(new Book(1, "Harry Potter", "J.K. Rowling", 25.99, 10, categories.get(0), "A magical adventure of a young wizard"));
        books.add(new Book(2, "Atomic Habits", "James Clear", 22.50, 8, categories.get(3), "Transform your life through tiny habits"));
        books.add(new Book(3, "The Alchemist", "Paulo Coelho", 18.00, 12, categories.get(0), "A philosophical novel about personal journey"));
        books.add(new Book(4, "Clean Code", "Robert Martin", 45.00, 5, categories.get(2), "A guide to writing better code"));
        books.add(new Book(5, "AI Basics", "Tom Smith", 30.00, 6, categories.get(2), "Introduction to artificial intelligence"));
        books.add(new Book(6, "Sapiens", "Yuval Noah Harari", 28.00, 9, categories.get(1), "A brief history of humankind"));
        books.add(new Book(7, "Thinking, Fast and Slow", "Daniel Kahneman", 35.00, 7, categories.get(1), "Psychology of human behavior"));

        // Add demo users
        users.add(new User(nextUserId++, "admin", "admin123", "admin@bookstore.com", "Admin User", "123 Admin St", true));
        users.add(new User(nextUserId++, "john_doe", "password123", "john@email.com", "John Doe", "456 Main St", false));
    }

    private void createUI() {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(BG_COLOR);

        // Add all screens to card panel
        cardPanel.add(createAuthenticationPanel(), "AUTH");
        cardPanel.add(createMainApplicationPanel(), "MAIN");

        add(cardPanel);
        cardLayout.show(cardPanel, "AUTH");
    }

    // ==================== AUTHENTICATION PANEL ====================
    private JPanel createAuthenticationPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(1200, 100));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("INK AND LANTERN BOOKS");
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("A cozy library-inspired shopping experience");
        subtitleLabel.setFont(new Font("Georgia", Font.ITALIC, 14));
        subtitleLabel.setForeground(SECONDARY_TEXT);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(titleLabel);
        headerPanel.add(subtitleLabel);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Center panel with tabs
        JTabbedPane authTabs = new JTabbedPane();
        authTabs.setBackground(BG_COLOR);
        authTabs.setForeground(PRIMARY_COLOR);
        authTabs.setFont(new Font("Segoe UI", Font.BOLD, 12));

        authTabs.add("Login", createLoginPanel());
        authTabs.add("Register", createRegisterPanel());

        panel.add(authTabs, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100));

        JLabel titleLabel = new JLabel("User Login");
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(30));

        JTextField usernameField = new JTextField(30);
        panel.add(createFormField("👤 Username:", usernameField));
        panel.add(Box.createVerticalStrut(15));

        JPasswordField passwordField = new JPasswordField(30);
        panel.add(createFormField("🔒 Password:", passwordField));
        panel.add(Box.createVerticalStrut(30));

        JButton loginButton = createStyledButton("✅ Login");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            User user = authenticate(username, password);
            if (user != null) {
                currentUser = user;
                cardLayout.show(cardPanel, "MAIN");
                usernameField.setText("");
                passwordField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(loginButton);

        return panel;
    }

    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100));

        JLabel titleLabel = new JLabel("Create New Account");
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(30));

        JTextField usernameField = new JTextField(30);
        panel.add(createFormField("👤 Username:", usernameField));
        panel.add(Box.createVerticalStrut(12));

        JPasswordField passwordField = new JPasswordField(30);
        panel.add(createFormField("🔒 Password:", passwordField));
        panel.add(Box.createVerticalStrut(12));

        JTextField emailField = new JTextField(30);
        panel.add(createFormField("📧 Email:", emailField));
        panel.add(Box.createVerticalStrut(12));

        JTextField fullNameField = new JTextField(30);
        panel.add(createFormField("👥 Full Name:", fullNameField));
        panel.add(Box.createVerticalStrut(12));

        JTextField addressField = new JTextField(30);
        panel.add(createFormField("📍 Address:", addressField));
        panel.add(Box.createVerticalStrut(30));

        JButton registerButton = createStyledButton("✅ Register");
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String email = emailField.getText().trim();
            String fullName = fullNameField.getText().trim();
            String address = addressField.getText().trim();

            if (username.isEmpty() || password.isEmpty() || email.isEmpty() || fullName.isEmpty() || address.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (userExists(username)) {
                JOptionPane.showMessageDialog(this, "Username already exists.", "Registration Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            User newUser = new User(nextUserId++, username, password, email, fullName, address, false);
            users.add(newUser);
            JOptionPane.showMessageDialog(this, "Account created successfully! Please login.", "Success", JOptionPane.INFORMATION_MESSAGE);

            usernameField.setText("");
            passwordField.setText("");
            emailField.setText("");
            fullNameField.setText("");
            addressField.setText("");
        });
        panel.add(registerButton);

        return panel;
    }

    // ==================== MAIN APPLICATION PANEL ====================
    private JPanel createMainApplicationPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);

        // Header
        JPanel headerPanel = createHeaderPanel();
        panel.add(headerPanel, BorderLayout.NORTH);

        // Tabbed Pane
        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(BG_COLOR);
        tabs.setForeground(PRIMARY_COLOR);
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));

        tabs.add("👤 Account", createAccountPanel());
        tabs.add("📚 Books", createBookPanel());
        tabs.add("🛒 Cart", createCartPanel());
        tabs.add("💳 Checkout", createCheckoutPanel());
        
        if (currentUser != null && currentUser.isAdmin()) {
            tabs.add("⚙️ Admin", createAdminPanel());
        }

        panel.add(tabs, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(PRIMARY_COLOR);
        panel.setPreferredSize(new Dimension(1200, 80));
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("INK AND LANTERN BOOKS");
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);

        JLabel userLabel = new JLabel("Welcome, " + (currentUser != null ? currentUser.getFullName() : "Guest"));
        userLabel.setFont(new Font("Georgia", Font.PLAIN, 14));
        userLabel.setForeground(SECONDARY_TEXT);

        panel.add(titleLabel, BorderLayout.WEST);
        panel.add(userLabel, BorderLayout.CENTER);

        JButton logoutButton = createStyledButton("🚪 Logout");
        logoutButton.addActionListener(e -> {
            currentUser = null;
            cart.clearCart();
            cardLayout.show(cardPanel, "AUTH");
        });
        panel.add(logoutButton, BorderLayout.EAST);

        return panel;
    }

    // ==================== ACCOUNT MANAGEMENT PANEL ====================
    private JPanel createAccountPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel titleLabel = new JLabel("My Account");
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 22));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(20));

        // Account Info Card
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(CARD_COLOR);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SECONDARY_COLOR, 2, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        if (currentUser != null) {
            addAccountInfoRow(infoPanel, "Username:", currentUser.getUsername());
            addAccountInfoRow(infoPanel, "Email:", currentUser.getEmail());
            addAccountInfoRow(infoPanel, "Full Name:", currentUser.getFullName());
            addAccountInfoRow(infoPanel, "Address:", currentUser.getAddress());
            addAccountInfoRow(infoPanel, "Account Type:", currentUser.isAdmin() ? "Administrator" : "Customer");
        }

        panel.add(infoPanel);
        panel.add(Box.createVerticalStrut(20));

        // Edit Info Section
        JLabel editLabel = new JLabel("Update Information");
        editLabel.setFont(new Font("Georgia", Font.BOLD, 18));
        editLabel.setForeground(PRIMARY_COLOR);
        editLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(editLabel);
        panel.add(Box.createVerticalStrut(15));

        JPanel editPanel = new JPanel();
        editPanel.setLayout(new BoxLayout(editPanel, BoxLayout.Y_AXIS));
        editPanel.setBackground(CARD_COLOR);
        editPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SECONDARY_COLOR, 2, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JTextField emailField = new JTextField(currentUser != null ? currentUser.getEmail() : "");
        editPanel.add(createFormField("📧 Email:", emailField));
        editPanel.add(Box.createVerticalStrut(12));

        JTextField nameField = new JTextField(currentUser != null ? currentUser.getFullName() : "");
        editPanel.add(createFormField("👥 Full Name:", nameField));
        editPanel.add(Box.createVerticalStrut(12));

        JTextField addressField = new JTextField(currentUser != null ? currentUser.getAddress() : "");
        editPanel.add(createFormField("📍 Address:", addressField));
        editPanel.add(Box.createVerticalStrut(15));

        JButton updateButton = createStyledButton("💾 Save Changes");
        updateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        updateButton.addActionListener(e -> {
            if (currentUser != null) {
                currentUser.setEmail(emailField.getText().trim());
                currentUser.setFullName(nameField.getText().trim());
                currentUser.setAddress(addressField.getText().trim());
                JOptionPane.showMessageDialog(this, "Account updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        editPanel.add(updateButton);

        panel.add(editPanel);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private void addAccountInfoRow(JPanel panel, String label, String value) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row.setBackground(CARD_COLOR);
        row.setMaximumSize(new Dimension(600, 30));

        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("Segoe UI", Font.BOLD, 12));
        labelComp.setForeground(TEXT_COLOR);
        labelComp.setPreferredSize(new Dimension(120, 30));

        JLabel valueComp = new JLabel(value);
        valueComp.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        valueComp.setForeground(ACCENT_COLOR);

        row.add(labelComp);
        row.add(valueComp);
        panel.add(row);
        panel.add(Box.createVerticalStrut(8));
    }

    // ==================== BOOKS PANEL WITH CATEGORIES ====================
    private JPanel createBookPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Category Filter
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filterPanel.setBackground(SECONDARY_BG);
        filterPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel filterLabel = new JLabel("Filter by Category:");
        filterLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        filterLabel.setForeground(TEXT_COLOR);

        JComboBox<String> categoryFilter = new JComboBox<>();
        categoryFilter.addItem("All Categories");
        for (Category cat : categories) {
            categoryFilter.addItem(cat.getName());
        }
        categoryFilter.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        filterPanel.add(filterLabel);
        filterPanel.add(categoryFilter);
        panel.add(filterPanel, BorderLayout.NORTH);

        bookModel = new DefaultTableModel(new String[]{"📖 ID", "📚 Title", "✍️ Author", "💰 Price", "📦 Stock", "🏷️ Category"}, 0);
        bookTable = new JTable(bookModel);
        bookTable.setBackground(CARD_COLOR);
        bookTable.setForeground(TEXT_COLOR);
        bookTable.setSelectionBackground(SECONDARY_COLOR);
        bookTable.setSelectionForeground(TEXT_COLOR);
        bookTable.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        bookTable.setRowHeight(25);
        bookTable.getTableHeader().setBackground(PRIMARY_COLOR);
        bookTable.getTableHeader().setForeground(Color.WHITE);
        bookTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < bookTable.getColumnCount(); i++) {
            bookTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        refreshBookTable(null); // null = show all

        categoryFilter.addActionListener(e -> {
            String selected = (String) categoryFilter.getSelectedItem();
            Category selectedCategory = null;
            if (!selected.equals("All Categories")) {
                for (Category cat : categories) {
                    if (cat.getName().equals(selected)) {
                        selectedCategory = cat;
                        break;
                    }
                }
            }
            refreshBookTable(selectedCategory);
        });

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BG_COLOR);

        JButton viewDetailsButton = createStyledButton("👁️ View Details");
        viewDetailsButton.addActionListener(e -> showBookDetails());

        JButton addButton = createStyledButton("➕ Add to Cart");
        addButton.addActionListener(e -> addSelectedBookToCart());

        buttonPanel.add(viewDetailsButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(addButton);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(BG_COLOR);
        centerPanel.add(new JScrollPane(bookTable), BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);

        panel.add(centerPanel, BorderLayout.CENTER);
        return panel;
    }

    private void showBookDetails() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book first.");
            return;
        }

        Book book = books.get(selectedRow);
        
        String details = String.format(
                "📚 BOOK DETAILS\n\n" +
                "Title: %s\n" +
                "Author: %s\n" +
                "Price: $%.2f\n" +
                "Stock Available: %d\n" +
                "Category: %s\n\n" +
                "Description:\n%s",
                book.getTitle(),
                book.getAuthor(),
                book.getPrice(),
                book.getStock(),
                book.getCategory().getName(),
                book.getDescription()
        );

        JOptionPane.showMessageDialog(this, details, "Book Details", JOptionPane.INFORMATION_MESSAGE);
    }

    // ==================== CART PANEL ====================
    private JPanel createCartPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        cartModel = new DefaultTableModel(new String[]{"📚 Title", "🔢 Qty", "💵 Unit Price", "💰 Subtotal"}, 0);
        cartTable = new JTable(cartModel);
        cartTable.setBackground(CARD_COLOR);
        cartTable.setForeground(TEXT_COLOR);
        cartTable.setSelectionBackground(SECONDARY_COLOR);
        cartTable.setSelectionForeground(TEXT_COLOR);
        cartTable.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        cartTable.setRowHeight(25);
        cartTable.getTableHeader().setBackground(ACCENT_COLOR);
        cartTable.getTableHeader().setForeground(Color.WHITE);
        cartTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        for (int i = 1; i < cartTable.getColumnCount(); i++) {
            cartTable.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
        }

        refreshCartTable();

        // Info panel
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        infoPanel.setBackground(SECONDARY_COLOR);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel totalLabel = new JLabel(String.format("💰 Cart Total: $%.2f", cart.getTotal()));
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        totalLabel.setForeground(Color.WHITE);
        infoPanel.add(totalLabel);

        JButton updateButton = createStyledButton("🔄 Refresh");
        updateButton.addActionListener(e -> {
            refreshCartTable();
            totalLabel.setText(String.format("💰 Cart Total: $%.2f", cart.getTotal()));
        });
        infoPanel.add(updateButton);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BG_COLOR);

        JButton removeButton = createStyledButton("🗑️ Remove Item");
        removeButton.addActionListener(e -> removeSelectedCartItem());

        JButton clearButton = createStyledButton("🗑️ Clear Cart");
        clearButton.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Clear entire cart?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                cart.clearCart();
                refreshCartTable();
                totalLabel.setText(String.format("💰 Cart Total: $%.2f", cart.getTotal()));
            }
        });

        buttonPanel.add(removeButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(clearButton);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(BG_COLOR);
        centerPanel.add(new JScrollPane(cartTable), BorderLayout.CENTER);
        centerPanel.add(infoPanel, BorderLayout.NORTH);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);

        panel.add(centerPanel, BorderLayout.CENTER);
        return panel;
    }

    // ==================== CHECKOUT PANEL ====================
    private JPanel createCheckoutPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(BG_COLOR);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SECONDARY_COLOR, 2, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel checkoutTitle = new JLabel("Checkout");
        checkoutTitle.setFont(new Font("Georgia", Font.BOLD, 22));
        checkoutTitle.setForeground(PRIMARY_COLOR);
        checkoutTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(checkoutTitle);
        formPanel.add(Box.createVerticalStrut(15));

        JTextField nameField = new JTextField(currentUser != null ? currentUser.getFullName() : "", 25);
        formPanel.add(createFormField("👤 Customer Name:", nameField));
        formPanel.add(Box.createVerticalStrut(12));

        JTextField addressField = new JTextField(currentUser != null ? currentUser.getAddress() : "", 25);
        formPanel.add(createFormField("📍 Delivery Address:", addressField));
        formPanel.add(Box.createVerticalStrut(12));

        JTextField emailField = new JTextField(currentUser != null ? currentUser.getEmail() : "", 25);
        formPanel.add(createFormField("📧 Email:", emailField));
        formPanel.add(Box.createVerticalStrut(20));

        JLabel orderTitle = new JLabel("Order Summary");
        orderTitle.setFont(new Font("Georgia", Font.BOLD, 16));
        orderTitle.setForeground(PRIMARY_COLOR);
        orderTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(orderTitle);
        formPanel.add(Box.createVerticalStrut(10));

        JLabel totalLabel = new JLabel();
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        totalLabel.setForeground(ACCENT_COLOR);
        totalLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(totalLabel);
        formPanel.add(Box.createVerticalStrut(20));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(BG_COLOR);

        JButton updateTotalButton = createStyledButton("🔄 Update Total");
        updateTotalButton.addActionListener(e ->
                totalLabel.setText(String.format("Order Total: $%.2f", cart.getTotal()))
        );

        JButton checkoutButton = createStyledButton("✅ Place Order");
        checkoutButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String address = addressField.getText().trim();
            String email = emailField.getText().trim();

            if (name.isEmpty() || address.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (cart.getItems().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Cart is empty. Please add books first.", "Cart Empty", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Create order
            User tempUser = new User(-1, "", "", email, name, address, false);
            Order order = new Order(nextOrderId++, tempUser, (ArrayList<OrderItem>) cart.getItems().clone(), address);

            // Create payment
            Payment payment = new Payment(nextPaymentId++, order.getTotalAmount(), "COMPLETED", LocalDateTime.now());
            order.setPayment(payment);

            // Create shipment
            Shipment shipment = new Shipment(nextShipmentId++, order);
            order.setShipment(shipment);

            order.setStatus("CONFIRMED");

            // Reduce stock and add to order list
            for (OrderItem item : cart.getItems()) {
                item.getBook().reduceStock(item.getQuantity());
            }
            orders.add(order);

            // Generate invoice
            Invoice invoice = new Invoice(nextInvoiceId++, order, LocalDateTime.now());
            String invoiceText = invoice.generateInvoiceText();

            JOptionPane.showMessageDialog(this, invoiceText, "Order Confirmation", JOptionPane.INFORMATION_MESSAGE);

            cart.clearCart();
            refreshCartTable();
            refreshBookTable(null);
            totalLabel.setText("Order Total: $0.00");
            nameField.setText("");
            addressField.setText("");
            emailField.setText("");
        });

        buttonPanel.add(updateTotalButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(checkoutButton);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        formPanel.add(buttonPanel);

        mainPanel.add(formPanel, BorderLayout.NORTH);
        return mainPanel;
    }

    // ==================== ADMIN PANEL ====================
    private JPanel createAdminPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTabbedPane adminTabs = new JTabbedPane();
        adminTabs.setBackground(BG_COLOR);
        adminTabs.setForeground(PRIMARY_COLOR);

        adminTabs.add("📦 Orders", createOrderManagementPanel());
        adminTabs.add("🚚 Shipping", createShippingManagementPanel());

        panel.add(adminTabs, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createOrderManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        DefaultTableModel orderModel = new DefaultTableModel(new String[]{"Order ID", "Customer", "Total", "Status", "Date"}, 0);
        JTable orderTable = new JTable(orderModel);
        orderTable.setBackground(CARD_COLOR);
        orderTable.setForeground(TEXT_COLOR);
        orderTable.setSelectionBackground(SECONDARY_COLOR);
        orderTable.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        orderTable.setRowHeight(25);
        orderTable.getTableHeader().setBackground(PRIMARY_COLOR);
        orderTable.getTableHeader().setForeground(Color.WHITE);

        refreshOrderTable(orderModel);

        JButton viewDetailsButton = createStyledButton("👁️ View Details");
        viewDetailsButton.addActionListener(e -> {
            int selectedRow = orderTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select an order.");
                return;
            }
            Order order = orders.get(selectedRow);
            showOrderDetails(order);
        });

        JButton updateStatusButton = createStyledButton("✏️ Update Status");
        updateStatusButton.addActionListener(e -> {
            int selectedRow = orderTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select an order.");
                return;
            }
            Order order = orders.get(selectedRow);
            String[] statuses = {"PENDING", "CONFIRMED", "PROCESSING", "SHIPPED", "DELIVERED", "CANCELLED"};
            String newStatus = (String) JOptionPane.showInputDialog(this, "Select new status:", "Update Order Status", JOptionPane.QUESTION_MESSAGE, null, statuses, order.getStatus());
            if (newStatus != null) {
                order.setStatus(newStatus);
                refreshOrderTable(orderModel);
                JOptionPane.showMessageDialog(this, "Order status updated.");
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BG_COLOR);
        buttonPanel.add(viewDetailsButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(updateStatusButton);

        panel.add(new JScrollPane(orderTable), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createShippingManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        DefaultTableModel shipmentModel = new DefaultTableModel(new String[]{"Shipment ID", "Order ID", "Tracking #", "Status", "Date"}, 0);
        JTable shipmentTable = new JTable(shipmentModel);
        shipmentTable.setBackground(CARD_COLOR);
        shipmentTable.setForeground(TEXT_COLOR);
        shipmentTable.setSelectionBackground(SECONDARY_COLOR);
        shipmentTable.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        shipmentTable.setRowHeight(25);
        shipmentTable.getTableHeader().setBackground(PRIMARY_COLOR);
        shipmentTable.getTableHeader().setForeground(Color.WHITE);

        refreshShipmentTable(shipmentModel);

        JButton updateShippingButton = createStyledButton("✏️ Update Shipping Status");
        updateShippingButton.addActionListener(e -> {
            int selectedRow = shipmentTable.getSelectedRow();
            if (selectedRow == -1 || selectedRow >= orders.size()) {
                JOptionPane.showMessageDialog(this, "Please select a shipment.");
                return;
            }
            Shipment shipment = orders.get(selectedRow).getShipment();
            String[] statuses = {"PENDING", "SHIPPED", "IN_TRANSIT", "DELIVERED"};
            String newStatus = (String) JOptionPane.showInputDialog(this, "Select new status:", "Update Shipping Status", JOptionPane.QUESTION_MESSAGE, null, statuses, shipment.getStatus());
            if (newStatus != null) {
                shipment.setStatus(newStatus);
                if (!newStatus.equals("PENDING")) {
                    shipment.setShipmentDate(LocalDateTime.now());
                }
                refreshShipmentTable(shipmentModel);
                JOptionPane.showMessageDialog(this, "Shipping status updated.");
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BG_COLOR);
        buttonPanel.add(updateShippingButton);

        panel.add(new JScrollPane(shipmentTable), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void showOrderDetails(Order order) {
        StringBuilder details = new StringBuilder();
        details.append("Order #").append(order.getId()).append("\n");
        details.append("Customer: ").append(order.getCustomer().getFullName()).append("\n");
        details.append("Email: ").append(order.getCustomer().getEmail()).append("\n");
        details.append("Address: ").append(order.getDeliveryAddress()).append("\n");
        details.append("Date: ").append(order.getOrderDate()).append("\n");
        details.append("Status: ").append(order.getStatus()).append("\n");
        details.append("Total: $").append(String.format("%.2f", order.getTotalAmount())).append("\n\n");
        details.append("Items:\n");

        for (OrderItem item : order.getItems()) {
            details.append("- ").append(item.getBook().getTitle()).append(" x").append(item.getQuantity()).append(" ($").append(String.format("%.2f", item.getSubtotal())).append(")\n");
        }

        JOptionPane.showMessageDialog(this, details.toString(), "Order Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private void refreshOrderTable(DefaultTableModel model) {
        model.setRowCount(0);
        for (Order order : orders) {
            model.addRow(new Object[]{
                    order.getId(),
                    order.getCustomer().getFullName(),
                    String.format("$%.2f", order.getTotalAmount()),
                    order.getStatus(),
                    order.getOrderDate()
            });
        }
    }

    private void refreshShipmentTable(DefaultTableModel model) {
        model.setRowCount(0);
        for (Order order : orders) {
            if (order.getShipment() != null) {
                Shipment s = order.getShipment();
                model.addRow(new Object[]{
                        s.getId(),
                        order.getId(),
                        s.getTrackingNumber(),
                        s.getStatus(),
                        s.getShipmentDate() != null ? s.getShipmentDate() : "Not shipped"
                });
            }
        }
    }

    // ==================== UTILITY METHODS ====================
    private JPanel createFormField(String labelText, JComponent field) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panel.setBackground(CARD_COLOR);
        panel.setMaximumSize(new Dimension(700, 40));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(TEXT_COLOR);
        label.setPreferredSize(new Dimension(150, 35));

        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setPreferredSize(new Dimension(450, 35));
        if (field instanceof JTextField) {
            ((JTextField) field).setBackground(CARD_COLOR);
            ((JTextField) field).setForeground(TEXT_COLOR);
            ((JTextField) field).setBorder(BorderFactory.createLineBorder(SECONDARY_COLOR, 1));
        } else if (field instanceof JPasswordField) {
            ((JPasswordField) field).setBackground(CARD_COLOR);
            ((JPasswordField) field).setForeground(TEXT_COLOR);
            ((JPasswordField) field).setBorder(BorderFactory.createLineBorder(SECONDARY_COLOR, 1));
        }

        panel.add(label);
        panel.add(field);

        return panel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(BUTTON_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(BUTTON_HOVER);
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(BUTTON_COLOR);
            }
        });

        return button;
    }

    private User authenticate(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    private boolean userExists(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    private void addSelectedBookToCart() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book first.");
            return;
        }

        Book selectedBook = books.get(selectedRow);

        String quantityText = JOptionPane.showInputDialog(this, "Enter quantity:");
        try {
            int quantity = Integer.parseInt(quantityText);

            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be greater than 0.");
                return;
            }

            if (quantity > selectedBook.getStock()) {
                JOptionPane.showMessageDialog(this, "Not enough stock available.");
                return;
            }

            cart.addBook(selectedBook, quantity);
            refreshCartTable();
            JOptionPane.showMessageDialog(this, "Book added to cart successfully.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.");
        }
    }

    private void removeSelectedCartItem() {
        int selectedRow = cartTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to remove.");
            return;
        }

        cart.removeItem(selectedRow);
        refreshCartTable();
        JOptionPane.showMessageDialog(this, "Item removed from cart.");
    }

    private void refreshBookTable(Category category) {
        bookModel.setRowCount(0);
        for (Book book : books) {
            if (category == null || book.getCategory().getId() == category.getId()) {
                bookModel.addRow(new Object[]{
                        book.getId(),
                        book.getTitle(),
                        book.getAuthor(),
                        String.format("$%.2f", book.getPrice()),
                        book.getStock(),
                        book.getCategory().getName()
                });
            }
        }
    }

    private void refreshCartTable() {
        cartModel.setRowCount(0);
        for (OrderItem item : cart.getItems()) {
            cartModel.addRow(new Object[]{
                    item.getBook().getTitle(),
                    item.getQuantity(),
                    String.format("$%.2f", item.getBook().getPrice()),
                    String.format("$%.2f", item.getSubtotal())
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new OnlineBookstoreApp().setVisible(true);
        });
    }
}