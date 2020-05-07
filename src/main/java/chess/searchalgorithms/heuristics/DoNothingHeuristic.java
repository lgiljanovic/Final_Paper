package chess.searchalgorithms.heuristics;

import chess.game.Position;
import chess.pieces.Figure;
import chess.util.Coordinate;
import chess.util.Move;

import java.util.ArrayList;
import java.util.List;

public class DoNothingHeuristic implements Heuristic {
    @Override
    public List<Move> sortByHeuristic(Position position, List<Figure> figures) {
        List<Move> moves = new ArrayList<>();

        for(Figure f : figures) {
            for(Coordinate c : f.possibleMoves(position)) {
                moves.add(new Move(f, c));
            }
        }
        return moves;
    }
}
