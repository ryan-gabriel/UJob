package Perusahaan;

import Components.PerusahaanNavigation;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Kelas untuk menampilkan halaman Inbox Perusahaan.
 * Mengintegrasikan navigasi terpusat dan menampilkan daftar pesan/notifikasi.
 *
 * @author Rian G S & AI Assistant
 */
public class Inbox extends JFrame {

    // Konstanta untuk warna UI
    private static final Color BORDER_COLOR = new Color(220, 220, 220);
    private static final Color TEXT_GRAY = new Color(128, 128, 128);
    private static final Color HOVER_COLOR = new Color(245, 248, 255); // Warna hover yang lebih lembut
    private static final Color BACKGROUND_WHITE = Color.WHITE;

    private JPanel inboxContainer;
    private final int userId;

    /**
     * Konstruktor untuk membuat frame Inbox.
     * @param userId ID pengguna yang sedang login, untuk konsistensi dengan frame lain.
     */
    public Inbox(int userId) {
        this.userId = userId;
        initializeUI();
        populateInboxData();
    }
    
    /**
     * Menginisialisasi komponen utama UI, layout, dan navigasi.
     */
    private void initializeUI() {
        setTitle("UJob - Inbox Perusahaan");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.setBackground(BACKGROUND_WHITE);

        PerusahaanNavigation navigationPanel = new PerusahaanNavigation("Inbox");
        contentPane.add(navigationPanel, BorderLayout.NORTH);
        
        contentPane.add(createMainContent(), BorderLayout.CENTER);
    }
    
    /**
     * Membuat panel konten utama yang berisi judul dan daftar inbox.
     * @return JPanel yang berisi seluruh konten utama.
     */
    private JPanel createMainContent() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBorder(new EmptyBorder(20, 40, 40, 40));
        mainPanel.setBackground(BACKGROUND_WHITE);
        
        JLabel pageTitle = new JLabel("Kotak Masuk (Inbox)");
        pageTitle.setFont(new Font("Arial", Font.BOLD, 28));
        pageTitle.setForeground(Color.BLACK);
        
        JPanel inboxHeader = createInboxHeader();
        
        JScrollPane inboxScrollPane = createInboxScrollPane();
        
        JPanel headerWrapper = new JPanel(new BorderLayout());
        headerWrapper.setBackground(BACKGROUND_WHITE);
        headerWrapper.add(pageTitle, BorderLayout.NORTH);
        headerWrapper.add(inboxHeader, BorderLayout.CENTER);
        
        mainPanel.add(headerWrapper, BorderLayout.NORTH);
        mainPanel.add(inboxScrollPane, BorderLayout.CENTER);
        
