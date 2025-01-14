package fr.mazure.homepagemanager.ui;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Function;

import javax.net.ssl.SSLHandshakeException;

import fr.mazure.homepagemanager.utils.ExitHelper;
import fr.mazure.homepagemanager.utils.FileHelper;
import fr.mazure.homepagemanager.utils.internet.UrlHelper;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 *
 */
public class ActionHelper {

    /**
     * Display a file using the OS default application
     *
     * @param file File to be displayed
     */
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

    /**
     * Display a HTML file (of the homepage) using the OS default browser
     *
     * @param file File to be displayed
     * @param homepagePath Path to the directory containing the pages
     */
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

    /**
     * Modify the content of a file by applying a transformation to it
     *
     * @param file File whose content is to be modified
     * @param apply Transformation to be applied to the file content
     */
    public static void modifyFile(final String file, // TODO this is not homogeneous with the other actions, we should have a file defined by a Path relative to the home directory
                                  final Optional<Function<String, String>> apply) {

        if (apply.isEmpty()) {
            return;
        }

        final String oldContent = FileHelper.slurpFile(new File(file));
        final String newContent = apply.get().apply(oldContent);
        FileHelper.writeFile(Path.of(file), newContent);
    }

    private static boolean isUrlAlive(final URL url) {
        HttpURLConnection huc = null;
        try {
            huc = (HttpURLConnection) url.openConnection();
            huc.setConnectTimeout(2000);
            huc.setReadTimeout(2000);
            huc.setRequestMethod("HEAD");
            final int responseCode = huc.getResponseCode();
            return (responseCode >= 200 && responseCode < 400);
        } catch (@SuppressWarnings("unused") final SSLHandshakeException e) {
            return true;
        } catch (@SuppressWarnings("unused") final IOException e) {
            return false;
        } finally {
            if (huc != null) {
                huc.disconnect();
            }
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
        final Label label = new Label("The exception stacktrace was:");
        final TextArea textArea = new TextArea(exceptionText);

        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);
        final GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);
        alert.getDialogPane().setExpandableContent(expContent);
        alert.showAndWait();
    }
}
