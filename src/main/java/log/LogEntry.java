package log;
/**
 * LogEntry представляет одну запись в журнале, содержащую уровень важности
 * (LogLevel) и текстовое сообщение.
 * Класс используется для хранения информации о конкретной записи лога.
 */
public class LogEntry
{
    private LogLevel m_logLevel;
    private String m_strMessage;
    /**
     * Конструктор LogEntry. Инициализирует запись лога с
     * указанным уровнем важности и сообщением.
     */
    public LogEntry(LogLevel logLevel, String strMessage)
    {
        m_strMessage = strMessage;
        m_logLevel = logLevel;
    }
    /**
     * Возвращает текстовое сообщение записи.
     */
    public String getMessage() {
        return m_strMessage;
    }
    /**
     * Возвращает уровень важности записи.
     */
    public LogLevel getLevel() {
        return m_logLevel;
    }
}

