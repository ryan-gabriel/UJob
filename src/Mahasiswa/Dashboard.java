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
import java.awt.Image;
import java.awt.RenderingHints;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Auth.SessionManager;
import Components.MahasiswaNavigation;
import Database.InboxDAO;
import Database.MahasiswaDAO;
import Database.UserDAO;
import Models.Inbox;

/**
 *
 * @author Rian G S
 */
public class Dashboard extends javax.swing.JFrame {
    
    private UserDAO userDAO;
    private MahasiswaDAO mahasiswaDAO;

    // Profile completion status
    private boolean isDataDiriComplete;
    private boolean isPendidikanComplete; 
    private boolean isPortofolioComplete;
    private boolean isPengalamanComplete;


    private String namaUser;
    private String pendidikan;

    /**
     * Creates new form Dashboard
     */
    public Dashboard() {
        userDAO = new UserDAO();
        mahasiswaDAO = new MahasiswaDAO();

        isDataDiriComplete = mahasiswaDAO.isSetRingkasan();
        isPendidikanComplete = mahasiswaDAO.isSetPendidikan();
        isPortofolioComplete = mahasiswaDAO.isSetPortofolio();
        isPengalamanComplete = mahasiswaDAO.isSetPengalaman();

        namaUser = userDAO.getNama(String.valueOf(SessionManager.getInstance().getId()));
        pendidikan = mahasiswaDAO.getCurrentMahasiswaPendidikan();

        initComponents();
        initializeUI();
    }

    public void initializeUI(){ 
            setTitle("UPI Job - Dashboard");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(1200, 700);
            setLocationRelativeTo(null);

            getContentPane().setLayout(new BorderLayout());
            getContentPane().setBackground(Color.decode("#F2F2F7"));

            MahasiswaNavigation navigation = new MahasiswaNavigation("Dashboard");
            getContentPane().add(navigation, BorderLayout.NORTH);

            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBorder(new EmptyBorder(20,20,20,20));
            mainPanel.setBackground(Color.decode("#F2F2F7"));
            getContentPane().add(mainPanel, BorderLayout.CENTER); 
            
            JLabel title = new JLabel(namaUser + "'s Dashboard");
            title.setFont(new Font("Arial", Font.BOLD, 24));
            title.setForeground(Color.decode("#333333"));
            title.setBorder(new EmptyBorder(0, 0, 20, 0));
            mainPanel.add(title, BorderLayout.NORTH);
            
            // Create main content area
            JPanel contentPanel = new JPanel(new BorderLayout());
            contentPanel.setOpaque(false);
            
            contentPanel.add(latestInbox(), BorderLayout.CENTER);
            
            JPanel bottomPanel = new JPanel(new BorderLayout());
            bottomPanel.setOpaque(false);
            bottomPanel.add(profile(), BorderLayout.CENTER);
            contentPanel.add(bottomPanel, BorderLayout.SOUTH);
            
            mainPanel.add(contentPanel, BorderLayout.CENTER);
    }
    
    public JPanel latestInbox(){
        JPanel latestInboxContainer = new JPanel(new BorderLayout()){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
            }

            @Override
            public boolean isOpaque() {
                return false;
            }
        };
        latestInboxContainer.setBorder(new EmptyBorder(25, 30, 25, 30));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel title = new JLabel("Inbox Terbaru");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(Color.decode("#333333"));
        
        headerPanel.add(title, BorderLayout.WEST);
        
        latestInboxContainer.add(headerPanel, BorderLayout.NORTH);

        JPanel inboxItemsPanel = new JPanel();
        inboxItemsPanel.setLayout(new BoxLayout(inboxItemsPanel, BoxLayout.Y_AXIS));
        inboxItemsPanel.setOpaque(false);
        inboxItemsPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        // Sample inbox items
        InboxDAO inboxDAO = new InboxDAO();
        List<Inbox> inboxItems = inboxDAO.getRecentInbox();
        
        for (Inbox item : inboxItems) {
            JPanel itemPanel = createInboxItem(item.getIsi(), item.getTanggal());
            inboxItemsPanel.add(itemPanel);
            inboxItemsPanel.add(Box.createVerticalStrut(15));
        }
        
        latestInboxContainer.add(inboxItemsPanel, BorderLayout.CENTER);
        
        // See More button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        JButton seeMoreBtn = new JButton("See More");
        seeMoreBtn.setBackground(new Color(37, 64, 143));
        seeMoreBtn.setForeground(Color.WHITE);
        seeMoreBtn.setFont(new Font("Arial", Font.BOLD, 12));
        seeMoreBtn.setBorder(new EmptyBorder(8, 20, 8, 20));
        seeMoreBtn.setFocusPainted(false);
        seeMoreBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        
        seeMoreBtn.addActionListener(_ -> {
            new Mahasiswa.Inbox().setVisible(true);
            this.dispose();
        });
        
