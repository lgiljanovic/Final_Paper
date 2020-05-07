package chess.searchalgorithms.heuristics;

import chess.game.Position;
import chess.pieces.Figure;
import chess.util.Move;

import java.util.List;

/**
 * This interface is used in tree search algorithms as heuristic function. It sorts current nodes (nodes of the same depth) by implemented heuristic.
 * As a result nodes that seem more promising will expand to their child nodes first.
 *
 * @author lukag
 */
public interface Heuristic {

    /**
     * Method sorts through all possible moves for every given figure using heuristic function.
     * @param position current position
     * @param figures figures off current player/computer.
     * @return sorted map of {@link Move}.
     */
    List<Move> sortByHeuristic(Position position, List<Figure> figures);
}
