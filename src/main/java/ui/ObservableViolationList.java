package ui;

import data.Violation;
import data.ViolationDataController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ObservableViolationList implements ViolationDataController {

    private final ObservableList<ObservableViolation> _data;

    public ObservableViolationList() {
        _data = FXCollections.observableArrayList();
    }


    public ObservableList<ObservableViolation> getObservableList() {
        return _data;
    }

    @Override
    public void add(final Violation violation) {
        _data.add(new ObservableViolation(violation));
    }

    @Override
    public void remove(final Violation violation) {
        _data.remove(new ObservableViolation(violation));
    }

    public ObservableList<ObservableViolation> getObservableViolationList() {
        return _data;
    }
}
