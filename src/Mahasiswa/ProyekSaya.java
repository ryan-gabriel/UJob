package Mahasiswa;

import net.miginfocom.swing.MigLayout;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Auth.SessionManager;
import Components.ProyekHeaderPanel;
import Database.ProyekDAO;
import Models.Proyek;

public class ProyekSaya extends JFrame {

    private JPanel projectsPanel;

    public ProyekSaya() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("UPI Job - Proyek");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 247, 250));

        ProyekHeaderPanel headerPanel = new ProyekHeaderPanel("Proyek Saya");
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
        projectsPanel = createProjectsPanel("");
        JScrollPane scrollPane = new JScrollPane(projectsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(new Color(245, 247, 250));
        scrollPane.getViewport().setBackground(new Color(245, 247, 250));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        contentPanel.add(scrollPane, "grow");
        SwingUtilities.invokeLater(() -> {
            scrollPane.getViewport().setViewPosition(new Point(0, 0));
        });
        
        
        searchBtn.addActionListener(_ -> {
            String query = searchField.getText().trim();
            JPanel newProjectsPanel = createProjectsPanel(query);
            scrollPane.setViewportView(newProjectsPanel);
            projectsPanel = newProjectsPanel;
        });

        return contentPanel;
    }
    
    private JPanel createProjectsPanel(String query) {
        JPanel projectsPanel = new JPanel(new MigLayout("wrap 2, gap 20 20, insets 10", "[grow][grow]", ""));
        projectsPanel.setBackground(new Color(245, 247, 250));

        List<Proyek> proyekList;
        try {
            ProyekDAO proyekDAO = new ProyekDAO();
            proyekList = proyekDAO.getProyekSendiri(String.valueOf(SessionManager.getInstance().getId()));
            String userId = String.valueOf(SessionManager.getInstance().getId());
            proyekList = (query != null && !query.isEmpty()) ?
                proyekDAO.getProyekSendiri(userId, query) :
                proyekDAO.getProyekSendiri(userId);
        } catch (Exception e) {
            proyekList = new ArrayList<>();
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat data proyek: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
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
        descArea.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Status label untuk menunjukkan status proyek
        JLabel statusLabel = new JLabel("Status: " + proyek.status.toUpperCase());
        statusLabel.setFont(new Font("Arial", Font.BOLD, 12));
        statusLabel.setOpaque(true);
        statusLabel.setBorder(new EmptyBorder(4, 8, 4, 8));
        
        // Set warna berdasarkan status
        switch (proyek.status.toLowerCase()) {
            case "aktif":
                statusLabel.setBackground(new Color(76, 175, 80));
                statusLabel.setForeground(Color.WHITE);
                break;
            case "ditutup":
                statusLabel.setBackground(new Color(255, 152, 0));
                statusLabel.setForeground(Color.WHITE);
                break;
            case "selesai":
                statusLabel.setBackground(new Color(96, 125, 139));
                statusLabel.setForeground(Color.WHITE);
                break;
            default:
                statusLabel.setBackground(Color.LIGHT_GRAY);
                statusLabel.setForeground(Color.BLACK);
        }

        // Create buttons
        JButton hapusBtn = createButton("Hapus Proyek", new Color(244, 67, 54));
        JButton lihatAnggotaProyekBtn = createButton("Lihat Anggota Proyek", new Color(33, 150, 243));
        JButton lihatPendaftaranAnggotaBtn = createButton("Lihat Pendaftaran Anggota", new Color(156, 39, 176));
        JButton tutupPendaftaranBtn = createButton("Tutup Pendaftaran", new Color(255, 152, 0));
        JButton bukaPendaftaranBtn = createButton("Buka Pendaftaran", new Color(76, 175, 80));
        JButton tandaiSelesaiBtn = createButton("Tandai Selesai", new Color(96, 125, 139));
        JButton editBtn = createButton("Edit Proyek", new Color(255, 193, 7)); // Kuning Amber


        // Button Panel yang akan diupdate dinamis
        JPanel buttonPanel = new JPanel(new GridLayout(0, 2, 10, 8));
        buttonPanel.setOpaque(false);

        // Method untuk update button panel
        Runnable updateButtonPanel = () -> {
            buttonPanel.removeAll();
            buttonPanel.add(hapusBtn);
            buttonPanel.add(lihatAnggotaProyekBtn);
            
            if (!proyek.status.equals("selesai")) {
                buttonPanel.add(editBtn);
                buttonPanel.add(lihatPendaftaranAnggotaBtn);
                
                if (proyek.status.equals("aktif")) {
                    buttonPanel.add(tutupPendaftaranBtn);
                } else if (proyek.status.equals("ditutup")) {
                    buttonPanel.add(bukaPendaftaranBtn);
                }
                
                buttonPanel.add(tandaiSelesaiBtn);
            }
            
            buttonPanel.revalidate();
            buttonPanel.repaint();
        };

        hapusBtn.addActionListener(_ -> {
            int confirm = JOptionPane.showConfirmDialog(
                this, 
                "Apakah Anda yakin ingin menghapus proyek ini?", 
                "Konfirmasi Hapus", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    ProyekDAO proyekDAO = new ProyekDAO();
                    proyekDAO.hapusProyek(proyek.proyekId, String.valueOf(SessionManager.getInstance().getId()));
                    JOptionPane.showMessageDialog(this, "Proyek berhasil dihapus", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Remove card from parent panel
                    JPanel parentPanel = (JPanel) card.getParent();
                    parentPanel.remove(card);
                    parentPanel.revalidate();
                    parentPanel.repaint();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Gagal menghapus proyek: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        editBtn.addActionListener(_ -> {
            try {
                new EditProyekForm(proyek).setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Gagal membuka form edit proyek: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });


        lihatAnggotaProyekBtn.addActionListener(_ -> {
            try {
                new AnggotaProyek(proyek.proyekId).setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Gagal membuka halaman anggota proyek: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        lihatPendaftaranAnggotaBtn.addActionListener(_ -> {
            try {
                new PendaftaranAnggotaProyek(proyek.proyekId).setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Gagal membuka halaman pendaftaran anggota: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        tutupPendaftaranBtn.addActionListener(_ -> {
            int confirm = JOptionPane.showConfirmDialog(
                this, 
                "Apakah Anda yakin ingin menutup pendaftaran proyek ini?", 
                "Konfirmasi Tutup Pendaftaran", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    ProyekDAO proyekDAO = new ProyekDAO();
                    proyekDAO.tutupProyek(proyek.proyekId, String.valueOf(SessionManager.getInstance().getId()));
                    
                    proyek.status = "ditutup";
                    
                    statusLabel.setText("Status: TUTUP");
                    statusLabel.setBackground(new Color(255, 152, 0));
                    statusLabel.setForeground(Color.WHITE);
                    
                    updateButtonPanel.run();
                    
                    JOptionPane.showMessageDialog(this, "Pendaftaran proyek berhasil ditutup", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Gagal menutup pendaftaran proyek: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        bukaPendaftaranBtn.addActionListener(_ -> {
            int confirm = JOptionPane.showConfirmDialog(
                this, 
                "Apakah Anda yakin ingin membuka kembali pendaftaran proyek ini?", 
                "Konfirmasi Buka Pendaftaran", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    ProyekDAO proyekDAO = new ProyekDAO();
                    proyekDAO.bukaProyek(proyek.proyekId, String.valueOf(SessionManager.getInstance().getId()));
                    
                    proyek.status = "aktif";
                    
                    statusLabel.setText("Status: AKTIF");
                    statusLabel.setBackground(new Color(76, 175, 80));
                    statusLabel.setForeground(Color.WHITE);
                    
                    updateButtonPanel.run();
                    
                    JOptionPane.showMessageDialog(this, "Pendaftaran proyek berhasil dibuka kembali", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Gagal membuka pendaftaran proyek: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
       
        tandaiSelesaiBtn.addActionListener(_ -> {
            int confirm = JOptionPane.showConfirmDialog(
                this, 
                "Apakah Anda yakin ingin menandai proyek ini sebagai selesai?", 
                "Konfirmasi Tandai Selesai", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    ProyekDAO proyekDAO = new ProyekDAO();
                    proyekDAO.tandaiProyekSelesai(proyek.proyekId, String.valueOf(SessionManager.getInstance().getId()));
                    
                    proyek.status = "selesai";
                    
                    statusLabel.setText("Status: SELESAI");
                    statusLabel.setBackground(new Color(96, 125, 139));
                    statusLabel.setForeground(Color.WHITE);
                    
                    updateButtonPanel.run();
                    
                    JOptionPane.showMessageDialog(this, "Proyek berhasil ditandai selesai", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Gagal menandai proyek selesai: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        card.add(titleLabel);
        card.add(infoLabel);
        card.add(statusLabel);
        card.add(descArea, "growx");
        
        updateButtonPanel.run();
        card.add(buttonPanel, "growx");

        return card;
    }

    private JButton createButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor.darker());
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });
        
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            new ProyekSaya().setVisible(true);
        });
    }
}