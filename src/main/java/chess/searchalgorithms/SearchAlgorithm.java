package chess.searchalgorithms;

import chess.game.Position;
import chess.util.Color;
import chess.util.Move;

/**
 * Interface SearchAlgorithm represents algorithm used in board games, in are case for game of chess. For given position algorithm calculates best move
 * based on provided position evaluator. In its most basic implementation, algorithm searches through all possible moves and then for chosen move searches through
 * all possible moves etc. to some depth.
 *
 * @author lukag
 */
public interface SearchAlgorithm {

    /**
     * For given position method calculates and returns best move to play.
     * @return best move
     */
    Move findBestMove(Position position, Color computer);
}
