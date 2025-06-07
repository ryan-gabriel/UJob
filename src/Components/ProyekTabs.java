package Components;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ProyekTabs extends JPanel {

    public ProyekTabs(String activeTab) {
        setLayout(new GridLayout(1, 5, 10, 0));
        setOpaque(false);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        String[] tabItems = {"Cari Teman", "Cari Proyek", "Proyek Saya", "Proyek Aktif", "Pendaftaran Aktif"};

        for (String tab : tabItems) {
            JButton tabButton = new JButton(tab);
            tabButton.setPreferredSize(new Dimension(150, 45));
            tabButton.setBackground(Color.WHITE);
            tabButton.setForeground(Color.GRAY);
            tabButton.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY));
            tabButton.setFont(new Font("Arial", Font.PLAIN, 14));
            tabButton.setFocusPainted(false);

            if (tab.equals(activeTab)) {
                tabButton.setForeground(new Color(37, 64, 143));
                tabButton.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(37, 64, 143)));
                tabButton.setFont(new Font("Arial", Font.BOLD, 14));
            }

            tabButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

            add(tabButton);
        }
    }
}
