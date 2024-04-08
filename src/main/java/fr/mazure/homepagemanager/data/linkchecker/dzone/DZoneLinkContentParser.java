package fr.mazure.homepagemanager.data.linkchecker.dzone;

import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.ExtractedLinkData;
import fr.mazure.homepagemanager.data.linkchecker.LinkContentParserUtils;
import fr.mazure.homepagemanager.data.linkchecker.LinkDataExtractor;
import fr.mazure.homepagemanager.data.linkchecker.TextParser;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkFormat;

/**
 * Dada extractor for DZone
 */
public class DZoneLinkContentParser extends LinkDataExtractor {

    private final String _data;

    private static final TextParser s_titleParser
        = new TextParser("<div class=\"title\">\n                        <h1 class=\"article-title\">",
                         "[^>]*",
                         "</h1>",
                         "DZone",
                         "title");

    private static final TextParser s_subtitleParser
        = new TextParser("<div class=\"subhead\">\n                        <h3>",
                         "[^>]*",
                         "</h3>",
                         "DZone",
                         "subtitle");

    private static final TextParser s_dateParser
        = new TextParser("\"datePublished\": \"",
                         "T00:00:00Z\"",
                         "DZone",
                         "date");

    private static final TextParser s_authorParser
        = new TextParser("<span class=\"author-name\">\n        <a href=\"/users/\\d+/\\w+\\.html\" rel=\"nofollow\">",
                         "</a>",
                         "DZone",
                         "author");

    /**
     * @param url URL of the link
     * @param data retrieved link data
     */
    public DZoneLinkContentParser(final String url,
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
        return url.startsWith("https://dzone.com/articles/");
    }

    @Override
    public String getTitle() throws ContentParserException {
        return s_titleParser.extract(_data);
    }

    @Override
    public Optional<String> getSubtitle() throws ContentParserException {
        final String subtitle = s_subtitleParser.extract(_data);
        if (subtitle.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(subtitle);
    }

    /**
     * @return publication date
     * @throws ContentParserException Failure to extract the information
     */
    public LocalDate getPublicationDate() throws ContentParserException {
        return LocalDate.parse(s_dateParser.extract(_data));
    }

    @Override
    public Optional<TemporalAccessor> getDate() throws ContentParserException {
        return Optional.of(getPublicationDate());
    }

    @Override
    public List<AuthorData> getSureAuthors() throws ContentParserException {
        final String author = s_authorParser.extract(_data);
        return Collections.singletonList(LinkContentParserUtils.getAuthor(author));
    }

    @Override
    public List<ExtractedLinkData> getLinks() throws ContentParserException {
        final ExtractedLinkData linkData = new ExtractedLinkData(getTitle(),
                                                                 getSubtitle().isPresent() ? new String[] { getSubtitle().get() }
                                                                                           : new String[] {},
                                                                 getUrl().toString(),
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
