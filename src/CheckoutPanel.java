import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CheckoutPanel extends JPanel {
    private final Cart cart;
    private final BookStoreData data;
    private final Customer customer;
    private final Runnable refreshCallback;
    private final Runnable messageCallback;

    private final JTextField deliveryNameField = new JTextField();
    private final JTextField deliveryAddressField = new JTextField();
    private final JTextField cardNameField = new JTextField();
    private final JTextField cardNumberField = new JTextField();
    private final JTextArea invoiceArea = new JTextArea();

    public CheckoutPanel(Cart cart, BookStoreData data, Customer customer, Runnable refreshCallback, Runnable messageCallback) {
        this.cart = cart;
        this.data = data;
        this.customer = customer;
        this.refreshCallback = refreshCallback;
        this.messageCallback = messageCallback;

        setLayout(new BorderLayout(16, 16));
        setBorder(new EmptyBorder(8, 8, 8, 8));
        setOpaque(false);

        JPanel formSection = buildFormSection();
        add(formSection, BorderLayout.WEST);

        invoiceArea.setEditable(false);
        invoiceArea.setBackground(Theme.PANEL_WHITE);
        invoiceArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 0, 0, 30)),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));
        invoiceArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        add(new JScrollPane(invoiceArea), BorderLayout.CENTER);
    }

    private JPanel buildFormSection() {
        JPanel form = new JPanel(new GridLayout(0, 1, 14, 14));
        form.setOpaque(false);

        form.add(buildField("Name for Delivery", deliveryNameField));
        form.add(buildField("Delivery Address", deliveryAddressField));
        form.add(buildField("Cardholder Name", cardNameField));
        form.add(buildField("Card Number", cardNumberField));

        JButton checkoutButton = new JButton("Place Order");
        Theme.styleButton(checkoutButton);
        checkoutButton.addActionListener(e -> placeOrder());

        JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionRow.setOpaque(false);
        actionRow.add(checkoutButton);
        form.add(actionRow);
        return form;
    }

    private JPanel buildField(String labelText, JComponent field) {
        JPanel wrapper = new JPanel(new BorderLayout(6, 6));
        wrapper.setOpaque(false);
        JLabel label = new JLabel(labelText);
        label.setForeground(Theme.TEXT_SECONDARY);
        wrapper.add(label, BorderLayout.NORTH);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 0, 0, 35)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        wrapper.add(field, BorderLayout.CENTER);
        return wrapper;
    }

    private void placeOrder() {
        try {
            if (cart.isEmpty()) {
                throw new IllegalArgumentException("Your cart is empty. Add books before checkout.");
            }
            if (customer.getEmail().contains("guest")) {
                throw new IllegalArgumentException("Please register or login to complete your purchase.");
            }
            if (deliveryNameField.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("Delivery name is required.");
            }
            if (deliveryAddressField.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("Delivery address is required.");
            }
            Payment.validate(cardNameField.getText(), cardNumberField.getText());

            Order order = data.placeOrder(customer, cart, deliveryAddressField.getText().trim());
            invoiceArea.setText(new Invoice(order).buildReceiptText());
            cart.clear();
            refreshCallback.run();
            messageCallback.run();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Checkout Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void refreshInvoice() {
        if (!cart.isEmpty()) {
            invoiceArea.setText("Ready to place your purchase. Your total is $" + String.format("%.2f", cart.getGrandTotal()));
        } else {
            invoiceArea.setText("Your invoice summary will appear here after checkout.");
        }
    }
}
