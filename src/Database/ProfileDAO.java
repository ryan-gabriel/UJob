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
        // Asumsi Anda memiliki kelas DatabaseConnection untuk mengelola koneksi
        connection = DatabaseConnection.getConnection();
    }

    public ProfilMahasiswa getProfileById(int userId) {
        // Query untuk mengambil profil mahasiswa sekaligus nama dari tabel user
        String query = "SELECT pm.*, u.nama " +
                       "FROM profil_mahasiswa pm " +
                       "JOIN user u ON pm.user_id = u.user_id " +
                       "WHERE pm.user_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ProfilMahasiswa profile = new ProfilMahasiswa();
                    profile.setProfil_id(rs.getInt("profil_id")); // [PENTING] Ambil profil_id
                    profile.setUserId(rs.getInt("user_id"));
                    profile.setNama(rs.getString("nama"));
                    profile.setRingkasan(rs.getString("ringkasan"));
                    profile.setPendidikan(rs.getString("pendidikan"));
                    profile.setPengalaman(rs.getString("pengalaman"));
                    profile.setSkill(rs.getString("skill"));
                    return profile;
                } else {
                    // Jika profil belum ada di tabel profil_mahasiswa, buat objek baru
                    // hanya dengan user_id dan nama.
                    String userQuery = "SELECT nama FROM user WHERE user_id = ?";
                    try (PreparedStatement userStmt = connection.prepareStatement(userQuery)) {
                        userStmt.setInt(1, userId);
                        try (ResultSet userRs = userStmt.executeQuery()) {
                            if (userRs.next()) {
                                ProfilMahasiswa newProfile = new ProfilMahasiswa();
                                newProfile.setUserId(userId);
                                newProfile.setNama(userRs.getString("nama"));
                                return newProfile; // profil_id akan 0 (default)
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle error, misalnya dengan menampilkan dialog
        }
        return null; // Jika user_id tidak ditemukan sama sekali
    }

    public boolean updateOrInsertProfile(ProfilMahasiswa profile) {
        // Jika profil_id > 0, berarti profil sudah ada di database -> UPDATE
        if (profile.getProfil_id() > 0) {
            String query = "UPDATE profil_mahasiswa SET ringkasan = ?, pendidikan = ?, pengalaman = ?, skill = ? WHERE profil_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, profile.getRingkasan());
                stmt.setString(2, profile.getPendidikan());
                stmt.setString(3, profile.getPengalaman());
                stmt.setString(4, profile.getSkill());
                stmt.setInt(5, profile.getProfil_id());
                return stmt.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            // Jika profil_id adalah 0, berarti ini profil baru -> INSERT
            String query = "INSERT INTO profil_mahasiswa (user_id, ringkasan, pendidikan, pengalaman, skill) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, profile.getUserId());
                stmt.setString(2, profile.getRingkasan());
                stmt.setString(3, profile.getPendidikan());
                stmt.setString(4, profile.getPengalaman());
                stmt.setString(5, profile.getSkill());
                return stmt.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
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