        buttonPanel.add(seeMoreBtn);
        latestInboxContainer.add(buttonPanel, BorderLayout.SOUTH);

        JPanel latestInboxContainerWrapper = new JPanel(new BorderLayout());
        latestInboxContainerWrapper.setOpaque(false);
        latestInboxContainerWrapper.setBorder(new EmptyBorder(0, 0, 20, 0));
        latestInboxContainerWrapper.add(latestInboxContainer, BorderLayout.CENTER);
        
        return latestInboxContainerWrapper;
    }
    
    private JPanel createInboxItem(String message, String date) {
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setOpaque(false);
        itemPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.decode("#E5E5E5")));
        itemPanel.setPreferredSize(new Dimension(0, 40));
        
        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        messageLabel.setForeground(Color.decode("#333333"));
        
        JLabel dateLabel = new JLabel(date);
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        dateLabel.setForeground(Color.decode("#999999"));
        
        itemPanel.add(messageLabel, BorderLayout.WEST);
        itemPanel.add(dateLabel, BorderLayout.EAST);
        
        return itemPanel;
    }
    
    public JPanel profile(){
        JPanel profileContainer = new JPanel(new BorderLayout()){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
            }

            @Override
            public boolean isOpaque() {
                return false;
            }
        };
        profileContainer.setBorder(new EmptyBorder(25, 30, 25, 30));
        
        // Left side - Profile Status
        JPanel leftPanel = profileStatus();
        
        // Right side - Profile Info
        JPanel rightPanel = profileMahasiswa();
        
        profileContainer.add(leftPanel, BorderLayout.WEST);
        profileContainer.add(rightPanel, BorderLayout.EAST);
        
        JPanel profileContainerWrapper = new JPanel(new BorderLayout());
        profileContainerWrapper.setOpaque(false);
        profileContainerWrapper.add(profileContainer, BorderLayout.CENTER);
        
        return profileContainerWrapper;
    }
    
    // Method untuk menghitung persentase completion
    private int calculateCompletionPercentage() {
        int completedItems = 0;
        if (isDataDiriComplete) completedItems++;
        if (isPendidikanComplete) completedItems++;
        if (isPortofolioComplete) completedItems++;
        if (isPengalamanComplete) completedItems++;
        
        return (completedItems * 100) / 4; // 4 adalah total item
    }
    
    // Method untuk mendapatkan warna berdasarkan persentase
    private Color getProgressColor(int percentage) {
        if (percentage == 0) {
            return Color.decode("#E5E5E5"); // Abu-abu untuk 0%
        } else if (percentage == 25) {
            return Color.decode("#FF6B6B"); // Merah untuk 25%
        } else if (percentage == 50) {
            return Color.decode("#FFD700"); // Kuning untuk 50%
        } else if (percentage == 75) {
            return Color.decode("#4ECDC4"); // Cyan untuk 75%
        } else if (percentage == 100) {
            return Color.decode("#28A745"); // Hijau untuk 100%
        }
        return Color.decode("#E5E5E5"); // Default
    }
    
