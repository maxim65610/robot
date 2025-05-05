package view;
import java.awt.*;
import java.awt.geom.AffineTransform;
/**
 * Реализация по умолчанию для визуализации робота и цели.
 * Использует комбинацию овалов для представления робота и простой кружок для цели.
 */
public class DefaultRobotVisualizer implements RobotVisualizer {
    /**
     * Рисует стилизованное изображение робота с поворотом согласно направлению
     */
    @Override
    public void drawRobot(Graphics2D g, int x, int y, double direction) {
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
     * Рисует цель в виде зеленого круга с черным контуром.
     */
    @Override
    public void drawTarget(Graphics2D g, int x, int y) {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);

        g.setColor(Color.GREEN);
        fillOval(g, x, y, 5, 5);

        g.setColor(Color.BLACK);
        drawOval(g, x, y, 5, 5);
    }
    /**
     * Вспомогательный метод для заливки овала с центром в указанных координатах
     */
    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }
    /**
     * Вспомогательный метод для рисования контура овала
     */
    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }
}
