package Mahasiswa;

import Components.MahasiswaNavigation;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Inbox extends JFrame {

    // Definisi Warna
    private static final Color COLOR_BACKGROUND = new Color(255, 255, 255);
    private static final Color COLOR_TEXT_PRIMARY = new Color(17, 24, 39);
    private static final Color COLOR_TEXT_SECONDARY = new Color(107, 114, 128);
    private static final Color COLOR_BORDER = new Color(229, 231, 235);

    // Data contoh statis dari sisi Mahasiswa (item "Lowongan Disukai" telah dihapus)
    private final InboxItemData[] messages = {
        new InboxItemData("Pengembang Web", "Dari PT Pactindo", "PT Pactindo mengundang Anda untuk melamar posisi Magang Pengembang Web", "21 - 05 - 2025"),
        new InboxItemData("Pengembang AI", "Dari Perusahaan ABC", "Perusahaan ABC mengundang Anda untuk melamar posisi Pengembang AI", "21 - 05 - 2025"),
        new InboxItemData("Permintaan Pertemanan", "Dari Dhafin", "Dhafin (Kampus UPI di Cibiru) ingin berteman dengan Anda", "21 - 05 - 2025"),
        new InboxItemData("Data analyst", "Dari Perusahaan Pactindo", "PT ABC mengundang Anda untuk melamar posisi Magang Pengembang Web", "21 - 05 - 2025")
    };

    public Inbox() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Inbox - UJob Mahasiswa");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(COLOR_BACKGROUND);

        mainPanel.add(new MahasiswaNavigation("Inbox"), BorderLayout.NORTH);

        // Panel konten utama yang akan berisi semua elemen di bawah navigasi.
        // Menggunakan BoxLayout agar semua komponen tersusun secara vertikal.
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(COLOR_BACKGROUND);
        contentPanel.setBorder(new EmptyBorder(40, 60, 40, 60)); // Padding (atas, kiri, bawah, kanan)
        
        // Atur agar semua komponen di dalam panel ini rata kiri.
        contentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel titleLabel = new JLabel("John Doe's Inbox");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setForeground(COLOR_TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // Pastikan label ini juga rata kiri
        contentPanel.add(titleLabel);

        // Memberi jarak antara judul dan item pesan pertama
        contentPanel.add(Box.createVerticalStrut(30));
        
        // Membuat Daftar Pesan
        for (InboxItemData message : messages) {
            contentPanel.add(createInboxItemPanel(message));
            // Tambahkan separator antar item, kecuali untuk item terakhir
            if (message != messages[messages.length - 1]) {
                contentPanel.add(Box.createVerticalStrut(15));
                JSeparator separator = new JSeparator();
                separator.setForeground(COLOR_BORDER);
                separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1)); // Pastikan separator tidak terlalu tebal
                contentPanel.add(separator);
                contentPanel.add(Box.createVerticalStrut(15));
            }
        }

        // Panel pembungkus dengan BorderLayout untuk memastikan `contentPanel` tidak ter-stretch
        // dan tetap berada di pojok kiri atas.
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBackground(COLOR_BACKGROUND);
        contentWrapper.add(contentPanel, BorderLayout.NORTH);
        
        JScrollPane scrollPane = new JScrollPane(contentWrapper); // ScrollPane sekarang membungkus `contentWrapper`
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        setContentPane(mainPanel);
    }
    
    private JPanel createInboxItemPanel(InboxItemData data) {
        JPanel itemPanel = new JPanel(new BorderLayout(20, 0));
        itemPanel.setBackground(COLOR_BACKGROUND);
        itemPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        itemPanel.setBorder(new EmptyBorder(10, 0, 10, 0)); // Padding hanya atas & bawah
        itemPanel.setAlignmentX(Component.LEFT_ALIGNMENT); // Rata kiri

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(data.title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(COLOR_TEXT_PRIMARY);
        textPanel.add(titleLabel);

        if (data.from != null && !data.from.isEmpty()) {
            textPanel.add(Box.createVerticalStrut(4));
            JLabel fromLabel = new JLabel(data.from);
            fromLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
            fromLabel.setForeground(COLOR_TEXT_SECONDARY);
            textPanel.add(fromLabel);
        }

        textPanel.add(Box.createVerticalStrut(8));
        JLabel descriptionLabel = new JLabel(data.description);
        descriptionLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        descriptionLabel.setForeground(COLOR_TEXT_SECONDARY);
        textPanel.add(descriptionLabel);
        
        JLabel dateLabel = new JLabel(data.date);
        dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        dateLabel.setForeground(COLOR_TEXT_SECONDARY);
        
        itemPanel.add(textPanel, BorderLayout.CENTER);
        itemPanel.add(dateLabel, BorderLayout.EAST);

        return itemPanel;
    }

    private static class InboxItemData {
        final String title;
        final String from;
        final String description;
        final String date;

        InboxItemData(String title, String from, String description, String date) {
            this.title = title;
            this.from = from;
            this.description = description;
            this.date = date;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new Inbox().setVisible(true);
        });
    }
}