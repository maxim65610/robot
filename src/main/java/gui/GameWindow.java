package gui;
import view.GameVisualizer;

import java.awt.BorderLayout;

import javax.swing.JInternalFrame;
/**
 * Класс GameWindow представляет внутреннее окно для отображения игрового поля.
 */
class GameWindow extends JInternalFrame {
    /**
     * Создает GameWindow с визуализацией модели робота.
     */
    public GameWindow(GameVisualizer view) {
        super("Игровое поле", true, true, true, true);
        add(view, BorderLayout.CENTER);
        pack();
    }
}
