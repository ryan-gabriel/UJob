package Database;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

import Auth.SessionManager;
import Models.Proyek;

/**
 *
 * @author Rian G S
 */
public class koneksi {
    private static koneksi instance;
    public Connection con;

    public koneksi() {
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

    public static koneksi getInstance() {
        if (instance == null) {
            instance = new koneksi();
        }
        return instance;
    }

    public Connection getConnection() {
        return con;
    }

    public void simpanProyek(String judul, String deskripsi, String bidang) {
        String userId = String.valueOf(SessionManager.getInstance().getId());
        try {
            String sql = "INSERT INTO proyek (user_id, judul, deskripsi, bidang, status) VALUES (?, ?, ?, ?, ?)";
            var stmt = con.prepareStatement(sql);
            stmt.setString(1, userId);
            stmt.setString(2, judul);
            stmt.setString(3, deskripsi);
            stmt.setString(4, bidang);
            stmt.setString(5, "aktif");
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Gagal menyimpan proyek: " + e.getMessage());
        }
    }

    public String[] getUser(String userId) {
        String[] data = null;
        try {
            String sql = "SELECT * FROM user WHERE user_id = ?";
            var stmt = con.prepareStatement(sql);
            stmt.setString(1, userId);
            var rs = stmt.executeQuery();

            if (rs.next()) {
                data = new String[3];
                data[0] = rs.getString("user_id");
                data[1] = rs.getString("nama");
                data[2] = rs.getString("email");
            }
        } catch (Exception e) {
            System.out.println("Gagal mengambil data user: " + e.getMessage());
        }
        return data;
    }

    public List<Proyek> getProyek(String userId) {
        List<Proyek> data = new ArrayList<>();
        try {
            String sql = """
                SELECT * FROM proyek p
                WHERE p.user_id != ?
                AND NOT EXISTS (
                    SELECT 1 FROM pengajuan_proyek pp 
                    WHERE pp.proyek_id = p.proyek_id AND pp.user_id = ?
                )
                AND NOT EXISTS (
                    SELECT 1 FROM anggota_proyek ap 
                    WHERE ap.proyek_id = p.proyek_id AND ap.user_id = ?
                )
            """;

            var stmt = con.prepareStatement(sql);
            stmt.setString(1, userId);
            stmt.setString(2, userId);
            stmt.setString(3, userId);

            var rs = stmt.executeQuery();

            while (rs.next()) {
                String proyekId = rs.getString("proyek_id");
                String pemilikId = rs.getString("user_id");
                String namaPemilik = getUser(pemilikId)[1]; // diasumsikan getUser(String id) mengembalikan String[]
                String judul = rs.getString("judul");
                String deskripsi = rs.getString("deskripsi");
                String bidang = rs.getString("bidang");
                String tanggal = rs.getString("tanggal_dibuat");

                data.add(new Proyek(proyekId, pemilikId, namaPemilik, judul, deskripsi, bidang, tanggal));
            }

        } catch (Exception e) {
            System.out.println("Gagal mengambil proyek: " + e.getMessage());
        }

        return data;
    }

    public List<Proyek> getProyekSedangDidaftari(String userId) {
        List<Proyek> data = new ArrayList<>();
        try {
            String sql = """
                SELECT p.* FROM proyek p
                JOIN pengajuan_proyek pp ON p.proyek_id = pp.proyek_id
                WHERE pp.user_id = ?
            """;

            var stmt = con.prepareStatement(sql);
            stmt.setString(1, userId);

            var rs = stmt.executeQuery();
            while (rs.next()) {
                String proyekId = rs.getString("proyek_id");
                String pemilikId = rs.getString("user_id");
                String namaPemilik = getUser(pemilikId)[1];
                String judul = rs.getString("judul");
                String deskripsi = rs.getString("deskripsi");
                String bidang = rs.getString("bidang");
                String tanggal = rs.getString("tanggal_dibuat");

                data.add(new Proyek(proyekId, pemilikId, namaPemilik, judul, deskripsi, bidang, tanggal));
            }
        } catch (Exception e) {
            System.out.println("Gagal mengambil proyek sedang didaftari: " + e.getMessage());
        }

        return data;
    }

    public List<Proyek> getProyekSendiri(String userId) {
        List<Proyek> data = new ArrayList<>();
        try {
            String sql = "SELECT * FROM proyek WHERE user_id = ?";

            var stmt = con.prepareStatement(sql);
            stmt.setString(1, userId);

            var rs = stmt.executeQuery();
            while (rs.next()) {
                String proyekId = rs.getString("proyek_id");
                String namaPemilik = getUser(userId)[1];
                String judul = rs.getString("judul");
                String deskripsi = rs.getString("deskripsi");
                String bidang = rs.getString("bidang");
                String tanggal = rs.getString("tanggal_dibuat");

                data.add(new Proyek(proyekId, userId, namaPemilik, judul, deskripsi, bidang, tanggal));
            }
        } catch (Exception e) {
            System.out.println("Gagal mengambil proyek sendiri: " + e.getMessage());
        }

        return data;
    }

    public List<Proyek> getProyekAnggota(String userId) {
        List<Proyek> data = new ArrayList<>();
        try {
            String sql = """
                SELECT p.* FROM proyek p
                JOIN anggota_proyek ap ON p.proyek_id = ap.proyek_id
                WHERE ap.user_id = ?
            """;

            var stmt = con.prepareStatement(sql);
            stmt.setString(1, userId);

            var rs = stmt.executeQuery();
            while (rs.next()) {
                String proyekId = rs.getString("proyek_id");
                String pemilikId = rs.getString("user_id");
                String namaPemilik = getUser(pemilikId)[1];
                String judul = rs.getString("judul");
                String deskripsi = rs.getString("deskripsi");
                String bidang = rs.getString("bidang");
                String tanggal = rs.getString("tanggal_dibuat");

                data.add(new Proyek(proyekId, pemilikId, namaPemilik, judul, deskripsi, bidang, tanggal));
            }
        } catch (Exception e) {
            System.out.println("Gagal mengambil proyek sebagai anggota: " + e.getMessage());
        }

        return data;
    }


}