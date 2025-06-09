package Mahasiswa;

import net.miginfocom.swing.MigLayout;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import Components.ProyekHeaderPanel;
import Models.ProfilMahasiswa;
import Database.ProfileDAO;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class ProyekCariTeman extends JFrame {
    private JTextField searchField;
    private ProfileDAO profileDAO;
    private JPanel profilesPanel;
    private int currentUserId;

    public ProyekCariTeman(int userId) {
        this.currentUserId = userId;
        initializeUI();
        profileDAO = new ProfileDAO();
        loadProfiles();
    }

    private void initializeUI() {
        setTitle("UPI Job - Cari Teman");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 247, 250));

        // Header dengan tab "Cari Teman" aktif
        ProyekHeaderPanel headerPanel = new ProyekHeaderPanel("Cari Teman");
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Content area
        mainPanel.add(createContentPanel(), BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new MigLayout("wrap 1, fillx", "[grow]", "[]20[grow]"));
        contentPanel.setBackground(new Color(245, 247, 250));
        contentPanel.setBorder(new EmptyBorder(10, 40, 30, 40));

        // === TOP SEARCH ===
        JPanel topSection = new JPanel(new MigLayout("insets 0", "[80%][20%]", "[150]"));
        topSection.setOpaque(false);
        searchField = new JTextField();
        searchField.setFont(new Font("Arial", Font.PLAIN, 13));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        JButton searchBtn = createSearchButton();

        topSection.add(searchField, "growx");
        topSection.add(searchBtn, "growx, gapleft 10");
        contentPanel.add(topSection, "growx, pushx");

        // === SCROLLABLE PROFILES ===
        profilesPanel = createProfilesPanel();
        JScrollPane scrollPane = new JScrollPane(profilesPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(new Color(245, 247, 250));
        scrollPane.getViewport().setBackground(new Color(245, 247, 250));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        contentPanel.add(scrollPane, "grow");
        return contentPanel;
    }

    private void loadProfiles() {
        List<ProfilMahasiswa> profiles = profileDAO.getAllProfiles();
        createProfileCards(profiles);
    }

    private void createProfileCards(List<ProfilMahasiswa> profiles) {
        profilesPanel.removeAll();

        for (ProfilMahasiswa profile : profiles) {
            if (profile.getUserId() != currentUserId) {
                JPanel card = createProfileCard(profile);
                profilesPanel.add(card, "grow");
            }
        }

        profilesPanel.revalidate();
        profilesPanel.repaint();
    }

    private void searchProfiles(String query) {
        List<ProfilMahasiswa> filteredProfiles = profileDAO.searchProfiles(query);
        createProfileCards(filteredProfiles);
    }

    private JButton createSearchButton() {
        JButton searchBtn = new JButton("Cari");
        searchBtn.addActionListener(e -> {
            String query = searchField.getText().trim();
            searchProfiles(query);
        });
        searchBtn.setBackground(new Color(37, 64, 143));
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFont(new Font("Arial", Font.BOLD, 13));
        searchBtn.setFocusPainted(false);
        return searchBtn;
    }

    private JPanel createProfilesPanel() {
        JPanel panel = new JPanel(new MigLayout("wrap 2, gap 20 20, insets 0", "[grow][grow]", ""));
        panel.setBackground(new Color(245, 247, 250));
        return panel;
    }

private JPanel createProfileCard(ProfilMahasiswa profile) {
    JPanel card = new JPanel(new MigLayout("wrap 1, fillx, insets 4")); // Menggunakan MigLayout
    card.setBackground(Color.WHITE);
    card.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(230, 230, 230)),
        new EmptyBorder(20, 20, 20, 20)
    ));

    JLabel namaLabel = new JLabel(profile.getNama());
    namaLabel.setFont(new Font("Arial", Font.BOLD, 18));
    namaLabel.setForeground(new Color(37, 64, 143));
    card.add(namaLabel, "growx");

    JLabel pendidikanLabel = new JLabel(profile.getPendidikan());
    pendidikanLabel.setFont(new Font("Arial", Font.PLAIN, 14));
    pendidikanLabel.setForeground(Color.GRAY);
    card.add(pendidikanLabel, "growx");
    
    JLabel skillLabel = new JLabel(profile.getSkill());
    skillLabel.setFont(new Font("Arial", Font.PLAIN, 14));
    skillLabel.setForeground(Color.DARK_GRAY);
    card.add(skillLabel, "growx");
    
