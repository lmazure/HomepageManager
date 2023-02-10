package data.linkchecker.stackoverflowblog;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import data.linkchecker.ContentParserException;
import data.linkchecker.ExtractedLinkData;
import data.linkchecker.LinkContentParserUtils;
import data.linkchecker.LinkDataExtractor;
import data.linkchecker.TextParser;
import utils.internet.HtmlHelper;
import utils.xmlparsing.AuthorData;
import utils.xmlparsing.LinkFormat;

/**
 * Data extractor for StackOverflow blog
 */
public class StackOverflowBlogContentParser extends LinkDataExtractor {

    private final String _data;

    private static final TextParser s_titleParser
        = new TextParser("<meta property=\"og:title\" content=\"",
                         "\" />",
                         "StackOverflow blog",
                         "title");
    private static final TextParser s_subtitleParser
        = new TextParser("<meta property=\"og:description\" content=\"",
                         "\" />",
                         "StackOverflow blog",
                         "subtitle");
    private static final TextParser s_dateParser
        = new TextParser("<meta property=\"article:published_time\" content=\"",
                         "\" />",
                         "StackOverflow blog",
                         "date");
    private static final TextParser s_authorParser
        = new TextParser("<a href=\"https://stackoverflow.blog/author/[^/]+/\" title=\"Posts by [^\"]+\" class=\"author url fn\" rel=\"author\">",
                         "</a>",
                         "StackOverflow blog",
                         "author");

    /**
     * Constructor
     * @param url URL of the link
     * @param data retrieved link data
     */
    public StackOverflowBlogContentParser(final String url,
                                          final String data) {
        super(url);
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
        final String extractedDate = HtmlHelper.cleanContent(s_dateParser.extract(_data));
        final LocalDate date = ZonedDateTime.parse(extractedDate, DateTimeFormatter.ISO_DATE_TIME).toLocalDate();
        return Optional.of(date);
    }

    @Override
    public List<AuthorData> getSureAuthors() throws ContentParserException {
        final List<AuthorData> list = new ArrayList<>(1);
        final String extracted = s_authorParser.extract(_data);
        list.add(LinkContentParserUtils.getAuthor(extracted));
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
