package ui;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.VBox;

public class XmlGeneratorDialog extends Dialog<String> {
    
    final TextField urlField;
    final TextField xmlField;
    
    public XmlGeneratorDialog() {
        super();
        this.urlField = new TextField();
        this.urlField.setMinWidth(640);
        initializeUrl();
        final Button pasteUrl = new Button("Paste URL");
        pasteUrl.setOnAction(e -> pasteUrl());
        final Button generateXml = new Button("Generate XML");
        generateXml.setOnAction(e -> generateXml());
        this.xmlField = new TextField();
        this.xmlField.setMinWidth(640);
        final Button copyXml = new Button("Copy XML");
        copyXml.setOnAction(e -> copyXml());
        final VBox vbox = new VBox(this.urlField, pasteUrl, generateXml, this.xmlField, copyXml);
        getDialogPane().setContent(vbox);
        getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
    }

    private void generateXml() {
        final String url = this.urlField.getText();
        final String xml = "***" + url + "***";
        this.xmlField.setText(xml);
    }

    private void pasteUrl() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        if (clipboard.hasString()) {
            final String url = clipboard.getString();
            this.urlField.setText(url);
        }
    }

    private void initializeUrl() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        if (clipboard.hasString()) {
            final String url = clipboard.getString();
            if (isValidUrl(url)) {
                this.urlField.setText(url);
            }
        }
    }

    private void copyXml() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(this.xmlField.getText());
        clipboard.setContent(content);
    }

    private static boolean isValidUrl(final String str) {
        return str.startsWith("http");
    }
}
