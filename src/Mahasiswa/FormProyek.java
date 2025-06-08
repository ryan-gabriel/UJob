package Mahasiswa;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import net.miginfocom.swing.MigLayout;
import Database.ProyekDAO;

public class FormProyek extends JFrame {

    public FormProyek() {
        setTitle("Tambah Proyek");
        setSize(520, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new MigLayout("wrap 2, insets 20", "[right][grow,fill]", "[]15[]15[]15[]25[]"));
        panel.setBackground(new Color(250, 252, 255));
        panel.setBorder(new CompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1, true),
            new EmptyBorder(20, 20, 20, 20)
        ));

        Font font = new Font("Segoe UI", Font.PLAIN, 14);

        JTextField judulField = new JTextField();
        judulField.setFont(font);
        judulField.setPreferredSize(new Dimension(0, 35));
        judulField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Color.LIGHT_GRAY),
            new EmptyBorder(5, 10, 5, 10)
        ));

        JTextArea deskripsiArea = new JTextArea(5, 25);
        deskripsiArea.setFont(font);
        deskripsiArea.setLineWrap(true);
        deskripsiArea.setWrapStyleWord(true);
        JScrollPane scrollDeskripsi = new JScrollPane(deskripsiArea);
        scrollDeskripsi.setPreferredSize(new Dimension(0, 100));
        scrollDeskripsi.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JTextField bidangField = new JTextField();
        bidangField.setFont(font);
        bidangField.setPreferredSize(new Dimension(0, 35));
        bidangField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Color.LIGHT_GRAY),
            new EmptyBorder(5, 10, 5, 10)
        ));

        
        JButton simpanButton = new JButton("Simpan Proyek");
        simpanButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        simpanButton.setBackground(new Color(37, 64, 143));
        simpanButton.setForeground(Color.WHITE);
        simpanButton.setFocusPainted(false);
        simpanButton.setPreferredSize(new Dimension(160, 40));
        simpanButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel titleLabel = new JLabel("Tambah Proyek Baru");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(37, 64, 143));

        panel.add(titleLabel, "span, center, gapbottom 10");

        panel.add(new JLabel("Judul Proyek:"), "gapbottom 2");
        panel.add(judulField);

        panel.add(new JLabel("Deskripsi:"), "gapbottom 2");
        panel.add(scrollDeskripsi);

        panel.add(new JLabel("Bidang:"), "gapbottom 2");
        panel.add(bidangField);

        panel.add(simpanButton, "span, center, gaptop 15");

        simpanButton.addActionListener(_ -> {
            String judul = judulField.getText().trim();
            String deskripsi = deskripsiArea.getText().trim();
            String bidang = bidangField.getText().trim();

            if (judul.isEmpty() || deskripsi.isEmpty() || bidang.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua field harus diisi.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            ProyekDAO proyekDAO = new ProyekDAO();
            proyekDAO.simpanProyek(judul, deskripsi, bidang);

            JOptionPane.showMessageDialog(this,
                "✅ Proyek Disimpan:\n\nJudul: " + judul +
                "\nDeskripsi: " + deskripsi +
                "\nBidang: " + bidang,
                "Sukses", JOptionPane.INFORMATION_MESSAGE);

            this.dispose();
        });

        setContentPane(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FormProyek().setVisible(true));
    }
}
