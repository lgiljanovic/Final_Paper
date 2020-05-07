package chess.gui;

import chess.game.Position;
import chess.pieces.Bishop;
import chess.pieces.Figure;
import chess.pieces.Knight;
import chess.pieces.Queen;
import chess.pieces.Rook;
import chess.util.ChessUtil;
import chess.util.Color;
import chess.util.Coordinate;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 * Popup window that occurs when pawn is promoting.
 */
public class PromoteWindow extends JFrame {

    /**
     * Buttons of the {@link MainFrame}.
     */
    private JButton[][] boardButtons;

    /**
     * Possible pieces for promotion.
     */
    private JButton[] buttons = new JButton[4];

    /**
     * Color of player promoting.
     */
    private Color color;

    /**
     * Current position.
     */
    private Position position;

    /**
     * Square where pawn is promoting.
     */
    private Coordinate coordinate;

    /**
     * Basic constructor.
     */
    public PromoteWindow(Color color, int x, int y, Position position, JButton[][] boardButtons) {
        this.color = color;
        this.coordinate = new Coordinate(x, y);
        this.position = position;
        this.boardButtons = boardButtons;

        initGui();
    }

    private void initGui() {
        setLayout(new GridLayout(1, 4));
        try {
            URL icon = MainFrame.class.getClassLoader().getResource("gameIcon.png");
            BufferedImage bufferedImage = ImageIO.read(icon);
            setIconImage(bufferedImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        buttons[0] = new JButton();
        buttons[0].setIcon(ChessUtil.getIconFor(color, 'Q'));
        buttons[0].addActionListener(l -> {
            Queen queen = new Queen(color, coordinate);
            setPiece(queen);
        });


        buttons[1] = new JButton();
        buttons[1].setIcon(ChessUtil.getIconFor(color, 'R'));
        buttons[1].addActionListener(l -> {
            Rook rook = new Rook(color, coordinate);
            setPiece(rook);
        });


        buttons[2] = new JButton();
        buttons[2].setIcon(ChessUtil.getIconFor(color, 'N'));
        buttons[2].addActionListener(l -> {
            Knight knight = new Knight(color, coordinate);
            setPiece(knight);
        });


        buttons[3] = new JButton();
        buttons[3].setIcon(ChessUtil.getIconFor(color, 'B'));
        buttons[3].addActionListener(l -> {
            Bishop bishop = new Bishop(color, coordinate);
            setPiece(bishop);
        });

        add(buttons[0]);
        add(buttons[1]);
        add(buttons[2]);
        add(buttons[3]);


        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                synchronized (ChessUtil.promoting) {
                    setVisible(false);
                    ChessUtil.promoting.notify();
                }
            }
        });
    }

    private void setPiece(Figure figure) {
        boardButtons[coordinate.getX()][coordinate.getY()].setIcon(figure.getIcon());
        position.add(figure);
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }


}
