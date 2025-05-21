package Common.requests;

public class PrintFieldAscendingGroupAdminRequest extends Request {
    public PrintFieldAscendingGroupAdminRequest(String login, String password) {
        super("print_field_ascending_group_admin", login, password);
    }
}