package data.linkchecker.youtubewatch;

import java.net.URL;
import java.nio.file.Path;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import data.linkchecker.ContentParserException;
import data.linkchecker.ExtractedLinkData;
import utils.xmlparsing.AuthorData;
import utils.xmlparsing.LinkFormat;

public class YoutubeWatchLinkDataExtractor extends data.linkchecker.LinkDataExtractor {

    private final YoutubeWatchLinkContentParser _parser;

    public YoutubeWatchLinkDataExtractor(final URL url,
                                        final Path cacheDirectory) throws ContentParserException {
        super(url, cacheDirectory);
        this._parser = new YoutubeWatchLinkContentParser(getContent());
    }

    @Override
    public Optional<TemporalAccessor> getDate() throws ContentParserException {
        return Optional.of(_parser.getPublishDate());
    }

    @Override
    public List<AuthorData> getAuthors() throws ContentParserException {
        final String channel = _parser.getChannel();
        if (channel.equals("Stand-up Maths")) {
            return buildListFromOneAuthor("Matt", "Parker");
        }
        if (channel.equals("Robert Miles")) {
            return buildListFromOneAuthor("Robert", "Miles");
        }
        if (channel.equals("monsieur bidouille")) {
            return buildListFromOneAuthor("Dimitri", "Ferrière");
        }
        final List<AuthorData> list = new ArrayList<>(0);
        return list;
    }

    @Override
    public List<ExtractedLinkData> getLinks() throws ContentParserException {
        final String channel = _parser.getChannel();
        Locale lang = Locale.ENGLISH;
        if (channel.equals("monsieur bidouille")) {
            lang = Locale.FRENCH;
        }
        final ExtractedLinkData linkData = new ExtractedLinkData(_parser.getTitle(),
                                                                 new String[0],
                                                                 getUrl().toString(),
                                                                 Optional.empty(),
                                                                 Optional.empty(),
                                                                 new LinkFormat[] { LinkFormat.MP4 },
                                                                 new Locale[] { lang },
                                                                 Optional.of(_parser.getMinDuration()),
                                                                 Optional.empty());
        final List<ExtractedLinkData> list = new ArrayList<>(1);
        list.add(linkData);
        return list;
    }
    
    private static List<AuthorData> buildListFromOneAuthor(final String firstName,
                                                           final String lastName) {
        final AuthorData authorData = new AuthorData(Optional.empty(),
                                                     Optional.of(firstName),
                                                     Optional.empty(),
                                                     Optional.of(lastName),
                                                     Optional.empty(),
                                                     Optional.empty());
        final List<AuthorData> list = new ArrayList<>(1);
        list.add(authorData);
        return list;
    }
}