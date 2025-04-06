package controller;

import log.Logger;
import model.RobotModel;
import view.GameVisualizer;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
/**
 * Контроллер для обработки кликов мыши и установки новой цели робота.
 */
public class MouseController {
    /**
     * Конструктор создает обработчик событий мыши для установки целевой позиции робота.
     */
    public MouseController(RobotModel model, GameVisualizer view){
        view.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                model.setTarget(e.getX(), e.getY());
                Logger.debug("Координаты цели изменились");
            }
        });
    }
}
