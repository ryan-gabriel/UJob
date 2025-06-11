package Models;

public class User {
    private int userId;
    private String nama;
    private String email;
    private String password;
    private String role;

    public User(int userId, String nama, String email, String password, String role) {
        this.userId = userId;
        this.nama = nama;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(String nama, String email, String password, String role) {
        this.nama = nama;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(int userId, String nama, String email, String password, String role, String major, String graduationYear, String description) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public User() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    // Getters and Setters
    public int getUserId() { return userId; }
    public String getNama() { return nama; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

    public void setUserId(int userId) { this.userId = userId; }
    public void setNama(String nama) { this.nama = nama; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(String role) { this.role = role; }
}
