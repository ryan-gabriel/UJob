package Components;

import Auth.SessionManager;
import Mahasiswa.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MahasiswaNavigation extends JPanel {
    public MahasiswaNavigation(String activeMenu) {
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

        String[] menuItems = {"Dashboard", "Profile", "Portofolio", "Lowongan", "Proyek", "Inbox"};
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.X_AXIS));
        menuPanel.setOpaque(false);

        for (int i = 0; i < menuItems.length; i++) {
            String item = menuItems[i];
            JLabel menuLabel = new JLabel(item);
            menuLabel.setForeground(item.equals(activeMenu) ? new Color(255, 215, 0) : Color.WHITE);
            menuLabel.setFont(new Font("Arial", item.equals(activeMenu) ? Font.BOLD : Font.PLAIN, 16));
            menuLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            menuPanel.add(menuLabel);

            menuLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent evt) {
                    try {
                        JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(MahasiswaNavigation.this);
                        int currentUserId = SessionManager.getInstance().getId();
                        
                        switch(item) {
                            case "Dashboard": new Dashboard().setVisible(true); break;
                            case "Profile": new Profile(currentUserId).setVisible(true); break;
                            case "Portofolio": new Portofolio(currentUserId).setVisible(true); break;
                            case "Lowongan": new Lowongan(currentUserId).setVisible(true); break;
                            case "Proyek": new CariProyek().setVisible(true); break;
                            case "Inbox": new Inbox().setVisible(true); break;
                        }
                        currentFrame.dispose();
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(MahasiswaNavigation.this, "Halaman " + item + " belum ada atau terjadi kesalahan.", "Info", JOptionPane.INFORMATION_MESSAGE);
                        e.printStackTrace();
                    }
                }
            });

            if (i < menuItems.length - 1) {
                menuPanel.add(Box.createRigidArea(new Dimension(40, 0)));
            }
        }

        navPanel.add(logoPanel, BorderLayout.WEST);
        navPanel.add(menuPanel, BorderLayout.EAST);
        add(navPanel, BorderLayout.CENTER);
    }
}