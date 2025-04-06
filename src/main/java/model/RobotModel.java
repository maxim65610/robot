package model;

import log.Logger;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
/**
 * Класс RobotModel представляет модель робота, управляющую его позицией и направлением.
 * Модель уведомляет слушателей об изменениях через PropertyChangeSupport.
 */
public class RobotModel {
    private volatile double x = 100;
    private volatile double y = 100;
    private volatile double direction = 0;
    private volatile int targetX = 150;
    private volatile int targetY = 100;
    private static final double maxVelocity = 0.1;
    private static final double maxAngularVelocity = 0.001;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    /**
     * Добавляет слушателя изменений.
     *
     * @param listener Слушатель изменений.
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
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

        moveRobot(velocity, angularVelocity, 10);

        support.firePropertyChange("position", null, new Point((int) x, (int) y));

    }
    /**
     * Перемещает робота на основе скорости и угловой скорости.
     *
     * @param velocity        Скорость робота.
     * @param angularVelocity Угловая скорость робота.
     * @param duration        Время перемещения.
     */
    private void moveRobot(double velocity, double angularVelocity, double duration) {
        velocity = applyLimits(velocity, 0, maxVelocity);
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
        double newX = x + velocity / angularVelocity *
                (Math.sin(direction  + angularVelocity * duration) -
                        Math.sin(direction));
        if (!Double.isFinite(newX))
        {
            newX = x + velocity * duration * Math.cos(direction);
        }
        double newY = y - velocity / angularVelocity *
                (Math.cos(direction  + angularVelocity * duration) -
                        Math.cos(direction));
        if (!Double.isFinite(newY))
        {
            newY = y + velocity * duration * Math.sin(direction);
        }
        x = newX;
        y = newY;
        double newDirection = asNormalizedRadians(direction + angularVelocity * duration);
        direction = newDirection;
    }
    /**
     * Ограничивает значение в заданных пределах.
     */
    private static double applyLimits(double value, double min, double max)
    {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
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
