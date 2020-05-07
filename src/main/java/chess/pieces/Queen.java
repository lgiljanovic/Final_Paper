package chess.pieces;

import chess.game.Position;
import chess.util.ChessUtil;
import chess.util.Color;
import chess.util.Coordinate;

import java.util.List;

/**
 * Queen is a chess {@link Figure}. Queen's value is 9 and symbol is 'Q'.
 */
public class Queen extends  Figure {


    /**
     * Basic constructor that takes all attributes except icon which is initialized using util class.
     * Value of the queen is initialized to 9.
     */
    public Queen(Color color, Coordinate coordinate) {
        super(color, 'Q', 9, coordinate);
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
}
