package ui;

import java.util.HashMap;
import java.util.Map;

import data.FileHandler;

public class StatusRepresentation {

    private static final Map<String, String> _colors;

    static {
        _colors = new HashMap<>();
        _colors.put(FileHandler.Status.HANDLING_NO_ERROR.toString(), "LightGreen");
        _colors.put(FileHandler.Status.HANDLING_WITH_ERROR.toString(), "LightRed");
        _colors.put(FileHandler.Status.HANDLED_WITH_SUCCESS.toString(), "Green");
        _colors.put(FileHandler.Status.HANDLED_WITH_ERROR.toString(), "Red");
        _colors.put(FileHandler.Status.FAILED_TO_HANDLED.toString(), "Blue");
    }

    public static Map<String, String> getColorMap() {
        return _colors;
    }
}
