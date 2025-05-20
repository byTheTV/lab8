package Common.requests;

public class AuthRequest extends Request {
    private final String login;
    private final String password;

    public AuthRequest(String login, String password) {
        super("auth");
        this.login = login;
        this.password = password;
    }
    public String getLogin() {
        return login;
    }
    public String getPassword() {
        return password;
    }
}