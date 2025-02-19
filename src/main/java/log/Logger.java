package log;
/**
 * Logger представляет утилитарный класс для работы с журналом (логом) приложения.
 */
public final class Logger
{
    /**
     * Статический блок инициализирует источник журнала
     * по умолчанию с максимальной длиной очереди 100 записей.
     */
    private static final LogWindowSource defaultLogSource;
    static {
        defaultLogSource = new LogWindowSource(100);
    }
    /**
     * Приватный конструктор предотвращает создание экземпляров этого класса,
     * так как все методы являются статическими.
     */
    private Logger()
    {
    }
    /**
     * Добавляет сообщение уровня Debug в журнал.
     */
    public static void debug(String strMessage)
    {
        defaultLogSource.append(LogLevel.Debug, strMessage);
    }
    /**
     * Добавляет сообщение уровня Error в журнал.
     */
    public static void error(String strMessage)
    {
        defaultLogSource.append(LogLevel.Error, strMessage);
    }
    /**
     * Возвращает источник журнала по умолчанию.
     */
    public static LogWindowSource getDefaultLogSource()
    {
        return defaultLogSource;
    }
}
