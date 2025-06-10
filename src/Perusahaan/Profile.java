package Perusahaan;

import Auth.SessionManager;
import Components.PerusahaanNavigation;
import Database.ProfilePerusahaanDAO;
import Models.ProfilPerusahaan;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;

public class Profile extends JFrame {
  
    private JLabel nameLabel;
    private JLabel connectionLabel;
    private JLabel employeeLabel;
    private JLabel taglineLabel;
    private JLabel locationLabel;
    private JPanel companyInfoPanel;
    
    // Data perusahaan dari database
    private ProfilPerusahaan currentProfile;
    private ProfilePerusahaanDAO profileDAO;
    
    // Session Manager
    private SessionManager sessionManager;

    public Profile() {
        this.sessionManager = SessionManager.getInstance();
        this.profileDAO = new ProfilePerusahaanDAO();
        
        // Cek apakah user sudah login
        if (!sessionManager.isLoggedIn()) {
            JOptionPane.showMessageDialog(null, 
                "Anda harus login terlebih dahulu!", 
                "Akses Ditolak", 
                JOptionPane.WARNING_MESSAGE);
            // Redirect ke halaman login atau tutup aplikasi
            System.exit(0);
            return;
        }
        
        initializeUI();
        loadDataFromDatabase();
    }

    private void initializeUI() {
        setTitle("Profil Perusahaan - UJob (" + sessionManager.getUsername() + ")");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(249, 250, 251));

        // Navigation Header
        PerusahaanNavigation headerPanel = new PerusahaanNavigation("Profile");
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Main Content Container dengan padding
        JPanel outerContainer = new JPanel(new BorderLayout());
        outerContainer.setBackground(new Color(249, 250, 251));
        outerContainer.setBorder(new EmptyBorder(20, 40, 20, 40));

        JPanel mainContainer = new JPanel(new MigLayout("wrap 1, fillx, insets 0, gap 0", "[fill]"));
        mainContainer.setBackground(new Color(249, 250, 251));

        // Profile Header dengan gradient biru
        mainContainer.add(createProfileHeaderPanel(), "growx");
        
        // Content Panel putih
        mainContainer.add(createContentPanel(), "growx, gaptop 0");

        outerContainer.add(mainContainer, BorderLayout.CENTER);

