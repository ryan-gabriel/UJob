package Auth;

public class SessionManager {
    private static SessionManager instance;
    private String currentUsername;
    private int currentUserId;
    private String currentUserEmail;
    private String currentUserRole;
    private boolean isLoggedIn;

    private SessionManager() {
        this.isLoggedIn = false;
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void login(int userId, String username, String email, String role) {
        this.currentUserId = userId;
        this.currentUsername = username;
        this.currentUserEmail = email;
        this.currentUserRole = role;
        this.isLoggedIn = true;
    }

    public void logout() {
        this.currentUserId = -1;
        this.currentUsername = null;
        this.currentUserEmail = null;
        this.currentUserRole = null;
        this.isLoggedIn = false;
    }

    public String getUsername() {
        return currentUsername;
    }

    public int getId() {
        return currentUserId;
    }

    public String getEmail() {
        return currentUserEmail;
    }

    public String getRole() {
        return currentUserRole;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }
}

