package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import log.Logger;

/**
 * MainApplicationFrame представляет главное окно приложения, содержащее панель рабочего пространства (JDesktopPane)
 * для управления внутренними окнами, такими как журнал работы (LogWindow) и игровое поле (GameWindow).
 * Класс также создает меню для изменения внешнего вида приложения и выполнения тестовых команд.
 */
public class MainApplicationFrame extends JFrame
{
    private final JDesktopPane desktopPane = new JDesktopPane();
    /**
     * Конструктор MainApplicationFrame.
     * Инициализирует главное окно приложения, добавляет внутренние окна (журнал и игровое поле),
     * устанавливает меню и настраивает параметры закрытия.
     */
    public MainApplicationFrame() {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
            screenSize.width  - inset*2,
            screenSize.height - inset*2);

        setContentPane(desktopPane);
        
        
        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);

        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400,  400);
        addWindow(gameWindow);

        setJMenuBar(createMenuBar());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    /**
     * Создает и настраивает окно журнала (LogWindow).
     */
    protected LogWindow createLogWindow()
    {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10,10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }
    /**
     * Добавляет внутреннее окно на панель рабочего пространства и делает его видимым.
     */
    protected void addWindow(JInternalFrame frame)
    {
        desktopPane.add(frame);
        frame.setVisible(true);
    }
    /**
     * Создает и настраивает менюбар с двумя меню: "Режим отображения" и "Тесты".
     */
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createLookAndFeelMenu()); // Создание меню "Режим отображения"
        menuBar.add(createTestMenu());       // Создание меню "Тесты"
        return menuBar;
    }
    /**
     * Создает меню "Режим отображения" с пунктами для выбора внешнего вида приложения.
     */
    private JMenu createLookAndFeelMenu() {
        JMenu lookAndFeelMenu = new JMenu("Режим отображения");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription("Управление режимом отображения приложения");

        lookAndFeelMenu.add(createSystemLookAndFeelItem());       // Пункт "Системная схема"
        lookAndFeelMenu.add(createCrossPlatformLookAndFeelItem()); // Пункт "Универсальная схема"

        return lookAndFeelMenu;
    }
    /**
     * Создает пункт меню "Универсальная схема" для выбора универсального внешнего вида.
     */
    private JMenuItem createCrossPlatformLookAndFeelItem() {
        JMenuItem crossPlatformLookAndFeel = new JMenuItem("Универсальная схема", KeyEvent.VK_S);
        crossPlatformLookAndFeel.addActionListener(event -> {
            setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            this.invalidate();
        });
        return crossPlatformLookAndFeel;
    }
    /**
     * Создает пункт меню "Системная схема" для выбора системного внешнего вида.
     */
    private JMenuItem createSystemLookAndFeelItem() {
        JMenuItem systemLookAndFeel = new JMenuItem("Системная схема", KeyEvent.VK_S);
        systemLookAndFeel.addActionListener(event -> {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            this.invalidate();
        });
        return systemLookAndFeel;
    }
    /**
     * Создает меню "Тесты" с пунктами для выполнения тестовых команд.
     */
    private JMenu createTestMenu() {
        JMenu testMenu = new JMenu("Тесты");
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription("Тестовые команды");

        testMenu.add(createAddLogMessageItem()); // Пункт "Сообщение в лог"

        return testMenu;
    }
    /**
     * Создает пункт меню "Сообщение в лог" для добавления записи в журнал.
     */
    private JMenuItem createAddLogMessageItem() {
        JMenuItem addLogMessageItem = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);
        addLogMessageItem.addActionListener(event -> Logger.debug("Новая строка"));
        return addLogMessageItem;
    }
    /**
     * Устанавливает внешний вид приложения.
     */
    private void setLookAndFeel(String className)
    {
        try
        {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        }
        catch (ClassNotFoundException | InstantiationException
            | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            // just ignore
        }
    }
}
