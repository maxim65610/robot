package model;
/**
 * Интерфейс модели игрового мира, управляющей движением и состоянием робота.
 * Определяет базовые операции для управления роботом и получения его текущего состояния.
 */
public interface GameModel {
    /**
     * Обновляет позицию и направление робота
     */
    void moveRobot(double velocity, double angularVelocity, double duration);
    /**
     * Возвращает текущую координату X центра робота
     */
    double getX();
    /**
     * Возвращает текущую координату Y центра робота
     */
    double getY();
    /**
     * Возвращает максимально допустимую линейную скорость
     */
    double getMaxVelocity();
    /**
     * Возвращает максимально допустимую угловую скорость
     */
    double getMaxAngularVelocity();
    /**
     * Возвращает текущее направление робота
     */
    double getDirection();
}
