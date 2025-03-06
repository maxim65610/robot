package gui;

import java.beans.PropertyVetoException;
import java.util.Map;

public interface WindowState {
    void saveState(Map<String, String> state);
    void restoreState(Map<String, String> state);
}
