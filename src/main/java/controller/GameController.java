package controller;

import model.RobotModel;
import javax.swing.*;
/**
 * Класс GameController управляет обновлением модели робота через таймер.
 */
public class GameController {
    private final Timer timer;
    /**
     * Создает GameController и запускает таймер для обновления модели.
     */
    public GameController(RobotModel model) {
        this.timer = new Timer(10, e -> model.updateModel());
        timer.start();
    }

}
