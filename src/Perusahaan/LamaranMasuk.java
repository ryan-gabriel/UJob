package Perusahaan;

import Auth.SessionManager;
import Components.PerusahaanNavigation;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LamaranMasuk extends JFrame {

    private final int currentUserId;
    private JPanel mainContentPanel;

    public LamaranMasuk() {
        this(SessionManager.getInstance().getId());
    }
    
    public LamaranMasuk(int userId) {
        this.currentUserId = userId;
        
        if (this.currentUserId <= 0) {
            dispose();
            SwingUtilities.invokeLater(() -> 
                JOptionPane.showMessageDialog(null, "Sesi tidak ditemukan. Silakan login terlebih dahulu.", "Akses Ditolak", JOptionPane.ERROR_MESSAGE)
            );
            return;
        }

        // Validasi apakah user adalah perusahaan
        if (!isValidPerusahaanUser(this.currentUserId)) {
            dispose();
            SwingUtilities.invokeLater(() -> 
                JOptionPane.showMessageDialog(null, "Akses ditolak. Hanya perusahaan yang dapat mengakses halaman ini.", "Akses Ditolak", JOptionPane.ERROR_MESSAGE)
            );
            return;
        }

        initializeUI();
    }
    
    private boolean isValidPerusahaanUser(int userId) {
        String sql = "SELECT COUNT(*) FROM user WHERE user_id = ? AND role = 'perusahaan'";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private void initializeUI() {
        setTitle("Lamaran Masuk - UJob (Perusahaan: " + SessionManager.getInstance().getUsername() + ")");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(new PerusahaanNavigation("Lamaran Masuk"), BorderLayout.NORTH);

        mainContentPanel = createContentPanel();
        JScrollPane scrollPane = new JScrollPane(mainContentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(new Color(243, 244, 246));

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        setContentPane(mainPanel);
        
        refreshContent();
    }
    
    private void refreshContent() {
        mainContentPanel.removeAll();
        mainContentPanel.add(createTopPanel(this.currentUserId));
        mainContentPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        JPanel listHeaderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        listHeaderPanel.setOpaque(false);
        listHeaderPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel listHeader = new JLabel("Daftar Lamaran");
        listHeader.setFont(new Font("SansSerif", Font.BOLD, 22));
        listHeaderPanel.add(listHeader);
        
        mainContentPanel.add(listHeaderPanel);
        mainContentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainContentPanel.add(createApplicationListPanel(this.currentUserId));
        mainContentPanel.add(Box.createVerticalGlue());
        
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    private JPanel createContentPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(243, 244, 246));
        panel.setBorder(new EmptyBorder(25, 40, 25, 40));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return panel;
    }

    private JPanel createTopPanel(int userId) {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        topPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        JLabel titleLabel = new JLabel("Lamaran Masuk");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setVerticalAlignment(SwingConstants.TOP);
        
        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(createStatsPanel(userId), BorderLayout.EAST);
        
        return topPanel;
    }

    private int[] fetchStats(int userId) {
        int[] stats = new int[3]; // [totalLowongan, aktifLowongan, totalLamaran]
        
        // Query yang disesuaikan untuk menggunakan user_id langsung
        String totalLowonganSql = "SELECT COUNT(*) FROM lowongan WHERE user_id = ?";
        String aktifLowonganSql = "SELECT COUNT(*) FROM lowongan WHERE user_id = ? AND status = 'aktif'";
        String totalLamaranSql = "SELECT COUNT(*) FROM lamaran l JOIN lowongan lo ON l.lowongan_id = lo.lowongan_id WHERE lo.user_id = ?";

        try (Connection conn = DatabaseConnector.getConnection()) {
            // Total lowongan
            try (PreparedStatement ps = conn.prepareStatement(totalLowonganSql)) {
                ps.setInt(1, userId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) stats[0] = rs.getInt(1);
            }
            
            // Lowongan aktif
            try (PreparedStatement ps = conn.prepareStatement(aktifLowonganSql)) {
                ps.setInt(1, userId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) stats[1] = rs.getInt(1);
            }
            
            // Total lamaran
            try (PreparedStatement ps = conn.prepareStatement(totalLamaranSql)) {
                ps.setInt(1, userId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) stats[2] = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stats;
    }

    private JPanel createStatsPanel(int userId) {
        JPanel statsContainer = new JPanel();
        statsContainer.setLayout(new BoxLayout(statsContainer, BoxLayout.X_AXIS));
        statsContainer.setOpaque(false);

        int[] stats = fetchStats(userId);

        statsContainer.add(createStatCard(String.valueOf(stats[0]), "Total Lowongan"));
        statsContainer.add(Box.createRigidArea(new Dimension(20, 0)));
        statsContainer.add(createStatCard(String.valueOf(stats[1]), "Lowongan Aktif"));
        statsContainer.add(Box.createRigidArea(new Dimension(20, 0)));
        statsContainer.add(createStatCard(String.valueOf(stats[2]), "Total Lamaran"));

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
    
    private JPanel createApplicationListPanel(int userId) {
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);
        listPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        List<LamaranData> lamaranList = new ArrayList<>();
        
        // Query yang disesuaikan untuk menggunakan user_id perusahaan langsung
        String sql = "SELECT l.lamaran_id, u.user_id, u.nama, lo.judul_pekerjaan, l.tanggal_lamar, l.status_lamaran, lo.lowongan_id " +
                     "FROM lamaran l " +
                     "JOIN user u ON l.user_id = u.user_id " +
                     "JOIN lowongan lo ON l.lowongan_id = lo.lowongan_id " +
                     "WHERE lo.user_id = ? " +
                     "ORDER BY l.tanggal_lamar DESC, l.lamaran_id DESC";
        
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()) {
                lamaranList.add(new LamaranData(
                    rs.getInt("lamaran_id"),
                    rs.getString("nama"),
                    rs.getString("judul_pekerjaan"),
                    rs.getTimestamp("tanggal_lamar"),
                    rs.getString("status_lamaran"),
                    rs.getInt("user_id"),
                    rs.getInt("lowongan_id")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JLabel errorLabel = new JLabel("Gagal memuat daftar lamaran. Error: " + e.getMessage());
            errorLabel.setForeground(Color.RED);
            listPanel.add(errorLabel);
            return listPanel;
        }

        if (lamaranList.isEmpty()) {
            JLabel noDataLabel = new JLabel("Belum ada lamaran yang masuk untuk perusahaan ini.");
            noDataLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
            noDataLabel.setForeground(Color.GRAY);
            noDataLabel.setHorizontalAlignment(JLabel.CENTER);
            listPanel.add(noDataLabel);
        } else {
            for (LamaranData data : lamaranList) {
                listPanel.add(createApplicationCard(data));
                listPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            }
        }
        
        return listPanel;
    }
    
    private JPanel createApplicationCard(LamaranData data) {
        JPanel cardPanel = new JPanel(new GridBagLayout());
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235)),
            new EmptyBorder(15, 20, 15, 20)
        ));
        cardPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        GridBagConstraints gbc = new GridBagConstraints();
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        
        JLabel nameLabel = new JLabel(data.applicantName);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(nameLabel);
        
        infoPanel.add(Box.createRigidArea(new Dimension(0, 4)));

        JLabel jobLabel = new JLabel("Melamar untuk posisi: " + data.jobTitle);
        jobLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        jobLabel.setForeground(new Color(107, 114, 128));
        jobLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(jobLabel);

        infoPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy, HH:mm", new Locale("id", "ID"));
        JLabel dateLabel = new JLabel("Dikirim pada: " + sdf.format(data.applicationTimestamp));
        dateLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        dateLabel.setForeground(Color.GRAY);
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(dateLabel);
        
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setOpaque(false);
        
        if ("dikirim".equalsIgnoreCase(data.status)) {
            JButton terimaButton = new JButton("Terima");
            styleActionButton(terimaButton, new Color(22, 163, 74), new Dimension(90, 35));
            terimaButton.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Apakah Anda yakin ingin menerima lamaran dari " + data.applicantName + "?",
                    "Konfirmasi Penerimaan",
                    JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    updateLamaranStatus(data, "diterima");
                }
            });
            actionPanel.add(terimaButton);
            
            JButton tolakButton = new JButton("Tolak");
            styleActionButton(tolakButton, new Color(220, 38, 38), new Dimension(90, 35));
            tolakButton.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Apakah Anda yakin ingin menolak lamaran dari " + data.applicantName + "?",
                    "Konfirmasi Penolakan",
                    JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    updateLamaranStatus(data, "ditolak");
                }
            });
            actionPanel.add(tolakButton);
        } else {
            JLabel statusLabel = new JLabel(data.status.substring(0, 1).toUpperCase() + data.status.substring(1));
            statusLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
            statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
            statusLabel.setOpaque(true);
            statusLabel.setBorder(new EmptyBorder(8, 18, 8, 18));
            statusLabel.setForeground(Color.WHITE);
            statusLabel.setBackground(getStatusColor(data.status));
            actionPanel.add(statusLabel);
        }
        
        gbc.gridx = 0; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
        cardPanel.add(infoPanel, gbc);
        
        gbc.gridx = 1; gbc.weightx = 0; gbc.anchor = GridBagConstraints.EAST;
        cardPanel.add(actionPanel, gbc);
        
        return cardPanel;
    }
    
    private void updateLamaranStatus(LamaranData data, String newStatus) {
        String sql = "UPDATE lamaran SET status_lamaran = ? WHERE lamaran_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, newStatus);
            ps.setInt(2, data.lamaranId);
            
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                // Kirim notifikasi ke pelamar
                String namaPerusahaan = SessionManager.getInstance().getUsername();
                String pesanNotifikasi;
                
                if ("diterima".equalsIgnoreCase(newStatus)) {
                    pesanNotifikasi = String.format(
                        "Selamat! Lamaran Anda untuk posisi \"%s\" di %s telah diterima. Silakan hubungi perusahaan untuk proses selanjutnya.",
                        data.jobTitle, namaPerusahaan
                    );
                } else if ("ditolak".equalsIgnoreCase(newStatus)) {
                    pesanNotifikasi = String.format(
                        "Lamaran Anda untuk posisi \"%s\" di %s tidak dapat kami terima saat ini. Terima kasih atas minat Anda.",
                        data.jobTitle, namaPerusahaan
                    );
                } else {
                    pesanNotifikasi = String.format(
                        "Status lamaran Anda untuk posisi \"%s\" di %s telah diperbarui menjadi: %s",
                        data.jobTitle, namaPerusahaan, newStatus
                    );
                }
                
                kirimNotifikasi(data.applicantUserId, pesanNotifikasi);
                
                JOptionPane.showMessageDialog(this, 
                    "Status lamaran berhasil diperbarui menjadi: " + newStatus, 
                    "Berhasil", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                refreshContent();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal memperbarui status.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi error pada database: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void kirimNotifikasi(int userId, String pesan) {
        String sql = "INSERT INTO notifikasi (user_id, isi, waktu, dibaca) VALUES (?, ?, NOW(), 0)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            ps.setString(2, pesan);
            ps.executeUpdate();
            System.out.println("Notifikasi terkirim ke user ID: " + userId);

        } catch (SQLException e) {
            // Jika tabel notifikasi tidak ada, abaikan error ini
            System.err.println("Gagal mengirim notifikasi (tabel mungkin tidak ada): " + e.getMessage());
        }
    }

    private void styleActionButton(JButton button, Color bgColor, Dimension size) {
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(8, 18, 8, 18));
        button.setPreferredSize(size);
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            Color originalColor = bgColor;
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(originalColor);
            }
        });
    }

    private Color getStatusColor(String status) {
        switch (status.toLowerCase()) {
            case "diterima": return new Color(34, 197, 94);
            case "ditolak": return new Color(239, 68, 68);
            case "diproses": return new Color(245, 158, 11);
            default: return Color.GRAY;
        }
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(() -> {
             try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            // PASTIKAN USER ID INI ADA DI DATABASE DAN ROLE-NYA 'perusahaan'
            // Ganti dengan user_id yang benar-benar ada
            SessionManager.getInstance().login(14, "PT Abadi Jaya", "abadi.jaya@perusahaan.com", "perusahaan");
            
            // Debug
            System.out.println("Session User ID: " + SessionManager.getInstance().getId());
            
            new LamaranMasuk().setVisible(true);
        });
    }

    static class DatabaseConnector {
        private static final String URL = "jdbc:mysql://localhost:3306/ujob";
        private static final String USER = "root";
        private static final String PASSWORD = "";

        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }
    }

    private static class LamaranData {
        final int lamaranId;
        final String applicantName;
        final String jobTitle;
        final Timestamp applicationTimestamp;
        final String status;
        final int applicantUserId;
        final int lowonganId;

        public LamaranData(int lamaranId, String applicantName, String jobTitle, Timestamp applicationTimestamp, String status, int applicantUserId, int lowonganId) {
            this.lamaranId = lamaranId;
            this.applicantName = applicantName;
            this.jobTitle = jobTitle;
            this.applicationTimestamp = applicationTimestamp;
            this.status = status;
            this.applicantUserId = applicantUserId;
            this.lowonganId = lowonganId;
        }
    }
}
