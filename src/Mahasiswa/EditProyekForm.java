package Mahasiswa;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import Components.ModernButton;
import Database.ProyekDAO;
import Models.Proyek;
import net.miginfocom.swing.MigLayout;

public class EditProyekForm extends JFrame {
    private Proyek proyek;

    public EditProyekForm(Proyek proyek) {
        this.proyek = proyek;
        initializeUI();
    }

    public void initializeUI() {
        setTitle("Edit Proyek");
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

        // Prefill fields with existing proyek data
        judulField.setText(proyek.judul);
        deskripsiArea.setText(proyek.deskripsi);
        bidangField.setText(proyek.bidang);

        ModernButton simpanButton = new ModernButton("Simpan Perubahan", new Color(13, 110, 253), Color.WHITE, new Color(11, 94, 215));
        simpanButton.setPreferredSize(new Dimension(150, 40));

        ModernButton cancelButton = new ModernButton("Batal", new Color(220, 53, 69), Color.WHITE, new Color(200, 35, 51));
        cancelButton.setPreferredSize(new Dimension(150, 40));
        cancelButton.addActionListener(_ -> this.dispose());

        JLabel titleLabel = new JLabel("Edit Proyek");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(37, 64, 143));

        panel.add(titleLabel, "span, center, gapbottom 10");

        panel.add(new JLabel("Judul Proyek:"), "gapbottom 2");
        panel.add(judulField);

        panel.add(new JLabel("Deskripsi:"), "gapbottom 2");
        panel.add(scrollDeskripsi);

        panel.add(new JLabel("Bidang:"), "gapbottom 2");
        panel.add(bidangField);

        JPanel buttonPanel = new JPanel(new MigLayout("", "[grow][push]", "[50]"));
        buttonPanel.setOpaque(false);
        buttonPanel.add(cancelButton);
        buttonPanel.add(simpanButton);
        panel.add(buttonPanel, "span, growx");

        simpanButton.addActionListener(_ -> {
            String judul = judulField.getText().trim();
            String deskripsi = deskripsiArea.getText().trim();
            String bidang = bidangField.getText().trim();

            if (judul.isEmpty() || deskripsi.isEmpty() || bidang.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua field harus diisi.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                ProyekDAO proyekDAO = new ProyekDAO();
                proyekDAO.updateProyek(proyek.proyekId, judul, deskripsi, bidang);

                JOptionPane.showMessageDialog(this,
                    "✅ Proyek berhasil diperbarui:\n\nJudul: " + judul +
                    "\nDeskripsi: " + deskripsi +
                    "\nBidang: " + bidang,
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);

                for (Window window : Window.getWindows()) {
                    if (window != this && window instanceof JFrame && window.isDisplayable()) {
                        window.dispose();
                    }
                }

                new ProyekSaya().setVisible(true);
                this.dispose();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Gagal memperbarui proyek: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        setContentPane(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {});
    }
}
