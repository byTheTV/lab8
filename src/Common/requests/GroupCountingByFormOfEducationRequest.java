package Common.requests;

public class GroupCountingByFormOfEducationRequest extends Request {
    public GroupCountingByFormOfEducationRequest(String login, String password) {
        super("group_counting_by_form_of_education", login, password);
    }
}