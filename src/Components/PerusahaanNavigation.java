package Components;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

// PERUBAHAN: Nama kelas diubah menjadi PerusahaanNavigation
public class PerusahaanNavigation extends JPanel {

    // PERUBAHAN: Konstruktor disesuaikan dengan nama kelas yang baru
    public PerusahaanNavigation(String activeMenu) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(0, 80));
        setBackground(new Color(37, 64, 143));

        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setBackground(new Color(37, 64, 143));
        navPanel.setBorder(new EmptyBorder(15, 40, 15, 40));

        // Logo and brand
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        logoPanel.setOpaque(false);

        JLabel logoCircle = new JLabel("UJOB");
        logoCircle.setPreferredSize(new Dimension(40, 40));
        logoCircle.setHorizontalAlignment(SwingConstants.CENTER);
        logoCircle.setVerticalAlignment(SwingConstants.CENTER);
        logoCircle.setBackground(Color.WHITE);
        logoCircle.setOpaque(true);
        logoCircle.setForeground(new Color(37, 64, 143));
        logoCircle.setFont(new Font("Arial", Font.BOLD, 12));
        logoCircle.setBorder(BorderFactory.createEmptyBorder());

        JLabel brandLabel = new JLabel("UPI Job");
        brandLabel.setForeground(Color.WHITE);
        brandLabel.setFont(new Font("Arial", Font.BOLD, 18));
        brandLabel.setBorder(new EmptyBorder(0, 10, 0, 0));

        logoPanel.add(logoCircle);
        logoPanel.add(brandLabel);

        // Navigation menu
        String[] menuItems = {"Dashboard", "Profile", "Kelola Lowongan", "Lamaran Masuk", "Inbox"};
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.X_AXIS));
        menuPanel.setOpaque(false);

        // Menambahkan perekat di awal untuk mendorong menu ke kanan
        menuPanel.add(Box.createHorizontalGlue());

        for (int i = 0; i < menuItems.length; i++) {
            String item = menuItems[i];
            JLabel menuLabel = new JLabel(item);
            menuLabel.setForeground(item.equals(activeMenu) ? new Color(255, 215, 0) : Color.WHITE);
            menuLabel.setFont(new Font("Arial", item.equals(activeMenu) ? Font.BOLD : Font.PLAIN, 14));
            menuLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            menuPanel.add(menuLabel);

            if (i < menuItems.length - 1) {
                menuPanel.add(Box.createRigidArea(new Dimension(30, 0)));
            }
        }

        navPanel.add(logoPanel, BorderLayout.WEST);
        navPanel.add(menuPanel, BorderLayout.CENTER);

        add(navPanel);
    }
}