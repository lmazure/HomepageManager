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
        _file = new SimpleStringProperty(violation.file());
        _type = new SimpleStringProperty(violation.type());
        _rule = new SimpleStringProperty(violation.rule());
        _description = new SimpleStringProperty(violation.description());
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
