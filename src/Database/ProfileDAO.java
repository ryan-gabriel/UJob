package Database;

import Models.ProfilMahasiswa;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ProfileDAO {

    private final Connection connection;

    public ProfileDAO() {
        connection = DatabaseConnection.getConnection();
    }

    public ProfilMahasiswa getProfileById(int userId) {
        String query = "SELECT pm.*, u.nama " +
                       "FROM profil_mahasiswa pm " +
                       "JOIN user u ON pm.user_id = u.user_id " +
                       "WHERE pm.user_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new ProfilMahasiswa(
                        rs.getInt("user_id"),
                        rs.getString("nama"),
                        rs.getString("ringkasan"),
                        rs.getString("pendidikan"),
                        rs.getString("pengalaman"),
                        rs.getString("skill")
                    );
                } else {
                    String userQuery = "SELECT nama FROM user WHERE user_id = ?";
                    try (PreparedStatement userStmt = connection.prepareStatement(userQuery)) {
                        userStmt.setInt(1, userId);
                        try (ResultSet userRs = userStmt.executeQuery()) {
                            if (userRs.next()) {
                                String nama = userRs.getString("nama");
                                return new ProfilMahasiswa(userId, nama, "", "", "", "");
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; 
    }

    public boolean updateOrInsertProfile(ProfilMahasiswa profile) {
        String query = "INSERT INTO profil_mahasiswa (user_id, ringkasan, pendidikan, pengalaman, skill) " +
                       "VALUES (?, ?, ?, ?, ?) " +
                       "ON DUPLICATE KEY UPDATE " +
                       "ringkasan = VALUES(ringkasan), " +
                       "pendidikan = VALUES(pendidikan), " +
                       "pengalaman = VALUES(pengalaman), " +
                       "skill = VALUES(skill)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, profile.getUserId());
            stmt.setString(2, profile.getRingkasan());
            stmt.setString(3, profile.getPendidikan());
            stmt.setString(4, profile.getPengalaman());
            stmt.setString(5, profile.getSkill());
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // =====================================================================
    // METODE-METODE BARU UNTUK UPDATE SPESIFIK (DITAMBAHKAN DI SINI)
    // =====================================================================

    public boolean updateRingkasan(int userId, String ringkasan) {
        String query = "UPDATE profil_mahasiswa SET ringkasan = ? WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, ringkasan);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePendidikan(int userId, String pendidikan) {
        String query = "UPDATE profil_mahasiswa SET pendidikan = ? WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, pendidikan);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateSkill(int userId, String skill) {
        String query = "UPDATE profil_mahasiswa SET skill = ? WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, skill);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePengalaman(int userId, String pengalaman) {
        String query = "UPDATE profil_mahasiswa SET pengalaman = ? WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, pengalaman);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ProfilMahasiswa> getAllProfiles() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public List<ProfilMahasiswa> searchProfiles(String query) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public boolean sendConnectionRequest(int currentUserId, int targetUserId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}