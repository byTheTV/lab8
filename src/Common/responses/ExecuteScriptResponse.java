package Common.responses;

import java.util.List;

public class ExecuteScriptResponse extends Response {
    private final List<String> results;

    public ExecuteScriptResponse(List<String> results, String error) {
        super("execute_script", error);
        this.results = results;
    }

    public List<String> getResults() {
        return results;
    }

    @Override
    public String toString() {
        if (getError() != null) {
            return "Ошибка: " + getError();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Результаты выполнения скрипта:\n");
        results.forEach(result -> sb.append(result).append("\n"));
        return sb.toString();
    }
} 