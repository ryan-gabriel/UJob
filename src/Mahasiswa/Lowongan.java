package Mahasiswa;

import Components.MahasiswaNavigation;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;

public class Lowongan extends JFrame {

    public Lowongan() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Lowongan Pekerjaan - UJob");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(248, 251, 255));

        // Navigation Header
        mainPanel.add(new MahasiswaNavigation("Lowongan"), BorderLayout.NORTH);
        
        // --- PERUBAHAN STRUKTUR UNTUK SCROLLBAR ---
        // Panel utama untuk konten yang akan memiliki padding
        JPanel contentContainer = new JPanel(new BorderLayout(0, 20));
        contentContainer.setBorder(new EmptyBorder(20, 40, 20, 40));
        contentContainer.setOpaque(false);

        // Title
        JLabel titleLabel = new JLabel("Lowongan Pekerjaan");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(new Color(31, 41, 55));
        contentContainer.add(titleLabel, BorderLayout.NORTH);

        // Panel untuk menampung semua kartu lowongan
        JPanel jobsPanel = new JPanel();
        jobsPanel.setLayout(new BoxLayout(jobsPanel, BoxLayout.Y_AXIS));
        jobsPanel.setOpaque(false);
        
        // Memuat data lowongan statis ke dalam jobsPanel
        populateJobsPanel(jobsPanel);
        
        contentContainer.add(jobsPanel, BorderLayout.CENTER);
        
        // JScrollPane sekarang membungkus contentContainer
        JScrollPane scrollPane = new JScrollPane(contentContainer);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        add(mainPanel);
    }
    
    /**
     * Mengisi panel dengan data lowongan statis.
     */
    private void populateJobsPanel(JPanel jobsPanel) {
        LowonganData[] lowonganList = {
            new LowonganData(
                "Frontend Developer", "PT Fictindo", "Magang", 
                "Kami mencari mahasiswa magang untuk posisi Frontend Developer. Anda akan bekerja dengan teknologi modern seperti React.js dan TypeScript, serta berkolaborasi dengan tim desain untuk mengimplementasikan UI/UX yang responsif dan menarik.", 
                "Bandung, Jawa Barat", "Rp 2.500.000/bulan", "3 bulan", "Deadline: 30 Juni 2025", 
                new Color(219, 234, 254), new Color(59, 130, 246)
            ),
            new LowonganData(
                "Backend Developer", "PT ABC", "Kerja Penuh", 
                "Bergabunglah dengan tim kami sebagai Backend Developer. Anda akan bertanggung jawab merancang dan mengembangkan sisi server aplikasi menggunakan Laravel dan mengelola database MySQL untuk memastikan performa yang optimal.", 
                "Bandung, Jawa Barat", "Rp 2.500.000/bulan", "3 bulan", "Deadline: 30 Juni 2025", 
                new Color(220, 252, 231), new Color(34, 197, 94)
            ),
            new LowonganData(
                "AI Developer", "PT Fictindo", "Magang", 
                "Posisi magang AI Developer terbuka bagi Anda yang tertarik dengan kecerdasan buatan. Anda akan terlibat dalam proyek pengembangan model machine learning menggunakan Python, TensorFlow, dan SQL.", 
                "Bandung, Jawa Barat", "Rp 2.500.000/bulan", "3 bulan", "Deadline: 30 Juni 2025", 
                new Color(219, 234, 254), new Color(59, 130, 246)
            )
        };
        
        for (LowonganData data : lowonganList) {
            addJobCard(jobsPanel, data);
        }
    }

    private void addJobCard(JPanel container, LowonganData data) {
        JPanel card = new JPanel(new MigLayout("wrap 1, fillx, insets 25", "[fill]"));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1));
        
        // --- Top Row ---
        JPanel topRow = new JPanel(new MigLayout("insets 0", "[grow, left]push[right]"));
        topRow.setOpaque(false);

        JLabel titleLabel = new JLabel(data.title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        
        JLabel companyLabel = new JLabel(data.company);
        companyLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        companyLabel.setForeground(Color.GRAY);
        
        JPanel titleAndCompany = new JPanel();
        titleAndCompany.setLayout(new BoxLayout(titleAndCompany, BoxLayout.Y_AXIS));
        titleAndCompany.setOpaque(false);
        titleAndCompany.add(titleLabel);
        titleAndCompany.add(companyLabel);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setOpaque(false);
        buttonsPanel.add(createTag("12 lamaran", new Color(241, 245, 249), new Color(100, 116, 139)));
        buttonsPanel.add(createStyledButton("Lamar", true));

        topRow.add(titleAndCompany);
        topRow.add(buttonsPanel);

        // --- Tag Row ---
        JPanel tagRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        tagRow.setOpaque(false);
        tagRow.add(createTag(data.type, data.tagBgColor, data.tagFgColor));
        
        // --- Description ---
        JTextArea descArea = new JTextArea(data.description);
        styleTextArea(descArea);
        
        // --- Separator ---
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(229, 231, 235));
        
        // --- Bottom Row ---
        JPanel bottomRow = new JPanel(new MigLayout("insets 0", "[left, grow][center, grow][center, grow][center, grow]"));
        bottomRow.setOpaque(false);
        bottomRow.add(createInfoLabel(data.location));
        bottomRow.add(createInfoLabel(data.salary));
        bottomRow.add(createInfoLabel(data.duration));
        bottomRow.add(createInfoLabel(data.deadline));
        
        // Add components to card
        card.add(topRow, "growx");
        card.add(tagRow, "gapy 10");
        card.add(descArea, "gapy 10, growx");
        card.add(separator, "gapy 15, growx");
        card.add(bottomRow, "gapy 15, growx");
        
        container.add(card);
        container.add(Box.createRigidArea(new Dimension(0, 15)));
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
        button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        
        if (primary) {
            // PERBAIKAN: Mengubah gaya tombol "Lamar"
            button.setBackground(Color.WHITE); // Latar belakang putih
            button.setForeground(new Color(59, 130, 246)); // Teks biru
            button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(219, 223, 234)), // Bingkai tipis
                new EmptyBorder(7, 19, 7, 19) // Padding
            ));
        }
        return button;
    }
    
    // Inner class untuk data lowongan
    private static class LowonganData {
        final String title, company, type, description, location, salary, duration, deadline;
        final Color tagBgColor, tagFgColor;

        LowonganData(String title, String company, String type, String description, String location, String salary, String duration, String deadline, Color tagBgColor, Color tagFgColor) {
            this.title = title;
            this.company = company;
            this.type = type;
            this.description = description;
            this.location = location;
            this.salary = salary;
            this.duration = duration;
            this.deadline = deadline;
            this.tagBgColor = tagBgColor;
            this.tagFgColor = tagFgColor;
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new Lowongan().setVisible(true);
        });
    }
}