public JPanel profileStatus(){
    JPanel profileStatusContainer = new JPanel(new BorderLayout());
    profileStatusContainer.setOpaque(false);
    profileStatusContainer.setPreferredSize(new Dimension(400, 200));
    
    // Header with title only (removed info icon)
    JPanel headerPanel = new JPanel(new BorderLayout());
    headerPanel.setOpaque(false);
    
    JLabel title = new JLabel("Status Profile");
    title.setFont(new Font("Arial", Font.BOLD, 18));
    title.setForeground(Color.decode("#333333"));
    
    headerPanel.add(title, BorderLayout.WEST);
    // Removed the info icon line
    
    profileStatusContainer.add(headerPanel, BorderLayout.NORTH);
    
    // Content area
    JPanel contentPanel = new JPanel(new BorderLayout());
    contentPanel.setOpaque(false);
    contentPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
    
    // Calculate current progress
    final int progressPercentage = calculateCompletionPercentage();
    final Color progressColor = getProgressColor(progressPercentage);
    
    // Left side - Progress circle
    JPanel progressPanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;
            int radius = 40;
            
            // Background circle
            g2.setColor(Color.decode("#E5E5E5"));
            g2.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
            
            // Progress arc berdasarkan persentase
            if (progressPercentage > 0) {
                g2.setColor(progressColor);
                int arcAngle = (int) (360 * (progressPercentage / 100.0));
                g2.fillArc(centerX - radius, centerY - radius, radius * 2, radius * 2, 90, arcAngle);
            }
            
            // Inner white circle
            g2.setColor(Color.WHITE);
            g2.fillOval(centerX - 30, centerY - 30, 60, 60);
            
            // Text dengan warna yang sesuai
            g2.setColor(progressPercentage == 0 ? Color.decode("#999999") : progressColor);
            g2.setFont(new Font("Arial", Font.BOLD, 16));
            String text = progressPercentage + "%";
            int textWidth = g2.getFontMetrics().stringWidth(text);
            g2.drawString(text, centerX - textWidth/2, centerY + 5);
            
            g2.dispose();
        }
    };
    progressPanel.setOpaque(false);
    progressPanel.setPreferredSize(new Dimension(120, 120));
    
    // Right side - Status items
    JPanel statusItemsPanel = new JPanel();
    statusItemsPanel.setLayout(new BoxLayout(statusItemsPanel, BoxLayout.Y_AXIS));
    statusItemsPanel.setOpaque(false);
    statusItemsPanel.setBorder(new EmptyBorder(10, 20, 0, 0));
    
    // Status items berdasarkan variabel boolean
    String[][] statusItems = {
        {isDataDiriComplete ? "check" : "cross", "Data Diri", isDataDiriComplete ? "#28A745" : "#DC3545"},
        {isPendidikanComplete ? "check" : "cross", "Pendidikan", isPendidikanComplete ? "#28A745" : "#DC3545"},
        {isPortofolioComplete ? "check" : "cross", "Portofolio", isPortofolioComplete ? "#28A745" : "#DC3545"},
        {isPengalamanComplete ? "check" : "cross", "Pengalaman", isPengalamanComplete ? "#28A745" : "#DC3545"}
    };
    
    for (String[] item : statusItems) {
        JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        itemPanel.setOpaque(false);
        
        JLabel iconLabel = item[0].equals("check") ?  new JLabel(new ImageIcon(getClass().getResource("/images/check.png"))) :  new JLabel(new ImageIcon(getClass().getResource("/images/cross.png")));
        iconLabel.setFont(new Font("Arial", Font.BOLD, 14));
        iconLabel.setForeground(Color.decode(item[2]));
        iconLabel.setBorder(new EmptyBorder(0, 0, 0, 10));
        
        String prefix = item[0].equals("check") ? "Sudah Mengisi " : "Belum Mengisi ";
        JLabel textLabel = new JLabel(prefix + item[1]);
        textLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        textLabel.setForeground(Color.decode(item[2]));
        
        itemPanel.add(iconLabel);
        itemPanel.add(textLabel);
        statusItemsPanel.add(itemPanel);
    }
    
    contentPanel.add(progressPanel, BorderLayout.WEST);
    contentPanel.add(statusItemsPanel, BorderLayout.CENTER);
    
    profileStatusContainer.add(contentPanel, BorderLayout.CENTER);
    
    return profileStatusContainer;
}    
    // Method untuk mengupdate status completion (bisa dipanggil dari luar)
    public void updateProfileStatus(boolean dataDiri, boolean pendidikan, boolean portofolio, boolean pengalaman) {
        this.isDataDiriComplete = dataDiri;
        this.isPendidikanComplete = pendidikan;
        this.isPortofolioComplete = portofolio;
        this.isPengalamanComplete = pengalaman;
        
        // Refresh UI
        repaint();
    }
    
    public JPanel profileMahasiswa() {
        JPanel profileMahasiswaContainer = new JPanel(new BorderLayout());
        profileMahasiswaContainer.setOpaque(false);
        profileMahasiswaContainer.setPreferredSize(new Dimension(300, 200));

        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        // Icon (Profile Image)
        ImageIcon profileIcon = new ImageIcon(getClass().getResource("/images/profile.png"));
        Image image = profileIcon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        JLabel iconLabel = new JLabel(new ImageIcon(image));
        iconLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        contentPanel.add(iconLabel);
        contentPanel.add(Box.createVerticalStrut(10)); // spacing antara ikon dan nama

        // Name
        JLabel nameLabel = new JLabel(namaUser);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        nameLabel.setForeground(Color.decode("#333333"));
        nameLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        // Status
        JLabel statusLabel = new JLabel("Mahasiswa");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        statusLabel.setForeground(Color.decode("#666666"));
        statusLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        statusLabel.setBorder(new EmptyBorder(5, 0, 0, 0));

        contentPanel.add(nameLabel);
        contentPanel.add(statusLabel);
        contentPanel.add(Box.createVerticalStrut(15));

        // See Profile button
        JButton seeProfileBtn = new JButton("See Profile");
        seeProfileBtn.setBackground(new Color(37, 64, 143));
        seeProfileBtn.setForeground(Color.WHITE);
        seeProfileBtn.setFont(new Font("Arial", Font.BOLD, 12));
        seeProfileBtn.setBorder(new EmptyBorder(8, 20, 8, 20));
        seeProfileBtn.setFocusPainted(false);
        seeProfileBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        seeProfileBtn.setAlignmentX(JButton.CENTER_ALIGNMENT);

        seeProfileBtn.addActionListener(_ -> {
            new Profile(SessionManager.getInstance().getId()).setVisible(true);
            this.dispose();
        });

        contentPanel.add(seeProfileBtn);

        profileMahasiswaContainer.add(contentPanel, BorderLayout.CENTER);

        return profileMahasiswaContainer;
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
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
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Dashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}