JLabel ringkasanLabel = new JLabel("<html><div style='width:300px'>" + profile.getRingkasan() + "</div></html>");
ringkasanLabel.setFont(new Font("Arial", Font.PLAIN, 14));
ringkasanLabel.setForeground(Color.DARK_GRAY);
ringkasanLabel.setVerticalAlignment(JLabel.TOP);
card.add(ringkasanLabel, "growx");

JLabel pengalamanLabel = new JLabel("<html><div style='width:300px'>" + profile.getPengalaman() + "</div></html>");
pengalamanLabel.setFont(new Font("Arial", Font.PLAIN, 14));
pengalamanLabel.setForeground(Color.DARK_GRAY);
pengalamanLabel.setVerticalAlignment(JLabel.TOP);
card.add(pengalamanLabel, "growx");

    JPanel buttonPanel = new JPanel(new MigLayout("insets 0, gap 10 0"));
    buttonPanel.setOpaque(false);

    JButton kirimPerminatanBtn = new JButton("Kirim Permintaan");
    kirimPerminatanBtn.setBackground(new Color(37, 64, 143));
    kirimPerminatanBtn.setForeground(Color.WHITE);
    kirimPerminatanBtn.setFont(new Font("Arial", Font.BOLD, 14));
    kirimPerminatanBtn.setFocusPainted(false);
    kirimPerminatanBtn.addActionListener(e -> sendConnectionRequest(profile.getUserId()));
    buttonPanel.add(kirimPerminatanBtn, "grow");

    JButton lihatProfilBtn = new JButton("Lihat Profil");
    lihatProfilBtn.setBackground(new Color(128, 128, 128));
    lihatProfilBtn.setForeground(Color.WHITE);
    lihatProfilBtn.setFont(new Font("Arial", Font.BOLD, 14));
    lihatProfilBtn.setFocusPainted(false);
    lihatProfilBtn.addActionListener(e -> viewProfile(profile.getUserId()));
    buttonPanel.add(lihatProfilBtn, "grow");

    card.add(buttonPanel, "align left, growx");

    return card;
}

    private void sendConnectionRequest(int targetUserId) {
        boolean requestSent = profileDAO.sendConnectionRequest(currentUserId, targetUserId);
        
        if (requestSent) {
            JOptionPane.showMessageDialog(
                this, 
                "Permintaan koneksi berhasil dikirim!", 
                "Berhasil", 
                JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(
                this, 
                "Gagal mengirim permintaan koneksi.", 
                "Kesalahan", 
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void viewProfile(int userId) {
        ProfilMahasiswa profile = profileDAO.getProfileById(userId);
        
        if (profile != null) {
            JDialog profilDialog = new JDialog(this, "Detail Profil", true);
            profilDialog.setLayout(new MigLayout("wrap 1, insets 20"));
            
            JLabel titleLabel = new JLabel("Profil " + profile.getNama());
            titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
            
            JPanel detailPanel = new JPanel(new MigLayout("wrap 2, gap 10"));
            detailPanel.add(new JLabel("Nama:"), "right");
            detailPanel.add(new JLabel(profile.getNama()), "left");
            
            detailPanel.add(new JLabel("Pendidikan:"), "right");
            detailPanel.add(new JLabel(profile.getPendidikan()), "left");
            
            detailPanel.add(new JLabel("Ringkasan:"), "right");
            detailPanel.add(new JLabel(profile.getRingkasan()), "left");
            
            detailPanel.add(new JLabel("Pengalaman:"), "right");
            detailPanel.add(new JLabel(profile.getPengalaman()), "left");
            
            detailPanel.add(new JLabel("Skill:"), "right");
            detailPanel.add(new JLabel(profile.getSkill()), "left");
            
            profilDialog.add(titleLabel, "align center");
            profilDialog.add(detailPanel, "grow");
            
            JButton tutupBtn = new JButton("Tutup");
            tutupBtn.addActionListener(e -> profilDialog.dispose());
            profilDialog.add(tutupBtn, "align center");
            
            profilDialog.pack();
            profilDialog.setLocationRelativeTo(this);
            profilDialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(
                this, 
                "Profil tidak ditemukan.", 
                "Kesalahan", 
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProyekCariTeman(1).setVisible(true));
    }
}
