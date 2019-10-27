package ui;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class ActionButtonTableCell2<S> extends TableCell<S, Button> implements ObservableValue<Button> {

    private final Button _actionButton;
    private final Function<S, Optional<String>> _labelSetter;

    public ActionButtonTableCell2(
        final Function<S, Optional<String>> labelSetter,
        final Consumer<S> callback) {
        
        this.getStyleClass().add("action-button-table-cell");

        _actionButton = new Button();
        _actionButton.setOnAction(e -> callback.accept(getCurrentItem()));
        _actionButton.setMaxWidth(Double.MAX_VALUE);
        _actionButton.setMnemonicParsing(false);
        _labelSetter = labelSetter;
    }

    public S getCurrentItem() {
        if (getTableView() == null) return null;
        return (getTableView().getItems() == null) ? null : getTableView().getItems().get(getIndex());
    }

    static public <S> Callback<CellDataFeatures<S, Button>, ObservableValue<Button>> forTableColumn(
            final Function<S, Optional<String>> labelSetter,
            final Consumer<S> callback) {
        return param -> new ActionButtonTableCell2<S>(labelSetter, callback);
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

    final private Set<InvalidationListener> il = new HashSet<InvalidationListener>();
    final private Set<ChangeListener<? super Button>> l = new HashSet<ChangeListener<? super Button>>();
    
    @Override
    public void addListener(InvalidationListener arg0) {
        il.add(arg0);
        
    }

    @Override
    public void removeListener(InvalidationListener arg0) {
        il.remove(arg0);
        
    }

    @Override
    public void addListener(ChangeListener<? super Button> arg0) {
        l.add(arg0);
        
    }

    @Override
    public Button getValue() {
        if (getCurrentItem() == null) return null;
        return _actionButton;
    }

    @Override
    public void removeListener(ChangeListener<? super Button> arg0) {
        l.remove(arg0);
        
    }
}