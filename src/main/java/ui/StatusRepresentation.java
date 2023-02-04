package ui;

import java.util.HashMap;
import java.util.Map;

import data.FileHandler;

/**
 * Definition of the colors for the status of a FileHanlder
 */
public class StatusRepresentation {

    private static final Map<String, String> _colors;

    static {
        _colors = new HashMap<>();
        _colors.put(FileHandler.Status.HANDLING_NO_ERROR.toString(),    "LightGreen");
        _colors.put(FileHandler.Status.HANDLING_WITH_ERROR.toString(),  "LightRed");
        _colors.put(FileHandler.Status.HANDLED_WITH_SUCCESS.toString(), "Green");
        _colors.put(FileHandler.Status.HANDLED_WITH_ERROR.toString(),   "Red");
        _colors.put(FileHandler.Status.FAILED_TO_HANDLE.toString(),     "Blue");
    }

    /**
     * @return
     */
    public static Map<String, String> getColorMap() {
        return _colors;
    }
}
