package Common.responses;

import java.util.Map;

public class HelpResponse extends Response {
    private final Map<String, String> commandDescriptions;

    public HelpResponse(Map<String, String> commandDescriptions, String error) {
        super("help", error);
        this.commandDescriptions = commandDescriptions;
    }

    public Map<String, String> getCommandDescriptions() {
        return commandDescriptions;
    }

    @Override
    public String toString() {
        if (getError() != null) {
            return "Ошибка: " + getError();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Доступные команды:\n");
        commandDescriptions.forEach((name, description) -> 
            sb.append(name).append(": ").append(description).append("\n"));
        return sb.toString();
    }
} 