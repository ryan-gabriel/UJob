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
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class KelolaLowongan extends JFrame {

    public KelolaLowongan() {
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Kelola Lowongan - UJob");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(new PerusahaanNavigation("Kelola Lowongan"), BorderLayout.NORTH);

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
        
        JLabel listHeader = new JLabel("Daftar Lowongan");
        listHeader.setFont(new Font("SansSerif", Font.BOLD, 22));
        listHeaderPanel.add(listHeader);
        
        panel.add(listHeaderPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(createJobListPanel());
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        topPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        JLabel titleLabel = new JLabel("Kelola Lowongan");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setVerticalAlignment(SwingConstants.TOP);
        titleLabel.setBorder(new EmptyBorder(20, 0, 20, 0));
        topPanel.add(titleLabel, BorderLayout.WEST);
        
        // Add stats below the title
        topPanel.add(createStatsPanel(), BorderLayout.SOUTH);

        return topPanel;
    }

    private JPanel createStatsPanel() {
        JPanel statsContainer = new JPanel();
        statsContainer.setLayout(new BoxLayout(statsContainer, BoxLayout.Y_AXIS));
        statsContainer.setOpaque(false);

        // Stat cards panel
        JPanel statCardsPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        statCardsPanel.setOpaque(false);
        statCardsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        statCardsPanel.add(createStatCard("5", "Total Lowongan"));
        statCardsPanel.add(createStatCard("4", "Lowongan Aktif"));
        statCardsPanel.add(createStatCard("0", "Draft"));
        statCardsPanel.add(createStatCard("47", "Total Lamaran"));

        statsContainer.add(statCardsPanel);

        // Search container
        JPanel searchContainer = new JPanel();
        searchContainer.setLayout(new BoxLayout(searchContainer, BoxLayout.X_AXIS));
        searchContainer.setOpaque(false);
        searchContainer.setBorder(new EmptyBorder(20, 0, 20, 0));

        JTextField searchField = new JTextField("Cari lowongan...", 20);
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        searchField.setForeground(Color.LIGHT_GRAY);
        
        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Cari lowongan...")) {
                    searchField.setText(""); 
                    searchField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Cari lowongan..."); 
                    searchField.setForeground(Color.LIGHT_GRAY);
                }
            }
        });

        searchContainer.add(searchField);
        searchContainer.add(Box.createHorizontalGlue());

        JButton addButton = new JButton("+ Tambah Lowongan Baru");
        addButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        addButton.setFocusPainted(false);
        addButton.setBackground(new Color(59, 130, 246));
        addButton.setForeground(Color.WHITE);
        addButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        addButton.setBorder(new EmptyBorder(8, 18, 8, 18));
        searchContainer.add(addButton);

        statsContainer.add(searchContainer);

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

    private JPanel createJobListPanel() {
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);
        listPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Create job cards based on the image
        listPanel.add(createJobCard(
            "Frontend Developer", 
            "PT Pactindo", 
            "Rp 2.500.000/bulan", 
            "30 Juni 2025", 
            "Magang", 
            12,
            "Aktif",
            "Bandung, Jawa Barat",
            "3 bulan"
        ));
        
        listPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        listPanel.add(createJobCard(
            "Data Analyst", 
            "PT Pactindo", 
            "Rp 5.000.000/bulan", 
            "31 Juli 2025", 
            "Penuh", 
            8,
            "Non-aktif",
            "Surabaya, Jawa Timur",
            "8 bulan"
        ));

        return listPanel;
    }

