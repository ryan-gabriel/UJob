// File: Components/PerusahaanNavigation.java
package Components;

import Auth.SessionManager;
import Perusahaan.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class PerusahaanNavigation extends JPanel {
    public PerusahaanNavigation(String activeMenu) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(0, 80));
        setBackground(new Color(37, 64, 143));

        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setOpaque(false);
        navPanel.setBorder(new EmptyBorder(15, 40, 15, 40));

        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        logoPanel.setOpaque(false);

        JLabel logoLabel = new JLabel();
        try {
            ImageIcon originalIcon = new ImageIcon(getClass().getResource("/images/Logo.png"));
            Image scaledImage = originalIcon.getImage().getScaledInstance(45, 45, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            logoLabel.setText("UJ");
            logoLabel.setFont(new Font("Arial", Font.BOLD, 18));
            logoLabel.setForeground(new Color(37, 64, 143));
            logoLabel.setBackground(Color.WHITE);
            logoLabel.setOpaque(true);
        }
        logoLabel.setPreferredSize(new Dimension(50, 50));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logoLabel.setVerticalAlignment(SwingConstants.CENTER);

        JLabel brandLabel = new JLabel("UJob");
        brandLabel.setForeground(Color.WHITE);
        brandLabel.setFont(new Font("Arial", Font.BOLD, 24));
        brandLabel.setBorder(new EmptyBorder(0, 10, 0, 0));

        logoPanel.add(logoLabel);
        logoPanel.add(brandLabel);

        String[] menuItems = {"Dashboard", "Profile", "Kelola Lowongan", "Lamaran Masuk", "Inbox"};
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.X_AXIS));
        menuPanel.setOpaque(false);
        menuPanel.add(Box.createHorizontalGlue());

        for (int i = 0; i < menuItems.length; i++) {
            String item = menuItems[i];
            JLabel menuLabel = new JLabel(item);
            menuLabel.setForeground(item.equals(activeMenu) ? new Color(255, 215, 0) : Color.WHITE);
            menuLabel.setFont(new Font("Arial", item.equals(activeMenu) ? Font.BOLD : Font.PLAIN, 14));
            menuLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

            menuLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (item.equals(activeMenu)) return;
                    JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(PerusahaanNavigation.this);
                    int activeUserId = SessionManager.getInstance().getId();
                    if (activeUserId <= 0) {
                        JOptionPane.showMessageDialog(currentFrame, "Sesi tidak valid.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    try {
                        switch (item) {
                            case "Dashboard": new Dashboard(activeUserId).setVisible(true); break;
                            case "Profile": new Profile(activeUserId).setVisible(true); break;
                            case "Kelola Lowongan": new KelolaLowongan(activeUserId).setVisible(true); break;
                            case "Lamaran Masuk": new LamaranMasuk(activeUserId).setVisible(true); break;
                            // --- PERBAIKAN ADA DI SINI ---
                            // Menambahkan case untuk navigasi ke halaman Inbox
                            case "Inbox": new Inbox(activeUserId).setVisible(true); break;
                        }
                        if (currentFrame != null) currentFrame.dispose();
                    } catch (Exception ex) {
                         JOptionPane.showMessageDialog(PerusahaanNavigation.this, "Halaman " + item + " belum ada.", "Info", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            });
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