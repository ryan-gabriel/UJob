package Database;

import Models.ProfilPerusahaan;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object untuk mengelola data perusahaan
 * Menggunakan JOIN dengan tabel user (sama seperti ProfileDAO)
 */
public class ProfilePerusahaanDAO {
    
    private static final Logger LOGGER = Logger.getLogger(ProfilePerusahaanDAO.class.getName());
    private final Connection connection;

    public ProfilePerusahaanDAO() {
        connection = DatabaseConnection.getConnection();
    }
    
    /**
     * Mendapatkan profil perusahaan berdasarkan user_id dengan JOIN
     * Menggunakan pola yang sama dengan ProfileDAO
     */
    public ProfilPerusahaan getProfilPerusahaanByUserId(int userId) {
        String query = "SELECT p.*, u.nama " +
                      "FROM perusahaan p " +
                      "JOIN user u ON p.user_id = u.user_id " +
                      "WHERE p.user_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            LOGGER.info("Mencoba mengambil profil perusahaan untuk user_id: " + userId);
            
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    LOGGER.info("Data ditemukan untuk user_id: " + userId);
                    return mapResultSetToProfilPerusahaan(rs);
                } else {
                    LOGGER.info("Tidak ada data profil perusahaan untuk user_id: " + userId);
                    return null; // Return null jika tidak ada data
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQLException details:", e);
            LOGGER.severe("Error Code: " + e.getErrorCode());
            LOGGER.severe("SQL State: " + e.getSQLState());
            LOGGER.severe("Message: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * INSERT profil perusahaan baru
     * Method terpisah untuk insert (dipanggil dari Profile.java)
     */
    public boolean insertProfilPerusahaan(ProfilPerusahaan profil) {
        String query = "INSERT INTO perusahaan (user_id, jenis_industri, alamat, " +
                      "email_kontak, website_resmi, jumlah_koneksi, jumlah_karyawan, " +
                      "deskripsi_perusahaan, kategori_produk_layanan, informasi_kontak_lainnya, " +
                      "deskripsi, website) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            LOGGER.info("Mencoba insert profil perusahaan baru untuk user_id: " + profil.getUser_id());
            
            setStatementParameters(stmt, profil);
            
            int affectedRows = stmt.executeUpdate();
            boolean success = affectedRows > 0;
            
            if (success) {
                LOGGER.info("Profil berhasil diinsert untuk user_id: " + profil.getUser_id());
            } else {
                LOGGER.warning("Tidak ada baris yang terpengaruh untuk user_id: " + profil.getUser_id());
            }
            
            return success;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQLException saat insert:", e);
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * UPDATE profil perusahaan yang sudah ada
     * Method terpisah untuk update (dipanggil dari Profile.java)
     */
    public boolean updateProfilPerusahaan(ProfilPerusahaan profil) {
        String query = "UPDATE perusahaan SET " +
                      "jenis_industri = ?, alamat = ?, email_kontak = ?, website_resmi = ?, " +
                      "jumlah_koneksi = ?, jumlah_karyawan = ?, deskripsi_perusahaan = ?, " +
                      "kategori_produk_layanan = ?, informasi_kontak_lainnya = ?, " +
                      "deskripsi = ?, website = ? " +
                      "WHERE user_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            LOGGER.info("Mencoba update profil perusahaan untuk user_id: " + profil.getUser_id());
            
            // Set parameters (tanpa user_id di awal)
            stmt.setString(1, profil.getJenis_industri());
            stmt.setString(2, profil.getAlamat());
            stmt.setString(3, profil.getEmail_kontak());
            stmt.setString(4, profil.getWebsite_resmi());
            stmt.setInt(5, profil.getJumlah_koneksi());
            stmt.setInt(6, profil.getJumlah_karyawan());
            stmt.setString(7, profil.getDeskripsi_perusahaan());
            stmt.setString(8, profil.getKategori_produk_layanan());
            stmt.setString(9, profil.getInformasi_kontak_lainnya());
            stmt.setString(10, profil.getDeskripsi());
            stmt.setString(11, profil.getWebsite());
            stmt.setInt(12, profil.getUser_id()); // WHERE clause
            
            int affectedRows = stmt.executeUpdate();
            boolean success = affectedRows > 0;
            
            if (success) {
                LOGGER.info("Profil berhasil diupdate untuk user_id: " + profil.getUser_id());
            } else {
                LOGGER.warning("Tidak ada baris yang terpengaruh untuk user_id: " + profil.getUser_id());
            }
            
            return success;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQLException saat update:", e);
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Helper method untuk set parameter statement (untuk insert)
     */
    private void setStatementParameters(PreparedStatement stmt, ProfilPerusahaan profil) throws SQLException {
        stmt.setInt(1, profil.getUser_id());
        stmt.setString(2, profil.getJenis_industri());
        stmt.setString(3, profil.getAlamat());
        stmt.setString(4, profil.getEmail_kontak());
        stmt.setString(5, profil.getWebsite_resmi());
        stmt.setInt(6, profil.getJumlah_koneksi());
        stmt.setInt(7, profil.getJumlah_karyawan());
        stmt.setString(8, profil.getDeskripsi_perusahaan());
        stmt.setString(9, profil.getKategori_produk_layanan());
        stmt.setString(10, profil.getInformasi_kontak_lainnya());
        stmt.setString(11, profil.getDeskripsi());
        stmt.setString(12, profil.getWebsite());
    }
    
    /**
     * Menyimpan atau update profil perusahaan (INSERT ON DUPLICATE KEY UPDATE)
     * Method ini tetap ada untuk kompatibilitas
     */
    public boolean updateOrInsertProfile(ProfilPerusahaan profil) {
        String query = "INSERT INTO perusahaan (user_id, jenis_industri, alamat, " +
                      "email_kontak, website_resmi, jumlah_koneksi, jumlah_karyawan, " +
                      "deskripsi_perusahaan, kategori_produk_layanan, informasi_kontak_lainnya, " +
                      "deskripsi, website) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                      "ON DUPLICATE KEY UPDATE " +
                      "jenis_industri = VALUES(jenis_industri), " +
                      "alamat = VALUES(alamat), " +
                      "email_kontak = VALUES(email_kontak), " +
                      "website_resmi = VALUES(website_resmi), " +
                      "jumlah_koneksi = VALUES(jumlah_koneksi), " +
                      "jumlah_karyawan = VALUES(jumlah_karyawan), " +
                      "deskripsi_perusahaan = VALUES(deskripsi_perusahaan), " +
                      "kategori_produk_layanan = VALUES(kategori_produk_layanan), " +
                      "informasi_kontak_lainnya = VALUES(informasi_kontak_lainnya), " +
                      "deskripsi = VALUES(deskripsi), " +
                      "website = VALUES(website)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            LOGGER.info("Mencoba menyimpan/update profil perusahaan untuk user_id: " + profil.getUser_id());
            
            setStatementParameters(stmt, profil);
            
            int affectedRows = stmt.executeUpdate();
            boolean success = affectedRows > 0;
            
            if (success) {
                LOGGER.info("Profil berhasil disimpan/diupdate untuk user_id: " + profil.getUser_id());
            } else {
                LOGGER.warning("Tidak ada baris yang terpengaruh untuk user_id: " + profil.getUser_id());
            }
            
            return success;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQLException saat insert/update:", e);
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Mendapatkan profil perusahaan berdasarkan perusahaan_id
     */
    public ProfilPerusahaan getProfilPerusahaanById(int perusahaanId) {
        String query = "SELECT p.*, u.nama " +
                      "FROM perusahaan p " +
                      "JOIN user u ON p.user_id = u.user_id " +
                      "WHERE p.perusahaan_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, perusahaanId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProfilPerusahaan(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error mengambil profil perusahaan dengan ID: " + perusahaanId, e);
        }
        
        return null;
    }
    
    /**
     * Mendapatkan semua profil perusahaan
     */
    public List<ProfilPerusahaan> getAllProfilPerusahaan() {
        String query = "SELECT p.*, u.nama " +
                      "FROM perusahaan p " +
                      "JOIN user u ON p.user_id = u.user_id " +
                      "ORDER BY p.perusahaan_id";
        
        List<ProfilPerusahaan> profilList = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                ProfilPerusahaan profil = mapResultSetToProfilPerusahaan(rs);
                profilList.add(profil);
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error mengambil semua profil perusahaan", e);
        }
        
        return profilList;
    }
    
    /**
     * Helper method untuk mapping ResultSet ke ProfilPerusahaan object
     * Menggunakan JOIN dengan tabel user
     */
    private ProfilPerusahaan mapResultSetToProfilPerusahaan(ResultSet rs) throws SQLException {
        ProfilPerusahaan profil = new ProfilPerusahaan();
        
        try {
            // Data dari tabel perusahaan
            profil.setPerusahaan_id(rs.getInt("perusahaan_id"));
            profil.setUser_id(rs.getInt("user_id"));
            
            // Data dari JOIN dengan tabel user
            profil.setNama(rs.getString("nama"));
            
            // Data lainnya dari tabel perusahaan
            profil.setJenis_industri(rs.getString("jenis_industri"));
            profil.setAlamat(rs.getString("alamat"));
            profil.setEmail_kontak(rs.getString("email_kontak"));
            profil.setWebsite_resmi(rs.getString("website_resmi"));
            profil.setJumlah_koneksi(rs.getInt("jumlah_koneksi"));
            profil.setJumlah_karyawan(rs.getInt("jumlah_karyawan"));
            profil.setDeskripsi_perusahaan(rs.getString("deskripsi_perusahaan"));
            profil.setKategori_produk_layanan(rs.getString("kategori_produk_layanan"));
            profil.setInformasi_kontak_lainnya(rs.getString("informasi_kontak_lainnya"));
            profil.setDeskripsi(rs.getString("deskripsi"));
            profil.setWebsite(rs.getString("website"));
            
            LOGGER.info("Successfully mapped profile for user_id: " + profil.getUser_id() + 
                       " with nama: " + profil.getNama());
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error mapping ResultSet:", e);
            throw e;
        }
        
        return profil;
    }
    
    /**
     * Mengecek apakah user sudah memiliki profil perusahaan
     */
    public boolean hasProfile(int userId) {
        String query = "SELECT COUNT(*) FROM perusahaan WHERE user_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error mengecek profil untuk user_id: " + userId, e);
        }
        
        return false;
    }
    
