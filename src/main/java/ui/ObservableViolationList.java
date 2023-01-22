package ui;

import data.Violation;
import data.ViolationDataController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ObservableViolationList implements ViolationDataController {

    private final ObservableList<Violation> _data;

    public ObservableViolationList() {
        _data = FXCollections.observableArrayList();
    }


    public ObservableList<Violation> getObservableList() {
        return _data;
    }

    @Override
    public void add(final Violation violation) {
        _data.add(violation);
        
        System.out.println("---- before add");
        for (Violation o: _data) {
            System.out.println(o);
        }
        System.out.println("---- after add");
    }

    @Override
    public void remove(final Violation violation) {
        _data.remove(violation);
    }

    public ObservableList<Violation> getObservableViolationList() {
        return _data;
    }
}
