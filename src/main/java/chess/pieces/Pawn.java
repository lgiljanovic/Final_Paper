package chess.pieces;

import chess.game.Position;
import chess.moverecorders.MoveRecorder;
import chess.util.ChessUtil;
import chess.util.Color;
import chess.util.Coordinate;

import java.util.ArrayList;
import java.util.List;

/**
 * Pawn is chess {@link Figure}. Pawn's value is 1 and symbol is 'P'.
 */
public class Pawn extends Figure {

    /**
     * Is move enPassant.
     */
    public boolean enPassant = false;

    /**
     * Constant indicates that pawn promoting. Used for move recording.
     */
    private static final int PROMOTING = 1;

    /**
     * Constant indicates that pawn is not promoting. Used for move recording.
     */
    private static final int NOT_PROMOTING = 0;

    /**
     * Constant indicates that pawn didn't eat in this move. Used for move recording.
     */
    private static final int NO_Y = 0;

    /**
     * Basic constructor that takes all attributes except icon which is initialized using util class.
     * Value of the pawn is initialized to 1.
     */
    public Pawn(Color color, Coordinate coordinate) {
        super(color,'P', 1, coordinate);
    }

    @Override
    public boolean move(Position position, int newX, int newY) {
        int difX = newX - getxCoordinate();
        int difY = Math.abs(newY - getyCoordinate());

        boolean down = (getColor() == (position.getPlayerWhite() ? Color.WHITE : Color.BLACK)); //up or down player on the board

        if(difX == (down ? -2 : 2)) {
            if(getxCoordinate() == (down ? 6 : 1) && difY == 0 &&
                    position.getFigure(newX, newY) == null && position.getFigure(down ? newX + 1 : newX - 1, newY) == null) {
                position.setFigure(getxCoordinate(), getyCoordinate(), newX, newY);
                position.enPassant.enPassantAllowed = true;
                position.enPassant.x = newX;
                position.enPassant.y = newY;
                position.isWhiteToMove = !position.isWhiteToMove;
                MoveRecorder.record(this, false, position, newX, newY, this, NO_Y, NOT_PROMOTING);
                return true;
            }
        }
        if(difX == (down ? -1 : 1)) {
            if(difY == 0) {
                if(position.getFigure(newX, newY) == null) {
                    if(newX == 7 || newX == 0) {
                        if (ChessUtil.computerPlaying && ChessUtil.computersMove) {
                            position.setNullValue(getxCoordinate(), getyCoordinate());
                            setxCoordinate(newX);
                            setyCoordinate(newY);
                            Color color = position.isWhiteToMove ? Color.WHITE : Color.BLACK;
                            Queen queen = new Queen(color, new Coordinate(newX, newY));
                            position.add(queen);

                            position.enPassant.enPassantAllowed = false;
                            position.isWhiteToMove = !position.isWhiteToMove;
                            if(ChessUtil.recordMove) {
                                MoveRecorder.record(this, false, position, newX, newY, this, NO_Y, PROMOTING);
                            }
                            return true;
                        }
                        position.setNullValue(getxCoordinate(), getyCoordinate());
                        position.enPassant.enPassantAllowed = false;
                        position.isWhiteToMove = !position.isWhiteToMove;
                        if(ChessUtil.recordMove) {
                            recordMove(false, position, newX, newY, this, NO_Y, NOT_PROMOTING);
                        }
                        return true;
                    }
                    position.setFigure(getxCoordinate(), getyCoordinate(), newX, newY);
                    position.enPassant.enPassantAllowed = false;
                    position.isWhiteToMove = !position.isWhiteToMove;
                    MoveRecorder.record(this, false, position, newX, newY, this, NO_Y, NOT_PROMOTING);
                    return true;
                }
            }
            if(difY == 1) {
                if(newX == 7 || newX == 0) {
                    int y = getyCoordinate();
                    if(ChessUtil.computerPlaying && ChessUtil.computersMove) {
                        position.setNullValue(getxCoordinate(), getyCoordinate());
                        setxCoordinate(newX);
                        setyCoordinate(newY);
                        Color color = position.isWhiteToMove ? Color.WHITE : Color.BLACK;
                        Queen queen = new Queen(color, new Coordinate(newX, newY));
                        position.add(queen);
                        position.enPassant.enPassantAllowed = false;
                        position.isWhiteToMove = !position.isWhiteToMove;
                        if(ChessUtil.recordMove) {
                            MoveRecorder.record(this,true, position, newX, newY, this, y, PROMOTING);
                        }
                        return true;
                    }

                    position.setFigure(getxCoordinate(), getyCoordinate(), newX, newY);
                    position.enPassant.enPassantAllowed = false;
                    position.isWhiteToMove = !position.isWhiteToMove;
                    if(ChessUtil.recordMove) {
                        recordMove(true, position, newX, newY, this, y, NOT_PROMOTING);
                    }
                    return true;
                }
                if(position.enPassant.enPassantAllowed && position.enPassant.x == getxCoordinate() && (position.enPassant.y == getyCoordinate() - 1 ||
                        position.enPassant.y == getyCoordinate() + 1)) {
                    int y = getyCoordinate();
                    enPassant = true;
                    position.setFigure(getxCoordinate(), getyCoordinate(), newX, newY);
                    position.setNullValue(position.enPassant.x, position.enPassant.y);
                    position.enPassant.enPassantAllowed = false;
                    position.isWhiteToMove = !position.isWhiteToMove;
                    if(ChessUtil.recordMove) {
                        MoveRecorder.record(this, true, position, newX, newY, this, y, NOT_PROMOTING);
                    }
                    return true;
                }
                if(position.getFigure(newX, newY) != null) {
                    int y = getyCoordinate();
                    position.setFigure(getxCoordinate(), getyCoordinate(), newX, newY);
                    position.enPassant.enPassantAllowed = false;
                    position.isWhiteToMove = !position.isWhiteToMove;
                    if(ChessUtil.recordMove) {
                        MoveRecorder.record(this,true, position, newX, newY, this, y, NOT_PROMOTING);
                    }
                    return true;
                }
            }
        }


        return false;
    }

