package Common.responses;

public class RemoveLowerResponse extends Response {
    private final int removedCount;

    public RemoveLowerResponse(int removedCount, String error) {
        super("remove_lower", error);
        this.removedCount = removedCount;
    }

    public int getRemovedCount() {
        return removedCount;
    }

    @Override
    public String toString() {
        if (getError() != null) {
            return "Ошибка: " + getError();
        }
        return "Удалено элементов: " + removedCount;
    }
} 