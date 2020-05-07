package chess.pieces;

import chess.game.Position;
import chess.moverecorders.MoveRecorder;
import chess.util.ChessUtil;
import chess.util.Color;
import chess.util.Coordinate;

import java.util.List;

/**
 * Rook is a chess {@link Figure}. Rook's value is 5 and symbol is 'R'.
 */
public class Rook extends Figure {

    /**
     * Indicates whether the rook has moved and how much time. This is used for castling rights.
     */
    private int isMoved = 0;

    /**
     * Basic constructor that takes all attributes except icon which is initialized using util class.
     */
    public Rook(Color color, Coordinate coordinate) {
        super(color, 'R', 5, coordinate);
    }

    @Override
    public boolean move(Position position, int newX, int newY) {
        if(!ChessUtil.recordMove) {
            position.setFigure(getxCoordinate(), getyCoordinate(), newX, newY);
            position.enPassant.enPassantAllowed = false;
            position.isWhiteToMove = !position.isWhiteToMove;
            isMoved++;
            return true;
        }

        if(possibleMoves(position).contains(new Coordinate(newX, newY))) {
            boolean isEating = position.getFigure(newX, newY) != null;
            position.setFigure(getxCoordinate(), getyCoordinate(), newX, newY);
            position.enPassant.enPassantAllowed = false;
            position.isWhiteToMove = !position.isWhiteToMove;
            isMoved++;
            MoveRecorder.record(this, isEating, position, newX, newY, this);
            return true;
        }
        return false;
    }

    @Override
    public List<Coordinate> attacking(Position position) {
        return ChessUtil.attackOrDefend(this, true, position);
    }

    @Override
    public List<Coordinate> possibleMoves(Position position) {
        List<Coordinate> coordinates = attacking(position);
        return ChessUtil.filterPossibleMoves(position, this, coordinates);
    }

    public int isMoved() {
        return isMoved;
    }

    /**
     * {@link #isMoved} cannot be lesser than 0. When #moved is lesser than 0, method stores 0 to {@link #isMoved}.
     * @param moved
     */
    public void setMoved(int moved) {
        if(moved < 0) {
            isMoved = 0;
            return;
        }
        isMoved = moved;
    }
}
