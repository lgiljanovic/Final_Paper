package chess.moverecorders;

import chess.game.Position;
import chess.pieces.Figure;
import chess.util.ChessUtil;
import chess.util.Color;
import chess.util.Coordinate;

/**
 * Records the latest move appending it to move list presented in {@link javax.swing.JTextArea}. It is called by figure after move is played to update move list.
 */
public class MoveRecorder {

    /**
     * First calls {@link StandardAlgebraicNotation#recordMove} for specific algebraic notation of figures, then checks whether the move is also check.
     * @param san {@link StandardAlgebraicNotation} for figure
     * @param isEating is some figure eaten with this move
     * @param position {@link Position}
     * @param x new x coordinate
     * @param y new y coordinate
     * @param f figure to be moved
     * @param additionalArgs additional arguments for {@link StandardAlgebraicNotation#recordMove}
     */
    public static void record(StandardAlgebraicNotation san, boolean isEating, Position position, int x, int y, Figure f, int... additionalArgs) {
        san.recordMove(isEating, position, x, y, f, additionalArgs);
        Coordinate oppKing = position.getFigureCoordinate(f.getColor() == Color.WHITE ? Color.BLACK : Color.WHITE, 'K');
        if(position.numberOfAttackers(oppKing.getX(), oppKing.getY(), f.getColor()) > 0) {
            ChessUtil.move += "+ ";
        } else {
            ChessUtil.move += " ";
        }
    }
}
