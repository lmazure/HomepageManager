package ui;

import java.util.function.Consumer;

/**
 *
 * @param <S>
 */
public class UpdatableButtonCell<S> extends ButtonCell<S> {

    /**
     * @param callback
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
