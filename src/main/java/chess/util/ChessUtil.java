package chess.util;

import chess.game.Position;
import chess.pieces.Bishop;
import chess.pieces.Figure;
import chess.pieces.Queen;
import chess.pieces.Rook;

import javax.imageio.ImageIO;
import java.awt.Image;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Class offers a lot of useful static methods and attributes.
 */
public class ChessUtil {

    /**
     * Object used as a lock when promoting window appears.
     */
    public static Object promoting = new Object();

    /**
     * Flag indicates whether is computer playing.
     */
    public static boolean computerPlaying = false;

    /**
     * Flag indicates whether is computer's move.
     */
    public static boolean computersMove = false;

    /**
     * Constant representing value of checkmate.
     */
    public static final double CHECKMATE = 1000;

    /**
     * Constant representing value of stalemate.
     */
    public static final double STALEMATE = 999;

    /**
     * Default depth level.
     */
    public static int DEPTH = 2;

    /**
     * Move counter. Starts from 1.
     */
    public static int moveNumber = 1;

    /**
     * If some square is clicked.
     */
    public static boolean clicked = false;

    /**
     * Flag indicates whether to record the move, i.e. write move to JTextArea.
     */
    public static boolean recordMove = true;

    /**
     * Columns as letters.
     */
    public static String abc = "abcdefgh";

    public static String numbers = "87654321";

    /**
     * Standard chess move description.
     */
    public static String move = "";

    /**
     * Name of the file containing image icons for pieces.
     */
    public static String icons = "classical.png";

    /**
     * Pieces icons as images.
     */
    public static Image[][] piecesIcons = getIcons();


