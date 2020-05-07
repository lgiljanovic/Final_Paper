package chess.pieces;

import chess.game.Position;
import chess.moverecorders.MoveRecorder;
import chess.util.ChessUtil;
import chess.util.Color;
import chess.util.Coordinate;

import java.util.ArrayList;
import java.util.List;

/**
 * Knight is chess {@link Figure}. Knight's value is 3 and symbol is 'N'.
 */
public class Knight extends Figure {


    /**
     * Basic constructor that takes all attributes except icon which is initialized using util class.
     * Value of the knight is initialized to 3.
     */
    public Knight(Color color, Coordinate coordinate) {
        super(color, 'N', 3, coordinate);
    }

    @Override
    public boolean move(Position position, int newX, int newY) {
        if(!ChessUtil.recordMove) {
            position.setFigure(getxCoordinate(), getyCoordinate(), newX, newY);     // recordMove is false when computer is calculating,
            position.enPassant.enPassantAllowed = false;                            // computer already knows possible moves and (newX, newY) is correct move, so
            position.isWhiteToMove = !position.isWhiteToMove;                       // there is no need to check possible in this method.
        }

        int difX = Math.abs(newX - getxCoordinate());
        int difY = Math.abs(newY - getyCoordinate());

        if((difX == 2 && difY == 1) || (difX == 1 && difY == 2)) {
            Figure figure = position.getFigure(newX, newY);
            if(figure == null || figure.getColor() != getColor()) {
                boolean isEating = position.getFigure(newX, newY) != null;
                position.setFigure(getxCoordinate(), getyCoordinate(), newX, newY);
                position.enPassant.enPassantAllowed = false;
                position.isWhiteToMove = !position.isWhiteToMove;
                MoveRecorder.record(this, isEating, position, newX, newY, this);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Coordinate> attacking(Position position) {
        List<Coordinate> coordinates = new ArrayList<>();
        Coordinate[] coords = new Coordinate[] { new Coordinate(1, 2, true), new Coordinate(2, 1, true),
                new Coordinate(-1, 2, true), new Coordinate(-2, 1, true),
                new Coordinate(1, -2, true), new Coordinate(-1, -2, true),
                new Coordinate(2, -1, true),  new Coordinate(-2, -1, true)};

        for(int i = 0; i < coords.length; i++) {
            try {
                Coordinate coordinate = new Coordinate(getxCoordinate() + coords[i].getX(),
                        getyCoordinate() + coords[i].getY());
                Figure figure = position.getFigure(coordinate.getX(), coordinate.getY());
                if(figure != null && figure.getColor() == getColor()) {
                    continue;
                }
                coordinates.add(coordinate);
            }
            catch(IllegalArgumentException e) {
                // do nothing, exception is acceptable and expected.
            }
        }
        return coordinates;
    }

    @Override
    public List<Coordinate> possibleMoves(Position position) {
        List<Coordinate> coordinates = attacking(position);
        return ChessUtil.filterPossibleMoves(position, this, coordinates);
    }

}
