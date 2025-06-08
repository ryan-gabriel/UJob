package Mahasiswa;

import Models.User;
import Auth.SessionManager;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Database.ProyekDAO;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class PendaftaranAnggotaProyek extends JFrame {
    private String proyekId;
    private ProyekDAO proyekDAO;
    private JPanel pendaftarPanel;
    private List<User> pendaftarList;
    private boolean isPemilikProyek;

    public PendaftaranAnggotaProyek(String proyekId) {
        this.proyekId = proyekId;
        this.proyekDAO = new ProyekDAO();
        
        // Check if current user is the project owner
        checkPemilikProyek();
        
        initializeUI();
        loadPendaftar();
    }

    private void checkPemilikProyek() {
        try {
            // Check if current user is the project owner
            isPemilikProyek = proyekDAO.isPemilikProyek(proyekId, String.valueOf(SessionManager.getInstance().getId()));
        } catch (Exception e) {
            isPemilikProyek = false;
            e.printStackTrace();
        }
    }

    private void initializeUI() {
        setTitle("Pendaftaran Anggota Proyek");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel with MigLayout
        JPanel mainPanel = new JPanel(new MigLayout("wrap 1, fillx", "[grow]", "[]20[grow][]"));
        mainPanel.setBackground(new Color(245, 247, 250));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, "growx");

        // Content panel with scroll
        JScrollPane scrollPane = createContentPanel();
        mainPanel.add(scrollPane, "grow");

        // Bottom panel
        JPanel bottomPanel = createBottomPanel();
        mainPanel.add(bottomPanel, "growx");

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new MigLayout("insets 0", "[grow][]", "[][]"));
        headerPanel.setBackground(new Color(245, 247, 250));

        JLabel titleLabel = new JLabel("Pendaftaran Anggota Proyek");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(new Color(37, 64, 143));

        JLabel proyekIdLabel = new JLabel("ID Proyek: " + proyekId);
        proyekIdLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        proyekIdLabel.setForeground(new Color(120, 120, 120));

        JLabel statusLabel = new JLabel();
        if (!isPemilikProyek) {
            statusLabel.setText("⚠️ Anda tidak memiliki izin untuk mengelola pendaftaran");
            statusLabel.setFont(new Font("Arial", Font.BOLD, 12));
            statusLabel.setForeground(new Color(244, 67, 54));
        }

        headerPanel.add(titleLabel, "span 2, wrap");
        headerPanel.add(proyekIdLabel, "wrap");
        if (!isPemilikProyek) {
            headerPanel.add(statusLabel, "span 2");
        }

        return headerPanel;
    }

    private JScrollPane createContentPanel() {
        pendaftarPanel = new JPanel(new MigLayout("wrap 1, fillx, gapy 10", "[grow]", ""));
        pendaftarPanel.setBackground(new Color(245, 247, 250));

        JScrollPane scrollPane = new JScrollPane(pendaftarPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Daftar Pendaftar",
            0, 0,
            new Font("Arial", Font.BOLD, 14),
            new Color(37, 64, 143)
        ));
        scrollPane.setBackground(new Color(245, 247, 250));
        scrollPane.getViewport().setBackground(new Color(245, 247, 250));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        return scrollPane;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new MigLayout("insets 0, right", "[]10[]", ""));
        bottomPanel.setBackground(new Color(245, 247, 250));

        JButton btnRefresh = new JButton("Muat Ulang");
        btnRefresh.setBackground(new Color(37, 64, 143));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFont(new Font("Arial", Font.BOLD, 12));
        btnRefresh.setFocusPainted(false);
        btnRefresh.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRefresh.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        btnRefresh.addActionListener(_ -> loadPendaftar());

        JButton btnClose = new JButton("Tutup");
        btnClose.setBackground(new Color(158, 158, 158));
        btnClose.setForeground(Color.WHITE);
        btnClose.setFont(new Font("Arial", Font.BOLD, 12));
        btnClose.setFocusPainted(false);
        btnClose.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnClose.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        btnClose.addActionListener(_ -> dispose());

        bottomPanel.add(btnRefresh);
        bottomPanel.add(btnClose);

        return bottomPanel;
    }

    private void loadPendaftar() {
        pendaftarPanel.removeAll();
        
        try {
            pendaftarList = proyekDAO.getPendaftaranAnggotaProyek(proyekId);
            
            if (pendaftarList.isEmpty()) {
                JPanel emptyPanel = createEmptyPanel();
                pendaftarPanel.add(emptyPanel, "growx");
            } else {
                for (User user : pendaftarList) {
                    JPanel pendaftarCard = createPendaftarCard(user);
                    pendaftarPanel.add(pendaftarCard, "growx");
                }
            }
        } catch (Exception e) {
            JPanel errorPanel = createErrorPanel(e.getMessage());
            pendaftarPanel.add(errorPanel, "growx");
            e.printStackTrace();
        }
        
        pendaftarPanel.revalidate();
        pendaftarPanel.repaint();
    }

    private JPanel createEmptyPanel() {
        JPanel emptyPanel = new JPanel(new MigLayout("center", "[center]", "[center]"));
        emptyPanel.setBackground(Color.WHITE);
        emptyPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            new EmptyBorder(40, 20, 40, 20)
        ));


        JLabel emptyLabel = new JLabel("Belum ada pendaftar untuk proyek ini");
        emptyLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        emptyLabel.setForeground(new Color(120, 120, 120));

        JLabel emptySubLabel = new JLabel("Pendaftar baru akan muncul di sini setelah mereka mengajukan diri");
        emptySubLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        emptySubLabel.setForeground(new Color(150, 150, 150));

        emptyPanel.add(emptyLabel, "wrap, center");
        emptyPanel.add(emptySubLabel, "center");

        return emptyPanel;
    }

    private JPanel createErrorPanel(String errorMessage) {
        JPanel errorPanel = new JPanel(new MigLayout("center", "[center]", "[center]"));
        errorPanel.setBackground(new Color(255, 235, 235));
        errorPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(244, 67, 54)),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel errorIcon = new JLabel("⚠️");
        errorIcon.setFont(new Font("Arial", Font.PLAIN, 24));

        JLabel errorLabel = new JLabel("Gagal memuat data pendaftar");
        errorLabel.setFont(new Font("Arial", Font.BOLD, 14));
        errorLabel.setForeground(new Color(244, 67, 54));

        JLabel errorDetailLabel = new JLabel("Error: " + errorMessage);
        errorDetailLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        errorDetailLabel.setForeground(new Color(200, 50, 50));

        errorPanel.add(errorIcon, "wrap, center");
        errorPanel.add(errorLabel, "wrap, center");
        errorPanel.add(errorDetailLabel, "center");

        return errorPanel;
    }

    private JPanel createPendaftarCard(User user) {
        JPanel card = new JPanel(new MigLayout("insets 20", "[grow][]", "[]5[]5[]"));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // User info section
        JLabel nameLabel = new JLabel(user.getNama());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setForeground(new Color(37, 64, 143));

        JLabel emailLabel = new JLabel(user.getEmail());
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        emailLabel.setForeground(new Color(120, 120, 120));


        card.add(nameLabel, "wrap");
        card.add(emailLabel, "wrap");

        // Action buttons section (only for project owner)
        if (isPemilikProyek) {
            JPanel buttonPanel = new JPanel(new MigLayout("insets 0, center", "[]10[]", ""));
            buttonPanel.setOpaque(false);

            JButton btnTerima = createActionButton("Terima", new Color(76, 175, 80));
            JButton btnTolak = createActionButton("Tolak", new Color(244, 67, 54));

            btnTerima.addActionListener(_ -> prosesKeputusan(user, true));
            btnTolak.addActionListener(_ -> prosesKeputusan(user, false));

            buttonPanel.add(btnTerima);
            buttonPanel.add(btnTolak);

            card.add(buttonPanel, "cell 1 0 1 3, center");
        } else {
            // Show read-only status for non-owners
            JLabel statusLabel = new JLabel("Menunggu Keputusan");
            statusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
            statusLabel.setForeground(new Color(255, 152, 0));
            statusLabel.setOpaque(true);
            statusLabel.setBackground(new Color(255, 243, 224));
            statusLabel.setBorder(new EmptyBorder(5, 10, 5, 10));

            card.add(statusLabel, "cell 1 0 1 3, center");
        }

        return card;
    }

    private JButton createActionButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(backgroundColor.darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(backgroundColor);
            }
        });
        
        return button;
    }

    private void prosesKeputusan(User user, boolean terima) {
        String action = terima ? "menerima" : "menolak";
        String actionTitle = terima ? "Terima" : "Tolak";
        
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Apakah Anda yakin ingin " + action + " pendaftaran dari " + user.getNama() + "?",
            "Konfirmasi " + actionTitle + " Pendaftaran",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean success;
                if (terima) {
                    success = proyekDAO.terimaPengajuan(proyekId, String.valueOf(user.getUserId()));
                } else {
                    success = proyekDAO.tolakPengajuan(proyekId, String.valueOf(user.getUserId()));
                }
                
                if (success) {
                    String message = terima ? 
                        user.getNama() + " berhasil diterima sebagai anggota proyek" :
                        "Pendaftaran " + user.getNama() + " berhasil ditolak";
                        
                    JOptionPane.showMessageDialog(
                        this,
                        message,
                        "Sukses",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    loadPendaftar(); // Refresh the list
                } else {
                    JOptionPane.showMessageDialog(
                        this,
                        "Gagal memproses pendaftaran",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                    this,
                    "Gagal memproses pendaftaran: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            new PendaftaranAnggotaProyek("1").setVisible(true);
        });
    }
}