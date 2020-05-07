package chess.pieces;

import chess.game.Position;
import chess.moverecorders.MoveRecorder;
import chess.util.ChessUtil;
import chess.util.Color;
import chess.util.Coordinate;

import java.util.ArrayList;
import java.util.List;

/**
 * King is a chess {@link Figure}. King's value is not defined but set to 1000 and symbol is 'K'.
 */
public class King extends Figure {

    public static final int NORMAL = 0;
    public static final int KINGSIDE = 1;
    public static final int QUEENSIDE = 2;

    private int isMoved = 0;
    private boolean isCastled = false;
    private Color opponentColor = getColor() == Color.WHITE ? Color.BLACK : Color.WHITE;
    public int moveDescription;
    public int movesAfterCastling = -1;         // not castled yet

    /**
     * Basic constructor that takes all attributes except icon which is initialized using util class.
     * Value of the king is initialized to 1000.
     */
    public King(Color color, Coordinate coordinate) {
        super(color, 'K', 1000, coordinate);
    }

    public int isMoved() {
        return isMoved;
    }

    public boolean isCastled() {
        return isCastled;
    }

    public void setCastled(boolean castled) {
        isCastled = castled;
    }

    public void setMoved(int moved) {
        if(moved < 0) {
            isMoved = 0;
            return;
        }
        isMoved = moved;
    }

