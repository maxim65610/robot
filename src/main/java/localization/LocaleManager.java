package localization;

import java.util.*;
import java.util.List;
/**
 * Управляет текущей локалью приложения и уведомляет
 * слушателей об изменениях языка. Поддерживает загрузку и сохранение языка
 * между запусками, используя LocaleStorage
 */
public class LocaleManager {
    private static LocaleManager instance;
    private ResourceBundle bundle;
    private Locale currentLocale;
    private final List<LocaleChangeListener> listeners = new ArrayList<>();
    private final LocaleStorage localeStorage = new LocaleStorage();
    /**
     * Приватный конструктор. Загружает сохранённую локаль из LocaleStorage
     */
    private LocaleManager() {
        String savedLanguage= localeStorage.load();
        currentLocale = new Locale(savedLanguage);
        bundle = ResourceBundle.getBundle("messages", currentLocale);
    }
    /**
     * Получает экземпляр LocaleManager (реализация паттерна синглтон)
     */
    public static synchronized LocaleManager getInstance() {
        if (instance == null) {
            instance = new LocaleManager();
        }
        return instance;
    }
    /**
     * Регистрирует слушателя, который будет уведомлён при смене локали.
     */
    public void addListener(LocaleChangeListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    /**
     * Удаляет слушателя из списка подписчиков на смену локали.
     */
    public void removeListener(LocaleChangeListener listener) {
        listeners.remove(listener);
    }
    /**
     * Устанавливает новую локаль и обновляет ресурсный пакет. После этого
     * уведомляет всех зарегистрированных слушателей.
     */
    public void setLocale(String language) {
        currentLocale = new Locale(language);
        bundle = ResourceBundle.getBundle("messages", currentLocale);
        notifyListeners();
    }
    /**
     * Возвращает локализованную строку по ключу.
     */
    public String getString(String key) {
        return bundle.getString(key);
    }
    /**
     * Уведомляет всех слушателей о смене локали.
     */
    private void notifyListeners() {
        new ArrayList<>(listeners).forEach(LocaleChangeListener::onLocaleChanged);
    }
    /**
     * Сохраняет текущий язык в конфигурационный файл.
     */
    public void saveLanguage(){
        localeStorage.save(currentLocale.getLanguage());
    }
}