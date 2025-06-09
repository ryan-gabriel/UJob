package Database;

import Models.ProfilMahasiswa;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfileDAO {
    private Connection connection;

    public ProfileDAO() {
           try {
            // Gunakan getInstance() untuk mendapatkan koneksi
            this.connection = DatabaseConnection.getInstance().getConnection();
            
            if (this.connection == null) {
                throw new SQLException("Koneksi database gagal dibuat");
            }
        } catch (SQLException e) {
            System.err.println("Kesalahan saat membuat koneksi: " + e.getMessage());
            throw new RuntimeException("Tidak dapat membuat koneksi database", e);
        }
    }

    // Mendapatkan semua profil
    public List<ProfilMahasiswa> getAllProfiles() {
    List<ProfilMahasiswa> profiles = new ArrayList<>();
    String query = "SELECT pm.*, u.nama " +
                   "FROM profil_mahasiswa pm " +
                   "JOIN user u ON pm.user_id = u.user_id";
    
    try (PreparedStatement stmt = connection.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {
        
        while (rs.next()) {
            ProfilMahasiswa profile = new ProfilMahasiswa(
                rs.getInt("user_id"),
                rs.getString("nama"), // Tambahkan nama dari tabel users
                rs.getString("pendidikan"),
                rs.getString("ringkasan"),
                rs.getString("pengalaman"),
                rs.getString("skill")
            );
            profiles.add(profile);
        }
    } catch (SQLException e) {
        System.err.println("Kesalahan saat mengambil semua profil: " + e.getMessage());
        e.printStackTrace();
    }
    return profiles;
}

public List<ProfilMahasiswa> searchProfiles(String query) {
    List<ProfilMahasiswa> filteredProfiles = new ArrayList<>();
    String searchQuery = "SELECT pm.*, u.nama " +
                         "FROM profil_mahasiswa pm " +
                         "JOIN user u ON pm.user_id = u.user_id " +
                         "WHERE pendidikan LIKE ? OR ringkasan LIKE ? " +
                         "OR pengalaman LIKE ? OR skill LIKE ? OR u.nama LIKE ?";
    
    try (PreparedStatement stmt = connection.prepareStatement(searchQuery)) {
        String searchParam = "%" + query + "%";
        stmt.setString(1, searchParam);
        stmt.setString(2, searchParam);
        stmt.setString(3, searchParam);
        stmt.setString(4, searchParam);
        stmt.setString(5, searchParam);

        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ProfilMahasiswa profile = new ProfilMahasiswa(
                    rs.getInt("user_id"),
                    rs.getString("nama"), // Tambahkan nama dari tabel users
                    rs.getString("pendidikan"),
                    rs.getString("ringkasan"),
                    rs.getString("pengalaman"),
                    rs.getString("skill")
                );
                filteredProfiles.add(profile);
            }
        }
    } catch (SQLException e) {
        System.err.println("Kesalahan saat mencari profil: " + e.getMessage());
        e.printStackTrace();
    }
    return filteredProfiles;
}

// Juga update metode getProfileById
public ProfilMahasiswa getProfileById(int userId) {
    ProfilMahasiswa profile = null;
    String query = "SELECT pm.*, u.nama " +
                   "FROM profil_mahasiswa pm " +
                   "JOIN user u ON pm.user_id = u.user_id " +
                   "WHERE pm.user_id = ?";
    
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setInt(1, userId);
        
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                profile = new ProfilMahasiswa(
                    rs.getInt("user_id"),
                    rs.getString("nama"), // Tambahkan nama dari tabel users
                    rs.getString("pendidikan"),
                    rs.getString("ringkasan"),
                    rs.getString("pengalaman"),
                    rs.getString("skill")
                );
            }
        }
    } catch (SQLException e) {
        System.err.println("Kesalahan saat mengambil profil berdasarkan ID: " + e.getMessage());
        e.printStackTrace();
    }
    return profile;
}


    // Mengirim permintaan koneksi
    public boolean sendConnectionRequest(int senderId, int receiverId) {
        String query = "INSERT INTO permintaan_koneksi (pengirim_id, penerima_id, status) VALUES (?, ?, 'menunggu')";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, senderId);
            stmt.setInt(2, receiverId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Kesalahan saat mengirim permintaan koneksi: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Metode untuk menutup koneksi database
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Kesalahan saat menutup koneksi: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
