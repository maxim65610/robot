package gui;
import localization.LocaleChangeListener;
import localization.LocaleManager;
import view.GameVisualizer;

import java.awt.BorderLayout;

import javax.swing.JInternalFrame;
/**
 * Класс GameWindow представляет внутреннее окно для отображения игрового поля.
 */
class GameWindow extends JInternalFrame implements LocaleChangeListener {
    /**
     * Создает GameWindow с визуализацией модели робота.
     */
    public GameWindow(GameVisualizer view) {
        super(LocaleManager.getInstance().getString("gameWindowTitle")
                , true, true, true, true);
        LocaleManager.getInstance().addListener(this);
        add(view, BorderLayout.CENTER);
        pack();
    }
    /**
     * Вызывается при смене локали.
     */
    @Override
    public void onLocaleChanged() {
        setTitle(LocaleManager.getInstance().getString("gameWindowTitle"));
    }
}
