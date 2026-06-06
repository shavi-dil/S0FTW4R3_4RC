import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CartPanel extends JPanel {
    private final Cart cart;
    private final JTable cartTable;
    private final DefaultTableModel cartTableModel;
    private final JLabel subtotalLabel = new JLabel();
    private final JLabel totalLabel = new JLabel();
    private final Runnable refreshCallback;

    public CartPanel(Cart cart, Runnable refreshCallback) {
        this.cart = cart;
        this.refreshCallback = refreshCallback;

        setLayout(new BorderLayout(16, 16));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JLabel title = new JLabel("Your cart holds your selected books and quantities.");
        title.setForeground(Theme.TEXT_DARK);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        cartTableModel = new DefaultTableModel(new Object[]{"Title", "Qty", "Price", "Total"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        cartTable = new JTable(cartTableModel);
        cartTable.setRowHeight(28);
        JScrollPane tableScroll = new JScrollPane(cartTable);
        add(tableScroll, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout(12, 12));
        bottom.setOpaque(false);
        bottom.add(buildTotalsPanel(), BorderLayout.CENTER);
        bottom.add(buildActionsPanel(), BorderLayout.SOUTH);

        add(bottom, BorderLayout.SOUTH);
        refreshCartTable();
    }

    private JPanel buildTotalsPanel() {
        JPanel totals = new JPanel(new GridLayout(0, 1, 6, 6));
        totals.setOpaque(false);
        subtotalLabel.setForeground(Theme.TEXT_SECONDARY);
        totalLabel.setForeground(Theme.TEXT_DARK);
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        totals.add(subtotalLabel);
        totals.add(totalLabel);
        return totals;
    }

    private JPanel buildActionsPanel() {
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 12));
        actions.setOpaque(false);

        JButton removeButton = new JButton("Remove Selected");
        Theme.styleButton(removeButton);
        removeButton.addActionListener(e -> removeSelectedItem());

        JButton clearButton = new JButton("Clear Cart");
        Theme.styleButton(clearButton);
        clearButton.addActionListener(e -> {
            cart.clear();
            refreshCartTable();
            refreshCallback.run();
        });

        JButton refreshButton = new JButton("Refresh");
        Theme.styleButton(refreshButton);
        refreshButton.addActionListener(e -> refreshCartTable());

        JButton changeQtyButton = new JButton("Update Quantity");
        Theme.styleButton(changeQtyButton);
        changeQtyButton.addActionListener(e -> updateSelectedQuantity());

        actions.add(changeQtyButton);
        actions.add(removeButton);
        actions.add(clearButton);
        actions.add(refreshButton);
        return actions;
    }

    public void refreshCartTable() {
        cartTableModel.setRowCount(0);
        for (CartItem item : cart.getItems()) {
            cartTableModel.addRow(new Object[]{
                    item.getBook().getTitle(),
                    item.getQuantity(),
                    String.format("$%.2f", item.getBook().getPrice()),
                    String.format("$%.2f", item.getLineTotal())
            });
        }
        subtotalLabel.setText(String.format("Subtotal: $%.2f", cart.getSubtotal()));
        totalLabel.setText(String.format("Total (incl. shipping & tax): $%.2f", cart.getGrandTotal()));
    }

    private void removeSelectedItem() {
        int index = cartTable.getSelectedRow();
        if (index < 0) {
            JOptionPane.showMessageDialog(this, "Please select a cart item first.", "Remove Item", JOptionPane.WARNING_MESSAGE);
            return;
        }
        CartItem item = cart.getItems().get(index);
        cart.removeBook(item.getBook());
        refreshCartTable();
        refreshCallback.run();
    }

    private void updateSelectedQuantity() {
        int index = cartTable.getSelectedRow();
        if (index < 0) {
            JOptionPane.showMessageDialog(this, "Select an item to update quantity.", "Update Quantity", JOptionPane.WARNING_MESSAGE);
            return;
        }
        CartItem selected = cart.getItems().get(index);
        String result = JOptionPane.showInputDialog(this, "Enter new quantity for " + selected.getBook().getTitle() + ":",
                selected.getQuantity());
        if (result == null || result.trim().isEmpty()) {
            return;
        }
        try {
            int quantity = Integer.parseInt(result.trim());
            cart.updateQuantity(selected.getBook(), quantity);
            refreshCartTable();
            refreshCallback.run();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Invalid Quantity", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Update Quantity", JOptionPane.WARNING_MESSAGE);
        }
    }
}
