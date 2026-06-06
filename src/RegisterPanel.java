import javax.swing.*;
import java.awt.*;

public class RegisterPanel extends JPanel {
    private final JTextField nameField;
    private final JTextField emailField;
    private final JTextField phoneField;
    private final JTextField addressField;
    private final JPasswordField passwordField;
    private final JPasswordField confirmPasswordField;
    private final JButton submitButton;
    private final JPanel togglePanel;

    public RegisterPanel() {
        setLayout(new BorderLayout());
        setBackground(Theme.DUSTY_BLUE);

        // Center the card-style form
        JPanel centerContainer = new JPanel(new GridBagLayout());
        centerContainer.setOpaque(false);
        centerContainer.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JPanel card = new JPanel(new BorderLayout(0, 16));
        card.setBackground(Theme.PANEL_WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 0, 0, 30), 1),
                BorderFactory.createEmptyBorder(20, 28, 32, 28)));
        card.setMaximumSize(new Dimension(540, Integer.MAX_VALUE));

        // Title
        JLabel title = new JLabel("Create your account");
        title.setForeground(Theme.TEXT_BLACK);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        card.add(title, BorderLayout.NORTH);

        // Form fields
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setOpaque(false);

        JPanel form = new JPanel(new GridLayout(6, 1, 0, 10));
        form.setOpaque(false);

        nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(340, 38));
        emailField = new JTextField();
        emailField.setPreferredSize(new Dimension(340, 38));
        phoneField = new JTextField();
        phoneField.setPreferredSize(new Dimension(340, 38));
        addressField = new JTextField();
        addressField.setPreferredSize(new Dimension(340, 38));
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(340, 38));
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setPreferredSize(new Dimension(340, 38));

        form.add(buildField("Full Name", nameField));
        form.add(buildField("Email", emailField));
        form.add(buildField("Phone", phoneField));
        form.add(buildField("Delivery Address", addressField));
        form.add(buildField("Password", passwordField));
        form.add(buildField("Confirm Password", confirmPasswordField));

        formContainer.add(form, BorderLayout.NORTH);
        card.add(formContainer, BorderLayout.CENTER);

        // Buttons and toggle
        JPanel bottomPanel = new JPanel(new BorderLayout(0, 12));
        bottomPanel.setOpaque(false);

        submitButton = new JButton("Register");
        submitButton.setBackground(Theme.NAVY_BLUE);
        submitButton.setForeground(Theme.TEXT_BLACK);
        submitButton.setFocusPainted(false);
        submitButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        submitButton.setPreferredSize(new Dimension(150, 40));
        submitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        submitButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        submitButton.setOpaque(true);

        // Add hover effect
        submitButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                submitButton.setBackground(Theme.DARK_NAVY);
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                submitButton.setBackground(Theme.NAVY_BLUE);
            }
        });

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        actionPanel.setOpaque(false);
        actionPanel.add(submitButton);
        bottomPanel.add(actionPanel, BorderLayout.NORTH);

        togglePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        togglePanel.setOpaque(false);
        bottomPanel.add(togglePanel, BorderLayout.SOUTH);

        card.add(bottomPanel, BorderLayout.SOUTH);

        centerContainer.add(card);

        JScrollPane scrollPane = new JScrollPane(centerContainer,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getViewport().setBackground(Theme.DUSTY_BLUE);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void setToggleAction(String toggleText, Runnable action) {
        JLabel label = new JLabel("Already have an account? ");
        label.setForeground(Theme.TEXT_DARK);
        label.setFont(new Font("SansSerif", Font.PLAIN, 12));
        togglePanel.add(label);

        JButton toggleButton = new JButton(toggleText);
        toggleButton.setForeground(Theme.NAVY_BLUE);
        toggleButton.setBorderPainted(false);
        toggleButton.setContentAreaFilled(false);
        toggleButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        toggleButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
        toggleButton.addActionListener(e -> action.run());
        togglePanel.add(toggleButton);
    }

    private JPanel buildField(String labelText, JComponent field) {
        JPanel panel = new JPanel(new BorderLayout(0, 4));
        panel.setOpaque(false);
        JLabel label = new JLabel(labelText);
        label.setForeground(Theme.TEXT_DARK);
        label.setFont(new Font("SansSerif", Font.PLAIN, 11));
        panel.add(label, BorderLayout.NORTH);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 0, 0, 60), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        field.setFont(new Font("SansSerif", Font.PLAIN, 12));
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    public String getNameValue() {
        return nameField.getText().trim();
    }

    public String getEmailValue() {
        return emailField.getText().trim();
    }

    public String getPhoneValue() {
        return phoneField.getText().trim();
    }

    public String getAddressValue() {
        return addressField.getText().trim();
    }

    public String getPasswordValue() {
        return new String(passwordField.getPassword()).trim();
    }

    public String getConfirmPasswordValue() {
        return new String(confirmPasswordField.getPassword()).trim();
    }

    public void setSubmitAction(Runnable action) {
        submitButton.addActionListener(e -> action.run());
    }

    public void resetFields() {
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        addressField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
    }
}
