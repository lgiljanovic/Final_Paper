package chess.gui;

import chess.game.Game;
import chess.game.Position;
import chess.gui.settingsobservers.DepthSettingsListener;
import chess.gui.settingsobservers.PieceStyleSettingsListener;
import chess.gui.settingsobservers.ThemeSettingsListener;
import chess.pieces.Figure;
import chess.searchalgorithms.AlphaBeta2;
import chess.searchalgorithms.AlphaBetaParallel;
import chess.searchalgorithms.evaluators.SimplePositionEvaluator;
import chess.searchalgorithms.heuristics.CheckTakingHeuristic;
import chess.util.ChessUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 * Main window of the application. It displays chess board.
 */
public class MainFrame extends JFrame {

    /**
     * Buttons represent chess board.
     */
    private JButton[][] boardButtons = new JButton[8][8];

    /**
     * Rows, in left corner of the board. (1-8).
     */
    private JLabel[] rows = new JLabel[8];

    /**
     * Columns, on top of the board. (A-H).
     */
    private JLabel[] columns = new JLabel[8];

    /**
     * List of played moves written in standard chess notation.
     */
    private JTextArea moveList = new JTextArea();

    /**
     * Instance of {@link Game} that is currently being played.
     */
    private Game game;

    /**
     * Settings window.
     */
    private SettingsWindow settingsWindow = new SettingsWindow();

    public MainFrame() {
        initGui();
    }

    private void initGui() {
        ImageIcon whiteIcon = null;
        ImageIcon blackIcon = null;
        ImageIcon settingsIcon = null;
        try {
            URL icon = MainFrame.class.getClassLoader().getResource("gameIcon.png");
            BufferedImage bufferedImage = ImageIO.read(icon);
            whiteIcon = new ImageIcon(ImageIO.read(Objects.requireNonNull(MainFrame.class.getClassLoader().getResource("white.png"))).getSubimage(0, 0, 10, 10));
            blackIcon = new ImageIcon(ImageIO.read(Objects.requireNonNull(MainFrame.class.getClassLoader().getResource("black.jpg"))).getSubimage(0, 0, 10, 10));
            settingsIcon = new ImageIcon(ImageIO.read(Objects.requireNonNull(MainFrame.class.getClassLoader().getResource("settings.png"))).getScaledInstance(20, 20, Image.SCALE_DEFAULT));
            setIconImage(bufferedImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setTitle("PlayChess.com");
        setLayout(new BorderLayout());

        moveList.setFont(moveList.getFont().deriveFont(Font.BOLD));
        JToolBar toolbar = new JToolBar();
        JButton playWhite = new JButton("Play White");
        playWhite.setIcon(whiteIcon);
        playWhite.addActionListener(l -> {
            ChessUtil.abc = "abcdefgh";
            ChessUtil.numbers = "87654321";
            moveList.setText("");
            ChessUtil.computerPlaying = true;
            ChessUtil.computersMove = false;
            ChessUtil.moveNumber = 1;
            game = new Game(true, true, boardButtons);
            setUpNewGame(true);
        });
        JButton playBlack = new JButton("Play Black");
        playBlack.setIcon(blackIcon);
        playBlack.addActionListener(l -> {
            moveList.setText("");
            ChessUtil.abc = "hgfedcba";
            ChessUtil.numbers = "12345678";
            ChessUtil.computerPlaying = true;
            ChessUtil.computersMove = true;
            ChessUtil.moveNumber = 1;
            game = new Game(false, true, boardButtons);
            setUpNewGame(true);
            MoveActionComputer comp = new MoveActionComputer(game.getPosition(), boardButtons,
                    game.getPosition().getPlayerWhite() ? chess.util.Color.BLACK : chess.util.Color.WHITE, moveList, new AlphaBetaParallel(new CheckTakingHeuristic(), new SimplePositionEvaluator()));
            comp.start();
        });

        JButton oneVsOne = new JButton("1vs1");
        oneVsOne.addActionListener(l -> {
            ChessUtil.abc = "abcdefgh";
            ChessUtil.numbers = "87654321";
            moveList.setText("");
            ChessUtil.computerPlaying = false;
            ChessUtil.computersMove = false;
            ChessUtil.moveNumber = 1;
            game = new Game(true, true, boardButtons);
            setUpNewGame(false);
        });

        settingsWindow.registerListener(new DepthSettingsListener());
        settingsWindow.registerListener(new ThemeSettingsListener());
        settingsWindow.registerListener(new PieceStyleSettingsListener());
        JButton settings = new JButton("Settings");
        settings.setIcon(settingsIcon);
        settings.addActionListener(l -> {
            SwingUtilities.invokeLater(() -> {
                if(this.game != null) {
                    settingsWindow.setPosition(game.getPosition());
                }
                settingsWindow.setBoardButtons(boardButtons);
                settingsWindow.initGui();
                settingsWindow.setResizable(false);
                settingsWindow.setLocation(230, 205);
                settingsWindow.pack();
                settingsWindow.setVisible(true);
            });
        });

        toolbar.add(playWhite);
        toolbar.add(playBlack);
        toolbar.add(oneVsOne);
        toolbar.add(new JLabel(" ".repeat(110)));
        toolbar.add(settings);
        add(toolbar, BorderLayout.NORTH);

        JPanel board = new JPanel();
        board.setLayout(new GridLayout(9, 9));
        boolean isWhite = true;
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                if(i == 0 && j == 0) {
                    board.add(new JLabel());
                    continue;
                }

                if(i == 0) {
                    columns[j - 1] = new JLabel("", SwingConstants.CENTER);
                    board.add(columns[j - 1]);
                    continue;
                }
                if(j == 0) {
                    rows[i - 1] = new JLabel("", SwingConstants.CENTER);
                    board.add(rows[i - 1]);
                    continue;
                }

                boardButtons[i - 1][j - 1] = new JButton();
                if(isWhite) {
                    boardButtons[i - 1][j - 1].setBackground(new Color(238, 238, 210));
                    if(j == 8) {
                        isWhite = true;
                    }
                    else {
                        isWhite = false;
                    }

                }
                else {
                    boardButtons[i - 1][j - 1].setBackground(new Color(118, 150, 86));
                    if(j == 8) {
                        isWhite = false;
                    }
                    else {
                        isWhite = true;
                    }

                }
                boardButtons[i - 1][j - 1].setBorderPainted(false);
                board.add(boardButtons[i - 1][j - 1]);
            }
        }
        add(board, BorderLayout.CENTER);

        JPanel east = new JPanel();
        east.setLayout(new BorderLayout());
        east.add(new JScrollPane(moveList), BorderLayout.CENTER);
        moveList.setEditable(false);
        JPanel flowPanel = new JPanel();
        flowPanel.setLayout(new FlowLayout());
        JButton resign = new JButton("Resign");
        resign.addActionListener(e -> {
            if(game != null) {
                WinnerFrame winner = new WinnerFrame(!game.getPosition().isWhiteToMove ? chess.util.Color.WHITE : chess.util.Color.BLACK, "resignation");
                winner.setLocation(300, 200);
                winner.pack();
                winner.setVisible(true);
                moveList.append(game.getPosition().isWhiteToMove ? "0-1" : "1-0");
                game = null;
            }
        });
        JButton draw = new JButton("Draw");
        draw.addActionListener(e -> {
            JFrame drawFrame = new JFrame("Draw");
            JLabel drawLabel = new JLabel("Draw by agreement");
            drawFrame.add(drawLabel);
            drawLabel.setFont(new Font("Draw", Font.BOLD, 20));
            drawFrame.setLocation(300, 200);
            drawFrame.pack();
            drawFrame.setVisible(true);
            moveList.append("1/2-1/2");
        });
        flowPanel.add(resign);
        flowPanel.add(draw);
        east.add(flowPanel, BorderLayout.SOUTH);
        add(east, BorderLayout.EAST);
        add(new JPanel(), BorderLayout.SOUTH);
    }

