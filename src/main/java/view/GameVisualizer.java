package view;

import model.RobotModel;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import java.awt.geom.AffineTransform;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
/**
 * Класс GameVisualizer отвечает за визуализацию робота и цели на игровом поле.
 * Он отображает текущее состояние модели (RobotModel) и обрабатывает события мыши
 * для обновления позиции цели.
 */
public class GameVisualizer extends JPanel implements PropertyChangeListener {
    private final RobotModel model;
    /**
     * Создает GameVisualizer для отображения состояния модели.
     */
    public GameVisualizer(RobotModel model) {
        this.model = model;
        model.addPropertyChangeListener(this);
        setDoubleBuffered(true);
    }
    /**
     * Отрисовывает компонент на основе текущего состояния модели.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        drawTarget(g2d, round(model.getTargetX()), round(model.getTargetY()));
        drawRobot(g2d, round(model.getX()), round(model.getY()), model.getDirection());
    }
    /**
     * Реагирует на изменения в модели, вызывая перерисовку.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        repaint();
    }

    /**
     * Отрисовывает робота.
     */
    private static void drawRobot(Graphics2D g, int x, int y, double direction) {
        AffineTransform t = AffineTransform.getRotateInstance(direction, x, y);
        g.setTransform(t);

        g.setColor(Color.MAGENTA);
        fillOval(g, x, y, 30, 10);

        g.setColor(Color.BLACK);
        drawOval(g, x, y, 30, 10);

        g.setColor(Color.WHITE);
        fillOval(g, x + 10, y, 5, 5);

        g.setColor(Color.BLACK);
        drawOval(g, x + 10, y, 5, 5);
    }
    /**
     * Отрисовывает цель.
     */
    private static void drawTarget(Graphics2D g, int x, int y) {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);

        g.setColor(Color.GREEN);
        fillOval(g, x, y, 5, 5);

        g.setColor(Color.BLACK);
        drawOval(g, x, y, 5, 5);
    }
    /**
     * Заливает овал.
     */
    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }
    /**
     * Рисует овал.
     */
    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }
    /**
     * Округляет значение.
     */
    private static int round(double value) {
        return (int) (value + 0.5);
    }

}
