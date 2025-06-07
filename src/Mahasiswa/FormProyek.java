package Mahasiswa;

import javax.swing.*;

import Database.koneksi;

import java.awt.*;
import net.miginfocom.swing.MigLayout;

public class FormProyek extends JFrame {

    public FormProyek() {
        setTitle("Tambah Proyek");
        setSize(500, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new MigLayout("wrap 2", "[right][grow,fill]", "[]15[]15[]15[]25[]"));

        JTextField judulField = new JTextField();
        judulField.setPreferredSize(new Dimension(0, 35));

        JTextArea deskripsiArea = new JTextArea(5, 25);
        deskripsiArea.setLineWrap(true);
        deskripsiArea.setWrapStyleWord(true);
        JScrollPane scrollDeskripsi = new JScrollPane(deskripsiArea);
        scrollDeskripsi.setPreferredSize(new Dimension(0, 100));

        JTextField bidangField = new JTextField();
        bidangField.setPreferredSize(new Dimension(0, 35));

        JButton simpanButton = new JButton("💾 Simpan");

        panel.add(new JLabel("Judul Proyek:"));
        panel.add(judulField);

        panel.add(new JLabel("Deskripsi:"));
        panel.add(scrollDeskripsi);

        panel.add(new JLabel("Bidang:"));
        panel.add(bidangField);

        panel.add(simpanButton, "span, center, gaptop 15");

        simpanButton.addActionListener(_ -> {
            String judul = judulField.getText().trim();
            String deskripsi = deskripsiArea.getText().trim();
            String bidang = bidangField.getText().trim();

            koneksi kon = new koneksi();
            kon.simpanProyek(judul, deskripsi, bidang);

            JOptionPane.showMessageDialog(this,
                "✅ Proyek Disimpan:\n\nJudul: " + judul +
                "\nDeskripsi: " + deskripsi +
                "\nBidang: " + bidang,
                "Sukses", JOptionPane.INFORMATION_MESSAGE);

            this.dispose();
        });

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FormProyek().setVisible(true);
        });
    }
}
