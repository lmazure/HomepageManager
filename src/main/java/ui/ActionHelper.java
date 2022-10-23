package ui;

import java.awt.Desktop;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

import javax.net.ssl.SSLHandshakeException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import utils.ExitHelper;
import utils.UrlHelper;

public class ActionHelper {

    public static void displayFile(final Path file) {

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

        displayVerifiedFile(file);
    }

    public static void displayHtmlFile(final Path file,
                                       final Path homepagePath) {

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

        final Path relativePath = homepagePath.relativize(file);
        final String url = "http://localhost/" + relativePath.toString().replace(java.io.File.separatorChar, '/');
        final URL u = UrlHelper.convertStringToUrl(url);
        if (isUrlAlive(u)) {
            try {
                Desktop.getDesktop().browse(u.toURI());
            } catch (final IOException | URISyntaxException e) {
                ExitHelper.exit(e);
            }
        } else {
            displayVerifiedFile(file);
        }

    }

    private static void displayVerifiedFile(final Path file) {
        try {
            Desktop.getDesktop().browse(file.toUri());
        } catch (final IOException e) {
            displayExceptionError("Error",
                                  "File display error",
                                  "An exception occurred while displaying file " + file.toString(),
                                  e);
        }
    }

    private static boolean isUrlAlive(final URL url) {
        try {
            final HttpURLConnection huc = (HttpURLConnection)url.openConnection();
            final int responseCode = huc.getResponseCode();
            return (responseCode == 200);
        } catch (final IOException e) {
            return e instanceof SSLHandshakeException;
        }
    }

    private static void displayExceptionError(final String dialogTitle,
                                              final String header,
                                              final String errorMessage,
                                              final Throwable exception) {
        // from https://code.makery.ch/blog/javafx-dialogs-official/
        final Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(dialogTitle);
        alert.setHeaderText(header);
        alert.setContentText(errorMessage);

        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        final String exceptionText = sw.toString();
        Label label = new Label("The exception stacktrace was:");
        final TextArea textArea = new TextArea(exceptionText);

        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);
        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);
        alert.getDialogPane().setExpandableContent(expContent);
        alert.showAndWait();
    }
}
