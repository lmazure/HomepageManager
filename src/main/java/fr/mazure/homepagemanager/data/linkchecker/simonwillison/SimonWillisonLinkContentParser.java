package fr.mazure.homepagemanager.data.linkchecker.simonwillison;

import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever;
import fr.mazure.homepagemanager.data.knowledge.WellKnownAuthors;
import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.ExtractedLinkData;
import fr.mazure.homepagemanager.data.linkchecker.LinkDataExtractor;
import fr.mazure.homepagemanager.data.linkchecker.TextParser;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;
import fr.mazure.homepagemanager.utils.internet.UrlHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkFormat;

/**
 * Data extractor for simonwillison.net
 */
public class SimonWillisonLinkContentParser extends LinkDataExtractor {

    private static final String s_sourceName = "simonwillison.net";

    private static final TextParser s_titleParser
        = new TextParser("<title>",
                         "</title>",
                         s_sourceName,
                         "title");

    private static final TextParser s_dateParser
        = new TextParser("\\Q<meta property=\"og:updated_time\" content=\"\\E",
                         "\\Q\">\\E",
                         s_sourceName,
                         "date");

    private final String _title;
    private final TemporalAccessor _creationDate;
    private final List<AuthorData> _authors;
    private final List<ExtractedLinkData> _links;

    /**
     * @param url URL of the link
     * @param data retrieved link data
     * @param retriever cache data retriever
     * @throws ContentParserException Failure to extract the information
     */
    public SimonWillisonLinkContentParser(final String url,
                                          final String data,
                                          final CachedSiteDataRetriever retriever) throws ContentParserException {
        super(url, retriever);

        _title = HtmlHelper.cleanContent(s_titleParser.extract(data));

        final int timestamp = Integer.parseInt(s_dateParser.extract(data));
        final Instant instant = Instant.ofEpochSecond(timestamp);
        _creationDate = instant.atZone(ZoneId.of("UTC")).toLocalDate();

        _authors = Collections.singletonList(WellKnownAuthors.SIMON_WILLISON);

        final ExtractedLinkData linkData = new ExtractedLinkData(_title,
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
        _links = list;
    }

    /**
     * Determine if the link is managed
     *
     * @param url link
     * @return true if the link is managed
     */
    public static boolean isUrlManaged(final String url) {
        return UrlHelper.hasPrefix(url, "https://simonwillison.net/") &&
               !UrlHelper.hasPrefix(url, "https://simonwillison.net/guides/");
    }

    @Override
    public String getTitle() {
        return _title;
    }

    @Override
    public Optional<String> getSubtitle() {
        return Optional.empty();
    }

    @Override
    public Optional<TemporalAccessor> getCreationDate() {
        return Optional.of(_creationDate);
    }

    @Override
    public Optional<TemporalAccessor> getPublicationDate() {
        return Optional.of(_creationDate);
    }

    @Override
    public List<AuthorData> getSureAuthors() {
        return _authors;
    }

    @Override
    public List<ExtractedLinkData> getLinks() {
        return _links;
    }

    @Override
    public Locale getLanguage() {
        return Locale.ENGLISH;
    }
}
