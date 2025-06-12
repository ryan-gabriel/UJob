import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import Database.UserDAO;
import Mahasiswa.Dashboard;
import Models.User;

// Asumsi Register.java berada dalam package yang sama, sehingga tidak perlu import eksplisit.
// Jika berada di package lain (misal: Auth), tambahkan 'import Auth.Register;'

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

        getContentPane().setLayout(new GridLayout(1, 2));

        JPanel bannerPanel = createBannerPanel();
        JPanel rightPanel = createLoginPanel();

        getContentPane().add(bannerPanel);
        getContentPane().add(rightPanel);
    }
    
    private JPanel createBannerPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(37, 64, 143));
        panel.setBorder(new EmptyBorder(40, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 0;

        CirclePanel logoCircle = new CirclePanel(80, Color.WHITE);
        logoCircle.setLayout(new GridBagLayout());
        JLabel logo = new JLabel("UJOB");
        logo.setFont(new Font("Arial", Font.BOLD, 16));
        logo.setForeground(new Color(37, 64, 143));
        logoCircle.add(logo);
        panel.add(logoCircle, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(20, 0, 0, 0);
        JLabel title = new JLabel("UJob Platform");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        panel.add(title, gbc);

        String[] descLines = {
            "Platform pencarian kerja dan magang",
            "eksklusif untuk mahasiswa Universitas",
            "Pendidikan Indonesia"
        };
        JPanel descPanel = new JPanel();
        descPanel.setOpaque(false);
        descPanel.setLayout(new BoxLayout(descPanel, BoxLayout.Y_AXIS));
        for (String line : descLines) {
            JLabel lbl = new JLabel(line);
            lbl.setFont(new Font("Arial", Font.PLAIN, 14));
            lbl.setForeground(Color.WHITE);
            lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
            descPanel.add(lbl);
        }
        gbc.gridy = 2;
        gbc.insets = new Insets(20, 0, 0, 0);
        panel.add(descPanel, gbc);

        Icon checkIcon = new Icon() {
            private final int SIZE = 12;
            @Override public int getIconWidth()  { return SIZE; }
            @Override public int getIconHeight() { return SIZE; }
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setStroke(new BasicStroke(2f));
                g2.setColor(Color.WHITE);
                g2.drawLine(x, y + SIZE/2, x + SIZE/3, y + SIZE);
                g2.drawLine(x + SIZE/3, y + SIZE, x + SIZE, y);
                g2.dispose();
            }
        };

        String[] fitur = {
            "Showcase portofolio akademik",
            "Temukan peluang magang & kerja",
            "Kolaborasi dengan sesama mahasiswa",
            "Koneksi dengan dunia industri"
        };
        JPanel fiturPanel = new JPanel(new GridLayout(fitur.length, 1, 0, 5));
        fiturPanel.setOpaque(false);
        for (String text : fitur) {
            JLabel lbl = new JLabel(text, checkIcon, SwingConstants.LEFT);
            lbl.setFont(new Font("Arial", Font.PLAIN, 13));
            lbl.setForeground(Color.WHITE);
            fiturPanel.add(lbl);
        }
        gbc.gridy = 3;
        gbc.insets = new Insets(30, 0, 0, 0);
        panel.add(fiturPanel, gbc);

        return panel;
    }

    private JPanel createLoginPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(new GridBagLayout());
        rightPanel.setBorder(new EmptyBorder(40, 40, 40, 40));
        
        JPanel loginContainer = new JPanel();
        loginContainer.setLayout(new GridBagLayout());
        loginContainer.setOpaque(false);
        loginContainer.setPreferredSize(new Dimension(350, 400));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);
        
        JPanel tabPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        tabPanel.setOpaque(false);
        
        JButton loginTab = new JButton("Login");
        loginTab.setPreferredSize(new Dimension(110, 35));
        loginTab.setBackground(new Color(37, 64, 143));
        loginTab.setForeground(Color.WHITE);
        loginTab.setBorder(BorderFactory.createEmptyBorder());
        loginTab.setFont(new Font("Arial", Font.PLAIN, 11));
        
        JButton registerMahasiswaTab = new JButton("Register Mahasiswa");
        registerMahasiswaTab.setPreferredSize(new Dimension(110, 35));
        registerMahasiswaTab.setBackground(Color.LIGHT_GRAY);
        registerMahasiswaTab.setForeground(Color.GRAY);
        registerMahasiswaTab.setBorder(BorderFactory.createEmptyBorder());
        registerMahasiswaTab.setFont(new Font("Arial", Font.PLAIN, 11));

        // --- PERUBAHAN DIMULAI: Memperbaiki aksi tombol Register ---
        registerMahasiswaTab.addActionListener(e -> {
            new RegisterMahasiswa().setVisible(true); // Membuka halaman Register
            this.dispose(); // Menutup halaman Login
        });

        JButton registerPerusahaanTab = new JButton("Register Perusahaan");
        registerPerusahaanTab.setPreferredSize(new Dimension(110, 35));
        registerPerusahaanTab.setBackground(Color.LIGHT_GRAY);
        registerPerusahaanTab.setForeground(Color.GRAY);
        registerPerusahaanTab.setBorder(BorderFactory.createEmptyBorder());
        registerPerusahaanTab.setFont(new Font("Arial", Font.PLAIN, 11));

        // --- PERUBAHAN DIMULAI: Memperbaiki aksi tombol Register ---
        registerPerusahaanTab.addActionListener(e -> {
            new RegisterPerusahaan().setVisible(true); // Membuka halaman Register
            this.dispose(); // Menutup halaman Login
        });
        
        tabPanel.add(loginTab);
        tabPanel.add(registerMahasiswaTab);
        tabPanel.add(registerPerusahaanTab);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        loginContainer.add(tabPanel, gbc);
        gbc.weightx = 0; // reset after use
        gbc.fill = GridBagConstraints.HORIZONTAL; // keep fill for next components
        
        JLabel welcomeLabel = new JLabel("WELCOME SOBAT UPI");
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        welcomeLabel.setForeground(Color.GRAY);
        
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 5, 0);
        loginContainer.add(welcomeLabel, gbc);
        
        JLabel mainTitle = new JLabel("Masuk ke UPI Job");
        mainTitle.setFont(new Font("Arial", Font.BOLD, 18));
        mainTitle.setForeground(Color.BLACK);
        
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        loginContainer.add(mainTitle, gbc);
        
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
        
        JButton loginButton = new JButton("LOGIN");
        loginButton.setPreferredSize(new Dimension(300, 40));
        loginButton.setBackground(new Color(37, 64, 143));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBorder(BorderFactory.createEmptyBorder());
        loginButton.setFont(new Font("Arial", Font.BOLD, 12));
        
        gbc.gridy = 7;
        gbc.insets = new Insets(0, 0, 15, 0);
        loginContainer.add(loginButton, gbc);

        loginButton.addActionListener(_ -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Email dan password tidak boleh kosong", "Login Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                UserDAO userDAO = new UserDAO();
                User user = userDAO.login(email, password);

                if (user == null) {
                    JOptionPane.showMessageDialog(this, "Email atau password salah", "Login Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    String role = user.getRole();
                    
                    if ("mahasiswa".equalsIgnoreCase(role)) {
                        new Dashboard().setVisible(true);
                        this.dispose();
                    } else if ("perusahaan".equalsIgnoreCase(role)) {
                        new Perusahaan.Dashboard().setVisible(true);
                        this.dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Login berhasil tetapi role tidak dikenali: " + role, "Login Info", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat login: " + ex.getMessage(), "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JLabel registerLink = new JLabel("<html><a href=''>Don't Have an Account Yet? Register</a></html>");
        registerLink.setFont(new Font("Arial", Font.PLAIN, 11));
        registerLink.setForeground(Color.GRAY);
        registerLink.setHorizontalAlignment(SwingConstants.CENTER);
        
        // --- PERUBAHAN DIMULAI: Memperbaiki aksi tautan Register ---
        registerLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new Register().setVisible(true); // Membuka halaman Register
                dispose(); // Menutup halaman Login
            }
        });
        // --- PERUBAHAN SELESAI ---
        
        gbc.gridy = 8;
        gbc.insets = new Insets(0, 0, 0, 0);
        loginContainer.add(registerLink, gbc);
        
        GridBagConstraints rightGbc = new GridBagConstraints();
        rightGbc.anchor = GridBagConstraints.CENTER;
        rightPanel.add(loginContainer, rightGbc);
        
        return rightPanel;
    }

    static class CirclePanel extends JPanel {
        private final int diameter;
        private final Color bg;

        public CirclePanel(int diameter, Color bg) {
            this.diameter = diameter;
            this.bg = bg;
            setPreferredSize(new Dimension(diameter, diameter));
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg);
            g2.fillOval(0, 0, diameter, diameter);
            g2.dispose();
        }
    }

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

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}