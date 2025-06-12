package Perusahaan;

import Components.PerusahaanNavigation;
import Database.DatabaseConnection;
import Database.KelolaLowonganDAO;
import Models.Lowongan;
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.Connection;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

public class KelolaLowongan extends JFrame {
    
    private KelolaLowonganDAO kelolaLowonganDAO;
    private Connection connection;
    private JPanel jobListPanel;
    private JTextField searchField;
    private JLabel totalLowonganLabel;
    private JLabel lowonganAktifLabel;
    private JLabel draftLabel;
    private JLabel totalLamaranLabel;
    
    private int currentUserId;
    
    public KelolaLowongan() {
        this(13); // User ID default untuk testing
    }
    
    public KelolaLowongan(int userId) {
        this.currentUserId = userId;
        initializeDatabase();
        initializeUI();
        loadData();
    }
    
    private void initializeDatabase() {
        try {
            connection = DatabaseConnection.getConnection();
            kelolaLowonganDAO = new KelolaLowonganDAO(connection);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Gagal koneksi ke database: " + e.getMessage(), 
                "Error Database", 
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
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
        
        jobListPanel = new JPanel();
        jobListPanel.setLayout(new BoxLayout(jobListPanel, BoxLayout.Y_AXIS));
        jobListPanel.setOpaque(false);
        jobListPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(jobListPanel);
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
        
        topPanel.add(createStatsPanel(), BorderLayout.SOUTH);

        return topPanel;
    }

    private JPanel createStatsPanel() {
        JPanel statsContainer = new JPanel();
        statsContainer.setLayout(new BoxLayout(statsContainer, BoxLayout.Y_AXIS));
        statsContainer.setOpaque(false);

        JPanel statCardsPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        statCardsPanel.setOpaque(false);
        statCardsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        totalLowonganLabel = new JLabel("0");
        lowonganAktifLabel = new JLabel("0");
        draftLabel = new JLabel("0");
        totalLamaranLabel = new JLabel("0");
        
        statCardsPanel.add(createStatCard(totalLowonganLabel, "Total Lowongan"));
        statCardsPanel.add(createStatCard(lowonganAktifLabel, "Lowongan Aktif"));
        statCardsPanel.add(createStatCard(draftLabel, "Draft"));
        statCardsPanel.add(createStatCard(totalLamaranLabel, "Total Lamaran"));

        statsContainer.add(statCardsPanel);

        JPanel searchContainer = new JPanel();
        searchContainer.setLayout(new BoxLayout(searchContainer, BoxLayout.X_AXIS));
        searchContainer.setOpaque(false);
        searchContainer.setBorder(new EmptyBorder(20, 0, 20, 0));

        searchField = new JTextField("Cari lowongan...", 20);
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
        
        Timer searchTimer = new Timer(500, e -> performSearch());
        searchTimer.setRepeats(false);
        
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { searchTimer.restart(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { searchTimer.restart(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { searchTimer.restart(); }
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
        addButton.addActionListener(e -> openTambahLowonganForm());
        
        searchContainer.add(addButton);
        statsContainer.add(searchContainer);
        return statsContainer;
    }
    
    private JPanel createStatCard(JLabel numberLabel, String labelText) {
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
        
        numberLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        gbc.gridy = 0;
        card.add(numberLabel, gbc);

        JLabel labelLabel = new JLabel(labelText);
        labelLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        labelLabel.setForeground(Color.GRAY);
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 0, 0, 0);
        card.add(labelLabel, gbc);

        return card;
    }
    
    private void loadData() {
        loadStatistics();
        loadJobList();
    }
    
    private void loadStatistics() {
        try {
            int totalLowongan = kelolaLowonganDAO.getTotalLowonganByUser(currentUserId);
            int lowonganAktif = kelolaLowonganDAO.getTotalLowonganAktifByUser(currentUserId);
            int draft = 0; // Fungsi draft belum ada di DB
            
            // PERBAIKAN: Gunakan method DAO yang akurat untuk menghitung total lamaran
            int totalLamaran = kelolaLowonganDAO.getTotalLamaranByUser(currentUserId);
            
            totalLowonganLabel.setText(String.valueOf(totalLowongan));
            lowonganAktifLabel.setText(String.valueOf(lowonganAktif));
            draftLabel.setText(String.valueOf(draft));
            totalLamaranLabel.setText(String.valueOf(totalLamaran));
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error memuat statistik: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadJobList() {
        try {
            jobListPanel.removeAll();
            List<Lowongan> lowonganList = kelolaLowonganDAO.getLowonganByUserId(currentUserId);
            
            if (lowonganList == null || lowonganList.isEmpty()) {
                JLabel noDataLabel = new JLabel("Belum ada lowongan yang dibuat");
                noDataLabel.setFont(new Font("SansSerif", Font.ITALIC, 16));
                noDataLabel.setForeground(Color.GRAY);
                noDataLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                jobListPanel.add(noDataLabel);
            } else {
                for (int i = 0; i < lowonganList.size(); i++) {
                    jobListPanel.add(createJobCard(lowonganList.get(i)));
                    if (i < lowonganList.size() - 1) {
                        jobListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                    }
                }
            }
            
            jobListPanel.revalidate();
            jobListPanel.repaint();
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error memuat daftar lowongan: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void performSearch() {
        try {
            String keyword = searchField.getText().trim();
            if (keyword.isEmpty() || keyword.equals("Cari lowongan...")) {
                loadJobList();
                return;
            }
            
            jobListPanel.removeAll();
            List<Lowongan> searchResults = kelolaLowonganDAO.searchLowonganByUser(keyword, currentUserId);
            
            if (searchResults == null || searchResults.isEmpty()) {
                JLabel noResultLabel = new JLabel("Tidak ada hasil pencarian untuk: '" + keyword + "'");
                noResultLabel.setFont(new Font("SansSerif", Font.ITALIC, 16));
                noResultLabel.setForeground(Color.GRAY);
                noResultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                jobListPanel.add(noResultLabel);
            } else {
                for (int i = 0; i < searchResults.size(); i++) {
                    jobListPanel.add(createJobCard(searchResults.get(i)));
                    if (i < searchResults.size() - 1) {
                        jobListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                    }
                }
            }
            
            jobListPanel.revalidate();
            jobListPanel.repaint();
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error saat mencari: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createJobCard(Lowongan lowongan) {
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235)),
            new EmptyBorder(20, 25, 20, 25)
        ));
        cardPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

        // Left side
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(lowongan.getJudulPekerjaan());
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        leftPanel.add(titleLabel);
        
        JLabel companyLabel = new JLabel(lowongan.getNamaPerusahaan());
        companyLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        companyLabel.setForeground(new Color(107, 114, 128));
        leftPanel.add(companyLabel);
        
        leftPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        JPanel badgeStatusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        badgeStatusPanel.setOpaque(false);
        badgeStatusPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        String jenisLowongan = lowongan.getJenisLowongan();
        JLabel typeLabel = new JLabel(jenisLowongan);
        typeLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
        typeLabel.setForeground(Color.WHITE);
        typeLabel.setOpaque(true);
        typeLabel.setBorder(new EmptyBorder(4, 10, 4, 10));
        typeLabel.setBackground(jenisLowongan.equalsIgnoreCase("Magang") ? new Color(34, 197, 94) : new Color(59, 130, 246));
        badgeStatusPanel.add(typeLabel);
        badgeStatusPanel.add(Box.createRigidArea(new Dimension(8, 0)));
        
        String status = lowongan.getStatus();
        JLabel statusLabel = new JLabel(status);
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setOpaque(true);
        statusLabel.setBorder(new EmptyBorder(4, 10, 4, 10));
        statusLabel.setBackground(status.equalsIgnoreCase("aktif") ? new Color(34, 197, 94) : new Color(239, 68, 68));
        badgeStatusPanel.add(statusLabel);
        badgeStatusPanel.add(Box.createRigidArea(new Dimension(15, 0)));
        
        JLabel applicantsLabel = new JLabel(lowongan.getJumlahLamaran() + " lamaran");
        applicantsLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        applicantsLabel.setForeground(Color.WHITE);
        applicantsLabel.setOpaque(true);
        applicantsLabel.setBackground(new Color(107, 114, 128));
        applicantsLabel.setBorder(new EmptyBorder(4, 10, 4, 10));
        badgeStatusPanel.add(applicantsLabel);
        
        leftPanel.add(badgeStatusPanel);
        leftPanel.add(Box.createVerticalGlue());

        cardPanel.add(leftPanel, BorderLayout.CENTER);
        
        // Right side
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);
        rightPanel.setBorder(new EmptyBorder(0, 30, 0, 0));
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        
        infoPanel.add(new JLabel("Lokasi    : " + lowongan.getLokasiKerja()));
        infoPanel.add(new JLabel("Gaji        : " + lowongan.getGaji()));
        infoPanel.add(new JLabel("Durasi    : " + lowongan.getDurasi()));
        String deadlineStr = (lowongan.getTanggalDeadline() != null) ? new SimpleDateFormat("dd MMM yyyy").format(lowongan.getTanggalDeadline()) : "-";
        infoPanel.add(new JLabel("Deadline : " + deadlineStr));
        rightPanel.add(infoPanel, BorderLayout.CENTER);
        
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        actionPanel.setOpaque(false);
        
        JButton lihatButton = new JButton("Lihat");
        lihatButton.addActionListener(e -> lihatLowongan(lowongan));
        JButton editButton = new JButton("Edit");
        editButton.addActionListener(e -> editLowongan(lowongan));
        JButton hapusButton = new JButton("Hapus");
        hapusButton.addActionListener(e -> hapusLowongan(lowongan));
        
        actionPanel.add(lihatButton);
        actionPanel.add(editButton);
        actionPanel.add(hapusButton);
        rightPanel.add(actionPanel, BorderLayout.SOUTH);
        
        cardPanel.add(rightPanel, BorderLayout.EAST);
        
        return cardPanel;
    }
    
    // ========== CREATE FUNCTIONALITY ==========
    private void openTambahLowonganForm() {
        JDialog dialog = new JDialog(this, "Tambah Lowongan Baru", true);
        dialog.setSize(600, 700);
        dialog.setLocationRelativeTo(this);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Tambah Lowongan Baru");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Form fields
        JTextField judulField = new JTextField(30);
        JTextArea deskripsiArea = new JTextArea(4, 30);
        deskripsiArea.setLineWrap(true);
        deskripsiArea.setWrapStyleWord(true);
        JScrollPane deskripsiScroll = new JScrollPane(deskripsiArea);
        
        JTextArea kualifikasiArea = new JTextArea(3, 30);
        kualifikasiArea.setLineWrap(true);
        kualifikasiArea.setWrapStyleWord(true);
        JScrollPane kualifikasiScroll = new JScrollPane(kualifikasiArea);
        
                JComboBox<String> jenisCombo = new JComboBox<>(new String[]{"Magang", "Full-time", "Part-time", "Kontrak"});
        JTextField gajiField = new JTextField(30);
        JTextField lokasiField = new JTextField(30);
        JTextField durasiField = new JTextField(30);
        JTextField deadlineField = new JTextField(30); // Format: yyyy-mm-dd
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"aktif", "nonaktif"});
        
        // Add components to form
        int row = 0;
        
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Judul Pekerjaan:"), gbc);
        gbc.gridx = 1;
        formPanel.add(judulField, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Deskripsi:"), gbc);
        gbc.gridx = 1;
        formPanel.add(deskripsiScroll, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Kualifikasi:"), gbc);
        gbc.gridx = 1;
        formPanel.add(kualifikasiScroll, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Jenis Lowongan:"), gbc);
        gbc.gridx = 1;
        formPanel.add(jenisCombo, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Gaji:"), gbc);
        gbc.gridx = 1;
        formPanel.add(gajiField, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Lokasi Kerja:"), gbc);
        gbc.gridx = 1;
        formPanel.add(lokasiField, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Durasi:"), gbc);
        gbc.gridx = 1;
        formPanel.add(durasiField, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Deadline (yyyy-mm-dd):"), gbc);
        gbc.gridx = 1;
        formPanel.add(deadlineField, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        formPanel.add(statusCombo, gbc);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton simpanButton = new JButton("Simpan");
        JButton batalButton = new JButton("Batal");
        
        simpanButton.addActionListener(e -> {
            try {
                // Validate inputs
                if (judulField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Judul pekerjaan harus diisi!");
                    return;
                }
                
                // Create Lowongan object
                Lowongan lowongan = new Lowongan();
                lowongan.setUserId(currentUserId);
                lowongan.setJudulPekerjaan(judulField.getText().trim());
                lowongan.setDeskripsi(deskripsiArea.getText().trim());
                lowongan.setKualifikasi(kualifikasiArea.getText().trim());
                lowongan.setJenisLowongan((String) jenisCombo.getSelectedItem());
                lowongan.setGaji(gajiField.getText().trim());
                lowongan.setLokasiKerja(lokasiField.getText().trim());
                lowongan.setDurasi(durasiField.getText().trim());
                lowongan.setStatus((String) statusCombo.getSelectedItem());
                
                // Parse deadline
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date utilDate = sdf.parse(deadlineField.getText().trim());
                    lowongan.setTanggalDeadline(new Date(utilDate.getTime()));
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(dialog, "Format tanggal deadline salah! Gunakan format yyyy-mm-dd");
                    return;
                }
                
                // Save to database
                boolean success = kelolaLowonganDAO.tambahLowongan(lowongan);
                
                if (success) {
                    JOptionPane.showMessageDialog(dialog, "Lowongan berhasil ditambahkan!");
                    dialog.dispose();
                    loadData(); // Refresh data
                } else {
                    JOptionPane.showMessageDialog(dialog, "Gagal menambahkan lowongan!");
                }
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });
        
        batalButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(simpanButton);
        buttonPanel.add(batalButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }
    
    // ========== VIEW FUNCTIONALITY ==========
    private void lihatLowongan(Lowongan lowongan) {
        JDialog dialog = new JDialog(this, "Detail Lowongan", true);
        dialog.setSize(600, 700);
        dialog.setLocationRelativeTo(this);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Detail Lowongan");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        // Add details
        addDetailRow(contentPanel, "Judul Pekerjaan", lowongan.getJudulPekerjaan());
        addDetailRow(contentPanel, "Perusahaan", lowongan.getNamaPerusahaan());
        addDetailRow(contentPanel, "Jenis Lowongan", lowongan.getJenisLowongan());
        addDetailRow(contentPanel, "Status", lowongan.getStatus());
        addDetailRow(contentPanel, "Lokasi Kerja", lowongan.getLokasiKerja());
        addDetailRow(contentPanel, "Gaji", lowongan.getGaji());
        addDetailRow(contentPanel, "Durasi", lowongan.getDurasi());
        
        // Deadline
        String deadlineStr = "-";
        if (lowongan.getTanggalDeadline() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
            deadlineStr = sdf.format(lowongan.getTanggalDeadline());
        }
        addDetailRow(contentPanel, "Deadline", deadlineStr);
        
        // Posting date
        String postingStr = "-";
        if (lowongan.getTanggalPosting() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
            postingStr = sdf.format(lowongan.getTanggalPosting());
        }
        addDetailRow(contentPanel, "Tanggal Posting", postingStr);
        
        addDetailRow(contentPanel, "Jumlah Lamaran", String.valueOf(lowongan.getJumlahLamaran()));
        
        // Description
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        JLabel descLabel = new JLabel("Deskripsi:");
        descLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(descLabel);
        
        JTextArea descArea = new JTextArea(lowongan.getDeskripsi());
        descArea.setEditable(false);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setBackground(new Color(249, 250, 251));
        descArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setPreferredSize(new Dimension(500, 100));
        descScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(descScroll);
        
        // Qualifications
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        JLabel qualLabel = new JLabel("Kualifikasi:");
        qualLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        qualLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(qualLabel);
        
        JTextArea qualArea = new JTextArea(lowongan.getKualifikasi());
        qualArea.setEditable(false);
        qualArea.setLineWrap(true);
        qualArea.setWrapStyleWord(true);
        qualArea.setBackground(new Color(249, 250, 251));
        qualArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        JScrollPane qualScroll = new JScrollPane(qualArea);
        qualScroll.setPreferredSize(new Dimension(500, 80));
        qualScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(qualScroll);
        
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton tutupButton = new JButton("Tutup");
        tutupButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(tutupButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }
    
    private void addDetailRow(JPanel parent, String label, String value) {
        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        rowPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel labelComp = new JLabel(label + ":");
        labelComp.setFont(new Font("SansSerif", Font.BOLD, 14));
        labelComp.setPreferredSize(new Dimension(150, 20));
        
        JLabel valueComp = new JLabel(value != null ? value : "-");
        valueComp.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        rowPanel.add(labelComp);
        rowPanel.add(valueComp);
        parent.add(rowPanel);
    }
    
    // ========== UPDATE FUNCTIONALITY ==========
    private void editLowongan(Lowongan lowongan) {
        JDialog dialog = new JDialog(this, "Edit Lowongan", true);
        dialog.setSize(600, 700);
        dialog.setLocationRelativeTo(this);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Edit Lowongan");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Form fields with existing data
        JTextField judulField = new JTextField(lowongan.getJudulPekerjaan(), 30);
        JTextArea deskripsiArea = new JTextArea(lowongan.getDeskripsi(), 4, 30);
        deskripsiArea.setLineWrap(true);
        deskripsiArea.setWrapStyleWord(true);
        JScrollPane deskripsiScroll = new JScrollPane(deskripsiArea);
        
        JTextArea kualifikasiArea = new JTextArea(lowongan.getKualifikasi(), 3, 30);
        kualifikasiArea.setLineWrap(true);
        kualifikasiArea.setWrapStyleWord(true);
        JScrollPane kualifikasiScroll = new JScrollPane(kualifikasiArea);
        
        JComboBox<String> jenisCombo = new JComboBox<>(new String[]{"Magang", "Full-time", "Part-time", "Kontrak"});
        jenisCombo.setSelectedItem(lowongan.getJenisLowongan());
        
        JTextField gajiField = new JTextField(lowongan.getGaji(), 30);
        JTextField lokasiField = new JTextField(lowongan.getLokasiKerja(), 30);
        JTextField durasiField = new JTextField(lowongan.getDurasi(), 30);
        
        // Format deadline for display
        String deadlineStr = "";
        if (lowongan.getTanggalDeadline() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            deadlineStr = sdf.format(lowongan.getTanggalDeadline());
        }
        JTextField deadlineField = new JTextField(deadlineStr, 30);
        
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"aktif", "nonaktif"});
        statusCombo.setSelectedItem(lowongan.getStatus());
        
        // Add components to form
        int row = 0;
        
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Judul Pekerjaan:"), gbc);
        gbc.gridx = 1;
        formPanel.add(judulField, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Deskripsi:"), gbc);
        gbc.gridx = 1;
        formPanel.add(deskripsiScroll, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Kualifikasi:"), gbc);
        gbc.gridx = 1;
        formPanel.add(kualifikasiScroll, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Jenis Lowongan:"), gbc);
        gbc.gridx = 1;
        formPanel.add(jenisCombo, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Gaji:"), gbc);
        gbc.gridx = 1;
        formPanel.add(gajiField, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Lokasi Kerja:"), gbc);
        gbc.gridx = 1;
        formPanel.add(lokasiField, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Durasi:"), gbc);
        gbc.gridx = 1;
        formPanel.add(durasiField, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Deadline (yyyy-mm-dd):"), gbc);
        gbc.gridx = 1;
        formPanel.add(deadlineField, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        formPanel.add(statusCombo, gbc);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton updateButton = new JButton("Update");
        JButton batalButton = new JButton("Batal");
        
        updateButton.addActionListener(e -> {
            try {
                // Validate inputs
                if (judulField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Judul pekerjaan harus diisi!");
                    return;
                }
                
                // Update Lowongan object
                lowongan.setJudulPekerjaan(judulField.getText().trim());
                lowongan.setDeskripsi(deskripsiArea.getText().trim());
                lowongan.setKualifikasi(kualifikasiArea.getText().trim());
                lowongan.setJenisLowongan((String) jenisCombo.getSelectedItem());
                lowongan.setGaji(gajiField.getText().trim());
                lowongan.setLokasiKerja(lokasiField.getText().trim());
                lowongan.setDurasi(durasiField.getText().trim());
                lowongan.setStatus((String) statusCombo.getSelectedItem());
                
                // Parse deadline
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date utilDate = sdf.parse(deadlineField.getText().trim());
                    lowongan.setTanggalDeadline(new Date(utilDate.getTime()));
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(dialog, "Format tanggal deadline salah! Gunakan format yyyy-mm-dd");
                    return;
                }
                
                // Update in database
                boolean success = kelolaLowonganDAO.updateLowongan(lowongan);
                
                if (success) {
                    JOptionPane.showMessageDialog(dialog, "Lowongan berhasil diupdate!");
                    dialog.dispose();
                    loadData(); // Refresh data
                } else {
                    JOptionPane.showMessageDialog(dialog, "Gagal mengupdate lowongan!");
                }
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });
        
        batalButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(updateButton);
        buttonPanel.add(batalButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }
    
    // ========== DELETE FUNCTIONALITY ==========
    private void hapusLowongan(Lowongan lowongan) {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Apakah Anda yakin ingin menghapus lowongan '" + lowongan.getJudulPekerjaan() + "'?", 
            "Konfirmasi Hapus", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean success = kelolaLowonganDAO.deleteLowongan(lowongan.getLowonganId());
                
                if (success) {
                    JOptionPane.showMessageDialog(this, 
                        "Lowongan berhasil dihapus!", 
                        "Sukses", 
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    // Refresh data
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Gagal menghapus lowongan!", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Error: " + e.getMessage(), 
                    "Database Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
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
            java.util.logging.Logger.getLogger(KelolaLowongan.class.getName())
                .log(java.util.logging.Level.SEVERE, null, ex);
        }

        SwingUtilities.invokeLater(() -> {
            // Gunakan user ID 13 sesuai data Anda
            new KelolaLowongan(13).setVisible(true);
        });
    }
}

