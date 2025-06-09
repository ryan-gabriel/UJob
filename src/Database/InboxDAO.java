package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Auth.SessionManager;
import Models.Inbox;

public class InboxDAO {
    private final Connection conn;

    public InboxDAO() {
        this.conn = new DatabaseConnection().getConnection();
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

    public List<Inbox> getRecentInbox() {
        List<Inbox> inboxList = new ArrayList<>();
        String query = "SELECT isi, waktu FROM notifikasi WHERE user_id = ? ORDER BY waktu DESC LIMIT 3";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, SessionManager.getInstance().getId());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String isi = rs.getString("isi");
                    String tanggal = formatTanggal(rs.getTimestamp("waktu"));
                    inboxList.add(new Inbox(isi, tanggal));
                }
            }
        } catch (Exception e) {
            System.err.println("Gagal mengambil inbox terbaru: " + e.getMessage());
            e.printStackTrace();
        }

        return inboxList;
    }

    public List<Inbox> getCurrentUserAllInbox() {
        List<Inbox> inboxList = new ArrayList<>();
        String query = "SELECT isi, waktu FROM notifikasi WHERE user_id = ? ORDER BY waktu DESC";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, SessionManager.getInstance().getId());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String isi = rs.getString("isi");
                    String waktu = rs.getString("waktu");
                    inboxList.add(new Inbox(isi, waktu));
                }
            }
        } catch (Exception e) {
            System.err.println("Gagal mengambil semua inbox: " + e.getMessage());
            e.printStackTrace();
        }

        return inboxList;
    }
}