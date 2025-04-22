package Common.requests;

import java.util.Objects;

public class ExecuteScriptRequest extends Request {
    private final String command;
    private final String argument;

    public ExecuteScriptRequest(String command, String argument) {
        super("execute_script");
        this.command = command;
        this.argument = argument;
    }

    public String getCommand() {
        return command;
    }

    public String getArgument() {
        return argument;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExecuteScriptRequest)) return false;
        if (!super.equals(o)) return false;
        ExecuteScriptRequest that = (ExecuteScriptRequest) o;
        return Objects.equals(command, that.command) &&
                Objects.equals(argument, that.argument);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), command, argument);
    }
}