    @Override
    public List<Coordinate> attacking(Position position) {
        List<Coordinate> coordinates = new ArrayList<>();
        int x = getxCoordinate();
        int y = getyCoordinate();

        if(x == 7 || x == 0) {
            return coordinates;
        }

        boolean down = (getColor() == (position.getPlayerWhite() ? Color.WHITE : Color.BLACK)); //up or down player on the board

        if(y == 7 || y == 0) {
            Coordinate coordinate = new Coordinate(down ? x - 1 : x + 1, y == 7 ? y - 1 : y + 1);
            if(position.getFigure(coordinate.getX(), coordinate.getY()) == null ||
                    position.getFigure(coordinate.getX(), coordinate.getY()).getColor() != getColor()) {
                coordinates.add(coordinate);
            }
            return coordinates;
        }

        Coordinate coordinate1 = new Coordinate(down ? x - 1 : x + 1, y - 1);
        Coordinate coordinate2 = new Coordinate(down ? x - 1 : x + 1, y + 1);
        if(position.getFigure(coordinate1.getX(), coordinate1.getY()) == null ||
                position.getFigure(coordinate1.getX(), coordinate1.getY()).getColor() != getColor()) {
            coordinates.add(coordinate1);
        }
        if(position.getFigure(coordinate2.getX(), coordinate2.getY()) == null ||
                position.getFigure(coordinate2.getX(), coordinate2.getY()).getColor() != getColor()) {
            coordinates.add(coordinate2);
        }

        return coordinates;
    }

    @Override
    public List<Coordinate> possibleMoves(Position position) {
        List<Coordinate> coordinates = new ArrayList<>();
        int x = getxCoordinate();
        int y = getyCoordinate();

        if(x == 7 || x == 0) {
            return coordinates;
        }

        boolean down = (getColor() == (position.getPlayerWhite() ? Color.WHITE : Color.BLACK)); //up or down player on the board

        if(x == (down ? 6 : 1)) {
            if(position.getFigure(down ? 4 : 3, y) == null && position.getFigure(down ? 5 : 2, y) == null) {
                coordinates.add(new Coordinate(down ? 4 : 3, y));
            }
        }

        if(position.getFigure(down ? x - 1 : x + 1, y) == null) {
            coordinates.add(new Coordinate(down ? x - 1 : x + 1, y));
        }

        if(y != 7 && position.getFigure(down ? x - 1 : x + 1, y + 1) != null && position.getFigure(down ? x - 1 : x + 1, y + 1).getColor() != getColor()) {
            coordinates.add(new Coordinate(down ? x - 1 : x + 1, y + 1));
        }

        if(y != 0 && position.getFigure(down ? x - 1 : x + 1, y - 1) != null && position.getFigure(down ? x - 1 : x + 1, y - 1).getColor() != getColor()) {
            coordinates.add(new Coordinate(down ? x - 1 : x + 1, y - 1));
        }

        if(position.enPassant.x == getxCoordinate() && (position.enPassant.y == getyCoordinate() - 1 ||
                position.enPassant.y == getyCoordinate() + 1) && position.enPassant.enPassantAllowed) {
            coordinates.add(new Coordinate(down ? x - 1 : x + 1, position.enPassant.y));
        }

        return ChessUtil.filterPossibleMoves(position, this, coordinates);
    }

    @Override
    public void recordMove(boolean isEating, Position position, int x, int y, Figure f, int... additionalArgs) {
        ChessUtil.move = (!position.isWhiteToMove ? ChessUtil.moveNumber + "." : "") + (!isEating ? "" : ChessUtil.abc.substring(additionalArgs[0], additionalArgs[0] + 1));
        if(isEating || enPassant) {
            ChessUtil.move += "x";
        }
        ChessUtil.move += ChessUtil.abc.substring(y, y + 1) + ChessUtil.numbers.substring(x, x + 1) + (additionalArgs[1] != 0 ? "=Q" : "");
    }


    /**
     * Static class that represents key information for en passant move.
     * @author lukag
     * @version 1.1
     */
    public static class EnPassant {

        /**
         * X coordinate of pawn that can possible be eaten by en passant.
         */
        public int x = -1;

        /**
         * Y coordinate of pawn that can possible be eaten by en passant.
         */
        public int y = -1;

        /**
         * Flag for possible en passant move.
         */
        public boolean enPassantAllowed = false;
    }
}
