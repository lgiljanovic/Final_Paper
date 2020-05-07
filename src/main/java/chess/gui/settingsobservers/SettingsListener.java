package chess.gui.settingsobservers;

import chess.gui.SettingsWindow;

/**
 * Observer of {@link chess.gui.SettingsWindow}.
 *
 * @author lukag
 */
public interface SettingsListener {

    /**
     * Updates some setting.
     */
    void update(SettingsWindow sw);
}
