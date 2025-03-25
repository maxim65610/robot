package gui;

import model.RobotModel;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Внутреннее окно для отображения текущих координат робота.
 */
public class RobotCoordinatesWindow extends JInternalFrame implements PropertyChangeListener {
    private final JLabel coordinatesLabel;

    /**
     * Создает внутреннее окно для отображения координат робота.
     *
     * @param model Модель робота.
     */
    public RobotCoordinatesWindow(RobotModel model) {
        super("Координаты робота", true, true, true, true);// Возможность свернуть окно в иконку
        setSize(250,  100);
        // Панель для отображения координат
        JPanel panel = new JPanel();
        coordinatesLabel = new JLabel();
        panel.add(coordinatesLabel);
        add(panel, BorderLayout.CENTER);

        // Подписка на изменения модели
        model.addPropertyChangeListener(this);
        updateCoordinates(model.getX(), model.getY()); // Инициализация начальных координат
    }

    /**
     * Обновляет отображаемые координаты.
     *
     * @param x Координата X.
     * @param y Координата Y.
     */
    private void updateCoordinates(double x, double y) {
        coordinatesLabel.setText(String.format("Координаты: x = %.2f  y = %.2f", x, y));
    }

    /**
     * Реагирует на изменения в модели.
     *
     * @param evt Событие изменения свойства.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("position".equals(evt.getPropertyName())) {
            Point newPosition = (Point) evt.getNewValue();
            updateCoordinates(newPosition.getX(), newPosition.getY());
        }
    }
}