package Database;

import java.sql.Connection;
import java.sql.DriverManager;
<<<<<<< HEAD
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
=======
import java.sql.PreparedStatement;
>>>>>>> abb5e4277fd4437f8f1950800357337ec45de54b

public class DatabaseConnection {
    private static final Logger LOGGER = Logger.getLogger(DatabaseConnection.class.getName());
    private static DatabaseConnection instance;
    private Connection connection;

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

<<<<<<< HEAD
    // Metode untuk mendapatkan koneksi
   

    // Metode untuk menutup koneksi
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                LOGGER.info("Koneksi database ditutup");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Gagal menutup koneksi database", e);
        }
    }
}
=======
    public void execute(String sql, String... params) {
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setString(i + 1, params[i]);
            }
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error executing prepared query: " + e.getMessage());
        }
    }
}
>>>>>>> abb5e4277fd4437f8f1950800357337ec45de54b
