package Mahasiswa;

import Components.MahasiswaNavigation;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class Portofolio extends JFrame {

    private JPanel githubPanel;
    private JPanel sertifikatPanel;
    private JPanel penghargaanPanel;
    private JPanel contentMainPanel;

    public Portofolio() {
        initializeUI();
        loadStaticData();
    }

    private void initializeUI() {
        setTitle("Portofolio Saya - UPI Job");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(243, 244, 246));

        // Navigation Header
        MahasiswaNavigation headerPanel = new MahasiswaNavigation("Portofolio");
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Main Content Container
        JPanel outerContainer = new JPanel(new BorderLayout());
        outerContainer.setBackground(new Color(243, 244, 246));
        outerContainer.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel mainContainer = new JPanel();
        mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));
        mainContainer.setBackground(new Color(243, 244, 246));

        // Portfolio Header dengan gradient biru
        JPanel headerPortfolio = createPortfolioHeaderPanel();
        mainContainer.add(headerPortfolio);
        
        // Spacing
        mainContainer.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // GitHub Panel
        githubPanel = createGithubProfilePanel();
        mainContainer.add(githubPanel);
        
        // Spacing
        mainContainer.add(Box.createRigidArea(new Dimension(0, 20)));

        // Sertifikat & Penghargaan Panel
        JPanel sertifikatPenghargaanPanel = createSertifikatPenghargaanPanel();
        mainContainer.add(sertifikatPenghargaanPanel);

        outerContainer.add(mainContainer, BorderLayout.NORTH);

        // Scroll Pane
        JScrollPane contentScrollPane = new JScrollPane(outerContainer);
        contentScrollPane.setBorder(BorderFactory.createEmptyBorder());
        contentScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        contentScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        contentScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainPanel.add(contentScrollPane, BorderLayout.CENTER);
        add(mainPanel);
    }

    private void loadStaticData() {
        displayGithubProfile();
        displaySertifikat();
        displayPenghargaan();
    }

    // Custom JPanel dengan gradient background
    class GradientPanel extends JPanel {
        private Color startColor;
        private Color endColor;
        
        public GradientPanel(Color startColor, Color endColor) {
            this.startColor = startColor;
            this.endColor = endColor;
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int width = getWidth();
            int height = getHeight();
            
            GradientPaint gp = new GradientPaint(0, 0, startColor, 0, height, endColor);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, width, height);
        }
    }

    // Custom JPanel untuk GitHub dengan rounded corners
    class RoundedPanel extends JPanel {
        private int radius;
        private Color backgroundColor;
        
        public RoundedPanel(int radius, Color backgroundColor) {
            this.radius = radius;
            this.backgroundColor = backgroundColor;
            setOpaque(false);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2d.setColor(backgroundColor);
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        }
    }

    private JPanel createPortfolioHeaderPanel() {
        // Header biru dengan gradient
        GradientPanel portfolioHeaderPanel = new GradientPanel(
            new Color(59, 130, 246),  // Blue-500
            new Color(29, 78, 216)    // Blue-700
        );
        portfolioHeaderPanel.setLayout(new BorderLayout());
        portfolioHeaderPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Left side - Portfolio Info
        JPanel portfolioInfoMainPanel = new JPanel();
        portfolioInfoMainPanel.setLayout(new BoxLayout(portfolioInfoMainPanel, BoxLayout.Y_AXIS));
        portfolioInfoMainPanel.setOpaque(false);

        // Portfolio Title
        JLabel titleLabel = new JLabel("Portofolio Saya");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Portfolio Subtitle
        JLabel subtitleLabel = new JLabel("Kelola dan tampilkan karya terbaik Anda");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(219, 234, 254));
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        portfolioInfoMainPanel.add(titleLabel);
        portfolioInfoMainPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        portfolioInfoMainPanel.add(subtitleLabel);

        // Right side - Button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        JButton tambahItemBtn = createTambahItemButton();
        buttonPanel.add(tambahItemBtn);

        portfolioHeaderPanel.add(portfolioInfoMainPanel, BorderLayout.WEST);
        portfolioHeaderPanel.add(buttonPanel, BorderLayout.EAST);

        return portfolioHeaderPanel;
    }

    private JButton createTambahItemButton() {
        JButton tambahItemBtn = new JButton("+ Tambah Item Portofolio");
        tambahItemBtn.setFocusPainted(false);
        tambahItemBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        tambahItemBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        
        tambahItemBtn.setBackground(new Color(30, 58, 138));
        tambahItemBtn.setForeground(Color.WHITE);
        tambahItemBtn.setBorder(new EmptyBorder(10, 20, 10, 20));
        tambahItemBtn.setOpaque(true);
        tambahItemBtn.setBorderPainted(false);
        
        // Hover effect
        tambahItemBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tambahItemBtn.setBackground(new Color(23, 37, 84));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                tambahItemBtn.setBackground(new Color(30, 58, 138));
            }
        });
        
        return tambahItemBtn;
    }

    private ImageIcon loadGithubIcon() {
        try {
            // Load GitHub icon dari images/github.png
            ImageIcon originalIcon = new ImageIcon(getClass().getResource("/images/github.png"));
            // Resize icon menjadi 32x32 pixels
            Image img = originalIcon.getImage();
            Image resizedImg = img.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            
            return new ImageIcon(resizedImg);
        } catch (Exception e) {
            System.out.println("Error loading GitHub icon: " + e.getMessage());
            return null;
        }
    }

    private ImageIcon loadGithubLogo() {
        try {
            // Load GitHub logo besar dari images/github.png
            ImageIcon originalIcon = new ImageIcon(getClass().getResource("/images/github.png"));
            // Resize logo menjadi 80x80 pixels untuk pojok kanan
            Image img = originalIcon.getImage();
            Image resizedImg = img.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            
            return new ImageIcon(resizedImg);
        } catch (Exception e) {
            System.out.println("Error loading GitHub logo: " + e.getMessage());
            return null;
        }
    }

    private JPanel createGithubProfilePanel() {
        // Panel dengan rounded corners
        RoundedPanel githubMainPanel = new RoundedPanel(12, new Color(55, 65, 81));
        githubMainPanel.setLayout(new BorderLayout());
        githubMainPanel.setBorder(new EmptyBorder(25, 30, 25, 30));
        githubMainPanel.setPreferredSize(new Dimension(0, 120));

        // Content panel
        JPanel githubContentPanel = new JPanel(new BorderLayout());
        githubContentPanel.setOpaque(false);

        // Left side - Icon and Info
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setOpaque(false);

        // GitHub Icon kecil
        JLabel githubIcon;
        ImageIcon githubIconImage = loadGithubIcon();
        
        if (githubIconImage != null) {
            githubIcon = new JLabel(githubIconImage);
        } else {
            // Fallback dengan simbol GitHub
            githubIcon = new JLabel("⚫");
            githubIcon.setFont(new Font("SansSerif", Font.BOLD, 24));
            githubIcon.setForeground(Color.WHITE);
            githubIcon.setHorizontalAlignment(SwingConstants.CENTER);
        }
        
        githubIcon.setPreferredSize(new Dimension(40, 40));

        // Info Panel
        JPanel githubInfoPanel = new JPanel();
        githubInfoPanel.setLayout(new BoxLayout(githubInfoPanel, BoxLayout.Y_AXIS));
        githubInfoPanel.setOpaque(false);
        githubInfoPanel.setBorder(new EmptyBorder(0, 15, 0, 0));

        JLabel githubTitleLabel = new JLabel("GitHub Profile");
        githubTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        githubTitleLabel.setForeground(Color.WHITE);
        githubTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel githubDescLabel = new JLabel("Showcase coding projects dan kontribusi");
        githubDescLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        githubDescLabel.setForeground(new Color(156, 163, 175));
        githubDescLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel githubUrlLabel = new JLabel("🔗 github.com/username");
        githubUrlLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        githubUrlLabel.setForeground(new Color(156, 163, 175));
        githubUrlLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        githubInfoPanel.add(githubTitleLabel);
        githubInfoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        githubInfoPanel.add(githubDescLabel);
        githubInfoPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        githubInfoPanel.add(githubUrlLabel);

        leftPanel.add(githubIcon);
        leftPanel.add(githubInfoPanel);

        // Right side - GitHub Logo Besar
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightPanel.setOpaque(false);
        
        // Load dan tampilkan logo GitHub yang sebenarnya
        JLabel githubLogo;
        ImageIcon githubLogoImage = loadGithubLogo();
        
        if (githubLogoImage != null) {
            githubLogo = new JLabel(githubLogoImage);
        } else {
            // Fallback jika gambar tidak bisa dimuat
            githubLogo = new JLabel("GitHub");
            githubLogo.setFont(new Font("SansSerif", Font.BOLD, 24));
            githubLogo.setForeground(Color.WHITE);
            githubLogo.setHorizontalAlignment(SwingConstants.CENTER);
            githubLogo.setVerticalAlignment(SwingConstants.CENTER);
            githubLogo.setPreferredSize(new Dimension(80, 80));
        }
        
        rightPanel.add(githubLogo);

        githubContentPanel.add(leftPanel, BorderLayout.WEST);
        githubContentPanel.add(rightPanel, BorderLayout.EAST);

        githubMainPanel.add(githubContentPanel, BorderLayout.CENTER);
        
        // Wrapper panel untuk memberikan shadow effect
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(new Color(243, 244, 246));
        wrapperPanel.setBorder(new EmptyBorder(2, 2, 8, 2));
        wrapperPanel.add(githubMainPanel, BorderLayout.CENTER);
        
        return wrapperPanel;
    }

    private JPanel createSertifikatPenghargaanPanel() {
        // Panel putih dengan rounded corners
        RoundedPanel mainPanel = new RoundedPanel(12, Color.WHITE);
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(25, 30, 30, 30));

        // Title
        JLabel titleLabel = new JLabel("Sertifikat & Penghargaan");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(new Color(31, 41, 55));
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Content Panel - Grid Layout
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 30, 0));
        contentPanel.setOpaque(false);

        // Initialize panels
        sertifikatPanel = new JPanel();
        sertifikatPanel.setLayout(new BoxLayout(sertifikatPanel, BoxLayout.Y_AXIS));
        sertifikatPanel.setOpaque(false);
        
        penghargaanPanel = new JPanel();
        penghargaanPanel.setLayout(new BoxLayout(penghargaanPanel, BoxLayout.Y_AXIS));
        penghargaanPanel.setOpaque(false);

        // Sertifikat Section
        JPanel sertifikatSection = new JPanel(new BorderLayout());
        sertifikatSection.setOpaque(false);
        
        // Sertifikat Label dengan rounded corners
        JPanel sertifikatLabelWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        sertifikatLabelWrapper.setOpaque(false);
        
        RoundedPanel sertifikatLabelPanel = new RoundedPanel(6, new Color(147, 51, 234));
        sertifikatLabelPanel.setLayout(new BorderLayout());
        sertifikatLabelPanel.setBorder(new EmptyBorder(8, 12, 8, 12));
        
        JLabel sertifikatLabel = new JLabel("Sertifikat");
        sertifikatLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        sertifikatLabel.setForeground(Color.WHITE);
        
        sertifikatLabelPanel.add(sertifikatLabel, BorderLayout.CENTER);
        sertifikatLabelWrapper.add(sertifikatLabelPanel);
        
        sertifikatSection.add(sertifikatLabelWrapper, BorderLayout.NORTH);
        sertifikatSection.add(sertifikatPanel, BorderLayout.CENTER);

        // Penghargaan Section
        JPanel penghargaanSection = new JPanel(new BorderLayout());
        penghargaanSection.setOpaque(false);
        
        // Penghargaan Label dengan rounded corners
        JPanel penghargaanLabelWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        penghargaanLabelWrapper.setOpaque(false);
        
        RoundedPanel penghargaanLabelPanel = new RoundedPanel(6, new Color(147, 51, 234));
        penghargaanLabelPanel.setLayout(new BorderLayout());
        penghargaanLabelPanel.setBorder(new EmptyBorder(8, 12, 8, 12));
        
        JLabel penghargaanLabel = new JLabel("Penghargaan");
        penghargaanLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        penghargaanLabel.setForeground(Color.WHITE);
        
        penghargaanLabelPanel.add(penghargaanLabel, BorderLayout.CENTER);
        penghargaanLabelWrapper.add(penghargaanLabelPanel);
        
        penghargaanSection.add(penghargaanLabelWrapper, BorderLayout.NORTH);
        penghargaanSection.add(penghargaanPanel, BorderLayout.CENTER);

        contentPanel.add(sertifikatSection);
        contentPanel.add(penghargaanSection);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Wrapper panel untuk shadow effect
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(new Color(243, 244, 246));
        wrapperPanel.setBorder(new EmptyBorder(2, 2, 8, 2));
        wrapperPanel.add(mainPanel, BorderLayout.CENTER);
        
        return wrapperPanel;
    }

    private void displayGithubProfile() {
        // GitHub profile sudah dibuat di createGithubProfilePanel()
    }

    private void displaySertifikat() {
        sertifikatPanel.removeAll();
        
        RoundedPanel sertifikatItemPanel = new RoundedPanel(8, Color.WHITE);
        sertifikatItemPanel.setLayout(new BorderLayout());
        sertifikatItemPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        JLabel sertifikatTitleLabel = new JLabel("Google IT Support Professional Certificate");
        sertifikatTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        sertifikatTitleLabel.setForeground(new Color(31, 41, 55));
        sertifikatTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sertifikatDescLabel = new JLabel("<html><div style='width: 300px;'>Sertifikat profesional dari Google dalam bidang IT Support dengan fokus pada troubleshooting, customer service, dan networking.</div></html>");
        sertifikatDescLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        sertifikatDescLabel.setForeground(new Color(107, 114, 128));
        sertifikatDescLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        contentPanel.add(sertifikatTitleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        contentPanel.add(sertifikatDescLabel);

        sertifikatItemPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Add margin
        JPanel marginPanel = new JPanel(new BorderLayout());
        marginPanel.setOpaque(false);
        marginPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
        marginPanel.add(sertifikatItemPanel, BorderLayout.CENTER);
        
        sertifikatPanel.add(marginPanel);
        sertifikatPanel.revalidate();
        sertifikatPanel.repaint();
    }

    private void displayPenghargaan() {
        penghargaanPanel.removeAll();
        
        RoundedPanel penghargaanItemPanel = new RoundedPanel(8, Color.WHITE);
        penghargaanItemPanel.setLayout(new BorderLayout());
        penghargaanItemPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        JLabel penghargaanTitleLabel = new JLabel("Juara 1 Hackathon UPI 2023");
        penghargaanTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        penghargaanTitleLabel.setForeground(new Color(31, 41, 55));
        penghargaanTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel penghargaanDescLabel = new JLabel("<html><div style='width: 300px;'>Meraih juara pertama dalam kompetisi hackathon tingkat universitas dengan tema \"Smart Campus Solution\".</div></html>");
        penghargaanDescLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        penghargaanDescLabel.setForeground(new Color(107, 114, 128));
        penghargaanDescLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        contentPanel.add(penghargaanTitleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        contentPanel.add(penghargaanDescLabel);

        penghargaanItemPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Add margin
        JPanel marginPanel = new JPanel(new BorderLayout());
        marginPanel.setOpaque(false);
        marginPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
        marginPanel.add(penghargaanItemPanel, BorderLayout.CENTER);
        
        penghargaanPanel.add(marginPanel);
        penghargaanPanel.revalidate();
        penghargaanPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new Mahasiswa.Portofolio().setVisible(true);
        });
    }
}