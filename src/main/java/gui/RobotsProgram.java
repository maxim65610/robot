package gui;

import java.awt.Frame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
/**
 * RobotsProgram представляет точку входа в приложение, устанавливающую внешний вид Swing-интерфейса
 */
public class RobotsProgram
{
  /**
   * Точка входа в программу. Устанавливает внешний вид приложения (Look and Feel),
   * создает и отображает главное окно приложения
   */
    public static void main(String[] args) {
      try {
        UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
//        UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
//        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
      } catch (Exception e) {
        e.printStackTrace();
      }
      /**
       * Запускает создание и отображение главного окна приложения в потоке обработки событий AWT.
       */
      SwingUtilities.invokeLater(() -> {
        MainApplicationFrame frame = new MainApplicationFrame();
        frame.pack();
        frame.setVisible(true);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
      });
    }}
