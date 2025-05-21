package Common.requests;

public class InfoRequest extends Request {
    public InfoRequest(String login, String password) {
        super("info", login, password);
    }
}