        // Scroll Pane
        JScrollPane contentScrollPane = new JScrollPane(outerContainer);
        contentScrollPane.setBorder(BorderFactory.createEmptyBorder());
        contentScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        contentScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        contentScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainPanel.add(contentScrollPane, BorderLayout.CENTER);
        add(mainPanel);
    }

    private void loadDataFromDatabase() {
        try {
            // Menggunakan user ID dari session yang sedang login
            int currentUserId = sessionManager.getId();
            currentProfile = profileDAO.getProfilPerusahaanByUserId(currentUserId);
            
            if (currentProfile != null) {
                // Update UI dengan data dari database
                updateUIWithProfileData();
                displayCompanyInfo();
            } else {
                // Jika data tidak ditemukan, tampilkan pesan dan buat profil kosong
                int response = JOptionPane.showConfirmDialog(this, 
                    "Profil perusahaan belum dibuat untuk akun Anda.\n" +
                    "Apakah Anda ingin membuat profil baru?",
                    "Profil Tidak Ditemukan", 
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
                
                if (response == JOptionPane.YES_OPTION) {
                    // Redirect ke halaman create/edit profile
                    createEmptyProfile();
                } else {
                    loadDefaultDataForNewUser();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error memuat data: " + e.getMessage(),
                "Error Database", 
                JOptionPane.ERROR_MESSAGE);
            loadDefaultDataForNewUser();
        }
    }
    
    private void updateUIWithProfileData() {
        nameLabel.setText(currentProfile.getNama() != null ? currentProfile.getNama() : "Nama Perusahaan");
        connectionLabel.setText(String.valueOf(currentProfile.getJumlah_koneksi()));
        employeeLabel.setText(currentProfile.getJumlah_karyawan() > 0 ? 
            String.valueOf(currentProfile.getJumlah_karyawan()) + "+" : "0");
        
        // Update tagline jika ada deskripsi
        if (currentProfile.getDeskripsi_perusahaan() != null && !currentProfile.getDeskripsi_perusahaan().isEmpty()) {
            taglineLabel.setText(currentProfile.getDeskripsi_perusahaan());
        } else {
            taglineLabel.setText("Deskripsi perusahaan belum diisi");
        }
        
        // Update location dengan alamat dan email
        String locationText = "";
        if (currentProfile.getAlamat() != null && !currentProfile.getAlamat().isEmpty()) {
            locationText = currentProfile.getAlamat();
        }
        if (currentProfile.getEmail_kontak() != null && !currentProfile.getEmail_kontak().isEmpty()) {
            if (!locationText.isEmpty()) {
                locationText += " | ";
            }
            locationText += currentProfile.getEmail_kontak();
        }
        
        // Jika tidak ada data alamat/email, gunakan email dari session
        if (locationText.isEmpty()) {
            locationText = "Alamat belum diisi | " + sessionManager.getEmail();
        }
        
        locationLabel.setText(locationText);
    }
    
    private void createEmptyProfile() {
        // Buat profil kosong dengan data minimal dari session
        nameLabel.setText("Nama Perusahaan Belum Diisi");
        connectionLabel.setText("0");
        employeeLabel.setText("0");
        taglineLabel.setText("Deskripsi perusahaan belum diisi");
        locationLabel.setText("Alamat belum diisi | " + sessionManager.getEmail());
        
        displayEmptyCompanyInfo();
    }
    
    private void loadDefaultDataForNewUser() {
        // Data default untuk user baru
        nameLabel.setText("Nama Perusahaan Belum Diisi");
        connectionLabel.setText("0");
        employeeLabel.setText("0");
        taglineLabel.setText("Silakan lengkapi profil perusahaan Anda");
        locationLabel.setText("Alamat belum diisi | " + sessionManager.getEmail());
        displayEmptyCompanyInfo();
    }

    // Custom JPanel dengan gradient background
    class GradientPanel extends JPanel {
        private Color startColor;
        private Color endColor;
        
        public GradientPanel(Color startColor, Color endColor) {
            this.startColor = startColor;
            this.endColor = endColor;
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            
            int width = getWidth();
            int height = getHeight();
            
            GradientPaint gp = new GradientPaint(0, 0, startColor, 0, height, endColor);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, width, height);
        }
    }

    private JPanel createProfileHeaderPanel() {
        // Header biru dengan gradient - bagian atas saja
        GradientPanel profileHeaderPanel = new GradientPanel(
            new Color(59, 130, 246),  // Biru terang
            new Color(37, 99, 235)    // Biru sedikit lebih gelap
        );
        profileHeaderPanel.setLayout(new MigLayout("insets 30 30 30 30", "[][grow][]"));

        // Profile Icon bulat putih
        JLabel profileIcon = createProfileIcon();

        // Company Info Panel
        JPanel companyInfoMainPanel = new JPanel(new MigLayout("wrap 1, insets 0"));
        companyInfoMainPanel.setOpaque(false);

        // Company Name - putih besar
        nameLabel = new JLabel();
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        nameLabel.setForeground(Color.WHITE);

        // Company Tagline - abu terang
        taglineLabel = new JLabel();
        taglineLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        taglineLabel.setForeground(new Color(219, 234, 254));

        // Location and Email - abu terang
        locationLabel = new JLabel();
        locationLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        locationLabel.setForeground(new Color(191, 219, 254));

        // Stats Panel - koneksi dan karyawan
        JPanel statsPanel = new JPanel(new MigLayout("insets 15 0 0 0"));
        statsPanel.setOpaque(false);
        
        // Koneksi
        connectionLabel = new JLabel();
        connectionLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        connectionLabel.setForeground(Color.WHITE);
        
        JLabel connectionTextLabel = new JLabel("Koneksi");
        connectionTextLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        connectionTextLabel.setForeground(new Color(191, 219, 254));
        
        // Karyawan
        employeeLabel = new JLabel();
        employeeLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        employeeLabel.setForeground(Color.WHITE);
        
        JLabel employeeTextLabel = new JLabel("Karyawan");
        employeeTextLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        employeeTextLabel.setForeground(new Color(191, 219, 254));

        // Panel untuk stats
        JPanel connectionPanel = new JPanel(new MigLayout("wrap 1, insets 0"));
        connectionPanel.setOpaque(false);
        connectionPanel.add(connectionLabel);
        connectionPanel.add(connectionTextLabel);
        
        JPanel employeePanel = new JPanel(new MigLayout("wrap 1, insets 0"));
        employeePanel.setOpaque(false);
        employeePanel.add(employeeLabel);
        employeePanel.add(employeeTextLabel);
        
        statsPanel.add(connectionPanel);
        statsPanel.add(employeePanel, "gapleft 30");

        // Menambahkan ke company info panel
        companyInfoMainPanel.add(nameLabel);
        companyInfoMainPanel.add(taglineLabel, "gaptop 5");
        companyInfoMainPanel.add(locationLabel, "gaptop 5");
        companyInfoMainPanel.add(statsPanel);

        // Edit Profile Button - biru gelap
        JButton editProfileBtn = createEditProfileButton();

        profileHeaderPanel.add(profileIcon, "h 90!, w 90!");
        profileHeaderPanel.add(companyInfoMainPanel, "grow, gapleft 20");
        profileHeaderPanel.add(editProfileBtn, "aligny top");

        return profileHeaderPanel;
    }

    private JButton createEditProfileButton() {
        JButton editProfileBtn = new JButton("Edit Profile");
        editProfileBtn.setFocusPainted(false);
        editProfileBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        editProfileBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        
        // Warna biru gelap untuk button
        editProfileBtn.setBackground(new Color(32, 64, 121));
        editProfileBtn.setForeground(Color.WHITE);
        editProfileBtn.setBorder(new EmptyBorder(10, 20, 10, 20));
        editProfileBtn.setOpaque(true);
        editProfileBtn.setBorderPainted(false);
        
        // Action listener untuk edit profile
        editProfileBtn.addActionListener(e -> {
            // TODO: Implementasi halaman edit profile
            JOptionPane.showMessageDialog(this, 
                "Fitur edit profile akan segera tersedia!",
                "Info", 
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        // Hover effect
        editProfileBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                editProfileBtn.setBackground(new Color(25, 50, 95));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                editProfileBtn.setBackground(new Color(32, 64, 121));
            }
        });
        
        return editProfileBtn;
    }

    private JLabel createProfileIcon() {
        JLabel profileIcon = new JLabel();
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/company-logo.png"));
            profileIcon.setIcon(icon);
        } catch (Exception e) {
            // Icon default - lingkaran putih dengan teks
            profileIcon.setOpaque(true);
            profileIcon.setBackground(Color.WHITE);
            profileIcon.setText("LOGO");
            profileIcon.setFont(new Font("SansSerif", Font.BOLD, 16));
            profileIcon.setForeground(new Color(32, 64, 121));
        }
        profileIcon.setPreferredSize(new Dimension(90, 90));
        profileIcon.setHorizontalAlignment(SwingConstants.CENTER);
        profileIcon.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
        return profileIcon;
    }

    private JPanel createContentPanel() {
        // Panel putih untuk konten bawah
        JPanel contentPanel = new JPanel(new MigLayout("wrap 1, fillx, insets 0", "[fill]"));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1));

        // Inisialisasi komponen
        companyInfoPanel = new JPanel(new MigLayout("wrap 2, fillx, gap 40 15", "[140!][grow]"));
        companyInfoPanel.setBackground(Color.WHITE);

        // Menambahkan section informasi perusahaan
        String sectionTitle = "Informasi " + (currentProfile != null && currentProfile.getNama() != null ? 
            currentProfile.getNama() : sessionManager.getUsername());
        contentPanel.add(createSectionPanel(sectionTitle, companyInfoPanel), "growx");
        
        return contentPanel;
    }

    private JPanel createSectionPanel(String title, Component content) {
        JPanel sectionPanel = new JPanel(new BorderLayout());
        sectionPanel.setBackground(Color.WHITE);

        // Title Panel dengan Edit button
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(new EmptyBorder(20, 20, 15, 20));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(new Color(31, 41, 55));
        
        JButton editButton = new JButton("Edit");
        editButton.setForeground(new Color(59, 130, 246)); // Biru sesuai header
        editButton.setContentAreaFilled(false);
        editButton.setBorderPainted(false);
        editButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        editButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        
        // Action listener untuk edit button
        editButton.addActionListener(e -> {
            // TODO: Implementasi halaman edit informasi perusahaan
            JOptionPane.showMessageDialog(this, 
                "Fitur edit informasi perusahaan akan segera tersedia!",
                "Info", 
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        titlePanel.add(titleLabel, BorderLayout.WEST);
        titlePanel.add(editButton, BorderLayout.EAST);
        
        // Content Wrapper
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBorder(new EmptyBorder(0, 20, 20, 20));
        contentWrapper.setBackground(Color.WHITE);
        contentWrapper.add(content, BorderLayout.CENTER);
        
        sectionPanel.add(titlePanel, BorderLayout.NORTH);
        sectionPanel.add(contentWrapper, BorderLayout.CENTER);
        
        return sectionPanel;
    }

    private void displayCompanyInfo() {
        companyInfoPanel.removeAll();
        
        if (currentProfile != null) {
            // Data dari database
            String[][] companyData = {
                {"Bidang Industri", currentProfile.getJenis_industri() != null ? currentProfile.getJenis_industri() : "Belum diisi"},
                {"Alamat", currentProfile.getAlamat() != null ? currentProfile.getAlamat() : "Belum diisi"},
                {"Email Kontak", currentProfile.getEmail_kontak() != null ? currentProfile.getEmail_kontak() : sessionManager.getEmail()},
                {"Website Resmi", currentProfile.getWebsite_resmi() != null ? currentProfile.getWebsite_resmi() : "Belum diisi"},
                {"Kategori Produk/Layanan", currentProfile.getKategori_produk_layanan() != null ? currentProfile.getKategori_produk_layanan() : "Belum diisi"},
                {"Informasi Kontak Lainnya", currentProfile.getInformasi_kontak_lainnya() != null ? currentProfile.getInformasi_kontak_lainnya() : "Belum diisi"}
            };
            
            for (String[] row : companyData) {
                addInfoRow(row[0], row[1]);
            }
        } else {
            displayEmptyCompanyInfo();
        }
        
        companyInfoPanel.revalidate();
        companyInfoPanel.repaint();
    }
    
    private void displayEmptyCompanyInfo() {
        companyInfoPanel.removeAll();
        
        String[][] companyData = {
            {"Bidang Industri", "Belum diisi"},
            {"Alamat", "Belum diisi"},
            {"Email Kontak", sessionManager.getEmail()},
            {"Website Resmi", "Belum diisi"},
            {"Kategori Produk/Layanan", "Belum diisi"},
            {"Informasi Kontak Lainnya", "Belum diisi"}
        };
        
        for (String[] row : companyData) {
            addInfoRow(row[0], row[1]);
        }
        
        companyInfoPanel.revalidate();
        companyInfoPanel.repaint();
    }
    
    private void addInfoRow(String key, String value) {
        JLabel labelKey = new JLabel(key);
        labelKey.setFont(new Font("SansSerif", Font.PLAIN, 14));
        labelKey.setForeground(new Color(107, 114, 128));

        JLabel labelValue = new JLabel("<html>" + value.replace("\n", "<br>") + "</html>");
        labelValue.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        // Jika data belum diisi, tampilkan dengan warna abu-abu dan italic
        if (value.equals("Belum diisi")) {
            labelValue.setForeground(new Color(156, 163, 175));
            labelValue.setFont(new Font("SansSerif", Font.ITALIC, 14));
        } else {
            labelValue.setForeground(new Color(31, 41, 55));
        }
        
        companyInfoPanel.add(labelKey);
        companyInfoPanel.add(labelValue, "growx");
    }

    // Method untuk refresh data (bisa dipanggil setelah edit)
    public void refreshProfile() {
        loadDataFromDatabase();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set Look and Feel untuk tampilan yang lebih baik
               javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            // Simulasi login untuk testing
            SessionManager.getInstance().login(14, "PT Pactindo", "pactindo@pactindo.com", "company");
            
            new Profile().setVisible(true);
        });
    }
}
