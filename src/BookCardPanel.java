import javax.swing.*;
import java.awt.*;

public class BookCardPanel extends JPanel {
    public BookCardPanel(Book book, Cart cart, Runnable cartChanged, Runnable showMessage) {
        setLayout(new BorderLayout(10, 10));
        setOpaque(true);
        setBackground(Theme.PANEL_WHITE);
        Theme.styleCard(this);

        // Top panel with cover and category badge
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel coverLabel = new JLabel();
        coverLabel.setHorizontalAlignment(SwingConstants.CENTER);
        coverLabel.setIcon(Theme.loadBookCover(book.getImageFileName(), 140, 220));
        topPanel.add(coverLabel, BorderLayout.CENTER);

        // Category badge
        JLabel badgeLabel = new JLabel(book.getCategory());
        badgeLabel.setForeground(Theme.PANEL_WHITE);
        badgeLabel.setBackground(Theme.DUSTY_BLUE);
        badgeLabel.setOpaque(true);
        badgeLabel.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        badgeLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
        badgeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel badgePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        badgePanel.setOpaque(false);
        badgePanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 0, 0));
        badgePanel.add(badgeLabel);
        topPanel.add(badgePanel, BorderLayout.NORTH);

        add(topPanel, BorderLayout.NORTH);

        // Info panel
        JPanel info = new JPanel(new GridLayout(0, 1, 6, 6));
        info.setOpaque(false);

        JLabel titleLabel = new JLabel(book.getTitle());
        titleLabel.setForeground(Theme.TEXT_DARK);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        info.add(titleLabel);

        JLabel authorLabel = new JLabel("by " + book.getAuthor());
        authorLabel.setForeground(Theme.TEXT_SECONDARY);
        authorLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        info.add(authorLabel);

        // Format label
        JLabel formatLabel = new JLabel("Paperback • " + (int) (Math.random() * 200 + 200) + " pages");
        formatLabel.setForeground(Theme.TEXT_SECONDARY);
        formatLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        info.add(formatLabel);

        JLabel priceLabel = new JLabel(String.format("$%.2f", book.getPrice()));
        priceLabel.setForeground(Theme.SOFT_MUSTARD);
        priceLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        info.add(priceLabel);

        JLabel stockLabel = new JLabel(book.isAvailable() ? "In stock: " + book.getStock() : "Out of stock");
        stockLabel.setForeground(book.isAvailable() ? Theme.SUCCESS_GREEN : Theme.ERROR_CORAL);
        stockLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        info.add(stockLabel);

        add(info, BorderLayout.CENTER);

        // Footer with quantity and add button
        JPanel footer = new JPanel(new BorderLayout(6, 6));
        footer.setOpaque(false);

        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, Math.max(1, book.getStock()), 1);
        JSpinner quantitySpinner = new JSpinner(spinnerModel);
        quantitySpinner.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        quantitySpinner.setPreferredSize(new Dimension(60, 28));
        footer.add(quantitySpinner, BorderLayout.WEST);

        JButton addButton = new JButton("Add to Cart");
        Theme.styleButton(addButton);
        addButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addButton.setEnabled(book.isAvailable());
        addButton.addActionListener(e -> {
            try {
                int quantity = (Integer) quantitySpinner.getValue();
                cart.addBook(book, quantity);
                showMessage.run();
                cartChanged.run();
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Add to Cart", JOptionPane.WARNING_MESSAGE);
            }
        });
        footer.add(addButton, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
    }
}
