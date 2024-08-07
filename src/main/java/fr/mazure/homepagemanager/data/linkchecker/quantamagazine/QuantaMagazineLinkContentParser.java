package fr.mazure.homepagemanager.data.linkchecker.quantamagazine;

import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.mazure.homepagemanager.data.knowledge.WellKnownAuthors;
import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.ExtractedLinkData;
import fr.mazure.homepagemanager.data.linkchecker.LinkContentParserUtils;
import fr.mazure.homepagemanager.data.linkchecker.LinkDataExtractor;
import fr.mazure.homepagemanager.data.linkchecker.TextParser;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkFormat;

/**
 * Data extractor for Quanta Magazine articles
 */
public class QuantaMagazineLinkContentParser extends LinkDataExtractor {

    private final String _data;
    private static final TextParser s_titleParser
        = new TextParser("<div class=\"ml025 h3 noe mv0\">",
                         "</div>",
                         "QuantaMagazine",
                         "title");
    private static final TextParser s_subtitleParser
        = new TextParser("<div class=\"post__title__excerpt wysiwyg p italic mb1 mt025 pr2 o4 theme__text[- a-z]*\">",
                         "</div>",
                         "QuantaMagazine",
                         "subtitle");
    private static final TextParser s_dateParser
        = new TextParser(",\"date\":\"",
                         "[^\"]*",
                         "T[0-9][0-9]:[0-9][0-9]:[0-9][0-9]\",\"featured_media_image\":",
                         "QuantaMagazine",
                         "date");
    private static final Pattern s_authorPattern
        = Pattern.compile("<a (class=\"[^\"]+\" )?href=\"/authors/[^/]+/\"><span [^>]+>([^<]+)</span></a>");
    private static final TextParser s_joyOfWhyAuthors1
        = new TextParser("\n<p[^>]*><strong>",
                         "[^<:]*[^: ]",
                         " ?:? ?</strong>",
                         "QuantaMagazine",
                         "The Joy Of Why authors");
    private static final TextParser s_joyOfWhyAuthors2
    = new TextParser("\n<p><b>",
                     "[^<]*",
                     "</b>",
                     "QuantaMagazine",
                     "The Joy Of Why authors");

    /**
     * @param url URL of the link
     * @param data retrieved link data
     */
    public QuantaMagazineLinkContentParser(final String url,
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
        return url.startsWith("https://www.quantamagazine.org/");
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
        return Optional.of(LocalDate.parse(HtmlHelper.cleanContent(s_dateParser.extract(_data))));
    }

    @Override
    public List<AuthorData> getSureAuthors() throws ContentParserException {
        if (_data.contains("<div class=\"mb1 pb025 mt0 h6t post__title__kicker\"><a class=\"kicker theme__accent theme__text-hover uppercase\" href=\"/tag/the-joy-of-why/\">The Joy of Why</a></div>")) {
            return getTheJoyOfWhyAuthors();
        }
        final List<AuthorData> authors = new ArrayList<>();
        final Matcher m = s_authorPattern.matcher(_data);
        while (m.find()) {
            authors.add(LinkContentParserUtils.getAuthor(m.group(2)));
        }
        if (authors.size() == 0) {
            throw new ContentParserException("Failed to find author in QuantaMagazine");
        }
        return authors;
    }

    private List<AuthorData> getTheJoyOfWhyAuthors() throws ContentParserException {

        boolean hostIsStrogatz = false;
        boolean hostIsLevin = false;

        final String cleanedStr =  _data.replaceAll("</strong> ?<strong>", " ");
        List<String> names = s_joyOfWhyAuthors1.extractMulti(cleanedStr);
        if (names.size() == 0) {
            names = s_joyOfWhyAuthors2.extractMulti(_data);
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
            if (name.equals("Announcer") || name.equals("Transcript")) {
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
            if (name.equals("(")) {
                continue;
            }
            uniqueNames.add(name);
        }

        if (uniqueNames.size() == 0) {
            throw new ContentParserException("Failed to find author in QuantaMagazine");
        }

        final List<AuthorData> authors = new ArrayList<>();
        for (final String name : uniqueNames) {
            authors.add(LinkContentParserUtils.getAuthor(name));
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
    public List<ExtractedLinkData> getLinks() throws ContentParserException {
        final ExtractedLinkData linkData = new ExtractedLinkData(getTitle(),
                                                                 new String[] { getSubtitle().get() },
                                                                 getUrl().toString(),
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
