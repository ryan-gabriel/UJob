package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;

import Auth.SessionManager;

public class MahasiswaDAO {
    private final Connection conn;

    public MahasiswaDAO() {
        conn = new DatabaseConnection().getConnection();
    }

    public boolean batalDaftarProyek(String proyekId){
        String query = "DELETE FROM pengajuan_proyek WHERE proyek_id = ? AND user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, proyekId);
            ps.setInt(2, SessionManager.getInstance().getId());
            int result = ps.executeUpdate();
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean daftarProyek(String proyekId) {
        try {
            String sql = "INSERT INTO pengajuan_proyek (user_id, proyek_id) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, SessionManager.getInstance().getId());
            stmt.setString(2, proyekId);
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Gagal mendaftar proyek: " + e.getMessage());
            return false;
        }
    }
}