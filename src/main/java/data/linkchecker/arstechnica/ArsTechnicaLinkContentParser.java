package data.linkchecker.arstechnica;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
import utils.HtmlHelper;
import utils.xmlparsing.AuthorData;
import utils.xmlparsing.LinkFormat;

public class ArsTechnicaLinkContentParser extends LinkDataExtractor {

    private final String _data;

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
        = new TextParser("<p class=\"byline\" itemprop=\"author creator\" itemscope itemtype=\"http://schema.org/Person\">.      <a itemprop=\"url\" href=\"https://arstechnica.com/author/[a-z0-9_-]+/\" rel=\"author\"><span itemprop=\"name\">",
                         "</span></a>",
                         "Ars Technica",
                         "author");

    public ArsTechnicaLinkContentParser(final String url,
                                        final String data) {
        super(url);
        _data = data;
    }

    public String getTitle() throws ContentParserException {
        return HtmlHelper.cleanContent(s_titleParser.extract(_data));
    }

    public String getSubtitle() throws ContentParserException {
        return HtmlHelper.cleanContent(s_subtitleParser.extract(_data));
    }

    private LocalDate getDateInternal() throws ContentParserException {
        final String date = HtmlHelper.cleanContent(s_dateParser.extract(_data));
        final Instant instant = Instant.ofEpochSecond(Long.parseLong(date));
        return LocalDate.ofInstant(instant, ZoneId.of("Europe/Paris"));
    }

    public Optional<AuthorData> getAuthor() throws ContentParserException {
        final String authorName = s_authorParser.extract(_data);
        if (authorName.equals("Ars Staff")) {
            return Optional.empty();
        }
        return Optional.of(LinkContentParserUtils.getAuthor(authorName));
    }

    @Override
    public Optional<TemporalAccessor> getDate() throws ContentParserException {
        return Optional.of(getDateInternal());
    }

    @Override
    public List<AuthorData> getSureAuthors() throws ContentParserException {
        final Optional<AuthorData> authorData = getAuthor();
        final List<AuthorData> list = new ArrayList<>(1);
        if (authorData.isPresent()) {
            list.add(authorData.get());
        }
        return list;
    }

    @Override
    public List<AuthorData> getProbableAuthors() {
        return new ArrayList<>(0);
    }

    @Override
    public List<AuthorData> getPossibleAuthors()  {
        return new ArrayList<>(0);
    }

    @Override
    public List<ExtractedLinkData> getLinks() throws ContentParserException {
        final ExtractedLinkData linkData = new ExtractedLinkData(getTitle(),
                                                                 new String[] { getSubtitle() },
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
    }}
