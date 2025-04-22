package Common.responses;

public class ClearResponse extends Response {
    public ClearResponse(String error) {
        super("clear", error);
    }
} 