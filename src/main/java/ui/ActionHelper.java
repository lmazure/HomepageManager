package ui;

import java.awt.Desktop;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

import javax.net.ssl.SSLHandshakeException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import utils.ExitHelper;
import utils.StringHelper;

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
        final String urlAsString = "https://localhost/" + relativePath.toString().replace(java.io.File.separatorChar, '/');
        final URL url = StringHelper.convertStringToUrl(urlAsString);
        if (url == null) {
            // this should never happen
            ExitHelper.exit("bad generated URL (" + urlAsString + ")");
        }
        assert url != null;
        if (isUrlAlive(url)) {
            try {
                Desktop.getDesktop().browse(url.toURI());
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
