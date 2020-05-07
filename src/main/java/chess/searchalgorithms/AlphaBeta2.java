package chess.searchalgorithms;

import chess.game.Position;
import chess.searchalgorithms.evaluators.Evaluator;
import chess.pieces.Figure;
import chess.pieces.Pawn;
import chess.searchalgorithms.heuristics.Heuristic;
import chess.util.ChessUtil;
import chess.util.Color;
import chess.util.Coordinate;
import chess.util.Move;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlphaBeta2 implements SearchAlgorithm {
    private Position position;
    private Color computer;
    private Color player;
    private Pawn.EnPassant enPassant = new Pawn.EnPassant();
    private Map<Move, Double> candidates = new HashMap<>();
    private Heuristic heuristic;
    private Evaluator evaluator;

    public AlphaBeta2(Heuristic heuristic, Evaluator evaluator) {
        this.heuristic = heuristic;
        this.evaluator = evaluator;
    }

    private void setAttributes(Position position, Color computer) {
        this.position = new Position(position.getPlayerWhite(), position.copy(), position.isWhiteToMove);
        this.computer = computer;
        player = computer == Color.WHITE ? Color.BLACK : Color.WHITE;
        enPassant.enPassantAllowed = position.enPassant.enPassantAllowed;
        enPassant.x = position.enPassant.x;
        enPassant.y = position.enPassant.y;
        ChessUtil.recordMove = false;
    }

    @Override
    public Move findBestMove(Position position, Color computer) {
        setAttributes(position, computer);
        double score = alphaBetaMax(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, ChessUtil.DEPTH);
        position.enPassant.enPassantAllowed = enPassant.enPassantAllowed;
        position.enPassant.x = enPassant.x;
        position.enPassant.y = enPassant.y;
        Move bestMove = candidates.entrySet().stream().max(Comparator.comparing(Map.Entry::getValue)).get().getKey();
        ChessUtil.recordMove = true;
        candidates.clear();
        return bestMove;
    }


    private double alphaBetaMax(double alpha, double beta, int depth) {
        int result = ChessUtil.isCheckMateOrStalemate(position, player, computer);
        if(result == ChessUtil.CHECKMATE) {
            return ChessUtil.CHECKMATE;
        } else if(result == ChessUtil.STALEMATE) {
            return 0; // Stalemate real value is 0.
        }
        if(depth == 0) {
            return evaluator.evaluate(position);
        }

        List<Figure> figuresComp = position.getPiecesOfColor(computer);
        Figure[][] tmp = position.copyByReference();
        Pawn.EnPassant enPassant = new Pawn.EnPassant();
        enPassant.x = position.enPassant.x;
        enPassant.y = position.enPassant.y;
        enPassant.enPassantAllowed = position.enPassant.enPassantAllowed;
        List<Move> moves = heuristic.sortByHeuristic(position, figuresComp);
        double m = alpha;
        for(Move move : moves) {
            Figure f = move.getFigure();
            Coordinate c = move.getCoordinate();
            Coordinate previousCoordinate = new Coordinate(f.getxCoordinate(), f.getyCoordinate());
            if(depth == ChessUtil.DEPTH) {
                candidates.put(move, Double.NEGATIVE_INFINITY);
            }
            f.move(position, c.getX(), c.getY());
            double value = alphaBetaMin(m, beta, depth - 1);
            if(depth == ChessUtil.DEPTH) {
                candidates.put(move, value);
            }
            if(m < value) {
                m = value;
            }
            position.setPositionBack(tmp, f, previousCoordinate);
            position.enPassant.x = enPassant.x;
            position.enPassant.y = enPassant.y;
            position.enPassant.enPassantAllowed = enPassant.enPassantAllowed;
            if(m >= beta) {
                return beta;
            }
        }
        return m;


    }

    private double alphaBetaMin(double alpha, double beta, int depth) {
        int result = ChessUtil.isCheckMateOrStalemate(position, player, computer);
        if(result == ChessUtil.CHECKMATE) {
            return ChessUtil.CHECKMATE;
        } else if(result == ChessUtil.STALEMATE) {
            return 0; // Stalemate real value is 0.
        }
        if(depth == 0) {
            return evaluator.evaluate(position);
        }
        List<Figure> figuresPlayer = position.getPiecesOfColor(player);
        Figure[][] tmp = position.copyByReference();
        Pawn.EnPassant enPassant = new Pawn.EnPassant();
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
            m = Math.min(m, alphaBetaMax(alpha, m, depth - 1));
            position.setPositionBack(tmp, f, previousCoordinate);
            position.enPassant.x = enPassant.x;
            position.enPassant.y = enPassant.y;
            position.enPassant.enPassantAllowed = enPassant.enPassantAllowed;
            if(m <= alpha) {
                return alpha;
            }
        }
        return m;

    }
}
