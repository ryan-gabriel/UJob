package Mahasiswa;

import Components.MahasiswaNavigation;
import Database.InboxDAO;

import java.util.List;

import javax.swing.*;

import Auth.SessionManager;

import java.awt.*;
import net.miginfocom.swing.MigLayout;

public class Inbox extends JFrame {

    InboxDAO inboxDAO;
    List<Models.Inbox> inboxs;

    // Warna UI
    private static final Color COLOR_BACKGROUND = new Color(248, 250, 252);
    private static final Color COLOR_TEXT_PRIMARY = new Color(17, 24, 39);
    private static final Color COLOR_TEXT_SECONDARY = new Color(107, 114, 128);
    private static final Color COLOR_BORDER = new Color(229, 231, 235);
    private static final Color COLOR_CARD = new Color(255, 255, 255);

    // Data inbox
    private InboxItemData[] messages;

    public Inbox() {
        inboxDAO = new InboxDAO();
        inboxs = inboxDAO.getCurrentUserAllInbox();
        messages = inboxs.stream()
            .map(i -> new InboxItemData(i.getIsi(), i.getTanggal()))
            .toArray(InboxItemData[]::new);

        initializeUI();
    }

    private void initializeUI() {
        setTitle("Inbox - UJob Mahasiswa");
        setSize(1200, 800);
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel utama
        JPanel mainPanel = new JPanel(new MigLayout("fill, ins 0", "[grow]", "[][grow]"));
        mainPanel.setBackground(COLOR_BACKGROUND);

        // Navigasi atas
        mainPanel.add(new MahasiswaNavigation("Inbox"), "north, growx, wrap");

        // Kontainer konten
        JPanel contentPanel = new JPanel(new MigLayout("fillx, ins 30 40 40 40", "[grow]", "[][grow]"));
        contentPanel.setBackground(COLOR_BACKGROUND);

        // Header
        JPanel headerPanel = new JPanel(new MigLayout("fillx, ins 0", "[left]", "[]"));
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Kotak Masuk " + SessionManager.getInstance().getUsername());
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(COLOR_TEXT_PRIMARY);

        headerPanel.add(titleLabel);
        contentPanel.add(headerPanel, "growx, wrap, gapbottom 20");

        // Panel notifikasi
        JPanel notificationsPanel = new JPanel(new MigLayout("fillx, ins 0, wrap 1", "[grow]", "[]20[]"));
        notificationsPanel.setOpaque(false);
        notificationsPanel.setBorder(null);

        for (InboxItemData message : messages) {
            notificationsPanel.add(createNotificationCard(message), "growx");
        }

        JScrollPane scrollPane = new JScrollPane(notificationsPanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(true);
        scrollPane.getViewport().setOpaque(true);
        scrollPane.getViewport().setBackground(COLOR_BACKGROUND);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        contentPanel.add(scrollPane, "grow, push");
        mainPanel.add(contentPanel, "grow");

        setContentPane(mainPanel);
    }

    private JPanel createNotificationCard(InboxItemData data) {
        JPanel cardPanel = new JPanel(new MigLayout("fillx, ins 20 25 20 25", "[grow][]", "[top]"));
        cardPanel.setBackground(COLOR_CARD);
        cardPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, COLOR_BORDER));
        cardPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Isi pesan
        JLabel messageLabel = new JLabel("<html><div style='width:600px'>" + data.message + "</div></html>");
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        messageLabel.setForeground(COLOR_TEXT_PRIMARY);

        // Tanggal
        JLabel dateLabel = new JLabel(data.date);
        dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        dateLabel.setForeground(COLOR_TEXT_SECONDARY);

        cardPanel.add(messageLabel, "growx, push, wrap");
        cardPanel.add(dateLabel, "gapleft 0, span 2, split 2, right");

        return cardPanel;
    }

    private static class InboxItemData {
        final String message;
        final String date;

        InboxItemData(String message, String date) {
            this.message = message;
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
