package chess.searchalgorithms.heuristics;

import chess.game.Position;
import chess.pieces.Figure;
import chess.util.Coordinate;
import chess.util.Move;

import java.util.ArrayList;
import java.util.List;

public class CheckTakingHeuristic implements Heuristic {
    @Override
    public List<Move> sortByHeuristic(Position position, List<Figure> figures) {
        List<Move> moves = new ArrayList<>();
        for(Figure f : figures) {
            for(Coordinate c : f.possibleMoves(position)) {
                if(moves.size() == 0) {
                    moves.add(new Move(f, c));
                } else {
                    int i = 0;
                    int value = position.getFigure(c.getX(), c.getY()) == null ? 0 : position.getFigure(c.getX(), c.getY()).getValue();
                    while(i < moves.size()) {
                        Figure figure = position.getFigure(moves.get(i).getCoordinate().getX(), moves.get(i).getCoordinate().getY());
                        int value2 = figure == null ? 0 : figure.getValue();
                        if(value > value2) {
                            break;
                        }
                        i++;
                    }
                    moves.add(i, new Move(f, c));
                }
            }
        }
        return moves;
    }
}
