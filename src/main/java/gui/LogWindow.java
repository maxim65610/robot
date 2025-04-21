package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextArea;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import localization.LocaleChangeListener;
import localization.LocaleManager;
import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;
/**
 * LogWindow представляет внутреннее окно Swing, отображающее журнал работы приложения.
 * Класс реализует интерфейс LogChangeListener для автоматического обновления
 * содержимого журнала при изменении данных.
 */
public class LogWindow extends JInternalFrame implements LogChangeListener, LocaleChangeListener
{
    private LogWindowSource m_logSource;
    private TextArea m_logContent;
    /**
     * Конструктор LogWindow.
     * Инициализирует внутреннее окно с заголовком "Протокол работы", регистрирует слушателя изменений журнала,
     * создает текстовое поле для вывода логов и добавляет его в центральную область панели.
     * @param logSource Источник журнала, предоставляющий данные для отображения.
     */
    public LogWindow(LogWindowSource logSource) 
    {
        super(LocaleManager.getInstance().getString("logWindowTitle")
                , true, true, true, true);
        m_logSource = logSource;
        m_logSource.registerListener(this);
        m_logContent = new TextArea("");
        m_logContent.setSize(200, 500);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        LocaleManager.getInstance().addListener(this);
        pack();
        updateLogContent();
    }
    /**
     * Вызывается при смене локали.
     */
    @Override
    public void onLocaleChanged() {
        setTitle(LocaleManager.getInstance().getString("logWindowTitle"));
    }
    /**
     * Обновляет содержимое текстового поля журнала, собирая все записи из источника лога.
     */
    private void updateLogContent()
    {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : m_logSource.all())
        {
            content.append(entry.getMessage()).append("\n");
        }
        m_logContent.setText(content.toString());
        m_logContent.invalidate();
    }
    /**
     * Метод вызывается при изменении содержимого журнала.
     * Запускает обновление содержимого текстового поля в потоке обработки событий AWT.
     */
    @Override
    public void onLogChanged()
    {
        EventQueue.invokeLater(this::updateLogContent);
    }
}
