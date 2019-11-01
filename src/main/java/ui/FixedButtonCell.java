package ui;

import java.util.function.Consumer;

public class FixedButtonCell<S> extends ButtonCell<S> {

    public FixedButtonCell(final String label, final Consumer<S> callback) {
        super(callback);
        getButton().setText(label);
    }
}
