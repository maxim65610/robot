package model;

import java.awt.*;
/**
 * Реализация модели игрового мира по умолчанию.
 * Обеспечивает корректное движение робота с ограничением скоростей
 * и нормализацией углов.
 */
public class DefaultGameModel implements GameModel{
    private volatile double x = 100;
    private volatile double y = 100;
    private static final double maxVelocity = 0.1;
    private static final double maxAngularVelocity = 0.001;
    private volatile double direction = 0;
    @Override
    public double getX() {
        return x;
    };
    @Override
    public double getY() {
        return y;
    };
    @Override
    public double getMaxVelocity(){
        return maxVelocity;
    }
    @Override
    public double getMaxAngularVelocity(){
        return maxAngularVelocity;
    }
    @Override
    public double getDirection(){
        return direction;
    }
    /**
     * Перемещает робота на основе скорости и угловой скорости.
     *
     * @param velocity        Скорость робота.
     * @param angularVelocity Угловая скорость робота.
     * @param duration        Время перемещения.
     */
    @Override
    public void moveRobot(double velocity, double angularVelocity, double duration) {
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
     * Ограничивает значение в заданных пределах
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
     * Нормализует угол в радианах к диапазону [0, 2π)
     */
    private static double asNormalizedRadians(double angle) {
        while (angle < 0) angle += 2 * Math.PI;
        while (angle >= 2 * Math.PI) angle -= 2 * Math.PI;
        return angle;
    }
}
