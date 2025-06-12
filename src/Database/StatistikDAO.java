package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class StatistikDAO {
    
    // Mendapatkan perusahaan_id dari user_id
    public int getPerusahaanIdFromUserId(int userId) {
        String sql = "SELECT perusahaan_id FROM perusahaan WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("perusahaan_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Mengindikasikan tidak ditemukan
    }

    // Mengambil semua statistik yang dibutuhkan untuk Dashboard dan halaman lain
    public Map<String, Integer> fetchDashboardStats(int perusahaanId) {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("totalLowongan", 0);
        stats.put("lowonganAktif", 0);
        stats.put("totalLamaran", 0);

        String totalLowonganSql = "SELECT COUNT(*) FROM lowongan WHERE perusahaan_id = ?";
        String aktifLowonganSql = "SELECT COUNT(*) FROM lowongan WHERE perusahaan_id = ? AND status = 'aktif'";
        String totalLamaranSql = "SELECT COUNT(*) FROM lamaran l JOIN lowongan lo ON l.lowongan_id = lo.lowongan_id WHERE lo.perusahaan_id = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Get total lowongan
            try (PreparedStatement ps = conn.prepareStatement(totalLowonganSql)) {
                ps.setInt(1, perusahaanId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) stats.put("totalLowongan", rs.getInt(1));
                }
            }
            // Get lowongan aktif
            try (PreparedStatement ps = conn.prepareStatement(aktifLowonganSql)) {
                ps.setInt(1, perusahaanId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) stats.put("lowonganAktif", rs.getInt(1));
                }
            }
            // Get total lamaran
            try (PreparedStatement ps = conn.prepareStatement(totalLamaranSql)) {
                ps.setInt(1, perusahaanId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) stats.put("totalLamaran", rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stats;
    }
}