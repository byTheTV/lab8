package Common.responses;

public class UpdateIdResponse extends Response {
    public UpdateIdResponse(String error) {
        super("update_id", error);
    }
} 