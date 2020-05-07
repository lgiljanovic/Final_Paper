package chess.pieces;

import chess.game.Position;
import chess.moverecorders.MoveRecorder;
import chess.moverecorders.StandardAlgebraicNotation;
import chess.util.ChessUtil;
import chess.util.Color;
import chess.util.Coordinate;

import javax.swing.Icon;
import java.util.List;

/**
 * Represents a piece in a chess game. Concrete implementation are: {@link Pawn}, {@link King}, {@link Queen}, {@link Rook}, {@link Knight} and {@link Bishop}.
 *
 * @author lukag
 * @version 1.1
 */
public abstract class Figure implements StandardAlgebraicNotation {

    /**
     * Graphic representation of a piece. Depends on a color and symbol.
     */
    private Icon icon;

    /**
     * White or black.
     */
    private Color color;

    /**
     * Characteristic chess symbol for a piece.
     */
    private char symbol;

    /**
     * Value of the piece.
     */
    private int value;

    /**
     * Coordinate of a piece.
     */
    private Coordinate coordinate;

    /**
     * Basic constructor. Icon is initialized using {@link ChessUtil#getIconFor(Color, char)}
     * @param color {@link Color}
     * @param symbol chess symbol for piece
     * @param value value of piece
     * @param coordinate {@link Coordinate} of the piece
     */
    public Figure(Color color, char symbol, int value, Coordinate coordinate) {
        this.color = color;
        this.symbol = symbol;
        this.coordinate = coordinate;
        this.value = value;
        icon = ChessUtil.getIconFor(color, symbol);
    }

    /**
     *  Getter for {@link Figure#value}
     */
    public int getValue() {
        return value;
    }

    /**
     *  Getter for {@link Figure#icon}
     */
    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    /**
     *  Getter for {@link Figure#color}
     */
    public Color getColor() {
        return color;
    }

    /**
     *  Getter for {@link Figure#symbol}
     */
    public char getSymbol() {
        return symbol;
    }

    /**
     *  Getter for x coordinate.
     */
    public int getxCoordinate() {
        return coordinate.getX();
    }

    /**
     *  Getter for y coordinate.
     */
    public int getyCoordinate() {
        return coordinate.getY();
    }

    /**
     *  Setter for x coordinate.
     */
    public void setxCoordinate(int xCoordinates) {
        coordinate.setX(xCoordinates);
    }

    /**
     *  Setter for y coordinate.
     */
    public void setyCoordinate(int yCoordinates) {
        coordinate.setY(yCoordinates);
    }

    /**
     *  Getter for coordinate.
     */
    public Coordinate getCoordinate() {
        return coordinate;
    }

    /**
     *  Setter for coordinate.
     */
    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    /**
     * Method is used for moving pieces. This class provides implementation for Queen and Bishop. Rook has same implementation with
     * additional flags so Rook, alongside with King, Pawn and Knight overrides this method.
     * If {@link ChessUtil#recordMove} is false i.e. computer is calculating, method skips parts for generating move as string and conditions
     * such as checking whether the move is possible in current position because computer works only with moves that oblige rules.
     * @param position position of the game
     * @param newX new x coordinate of a piece
     * @param newY new y coordinate of a piece
     * @return true - if move is possible, otherwise false
     */
    public boolean move(Position position, int newX, int newY) {
        if(!ChessUtil.recordMove) {                                              // recordMove is false when computer is calculating,
            position.setFigure(getxCoordinate(), getyCoordinate(), newX, newY);  // computer already knows possible moves and (newX, newY) is correct move, so
            position.enPassant.enPassantAllowed = false;                         // there is no need to check possible in this method.
            position.isWhiteToMove = !position.isWhiteToMove;
            return true;
        }

        if(possibleMoves(position).contains(new Coordinate(newX, newY))) {
            boolean isEating = position.getFigure(newX, newY) != null;
            position.setFigure(getxCoordinate(), getyCoordinate(), newX, newY);
            position.enPassant.enPassantAllowed = false;
            position.isWhiteToMove = !position.isWhiteToMove;
            MoveRecorder.record(this, isEating, position, newX, newY, this);
            return true;
        }
        return false;
    }

    /**
     * Method calculates all squares that this piece is attacking.
     * @param position current position of the game
     * @return list of squares piece is attacking
     */
    public abstract List<Coordinate> attacking(Position position);


    /**
     * Method calculates all possible moves for the piece on given position.
     * @param position current position of the game
     * @return list of possible moves
     */
    public abstract List<Coordinate> possibleMoves(Position position);
}
