package Mahasiswa;

import Components.MahasiswaNavigation;
import Database.ProfileDAO;
import Models.ProfilMahasiswa;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;

// Placeholder for the missing EditAkademikDialog class
class EditAkademikDialog extends JOptionPane {
    private String updatedData;

    public EditAkademikDialog(JFrame parent, String currentData) {
        Map<String, String> academicData = parseKeyValueString(currentData);

        JPanel panel = new JPanel(new MigLayout("wrap 2", "[align label]15[grow,fill]"));
        String[] labels = {"NIM", "Fakultas", "Program Studi", "Angkatan", "Semester", "IPK"};
        Map<String, JTextField> fields = new LinkedHashMap<>();

        for (String label : labels) {
            panel.add(new JLabel(label + ":"));
            JTextField field = new JTextField(academicData.getOrDefault(label, ""));
            panel.add(field, "growx");
            fields.put(label, field);
        }

        int result = showConfirmDialog(parent, panel, "Edit Informasi Akademik", OK_CANCEL_OPTION, PLAIN_MESSAGE);
        if (result == OK_OPTION) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, JTextField> entry : fields.entrySet()) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append(entry.getKey()).append(":").append(entry.getValue().getText().trim());
            }
            this.updatedData = sb.toString();
        }
    }

    public String getUpdatedData() {
        return updatedData;
    }
    
    private Map<String, String> parseKeyValueString(String text) {
        Map<String, String> map = new LinkedHashMap<>();
        if (text == null || text.trim().isEmpty()) {
            return map;
        }
        String[] pairs = text.split(",");
        for (String pair : pairs) {
            String[] kv = pair.split(":", 2);
            if (kv.length == 2) {
                map.put(kv[0].trim(), kv[1].trim());
            }
        }
        return map;
    }
}

// MODIFIED: EditPengalamanDialog is now completely rebuilt based on the new design.
class EditPengalamanDialog extends JOptionPane {
    private String updatedPengalaman;
    private boolean saved = false;

