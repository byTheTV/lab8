package Common.responses;

public class InfoResponse extends Response {
    private final String collectionType;
    private final String creationDate;
    private final int size;

    public InfoResponse(String collectionType, String creationDate, int size, String error) {
        super("info", error);
        this.collectionType = collectionType;
        this.creationDate = creationDate;
        this.size = size;
    }

    public String getCollectionType() {
        return collectionType;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        if (getError() != null) {
            return "Ошибка: " + getError();
        }
        return "Тип коллекции: " + collectionType + "\n" +
               "Дата инициализации: " + creationDate + "\n" +
               "Количество элементов: " + size;
    }
} 