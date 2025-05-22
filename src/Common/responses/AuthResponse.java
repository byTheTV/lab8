package Common.responses;

public class AuthResponse extends Response {

    public enum AuthStatus { AUTH_SUCCESS, AUTH_FAILED, NOT_REQUIRED }

    private final AuthStatus authStatus;
    private final Integer userId;
    private final String uid;

    public AuthResponse(AuthStatus authStatus, String error, Integer userId, String uid) {
        super("auth", error);
        this.authStatus = authStatus;
        this.userId = userId;
        this.uid = uid;
    }

    public Integer getUserId() {
        return userId;
    }

    public AuthStatus getAuthStatus() {
        return authStatus;
    }

    public String getUid() {
        return uid;
    }
}