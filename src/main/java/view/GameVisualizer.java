package view;

import model.RobotModel;

import java.awt.*;
import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
/**
 * Класс GameVisualizer отвечает за визуализацию робота и цели на игровом поле.
 * Он отображает текущее состояние модели (RobotModel) и обрабатывает события мыши
 * для обновления позиции цели.
 */
public class GameVisualizer extends JPanel implements PropertyChangeListener {
    private final RobotModel model;
    private RobotVisualizer visualizer;
    /**
    * Создает GameVisualizer для отображения состояния модели.
    */
    public GameVisualizer(RobotModel model, RobotVisualizer visualizer) {
        this.model = model;
        this.visualizer = visualizer;
        model.addPropertyChangeListener(this);
        setDoubleBuffered(true);
    }
    /**
     * Изменяет стратегию визуализации
     */
    public void setVisualizer(RobotVisualizer visualizer) {
        this.visualizer = visualizer;
        repaint();
    }
    /**
     * Отрисовывает компонент, делегируя рисование текущему визуализатору
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        visualizer.drawTarget(g2d, round(model.getTargetX()), round(model.getTargetY()));
        visualizer.drawRobot(g2d, round(model.getX()), round(model.getY()), model.getDirection());
    }
    /**
     * Реакция на изменения в модели - запрос перерисовки
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        repaint();
    }
    /**
     * Округление координат для целочисленного позиционирования
     */
    private static int round(double value) {
        return (int)(value + 0.5);
    }
}