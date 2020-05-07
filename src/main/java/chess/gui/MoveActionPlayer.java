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


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class defines action that is performed when player clicks on some square of the board.
 * @author lukag
 * @version 1.1
 */
public class MoveActionPlayer implements ActionListener {

    /**
     * X coordinate of the square that was clicked.
     */
    private int x;

    /**
     * Y coordinate of the square that aws clicked.
     */
    private int y;

    /**
     * Current position.
     */
    private Position position;

    /**
     * Buttons of the {@link MainFrame}.
     */
    private JButton[][] boardButtons;

    /**
     * Text area for move list.
     */
    private JTextArea moveList;

    /**
     * Algorithm used for generating computer's move.
     */
    private SearchAlgorithm algorithm;

    /**
     * Basic constructor with {@link SearchAlgorithm} indicating that computer is playing.
     * @param x {@link #x}
     * @param y {@link #y}
     * @param position {@link #position}
     * @param boardButtons {@link #boardButtons}
     * @param algorithm {@link SearchAlgorithm}
     */
    public MoveActionPlayer(int x, int y, Position position, JButton[][] boardButtons, JTextArea moveList, SearchAlgorithm algorithm) {
        this(x, y, position, boardButtons, moveList);
        this.algorithm = algorithm;
    }

    /**
     * Basic constructor.
     * @param x {@link #x}
     * @param y {@link #y}
     * @param position {@link #position}
     * @param boardButtons {@link #boardButtons}
     */
    public MoveActionPlayer(int x, int y, Position position, JButton[][] boardButtons, JTextArea moveList) {
        this.x = x;
        this.y = y;
        this.position = position;
        this.boardButtons = boardButtons;
        this.moveList = moveList;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Action action = new Action();
        Thread thread = new Thread(action);
        thread.start();

    }

    /**
     * First click defines the figure to be moved, so if a square without figure is clicked method does nothing.
     * Second click defines new square for clicked figure, if that moves is possible {@link Figure#move} is called. After that gui is properly changed to position after the move.
     * Also, move list in JTextArea is updated.
     */
    private class Action implements Runnable {

        @Override
        public void run() {
            if(!ChessUtil.clicked && !ChessUtil.computersMove) {
                Figure figure = position.getFigure(x, y);
                if(figure != null){
                    if((figure.getColor() == Color.WHITE && position.isWhiteToMove) || (figure.getColor() == Color.BLACK && !position.isWhiteToMove)) {
                        boardButtons[x][y].setEnabled(false);
                        ChessUtil.clicked = true;
                    }
                }
                return;
            }

            for(int i = 0; i < 8; i++) {
                for(int j = 0; j < 8; j++) {
                    if(!boardButtons[i][j].isEnabled()) {
                        Figure figure = position.getFigure(i, j);
                        if(!figure.possibleMoves(position).contains(new Coordinate(x, y))) {
                            ChessUtil.clicked = false;
                            boardButtons[i][j].setEnabled(true);
                            return;
                        }

                        if(figure.move(position, x, y)) {
                            boardButtons[i][j].setEnabled(true);
                            boardButtons[i][j].setIcon(null);
                            if (!(figure instanceof Pawn) || x != 7 && x != 0) {
                                boardButtons[x][y].setIcon(figure.getIcon());
                            } else {
                                createPromotingWindow(position, x, y, boardButtons, figure.getColor());
                                if(ChessUtil.recordMove) {
                                    Figure f = position.getFigure(x, y);
                                    ChessUtil.move += "=" + f.getSymbol();
                                    if(f.attacking(position).contains(position.getFigureCoordinate(f.getColor() == Color.WHITE ? Color.BLACK : Color.WHITE, 'K'))) {
                                        ChessUtil.move += "+ ";
                                    }
                                }
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
                            ChessUtil.clicked = false;

                            String[] lines = moveList.getText().split("\n");
                            if(lines[lines.length - 1].length() > 16) {
                                moveList.append("\n");
                            }

                            Color opponentColor = position.getPlayerWhite() ? Color.BLACK : Color.WHITE;
                            Coordinate kingCoordinate = position.getFigureCoordinate(opponentColor, 'K');
                            if (position.allPossibleMoves(opponentColor) == 0) {
                                if (position.numberOfAttackers(kingCoordinate.getX(), kingCoordinate.getY(), figure.getColor()) > 0) {
                                    WinnerFrame winner = new WinnerFrame(opponentColor == Color.WHITE ? Color.BLACK : Color.WHITE, "checkmate");
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
                            if(ChessUtil.computerPlaying) {
                                MoveActionComputer comp = new MoveActionComputer(position, boardButtons, position.getPlayerWhite() ? Color.BLACK : Color.WHITE, moveList, algorithm);
                                comp.start();
                            }
                            return;
                        } else {
                            ChessUtil.clicked = false;
                            boardButtons[i][j].setEnabled(true);
                            return;
                        }
                    }
                }
            }
        }

    }

    /**
     * Method create {@link PromoteWindow} for player to choose promoting figure. Game cannot continue until new window is closed.
     * @param position current position
     * @param newX  x coordinate of promotion
     * @param newY  y coordinate of promotion
     * @param boardButtons boardButtons
     * @param color color of player promoting
     */
    private void createPromotingWindow(Position position, int newX, int newY, JButton[][] boardButtons, Color color) {
        PromoteWindow promote = new PromoteWindow(color, newX, newY, position, boardButtons);
        promote.setVisible(true);
        promote.setLocation(300, 200);
        promote.pack();
        synchronized (ChessUtil.promoting) {
            while(promote.isVisible()) {
                try {
                    ChessUtil.promoting.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
