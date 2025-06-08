package Database;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import Auth.SessionManager;
import Models.User;

public class UserDAO {

    private final Connection conn;

    public UserDAO() {
        conn = new DatabaseConnection().getConnection();
    }

    public User login(String email, String password) {
        String query = "SELECT * FROM user WHERE email = ? AND password = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user =  new User(
                    rs.getInt("user_id"),
                    rs.getString("nama"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("role")
                );

                SessionManager.getInstance().login(
                    user.getUserId(),
                    user.getNama(),
                    user.getEmail(),
                    user.getRole()
                );
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean register(User user) {
        String query = "INSERT INTO user (nama, email, password, role) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, user.getNama());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getRole());

            int result = ps.executeUpdate();
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(User user) {
        String query = "UPDATE user SET nama = ?, email = ?, password = ?, role = ? WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, user.getNama());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getRole());
            ps.setInt(5, user.getUserId());

            int result = ps.executeUpdate();
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isEmailTaken(String email) {
        String query = "SELECT user_id FROM user WHERE email = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // jika ada, email sudah terdaftar
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
