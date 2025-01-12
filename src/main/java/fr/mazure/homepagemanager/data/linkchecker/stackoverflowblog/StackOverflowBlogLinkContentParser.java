package fr.mazure.homepagemanager.data.linkchecker.stackoverflowblog;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

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
 * Data extractor for StackOverflow blog
 */
public class StackOverflowBlogLinkContentParser extends LinkDataExtractor {

    private static final String s_sourceName = "StackOverflow blog";

    private final String _title;
    private final String _subtitle;
    private final Optional<TemporalAccessor> _creationDate;
    private final List<AuthorData> _authors;
    private final List<ExtractedLinkData> _links;

    private static final TextParser s_titleParser
        = new TextParser("<h1 class=\"fs-display2 lh-xs p-ff-roboto-slab-bold mb24\" itemprop=\"name\">",
                         "</h1>",
                         s_sourceName,
                         "title");
    private static final TextParser s_subtitleParser
        = new TextParser("<p class=\"fs-title fc-black-500 wmx6\" itemprop=\"abstract\">",
                         "</p>",
                         s_sourceName,
                         "subtitle");
    private static final TextParser s_dateParser
        = new TextParser("<header class=\"mb32 pt12\"><time datetime=\"",
                         "\"",
                         s_sourceName,
                         "date");
    private static final TextParser s_authorParser
        = new TextParser("<div class=\"fw-bold fs-body3\" itemprop=\"author\">",
                         "</div>",
                         s_sourceName,
                         "author");

    /**
     * Constructor
     * @param url URL of the link
     * @param data retrieved link data
     * @param retriever cache data retriever
     * @throws ContentParserException Failure to extract the information
     */
    public StackOverflowBlogLinkContentParser(final String url,
                                              final String data,
                                              final CachedSiteDataRetriever retriever) throws ContentParserException {
        super(url, retriever);

        _title = HtmlHelper.cleanContent(s_titleParser.extract(data));
        _subtitle = HtmlHelper.cleanContent(s_subtitleParser.extract(data));

        final String extractedDate = HtmlHelper.cleanContent(s_dateParser.extract(data));
        final LocalDate date = ZonedDateTime.parse(extractedDate, DateTimeFormatter.ISO_DATE_TIME).toLocalDate();
        _creationDate = Optional.of(date);

        final String extracted = s_authorParser.extract(data);
        final List<AuthorData> authorList = new ArrayList<>(1);
        authorList.add(LinkContentParserUtils.parseAuthorName(HtmlHelper.cleanContent(extracted)));
        _authors = authorList;

        final ExtractedLinkData linkData = new ExtractedLinkData(_title,
                                                                 new String[] { _subtitle },
                                                                 url,
                                                                 Optional.empty(),
                                                                 Optional.empty(),
                                                                 new LinkFormat[] { LinkFormat.HTML },
                                                                 new Locale[] { getLanguage() },
                                                                 Optional.empty(),
                                                                 Optional.empty());
        final List<ExtractedLinkData> linkList = new ArrayList<>(1);
        linkList.add(linkData);
        _links = linkList;
    }

    /**
     * Determine if the link is managed
     *
     * @param url link
     * @return true if the link is managed
     */
    public static boolean isUrlManaged(final String url) {
        return UrlHelper.hasPrefix(url, "https://stackoverflow.blog/");
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
        return _creationDate;
    }

    @Override
    public Optional<TemporalAccessor> getPublicationDate() {
        return _creationDate;
    }

    @Override
    public List<AuthorData> getSureAuthors() {
        return _authors;
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
