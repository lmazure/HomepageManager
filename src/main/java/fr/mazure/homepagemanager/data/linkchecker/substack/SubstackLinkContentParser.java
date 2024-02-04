package fr.mazure.homepagemanager.data.linkchecker.substack;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.ExtractedLinkData;
import fr.mazure.homepagemanager.data.linkchecker.LinkContentParserUtils;
import fr.mazure.homepagemanager.data.linkchecker.LinkDataExtractor;
import fr.mazure.homepagemanager.data.linkchecker.TextParser;
import fr.mazure.homepagemanager.utils.StringHelper;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkFormat;

/**
 * Data extractor for Substack
 */
public class SubstackLinkContentParser extends LinkDataExtractor {


    private final String _data;

    private static final TextParser s_titleParser
    = new TextParser("<h1 class=\"post-title unpublished\">",
                     "</h1>",
                     "Substack",
                     "title");
private static final TextParser s_subtitleParser
    = new TextParser("<h3 class=\"subtitle\">",
                     "</h3>",
                     "Substack",
                     "subtitle");
private static final TextParser s_dateParser
    = new TextParser(",\"datePublished\":\"",
                     "\"",
                     "Substack",
                     "date");
private static final TextParser s_authorParser
    = new TextParser("<script type=\"application/ld\\+json\">",
                     "</script>",
                     "Substack",
                     "author");

    private static final Pattern s_mediumUrl = Pattern.compile("https://[^/]+\\.substack\\.com/.+");

    /**
     * @param url URL of the link
     * @param data retrieved link data
     */
    public SubstackLinkContentParser(final String url,
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
        if (url.startsWith("https://magazine.sebastianraschka.com/")) {
            return true;
        }
        return s_mediumUrl.matcher(url).matches();
    }

    @Override
    public String getTitle() throws ContentParserException {
        return HtmlHelper.cleanContent(s_titleParser.extract(_data));
    }

    @Override
    public Optional<String> getSubtitle() throws ContentParserException {
        final Optional<String> subtitle = s_subtitleParser.extractOptional(_data);
        if (subtitle.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(HtmlHelper.cleanContent(s_subtitleParser.extract(_data)));
    }

    @Override
    public Optional<TemporalAccessor> getDate() throws ContentParserException {
        final String date = HtmlHelper.cleanContent(s_dateParser.extract(_data));
        return Optional.of(LocalDateTime.parse(date, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toLocalDate());
    }

    @Override
    public List<AuthorData> getSureAuthors() throws ContentParserException {
        final List<AuthorData> list = new ArrayList<>(1);
        final String extracted = s_authorParser.extract(_data);
        final JSONObject payload = new JSONObject(extracted);
        String channelName = null;
        try {
            final Object authorNode = payload.get("author");
            if (authorNode instanceof JSONArray) {
                channelName = ((JSONArray)authorNode).getJSONObject(0).getString("name");
            } else if (authorNode instanceof JSONObject) {
                channelName = ((JSONObject)authorNode).getString("name");
            } else {
                throw new ContentParserException("Error while parsing JSON, author node is of type " + authorNode.getClass().getName());
            }
        } catch (final JSONException e) {
            throw new ContentParserException("Error while parsing JSON", e);
        }

        final AuthorData author = getWellKnownAuthor(channelName);
        if (author != null) {
            list.add(author);
            return list;
        }

        final String[] components = channelName.split("(, and | and |, )");
        for (final String component: components) {
            list.add(LinkContentParserUtils.getAuthor(component));
        }
        return list;
    }

    static private AuthorData getWellKnownAuthor(final String authorName) {
        return switch (authorName) {
            case "Sebastian Raschka, PhD",
                 "Ahead of AI"             -> new AuthorData(Optional.empty(),
                                                            Optional.of("Sebastian"),
                                                            Optional.empty(),
                                                            Optional.of("Raschka"),
                                                            Optional.empty(),
                                                            Optional.empty());
            case "Science étonnante" -> new AuthorData(Optional.empty(),
                                                       Optional.of("David"),
                                                       Optional.empty(),
                                                       Optional.of("Louapre"),
                                                       Optional.empty(),
                                                       Optional.empty());
             default -> null;
        };
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
    public Locale getLanguage() throws ContentParserException {
        return StringHelper.guessLanguage(HtmlHelper.cleanContent(_data)).get();
    }
}
