package controller;

import model.RobotModel;
import view.GameVisualizer;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseController {
    public MouseController(RobotModel model, GameVisualizer view){
        view.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                model.setTarget(e.getX(), e.getY());
            }
        });

    }
}
