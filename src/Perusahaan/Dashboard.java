package Perusahaan;

import Components.PerusahaanNavigation;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;

public class Dashboard extends JFrame {

    public Dashboard() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Dashboard Perusahaan - UJob");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(243, 244, 246));

        // Navigation Header
        mainPanel.add(new PerusahaanNavigation("Dashboard"), BorderLayout.NORTH);

        // Main content area with scroll
        JScrollPane scrollPane = new JScrollPane(createContentPanel());
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new MigLayout("wrap 1, fillx, insets 30 50 30 50, gapy 30", "[fill]"));
        contentPanel.setOpaque(false);

        // Title
        JLabel titleLabel = new JLabel("PT Pactindo's Dashboard");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setForeground(new Color(30, 41, 59));
        contentPanel.add(titleLabel);

        // Inbox Card (full width)
        contentPanel.add(createInboxCard(), "growx");
        
        // Bottom Cards Panel (Status Profile and Activity side by side)
        JPanel bottomCardsPanel = new JPanel(new MigLayout("wrap 2, fillx, gap 30", "[grow, 50%][grow, 50%]"));
        bottomCardsPanel.setOpaque(false);
        
        bottomCardsPanel.add(createStatusProfileCard(), "grow");
        bottomCardsPanel.add(createActivityCard(), "grow");
        
        contentPanel.add(bottomCardsPanel, "growx");

        return contentPanel;
    }
    
    private JPanel createInboxCard() {
        JPanel card = createStyledCard();
        card.setLayout(new BorderLayout());

        JLabel title = new JLabel("Inbox Terbaru");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setBorder(new EmptyBorder(0, 0, 15, 0));
        title.setHorizontalAlignment(JLabel.LEFT);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        card.add(title, BorderLayout.NORTH);
        card.add(contentPanel, BorderLayout.CENTER);
        String[][] inboxItems = {
            {"Lamaran dari Dhafin untuk posisi Pengembang Web", "21 - 05 - 2025"},
            {"Lamaran dari Ryan untuk posisi Magang Data Analyst", "21 - 05 - 2025"},
            {"Menambahkan Lowongan Pengembang Web", "21 - 05 - 2025"},
            {"Menambahkan Lowongan Magang Data Analyst", "21 - 05 - 2025"}
        };
        
        for (int i = 0; i < inboxItems.length; i++) {
            contentPanel.add(createInboxItem(inboxItems[i][0], inboxItems[i][1]));
            if (i < inboxItems.length - 1) {
                JSeparator separator = new JSeparator();
                separator.setForeground(new Color(229, 231, 235));
                contentPanel.add(Box.createVerticalStrut(15));
                contentPanel.add(separator);
                contentPanel.add(Box.createVerticalStrut(15));
            }
        }
        
        contentPanel.add(Box.createVerticalStrut(20));
        
        JButton seeMoreButton = new JButton("See More  >");
        seeMoreButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        seeMoreButton.setForeground(new Color(59, 130, 246)); // Biru
        seeMoreButton.setBackground(Color.WHITE);
        seeMoreButton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        seeMoreButton.setFocusPainted(false);
        seeMoreButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        seeMoreButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        JPanel buttonWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonWrapper.setOpaque(false);
        buttonWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonWrapper.add(seeMoreButton);
        
        contentPanel.add(buttonWrapper);

        return card;
    }
    
    private JPanel createInboxItem(String text, String date) {
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setOpaque(false);
        
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
        
        JLabel dateLabel = new JLabel(date);
        dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        dateLabel.setForeground(Color.GRAY);
        
        itemPanel.add(textLabel, BorderLayout.WEST);
        itemPanel.add(dateLabel, BorderLayout.EAST);
        return itemPanel;
    }

    private JPanel createStatusProfileCard() {
        JPanel card = createStyledCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Status Profile");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        card.add(title);
        card.add(Box.createVerticalStrut(20));
        
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        statusPanel.setOpaque(false);
        statusPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Menggunakan placeholder jika ikon tidak ada
        JLabel checkIcon = new JLabel();
        try {
            checkIcon.setIcon(new ImageIcon(getClass().getResource("/images/check-circle.png")));
        } catch (Exception e) {
            checkIcon.setText("✓");
            checkIcon.setFont(new Font("SansSerif", Font.BOLD, 20));
            checkIcon.setForeground(new Color(34, 197, 94));
        }

        JLabel statusLabel = new JLabel("PT Pactindo sudah terverifikasi");
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        statusLabel.setForeground(new Color(34, 197, 94));
        
        statusPanel.add(checkIcon);
        statusPanel.add(statusLabel);
        
        card.add(statusPanel);
        
        return card;
    }

    private JPanel createActivityCard() {
        JPanel card = createStyledCard();
        card.setLayout(new MigLayout("wrap 1, fillx", "[fill]"));

        JLabel title = new JLabel("Aktivitas Perusahaan");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        card.add(title, "wrap, gapbottom 15");
        
        card.add(createActivityItem("Lowongan Aktif", "5"), "growx");
        card.add(createActivityItem("Lamaran Masuk", "12"), "growx");
        
        return card;
    }
    
    private JPanel createActivityItem(String label, String value) {
        JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        itemPanel.setOpaque(false);
        
        JLabel icon = new JLabel("📄"); // Placeholder ikon
        icon.setFont(new Font("SansSerif", Font.PLAIN, 18));
        
        JLabel labelText = new JLabel(label + " :");
        labelText.setFont(new Font("SansSerif", Font.PLAIN, 15));
        
        JLabel valueText = new JLabel(value);
        valueText.setFont(new Font("SansSerif", Font.BOLD, 24));
        valueText.setForeground(new Color(30, 41, 59));
        
        itemPanel.add(icon);
        itemPanel.add(labelText);
        itemPanel.add(valueText);
        
        return itemPanel;
    }
    
    private JPanel createStyledCard() {
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
            new EmptyBorder(25, 25, 25, 25)
        ));
        return card;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new Dashboard().setVisible(true);
        });
    }
}