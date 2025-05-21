package Common.requests;

public class ClearRequest extends Request {
    public ClearRequest(String login, String password) {
        super("clear", login, password);
    }
}