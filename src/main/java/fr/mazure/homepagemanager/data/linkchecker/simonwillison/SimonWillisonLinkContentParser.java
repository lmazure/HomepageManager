package fr.mazure.homepagemanager.data.linkchecker.simonwillison;

import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import fr.mazure.homepagemanager.data.knowledge.WellKnownAuthors;
import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.ExtractedLinkData;
import fr.mazure.homepagemanager.data.linkchecker.LinkDataExtractor;
import fr.mazure.homepagemanager.data.linkchecker.TextParser;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkFormat;

/**
 * Data extractor for simonwillison.net
 */
public class SimonWillisonLinkContentParser extends LinkDataExtractor {

    private static final TextParser s_titleParser
        = new TextParser("<title>",
                         "</title>",
                         "simonwillison.net",
                         "title");

    private static final TextParser s_dateParser
        = new TextParser("\\Q<meta property=\"og:updated_time\" content=\"\\E",
                         "\\Q\">\\E",
                         "simonwillison.net",
                         "date");

    private final String _data;

    /**
     * @param url URL of the link
     * @param data retrieved link data
     */
    public SimonWillisonLinkContentParser(final String url,
                                          final String data) {
        super(url);
        _data = data;
    }

    /**
     * Determine if the link is managed
     *
     * @param url link
     * @return true if the link is managed
     */
    public static boolean isUrlManaged(final String url) {
        return url.startsWith("https://simonwillison.net/");
    }

    @Override
    public String getTitle() throws ContentParserException {
        return HtmlHelper.cleanContent(s_titleParser.extract(_data));
    }

    @Override
    public Optional<String> getSubtitle() throws ContentParserException {
        return Optional.empty();
    }

    @Override
    public Optional<TemporalAccessor> getDate() throws ContentParserException {
        final int timestamp = Integer.parseInt(s_dateParser.extract(_data));
        final Instant instant = Instant.ofEpochSecond(timestamp);
        return Optional.of(instant.atZone(ZoneId.of("UTC")).toLocalDate());
    }

    @Override
    public List<AuthorData> getSureAuthors() throws ContentParserException {
        return Collections.singletonList(WellKnownAuthors.SIMON_WILLISON);
    }

    @Override
    public List<ExtractedLinkData> getLinks() throws ContentParserException {
        final ExtractedLinkData linkData = new ExtractedLinkData(getTitle(),
                                                                 new String[] { },
                                                                 getUrl(),
                                                                 Optional.empty(),
                                                                 Optional.empty(),
                                                                 new LinkFormat[] { LinkFormat.HTML },
                                                                 new Locale[] { getLanguage() },
                                                                 Optional.empty(),
                                                                 Optional.empty());
        final List<ExtractedLinkData> list = new ArrayList<>(1);
        list.add(linkData);
        return list;
    }

    @Override
    public Locale getLanguage() throws ContentParserException {
        return Locale.ENGLISH;
    }
}
