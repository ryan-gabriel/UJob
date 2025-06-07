package Mahasiswa;

import Components.MahasiswaNavigation;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;

public class Profile extends JFrame {
    
    // Instance variables untuk komponen yang akan diisi dengan data
    private JLabel nameLabel;
    private JTextArea aboutTextArea;
    private JPanel academicInfoPanel;
    private JPanel skillsPanel;
    private JPanel experiencePanel;

    public Profile() {
        initializeUI();
        loadStaticData();
    }

    private void initializeUI() {
        setTitle("Profil Mahasiswa - UJob");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(249, 250, 251));

        // Navigation Header
        MahasiswaNavigation headerPanel = new MahasiswaNavigation("Profile");
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Main Content Container
        JPanel mainContainer = new JPanel(new MigLayout("wrap 1, fillx, insets 0", "[fill]"));
        mainContainer.setBackground(new Color(249, 250, 251));

        mainContainer.add(createProfileHeaderPanel(), "h 150!, growx");
        mainContainer.add(createContentPanel(), "growx");

        // Scroll Pane untuk konten
        JScrollPane contentScrollPane = new JScrollPane(mainContainer);
        contentScrollPane.setBorder(BorderFactory.createEmptyBorder());
        contentScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        contentScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        contentScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainPanel.add(contentScrollPane, BorderLayout.CENTER);
        add(mainPanel);
    }

    private void loadStaticData() {
        // Data Header
        nameLabel.setText("John Doe's");

        // Data Tentang Saya
        String aboutText = "Saya adalah mahasiswa Teknik Informatika UPI semester 6 yang passionate dalam pengembangan web dan mobile. " +
                           "Memiliki pengalaman dalam berbagai teknologi modern seperti React, Node.js, dan Python. " +
                           "Saya senang berkolaborasi dalam tim dan selalu eager to learn teknologi baru.\n\n" +
                           "Saat ini sedang fokus mengembangkan keahlian di bidang Full Stack Development dan " +
                           "tertarik untuk berkontribusi dalam proyek-proyek yang berdampak positif untuk pendidikan dan masyarakat.";
        aboutTextArea.setText(aboutText);

        // Muat data untuk setiap section
        displayAcademicInfo();
        displaySkills();
        displayExperience();
    }

    private JPanel createProfileHeaderPanel() {
        JPanel profileHeaderPanel = new JPanel(new MigLayout("insets 20 40 20 40", "[][grow][]"));
        profileHeaderPanel.setBackground(new Color(59, 130, 246));

        // Profile Icon
        JLabel profileIcon = createProfileIcon();

        // Name Label
        nameLabel = new JLabel();
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        nameLabel.setForeground(Color.WHITE);

        // Buttons
        JButton editProfileBtn = createStyledButton("Edit Profile", true);
        JButton portfolioBtn = createStyledButton("Portofolio", false);
        JButton proyekBtn = createStyledButton("Proyek", false);

        // Panel untuk nama dan tombol
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
        profileHeaderPanel.add(editProfileBtn, "aligny top");

        return profileHeaderPanel;
    }

    private JLabel createProfileIcon() {
        JLabel profileIcon = new JLabel();
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/profile.png"));
            profileIcon.setIcon(icon);
        } catch (Exception e) {
            profileIcon.setOpaque(true);
            profileIcon.setBackground(Color.WHITE);
            profileIcon.setText("Foto");
            profileIcon.setFont(new Font("SansSerif", Font.BOLD, 20));
            profileIcon.setForeground(Color.LIGHT_GRAY);
        }
        profileIcon.setPreferredSize(new Dimension(90, 90));
        profileIcon.setHorizontalAlignment(SwingConstants.CENTER);
        profileIcon.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
        return profileIcon;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new MigLayout("wrap 1, fillx, insets 20 40 20 40, gapy 15", "[fill]"));
        contentPanel.setOpaque(false);

        // Inisialisasi komponen
        aboutTextArea = new JTextArea();
        styleTextArea(aboutTextArea);
        
        academicInfoPanel = new JPanel(new MigLayout("wrap 4, fillx, gap 10 5", "[][grow][][grow]"));
        academicInfoPanel.setBackground(Color.WHITE);
        
        skillsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        skillsPanel.setBackground(Color.WHITE);
        
        experiencePanel = new JPanel();
        experiencePanel.setLayout(new BoxLayout(experiencePanel, BoxLayout.Y_AXIS));
        experiencePanel.setBackground(Color.WHITE);

        // Menambahkan section ke content panel
        contentPanel.add(createSectionPanel("Tentang Saya", aboutTextArea), "growx");
        contentPanel.add(createSectionPanel("Informasi Akademik", academicInfoPanel), "growx");
        contentPanel.add(createSectionPanel("Keahlian & Skill", skillsPanel), "growx");
        contentPanel.add(createSectionPanel("Pengalaman", experiencePanel), "growx");
        
        return contentPanel;
    }

    private JPanel createSectionPanel(String title, Component content) {
        JPanel sectionPanel = new JPanel(new BorderLayout());
        sectionPanel.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1));
        sectionPanel.setBackground(Color.WHITE);

        // Title Panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(new EmptyBorder(15, 15, 10, 15));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(new Color(31, 41, 55));
        
        JButton editButton = new JButton("Edit");
        editButton.setForeground(new Color(59, 130, 246));
        editButton.setContentAreaFilled(false);
        editButton.setBorderPainted(false);
        editButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        editButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        
        titlePanel.add(titleLabel, BorderLayout.WEST);
        titlePanel.add(editButton, BorderLayout.EAST);
        
        // Content Wrapper
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBorder(new EmptyBorder(0, 15, 15, 15));
        contentWrapper.setBackground(Color.WHITE);
        contentWrapper.add(content, BorderLayout.CENTER);
        
        sectionPanel.add(titlePanel, BorderLayout.NORTH);
        sectionPanel.add(contentWrapper, BorderLayout.CENTER);
        
        return sectionPanel;
    }

    private void displayAcademicInfo() {
        academicInfoPanel.removeAll();
        
        String[][] academicData = {
            {"NIM", "2308816", "Fakultas", "Fakultas UPI Cibiru"},
            {"Program Studi", "S1 Teknik Informatika", "Angkatan", "2023"},
            {"Semester", "6 (Enam)", "IPK", "3.85"}
        };
        
        for (String[] row : academicData) {
            JLabel labelKey1 = new JLabel(row[0]);
            labelKey1.setFont(new Font("SansSerif", Font.PLAIN, 12));
            labelKey1.setForeground(new Color(107, 114, 128));

            JLabel labelValue1 = new JLabel(row[1]);
            labelValue1.setFont(new Font("SansSerif", Font.BOLD, 12));
            labelValue1.setForeground(new Color(31, 41, 55));
            
            JLabel labelKey2 = new JLabel(row[2]);
            labelKey2.setFont(new Font("SansSerif", Font.PLAIN, 12));
            labelKey2.setForeground(new Color(107, 114, 128));

            JLabel labelValue2 = new JLabel(row[3]);
            labelValue2.setFont(new Font("SansSerif", Font.BOLD, 12));
            labelValue2.setForeground(new Color(31, 41, 55));
            
            academicInfoPanel.add(labelKey1);
            academicInfoPanel.add(labelValue1, "growx");
            academicInfoPanel.add(labelKey2);
            academicInfoPanel.add(labelValue2, "growx, wrap");
        }
        
        academicInfoPanel.revalidate();
        academicInfoPanel.repaint();
    }

    private void displaySkills() {
        skillsPanel.removeAll();
        
        String[] skills = {
            "JavaScript", "React.js", "Node.js", "Python", "PHP", 
            "MySQL", "MongoDB", "Git", "Figma", "UI/UX Design"
        };
        
        for (String skill : skills) {
            JButton skillButton = createSkillButton(skill.trim());
            skillsPanel.add(skillButton);
        }
        
        // Tombol untuk menambah skill baru
        JButton addSkillBtn = createAddButton("+ Tambah Skill Baru");
        skillsPanel.add(addSkillBtn);
        
        skillsPanel.revalidate();
        skillsPanel.repaint();
    }

    private JButton createSkillButton(String skillName) {
        JButton skillButton = new JButton(skillName);
        skillButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
        skillButton.setBackground(new Color(239, 246, 255));
        skillButton.setForeground(new Color(30, 64, 175));
        skillButton.setBorder(new EmptyBorder(6, 12, 6, 12));
        skillButton.setFocusPainted(false);
        skillButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        return skillButton;
    }

    private void displayExperience() {
        experiencePanel.removeAll();
        
        // Data pengalaman
        ExperienceData[] experiences = {
            new ExperienceData(
                "Frontend Developer Intern",
                "PT Teknologi Digital Indonesia",
                "Juni 2024 - Agustus 2024",
                "Mengembangkan antarmuka pengguna menggunakan React.js dan TypeScript. " +
                "Berkolaborasi dengan tim desain untuk implementasi UI/UX yang responsif."
            ),
            new ExperienceData(
                "Asisten Praktikum Pemrograman Web",
                "Universitas Pendidikan Indonesia",
                "Februari 2024 - Juni 2024",
                "Membantu mahasiswa dalam memahami konsep pemrograman web, HTML, CSS, dan JavaScript. " +
                "Menilai tugas dan memberikan feedback konstruktif."
            )
        };
        
        // Menambahkan setiap pengalaman
        for (int i = 0; i < experiences.length; i++) {
            ExperienceData exp = experiences[i];
            JPanel expPanel = createExperienceEntry(exp.title, exp.company, exp.period, exp.description);
            experiencePanel.add(expPanel);
            
            // Menambahkan separator kecuali untuk item terakhir
            if (i < experiences.length - 1) {
                experiencePanel.add(Box.createVerticalStrut(10));
                JSeparator separator = new JSeparator();
                separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
                separator.setForeground(new Color(229, 231, 235));
                experiencePanel.add(separator);
                experiencePanel.add(Box.createVerticalStrut(10));
            }
        }
        
        // Menambahkan spacing sebelum tombol tambah
        experiencePanel.add(Box.createVerticalStrut(15));
        
        // Tombol untuk menambah pengalaman baru
        JButton addExpBtn = createAddButton("+ Tambah Pengalaman Baru");
        addExpBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        experiencePanel.add(addExpBtn);
        
        experiencePanel.revalidate();
        experiencePanel.repaint();
    }

    private JPanel createExperienceEntry(String title, String company, String period, String description) {
        JPanel entryPanel = new JPanel();
        entryPanel.setLayout(new BoxLayout(entryPanel, BoxLayout.Y_AXIS));
        entryPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        entryPanel.setBorder(new EmptyBorder(5, 0, 5, 0));
        entryPanel.setOpaque(false);
        
        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        titleLabel.setForeground(new Color(31, 41, 55));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Company
        JLabel companyLabel = new JLabel(company);
        companyLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        companyLabel.setForeground(new Color(59, 130, 246));
        companyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Period
        JLabel periodLabel = new JLabel(period);
        periodLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        periodLabel.setForeground(new Color(107, 114, 128));
        periodLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Description
        JTextArea descArea = new JTextArea(description);
        styleTextArea(descArea);
        descArea.setBorder(new EmptyBorder(8, 0, 0, 0));
        descArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Menambahkan komponen ke panel
        entryPanel.add(titleLabel);
        entryPanel.add(Box.createVerticalStrut(3));
        entryPanel.add(companyLabel);
        entryPanel.add(Box.createVerticalStrut(2));
        entryPanel.add(periodLabel);
        entryPanel.add(descArea);
        
        return entryPanel;
    }

    private JButton createAddButton(String text) {
        JButton addButton = new JButton(text);
        addButton.setContentAreaFilled(false);
        addButton.setBorder(BorderFactory.createDashedBorder(new Color(156, 163, 175), 1, 4, 4, false));
        addButton.setForeground(new Color(107, 114, 128));
        addButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
        addButton.setFocusPainted(false);
        addButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        addButton.setPreferredSize(new Dimension(180, 35));
        
        // Hover effect
        addButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                addButton.setForeground(new Color(59, 130, 246));
                addButton.setBorder(BorderFactory.createDashedBorder(new Color(59, 130, 246), 1, 4, 4, false));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                addButton.setForeground(new Color(107, 114, 128));
                addButton.setBorder(BorderFactory.createDashedBorder(new Color(156, 163, 175), 1, 4, 4, false));
            }
        });
        
        return addButton;
    }

    private void styleTextArea(JTextArea textArea) {
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setOpaque(false);
        textArea.setEditable(false);
        textArea.setFont(new Font("SansSerif", Font.PLAIN, 13));
        textArea.setForeground(new Color(55, 65, 81));
    }

private JButton createStyledButton(String text, boolean primary) {
    JButton button = new JButton(text);
    button.setFocusPainted(false);
    button.setFont(new Font("SansSerif", Font.BOLD, 14));
    button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    
    if (primary) { // Tombol "Edit Profile"
        button.setBackground(Color.WHITE);
        button.setForeground(new Color(59, 130, 246));
        button.setBorder(new EmptyBorder(8, 20, 8, 20));
    } else { // Tombol "Portofolio" dan "Proyek"
        button.setBackground(new Color(37, 99, 235));
        
        // --- PERUBAHAN DI SINI ---
        button.setForeground(new Color(59, 130, 246)); // Mengubah warna font menjadi biru
        
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(191, 219, 254)), // Bingkai biru muda
            new EmptyBorder(7, 19, 7, 19) // Padding
        ));
    }
    return button;
}

    // Inner class untuk data pengalaman
    private static class ExperienceData {
        final String title;
        final String company;
        final String period;
        final String description;
        
        ExperienceData(String title, String company, String period, String description) {
            this.title = title;
            this.company = company;
            this.period = period;
            this.description = description;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set Look and Feel untuk tampilan yang lebih baik
                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new Profile().setVisible(true);
        });
    }
}
