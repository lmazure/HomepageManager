package fr.mazure.homepagemanager.ui;

import java.util.function.Consumer;

/**
 * Table cell containing a button with a fixed label
 *
 * @param <S> Class of the object displayed in the column
 */
public class FixedButtonCell<S> extends ButtonCell<S> {

    /**
     * Constructor
     *
     * @param label label of the button
     * @param callback action to be executed when the button is clicked
     */
    public FixedButtonCell(final String label,
                           final Consumer<S> callback) {
        super(callback);
        setColor("black");
        getButton().setText(label);
    }
}
