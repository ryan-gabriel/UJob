package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnection {
    private static final Logger LOGGER = Logger.getLogger(DatabaseConnection.class.getName());
    private static DatabaseConnection instance;
    private Connection connection;

    // Konstruktor dibuat package-private untuk Singleton pattern
    DatabaseConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/ujob?useSSL=false&serverTimezone=UTC";
            String username = "root";
            String password = "";
            String driver = "com.mysql.cj.jdbc.Driver";

            Class.forName(driver);
            this.connection = DriverManager.getConnection(url, username, password);
            LOGGER.info("Koneksi database berhasil");
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Driver database tidak ditemukan", e);
            throw new RuntimeException("Driver database tidak ditemukan", e);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Gagal membuat koneksi database", e);
            throw new RuntimeException("Gagal membuat koneksi database", e);
        }
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public static Connection getConnection() {
        return getInstance().connection;
    }

    public void execute(String sql, String... params) {
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setString(i + 1, params[i]);
            }
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error executing prepared query: " + e.getMessage());
        }
    }
}