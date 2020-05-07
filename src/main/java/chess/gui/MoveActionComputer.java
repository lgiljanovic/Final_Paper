package chess.gui;

import chess.game.Position;
import chess.pieces.Figure;
import chess.pieces.King;
import chess.pieces.Pawn;
import chess.pieces.Rook;
import chess.searchalgorithms.SearchAlgorithm;
import chess.util.ChessUtil;
import chess.util.Color;
import chess.util.Coordinate;
import chess.util.Move;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import java.awt.Font;
import java.util.Objects;

/**
 * Class defines action for computer to make a move on the board.
 */
public class MoveActionComputer {

    /**
     * Current position.
     */
    private Position position;

    /**
     * Buttons of the {@link MainFrame}.
     */
    private JButton[][] boardButtons;

    /**
     * Color of the computer (White/Black).
     */
    private Color computersColor;

    /**
     * Text area for move list.
     */
    private JTextArea moveList;

    /**
     * Algorithm used for generating computer's move.
     */
    private SearchAlgorithm algorithm;

    /**
     * Basic constructor.
     * @param position {@link #position}
     * @param buttons {@link #boardButtons}
     * @param color {@link #computersColor}
     */
    public MoveActionComputer(Position position, JButton[][] buttons, Color color, JTextArea moveList, SearchAlgorithm algorithm) {
        this.position = Objects.requireNonNull(position);
        boardButtons = Objects.requireNonNull(buttons);
        computersColor = Objects.requireNonNull(color);
        this.moveList = Objects.requireNonNull(moveList);
        this.algorithm = Objects.requireNonNull(algorithm);
    }

    /**
     * Method is used for making computer's move.
     * Method calls some implementation of {@link SearchAlgorithm#findBestMove}. After algorithm returns the move, gui is properly changed to position after the move.
     * Also, move list in JTextArea is updated.
     */
    public void start() {
        ChessUtil.computersMove = true;
        try {
            Move move = algorithm.findBestMove(position, computersColor);
            Figure figure = move.getFigure();

            int x = figure.getxCoordinate();
            int y = figure.getyCoordinate();
            int newX = move.getCoordinate().getX();
            int newY = move.getCoordinate().getY();
            if(figure.move(position, newX, newY)) {
                boardButtons[x][y].setIcon(null);
                Color player = computersColor == Color.WHITE ? Color.BLACK : Color.WHITE;
                if(!(figure instanceof Pawn) || newX != 7 && newX != 0) {
                    boardButtons[newX][newY].setIcon(figure.getIcon());
                } else {
                    boardButtons[newX][newY].setIcon(position.getFigure(newX, newY).getIcon());
                }

                if(figure instanceof King) {
                    boolean down = (figure.getColor() == (position.getPlayerWhite() ? Color.WHITE : Color.BLACK)); //up or down player on the board
                    if(((King) figure).moveDescription == King.KINGSIDE) {
                        Rook rook = (Rook) position.getFigure(down ? 7 : 0, position.getPlayerWhite() ? 5 : 2);
                        boardButtons[down ? 7 : 0][position.getPlayerWhite() ? 7 : 0].setIcon(null);
                        boardButtons[rook.getxCoordinate()][rook.getyCoordinate()].setIcon(rook.getIcon());
                        ((King) figure).moveDescription = King.NORMAL;
                    } else if(((King) figure).moveDescription == King.QUEENSIDE) {
                        Rook rook = (Rook) position.getFigure(down ? 7 : 0, position.getPlayerWhite() ? 3 : 4);
                        boardButtons[down ? 7 : 0][position.getPlayerWhite() ? 0 : 7].setIcon(null);
                        boardButtons[rook.getxCoordinate()][rook.getyCoordinate()].setIcon(rook.getIcon());
                        ((King) figure).moveDescription = King.NORMAL;
                    }
                }
                if(figure instanceof Pawn && ((Pawn) figure).enPassant) {
                    boardButtons[position.enPassant.x][position.enPassant.y].setIcon(null);
                    ((Pawn) figure).enPassant = false;
                }

                if(!position.isWhiteToMove) {
                    ChessUtil.moveNumber++;
                }

                String[] lines = moveList.getText().split("\n");
                if(lines[lines.length - 1].length() > 16) {
                    moveList.append("\n");
                }
                Coordinate kingCoordinate = position.getFigureCoordinate(player, 'K');
                if(position.allPossibleMoves(player) == 0) {
                    if(position.numberOfAttackers(kingCoordinate.getX(), kingCoordinate.getY(), computersColor) > 0) {
                        WinnerFrame winner = new WinnerFrame(computersColor, "checkmate");
                        winner.pack();
                        winner.setLocation(300, 200);
                        winner.setVisible(true);
                        ChessUtil.move = ChessUtil.move.substring(0, ChessUtil.move.length() - 2) + "# ";
                        ChessUtil.move += position.isWhiteToMove ? "0-1" : "1-0";
                        moveList.append(ChessUtil.move);
                        return;
                    } else {
                        JFrame draw = new JFrame("Draw");
                        JLabel drawLabel = new JLabel("Draw by stalemate");
                        draw.add(drawLabel);
                        drawLabel.setFont(new Font("Draw", Font.BOLD, 20));
                        draw.setLocation(300, 200);
                        draw.pack();
                        draw.setVisible(true);
                        ChessUtil.move += "1/2-1/2";
                        moveList.append(ChessUtil.move);
                        return;
                    }
                }
                moveList.append(ChessUtil.move);
                ChessUtil.computersMove = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
