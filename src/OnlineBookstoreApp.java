import java.io.File;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
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
    private String imagePath;

    public Book(int id, String title, String author, double price, int stock,
                Category category, String description, String imagePath) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.description = description;
        this.imagePath = imagePath;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public Category getCategory() { return category; }
    public String getDescription() { return description; }
    public String getImagePath() { return imagePath; }

    public void reduceStock(int quantity) { stock -= quantity; }
    public void increaseStock(int quantity) { stock += quantity; }
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

    public User(int id, String username, String password, String email,
                String fullName, String address, boolean isAdmin) {
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
    private String status;
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
            sb.append(String.format("%-25s %3d x $%7.2f = $%7.2f\n",
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
    private String status;
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

    public void setQuantity(int quantity) { this.quantity = quantity; }

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
    private String status;
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

    public ArrayList<OrderItem> getItems() { return items; }

    public double getTotal() {
        double total = 0;
        for (OrderItem item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    public void clearCart() { items.clear(); }
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

    private JTable cartTable;
    private DefaultTableModel cartModel;

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JPanel mainPanel;
    private JPanel bookCarouselPanel;
    private JScrollPane bookCarouselScrollPane;

    private JTextField bookSearchField;
    private Category selectedCategoryFilter;
    private HashMap<Integer, JPanel> categoryCardMap = new HashMap<>();
    private HashMap<Integer, ImageIcon> bookCoverMap = new HashMap<>();

    private static final String[] IMAGE_DIRS = {
            "images",
            "src/images"
    };

    private static final String SEARCH_PLACEHOLDER = "Search Title, Author or ISBN";
    private static final Color BG_COLOR = new Color(250, 247, 242);
    private static final Color PRIMARY_COLOR = new Color(8, 35, 70);
    private static final Color SECONDARY_COLOR = new Color(42, 76, 118);
    private static final Color CATEGORY_CARD_COLOR = new Color(95, 22, 30);
    private static final Color CATEGORY_CARD_SELECTED = new Color(170, 38, 50);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(47, 47, 47);
    private static final Color SECONDARY_TEXT = new Color(122, 122, 122);
    private static final Color BUTTON_COLOR = PRIMARY_COLOR;
    private static final Color BUTTON_HOVER = SECONDARY_COLOR;

    public OnlineBookstoreApp() {
        setTitle("Ink and Lantern Books");
        setSize(1250, 780);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeData();
        createUI();
    }

    private void initializeData() {
        categories.add(new Category(1, "Fiction"));
        categories.add(new Category(2, "Non-Fiction"));
        categories.add(new Category(3, "Science"));
        categories.add(new Category(4, "Self-Help"));

        books.add(new Book(1, "Harry Potter", "J.K. Rowling", 25.99, 10, categories.get(0),
                "A magical adventure of a young wizard", "harrypotter.jpg"));

        books.add(new Book(2, "Atomic Habits", "James Clear", 22.50, 8, categories.get(3),
                "Transform your life through tiny habits", "atomichabits.jpg"));

        books.add(new Book(3, "The Alchemist", "Paulo Coelho", 18.00, 12, categories.get(0),
                "A philosophical novel about personal journey", "thealchemist.jpg"));

        books.add(new Book(4, "Clean Code", "Robert Martin", 45.00, 5, categories.get(2),
                "A guide to writing better code", "cleancode.jpg"));

        books.add(new Book(5, "AI Basics", "Tom Smith", 30.00, 6, categories.get(2),
                "Introduction to artificial intelligence", "aibasics.jpg"));

        books.add(new Book(6, "Sapiens", "Yuval Noah Harari", 28.00, 9, categories.get(1),
                "A brief history of humankind", "sapiens.jpg"));

        books.add(new Book(7, "Thinking, Fast and Slow", "Daniel Kahneman", 35.00, 7, categories.get(1),
                "Psychology of human behavior", "thinkingfastandslow.jpg"));

        users.add(new User(nextUserId++, "admin", "admin123", "admin@bookstore.com",
                "Admin User", "123 Admin Street", true));

        users.add(new User(nextUserId++, "john_doe", "password123", "john@email.com",
                "John Doe", "456 Main Street", false));
    }

    private void createUI() {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        cardPanel.add(createAuthenticationPanel(), "AUTH");

        mainPanel = new JPanel();
        cardPanel.add(mainPanel, "MAIN");

        add(cardPanel);
        cardLayout.show(cardPanel, "AUTH");
    }

    // ==================== LOGIN / REGISTER ====================
    private JPanel createAuthenticationPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(1200, 100));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("INK AND LANTERN BOOKS");
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("A cozy online bookstore experience");
        subtitleLabel.setFont(new Font("Georgia", Font.ITALIC, 14));
        subtitleLabel.setForeground(new Color(220, 220, 220));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(titleLabel);
        headerPanel.add(subtitleLabel);

        panel.add(headerPanel, BorderLayout.NORTH);

        JTabbedPane authTabs = new JTabbedPane();
        authTabs.add("Login", createLoginPanel());
        authTabs.add("Register", createRegisterPanel());

        panel.add(authTabs, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(50, 120, 50, 120));

        JLabel titleLabel = new JLabel("User Login");
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 26));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField usernameField = new JTextField(30);
        JPasswordField passwordField = new JPasswordField(30);

        JButton loginButton = createStyledButton("Login");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginButton.addActionListener(e -> {
            User user = authenticate(usernameField.getText().trim(), new String(passwordField.getPassword()));

            if (user != null) {
                currentUser = user;
                rebuildMainPanel();
                cardLayout.show(cardPanel, "MAIN");
                usernameField.setText("");
                passwordField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.");
            }
        });

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(30));
        panel.add(createFormField("Username:", usernameField));
        panel.add(Box.createVerticalStrut(15));
        panel.add(createFormField("Password:", passwordField));
        panel.add(Box.createVerticalStrut(30));
        panel.add(loginButton);

        return panel;
    }

    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 120, 40, 120));

        JLabel titleLabel = new JLabel("Create New Account");
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 26));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField usernameField = new JTextField(30);
        JPasswordField passwordField = new JPasswordField(30);
        JTextField emailField = new JTextField(30);
        JTextField fullNameField = new JTextField(30);
        JTextField addressField = new JTextField(30);

        JButton registerButton = createStyledButton("Register");
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        registerButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String email = emailField.getText().trim();
            String fullName = fullNameField.getText().trim();
            String address = addressField.getText().trim();

            if (username.isEmpty() || password.isEmpty() || email.isEmpty() || fullName.isEmpty() || address.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.");
                return;
            }

            if (userExists(username)) {
                JOptionPane.showMessageDialog(this, "Username already exists.");
                return;
            }

            users.add(new User(nextUserId++, username, password, email, fullName, address, false));
            JOptionPane.showMessageDialog(this, "Account created successfully. Please login.");

            usernameField.setText("");
            passwordField.setText("");
            emailField.setText("");
            fullNameField.setText("");
            addressField.setText("");
        });

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(25));
        panel.add(createFormField("Username:", usernameField));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createFormField("Password:", passwordField));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createFormField("Email:", emailField));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createFormField("Full Name:", fullNameField));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createFormField("Address:", addressField));
        panel.add(Box.createVerticalStrut(25));
        panel.add(registerButton);

        return panel;
    }

    // ==================== MAIN APPLICATION ====================
    private void rebuildMainPanel() {
        cardPanel.remove(mainPanel);
        mainPanel = createMainApplicationPanel();
        cardPanel.add(mainPanel, "MAIN");
        cardPanel.revalidate();
        cardPanel.repaint();
    }

    private JPanel createMainApplicationPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);

        panel.add(createHeaderPanel(), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Books", createBookPanel());
        tabs.add("Cart", createCartPanel());
        tabs.add("Checkout", createCheckoutPanel());
        tabs.add("Account", createAccountPanel());

        if (currentUser != null && currentUser.isAdmin()) {
            tabs.add("Admin", createAdminPanel());
        }

        panel.add(tabs, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 10));
        panel.setBackground(PRIMARY_COLOR);
        panel.setPreferredSize(new Dimension(1200, 75));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));

        JLabel logo = new JLabel("INK & LANTERN BOOKS");
        logo.setFont(new Font("Georgia", Font.BOLD, 24));
        logo.setForeground(Color.WHITE);

        JLabel userLabel = new JLabel("Welcome, " + currentUser.getFullName());
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(Color.WHITE);

        JButton logoutButton = createStyledButton("Logout");
        logoutButton.addActionListener(e -> {
            currentUser = null;
            cart.clearCart();
            cardLayout.show(cardPanel, "AUTH");
        });

        panel.add(logo, BorderLayout.WEST);
        panel.add(userLabel, BorderLayout.CENTER);
        panel.add(logoutButton, BorderLayout.EAST);

        return panel;
    }

    // ==================== DYM0CKS STYLE BOOK PAGE ====================
    private JPanel createBookPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JPanel topNav = new JPanel(new BorderLayout(20, 10));
        topNav.setBackground(Color.WHITE);
        topNav.setBorder(BorderFactory.createEmptyBorder(18, 25, 18, 25));

        JLabel logo = new JLabel("INK & LANTERN");
        logo.setFont(new Font("Georgia", Font.BOLD, 24));
        logo.setForeground(new Color(190, 20, 40));

        bookSearchField = new JTextField(SEARCH_PLACEHOLDER);
        bookSearchField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        bookSearchField.setForeground(Color.GRAY);
        bookSearchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 1, true),
                BorderFactory.createEmptyBorder(10, 18, 10, 18)
        ));

        bookSearchField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (bookSearchField.getText().equals(SEARCH_PLACEHOLDER)) {
                    bookSearchField.setText("");
                    bookSearchField.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (bookSearchField.getText().trim().isEmpty()) {
                    bookSearchField.setText(SEARCH_PLACEHOLDER);
                    bookSearchField.setForeground(Color.GRAY);
                }
            }
        });

        bookSearchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { refreshBookCarousel(); }
            public void removeUpdate(DocumentEvent e) { refreshBookCarousel(); }
            public void changedUpdate(DocumentEvent e) { refreshBookCarousel(); }
        });

        JLabel icons = new JLabel("Select Store   ♡   👤   🛒");
        icons.setFont(new Font("Segoe UI", Font.BOLD, 14));
        icons.setForeground(TEXT_COLOR);

        topNav.add(logo, BorderLayout.WEST);
        topNav.add(bookSearchField, BorderLayout.CENTER);
        topNav.add(icons, BorderLayout.EAST);

        panel.add(topNav, BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(30, 30, 20, 30));

        JPanel categoryPanel = new JPanel(new GridLayout(1, 4, 18, 0));
        categoryPanel.setBackground(Color.WHITE);
        categoryPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        categoryPanel.add(createCategoryCard("Shop Fiction", categories.get(0)));
        categoryPanel.add(createCategoryCard("Shop Non-Fiction", categories.get(1)));
        categoryPanel.add(createCategoryCard("Shop Science", categories.get(2)));
        categoryPanel.add(createCategoryCard("Shop Self-Help", categories.get(3)));

        content.add(categoryPanel);
        content.add(Box.createVerticalStrut(35));

        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setBackground(Color.WHITE);

        JLabel sectionTitle = new JLabel("Top Bestselling books");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 30));
        sectionTitle.setForeground(new Color(35, 35, 35));

        JButton viewAllButton = new JButton("View All  >");
        viewAllButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        viewAllButton.setFocusPainted(false);
        viewAllButton.setBorderPainted(false);
        viewAllButton.setContentAreaFilled(false);
        viewAllButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewAllButton.addActionListener(e -> {
            selectedCategoryFilter = null;
            refreshBookCarousel();
        });

        titleRow.add(sectionTitle, BorderLayout.WEST);
        titleRow.add(viewAllButton, BorderLayout.EAST);

        content.add(titleRow);
        content.add(Box.createVerticalStrut(25));

        bookCarouselPanel = new JPanel();
        bookCarouselPanel.setLayout(new BoxLayout(bookCarouselPanel, BoxLayout.X_AXIS));
        bookCarouselPanel.setBackground(Color.WHITE);

        bookCarouselScrollPane = new JScrollPane(
                bookCarouselPanel,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        bookCarouselScrollPane.setBorder(BorderFactory.createEmptyBorder());
        bookCarouselScrollPane.getHorizontalScrollBar().setUnitIncrement(18);
        bookCarouselScrollPane.setPreferredSize(new Dimension(1100, 420));

        refreshBookCarousel();

        content.add(bookCarouselScrollPane);
        panel.add(content, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createCategoryCard(String text, Category category) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CATEGORY_CARD_COLOR);
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(label, BorderLayout.CENTER);
        categoryCardMap.put(category.getId(), card);

        card.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                selectedCategoryFilter = category;
                refreshBookCarousel();
            }
        });

        return card;
    }

    private void refreshBookCarousel() {
        String query = "";
        if (bookSearchField != null) {
            String rawText = bookSearchField.getText().trim();
            if (!SEARCH_PLACEHOLDER.equals(rawText)) {
                query = rawText.toLowerCase();
            }
        }

        bookCarouselPanel.removeAll();
        boolean found = false;

        for (Book book : books) {
            if (matchesBookFilter(book, query, selectedCategoryFilter)) {
                bookCarouselPanel.add(createBookCard(book));
                bookCarouselPanel.add(Box.createHorizontalStrut(25));
                found = true;
            }
        }

        if (!found) {
            JPanel emptyPanel = new JPanel();
            emptyPanel.setBackground(Color.WHITE);
            JLabel emptyLabel = new JLabel("No books match your search or selected category.");
            emptyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            emptyLabel.setForeground(TEXT_COLOR);
            emptyPanel.add(emptyLabel);
            bookCarouselPanel.add(emptyPanel);
        }

        updateCategoryCardStyles();
        bookCarouselPanel.revalidate();
        bookCarouselPanel.repaint();
    }

    private boolean matchesBookFilter(Book book, String query, Category category) {
        if (category != null && book.getCategory().getId() != category.getId()) {
            return false;
        }
        if (query.isEmpty()) {
            return true;
        }
        return book.getTitle().toLowerCase().contains(query)
                || book.getAuthor().toLowerCase().contains(query)
                || String.valueOf(book.getId()).contains(query);
    }

    private void updateCategoryCardStyles() {
        for (Integer id : categoryCardMap.keySet()) {
            JPanel card = categoryCardMap.get(id);
            if (selectedCategoryFilter != null && selectedCategoryFilter.getId() == id) {
                card.setBackground(CATEGORY_CARD_SELECTED);
            } else {
                card.setBackground(CATEGORY_CARD_COLOR);
            }
        }
    }

    private JPanel createBookCard(Book book) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(185, 395));
        card.setMaximumSize(new Dimension(185, 395));
        card.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel coverLabel = new JLabel();
        coverLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        coverLabel.setHorizontalAlignment(SwingConstants.CENTER);
        coverLabel.setPreferredSize(new Dimension(165, 235));
        coverLabel.setMaximumSize(new Dimension(165, 235));

        ImageIcon coverIcon = getBookCoverIcon(book);
        if (coverIcon != null) {
            coverLabel.setIcon(coverIcon);
        } else {
            coverLabel.setText("No Image");
            coverLabel.setForeground(Color.GRAY);
        }

        JLabel authorLabel = new JLabel(book.getAuthor() + "     Paperback");
        authorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        authorLabel.setForeground(new Color(140, 140, 140));
        authorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("<html><div style='width:160px;'>" + book.getTitle() + "</div></html>");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        titleLabel.setForeground(new Color(25, 25, 25));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel priceLabel = new JLabel(String.format("$%.2f", book.getPrice()));
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        priceLabel.setForeground(new Color(20, 20, 20));
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton addButton = createStyledButton("Add to Cart");
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        addButton.addActionListener(e -> {
            if (book.getStock() <= 0) {
                JOptionPane.showMessageDialog(this, "This book is out of stock.");
                return;
            }

            cart.addBook(book, 1);
            refreshCartTable();
            JOptionPane.showMessageDialog(this, book.getTitle() + " added to cart.");
        });

        card.add(coverLabel);
        card.add(Box.createVerticalStrut(12));
        card.add(authorLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(priceLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(addButton);

        return card;
    }

    private ImageIcon getBookCoverIcon(Book book) {
        if (bookCoverMap.containsKey(book.getId())) {
            return bookCoverMap.get(book.getId());
        }

        for (String dir : IMAGE_DIRS) {
            File imageFile = new File(dir, book.getImagePath());

            if (imageFile.exists()) {
                ImageIcon icon = new ImageIcon(imageFile.getAbsolutePath());
                Image image = icon.getImage().getScaledInstance(165, 235, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(image);
                bookCoverMap.put(book.getId(), scaledIcon);
                return scaledIcon;
            }
        }

        bookCoverMap.put(book.getId(), null);
        return null;
    }

    // ==================== CART PANEL ====================
    private JPanel createCartPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        cartModel = new DefaultTableModel(new String[]{"Cover", "Title", "Qty", "Unit Price", "Subtotal"}, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 0 ? ImageIcon.class : Object.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        cartTable = new JTable(cartModel);
        cartTable.setRowHeight(90);
        cartTable.setDefaultRenderer(ImageIcon.class, new DefaultTableCellRenderer() {
            @Override
            public void setValue(Object value) {
                setText("");
                setIcon(value instanceof ImageIcon ? (ImageIcon) value : null);
                setHorizontalAlignment(CENTER);
            }
        });

        refreshCartTable();

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setBackground(BG_COLOR);

        JLabel totalLabel = new JLabel(String.format("Cart Total: $%.2f", cart.getTotal()));
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JButton refreshButton = createStyledButton("Refresh");
        refreshButton.addActionListener(e -> {
            refreshCartTable();
            totalLabel.setText(String.format("Cart Total: $%.2f", cart.getTotal()));
        });

        JButton removeButton = createStyledButton("Remove Item");
        removeButton.addActionListener(e -> {
            int row = cartTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select an item.");
                return;
            }
            cart.removeItem(row);
            refreshCartTable();
            totalLabel.setText(String.format("Cart Total: $%.2f", cart.getTotal()));
        });

        topPanel.add(totalLabel);
        topPanel.add(refreshButton);
        topPanel.add(removeButton);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(cartTable), BorderLayout.CENTER);

        return panel;
    }

    private void refreshCartTable() {
        if (cartModel == null) return;

        cartModel.setRowCount(0);

        for (OrderItem item : cart.getItems()) {
            cartModel.addRow(new Object[]{
                    getCartCoverIcon(item.getBook()),
                    item.getBook().getTitle(),
                    item.getQuantity(),
                    String.format("$%.2f", item.getBook().getPrice()),
                    String.format("$%.2f", item.getSubtotal())
            });
        }
    }

    private ImageIcon getCartCoverIcon(Book book) {
        ImageIcon icon = getBookCoverIcon(book);
        if (icon == null) {
            return null;
        }
        Image image = icon.getImage().getScaledInstance(70, 90, Image.SCALE_SMOOTH);
        return new ImageIcon(image);
    }

    // ==================== CHECKOUT PANEL ====================
    private JPanel createCheckoutPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(CARD_COLOR);
        formPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel title = new JLabel("Checkout");
        title.setFont(new Font("Georgia", Font.BOLD, 26));
        title.setForeground(PRIMARY_COLOR);

        JTextField nameField = new JTextField(currentUser.getFullName(), 25);
        JTextField addressField = new JTextField(currentUser.getAddress(), 25);
        JTextField emailField = new JTextField(currentUser.getEmail(), 25);

        JLabel totalLabel = new JLabel(String.format("Order Total: $%.2f", cart.getTotal()));
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JButton updateButton = createStyledButton("Update Total");
        updateButton.addActionListener(e ->
                totalLabel.setText(String.format("Order Total: $%.2f", cart.getTotal()))
        );

        JButton placeOrderButton = createStyledButton("Place Order");
        placeOrderButton.addActionListener(e -> {
            if (cart.getItems().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Cart is empty.");
                return;
            }

            String name = nameField.getText().trim();
            String address = addressField.getText().trim();
            String email = emailField.getText().trim();

            if (name.isEmpty() || address.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all checkout fields.");
                return;
            }

            User tempUser = new User(-1, "", "", email, name, address, false);
            Order order = new Order(nextOrderId++, tempUser, new ArrayList<>(cart.getItems()), address);

            Payment payment = new Payment(nextPaymentId++, order.getTotalAmount(), "COMPLETED", LocalDateTime.now());
            order.setPayment(payment);

            Shipment shipment = new Shipment(nextShipmentId++, order);
            order.setShipment(shipment);

            order.setStatus("CONFIRMED");

            for (OrderItem item : cart.getItems()) {
                item.getBook().reduceStock(item.getQuantity());
            }

            orders.add(order);

            Invoice invoice = new Invoice(nextInvoiceId++, order, LocalDateTime.now());
            JOptionPane.showMessageDialog(this, invoice.generateInvoiceText(), "Invoice", JOptionPane.INFORMATION_MESSAGE);

            cart.clearCart();
            refreshCartTable();
            totalLabel.setText("Order Total: $0.00");
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(CARD_COLOR);
        buttonPanel.add(updateButton);
        buttonPanel.add(placeOrderButton);

        formPanel.add(title);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(createFormField("Customer Name:", nameField));
        formPanel.add(Box.createVerticalStrut(12));
        formPanel.add(createFormField("Delivery Address:", addressField));
        formPanel.add(Box.createVerticalStrut(12));
        formPanel.add(createFormField("Email:", emailField));
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(totalLabel);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(buttonPanel);

        panel.add(formPanel, BorderLayout.NORTH);
        return panel;
    }

    // ==================== ACCOUNT PANEL ====================
    private JPanel createAccountPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel title = new JLabel("My Account");
        title.setFont(new Font("Georgia", Font.BOLD, 26));
        title.setForeground(PRIMARY_COLOR);

        panel.add(title);
        panel.add(Box.createVerticalStrut(20));

        if (currentUser != null) {
            panel.add(new JLabel("Username: " + currentUser.getUsername()));
            panel.add(new JLabel("Full Name: " + currentUser.getFullName()));
            panel.add(new JLabel("Email: " + currentUser.getEmail()));
            panel.add(new JLabel("Address: " + currentUser.getAddress()));
            panel.add(new JLabel("Account Type: " + (currentUser.isAdmin() ? "Admin" : "Customer")));
        }

        return panel;
    }

    // ==================== ADMIN PANEL ====================
    private JPanel createAdminPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        DefaultTableModel orderModel = new DefaultTableModel(
                new String[]{"Order ID", "Customer", "Total", "Status", "Date"}, 0);

        JTable orderTable = new JTable(orderModel);
        orderTable.setRowHeight(28);

        for (Order order : orders) {
            orderModel.addRow(new Object[]{
                    order.getId(),
                    order.getCustomer().getFullName(),
                    String.format("$%.2f", order.getTotalAmount()),
                    order.getStatus(),
                    order.getOrderDate()
            });
        }

        panel.add(new JScrollPane(orderTable), BorderLayout.CENTER);
        return panel;
    }

    // ==================== UTILITY METHODS ====================
    private JPanel createFormField(String labelText, JComponent field) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panel.setBackground(CARD_COLOR);
        panel.setMaximumSize(new Dimension(750, 42));

        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(150, 35));
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT_COLOR);

        field.setPreferredSize(new Dimension(450, 35));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));

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

    // ==================== MAIN METHOD ====================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new OnlineBookstoreApp().setVisible(true);
        });
    }
}