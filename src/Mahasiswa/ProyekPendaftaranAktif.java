package Mahasiswa;

import net.miginfocom.swing.MigLayout;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import Components.ProyekHeaderPanel;

public class ProyekPendaftaranAktif extends JFrame {

    public ProyekPendaftaranAktif() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("UPI Job - Proyek");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 247, 250));

        ProyekHeaderPanel headerPanel = new ProyekHeaderPanel("Pendaftaran Aktif");
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = createContentPanel();
        mainPanel.add(contentPanel, BorderLayout.CENTER);

    
        add(mainPanel);
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new MigLayout("wrap 1, fillx", "[grow]", "[]20[grow]"));
        contentPanel.setBackground(new Color(245, 247, 250));
        contentPanel.setBorder(new EmptyBorder(10, 40, 30, 40));

        JPanel topSection = new JPanel(new MigLayout("insets 0", "[80%][20%]", "[150]"));
        topSection.setOpaque(false);

        JTextField searchField = new JTextField();
        searchField.setFont(new Font("Arial", Font.PLAIN, 13));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JButton searchBtn = new JButton("Search");
        searchBtn.setBackground(new Color(37, 64, 143));
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFont(new Font("Arial", Font.BOLD, 13));
        searchBtn.setFocusPainted(false);

        topSection.add(searchField, "growx");
        topSection.add(searchBtn, "growx, gapleft 10");

        contentPanel.add(topSection, "growx, pushx");

        // === SCROLLABLE PROJECTS ===
        JPanel projectsPanel = createProjectsPanel();
        JScrollPane scrollPane = new JScrollPane(projectsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(new Color(245, 247, 250));
        scrollPane.getViewport().setBackground(new Color(245, 247, 250));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        contentPanel.add(scrollPane, "grow");
        return contentPanel;
    }

    private JPanel createProjectsPanel() {
        JPanel projectsPanel = new JPanel(new MigLayout("wrap 2, gap 20 20, insets 0", "[grow][grow]", ""));
        projectsPanel.setBackground(new Color(245, 247, 250));

        String[][] projects = {
            {"Aplikasi Pengingat Jadwal Kuliah Berbasis Mobile", "21 Mei 2025", "25 Mei 2025", "Membuat aplikasi mobile sederhana..."},
            {"Sistem Pendaftaran Online untuk Organisasi Kampus", "21 Mei 2025", "25 Mei 2025", "Mengembangkan platform web..."},
            {"Website E-Commerce untuk Produk Mahasiswa", "20 Mei 2025", "30 Mei 2025", "Membangun platform jual beli online..."},
            {"Aplikasi Manajemen Keuangan Pribadi", "19 Mei 2025", "28 Mei 2025", "Mengembangkan aplikasi mobile..."},
            {"Platform Sharing Catatan Kuliah", "18 Mei 2025", "27 Mei 2025", "Membuat website untuk berbagi catatan..."},
            {"Sistem Informasi Perpustakaan Digital", "17 Mei 2025", "26 Mei 2025", "Mengembangkan sistem manajemen..."}
        };

        for (String[] project : projects) {
            JPanel card = createProjectCard(project[0], project[1], project[2], project[3]);
            projectsPanel.add(card, "grow");
        }

        return projectsPanel;
    }

    private JPanel createProjectCard(String title, String startDate, String endDate, String description) {
        JPanel card = new JPanel(new MigLayout("wrap 1, fillx"));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(37, 64, 143));

        JLabel dateLabel = new JLabel("Diposting: " + startDate + " | Ditutup: " + endDate);
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        dateLabel.setForeground(Color.GRAY);

        JLabel descLabel = new JLabel("<html><p style='line-height:1.4'>" + description + "</p></html>");
        descLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        descLabel.setForeground(Color.DARK_GRAY);

        JButton daftarBtn = new JButton("Daftar");
        daftarBtn.setBackground(new Color(37, 64, 143));
        daftarBtn.setForeground(Color.WHITE);
        daftarBtn.setFont(new Font("Arial", Font.BOLD, 12));
        daftarBtn.setFocusPainted(false);

        card.add(titleLabel);
        card.add(dateLabel);
        card.add(descLabel);
        card.add(daftarBtn, "align left");

        return card;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ProyekPendaftaranAktif().setVisible(true);
        });
    }
}
