package ui;

import java.util.function.Consumer;

public class UpdatableButtonCell<S> extends ButtonCell<S> {
    
    public UpdatableButtonCell(final Consumer<S> callback) {
        super(callback);
        getButton().textProperty().bind(itemProperty());
    }
}
