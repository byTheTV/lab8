package Common.requests;

public class HelpRequest extends Request {
    public HelpRequest(String login, String password) {
        super("help", login, password);
    }
}