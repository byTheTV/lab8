public class Coordinates {
    private Long x; //Максимальное значение поля: 648, Поле не может быть null
    private Long y; //Поле не может быть null

    public Coordinates(Long x, Long y) {
        setX(x);
        setY(y);
    }

    public void setX(Long x) {
        if (x == null || x > 648) {
            throw new IllegalArgumentException("X не может быть null и должен быть не больше 648");
        }
        this.x = x;
    }

    public void setY(Long y) {
        if (y == null) {
            throw new IllegalArgumentException("Y не может быть null");
        }
        this.y = y;
    }

    // Геттеры
    public Long getX() { return x; }
    public Long getY() { return y; }
}   