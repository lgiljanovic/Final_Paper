package chess.moverecorders;

import chess.game.Position;
import chess.pieces.Figure;
import chess.util.ChessUtil;

/**
 * StandardAlgebraicNotation defines algebraic notation for recording figures. Default behaviour applies to all except {@link chess.pieces.Pawn} and {@link chess.pieces.King}
 */
public interface StandardAlgebraicNotation {

    /**
     * Method appends move to list of played moves in standard chess notation. Method is accessing {@link Position#isWhiteToMove} flag to append
     * move number if it was white to move, and expects that this flag is changed before calling this method.
     * @param isEating will this figure eating other with this move
     * @param position position after the move
     * @param x x coordinate of move
     * @param y y coordinate of move
     * @param f figure to be moved
     */
    default void recordMove(boolean isEating, Position position, int x, int y, Figure f, int... additionalArgs) {
        ChessUtil.move = (!position.isWhiteToMove ? ChessUtil.moveNumber + "." : "") + f.getSymbol();
        if(isEating) {
            ChessUtil.move += "x";
        }
        ChessUtil.move += ChessUtil.abc.substring(y, y + 1) + ChessUtil.numbers.substring(x, x + 1);
    }
}
