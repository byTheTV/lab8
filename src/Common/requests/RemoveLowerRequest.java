package Common.requests;

import java.util.Objects;


public class RemoveLowerRequest extends Request {
     private final Long id; // Теперь храним ID

    public RemoveLowerRequest(Long id) {
        super("remove_lower");
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RemoveLowerRequest)) return false;
        if (!super.equals(o)) return false;
        RemoveLowerRequest that = (RemoveLowerRequest) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }
}
