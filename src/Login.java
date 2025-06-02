
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author Rian G S
 */
public class Login extends javax.swing.JFrame {

    /**
     * Creates new form Login
     */
    public Login() {
        initComponents();
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("UPI Job - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);

        getContentPane().setLayout(new GridLayout(1, 2)); // Bagi dua kolom setara

        // Panel kiri (banner)
        JPanel bannerPanel = createBannerPanel();
        
        // Panel kanan (form login)
        JPanel rightPanel = createLoginPanel();

        getContentPane().add(bannerPanel);
        getContentPane().add(rightPanel);
    }
    
    private JPanel createBannerPanel() {
        JPanel bannerPanel = new JPanel();
        bannerPanel.setBackground(new Color(37, 64, 143)); // Warna biru sesuai gambar
        bannerPanel.setLayout(new GridBagLayout());
        bannerPanel.setBorder(new EmptyBorder(40, 40, 40, 40));

        // Container untuk semua konten
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Logo UJOB dalam lingkaran
        JPanel logoContainer = new JPanel();
        logoContainer.setBackground(Color.WHITE);
        logoContainer.setPreferredSize(new Dimension(80, 80));
        logoContainer.setLayout(new GridBagLayout());
        logoContainer.setBorder(BorderFactory.createEmptyBorder());
        // Membuat lingkaran (akan terlihat seperti oval karena keterbatasan Swing)
        
        JLabel logoLabel = new JLabel("UJOB");
        logoLabel.setForeground(new Color(37, 64, 143));
        logoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logoContainer.add(logoLabel);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 20, 0);
        contentPanel.add(logoContainer, gbc);

        // Title "UJob Platform"
        JLabel titleLabel = new JLabel("UJob Platform");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 20, 0);
        contentPanel.add(titleLabel, gbc);

        // Deskripsi
        JTextArea desc = new JTextArea("Platform pencarian kerja dan magang\neksklusif untuk mahasiswa Universitas\nPendidikan Indonesia");
        desc.setLineWrap(false);
        desc.setWrapStyleWord(true);
        desc.setOpaque(false);
        desc.setEditable(false);
        desc.setFocusable(false);
        desc.setFont(new Font("Arial", Font.PLAIN, 14));
        desc.setForeground(Color.WHITE);
        desc.setBackground(new Color(0, 0, 0, 0));
        
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 30, 0);
        contentPanel.add(desc, gbc);
        
        // Fitur-fitur dengan checkmark
        String[] features = {
            "✓  Showcase portofolio akademik",
            "✓  Temukan peluang magang & kerja",
            "✓  Kolaborasi dengan sesama mahasiswa",
            "✓  Koneksi dengan dunia industri"
        };
        
        JPanel featuresPanel = new JPanel();
        featuresPanel.setLayout(new GridBagLayout());
        featuresPanel.setOpaque(false);
        
        GridBagConstraints featureGbc = new GridBagConstraints();
        featureGbc.anchor = GridBagConstraints.WEST;
        featureGbc.insets = new Insets(5, 0, 5, 0);
        
        for (int i = 0; i < features.length; i++) {
            JLabel featureLabel = new JLabel(features[i]);
            featureLabel.setForeground(Color.WHITE);
            featureLabel.setFont(new Font("Arial", Font.PLAIN, 13));
            
            featureGbc.gridy = i;
            featuresPanel.add(featureLabel, featureGbc);
        }
        
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 0, 0);
        contentPanel.add(featuresPanel, gbc);
        
        // Add content panel to banner panel
        GridBagConstraints bannerGbc = new GridBagConstraints();
        bannerGbc.anchor = GridBagConstraints.CENTER;
        bannerPanel.add(contentPanel, bannerGbc);
        
        return bannerPanel;
    }
    
    private JPanel createLoginPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(new GridBagLayout());
        rightPanel.setBorder(new EmptyBorder(40, 40, 40, 40));
        
        // Container untuk form login
        JPanel loginContainer = new JPanel();
        loginContainer.setLayout(new GridBagLayout());
        loginContainer.setOpaque(false);
        loginContainer.setPreferredSize(new Dimension(350, 400));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);
        
        // Tab Login/Register (simulasi)
        JPanel tabPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        tabPanel.setOpaque(false);
        
        JButton loginTab = new JButton("Login");
        loginTab.setPreferredSize(new Dimension(100, 35));
        loginTab.setBackground(new Color(37, 64, 143));
        loginTab.setForeground(Color.WHITE);
        loginTab.setBorder(BorderFactory.createEmptyBorder());
        loginTab.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JButton registerTab = new JButton("Register");
        registerTab.setPreferredSize(new Dimension(100, 35));
        registerTab.setBackground(Color.LIGHT_GRAY);
        registerTab.setForeground(Color.GRAY);
        registerTab.setBorder(BorderFactory.createEmptyBorder());
        registerTab.setFont(new Font("Arial", Font.PLAIN, 12));
        
        tabPanel.add(loginTab);
        tabPanel.add(registerTab);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        loginContainer.add(tabPanel, gbc);
        
        // Welcome text
        JLabel welcomeLabel = new JLabel("WELCOME SOBAT UPI");
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        welcomeLabel.setForeground(Color.GRAY);
        
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 5, 0);
        loginContainer.add(welcomeLabel, gbc);
        
        // Main title
        JLabel mainTitle = new JLabel("Masuk ke UPI Job");
        mainTitle.setFont(new Font("Arial", Font.BOLD, 18));
        mainTitle.setForeground(Color.BLACK);
        
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        loginContainer.add(mainTitle, gbc);
        
        // Email field
        JLabel emailLabel = new JLabel("Email UPI");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        emailLabel.setForeground(Color.DARK_GRAY);
        
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 5, 0);
        loginContainer.add(emailLabel, gbc);
        
        JTextField emailField = new JTextField();
        emailField.setPreferredSize(new Dimension(300, 35));
        emailField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        emailField.setFont(new Font("Arial", Font.PLAIN, 12));
        
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 15, 0);
        loginContainer.add(emailField, gbc);
        
        // Password field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        passwordLabel.setForeground(Color.DARK_GRAY);
        
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 5, 0);
        loginContainer.add(passwordLabel, gbc);
        
        JPasswordField passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(300, 35));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 12));
        
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 20, 0);
        loginContainer.add(passwordField, gbc);
        
        // Login button
        JButton loginButton = new JButton("LOGIN");
        loginButton.setPreferredSize(new Dimension(300, 40));
        loginButton.setBackground(new Color(37, 64, 143));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBorder(BorderFactory.createEmptyBorder());
        loginButton.setFont(new Font("Arial", Font.BOLD, 12));
        
        gbc.gridy = 7;
        gbc.insets = new Insets(0, 0, 15, 0);
        loginContainer.add(loginButton, gbc);
        
        // Register link
        JLabel registerLink = new JLabel("Don't Have an Account Yet? Register");
        registerLink.setFont(new Font("Arial", Font.PLAIN, 11));
        registerLink.setForeground(Color.GRAY);
        registerLink.setHorizontalAlignment(SwingConstants.CENTER);
        
        gbc.gridy = 8;
        gbc.insets = new Insets(0, 0, 0, 0);
        loginContainer.add(registerLink, gbc);
        
        // Add login container to right panel
        GridBagConstraints rightGbc = new GridBagConstraints();
        rightGbc.anchor = GridBagConstraints.CENTER;
        rightPanel.add(loginContainer, rightGbc);
        
        return rightPanel;
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
            .addGap(0, 1200, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 700, Short.MAX_VALUE)
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
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
