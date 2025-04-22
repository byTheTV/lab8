package Common.responses;

import java.util.List;

public class ExecuteScriptResponse extends Response {
    private final List<String> executionResults;

    public ExecuteScriptResponse(List<String> executionResults, String error) {
        super(error);
        this.executionResults = executionResults;
    }

    public List<String> getExecutionResults() {
        return executionResults;
    }

    @Override
    public String toString() {
        if (getError() != null) {
            return "Ошибка: " + getError();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Результаты выполнения скрипта:\n");
        executionResults.forEach(result -> sb.append(result).append("\n"));
        return sb.toString();
    }
} 