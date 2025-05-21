package Common.requests;

public class AverageOfTransferredStudentsRequest extends Request {
    public AverageOfTransferredStudentsRequest(String login, String password) {
        super("average_of_transferred_students", login, password);
    }
}