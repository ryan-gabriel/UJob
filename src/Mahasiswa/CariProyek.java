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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
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

        // Main container
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 247, 250));
        // Header
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Content panel
        JPanel contentPanel = createContentPanel();
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(new Color(37, 64, 143));
        headerPanel.setPreferredSize(new Dimension(0, 250));
        headerPanel.setBorder(new EmptyBorder(20,20,20,20));
        // Navigation bar
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setBackground(new Color(37, 64, 143));
        navPanel.setBorder(new EmptyBorder(15, 40, 15, 40));
        
        // Logo and brand
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        logoPanel.setOpaque(false);
        
        JLabel logoCircle = new JLabel("UJOB");
        logoCircle.setPreferredSize(new Dimension(40, 40));
        logoCircle.setHorizontalAlignment(SwingConstants.CENTER);
        logoCircle.setVerticalAlignment(SwingConstants.CENTER);
        logoCircle.setBackground(Color.WHITE);
        logoCircle.setOpaque(true);
        logoCircle.setForeground(new Color(37, 64, 143));
        logoCircle.setFont(new Font("Arial", Font.BOLD, 12));
        logoCircle.setBorder(BorderFactory.createEmptyBorder());
        
        JLabel brandLabel = new JLabel("UPI Job");
        brandLabel.setForeground(Color.WHITE);
        brandLabel.setFont(new Font("Arial", Font.BOLD, 18));
        brandLabel.setBorder(new EmptyBorder(0, 10, 0, 0));
        
        logoPanel.add(logoCircle);
        logoPanel.add(brandLabel);
        
        // Navigation menu
        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        menuPanel.setOpaque(false);
        
        String[] menuItems = {"Dashboard", "Profil", "Portofolio", "Lowongan", "Proyek", "Inbox"};
        for (String item : menuItems) {
            JLabel menuLabel = new JLabel(item);
            menuLabel.setForeground(item.equals("Proyek") ? new Color(255, 215, 0) : Color.WHITE);
            menuLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            menuLabel.setBorder(new EmptyBorder(5, 10, 5, 10));
            if (item.equals("Proyek")) {
                menuLabel.setFont(new Font("Arial", Font.BOLD, 14));
            }
            menuPanel.add(menuLabel);
        }
        
        navPanel.add(logoPanel, BorderLayout.WEST);
        navPanel.add(menuPanel, BorderLayout.EAST);
        
        headerPanel.add(navPanel, BorderLayout.NORTH);
        
        // Hero section
        JPanel heroPanel = new JPanel();
        heroPanel.setLayout(new BoxLayout(heroPanel, BoxLayout.Y_AXIS));
        heroPanel.setBackground(new Color(120, 119, 198)); // Gradient effect simulation
        heroPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        
        JLabel heroTitle = new JLabel("Cari Teman & Bantu Proyek");
        heroTitle.setForeground(Color.WHITE);
        heroTitle.setFont(new Font("Arial", Font.BOLD, 28));
        heroTitle.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        
        JLabel heroSubtitle = new JLabel("Temukan kolaborator ideal dan bergabung dengan proyek menarik");
        heroSubtitle.setForeground(Color.WHITE);
        heroSubtitle.setFont(new Font("Arial", Font.PLAIN, 16));
        heroSubtitle.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        heroSubtitle.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        heroPanel.add(heroTitle);
        heroPanel.add(heroSubtitle);
        
        headerPanel.add(heroPanel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(245, 247, 250));
        contentPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        
        // Top section with tabs and search
        JPanel topSection = new JPanel(new BorderLayout());
        topSection.setOpaque(false);
        
        // Tabs panel
        JPanel tabsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        tabsPanel.setOpaque(false);
        
        JButton cariTemanTab = new JButton("Cari Teman");
        cariTemanTab.setPreferredSize(new Dimension(150, 45));
        cariTemanTab.setBackground(Color.WHITE);
        cariTemanTab.setForeground(Color.GRAY);
        cariTemanTab.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY));
        cariTemanTab.setFont(new Font("Arial", Font.PLAIN, 14));
        cariTemanTab.setFocusPainted(false);
        
        JButton cariProyekTab = new JButton("Cari Proyek");
        cariProyekTab.setPreferredSize(new Dimension(150, 45));
        cariProyekTab.setBackground(Color.WHITE);
        cariProyekTab.setForeground(new Color(37, 64, 143));
        cariProyekTab.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(37, 64, 143)));
        cariProyekTab.setFont(new Font("Arial", Font.BOLD, 14));
        cariProyekTab.setFocusPainted(false);
        
        tabsPanel.add(cariTemanTab);
        tabsPanel.add(cariProyekTab);
        
        // Search and button panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        searchPanel.setOpaque(false);
        
        JTextField searchField = new JTextField("Cari nama...");
        searchField.setPreferredSize(new Dimension(200, 40));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        searchField.setForeground(Color.GRAY);
        searchField.setFont(new Font("Arial", Font.PLAIN, 13));
        
        JButton buatProyekBtn = new JButton("+ Buat Proyek Baru");
        buatProyekBtn.setPreferredSize(new Dimension(180, 40));
        buatProyekBtn.setBackground(new Color(34, 197, 94));
        buatProyekBtn.setForeground(Color.WHITE);
        buatProyekBtn.setBorder(BorderFactory.createEmptyBorder());
        buatProyekBtn.setFont(new Font("Arial", Font.BOLD, 13));
        buatProyekBtn.setFocusPainted(false);
        
        searchPanel.add(searchField);
        searchPanel.add(buatProyekBtn);
        
        topSection.add(tabsPanel, BorderLayout.WEST);
        topSection.add(searchPanel, BorderLayout.EAST);
        
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
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
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
