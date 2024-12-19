package fr.mazure.homepagemanager.ui;

import java.nio.file.Path;
import java.util.List;

import fr.mazure.homepagemanager.data.MetricsExtractor;
import fr.mazure.homepagemanager.data.SiteFilesGenerator;
import fr.mazure.homepagemanager.data.jsongenerator.JsonGenerator;
import javafx.concurrent.Task;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 */
public class GlobalFileCreationDialog extends Dialog<Void> {

    /**
     * Constructor
     * The constructor is displaying the progress dialog and launch the file generation
     *
     * @param homepage path of the homepage directory
     * @param files list of the paths of all files
     */
    public GlobalFileCreationDialog(final Path homepage,
                                    final List<Path> files) {
        final Task<Void> task = buildFileCreationTask(homepage, files);

        setTitle("Global File Creation");
        final TextArea textArea = new TextArea();
        textArea.setPrefHeight(100);
        textArea.setPrefWidth(400);
        textArea.textProperty().bind(task.messageProperty());
        final VBox vbox = new VBox(textArea);
        getDialogPane().setContent(vbox);
        initModality(Modality.NONE);

        task.setOnSucceeded(_ -> ((Stage)(getDialogPane().getScene().getWindow())).close());
        task.setOnFailed(_ -> {
            textArea.textProperty().unbind();
            textArea.setText(task.getException().getMessage());
            });

        show();

        new Thread(task).start();
    }

    private static Task<Void> buildFileCreationTask(final Path homepagePath,
                                                    final List<Path> files) {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
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
