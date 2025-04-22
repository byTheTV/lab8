package Common.responses;

import java.util.Collection;

import Common.models.StudyGroup;

public class ShowResponse extends Response {
    private final Collection<StudyGroup> collection;

    public ShowResponse(Collection<StudyGroup> collection, String error) {
        super("show", error);
        this.collection = collection;
    }

    public Collection<StudyGroup> getCollection() {
        return collection;
    }

    @Override
    public String toString() {
        if (getError() != null) {
            return "Ошибка: " + getError();
        }
        if (collection.isEmpty()) {
            return "Коллекция пуста";
        }
        StringBuilder sb = new StringBuilder();
        for (StudyGroup group : collection) {
            sb.append(group).append("\n");
        }
        return sb.toString();
    }
} 