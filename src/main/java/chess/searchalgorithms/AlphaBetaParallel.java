package chess.searchalgorithms;

import chess.game.Position;
import chess.searchalgorithms.evaluators.Evaluator;
import chess.pieces.Figure;
import chess.pieces.Pawn.EnPassant;
import chess.searchalgorithms.heuristics.Heuristic;
import chess.util.ChessUtil;
import chess.util.Color;
import chess.util.Coordinate;
import chess.util.Move;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * This is {@link AlphaBeta2} algorithm implemented with multithreaded environment.
 * It works with thread pool of size {@link Runtime#availableProcessors()}.
 *
 * @author lukag
 * @version 1.1
 */
public class AlphaBetaParallel implements SearchAlgorithm {
    private Position position;
    private Color computer;
    private Color player;
    private EnPassant enPassant = new EnPassant();
    private ExecutorService pool;
    private Heuristic heuristic;
    private Evaluator evaluator;

    public AlphaBetaParallel(Heuristic heuristic, Evaluator evaluator) {
        this.heuristic = heuristic;
        this.evaluator = evaluator;
    }

    private void setAttrbiutes(Position position, Color computer) {
        pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        this.position = position;
        this.computer = computer;
        player = computer == Color.WHITE ? Color.BLACK : Color.WHITE;
        enPassant.enPassantAllowed = position.enPassant.enPassantAllowed;
        enPassant.x = position.enPassant.x;
        enPassant.y = position.enPassant.y;
        ChessUtil.recordMove = false;
    }

    @Override
    public Move findBestMove(Position position, Color computer) {
        setAttrbiutes(position, computer);
        Move move = find();
        position.enPassant.enPassantAllowed = enPassant.enPassantAllowed;
        position.enPassant.x = enPassant.x;
        position.enPassant.y = enPassant.y;
        ChessUtil.recordMove = true;
        return move;
    }

    /**
     *  Method iterates through computer's possible moves and wraps them to class {@link Task} for multithreaded execution.
     * @return
     */
    private Move find() {
        Map<Move, Future<Double>> results = new HashMap<>();
        List<Figure> figuresComp = position.getPiecesOfColor(computer);
        try {
            for (Figure f : figuresComp) {
                for (Coordinate c : f.possibleMoves(position)) {
                    results.put(new Move(f, c), pool.submit(
                            new Task(new Position(
                                    position.getPlayerWhite(), position.copy(), position.isWhiteToMove), f, c, evaluator)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        pool.shutdown();
        try {
            pool.awaitTermination(24L, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return results.entrySet().stream().max((e1, e2) -> {
            try {
                return e1.getValue().get().compareTo(e2.getValue().get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            return 0;
        }).get().getKey();
    }



    /**
     * Task represents one branch in tree search. There are as many tasks as computer's possible moves on current move.
     * Tasks are executed as separated thread that performs Minmax algorithm combined with AlphaBeta pruning.
     */
    private class Task implements Callable<Double> {

        /**
         * Copy of current position in game.
         */
        private Position position;

        /**
         * Position evaluator.
         */
        private Evaluator evaluator;

        /**
         * Makes move defined with provided figure and coordinate.
         * @param position copy of current position in the game
         * @param figure figure to be moved
         * @param coordinate coordinate where #figure will be moved to
         */
        private Task(Position position, Figure figure, Coordinate coordinate, Evaluator evaluator) {
            this.position = position;
            Figure f = this.position.getFigure(figure.getxCoordinate(), figure.getyCoordinate());
            f.move(this.position, coordinate.getX(), coordinate.getY());
            this.evaluator = evaluator;
        }

        @Override
        public Double call() {
            return alphaBeta(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, ChessUtil.DEPTH - 1, false);
        }

        /**
         * Alpha-beta pruning.
         */
        private double alphaBeta(double alpha, double beta, int depth, boolean isMaximizing) {
            int result = ChessUtil.isCheckMateOrStalemate(position, player, computer);
            if(result == ChessUtil.CHECKMATE) {
                return ChessUtil.CHECKMATE;
            } else if(result == ChessUtil.STALEMATE) {
                return 0; // Stalemate real value is 0.
            }
            if (depth == 0) {
                return evaluator.evaluate(position);
            }
            if (isMaximizing) {
                List<Figure> figuresComp = position.getPiecesOfColor(computer);
                Figure[][] tmp = position.copyByReference();
                EnPassant enPassant = new EnPassant();
                enPassant.x = position.enPassant.x;
                enPassant.y = position.enPassant.y;
                enPassant.enPassantAllowed = position.enPassant.enPassantAllowed;
                double m = alpha;
                List<Move> moves = heuristic.sortByHeuristic(position, figuresComp);
                for(Move move : moves) {
                    Figure f = move.getFigure();
                    Coordinate c = move.getCoordinate();
                    Coordinate previousCoordinate = new Coordinate(f.getxCoordinate(), f.getyCoordinate());
                    f.move(position, c.getX(), c.getY());
                    m = Math.max(m, alphaBeta(m, beta, depth - 1, false));
                    position.setPositionBack(tmp, f, previousCoordinate);
                    position.enPassant.x = enPassant.x;
                    position.enPassant.y = enPassant.y;
                    position.enPassant.enPassantAllowed = enPassant.enPassantAllowed;
                    if (m >= beta) {
                        return beta;
                    }
                }
                return m;
            } else {
                List<Figure> figuresPlayer = position.getPiecesOfColor(player);
                Figure[][] tmp = position.copyByReference();
                EnPassant enPassant = new EnPassant();
                enPassant.x = position.enPassant.x;
                enPassant.y = position.enPassant.y;
                enPassant.enPassantAllowed = position.enPassant.enPassantAllowed;
                List<Move> moves = heuristic.sortByHeuristic(position, figuresPlayer);
                double m = beta;
                for(Move move : moves) {
                    Figure f = move.getFigure();
                    Coordinate c = move.getCoordinate();
                    Coordinate previousCoordinate = new Coordinate(f.getxCoordinate(), f.getyCoordinate());
                    f.move(position, c.getX(), c.getY());
                    m = Math.min(m, alphaBeta(alpha, m, depth - 1, true));
                    position.setPositionBack(tmp, f, previousCoordinate);
                    position.enPassant.x = enPassant.x;
                    position.enPassant.y = enPassant.y;
                    position.enPassant.enPassantAllowed = enPassant.enPassantAllowed;
                    if (m <= alpha) {
                        return alpha;
                    }
                }
                return m;
            }
        }
    }

}

