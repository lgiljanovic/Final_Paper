package chess.util;

/**
 * Represents coordinate on the chess board. Values are from 1 to 8 starting from top left corner.
 *
 * @author lukag
 * @version 1.1
 */
public class Coordinate {
    private int x;
    private int y;

    /**
     * Basic constructor. Checks whether given arguments are in interval [0, 7].
     */
    public Coordinate(int x, int y) {
        if(x < 0 || x > 7 || y < 0 || y > 7) {
            throw new IllegalArgumentException("Coordinates must be from 0 to 7");
        }
        this.x = x;
        this.y = y;
    }

    /**
     * Constructor used when client doesn't want to check argument's values.
     * @param x x coordinate
     * @param y y coordinate
     * @param unused unused variable
     */
    public Coordinate(int x, int y, boolean unused) {
        this.x = x;
        this.y = y;
    }

    /**
     * Getter for {@link #x} coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Setter for {@link #x} coordinate.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Getter for {@link #y} coordinate.
     */
    public int getY() {
        return y;
    }

    /**
     * Setter for {@link #y} coordinate.
     */
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Coordinate) {
            Coordinate c = (Coordinate) o;
            if(x == c.getX() && y == c.getY()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return x + "-" + y;
    }
}
