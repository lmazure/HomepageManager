package ui;

import java.net.URL;
import java.nio.file.Path;

import data.linkchecker.ContentParserException;
import data.linkchecker.LinkDataExtractor;
import data.linkchecker.LinkDataExtractorFactory;
import data.linkchecker.XmlGenerator;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.VBox;
import utils.StringHelper;

public class XmlGeneratorDialog extends Dialog<String> {

    private final TextField _urlField;
    private final TextField _xmlField;
    private final Path _cacheDirectory;

    public XmlGeneratorDialog(final Path cacheDirectory) {
        super();
        _cacheDirectory = cacheDirectory;
        _urlField = new TextField();
        _urlField.setMinWidth(640);
        initializeUrl();
        final Button pasteUrl = new Button("Paste URL");
        pasteUrl.setOnAction(e -> pasteUrl());
        final Button generateXml = new Button("Generate XML");
        generateXml.setOnAction(e -> generateXml());
        _xmlField = new TextField();
        _xmlField.setMinWidth(640);
        final Button copyXml = new Button("Copy XML");
        copyXml.setOnAction(e -> copyXml());
        final VBox vbox = new VBox(_urlField, pasteUrl, generateXml, _xmlField, copyXml);
        getDialogPane().setContent(vbox);
        getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
    }

    private void generateXml() {
        final String txt = _urlField.getText();
        final URL url = StringHelper.convertStringToUrl(txt);
        if (url == null) {
            displayError("Cannot generate XML", "The URL is invalid");
            return;
        }
        LinkDataExtractor extractor;
        try {
            extractor = LinkDataExtractorFactory.build(_cacheDirectory, url);
        } catch (final ContentParserException e) {
            displayError("Cannot generate XML", "Failed to extract data from that URL:\n" + e.getMessage());
            return;
        }
        if (extractor == null) {
            displayError("Cannot generate XML", "Don't know how to extract data from that URL");
            return;
        }

        String xml;
        try {
            xml = XmlGenerator.generateXml(extractor);
        } catch (final ContentParserException e) {
            displayError("Cannot generate XML", "Failer to parse the URL data:\n" + e.getMessage());
            return;
        }
        _xmlField.setText(xml);
    }


    private void pasteUrl() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        if (clipboard.hasString()) {
            final String url = clipboard.getString();
            _urlField.setText(url);
        }
    }

    private void initializeUrl() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        if (clipboard.hasString()) {
            final String url = clipboard.getString();
            if (isValidUrl(url)) {
                _urlField.setText(url);
            }
        }
    }

    private void copyXml() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(_xmlField.getText());
        clipboard.setContent(content);
    }

    private static boolean isValidUrl(final String str) {
        return StringHelper.convertStringToUrl(str) != null;
    }

    private static void displayError(final String header,
                                     final String errorMessage) {
        final Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }
}
