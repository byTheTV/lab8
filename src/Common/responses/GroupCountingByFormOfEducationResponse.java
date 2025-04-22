package Common.responses;

import java.util.Map;

public class GroupCountingByFormOfEducationResponse extends Response {
    private final Map<String, Integer> counts;

    public GroupCountingByFormOfEducationResponse(Map<String, Integer> counts, String error) {
        super(error);
        this.counts = counts;
    }

    public Map<String, Integer> getCounts() {
        return counts;
    }

    @Override
    public String toString() {
        if (getError() != null) {
            return "Ошибка: " + getError();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Количество групп по формам обучения:\n");
        counts.forEach((form, count) -> sb.append(form).append(": ").append(count).append("\n"));
        return sb.toString();
    }
} 