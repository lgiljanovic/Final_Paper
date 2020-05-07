package chess.gui.settingsobservers;

import chess.gui.SettingsWindow;
import chess.util.ChessUtil;

/**
 * Observer for {@link chess.util.ChessUtil#DEPTH}.
 *
 * @author lukag
 */
public class DepthSettingsListener implements SettingsListener {

    @Override
    public void update(SettingsWindow sw) {
        ChessUtil.DEPTH = Integer.parseInt(sw.getDepth());
    }
}
