package chess.game;

import chess.pieces.Figure;
import chess.pieces.King;
import chess.pieces.Pawn.EnPassant;
import chess.pieces.Rook;
import chess.util.Color;
import chess.util.Coordinate;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class represents current position in the game. Position is defined with 8x8 array of {@link Figure}.
 * Position is used for manipulating figures and setting up new positions after each move. Also moves can be undone which is used in {@link chess.searchalgorithms.SearchAlgorithm}s,
 * and this class supports method for that.
 * @author lukag
 * @version 1.1
 */
public class Position {

    /**
     * 8x8 matrix which represents chess board with active pieces.
     */
    private Figure[][] position;

    /**
     * If true player white is on down side of the board.
     */
    private boolean playerWhite;

    /**
     * Flag for move order.
     */
    public boolean isWhiteToMove;


    /**
     * Stores information for enpassant on current move. This instance is used for both players.
     */
    public EnPassant enPassant = new EnPassant();

    /**
     * Basic constructor with already initialized array of figures.
     * @param playerWhite {@link Position#playerWhite}
     * @param position initialized position
     * @param isWhiteToMove {@link Position#isWhiteToMove}
     */
    public Position(boolean playerWhite, Figure[][] position, boolean isWhiteToMove){
        this(playerWhite, isWhiteToMove);
        this.position = position;
    }

    /**
     * Constructor without initialized {@link Position#position}.
     * @param playerWhite {@link Position#playerWhite}
     * @param isWhiteToMove {@link Position#isWhiteToMove}
     */
    private Position(boolean playerWhite, boolean isWhiteToMove){
        this.playerWhite = playerWhite;
        this.isWhiteToMove = isWhiteToMove;
    }

    /**
     * Getter for {@link Position#playerWhite}
     */
    public boolean getPlayerWhite() {
        return playerWhite;
    }

    /**
     * Getter for {@link Position#position}
     */
    public Figure[][] getPosition() {
        return position;
    }

    /**
     * Method sets position to position before latest move. After setting position back method calls {@link Position#setFigureAttributesBack} because {@link King}
     * and {@link Rook} have some attributes that could have been changed with latest move.
     * @param position position before move
     * @param f {@link Figure} that was moved
     * @param previousCoordinate previous coordinate of given figure
     */
    public void setPositionBack(Figure[][] position, Figure f, Coordinate previousCoordinate) {
        this.position[f.getxCoordinate()][f.getyCoordinate()] = position[f.getxCoordinate()][f.getyCoordinate()];
        this.position[previousCoordinate.getX()][previousCoordinate.getY()] = f;
        f.setxCoordinate(previousCoordinate.getX());
        f.setyCoordinate(previousCoordinate.getY());
        isWhiteToMove = !isWhiteToMove;
        setFigureAttributesBack(f);
    }

