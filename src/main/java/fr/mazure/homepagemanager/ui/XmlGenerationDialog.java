package fr.mazure.homepagemanager.ui;

import java.nio.file.Path;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.ExtractedLinkData;
import fr.mazure.homepagemanager.data.linkchecker.LinkDataExtractor;
import fr.mazure.homepagemanager.data.linkchecker.LinkDataExtractorFactory;
import fr.mazure.homepagemanager.data.linkchecker.XmlGenerator;
import fr.mazure.homepagemanager.utils.internet.UriHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.VBox;

/**
 * Dialog for generating the XML of a link
 */
public class XmlGenerationDialog extends Dialog<Void> {

    private final TextField _urlField;
    private final ComboBox<String> _quality;
    private final TextArea _xmlField;
    private final Path _cacheDirectory;
    private final VBox _authors;
    private List<ExtractedLinkData> _links;
    private Optional<TemporalAccessor> _date;
    private List<AuthorData> _sureAuthors;
    private List<AuthorData> _probableAuthors;
    private List<AuthorData> _possibleAuthors;

    /**
     * Constructor
     * @param cacheDirectory directory where the cache files are written
     */
    public XmlGenerationDialog(final Path cacheDirectory) {
        _cacheDirectory = cacheDirectory;

        setTitle("XML Generation");

        _urlField = new TextField();
        _urlField.setMinWidth(640);

        final Button pasteUrl = new Button("Paste URL");
        pasteUrl.setOnAction(e -> pasteUrl());

        _authors = new VBox();

        final String[] qualities = { "very good", "good", "average", "bad", "very bad" };
        _quality = new ComboBox<>(FXCollections.observableArrayList(qualities));
        _quality.getSelectionModel().select(2);
        _quality.valueProperty().addListener((final ObservableValue<? extends String> observable, final String oldValue, final String newValue) -> generateXml());

        _xmlField = new TextArea();
        _xmlField.setMinWidth(640);
        _xmlField.setPrefRowCount(6);
        _xmlField.setWrapText(true);

        final Button copyXml = new Button("Copy XML");
        copyXml.setOnAction(e -> copyXml());

        final VBox vbox = new VBox(_urlField, pasteUrl, _authors, _quality, _xmlField, copyXml);
        getDialogPane().setContent(vbox);
        getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        initializeUrl();
    }

    private void generateXml() {
        if ((_links == null) || (_date == null) || (_sureAuthors == null) || (_probableAuthors == null) || (_possibleAuthors == null)) {
            return;
        }

        final List<AuthorData> authors = new ArrayList<>(_sureAuthors);

        int i = 0;
        for (final AuthorData author: _probableAuthors) {
            if (((CheckBox)_authors.getChildren().get(i++)).isSelected()) {
                authors.add(author);
            }
        }
        for (final AuthorData author: _possibleAuthors) {
            if (((CheckBox)_authors.getChildren().get(i++)).isSelected()) {
                authors.add(author);
            }
        }

        final int quality = 2 - _quality.getSelectionModel().getSelectedIndex();
        final String xml = XmlGenerator.generateXml(_links, _date, authors, quality);
        _xmlField.setText(xml);
        _xmlField.setStyle("-fx-text-fill: darkGreen;");
    }

    private void pasteUrl() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        if (!clipboard.hasString()) {
            return;
        }

        _links = null;
        _date = null;
        _sureAuthors = null;
        _probableAuthors = null;
        _possibleAuthors = null;

        final String url = clipboard.getString();
        _urlField.setText(url);
        _xmlField.clear();
        _authors.getChildren().clear();

        LinkDataExtractor extractor;
        try {
            extractor = LinkDataExtractorFactory.build(_cacheDirectory, url);
        } catch (final ContentParserException e) {
            displayError("Failed to extract data from that URL:\n" + e.getMessage());
            return;
        }
        if (extractor == null) {
            displayError("Don't know how to extract data from that URL");
            return;
        }

        try {
            _links = extractor.getLinks();
            _date = extractor.getDate();
            _sureAuthors= extractor.getSureAuthors();
            _probableAuthors = extractor.getProbableAuthors();
            _possibleAuthors = extractor.getPossibleAuthors();
        } catch (final ContentParserException e) {
            displayError("Failed to parse the URL data:\n" + e.getMessage());
            return;
        }

        for (final AuthorData author: _probableAuthors) {
            final CheckBox cb = new CheckBox(authorAsString(author));
            cb.setSelected(true);
            cb.selectedProperty().addListener((final ObservableValue<? extends Boolean> observable, final Boolean oldValue, final Boolean newValue) -> generateXml());
            _authors.getChildren().add(cb);
        }

        for (final AuthorData author: _possibleAuthors) {
            final CheckBox cb = new CheckBox(authorAsString(author));
            cb.setSelected(false);
            cb.selectedProperty().addListener((final ObservableValue<? extends Boolean> observable, final Boolean oldValue, final Boolean newValue) -> generateXml());
            _authors.getChildren().add(cb);
        }

        generateXml();
    }

    private void initializeUrl() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        if (clipboard.hasString()) {
            final String url = clipboard.getString();
            if (UriHelper.isValidUri(url)) {
                pasteUrl();
            }
        }
    }

    private void copyXml() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(_xmlField.getText());
        clipboard.setContent(content);
    }

    private static String authorAsString(final AuthorData author) {
        return author.getNamePrefix().orElse(" ") +
               author.getFirstName().orElse(" ") +
               author.getMiddleName().orElse(" ") +
               author.getLastName().orElse(" ") +
               author.getNameSuffix().orElse(" ") +
               author.getGivenName().orElse(" ");
    }

    private void displayError(final String errorMessage) {
        _xmlField.setText(errorMessage);
        _xmlField.setStyle("-fx-text-fill: red;");
    }
}
