import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private final JTextField emailField;
    private final JPasswordField passwordField;
    private final JButton submitButton;
    private final JPanel togglePanel;

    public LoginPanel(String titleText, String buttonText) {
        setLayout(new BorderLayout());
        setBackground(Theme.DUSTY_BLUE);

        // Center the card-style form
        JPanel centerContainer = new JPanel(new GridBagLayout());
        centerContainer.setOpaque(false);

        JPanel card = new JPanel(new BorderLayout(0, 16));
        card.setBackground(Theme.PANEL_WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 0, 0, 30), 1),
                BorderFactory.createEmptyBorder(24, 32, 24, 32)
        ));
        card.setPreferredSize(new Dimension(420, 320));

        // Title
        JLabel title = new JLabel(titleText);
        title.setForeground(Theme.TEXT_BLACK);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        card.add(title, BorderLayout.NORTH);

        // Form fields
        JPanel form = new JPanel(new GridLayout(2, 1, 0, 14));
        form.setOpaque(false);

        emailField = new JTextField();
        emailField.setPreferredSize(new Dimension(360, 38));
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(360, 38));

        form.add(buildField("Email", emailField));
        form.add(buildField("Password", passwordField));

        card.add(form, BorderLayout.CENTER);

        // Buttons and toggle
        JPanel bottomPanel = new JPanel(new BorderLayout(0, 12));
        bottomPanel.setOpaque(false);

        submitButton = new JButton(buttonText);
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
        add(centerContainer, BorderLayout.CENTER);
    }

    public void setToggleAction(String toggleText, Runnable action) {
        JLabel label = new JLabel("Don't have an account? ");
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
        label.setFont(new Font("SansSerif", Font.PLAIN, 12));
        panel.add(label, BorderLayout.NORTH);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 0, 0, 60), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    public String getEmail() {
        return emailField.getText().trim();
    }

    public String getPassword() {
        return new String(passwordField.getPassword()).trim();
    }

    public void setSubmitAction(Runnable action) {
        submitButton.addActionListener(e -> action.run());
    }

    public void resetFields() {
        emailField.setText("");
        passwordField.setText("");
    }
}
