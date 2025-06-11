package Database;

import Models.Lowongan;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KelolaLowonganDAO {
    private Connection connection;
    
    public KelolaLowonganDAO(Connection connection) {
        this.connection = connection;
    }
    
    // CREATE - Menambah lowongan baru
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
    
    // READ - Mendapatkan semua lowongan dengan nama perusahaan
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
    
    // READ - Mendapatkan lowongan berdasarkan ID
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
    
    // READ - Mendapatkan lowongan berdasarkan user ID (perusahaan)
    public List<Lowongan> getLowonganByUserId(int userId) {
        List<Lowongan> lowonganList = new ArrayList<>();
        String sql = "SELECT l.*, u.nama as nama_perusahaan " +
                    "FROM lowongan l " +
                    "JOIN user u ON l.user_id = u.user_id " +
                    "WHERE l.user_id = ? AND u.role = 'perusahaan' " +
                    "ORDER BY l.tanggal_posting DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Lowongan lowongan = mapResultSetToLowongan(rs);
                    lowonganList.add(lowongan);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error saat mengambil lowongan by user ID: " + e.getMessage());
        }
        
        return lowonganList;
    }
    
    // READ - Mendapatkan lowongan aktif saja
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
    
    // READ - Search lowongan berdasarkan keyword
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
    
    // ========== METHOD BARU YANG DITAMBAHKAN ==========
    
    // READ - Search lowongan berdasarkan keyword dan user ID
    public List<Lowongan> searchLowonganByUser(String keyword, int userId) throws SQLException {
        List<Lowongan> results = new ArrayList<>();
        String sql = "SELECT l.*, u.nama as nama_perusahaan " +
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
                results.add(mapResultSetToLowongan(rs));
            }
        }
        return results;
    }
    
    // UTILITY - Count lowongan aktif berdasarkan user ID
    public int getTotalLowonganAktifByUser(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM lowongan l " +
                    "JOIN user u ON l.user_id = u.user_id " +
                    "WHERE l.user_id = ? AND l.status = 'aktif' AND u.role = 'perusahaan'";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
    
    // ========== METHOD LAMA YANG SUDAH ADA ==========
    
    // UPDATE - Mengupdate lowongan
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
    
    // UPDATE - Mengupdate status lowongan
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
    
    // UPDATE - Increment jumlah lamaran
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
    
    // DELETE - Menghapus lowongan
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
    
    // UTILITY - Count total lowongan
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
    
    // UTILITY - Count lowongan aktif
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
    
    // UTILITY - Count lowongan by user (perusahaan)
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
    
    // UTILITY - Method untuk mapping ResultSet ke Lowongan object
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
        lowongan.setNamaPerusahaan(rs.getString("nama_perusahaan"));
        
        return lowongan;
    }
}
