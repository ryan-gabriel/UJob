package Database;

import Models.Proyek;
import Auth.SessionManager;

import java.sql.Connection;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ProyekDAO {
    private final Connection con = DatabaseConnection.getInstance().getConnection();

    public void simpanProyek(String judul, String deskripsi, String bidang) {
        String userId = String.valueOf(SessionManager.getInstance().getId());
        try {
            var sql = "INSERT INTO proyek (user_id, judul, deskripsi, bidang, status) VALUES (?, ?, ?, ?, ?)";
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

    private String getNamaPemilik(String userId) {
        try {
            var sql = "SELECT nama FROM user WHERE user_id = ?";
            var stmt = con.prepareStatement(sql);
            stmt.setString(1, userId);
            var rs = stmt.executeQuery();
            return rs.next() ? rs.getString("nama") : "Tidak diketahui";
        } catch (Exception e) {
            return "Gagal ambil nama";
        }
    }

    private String formatTanggal(Timestamp timestamp) {
        if (timestamp == null) return "";
        try {
            LocalDateTime dateTime = timestamp.toLocalDateTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.forLanguageTag("id-ID"));
            return dateTime.format(formatter);
        } catch (Exception e) {
            return timestamp.toString();
        }
    }

    public List<Proyek> getProyek(String userId) {
        List<Proyek> data = new ArrayList<>();
        try {
            var sql = """
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
                AND status = ?
            """;

            var stmt = con.prepareStatement(sql);
            stmt.setString(1, userId);
            stmt.setString(2, userId);
            stmt.setString(3, userId);
            stmt.setString(4, "aktif");

            var rs = stmt.executeQuery();
            while (rs.next()) {
                data.add(new Proyek(
                    rs.getString("proyek_id"),
                    rs.getString("user_id"),
                    getNamaPemilik(rs.getString("user_id")),
                    rs.getString("judul"),
                    rs.getString("deskripsi"),
                    rs.getString("bidang"),
                    formatTanggal(rs.getTimestamp("tanggal_dibuat"))
                ));
            }

        } catch (Exception e) {
            System.out.println("Gagal mengambil proyek: " + e.getMessage());
        }

        return data;
    }

    // Mendapatkan daftar proyek yang sedang didaftari user (pengajuan belum diterima)
    public List<Proyek> getProyekSedangDidaftari(String userId) {
        List<Proyek> data = new ArrayList<>();
        try {
            var sql = """
                SELECT p.* FROM proyek p
                JOIN pengajuan_proyek pp ON p.proyek_id = pp.proyek_id
                WHERE pp.user_id = ? AND p.status = ?
            """;
            var stmt = con.prepareStatement(sql);
            stmt.setString(1, userId);
            stmt.setString(2, "aktif");
            var rs = stmt.executeQuery();
            while (rs.next()) {
                data.add(new Proyek(
                    rs.getString("proyek_id"),
                    rs.getString("user_id"),
                    getNamaPemilik(rs.getString("user_id")),
                    rs.getString("judul"),
                    rs.getString("deskripsi"),
                    rs.getString("bidang"),
                    formatTanggal(rs.getTimestamp("tanggal_dibuat"))
                ));
            }
        } catch (Exception e) {
            System.out.println("Gagal mengambil proyek sedang didaftari: " + e.getMessage());
        }
        return data;
    }

    public List<Proyek> getProyekSendiri(String userId) {
        List<Proyek> data = new ArrayList<>();
        try {
            var sql = "SELECT * FROM proyek WHERE user_id = ?";
            var stmt = con.prepareStatement(sql);
            stmt.setString(1, userId);
            var rs = stmt.executeQuery();
            while (rs.next()) {
                data.add(new Proyek(
                    rs.getString("proyek_id"),
                    userId,
                    getNamaPemilik(userId),
                    rs.getString("judul"),
                    rs.getString("deskripsi"),
                    rs.getString("bidang"),
                    formatTanggal(rs.getTimestamp("tanggal_dibuat"))
                ));
            }
        } catch (Exception e) {
            System.out.println("Gagal mengambil proyek sendiri: " + e.getMessage());
        }
        return data;
    }

    public List<Proyek> getProyekAnggota(String userId) {
        List<Proyek> data = new ArrayList<>();
        try {
            var sql = """
                SELECT p.* FROM proyek p
                JOIN anggota_proyek ap ON p.proyek_id = ap.proyek_id
                WHERE ap.user_id = ?
            """;
            var stmt = con.prepareStatement(sql);
            stmt.setString(1, userId);
            var rs = stmt.executeQuery();
            while (rs.next()) {
                data.add(new Proyek(
                    rs.getString("proyek_id"),
                    rs.getString("user_id"),
                    getNamaPemilik(rs.getString("user_id")),
                    rs.getString("judul"),
                    rs.getString("deskripsi"),
                    rs.getString("bidang"),
                    formatTanggal(rs.getTimestamp("tanggal_dibuat"))
                ));
            }
        } catch (Exception e) {
            System.out.println("Gagal mengambil proyek sebagai anggota: " + e.getMessage());
        }
        return data;
    }

    public boolean terimaPengajuan(String proyekId, String userId) {
        try {
            var insertSql = "INSERT INTO anggota_proyek (user_id, proyek_id) VALUES (?, ?)";
            var stmt1 = con.prepareStatement(insertSql);
            stmt1.setString(1, userId);
            stmt1.setString(2, proyekId);
            stmt1.executeUpdate();

            var deleteSql = "DELETE FROM pengajuan_proyek WHERE user_id = ? AND proyek_id = ?";
            var stmt2 = con.prepareStatement(deleteSql);
            stmt2.setString(1, userId);
            stmt2.setString(2, proyekId);
            stmt2.executeUpdate();

            return true;
        } catch (Exception e) {
            System.out.println("Gagal menerima pengajuan: " + e.getMessage());
            return false;
        }
    }

    public boolean tolakPengajuan(String proyekId, String userId) {
        try {
            var sql = "DELETE FROM pengajuan_proyek WHERE user_id = ? AND proyek_id = ?";
            var stmt = con.prepareStatement(sql);
            stmt.setString(1, userId);
            stmt.setString(2, proyekId);
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Gagal menolak pengajuan: " + e.getMessage());
            return false;
        }
    }

    public boolean hapusProyek(String proyekId, String userId) {
        try {
            var sql = "DELETE FROM proyek WHERE proyek_id = ? AND user_id = ?";
            var stmt = con.prepareStatement(sql);
            stmt.setString(1, proyekId);
            stmt.setString(2, userId);
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Gagal menghapus proyek: " + e.getMessage());
            return false;
        }
    }

    public boolean tandaiProyekSelesai(String proyekId, String userId) {
        try {
            var sql = "UPDATE proyek SET status = 'selesai' WHERE proyek_id = ? AND user_id = ?";
            var stmt = con.prepareStatement(sql);
            stmt.setString(1, proyekId);
            stmt.setString(2, userId);
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Gagal menandai proyek selesai: " + e.getMessage());
            return false;
        }
    }

    public boolean tandaiProyekTutup(String proyekId, String userId) {
        try {
            var sql = "UPDATE proyek SET status = 'ditutup' WHERE proyek_id = ? AND user_id = ?";
            var stmt = con.prepareStatement(sql);
            stmt.setString(1, proyekId);
            stmt.setString(2, userId);
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Gagal menandai proyek ditutup: " + e.getMessage());
            return false;
        }
    }
    
    public boolean daftarProyek(String userId, String proyekId) {
        try {
            var sql = "INSERT INTO pengajuan_proyek (user_id, proyek_id) VALUES (?, ?)";
            var stmt = con.prepareStatement(sql);
            stmt.setString(1, userId);
            stmt.setString(2, proyekId);
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Gagal mendaftar proyek: " + e.getMessage());
            return false;
        }
    }
}
