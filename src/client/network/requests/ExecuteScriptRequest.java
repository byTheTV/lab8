package client.network.requests;


import java.util.Objects;
import client.network.Request;


public class ExecuteScriptRequest extends Request {
    private final String fileName;

    public ExecuteScriptRequest(String fileName) {
        super("execute_script");
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExecuteScriptRequest)) return false;
        if (!super.equals(o)) return false;
        ExecuteScriptRequest that = (ExecuteScriptRequest) o;
        return Objects.equals(fileName, that.fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), fileName);
    }
}
