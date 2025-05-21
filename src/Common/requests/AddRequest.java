package Common.requests;

import java.util.Objects;

import Common.models.StudyGroup;

public class AddRequest extends Request {
    private final StudyGroup studyGroup;

    public AddRequest(StudyGroup studyGroup, String login, String password) {
        super("add", login, password);
        this.studyGroup = studyGroup;
    }

    public StudyGroup getStudyGroup() {
        return studyGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AddRequest)) return false;
        if (!super.equals(o)) return false;
        AddRequest that = (AddRequest) o;
        return Objects.equals(studyGroup, that.studyGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), studyGroup);
    }
}