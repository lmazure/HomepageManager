package fr.mazure.homepagemanager.data.linkchecker.spectrum;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

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
 * Data extractor for IEEE Spectrum articles
 */
public class SpectrumLinkContentParser extends LinkDataExtractor {

    private final String _data;

    private List<AuthorData> _authors;

    private static final TextParser s_titleParser
        = new TextParser("<h1 class=\"widget__headline h1\">",
                         "</h1>",
                         "IEEE Spectrum",
                         "title");
    private static final TextParser s_subtitleParser
        = new TextParser("<h2 class=\"widget__subheadline-text h2\" data-type=\"text\">",
                         "</h2>",
                         "IEEE Spectrum",
                         "subtitle");
    private static final TextParser s_dateParser
        = new TextParser("\"datePublished\":\"",
                         "\"",
                         "IEEE Spectrum",
                         "date");
    private static final TextParser s_authorParser
        = new TextParser("<a class=\"social-author__name\" [^>]+>",
                         "</a>",
                         "IEEE Spectrum",
                         "author");

    /**
     * @param url URL of the link
     * @param data retrieved link data
     */
    public SpectrumLinkContentParser(final String url,
                                     final String data) {
        super(UrlHelper.removeQueryParameters(url,"comments",
                                                  "comments-page"));
        _data = data;
    }

    /**
     * Determine if the link is managed
     *
     * @param url link
     * @return true if the link is managed
     */
    public static boolean isUrlManaged(final String url) {
        return url.startsWith("https://spectrum.ieee.org/");
    }    @Override

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
        final Instant instant = Instant.parse(date);
        return Optional.of(LocalDate.ofInstant(instant, ZoneId.of("Europe/Paris")));
    }

    @Override
    public List<AuthorData> getSureAuthors() throws ContentParserException {
        if (_authors != null) {
            return _authors;
        }
        _authors = new ArrayList<>(1);
        final List<String> extracted = s_authorParser.extractMulti(_data);
        for (final String extract: extracted) {
            final AuthorData author = LinkContentParserUtils.getAuthor(extract);
            if (!_authors.contains(author)) {
                _authors.add(author);
            }
        }
        return _authors;
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
