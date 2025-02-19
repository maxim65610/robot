package gui;

import java.awt.BorderLayout;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
/**
 * GameWindow наследуется от JInternalFrame и предоставляет пользовательский интерфейс
 * для взаимодействия с визуализацией движения робота.
 */
public class GameWindow extends JInternalFrame
{
    private final GameVisualizer m_visualizer;
    /**
     * Конструктор GameWindow.
     * Инициализирует внутреннее окно с заголовком "Игровое поле",
     * добавляет компонент GameVisualizer в центральную область панели
     * и устанавливает минимальный размер окна.
     */
    public GameWindow() 
    {
        super("Игровое поле", true, true, true, true);
        m_visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }
}
