package Mahasiswa;

import Components.MahasiswaNavigation;
import Models.Portofolioo;
import Database.PortofoliooDAO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class Portofolio extends JFrame {

  private JPanel portofolioPanel;
  private JPanel contentMainPanel;
  
  // ID Pengguna
  private int userId; 

  // Database access
  private PortofoliooDAO portofolioDAO;
  private List<Portofolioo> portofolioList;
  
  // Semua jenis portofolio yang tersedia
  private final String[] JENIS_PORTOFOLIO = {
      "Sertifikat Keahlian", "Sertifikat Pelatihan", "Sertifikat Kursus Online", 
      "Sertifikat Workshop", "Sertifikat Seminar", "Sertifikat Webinar",
      "Sertifikat Profesi", "Sertifikat Kompetensi", "Juara Kompetisi", 
      "Penghargaan Akademik", "Beasiswa", "Penghargaan Prestasi",
      "Recognition Award", "Achievement Award", "Excellence Award", 
      "Leadership Award", "Proyek", "Aplikasi", "Website", "Karya Tulis",
      "Penelitian", "Publikasi", "Patent", "Inovasi"
  };
  
  // Warna untuk setiap jenis
  private final Map<String, Color> jenisColors;
  
  // Constructor yang menerima userId (digunakan oleh MahasiswaNavigation)
  public Portofolio(int currentUserId) {
      this.userId = currentUserId; // Simpan ID pengguna
      
      jenisColors = new HashMap<>();
      initializeJenisColors();
      
      try {
          // Initialize DAO and data
          portofolioDAO = new PortofoliooDAO();
          portofolioList = new ArrayList<>();
          
          initializeUI();
          loadPortofolioData(); // Muat data untuk pengguna spesifik
      } catch (Exception e) {
          e.printStackTrace();
          JOptionPane.showMessageDialog(null, "Error initializing Portofolio: " + e.getMessage());
      }
  }

  // Default constructor (jika ada bagian lain yang masih memanggil tanpa parameter)
  // Ini bisa dihapus jika semua pemanggilan Portofolio sudah menggunakan parameter userId
  public Portofolio() {
      // Default ke userId 1 jika dipanggil tanpa parameter
      this(1); 
  }
  
  private void initializeJenisColors() {
      // Warna untuk berbagai jenis portofolio
      jenisColors.put("Sertifikat Keahlian", new Color(59, 130, 246));      // Blue
      jenisColors.put("Sertifikat Pelatihan", new Color(16, 185, 129));     // Green
      jenisColors.put("Sertifikat Kursus Online", new Color(245, 158, 11)); // Yellow
      jenisColors.put("Sertifikat Workshop", new Color(139, 92, 246));      // Purple
      jenisColors.put("Sertifikat Seminar", new Color(236, 72, 153));       // Pink
      jenisColors.put("Sertifikat Webinar", new Color(14, 165, 233));       // Sky
      jenisColors.put("Sertifikat Profesi", new Color(34, 197, 94));        // Emerald
      jenisColors.put("Sertifikat Kompetensi", new Color(249, 115, 22));    // Orange
      
      jenisColors.put("Juara Kompetisi", new Color(220, 38, 127));          // Rose
      jenisColors.put("Penghargaan Akademik", new Color(147, 51, 234));     // Violet
      jenisColors.put("Beasiswa", new Color(5, 150, 105));                  // Teal
      jenisColors.put("Penghargaan Prestasi", new Color(217, 119, 6));      // Amber
      jenisColors.put("Recognition Award", new Color(99, 102, 241));        // Indigo
      jenisColors.put("Achievement Award", new Color(239, 68, 68));         // Red
      jenisColors.put("Excellence Award", new Color(6, 182, 212));          // Cyan
      jenisColors.put("Leadership Award", new Color(132, 204, 22));         // Lime
      
      jenisColors.put("Proyek", new Color(168, 85, 247));                   // Purple
      jenisColors.put("Aplikasi", new Color(34, 197, 94));                  // Green
      jenisColors.put("Website", new Color(59, 130, 246));                  // Blue
      jenisColors.put("Karya Tulis", new Color(245, 158, 11));              // Yellow
      jenisColors.put("Penelitian", new Color(139, 92, 246));               // Purple
      jenisColors.put("Publikasi", new Color(236, 72, 153));                // Pink
      jenisColors.put("Patent", new Color(220, 38, 127));                   // Rose
      jenisColors.put("Inovasi", new Color(14, 165, 233));                  // Sky
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
      JPanel githubPanel = createGithubProfilePanel();
      mainContainer.add(githubPanel);
      
      // Spacing
      mainContainer.add(Box.createRigidArea(new Dimension(0, 20)));

      // Portofolio Panel
      JPanel portofolioMainPanel = createPortofolioPanel();
      mainContainer.add(portofolioMainPanel);

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

  private void loadPortofolioData() {
      try {
          // Memanggil metode untuk mengambil data portofolio berdasarkan userId dari instance ini
          portofolioList = portofolioDAO.getAllPortofolioByUserId(this.userId);
          displayPortofolio();
      } catch (Exception e) {
          System.err.println("Error loading portfolio data: " + e.getMessage());
          JOptionPane.showMessageDialog(this, 
              "Error saat memuat data portofolio: " + e.getMessage(), 
              "Error", JOptionPane.ERROR_MESSAGE);
      }
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
      JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
      buttonPanel.setOpaque(false);
      
      JButton tambahPortofolioBtn = createTambahButton("+ Tambah Portofolio");
      buttonPanel.add(tambahPortofolioBtn);

      portfolioHeaderPanel.add(portfolioInfoMainPanel, BorderLayout.WEST);
      portfolioHeaderPanel.add(buttonPanel, BorderLayout.EAST);

      return portfolioHeaderPanel;
  }

  private JButton createTambahButton(String text) {
      JButton btn = new JButton(text);
      btn.setFocusPainted(false);
      btn.setFont(new Font("SansSerif", Font.BOLD, 14));
      btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
      
      btn.setBackground(new Color(30, 58, 138));
      btn.setForeground(Color.WHITE);
      btn.setBorder(new EmptyBorder(10, 20, 10, 20));
      btn.setOpaque(true);
      btn.setBorderPainted(false);
      
      // Hover effect
      btn.addMouseListener(new java.awt.event.MouseAdapter() {
          public void mouseEntered(java.awt.event.MouseEvent evt) {
              btn.setBackground(new Color(23, 37, 84));
          }
          public void mouseExited(java.awt.event.MouseEvent evt) {
              btn.setBackground(new Color(30, 58, 138));
          }
      });
      
      // Action listener
      btn.addActionListener(e -> showTambahDialog());
      
      return btn;
  }
  
  private void showTambahDialog() {
      showPortfolioDialog(null); // null means add new
  }
  
  private void showEditDialog(Portofolioo portfolio) {
      showPortfolioDialog(portfolio); // existing portfolio means edit
  }
  
  private void showPortfolioDialog(Portofolioo existingPortfolio) {
      boolean isEdit = (existingPortfolio != null);
      String dialogTitle = isEdit ? "Edit Portofolio" : "Tambah Portofolio";
      
      JDialog dialog = new JDialog(this, dialogTitle, true);
      dialog.setSize(500, 500);
      dialog.setLocationRelativeTo(this);
      
      JPanel mainPanel = new JPanel(new BorderLayout());
      mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
      
      // Form panel
      JPanel formPanel = new JPanel();
      formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
      
      // Jenis dropdown
      JLabel jenisLabel = new JLabel("Jenis:");
      jenisLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
      jenisLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
      
      JComboBox<String> jenisCombo = new JComboBox<>(JENIS_PORTOFOLIO);
      jenisCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
      jenisCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
      
      // Judul field
      JLabel judulLabel = new JLabel("Judul:");
      judulLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
      judulLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
      
      JTextField judulField = new JTextField();
      judulField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
      judulField.setAlignmentX(Component.LEFT_ALIGNMENT);
      
      // Link field
      JLabel linkLabel = new JLabel("Link (Opsional):");
      linkLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
      linkLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
      
      JTextField linkField = new JTextField();
      linkField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
      linkField.setAlignmentX(Component.LEFT_ALIGNMENT);
      
      // Deskripsi field
      JLabel deskripsiLabel = new JLabel("Deskripsi:");
      deskripsiLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
      deskripsiLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
      
      JTextArea deskripsiArea = new JTextArea(5, 30);
      deskripsiArea.setLineWrap(true);
      deskripsiArea.setWrapStyleWord(true);
      JScrollPane deskripsiScroll = new JScrollPane(deskripsiArea);
      deskripsiScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
      deskripsiScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
      
      // Fill fields if editing
      if (isEdit) {
          jenisCombo.setSelectedItem(existingPortfolio.getJenis());
          judulField.setText(existingPortfolio.getJudul());
          linkField.setText(existingPortfolio.getLink() != null ? existingPortfolio.getLink() : "");
          deskripsiArea.setText(existingPortfolio.getDeskripsi());
      }
      
      // Add components to form
      formPanel.add(jenisLabel);
      formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
      formPanel.add(jenisCombo);
      formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
      
      formPanel.add(judulLabel);
      formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
      formPanel.add(judulField);
      formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
      
      formPanel.add(linkLabel);
      formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
      formPanel.add(linkField);
      formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
      
      formPanel.add(deskripsiLabel);
      formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
      formPanel.add(deskripsiScroll);
      
      // Button panel
      JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
      
      JButton cancelBtn = new JButton("Batal");
      cancelBtn.addActionListener(e -> dialog.dispose());
      
      JButton saveBtn = new JButton(isEdit ? "Update" : "Simpan");
      saveBtn.addActionListener(e -> {
          String jenis = (String) jenisCombo.getSelectedItem();
          String judul = judulField.getText().trim();
          String link = linkField.getText().trim();
          String deskripsi = deskripsiArea.getText().trim();
          
          if (judul.isEmpty() || deskripsi.isEmpty()) {
              JOptionPane.showMessageDialog(dialog, "Mohon lengkapi judul dan deskripsi!", "Error", JOptionPane.ERROR_MESSAGE);
              return;
          }
          
          try {
              boolean success;
              if (isEdit) {
                  // Update existing portfolio
                  existingPortfolio.setJenis(jenis);
                  existingPortfolio.setJudul(judul);
                  existingPortfolio.setLink(link.isEmpty() ? null : link);
                  existingPortfolio.setDeskripsi(deskripsi);
                  
                  success = portofolioDAO.updatePortofolio(existingPortfolio);
                  if (success) {
                      dialog.dispose();
                      JOptionPane.showMessageDialog(this, 
                          "Portofolio berhasil diupdate!", 
                          "Sukses", JOptionPane.INFORMATION_MESSAGE);
                      loadPortofolioData(); // Refresh data
                  } else {
                      JOptionPane.showMessageDialog(dialog, 
                          "Gagal mengupdate portofolio!", 
                          "Error", JOptionPane.ERROR_MESSAGE);
                  }
              } else {
                  // Create new portfolio item
                  Portofolioo portfolio = new Portofolioo();
                  portfolio.setUserId(this.userId); // Set userId dari instance Portofolio ini
                  portfolio.setJenis(jenis);
                  portfolio.setJudul(judul);
                  portfolio.setLink(link.isEmpty() ? null : link);
                  portfolio.setDeskripsi(deskripsi);
                  
                  success = portofolioDAO.insertPortofolio(portfolio);
                  if (success) {
                      dialog.dispose();
                      JOptionPane.showMessageDialog(this, 
                          "Portofolio berhasil ditambahkan!", 
                          "Sukses", JOptionPane.INFORMATION_MESSAGE);
                      loadPortofolioData(); // Refresh data
                  } else {
                      JOptionPane.showMessageDialog(dialog, 
                          "Gagal menambahkan portofolio!", 
                          "Error", JOptionPane.ERROR_MESSAGE);
                  }
              }
          } catch (Exception ex) {
              JOptionPane.showMessageDialog(dialog, 
                  "Error: " + ex.getMessage(), 
                  "Error", JOptionPane.ERROR_MESSAGE);
          }
      });
      
      buttonPanel.add(cancelBtn);
      buttonPanel.add(saveBtn);
      
      mainPanel.add(formPanel, BorderLayout.CENTER);
      mainPanel.add(buttonPanel, BorderLayout.SOUTH);
      
      dialog.add(mainPanel);
      dialog.setVisible(true);
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
      githubUrlLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR)); // Make link clickable
      
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

  private JPanel createPortofolioPanel() {
      // Panel putih dengan rounded corners
      RoundedPanel mainPanel = new RoundedPanel(12, Color.WHITE);
      mainPanel.setLayout(new BorderLayout());
      mainPanel.setBorder(new EmptyBorder(25, 30, 30, 30));

      // Title
      JLabel titleLabel = new JLabel("Koleksi Portofolio");
      titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
      titleLabel.setForeground(new Color(31, 41, 55));
      titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
      
      mainPanel.add(titleLabel, BorderLayout.NORTH);

      // Initialize panel
      portofolioPanel = new JPanel();
      portofolioPanel.setLayout(new BoxLayout(portofolioPanel, BoxLayout.Y_AXIS));
      portofolioPanel.setOpaque(false);

      mainPanel.add(portofolioPanel, BorderLayout.CENTER);
      
      // Wrapper panel untuk shadow effect
      JPanel wrapperPanel = new JPanel(new BorderLayout());
      wrapperPanel.setBackground(new Color(243, 244, 246));
      wrapperPanel.setBorder(new EmptyBorder(2, 2, 8, 2));
      wrapperPanel.add(mainPanel, BorderLayout.CENTER);
      
      return wrapperPanel;
  }

  private void displayPortofolio() {
      portofolioPanel.removeAll();
      
      if (portofolioList.isEmpty()) {
          // Empty state
          JPanel emptyPanel = new JPanel();
          emptyPanel.setLayout(new BoxLayout(emptyPanel, BoxLayout.Y_AXIS));
          emptyPanel.setOpaque(false);
          emptyPanel.setBorder(new EmptyBorder(40, 0, 40, 0));
          
          JLabel emptyLabel = new JLabel("Belum ada portofolio");
          emptyLabel.setFont(new Font("SansSerif", Font.ITALIC, 16));
          emptyLabel.setForeground(new Color(156, 163, 175));
          emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
          
          JLabel emptyDescLabel = new JLabel("Mulai tambahkan karya terbaik Anda");
          emptyDescLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
          emptyDescLabel.setForeground(new Color(156, 163, 175));
          emptyDescLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
          
          emptyPanel.add(emptyLabel);
          emptyPanel.add(Box.createRigidArea(new Dimension(0, 5)));
          emptyPanel.add(emptyDescLabel);
          
          portofolioPanel.add(emptyPanel);
      } else {
          // Display portfolio items
          for (int i = 0; i < portofolioList.size(); i++) {
              Portofolioo item = portofolioList.get(i);
              JPanel itemPanel = createPortfolioItemPanel(item);
              portofolioPanel.add(itemPanel);
              
              // Add spacing between items (except for the last item)
              if (i < portofolioList.size() - 1) {
                  portofolioPanel.add(Box.createRigidArea(new Dimension(0, 15)));
              }
          }
      }
      
      portofolioPanel.revalidate();
      portofolioPanel.repaint();
  }

  private JPanel createPortfolioItemPanel(Portofolioo item) {
    // Main item panel dengan border
    JPanel itemPanel = new JPanel(new BorderLayout());
    itemPanel.setBackground(Color.WHITE);
    itemPanel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
        new EmptyBorder(20, 20, 20, 20)
    ));
    
    // Header panel
    JPanel headerPanel = new JPanel(new BorderLayout());
    headerPanel.setOpaque(false);
    
    // Left side - Jenis badge
    Color jenisColor = jenisColors.getOrDefault(item.getJenis(), new Color(107, 114, 128));
    JPanel jenisWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    jenisWrapper.setOpaque(false);
    
    JLabel jenisLabel = new JLabel(item.getJenis());
    jenisLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
    jenisLabel.setForeground(Color.WHITE);
    jenisLabel.setBackground(jenisColor);
    jenisLabel.setOpaque(true);
    jenisLabel.setBorder(new EmptyBorder(4, 8, 4, 8));
    
    jenisWrapper.add(jenisLabel);
    
    // Right side - Action buttons
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
    buttonPanel.setOpaque(false);
    
    // Edit button
    JButton editBtn = new JButton("Edit");
    editBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
    editBtn.setForeground(Color.WHITE);
    editBtn.setBackground(new Color(59, 130, 246)); 
    editBtn.setPreferredSize(new Dimension(60, 28));
    editBtn.setFocusPainted(false);
    editBtn.setBorderPainted(false);
    editBtn.setOpaque(true);
    editBtn.setContentAreaFilled(true);
    
    editBtn.addActionListener(e -> showEditDialog(item));
    
    editBtn.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            editBtn.setBackground(new Color(37, 99, 235)); 
        }
        public void mouseExited(java.awt.event.MouseEvent evt) {
            editBtn.setBackground(new Color(59, 130, 246)); 
        }
    });
    
    // Delete button
    JButton deleteBtn = new JButton("Hapus");
    deleteBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
    deleteBtn.setForeground(Color.WHITE);
    deleteBtn.setBackground(new Color(239, 68, 68)); 
    deleteBtn.setPreferredSize(new Dimension(90, 28));
    deleteBtn.setFocusPainted(false);
    deleteBtn.setBorderPainted(false);
    deleteBtn.setOpaque(true); 
    deleteBtn.setContentAreaFilled(true); 
    
    deleteBtn.addActionListener(e -> {
        int result = JOptionPane.showConfirmDialog(
            this,
            "Apakah Anda yakin ingin menghapus portofolio ini?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (result == JOptionPane.YES_OPTION) {
            try {
                boolean success = portofolioDAO.deletePortofolio(item.getId());
                if (success) {
                    JOptionPane.showMessageDialog(this, 
                        "Portofolio berhasil dihapus!", 
                        "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    loadPortofolioData(); 
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Gagal menghapus portofolio!", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Error: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    });
    
    deleteBtn.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            deleteBtn.setBackground(new Color(220, 38, 38)); 
        }
        public void mouseExited(java.awt.event.MouseEvent evt) {
            deleteBtn.setBackground(new Color(239, 68, 68));
        }
    });
    
    buttonPanel.add(editBtn);
    buttonPanel.add(deleteBtn);
    
    headerPanel.add(jenisWrapper, BorderLayout.WEST);
    headerPanel.add(buttonPanel, BorderLayout.EAST);
    
    // Content panel
    JPanel contentPanel = new JPanel();
    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
    contentPanel.setOpaque(false);
    contentPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
    
    // Title
    JLabel titleLabel = new JLabel(item.getJudul());
    titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
    titleLabel.setForeground(new Color(31, 41, 55));
    titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    
    // Description
    JTextArea descArea = new JTextArea(item.getDeskripsi());
    descArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
    descArea.setForeground(new Color(75, 85, 99));
    descArea.setBackground(Color.WHITE);
    descArea.setLineWrap(true);
    descArea.setWrapStyleWord(true);
    descArea.setEditable(false);
    descArea.setBorder(BorderFactory.createEmptyBorder());
    descArea.setAlignmentX(Component.LEFT_ALIGNMENT);
    
    contentPanel.add(titleLabel);
    contentPanel.add(Box.createRigidArea(new Dimension(0, 8)));
    contentPanel.add(descArea);
    
    // Link (if available)
    if (item.getLink() != null && !item.getLink().trim().isEmpty()) {
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JLabel linkLabel = new JLabel("🔗 " + item.getLink());
        linkLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        linkLabel.setForeground(new Color(59, 130, 246));
        linkLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        linkLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        
        contentPanel.add(linkLabel);
    }
    
    itemPanel.add(headerPanel, BorderLayout.NORTH);
    itemPanel.add(contentPanel, BorderLayout.CENTER);
    
    return itemPanel;
  }

  public static void main(String[] args) {
      SwingUtilities.invokeLater(() -> {
          try {
               javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
          } catch (Exception e) {
              e.printStackTrace();
          }
          
          // Ganti dengan ID pengguna yang valid untuk pengujian
          int testUserId = 1; 
          new Portofolio(testUserId).setVisible(true);
      });
  }
}