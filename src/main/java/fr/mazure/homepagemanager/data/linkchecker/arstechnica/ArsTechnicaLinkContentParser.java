package fr.mazure.homepagemanager.data.linkchecker.arstechnica;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

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
*  * Data extractor for Ars Technica articles
*/
public class ArsTechnicaLinkContentParser extends LinkDataExtractor {

    private final String _data;

    private static final Set<String> parternSites
        = new HashSet<>(Arrays.asList("Ars Staff",
                                      "FT",
                                      "wired.com",
                                      "Financial Times",
                                      "The Conversation",
                                      "Inside Climate News"));
    private static final TextParser s_titleParser
        = new TextParser("<h1 itemprop=\"headline\">",
                         "</h1>",
                         "Ars Technica",
                         "title");
    private static final TextParser s_subtitleParser
        = new TextParser("<h2 itemprop=\"description\">",
                         "</h2>",
                         "Ars Technica",
                         "subtitle");
    private static final TextParser s_dateParser
        = new TextParser("<time class=\"date\" data-time=\"",
                         "\" datetime=\".*?\">",
                         "Ars Technica",
                         "date");
    private static final TextParser s_authorParser
        = new TextParser("<p class=\"byline\" itemprop=\"author creator\" itemscope itemtype=\"http://schema.org/Person\">.      <a itemprop=\"url\" href=\"https://arstechnica.com/author/[a-z0-9_-]+/\"  rel=\"author\" ><span itemprop=\"name\">",
                         "</span></a>",
                         "Ars Technica",
                         "author");

    /**
     * @param url URL of the link
     * @param data retrieved link data
     */
    public ArsTechnicaLinkContentParser(final String url,
                                        final String data) {
        super(UrlHelper.removeQueryParameters(url,"comments",
                                                  "comments-page"));
        _data = data;
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
        final String date = HtmlHelper.cleanContent(s_dateParser.extract(_data));
        final Instant instant = Instant.ofEpochSecond(Long.parseLong(date));
        return Optional.of(LocalDate.ofInstant(instant, ZoneId.of("Europe/Paris")));
    }

    @Override
    public List<AuthorData> getSureAuthors() throws ContentParserException {
        final List<AuthorData> list = new ArrayList<>(1);
        final String extracted = s_authorParser.extract(_data);
        final String[] components = extracted.split("(, and | and |, )");
        for (final String author: components) {
            if (parternSites.contains(author)) {
                continue;
            }
            list.add(LinkContentParserUtils.getAuthor(author));
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
    public Locale getLanguage() {
        return Locale.ENGLISH;
    }
}
