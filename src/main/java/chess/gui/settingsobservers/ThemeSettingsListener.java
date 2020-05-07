package chess.gui.settingsobservers;

import chess.gui.SettingsWindow;

import java.awt.Color;

/**
 * Observer for {@link SettingsWindow#getTheme()}.
 *
 * @author lukag
 */
public class ThemeSettingsListener implements SettingsListener {
    @Override
    public void update(SettingsWindow sw) {
        Color white = null;
        Color black = null;
        String theme = sw.getTheme();
        switch (theme) {
            case "Green":
                white = new Color(238, 238, 210);
                black = new Color(118,150,86);
                break;
            case "Wood":
                white = new Color(229,191,147);
                black = new Color(130,92,66);
                break;
            case "Glass":
                white = new Color(189, 200, 215);
                black = new Color(96, 121, 157);
                break;
        }
        sw.setTheme(white, black);
    }
}
