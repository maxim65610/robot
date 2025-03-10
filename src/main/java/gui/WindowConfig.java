package gui;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
/**
 * Класс для управления загрузкой и сохранением состояния окна в конфигурационный файл.
 * Этот класс используется для чтения и записи состояния в файл конфигурации, где сохраняются параметры
 * окна приложения, такие как размеры и положение на экране.
 */
public class WindowConfig {
    private final String configPath;

    public WindowConfig(String homeDir, String fileName) {
        this.configPath = homeDir + File.separator + fileName;
    }
    /**
     * Загружает состояние из конфигурационного файла в виде карты ключ-значение.
     */
    public Map<String, String> loadState() {
        Map<String, String> state = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(configPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    state.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return state;
    }
    /**
     * Сохраняет состояние в конфигурационный файл.
     */
    public void saveState(Map<String, String> state) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(configPath))) {
            for (Map.Entry<String, String> entry : state.entrySet()) {
                writer.write(entry.getKey() + " = " + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
