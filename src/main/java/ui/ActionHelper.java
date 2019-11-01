package ui;

import java.awt.Desktop;
import java.io.IOException;
import java.nio.file.Path;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import utils.ExitHelper;

public class ActionHelper {

    static public void displayFile(final Path file) {
        
        if (file == null) {
            final Alert alert = new Alert(AlertType.INFORMATION, "Undefined file");
            alert.showAndWait();
            return;
        }

        if (!file.toFile().isFile()) {
            final Alert alert = new Alert(AlertType.INFORMATION, "The file " + file + " does not exist.");
            alert.showAndWait();
            return;
        }
        
        try {
            Desktop.getDesktop().browse(file.toUri());
        } catch (final IOException e) {
            ExitHelper.exit(e);
        }
    }
}
