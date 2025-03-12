package gui;

import javax.swing.JFrame;
import java.util.Map;

/**
 * MainFrameStateAdapter сохраняет и восстанавливает состояние главного окна (JFrame),
 * включая его размеры, положение и расширенное состояние.
 */
public class MainFrameStateAdapter implements WindowState {
    private final JFrame frame;
    private final String prefix;

    public MainFrameStateAdapter(JFrame frame, String prefix) {
        this.frame = frame;
        this.prefix = prefix;
    }
    /**
     * Сохраняет состояние главного окна в карту состояния с использованием префикса.
     */
    @Override
    public void saveState(Map<String, String> state) {
        PrefixedMap prefixedState = new PrefixedMap(state, prefix);
        prefixedState.put("width", Integer.toString(frame.getWidth()));
        prefixedState.put("height", Integer.toString(frame.getHeight()));
        prefixedState.put("x", Integer.toString(frame.getX()));
        prefixedState.put("y", Integer.toString(frame.getY()));
        prefixedState.put("extendedState", Integer.toString(frame.getExtendedState()));
    }
    /**
     * Восстанавливает состояние главного окна из карты состояния, используя префикс.
     */
    @Override
    public void restoreState(Map<String, String> state) {
        PrefixedMap prefixedState = new PrefixedMap(state, prefix);

        // Переводим окно в нормальное состояние, чтобы можно было изменить размеры и положение
        frame.setExtendedState(JFrame.NORMAL);

        if (prefixedState.containsKey("width") && prefixedState.containsKey("height")) {
            frame.setSize(
                    Integer.parseInt(prefixedState.get("width")),
                    Integer.parseInt(prefixedState.get("height"))
            );
        }
        if (prefixedState.containsKey("x") && prefixedState.containsKey("y")) {
            frame.setLocation(
                    Integer.parseInt(prefixedState.get("x")),
                    Integer.parseInt(prefixedState.get("y"))
            );
        }
        if (prefixedState.containsKey("extendedState")) {
            int extState = Integer.parseInt(prefixedState.get("extendedState"));
            frame.setExtendedState(extState);
        }
    }
}
