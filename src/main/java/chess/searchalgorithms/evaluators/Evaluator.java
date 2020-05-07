package chess.searchalgorithms.evaluators;

import chess.game.Position;

/**
 * Interface defines behaviour of chess position evaluator.
 * It's task is to evaluate any given position and return double representing value of position (like fitness function).
 *
 * @author lukag
 */
public interface Evaluator {

    /**
     * Method evaluates given position.
     * @param position position of chess game
     * @return fitness of position
     */
    double evaluate(Position position);
}
