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
    protected void updateItem(final String t, final boolean empty) {
        if ((t!=null) && _colorMap.containsKey(t)) {
            setColor(_colorMap.get(t));
        } else {
            setColor("black");
        }
        super.updateItem(t, empty);
    }}
