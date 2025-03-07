package gui;

import java.beans.PropertyVetoException;
import java.util.Map;
/**
 * Интерфейс, который определяет методы для сохранения и восстановления состояния окна.
 */
public interface WindowState {
    /**
     * Этот метод используется для сохранения параметров состояния окна, таких как размеры, положение,
     * и другие параметры, которые могут быть восстановлены позже.
     */
    void saveState(Map<String, String> state);
    /**
     * Этот метод используется для восстановления параметров состояния окна из сохранённого состояния,
     * таких как размеры, положение и другие параметры.
     */
    void restoreState(Map<String, String> state);
}
