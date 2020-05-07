package chess.gui.settingsobservers;

import chess.gui.SettingsWindow;
import chess.pieces.Figure;
import chess.util.ChessUtil;

/**
 * Observer for {@link SettingsWindow#getPieceStyle()}.
 *
 * @author lukag
 */
public class PieceStyleSettingsListener implements SettingsListener {
    @Override
    public void update(SettingsWindow sw) {
        String piecesStyle = sw.getPieceStyle();
        if(sw.getPosition() == null) {
            ChessUtil.icons = piecesStyle.toLowerCase() + ".png";
            ChessUtil.piecesIcons = ChessUtil.getIcons();
        }
        else if(!ChessUtil.icons.equals(piecesStyle)) {
            ChessUtil.icons = piecesStyle.toLowerCase() + ".png";
            ChessUtil.piecesIcons = ChessUtil.getIcons();
            for(int i = 0; i < 8; i++) {
                for(int j = 0; j < 8; j++) {
                    Figure f = sw.getPosition().getFigure(i, j);
                    if(f != null) {
                        f.setIcon(ChessUtil.getIconFor(f.getColor(), f.getSymbol()));
                        sw.getBoardButtons()[i][j].setIcon(f.getIcon());
                    }
                }
            }
        }
    }
}
