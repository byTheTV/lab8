package Common.requests;

public class HeadRequest extends Request {
    public HeadRequest(String login, String password) {
        super("head", login, password);
    }
}
