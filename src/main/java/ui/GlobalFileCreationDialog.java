package ui;

import java.nio.file.Path;
import java.util.List;

import data.MetricsExtractor;
import data.SiteFilesGenerator;
import data.jsongenerator.JsonGenerator;
import javafx.concurrent.Task;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GlobalFileCreationDialog extends Dialog<Void> {

    public GlobalFileCreationDialog(final Path homepagePath,
                                    final List<Path> files) {
        super();

        final Task<Void> task = buildFileCreationTask(homepagePath, files);

        setTitle("Global File Creation");
        final TextArea textArea = new TextArea();
        textArea.setPrefHeight(100); 
        textArea.setPrefWidth(400);
        textArea.textProperty().bind(task.messageProperty());
        final VBox vbox = new VBox(textArea);
        getDialogPane().setContent(vbox);
        initModality(Modality.NONE);

        task.setOnSucceeded(event -> ((Stage)(getDialogPane().getScene().getWindow())).close());

        show();

        new Thread(task).start();
    }

    private static Task<Void> buildFileCreationTask(final Path homepagePath,
                                                    final List<Path> files) {
        return new Task<>() {
            @Override 
            protected Void call() {
                final StringBuilder status = new StringBuilder();

                SiteFilesGenerator.generate(homepagePath, files);
                status.append("site files generated");
                updateMessage(status.toString());

                JsonGenerator.generate(homepagePath, files);
                status.append("\nJSON files generated");
                updateMessage(status.toString());

                MetricsExtractor.generate(homepagePath);
                status.append("\nmetric files generated");
                updateMessage(status.toString());

                return null;
            }
        };
    }
}