    @Override
    public boolean move(Position position, int newX, int newY) {
        int difX = newX - getxCoordinate();
        int difY = newY - getyCoordinate();

        boolean down = (getColor() == (position.getPlayerWhite() ? Color.WHITE : Color.BLACK)); //up or down player on the board

        if(!ChessUtil.recordMove) {
            if(difX == 0) {
                if((position.getPlayerWhite() ? difY == 2 : difY == -2)) {
                    Rook rook = (Rook) position.getFigure(down ? 7 : 0, position.getPlayerWhite() ? 7 : 0);
                    position.setFigure(rook.getxCoordinate(), rook.getyCoordinate(), down ? 7 : 0, position.getPlayerWhite() ? 5 : 2);
                    position.setFigure(getxCoordinate(), getyCoordinate(), newX, newY);
                    position.enPassant.enPassantAllowed = false;
                    isMoved++;
                    rook.setMoved(rook.isMoved() + 1);
                    position.isWhiteToMove = !position.isWhiteToMove;
                    moveDescription = KINGSIDE;
                    movesAfterCastling = 0;
                    isCastled = true;
                    return true;
                }
                else if(position.getPlayerWhite() ? difY == -2 : difY == 2) {
                    Rook rook = (Rook) position.getFigure(down ? 7 : 0, position.getPlayerWhite() ? 0 : 7);
                    position.setFigure(rook.getxCoordinate(), rook.getyCoordinate(), down ? 7 : 0, position.getPlayerWhite() ? 3 : 4);
                    position.setFigure(getxCoordinate(), getyCoordinate(), newX, newY);
                    position.enPassant.enPassantAllowed = false;
                    isMoved++;
                    rook.setMoved(rook.isMoved() + 1);
                    position.isWhiteToMove = !position.isWhiteToMove;
                    moveDescription = QUEENSIDE;
                    movesAfterCastling = 0;
                    isCastled = true;
                    return true;
                }
            }
            position.setFigure(getxCoordinate(), getyCoordinate(), newX, newY);
            position.enPassant.enPassantAllowed = false;
            isMoved++;
            position.isWhiteToMove = !position.isWhiteToMove;
            movesAfterCastling = movesAfterCastling >= 0 ? movesAfterCastling + 1 : movesAfterCastling;
        }

        //kingside castle
        if(difX == 0 && (position.getPlayerWhite() ? difY == 2 : difY == -2)) {
            if(!(position.getFigure(down ? 7 : 0, position.getPlayerWhite() ? 7 : 0) instanceof Rook) ||
                    !(position.getFigure(down ? 7 : 0, position.getPlayerWhite() ? 4 : 3) instanceof King)) {
                return false;
            }

            Rook rook = (Rook) position.getFigure(down ? 7 : 0, position.getPlayerWhite() ? 7 : 0);
            if(rook.isMoved() > 0 || isMoved > 0) {
                return false;
            }
            if(position.getFigure(down ? 7 : 0, position.getPlayerWhite() ? 6 : 1) != null ||
                    position.getFigure(down ? 7 : 0, position.getPlayerWhite() ? 5 : 2) != null) {
                return false;
            }
            if(position.numberOfAttackers(down ? 7 : 0, position.getPlayerWhite() ? 4 : 3, opponentColor) > 0 ||
                    position.numberOfAttackers(down ? 7 : 0, position.getPlayerWhite() ? 5 : 2, opponentColor) > 0 ||
                    position.numberOfAttackers(down ? 7 : 0, position.getPlayerWhite() ? 6 : 1, opponentColor) > 0) {

                return false;
            }

            position.setFigure(rook.getxCoordinate(), rook.getyCoordinate(), down ? 7 : 0, position.getPlayerWhite() ? 5 : 2);
            position.setFigure(getxCoordinate(), getyCoordinate(), newX, newY);

            position.enPassant.enPassantAllowed = false;
            isMoved++;
            rook.setMoved(rook.isMoved() + 1);
            position.isWhiteToMove = !position.isWhiteToMove;
            moveDescription = KINGSIDE;
            movesAfterCastling = 0;
            isCastled = true;
            MoveRecorder.record(this, false, position, newX, newY, this);

            return true;
        }

        //queenside castling
        if(difX == 0 && (position.getPlayerWhite() ? difY == -2 : difY == 2)) {
            if(!(position.getFigure(down ? 7 : 0, position.getPlayerWhite() ? 0 : 7) instanceof Rook) ||
                    !(position.getFigure(down ? 7 : 0, position.getPlayerWhite() ? 4 : 3) instanceof King)) {
                return false;
            }
            Rook rook = (Rook) position.getFigure(down ? 7 : 0, position.getPlayerWhite() ? 0 : 7);
            if(rook.isMoved() > 0 || isMoved > 0) {
                return false;
            }
            if(position.getFigure(down ? 7 : 0, position.getPlayerWhite() ? 1 : 6) != null ||
                    position.getFigure(down ? 7 : 0, position.getPlayerWhite() ? 2 : 5) != null) {
                return false;
            }
            if(position.numberOfAttackers(down ? 7 : 0, position.getPlayerWhite() ? 4 : 3, opponentColor) > 0 ||
                    position.numberOfAttackers(down ? 7 : 0, position.getPlayerWhite() ? 2 : 5, opponentColor) > 0 ||
                    position.numberOfAttackers(down ? 7 : 0, position.getPlayerWhite() ? 3 : 4, opponentColor) > 0) {
                return false;
            }

            position.setFigure(rook.getxCoordinate(), rook.getyCoordinate(), down ? 7 : 0, position.getPlayerWhite() ? 3 : 4);
            position.setFigure(getxCoordinate(), getyCoordinate(), newX, newY);
            position.enPassant.enPassantAllowed = false;

            isMoved++;
            rook.setMoved(rook.isMoved() + 1);
            position.isWhiteToMove = !position.isWhiteToMove;
            moveDescription = QUEENSIDE;
            movesAfterCastling = 0;
            isCastled = true;
            MoveRecorder.record(this, false, position, newX, newY, this);

            return true;
        }

        // normal move
        if(Math.abs(difY) > 1 || Math.abs(difX) > 1) {
            return false;
        }

        Figure figure = position.getFigure(newX, newY);
        if(figure != null && figure.getColor().equals(getColor())) {
            return false;
        }
        boolean isEating = figure != null;

        int x = getxCoordinate();
        int y = getyCoordinate();
        Figure f = position.getFigure(newX, newY);
        position.setFigure(x, y, newX, newY);
        if(position.numberOfAttackers(newX, newY, getColor()) > 0) {
            position.setFigureBack(newX, newY, x, y, f);
            return false;
        }
        position.enPassant.enPassantAllowed = false;
        isMoved++;
        position.isWhiteToMove = !position.isWhiteToMove;
        movesAfterCastling = movesAfterCastling >= 0 ? movesAfterCastling + 1 : movesAfterCastling;
        MoveRecorder.record(this, isEating, position, newX, newY, this);

        return true;
    }

