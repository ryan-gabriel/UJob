package Database;

import Models.Proyek;
import Models.User;
import Auth.SessionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ProyekDAO {
    private final Connection con = DatabaseConnection.getConnection();

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

    public List<Proyek> getProyek(String userId, String search) {
        List<Proyek> data = new ArrayList<>();
        if (search == null) search = "";

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
                AND p.status = ?
                order by p.tanggal_dibuat DESC
            """;

            // Tambah kondisi pencarian jika keyword tidak kosong
            boolean hasSearch = !search.isBlank();
            if (hasSearch) {
                sql += """
                    AND (
                        p.judul LIKE ?
                        OR p.deskripsi LIKE ?
                        OR p.bidang LIKE ?
                    )
                """;
            }

            PreparedStatement stmt = con.prepareStatement(sql);

            int idx = 1;
            stmt.setString(idx++, userId); // p.user_id != ?
            stmt.setString(idx++, userId); // NOT EXISTS pengajuan
            stmt.setString(idx++, userId); // NOT EXISTS anggota
            stmt.setString(idx++, "aktif"); // p.status = 'aktif'

            if (hasSearch) {
                String keyword = "%" + search + "%";
                stmt.setString(idx++, keyword); // LIKE judul
                stmt.setString(idx++, keyword); // LIKE deskripsi
                stmt.setString(idx++, keyword); // LIKE bidang
            }

            var rs = stmt.executeQuery();
            while (rs.next()) {
                data.add(new Proyek(
                    rs.getString("proyek_id"),
                    rs.getString("user_id"),
                    getNamaPemilik(rs.getString("user_id")),
                    rs.getString("judul"),
                    rs.getString("deskripsi"),
                    rs.getString("bidang"),
                    formatTanggal(rs.getTimestamp("tanggal_dibuat")),
                    rs.getString("status")
                ));
            }

        } catch (Exception e) {
            System.out.println("Gagal mengambil proyek: " + e.getMessage());
        }

        return data;
    }

    // Overload: jika search tidak diberikan, gunakan string kosong
    public List<Proyek> getProyek(String userId) {
        return getProyek(userId, "");
    }

    // Mendapatkan daftar proyek yang sedang didaftari user (pengajuan belum diterima)
    public List<Proyek> getProyekSedangDidaftari(String userId, String search) {
        List<Proyek> data = new ArrayList<>();

        if (search == null) search = "";
        boolean isSearching = !search.trim().isEmpty();

        try {
            String sql = """
                SELECT p.* FROM proyek p
                JOIN pengajuan_proyek pp ON p.proyek_id = pp.proyek_id
                WHERE pp.user_id = ? AND p.status = ?
            """;

            if (isSearching) {
                sql += """
                    AND (
                        p.judul LIKE ?
                        OR p.deskripsi LIKE ?
                        OR p.bidang LIKE ?
                    )
                """;
            }

            sql += " ORDER BY p.tanggal_dibuat DESC";

            var stmt = con.prepareStatement(sql);
            int idx = 1;
            stmt.setString(idx++, userId);
            stmt.setString(idx++, "aktif");

            if (isSearching) {
                String keyword = "%" + search + "%";
                stmt.setString(idx++, keyword);
                stmt.setString(idx++, keyword);
                stmt.setString(idx++, keyword);
            }

            var rs = stmt.executeQuery();
            while (rs.next()) {
                data.add(new Proyek(
                    rs.getString("proyek_id"),
                    rs.getString("user_id"),
                    getNamaPemilik(rs.getString("user_id")),
                    rs.getString("judul"),
                    rs.getString("deskripsi"),
                    rs.getString("bidang"),
                    formatTanggal(rs.getTimestamp("tanggal_dibuat")),
                    rs.getString("status")
                ));
            }

        } catch (Exception e) {
            System.out.println("Gagal mengambil proyek sedang didaftari: " + e.getMessage());
        }

        return data;
    }

    public List<Proyek> getProyekSedangDidaftari(String userId) {
        return getProyekSedangDidaftari(userId, "");
    }


    public List<Proyek> getProyekSendiri(String userId, String search) {
        List<Proyek> data = new ArrayList<>();

        if (search == null) search = "";
        boolean isSearching = !search.trim().isEmpty();

        try {
            String sql = "SELECT * FROM proyek WHERE user_id = ?";

            if (isSearching) {
                sql += """
                    AND (
                        judul LIKE ?
                        OR deskripsi LIKE ?
                        OR bidang LIKE ?
                    )
                """;
            }

            sql += " ORDER BY tanggal_dibuat DESC";

            var stmt = con.prepareStatement(sql);
            int idx = 1;
            stmt.setString(idx++, userId);

            if (isSearching) {
                String keyword = "%" + search + "%";
                stmt.setString(idx++, keyword);
                stmt.setString(idx++, keyword);
                stmt.setString(idx++, keyword);
            }

            var rs = stmt.executeQuery();
            while (rs.next()) {
                data.add(new Proyek(
                    rs.getString("proyek_id"),
                    userId,
                    getNamaPemilik(userId),
                    rs.getString("judul"),
                    rs.getString("deskripsi"),
                    rs.getString("bidang"),
                    formatTanggal(rs.getTimestamp("tanggal_dibuat")),
                    rs.getString("status")
                ));
            }
        } catch (Exception e) {
            System.out.println("Gagal mengambil proyek sendiri: " + e.getMessage());
        }

        return data;
    }

    public List<Proyek> getProyekSendiri(String userId) {
        return getProyekSendiri(userId, "");
    }


    public List<Proyek> getProyekAnggota(String userId, String search) {
        List<Proyek> data = new ArrayList<>();

        if (search == null) search = "";
        boolean isSearching = !search.trim().isEmpty();

        try {
            String sql = """
                SELECT p.* FROM proyek p
                JOIN anggota_proyek ap ON p.proyek_id = ap.proyek_id
                WHERE ap.user_id = ?
            """;

            if (isSearching) {
                sql += """
                    AND (
                        p.judul LIKE ?
                        OR p.deskripsi LIKE ?
                        OR p.bidang LIKE ?
                    )
                """;
            }

            sql += " ORDER BY p.tanggal_dibuat DESC";

            var stmt = con.prepareStatement(sql);
            int idx = 1;
            stmt.setString(idx++, userId);

            if (isSearching) {
                String keyword = "%" + search + "%";
                stmt.setString(idx++, keyword);
                stmt.setString(idx++, keyword);
                stmt.setString(idx++, keyword);
            }

            var rs = stmt.executeQuery();
            while (rs.next()) {
                data.add(new Proyek(
                    rs.getString("proyek_id"),
                    rs.getString("user_id"),
                    getNamaPemilik(rs.getString("user_id")),
                    rs.getString("judul"),
                    rs.getString("deskripsi"),
                    rs.getString("bidang"),
                    formatTanggal(rs.getTimestamp("tanggal_dibuat")),
                    rs.getString("status")
                ));
            }
        } catch (Exception e) {
            System.out.println("Gagal mengambil proyek sebagai anggota: " + e.getMessage());
        }

        return data;
    }

    public List<Proyek> getProyekAnggota(String userId) {
        return getProyekAnggota(userId, "");
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

    public boolean terimaPengajuan(String proyekId, String userId){
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

    public boolean tutupProyek(String proyekId, String userId) {
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
    
    public boolean bukaProyek(String proyekId, String userId) {
        try {
            var sql = "UPDATE proyek SET status = 'aktif' WHERE proyek_id = ? AND user_id = ?";
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

    public List<User> getAnggotaProyek(String proyekId) {
        List<User> anggota = new ArrayList<>();
        try {
            var sql = "SELECT u.user_id, u.nama, u.email FROM anggota_proyek ap JOIN user u ON ap.user_id = u.user_id WHERE ap.proyek_id = ?";
            var stmt = con.prepareStatement(sql);
            stmt.setString(1, proyekId);
            var rs = stmt.executeQuery();
            while (rs.next()) {
                anggota.add(new User(
                    rs.getInt("user_id"),
                    rs.getString("nama"),
                    rs.getString("email"),
                    null,
                    null
                ));
            }
        } catch (Exception e) {
            System.out.println("Gagal mengambil anggota proyek: " + e.getMessage());
        }
        return anggota;
    }

    public List<User> getPendaftaranAnggotaProyek(String proyekId) {
        List<User> pendaftaran = new ArrayList<>();
        try {
            var sql = "SELECT u.user_id, u.nama, u.email FROM pengajuan_proyek pp JOIN user u ON pp.user_id = u.user_id WHERE pp.proyek_id = ?";
            var stmt = con.prepareStatement(sql);
            stmt.setString(1, proyekId);
            var rs = stmt.executeQuery();
            while (rs.next()) {
                pendaftaran.add(new User(
                    rs.getInt("user_id"),
                    rs.getString("nama"),
                    rs.getString("email"),
                    null,
                    null
                ));
            }
        } catch (Exception e) {
            System.out.println("Gagal mengambil pendaftaran anggota proyek: " + e.getMessage());
        }
        return pendaftaran;
    }

    public boolean hapusAnggotaProyek(String proyekId, String userId) {
        try {
            var sql = "DELETE FROM anggota_proyek WHERE proyek_id = ? AND user_id = ?";
            var stmt = con.prepareStatement(sql);
            stmt.setString(1, proyekId);
            stmt.setString(2, userId);
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Gagal menghapus anggota proyek: " + e.getMessage());
            return false;
        }
    }

    public boolean isPemilikProyek(String proyekId, String userId) {
        try {
            var sql = "SELECT COUNT(*) FROM proyek WHERE proyek_id = ? AND user_id = ?";
            var stmt = con.prepareStatement(sql);
            stmt.setString(1, proyekId);
            stmt.setString(2, userId);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            System.out.println("Gagal memeriksa pemilik proyek: " + e.getMessage());
        }
        return false;
    }

    public boolean updateProyek(String proyekId, String judul, String deskripsi, String bidang) {
        try {
            var sql = "UPDATE proyek SET judul = ?, deskripsi = ?, bidang = ? WHERE proyek_id = ?";
            var stmt = con.prepareStatement(sql);
            stmt.setString(1, judul);
            stmt.setString(2, deskripsi);
            stmt.setString(3, bidang);
            stmt.setString(4, proyekId);
            stmt.executeUpdate();

            return true;
        } catch (Exception e) {
            System.out.println("Gagal memperbarui proyek: " + e.getMessage());
        }
        return false;
    }

    public String getOwnerId(String proyekId){
        try {
            var sql = "SELECT user_id FROM proyek WHERE proyek_id = ?";
            var stmt = con.prepareStatement(sql);
            stmt.setString(1, proyekId);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("user_id");
            }
        } catch (Exception e) {
            System.out.println("Gagal mengambil owner id: " + e.getMessage());
        }
        return null;
    }

    public String getJudulProyek(String proyekId){
        try {
            var sql = "SELECT judul FROM proyek WHERE proyek_id = ?";
            var stmt = con.prepareStatement(sql);
            stmt.setString(1, proyekId);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("judul");
            }
        } catch (Exception e) {
            System.out.println("Gagal mengambil judul proyek: " + e.getMessage());
        }
        return null;
    }
}
