import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.util.List;

public class BookstoreAppUI extends JFrame {
    private final BookstoreService service;
    private User currentUser;
    private Cart cart;

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);

    private JTextField searchField;
    private JPanel browseBookPanel;
    private JPanel checkoutPanel;
    private JLabel checkoutTotalLabel;
    private DefaultListModel<String> checkoutListModel;
    private JList<String> checkoutList;
    private Category activeCategory = new Category(0, "All Categories");
    private final String searchPlaceholder = "Search Title, Author or ISBN";
    private CardLayout customerContentLayout;
    private JPanel customerContentPanel;
    private CardLayout adminContentLayout;
    private JPanel adminContentPanel;

    private DefaultListModel<String> cartListModel;
    private JList<String> cartList;
    private JLabel cartTotalLabel;

    private DefaultListModel<String> orderHistoryModel;
    private JList<String> orderHistoryList;

    private DefaultListModel<Book> adminBookListModel;
    private JList<Book> adminBookList;
    private DefaultListModel<String> adminOrderListModel;
    private JList<String> adminOrderList;

    private static final Color BG_MAIN = new Color(0xFA, 0xF7, 0xF2);
    private static final Color BG_SECONDARY = new Color(0xF1, 0xEC, 0xE6);
    private static final Color PRIMARY = new Color(0x6C, 0x8E, 0xBF);
    private static final Color SECONDARY = new Color(0x8F, 0xA8, 0xC9);
    private static final Color ACCENT = new Color(0xE8, 0xC7, 0x6A);
    private static final Color CARD = new Color(0xFF, 0xFF, 0xFF);
    private static final Color TEXT_MAIN = new Color(0x2F, 0x2F, 0x2F);
    private static final Color TEXT_SECOND = new Color(0x7A, 0x7A, 0x7A);
    private static final Color SUCCESS = new Color(0x7B, 0xAE, 0x7F);
    private static final Color ERROR = new Color(0xD9, 0x7C, 0x6C);

    public BookstoreAppUI(BookstoreService service) {
        this.service = service;
        this.cart = new Cart();
        setTitle("Ink and Lantern Bookstore");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_MAIN);
        cardPanel.setBackground(BG_MAIN);

        cardPanel.add(createAuthenticationPanel(), "AUTH");
        add(cardPanel, BorderLayout.CENTER);
        cardLayout.show(cardPanel, "AUTH");
    }

    private JPanel createAuthenticationPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG_MAIN);
        panel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        RoundedPanel card = new RoundedPanel(BG_SECONDARY, 24);
        card.setLayout(new GridBagLayout());
        card.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JLabel titleLabel = new JLabel("Ink & Lantern Bookstore", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
        titleLabel.setForeground(TEXT_MAIN);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        JLabel subtitleLabel = new JLabel("A calm reading experience for every customer.", SwingConstants.CENTER);
        subtitleLabel.setForeground(TEXT_SECOND);
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(BG_SECONDARY);
        tabs.setForeground(TEXT_MAIN);
        tabs.setFont(new Font("SansSerif", Font.BOLD, 14));
        tabs.addTab("Login", createLoginPanel());
        tabs.addTab("Register", createRegisterPanel());
        tabs.setBorder(BorderFactory.createEmptyBorder(16, 0, 0, 0));

        card.add(titleLabel, gbc);
        gbc.gridy++;
        card.add(subtitleLabel, gbc);
        gbc.gridy++;
        card.add(tabs, gbc);

        panel.add(card);
        return panel;
    }

    private JPanel createLoginPanel() {
        JPanel panel = new RoundedPanel(CARD, 18);
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(TEXT_MAIN);
        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(TEXT_MAIN);
        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        styleInputField(usernameField);
        styleInputField(passwordField);
        JButton loginButton = createPrimaryButton("Login");
        loginButton.addActionListener(e -> handleLogin(usernameField.getText().trim(), new String(passwordField.getPassword())));

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(userLabel, gbc);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passLabel, gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(loginButton, gbc);

        return panel;
    }

    private JPanel createRegisterPanel() {
        JPanel panel = new RoundedPanel(CARD, 18);
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(TEXT_MAIN);
        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(TEXT_MAIN);
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(TEXT_MAIN);
        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setForeground(TEXT_MAIN);
        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setForeground(TEXT_MAIN);

        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JTextField emailField = new JTextField(20);
        JTextField fullNameField = new JTextField(20);
        JTextField addressField = new JTextField(20);
        styleInputField(usernameField);
        styleInputField(passwordField);
        styleInputField(emailField);
        styleInputField(fullNameField);
        styleInputField(addressField);

        JButton registerButton = createPrimaryButton("Register");
        registerButton.addActionListener(e -> handleRegister(
                usernameField.getText().trim(),
                new String(passwordField.getPassword()),
                emailField.getText().trim(),
                fullNameField.getText().trim(),
                addressField.getText().trim()));

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(userLabel, gbc);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(passLabel, gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(emailLabel, gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(nameLabel, gbc);
        gbc.gridx = 1;
        panel.add(fullNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(addressLabel, gbc);
        gbc.gridx = 1;
        panel.add(addressField, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        panel.add(registerButton, gbc);

        return panel;
    }

    private void handleLogin(String username, String password) {
        User user = service.authenticate(username, password);
        if (user == null) {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.WARNING_MESSAGE);
            return;
        }
        currentUser = user;
        cart = new Cart();
        if (currentUser.isAdmin()) {
            cardPanel.add(createAdminPanel(), "ADMIN");
            cardLayout.show(cardPanel, "ADMIN");
        } else {
            cardPanel.add(createCustomerPanel(), "CUSTOMER");
            cardLayout.show(cardPanel, "CUSTOMER");
        }
    }

    private void handleRegister(String username, String password, String email, String fullName, String address) {
        try {
            service.registerCustomer(username, password, email, fullName, address);
            JOptionPane.showMessageDialog(this, "Registration complete. Please login.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Registration Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private JPanel createCustomerPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_MAIN);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        JLabel title = new JLabel("Ink & Lantern Bookstore");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        headerPanel.add(title, BorderLayout.WEST);

        JLabel userLabel = new JLabel("Welcome, " + currentUser.getFullName());
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JButton logoutButton = createSecondaryButton("Logout");
        logoutButton.addActionListener(e -> logout());

        JPanel headerActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        headerActions.setOpaque(false);
        headerActions.add(userLabel);
        headerActions.add(logoutButton);
        headerPanel.add(headerActions, BorderLayout.EAST);

        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 10));
        navPanel.setBackground(BG_SECONDARY);
        JButton booksButton = createPrimaryButton("Catalogue");
        JButton cartButton = createPrimaryButton("Cart");
        JButton checkoutButton = createPrimaryButton("Checkout");
        JButton ordersButton = createPrimaryButton("Orders");
        JButton accountButton = createPrimaryButton("Account");
        navPanel.add(booksButton);
        navPanel.add(cartButton);
        navPanel.add(checkoutButton);
        navPanel.add(ordersButton);
        navPanel.add(accountButton);

        customerContentLayout = new CardLayout();
        customerContentPanel = new JPanel(customerContentLayout);
        customerContentPanel.add(createBrowsePanel(), "BOOKS");
        customerContentPanel.add(createCartPanel(), "CART");
        customerContentPanel.add(createCheckoutPanel(), "CHECKOUT");
        customerContentPanel.add(createOrderHistoryPanel(), "ORDERS");
        customerContentPanel.add(createAccountPanel(), "ACCOUNT");

        booksButton.addActionListener(e -> {
            customerContentLayout.show(customerContentPanel, "BOOKS");
            refreshBrowseGrid();
        });
        cartButton.addActionListener(e -> {
            customerContentLayout.show(customerContentPanel, "CART");
            refreshCartList();
        });
        checkoutButton.addActionListener(e -> {
            customerContentLayout.show(customerContentPanel, "CHECKOUT");
            refreshCheckoutPanel();
        });
        ordersButton.addActionListener(e -> {
            customerContentLayout.show(customerContentPanel, "ORDERS");
            refreshOrderHistory();
        });
        accountButton.addActionListener(e -> customerContentLayout.show(customerContentPanel, "ACCOUNT"));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(headerPanel, BorderLayout.NORTH);
        topPanel.add(navPanel, BorderLayout.SOUTH);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(customerContentPanel, BorderLayout.CENTER);
        customerContentLayout.show(customerContentPanel, "BOOKS");
        return panel;
    }

    private JPanel createAdminPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_MAIN);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        JLabel title = new JLabel("Admin Dashboard");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        headerPanel.add(title, BorderLayout.WEST);

        JLabel userLabel = new JLabel("Admin: " + currentUser.getFullName());
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JButton logoutButton = createSecondaryButton("Logout");
        logoutButton.addActionListener(e -> logout());

        JPanel headerActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        headerActions.setOpaque(false);
        headerActions.add(userLabel);
        headerActions.add(logoutButton);
        headerPanel.add(headerActions, BorderLayout.EAST);

        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 10));
        navPanel.setBackground(BG_SECONDARY);
        JButton booksButton = createPrimaryButton("Books");
        JButton ordersButton = createPrimaryButton("Orders");
        navPanel.add(booksButton);
        navPanel.add(ordersButton);

        adminContentLayout = new CardLayout();
        adminContentPanel = new JPanel(adminContentLayout);
        adminContentPanel.add(createAdminBooksPanel(), "BOOKS");
        adminContentPanel.add(createAdminOrdersPanel(), "ORDERS");

        booksButton.addActionListener(e -> adminContentLayout.show(adminContentPanel, "BOOKS"));
        ordersButton.addActionListener(e -> adminContentLayout.show(adminContentPanel, "ORDERS"));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(headerPanel, BorderLayout.NORTH);
        topPanel.add(navPanel, BorderLayout.SOUTH);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(adminContentPanel, BorderLayout.CENTER);
        adminContentLayout.show(adminContentPanel, "BOOKS");
        return panel;
    }

    private JPanel createBrowsePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        panel.setBackground(BG_MAIN);

        JPanel searchPanel = new JPanel(new BorderLayout(12, 12));
        searchPanel.setBackground(BG_SECONDARY);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(SECONDARY), BorderFactory.createEmptyBorder(16, 16, 16, 16)));

        searchField = new JTextField(searchPlaceholder);
        searchField.setForeground(TEXT_SECOND);
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        styleInputField(searchField);
        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals(searchPlaceholder)) {
                    searchField.setText("");
                    searchField.setForeground(TEXT_MAIN);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().trim().isEmpty()) {
                    searchField.setText(searchPlaceholder);
                    searchField.setForeground(TEXT_SECOND);
                }
            }
        });

        JButton searchButton = createPrimaryButton("Search");
        searchButton.addActionListener(e -> refreshBrowseGrid());

        JPanel searchInputPanel = new JPanel(new BorderLayout(8, 0));
        searchInputPanel.setOpaque(false);
        searchInputPanel.add(searchField, BorderLayout.CENTER);
        searchInputPanel.add(searchButton, BorderLayout.EAST);

        JPanel categoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        categoryPanel.setOpaque(false);
        for (Category category : service.getCategories()) {
            JButton categoryButton = createSecondaryButton(category.getName());
            categoryButton.addActionListener(e -> {
                activeCategory = category;
                refreshBrowseGrid();
            });
            categoryPanel.add(categoryButton);
        }
        JButton allButton = createSecondaryButton("View All");
        allButton.addActionListener(e -> {
            activeCategory = new Category(0, "All Categories");
            refreshBrowseGrid();
        });
        categoryPanel.add(allButton);

        searchPanel.add(searchInputPanel, BorderLayout.NORTH);
        searchPanel.add(categoryPanel, BorderLayout.SOUTH);

        panel.add(searchPanel, BorderLayout.NORTH);

        browseBookPanel = new JPanel(new GridLayout(0, 3, 16, 16));
        browseBookPanel.setOpaque(false);
        JScrollPane scrollPane = new JScrollPane(browseBookPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scrollPane, BorderLayout.CENTER);

        refreshBrowseGrid();
        return panel;
    }

    private JPanel createCheckoutPanel() {
        checkoutPanel = new JPanel(new BorderLayout(12, 12));
        checkoutPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        checkoutPanel.setBackground(BG_SECONDARY);

        JLabel title = new JLabel("Checkout");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(TEXT_MAIN);

        checkoutTotalLabel = new JLabel("Order Total: $0.00");
        checkoutTotalLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        checkoutTotalLabel.setForeground(ACCENT);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(title, BorderLayout.WEST);
        topPanel.add(checkoutTotalLabel, BorderLayout.EAST);

        checkoutListModel = new DefaultListModel<>();
        checkoutList = new JList<>(checkoutListModel);
        checkoutList.setBackground(CARD);
        checkoutList.setForeground(TEXT_MAIN);
        checkoutList.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JScrollPane listScrollPane = new JScrollPane(checkoutList);

        JButton placeOrderButton = createPrimaryButton("Place Order");
        placeOrderButton.addActionListener(e -> handleGuiCheckout());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);
        bottomPanel.add(placeOrderButton);

        checkoutPanel.add(topPanel, BorderLayout.NORTH);
        checkoutPanel.add(listScrollPane, BorderLayout.CENTER);
        checkoutPanel.add(bottomPanel, BorderLayout.SOUTH);

        refreshCheckoutPanel();
        return checkoutPanel;
    }

    private JPanel createCartPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        panel.setBackground(BG_SECONDARY);

        cartListModel = new DefaultListModel<>();
        cartList = new JList<>(cartListModel);
        cartList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cartList.setBackground(CARD);
        cartList.setForeground(TEXT_MAIN);
        cartList.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(cartList);

        cartTotalLabel = new JLabel("Total: $0.00");
        cartTotalLabel.setForeground(TEXT_MAIN);
        cartTotalLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        JButton removeButton = createSecondaryButton("Remove Selected");
        removeButton.addActionListener(e -> removeSelectedCartItem());
        JButton updateButton = createSecondaryButton("Update Qty");
        updateButton.addActionListener(e -> updateSelectedCartItemQuantity());
        JButton checkoutButton = createPrimaryButton("Checkout");
        checkoutButton.addActionListener(e -> handleGuiCheckout());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.add(removeButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(checkoutButton);
        buttonPanel.add(cartTotalLabel);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        refreshCartList();
        return panel;
    }

    private JPanel createOrderHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        panel.setBackground(BG_SECONDARY);

        orderHistoryModel = new DefaultListModel<>();
        orderHistoryList = new JList<>(orderHistoryModel);
        orderHistoryList.setBackground(CARD);
        orderHistoryList.setForeground(TEXT_MAIN);
        orderHistoryList.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(orderHistoryList);

        panel.add(scrollPane, BorderLayout.CENTER);
        refreshOrderHistory();
        return panel;
    }

    private JPanel createAccountPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG_SECONDARY);
        panel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel usernameLabel = new JLabel("Username: " + currentUser.getUsername());
        usernameLabel.setForeground(TEXT_MAIN);
        usernameLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        JLabel roleLabel = new JLabel("Role: " + (currentUser.isAdmin() ? "Admin" : "Customer"));
        roleLabel.setForeground(TEXT_SECOND);

        JLabel fullNameLabel = new JLabel("Name:");
        fullNameLabel.setForeground(TEXT_MAIN);
        JTextField fullNameField = new JTextField(currentUser.getFullName());
        styleInputField(fullNameField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(TEXT_MAIN);
        JTextField emailField = new JTextField(currentUser.getEmail());
        styleInputField(emailField);

        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setForeground(TEXT_MAIN);
        JTextField addressField = new JTextField(currentUser.getAddress());
        styleInputField(addressField);

        JButton saveButton = createPrimaryButton("Save Changes");
        saveButton.addActionListener(e -> saveAccountInfo(fullNameField, emailField, addressField));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(usernameLabel, gbc);

        gbc.gridy = 1;
        panel.add(roleLabel, gbc);

        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel.add(fullNameLabel, gbc);
        gbc.gridx = 1;
        panel.add(fullNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(emailLabel, gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(addressLabel, gbc);
        gbc.gridx = 1;
        panel.add(addressField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(saveButton, gbc);

        return panel;
    }

    private JPanel createAdminBooksPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        panel.setBackground(BG_SECONDARY);

        adminBookListModel = new DefaultListModel<>();
        adminBookList = new JList<>(adminBookListModel);
        adminBookList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        adminBookList.setBackground(CARD);
        adminBookList.setForeground(TEXT_MAIN);
        adminBookList.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(adminBookList);

        JButton addButton = createPrimaryButton("Add Book");
        addButton.addActionListener(e -> showBookForm(null));
        JButton editButton = createSecondaryButton("Edit Selected");
        editButton.addActionListener(e -> showSelectedBookForm());
        JButton deleteButton = createSecondaryButton("Delete Selected");
        deleteButton.addActionListener(e -> deleteSelectedBook());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        refreshAdminBookList();
        return panel;
    }

    private JPanel createAdminOrdersPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        panel.setBackground(BG_SECONDARY);

        adminOrderListModel = new DefaultListModel<>();
        adminOrderList = new JList<>(adminOrderListModel);
        adminOrderList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        adminOrderList.setBackground(CARD);
        adminOrderList.setForeground(TEXT_MAIN);
        adminOrderList.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(adminOrderList);

        JButton updateStatusButton = createPrimaryButton("Update Status");
        updateStatusButton.addActionListener(e -> updateSelectedOrderStatus());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.add(updateStatusButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        refreshAdminOrderList();
        return panel;
    }

    private void refreshBrowseGrid() {
        if (browseBookPanel == null) {
            return;
        }
        browseBookPanel.removeAll();
        String query = searchField == null ? "" : searchField.getText().trim();
        if (query.equals(searchPlaceholder)) {
            query = "";
        }
        List<Book> found = service.searchBooks(query);
        for (Book book : found) {
            if (activeCategory != null && activeCategory.getId() != 0 && book.getCategory().getId() != activeCategory.getId()) {
                continue;
            }
            browseBookPanel.add(createBookCard(book));
        }
        if (browseBookPanel.getComponentCount() == 0) {
            browseBookPanel.add(createEmptyMessagePanel("No books found."));
        }
        browseBookPanel.revalidate();
        browseBookPanel.repaint();
    }

    private void refreshCheckoutPanel() {
        if (checkoutListModel == null || checkoutTotalLabel == null) {
            return;
        }
        checkoutListModel.clear();
        if (cart.isEmpty()) {
            checkoutListModel.addElement("Your cart is empty.");
            checkoutTotalLabel.setText("Order Total: $0.00");
            return;
        }
        for (CartItem item : cart.getItems()) {
            checkoutListModel.addElement(String.format("%s x%d - $%.2f", item.getBook().getTitle(), item.getQuantity(), item.getSubtotal()));
        }
        checkoutTotalLabel.setText(String.format("Order Total: $%.2f", cart.getTotal()));
    }

    private void refreshCartList() {
        cartListModel.clear();
        for (CartItem item : cart.getItems()) {
            cartListModel.addElement(String.format("%s x%d - $%.2f", item.getBook().getTitle(), item.getQuantity(), item.getSubtotal()));
        }
        cartTotalLabel.setText(String.format("Total: $%.2f", cart.getTotal()));
    }

    private void refreshOrderHistory() {
        orderHistoryModel.clear();
        if (!(currentUser instanceof Customer)) {
            return;
        }
        List<Order> orders = service.getOrdersForCustomer((Customer) currentUser);
        for (Order order : orders) {
            orderHistoryModel.addElement(String.format("Order %d: $%.2f - %s", order.getId(), order.getTotalAmount(), order.getStatus()));
        }
    }

    private void refreshAdminBookList() {
        adminBookListModel.clear();
        for (Book book : service.searchBooks("")) {
            adminBookListModel.addElement(book);
        }
    }

    private void refreshAdminOrderList() {
        adminOrderListModel.clear();
        for (Order order : service.getAllOrders()) {
            adminOrderListModel.addElement(String.format("Order %d - %s - $%.2f - %s", order.getId(), order.getCustomer().getFullName(), order.getTotalAmount(), order.getStatus()));
        }
    }

    private void addSelectedBookToCart(Book book) {
        if (book == null) {
            JOptionPane.showMessageDialog(this, "Please select a book first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            service.addBookToCart(cart, book.getId(), 1);
            refreshCartList();
            refreshCheckoutPanel();
            JOptionPane.showMessageDialog(this, "Added to cart.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private JPanel createBookCard(Book book) {
        JPanel card = new JPanel(new BorderLayout(8, 8));
        card.setBackground(CARD);
        card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(SECONDARY), BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        JLabel coverLabel = new JLabel(getBookCoverIcon(book));
        coverLabel.setHorizontalAlignment(SwingConstants.CENTER);
        coverLabel.setPreferredSize(new Dimension(160, 210));
        card.add(coverLabel, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel(new GridLayout(0, 1, 4, 4));
        infoPanel.setOpaque(false);
        JLabel titleLabel = new JLabel(String.format("<html><b>%s</b></html>", book.getTitle()));
        JLabel authorLabel = new JLabel(book.getAuthor());
        authorLabel.setForeground(TEXT_SECOND);
        JLabel priceLabel = new JLabel(String.format("$%.2f", book.getPrice()));
        priceLabel.setForeground(ACCENT);
        priceLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        JLabel stockLabel = new JLabel("Stock: " + book.getStock());
        stockLabel.setForeground(TEXT_SECOND);

        infoPanel.add(titleLabel);
        infoPanel.add(authorLabel);
        infoPanel.add(priceLabel);
        infoPanel.add(stockLabel);
        card.add(infoPanel, BorderLayout.CENTER);

        JButton detailsButton = createSecondaryButton("Details");
        detailsButton.addActionListener(e -> showBookDetailDialog(book));
        JButton addButton = createPrimaryButton("Add to Cart");
        addButton.addActionListener(e -> addSelectedBookToCart(book));

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        actionPanel.setOpaque(false);
        actionPanel.add(detailsButton);
        actionPanel.add(addButton);
        card.add(actionPanel, BorderLayout.SOUTH);

        return card;
    }

    private void showBookDetailDialog(Book book) {
        JPanel detailPanel = new JPanel(new BorderLayout(12, 12));
        detailPanel.setBackground(BG_SECONDARY);
        detailPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel title = new JLabel("<html><h2>" + book.getTitle() + "</h2></html>");
        title.setForeground(TEXT_MAIN);
        JLabel author = new JLabel("by " + book.getAuthor());
        author.setForeground(TEXT_SECOND);
        JLabel price = new JLabel(String.format("Price: $%.2f", book.getPrice()));
        price.setForeground(ACCENT);
        JLabel stock = new JLabel("Stock available: " + book.getStock());
        stock.setForeground(TEXT_SECOND);
        JTextArea description = new JTextArea(book.getDescription());
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setEditable(false);
        description.setBackground(BG_MAIN);
        description.setForeground(TEXT_MAIN);
        description.setFont(new Font("SansSerif", Font.PLAIN, 14));
        description.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(SECONDARY), BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        JPanel infoPanel = new JPanel(new GridLayout(0, 1, 8, 8));
        infoPanel.setOpaque(false);
        infoPanel.add(title);
        infoPanel.add(author);
        infoPanel.add(price);
        infoPanel.add(stock);

        detailPanel.add(infoPanel, BorderLayout.NORTH);
        detailPanel.add(new JScrollPane(description), BorderLayout.CENTER);

        int option = JOptionPane.showOptionDialog(this, detailPanel, "Book Details", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                new Object[] {"Add to Cart", "Close"}, "Add to Cart");
        if (option == 0) {
            addSelectedBookToCart(book);
        }
    }

    private void saveAccountInfo(JTextField fullNameField, JTextField emailField, JTextField addressField) {
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String address = addressField.getText().trim();
        if (fullName.isEmpty() || email.isEmpty() || address.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please complete all account fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        currentUser.setFullName(fullName);
        currentUser.setEmail(email);
        currentUser.setAddress(address);
        JOptionPane.showMessageDialog(this, "Account details updated successfully.", "Saved", JOptionPane.INFORMATION_MESSAGE);
    }

    private ImageIcon getBookCoverIcon(Book book) {
        String fileName = sanitizeFileName(book.getTitle()) + ".jpg";
        File imageFile = new File("src/images/" + fileName);
        ImageIcon icon = null;
        if (imageFile.exists()) {
            icon = new ImageIcon(imageFile.getPath());
        } else {
            icon = new ImageIcon();
        }
        Image image = icon.getImage();
        Image scaled = image.getScaledInstance(160, 210, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    private String sanitizeFileName(String title) {
        return title.toLowerCase().replaceAll("[^a-z0-9]", "");
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(PRIMARY);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        return button;
    }

    private JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(SECONDARY);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.PLAIN, 13));
        button.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        return button;
    }

    private JButton createAccentButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(ACCENT);
        button.setForeground(TEXT_MAIN);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        return button;
    }

    private void styleInputField(JTextField field) {
        field.setBackground(CARD);
        field.setForeground(TEXT_MAIN);
        field.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(SECONDARY), BorderFactory.createEmptyBorder(8, 8, 8, 8)));
    }

    private static class RoundedPanel extends JPanel {
        private final Color backgroundColor;
        private final int cornerRadius;

        public RoundedPanel(Color backgroundColor, int cornerRadius) {
            this.backgroundColor = backgroundColor;
            this.cornerRadius = cornerRadius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private JPanel createEmptyMessagePanel(String message) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        JLabel label = new JLabel(message, SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.ITALIC, 14));
        label.setForeground(TEXT_SECOND);
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    private void updateSelectedCartItemQuantity() {
        int index = cartList.getSelectedIndex();
        if (index < 0) {
            JOptionPane.showMessageDialog(this, "Please select an item to update.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        CartItem item = cart.getItems().get(index);
        String quantityText = JOptionPane.showInputDialog(this, "Quantity for " + item.getBook().getTitle() + ":", item.getQuantity());
        if (quantityText == null) {
            return;
        }
        try {
            int quantity = Integer.parseInt(quantityText.trim());
            if (quantity <= 0) {
                throw new NumberFormatException();
            }
            service.addBookToCart(cart, item.getBook().getId(), quantity - item.getQuantity());
            refreshCartList();
            refreshCheckoutPanel();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid quantity.", "Invalid Quantity", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void removeSelectedCartItem() {
        int index = cartList.getSelectedIndex();
        if (index < 0) {
            JOptionPane.showMessageDialog(this, "Please select an item to remove.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        CartItem item = cart.getItems().get(index);
        cart.removeItem(item.getBook().getId());
        refreshCartList();
    }

    private void handleGuiCheckout() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your cart is empty.", "Checkout", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String address = JOptionPane.showInputDialog(this, "Delivery address:", currentUser.getAddress());
        if (address == null || address.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Delivery address cannot be empty.", "Checkout", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            Order order = service.checkout(cart, (Customer) currentUser, address.trim());
            Invoice invoice = new Invoice(service.getNextInvoiceId(), order);
            refreshCartList();
            refreshOrderHistory();
            refreshAdminOrderList();
            JOptionPane.showMessageDialog(this, invoice.generateInvoiceText(), "Order Confirmed", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Checkout Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void showBookForm(Book bookToEdit) {
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField stockField = new JTextField();
        JComboBox<Category> categoryCombo = new JComboBox<>();
        JTextField descriptionField = new JTextField();

        for (Category category : service.getCategories()) {
            categoryCombo.addItem(category);
        }

        if (bookToEdit != null) {
            titleField.setText(bookToEdit.getTitle());
            authorField.setText(bookToEdit.getAuthor());
            priceField.setText(String.valueOf(bookToEdit.getPrice()));
            stockField.setText(String.valueOf(bookToEdit.getStock()));
            descriptionField.setText(bookToEdit.getDescription());
            categoryCombo.setSelectedItem(bookToEdit.getCategory());
        }

        Object[] form = {
                "Title:", titleField,
                "Author:", authorField,
                "Price:", priceField,
                "Stock:", stockField,
                "Category:", categoryCombo,
                "Description:", descriptionField
        };

        int result = JOptionPane.showConfirmDialog(this, form, bookToEdit == null ? "Add Book" : "Edit Book", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            double price = Double.parseDouble(priceField.getText().trim());
            int stock = Integer.parseInt(stockField.getText().trim());
            Category category = (Category) categoryCombo.getSelectedItem();
            String description = descriptionField.getText().trim();
            if (bookToEdit == null) {
                service.addBook(title, author, price, stock, category, description);
            } else {
                service.updateBook(bookToEdit.getId(), title, author, price, stock, category, description);
            }
            refreshAdminBookList();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid book data. " + ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void showSelectedBookForm() {
        Book book = adminBookList.getSelectedValue();
        if (book == null) {
            JOptionPane.showMessageDialog(this, "Please select a book to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        showBookForm(book);
    }

    private void deleteSelectedBook() {
        Book book = adminBookList.getSelectedValue();
        if (book == null) {
            JOptionPane.showMessageDialog(this, "Please select a book to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int choice = JOptionPane.showConfirmDialog(this, "Delete " + book.getTitle() + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            service.deleteBook(book.getId());
            refreshAdminBookList();
        }
    }

    private void updateSelectedOrderStatus() {
        int index = adminOrderList.getSelectedIndex();
        if (index < 0) {
            JOptionPane.showMessageDialog(this, "Please select an order.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Order order = service.getAllOrders().get(index);
        String status = JOptionPane.showInputDialog(this, "New status:", order.getStatus());
        if (status == null || status.trim().isEmpty()) {
            return;
        }
        try {
            service.updateOrderStatus(order.getId(), status.trim());
            refreshAdminOrderList();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void logout() {
        currentUser = null;
        cart.clear();
        cardLayout.show(cardPanel, "AUTH");
    }
}
