package Common.responses;

import java.time.LocalDateTime;


public class InfoResponse extends Response {
    private final String collectionType;
    private final LocalDateTime creationDate;
    private final int size;

    public InfoResponse(String collectionType, LocalDateTime creationDate, int size, String error) {
        super("info", error);
        this.collectionType = collectionType;
        this.creationDate = creationDate;
        this.size = size;
    }

    public String getCollectionType() {
        return collectionType;
    }

    public LocalDateTime getCreationDate() {
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