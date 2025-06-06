/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Mahasiswa;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import Components.ProyekHeaderPanel;
import Components.ProyekTabs;
/**
 *
 * @author Rian G S
 */
public class CariProyek extends javax.swing.JFrame {

    /**
     * Creates new form CariProyek
     */
    public CariProyek() {
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("UPI Job - Proyek");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setBackground(Color.WHITE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 247, 250));
        
        ProyekHeaderPanel headerPanel = new ProyekHeaderPanel("Cari Proyek");
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        JPanel contentPanel = createContentPanel();
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }

    
    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(245, 247, 250));
        contentPanel.setBorder(new EmptyBorder(10, 40, 30, 40));
        
        JPanel topSection = new JPanel(new GridBagLayout());
        topSection.setOpaque(false);
        
        
        // Search and button panel
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setOpaque(false);
        
        JTextField searchField = new JTextField("Cari nama...");
        searchField.setPreferredSize(new Dimension(200, 40));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 10, 5, 15)
        ));
        searchField.setForeground(Color.GRAY);
        searchField.setFont(new Font("Arial", Font.PLAIN, 13));
        
        JPanel buatProyekBtnWrapper = new JPanel(new GridBagLayout());
        buatProyekBtnWrapper.setOpaque(false);
        buatProyekBtnWrapper.setBorder(new EmptyBorder(0, 20, 0, 0));
        
        GridBagConstraints buatProyekBtnWrapperGbc = new GridBagConstraints();
        buatProyekBtnWrapperGbc.fill = GridBagConstraints.HORIZONTAL;
        buatProyekBtnWrapperGbc.weightx = 1.0;
        
        JButton buatProyekBtn = new JButton("+ Buat Proyek Baru");
        buatProyekBtn.setPreferredSize(new Dimension(180, 40));
        buatProyekBtn.setBackground(new Color(34, 197, 94));
        buatProyekBtn.setForeground(Color.WHITE);
        buatProyekBtn.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        buatProyekBtn.setFont(new Font("Arial", Font.BOLD, 13));
        buatProyekBtn.setFocusPainted(false);
        
        buatProyekBtnWrapper.add(buatProyekBtn, buatProyekBtnWrapperGbc);
        
        GridBagConstraints searchPanelGbc = new GridBagConstraints();
        searchPanelGbc.fill = GridBagConstraints.HORIZONTAL;
        searchPanelGbc.insets = new Insets(0, 0, 0, 0); // spacing antara tombol
        searchPanelGbc.weighty = 1.0;
        
        searchPanelGbc.gridx = 0;
        searchPanelGbc.weightx = 0.75;
        searchPanel.add(searchField, searchPanelGbc);
        
        searchPanelGbc.gridx = 1;
        searchPanelGbc.weightx = 0.25;
        searchPanel.add(buatProyekBtnWrapper, searchPanelGbc);
        
        GridBagConstraints gbcTopSection = new GridBagConstraints();
        
        gbcTopSection.anchor = GridBagConstraints.CENTER;
        gbcTopSection.insets = new Insets(10,10,10,10);
        gbcTopSection.weightx = 1.0;
        gbcTopSection.fill = GridBagConstraints.HORIZONTAL;
        
        gbcTopSection.gridx = 0;
        gbcTopSection.gridy = 0;
        
        topSection.setBorder(new EmptyBorder(0,0,0,0));
        
        topSection.add(searchPanel, gbcTopSection);
        searchPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        contentPanel.add(topSection, BorderLayout.NORTH);
        
        // Projects scroll panel
        JPanel projectsContainer = createProjectsPanel();
        JScrollPane scrollPane = new JScrollPane(projectsContainer);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(new Color(245, 247, 250));
        scrollPane.getViewport().setBackground(new Color(245, 247, 250));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        contentPanel.add(Box.createVerticalStrut(20), BorderLayout.CENTER);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        return contentPanel;
    }
    
    private JPanel createProjectsPanel() {
        JPanel projectsPanel = new JPanel();
        projectsPanel.setLayout(new GridLayout(0, 2, 20, 20)); // 2 columns
        projectsPanel.setBackground(new Color(245, 247, 250));
        projectsPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        // Sample projects data
        String[][] projects = {
            {"Aplikasi Pengingat Jadwal Kuliah Berbasis Mobile", "21 Mei 2025", "25 Mei 2025", "Membuat aplikasi mobile sederhana yang membantu mahasiswa mengatur dan mengingat jadwal kuliah, tugas, dan ujian dengan notifikasi otomatis."},
            {"Sistem Pendaftaran Online untuk Organisasi Kampus", "21 Mei 2025", "25 Mei 2025", "Mengembangkan platform web yang memungkinkan mahasiswa mendaftar dan mengelola keanggotaan organisasi kampus secara digital dan mudah."},
            {"Website E-Commerce untuk Produk Mahasiswa", "20 Mei 2025", "30 Mei 2025", "Membangun platform jual beli online khusus untuk produk yang dibuat oleh mahasiswa, seperti makanan, kerajinan, dan jasa."},
            {"Aplikasi Manajemen Keuangan Pribadi", "19 Mei 2025", "28 Mei 2025", "Mengembangkan aplikasi mobile untuk membantu mahasiswa mengelola keuangan pribadi, mencatat pengeluaran, dan membuat budget bulanan."},
            {"Platform Sharing Catatan Kuliah", "18 Mei 2025", "27 Mei 2025", "Membuat website untuk berbagi catatan kuliah antar mahasiswa dengan sistem rating dan komentar untuk meningkatkan kualitas pembelajaran."},
            {"Sistem Informasi Perpustakaan Digital", "17 Mei 2025", "26 Mei 2025", "Mengembangkan sistem manajemen perpustakaan digital dengan fitur peminjaman online, katalog buku, dan notifikasi pengembalian."}
        };
        
        for (String[] project : projects) {
            JPanel projectCard = createProjectCard(project[0], project[1], project[2], project[3]);
            projectsPanel.add(projectCard);
        }
        
        return projectsPanel;
    }
    
    private JPanel createProjectCard(String title, String startDate, String endDate, String description) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            new EmptyBorder(20, 20, 20, 20)
        ));
        card.setPreferredSize(new Dimension(0, 200));
        
        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(37, 64, 143));
        
        // Dates panel
        JPanel datesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        datesPanel.setOpaque(false);
        
        JLabel startLabel = new JLabel("Diposting pada " + startDate);
        startLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        startLabel.setForeground(Color.GRAY);
        
        JLabel endLabel = new JLabel("Ditutup pada " + endDate);
        endLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        endLabel.setForeground(Color.RED);
        endLabel.setBorder(new EmptyBorder(0, 0, 0, 0));
        
        datesPanel.add(startLabel);
        datesPanel.add(Box.createHorizontalStrut(20));
        datesPanel.add(endLabel);
        
        // Description
        JLabel descLabel = new JLabel("<html><p style='line-height: 1.4;'>" + description + "</p></html>");
        descLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        descLabel.setForeground(Color.DARK_GRAY);
        descLabel.setBorder(new EmptyBorder(10, 0, 15, 0));
        
        // Button
        JButton daftarBtn = new JButton("Daftar");
        daftarBtn.setPreferredSize(new Dimension(100, 35));
        daftarBtn.setBackground(new Color(37, 64, 143));
        daftarBtn.setForeground(Color.WHITE);
        daftarBtn.setBorder(BorderFactory.createEmptyBorder());
        daftarBtn.setFont(new Font("Arial", Font.BOLD, 12));
        daftarBtn.setFocusPainted(false);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(daftarBtn);
        
        // Top panel (title + dates)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(datesPanel, BorderLayout.SOUTH);
        
        card.add(topPanel, BorderLayout.NORTH);
        card.add(descLabel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.SOUTH);
        
        return card;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    @SuppressWarnings("unused")
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CariProyek.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CariProyek.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CariProyek.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CariProyek.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CariProyek().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
