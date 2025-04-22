package Common.responses;

import Common.models.StudyGroup;

public class RemoveHeadResponse extends Response {
    private final StudyGroup studyGroup;

    public RemoveHeadResponse(StudyGroup studyGroup, String error) {
        super("remove_head", error);
        this.studyGroup = studyGroup;
    }

    public StudyGroup getStudyGroup() {
        return studyGroup;
    }

    @Override
    public String toString() {
        if (getError() != null) {
            return "Ошибка: " + getError();
        }
        if (studyGroup == null) {
            return "Коллекция пуста";
        }
        return studyGroup.toString() + "\nЭлемент удалён.";
    }
} 