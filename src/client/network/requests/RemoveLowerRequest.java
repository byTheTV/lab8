package client.network.requests;

import client.network.Request;
import client.models.StudyGroup;
import java.util.Objects;


public class RemoveLowerRequest extends Request {
    private final StudyGroup studyGroup;

    public RemoveLowerRequest(StudyGroup studyGroup) {
        super("remove_lower");
        this.studyGroup = studyGroup;
    }

    public StudyGroup getStudyGroup() {
        return studyGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RemoveLowerRequest)) return false;
        if (!super.equals(o)) return false;
        RemoveLowerRequest that = (RemoveLowerRequest) o;
        return Objects.equals(studyGroup, that.studyGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), studyGroup);
    }
}
