package Mahasiswa;

import net.miginfocom.swing.MigLayout;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import Components.ProyekHeaderPanel;

public class ProyekCariTeman extends JFrame {

    public ProyekCariTeman() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("UPI Job - Cari Teman");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 247, 250));

        // Header dengan tab "Cari Teman" aktif
        ProyekHeaderPanel headerPanel = new ProyekHeaderPanel("Cari Teman");
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Content area
        mainPanel.add(createContentPanel(), BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

private JPanel createContentPanel() {
    JPanel contentPanel = new JPanel(new MigLayout("wrap 1, fillx", "[grow]", "[]20[grow]"));
    contentPanel.setBackground(new Color(245, 247, 250));
    contentPanel.setBorder(new EmptyBorder(10, 40, 30, 40));

    // === TOP SEARCH === (tetap sama)
    JPanel topSection = new JPanel(new MigLayout("insets 0", "[80%][20%]", "[150]"));
    topSection.setOpaque(false);
    JTextField searchField = new JTextField();
    searchField.setFont(new Font("Arial", Font.PLAIN, 13));
    searchField.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.LIGHT_GRAY),
        BorderFactory.createEmptyBorder(5, 10, 5, 10)
    ));
    JButton searchBtn = new JButton("Search");
    searchBtn.setBackground(new Color(37, 64, 143));
    searchBtn.setForeground(Color.WHITE);
    searchBtn.setFont(new Font("Arial", Font.BOLD, 13));
    searchBtn.setFocusPainted(false);

    topSection.add(searchField, "growx");
    topSection.add(searchBtn, "growx, gapleft 10");
    contentPanel.add(topSection, "growx, pushx");

    // === SCROLLABLE PROFILES ===
    JPanel profilesPanel = createProfilesPanel();
    JScrollPane scrollPane = new JScrollPane(profilesPanel);
    scrollPane.setBorder(BorderFactory.createEmptyBorder());
    scrollPane.setBackground(new Color(245, 247, 250));
    scrollPane.getViewport().setBackground(new Color(245, 247, 250));
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.getVerticalScrollBar().setUnitIncrement(16);

    contentPanel.add(scrollPane, "grow");
    return contentPanel;
}

    private JPanel createProfilesPanel() {
        JPanel profilesPanel = new JPanel(new MigLayout("wrap 2, gap 20 20, insets 0", "[grow][grow]", ""));
        profilesPanel.setBackground(new Color(245, 247, 250));

        String[][] profiles = {
            {"Ryan Gabriel", "Matematika", "2023", "Data enthusiast dengan keahlian machine learning dan statistik. Senang menganalisis data untuk mendapatkan insights."},
            {"Dinda Sabila", "Rekayasa Perangkat Lunak", "2023", "Mahasiswi RPL yang tertarik pada pengembangan mobile dan UI/UX design. Aktif mengerjakan proyek open-source."},
            {"Nadia Putri", "Fisika", "2022", "Peneliti fisika partikel dengan pengalaman simulasi Monte Carlo. Tertarik pada kolaborasi interdisipliner."},
            {"Dewa Santoso", "Ilmu Komputer", "2024", "Software engineer pemula yang fokus pada pengembangan aplikasi AI dan visi komputer."},
            {"Lestari Ayu", "Bioteknologi", "2021", "Spesialis analisis genom dengan latar belakang bioinformatika. Bersemangat mengolah big data biologis."},
            {"Arif Nugraha", "Statistika", "2022", "Tertarik pada data science dan visualisasi data. Pernah mengikuti pelatihan Google Data Analytics."},
            {"Putri Ananda", "Teknik Elektro", "2023", "Mempunyai minat dalam sistem embedded dan pengembangan perangkat IoT."},
            {"Bagas Pratama", "Sistem Informasi", "2024", "Fokus pada integrasi sistem bisnis dan teknologi. Pernah membuat sistem POS untuk UMKM."},
            {"Selina Mahesa", "Kesehatan Masyarakat", "2021", "Berpengalaman dalam kampanye kesehatan digital. Aktif di organisasi kemahasiswaan dan riset perilaku masyarakat."},
            {"Hafiz Rahman", "Teknik Industri", "2023", "Menekuni manajemen proyek dan optimasi proses bisnis. Sedang magang di perusahaan manufaktur multinasional."}
        };

        for (String[] project : profiles) {
            JPanel card = createProfilesCard(project[0], project[1], project[2], project[3]);
            profilesPanel.add(card, "grow");
        }

        return profilesPanel;
    }

private JPanel createProfilesCard(String title, String startDate, String endDate, String description) {
    JPanel card = new JPanel(new MigLayout("wrap 1, fillx"));
    card.setBackground(Color.WHITE);
    card.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(230, 230, 230)),
        new EmptyBorder(20, 20, 20, 20)
    ));

    JLabel titleLabel = new JLabel(title);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
    titleLabel.setForeground(new Color(37, 64, 143));

    JLabel dateLabel = new JLabel("Diposting: " + startDate + " | Ditutup: " + endDate);
    dateLabel.setFont(new Font("Arial", Font.PLAIN, 12));
    dateLabel.setForeground(Color.GRAY);

    // HTML wrapping for multi-line description with fixed width
    String htmlDesc = "<html><div style='width:300px; line-height:1.4;'>" + description + "</div></html>";
    JLabel descLabel = new JLabel(htmlDesc);
    descLabel.setFont(new Font("Arial", Font.PLAIN, 13));
    descLabel.setForeground(Color.DARK_GRAY);

    JButton daftarBtn = new JButton("Daftar");
    daftarBtn.setBackground(new Color(37, 64, 143));
    daftarBtn.setForeground(Color.WHITE);
    daftarBtn.setFont(new Font("Arial", Font.BOLD, 12));
    daftarBtn.setFocusPainted(false);

    card.add(titleLabel);
    card.add(dateLabel);
    card.add(descLabel);
    card.add(daftarBtn, "align left");

    return card;
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProyekCariTeman().setVisible(true));
    }
}




