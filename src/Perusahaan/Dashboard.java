package Perusahaan;

import Components.PerusahaanNavigation;
import Database.DatabaseConnection;
import Database.KelolaLowonganDAO;
import Database.KelolaLowonganDAO.ProfileStatus; // Import class status

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.BasicStroke;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import java.sql.Connection;

public class Dashboard extends JFrame {
    
    private int userId;
    private KelolaLowonganDAO kelolaLowonganDAO;
    private Connection connection;

    private JLabel lowonganAktifValueLabel;
    private JLabel lamaranMasukValueLabel;
    private JLabel dashboardTitleLabel;
    private JPanel statusProfileCard; 

    public static class InfoPerusahaan { 
        // Inner class ini tidak diubah, berguna untuk fitur lain
        private String nama;
        private String industri;
        private int jumlahKaryawan;
        private String deskripsi;
        public String getNama() { return nama; }
        public void setNama(String nama) { this.nama = nama; }
        public String getIndustri() { return industri; }
        public void setIndustri(String industri) { this.industri = industri; }
        public int getJumlahKaryawan() { return jumlahKaryawan; }
        public void setJumlahKaryawan(int jumlahKaryawan) { this.jumlahKaryawan = jumlahKaryawan; }
        public String getDeskripsi() { return deskripsi; }
        public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }
    }

    // Class untuk Custom Progress Bar, diadaptasi dari contoh mahasiswa
    class CircularProgressBar extends JPanel {
        private int progress;
        public void setProgress(int progress) {
            this.progress = progress;
            repaint();
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int size = Math.min(getWidth(), getHeight()) - 20;
            int strokeWidth = 12;
            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;
            g2.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.setColor(new Color(230, 230, 230));
            g2.drawArc(centerX - size/2, centerY - size/2, size, size, 90, 360);
            g2.setColor(getProgressColor(progress));
            int arcAngle = (int) (360 * (progress / 100.0));
            g2.drawArc(centerX - size/2, centerY - size/2, size, size, 90, -arcAngle);
            g2.setColor(new Color(60, 60, 60));
            g2.setFont(new Font("SansSerif", Font.BOLD, 28));
            String text = progress + "%";
            int textWidth = g2.getFontMetrics().stringWidth(text);
            g2.drawString(text, centerX - textWidth/2, centerY + 10);
            g2.dispose();
        }
    }

    public Dashboard(int activeUserId) {
        this.userId = activeUserId;
        initializeDatabase();
        initializeUI();
        loadAllData();
    }
    
    public Dashboard() {
        this(14); // Default: PT Abadi Jaya
    }

    private void initializeDatabase() {
        try {
            this.connection = DatabaseConnection.getConnection();
            this.kelolaLowonganDAO = new KelolaLowonganDAO(this.connection);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal koneksi: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadAllData() {
        loadStats();
        loadProfileStatus();
        loadCompanyName();
    }

    private void loadCompanyName() {
        if (kelolaLowonganDAO == null) return;
        InfoPerusahaan info = kelolaLowonganDAO.getInfoPerusahaanByUser(userId);
        if (info != null && info.getNama() != null) {
            dashboardTitleLabel.setText("Dashboard " + info.getNama());
        }
    }

    private void loadProfileStatus() {
        if (kelolaLowonganDAO == null) return;
        ProfileStatus status = kelolaLowonganDAO.getProfileStatus(this.userId);
        updateStatusProfileCard(status);
    }
    
    private void loadStats() {
        if (kelolaLowonganDAO == null) return;
        int lowonganAktifCount = kelolaLowonganDAO.getTotalLowonganAktifByUser(this.userId);
        int lamaranMasukCount = kelolaLowonganDAO.getTotalLamaranByUser(this.userId);
        lowonganAktifValueLabel.setText(String.valueOf(lowonganAktifCount));
        lamaranMasukValueLabel.setText(String.valueOf(lamaranMasukCount));
    }

    private void initializeUI() {
        setTitle("Dashboard Perusahaan - UJob");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(243, 244, 246));
        mainPanel.add(new PerusahaanNavigation("Dashboard"), BorderLayout.NORTH);
        mainPanel.add(createContentPanel(), BorderLayout.CENTER);
        add(mainPanel);
    }

    private JScrollPane createContentPanel() {
        JPanel contentPanel = new JPanel(new MigLayout("wrap 1, fillx, insets 30 50 30 50, gapy 30", "[fill]"));
        contentPanel.setOpaque(false);
        dashboardTitleLabel = new JLabel("Dashboard Perusahaan");
        dashboardTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        contentPanel.add(dashboardTitleLabel);
        contentPanel.add(createInboxCard(), "growx");
        JPanel bottomCardsPanel = new JPanel(new MigLayout("fill, wrap 2, gap 30", "[grow, 50%][grow, 50%]"));
        bottomCardsPanel.setOpaque(false);
        statusProfileCard = createStyledCard(); 
        bottomCardsPanel.add(statusProfileCard, "grow, h 100%");
        bottomCardsPanel.add(createActivityCard(), "grow, h 100%");
        contentPanel.add(bottomCardsPanel, "growx");
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }
    
    // Method ini sekarang membangun UI Status Profil secara dinamis
    private void updateStatusProfileCard(ProfileStatus status) {
        statusProfileCard.removeAll();
        statusProfileCard.setLayout(new BorderLayout(20, 0));

        JLabel title = new JLabel("Status Profile");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setBorder(new EmptyBorder(0, 0, 15, 0));
        statusProfileCard.add(title, BorderLayout.NORTH);

        if (status == null) {
            statusProfileCard.add(new JLabel("Gagal memuat status profil."));
        } else {
            CircularProgressBar progressBar = new CircularProgressBar();
            progressBar.setPreferredSize(new Dimension(160, 160));
            progressBar.setProgress(status.getCompletionPercentage());

            JPanel checklistPanel = new JPanel();
            checklistPanel.setOpaque(false);
            checklistPanel.setLayout(new BoxLayout(checklistPanel, BoxLayout.Y_AXIS));
            checklistPanel.add(Box.createVerticalGlue());
            checklistPanel.add(createChecklistItem("Mengisi Info Dasar", status.infoDasarComplete));
            checklistPanel.add(Box.createVerticalStrut(12));
            checklistPanel.add(createChecklistItem("Mengisi Deskripsi", status.deskripsiComplete));
            checklistPanel.add(Box.createVerticalStrut(12));
            checklistPanel.add(createChecklistItem("Mengisi Kontak & Website", status.kontakWebsiteComplete));
            checklistPanel.add(Box.createVerticalStrut(12));
            checklistPanel.add(createChecklistItem("Membuat Lowongan Pertama", status.lowonganPertamaComplete));
            checklistPanel.add(Box.createVerticalGlue());

            statusProfileCard.add(progressBar, BorderLayout.WEST);
            statusProfileCard.add(checklistPanel, BorderLayout.CENTER);
        }

        statusProfileCard.revalidate();
        statusProfileCard.repaint();
    }

    private JPanel createChecklistItem(String text, boolean isComplete) {
        JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        itemPanel.setOpaque(false);
        
        // Menggunakan sumber daya ikon dari classpath
        String iconPath = isComplete ? "/images/check.png" : "/images/cross.png";
        Color textColor = isComplete ? new Color(22, 163, 74) : new Color(220, 38, 38);
        
        JLabel iconLabel;
        try {
            iconLabel = new JLabel(new ImageIcon(getClass().getResource(iconPath)));
        } catch (Exception e) {
            // Fallback jika ikon tidak ditemukan
            iconLabel = new JLabel(isComplete ? "✓" : "✗");
            iconLabel.setForeground(textColor);
            iconLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        }
        
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
        textLabel.setForeground(new Color(55, 65, 81));
        
        itemPanel.add(iconLabel);
        itemPanel.add(textLabel);
        return itemPanel;
    }
    
    private Color getProgressColor(int percentage) {
        if (percentage < 50) return new Color(239, 68, 68); // Merah
        if (percentage < 100) return new Color(245, 158, 11); // Kuning
        return new Color(34, 197, 94); // Hijau
    }
    
    // ... Sisa method tidak diubah (createInboxCard, createActivityCard, dll.) ...
    private JPanel createInboxCard() {
        JPanel card = createStyledCard(); card.setLayout(new BorderLayout());
        JLabel title = new JLabel("Inbox Terbaru"); title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setBorder(new EmptyBorder(0,0,15,0));
        card.add(title, BorderLayout.NORTH); 
        // Konten statis untuk contoh
        JPanel content = new JPanel(); content.setOpaque(false);
        content.add(new JLabel("Belum ada pesan baru."));
        card.add(content, BorderLayout.CENTER);
        return card;
    }
    private JPanel createActivityCard() {
        JPanel card = createStyledCard(); card.setLayout(new MigLayout("wrap 1, fillx, gapy 10", "[fill]"));
        JLabel title = new JLabel("Aktivitas Perusahaan"); title.setFont(new Font("SansSerif", Font.BOLD, 18));
        card.add(title, "wrap, gapbottom 15");
        lowonganAktifValueLabel = new JLabel("0"); lamaranMasukValueLabel = new JLabel("0");
        card.add(createActivityItem("Lowongan Aktif", lowonganAktifValueLabel), "growx");
        card.add(createActivityItem("Lamaran Masuk", lamaranMasukValueLabel), "growx"); return card;
    }
    private JPanel createActivityItem(String label, JLabel valueLabel) {
        JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0)); itemPanel.setOpaque(false);
        JLabel icon = new JLabel("📄"); icon.setFont(new Font("SansSerif", Font.PLAIN, 18));
        JLabel labelText = new JLabel(label + " :"); labelText.setFont(new Font("SansSerif", Font.PLAIN, 15));
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        itemPanel.add(icon); itemPanel.add(labelText); itemPanel.add(valueLabel); return itemPanel;
    }
    private JPanel createStyledCard() {
        JPanel card = new JPanel(); card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235), 1), new EmptyBorder(25, 25, 25, 25)));
        return card;
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(() -> new Dashboard(14).setVisible(true));
    }
}