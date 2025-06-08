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

            ProyekDAO proyekDAO = new ProyekDAO();
            String isiNotifikasi = "Anda membatalkan pendaftaran ke proyek '" + proyekDAO.getJudulProyek(proyekId) + "'.";
            String logSql = "INSERT INTO notifikasi (user_id, isi) VALUES (?, ?)";
            DatabaseConnection.getInstance().execute(logSql, String.valueOf(SessionManager.getInstance().getId()), isiNotifikasi);

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

            ProyekDAO proyekDAO = new ProyekDAO();

            String isiNotifikasi = "Anda melakukan pendaftaran ke proyek '" + proyekDAO.getJudulProyek(proyekId) + "'.";
            String logSql = "INSERT INTO notifikasi (user_id, isi) VALUES (?, ?)";
            DatabaseConnection.getInstance().execute(logSql, String.valueOf(SessionManager.getInstance().getId()), isiNotifikasi);

            return true;
        } catch (Exception e) {
            System.out.println("Gagal mendaftar proyek: " + e.getMessage());
            return false;
        }
    }
}