package Common.models;

import java.io.Serializable;

public class Coordinates implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long x; // Максимальное значение поля: 648, Поле не может быть null
    private Long y; // Поле не может быть null

    public Coordinates(Long x, Long y) {
        setX(x);
        setY(y);
    }

    public void setX(Long x) {
        if (x == null) {
            throw new IllegalArgumentException("X coordinate cannot be null");
        }
        if (x > 648) {
            throw new IllegalArgumentException("X coordinate cannot be greater than 648");
        }
        this.x = x;
    }

    public void setY(Long y) {
        if (y == null) {
            throw new IllegalArgumentException("Y coordinate cannot be null");
        }
        this.y = y;
    }

    public Long getX() { return x; }
    public Long getY() { return y; }
    
    @Override
    public String toString() {
        return "Coordinates{" +"x=" + x +", y=" + y + '}';
    }
}   