package fr.mazure.homepagemanager.ui;

import java.util.function.Consumer;

import javafx.scene.control.Button;
import javafx.scene.control.TableCell;

/**
 * Table cell containing a button triggering an action when clicked
 *
 * @param <S> Class of the object displayed in the column
 */
abstract class ButtonCell<S> extends TableCell<S, String> {

    private final Button _cellButton;

    /**
     * Constructor
     *
     * @param callback action to be executed when the button is clicked
     */
    public ButtonCell(final Consumer<S> callback) {
        _cellButton = new Button();
        _cellButton.setMaxWidth(Double.MAX_VALUE);
        _cellButton.setMnemonicParsing(false);
        _cellButton.setOnAction(_ -> callback.accept(getCurrentItem()));
    }

    /**
     * @return the button of the cell
     */
    protected Button getButton() {
        return _cellButton;
    }

    private S getCurrentItem() {
        return getTableView().getItems().get(getIndex());
    }

    @Override
    protected void updateItem(final String buttonLabel,
                              final boolean empty) {
        super.updateItem(buttonLabel, empty);
        if (!empty) {
            setGraphic(_cellButton);
        }
    }

    /**
     * Set the color of the text and the border of the button
     *
     * @param color color of the text and the border of the button
     */
    protected void setColor(final String color) {
        _cellButton.setStyle("-fx-text-fill: " +
                             color +
                             ";-fx-background-color: white;-fx-border-color: " +
                             color);
    }
}