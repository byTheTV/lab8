package Common.requests;

public class AuthRequest extends Request {
    public AuthRequest(String login, String password) {
        super("auth", login, password);
    }
}