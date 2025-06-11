package Mahasiswa;

import Database.ProyekDAO;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;

public class TambahProyekFrame extends JFrame {

    private final int userId;
    private final ProyekDAO proyekDAO;
    
    // Komponen UI
    private JTextField judulField;
    private JTextArea deskripsiArea;
    private JTextField bidangField;

    /**
     * Konstruktor untuk membuat frame Tambah Proyek.
     * @param userId ID dari pengguna yang sedang login, untuk dihubungkan dengan proyek.
     */
    public TambahProyekFrame(int userId) {
        this.userId = userId;
        this.proyekDAO = new ProyekDAO();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Tambah Proyek");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Tutup frame ini saja, bukan seluruh aplikasi
        
        // Panel utama dengan MigLayout untuk fleksibilitas
        JPanel mainPanel = new JPanel(new MigLayout("wrap 2, insets 25", "[right][grow, fill]"));
        mainPanel.setBackground(Color.WHITE);

        // Judul Utama
        JLabel titleLabel = new JLabel("Tambah Proyek Baru");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setForeground(new Color(30, 41, 59)); // Warna biru tua
        mainPanel.add(titleLabel, "span 2, center, gapbottom 20, wrap");

        // Form Judul Proyek
        mainPanel.add(new JLabel("Judul Proyek:"));
        judulField = new JTextField();
        mainPanel.add(judulField, "wrap");

        // Form Deskripsi
        mainPanel.add(new JLabel("Deskripsi:"), "aligny top");
        deskripsiArea = new JTextArea(5, 20);
        deskripsiArea.setLineWrap(true);
        deskripsiArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(deskripsiArea);
        mainPanel.add(scrollPane, "grow, wrap");
        
        // Form Bidang
        mainPanel.add(new JLabel("Bidang:"));
        bidangField = new JTextField();
        mainPanel.add(bidangField, "wrap, gaptop 5");
        
        // --- Tombol ---
        JButton batalButton = new JButton("Batal");
        styleButton(batalButton, new Color(108, 117, 125), Color.WHITE); // Abu-abu

        JButton simpanButton = new JButton("Simpan Proyek");
        styleButton(simpanButton, new Color(13, 110, 253), Color.WHITE); // Biru

        // Panel untuk menampung tombol
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(batalButton);
        buttonPanel.add(simpanButton);
        mainPanel.add(buttonPanel, "span 2, growx, gaptop 20");

        // --- Action Listeners ---
        batalButton.addActionListener(e -> dispose()); // Menutup jendela
        simpanButton.addActionListener(e -> simpanProyek());

        add(mainPanel);
        pack(); // Mengatur ukuran frame agar pas dengan komponen
        setResizable(false);
        setLocationRelativeTo(null); // Menampilkan frame di tengah layar
    }
    
    // Metode untuk memberikan style pada tombol
    private void styleButton(JButton button, Color background, Color foreground) {
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(8, 20, 8, 20));
        button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }
    
    // Metode untuk menyimpan data proyek
    private void simpanProyek() {
        String judul = judulField.getText();
        String deskripsi = deskripsiArea.getText();
        String bidang = bidangField.getText();

        // Validasi input sederhana
        if (judul.trim().isEmpty() || deskripsi.trim().isEmpty() || bidang.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Panggil DAO untuk menyimpan data
        boolean sukses = proyekDAO.tambahProyek(userId, judul, deskripsi, bidang);

        if (sukses) {
            JOptionPane.showMessageDialog(this, "Proyek berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Tutup jendela setelah berhasil
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menambahkan proyek ke database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Metode main untuk testing (opsional)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Gunakan Look and Feel sistem untuk tampilan yang lebih modern
                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            // Contoh pemanggilan dengan user_id = 1
            new TambahProyekFrame(1).setVisible(true);
        });
    }
}