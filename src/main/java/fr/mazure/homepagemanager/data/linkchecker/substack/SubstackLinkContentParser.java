package fr.mazure.homepagemanager.data.linkchecker.substack;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever;
import fr.mazure.homepagemanager.data.knowledge.WellKnownAuthors;
import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.ExtractedLinkData;
import fr.mazure.homepagemanager.data.linkchecker.LinkContentParserUtils;
import fr.mazure.homepagemanager.data.linkchecker.LinkDataExtractor;
import fr.mazure.homepagemanager.data.linkchecker.TextParser;
import fr.mazure.homepagemanager.utils.StringHelper;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;
import fr.mazure.homepagemanager.utils.internet.UrlHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkFormat;

/**
 * Data extractor for Substack
 */
public class SubstackLinkContentParser extends LinkDataExtractor {

    private static final String s_sourceName = "Substack";

    private final String _title;
    private final Optional<String> _subtitle;
    private final Optional<TemporalAccessor> _date;
    private final List<AuthorData> _sureAuthors;
    private final Locale _language;

    private static final TextParser s_titleParser
        = new TextParser("<h1 class=\"post-title published\">",
                         "</h1>",
                         s_sourceName,
                         "title");
    private static final TextParser s_subtitleParser
        = new TextParser("<h3 class=\"subtitle\">",
                         "</h3>",
                         s_sourceName,
                         "subtitle");
    private static final TextParser s_dateParser
        = new TextParser(",\"datePublished\":\"",
                         "\"",
                         s_sourceName,
                         "date");
    private static final TextParser s_authorParser
        = new TextParser("<script type=\"application/ld\\+json\">",
                         "</script>",
                         s_sourceName,
                         "author");

    private static final Pattern s_mediumUrl = Pattern.compile("https://[^/]+\\.substack\\.com/.+");

    /**
     * @param url URL of the link
     * @param data retrieved link data
     * @param retriever cache data retriever
     *
     * @throws ContentParserException Failure to extract the information
     */
    public SubstackLinkContentParser(final String url,
                                     final String data,
                                     final CachedSiteDataRetriever retriever) throws ContentParserException {
        super(url, retriever);

        _title = HtmlHelper.cleanContent(s_titleParser.extract(data));

        final Optional<String> subtitleRaw = s_subtitleParser.extractOptional(data);
        _subtitle = subtitleRaw.isEmpty() ? Optional.empty()
                                          : Optional.of(HtmlHelper.cleanContent(s_subtitleParser.extract(data)));

        final String date = HtmlHelper.cleanContent(s_dateParser.extract(data));
        _date = Optional.of(LocalDateTime.parse(date, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toLocalDate());

        final String extracted = s_authorParser.extract(data);
        final JSONObject payload = new JSONObject(extracted);
        _sureAuthors = extractAuthors(payload);

        _language = StringHelper.guessLanguage(HtmlHelper.cleanContent(data)).get();
    }

    /**
     * Determine if the link is managed
     *
     * @param url link
     * @return true if the link is managed
     */
    public static boolean isUrlManaged(final String url) {
        if (UrlHelper.hasPrefix(url, "https://magazine.sebastianraschka.com/") ||
            UrlHelper.hasPrefix(url, "https://www.thecoder.cafe/") ||
            UrlHelper.hasPrefix(url, "https://blog.sshh.io/")) {
            return true;
        }
        return s_mediumUrl.matcher(url).matches();
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
        return _date;
    }

    @Override
    public Optional<TemporalAccessor> getPublicationDate() {
        return _date;
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
        final ExtractedLinkData linkData = new ExtractedLinkData(getTitle(),
                                                                 getSubtitle().isPresent() ? new String[] { getSubtitle().get() }
                                                                                           : new String[0],
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
        return _language;
    }

    private static List<AuthorData> extractAuthors(final JSONObject payload) throws ContentParserException {
        final List<AuthorData> list = new ArrayList<>(1);
        try {
            final Object authorNode = payload.get("author");
            String channelName = null;
            switch (authorNode) {
              case JSONArray node -> {
                  if (node.length() > 1) {
                      final List<AuthorData> authors = new ArrayList<>();
                      for (int i = 0; i < ((JSONArray)authorNode).length(); i++) {
                          final String name = ((JSONArray)authorNode).getJSONObject(i).getString("name");
                          authors.add(LinkContentParserUtils.parseAuthorName(name));
                      }
                      return authors;
                  }
                  channelName = node.getJSONObject(0).getString("name");
              }
              case JSONObject node -> {
                  channelName = node.getString("name");
              }
              default -> {
                  throw new ContentParserException("Error while parsing JSON, author node is of type " + authorNode.getClass().getName());
              }
            }

            final AuthorData author = getWellKnownAuthor(channelName);
            if (author != null) {
                list.add(author);
                return list;
            }

            final String[] components = channelName.split("(, and | and |, )");
            for (final String component: components) {
                list.add(LinkContentParserUtils.parseAuthorName(component));
            }
            return list;
        } catch (final JSONException e) {
            throw new ContentParserException("Error while parsing JSON", e);
        }
    }

    private static AuthorData getWellKnownAuthor(final String authorName) {
        return switch (authorName) {
            case "Sebastian Raschka, PhD",
                 "Ahead of AI" -> new AuthorData(Optional.empty(),
                                                 Optional.of("Sebastian"),
                                                 Optional.empty(),
                                                 Optional.of("Raschka"),
                                                 Optional.empty(),
                                                 Optional.empty());
            case "Science étonnante" -> WellKnownAuthors.DAVID_LOUAPRE;
            default -> null;
        };
    }
}
