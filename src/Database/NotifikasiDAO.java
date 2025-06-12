package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NotifikasiDAO {

    /**
     * Metode statis untuk mengirim notifikasi ke user tertentu.
     * Bisa dipanggil dari mana saja di dalam proyek.
     * @param userId ID user penerima notifikasi.
     * @param pesan Isi pesan notifikasi.
     */
    public static void kirimNotifikasi(int userId, String pesan) {
        System.out.println("[NotifikasiDAO] Mencoba kirim notifikasi ke User ID: " + userId);
        
        // Query INSERT yang sudah disesuaikan dengan struktur tabel yang benar
        String sql = "INSERT INTO notifikasi (user_id, isi, waktu, dibaca) VALUES (?, ?, NOW(), 0)";

        // Menggunakan try-with-resources untuk memastikan koneksi ditutup
        try (Connection conn = DatabaseConnection.getConnection(); // Pastikan Anda punya class ini
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            ps.setString(2, pesan);
            
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows > 0) {
                System.out.println("[NotifikasiDAO] SUKSES: Notifikasi berhasil disimpan ke database.");
            } else {
                System.err.println("[NotifikasiDAO] GAGAL: Perintah INSERT notifikasi tidak berhasil.");
            }

        } catch (SQLException e) {
            // Error ini akan tercetak di konsol jika ada masalah
            System.err.println("[NotifikasiDAO] GAGAL TOTAL karena SQLException!");
            e.printStackTrace();
        }
    }
}