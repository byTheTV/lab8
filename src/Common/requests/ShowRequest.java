package Common.requests;

public class ShowRequest extends Request {
    public ShowRequest(String login, String password) {
        super("show", login, password);
    }
}