    @Override
    public List<Coordinate> attacking(Position position) {
        List<Coordinate> coordinates = new ArrayList<>();
        int top = getxCoordinate() > 0 ? 1 : 0;
        int bottom = 7 - getxCoordinate() > 0 ? 1 : 0;
        int left = getyCoordinate() > 0 ? 1 : 0;
        int right = 7 - getyCoordinate() > 0 ? 1 : 0;
        int topLeft = Math.min(top, left);
        int topRight = Math.min(top, right);
        int bottomLeft = Math.min(bottom, left);
        int bottomRight = Math.min(bottom, right);

        if(top == 1) {
            Coordinate c = new Coordinate(getxCoordinate() - 1,  getyCoordinate());
            if(position.getFigure(c.getX(), c.getY()) == null ||
                    position.getFigure(c.getX(), c.getY()).getColor() != getColor()) {
                coordinates.add(c);
            }
        }
        if(bottom == 1) {
            Coordinate c = new Coordinate(getxCoordinate() + 1,  getyCoordinate());
            if(position.getFigure(c.getX(), c.getY()) == null ||
                    position.getFigure(c.getX(), c.getY()).getColor() != getColor()) {
                coordinates.add(c);
            }
        }
        if(left == 1) {
            Coordinate c = new Coordinate(getxCoordinate(),  getyCoordinate() - 1);
            if(position.getFigure(c.getX(), c.getY()) == null ||
                    position.getFigure(c.getX(), c.getY()).getColor() != getColor()) {
                coordinates.add(c);
            }
        }
        if(right == 1) {
            Coordinate c = new Coordinate(getxCoordinate(),  getyCoordinate() + 1);
            if(position.getFigure(c.getX(), c.getY()) == null ||
                    position.getFigure(c.getX(), c.getY()).getColor() != getColor()) {
                coordinates.add(c);
            }
        }
        if(topLeft == 1) {
            Coordinate c = new Coordinate(getxCoordinate() - 1,  getyCoordinate() - 1);
            if(position.getFigure(c.getX(), c.getY()) == null ||
                    position.getFigure(c.getX(), c.getY()).getColor() != getColor()) {
                coordinates.add(c);
            }
        }
        if(topRight == 1) {
            Coordinate c = new Coordinate(getxCoordinate() - 1,  getyCoordinate() + 1);
            if(position.getFigure(c.getX(), c.getY()) == null ||
                    position.getFigure(c.getX(), c.getY()).getColor() != getColor()) {
                coordinates.add(c);
            }
        }
        if(bottomLeft == 1) {
            Coordinate c = new Coordinate(getxCoordinate() + 1,  getyCoordinate() - 1);
            if(position.getFigure(c.getX(), c.getY()) == null ||
                    position.getFigure(c.getX(), c.getY()).getColor() != getColor()) {
                coordinates.add(c);
            }
        }
        if(bottomRight == 1) {
            Coordinate c = new Coordinate(getxCoordinate() + 1,  getyCoordinate() + 1);
            if(position.getFigure(c.getX(), c.getY()) == null ||
                    position.getFigure(c.getX(), c.getY()).getColor() != getColor()) {
                coordinates.add(c);
            }
        }
        return coordinates;
    }

