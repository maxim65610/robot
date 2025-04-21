package localization;

import java.io.*;
/**
 * Отвечает за сохранение и загрузку текущей локали пользователя
 * в файл конфигурации. Используется для обеспечения сохранения языка между запусками приложения.
 */
public class LocaleStorage {
    private final String configPath = System.getProperty("user.home")
            +  File.separator + "locale.cfg";
    /**
     * Сохраняет код языка в файл конфигурации.
     */
    public void save(String language) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(configPath))) {
            oos.writeObject(language);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Загружает сохранённую локаль из файла конфигурации.
     */
    public String load() {
        File file = new File(configPath);
        if (!file.exists()) return "ru";

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (String) ois.readObject();
        } catch (Exception e) {
            return "ru";
        }
    }
}