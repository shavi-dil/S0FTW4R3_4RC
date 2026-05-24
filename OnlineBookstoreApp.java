import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

class Book {
    private int id;
    private String title;
    private String author;
    private double price;
    private int stock;

    public Book(int id, String title, String author, double price, int stock) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.stock = stock;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }

    public void reduceStock(int quantity) {
        stock -= quantity;
    }
}

class CartItem {
    private Book book;
    private int quantity;

    public CartItem(Book book, int quantity) {
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

class Cart {
    private ArrayList<CartItem> items = new ArrayList<>();

    public void addBook(Book book, int quantity) {
        for (CartItem item : items) {
            if (item.getBook().getId() == book.getId()) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        items.add(new CartItem(book, quantity));
    }

    public void removeItem(int index) {
        if (index >= 0 && index < items.size()) {
            items.remove(index);
        }
    }

    public ArrayList<CartItem> getItems() {
        return items;
    }

    public double getTotal() {
        double total = 0;
        for (CartItem item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    public void clearCart() {
        items.clear();
    }
}

public class OnlineBookstoreApp extends JFrame {
    private ArrayList<Book> books = new ArrayList<>();
    private Cart cart = new Cart();

    private JTable bookTable;
    private JTable cartTable;
    private DefaultTableModel bookModel;
    private DefaultTableModel cartModel;

    // Cafe color scheme
    private static final Color BG_COLOR = new Color(245, 242, 235);        // Cream
    private static final Color PRIMARY_COLOR = new Color(139, 90, 43);     // Brown
    private static final Color SECONDARY_COLOR = new Color(210, 165, 104); // Light Brown
    private static final Color ACCENT_COLOR = new Color(184, 134, 11);     // Dark Goldenrod
    private static final Color TEXT_COLOR = new Color(60, 40, 20);         // Dark Brown
    private static final Color BUTTON_COLOR = new Color(160, 110, 60);     // Medium Brown
    private static final Color BUTTON_HOVER = new Color(180, 130, 80);     // Lighter Brown

    public OnlineBookstoreApp() {
        setTitle("☕ Aesthetic Book Cafe - Online Bookstore");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setBackground(BG_COLOR);

        UIManager.put("TabbedPane.background", BG_COLOR);
        UIManager.put("Panel.background", BG_COLOR);
        UIManager.put("Button.background", BUTTON_COLOR);
        UIManager.put("Button.foreground", Color.WHITE);

        loadSampleBooks();
        createUI();
    }

    private void loadSampleBooks() {
        books.add(new Book(1, "Harry Potter", "J.K. Rowling", 25.99, 10));
        books.add(new Book(2, "Atomic Habits", "James Clear", 22.50, 8));
        books.add(new Book(3, "The Alchemist", "Paulo Coelho", 18.00, 12));
        books.add(new Book(4, "Clean Code", "Robert Martin", 45.00, 5));
        books.add(new Book(5, "AI Basics", "Tom Smith", 30.00, 6));
    }

    private void createUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_COLOR);

        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Tabbed Pane
        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(BG_COLOR);
        tabs.setForeground(PRIMARY_COLOR);
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));

        tabs.add("📚 Books", createBookPanel());
        tabs.add("🛒 Cart", createCartPanel());
        tabs.add("💳 Checkout", createCheckoutPanel());

        mainPanel.add(tabs, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(PRIMARY_COLOR);
        panel.setPreferredSize(new Dimension(1000, 80));
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("☕ Aesthetic Book Cafe");
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Discover Your Next Favorite Read");
        subtitleLabel.setFont(new Font("Georgia", Font.ITALIC, 14));
        subtitleLabel.setForeground(new Color(220, 200, 170));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(PRIMARY_COLOR);
        textPanel.add(titleLabel);
        textPanel.add(subtitleLabel);

        panel.add(textPanel, BorderLayout.WEST);
        return panel;
    }

    private JPanel createBookPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        bookModel = new DefaultTableModel(new String[]{"📖 ID", "📚 Title", "✍️ Author", "💰 Price", "📦 Stock"}, 0);
        bookTable = new JTable(bookModel);
        bookTable.setBackground(Color.WHITE);
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

        refreshBookTable();

        JButton addButton = createStyledButton("➕ Add to Cart");
        addButton.addActionListener(e -> addSelectedBookToCart());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BG_COLOR);
        buttonPanel.add(addButton);

        panel.add(new JScrollPane(bookTable), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createCartPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        cartModel = new DefaultTableModel(new String[]{"📚 Title", "🔢 Qty", "💵 Unit Price", "💰 Subtotal"}, 0);
        cartTable = new JTable(cartModel);
        cartTable.setBackground(Color.WHITE);
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

        JButton removeButton = createStyledButton("🗑️ Remove Item");
        removeButton.addActionListener(e -> removeSelectedCartItem());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BG_COLOR);
        buttonPanel.add(removeButton);

        panel.add(new JScrollPane(cartTable), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

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

        // Title
        JLabel checkoutTitle = new JLabel("Order Summary");
        checkoutTitle.setFont(new Font("Georgia", Font.BOLD, 22));
        checkoutTitle.setForeground(PRIMARY_COLOR);
        checkoutTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(checkoutTitle);
        formPanel.add(Box.createVerticalStrut(15));

        // Customer Name
        JTextField nameField = new JTextField(25);
        formPanel.add(createFormField("👤 Customer Name:", nameField));
        formPanel.add(Box.createVerticalStrut(12));

        // Delivery Address
        JTextField addressField = new JTextField(25);
        formPanel.add(createFormField("📍 Delivery Address:", addressField));
        formPanel.add(Box.createVerticalStrut(12));

        // Order Total
        JLabel totalLabel = new JLabel("Order Total: $0.00");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        totalLabel.setForeground(ACCENT_COLOR);
        totalLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(totalLabel);
        formPanel.add(Box.createVerticalStrut(20));

        // Buttons
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

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Customer name cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (address.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Delivery address cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (cart.getItems().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Cart is empty. Please add books first.", "Cart Empty", JOptionPane.WARNING_MESSAGE);
                return;
            }

            for (CartItem item : cart.getItems()) {
                item.getBook().reduceStock(item.getQuantity());
            }

            String orderMessage = String.format(
                    "🎉 Order Placed Successfully!\n\n" +
                            "💳 Payment Processed\n" +
                            "📧 Invoice Generated for: %s\n" +
                            "📦 Delivery Address: %s\n" +
                            "💰 Total Paid: $%.2f\n\n" +
                            "Thank you for your order!",
                    name, address, cart.getTotal()
            );

            JOptionPane.showMessageDialog(this, orderMessage, "Order Confirmation", JOptionPane.INFORMATION_MESSAGE);

            cart.clearCart();
            refreshBookTable();
            refreshCartTable();
            totalLabel.setText("Order Total: $0.00");
            nameField.setText("");
            addressField.setText("");
        });

        buttonPanel.add(updateTotalButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(checkoutButton);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        formPanel.add(buttonPanel);

        mainPanel.add(formPanel, BorderLayout.NORTH);
        return mainPanel;
    }

    private JPanel createFormField(String labelText, JTextField field) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panel.setBackground(BG_COLOR);
        panel.setMaximumSize(new Dimension(500, 35));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(TEXT_COLOR);
        label.setPreferredSize(new Dimension(180, 30));

        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        field.setPreferredSize(new Dimension(300, 30));
        field.setBackground(Color.WHITE);
        field.setForeground(TEXT_COLOR);
        field.setBorder(BorderFactory.createLineBorder(SECONDARY_COLOR, 1));

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

        } catch (Exception ex) {
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

    private void refreshBookTable() {
        bookModel.setRowCount(0);
        for (Book book : books) {
            bookModel.addRow(new Object[]{
                    book.getId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getPrice(),
                    book.getStock()
            });
        }
    }

    private void refreshCartTable() {
        cartModel.setRowCount(0);
        for (CartItem item : cart.getItems()) {
            cartModel.addRow(new Object[]{
                    item.getBook().getTitle(),
                    item.getQuantity(),
                    item.getBook().getPrice(),
                    item.getSubtotal()
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new OnlineBookstoreApp().setVisible(true);
        });
    }
}