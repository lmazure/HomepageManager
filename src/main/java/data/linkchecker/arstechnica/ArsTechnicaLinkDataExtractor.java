package data.linkchecker.arstechnica;

import java.net.URL;
import java.nio.file.Path;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import data.linkchecker.ContentParserException;
import data.linkchecker.ExtractedLinkData;
import data.linkchecker.LinkDataExtractor;
import utils.xmlparsing.AuthorData;
import utils.xmlparsing.LinkFormat;

public class ArsTechnicaLinkDataExtractor extends LinkDataExtractor {

    private final ArsTechnicaLinkContentParser _parser;

    public ArsTechnicaLinkDataExtractor(final URL url,
                                        final Path cacheDirectory) {
        super(url, cacheDirectory);
        _parser = new ArsTechnicaLinkContentParser(getContent());
    }

    @Override
    public Optional<TemporalAccessor> getDate() throws ContentParserException {
        return Optional.of(_parser.getDate());
    }

    @Override
    public List<AuthorData> getAuthors() throws ContentParserException {
        final Optional<AuthorData> authorData = _parser.getAuthor();
        final List<AuthorData> list = new ArrayList<>(1);
        if (authorData.isPresent()) {
            list.add(authorData.get());
        }
        return list;
    }

    @Override
    public List<ExtractedLinkData> getLinks() throws ContentParserException {
        final ExtractedLinkData linkData = new ExtractedLinkData(_parser.getTitle(),
                new String[] { _parser.getSubtitle() }, getUrl().toString(), Optional.empty(), Optional.empty(),
                new LinkFormat[] { LinkFormat.HTML }, new Locale[] { Locale.ENGLISH }, Optional.empty(),
                Optional.empty());
        final List<ExtractedLinkData> list = new ArrayList<>(1);
        list.add(linkData);
        return list;
    }
}