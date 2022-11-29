package ui;

import java.util.function.Consumer;

/**
 *
 * @param <S>
 */
public class FixedButtonCell<S> extends ButtonCell<S> {

    /**
     * @param label
     * @param callback
     */
    public FixedButtonCell(final String label,
                           final Consumer<S> callback) {
        super(callback);
        setColor("black");
        getButton().setText(label);
    }
}
