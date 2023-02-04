package ui;

import java.util.Iterator;
import java.util.function.Predicate;

import data.Violation;
import data.ViolationDataController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * List of violations
 */
public class ObservableViolationList implements ViolationDataController {

    private final ObservableList<Violation> _data;

    /**
     * Constructor
     */
    public ObservableViolationList() {
        _data = FXCollections.observableArrayList();
    }

    /**
     * @return observable list of violations
     */
    public ObservableList<Violation> getObservableList() {
        return _data;
    }

    @Override
    public void add(final Violation violation) {
        _data.add(violation);
    }

    @Override
    public void remove(final Predicate<Violation> violationFilter) {
        for (final Iterator<Violation> iterator = _data.iterator(); iterator.hasNext(); ) {
            final Violation violation = iterator.next();
            if (violationFilter.test(violation)) {
                iterator.remove();
            }
        }
    }
}
