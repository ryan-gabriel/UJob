package Mahasiswa;

import net.miginfocom.swing.MigLayout;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Auth.SessionManager;
import Components.ProyekHeaderPanel;
import Database.koneksi;
import Models.Proyek;

public class ProyekAktif extends JFrame {

    public ProyekAktif() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("UPI Job - Proyek");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 247, 250));

        ProyekHeaderPanel headerPanel = new ProyekHeaderPanel("Proyek Aktif");
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
        topSection.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JTextField searchField = new JTextField();
        searchField.setFont(new Font("Arial", Font.PLAIN, 13));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        searchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JButton searchBtn = new JButton("Search");
        searchBtn.setBackground(new Color(37, 64, 143));
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFont(new Font("Arial", Font.BOLD, 13));
        searchBtn.setFocusPainted(false);
        searchBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

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
        JPanel projectsPanel = new JPanel(new MigLayout("wrap 2, gap 20 20, insets 10", "[grow][grow]", ""));
        projectsPanel.setBackground(new Color(245, 247, 250));

        List<Proyek> proyekList;
        try {
            proyekList = koneksi.getInstance().getProyekAnggota(String.valueOf(SessionManager.getInstance().getId()));
        } catch (Exception e) {
            proyekList = new ArrayList<>();
            e.printStackTrace();
        }

        for (Proyek p : proyekList) {
            JPanel card = createProjectCard(p);
            projectsPanel.add(card, "grow");
        }

        return projectsPanel;
    }

    private JPanel createProjectCard(Proyek proyek) {
        JPanel card = new JPanel(new MigLayout("wrap 1, fillx, gapy 10"));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel(proyek.judul);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(37, 64, 143));

        JLabel infoLabel = new JLabel("Diposting: " + proyek.tanggalDibuat + " | Oleh: " + proyek.namaPemilik);
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        infoLabel.setForeground(new Color(120, 120, 120));

        JTextArea descArea = new JTextArea(proyek.deskripsi);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setEditable(false);
        descArea.setOpaque(false);
        descArea.setFont(new Font("Arial", Font.PLAIN, 13));
        descArea.setForeground(new Color(60, 60, 60));

        JButton daftarBtn = new JButton("Daftar");
        daftarBtn.setBackground(new Color(37, 64, 143));
        daftarBtn.setForeground(Color.WHITE);
        daftarBtn.setFont(new Font("Arial", Font.BOLD, 12));
        daftarBtn.setFocusPainted(false);
        daftarBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Event saat klik tombol
        daftarBtn.addActionListener(_ -> {
            // aksi daftar, misalnya tampilkan konfirmasi atau kirim ke DB
            System.out.println("Daftar proyek: " + proyek.judul);
        });

        card.add(titleLabel);
        card.add(infoLabel);
        card.add(descArea, "growx");
        card.add(daftarBtn, "align left");

        return card;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ProyekAktif().setVisible(true);
        });
    }
}
