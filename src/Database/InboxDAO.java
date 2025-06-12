package Database;

import Auth.SessionManager;
import Models.Inbox;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class InboxDAO {
    private final Connection conn;

    public InboxDAO() {
        // Pastikan Anda memiliki class DatabaseConnection yang berfungsi
        conn = DatabaseConnection.getConnection();
    }

    private String formatTanggal(Timestamp timestamp) {
        if (timestamp == null) return "";
        try {
            LocalDateTime dateTime = timestamp.toLocalDateTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.forLanguageTag("id-ID"));
            return dateTime.format(formatter);
        } catch (Exception e) {
            e.printStackTrace();
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
                    inboxList.add(new Inbox(rs.getString("isi"), formatTanggal(rs.getTimestamp("waktu"))));
                }
            }
        } catch (SQLException e) {
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
                    String tanggal = formatTanggal(rs.getTimestamp("waktu"));
                    inboxList.add(new Inbox(isi, tanggal));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inboxList;
    }
}