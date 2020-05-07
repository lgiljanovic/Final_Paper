package chess.gui;

import chess.game.Position;
import chess.gui.settingsobservers.SettingsListener;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Settings window provides possibility to change board theme, piece style and depth for computer calculation.
 */
public class SettingsWindow extends JFrame {

    /**
     * Buttons of the {@link MainFrame}.
     */
    private JButton[][] boardButtons;

    /**
     * Current position.
     */
    private Position position;

    /**
     * Theme of the board.
     */
    private String theme;

    /**
     * Depth of {@link chess.searchalgorithms.SearchAlgorithm}.
     */
    private String depth;

    /**
     * Icons for pieces.
     */
    private String pieceStyle;

    /**
     * List of listeners.
     */
    private List<SettingsListener> listeners = new ArrayList<>();

    /**
     * Default constructor.
     */
    public SettingsWindow() {

    }

    public void setBoardButtons(JButton[][] boardButtons) {
        this.boardButtons = boardButtons;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getTheme() {
        return theme;
    }

    public String getDepth() {
        return depth;
    }

    public String getPieceStyle() {
        return pieceStyle;
    }

    public JButton[][] getBoardButtons() {
        return boardButtons;
    }

    public Position getPosition() {
        return position;
    }

    public void initGui() {
        setTitle("Settings");
        try {
            setIconImage(new ImageIcon(ImageIO.read(Objects.requireNonNull(SettingsWindow.class.getClassLoader().getResource("settings.png")))).getImage());
        } catch (IOException e) {
            e.printStackTrace();
        }

        JPanel panel = new JPanel();
        SpringLayout layout = new SpringLayout();
        panel.setLayout(layout);

        JLabel underscore = new JLabel("__________________________");
        JLabel label = new JLabel("Board themes:  ");
        String[] themes = new String[] {"Green", "Wood", "Glass"};
        JComboBox<String> comboBox = new JComboBox<>(themes);


        JLabel jLabel = new JLabel("Piece style: ");
        String[] pieces = new String[] {"Classical", "Persian"};
        JComboBox<String> comboPieces = new JComboBox<>(pieces);

        JLabel depth = new JLabel("Depth: ");
        String[] depths = new String[] {"1", "2", "3", "4", "5", "6" };
        JComboBox<String> depthComboBox = new JComboBox<>(depths);

        panel.add(label);
        panel.add(comboBox);
        panel.add(jLabel);
        panel.add(comboPieces);
        panel.add(underscore);
        panel.add(new JLabel("  "));
        panel.add(depth);
        panel.add(depthComboBox);

        SpringUtilities.makeCompactGrid(panel, 4, 2, 10, 10, 10, 15);
        add(panel, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel();
        JPanel south = new JPanel();
        south.setLayout(new GridLayout(1, 2, 10, 0));
        JButton ok = new JButton("Ok");
        ok.addActionListener(l -> {
            this.depth = (String) depthComboBox.getSelectedItem();
            this.pieceStyle = (String) comboPieces.getSelectedItem();
            this.theme = (String) comboBox.getSelectedItem();
            notifyListeners();
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        });

        JButton close = new JButton("Close");
        close.addActionListener(l -> {
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        });

        south.add(ok);
        south.add(close);
        buttonsPanel.add(south);
        add(buttonsPanel, BorderLayout.SOUTH);
    }


    public void setTheme(Color white, Color black) {
        boolean isWhite = true;
        for(int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (isWhite) {
                    boardButtons[i][j].setBackground(white);
                    isWhite = j == 7;
                } else {
                    boardButtons[i][j].setBackground(black);
                    isWhite = j != 7;
                }
            }
        }
    }

    public void registerListener(SettingsListener listener) {
        listeners.add(listener);
    }

    public void removeListener(SettingsListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        for(SettingsListener listener : listeners) {
            listener.update(this);
        }
    }
}
