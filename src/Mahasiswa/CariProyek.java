package Mahasiswa;

import net.miginfocom.swing.MigLayout;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Auth.SessionManager;
import Components.ProyekHeaderPanel;
import Database.MahasiswaDAO;
import Database.ProyekDAO;
import Models.Proyek;

public class CariProyek extends JFrame {

    private JPanel projectsPanel;

    public CariProyek() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("UPI Job - Proyek");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 247, 250));

        ProyekHeaderPanel headerPanel = new ProyekHeaderPanel("Cari Proyek");
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = createContentPanel();
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new MigLayout("wrap 1, fillx", "[grow]", "[]20[grow]"));
        contentPanel.setBackground(new Color(245, 247, 250));
        contentPanel.setBorder(new EmptyBorder(10, 40, 30, 40));

        // === TOP SECTION ===
        JPanel topSection = new JPanel(new MigLayout("insets 0", "[50%]10[15%]50[25%]", "[]"));
        topSection.setOpaque(false);
        topSection.add(Box.createHorizontalGlue(), "span, growx, pushx");

        JTextField searchField = new JTextField();
        searchField.setFont(new Font("Arial", Font.PLAIN, 13));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JButton searchBtn = new JButton("Search");
        searchBtn.setBackground(Color.BLUE);
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFont(new Font("Arial", Font.BOLD, 13));
        searchBtn.setFocusPainted(false);

        JButton buatProyekBtn = new JButton("+ Buat Proyek Baru");
        buatProyekBtn.setBackground(new Color(34, 197, 94));
        buatProyekBtn.setForeground(Color.WHITE);
        buatProyekBtn.setFont(new Font("Arial", Font.BOLD, 13));
        buatProyekBtn.setFocusPainted(false);
        buatProyekBtn.addActionListener(_ -> new FormProyek().setVisible(true));

        topSection.add(searchField, "growx");
        topSection.add(searchBtn, "growx");
        topSection.add(buatProyekBtn, "growx");

        contentPanel.add(topSection, "growx, pushx");

        // === SCROLLABLE PROJECTS PANEL ===
        projectsPanel = createProjectsPanel("");
        JScrollPane scrollPane = new JScrollPane(projectsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(new Color(245, 247, 250));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        contentPanel.add(scrollPane, "grow");

        // === SEARCH FUNCTION ===
        searchBtn.addActionListener(_ -> {
            String query = searchField.getText().trim();
            JPanel newProjectsPanel = createProjectsPanel(query);
            scrollPane.setViewportView(newProjectsPanel);
            projectsPanel = newProjectsPanel;
        });

        return contentPanel;
    }

    private JPanel createProjectsPanel(String query) {
        JPanel panel = new JPanel(new MigLayout("wrap 2, gap 20 20, insets 10", "[grow][grow]", ""));
        panel.setBackground(new Color(245, 247, 250));

        List<Proyek> proyekList;
        try {
            ProyekDAO proyekDAO = new ProyekDAO();
            String userId = String.valueOf(SessionManager.getInstance().getId());
            proyekList = (query != null && !query.isEmpty()) ?
                proyekDAO.getProyek(userId, query) :
                proyekDAO.getProyek(userId);
        } catch (Exception e) {
            proyekList = new ArrayList<>();
            e.printStackTrace();
        }

        for (Proyek p : proyekList) {
            JPanel card = createProjectCard(p);
            panel.add(card, "grow");
        }

        return panel;
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
        descArea.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JButton daftarBtn = new JButton("Daftar");
        daftarBtn.setBackground(new Color(37, 64, 143));
        daftarBtn.setForeground(Color.WHITE);
        daftarBtn.setFont(new Font("Arial", Font.BOLD, 12));
        daftarBtn.setFocusPainted(false);
        daftarBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        daftarBtn.addActionListener(_ -> {
            try {
                MahasiswaDAO mahasiswaDAO = new MahasiswaDAO();
                mahasiswaDAO.daftarProyek(proyek.proyekId);
                JOptionPane.showMessageDialog(this, "Berhasil mendaftar proyek!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                JPanel parentPanel = (JPanel) card.getParent();
                parentPanel.remove(card);
                parentPanel.revalidate();
                parentPanel.repaint();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Gagal mendaftar proyek: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        card.add(titleLabel);
        card.add(infoLabel);
        card.add(descArea, "growx");
        card.add(daftarBtn, "growx");

        return card;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CariProyek().setVisible(true));
    }
}
