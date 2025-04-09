package client.network.requests;

import client.network.Request;
import client.models.StudyGroup;
import java.util.Objects;


public class AddRequest extends Request {
    private final StudyGroup studyGroup;

    public AddRequest(StudyGroup studyGroup) {
        super("add");
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