private JPanel createJobCard(String position, String company, String salary, String deadline, 
                            String type, int applicantsCount, String status, String location, String duration) {
    JPanel cardPanel = new JPanel(new BorderLayout());
    cardPanel.setBackground(Color.WHITE);
    cardPanel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(229, 231, 235)),
        new EmptyBorder(20, 25, 20, 25)
    ));
    cardPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    cardPanel.setMinimumSize(new Dimension(1000, 180));
    cardPanel.setPreferredSize(new Dimension(1000, 180));
    cardPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

    // Left side - Job info (give it more space)
    JPanel leftPanel = new JPanel();
    leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
    leftPanel.setOpaque(false);
    leftPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    leftPanel.setPreferredSize(new Dimension(600, 140)); // Give more width to left panel

    // Title
    JLabel titleLabel = new JLabel(position);
    titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
    titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    leftPanel.add(titleLabel);
    
    // Company name
    JLabel companyLabel = new JLabel("PT " + company);
    companyLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
    companyLabel.setForeground(new Color(107, 114, 128));
    companyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    leftPanel.add(companyLabel);
    
    leftPanel.add(Box.createRigidArea(new Dimension(0, 12)));

    // Badge and status panel
    JPanel badgeStatusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    badgeStatusPanel.setOpaque(false);
    badgeStatusPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    
    // Job type badge
    JLabel typeLabel = new JLabel(type);
    typeLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
    typeLabel.setForeground(Color.WHITE);
    typeLabel.setOpaque(true);
    typeLabel.setBorder(new EmptyBorder(4, 10, 4, 10));
    
    if (type.equals("Magang")) {
        typeLabel.setBackground(new Color(34, 197, 94)); // Green
    } else {
        typeLabel.setBackground(new Color(59, 130, 246)); // Blue
    }
    
    badgeStatusPanel.add(typeLabel);
    badgeStatusPanel.add(Box.createRigidArea(new Dimension(8, 0)));
    
    // Status label
    JLabel statusLabel = new JLabel(status);
    statusLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
    statusLabel.setForeground(Color.WHITE);
    statusLabel.setOpaque(true);
    statusLabel.setBorder(new EmptyBorder(4, 10, 4, 10));
    
    if (status.equals("Aktif")) {
        statusLabel.setBackground(new Color(34, 197, 94)); // Green
    } else {
        statusLabel.setBackground(new Color(239, 68, 68)); // Red
    }
    
    badgeStatusPanel.add(statusLabel);
    badgeStatusPanel.add(Box.createRigidArea(new Dimension(15, 0)));
    
    // Applicants count
    JLabel applicantsLabel = new JLabel(applicantsCount + " lamaran");
    applicantsLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
    applicantsLabel.setForeground(Color.WHITE);
    applicantsLabel.setOpaque(true);
    applicantsLabel.setBackground(new Color(107, 114, 128));
    applicantsLabel.setBorder(new EmptyBorder(4, 10, 4, 10));
    badgeStatusPanel.add(applicantsLabel);
    
    leftPanel.add(badgeStatusPanel);
    leftPanel.add(Box.createRigidArea(new Dimension(0, 12)));

    // Description
    String description = type.equals("Magang") ? 
        "Mencari mahasiswa untuk posisi magang Frontend Developer. Akan bekerja dengan teknologi React.js, TypeScript, dan berkolaborasi dengan tim desain untuk implementasi UI/UX yang responsif." :
        "Data Analyst dengan pengalaman dalam analisis data dan pengolahan data. Bertanggung jawab atas laporan dan analisis data secara rutin.";
            
    JLabel descLabel = new JLabel("<html><div style='width: 550px; color: #6B7280;'>" + description + "</div></html>");
    descLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
    descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    leftPanel.add(descLabel);
    
    cardPanel.add(leftPanel, BorderLayout.WEST);
    
    // Right side - Info and actions (make it more compact and organized)
    JPanel rightPanel = new JPanel(new BorderLayout());
    rightPanel.setOpaque(false);
    rightPanel.setBorder(new EmptyBorder(0, 30, 0, 0)); // More spacing from left panel
    rightPanel.setPreferredSize(new Dimension(300, 140));
    rightPanel.setMinimumSize(new Dimension(300, 140));
    
    // Info panel - simple vertical layout like the example
    JPanel infoPanel = new JPanel();
    infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
    infoPanel.setOpaque(false);
    infoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    
    // Location
    JLabel lokasiLine = new JLabel("Lokasi    : " + location);
    lokasiLine.setFont(new Font("SansSerif", Font.PLAIN, 12));
    lokasiLine.setForeground(new Color(75, 85, 99));
    lokasiLine.setAlignmentX(Component.LEFT_ALIGNMENT);
    infoPanel.add(lokasiLine);
    infoPanel.add(Box.createRigidArea(new Dimension(0, 4)));
    
    // Salary
    JLabel gajiLine = new JLabel("Gaji        : " + salary);
    gajiLine.setFont(new Font("SansSerif", Font.PLAIN, 12));
    gajiLine.setForeground(new Color(75, 85, 99));
    gajiLine.setAlignmentX(Component.LEFT_ALIGNMENT);
    infoPanel.add(gajiLine);
    infoPanel.add(Box.createRigidArea(new Dimension(0, 4)));
    
    // Duration
    JLabel durasiLine = new JLabel("Durasi    : " + duration);
    durasiLine.setFont(new Font("SansSerif", Font.PLAIN, 12));
    durasiLine.setForeground(new Color(75, 85, 99));
    durasiLine.setAlignmentX(Component.LEFT_ALIGNMENT);
    infoPanel.add(durasiLine);
    infoPanel.add(Box.createRigidArea(new Dimension(0, 4)));
    
    // Deadline
    JLabel deadlineLine = new JLabel("Deadline : " + deadline);
    deadlineLine.setFont(new Font("SansSerif", Font.PLAIN, 12));
    deadlineLine.setForeground(new Color(75, 85, 99));
    deadlineLine.setAlignmentX(Component.LEFT_ALIGNMENT);
    infoPanel.add(deadlineLine);
    
    rightPanel.add(infoPanel, BorderLayout.NORTH);
    
    // Action buttons at the bottom right
    JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
    actionPanel.setOpaque(false);
    actionPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
    
    JButton lihatButton = new JButton("Lihat");
    lihatButton.setFont(new Font("SansSerif", Font.BOLD, 11));
    lihatButton.setFocusPainted(false);
    lihatButton.setBackground(new Color(59, 130, 246));
    lihatButton.setForeground(Color.WHITE);
    lihatButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    lihatButton.setBorder(new EmptyBorder(6, 14, 6, 14));
    
    JButton editButton = new JButton("Edit");
    editButton.setFont(new Font("SansSerif", Font.BOLD, 11));
    editButton.setFocusPainted(false);
    editButton.setBackground(new Color(156, 163, 175));
    editButton.setForeground(Color.WHITE);
    editButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    editButton.setBorder(new EmptyBorder(6, 14, 6, 14));
    
    JButton hapusButton = new JButton("Hapus");
    hapusButton.setFont(new Font("SansSerif", Font.BOLD, 11));
    hapusButton.setFocusPainted(false);
    hapusButton.setBackground(new Color(239, 68, 68));
    hapusButton.setForeground(Color.WHITE);
    hapusButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    hapusButton.setBorder(new EmptyBorder(6, 14, 6, 14));
    
    actionPanel.add(lihatButton);
    actionPanel.add(editButton);
    actionPanel.add(hapusButton);
    
    rightPanel.add(actionPanel, BorderLayout.SOUTH);
    
    cardPanel.add(rightPanel, BorderLayout.EAST);
    
    return cardPanel;
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
            java.util.logging.Logger.getLogger(KelolaLowongan.class.getName())
                .log(java.util.logging.Level.SEVERE, null, ex);
        }

        SwingUtilities.invokeLater(() -> {
            new KelolaLowongan().setVisible(true);
        });
    }
}