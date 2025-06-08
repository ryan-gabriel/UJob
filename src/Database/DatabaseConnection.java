package Database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection con;

    public DatabaseConnection() {
        try {
            String id = "root";
            String pass = "";
            String url = "jdbc:mysql://localhost:3306/ujob?useSSL=false&serverTimezone=UTC";
            String driver = "com.mysql.cj.jdbc.Driver";

            Class.forName(driver);
            con = DriverManager.getConnection(url, id, pass);
            System.out.println("Koneksi berhasil");
        } catch (Exception e) {
            System.out.println("Koneksi gagal: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return con;
    }
}