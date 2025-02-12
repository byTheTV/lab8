package models;

public class Location {
    private Float x; // Поле не может быть null
    private Float y; // Поле не может быть null
    private Float z; // Поле не может быть null

    public Location(Float x, Float y, Float z) {
        setX(x);
        setY(y);
        setZ(z);
    }

    public void setX(Float x) {
        if (x == null) {
            throw new IllegalArgumentException("X coordinate cannot be null");
        }
        this.x = x;
    }

    public void setY(Float y) {
        if (y == null) {
            throw new IllegalArgumentException("Y coordinate cannot be null");
        }
        this.y = y;
    }

    public void setZ(Float z) {
        if (z == null) {
            throw new IllegalArgumentException("Z coordinate cannot be null");
        }
        this.z = z;
    }

    public Float getX() {
        return x;
    }
    
    public Float getY() {
        return y;
    }
    
    public Float getZ() {
        return z;
    }

    /**
     * Переопределённый метод toString возвращает красиво отформатированное
     * строковое представление объекта Location.
     */
    @Override
    public String toString() {
        String separator = "----------------------------------\n";
        return separator +
               "          Location Details         \n" +
               separator +
               String.format("X: %s%n", x != null ? x : "N/A") +
               String.format("Y: %s%n", y != null ? y : "N/A") +
               String.format("Z: %s%n", z != null ? z : "N/A") +
               separator;
    }
}