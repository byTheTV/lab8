package Common.responses;

public class AuthResponse extends Response {

    public enum AuthStatus { AUTH_SUCCESS, AUTH_FAILED, NOT_REQUIRED }

    private final AuthStatus authStatus;
    private final Integer userId;

    public AuthResponse(AuthStatus authStatus, String error, Integer userId) {
        super("add", error);
        this.authStatus = authStatus;
        this.userId = userId;

    }

    public Integer getUserId() {
        return userId;
    }

    public AuthStatus getAuthStatus() {
        return authStatus;
    }
}