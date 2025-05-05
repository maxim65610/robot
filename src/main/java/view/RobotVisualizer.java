package view;

import java.awt.*;
/**
 * Интерфейс для визуализации робота и цели на игровом поле.
 * Определяет контракт для отрисовки графических компонентов.
 */
public interface RobotVisualizer {
    /**
     * Отрисовывает робота с указанными параметрами
     */
    void drawRobot(Graphics2D g, int x, int y, double direction);
    /**
     * Отрисовывает целевую точку для движения робота.
     */
    void drawTarget(Graphics2D g, int x, int y);
}