    // =====================================================================
    // METODE-METODE UPDATE SPESIFIK (sama seperti ProfileDAO)
    // =====================================================================

    public boolean updateJenisIndustri(int userId, String jenisIndustri) {
        String query = "UPDATE perusahaan SET jenis_industri = ? WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, jenisIndustri);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating jenis industri", e);
            return false;
        }
    }

    public boolean updateAlamat(int userId, String alamat) {
        String query = "UPDATE perusahaan SET alamat = ? WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, alamat);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating alamat", e);
            return false;
        }
    }

    public boolean updateEmailKontak(int userId, String emailKontak) {
        String query = "UPDATE perusahaan SET email_kontak = ? WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, emailKontak);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating email kontak", e);
            return false;
        }
    }

    public boolean updateWebsiteResmi(int userId, String websiteResmi) {
        String query = "UPDATE perusahaan SET website_resmi = ? WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, websiteResmi);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating website resmi", e);
            return false;
        }
    }

    public boolean updateDeskripsiPerusahaan(int userId, String deskripsi) {
        String query = "UPDATE perusahaan SET deskripsi_perusahaan = ? WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, deskripsi);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating deskripsi perusahaan", e);
            return false;
        }
    }
    
    /**
     * Method untuk menghapus profil perusahaan (jika diperlukan)
     */
    public boolean deleteProfilPerusahaan(int userId) {
        String query = "DELETE FROM perusahaan WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting profil perusahaan", e);
            return false;
        }
    }
}
