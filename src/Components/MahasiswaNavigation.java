package Components;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

// Import yang diperlukan untuk navigasi
import Auth.SessionManager; 
import Mahasiswa.CariProyek;
import Mahasiswa.Dashboard;
import Mahasiswa.Inbox;
import Mahasiswa.Lowongan;
import Mahasiswa.Profile;

import Mahasiswa.Portofolio;


public class MahasiswaNavigation extends JPanel {
    public MahasiswaNavigation(String activeMenu) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(0, 80));
        setBackground(new Color(37, 64, 143));

        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setBackground(new Color(37, 64, 143));
        navPanel.setBorder(new EmptyBorder(15, 40, 15, 40));

        // Logo dan Nama Aplikasi
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

        // Menu Navigasi
        String[] menuItems = {"Dashboard", "Profile", "Portofolio", "Lowongan", "Proyek", "Inbox"};
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
            menuPanel.add(menuLabel);

            menuLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    if (item.equals("Dashboard")) {
                        new Dashboard().setVisible(true);
                        SwingUtilities.getWindowAncestor(MahasiswaNavigation.this).dispose();
                    
                    // === PERUBAHAN UTAMA ADA DI BLOK INI ===
                    } else if (item.equals("Profile")) {
                        // 1. Ambil ID pengguna yang sedang login dari SessionManager
                        int currentUserId = SessionManager.getInstance().getId();
                        
                        // 2. Panggil constructor Profile dengan menyertakan ID pengguna
                        new Profile(currentUserId).setVisible(true);
                        
                        // 3. Tutup jendela saat ini
                        SwingUtilities.getWindowAncestor(MahasiswaNavigation.this).dispose();
                    // =======================================

                    } else if (item.equals("Portofolio")) {

                        new Portofolio().setVisible(true);
                        SwingUtilities.getWindowAncestor(MahasiswaNavigation.this).dispose();
                    } else if (item.equals("Lowongan")) {
                        new Lowongan().setVisible(true);
                        SwingUtilities.getWindowAncestor(MahasiswaNavigation.this).dispose();
                    } else if (item.equals("Proyek")) {
                        new CariProyek().setVisible(true);
                        SwingUtilities.getWindowAncestor(MahasiswaNavigation.this).dispose();
                    } else if (item.equals("Inbox")) {
                        new Inbox().setVisible(true);
                        SwingUtilities.getWindowAncestor(MahasiswaNavigation.this).dispose();
                    }
                }
            });

            if (i < menuItems.length - 1) {
                menuPanel.add(Box.createRigidArea(new Dimension(30, 0)));
            }
        }

        menuPanel.add(Box.createHorizontalGlue());

        navPanel.add(logoPanel, BorderLayout.WEST);
        navPanel.add(menuPanel, BorderLayout.EAST);

        add(navPanel, BorderLayout.CENTER);
    }
}