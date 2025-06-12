package Database;

import Models.Lowongan;
import Perusahaan.Dashboard.InfoPerusahaan;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KelolaLowonganDAO {
    private final Connection connection;
    
    public KelolaLowonganDAO(Connection connection) {
        this.connection = connection;
    }

    //======================================================================
    // 1. INNER CLASS & METHOD UNTUK DASHBOARD (STATUS PROFILE)
    //======================================================================

    /**
     * Inner class untuk menampung status kelengkapan profil perusahaan.
     */
    public static class ProfileStatus {
        public boolean infoDasarComplete;
        public boolean deskripsiComplete;
        public boolean kontakWebsiteComplete;
        public boolean lowonganPertamaComplete;

        public int getCompletionPercentage() {
            int completedItems = 0;
            if (infoDasarComplete) completedItems++;
            if (deskripsiComplete) completedItems++;
            if (kontakWebsiteComplete) completedItems++;
            if (lowonganPertamaComplete) completedItems++;
            return (completedItems * 100) / 4;
        }
    }

    /**
     * Mengambil status kelengkapan profil perusahaan dari database.
     * @param userId ID dari user perusahaan.
     * @return Objek ProfileStatus yang berisi data kelengkapan.
     */
    public ProfileStatus getProfileStatus(int userId) {
        ProfileStatus status = new ProfileStatus();
        String sqlPerusahaan = "SELECT alamat, jenis_industri, deskripsi_perusahaan, email_kontak, website_resmi FROM perusahaan WHERE user_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sqlPerusahaan)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String alamat = rs.getString("alamat");
                    String industri = rs.getString("jenis_industri");
                    String deskripsi = rs.getString("deskripsi_perusahaan");
                    String email = rs.getString("email_kontak");
                    String web = rs.getString("website_resmi");

                    status.infoDasarComplete = (alamat != null && !alamat.trim().isEmpty() && industri != null && !industri.trim().isEmpty());
                    status.deskripsiComplete = (deskripsi != null && !deskripsi.trim().isEmpty());
                    status.kontakWebsiteComplete = (email != null && !email.trim().isEmpty() && web != null && !web.trim().isEmpty());
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saat cek profil perusahaan: " + e.getMessage());
        }

        // Cek lowongan pertama berdasarkan user_id
        String sqlLowongan = "SELECT COUNT(*) FROM lowongan WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sqlLowongan)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    status.lowonganPertamaComplete = rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saat cek lowongan pertama: " + e.getMessage());
        }
        
        return status;
    }
    
    /**
     * Mengambil informasi ringkas sebuah perusahaan berdasarkan user ID.
     */
    public InfoPerusahaan getInfoPerusahaanByUser(int userId) {
        String sql = "SELECT u.nama, p.jenis_industri, p.jumlah_karyawan, p.deskripsi_perusahaan " +
                    "FROM user u JOIN perusahaan p ON u.user_id = p.user_id " +
                    "WHERE u.user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    InfoPerusahaan info = new InfoPerusahaan();
                    info.setNama(rs.getString("nama"));
                    info.setIndustri(rs.getString("jenis_industri"));
                    info.setJumlahKaryawan(rs.getInt("jumlah_karyawan"));
                    String deskripsi = rs.getString("deskripsi_perusahaan");
                    if (deskripsi != null && deskripsi.length() > 100) {
                        info.setDeskripsi(deskripsi.substring(0, 100) + "...");
                    } else {
                        info.setDeskripsi(deskripsi);
                    }
                    return info;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil info perusahaan: " + e.getMessage());
        }
        return null;
    }

    //======================================================================
    // 2. CRUD OPERATIONS - CREATE, READ, UPDATE, DELETE
    //======================================================================

    /**
     * CREATE - Menambah lowongan baru
     */
    public boolean tambahLowongan(Lowongan lowongan) {
        String sql = "INSERT INTO lowongan (user_id, judul_pekerjaan, deskripsi, kualifikasi, " +
                    "tanggal_posting, status, tanggal_deadline, jenis_lowongan, gaji, lokasi_kerja, " +
                    "durasi, jumlah_lamaran) VALUES (?, ?, ?, ?, CURDATE(), ?, ?, ?, ?, ?, ?, 0)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, lowongan.getUserId());
            stmt.setString(2, lowongan.getJudulPekerjaan());
            stmt.setString(3, lowongan.getDeskripsi());
            stmt.setString(4, lowongan.getKualifikasi());
            stmt.setString(5, lowongan.getStatus());
            stmt.setDate(6, lowongan.getTanggalDeadline());
            stmt.setString(7, lowongan.getJenisLowongan());
            stmt.setString(8, lowongan.getGaji());
            stmt.setString(9, lowongan.getLokasiKerja());
            stmt.setString(10, lowongan.getDurasi());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error saat menambah lowongan: " + e.getMessage());
            return false;
        }
    }

    /**
     * READ - Mendapatkan semua lowongan dengan nama perusahaan
     */
    public List<Lowongan> getAllLowongan() {
        List<Lowongan> lowonganList = new ArrayList<>();
        String sql = "SELECT l.*, u.nama as nama_perusahaan " +
                    "FROM lowongan l " +
                    "JOIN user u ON l.user_id = u.user_id " +
                    "WHERE u.role = 'perusahaan' " +
                    "ORDER BY l.tanggal_posting DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Lowongan lowongan = mapResultSetToLowongan(rs);
                lowonganList.add(lowongan);
            }
            
        } catch (SQLException e) {
            System.err.println("Error saat mengambil semua lowongan: " + e.getMessage());
        }
        
        return lowonganList;
    }

    /**
     * READ - Mendapatkan lowongan berdasarkan ID
     */
    public Lowongan getLowonganById(int lowonganId) {
        String sql = "SELECT l.*, u.nama as nama_perusahaan " +
                    "FROM lowongan l " +
                    "JOIN user u ON l.user_id = u.user_id " +
                    "WHERE l.lowongan_id = ? AND u.role = 'perusahaan'";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, lowonganId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToLowongan(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error saat mengambil lowongan by ID: " + e.getMessage());
        }
        
        return null;
    }

    /**
     * READ - Mendapatkan lowongan berdasarkan user ID (perusahaan) dengan jumlah lamaran real
     */
    public List<Lowongan> getLowonganByUserId(int userId) {
        List<Lowongan> lowonganList = new ArrayList<>();
        String sql = "SELECT l.*, u.nama as nama_perusahaan, " +
                     "(SELECT COUNT(*) FROM lamaran la WHERE la.lowongan_id = l.lowongan_id) as real_jumlah_lamaran " +
                     "FROM lowongan l " +
                     "JOIN user u ON l.user_id = u.user_id " +
                     "WHERE l.user_id = ? AND u.role = 'perusahaan' " +
                     "ORDER BY l.tanggal_posting DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Lowongan lowongan = mapResultSetToLowongan(rs);
                    lowongan.setJumlahLamaran(rs.getInt("real_jumlah_lamaran"));
                    lowonganList.add(lowongan);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error saat mengambil lowongan by user ID: " + e.getMessage());
        }
        
        return lowonganList;
    }

    /**
     * READ - Mendapatkan lowongan aktif saja
     */
    public List<Lowongan> getLowonganAktif() {
        List<Lowongan> lowonganList = new ArrayList<>();
        String sql = "SELECT l.*, u.nama as nama_perusahaan " +
                    "FROM lowongan l " +
                    "JOIN user u ON l.user_id = u.user_id " +
                    "WHERE l.status = 'aktif' AND l.tanggal_deadline >= CURDATE() " +
                    "AND u.role = 'perusahaan' " +
                    "ORDER BY l.tanggal_posting DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Lowongan lowongan = mapResultSetToLowongan(rs);
                lowonganList.add(lowongan);
            }
            
        } catch (SQLException e) {
            System.err.println("Error saat mengambil lowongan aktif: " + e.getMessage());
        }
        
        return lowonganList;
    }

    /**
     * READ - Search lowongan berdasarkan keyword
     */
    public List<Lowongan> searchLowongan(String keyword) {
        List<Lowongan> lowonganList = new ArrayList<>();
        String sql = "SELECT l.*, u.nama as nama_perusahaan " +
                    "FROM lowongan l " +
                    "JOIN user u ON l.user_id = u.user_id " +
                    "WHERE (l.judul_pekerjaan LIKE ? OR l.deskripsi LIKE ? OR " +
                    "l.lokasi_kerja LIKE ? OR u.nama LIKE ?) " +
                    "AND l.status = 'aktif' AND u.role = 'perusahaan' " +
                    "ORDER BY l.tanggal_posting DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            stmt.setString(4, searchPattern);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Lowongan lowongan = mapResultSetToLowongan(rs);
                    lowonganList.add(lowongan);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error saat search lowongan: " + e.getMessage());
        }
        
        return lowonganList;
    }

    /**
     * READ - Search lowongan berdasarkan keyword dan user ID dengan jumlah lamaran real
     */
    public List<Lowongan> searchLowonganByUser(String keyword, int userId) {
        List<Lowongan> results = new ArrayList<>();
        String sql = "SELECT l.*, u.nama as nama_perusahaan, " +
                     "(SELECT COUNT(*) FROM lamaran la WHERE la.lowongan_id = l.lowongan_id) as real_jumlah_lamaran " +
                     "FROM lowongan l " +
                     "JOIN user u ON l.user_id = u.user_id " +
                     "WHERE l.user_id = ? AND u.role = 'perusahaan' AND " +
                     "(l.judul_pekerjaan LIKE ? OR l.deskripsi LIKE ? OR u.nama LIKE ?) " +
                     "ORDER BY l.tanggal_posting DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            String searchPattern = "%" + keyword + "%";
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            stmt.setString(4, searchPattern);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Lowongan lowongan = mapResultSetToLowongan(rs);
                lowongan.setJumlahLamaran(rs.getInt("real_jumlah_lamaran"));
                results.add(lowongan);
            }
        } catch (SQLException e) {
            System.err.println("Error saat search lowongan by user: " + e.getMessage());
        }
        return results;
    }

    /**
     * UPDATE - Mengupdate lowongan
     */
    public boolean updateLowongan(Lowongan lowongan) {
        String sql = "UPDATE lowongan SET judul_pekerjaan = ?, deskripsi = ?, kualifikasi = ?, " +
                    "status = ?, tanggal_deadline = ?, jenis_lowongan = ?, gaji = ?, " +
                    "lokasi_kerja = ?, durasi = ? WHERE lowongan_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, lowongan.getJudulPekerjaan());
            stmt.setString(2, lowongan.getDeskripsi());
            stmt.setString(3, lowongan.getKualifikasi());
            stmt.setString(4, lowongan.getStatus());
            stmt.setDate(5, lowongan.getTanggalDeadline());
            stmt.setString(6, lowongan.getJenisLowongan());
            stmt.setString(7, lowongan.getGaji());
            stmt.setString(8, lowongan.getLokasiKerja());
            stmt.setString(9, lowongan.getDurasi());
            stmt.setInt(10, lowongan.getLowonganId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error saat update lowongan: " + e.getMessage());
            return false;
        }
    }

    /**
     * UPDATE - Mengupdate status lowongan
     */
    public boolean updateStatusLowongan(int lowonganId, String status) {
        String sql = "UPDATE lowongan SET status = ? WHERE lowongan_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, lowonganId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error saat update status lowongan: " + e.getMessage());
            return false;
        }
    }

    /**
     * UPDATE - Increment jumlah lamaran
     */
    public boolean incrementJumlahLamaran(int lowonganId) {
        String sql = "UPDATE lowongan SET jumlah_lamaran = jumlah_lamaran + 1 WHERE lowongan_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, lowonganId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error saat increment jumlah lamaran: " + e.getMessage());
            return false;
        }
    }

    /**
     * DELETE - Menghapus lowongan
     */
    public boolean deleteLowongan(int lowonganId) {
        String sql = "DELETE FROM lowongan WHERE lowongan_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, lowonganId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error saat delete lowongan: " + e.getMessage());
            return false;
        }
    }

    //======================================================================
    // 3. UTILITY METHODS - STATISTICS & COUNTING
    //======================================================================

    /**
     * UTILITY - Count total lowongan
     */
    public int getTotalLowongan() {
        String sql = "SELECT COUNT(*) FROM lowongan l " +
                    "JOIN user u ON l.user_id = u.user_id " +
                    "WHERE u.role = 'perusahaan'";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error saat count total lowongan: " + e.getMessage());
        }
        
        return 0;
    }

    /**
     * UTILITY - Count lowongan aktif
     */
    public int getTotalLowonganAktif() {
        String sql = "SELECT COUNT(*) FROM lowongan l " +
                    "JOIN user u ON l.user_id = u.user_id " +
                    "WHERE l.status = 'aktif' AND l.tanggal_deadline >= CURDATE() " +
                    "AND u.role = 'perusahaan'";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error saat count lowongan aktif: " + e.getMessage());
        }
        
        return 0;
    }

    /**
     * UTILITY - Count lowongan by user (perusahaan)
     */
    public int getTotalLowonganByUser(int userId) {
        String sql = "SELECT COUNT(*) FROM lowongan l " +
                    "JOIN user u ON l.user_id = u.user_id " +
                    "WHERE l.user_id = ? AND u.role = 'perusahaan'";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error saat count lowongan by user: " + e.getMessage());
        }
        
        return 0;
    }

    /**
     * UTILITY - Count lowongan aktif berdasarkan user ID
     */
    public int getTotalLowonganAktifByUser(int userId) {
        String sql = "SELECT COUNT(*) FROM lowongan l " +
                    "JOIN user u ON l.user_id = u.user_id " +
                    "WHERE l.user_id = ? AND l.status = 'aktif' AND u.role = 'perusahaan'";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saat count lowongan aktif by user: " + e.getMessage());
        }
        return 0;
    }

    /**
     * UTILITY - Count total lamaran by user
     */
    public int getTotalLamaranByUser(int userId) {
        String sql = "SELECT COUNT(l.lamaran_id) FROM lamaran l " +
                     "JOIN lowongan lo ON l.lowongan_id = lo.lowongan_id " +
                     "WHERE lo.user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saat menghitung total lamaran by user: " + e.getMessage());
        }
        return 0;
    }

    //======================================================================
    // 4. HELPER METHODS (PRIVATE)
    //======================================================================
    
    /**
     * UTILITY - Method untuk mapping ResultSet ke Lowongan object
     */
    private Lowongan mapResultSetToLowongan(ResultSet rs) throws SQLException {
        Lowongan lowongan = new Lowongan();
        
        lowongan.setLowonganId(rs.getInt("lowongan_id"));
        lowongan.setUserId(rs.getInt("user_id"));
        lowongan.setJudulPekerjaan(rs.getString("judul_pekerjaan"));
        lowongan.setDeskripsi(rs.getString("deskripsi"));
        lowongan.setKualifikasi(rs.getString("kualifikasi"));
        lowongan.setTanggalPosting(rs.getDate("tanggal_posting"));
        lowongan.setStatus(rs.getString("status"));
        lowongan.setTanggalDeadline(rs.getDate("tanggal_deadline"));
        lowongan.setJenisLowongan(rs.getString("jenis_lowongan"));
        lowongan.setGaji(rs.getString("gaji"));
        lowongan.setLokasiKerja(rs.getString("lokasi_kerja"));
        lowongan.setDurasi(rs.getString("durasi"));
        lowongan.setJumlahLamaran(rs.getInt("jumlah_lamaran"));
        
        if (hasColumn(rs, "nama_perusahaan")) {
            lowongan.setNamaPerusahaan(rs.getString("nama_perusahaan"));
        }
        
        return lowongan;
    }

    /**
     * Helper method untuk mengecek apakah kolom ada di ResultSet
     */
    private boolean hasColumn(ResultSet rs, String columnName) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columns = rsmd.getColumnCount();
        for (int x = 1; x <= columns; x++) {
            if (columnName.equals(rsmd.getColumnName(x))) {
                return true;
            }
        }
        return false;
    }
}
