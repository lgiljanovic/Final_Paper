package chess.util;

import chess.pieces.Figure;

import java.util.Objects;

/**
 * Class is used for storing computer's move.
 */
public class Move {

    /**
     * Figure to be moved.
     */
    private Figure figure;

    /**
     * New coordinate.
     */
    private Coordinate coordinate;

    public Move() {

    }

    /**
     * Basic constructor.
     * @param figure {@link #figure}
     * @param c {@link Coordinate}
     */
    public Move(Figure figure, Coordinate c) {
        this.figure = figure;
        coordinate = c;
    }

    public Figure getFigure() {
        return figure;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setFigure(Figure figure) {
        this.figure = figure;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Move)) return false;
        Move move = (Move) o;
        return figure.equals(move.figure) &&
                coordinate.equals(move.coordinate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(figure, coordinate);
    }
}
