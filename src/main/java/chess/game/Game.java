package chess.game;

import chess.pieces.*;
import chess.util.Color;
import chess.util.Coordinate;

import javax.swing.JButton;

/**
 * Class represents one chess game.
 */
public class Game {

    /**
     * This position is used by all other classes when referencing a position.
     */
    private Position position;

    /**
     * Sets up starting position based on where is player white on the board.
     * @param playerWhite if true player with is on the down side of the board.
     */
    public Game(boolean playerWhite, boolean isWhiteToMove, JButton[][] buttons) {
        Figure[][] figures = new Figure[8][8];
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                //for up pieces
                Coordinate c = new Coordinate(i, j);
                if(i == 0) {
                    if(j == 0 || j == 7) {
                        Rook rook = new Rook(playerWhite ? Color.BLACK : Color.WHITE, c);
                        figures[i][j] = rook;
                    }
                    if(j == 1 || j == 6) {
                        Knight knight = new Knight(playerWhite ? Color.BLACK : Color.WHITE, c);
                        figures[i][j] = knight;
                    }
                    if(j == 2 || j == 5) {
                        Bishop bishop = new Bishop(playerWhite ? Color.BLACK : Color.WHITE, c);
                        figures[i][j] = bishop;
                    }
                    if(j == 3) {
                        if(playerWhite) {
                            Queen queen = new Queen(playerWhite ? Color.BLACK : Color.WHITE, c);
                            figures[i][j] = queen;
                        }
                        else {
                            King king = new King(playerWhite ? Color.BLACK : Color.WHITE, c);
                            figures[i][j] = king;
                        }

                    }
                    if(j == 4) {
                        if(playerWhite) {
                            King king = new King(playerWhite ? Color.BLACK : Color.WHITE, c);
                            figures[i][j] = king;
                        }
                        else {
                            Queen queen = new Queen(playerWhite ? Color.BLACK : Color.WHITE, c);
                            figures[i][j] = queen;
                        }

                    }
                }
                else if(i == 1) {
                    Pawn pawn = new Pawn(playerWhite ? Color.BLACK : Color.WHITE, c);
                    figures[i][j] = pawn;
                }
                // for down pieces
                else if(i == 7) {
                    if(j == 0 || j == 7) {
                        Rook rook = new Rook(playerWhite ? Color.WHITE : Color.BLACK, c);
                        figures[i][j] = rook;
                    }
                    if(j == 1 || j == 6) {
                        Knight knight = new Knight(playerWhite ? Color.WHITE : Color.BLACK, c);
                        figures[i][j] = knight;
                    }
                    if(j == 2 || j == 5) {
                        Bishop bishop = new Bishop(playerWhite ? Color.WHITE : Color.BLACK, c);
                        figures[i][j] = bishop;
                    }
                    if(j == 3) {
                        if(playerWhite) {
                            Queen queen = new Queen(playerWhite ? Color.WHITE : Color.BLACK, c);
                            figures[i][j] = queen;
                        }
                        else {
                            King king = new King(playerWhite ? Color.WHITE : Color.BLACK, c);
                            figures[i][j] = king;
                        }

                    }
                    if(j == 4) {
                        if(playerWhite) {
                            King king = new King(playerWhite ? Color.WHITE : Color.BLACK, c);
                            figures[i][j] = king;
                        }
                        else {
                            Queen queen = new Queen(playerWhite ? Color.WHITE : Color.BLACK, c);
                            figures[i][j] = queen;
                        }

                    }
                }
                else if(i == 6) {
                    Pawn pawn = new Pawn(playerWhite ? Color.WHITE : Color.BLACK, c);
                    figures[i][j] = pawn;
                }
                else {
                    figures[i][j] = null;
                }
            }
        }
        position = new Position(playerWhite, figures, isWhiteToMove);
    }

    public Position getPosition() {
        return position;
    }

    public  void setPosition(Position position) {
        this.position = position;
    }
}