    /**
     * Method return 2x6 field of images.
     */
    public static Image[][] getIcons() {
        Image[][] piecesIcons = new Image[2][6];
        try {
            URL pieces = ChessUtil.class.getClassLoader().getResource(icons);
            BufferedImage bufferedImage = ImageIO.read(pieces);
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 6; j++) {
                    if(icons.equals("classical.png")) {
                        piecesIcons[i][j] = bufferedImage.getSubimage(j * 85, i * 85, 85, 85);
                        piecesIcons[i][j] = piecesIcons[i][j].getScaledInstance(64, 64, Image.SCALE_DEFAULT);
                    } else {
                        piecesIcons[i][j] = bufferedImage.getSubimage(j * 64, i * 64, 64, 64);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return piecesIcons;
    }

    /**
     * Method returns appropriate icon for given color and symbol.
     */
    public static Icon getIconFor(Color color, char symbol) {
        if(icons.equals("classical.png")) {
            switch(symbol) {
                case 'K':
                    return new ImageIcon(piecesIcons[color.getValue()][0]);
                case 'Q':
                    return new ImageIcon(piecesIcons[color.getValue()][1]);
                case 'B':
                    return new ImageIcon(piecesIcons[color.getValue()][2]);
                case 'N':
                    return new ImageIcon(piecesIcons[color.getValue()][3]);
                case 'R':
                    return new ImageIcon(piecesIcons[color.getValue()][4]);
                case 'P':
                    return new ImageIcon(piecesIcons[color.getValue()][5]);
                default:
                    throw new IllegalArgumentException("Invalid symbol");
            }
        } else {
            switch(symbol) {
                case 'K':
                    return new ImageIcon(piecesIcons[(color.getValue() * -1) + 1][0]);
                case 'Q':
                    return new ImageIcon(piecesIcons[(color.getValue() * -1) + 1][1]);
                case 'B':
                    return new ImageIcon(piecesIcons[(color.getValue() * -1) + 1][4]);
                case 'N':
                    return new ImageIcon(piecesIcons[(color.getValue() * -1) + 1][3]);
                case 'R':
                    return new ImageIcon(piecesIcons[(color.getValue() * -1) + 1][2]);
                case 'P':
                    return new ImageIcon(piecesIcons[(color.getValue() * -1) + 1][5]);
                default:
                    throw new IllegalArgumentException("Invalid symbol");
            }
        }

    }

    /**
     * Method fills list of coordinates that given figure can go to depending on move pattern provided in arguments.
     * This is used for movement down the line.
     * @param figure should be instance of Rook or Queen class, otherwise exception is thrown
     * @param coordinates list of coordinates to be filled
     * @param length how many squares to look at
     * @param pattern +0, 0+, -0, 0- (for example +0 means: positive direction on x axis and no movement on y axis)
     */
    public static void fillCoordinatesListForLines(Figure figure, List<Coordinate> coordinates, int length, String pattern, Position position) {
        for(int i = 0; i < length; i++) {
            try {
                Coordinate c = null;
                switch(pattern) {
                    case "+0":
                        c = new Coordinate(figure.getxCoordinate() + i + 1, figure.getyCoordinate());
                        break;
                    case "0+":
                        c = new Coordinate(figure.getxCoordinate(), figure.getyCoordinate() + i + 1);
                        break;
                    case "-0":
                        c = new Coordinate(figure.getxCoordinate() - i - 1, figure.getyCoordinate());
                        break;
                    case "0-":
                        c = new Coordinate(figure.getxCoordinate(), figure.getyCoordinate() - i - 1);
                        break;
                }
                if(!filterCoordinates(figure, c, position, coordinates)){
                    return;
                }
            }
            catch(IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method fills list of coordinates that given figure can go to depending on move pattern provided in arguments.
     * This is used for diagonal movement.
     * @param figure should be instance of Rook or Queen class, otherwise exception is thrown
     * @param coordinates list of coordinates to be filled
     * @param length how many squares to look at
     * @param pattern +-, -+, --, ++ (for example +- means: positive direction on x axis and negative on y axis)
     */
    public static void fillCoordinatesListForDiagonals(Figure figure, List<Coordinate> coordinates, int length, String pattern, Position position) {
        for(int i = 0; i < length; i++) {
            try {
                Coordinate c = null;
                switch(pattern) {
                    case "++":
                        c = new Coordinate(figure.getxCoordinate() + i + 1, figure.getyCoordinate() + i + 1);
                        break;
                    case "+-":
                        c = new Coordinate(figure.getxCoordinate() + i + 1, figure.getyCoordinate() - i - 1);
                        break;
                    case "-+":
                        c = new Coordinate(figure.getxCoordinate() - i - 1, figure.getyCoordinate() + i + 1);
                        break;
                    case "--":
                        c = new Coordinate(figure.getxCoordinate() - i - 1, figure.getyCoordinate() - i - 1);
                        break;
                }
                if(!filterCoordinates(figure, c, position, coordinates)) {
                    return;
                }
            }
            catch(IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns list of coordinates that given figure instance attacks or defends.
     */
    public static List<Coordinate> attackOrDefend(Figure figure, boolean attack, Position position) {
        List<Coordinate> coordinates = new ArrayList<>();
        int top = figure.getxCoordinate();
        int bottom = 7 - figure.getxCoordinate();
        int left = figure.getyCoordinate();
        int right = 7 - figure.getyCoordinate();

        if(figure instanceof Rook || figure instanceof Queen) {
            fillCoordinatesListForLines(figure, coordinates, top, "-0", position);
            fillCoordinatesListForLines(figure, coordinates, bottom, "+0", position);
            fillCoordinatesListForLines(figure, coordinates, left, "0-", position);
            fillCoordinatesListForLines(figure, coordinates, right, "0+", position);
        }

        if(figure instanceof Bishop || figure instanceof Queen) {
            int topLeft = Math.min(top, left);
            int topRight = Math.min(top, right);
            int bottomLeft = Math.min(bottom, left);
            int bottomRight = Math.min(bottom, right);

            fillCoordinatesListForDiagonals(figure, coordinates, topLeft, "--", position);
            fillCoordinatesListForDiagonals(figure, coordinates, topRight, "-+", position);
            fillCoordinatesListForDiagonals(figure, coordinates, bottomLeft, "+-", position);
            fillCoordinatesListForDiagonals(figure, coordinates, bottomRight, "++", position);
        }
        return coordinates;
    }

    /**
     * Method filters through list of possible moves and removes ones that are prevented by check.
     */
    public static List<Coordinate> filterPossibleMoves(Position position, Figure figure, List<Coordinate> coordinates) {
        try {
            List<Coordinate> coordinates2 = new ArrayList<>();
            Coordinate king = position.getFigureCoordinate(figure.getColor(), 'K');
            int x = figure.getxCoordinate();
            int y = figure.getyCoordinate();
            coordinates2.addAll(coordinates);
            for(Coordinate c : coordinates) {
                Figure f = position.getFigure(c);
                position.setFigure(x, y, c.getX(), c.getY());
                if(position.numberOfAttackers(king.getX(), king.getY(), figure.getColor() == Color.WHITE ? Color.BLACK : Color.WHITE) > 0) {
                    coordinates2.remove(c);
                }
                position.setFigureBack(c.getX(), c.getY(), x, y, f);
            }
            return coordinates2;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Method filters coordinates depending on #attacking parameter.
     */
    private static boolean filterCoordinates(Figure figure, Coordinate c, Position position, List<Coordinate> coordinates) {
        Figure piece = position.getFigure(c.getX(), c.getY());
        if(piece != null && piece.getColor() != figure.getColor()) {
            coordinates.add(c);
            return false;
        } else if(piece != null && piece.getColor() == figure.getColor()) {
            return false;
        }
        coordinates.add(c);
        return true;
    }


    /**
     * Method checks if it is checkmate or stalemate. Used as terminate function in {@link chess.searchalgorithms.SearchAlgorithm}.
     * @return 0 if it is neither, {@link ChessUtil#CHECKMATE} or {@link ChessUtil#STALEMATE}
     */
    public static int isCheckMateOrStalemate(Position position, Color opponentColor, Color color) {
        Coordinate kingCoordinate = position.getFigureCoordinate(color, 'K');
        if (position.allPossibleMoves(color) == 0) {
            if (position.numberOfAttackers(kingCoordinate.getX(), kingCoordinate.getY(), opponentColor) > 0) {
                return (int) CHECKMATE;
            } else {
                return (int) STALEMATE;
            }
        }
        return 0;
    }
}