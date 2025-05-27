package Common.requests;

public class RegisterRequest extends Request {
    public RegisterRequest(String login, String password) {
        super("register", login, password);
    }
} 