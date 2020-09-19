package ui;

import java.util.function.Consumer;

import javafx.scene.control.Button;
import javafx.scene.control.TableCell;

abstract class ButtonCell<S> extends TableCell<S, String> {

    final private Button _cellButton;

    public ButtonCell(final Consumer<S> callback) {
        _cellButton = new Button();
        _cellButton.setMaxWidth(Double.MAX_VALUE);
        _cellButton.setMnemonicParsing(false);
        _cellButton.setOnAction(e -> callback.accept(getCurrentItem()));
    }

    protected Button getButton() {
        return _cellButton;
    }

    private S getCurrentItem() {
        return getTableView().getItems().get(getIndex());
    }

    @Override
    protected void updateItem(final String t, final boolean empty) {
        super.updateItem(t, empty);
        if (!empty) {
            setGraphic(_cellButton);
        }
    }
}