public class Location {
    private Float x; //Поле не может быть null
    private Float y; //Поле не может быть null
    private Float z; //Поле не может быть null

    public Location(Float x, Float y, Float z) {
        setX(x);
        setY(y);
        setZ(z);
    }

    public void setX(Float x) {
        if (x == null) {
            throw new IllegalArgumentException("X не может быть null");
        }
        this.x = x;
    }

    public void setY(Float y) {
        if (y == null) {
            throw new IllegalArgumentException("Y не может быть null");
        }
        this.y = y;
    }

    public void setZ(Float z) {
        if (z == null) {
            throw new IllegalArgumentException("Z не может быть null");
        }
        this.z = z;
    }

    // Геттеры
}