package data.linkchecker.wired;

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

public class WiredLinkDataExtractor extends LinkDataExtractor {

    private final WiredLinkContentParser _parser;

    public WiredLinkDataExtractor(final URL url,
                                  final Path cacheDirectory) {
        super(url, cacheDirectory);
        _parser = new WiredLinkContentParser(getContent());
    }

    @Override
    public Optional<TemporalAccessor> getDate() throws ContentParserException {
        return Optional.of(_parser.getDate());
    }

    @Override
    public List<AuthorData> getSureAuthors() throws ContentParserException {
        return _parser.getAuthors();
    }


    @Override
    public List<AuthorData> getProbableAuthors() {
        return new ArrayList<>(0);
    }

    @Override
    public List<AuthorData> getPossibleAuthors()  {
        return new ArrayList<>(0);
    }

    @Override
    public List<ExtractedLinkData> getLinks() throws ContentParserException {
        final String[] subtitles = _parser.getSubtitle().isPresent() ? new String[]{ _parser.getSubtitle().get() }
                                                                     : new String[0];
        final ExtractedLinkData linkData = new ExtractedLinkData(_parser.getTitle(),
                                                                 subtitles,
                                                                 getUrl().toString(),
                                                                 Optional.empty(),
                                                                 Optional.empty(),
                                                                 new LinkFormat[] { LinkFormat.HTML },
                                                                 new Locale[] { Locale.ENGLISH },
                                                                 Optional.empty(),
                                                                 Optional.empty());
        final List<ExtractedLinkData> list = new ArrayList<>(1);
        list.add(linkData);
        return list;
    }
}
