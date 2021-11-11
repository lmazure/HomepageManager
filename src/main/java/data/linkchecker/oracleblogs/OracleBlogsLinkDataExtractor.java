package data.linkchecker.oracleblogs;

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

public class OracleBlogsLinkDataExtractor  extends LinkDataExtractor {

    private final OracleBlogsLinkContentParser _parser;

    public OracleBlogsLinkDataExtractor(final URL url,
                                        final Path cacheDirectory) {
        super(url, cacheDirectory);
        _parser = new OracleBlogsLinkContentParser(getContent(), url);
    }

    @Override
    public Optional<TemporalAccessor> getDate() throws ContentParserException {
        return Optional.of(_parser.getDate());
    }

    @Override
    public List<AuthorData> getAuthors() throws ContentParserException {
        return _parser.getAuthors();
    }

    @Override
    public List<ExtractedLinkData> getLinks() throws ContentParserException {
        final ExtractedLinkData linkData = new ExtractedLinkData(_parser.getTitle(),
                                                                 getSubtitle(),
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
    
    private String[] getSubtitle() throws ContentParserException {
        final Optional<String> subtitle = _parser.getSubtitle();
        if (subtitle.isPresent()) {
            return new String[] { subtitle.get() };
        }
        return new String[] { };
    }
}