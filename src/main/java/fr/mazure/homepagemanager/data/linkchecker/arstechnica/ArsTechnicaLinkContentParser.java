package fr.mazure.homepagemanager.data.linkchecker.arstechnica;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever;
import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.ExtractedLinkData;
import fr.mazure.homepagemanager.data.linkchecker.LinkContentParserUtils;
import fr.mazure.homepagemanager.data.linkchecker.LinkDataExtractor;
import fr.mazure.homepagemanager.data.linkchecker.TextParser;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;
import fr.mazure.homepagemanager.utils.internet.UrlHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkFormat;

/**
 * Data extractor for Ars Technica articles
 */
public class ArsTechnicaLinkContentParser extends LinkDataExtractor {

    private static final String s_sourceName = "Ars Technica";

    private final String _data;

    private static final Set<String> parternSites
        = new HashSet<>(Arrays.asList("Ars Staff",
                                      "FT",
                                      "wired.com",
                                      "Financial Times",
                                      "The Conversation",
                                      "Inside Climate News"));
    private static final TextParser s_titleParser
        = new TextParser("<h1 class=\"[^\"]+\">",
                         "</h1>",
                         s_sourceName,
                         "title");
    private static final TextParser s_subtitleParser
        = new TextParser("</h1>\\n\\n *<p class=\"[^\"]+\">",
                         "</p>",
                         s_sourceName,
                         "subtitle");
    private static final TextParser s_dateParser
        = new TextParser("<meta property=\"article:published_time\" content=\"",
                         "\" />",
                         s_sourceName,
                         "date");
    private static final TextParser s_authorParser
        = new TextParser("<a class=\"text-orange-400 hover:text-orange-500\" href=\"https://arstechnica.com/author/[-_a-z0-9]+/\">",
                         "</a>",
                         s_sourceName,
                         "author");

    /**
     * @param url URL of the link
     * @param data retrieved link data
     * @param retriever cache data retriever
     */
    public ArsTechnicaLinkContentParser(final String url,
                                        final String data,
                                        final CachedSiteDataRetriever retriever) {
        super(UrlHelper.removeQueryParameters(url,"comments",
                                                  "comments-page"),
              retriever);
        _data = data;
    }

    /**
     * Determine if the link is managed
     *
     * @param url link
     * @return true if the link is managed
     */
    public static boolean isUrlManaged(final String url) {
        return url.startsWith("https://arstechnica.com/");
    }

    @Override
    public String getTitle() throws ContentParserException {
        return HtmlHelper.cleanContent(s_titleParser.extract(_data));
    }

    @Override
    public Optional<String> getSubtitle() throws ContentParserException {
        return Optional.of(HtmlHelper.cleanContent(s_subtitleParser.extract(_data)));
    }

    @Override
    public Optional<TemporalAccessor> getDate() throws ContentParserException {
        final ZonedDateTime inputDateTime = ZonedDateTime.parse(s_dateParser.extract(_data), DateTimeFormatter.ISO_DATE_TIME);
        final ZoneId franceZoneId = ZoneId.of("Europe/Paris");
        return Optional.of(inputDateTime.withZoneSameInstant(franceZoneId).toLocalDate());
    }

    @Override
    public List<AuthorData> getSureAuthors() throws ContentParserException {
        final List<AuthorData> list = new ArrayList<>(1);
        final String extracted = s_authorParser.extract(_data);
        final String[] components = extracted.split("(, and | and |, )");
        for (final String author: components) {
            final String cleanedAuthor = HtmlHelper.cleanContent(author);
            if (parternSites.contains(cleanedAuthor)) {
                continue;
            }
            list.add(LinkContentParserUtils.parseAuthorName(cleanedAuthor));
        }
        return list;
    }

    @Override
    public List<AuthorData> getProbableAuthors() {
        return new ArrayList<>(0);
    }

    @Override
    public List<AuthorData> getPossibleAuthors() {
        return new ArrayList<>(0);
    }

    @Override
    public List<ExtractedLinkData> getLinks() throws ContentParserException {
        final ExtractedLinkData linkData = new ExtractedLinkData(getTitle(),
                                                                 new String[] { getSubtitle().get() },
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
    public Locale getLanguage() {
        return Locale.ENGLISH;
    }
}