    /**
     * Method initializes {@link #boardButtons} icons to {@link Figure#getIcon()}.
     */
    private void initPieces() {
        String letters = "ABCDEFGH";
        Position position = game.getPosition();
        for(int i = 0; i < 8; i++) {
            columns[i].setText(position.getPlayerWhite() ? String.valueOf(letters.charAt(i)) : String.valueOf(letters.charAt(8 - i - 1)));
            rows[i].setText(position.getPlayerWhite() ? String.valueOf(8 - i) : String.valueOf(i + 1));
        }

        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(game.getPosition().getFigure(i, j) != null) {
                    boardButtons[i][j].setIcon(position.getFigure(i, j).getIcon());
                }
            }
        }
    }

    /**
     * Method removes all current action listeners of board buttons and adds new one.
     */
    private void setUpNewGame(boolean isComputerPlaying) {
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(boardButtons[i][j].getActionListeners().length > 0) {
                    boardButtons[i][j].removeActionListener(boardButtons[i][j].getActionListeners()[0]);
                }
                boardButtons[i][j].setIcon(null);
                boardButtons[i][j].addActionListener(
                        new MoveActionPlayer(i, j, game.getPosition(), boardButtons, moveList,
                                new AlphaBetaParallel(new CheckTakingHeuristic(), new SimplePositionEvaluator())));
            }
        }
        initPieces();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.pack();
            mainFrame.setLocation(20,20);
            mainFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
            mainFrame.setSize(new Dimension(700, 600));
            mainFrame.setResizable(false);
            mainFrame.setVisible(true);
        });
    }


}

