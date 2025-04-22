package Common.responses;

import Common.models.StudyGroup;

public class HeadResponse extends Response {
    private final StudyGroup studyGroup;

    public HeadResponse(StudyGroup studyGroup, String error) {
        super("head", error);
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
        return studyGroup.toString();
    }
} 