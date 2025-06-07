package Perusahaan;

import Components.PerusahaanNavigation;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class LamaranMasuk extends JFrame {

    public LamaranMasuk() {
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Lamaran Masuk - UJob");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(new PerusahaanNavigation("Lamaran Masuk"), BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(createContentPanel());
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(new Color(243, 244, 246));

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        setContentPane(mainPanel);
    }
    
    private JPanel createContentPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(243, 244, 246));
        panel.setBorder(new EmptyBorder(25, 40, 25, 40));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(createTopPanel());
        panel.add(Box.createRigidArea(new Dimension(0, 25)));

        JPanel listHeaderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        listHeaderPanel.setOpaque(false);
        listHeaderPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel listHeader = new JLabel("Daftar Lamaran");
        listHeader.setFont(new Font("SansSerif", Font.BOLD, 22));
        listHeaderPanel.add(listHeader);
        
        panel.add(listHeaderPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(createApplicationListPanel());
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        topPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        JLabel titleLabel = new JLabel("Lamaran Masuk");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setVerticalAlignment(SwingConstants.TOP);
        
        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(createStatsPanel(), BorderLayout.EAST);
        
        return topPanel;
    }

    private JPanel createStatsPanel() {
        JPanel statsContainer = new JPanel();
        statsContainer.setLayout(new BoxLayout(statsContainer, BoxLayout.X_AXIS));
        statsContainer.setOpaque(false);

        statsContainer.add(createStatCard("5", "Total Lowongan"));
        statsContainer.add(Box.createRigidArea(new Dimension(20, 0)));
        statsContainer.add(createStatCard("4", "Lowongan Aktif"));
        statsContainer.add(Box.createRigidArea(new Dimension(20, 0)));
        statsContainer.add(createStatCard("47", "Total Lamaran"));

        return statsContainer;
    }

    private JPanel createStatCard(String number, String label) {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235)),
            new EmptyBorder(20, 25, 20, 25)
        ));
        card.setMinimumSize(new Dimension(180, 100));
        card.setPreferredSize(new Dimension(180, 100));
        card.setMaximumSize(new Dimension(200, 100));

        GridBagConstraints gbc = new GridBagConstraints();
        
        JLabel numberLabel = new JLabel(number);
        numberLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        gbc.gridy = 0;
        card.add(numberLabel, gbc);

        JLabel labelLabel = new JLabel(label);
        labelLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        labelLabel.setForeground(Color.GRAY);
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 0, 0, 0);
        card.add(labelLabel, gbc);

        return card;
    }
    
    private JPanel createApplicationListPanel() {
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);
        listPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Data lamaran dengan status yang sudah diproses (tanpa "Ditinjau")
        listPanel.add(createApplicationCard("Dhafin Salman", "Pengembang AI", "22 Mei 2024", "Terima"));
        listPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        listPanel.add(createApplicationCard("Ryan Gabriel", "Pengembang Web", "21 Mei 2024", "Terima"));
        listPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        listPanel.add(createApplicationCard("Ahmad Rizki", "UI/UX Designer", "20 Mei 2024", "Diterima"));
        listPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        listPanel.add(createApplicationCard("Sari Dewi", "Data Analyst", "19 Mei 2024", "Ditolak"));

        return listPanel;
    }
    
    private JPanel createApplicationCard(String applicantName, String jobTitle, String applicationDate, String status) {
        JPanel cardPanel = new JPanel(new GridBagLayout());
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235)),
            new EmptyBorder(20, 25, 20, 25)
        ));
        cardPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // Kolom 1: Informasi Pelamar
        JPanel applicantInfoPanel = new JPanel();
        applicantInfoPanel.setLayout(new BoxLayout(applicantInfoPanel, BoxLayout.Y_AXIS));
        applicantInfoPanel.setOpaque(false);
        
        applicantInfoPanel.add(Box.createVerticalGlue());
        
        JLabel nameLabel = new JLabel(applicantName + " (" + applicationDate + ")");
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        applicantInfoPanel.add(nameLabel);
        
        applicantInfoPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        
        JLabel jobLabel = new JLabel("Posisi yang dilamar: " + jobTitle);
        jobLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        jobLabel.setForeground(new Color(59, 130, 246));
        jobLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        applicantInfoPanel.add(jobLabel);

        applicantInfoPanel.add(Box.createVerticalGlue());

        gbc.gridx = 0;
        gbc.weightx = 0.3;
        gbc.anchor = GridBagConstraints.WEST;
        cardPanel.add(applicantInfoPanel, gbc);

        // Kolom 2: Spacer (kosong)
        JPanel spacerPanel = new JPanel();
        spacerPanel.setOpaque(false);
        
        gbc.gridx = 1;
        gbc.weightx = 0.25;
        gbc.anchor = GridBagConstraints.CENTER;
        cardPanel.add(spacerPanel, gbc);

        // Kolom 3: Status dan Tombol Aksi
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        actionPanel.setOpaque(false);
        
        // Tombol aksi berdasarkan status
        if ("Terima".equals(status)) {
            JButton terimaButton = new JButton("Terima");
            styleActionButton(terimaButton, new Color(34, 197, 94));
            actionPanel.add(terimaButton);
            
            actionPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            
            JButton tolakButton = new JButton("Tolak");
            styleActionButton(tolakButton, new Color(239, 68, 68));
            actionPanel.add(tolakButton);
            
            actionPanel.add(Box.createRigidArea(new Dimension(15, 0)));
            
            JButton cekProfileButton = new JButton("Cek Profil");
            styleActionButton(cekProfileButton, new Color(59, 130, 246));
            actionPanel.add(cekProfileButton);
        } else {
            // Status label untuk lamaran yang sudah diproses
            JLabel statusLabel = new JLabel(status);
            statusLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
            statusLabel.setOpaque(true);
            statusLabel.setBorder(new EmptyBorder(6, 15, 6, 15));
            statusLabel.setForeground(Color.WHITE);
            statusLabel.setBackground(getStatusColor(status));
            statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
            actionPanel.add(statusLabel);
            
            actionPanel.add(Box.createRigidArea(new Dimension(15, 0)));
            
            JButton cekProfileButton = new JButton("Cek Profil");
            styleActionButton(cekProfileButton, new Color(59, 130, 246));
            actionPanel.add(cekProfileButton);
        }
        
        gbc.gridx = 2;
        gbc.weightx = 0.45;
        gbc.anchor = GridBagConstraints.EAST;
        cardPanel.add(actionPanel, gbc);
        
        return cardPanel;
    }
    
    private void styleActionButton(JButton button, Color bgColor) {
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(8, 18, 8, 18));
        button.setPreferredSize(new Dimension(70, 32));
    }

    private Color getStatusColor(String status) {
        switch (status) {
            case "Diterima":
                return new Color(34, 197, 94);
            case "Ditolak":
                return new Color(239, 68, 68);
            default:
                return Color.GRAY;
        }
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(LamaranMasuk.class.getName())
                .log(java.util.logging.Level.SEVERE, null, ex);
        }

        SwingUtilities.invokeLater(() -> {
            new LamaranMasuk().setVisible(true);
        });
    }
}