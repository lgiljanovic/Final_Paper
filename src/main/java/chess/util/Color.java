package chess.util;

/**
 * There are two possibilities for color in chess: white and black.
 *
 * @author lukag
 * @version 1.1
 */
public enum Color {
    WHITE(0),
    BLACK(1);

    private final int color;

    Color(int color) {
        this.color = color;
    }

    public int getValue() {
        return color;
    }
}
