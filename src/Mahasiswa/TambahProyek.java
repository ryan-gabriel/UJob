package Mahasiswa;

import Components.MahasiswaNavigation;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class TambahProyek extends JFrame {

  private JTextField judulProyekField;
  private JTextField bidangField;
  private JTextField batasPendaftaranField;
  private JTextField keahlianField;
  private JTextArea deskripsiArea;

  public TambahProyek() {
      initializeUI();
  }

  private void initializeUI() {
      setTitle("Buat Proyek Baru - UPI Job");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setSize(1000, 800);
      setMinimumSize(new Dimension(900, 700));
      setLocationRelativeTo(null);
      setExtendedState(JFrame.MAXIMIZED_BOTH); // Start maximized

      JPanel mainPanel = new JPanel(new BorderLayout());
      mainPanel.setBackground(new Color(243, 244, 246));

      // Navigation Header
      MahasiswaNavigation headerPanel = new MahasiswaNavigation("Proyek");
      mainPanel.add(headerPanel, BorderLayout.NORTH);

      // Main Content Container - Responsive
      JPanel outerContainer = new JPanel(new BorderLayout());
      outerContainer.setBackground(new Color(243, 244, 246));

      JPanel mainContainer = new JPanel();
      mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));
      mainContainer.setBackground(new Color(243, 244, 246));

      // PADDING ATAS UNTUK HEADER - Jarak dari navbar
      mainContainer.add(Box.createRigidArea(new Dimension(0, 40))); // Tambahan spacing dari navbar

      // Header Panel dengan gradient - Full width
      JPanel headerProyek = createHeaderPanel();
      mainContainer.add(headerProyek);
      
      // Spacing antara header dan form
      mainContainer.add(Box.createRigidArea(new Dimension(0, 40)));
      
      // Form Panel - Responsive
      JPanel formPanel = createFormPanel();
      mainContainer.add(formPanel);

      // Add some bottom spacing
      mainContainer.add(Box.createRigidArea(new Dimension(0, 50)));

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
          g2d.fillRoundRect(0, 0, width, height, 20, 20);
      }
  }

  // Custom Button Class dengan styling modern/flat
  class ModernButton extends JButton {
      private Color bgColor;
      private Color hoverColor;
      private Color originalColor;
      
      public ModernButton(String text, Color backgroundColor, Color hoverColor) {
          super(text);
          this.bgColor = backgroundColor;
          this.originalColor = backgroundColor;
          this.hoverColor = hoverColor;
          
          setOpaque(false);
          setBorderPainted(false);
          setFocusPainted(false);
          setContentAreaFilled(false);
          setCursor(new Cursor(Cursor.HAND_CURSOR));
          
          addMouseListener(new MouseAdapter() {
              @Override
              public void mouseEntered(MouseEvent e) {
                  ModernButton.this.bgColor = hoverColor;
                  repaint();
              }
              
              @Override
              public void mouseExited(MouseEvent e) {
                  ModernButton.this.bgColor = originalColor;
                  repaint();
              }
              
              @Override
              public void mousePressed(MouseEvent e) {
                  // Efek pressed - warna sedikit lebih gelap
                  ModernButton.this.bgColor = hoverColor.darker();
                  repaint();
              }
              
              @Override
              public void mouseReleased(MouseEvent e) {
                  ModernButton.this.bgColor = hoverColor;
                  repaint();
              }
          });
      }
      
      @Override
      protected void paintComponent(Graphics g) {
          Graphics2D g2 = (Graphics2D) g.create();
          g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
          
          // Fill background dengan corner radius kecil seperti gambar
          g2.setColor(bgColor);
          g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4); // Corner radius kecil
          
          // Draw text dengan posisi center
          g2.setColor(getForeground());
          g2.setFont(getFont());
          FontMetrics fm = g2.getFontMetrics();
          int x = (getWidth() - fm.stringWidth(getText())) / 2;
          int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
          g2.drawString(getText(), x, y);
          
          g2.dispose();
      }
  }

  private JPanel createHeaderPanel() {
      // Container untuk header dengan margin yang lebih besar
      JPanel headerContainer = new JPanel(new BorderLayout());
      headerContainer.setBackground(new Color(243, 244, 246));
      headerContainer.setBorder(new EmptyBorder(0, 30, 0, 30)); // Side margins lebih besar

      // Header dengan gradient ungu - Full width responsive
      GradientPanel headerPanel = new GradientPanel(
          new Color(123, 104, 238),  // Medium Slate Blue
          new Color(147, 51, 234)    // Purple
      );
      headerPanel.setLayout(new BorderLayout());
      // PADDING INTERNAL YANG LEBIH BESAR - mirip dengan UI yang ditunjukkan
      headerPanel.setBorder(new EmptyBorder(60, 80, 60, 80)); // Padding internal lebih besar
      headerPanel.setPreferredSize(new Dimension(0, 180)); // Height lebih besar

      // Content panel
      JPanel contentPanel = new JPanel();
      contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
      contentPanel.setOpaque(false);

      // Title - Font lebih besar dan bold
      JLabel titleLabel = new JLabel("Buat Proyek Baru");
      titleLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
      titleLabel.setForeground(Color.WHITE);
      titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

      // Subtitle - Font lebih besar
      JLabel subtitleLabel = new JLabel("Bagikan ide Proyek Anda dan Temukan Kolaborator yang tepat");
      subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
      subtitleLabel.setForeground(new Color(220, 220, 255));
      subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

      contentPanel.add(titleLabel);
      contentPanel.add(Box.createRigidArea(new Dimension(0, 15))); // Spacing lebih besar
      contentPanel.add(subtitleLabel);

      headerPanel.add(contentPanel, BorderLayout.CENTER);
      headerContainer.add(headerPanel, BorderLayout.CENTER);
      
      return headerContainer;
  }

  private JPanel createFormPanel() {
      // Container untuk form dengan margin responsif
      JPanel formContainer = new JPanel(new BorderLayout());
      formContainer.setBackground(new Color(243, 244, 246));
      
      // Responsive margins - akan menyesuaikan dengan lebar layar
      JPanel marginPanel = new JPanel(new BorderLayout());
      marginPanel.setBackground(new Color(243, 244, 246));
      marginPanel.setBorder(new EmptyBorder(0, 30, 0, 30)); // Margin sama dengan header

      // Panel putih dengan background - Full width
      JPanel formMainPanel = new JPanel(new BorderLayout());
      formMainPanel.setBackground(Color.WHITE);
      formMainPanel.setBorder(new EmptyBorder(60, 80, 60, 80)); // Internal padding sama dengan header

      // Form content dengan layout yang lebih fleksibel
      JPanel formContent = new JPanel();
      formContent.setLayout(new BoxLayout(formContent, BoxLayout.Y_AXIS));
      formContent.setBackground(Color.WHITE);

      // Create all form fields dengan spacing yang konsisten
      formContent.add(createFieldGroup("Judul Proyek", 
          judulProyekField = createTextField("Masukkan Judul Proyek Anda ....")));
      
      formContent.add(createFieldGroup("Bidang", 
          bidangField = createTextField("Masukkan Bidang Proyek Anda ....")));
      
      formContent.add(createFieldGroup("Batas Pendaftaraan", 
          batasPendaftaranField = createTextField("Masukkan Batas Pendaftaraan Proyek Anda ....")));
      
      formContent.add(createFieldGroup("Keahlian yang dibutuhkan", 
          keahlianField = createTextField("Masukkan Bidang Proyek Anda ...")));

      // Deskripsi field (TextArea) dengan styling yang sama
      JPanel deskripsiGroup = new JPanel();
      deskripsiGroup.setLayout(new BoxLayout(deskripsiGroup, BoxLayout.Y_AXIS));
      deskripsiGroup.setBackground(Color.WHITE);
      deskripsiGroup.setAlignmentX(Component.LEFT_ALIGNMENT);
      deskripsiGroup.setBorder(new EmptyBorder(0, 0, 35, 0)); // Spacing lebih besar

      JLabel deskripsiLabel = createFieldLabel("Deskripsi");
      deskripsiArea = createTextArea("Masukkan Bidang Proyek Anda .....", 5);
      
      JScrollPane deskripsiScroll = new JScrollPane(deskripsiArea);
      deskripsiScroll.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1));
      deskripsiScroll.setAlignmentX(Component.LEFT_ALIGNMENT);

      deskripsiGroup.add(deskripsiLabel);
      deskripsiGroup.add(Box.createRigidArea(new Dimension(0, 10))); // Spacing konsisten
      deskripsiGroup.add(deskripsiScroll);

      formContent.add(deskripsiGroup);

      // Buttons Panel dengan spacing yang lebih besar
      JPanel buttonPanel = createButtonPanel();
      buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
      formContent.add(buttonPanel);

      formMainPanel.add(formContent, BorderLayout.CENTER);
      marginPanel.add(formMainPanel, BorderLayout.CENTER);
      formContainer.add(marginPanel, BorderLayout.CENTER);
      
      return formContainer;
  }

  private JPanel createFieldGroup(String labelText, JTextField field) {
      JPanel fieldGroup = new JPanel();
      fieldGroup.setLayout(new BoxLayout(fieldGroup, BoxLayout.Y_AXIS));
      fieldGroup.setBackground(Color.WHITE);
      fieldGroup.setAlignmentX(Component.LEFT_ALIGNMENT);
      fieldGroup.setBorder(new EmptyBorder(0, 0, 30, 0)); // Spacing lebih besar dan konsisten

      JLabel label = createFieldLabel(labelText);
      field.setAlignmentX(Component.LEFT_ALIGNMENT);

      fieldGroup.add(label);
      fieldGroup.add(Box.createRigidArea(new Dimension(0, 10))); // Spacing konsisten
      fieldGroup.add(field);

      return fieldGroup;
  }

  private JLabel createFieldLabel(String text) {
      JLabel label = new JLabel(text);
      label.setFont(new Font("SansSerif", Font.BOLD, 16)); // Font lebih besar
      label.setForeground(new Color(31, 41, 55));
      label.setAlignmentX(Component.LEFT_ALIGNMENT);
      return label;
  }

  private JTextField createTextField(String placeholder) {
      JTextField field = new JTextField();
      field.setFont(new Font("SansSerif", Font.PLAIN, 15)); // Font lebih besar
      field.setBorder(BorderFactory.createCompoundBorder(
          BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
          new EmptyBorder(18, 22, 18, 22) // Padding lebih besar
      ));
      field.setPreferredSize(new Dimension(0, 55)); // Height lebih besar
      field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55)); // Allow horizontal expansion
      field.setBackground(Color.WHITE);
      
      // Placeholder effect
      field.setText(placeholder);
      field.setForeground(new Color(156, 163, 175));
      
      field.addFocusListener(new java.awt.event.FocusAdapter() {
          public void focusGained(java.awt.event.FocusEvent evt) {
              if (field.getText().equals(placeholder)) {
                  field.setText("");
                  field.setForeground(new Color(31, 41, 55));
              }
          }
          public void focusLost(java.awt.event.FocusEvent evt) {
              if (field.getText().isEmpty()) {
                  field.setText(placeholder);
                  field.setForeground(new Color(156, 163, 175));
              }
          }
      });
      
      return field;
  }

  private JTextArea createTextArea(String placeholder, int rows) {
      JTextArea area = new JTextArea(rows, 0);
      area.setFont(new Font("SansSerif", Font.PLAIN, 15)); // Font lebih besar
      area.setBorder(new EmptyBorder(18, 22, 18, 22)); // Padding sama dengan textfield
      area.setLineWrap(true);
      area.setWrapStyleWord(true);
      area.setBackground(Color.WHITE);
      
      // Placeholder effect
      area.setText(placeholder);
      area.setForeground(new Color(156, 163, 175));
      
      area.addFocusListener(new java.awt.event.FocusAdapter() {
          public void focusGained(java.awt.event.FocusEvent evt) {
              if (area.getText().equals(placeholder)) {
                  area.setText("");
                  area.setForeground(new Color(31, 41, 55));
              }
          }
          public void focusLost(java.awt.event.FocusEvent evt) {
              if (area.getText().trim().isEmpty()) {
                  area.setText(placeholder);
                  area.setForeground(new Color(156, 163, 175));
              }
          }
      });
      
      return area;
  }

  private JPanel createButtonPanel() {
      JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0)); // Spacing disesuaikan dengan gambar
      buttonPanel.setBackground(Color.WHITE);

      // Tombol Batal (Merah) - Modern flat design
      ModernButton batalButton = new ModernButton(
          "Batal", 
          new Color(220, 53, 69),   // Bootstrap danger red
          new Color(200, 35, 51)    // Darker red on hover
      );
      batalButton.setFont(new Font("SansSerif", Font.BOLD, 15));
      batalButton.setForeground(Color.WHITE);
      batalButton.setPreferredSize(new Dimension(120, 45)); // Size sesuai gambar

      // Tombol Tambah Proyek (Biru) - Modern flat design
      ModernButton tambahButton = new ModernButton(
          "Tambah Proyek", 
          new Color(13, 110, 253),  // Bootstrap primary blue
          new Color(11, 94, 215)    // Darker blue on hover
      );
      tambahButton.setFont(new Font("SansSerif", Font.BOLD, 15));
      tambahButton.setForeground(Color.WHITE);
      tambahButton.setPreferredSize(new Dimension(160, 45)); // Size sesuai gambar

      // Action listeners
      batalButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
        setVisible(false);
        }
});


      tambahButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
              tambahProyek();
          }
      });

      buttonPanel.add(batalButton);
      buttonPanel.add(tambahButton);

      return buttonPanel;
  }

  private void tambahProyek() {
      if (validateForm()) {
          javax.swing.JOptionPane.showMessageDialog(this, 
              "Proyek berhasil ditambahkan!\nProyek Anda akan ditampilkan setelah diverifikasi.",
              "Sukses", 
              javax.swing.JOptionPane.INFORMATION_MESSAGE);
          clearForm();
      }
  }

  private boolean validateForm() {
      StringBuilder errors = new StringBuilder();
      
      if (judulProyekField.getText().trim().isEmpty() || 
          judulProyekField.getForeground().equals(new Color(156, 163, 175))) {
          errors.append("- Judul proyek harus diisi\n");
      }
      
      if (bidangField.getText().trim().isEmpty() || 
          bidangField.getForeground().equals(new Color(156, 163, 175))) {
          errors.append("- Bidang proyek harus diisi\n");
      }
      
      if (batasPendaftaranField.getText().trim().isEmpty() || 
          batasPendaftaranField.getForeground().equals(new Color(156, 163, 175))) {
          errors.append("- Batas pendaftaran harus diisi\n");
      }
      
      if (keahlianField.getText().trim().isEmpty() || 
          keahlianField.getForeground().equals(new Color(156, 163, 175))) {
          errors.append("- Keahlian yang dibutuhkan harus diisi\n");
      }
      
      if (deskripsiArea.getText().trim().isEmpty() || 
          deskripsiArea.getForeground().equals(new Color(156, 163, 175))) {
          errors.append("- Deskripsi proyek harus diisi\n");
      }
      
      if (errors.length() > 0) {
          javax.swing.JOptionPane.showMessageDialog(this, 
              "Mohon lengkapi field berikut:\n\n" + errors.toString(),
              "Validasi Error", 
              javax.swing.JOptionPane.ERROR_MESSAGE);
          return false;
      }
      
      return true;
  }

  private void clearForm() {
      judulProyekField.setText("Masukkan Judul Proyek Anda ....");
      judulProyekField.setForeground(new Color(156, 163, 175));
      bidangField.setText("Masukkan Bidang Proyek Anda ....");
      bidangField.setForeground(new Color(156, 163, 175));
      batasPendaftaranField.setText("Masukkan Batas Pendaftaraan Proyek Anda ....");
      batasPendaftaranField.setForeground(new Color(156, 163, 175));
      keahlianField.setText("Masukkan Bidang Proyek Anda ...");
      keahlianField.setForeground(new Color(156, 163, 175));
      deskripsiArea.setText("Masukkan Bidang Proyek Anda .....");
      deskripsiArea.setForeground(new Color(156, 163, 175));
  }

  public static void main(String[] args) {
      SwingUtilities.invokeLater(() -> {
          try {
              javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
          } catch (Exception e) {
              e.printStackTrace();
          }
          new TambahProyek().setVisible(true);
      });
  }
}