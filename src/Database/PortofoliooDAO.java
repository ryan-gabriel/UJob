package Database;

import Models.Portofolioo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PortofoliooDAO {
    private Connection connection;
    
    public PortofoliooDAO() {
        try {
            this.connection = DatabaseConnection.getConnection();
            System.out.println("Koneksi database berhasil!"); // Debugging line
        } catch (Exception e) {
            System.err.println("Error connecting to database: " + e.getMessage());
            e.printStackTrace();
            System.out.println("Gagal koneksi database!"); // Debugging line
        }
    }
    
    /**
     * Insert portofolio baru
     */
    public boolean insertPortofolio(Portofolioo portofolio) {
        String sql = "INSERT INTO portofolio (user_id, judul, jenis, deskripsi, link, tanggal_upload) VALUES (?, ?, ?, ?, ?, NOW())";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            int currentUserId = getCurrentUserId(); // Get hardcoded user ID
            
            stmt.setInt(1, currentUserId);
            stmt.setString(2, portofolio.getJudul());
            stmt.setString(3, portofolio.getJenis());
            stmt.setString(4, portofolio.getDeskripsi());
            stmt.setString(5, portofolio.getLink());
            
            int rowsAffected = stmt.executeUpdate();
            System.out.println("Rows affected by insert: " + rowsAffected); // Debugging line
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error inserting portfolio: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Update portofolio
     */
    public boolean updatePortofolio(Portofolioo portofolio) {
        String sql = "UPDATE portofolio SET judul = ?, jenis = ?, deskripsi = ?, link = ? WHERE porto_id = ? AND user_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, portofolio.getJudul());
            stmt.setString(2, portofolio.getJenis());
            stmt.setString(3, portofolio.getDeskripsi());
            stmt.setString(4, portofolio.getLink());
            stmt.setInt(5, portofolio.getId());
            stmt.setInt(6, getCurrentUserId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating portfolio: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Delete portofolio
     */
    public boolean deletePortofolio(int id) {
        String sql = "DELETE FROM portofolio WHERE porto_id = ? AND user_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setInt(2, getCurrentUserId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting portfolio: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get portofolio by ID
     */
    public Portofolioo getPortofolioById(int id) {
        String sql = "SELECT * FROM portofolio WHERE porto_id = ? AND user_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setInt(2, getCurrentUserId());
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToPortofolio(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting portfolio by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Get all portofolio untuk user yang sedang login
     */
    public List<Portofolioo> getAllPortofolioByCurrentUser() {
        List<Portofolioo> portofolioList = new ArrayList<>();
        String sql = "SELECT * FROM portofolio WHERE user_id = ? ORDER BY tanggal_upload DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, getCurrentUserId()); // Using hardcoded user ID
            System.out.println("Executing SQL: " + sql + " with userId: " + getCurrentUserId()); // Debugging line
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Portofolioo portofolio = mapResultSetToPortofolio(rs);
                portofolioList.add(portofolio);
            }
            System.out.println("Number of portfolios found: " + portofolioList.size()); // Debugging line
        } catch (SQLException e) {
            System.err.println("Error getting all portfolio: " + e.getMessage());
            e.printStackTrace();
        }
        
        return portofolioList;
    }
    
    /**
     * Get all portofolio by user ID
     */
    public List<Portofolioo> getAllPortofolioByUserId(int userId) {
        List<Portofolioo> portofolioList = new ArrayList<>();
        String sql = "SELECT * FROM portofolio WHERE user_id = ? ORDER BY tanggal_upload DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Portofolioo portofolio = mapResultSetToPortofolio(rs);
                portofolioList.add(portofolio);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting portfolio by user ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return portofolioList;
    }
    
    /**
     * Get portofolio by jenis
     */
    public List<Portofolioo> getPortofolioByJenis(String jenis) {
        List<Portofolioo> portofolioList = new ArrayList<>();
        String sql = "SELECT * FROM portofolio WHERE user_id = ? AND jenis = ? ORDER BY tanggal_upload DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, getCurrentUserId());
            stmt.setString(2, jenis);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Portofolioo portofolio = mapResultSetToPortofolio(rs);
                portofolioList.add(portofolio);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting portfolio by jenis: " + e.getMessage());
            e.printStackTrace();
        }
        
        return portofolioList;
    }
    
    /**
     * Search portofolio
     */
    public List<Portofolioo> searchPortofolio(String keyword) {
        List<Portofolioo> portofolioList = new ArrayList<>();
        String sql = "SELECT * FROM portofolio WHERE user_id = ? AND (judul LIKE ? OR deskripsi LIKE ? OR jenis LIKE ?) ORDER BY tanggal_upload DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String searchPattern = "%" + keyword + "%";
            stmt.setInt(1, getCurrentUserId());
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            stmt.setString(4, searchPattern);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Portofolioo portofolio = mapResultSetToPortofolio(rs);
                portofolioList.add(portofolio);
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching portfolio: " + e.getMessage());
            e.printStackTrace();
        }
        
        return portofolioList;
    }
    
    /**
     * Get count portofolio by user
     */
    public int getPortofolioCount() {
        String sql = "SELECT COUNT(*) FROM portofolio WHERE user_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, getCurrentUserId());
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting portfolio count: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Get count portofolio by jenis
     */
    public int getPortofolioCountByJenis(String jenis) {
        String sql = "SELECT COUNT(*) FROM portofolio WHERE user_id = ? AND jenis = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, getCurrentUserId());
            stmt.setString(2, jenis);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting portfolio count by jenis: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Helper method untuk mapping ResultSet ke Portofolioo object
     */
    private Portofolioo mapResultSetToPortofolio(ResultSet rs) throws SQLException {
        Portofolioo portofolio = new Portofolioo();
        portofolio.setId(rs.getInt("porto_id"));
        portofolio.setUserId(rs.getInt("user_id"));
        portofolio.setJudul(rs.getString("judul"));
        portofolio.setJenis(rs.getString("jenis"));
        portofolio.setDeskripsi(rs.getString("deskripsi"));
        portofolio.setLink(rs.getString("link"));
        portofolio.setTanggalUpload(rs.getTimestamp("tanggal_upload"));
        
        return portofolio;
    }
    
    /**
     * Helper method untuk mendapatkan user ID yang sedang login
     * TODO: Implementasi session management yang proper
     */
    private int getCurrentUserId() {
        // Sementara hardcode ke 1.
        // Nanti ganti dengan session management yang proper
        return 1;
    }
    
    /**
     * Close database connection
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
            e.printStackTrace();
        }
    }
}