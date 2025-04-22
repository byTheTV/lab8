package Common.responses;

import java.util.List;

public class PrintFieldAscendingGroupAdminResponse extends Response {
    private final List<String> adminNames;

    public PrintFieldAscendingGroupAdminResponse(List<String> adminNames, String error) {
        super(error);
        this.adminNames = adminNames;
    }

    public List<String> getAdminNames() {
        return adminNames;
    }

    @Override
    public String toString() {
        if (getError() != null) {
            return "Ошибка: " + getError();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Имена администраторов групп в порядке возрастания:\n");
        adminNames.forEach(name -> sb.append(name).append("\n"));
        return sb.toString();
    }
} 