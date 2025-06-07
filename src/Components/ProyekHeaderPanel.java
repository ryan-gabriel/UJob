package Components;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ProyekHeaderPanel extends JPanel {

    public ProyekHeaderPanel(String activeTab) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE); // Overall panel background

        // === NAVIGATION SECTION ===
        MahasiswaNavigation navContainer = new MahasiswaNavigation("Proyek");
        navContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60)); // Supaya terlihat
        navContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(navContainer, BorderLayout.NORTH);

        // === HERO SECTION ===
        JPanel heroWrapper = new JPanel(new BorderLayout());
        heroWrapper.setBackground(Color.WHITE);
        heroWrapper.setBorder(new EmptyBorder(10, 40, 10, 40));

        JPanel heroPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(120, 119, 198)); // Hero background color
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }

            @Override
            public boolean isOpaque() {
                return false;
            }
        };

        heroPanel.setLayout(new BoxLayout(heroPanel, BoxLayout.Y_AXIS));
        heroPanel.setBorder(new EmptyBorder(40, 40, 40, 40));

        JLabel heroTitle = new JLabel("Cari Teman & Bantu Proyek");
        heroTitle.setForeground(Color.WHITE);
        heroTitle.setFont(new Font("Arial", Font.BOLD, 28));
        heroTitle.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        JLabel heroSubtitle = new JLabel("Temukan kolaborator ideal dan bergabung dengan proyek menarik");
        heroSubtitle.setForeground(Color.WHITE);
        heroSubtitle.setFont(new Font("Arial", Font.PLAIN, 16));
        heroSubtitle.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        heroSubtitle.setBorder(new EmptyBorder(10, 0, 0, 0));

        heroPanel.add(heroTitle);
        heroPanel.add(heroSubtitle);
        heroWrapper.add(heroPanel, BorderLayout.CENTER);

        add(heroWrapper, BorderLayout.CENTER);


        ProyekTabs tabsPanel = new ProyekTabs(activeTab);

        JPanel tabsWrapper = new JPanel();
        tabsWrapper.setOpaque(false);
        tabsWrapper.setBorder(new EmptyBorder(10, 40, 10, 40));
        tabsWrapper.setLayout(new BoxLayout(tabsWrapper, BoxLayout.X_AXIS));
        tabsWrapper.add(tabsPanel);
        
        add(tabsWrapper, BorderLayout.SOUTH);

    }
}
