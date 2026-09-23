package fr.mazure.homepagemanager.ui;

import java.util.Map;
import java.util.function.Consumer;

/**
 * Table cell containing a button whose label is updated with the cell content and whose color depends on the label
 *
 * @param <S> Class of the object displayed in the column
 */
public class ColoredUpdatableButtonCell<S> extends ButtonCell<S> {

    private final Map<String, String> _colorMap;

    /**
     * Constructor
     *
     * @param callback action to be executed when the button is clicked
     * @param colorMap map between the labels and their color
     */
    public ColoredUpdatableButtonCell(final Consumer<S> callback,
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
    }
}
