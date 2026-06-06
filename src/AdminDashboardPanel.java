import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminDashboardPanel extends JPanel {
    private final BookStoreData data;
    private DefaultTableModel bookModel;
    private DefaultTableModel orderModel;
    private JTable bookTable;
    private JTable orderTable;
    private final JTextField titleField = new JTextField();
    private final JTextField authorField = new JTextField();
    private final JTextField categoryField = new JTextField();
    private final JTextField priceField = new JTextField();
    private final JTextField stockField = new JTextField();
    private final JTextField imageField = new JTextField();

    public AdminDashboardPanel(BookStoreData data) {
        this.data = data;
        setLayout(new BorderLayout(16, 16));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JLabel title = new JLabel("Admin control centre for inventory and orders");
        title.setForeground(Theme.TEXT_DARK);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Book Catalogue", buildBookManagementPanel());
        tabs.addTab("Order Management", buildOrderManagementPanel());
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel buildBookManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setOpaque(false);

        bookModel = new DefaultTableModel(new Object[]{"ID", "Title", "Author", "Category", "Price", "Stock", "Image"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bookTable = new JTable(bookModel);
        bookTable.setRowHeight(28);
        bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookTable.getSelectionModel().addListSelectionListener(e -> populateBookForm());
        panel.add(new JScrollPane(bookTable), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridLayout(0, 1, 10, 10));
        form.setOpaque(false);
        form.setBorder(BorderFactory.createTitledBorder("Add or update book"));

        form.add(buildField("Title", titleField));
        form.add(buildField("Author", authorField));
        form.add(buildField("Category", categoryField));
        form.add(buildField("Price", priceField));
        form.add(buildField("Stock", stockField));
        form.add(buildField("Image file name", imageField));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        actions.setOpaque(false);

        JButton addButton = new JButton("Add Book");
        Theme.styleButton(addButton);
        addButton.addActionListener(e -> addBook());

        JButton updateButton = new JButton("Update Book");
        Theme.styleButton(updateButton);
        updateButton.addActionListener(e -> updateBook());

        JButton deleteButton = new JButton("Delete Book");
        Theme.styleButton(deleteButton);
        deleteButton.addActionListener(e -> deleteBook());

        actions.add(addButton);
        actions.add(updateButton);
        actions.add(deleteButton);
        form.add(actions);

        panel.add(form, BorderLayout.SOUTH);
        refreshBookTable();
        return panel;
    }

    private JPanel buildOrderManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setOpaque(false);

        orderModel = new DefaultTableModel(new Object[]{"Order #", "Customer", "Total", "Status", "Shipment"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        orderTable = new JTable(orderModel);
        orderTable.setRowHeight(28);
        panel.add(new JScrollPane(orderTable), BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        actions.setOpaque(false);

        JButton statusButton = new JButton("Mark Prepared");
        Theme.styleButton(statusButton);
        statusButton.addActionListener(e -> updateOrderStatus("Prepared"));

        JButton shipButton = new JButton("Mark Shipped");
        Theme.styleButton(shipButton);
        shipButton.addActionListener(e -> updateOrderStatus("Shipped"));

        actions.add(statusButton);
        actions.add(shipButton);
        panel.add(actions, BorderLayout.SOUTH);

        refreshOrderTable();
        return panel;
    }

    private JPanel buildField(String labelText, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout(4, 4));
        panel.setOpaque(false);
        JLabel label = new JLabel(labelText);
        label.setForeground(Theme.TEXT_SECONDARY);
        panel.add(label, BorderLayout.NORTH);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 0, 0, 30)),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    private void addBook() {
        try {
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String category = categoryField.getText().trim();
            double price = Double.parseDouble(priceField.getText().trim());
            int stock = Integer.parseInt(stockField.getText().trim());
            String imageName = imageField.getText().trim();
            if (title.isEmpty() || author.isEmpty() || category.isEmpty() || imageName.isEmpty()) {
                throw new IllegalArgumentException("All book fields are required.");
            }
            int nextId = data.getBooks().stream().mapToInt(Book::getId).max().orElse(0) + 1;
            data.addBook(new Book(nextId, title, author, category, "Added by admin.", price, stock, imageName));
            refreshBookTable();
            JOptionPane.showMessageDialog(this, "Book added successfully.", "Book Management", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Price and stock must be valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void updateBook() {
        int row = bookTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a book from the table to update.", "Book Management", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Book selected = data.getBooks().get(row);
        try {
            selected.setTitle(titleField.getText().trim());
            selected.setAuthor(authorField.getText().trim());
            selected.setCategory(categoryField.getText().trim());
            selected.setPrice(Double.parseDouble(priceField.getText().trim()));
            selected.setStock(Integer.parseInt(stockField.getText().trim()));
            selected.setImageFileName(imageField.getText().trim());
            refreshBookTable();
            JOptionPane.showMessageDialog(this, "Book updated successfully.", "Book Management", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Price and stock must be valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteBook() {
        int row = bookTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a book from the table to delete.", "Book Management", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Book selected = data.getBooks().get(row);
        data.deleteBook(selected);
        refreshBookTable();
        clearFormFields();
        JOptionPane.showMessageDialog(this, "Book deleted successfully.", "Book Management", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateOrderStatus(String newStatus) {
        int row = orderTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Choose an order before updating status.", "Order Management", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Order order = data.getOrders().get(row);
        order.setStatus(newStatus);
        order.getShipment().setStatus(newStatus);
        refreshOrderTable();
    }

    private void populateBookForm() {
        int row = bookTable.getSelectedRow();
        if (row < 0) {
            return;
        }
        Book selected = data.getBooks().get(row);
        titleField.setText(selected.getTitle());
        authorField.setText(selected.getAuthor());
        categoryField.setText(selected.getCategory());
        priceField.setText(String.format("%.2f", selected.getPrice()));
        stockField.setText(String.valueOf(selected.getStock()));
        imageField.setText(selected.getImageFileName());
    }

    private void clearFormFields() {
        titleField.setText("");
        authorField.setText("");
        categoryField.setText("");
        priceField.setText("");
        stockField.setText("");
        imageField.setText("");
    }

    private void refreshBookTable() {
        bookModel.setRowCount(0);
        for (Book book : data.getBooks()) {
            bookModel.addRow(new Object[]{
                    book.getId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getCategory(),
                    String.format("$%.2f", book.getPrice()),
                    book.getStock(),
                    book.getImageFileName()
            });
        }
    }

    private void refreshOrderTable() {
        orderModel.setRowCount(0);
        for (Order order : data.getOrders()) {
            orderModel.addRow(new Object[]{
                    order.getOrderNumber(),
                    order.getCustomer().getName(),
                    String.format("$%.2f", order.getGrandTotal()),
                    order.getStatus(),
                    order.getShipment().getStatus()
            });
        }
    }
}
