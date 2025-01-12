package fr.mazure.homepagemanager.data.linkchecker.arstechnica;

import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import fr.mazure.homepagemanager.utils.DateTimeHelper;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;
import fr.mazure.homepagemanager.utils.internet.UrlHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkFormat;

/**
 * Data extractor for Ars Technica articles
 */
public class ArsTechnicaLinkContentParser extends LinkDataExtractor {

    private static final String s_sourceName = "Ars Technica";

    private final String _title;
    private final String _subtitle;
    private final TemporalAccessor _creationDate;
    private final List<AuthorData> _sureAuthors;
    private final List<ExtractedLinkData> _links;

    private static final Set<String> parternSites
        = new HashSet<>(Arrays.asList("Ars Staff",
                                      "FT",
                                      "wired.com",
                                      "Financial Times",
                                      "The Conversation",
                                      "Inside Climate News"));
    private static final TextParser s_titleParser
        = new TextParser("<h1(?:\\n *)? class=\"[^\"]+\">",
                         "</h1>",
                         s_sourceName,
                         "title");
    private static final TextParser s_subtitleParser
        = new TextParser("</h1>\\n\\n *<p(?:\\n *)? class=\"[^\"]+\">",
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
     * @throws ContentParserException Failure to extract the information
     */
    public ArsTechnicaLinkContentParser(final String url,
                                        final String data,
                                        final CachedSiteDataRetriever retriever) throws ContentParserException {
        super(UrlHelper.removeQueryParameters(url,"comments",
                                                  "comments-page"),
              retriever);

        _title = HtmlHelper.cleanContent(s_titleParser.extract(data));
        _subtitle = HtmlHelper.cleanContent(s_subtitleParser.extract(data));
        _creationDate = DateTimeHelper.convertISO8601StringToDateTime(s_dateParser.extract(data));

        final List<AuthorData> authorList = new ArrayList<>();
        final String extracted = s_authorParser.extract(data);
        final String[] components = extracted.split("(, and | and |, )");
        for (final String author: components) {
            final String cleanedAuthor = HtmlHelper.cleanContent(author);
            if (parternSites.contains(cleanedAuthor)) {
                continue;
            }
            authorList.add(LinkContentParserUtils.parseAuthorName(cleanedAuthor));
        }
        _sureAuthors = Collections.unmodifiableList(authorList);
        final ExtractedLinkData linkData = new ExtractedLinkData(_title,
                                                                new String[] { _subtitle },
                                                                getUrl(),
                                                                Optional.empty(),
                                                                Optional.empty(),
                                                                new LinkFormat[] { LinkFormat.HTML },
                                                                new Locale[] { getLanguage() },
                                                                Optional.empty(),
                                                                Optional.empty());
        final List<ExtractedLinkData> linkList = new ArrayList<>(1);
        linkList.add(linkData);
        _links = Collections.unmodifiableList(linkList);
    }

    /**
     * Determine if the link is managed
     *
     * @param url link
     * @return true if the link is managed
     */
    public static boolean isUrlManaged(final String url) {
        return UrlHelper.hasPrefix(url, "https://arstechnica.com/");
    }

    @Override
    public String getTitle() {
        return _title;
    }

    @Override
    public Optional<String> getSubtitle() {
        return Optional.of(_subtitle);
    }

    @Override
    public Optional<TemporalAccessor> getCreationDate() {
        return Optional.of(_creationDate);
    }

    @Override
    public Optional<TemporalAccessor> getPublicationDate() {
        return getCreationDate();
    }

    @Override
    public List<AuthorData> getSureAuthors() {
        return _sureAuthors;
    }

    @Override
    public List<AuthorData> getProbableAuthors() {
        return Collections.emptyList();
    }

    @Override
    public List<AuthorData> getPossibleAuthors() {
        return Collections.emptyList();
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
