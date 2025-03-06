package gui;

import javax.swing.JInternalFrame;
import java.beans.PropertyVetoException;
import java.util.Map;

public class InternalFrameStateAdapter implements WindowState {
    private final JInternalFrame frame;
    private final String prefix;

    public InternalFrameStateAdapter(JInternalFrame frame, String prefix) {
        this.frame = frame;
        this.prefix = prefix + ".";
    }

    @Override
    public void saveState(Map<String, String> state) {
        PrefixedMap prefixedState = new PrefixedMap(state, prefix);

        prefixedState.put("width", Integer.toString(frame.getWidth()));
        prefixedState.put("height", Integer.toString(frame.getHeight()));
        prefixedState.put("x", Integer.toString(frame.getX()));
        prefixedState.put("y", Integer.toString(frame.getY()));
        prefixedState.put("iconified", Boolean.toString(isIconified()));
    }

    @Override
    public void restoreState(Map<String, String> state) {

        PrefixedMap prefixedState = new PrefixedMap(state, prefix);

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

        if (prefixedState.containsKey("iconified")) {
            setIconified(Boolean.parseBoolean(prefixedState.get("iconified")));
        }
    }

    private boolean isIconified() {
        return frame.isIcon(); // Проверяем, свернуто ли окно
    }

    private void setIconified(boolean iconified) {
        try {
            if (iconified) {
                frame.setIcon(true);
            } else {
                frame.setIcon(false);
            }
        } catch (PropertyVetoException ignored) {
        }
    }
}