    @Override
    public List<Coordinate> possibleMoves(Position position) {
        List<Coordinate> coordinates = attacking(position);
        boolean down = (getColor() == (position.getPlayerWhite() ? Color.WHITE : Color.BLACK)); //up or down player on the board
        List<Coordinate> coordinates2 = new ArrayList<>();
        if((position.getFigure(down ? 7 : 0, position.getPlayerWhite() ? 0 : 7) instanceof Rook) &&							//queenside castling
                (position.getFigure(down ? 7 : 0, position.getPlayerWhite() ? 4 : 3) instanceof King)) {
            Rook rook = (Rook) position.getFigure(down ? 7 : 0, position.getPlayerWhite() ? 0 : 7);
            if(rook.isMoved() == 0 && isMoved == 0) {
                if(position.getFigure(down ? 7 : 0, position.getPlayerWhite() ? 1 : 6) == null &&
                        position.getFigure(down ? 7 : 0, position.getPlayerWhite() ? 2 : 5) == null &&
                        position.getFigure(down ? 7 : 0, position.getPlayerWhite() ? 3 : 4) == null) {
                    if(position.numberOfAttackers(down ? 7 : 0, position.getPlayerWhite() ? 4 : 3, opponentColor) == 0 &&
                            position.numberOfAttackers(down ? 7 : 0, position.getPlayerWhite() ? 2 : 5, opponentColor) == 0 &&
                            position.numberOfAttackers(down ? 7 : 0, position.getPlayerWhite() ? 3 : 4, opponentColor) == 0) {
                        coordinates2.add(new Coordinate(down ? 7 : 0, position.getPlayerWhite() ? 2 : 5));
                    }
                }
            }
        }

        if((position.getFigure(down ? 7 : 0, position.getPlayerWhite() ? 7 : 0) instanceof Rook) &&
                (position.getFigure(down ? 7 : 0, position.getPlayerWhite() ? 4 : 3) instanceof King)) {
            Rook rook = (Rook) position.getFigure(down ? 7 : 0, position.getPlayerWhite() ? 7 : 0);
            if(rook.isMoved() == 0 && isMoved == 0) {
                if(position.getFigure(down ? 7 : 0, position.getPlayerWhite() ? 6 : 1) == null &&
                        position.getFigure(down ? 7 : 0, position.getPlayerWhite() ? 5 : 2) == null) {
                    if(position.numberOfAttackers(down ? 7 : 0, position.getPlayerWhite() ? 4 : 3, opponentColor) == 0 &&
                            position.numberOfAttackers(down ? 7 : 0, position.getPlayerWhite() ? 5 : 2, opponentColor) == 0 &&
                            position.numberOfAttackers(down ? 7 : 0, position.getPlayerWhite() ? 6 : 1, opponentColor) == 0) {
                        coordinates2.add(new Coordinate(down ? 7 : 0, position.getPlayerWhite() ? 6 : 1));
                    }
                }
            }
        }

        int x = getxCoordinate();
        int y = getyCoordinate();
        coordinates2.addAll(coordinates);
        for(Coordinate c : coordinates) {
            Figure f = position.getFigure(c);
            position.setFigure(getxCoordinate(), getyCoordinate(), c.getX(), c.getY());
            if(position.numberOfAttackers(c.getX(), c.getY(), opponentColor) > 0) {
                coordinates2.remove(c);
            }
            position.setFigureBack(c.getX(), c.getY(), x, y, f);
        }
        return coordinates2;
    }


    @Override
    public void recordMove(boolean isEating, Position position, int x, int y, Figure f, int... additionalArgs) {
        if(movesAfterCastling == 0 && moveDescription == KINGSIDE) {
            ChessUtil.move = (!position.isWhiteToMove ? ChessUtil.moveNumber + "." : "") + "0-0";
        } else if(movesAfterCastling == 0 && moveDescription == QUEENSIDE) {
            ChessUtil.move = (!position.isWhiteToMove ? ChessUtil.moveNumber + "." : "") + "0-0-0";
        } else {
            ChessUtil.move = (!position.isWhiteToMove ? ChessUtil.moveNumber + "." : "")  + getSymbol();
            if(isEating) {
                ChessUtil.move += "x";
            }
            ChessUtil.move += ChessUtil.abc.substring(y, y + 1) + ChessUtil.numbers.substring(x, x + 1);
        }
    }
}
