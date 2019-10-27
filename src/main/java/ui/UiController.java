package ui;

import data.DataController;
import javafx.scene.control.TableColumn;

public interface UiController extends DataController {
    
    public TableColumn<ObservableFile, ?> getColumns();
}