    /**
     * Method checks if given figure is instance of {@link King} or {@link Rook}.
     * If figure is of type King than method checks if move was kingside or queenside castling or just normal king move. Based on that it takes appropriate action.
     * If figure is of type rook that it just decrements {@link Rook#isMoved()}.
     * @param f figure that was moved.
     */
    private void setFigureAttributesBack(Figure f) {
        try {
            if(f instanceof King) {
                boolean down = (f.getColor() == (playerWhite ? Color.WHITE : Color.BLACK));
                if(((King) f).movesAfterCastling == 0 && ((King) f).moveDescription == King.KINGSIDE) {
                    Rook rook = (Rook) getFigure(down ? 7 : 0, playerWhite ? 5 : 2);
                    position[down ? 7 : 0][playerWhite ? 5 : 2] = null;
                    rook.setxCoordinate(down ? 7 : 0);
                    rook.setyCoordinate(playerWhite ? 7 : 0);
                    position[rook.getxCoordinate()][rook.getyCoordinate()] = rook;
                    rook.setMoved(rook.isMoved() - 1);
                    ((King) f).setCastled(false);
                    ((King) f).moveDescription = King.NORMAL;
                    ((King) f).movesAfterCastling = ((King) f).movesAfterCastling - 1;
                } else if(((King) f).moveDescription == King.QUEENSIDE && ((King) f).movesAfterCastling == 0) {
                    Rook rook = (Rook) getFigure(down ? 7 : 0, playerWhite ? 3 : 4);
                    position[down ? 7 : 0][playerWhite ? 3 : 4] = null;
                    rook.setxCoordinate(down ? 7 : 0);
                    rook.setyCoordinate(playerWhite ? 0 : 7);
                    position[rook.getxCoordinate()][rook.getyCoordinate()] = rook;
                    rook.setMoved(rook.isMoved() - 1);
                    ((King) f).setCastled(false);
                    ((King) f).moveDescription = King.NORMAL;
                    ((King) f).movesAfterCastling = ((King) f).movesAfterCastling - 1;
                }
                King k = (King) f;
                k.setMoved(k.isMoved() - 1);
                if(k.moveDescription == King.KINGSIDE || k.moveDescription == King.QUEENSIDE) {
                    k.movesAfterCastling = k.movesAfterCastling - 1;
                }
            } else if(f instanceof Rook) {
                Rook r = (Rook) f;
                r.setMoved(r.isMoved() - 1);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    /**
     * Return figure on given coordinate.
     * WARNING: method can return null, it doesn't check whether the figure is null
     */
    public Figure getFigure(Coordinate coordinate) {
        return position[coordinate.getX()][coordinate.getY()];
    }

    /**
     * Return figure on given (x,y) coordinate.
     * WARNING: method can return null, it doesn't check whether the figure is null
     */
    public Figure getFigure(int x, int y){
        return position[x][y];
    }

    /**
     * Method returns first {@link Figure} that matches given arguments starting from top left corner.
     * If there isn't any {@link Figure} that matches arguments method return null.
     */
    public Coordinate getFigureCoordinate(Color color, char symbol) {
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(position[i][j] != null && position[i][j].getColor() == color &&
                        position[i][j].getSymbol() == symbol) {
                    return new Coordinate(i, j);
                }
            }
        }
        return null;
    }

    /**
     * Method checks whether given arguments are valid and after sets new position for a
     * {@link Figure} defined with (oldX, oldY) coordinate.
     */
    public void setFigure(int oldX, int oldY, int newX, int newY) {
        if(oldX < 0 || oldX > 7 || oldY < 0 || oldY > 7 ||
                newX < 0 || newX > 7 || newY < 0 || newY > 7) {
            throw new IllegalArgumentException("Coordinates must be from 0 to 7");
        }
        try {
            Figure figure = getFigure(oldX, oldY);
            figure.setxCoordinate(newX);
            figure.setyCoordinate(newY);
            position[newX][newY] = figure;
            position[oldX][oldY] = null;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    /**
     * Method sets position back from a move defined with given coordinates.
     * It is a reverse process of {@link Position#setFigure} method.
     * @param oldX x coordinate from where to remove figure
     * @param oldY y coordinate from where to remove figure
     * @param newX x coordinate where to move figure
     * @param newY y coordinate where to move figure
     * @param figure figure that was previously on (oldX, oldY) coordinate.
     */
    public void setFigureBack(int oldX, int oldY, int newX, int newY, Figure figure) {
        position[newX][newY] = position[oldX][oldY];
        position[newX][newY].setxCoordinate(newX);
        position[newX][newY].setyCoordinate(newY);
        position[oldX][oldY] = figure;
    }

    /**
     * Method adds given piece to position using {@link Figure#getCoordinate()}.
     */
    public void add(Figure figure) {
        if(figure == null) {
            throw new IllegalArgumentException("Figure cannot be null");
        }
        position[figure.getxCoordinate()][figure.getyCoordinate()] = figure;
    }

    /**
     * Method sets null value to given (x, y) coordinate, overwriting previous
     * value of that square.
     */
    public void setNullValue(int x, int y) {
        position[x][y] = null;
    }

    /**
     * Method counts number of attackers on given square ((x, y) coordinate).
     * @param color defines player who is attacking given square.
     */
    public int numberOfAttackers(int x, int y, Color color) {
        int counter = 0;
        List<Figure> figures = getPiecesOfColor(color);
        for(Figure piece : figures) {
            for(Coordinate c : piece.attacking(this)) {
                if(c.getX() == x && c.getY() == y) {
                    counter++;
                }
            }
        }
        return counter;
    }


    /**
     * Method returns number of possible moves for a player defined with given color.
     */
    public int allPossibleMoves(Color color) {
        int counter = 0;
        List<Figure> figures = getPiecesOfColor(color);
        for(Figure piece : figures) {
            counter += piece.possibleMoves(this).size();
        }
        return counter;
    }

    /**
     * Method copies figures to newly allocated 2D array. Copied figures reference same object in memory as {@link Position#position}.
     *
     * @see Figure
     * @return copy of position by reference
     */
    public Figure[][] copyByReference() {
        Figure[][] copy = new Figure[8][8];
        for(int i = 0; i < 8; i++) {
            System.arraycopy(position[i], 0, copy[i], 0, 8);
        }
        return copy;
    }

    /**
     * Returns copy of current position. Figures in copied position doesn't reference to the objects of {@link Position#position}.
     * @return
     */
    public Figure[][] copy() {
        Figure[][] copy = new Figure[8][8];
        try {
            for(int i = 0; i < 8; i++) {
                for(int j = 0; j < 8; j++) {
                    Figure f = getPosition()[i][j];
                    if(f == null){
                        continue;
                    }

                    copy[i][j] = f.getClass().getDeclaredConstructor(new Class[] {Color.class, Coordinate.class}).newInstance(f.getColor(), new Coordinate(i, j));
                    if(f instanceof Rook) {
                        ((Rook) f).setMoved(((Rook) f).isMoved());
                    }
                    else if(f instanceof King) {
                        ((King) copy[i][j]).setMoved(((King) f).isMoved());
                        ((King) copy[i][j]).setCastled(((King) f).isCastled());
                        ((King) copy[i][j]).moveDescription = ((King) f).moveDescription;
                        ((King) copy[i][j]).movesAfterCastling = ((King) f).movesAfterCastling;
                    }
                }
            }
        }
        catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return copy;
    }

    /**
     * Returns all figures of given color.
     */
    public List<Figure> getPiecesOfColor(Color color) {
        List<Figure> figures = new ArrayList<>();
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                Figure figure = getFigure(i, j);
                if(figure != null && figure.getColor() == color) {
                    figures.add(figure);
                }
            }
        }
        return figures;
    }
}
