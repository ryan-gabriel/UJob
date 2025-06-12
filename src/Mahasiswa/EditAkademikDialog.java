package Mahasiswa;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;

public class EditAkademikDialog extends JDialog {

    private Map<String, JTextField> textFields;
    private String updatedData = null;

    public EditAkademikDialog(JFrame owner, String currentAcademicText) {
        super(owner, "Edit Informasi Akademik", true);
        initializeUI(currentAcademicText);
    }

    private void initializeUI(String currentAcademicText) {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        // [PERBAIKAN] 'gap unrelated' dihapus dari definisi kolom utama karena sintaksnya salah
        // dan menyebabkan error saat dialog dibuat. Jarak akan ditambahkan pada komponennya langsung.
        JPanel mainPanel = new JPanel(new MigLayout(
            "insets 25, wrap 4", // Layout constraints
            "[right][grow, fill, 150px::][right][grow, fill, 150px::]" // Column constraints
        ));
        mainPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Edit Informasi Akademik");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setForeground(new Color(30, 41, 59));
        mainPanel.add(titleLabel, "span 4, center, wrap, gapbottom 20");

        Map<String, String> currentData = new HashMap<>();
        if (currentAcademicText != null && !currentAcademicText.trim().isEmpty()) {
            String[] pairs = currentAcademicText.split(",");
            for (String pair : pairs) {
                String[] kv = pair.split(":", 2);
                if (kv.length == 2) {
                    currentData.put(kv[0].trim(), kv[1].trim());
                }
            }
        }

        String[] labels = {"NIM", "Fakultas", "Program Studi", "Angkatan", "Semester", "IPK"};
        textFields = new LinkedHashMap<>();
        for (String label : labels) {
            JTextField field = new JTextField(currentData.getOrDefault(label, ""));
            field.setFont(new Font("SansSerif", Font.PLAIN, 13));
            textFields.put(label, field);
        }

        // [PERBAIKAN] Menambahkan constraint 'gap unrelated' langsung ke komponen label
        // di kolom ketiga untuk membuat jarak antar kolom. Ini adalah cara yang benar.
        
        mainPanel.add(new JLabel("NIM:"));
        mainPanel.add(textFields.get("NIM"));
        mainPanel.add(new JLabel("Fakultas:"), "gap unrelated");
        mainPanel.add(textFields.get("Fakultas"));

        mainPanel.add(new JLabel("Program Studi:"));
        mainPanel.add(textFields.get("Program Studi"));
        mainPanel.add(new JLabel("Angkatan:"), "gap unrelated");
        mainPanel.add(textFields.get("Angkatan"));

        mainPanel.add(new JLabel("Semester:"));
        mainPanel.add(textFields.get("Semester"));
        mainPanel.add(new JLabel("IPK:"), "gap unrelated");
        mainPanel.add(textFields.get("IPK"));
        
        JButton batalButton = new JButton("Batal");
        styleButton(batalButton, new Color(108, 117, 125), Color.WHITE);

        JButton simpanButton = new JButton("Simpan Perubahan");
        styleButton(simpanButton, new Color(13, 110, 253), Color.WHITE);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(batalButton);
        buttonPanel.add(simpanButton);
        mainPanel.add(buttonPanel, "span 4, growx, gaptop 30");

        batalButton.addActionListener(e -> dispose());
        simpanButton.addActionListener(e -> simpanPerubahan());

        add(mainPanel);
        pack();
        setMinimumSize(getSize());
        setResizable(false);
        setLocationRelativeTo(getParent());
    }

    private void styleButton(JButton button, Color background, Color foreground) {
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        button.setForeground(foreground);
        button.setBackground(background);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setBorder(new EmptyBorder(10, 25, 10, 25));
    }

    private void simpanPerubahan() {
        // [PERBAIKAN] Logika validasi yang lebih baik
        JTextField nimField = textFields.get("NIM");
        JTextField angkatanField = textFields.get("Angkatan");
        JTextField semesterField = textFields.get("Semester");
        JTextField ipkField = textFields.get("IPK");
        
        // Reset warna background jika sebelumnya error
        Color defaultBgColor = UIManager.getColor("TextField.background");
        for (JTextField field : textFields.values()) {
            field.setBackground(defaultBgColor);
        }
        
        // Validasi setiap field numerik
        try {
            if (!nimField.getText().trim().isEmpty()) Long.parseLong(nimField.getText().trim());
        } catch (NumberFormatException e) {
            showError(nimField, "NIM harus berupa angka.");
            return;
        }
        try {
            if (!angkatanField.getText().trim().isEmpty()) Integer.parseInt(angkatanField.getText().trim());
        } catch (NumberFormatException e) {
            showError(angkatanField, "Angkatan harus berupa angka (contoh: 2023).");
            return;
        }
        try {
            if (!semesterField.getText().trim().isEmpty()) Integer.parseInt(semesterField.getText().trim());
        } catch (NumberFormatException e) {
            showError(semesterField, "Semester harus berupa angka.");
            return;
        }
        try {
            // Mengizinkan koma sebagai pemisah desimal dan menggantinya dengan titik
            if (!ipkField.getText().trim().isEmpty()) Double.parseDouble(ipkField.getText().trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            showError(ipkField, "IPK harus berupa angka desimal (contoh: 3.85).");
            return;
        }
        
        StringBuilder sb = new StringBuilder();
        String[] labelsInOrder = {"NIM", "Fakultas", "Program Studi", "Angkatan", "Semester", "IPK"};
        for (String label : labelsInOrder) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(label).append(":").append(textFields.get(label).getText().trim());
        }
        this.updatedData = sb.toString();
        dispose();
    }

    private void showError(JTextField field, String message) {
        JOptionPane.showMessageDialog(this, message, "Input Tidak Valid", JOptionPane.ERROR_MESSAGE);
        field.setBackground(new Color(255, 224, 224));
        field.requestFocus();
    }
    
    public String getUpdatedData() {
        return this.updatedData;
    }
    
    // [PERBAIKAN] Menghapus metode isSaved() yang tidak digunakan dan menyebabkan error.
}