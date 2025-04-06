package log;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;
/**
 * Источник сообщений для окна лога с поддержкой подписки на изменения.
 * Хранит сообщения в кольцевом буфере фиксированного размера и уведомляет
 * зарегистрированных слушателей о новых сообщениях.
 */
public class LogWindowSource {
    private final int m_iQueueLength;
    private final CircularBuffer<LogEntry> m_messages;
    private final Set<LogChangeListener> m_listeners;
    private volatile LogChangeListener[] m_activeListeners;
    /**
     * Создает новый источник лога с указанной емкостью.
     */
    public LogWindowSource(int iQueueLength) {
        m_iQueueLength = iQueueLength;
        m_messages = new CircularBuffer<>(iQueueLength);
        m_listeners = Collections.newSetFromMap(new WeakHashMap<>());
    }
    /**
     * Регистрирует слушателя изменений лога.
     */
    public void registerListener(LogChangeListener listener) {
        synchronized (m_listeners) {
            m_listeners.add(listener);
            m_activeListeners = null;
        }
    }
    /**
     * Отменяет регистрацию слушателя.
     */
    public void unregisterListener(LogChangeListener listener) {
        synchronized (m_listeners) {
            m_listeners.remove(listener);
            m_activeListeners = null;
        }
    }
    /**
     * Добавляет новое сообщение в лог и уведомляет слушателей.
     */
    public void append(LogLevel logLevel, String message) {
        LogEntry entry = new LogEntry(logLevel, message);
        m_messages.add(entry);

        LogChangeListener[] activeListeners = m_activeListeners;
        if (activeListeners == null) {
            synchronized (m_listeners) {
                if (m_activeListeners == null) {
                    activeListeners = m_listeners.toArray(new LogChangeListener[0]);
                    m_activeListeners = activeListeners;
                }
            }
        }
        for (LogChangeListener listener : activeListeners) {
            listener.onLogChanged();
        }
    }
    /**
     * Возвращает текущее количество сообщений в логе.
     */
    public int size() {
        return m_messages.size();
    }
    /**
     * Возвращает диапазон сообщений из лога.
     */
    public Iterable<LogEntry> range(int startFrom, int count) {
        return m_messages.range(startFrom, count);
    }
    /**
     * Возвращает все сообщения в логе.
     */
    public Iterable<LogEntry> all() {
        return m_messages;
    }
}