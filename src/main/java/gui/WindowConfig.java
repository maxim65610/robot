package gui;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class WindowConfig {
    private final String configPath;

    public WindowConfig(String homeDir, String fileName) {
        this.configPath = homeDir + File.separator + fileName;
    }

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
        } catch (IOException ignored) {}
        return state;
    }

    public void saveState(Map<String, String> state) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(configPath))) {
            for (Map.Entry<String, String> entry : state.entrySet()) {
                writer.write(entry.getKey() + " = " + entry.getValue());
                writer.newLine();
            }
        } catch (IOException ignored) {}
    }
}
