package ui;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class ActionButtonTableCell<S> extends TableCell<S, Button> {

    private final Button _actionButton;
    private final Function<S, Optional<String>> _labelSetter;

    public ActionButtonTableCell(
        final Function<S, Optional<String>> labelSetter,
        final Consumer< S> callback) {
        
        this.getStyleClass().add("action-button-table-cell");

        _actionButton = new Button();
        _actionButton.setOnAction(e -> callback.accept(getCurrentItem()));
        _actionButton.setMaxWidth(Double.MAX_VALUE);
        _labelSetter = labelSetter;
    }

    public S getCurrentItem() {
        return getTableView().getItems().get(getIndex());
    }

    static public <S> Callback<TableColumn<S, Button>, TableCell<S, Button>> forTableColumn(
            final Function<S, Optional<String>> labelSetter,
            final Consumer<S> callback) {
        return param -> new ActionButtonTableCell<>(labelSetter, callback);
    }

    @Override
    public void updateItem(final Button item, final boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setGraphic(null);
        } else {
            final Optional<String> label = _labelSetter.apply(getCurrentItem());
            if (label.isPresent()) {
                _actionButton.setText(label.get());
                setGraphic(_actionButton);
            } else {
                setGraphic(null);                
            }
        }
    }
}