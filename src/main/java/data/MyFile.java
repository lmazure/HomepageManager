package data;
import java.nio.file.Path;

import javafx.beans.property.SimpleStringProperty;

public class MyFile {

    private final SimpleStringProperty _name;
    
    public MyFile(final Path path) {
        _name = new SimpleStringProperty(path.toString());
        System.out.println("MyFile " + path.toString());
    }

    public SimpleStringProperty nameProperty() {
        return _name;
    }
    
    public String getName() {
        return _name.get();
    }

    public void setName(final String name) { // TODO do we really need this method?
       _name.set(name);
    }
}
