package gui;

import controller.GameController;
import controller.MouseController;
import localization.LocaleChangeListener;
import localization.LocaleManager;
import log.Logger;
import model.RobotModel;
import view.GameVisualizer;
import view.RobotCoordinatesWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
/**
 * Представляет главное окно приложения.
 * Оно управляет внутренними окнами, пользовательским интерфейсом
 * (меню, визуализаторы, лог), а также сохраняет и восстанавливает состояние между запусками.
 * Кроме того, данный класс реализует интерфейс LocaleChangeListener и поддерживает
 * локализацию пользовательского интерфейса, включая меню и сообщения.
 */
public class MainApplicationFrame extends JFrame implements LocaleChangeListener {
    private final JDesktopPane desktopPane = new JDesktopPane();
    private final WindowConfig windowConfig;
    private final List<WindowState> windowStates = new ArrayList<>();
    private JMenu lookAndFeelMenu;
    private JMenu testMenu;
    private JMenu languageMenu;
    private JMenu managementMenu;
    private JMenuItem exitItem;
    private JMenuItem systemLookAndFeelItem;
    private JMenuItem crossPlatformLookAndFeelItem;
    private JMenuItem addLogMessageItem;
    private JMenuItem russianItem;
    private JMenuItem englishItem;
    /**
     * Конструктор, инициализирующий главное окно:
     * Создает и отображает визуализатор и координаты робота.
     * Создает контроллеры управления.
     * Устанавливает меню и поддержку локализации.
     */
    public MainApplicationFrame() {
        LocaleManager.getInstance().addListener(this);
        RobotModel model = new RobotModel();
        GameVisualizer view = new GameVisualizer(model);
        new GameController(model);
        new MouseController(model, view);

        String userDir = System.getProperty("user.home");
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

        GameWindow gameWindow = new GameWindow(view);
        gameWindow.setSize(400,  400);
        addWindow(gameWindow);
        windowStates.add(new InternalFrameStateAdapter(gameWindow, "game"));

        RobotCoordinatesWindow robotCoordinatesWindow = new RobotCoordinatesWindow(model);
        addWindow(robotCoordinatesWindow);
        windowStates.add(new InternalFrameStateAdapter(robotCoordinatesWindow, "coordinate"));

        windowStates.add(new MainFrameStateAdapter(this, "main"));

        restoreStateFromConfig();
        setJMenuBar(createMenuBar());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmExitAndSaveState();
            }
        });
    }
    /**
     * Метод, вызываемый при смене языка интерфейса.
     * Обновляет текст всех пунктов меню в соответствии с новой локалью.
     */
    @Override
    public void onLocaleChanged() {
        updateMenuTexts();
    }
    /**
     * Обновляет надписи всех пунктов меню с учетом текущей локали.
     * Вызывается как при инициализации, так и при смене языка.
     */
    private void updateMenuTexts() {
        LocaleManager locale = LocaleManager.getInstance();
        lookAndFeelMenu.setText(locale.getString("menu.view"));
        exitItem.setText(locale.getString("menu.exit"));
        testMenu.setText(locale.getString("menu.tests"));
        languageMenu.setText(locale.getString("menu.language"));
        managementMenu.setText(locale.getString("menu.management"));
        systemLookAndFeelItem.setText(locale.getString("menu.view.system"));
        crossPlatformLookAndFeelItem.setText(locale.getString("menu.view.cross"));
        addLogMessageItem.setText(locale.getString("menu.tests.logMessage"));
    }
    /**
     * Создает меню выбора языка с двумя пунктами — Русский и Английский.
     * Меняет локаль в LocaleManager при выборе соответствующего пункта
     */
    private JMenu createLanguageMenu() {
        languageMenu = new JMenu(LocaleManager.getInstance().getString("menu.language"));
        russianItem = new JMenuItem("Русский");
        englishItem = new JMenuItem("English");

        russianItem.addActionListener(e ->
                LocaleManager.getInstance().setLocale("ru"));
        englishItem.addActionListener(e ->
                LocaleManager.getInstance().setLocale("en"));

        languageMenu.add(russianItem);
        languageMenu.add(englishItem);
        return languageMenu;
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
     * Создает строку меню с подменю:
     * Режим отображения
     * Тесты
     * Управление
     * Язык
     * и настраивает их тексты с учетом текущей локали.
     */
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createLookAndFeelMenu());
        menuBar.add(createTestMenu());
        menuBar.add(createManagementMenu());
        menuBar.add(createLanguageMenu());
        updateMenuTexts();
        return menuBar;
    }
    /**
     * Создает пункт меню "Выход"
     */
    private JMenuItem createExitMenu(){
        exitItem = new JMenuItem();
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
        managementMenu = new JMenu();
        managementMenu.add(createExitMenu());
        return managementMenu;
    }
    /**
     * Создает меню "Режим отображения" с пунктами для выбора внешнего вида приложения.
     */
    private JMenu createLookAndFeelMenu() {
        lookAndFeelMenu = new JMenu();
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);

        lookAndFeelMenu.add(createSystemLookAndFeelItem());
        lookAndFeelMenu.add(createCrossPlatformLookAndFeelItem());

        return lookAndFeelMenu;
    }
    /**
     * Создает пункт меню "Универсальная схема" для выбора универсального внешнего вида.
     */
    private JMenuItem createCrossPlatformLookAndFeelItem() {
        crossPlatformLookAndFeelItem = new JMenuItem();
        crossPlatformLookAndFeelItem.addActionListener(event -> {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                SwingUtilities.updateComponentTreeUI(this);
            } catch (Exception ignored) {}
        });
        return crossPlatformLookAndFeelItem;
    }
    /**
     * Создает пункт меню для выбора системной схемы внешнего вида.
     */
    private JMenuItem createSystemLookAndFeelItem() {
        systemLookAndFeelItem = new JMenuItem();
        systemLookAndFeelItem.addActionListener(event -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                SwingUtilities.updateComponentTreeUI(this);
            } catch (Exception ignored) {}
        });
        return systemLookAndFeelItem;
    }
    /**
     * Создает меню "Тесты" с пунктами для выполнения тестовых команд.
     */
    private JMenu createTestMenu() {
        testMenu = new JMenu();
        testMenu.setMnemonic(KeyEvent.VK_T);

        testMenu.add(createAddLogMessageItem());

        return testMenu;
    }
    /**
     * Создает пункт меню "Сообщение в лог" для добавления записи в журнал.
     */
    private JMenuItem createAddLogMessageItem() {
        addLogMessageItem = new JMenuItem();
        addLogMessageItem.addActionListener(event -> Logger.debug
                (LocaleManager.getInstance().getString("log.test")));
        return addLogMessageItem;
    }
    /**
     * Сохраняет состояние всех окон в конфигурационный файл.
     */
    private void saveStateToConfig() {
        Map<String, String> state = new HashMap<>();
        for (WindowState windowState : windowStates) {
            windowState.saveState(state);
        }
        windowConfig.saveState(state);
    }
    /**
     * Восстанавливает состояние окон из конфигурационного файла.
     */
    private void restoreStateFromConfig() {
        Map<String, String> state = windowConfig.loadState();
        if (state.isEmpty()) {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        } else {
            for (WindowState windowState : windowStates) {
                windowState.restoreState(state);
            }
        }
    }
    /**
     * Подтверждает выход из приложения и сохраняет состояние перед выходом.
     */
    private void confirmExitAndSaveState() {
        LocaleManager localeManager = LocaleManager.getInstance();
        String[] options = {localeManager.getString("confirmation.yes"),
                localeManager.getString("confirmation.no")};
        int result = JOptionPane.showOptionDialog(
                this,
                localeManager.getString("exitConfirmation"),
                localeManager.getString("exitTitle"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]

        );

        if (result == JOptionPane.YES_OPTION) {
            localeManager.saveLanguage();
            saveStateToConfig();
            dispose();
            System.exit(0);
        }
    }
}
