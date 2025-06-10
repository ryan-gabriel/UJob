package Database;

import Database.DatabaseConnection;
import Models.ProfilPerusahaan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProfilePerusahaanDAO {
    
    private static final Logger LOGGER = Logger.getLogger(ProfilePerusahaanDAO.class.getName());

    // Method untuk mengambil profil berdasarkan user_id
    public ProfilPerusahaan getProfilPerusahaanByUserId(int userId) {
        ProfilPerusahaan profilPerusahaan = null;
        
        String query = "SELECT * FROM profil_perusahaan WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                profilPerusahaan = mapResultSetToProfilPerusahaan(rs);
                LOGGER.info("Profil perusahaan ditemukan untuk user_id: " + userId);
            } else {
                LOGGER.info("Profil perusahaan tidak ditemukan untuk user_id: " + userId);
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error mengambil profil perusahaan untuk user_id: " + userId, e);
        }
        
        return profilPerusahaan;
    }

    // Method untuk mengambil profil berdasarkan perusahaan_id
    public ProfilPerusahaan getProfilPerusahaanById(int perusahaanId) {
        ProfilPerusahaan profilPerusahaan = null;
        
        String query = "SELECT * FROM profil_perusahaan WHERE perusahaan_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, perusahaanId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                profilPerusahaan = mapResultSetToProfilPerusahaan(rs);
                LOGGER.info("Profil perusahaan ditemukan untuk perusahaan_id: " + perusahaanId);
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error mengambil profil perusahaan untuk perusahaan_id: " + perusahaanId, e);
        }
        
        return profilPerusahaan;
    }

    // Method untuk mengambil semua profil perusahaan
    public List<ProfilPerusahaan> getAllProfilPerusahaan() {
        List<ProfilPerusahaan> daftarProfilPerusahaan = new ArrayList<>();
        
        String query = "SELECT * FROM profil_perusahaan ORDER BY nama ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ProfilPerusahaan profilPerusahaan = mapResultSetToProfilPerusahaan(rs);
                daftarProfilPerusahaan.add(profilPerusahaan);
            }
            
            LOGGER.info("Berhasil mengambil " + daftarProfilPerusahaan.size() + " profil perusahaan");
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error mengambil semua profil perusahaan", e);
        }

        return daftarProfilPerusahaan;
    }

    // Method untuk menambah profil perusahaan baru
    public boolean insertProfilPerusahaan(ProfilPerusahaan profil) {
        if (profil == null || !profil.isValid()) {
            LOGGER.warning("Data profil perusahaan tidak valid");
            return false;
        }

        String query = "INSERT INTO profil_perusahaan (user_id, nama, jenis_industri, alamat, " +
                      "email_kontak, website_resmi, jumlah_koneksi, jumlah_karyawan, " +
                      "deskripsi_perusahaan, kategori_produk_layanan, informasi_kontak_lainnya, " +
                      "deskripsi, website) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            setProfilPerusahaanParameters(stmt, profil);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                // Mendapatkan ID yang baru dibuat
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    profil.setPerusahaan_id(generatedKeys.getInt(1));
                }
                
                LOGGER.info("Profil perusahaan berhasil ditambahkan untuk user_id: " + profil.getUser_id());
                return true;
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error menambah profil perusahaan", e);
        }

        return false;
    }

    // Method untuk mengupdate profil perusahaan
    public boolean updateProfilPerusahaan(ProfilPerusahaan profil) {
        if (profil == null || !profil.isValid() || profil.getPerusahaan_id() <= 0) {
            LOGGER.warning("Data profil perusahaan tidak valid untuk update");
            return false;
        }

        String query = "UPDATE profil_perusahaan SET nama = ?, jenis_industri = ?, alamat = ?, " +
                      "email_kontak = ?, website_resmi = ?, jumlah_koneksi = ?, jumlah_karyawan = ?, " +
                      "deskripsi_perusahaan = ?, kategori_produk_layanan = ?, informasi_kontak_lainnya = ?, " +
                      "deskripsi = ?, website = ? WHERE perusahaan_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            setProfilPerusahaanParameters(stmt, profil);
            stmt.setInt(13, profil.getPerusahaan_id());

            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                LOGGER.info("Profil perusahaan berhasil diupdate untuk perusahaan_id: " + profil.getPerusahaan_id());
                return true;
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error mengupdate profil perusahaan", e);
        }

        return false;
    }

    // Method untuk menghapus profil perusahaan
    public boolean deleteProfilPerusahaan(int perusahaanId) {
        String query = "DELETE FROM profil_perusahaan WHERE perusahaan_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, perusahaanId);
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                LOGGER.info("Profil perusahaan berhasil dihapus untuk perusahaan_id: " + perusahaanId);
                return true;
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error menghapus profil perusahaan", e);
        }

        return false;
    }

    // Method untuk menghapus profil berdasarkan user_id
    public boolean deleteProfilPerusahaanByUserId(int userId) {
        String query = "DELETE FROM profil_perusahaan WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                LOGGER.info("Profil perusahaan berhasil dihapus untuk user_id: " + userId);
                return true;
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error menghapus profil perusahaan untuk user_id: " + userId, e);
        }

        return false;
    }

    // Method untuk mencari profil berdasarkan nama
    public List<ProfilPerusahaan> searchProfilPerusahaanByNama(String nama) {
        List<ProfilPerusahaan> daftarProfilPerusahaan = new ArrayList<>();
        
        String query = "SELECT * FROM profil_perusahaan WHERE nama LIKE ? ORDER BY nama ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, "%" + nama + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ProfilPerusahaan profilPerusahaan = mapResultSetToProfilPerusahaan(rs);
                daftarProfilPerusahaan.add(profilPerusahaan);
            }
            
            LOGGER.info("Ditemukan " + daftarProfilPerusahaan.size() + " profil dengan nama mengandung: " + nama);

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error mencari profil perusahaan berdasarkan nama", e);
        }

        return daftarProfilPerusahaan;
    }

    // Method untuk mencari profil berdasarkan jenis industri
    public List<ProfilPerusahaan> getProfilPerusahaanByJenisIndustri(String jenisIndustri) {
        List<ProfilPerusahaan> daftarProfilPerusahaan = new ArrayList<>();
        
        String query = "SELECT * FROM profil_perusahaan WHERE jenis_industri = ? ORDER BY nama ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, jenisIndustri);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ProfilPerusahaan profilPerusahaan = mapResultSetToProfilPerusahaan(rs);
                daftarProfilPerusahaan.add(profilPerusahaan);
            }
            
            LOGGER.info("Ditemukan " + daftarProfilPerusahaan.size() + " profil dengan jenis industri: " + jenisIndustri);

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error mengambil profil berdasarkan jenis industri", e);
        }

        return daftarProfilPerusahaan;
    }

    // Method untuk mengecek apakah profil sudah ada untuk user tertentu
    public boolean isProfilExists(int userId) {
        String query = "SELECT COUNT(*) FROM profil_perusahaan WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error mengecek keberadaan profil untuk user_id: " + userId, e);
        }
        
        return false;
    }

    // Method untuk mendapatkan jumlah total profil perusahaan
    public int getTotalProfilPerusahaan() {
        String query = "SELECT COUNT(*) FROM profil_perusahaan";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error mendapatkan total profil perusahaan", e);
        }
        
        return 0;
    }

    // Method untuk update jumlah koneksi
    public boolean updateJumlahKoneksi(int perusahaanId, int jumlahKoneksi) {
        String query = "UPDATE profil_perusahaan SET jumlah_koneksi = ? WHERE perusahaan_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, jumlahKoneksi);
            stmt.setInt(2, perusahaanId);

            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                LOGGER.info("Jumlah koneksi berhasil diupdate untuk perusahaan_id: " + perusahaanId);
                return true;
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error mengupdate jumlah koneksi", e);
        }

        return false;
    }

    // Helper method untuk mapping ResultSet ke ProfilPerusahaan
    private ProfilPerusahaan mapResultSetToProfilPerusahaan(ResultSet rs) throws SQLException {
        return new ProfilPerusahaan(
            rs.getInt("perusahaan_id"),
            rs.getInt("user_id"),
            rs.getString("nama"),
            rs.getString("jenis_industri"),
            rs.getString("alamat"),
            rs.getString("email_kontak"),
            rs.getString("website_resmi"),
            rs.getInt("jumlah_koneksi"),
            rs.getInt("jumlah_karyawan"),
            rs.getString("deskripsi_perusahaan"),
            rs.getString("kategori_produk_layanan"),
            rs.getString("informasi_kontak_lainnya"),
            rs.getString("deskripsi"),
            rs.getString("website")
        );
    }

    // Helper method untuk set parameter PreparedStatement
    private void setProfilPerusahaanParameters(PreparedStatement stmt, ProfilPerusahaan profil) throws SQLException {
        stmt.setInt(1, profil.getUser_id());
        stmt.setString(2, profil.getNama());
        stmt.setString(3, profil.getJenis_industri());
        stmt.setString(4, profil.getAlamat());
        stmt.setString(5, profil.getEmail_kontak());
        stmt.setString(6, profil.getWebsite_resmi());
        stmt.setInt(7, profil.getJumlah_koneksi());
        stmt.setInt(8, profil.getJumlah_karyawan());
        stmt.setString(9, profil.getDeskripsi_perusahaan());
        stmt.setString(10, profil.getKategori_produk_layanan());
        stmt.setString(11, profil.getInformasi_kontak_lainnya());
        stmt.setString(12, profil.getDeskripsi());
        stmt.setString(13, profil.getWebsite());
    }

    // Method untuk mendapatkan profil dengan pagination
    public List<ProfilPerusahaan> getProfilPerusahaanWithPagination(int offset, int limit) {
        List<ProfilPerusahaan> daftarProfilPerusahaan = new ArrayList<>();
        
        String query = "SELECT * FROM profil_perusahaan ORDER BY nama ASC LIMIT ? OFFSET ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ProfilPerusahaan profilPerusahaan = mapResultSetToProfilPerusahaan(rs);
                daftarProfilPerusahaan.add(profilPerusahaan);
            }
            
            LOGGER.info("Berhasil mengambil " + daftarProfilPerusahaan.size() + " profil dengan pagination");

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error mengambil profil dengan pagination", e);
        }

        return daftarProfilPerusahaan;
    }
}