        return mainPanel;
    }
    
    /**
     * Membuat header untuk area inbox, seperti label "Inbox Terbaru".
     * @return JPanel header.
     */
    private JPanel createInboxHeader() {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        header.setBackground(BACKGROUND_WHITE);
        header.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        JLabel inboxLabel = new JLabel("Inbox Terbaru");
        inboxLabel.setFont(new Font("Arial", Font.BOLD, 16));
        inboxLabel.setForeground(Color.BLACK);
        
        header.add(inboxLabel);
        
        return header;
    }
    
    /**
     * Membuat JScrollPane yang akan menampung semua item inbox.
     * @return JScrollPane yang telah dikonfigurasi.
     */
    private JScrollPane createInboxScrollPane() {
        inboxContainer = new JPanel();
        inboxContainer.setLayout(new BoxLayout(inboxContainer, BoxLayout.Y_AXIS));
        inboxContainer.setBackground(BACKGROUND_WHITE);
        
        JScrollPane scrollPane = new JScrollPane(inboxContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(BACKGROUND_WHITE);
        
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setUI(new ThinScrollBarUI());
        
        return scrollPane;
    }
    
    /**
     * Mengisi inbox dengan data sampel.
     */
    private void populateInboxData() {
        addInboxItem("Lamaran Masuk", "Lamaran dari Dhafin Salman", 
                    "Mahasiswa bernama Dhafin Salman dari program studi Rekayasa Perangkat Lunak, telah melamar posisi \"Pengembang Web\" yang Anda posting pada 21/05/2025.", 
                    "21 Mei 2025");
        
        addInboxItem("Lamaran Masuk", "Lamaran dari Ryan Gabriel", 
                    "Mahasiswa bernama Ryan Gabriel dari program studi Rekayasa Perangkat Lunak telah melamar posisi \"Pengembang Web\" yang Anda posting pada 21/05/2025.", 
                    "21 Mei 2025");
        
        addInboxItem("Lamaran Masuk", "Lamaran dari John Doe", 
                    "Mahasiswa bernama John Doe dari program studi Teknik Komputer telah melamar posisi \"Magang Pengembang AI\" yang Anda posting pada 21/05/2025.", 
                    "21 Mei 2025");
        
        addInboxItem("Lowongan Dibuat", "Dibuat oleh PT Pactindo", 
                    "Anda telah membuat lowongan \"Pengembang Web\" pada tanggal 21/05/2025.", 
                    "20 Mei 2025");
    }
    
    /**
     * Membuat dan menambahkan satu item ke dalam kontainer inbox.
     */
    private void addInboxItem(String type, String title, String description, String date) {
        JPanel itemPanel = new JPanel(new BorderLayout(0, 8));
        itemPanel.setBackground(BACKGROUND_WHITE);
        itemPanel.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 0, 1, 0, BORDER_COLOR),
            new EmptyBorder(20, 20, 20, 20)
        ));
        itemPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel typeLabel = new JLabel(type);
        typeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        typeLabel.setForeground(Color.BLACK);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        titleLabel.setForeground(TEXT_GRAY);
        
        JPanel leftHeaderContent = new JPanel();
        leftHeaderContent.setLayout(new BoxLayout(leftHeaderContent, BoxLayout.Y_AXIS));
        leftHeaderContent.setOpaque(false);
        leftHeaderContent.add(typeLabel);
        leftHeaderContent.add(Box.createVerticalStrut(5));
        leftHeaderContent.add(titleLabel);
        
        JLabel dateLabel = new JLabel(date);
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        dateLabel.setForeground(TEXT_GRAY);
        
        headerPanel.add(leftHeaderContent, BorderLayout.CENTER);
        headerPanel.add(dateLabel, BorderLayout.EAST);
        
        JTextArea descArea = new JTextArea(description);
        descArea.setFont(new Font("Arial", Font.PLAIN, 13));
        descArea.setForeground(new Color(51, 51, 51));
        descArea.setOpaque(false);
        descArea.setEditable(false);
        descArea.setFocusable(false);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);

        itemPanel.add(headerPanel, BorderLayout.NORTH);
        itemPanel.add(descArea, BorderLayout.CENTER);

        MouseAdapter hoverAdapter = new MouseAdapter() {
            private final Component[] components = {itemPanel, headerPanel, leftHeaderContent, descArea};

            @Override
            public void mouseEntered(MouseEvent e) {
                for (Component comp : components) {
                    comp.setBackground(HOVER_COLOR);
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                for (Component comp : components) {
                    comp.setBackground(BACKGROUND_WHITE);
                }
            }
        };

        itemPanel.addMouseListener(hoverAdapter);
        descArea.addMouseListener(hoverAdapter);
        
        inboxContainer.add(itemPanel);
    }
    
    /**
     * Main method untuk menjalankan frame.
     */
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(Inbox.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        EventQueue.invokeLater(() -> {
            new Inbox(1).setVisible(true);
        });
    }

    /**
     * Kustomisasi UI untuk JScrollBar agar lebih tipis dan modern.
     */
    static class ThinScrollBarUI extends BasicScrollBarUI {
        private final int SCROLL_BAR_WIDTH = 8;
        private final Color THUMB_COLOR = new Color(180, 180, 180);
        private final Color TRACK_COLOR = new Color(245, 245, 245);

        // --- PERBAIKAN UTAMA ADA DI SINI ---
        @Override
        public Dimension getPreferredSize(JComponent c) {
            // Memeriksa orientasi scrollbar dan mengembalikan dimensi yang tetap.
            // Ini menghindari pemanggilan rekursif yang menyebabkan StackOverflowError.
            if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
                return new Dimension(SCROLL_BAR_WIDTH, 0);
            } else {
                return new Dimension(0, SCROLL_BAR_WIDTH);
            }
        }
        // --- AKHIR PERBAIKAN ---

        @Override
        protected void configureScrollBarColors() {
            thumbColor = THUMB_COLOR;
            trackColor = TRACK_COLOR;
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            g.setColor(trackColor);
            g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) return;
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(thumbColor);
            g2.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 10, 10);
            g2.dispose();
        }

        @Override
        protected JButton createDecreaseButton(int orientation) { return createZeroButton(); }

        @Override
        protected JButton createIncreaseButton(int orientation) { return createZeroButton(); }

        private JButton createZeroButton() {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            button.setMinimumSize(new Dimension(0, 0));
            button.setMaximumSize(new Dimension(0, 0));
            return button;
        }
    }
}