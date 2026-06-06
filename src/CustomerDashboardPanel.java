import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class CustomerDashboardPanel extends JPanel {
    private final BookStoreData data;
    private final Cart cart;
    private final Customer customer;
    private final Runnable showMessageCallback;

    private final JPanel bookGridPanel = new JPanel();
    private final JTextField searchField = new JTextField();
    private final JLabel categoryLabel = new JLabel("All Books");
    private String selectedCategory = "All";
    private final JLabel welcomeLabel = new JLabel();
    private final CartPanel cartPanel;
    private final CheckoutPanel checkoutPanel;
    private final OrderHistoryPanel orderHistoryPanel;

    public CustomerDashboardPanel(BookStoreData data, Cart cart, Customer customer,
                                  Runnable refreshAllCallback, Runnable showMessageCallback) {
        this.data = data;
        this.cart = cart;
        this.customer = customer;
        this.showMessageCallback = showMessageCallback;

        setLayout(new BorderLayout(16, 16));
        setBackground(Theme.WARM_CREAM);
        setBorder(new EmptyBorder(16, 16, 16, 16));

        cartPanel = new CartPanel(cart, refreshAllCallback);
        checkoutPanel = new CheckoutPanel(cart, data, customer, refreshAllCallback, showMessageCallback);
        orderHistoryPanel = new OrderHistoryPanel(customer);

        add(buildHeroPanel(), BorderLayout.NORTH);
        add(buildDashboardTabs(), BorderLayout.CENTER);
        refreshBrowseResults();
    }

    private JPanel buildHeroPanel() {
        JPanel hero = new JPanel(new BorderLayout(12, 12));
        hero.setOpaque(false);

        JPanel textPanel = new JPanel(new GridLayout(0, 1, 6, 6));
        textPanel.setOpaque(false);
        
        JLabel mainTitle = new JLabel("Great Reads, Great Prices");
        mainTitle.setFont(new Font("SansSerif", Font.BOLD, 32));
        mainTitle.setForeground(Theme.TEXT_DARK);
        textPanel.add(mainTitle);

        JLabel welcomeSubtitle = new JLabel("Welcome, " + customer.getName() + " — Discover your next favourite read.");
        welcomeSubtitle.setForeground(Theme.TEXT_SECONDARY);
        welcomeSubtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        textPanel.add(welcomeSubtitle);

        hero.add(textPanel, BorderLayout.NORTH);
        hero.add(buildSearchAndCategoryPanel(), BorderLayout.SOUTH);
        return hero;
    }

    private JPanel buildSearchAndCategoryPanel() {
        JPanel container = new JPanel(new BorderLayout(12, 12));
        container.setOpaque(false);

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        searchPanel.setOpaque(false);

        searchField.setPreferredSize(new Dimension(360, 42));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 0, 0, 40)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JButton searchButton = new JButton("Search");
        Theme.styleButton(searchButton);
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.addActionListener(e -> refreshBrowseResults());

        JButton resetButton = new JButton("Reset");
        Theme.styleButton(resetButton);
        resetButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        resetButton.addActionListener(e -> {
            searchField.setText("");
            selectedCategory = "All";
            categoryLabel.setText("All Books");
            refreshBrowseResults();
        });

        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(resetButton);
        container.add(searchPanel, BorderLayout.NORTH);

        // Category navigation
        JPanel categoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        categoryPanel.setOpaque(false);
        
        JLabel categoryTitle = new JLabel("Browse by Category:");
        categoryTitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        categoryTitle.setForeground(Theme.TEXT_SECONDARY);
        categoryPanel.add(categoryTitle);

        List<String> categories = data.getCategories();
        for (String category : categories) {
            JButton categoryButton = new JButton(category);
            categoryButton.setPreferredSize(new Dimension(120, 36));
            categoryButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
            categoryButton.setForeground(Theme.PANEL_WHITE);
            categoryButton.setBackground(category.equals("All") ? Theme.DUSTY_BLUE : Theme.SOFT_BEIGE);
            categoryButton.setForeground(category.equals("All") ? Theme.PANEL_WHITE : Theme.TEXT_DARK);
            categoryButton.setBorder(BorderFactory.createLineBorder(Theme.TEXT_SECONDARY, 1));
            categoryButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            
            categoryButton.addActionListener(e -> {
                selectedCategory = category;
                categoryLabel.setText(category + " Books");
                
                // Update button styles
                for (Component comp : categoryPanel.getComponents()) {
                    if (comp instanceof JButton) {
                        JButton btn = (JButton) comp;
                        btn.setBackground(btn.getText().equals(category) ? Theme.DUSTY_BLUE : Theme.SOFT_BEIGE);
                        btn.setForeground(btn.getText().equals(category) ? Theme.PANEL_WHITE : Theme.TEXT_DARK);
                    }
                }
                refreshBrowseResults();
            });
            categoryButton.setName(category);
            categoryPanel.add(categoryButton);
        }
        
        container.add(categoryPanel, BorderLayout.SOUTH);
        return container;
    }

    private JTabbedPane buildDashboardTabs() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(Theme.WARM_CREAM);
        tabs.setForeground(Theme.TEXT_DARK);

        tabs.addTab("Browse Books", buildBrowsePanel());
        tabs.addTab("Shopping Cart", cartPanel);
        tabs.addTab("Checkout", checkoutPanel);
        tabs.addTab("Order History", orderHistoryPanel);

        return tabs;
    }

    private JPanel buildBrowsePanel() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setOpaque(false);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titlePanel.setOpaque(false);
        categoryLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        categoryLabel.setForeground(Theme.TEXT_DARK);
        titlePanel.add(categoryLabel);
        panel.add(titlePanel, BorderLayout.NORTH);

        bookGridPanel.setOpaque(false);
        bookGridPanel.setLayout(new GridLayout(0, 3, 18, 18));
        JScrollPane scrollPane = new JScrollPane(bookGridPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Theme.WARM_CREAM);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    public void refreshBrowseResults() {
        bookGridPanel.removeAll();
        List<Book> matching = data.searchBooks(searchField.getText(), selectedCategory);
        if (matching.isEmpty()) {
            JLabel empty = new JLabel("No books found. Try adjusting your search or category filter.");
            empty.setForeground(Theme.TEXT_SECONDARY);
            empty.setFont(new Font("SansSerif", Font.ITALIC, 14));
            bookGridPanel.add(empty);
        } else {
            matching.forEach(book -> bookGridPanel.add(new BookCardPanel(book, cart, () -> {
                cartPanel.refreshCartTable();
                checkoutPanel.refreshInvoice();
            }, () -> showMessageCallback.run())));
        }
        bookGridPanel.revalidate();
        bookGridPanel.repaint();
    }

    public void refreshAllPanels() {
        selectedCategory = "All";
        categoryLabel.setText("All Books");
        refreshBrowseResults();
        cartPanel.refreshCartTable();
        checkoutPanel.refreshInvoice();
        orderHistoryPanel.refreshOrderHistory();
    }
}
