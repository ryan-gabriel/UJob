package Mahasiswa;

import Models.User;
import Auth.SessionManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Database.ProyekDAO;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class AnggotaProyek extends JFrame {
    private String proyekId;
    private ProyekDAO proyekDAO;
    private JPanel anggotaPanel;
    private List<User> anggotaList;
    private boolean isPemilikProyek;

    public AnggotaProyek(String proyekId) {
        this.proyekId = proyekId;
        this.proyekDAO = new ProyekDAO();
        
        // Check if current user is the project owner
        checkPemilikProyek();
        
        initializeUI();
        muatDataAnggota();
    }

    private void checkPemilikProyek() {
        try {
            isPemilikProyek = proyekDAO.isPemilikProyek(proyekId, String.valueOf(SessionManager.getInstance().getId()));
        } catch (Exception e) {
            isPemilikProyek = false;
            e.printStackTrace();
        }
    }

    private void initializeUI() {
        setTitle("Anggota Proyek");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(245, 247, 250));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Content panel with scroll
        JScrollPane scrollPane = createContentPanel();
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Bottom panel
        JPanel bottomPanel = createBottomPanel();
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Anggota Proyek");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(37, 64, 143));

        JLabel proyekIdLabel = new JLabel("ID Proyek: " + proyekId);
        proyekIdLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        proyekIdLabel.setForeground(new Color(120, 120, 120));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(proyekIdLabel, BorderLayout.SOUTH);

        headerPanel.add(titlePanel, BorderLayout.WEST);

        return headerPanel;
    }

    private JScrollPane createContentPanel() {
        anggotaPanel = new JPanel();
        anggotaPanel.setLayout(new BoxLayout(anggotaPanel, BoxLayout.Y_AXIS));
        anggotaPanel.setBackground(new Color(245, 247, 250));

        JScrollPane scrollPane = new JScrollPane(anggotaPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Daftar Anggota",
            0, 0,
            new Font("Arial", Font.BOLD, 14),
            new Color(37, 64, 143)
        ));
        scrollPane.setBackground(new Color(245, 247, 250));
        scrollPane.getViewport().setBackground(new Color(245, 247, 250));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        return scrollPane;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);

        JButton btnRefresh = new JButton("Muat Ulang");
        btnRefresh.setBackground(new Color(37, 64, 143));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFont(new Font("Arial", Font.BOLD, 12));
        btnRefresh.setFocusPainted(false);
        btnRefresh.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRefresh.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        btnRefresh.addActionListener(_ -> muatDataAnggota());

        JButton btnClose = new JButton("Tutup");
        btnClose.setBackground(new Color(158, 158, 158));
        btnClose.setForeground(Color.WHITE);
        btnClose.setFont(new Font("Arial", Font.BOLD, 12));
        btnClose.setFocusPainted(false);
        btnClose.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnClose.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        btnClose.addActionListener(_ -> dispose());

        bottomPanel.add(btnRefresh);
        bottomPanel.add(Box.createHorizontalStrut(10));
        bottomPanel.add(btnClose);

        return bottomPanel;
    }

    private void muatDataAnggota() {
        anggotaPanel.removeAll();
        
        try {
            anggotaList = proyekDAO.getAnggotaProyek(proyekId);
            
            if (anggotaList.isEmpty()) {
                JPanel emptyPanel = createEmptyPanel();
                anggotaPanel.add(emptyPanel);
            } else {
                for (User user : anggotaList) {
                    JPanel memberCard = createMemberCard(user);
                    anggotaPanel.add(memberCard);
                    anggotaPanel.add(Box.createVerticalStrut(10));
                }
            }
        } catch (Exception e) {
            JPanel errorPanel = createErrorPanel(e.getMessage());
            anggotaPanel.add(errorPanel);
            e.printStackTrace();
        }
        
        anggotaPanel.revalidate();
        anggotaPanel.repaint();
    }

    private JPanel createEmptyPanel() {
        JPanel emptyPanel = new JPanel(new BorderLayout());
        emptyPanel.setBackground(Color.WHITE);
        emptyPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            new EmptyBorder(30, 20, 30, 20)
        ));
        emptyPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JLabel emptyLabel = new JLabel("Belum ada anggota yang bergabung dengan proyek ini", JLabel.CENTER);
        emptyLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        emptyLabel.setForeground(new Color(120, 120, 120));

        emptyPanel.add(emptyLabel, BorderLayout.CENTER);
        return emptyPanel;
    }

    private JPanel createErrorPanel(String errorMessage) {
        JPanel errorPanel = new JPanel(new BorderLayout());
        errorPanel.setBackground(new Color(255, 235, 235));
        errorPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(244, 67, 54)),
            new EmptyBorder(20, 20, 20, 20)
        ));
        errorPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JLabel errorLabel = new JLabel("Error: " + errorMessage, JLabel.CENTER);
        errorLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        errorLabel.setForeground(new Color(244, 67, 54));

        errorPanel.add(errorLabel, BorderLayout.CENTER);
        return errorPanel;
    }

    private JPanel createMemberCard(User user) {
        JPanel card = new JPanel(new BorderLayout(15, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            new EmptyBorder(15, 20, 15, 20)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        // Left panel - User info
        JPanel userInfoPanel = new JPanel(new BorderLayout());
        userInfoPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(user.getNama());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setForeground(new Color(37, 64, 143));

        JLabel emailLabel = new JLabel(user.getEmail());
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        emailLabel.setForeground(new Color(120, 120, 120));

        JPanel infoTextPanel = new JPanel(new BorderLayout());
        infoTextPanel.setOpaque(false);
        infoTextPanel.add(nameLabel, BorderLayout.NORTH);
        infoTextPanel.add(emailLabel, BorderLayout.CENTER);

        userInfoPanel.add(infoTextPanel, BorderLayout.CENTER);

        card.add(userInfoPanel, BorderLayout.CENTER);

        // Right panel - Actions (only for project owner)
        if (isPemilikProyek && user.getUserId() != SessionManager.getInstance().getId()) {
            JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            actionPanel.setOpaque(false);

            JButton removeButton = new JButton("Hapus");
            removeButton.setBackground(new Color(244, 67, 54));
            removeButton.setForeground(Color.WHITE);
            removeButton.setFont(new Font("Arial", Font.BOLD, 11));
            removeButton.setFocusPainted(false);
            removeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            removeButton.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
            
            // Hover effect
            removeButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    removeButton.setBackground(new Color(244, 67, 54).darker());
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    removeButton.setBackground(new Color(244, 67, 54));
                }
            });

            removeButton.addActionListener(_ -> hapusAnggota(user));

            actionPanel.add(removeButton);
            card.add(actionPanel, BorderLayout.EAST);
        }

        // Add owner indicator if current user is the project owner
        if (user.getUserId() == SessionManager.getInstance().getId()) {
            JLabel ownerLabel = new JLabel("(Pemilik Proyek)");
            ownerLabel.setFont(new Font("Arial", Font.BOLD, 11));
            ownerLabel.setForeground(new Color(255, 152, 0));
            
            JPanel ownerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            ownerPanel.setOpaque(false);
            ownerPanel.add(ownerLabel);
            
            card.add(ownerPanel, BorderLayout.EAST);
        }

        return card;
    }

    private void hapusAnggota(User user) {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Apakah Anda yakin ingin menghapus " + user.getNama() + " dari proyek ini?",
            "Konfirmasi Hapus Anggota",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Assuming ProyekDAO has a method to remove member from project
                // You might need to add this method to ProyekDAO if it doesn't exist
                boolean success = proyekDAO.hapusAnggotaProyek(proyekId, String.valueOf(user.getUserId()));
                
                if (success) {
                    JOptionPane.showMessageDialog(
                        this,
                        user.getNama() + " berhasil dihapus dari proyek",
                        "Sukses",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    muatDataAnggota(); // Refresh the list
                } else {
                    JOptionPane.showMessageDialog(
                        this,
                        "Gagal menghapus anggota dari proyek",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                    this,
                    "Gagal menghapus anggota: " + e.getMessage(),
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
            
            new AnggotaProyek("1").setVisible(true);
        });
    }
}