package ui;

import data.Violation;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 */
public class ObservableViolation {

    private final SimpleStringProperty _file;
    private final SimpleStringProperty _type;
    private final SimpleStringProperty _rule;
    private final SimpleStringProperty _description;

    /**
     * @param violation
     */
    public ObservableViolation(final Violation violation) {
        _file = new SimpleStringProperty(violation.getFile());
        _type = new SimpleStringProperty(violation.getType());
        _rule = new SimpleStringProperty(violation.getRule());
        _description = new SimpleStringProperty(violation.getDescription());
    }

    /**
     * @return the file
     */
    public SimpleStringProperty getFile() {
        return _file;
    }

    /**
     * @return the type
     */
    public SimpleStringProperty getType() {
        return _type;
    }

    /**
     * @return the rule
     */
    public SimpleStringProperty getRule() {
        return _rule;
    }

    /**
     * @return the description
     */
    public SimpleStringProperty getDescription() {
        return _description;
    }
}
