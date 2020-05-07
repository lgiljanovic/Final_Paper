package chess.gui;

import chess.util.Color;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class WinnerFrame extends JFrame {

    private Color winner;
    /**
     * Check mate or resignation.
     */
    private String winningBy;


    /**
     * Basic constructor.
     * @param winner winner
     * @param winningBy mate or resignation
     */
    public WinnerFrame(Color winner, String winningBy) {
        this.winner = winner;
        this.winningBy = winningBy;
        initGui();
    }

    /**
     * Method that initializes gui.
     */
    private void initGui() {
        try {
            URL icon = MainFrame.class.getClassLoader().getResource("gameIcon.png");
            BufferedImage bufferedImage = ImageIO.read(icon);
            setIconImage(bufferedImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setTitle("Game over");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel win = new JLabel(winner.toString() + " won by " + winningBy);
        win.setFont(new Font("Winner font", Font.BOLD, 30));
        add(win, BorderLayout.NORTH);

        add(new JPanel(), BorderLayout.CENTER);
    }
}
