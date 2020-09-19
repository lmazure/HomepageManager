package ui;

import java.awt.Desktop;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

import javax.net.ssl.SSLHandshakeException;

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

        displayVerifiedFile(file);
    }

    static public void displayHtmlFile(final Path file,
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
        try {
            final URL url = new URL("https://localhost/" + relativePath.toString().replace(java.io.File.separatorChar, '/'));
            if (isUrlAlive(url)) {
                try {
                    Desktop.getDesktop().browse(url.toURI());
                } catch (final IOException | URISyntaxException e) {
                    ExitHelper.exit(e);
                }
            } else {
                displayVerifiedFile(file);
            }
        } catch (final MalformedURLException e) {
            ExitHelper.exit(e);
        }

    }

    static private void displayVerifiedFile(final Path file) {
        try {
            Desktop.getDesktop().browse(file.toUri());
        } catch (final IOException e) {
            ExitHelper.exit(e);
        }
    }

    static boolean isUrlAlive(final URL url) {
        try {
            final HttpURLConnection huc = (HttpURLConnection)url.openConnection();
            final int responseCode = huc.getResponseCode();
            return (responseCode == 400);
        } catch (final IOException e) {
            return e instanceof SSLHandshakeException;
        }
    }
}
