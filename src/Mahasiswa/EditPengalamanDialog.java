package Mahasiswa;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.stream.IntStream;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;

public class EditPengalamanDialog extends JDialog {

    private JTextField titleField, companyField, locationField;
    private JComboBox<String> typeCombo, locationTypeCombo, startMonthCombo, startYearCombo, endMonthCombo, endYearCombo;
    private JCheckBox currentlyWorkingCheck;
    private JTextArea descriptionArea;
    private boolean saved = false;

    public EditPengalamanDialog(JFrame owner, String data) {
        super(owner, "Tambah/Edit Pengalaman", true);
        initializeUI();
        // NOTE: Logika untuk mengisi form dari 'data' (saat mengedit) bisa ditambahkan di sini.
        // Untuk saat ini, dialog difokuskan untuk menambah entri baru.
    }

    private void initializeUI() {
        setSize(550, 650);
        setResizable(false);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new MigLayout("wrap 2, fillx, insets 20", "[right, 120]rel[grow, fill]"));
        mainPanel.setBackground(Color.WHITE);

        // --- Form Fields ---
        titleField = new JTextField();
        companyField = new JTextField();
        locationField = new JTextField();
        
        String[] employmentTypes = {"Pilih Tipe", "Penuh Waktu", "Paruh Waktu", "Magang", "Kontrak", "Freelance"};
        typeCombo = new JComboBox<>(employmentTypes);

        currentlyWorkingCheck = new JCheckBox("Saya sedang bekerja di peran ini");
        currentlyWorkingCheck.setBackground(Color.WHITE);

        String[] months = {"Bulan", "Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
        startMonthCombo = new JComboBox<>(months);
        endMonthCombo = new JComboBox<>(months);
        
        Integer[] years = IntStream.range(1980, 2026).boxed().sorted((a, b) -> b.compareTo(a)).toArray(Integer[]::new);
        startYearCombo = new JComboBox(years);
        endYearCombo = new JComboBox(years);
        
        String[] locationTypes = {"Pilih Tipe Lokasi", "On-site", "Hybrid", "Remote"};
        locationTypeCombo = new JComboBox<>(locationTypes);
        
        descriptionArea = new JTextArea(5, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descScrollPane = new JScrollPane(descriptionArea);

        // --- Layouting ---
        mainPanel.add(new JLabel("Judul*"));
        mainPanel.add(titleField, "wrap");

        mainPanel.add(new JLabel("Tipe Pekerjaan*"));
        mainPanel.add(typeCombo, "wrap");

        mainPanel.add(new JLabel("Nama Perusahaan*"));
        mainPanel.add(companyField, "wrap");

        mainPanel.add(new JLabel(""));
        mainPanel.add(currentlyWorkingCheck, "wrap");

        mainPanel.add(new JLabel("Tanggal Mulai*"));
        JPanel startPanel = new JPanel(new GridLayout(1, 2, 5, 0));
        startPanel.setOpaque(false);
        startPanel.add(startMonthCombo);
        startPanel.add(startYearCombo);
        mainPanel.add(startPanel, "split 2, growx, wrap");

        final JLabel endLabel = new JLabel("Tanggal Selesai*");
        final JPanel endPanel = new JPanel(new GridLayout(1, 2, 5, 0));
        endPanel.setOpaque(false);
        endPanel.add(endMonthCombo);
        endPanel.add(endYearCombo);
        mainPanel.add(endLabel);
        mainPanel.add(endPanel, "split 2, growx, wrap");
        
        mainPanel.add(new JLabel("Lokasi"));
        mainPanel.add(locationField, "wrap");
        
        mainPanel.add(new JLabel("Tipe Lokasi"));
        mainPanel.add(locationTypeCombo, "wrap");

        mainPanel.add(new JLabel("Deskripsi"), "aligny top");
        mainPanel.add(descScrollPane, "h 100!, grow, wrap");

        // --- Checkbox Logic ---
        currentlyWorkingCheck.addItemListener(e -> {
            boolean selected = e.getStateChange() == ItemEvent.SELECTED;
            endLabel.setEnabled(!selected);
            endMonthCombo.setEnabled(!selected);
            endYearCombo.setEnabled(!selected);
        });

        // --- Buttons ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JButton saveButton = new JButton("Simpan");
        JButton cancelButton = new JButton("Batal");

        saveButton.addActionListener(e -> onSave());
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void onSave() {
        if (titleField.getText().trim().isEmpty() || 
            companyField.getText().trim().isEmpty() || 
            typeCombo.getSelectedIndex() == 0 ||
            startMonthCombo.getSelectedIndex() == 0 ||
            (!currentlyWorkingCheck.isSelected() && endMonthCombo.getSelectedIndex() == 0)) {
            JOptionPane.showMessageDialog(this, "Mohon isi semua field yang bertanda *", "Input Tidak Lengkap", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        saved = true;
        dispose();
    }

    public boolean isSaved() {
        return saved;
    }
    
    /**
     * [DIPERBAIKI] Mengembalikan string dengan format 8 bagian yang dipisahkan "||"
     * agar sesuai dengan parser di Profile.java.
     * Format: Judul||Tipe Pekerjaan||Nama Perusahaan||Tanggal Mulai||Tanggal Selesai||Lokasi||Tipe Lokasi||Deskripsi
     */
    public String getUpdatedPengalaman() {
        if (!saved) return null;

        String judul = titleField.getText().trim();
        String tipePekerjaan = typeCombo.getSelectedItem().toString();
        String namaPerusahaan = companyField.getText().trim();
        String tanggalMulai = startMonthCombo.getSelectedItem().toString() + " " + startYearCombo.getSelectedItem().toString();
        String tanggalSelesai = currentlyWorkingCheck.isSelected() ? "Saat ini" : endMonthCombo.getSelectedItem().toString() + " " + endYearCombo.getSelectedItem().toString();
        String lokasi = locationField.getText().trim();
        String tipeLokasi = locationTypeCombo.getSelectedItem().toString();
        String deskripsi = descriptionArea.getText().trim();
        
        return String.join("||", 
                judul, tipePekerjaan, namaPerusahaan, 
                tanggalMulai, tanggalSelesai, lokasi, 
                tipeLokasi, deskripsi);
    }
}