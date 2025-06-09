package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import Auth.SessionManager;

public class MahasiswaDAO {
    private final Connection conn;

    public MahasiswaDAO() {
        conn = DatabaseConnection.getConnection();
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

            String ownerId = proyekDAO.getOwnerId(proyekId);
            String isiNotifikasi2 = SessionManager.getInstance().getUsername() + " membatalkan pendaftaran ke proyek anda dengan judul '" + proyekDAO.getJudulProyek(proyekId) + "'.";
            String logSql2 = "INSERT INTO notifikasi (user_id, isi) VALUES (?, ?)";

            DatabaseConnection.getInstance().execute(logSql2, ownerId, isiNotifikasi2);

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
            
            String ownerId = proyekDAO.getOwnerId(proyekId);
            String isiNotifikasi2 = SessionManager.getInstance().getUsername() + " mendaftar ke proyek anda dengan judul '" + proyekDAO.getJudulProyek(proyekId) + "'.";
            String logSql2 = "INSERT INTO notifikasi (user_id, isi) VALUES (?, ?)";

            DatabaseConnection.getInstance().execute(logSql2, ownerId, isiNotifikasi2);

            return true;
        } catch (Exception e) {
            System.out.println("Gagal mendaftar proyek: " + e.getMessage());
            return false;
        }
    }

    public boolean isSetPortofolio() {
        String query = "SELECT 1 FROM portofolio WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, SessionManager.getInstance().getId());
            return ps.executeQuery().next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isSetRingkasan() {
        String query = "SELECT ringkasan FROM profil_mahasiswa WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, SessionManager.getInstance().getId());
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getString("ringkasan") != null && !rs.getString("ringkasan").trim().isEmpty();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isSetPendidikan() {
        String query = "SELECT pendidikan FROM profil_mahasiswa WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, SessionManager.getInstance().getId());
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getString("pendidikan") != null && !rs.getString("pendidikan").trim().isEmpty();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isSetPengalaman() {
        String query = "SELECT pengalaman FROM profil_mahasiswa WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, SessionManager.getInstance().getId());
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getString("pengalaman") != null && !rs.getString("pengalaman").trim().isEmpty();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getCurrentMahasiswaPendidikan(){
        String query = "SELECT pendidikan FROM profil_mahasiswa WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, SessionManager.getInstance().getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("pendidikan");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}