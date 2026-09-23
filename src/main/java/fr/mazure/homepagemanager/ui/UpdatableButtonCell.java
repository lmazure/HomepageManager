package fr.mazure.homepagemanager.ui;

import java.util.function.Consumer;

/**
 * Table cell containing a button whose label is updated with the cell content
 *
 * @param <S> Class of the object displayed in the column
 */
public class UpdatableButtonCell<S> extends ButtonCell<S> {

    /**
     * Constructor
     *
     * @param callback action to be executed when the button is clicked
     */
    public UpdatableButtonCell(final Consumer<S> callback) {
        super(callback);
        getButton().textProperty().bind(itemProperty());
    }

    @Override
    protected void updateItem(final String buttonLabel,
                              final boolean empty) {
        super.updateItem(buttonLabel, empty);
    }
}
