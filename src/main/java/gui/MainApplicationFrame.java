package gui;

import log.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class MainApplicationFrame extends JFrame {
    private final JDesktopPane desktopPane = new JDesktopPane();
    private final WindowConfig windowConfig;
    private final List<WindowState> windowStates = new ArrayList<>();

    public MainApplicationFrame() {

        String homeDir = System.getProperty("user.home");
        String userDir = homeDir + File.separator + System.getProperty("user.name");
        new File(userDir).mkdirs(); // Создаем директорию пользователя
        this.windowConfig = new WindowConfig(userDir, "state.cfg");

        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
            screenSize.width  - inset*2,
            screenSize.height - inset*2);

        setContentPane(desktopPane);

        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);
        windowStates.add(new InternalFrameStateAdapter(logWindow, "log"));

        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400,  400);
        addWindow(gameWindow);
        windowStates.add(new InternalFrameStateAdapter(gameWindow, "game"));

        restoreStateFromConfig();

        setJMenuBar(createMenuBar());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmExit();
            }
        });
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
        menuBar.add(createLookAndFeelMenu());
        menuBar.add(createTestMenu());
        menuBar.add(createManagementMenu());
        return menuBar;
    }
    /**
     * Создает пункт меню "Выход" с горячей клавишей Alt + X.
     */
    private JMenuItem createExitMenu(){
        JMenuItem exitItem = new JMenuItem("Выход", KeyEvent.VK_X);
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.ALT_MASK));
        exitItem.addActionListener(event -> {
            Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(
                    new WindowEvent(this, WindowEvent.WINDOW_CLOSING)
            );
        });
        return exitItem;
    }
    /**
     * Создает меню "Управление" с пунктом для выхода.
     */
    private JMenuItem createManagementMenu(){
        JMenu managementMenu = new JMenu("Управление");
        managementMenu.add(createExitMenu());
        return managementMenu;
    }
    /**
     * Создает меню "Режим отображения" с пунктами для выбора внешнего вида приложения.
     */
    private JMenu createLookAndFeelMenu() {
        JMenu lookAndFeelMenu = new JMenu("Режим отображения");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription("Управление режимом отображения приложения");

        lookAndFeelMenu.add(createSystemLookAndFeelItem());
        lookAndFeelMenu.add(createCrossPlatformLookAndFeelItem());

        return lookAndFeelMenu;
    }
    /**
     * Создает пункт меню "Универсальная схема" для выбора универсального внешнего вида.
     */
    private JMenuItem createCrossPlatformLookAndFeelItem() {
        JMenuItem crossPlatformLookAndFeel = new JMenuItem("Универсальная схема", KeyEvent.VK_S);
        crossPlatformLookAndFeel.addActionListener(event -> {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                SwingUtilities.updateComponentTreeUI(this);
            } catch (Exception ignored) {}
        });
        return crossPlatformLookAndFeel;
    }

    private JMenuItem createSystemLookAndFeelItem() {
        JMenuItem systemLookAndFeel = new JMenuItem("Системная схема", KeyEvent.VK_S);
        systemLookAndFeel.addActionListener(event -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                SwingUtilities.updateComponentTreeUI(this);
            } catch (Exception ignored) {}
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

        testMenu.add(createAddLogMessageItem());

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
    private void saveStateToConfig() {
        Map<String, String> state = new HashMap<>();
        for (WindowState windowState : windowStates) {
            windowState.saveState(state); // Сохраняем состояние каждого окна
        }
        windowConfig.saveState(state); // Записываем состояние в файл
    }

    private void restoreStateFromConfig() {
        Map<String, String> state = windowConfig.loadState(); // Загружаем состояние из файла
        for (WindowState windowState : windowStates) {
            windowState.restoreState(state); // Восстанавливаем состояние каждого окна
        }
    }


    private void confirmExit() {
        String[] options = {"Да", "Нет"};
        int result = JOptionPane.showOptionDialog(
                this,
                "Вы действительно хотите выйти?",
                "Подтверждение выхода",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]

        );

        if (result == JOptionPane.YES_OPTION) {
            saveStateToConfig();
            dispose();
            System.exit(0);
        }
    }
}
