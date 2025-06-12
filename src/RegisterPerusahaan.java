import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;
import Database.UserDAO; // Import UserDAO
import Models.User; // Import User

public class RegisterPerusahaan extends javax.swing.JFrame {
    private char defaultEchoChar;

    public RegisterPerusahaan() {
        initUI();
    }

    private void initUI() {
        setTitle("UPI Job - Register Perusahaan");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2));

        add(createBannerPanel());
        add(createRegisterPanel());
    }

    private JPanel createBannerPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(37, 64, 143));
        panel.setBorder(new EmptyBorder(40, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;

        // 1) Logo bulat
        CirclePanel logoCircle = new CirclePanel(80, Color.WHITE);
        logoCircle.setLayout(new GridBagLayout());
        JLabel logo = new JLabel("UJOB");
        logo.setFont(new Font("Arial", Font.BOLD, 16));
        logo.setForeground(new Color(37, 64, 143));
        logoCircle.add(logo);
        panel.add(logoCircle, gbc);

        // 2) Judul “UJob Platform”
        gbc.gridy = 1;
        gbc.insets = new Insets(20, 0, 0, 0);
        JLabel title = new JLabel("UJob Platform");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        panel.add(title, gbc);

        // 3) Deskripsi rata-tengah (multi-line)
        String[] descLines = {
            "Platform pencarian kerja dan magang",
            "eksklusif untuk perusahaan",
            "Pendidikan Indonesia"
        };
        JPanel descPanel = new JPanel();
        descPanel.setOpaque(false);
        descPanel.setLayout(new BoxLayout(descPanel, BoxLayout.Y_AXIS));
        for (String line : descLines) {
            JLabel lbl = new JLabel(line);
            lbl.setFont(new Font("Arial", Font.BOLD, 14));
            lbl.setForeground(Color.WHITE);
            lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
            descPanel.add(lbl);
        }
        gbc.gridy = 2;
        gbc.insets = new Insets(20, 0, 0, 0);
        panel.add(descPanel, gbc);

        // 4) Custom check-icon (digambar Java2D)
        Icon checkIcon = new Icon() {
            private final int SIZE = 12;
            @Override public int getIconWidth()  { return SIZE; }
            @Override public int getIconHeight() { return SIZE; }
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setStroke(new BasicStroke(2f));
                g2.setColor(Color.WHITE);
                g2.drawLine(x, y + SIZE/2, x + SIZE/3, y + SIZE);
                g2.drawLine(x + SIZE/3, y + SIZE, x + SIZE, y);
                g2.dispose();
            }
        };

        // 5) Daftar fitur dengan centang
        String[] fitur = {
            "Showcase peluang kerja",
            "Terhubung dengan mahasiswa",
            "Mendapatkan akses ke portofolio",
            "Memperluas jaringan industri"
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

    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(40, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Tab Login / Register
        JPanel tab = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        tab.setOpaque(false);
        JButton loginTab = new JButton("Login");
        loginTab.setPreferredSize(new Dimension(100, 35));
        loginTab.setBackground(Color.WHITE);
        loginTab.setForeground(Color.GRAY);
        loginTab.setFont(new Font("Arial", Font.PLAIN, 11));
        loginTab.setBorder(BorderFactory.createMatteBorder(1,1,1,0, Color.LIGHT_GRAY));
        
        JButton registerTab = new JButton("Register Mahasiswa");
        registerTab.setPreferredSize(new Dimension(100, 35));
        registerTab.setBackground(Color.WHITE);
        registerTab.setForeground(Color.GRAY);
        registerTab.setFont(new Font("Arial", Font.PLAIN, 11));
        registerTab.setBorder(BorderFactory.createMatteBorder(1,1,1,0, Color.LIGHT_GRAY));
        
        JButton registerPerusahaanTab = new JButton("Register Perusahaan");
        registerPerusahaanTab.setPreferredSize(new Dimension(100, 35));
        registerPerusahaanTab.setBackground(new Color(37, 64, 143));
        registerPerusahaanTab.setForeground(Color.WHITE);
        registerPerusahaanTab.setFont(new Font("Arial", Font.PLAIN, 11));
        registerPerusahaanTab.setBorder(BorderFactory.createMatteBorder(1,0,1,1, Color.LIGHT_GRAY));

        tab.add(loginTab);
        tab.add(registerTab);
        tab.add(registerPerusahaanTab);
        gbc.gridy = 0;
        panel.add(tab, gbc);
        
        loginTab.addActionListener(e -> {
            // buka window Login
            new Login().setVisible(true);
            // tutup window Register saat ini
            SwingUtilities.getWindowAncestor(loginTab).dispose();
        });
       
        registerTab.addActionListener(e -> {
            // buka window Login
            new RegisterMahasiswa().setVisible(true);
            this.dispose();
        });

        // Welcome text
        gbc.gridy = 1;
        gbc.insets = new Insets(20, 0, 5, 0);
        JLabel welcome = new JLabel("WELCOME PERUSAHAAN", SwingConstants.CENTER);
        welcome.setFont(new Font("Arial", Font.PLAIN, 11));
        welcome.setForeground(Color.GRAY);
        panel.add(welcome, gbc);

        // Main title
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        JLabel mainTitle = new JLabel("Daftar UPI Job", SwingConstants.CENTER);
        mainTitle.setFont(new Font("Arial", Font.BOLD, 18));
        mainTitle.setForeground(Color.BLACK);
        panel.add(mainTitle, gbc);

        // Fields
        int y = 3;
        JTextField companyName = new JTextField();
        addLabeledField(panel, "Nama Perusahaan", companyName, y++, gbc);

        JTextField email = new JTextField();
        addLabeledField(panel, "Email Perusahaan", email, y++, gbc);

        // Password dengan eye-toggle
        JLabel pwdLabel = new JLabel("Password");
        pwdLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        pwdLabel.setForeground(Color.DARK_GRAY);
        gbc.gridy = y++;
        gbc.insets = new Insets(10, 0, 5, 0);
        panel.add(pwdLabel, gbc);

        JPanel pwdPanel = new JPanel(new BorderLayout());
        pwdPanel.setOpaque(false);
        JPasswordField pwdField = new JPasswordField();
        pwdField.setPreferredSize(new Dimension(300, 35));
        pwdField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        GhostText ghostText = new GhostText(pwdField, "Minimal 8 karakter");

        JButton eye = new JButton("👁");
        eye.setBorder(null);
        eye.setContentAreaFilled(false);
        eye.setPreferredSize(new Dimension(30, 30));
        eye.addActionListener(e -> {
            if (pwdField.getEchoChar() != (char)0) {
                pwdField.setEchoChar((char)0);
            } else {
                pwdField.setEchoChar(((JPasswordField)pwdField).getEchoChar());
            }
        });

        pwdPanel.add(pwdField, BorderLayout.CENTER);
        pwdPanel.add(eye, BorderLayout.EAST);
        gbc.gridy = y++;
        gbc.insets = new Insets(0, 0, 15, 0);
        panel.add(pwdPanel, gbc);

        // Register button
        JButton btn = new JButton("REGISTER");
        btn.setPreferredSize(new Dimension(300, 40));
        btn.setBackground(new Color(37, 64, 143));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setBorder(null);
        gbc.gridy = y++;
        gbc.insets = new Insets(0, 0, 15, 0);
        panel.add(btn, gbc);

        // Tambahkan ActionListener untuk tombol register
        btn.addActionListener(e -> {
            String nama = companyName.getText();
            String emailAddress = email.getText();
            String passwordValue = new String(pwdField.getPassword());

            // Automatically set role to "Perusahaan"
            String roleValue = "Perusahaan";

            User newUser = new User(nama, emailAddress, passwordValue, roleValue);
            UserDAO userDAO = new UserDAO();

            // Cek jika email sudah terdaftar
            if (userDAO.isEmailTaken(emailAddress)) {
                JOptionPane.showMessageDialog(this, "Email sudah terdaftar!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // Lakukan pendaftaran
                if (userDAO.register(newUser)) {
                    JOptionPane.showMessageDialog(this, "Pendaftaran berhasil!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    // Bisa ditambahkan logika untuk kembali ke halaman login atau lainnya
                } else {
                    JOptionPane.showMessageDialog(this, "Pendaftaran gagal, silakan coba lagi.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        return panel;
    }

    private void addLabeledField(JPanel panel, String labelText, JTextField field,
                                  int gridy, GridBagConstraints gbc) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        label.setForeground(Color.DARK_GRAY);
        gbc.gridy = gridy;
        gbc.insets = new Insets(10, 0, 5, 0);
        panel.add(label, gbc);

        field.setPreferredSize(new Dimension(300, 35));
        field.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        new GhostText(field, "Masukkan " + labelText.toLowerCase());
        gbc.gridy = gridy + 1;
        gbc.insets = new Insets(0, 0, 15, 0);
        panel.add(field, gbc);
    }

    // Panel custom berbentuk lingkaran
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
            g2.setColor(bg);
            g2.fillOval(0, 0, diameter, diameter);
            g2.dispose();
        }
    }

    // Placeholder text + echo-char handling
    private static class GhostText implements FocusListener {
        private final JTextComponent comp;
        private final String ghost;
        private boolean showing = true;
        private char echo;
        private final boolean isPwd;

        GhostText(JTextComponent comp, String ghostText) {
            this.comp = comp;
            this.ghost = ghostText;
            this.isPwd = comp instanceof JPasswordField;
            if (isPwd) {
                JPasswordField pf = (JPasswordField) comp;
                echo = pf.getEchoChar();
                pf.setEchoChar((char)0);
            }
            comp.setText(ghost);
            comp.setForeground(Color.GRAY);
            comp.addFocusListener(this);
        }

        @Override
        public void focusGained(FocusEvent e) {
            if (showing) {
                comp.setText("");
                comp.setForeground(Color.BLACK);
                if (isPwd) {
                    ((JPasswordField) comp).setEchoChar(echo);
                }
                showing = false;
            }
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (comp.getText().isEmpty()) {
                if (isPwd) {
                    ((JPasswordField) comp).setEchoChar((char)0);
                }
                comp.setText(ghost);
                comp.setForeground(Color.GRAY);
                showing = true;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegisterPerusahaan().setVisible(true));
    }
}
