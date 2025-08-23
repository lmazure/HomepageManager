package fr.mazure.homepagemanager.data.linkchecker.quantamagazine;

import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever;
import fr.mazure.homepagemanager.data.knowledge.WellKnownAuthors;
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
 * Data extractor for Quanta Magazine articles
 */
public class QuantaMagazineLinkContentParser extends LinkDataExtractor {

    private static final String s_sourceName = "QuantaMagazine";

    private final String _title;
    private final Optional<String> _subtitle;
    private final Optional<TemporalAccessor> _creationDate;
    private final List<AuthorData> _sureAuthors;
    private final List<ExtractedLinkData> _links;

    private static final TextParser s_titleParser
        = new TextParser("<h1 class='post__title__title mv025 noe theme__text' >",
                         "</h1>",
                         s_sourceName,
                         "title");
    private static final TextParser s_subtitleParser
        = new TextParser("<div class='post__title__excerpt [^']+' >\n",
                         "</div>",
                         s_sourceName,
                         "subtitle");
    private static final TextParser s_dateParser
        = new TextParser("<meta property=\"article:published_time\" content=\"",
                         "[^\"]*",
                         "T[0-9][0-9]:[0-9][0-9]:[0-9][0-9]\\+00:00\"",
                         s_sourceName,
                         "date");
    private static final Pattern s_authorPattern1
        = Pattern.compile("<div class='h3t mv05 mbold'>\n                    <span class='screen-reader-text'>By </span>([^<]+)</div>");
    private static final Pattern s_authorPattern2
        = Pattern.compile("<h5 class='sidebar__author__name mb0 mt0'>([^<]+)</h5>");
    private static final TextParser s_joyOfWhyAuthors1
        = new TextParser("\n<p[^>]*><strong>",
                         "[^<:]*[^: ]",
                         " ?:? ?</strong>",
                         s_sourceName,
                         "The Joy Of Why authors");
    private static final TextParser s_joyOfWhyAuthors2
        = new TextParser("\n<p><b>",
                         "[^<]*",
                         "</b>",
                         s_sourceName,
                         "The Joy Of Why authors");

    /**
     * @param url URL of the link
     * @param data retrieved link data
     * @param retriever cache data retriever
     * @throws ContentParserException Failure to extract the information
     */
         public QuantaMagazineLinkContentParser(final String url,
                                                final String data,
                                                final CachedSiteDataRetriever retriever) throws ContentParserException {
        super(url, retriever);

        _title = HtmlHelper.cleanContent(s_titleParser.extract(data));
        _subtitle = Optional.of(HtmlHelper.cleanContent(s_subtitleParser.extract(data)));
        _creationDate = Optional.of(LocalDate.parse(HtmlHelper.cleanContent(s_dateParser.extract(data))));
        _sureAuthors = data.contains("'topic':'The Joy of Why'") ? getTheJoyOfWhyAuthors(data) : extractAuthors(data);
        _links = initializeLinks();
    }

    /**
     * Determine if the link is managed
     *
     * @param url link
     * @return true if the link is managed
     */
    public static boolean isUrlManaged(final String url) {
        return UrlHelper.hasPrefix(url, "https://www.quantamagazine.org/");
    }

    @Override
    public String getTitle() {
        return _title;
    }

    @Override
    public Optional<String> getSubtitle() {
        return _subtitle;
    }

    @Override
    public Optional<TemporalAccessor> getCreationDate() {
        return _creationDate;
    }

    @Override
    public Optional<TemporalAccessor> getPublicationDate() {
        return getCreationDate();
    }

    @Override
    public List<AuthorData> getSureAuthors() {
        return _sureAuthors;
    }

    private static List<AuthorData> extractAuthors(final String data) throws ContentParserException {
        final List<AuthorData> authors = new ArrayList<>();
        final Matcher m1 = s_authorPattern1.matcher(data);
        while (m1.find()) {
            final AuthorData a = LinkContentParserUtils.parseAuthorName(m1.group(1));
            if (!authors.contains(a)) {
                authors.add(a);
            }
        }
        if (authors.isEmpty()) {
            final Matcher m2 = s_authorPattern2.matcher(data);
            while (m2.find()) {
                final AuthorData a = LinkContentParserUtils.parseAuthorName(m2.group(1));
                if (!authors.contains(a)) {
                    authors.add(a);
                }
            }
        }
        return authors;
    }

    private static List<AuthorData> getTheJoyOfWhyAuthors(final String data) throws ContentParserException {
        boolean hostIsStrogatz = false;
        boolean hostIsLevin = false;

        final String cleanedStr = data.replaceAll("</strong> ?<strong>", " ");
        List<String> names = s_joyOfWhyAuthors1.extractMulti(cleanedStr);
        if (names.isEmpty()) {
            names = s_joyOfWhyAuthors2.extractMulti(data);
        }
        final List <String> uniqueNames = new ArrayList<>();
        nameLoop:
        for (final String rawName : names) {
            final String name = toTitleCase(rawName);
            if (uniqueNames.contains(name)) {
                continue;
            }
            for (final String n: uniqueNames) {
                if (n.contains(name)) {
                    continue nameLoop;
                }
            }
            if (name.equals("Announcer") || name.equals("Transcript") || name.equals("(")) {
                continue;
            }
            if (name.toUpperCase().contains("STROGATZ")) {
                hostIsStrogatz = true;
                continue;
            }
            if (name.toUpperCase().contains("LEVIN")) {
                hostIsLevin = true;
                continue;
            }
            uniqueNames.add(name);
        }

        if (uniqueNames.isEmpty()) {
            throw new ContentParserException("Failed to find author in the Joy of Why (QuantaMagazine)");
        }

        final List<AuthorData> authors = new ArrayList<>();
        for (final String name : uniqueNames) {
            authors.add(LinkContentParserUtils.parseAuthorName(name));
        }
        if (hostIsStrogatz) {
            authors.add(WellKnownAuthors.STEVEN_STROGATZ);
        }
        if (hostIsLevin) {
            authors.add(WellKnownAuthors.JANNA_LEVIN);
        }

        return authors;
    }

    @Override
    public List<ExtractedLinkData> getLinks() {
        return _links;
    }

    private List<ExtractedLinkData> initializeLinks() {
        final ExtractedLinkData linkData = new ExtractedLinkData(_title,
                                                                new String[] { _subtitle.get() },
                                                                getUrl(),
                                                                Optional.empty(),
                                                                Optional.empty(),
                                                                new LinkFormat[] { LinkFormat.HTML },
                                                                new Locale[] {getLanguage() },
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

    private static String toTitleCase(String str) {
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;

        for (final char c : str.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
                titleCase.append(c);
            } else if (nextTitleCase) {
                titleCase.append(Character.toTitleCase(c));
                nextTitleCase = false;
            } else {
                titleCase.append(Character.toLowerCase(c));
            }
        }

        return titleCase.toString();
    }
}
