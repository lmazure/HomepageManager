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
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.VBox;

/**
 * Dialog for generating the XML of a link
 */
public class XmlGenerationDialog extends Dialog<Void> {

    private final TextField _url;
    private final ComboBox<String> _quality;
    private final TextArea _comment;
    private final TextArea _xml;
    private final Path _cacheDirectory;
    private final VBox _authors;
    private List<ExtractedLinkData> _links;
    private Optional<TemporalAccessor> _date;
    private List<AuthorData> _sureAuthors;
    private List<AuthorData> _probableAuthors;
    private List<AuthorData> _possibleAuthors;

    /**
     * Constructor
     *
     * @param cacheDirectory directory where the cache files are written
     */
    public XmlGenerationDialog(final Path cacheDirectory) {
        _cacheDirectory = cacheDirectory;

        setTitle("XML Generation");

        _url = new TextField();
        _url.setMinWidth(640);

        final Button pasteUrl = new Button("Paste URL");
        pasteUrl.setOnAction(e -> pasteUrl());

        final Separator separator1 = new Separator();

        final Label authorsLabel = new Label("Authors");

        _authors = new VBox();

        final Label qualityLabel = new Label("Quality");

        final String[] qualities = { "very good", "good", "average", "bad", "very bad" };
        _quality = new ComboBox<>(FXCollections.observableArrayList(qualities));
        _quality.getSelectionModel().select(2);
        _quality.valueProperty().addListener((final ObservableValue<? extends String> observable, final String oldValue, final String newValue) -> generateXml());

        final Label commentLabel = new Label("Comment");

        _comment = new TextArea();
        _comment.setMinWidth(640);
        _comment.setPrefRowCount(3);
        _comment.setWrapText(true);
        _comment.textProperty().addListener((final ObservableValue<? extends String> observable, final String oldValue, final String newValue) -> generateXml());

        final Separator separator2 = new Separator();

        _xml = new TextArea();
        _xml.setMinWidth(640);
        _xml.setPrefRowCount(6);
        _xml.setWrapText(true);

        final Button copyXml = new Button("Copy XML");
        copyXml.setOnAction(e -> copyXml());

        final VBox vbox = new VBox(_url, pasteUrl, separator1, authorsLabel, _authors, qualityLabel, _quality, commentLabel, _comment, separator2, _xml, copyXml);
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

        final String comment = _comment.getText();

        final int quality = 2 - _quality.getSelectionModel().getSelectedIndex();
        final String xml = XmlGenerator.generateXml(_links, _date, authors, quality, comment);
        _xml.setText(xml);
        _xml.setStyle("-fx-text-fill: darkGreen;");
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
        _url.setText(url);
        _xml.clear();
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
            _sureAuthors = extractor.getSureAuthors();
            _probableAuthors = extractor.getProbableAuthors();
            _possibleAuthors = extractor.getPossibleAuthors();
        } catch (final ContentParserException e) {
            displayError("Failed to parse the URL data:\n" + e.getMessage());
            return;
        }

        if ((_sureAuthors.size() == 0) && (_probableAuthors.size() == 0) && (_possibleAuthors.size() == 0)) {
            _authors.getChildren().add(new Label("No authors"));
        } else {
            for (final AuthorData author: _sureAuthors) {
                _authors.getChildren().add(new Label(authorAsString(author)));
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
        content.putString(_xml.getText());
        clipboard.setContent(content);
    }

    private static String authorAsString(final AuthorData author) {
        final StringBuilder builder = new StringBuilder();
        if (author.getNamePrefix().isPresent()) {
            builder.append(author.getNamePrefix().get());
        }
        if (author.getFirstName().isPresent()) {
            if (builder.length() > 0) {
                builder.append(' ');
            }
            builder.append(author.getFirstName().get());
        }
        if (author.getMiddleName().isPresent()) {
            if (builder.length() > 0) {
                builder.append(' ');
            }
            builder.append(author.getMiddleName().get());
        }
        if (author.getLastName().isPresent()) {
            if (builder.length() > 0) {
                builder.append(' ');
            }
            builder.append(author.getLastName().get());
        }
        if (author.getNameSuffix().isPresent()) {
            if (builder.length() > 0) {
                builder.append(' ');
            }
            builder.append(author.getNameSuffix().get());
        }
        if (author.getGivenName().isPresent()) {
            if (builder.length() > 0) {
                builder.append(' ');
            }
            builder.append('"');
            builder.append(author.getGivenName().get());
            builder.append('"');
        }
        return builder.toString();
    }

    private void displayError(final String errorMessage) {
        _xml.setText(errorMessage);
        _xml.setStyle("-fx-text-fill: red;");
    }
}
