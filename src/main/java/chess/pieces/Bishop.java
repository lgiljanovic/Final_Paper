package chess.pieces;

import chess.game.Position;
import chess.util.ChessUtil;
import chess.util.Color;
import chess.util.Coordinate;
import java.util.List;

/**
 * Bishop is chess {@link Figure}. Bishop's value is 3 and symbol is 'B'.
 */
public class Bishop extends  Figure {

    /**
     * Basic constructor that takes all attributes except icon which is initialized using util class.
     * Value of the bishop is initialized to 3.
     */
    public Bishop(Color color, Coordinate coordinate) {
        super(color, 'B',3, coordinate);
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
