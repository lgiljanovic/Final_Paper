package chess.searchalgorithms.evaluators;

import chess.game.Position;
import chess.pieces.Figure;
import chess.pieces.King;
import chess.pieces.Pawn;
import chess.pieces.Queen;
import chess.pieces.Rook;
import chess.util.ChessUtil;
import chess.util.Color;

/**
 * SimplePositionEvaluator evaluates position based on human knowledge. It is split in two categories: opening and middlegame and they are not evaluated with same parameters.
 * It evaluates material so that it always thrives for maximum material advantage, from that comes its tactical abilities.
 * Also, for every figure there is some positional value. For example, number of squares figure is attacking ({@link Figure#attacking}).
 */
public class SimplePositionEvaluator implements Evaluator {

    @Override
    public double evaluate(Position position) {
        Color comp = position.getPlayerWhite() ? Color.BLACK : Color.WHITE;
        Color player = position.getPlayerWhite() ? Color.WHITE : Color.BLACK;
        double result = 0;
        if(ChessUtil.moveNumber <= 8) {
            result += evaluateOpening(position, comp);
        } else {
            result += evaluatePositionalMiddleGame(position, comp);
        }
        return result;
    }


    /**
     * Evaluates opening, priority is to develop minor pieces (Bihsop and Knight), castle, and secure center with central pawns.
     */
    private double evaluateOpening(Position position, Color color) {
        int comp = 0;
        int player = 0;
        Figure[][] figures = position.getPosition();
        double result = 0;
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                Figure figure = figures[i][j];
                if(figure != null) {
                    if(figure.getColor() == color) {
                        comp += figure.getValue() * 150;
                        if(figure instanceof King) {
                            result += ((King) figure).isCastled() ? 50 : 0;
                            continue;
                        }
                        if(figure instanceof Pawn && (i == 4 && (j == 4 || j == 5))) {
                            result += 10;
                            continue;
                        }
                        if(!(figure instanceof Queen) && !(figure instanceof Pawn) && !(figure instanceof Rook)) {
                            result += figure.attacking(position).size() * 5;
                        }
                    } else {
                        player += figure.getValue() * 150;
                        if(figure instanceof King) {
                            result -= ((King) figure).isCastled() ? 50 : 0;
                            continue;
                        }
                        if(figure instanceof Pawn && (i == 4 && (j == 4 || j == 5))) {
                            result -= 10;
                            continue;
                        }
                        if(!(figure instanceof Pawn) && !(figure instanceof Queen) && !(figure instanceof Rook)) {
                            result -= figure.attacking(position).size() * 5;
                        }
                    }
                }
            }
        }
        return result + comp - player;
    }

    /**
     * Evaluates middle game. Figure placement is more important as is king's safety (castling).
     */
    private double evaluatePositionalMiddleGame(Position position, Color color) {
        int comp = 0;
        int player = 0;
        Figure[][] figures = position.getPosition();
        double result = 0;
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                Figure figure = figures[i][j];
                if(figure != null) {
                    if(figure.getColor() == color) {
                        comp += figure.getValue() * 100;
                        if(figure instanceof King) {
                            result += ((King) figure).isCastled() ? 100 : 0;
                            continue;
                        }
                        if(!(figure instanceof Pawn)) {
                            result += figure.attacking(position).size() * 10;
                        }
                    } else {
                        player += figure.getValue() * 100;
                        if(figure instanceof King) {
                            result -= ((King) figure).isCastled() ? 100 : 0;
                            continue;
                        }
                        if(!(figure instanceof Pawn)) {
                            result -= figure.attacking(position).size() * 10;
                        }
                    }
                }
            }
        }
        return  result + comp - player;
    }
}
