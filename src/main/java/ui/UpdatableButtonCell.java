package ui;

import java.util.Map;
import java.util.function.Consumer;

public class UpdatableButtonCell<S> extends ButtonCell<S> {

    private final Map<String, String> _colorMap;

    public UpdatableButtonCell(final Consumer<S> callback,
                               final Map<String, String> colorMap) {
        super(callback);
        getButton().textProperty().bind(itemProperty());
        _colorMap = colorMap;
    }

    @Override
    protected void updateItem(final String buttonLabel,
                              final boolean empty) {
        if ((buttonLabel != null) && _colorMap.containsKey(buttonLabel)) {
            setColor(_colorMap.get(buttonLabel));
        } else {
            setColor("black");
        }
        super.updateItem(buttonLabel, empty);
    }}