    public EditPengalamanDialog(JFrame parent) {
        // --- Create UI Components ---
        JPanel panel = new JPanel(new MigLayout("wrap 2, fillx", "[right]15[grow,fill]"));
        
        JTextField judulField = new JTextField();
        JComboBox<String> tipePekerjaanComboBox = new JComboBox<>(new String[]{"Pilih Tipe", "Purna Waktu", "Paruh Waktu", "Magang", "Kontrak", "Musiman"});
        JTextField namaPerusahaanField = new JTextField();
        JCheckBox saatIniCheckBox = new JCheckBox("Saya sedang bekerja di peran ini");

        String[] months = {"Bulan", "Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
        JComboBox<String> bulanMulaiComboBox = new JComboBox<>(months);
        JComboBox<String> bulanSelesaiComboBox = new JComboBox<>(months);

        Integer[] years = new Integer[50];
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for(int i=0; i<50; i++) {
            years[i] = currentYear - i;
        }
        JComboBox<Integer> tahunMulaiComboBox = new JComboBox<>(years);
        JComboBox<Integer> tahunSelesaiComboBox = new JComboBox<>(years);

        JPanel startDatePanel = new JPanel(new MigLayout("insets 0", "[grow, fill][grow, fill]"));
        startDatePanel.add(bulanMulaiComboBox);
        startDatePanel.add(tahunMulaiComboBox);
        
        JPanel endDatePanel = new JPanel(new MigLayout("insets 0", "[grow, fill][grow, fill]"));
        endDatePanel.add(bulanSelesaiComboBox);
        endDatePanel.add(tahunSelesaiComboBox);

        JTextField lokasiField = new JTextField();
        JComboBox<String> tipeLokasiComboBox = new JComboBox<>(new String[]{"Pilih Tipe Lokasi", "On-site", "Hybrid", "Remote"});
        JTextArea deskripsiArea = new JTextArea(5, 20);
        deskripsiArea.setLineWrap(true);
        deskripsiArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(deskripsiArea);

        // --- Add components to panel ---
        panel.add(new JLabel("Judul*:"));
        panel.add(judulField, "growx");
        panel.add(new JLabel("Tipe Pekerjaan*:"));
        panel.add(tipePekerjaanComboBox, "growx");
        panel.add(new JLabel("Nama Perusahaan*:"));
        panel.add(namaPerusahaanField, "growx");
        panel.add(new JLabel("")); // empty label for alignment
        panel.add(saatIniCheckBox);
        panel.add(new JLabel("Tanggal Mulai*:"));
        panel.add(startDatePanel, "growx");
        panel.add(new JLabel("Tanggal Selesai*:"));
        panel.add(endDatePanel, "growx");
        panel.add(new JLabel("Lokasi:"));
        panel.add(lokasiField, "growx");
        panel.add(new JLabel("Tipe Lokasi:"));
        panel.add(tipeLokasiComboBox, "growx");
        panel.add(new JLabel("Deskripsi:"));
        panel.add(scrollPane, "growx, h 100!");

        // --- Add Logic ---
        saatIniCheckBox.addItemListener(e -> {
            boolean isChecked = e.getStateChange() == ItemEvent.SELECTED;
            bulanSelesaiComboBox.setEnabled(!isChecked);
            tahunSelesaiComboBox.setEnabled(!isChecked);
        });

        // --- Show Dialog ---
        int result = showConfirmDialog(parent, panel, "Tambah Pengalaman", OK_CANCEL_OPTION, PLAIN_MESSAGE);
        if (result == OK_OPTION) {
            // --- Data Collection and Validation ---
            String judul = judulField.getText().trim();
            String namaPerusahaan = namaPerusahaanField.getText().trim();
            if (judul.isEmpty() || namaPerusahaan.isEmpty() || tipePekerjaanComboBox.getSelectedIndex() == 0 || bulanMulaiComboBox.getSelectedIndex() == 0) {
                showMessageDialog(parent, "Judul, Tipe Pekerjaan, Nama Perusahaan, dan Tanggal Mulai wajib diisi.", "Input Tidak Valid", WARNING_MESSAGE);
                return;
            }

            String tipePekerjaan = (String) tipePekerjaanComboBox.getSelectedItem();
            String tanggalMulai = bulanMulaiComboBox.getSelectedItem() + " " + tahunMulaiComboBox.getSelectedItem();
            String tanggalSelesai = saatIniCheckBox.isSelected() ? "Saat ini" : bulanSelesaiComboBox.getSelectedItem() + " " + tahunSelesaiComboBox.getSelectedItem();
            if (!saatIniCheckBox.isSelected() && bulanSelesaiComboBox.getSelectedIndex() == 0) {
                 showMessageDialog(parent, "Tanggal Selesai wajib diisi jika tidak sedang bekerja.", "Input Tidak Valid", WARNING_MESSAGE);
                return;
            }
            
            String lokasi = lokasiField.getText().trim();
            String tipeLokasi = (String) tipeLokasiComboBox.getSelectedItem();
            String deskripsi = deskripsiArea.getText().trim();

            // Format: Judul||TipePekerjaan||NamaPerusahaan||TglMulai||TglSelesai||Lokasi||TipeLokasi||Deskripsi
            this.updatedPengalaman = String.join("||", judul, tipePekerjaan, namaPerusahaan, tanggalMulai, tanggalSelesai, lokasi, tipeLokasi, deskripsi);
            this.saved = true;
        }
    }

    public boolean isSaved() {
        return saved;
    }

    public String getUpdatedPengalaman() {
        return updatedPengalaman;
    }
}


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
        nameLabel.setText(currentProfile.getNama() + "'s Profile");
        displayAboutMe(currentProfile.getRingkasan());
        displayAcademicInfo(currentProfile.getPendidikan());
        displaySkills(currentProfile.getSkill());
        displayExperience(currentProfile.getPengalaman());
    }

    // =================================================================================
    // START OF MAJOR CHANGE: The structure of the experience panel is modified here.
    // =================================================================================
    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new MigLayout("wrap 1, fillx, insets 20 40 20 40, gapy 15", "[fill]"));
        contentPanel.setOpaque(false);
        
        // --- About Me Section ---
        aboutTextArea = new JTextArea();
        styleTextArea(aboutTextArea);
        aboutTextArea.setFocusable(false);
        contentPanel.add(createSectionPanel("Tentang Saya", aboutTextArea, true), "growx");
        
        // --- Academic Info Section ---
        academicInfoPanel = new JPanel(new MigLayout("wrap 4, fillx, gap 10 5", "[][grow][][grow]"));
        academicInfoPanel.setBackground(Color.WHITE);
        contentPanel.add(createSectionPanel("Informasi Akademik", academicInfoPanel, true), "growx");
        
        // --- Skills Section ---
        skillsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        skillsPanel.setBackground(Color.WHITE);
        contentPanel.add(createSectionPanel("Keahlian & Skill", skillsPanel, true), "growx");
        
        // --- Experience Section (REVISED STRUCTURE) ---
        // 1. The main panel that holds the list of experiences (using BoxLayout)
        experiencePanel = new JPanel();
        experiencePanel.setLayout(new BoxLayout(experiencePanel, BoxLayout.Y_AXIS));
        experiencePanel.setBackground(Color.WHITE);
        experiencePanel.setBorder(new EmptyBorder(0, 0, 15, 0)); // Add some bottom padding

        // 2. A wrapper panel for the button, guaranteeing left alignment
        JPanel buttonWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)); // FlowLayout aligned left
        buttonWrapper.setBackground(Color.WHITE);
        buttonWrapper.add(createAddExperienceButton());

        // 3. A container that combines the list and the button using BorderLayout
        JPanel experienceSectionContainer = new JPanel(new BorderLayout());
        experienceSectionContainer.setBackground(Color.WHITE);
        experienceSectionContainer.add(experiencePanel, BorderLayout.CENTER); // List goes in the center
        experienceSectionContainer.add(buttonWrapper, BorderLayout.SOUTH);    // Button goes at the bottom

        // 4. Finally, create the titled section panel with the new combined container
        //    The 'false' argument hides the "Edit" button from the title bar.
        contentPanel.add(createSectionPanel("Pengalaman", experienceSectionContainer, false), "growx");
        
        return contentPanel;
    }
    // =================================================================================
    // END OF MAJOR CHANGE
    // =================================================================================

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

    // MODIFIED: This method now ONLY populates the list. The button is no longer handled here.
    private void displayExperience(String experienceText) {
        experiencePanel.removeAll(); // Clear only the list panel
        if (experienceText != null && !experienceText.trim().isEmpty()) {
            String[] experiences = experienceText.split(";;");
            for (int i = 0; i < experiences.length; i++) {
                String[] parts = experiences[i].split("\\|\\|");
                if (parts.length == 8) {
                    JPanel expPanel = createExperienceEntry(parts, i);
                    experiencePanel.add(expPanel);
                    // Add separator between entries
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
            // Optionally, add a message if there are no experiences
            JLabel emptyLabel = new JLabel("Belum ada pengalaman yang ditambahkan.");
            emptyLabel.setForeground(EMPTY_TEXT_COLOR);
            emptyLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
            experiencePanel.add(emptyLabel);
        }
        
        experiencePanel.revalidate();
        experiencePanel.repaint();
    }

    private void handleEditAction(String title) {
        boolean success = false;
        
        switch (title) {
            case "Tentang Saya": {
                String currentVal = currentProfile.getRingkasan() != null ? currentProfile.getRingkasan() : "";
                String newValue = showMultilineInputDialog("Edit Tentang Saya", "Deskripsikan tentang diri Anda:", currentVal);
                if (newValue != null) {
                    success = profileDAO.updateRingkasan(currentUserId, newValue);
                }
                break;
            }
            case "Informasi Akademik": {
                EditAkademikDialog editDialog = new EditAkademikDialog(this, currentProfile.getPendidikan());
                String newValue = editDialog.getUpdatedData();
                if (newValue != null) {
                    success = profileDAO.updatePendidikan(currentUserId, newValue);
                }
                break;
            }
            case "Keahlian & Skill": {
                 String currentVal = currentProfile.getSkill() != null ? currentProfile.getSkill() : "";
                String newValue = showMultilineInputDialog("Edit Keahlian & Skill", "Masukkan keahlian Anda, dipisahkan koma (e.g., Java, Python, SQL):", currentVal);
                if (newValue != null) {
                    success = profileDAO.updateSkill(currentUserId, newValue);
                }
                break;
            }
            case "Pengalaman": { // This case is now triggered by the "Tambah Pengalaman" button
                EditPengalamanDialog pengalamanDialog = new EditPengalamanDialog(this);
                
                if (pengalamanDialog.isSaved()) {
                    String newExperience = pengalamanDialog.getUpdatedPengalaman();
                    String existingExperiences = currentProfile.getPengalaman() == null ? "" : currentProfile.getPengalaman();
                    
                    // Filter out the placeholder message if it exists
                    if(existingExperiences.trim().isEmpty() || existingExperiences.equals("Belum ada pengalaman yang ditambahkan.")){
                        existingExperiences = "";
                    }
                    
                    String finalExperiences = existingExperiences.isEmpty() ? newExperience : existingExperiences + ";;" + newExperience;
                    success = profileDAO.updatePengalaman(currentUserId, finalExperiences);
                }
                break;
            }
        }

        if (success) {
            JOptionPane.showMessageDialog(this, "Profil berhasil diperbarui!");
            loadProfileData();
        }
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
                boolean success = profileDAO.updatePengalaman(currentUserId, updatedPengalaman);
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
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.add(new JLabel(message), BorderLayout.NORTH);
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        int result = JOptionPane.showConfirmDialog(this, panel, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            return textArea.getText();
        }
        return null;
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
    
    class LinePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(new Color(229, 231, 235));
            g.fillRect(4, 5, 2, getHeight() - 5);
            g.setColor(new Color(156, 163, 175));
            g.fillOval(0, 5, 10, 10);
        }
    }

    private JPanel createExperienceEntry(String[] parts, int index) {
        String judul = parts[0];
        String tipePekerjaan = parts[1];
        String namaPerusahaan = parts[2];
        String tanggalMulai = parts[3];
        String tanggalSelesai = parts[4];
        String lokasi = parts[5];
        String tipeLokasi = parts[6];
        String deskripsi = parts[7];

        JPanel entryPanel = new JPanel(new BorderLayout(15, 0));
        entryPanel.setOpaque(false);
        entryPanel.setAlignmentX(Component.LEFT_ALIGNMENT); // Ensure the entry itself aligns left

        LinePanel linePanel = new LinePanel();
        linePanel.setOpaque(false);
        linePanel.setPreferredSize(new Dimension(15, 0));

        JPanel contentContainer = new JPanel(new BorderLayout(10, 0));
        contentContainer.setOpaque(false);
        contentContainer.setBorder(new EmptyBorder(2, 0, 0, 0));

        JLabel titleLabel = new JLabel(judul);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
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
        
        JLabel locationLabel = new JLabel(lokasi + (!tipeLokasi.equals("Pilih Tipe Lokasi") ? " (" + tipeLokasi + ")" : ""));
        locationLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        locationLabel.setForeground(Color.GRAY);

        JTextArea descArea = new JTextArea(deskripsi);
        styleTextArea(descArea);
        
        titlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        companyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        periodLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        locationLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        descArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        detailsPanel.add(titlePanel);
        detailsPanel.add(companyLabel);
        detailsPanel.add(periodLabel);
        if (!lokasi.isEmpty()) {
            detailsPanel.add(locationLabel);
        }
        if (!deskripsi.isEmpty()) {
            detailsPanel.add(Box.createVerticalStrut(8));
            detailsPanel.add(descArea);
        }

        contentContainer.add(detailsPanel, BorderLayout.CENTER);
        entryPanel.add(linePanel, BorderLayout.WEST);
        entryPanel.add(contentContainer, BorderLayout.CENTER);

        return entryPanel;
    }
    
    private JButton createAddExperienceButton() {
        JButton button = new JButton("+ Tambah Pengalaman Baru");
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setForeground(Color.GRAY);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);

        Border dashed = BorderFactory.createDashedBorder(Color.LIGHT_GRAY, 1.2f, 5, 2, true);
        button.setBorder(BorderFactory.createCompoundBorder(dashed, new EmptyBorder(10, 20, 10, 20)));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(new Color(59, 130, 246));
                Border newDashed = BorderFactory.createDashedBorder(new Color(59, 130, 246), 1.2f, 5, 2, true);
                button.setBorder(BorderFactory.createCompoundBorder(newDashed, new EmptyBorder(10, 20, 10, 20)));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(Color.GRAY);
                button.setBorder(BorderFactory.createCompoundBorder(dashed, new EmptyBorder(10, 20, 10, 20)));
            }
        });

        // The button now triggers the "Pengalaman" case in handleEditAction
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

    private void styleTextArea(JTextArea textArea) {
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setOpaque(false);
        textArea.setEditable(false);
        textArea.setFont(new Font("SansSerif", Font.PLAIN, 13));
        textArea.setForeground(NORMAL_TEXT_COLOR);
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
                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            ProfileDAO dummyDao = new ProfileDAO() {
                private ProfilMahasiswa dummyProfile = createDummyProfile();

                private ProfilMahasiswa createDummyProfile() {
                    ProfilMahasiswa profile = new ProfilMahasiswa();
                    profile.setNama("Jane Doe");
                    profile.setRingkasan("Mahasiswa Teknik Informatika yang bersemangat dengan keahlian dalam pengembangan perangkat lunak dan analisis data. Saat ini sedang mencari kesempatan magang untuk menerapkan dan mengembangkan keterampilan saya lebih lanjut.");
                    profile.setPendidikan("NIM:12345678,Fakultas:Ilmu Komputer,Program Studi:Teknik Informatika,Angkatan:2022,Semester:4,IPK:3.85");
                    profile.setSkill("Java,Python,SQL,Git,Spring Boot,Data Structures,React");
                    
                    String exp1 = "Frontend Developer Intern||Magang||PT Telkom Indonesia||Januari 2025||Saat ini||Bandung, Jawa Barat||Hybrid||Mengembangkan dan memelihara antarmuka pengguna untuk aplikasi web internal menggunakan React dan TypeScript.";
                    String exp2 = "Software Engineer Intern||Magang||Google Inc.||Juni 2024||Agustus 2024||Jakarta, Indonesia||On-site||Bertanggung jawab atas pengembangan fitur baru untuk produk Google Search, fokus pada optimisasi backend dan efisiensi query.";
                    profile.setPengalaman(exp1 + ";;" + exp2);
                    
                    return profile;
                }

                @Override
                public ProfilMahasiswa getProfileById(int userId) {
                    return dummyProfile;
                }
                @Override
                public boolean updateRingkasan(int userId, String ringkasan) { 
                    dummyProfile.setRingkasan(ringkasan);
                    System.out.println("Updated Ringkasan: " + ringkasan);
                    return true; 
                }
                @Override
                public boolean updatePendidikan(int userId, String pendidikan) { 
                    dummyProfile.setPendidikan(pendidikan);
                    System.out.println("Updated Pendidikan: " + pendidikan);
                    return true; 
                }
                @Override
                public boolean updateSkill(int userId, String skill) { 
                    dummyProfile.setSkill(skill);
                    System.out.println("Updated Skill: " + skill);
                    return true; 
                }
                @Override
                public boolean updatePengalaman(int userId, String pengalaman) { 
                    dummyProfile.setPengalaman(pengalaman);
                    System.out.println("Updated Pengalaman: " + pengalaman);
                    return true; 
                }
            };
            
            Profile profileFrame = new Profile(1);
            profileFrame.profileDAO = dummyDao; 
            profileFrame.loadProfileData(); 
            profileFrame.setVisible(true);
        });
    }
}