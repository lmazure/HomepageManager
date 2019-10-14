package ui;

import java.util.List;

import data.DataController;
import javafx.scene.control.TableColumn;

public interface UiController extends DataController {
    
    public List<TableColumn<ObservableFile, ?>> getColumns();
}
