package fr.mazure.homepagemanager.data.linkchecker.substack;

import java.time.ZonedDateTime;
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
import fr.mazure.homepagemanager.data.dataretriever.SiteSlurper;
import fr.mazure.homepagemanager.data.knowledge.WellKnownAuthors;
import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.ExtractedLinkData;
import fr.mazure.homepagemanager.data.linkchecker.LinkContentParserUtils;
import fr.mazure.homepagemanager.data.linkchecker.LinkDataExtractor;
import fr.mazure.homepagemanager.data.linkchecker.TextParser;
import fr.mazure.homepagemanager.utils.StringHelper;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;
import fr.mazure.homepagemanager.utils.internet.JsonHelper;
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

    private static final TextParser s_jsonParser
        = new TextParser("window\\._preloads\\s+=\\s+JSON\\.parse\\(\"",
                         "\"\\)</script>",
                         s_sourceName,
                         "JSON");

    private static final Pattern s_mediumUrl = Pattern.compile("https://[^/]+\\.substack\\.com/.+");

    /**
     * @param url URL of the link
     * @param retriever cache data retriever
     *
     * @throws ContentParserException Failure to extract the information
     */
    public SubstackLinkContentParser(final String url,
                                     final CachedSiteDataRetriever retriever) throws ContentParserException {
        super(url, retriever);

        final SiteSlurper sluper = new SiteSlurper(getRetriever(), url);
        final String data = sluper.getContent();

        final String escapedJson = s_jsonParser.extract(data);
        final String json = JsonHelper.unescape(escapedJson);
        final JSONObject payload = new JSONObject(json);
        final JSONObject post = JsonHelper.getAsNode(payload, "post");

        _title = HtmlHelper.cleanContent(JsonHelper.getAsText(post, "title"));

        final String subtitleStr = post.optString("subtitle");
        _subtitle = (subtitleStr == null || subtitleStr.isEmpty()) ? Optional.empty()
                                                                   : Optional.of(HtmlHelper.cleanContent(subtitleStr));

        final String postDate = JsonHelper.getAsText(post, "post_date");
        _date = Optional.of(ZonedDateTime.parse(postDate).toLocalDate());

        JSONArray bylines = JsonHelper.getAsArray(post, "publishedBylines");
        if (bylines.length() == 0) {
            // Older posts have an empty publishedBylines array; fall back to the publication's contributors
            bylines = JsonHelper.getAsArray(JsonHelper.getAsNode(payload, "pub"), "contributors");
        }
        final JSONObject authorWrapper = new JSONObject();
        authorWrapper.put("author", bylines);
        _sureAuthors = extractAuthors(authorWrapper);

        final String lang = post.optString("language");
        _language = (lang != null && !lang.isEmpty()) ? Locale.forLanguageTag(lang)
                                                      : StringHelper.guessLanguage(HtmlHelper.cleanContent(data)).get();
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
            UrlHelper.hasPrefix(url, "https://blog.kilo.ai/") ||
            UrlHelper.hasPrefix(url, "https://blog.sshh.io/") ||
            UrlHelper.hasPrefix(url, "https://newsletter.kentbeck.com/") ||
            UrlHelper.hasPrefix(url, "https://www.lennysnewsletter.com/")) {
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
                 "Ahead of AI" -> WellKnownAuthors.SEBASTIAN_RASCHKA;
            case "Science étonnante" -> WellKnownAuthors.DAVID_LOUAPRE;
            default -> null;
        };
    }
}
