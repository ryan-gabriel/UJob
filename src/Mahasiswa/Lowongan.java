package Mahasiswa;

import Auth.SessionManager;
import Components.MahasiswaNavigation;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Lowongan extends JFrame {
    
    private final int currentUserId;

    public Lowongan() {
        this(SessionManager.getInstance().getId());
    }

    public Lowongan(int loggedInUserId) {
        this.currentUserId = loggedInUserId;

        if (this.currentUserId <= 0) {
            dispose();
            SwingUtilities.invokeLater(() -> 
                JOptionPane.showMessageDialog(null, "Sesi tidak ditemukan. Silakan login terlebih dahulu.", "Akses Ditolak", JOptionPane.ERROR_MESSAGE)
            );
            return;
        }

        // Validasi user ID ada di database
        if (!isValidUserId(this.currentUserId)) {
            dispose();
            SwingUtilities.invokeLater(() -> 
                JOptionPane.showMessageDialog(null, "User dengan ID " + this.currentUserId + " tidak ditemukan di database.", "User Tidak Valid", JOptionPane.ERROR_MESSAGE)
            );
            return;
        }

        initializeUI();
    }
    
    private void initializeUI() {
        String activeUser = SessionManager.getInstance().getUsername();
        setTitle("Lowongan Pekerjaan - UJob (User: " + activeUser + ")");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(248, 251, 255));
        
        mainPanel.add(new MahasiswaNavigation("Lowongan"), BorderLayout.NORTH);
        
        JPanel contentContainer = new JPanel(new BorderLayout(0, 20));
        contentContainer.setBorder(new EmptyBorder(20, 40, 20, 40));
        contentContainer.setOpaque(false);

        JLabel titleLabel = new JLabel("Lowongan Pekerjaan");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(new Color(31, 41, 55));
        contentContainer.add(titleLabel, BorderLayout.NORTH);

        JPanel jobsPanel = new JPanel();
        jobsPanel.setLayout(new BoxLayout(jobsPanel, BoxLayout.Y_AXIS));
        jobsPanel.setOpaque(false);
        
        populateJobsFromDatabase(jobsPanel);
        
        JScrollPane scrollPane = new JScrollPane(jobsPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(new Color(248, 251, 255));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        contentContainer.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(contentContainer, BorderLayout.CENTER);
        setContentPane(mainPanel);
    }

    private void populateJobsFromDatabase(JPanel jobsPanel) {
        Set<Integer> appliedLowonganIds = getAppliedLowonganIds(this.currentUserId);
        
        // Query yang sudah disesuaikan untuk menggunakan user_id langsung
        String sql = "SELECT l.lowongan_id, l.judul_pekerjaan, u.nama AS nama_perusahaan, l.jenis_lowongan, " +
                     "l.deskripsi, l.lokasi_kerja, l.gaji, l.durasi, l.tanggal_deadline " +
                     "FROM lowongan l " +
                     "JOIN user u ON l.user_id = u.user_id " +
                     "WHERE l.status = 'aktif' AND u.role = 'perusahaan' AND l.tanggal_deadline >= CURDATE() " +
                     "ORDER BY l.tanggal_posting DESC";

        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                LowonganData data = new LowonganData(
                    rs.getInt("lowongan_id"),
                    rs.getString("judul_pekerjaan"),
                    rs.getString("nama_perusahaan"),
                    rs.getString("jenis_lowongan"),
                    rs.getString("deskripsi"),
                    rs.getString("lokasi_kerja"),
                    rs.getString("gaji"),
                    rs.getString("durasi"),
                    "Deadline: " + rs.getDate("tanggal_deadline").toString(),
                    appliedLowonganIds.contains(rs.getInt("lowongan_id"))
                );
                
                addJobCard(jobsPanel, data);
            }

            if (!hasData) {
                JLabel noDataLabel = new JLabel("Tidak ada lowongan yang tersedia saat ini.");
                noDataLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
                noDataLabel.setForeground(Color.GRAY);
                noDataLabel.setHorizontalAlignment(JLabel.CENTER);
                jobsPanel.add(noDataLabel);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JLabel errorLabel = new JLabel("Gagal memuat data dari database. Error: " + e.getMessage());
            errorLabel.setForeground(Color.RED);
            jobsPanel.add(errorLabel);
        }
    }
    
    private void addJobCard(JPanel container, LowonganData data) {
        JPanel card = new JPanel(new MigLayout("wrap 1, fillx, insets 25", "[fill]"));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1));
        
        JPanel topRow = new JPanel(new MigLayout("insets 0", "[grow, left]push[right]"));
        topRow.setOpaque(false);

        JLabel titleLabel = new JLabel(data.title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        
        JLabel companyLabel = new JLabel(data.companyName);
        companyLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        companyLabel.setForeground(Color.GRAY);
        
        JPanel titleAndCompany = new JPanel();
        titleAndCompany.setLayout(new BoxLayout(titleAndCompany, BoxLayout.Y_AXIS));
        titleAndCompany.setOpaque(false);
        titleAndCompany.add(titleLabel);
        titleAndCompany.add(companyLabel);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setOpaque(false);
        
        if (data.hasApplied) {
            JButton appliedButton = new JButton("Sudah Dilamar");
            appliedButton.setFont(new Font("SansSerif", Font.BOLD, 14));
            appliedButton.setEnabled(false);
            appliedButton.setBackground(new Color(241, 245, 249));
            appliedButton.setForeground(new Color(100, 116, 139));
            appliedButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235)),
                new EmptyBorder(7, 19, 7, 19)
            ));
            buttonsPanel.add(appliedButton);
        } else {
            JButton lamarButton = createStyledButton("Lamar", true);
            lamarButton.addActionListener((ActionEvent e) -> {
                int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Apakah Anda yakin ingin melamar untuk posisi \"" + data.title + "\" di " + data.companyName + "?",
                    "Konfirmasi Lamaran",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );
                
                if (confirm == JOptionPane.YES_OPTION) {
                    handleLamarAction(data, this.currentUserId, (JFrame) SwingUtilities.getWindowAncestor(container));
                }
            });
            buttonsPanel.add(lamarButton);
        }

        topRow.add(titleAndCompany);
        topRow.add(buttonsPanel);

        JPanel tagRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        tagRow.setOpaque(false);
        Color tagBgColor, tagFgColor;
        switch (data.type.toLowerCase()) {
            case "magang":
                tagBgColor = new Color(219, 234, 254);
                tagFgColor = new Color(59, 130, 246);
                break;
            case "full time": case "kerja penuh":
                tagBgColor = new Color(220, 252, 231);
                tagFgColor = new Color(34, 197, 94);
                break;
            case "paruh waktu": case "part time":
                tagBgColor = new Color(254, 249, 195);
                tagFgColor = new Color(202, 138, 4);
                break;
            default:
                tagBgColor = new Color(229, 231, 235);
                tagFgColor = new Color(107, 114, 128);
                break;
        }
        tagRow.add(createTag(data.type, tagBgColor, tagFgColor));
        
        JTextArea descArea = new JTextArea(data.description);
        styleTextArea(descArea);
        
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(229, 231, 235));
        
        JPanel bottomRow = new JPanel(new MigLayout("insets 0", "[left, grow][center, grow][center, grow][right, grow]"));
        bottomRow.setOpaque(false);
        bottomRow.add(createInfoLabel("📍 " + data.location));
        bottomRow.add(createInfoLabel("💰 " + data.salary));
        bottomRow.add(createInfoLabel("🕒 " + data.duration));
        bottomRow.add(createInfoLabel(data.deadline));
        
        card.add(topRow, "growx");
        card.add(tagRow, "gapy 10");
        card.add(descArea, "gapy 10, growx");
        card.add(separator, "gapy 15, growx");
        card.add(bottomRow, "gapy 15, growx");
        
        container.add(card);
        container.add(Box.createRigidArea(new Dimension(0, 15)));
    }
    
    private void handleLamarAction(LowonganData data, int userId, JFrame frame) {
        // Validasi user_id dan lowongan_id terlebih dahulu
        if (!isValidUserId(userId)) {
            JOptionPane.showMessageDialog(frame, 
                "User ID tidak valid. Silakan login ulang.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!isValidLowonganId(data.lowonganId)) {
            JOptionPane.showMessageDialog(frame, 
                "Lowongan tidak valid atau sudah tidak aktif.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Cek apakah sudah pernah melamar
        if (hasAlreadyApplied(userId, data.lowonganId)) {
            JOptionPane.showMessageDialog(frame, 
                "Anda sudah pernah melamar di lowongan ini.", 
                "Gagal", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Query yang disederhanakan tanpa profil_id
        String insertSql = "INSERT INTO lamaran (user_id, lowongan_id, tanggal_lamar, status_lamaran) VALUES (?, ?, NOW(), 'dikirim')";
    
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
            
            insertStmt.setInt(1, userId);
            insertStmt.setInt(2, data.lowonganId);
            
            System.out.println("Attempting to insert: user_id=" + userId + ", lowongan_id=" + data.lowonganId);
            
            int affectedRows = insertStmt.executeUpdate();
    
            if (affectedRows > 0) {
                // Update jumlah lamaran di tabel lowongan
                updateJumlahLamaran(data.lowonganId);
                
                String pesanNotifikasi = String.format(
                    "Lamaran Anda untuk posisi \"%s\" di perusahaan %s telah berhasil dikirim.",
                    data.title, data.companyName
                );
                kirimNotifikasi(userId, pesanNotifikasi);

                JOptionPane.showMessageDialog(frame, "Lamaran Anda berhasil dikirim!", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
                new Lowongan(userId).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(frame, "Gagal mengirim lamaran.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("SQL Error Code: " + e.getErrorCode());
            System.err.println("SQL State: " + e.getSQLState());
            
            if (e.getErrorCode() == 1062) {
                JOptionPane.showMessageDialog(frame, "Anda sudah pernah melamar di lowongan ini.", "Gagal", JOptionPane.WARNING_MESSAGE);
            } else if (e.getErrorCode() == 1452) {
                JOptionPane.showMessageDialog(frame, "Data user atau lowongan tidak valid.", "Error Foreign Key", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Terjadi error pada database: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Method validasi user ID
    private boolean isValidUserId(int userId) {
        String sql = "SELECT COUNT(*) FROM user WHERE user_id = ? AND role = 'mahasiswa'";
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

    // Method validasi lowongan
    private boolean isValidLowonganId(int lowonganId) {
        String sql = "SELECT COUNT(*) FROM lowongan WHERE lowongan_id = ? AND status = 'aktif' AND tanggal_deadline >= CURDATE()";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, lowonganId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method untuk cek apakah sudah melamar
    private boolean hasAlreadyApplied(int userId, int lowonganId) {
        String sql = "SELECT COUNT(*) FROM lamaran WHERE user_id = ? AND lowongan_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            ps.setInt(2, lowonganId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method untuk update jumlah_lamaran
    private void updateJumlahLamaran(int lowonganId) {
        String updateSql = "UPDATE lowongan SET jumlah_lamaran = (SELECT COUNT(*) FROM lamaran WHERE lowongan_id = ?) WHERE lowongan_id = ?";
        
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(updateSql)) {
            
            ps.setInt(1, lowonganId);
            ps.setInt(2, lowonganId);
            ps.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Gagal mengupdate jumlah lamaran untuk lowongan ID: " + lowonganId);
        }
    }

    private void kirimNotifikasi(int userId, String pesan) {
        String sql = "INSERT INTO notifikasi (user_id, isi, waktu, dibaca) VALUES (?, ?, NOW(), 0)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            ps.setString(2, pesan);
            ps.executeUpdate();
            System.out.println("Notifikasi lamaran terkirim ke user ID: " + userId);

        } catch (SQLException e) {
            // Jika tabel notifikasi tidak ada, abaikan error ini
            System.err.println("Gagal mengirim notifikasi (tabel mungkin tidak ada): " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            // PASTIKAN USER ID INI ADA DI TABEL USER
            // Cek dulu dengan query: SELECT user_id, nama FROM user WHERE user_id = 21;
            SessionManager.getInstance().login(21, "Adit", "adit@upi.edu", "mahasiswa");
            
            // Debug
            System.out.println("Session User ID: " + SessionManager.getInstance().getId());

            new Lowongan().setVisible(true);
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

    private static class LowonganData {
        final int lowonganId;
        final String title, companyName, type, description, location, salary, duration, deadline;
        final boolean hasApplied;

        LowonganData(int lowonganId, String title, String companyName, String type, String description, String location, String salary, String duration, String deadline, boolean hasApplied) {
            this.lowonganId = lowonganId;
            this.title = title;
            this.companyName = companyName;
            this.type = type;
            this.description = description;
            this.location = location;
            this.salary = salary;
            this.duration = duration;
            this.deadline = deadline;
            this.hasApplied = hasApplied;
        }
    }
    
    private Set<Integer> getAppliedLowonganIds(int userId) {
        Set<Integer> ids = new HashSet<>();
        String sql = "SELECT lowongan_id FROM lamaran WHERE user_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ids.add(rs.getInt("lowongan_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ids;
    }
    
    private void styleTextArea(JTextArea textArea) {
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setOpaque(false);
        textArea.setEditable(false);
        textArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        textArea.setForeground(new Color(55, 65, 81));
    }

    private JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));
        label.setForeground(Color.GRAY);
        return label;
    }

    private JLabel createTag(String text, Color bgColor, Color fgColor) {
        JLabel tag = new JLabel(text);
        tag.setOpaque(true);
        tag.setBackground(bgColor);
        tag.setForeground(fgColor);
        tag.setFont(new Font("SansSerif", Font.BOLD, 12));
        tag.setBorder(new EmptyBorder(5, 10, 5, 10));
        return tag;
    }
    
    private JButton createStyledButton(String text, boolean primary) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        if (primary) {
            button.setBackground(new Color(59, 130, 246));
            button.setForeground(Color.WHITE);
            button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(59, 130, 246)),
                new EmptyBorder(7, 19, 7, 19)
            ));
            
            // Hover effect
            button.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    button.setBackground(new Color(37, 99, 235));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setBackground(new Color(59, 130, 246));
                }
            });
        }
        return button;
    }
}
