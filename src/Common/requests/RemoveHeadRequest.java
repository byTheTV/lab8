package Common.requests;

public class RemoveHeadRequest extends Request {
    public RemoveHeadRequest(String login, String password) {
        super("remove_head", login, password);
    }
}