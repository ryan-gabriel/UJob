package Mahasiswa;

import Components.MahasiswaNavigation;
import Database.DatabaseConnection;
import Database.ProfileDAO;
import Models.ProfilMahasiswa;
// [PERUBAHAN] Mengganti import dari CariProyek ke ProyekSaya
import Mahasiswa.Portofolio;
import Mahasiswa.ProyekSaya; 
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class Profile extends JFrame {

    private int currentUserId;
    private ProfileDAO profileDAO;
    private ProfilMahasiswa currentProfile;

    private JLabel nameLabel;
    private JTextArea aboutTextArea;
    private JPanel academicInfoPanel;
    private JPanel skillsPanel;
    private JPanel experiencePanel;
    private final Color EMPTY_TEXT_COLOR = new Color(107, 114, 128);
    private final Color NORMAL_TEXT_COLOR = new Color(55, 65, 81);
    private final Color FRAME_BACKGROUND_COLOR = new Color(249, 250, 251);

    public Profile(int userId) {
        this.currentUserId = userId;
        this.profileDAO = new ProfileDAO(); 
        initializeUI();
        loadProfileData();
    }
    
    private void initializeUI() {
        setTitle("Profil Mahasiswa - UJob");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(FRAME_BACKGROUND_COLOR);
        
        mainPanel.add(new MahasiswaNavigation("Profile"), BorderLayout.NORTH);

        JPanel mainContainer = new JPanel(new MigLayout("wrap 1, fillx, insets 0", "[fill]"));
        mainContainer.setBackground(FRAME_BACKGROUND_COLOR);

        mainContainer.add(createProfileHeaderPanel(), "h 150!, growx");
        mainContainer.add(createContentPanel(), "growx");

        JScrollPane contentScrollPane = new JScrollPane(mainContainer);
        contentScrollPane.setBorder(BorderFactory.createEmptyBorder());
        contentScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        contentScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        mainPanel.add(contentScrollPane, BorderLayout.CENTER);
        add(mainPanel);
    }
    
    private void loadProfileData() {
        currentProfile = profileDAO.getProfileById(currentUserId);
        if (currentProfile == null) {
            JOptionPane.showMessageDialog(this, "Gagal memuat profil untuk user ID: " + currentUserId, "Error", JOptionPane.ERROR_MESSAGE);
            nameLabel.setText("User tidak ditemukan");
            return;
        }
        nameLabel.setText(currentProfile.getNama()); 
        displayAboutMe(currentProfile.getRingkasan());
        displayAcademicInfo(currentProfile.getPendidikan());
        displaySkills(currentProfile.getSkill());
        displayExperience(currentProfile.getPengalaman());
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new MigLayout("wrap 1, fillx, insets 20 40 20 40, gapy 15", "[fill]"));
        contentPanel.setOpaque(false);

        aboutTextArea = new JTextArea();
        styleTextArea(aboutTextArea);
        aboutTextArea.setFocusable(false);
        contentPanel.add(createSectionPanel("Tentang Saya", aboutTextArea, true), "growx");

        academicInfoPanel = new JPanel(new MigLayout("wrap 4, fillx, gap 10 5", "[][grow][][grow]"));
        academicInfoPanel.setBackground(Color.WHITE);
        contentPanel.add(createSectionPanel("Informasi Akademik", academicInfoPanel, true), "growx");

        skillsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        skillsPanel.setBackground(Color.WHITE);
        contentPanel.add(createSectionPanel("Keahlian & Skill", skillsPanel, true), "growx");

        experiencePanel = new JPanel();
        experiencePanel.setLayout(new BoxLayout(experiencePanel, BoxLayout.Y_AXIS));
        experiencePanel.setBackground(Color.WHITE);
        
        JPanel buttonWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonWrapper.setBackground(Color.WHITE);
        buttonWrapper.setBorder(new EmptyBorder(15, 0, 0, 0));
        buttonWrapper.add(createAddExperienceButton());

        JPanel experienceSectionContainer = new JPanel(new BorderLayout());
        experienceSectionContainer.setBackground(Color.WHITE);
        experienceSectionContainer.add(experiencePanel, BorderLayout.CENTER);
        experienceSectionContainer.add(buttonWrapper, BorderLayout.SOUTH);

        contentPanel.add(createSectionPanel("Pengalaman", experienceSectionContainer, false), "growx");

        return contentPanel;
    }
    
    private void handleEditAction(String title) {
        boolean success = false;
        
        switch (title) {
            case "Tentang Saya":
                String currentRingkasan = currentProfile.getRingkasan() != null ? currentProfile.getRingkasan() : "";
                String newRingkasan = showMultilineInputDialog("Edit Tentang Saya", "Deskripsikan tentang diri Anda:", currentRingkasan);
                if (newRingkasan != null) {
                    currentProfile.setRingkasan(newRingkasan);
                    success = profileDAO.updateOrInsertProfile(currentProfile);
                }
                break;
            
            case "Informasi Akademik":
                EditAkademikDialog akademikDialog = new EditAkademikDialog(this, currentProfile.getPendidikan());
                akademikDialog.setVisible(true);
                
                String newPendidikan = akademikDialog.getUpdatedData(); 
                if (newPendidikan != null) {
                    currentProfile.setPendidikan(newPendidikan);
                    success = profileDAO.updateOrInsertProfile(currentProfile);
                }
                break;

            case "Keahlian & Skill":
                String currentSkill = currentProfile.getSkill() != null ? currentProfile.getSkill() : "";
                String newSkill = showMultilineInputDialog("Edit Keahlian & Skill", "Masukkan keahlian (pisahkan koma):", currentSkill);
                if (newSkill != null) {
                    currentProfile.setSkill(newSkill);
                    success = profileDAO.updateOrInsertProfile(currentProfile);
                }
                break;

            case "Pengalaman":
                EditPengalamanDialog pengalamanDialog = new EditPengalamanDialog(this, null);
                pengalamanDialog.setVisible(true);
                
                if (pengalamanDialog.isSaved()) {
                    String newExperience = pengalamanDialog.getUpdatedPengalaman();
                    if (newExperience == null) break; 

                    String existingExperiences = currentProfile.getPengalaman() == null ? "" : currentProfile.getPengalaman();
                    if(existingExperiences.trim().isEmpty()){
                        existingExperiences = "";
                    }
                    
                    String finalExperiences = existingExperiences.isEmpty() ? newExperience : existingExperiences + ";;" + newExperience;
                    currentProfile.setPengalaman(finalExperiences);
                    success = profileDAO.updateOrInsertProfile(currentProfile);
                }
                break;
        }

        if (success) {
            JOptionPane.showMessageDialog(this, "Profil berhasil diperbarui!");
            loadProfileData();
        }
    }
    
    private JPanel createExperienceEntry(String[] parts, int index) {
        String judul = parts.length > 0 ? parts[0] : "N/A";
        String tipePekerjaan = parts.length > 1 ? parts[1] : "-";
        String namaPerusahaan = parts.length > 2 ? parts[2] : "-";
        String tanggalMulai = parts.length > 3 ? parts[3] : "-";
        String tanggalSelesai = parts.length > 4 ? parts[4] : "-";
        String lokasi = parts.length > 5 ? parts[5] : "";
        String tipeLokasi = parts.length > 6 ? parts[6] : "";
        String deskripsi = parts.length > 7 ? parts[7] : "";

        JPanel entryPanel = new JPanel(new BorderLayout(15, 0));
        entryPanel.setOpaque(false);
        entryPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel iconPanel = new JPanel();
        iconPanel.setOpaque(false);
        iconPanel.setLayout(new BoxLayout(iconPanel, BoxLayout.Y_AXIS));
        
        JLabel iconLabel = new JLabel();
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/work-icon.png"));
            if (icon.getImageLoadStatus() == java.awt.MediaTracker.ERRORED) throw new Exception();
            iconLabel.setIcon(icon);
        } catch (Exception e) {
            iconLabel.setText("🏢");
            iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        }
        iconPanel.add(iconLabel);
        
        entryPanel.add(iconPanel, BorderLayout.WEST);

        JPanel contentContainer = new JPanel(new BorderLayout());
        contentContainer.setOpaque(false);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        JLabel titleLabel = new JLabel(judul);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        JButton deleteButton = new JButton("Hapus");
        deleteButton.setForeground(Color.RED);
        deleteButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
        deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteButton.setBorder(null);
        deleteButton.setContentAreaFilled(false);
        deleteButton.addActionListener(e -> deleteExperience(index));
        titlePanel.add(deleteButton, BorderLayout.EAST);
        
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setOpaque(false);
        
        JLabel companyLabel = new JLabel(namaPerusahaan + " · " + tipePekerjaan);
        companyLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        JLabel periodLabel = new JLabel(tanggalMulai + " - " + tanggalSelesai);
        periodLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        periodLabel.setForeground(Color.GRAY);
        
        JLabel locationLabel = new JLabel(lokasi + (!tipeLokasi.equals("Pilih Tipe Lokasi") && !tipeLokasi.isEmpty() ? " (" + tipeLokasi + ")" : ""));
        locationLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        locationLabel.setForeground(Color.GRAY);

        JTextArea descArea = new JTextArea(deskripsi);
        styleTextArea(descArea);
        
        companyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        periodLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        locationLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        descArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        detailsPanel.add(companyLabel);
        detailsPanel.add(Box.createVerticalStrut(2));
        detailsPanel.add(periodLabel);
        if (!lokasi.isEmpty()) {
            detailsPanel.add(locationLabel);
        }
        if (!deskripsi.isEmpty()) {
            detailsPanel.add(Box.createVerticalStrut(8));
            detailsPanel.add(descArea);
        }

        contentContainer.add(titlePanel, BorderLayout.NORTH);
        contentContainer.add(detailsPanel, BorderLayout.CENTER);
        entryPanel.add(contentContainer, BorderLayout.CENTER);
        
        return entryPanel;
    }

    private JPanel createProfileHeaderPanel() {
        JPanel profileHeaderPanel = new JPanel(new MigLayout("insets 20 40 20 40", "[][grow][]"));
        profileHeaderPanel.setBackground(new Color(59, 130, 246));
        JLabel profileIcon = createProfileIcon();
        nameLabel = new JLabel();
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        nameLabel.setForeground(Color.WHITE);
        
        JButton portfolioBtn = createStyledButton("Portofolio", false);
        JButton proyekBtn = createStyledButton("Proyek", false);

        // Navigasi ke Portofolio
        portfolioBtn.addActionListener(e -> {
            int userId = Auth.SessionManager.getInstance().getId();
            new Portofolio(userId).setVisible(true);
            SwingUtilities.getWindowAncestor(this).dispose();
        });

        // [PERUBAHAN] Navigasi ke ProyekSaya
        proyekBtn.addActionListener(e -> {
            new ProyekSaya().setVisible(true);
            SwingUtilities.getWindowAncestor(this).dispose();
        });
        
        JPanel nameAndButtonsPanel = new JPanel(new MigLayout("wrap 1, insets 0"));
        nameAndButtonsPanel.setOpaque(false);
        nameAndButtonsPanel.add(nameLabel);
        
        JPanel bottomButtons = new JPanel(new MigLayout("insets 8 0 0 0"));
        bottomButtons.setOpaque(false);
        bottomButtons.add(portfolioBtn);
        bottomButtons.add(proyekBtn, "gapleft 10");
        nameAndButtonsPanel.add(bottomButtons);
        
        profileHeaderPanel.add(profileIcon, "h 90!, w 90!");
        profileHeaderPanel.add(nameAndButtonsPanel, "grow, gapleft 20");
        return profileHeaderPanel;
    }
    
    // ... (Sisa kode tidak ada perubahan)
    private void displayExperience(String experienceText) {
        experiencePanel.removeAll();
        if (experienceText != null && !experienceText.trim().isEmpty()) {
            String[] experiences = experienceText.split(";;");
            for (int i = 0; i < experiences.length; i++) {
                String[] parts = experiences[i].split("\\|\\|");
                if (parts.length >= 7) { 
                    JPanel expPanel = createExperienceEntry(parts, i);
                    experiencePanel.add(expPanel);
                    if (i < experiences.length - 1) {
                        experiencePanel.add(Box.createVerticalStrut(15));
                        JSeparator separator = new JSeparator();
                        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
                        experiencePanel.add(separator);
                        experiencePanel.add(Box.createVerticalStrut(15));
                    }
                }
            }
        } else {
            JLabel emptyLabel = new JLabel("Belum ada pengalaman yang ditambahkan.");
            emptyLabel.setForeground(EMPTY_TEXT_COLOR);
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            emptyLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
            experiencePanel.add(emptyLabel);
        }
        experiencePanel.revalidate();
        experiencePanel.repaint();
    }
    
    private void deleteExperience(int index) {
        int response = JOptionPane.showConfirmDialog(this, 
                "Apakah Anda yakin ingin menghapus pengalaman ini?", 
                "Konfirmasi Hapus", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.WARNING_MESSAGE);

        if (response == JOptionPane.YES_OPTION) {
            String currentPengalaman = currentProfile.getPengalaman();
            if (currentPengalaman == null || currentPengalaman.isEmpty()) return;

            ArrayList<String> experiences = new ArrayList<>(Arrays.asList(currentPengalaman.split(";;")));
            
            if (index >= 0 && index < experiences.size()) {
                experiences.remove(index);
                String updatedPengalaman = String.join(";;", experiences);
                currentProfile.setPengalaman(updatedPengalaman);
                boolean success = profileDAO.updateOrInsertProfile(currentProfile);

                if (success) {
                    JOptionPane.showMessageDialog(this, "Pengalaman berhasil dihapus.");
                    loadProfileData();
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal menghapus pengalaman.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private Map<String, String> parseKeyValueString(String text, String pairSeparator, String kvSeparator) {
        Map<String, String> map = new LinkedHashMap<>();
        if (text == null || text.trim().isEmpty()) {
            return map;
        }
        String[] pairs = text.split(pairSeparator);
        for (String pair : pairs) {
            String[] kv = pair.split(kvSeparator, 2);
            if (kv.length == 2) {
                map.put(kv[0].trim(), kv[1].trim());
            }
        }
        return map;
    }

    private String showMultilineInputDialog(String title, String message, String initialValue) {
        JTextArea textArea = new JTextArea(10, 50);
        textArea.setText(initialValue);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(480, 200));
        
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.add(new JLabel(message), BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        int result = JOptionPane.showConfirmDialog(this, panel, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            return textArea.getText();
        }
        return null;
    }
    
    private JPanel createSectionPanel(String title, Component content, boolean showEditButton) {
        JPanel sectionPanel = new JPanel(new BorderLayout());
        sectionPanel.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1));
        sectionPanel.setBackground(Color.WHITE);
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(new EmptyBorder(15, 15, 10, 15));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titlePanel.add(titleLabel, BorderLayout.WEST);

        if (showEditButton) {
            JButton editButton = new JButton("Edit");
            editButton.setForeground(new Color(59, 130, 246));
            editButton.setContentAreaFilled(false);
            editButton.setBorderPainted(false);
            editButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
            editButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            editButton.addActionListener((ActionEvent e) -> handleEditAction(title));
            titlePanel.add(editButton, BorderLayout.EAST);
        }

        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBorder(new EmptyBorder(0, 15, 15, 15));
        contentWrapper.setBackground(Color.WHITE);
        contentWrapper.add(content, BorderLayout.CENTER);
        
        sectionPanel.add(titlePanel, BorderLayout.NORTH);
        sectionPanel.add(contentWrapper, BorderLayout.CENTER);
        return sectionPanel;
    }
    
    private void styleTextArea(JTextArea textArea) {
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setOpaque(false);
        textArea.setEditable(false);
        textArea.setFont(new Font("SansSerif", Font.PLAIN, 13));
        textArea.setForeground(NORMAL_TEXT_COLOR);
    }
    
    private void displayAboutMe(String aboutText) {
        aboutTextArea.setText("");
        if (aboutText == null || aboutText.trim().isEmpty()) {
            aboutTextArea.setForeground(EMPTY_TEXT_COLOR);
            aboutTextArea.setText("Informasi 'Tentang Saya' masih kosong. Klik 'Edit' untuk menambahkan ringkasan diri Anda.");
        } else {
            aboutTextArea.setForeground(NORMAL_TEXT_COLOR);
            aboutTextArea.setText(aboutText);
        }
    }

    private void displayAcademicInfo(String academicText) {
        academicInfoPanel.removeAll();
        if (academicText == null || academicText.trim().isEmpty()) {
            JLabel emptyLabel = new JLabel("Informasi akademik masih kosong. Klik 'Edit' untuk menambahkan.");
            emptyLabel.setForeground(EMPTY_TEXT_COLOR);
            academicInfoPanel.add(emptyLabel, "span");
        } else {
            Map<String, String> academicData = parseKeyValueString(academicText, ",", ":");
            String[] labelsInOrder = {"NIM", "Fakultas", "Program Studi", "Angkatan", "Semester", "IPK"};
            for (int i = 0; i < labelsInOrder.length; i += 2) {
                String key1 = labelsInOrder[i];
                academicInfoPanel.add(new JLabel(key1 + ":"));
                academicInfoPanel.add(new JLabel(academicData.getOrDefault(key1, "-")), "growx");
                if (i + 1 < labelsInOrder.length) {
                    String key2 = labelsInOrder[i + 1];
                    academicInfoPanel.add(new JLabel(key2 + ":"));
                    academicInfoPanel.add(new JLabel(academicData.getOrDefault(key2, "-")), "growx, wrap");
                }
            }
        }
        academicInfoPanel.revalidate();
        academicInfoPanel.repaint();
    }

    private void displaySkills(String skillsText) {
        skillsPanel.removeAll();
        if (skillsText == null || skillsText.trim().isEmpty()) {
            JLabel emptyLabel = new JLabel("Belum ada keahlian yang ditambahkan.");
            emptyLabel.setForeground(EMPTY_TEXT_COLOR);
            skillsPanel.add(emptyLabel);
        } else {
            String[] skills = skillsText.split(",");
            for (String skill : skills) {
                if (!skill.trim().isEmpty()) {
                    skillsPanel.add(createSkillButton(skill.trim()));
                }
            }
        }
        skillsPanel.revalidate();
        skillsPanel.repaint();
    }
    
    private JButton createAddExperienceButton() {
        JButton button = new JButton("+ Tambah Pengalaman Baru");
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setForeground(new Color(59, 130, 246));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);

        Border dashed = BorderFactory.createDashedBorder(new Color(156, 163, 175), 1.2f, 5, 2, true);
        button.setBorder(BorderFactory.createCompoundBorder(dashed, new EmptyBorder(10, 20, 10, 20)));
        button.setPreferredSize(new Dimension(500, 40));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                Border newDashed = BorderFactory.createDashedBorder(new Color(59, 130, 246), 1.2f, 5, 2, true);
                button.setBorder(BorderFactory.createCompoundBorder(newDashed, new EmptyBorder(10, 20, 10, 20)));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBorder(BorderFactory.createCompoundBorder(dashed, new EmptyBorder(10, 20, 10, 20)));
            }
        });

        button.addActionListener(e -> handleEditAction("Pengalaman"));
        
        return button;
    }

    private JLabel createProfileIcon() {
        JLabel profileIcon = new JLabel();
        profileIcon.setPreferredSize(new Dimension(90, 90));
        profileIcon.setHorizontalAlignment(SwingConstants.CENTER);
        profileIcon.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/profile.png"));
            if (icon.getImageLoadStatus() == java.awt.MediaTracker.ERRORED) {
                throw new Exception("Image not found");
            }
            profileIcon.setIcon(icon);
        } catch (Exception e) {
            profileIcon.setOpaque(true);
            profileIcon.setBackground(new Color(229, 231, 235));
            profileIcon.setText("Foto");
            profileIcon.setFont(new Font("SansSerif", Font.BOLD, 20));
            profileIcon.setForeground(new Color(107, 114, 128));
        }
        return profileIcon;
    }
    
    private JButton createSkillButton(String skillName) {
        JButton skillButton = new JButton(skillName);
        skillButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
        skillButton.setBackground(new Color(239, 246, 255));
        skillButton.setForeground(new Color(30, 64, 175));
        skillButton.setBorder(new EmptyBorder(6, 12, 6, 12));
        skillButton.setFocusPainted(false);
        return skillButton;
    }

    private JButton createStyledButton(String text, boolean primary) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        if (primary) {
            button.setBackground(Color.WHITE);
            button.setForeground(new Color(59, 130, 246));
            button.setBorder(new EmptyBorder(8, 20, 8, 20));
        } else {
            Color baseColor = new Color(30, 64, 175);
            Color hoverColor = new Color(37, 99, 235);
            
            button.setContentAreaFilled(true);
            button.setBackground(baseColor);
            button.setForeground(Color.WHITE);
            button.setBorder(new EmptyBorder(8, 20, 8, 20));
            
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setBackground(hoverColor);
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    button.setBackground(baseColor);
                }
            });
        }
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            if (DatabaseConnection.getConnection() == null) {
                JOptionPane.showMessageDialog(null, 
                    "Koneksi ke database gagal. Pastikan XAMPP/MySQL sudah berjalan.", 
                    "Koneksi Gagal", 
                    JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
            
            int userIdToView = 21;
            String username = "Adit";
            String email = "adit@upi.edu";
            String role = "mahasiswa";

            Auth.SessionManager.getInstance().login(userIdToView, username, email, role);
            
            Profile profileFrame = new Profile(userIdToView);
            profileFrame.setVisible(true);
        });
    }
}