import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Theme {
    // Main background colors
    public static final Color DUSTY_BLUE = new Color(0x6C, 0x8E, 0xBF);
    public static final Color SOFT_SLATE = new Color(0x8F, 0xA8, 0xC9);
    public static final Color WARM_CREAM = new Color(0xFA, 0xF7, 0xF2);
    public static final Color SOFT_BEIGE = new Color(0xF1, 0xEC, 0xE6);
    
    // Button and navbar colors
    public static final Color NAVY_BLUE = new Color(0x1F, 0x3A, 0x5F);
    public static final Color DARK_NAVY = new Color(0x16, 0x2B, 0x46);
    
    // Accent colors
    public static final Color SOFT_MUSTARD = new Color(0xE8, 0xC7, 0x6A);
    
    // Text colors - for visibility
    public static final Color TEXT_BLACK = new Color(0x00, 0x00, 0x00);
    public static final Color TEXT_DARK = new Color(0x2F, 0x2F, 0x2F);
    public static final Color TEXT_SECONDARY = new Color(0x7A, 0x7A, 0x7A);
    public static final Color LIGHT_BEIGE = new Color(0xFA, 0xF7, 0xF2);
    
    // Component backgrounds
    public static final Color PANEL_WHITE = new Color(0xFF, 0xFF, 0xFF);
    
    // Status colors
    public static final Color SUCCESS_GREEN = new Color(0x7B, 0xAE, 0x7F);
    public static final Color ERROR_CORAL = new Color(0xD9, 0x7C, 0x6C);

    public static void styleButton(JButton button) {
        button.setBackground(NAVY_BLUE);
        button.setForeground(TEXT_BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
    }

    public static void styleCard(JComponent component) {
        component.setBackground(PANEL_WHITE);
        component.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 0, 0, 20), 1),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
    }

    public static void stylePanel(JPanel panel) {
        panel.setBackground(DUSTY_BLUE);
    }

    public static ImageIcon loadBookCover(String imageName, int width, int height) {
        List<String> candidateDirectories = Arrays.asList(
                "images",
                "src/images",
                System.getProperty("user.dir") + "/images",
                System.getProperty("user.dir") + "/src/images"
        );

        for (String directory : candidateDirectories) {
            File file = new File(directory, imageName);
            if (file.exists()) {
                try {
                    BufferedImage original = ImageIO.read(file);
                    if (original != null) {
                        Image scaled = original.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                        return new ImageIcon(scaled);
                    }
                } catch (IOException ignored) {
                    // continue to placeholder
                }
            }
        }
        return createPlaceholder(width, height);
    }

    private static ImageIcon createPlaceholder(int width, int height) {
        BufferedImage placeholder = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = placeholder.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(SOFT_BEIGE);
        g.fillRect(0, 0, width, height);
        g.setColor(TEXT_SECONDARY);
        g.setFont(new Font("SansSerif", Font.BOLD, 14));
        FontMetrics fm = g.getFontMetrics();
        String message = "Image not found";
        int x = (width - fm.stringWidth(message)) / 2;
        int y = height / 2;
        g.drawString(message, x, y);
        g.dispose();
        return new ImageIcon(placeholder);
    }
}
