package model;
import view.RobotVisualizer;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
/**
 * Класс RobotModel представляет модель робота, управляющую его позицией и направлением.
 * Модель уведомляет слушателей об изменениях через PropertyChangeSupport.
 */
public class RobotModel{
    private volatile double x;
    private volatile double y;
    private volatile double direction = 0;
    private volatile int targetX = 150;
    private volatile int targetY = 100;
    private static double maxVelocity;
    private static double maxAngularVelocity;
    private GameModel model;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    /**
     * Добавляет слушателя изменений.
     *
     * @param listener Слушатель изменений.
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
    public RobotModel(GameModel model){
        setModel(model);
    }
    public void setModel(GameModel model) {
        this.model = model;
        setField();
    }
    public void setField(){
        x = model.getX();
        y = model.getY();
        maxVelocity = model.getMaxVelocity();
        maxAngularVelocity = model.getMaxAngularVelocity();
        direction = model.getDirection();
    }
    /**
     * Устанавливает новую цель для робота.
     *
     * @param x Координата X цели.
     * @param y Координата Y цели.
     */
    public void setTarget(int x, int y) {
        this.targetX = x;
        this.targetY = y;
        support.firePropertyChange("target", null, new Point(x, y));
    }
    /**
     * Обновляет состояние модели (позицию и направление робота).
     */
    public void updateModel() {
        double distance = distance(targetX, targetY, x, y);
        if (distance < 0.5) return;

        double velocity = maxVelocity;
        double angleToTarget = angleTo(x, y, targetX, targetY);

        double angleDifference = asNormalizedRadians(angleToTarget - direction);
        double angularVelocity = (angleDifference <= Math.PI) ? maxAngularVelocity : -maxAngularVelocity;

        model.moveRobot(velocity, angularVelocity, 10);
        setField();
        support.firePropertyChange("position", null, new Point((int) x, (int) y));

    }
    /**
     * Вычисляет расстояние между двумя точками.
     */
    private static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
    /**
     * Вычисляет угол до цели.
     */
    private static double angleTo(double fromX, double fromY, double toX, double toY) {
        return asNormalizedRadians(Math.atan2(toY - fromY, toX - fromX));
    }
    /**
     * Нормализует угол в радианах.
     */
    private static double asNormalizedRadians(double angle) {
        while (angle < 0) angle += 2 * Math.PI;
        while (angle >= 2 * Math.PI) angle -= 2 * Math.PI;
        return angle;
    }
    public int getTargetX() { return targetX; }
    public int getTargetY() { return targetY; }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getDirection() {
        return direction;
    }
}
