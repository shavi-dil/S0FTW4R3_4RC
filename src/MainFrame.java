import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final BookStoreData data = BookStoreData.getInstance();
    private final Cart cart = new Cart();
    private Customer currentCustomer;
    private Admin currentAdmin;

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cards = new JPanel(cardLayout);
    private final JLabel statusLabel = new JLabel("Please login or register to begin.");
    private CustomerDashboardPanel customerPanel;
    private AdminDashboardPanel adminPanel;

    public MainFrame() {
        setTitle("Favourite Books");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1280, 820);
        setLocationRelativeTo(null);
        setBackground(Theme.WARM_CREAM);
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout());
        add(buildNavbar(), BorderLayout.NORTH);

        cards.add(buildWelcomePanel(), "WELCOME");
        cards.add(new JPanel(), "CUSTOMER");
        cards.add(new JPanel(), "ADMIN");
        add(cards, BorderLayout.CENTER);

        cardLayout.show(cards, "WELCOME");
    }

    private JPanel buildNavbar() {
        JPanel navbar = new JPanel(new BorderLayout());
        navbar.setBackground(Theme.DUSTY_BLUE);
        navbar.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));

        JLabel title = new JLabel("Favourite Books");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        navbar.add(title, BorderLayout.WEST);

        JButton backButton = new JButton("Home");
        Theme.styleButton(backButton);
        backButton.addActionListener(e -> showWelcome());

        JButton logoutButton = new JButton("Logout");
        Theme.styleButton(logoutButton);
        logoutButton.addActionListener(e -> logout());

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setOpaque(false);
        actions.add(backButton);
        actions.add(logoutButton);
        navbar.add(actions, BorderLayout.EAST);

        statusLabel.setForeground(Color.WHITE);
        navbar.add(statusLabel, BorderLayout.SOUTH);
        return navbar;
    }

    private JPanel buildWelcomePanel() {
        JPanel welcomePanel = new JPanel(new BorderLayout(16, 16));
        welcomePanel.setBackground(Theme.WARM_CREAM);
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JPanel hero = new JPanel(new BorderLayout(16, 16));
        hero.setBackground(Theme.SOFT_BEIGE);
        hero.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JLabel heading = new JLabel("A modern desktop bookstore experience");
        heading.setForeground(Theme.TEXT_DARK);
        heading.setFont(new Font("SansSerif", Font.BOLD, 32));
        hero.add(heading, BorderLayout.NORTH);

        JLabel summary = new JLabel(
                "Browse curated books, manage your cart, and place orders with a clean Favourite Books theme.");
        summary.setForeground(Theme.TEXT_SECONDARY);
        summary.setFont(new Font("SansSerif", Font.PLAIN, 18));
        hero.add(summary, BorderLayout.CENTER);

        JPanel actionButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        actionButtons.setOpaque(false);

        JButton browseBtn = new JButton("Browse Books");
        Theme.styleButton(browseBtn);
        browseBtn.setBackground(Theme.SOFT_MUSTARD);
        browseBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JButton customerLoginBtn = new JButton("Customer Login");
        Theme.styleButton(customerLoginBtn);
        customerLoginBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JButton registerBtn = new JButton("Register");
        Theme.styleButton(registerBtn);
        registerBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JButton adminLoginBtn = new JButton("Admin Login");
        Theme.styleButton(adminLoginBtn);
        adminLoginBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        browseBtn.addActionListener(e -> browseBooksAsGuest());
        actionButtons.add(browseBtn);
        actionButtons.add(customerLoginBtn);
        actionButtons.add(registerBtn);
        actionButtons.add(adminLoginBtn);
        hero.add(actionButtons, BorderLayout.SOUTH);

        welcomePanel.add(hero, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setOpaque(true);
        tabs.setBackground(Theme.WARM_CREAM);

        LoginPanel customerLogin = new LoginPanel("Customer login", "Sign in");
        customerLogin.setSubmitAction(() -> handleCustomerLogin(customerLogin));
        customerLogin.setToggleAction("Don't have an account? Register here", () -> tabs.setSelectedIndex(1));

        RegisterPanel registerPanel = new RegisterPanel();
        registerPanel.setSubmitAction(() -> handleRegister(registerPanel));
        registerPanel.setToggleAction("Already have an account? Login here", () -> tabs.setSelectedIndex(0));

        LoginPanel adminLogin = new LoginPanel("Admin login", "Sign in as admin");
        adminLogin.setSubmitAction(() -> handleAdminLogin(adminLogin));

        tabs.addTab("Customer Login", customerLogin);
        tabs.addTab("Register", registerPanel);
        tabs.addTab("Admin Login", adminLogin);

        customerLoginBtn.addActionListener(e -> tabs.setSelectedIndex(0));
        registerBtn.addActionListener(e -> tabs.setSelectedIndex(1));
        adminLoginBtn.addActionListener(e -> tabs.setSelectedIndex(2));

        welcomePanel.add(tabs, BorderLayout.CENTER);
        return welcomePanel;
    }

    private void handleCustomerLogin(LoginPanel loginPanel) {
        String email = loginPanel.getEmail();
        String password = loginPanel.getPassword();
        if (email.isEmpty() || password.isEmpty()) {
            showError("Email and password are required.");
            return;
        }
        Customer customer = data.findCustomerByEmail(email);
        if (customer == null || !customer.getPassword().equals(password)) {
            showError("Wrong email or password. Please try again.");
            return;
        }
        currentCustomer = customer;
        statusLabel.setText("Logged in as: " + customer.getName());
        showInfo("Login successful. Welcome to Favourite Books!");
        loginPanel.resetFields();
        customerDashboard();
    }

    private void handleAdminLogin(LoginPanel loginPanel) {
        String email = loginPanel.getEmail();
        String password = loginPanel.getPassword();
        if (email.isEmpty() || password.isEmpty()) {
            showError("Email and password are required.");
            return;
        }
        Admin admin = data.findAdminByEmail(email);
        if (admin == null || !admin.getPassword().equals(password)) {
            showError("Wrong admin login details. Please check your email and password.");
            return;
        }
        currentAdmin = admin;
        statusLabel.setText("Logged in as admin: " + admin.getName());
        showInfo("Admin login successful. Welcome to the Admin Dashboard!");
        adminDashboard();
    }

    private void handleRegister(RegisterPanel registerPanel) {
        String name = registerPanel.getNameValue();
        String email = registerPanel.getEmailValue();
        String phone = registerPanel.getPhoneValue();
        String address = registerPanel.getAddressValue();
        String password = registerPanel.getPasswordValue();
        String confirmPassword = registerPanel.getConfirmPasswordValue();

        if (name.isEmpty()) {
            showError("Full name cannot be blank.");
            return;
        }
        if (email.isEmpty()) {
            showError("Email cannot be blank.");
            return;
        }
        if (!email.contains("@")) {
            showError("Email must be a valid email address.");
            return;
        }
        if (phone.isEmpty()) {
            showError("Phone number cannot be blank.");
            return;
        }
        if (address.isEmpty()) {
            showError("Delivery address cannot be blank.");
            return;
        }
        if (password.isEmpty()) {
            showError("Password cannot be blank.");
            return;
        }
        if (confirmPassword.isEmpty()) {
            showError("Please confirm your password.");
            return;
        }
        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match.");
            return;
        }
        if (data.findCustomerByEmail(email) != null) {
            showError("An account with this email already exists.");
            return;
        }
        Customer newCustomer = new Customer(name, email, password, phone, address);
        data.addCustomer(newCustomer);
        currentCustomer = newCustomer;
        showInfo("Account created successfully. Welcome to Favourite Books!");
        registerPanel.resetFields();
        statusLabel.setText("Logged in as: " + newCustomer.getName());
        customerDashboard();
    }

    private void customerDashboard() {
        if (customerPanel == null) {
            customerPanel = new CustomerDashboardPanel(data, cart, currentCustomer,
                    this::refreshDashboard, this::showSuccessMessage);
            cards.add(customerPanel, "CUSTOMER");
        } else {
            customerPanel.refreshAllPanels();
        }
        cardLayout.show(cards, "CUSTOMER");
    }

    private void adminDashboard() {
        if (adminPanel == null) {
            adminPanel = new AdminDashboardPanel(data);
            cards.add(adminPanel, "ADMIN");
        }
        cardLayout.show(cards, "ADMIN");
    }

    private void showWelcome() {
        cardLayout.show(cards, "WELCOME");
        statusLabel.setText("Please login or register to begin.");
    }

    private void logout() {
        currentCustomer = null;
        currentAdmin = null;
        cart.clear();
        showWelcome();
    }

    private void refreshDashboard() {
        if (customerPanel != null) {
            customerPanel.refreshAllPanels();
        }
    }

    private void browseBooksAsGuest() {
        Customer guestCustomer = new Customer("Guest User", "guest@favouritebooks.com", "guest",
                "0412345678", "Guest Address");
        currentCustomer = guestCustomer;
        statusLabel.setText("Browsing as guest — Create an account to checkout");
        customerDashboard();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Favourite Books", JOptionPane.WARNING_MESSAGE);
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Favourite Books", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showSuccessMessage() {
        JOptionPane.showMessageDialog(this, "Action completed successfully.", "Favourite Books",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
