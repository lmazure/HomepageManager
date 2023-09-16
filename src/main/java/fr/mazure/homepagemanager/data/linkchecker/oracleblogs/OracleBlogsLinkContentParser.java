package fr.mazure.homepagemanager.data.linkchecker.oracleblogs;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.mazure.homepagemanager.data.dataretriever.NotGzipException;
import fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever;
import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.ExtractedLinkData;
import fr.mazure.homepagemanager.data.linkchecker.LinkContentParserUtils;
import fr.mazure.homepagemanager.data.linkchecker.LinkDataExtractor;
import fr.mazure.homepagemanager.data.linkchecker.TextParser;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;
import fr.mazure.homepagemanager.utils.internet.UriHelper;
import fr.mazure.homepagemanager.utils.internet.UrlHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkFormat;

/**
 * Data extractor for Oracle blog
*/
public class OracleBlogsLinkContentParser extends LinkDataExtractor {

    private static final String s_htmlTemplate = """
            <html>\r
            <head>
            <script type="text/javascript">
            var SCSCacheKeys = \\{
            \tproduct: '_cache_\\p{XDigit}\\p{XDigit}\\p{XDigit}\\p{XDigit}',
            \tsite: '(_cache_\\p{XDigit}\\p{XDigit}\\p{XDigit}\\p{XDigit})',
            \ttheme: '_cache_\\p{XDigit}\\p{XDigit}\\p{XDigit}\\p{XDigit}',
            \tcomponent: '_cache_\\p{XDigit}\\p{XDigit}\\p{XDigit}\\p{XDigit}',
            \tcaas: '(_cache_\\p{XDigit}\\p{XDigit}\\p{XDigit}\\p{XDigit})'
            \\};
            """;
    private static final Pattern s_htmlPattern = Pattern.compile(s_htmlTemplate);

    private static final Pattern s_subtitlePattern = Pattern.compile("^(<!DOCTYPE html>)?<h2>(.*?)</h2>", Pattern.DOTALL);

    private static final TextParser s_titleParser
        = new TextParser("<meta name=\"title\" content=\"",
                         "\">",
                         "Oracle Blog",
                         "title");
    private static final TextParser s_dateParser
        = new TextParser("<meta name=\"publish_date\" content=\"",
                         "\">",
                         "Oracle Blog",
                         "date");
    private static final TextParser s_authorParser
        = new TextParser("<meta name=\"author\" content=\"",
                         "\">",
                         "Oracle Blog",
                         "author");
    private static DateTimeFormatter s_formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.US);

    private final String _title;
    private final Optional<String> _subtitle;
    private final LocalDate _publicationDate;
    private final List<AuthorData> _authors;
    private final ContentParserException _exception;
    private final ContentParserException _authorException;
    private final SynchronousSiteDataRetriever _retriever;

    /**
     * @param url URL of the link
     * @param data retrieved link data
     */
    public OracleBlogsLinkContentParser(final String url,
                                        final String data) {
        super(cleanUrl(url));
        _retriever = new SynchronousSiteDataRetriever(null);

        // retrieve site and caas from initial HTML
        final Matcher m = s_htmlPattern.matcher(data);
        if (!m.find()) {
            // we assume that we are in the case where the HTML is fully generated, so we try to parse it
            String title;
            try {
                title = s_titleParser.extract(data);
            } catch (final ContentParserException e) {
                _exception = e;
                _title = null;
                _subtitle = null;
                _publicationDate = null;
                _authors = null;
                _authorException = null;
                return;
            }
            _title = title;
            _subtitle = Optional.empty();
            final LocalDate publicationDate;
            try {
                publicationDate = LocalDate.parse(s_dateParser.extract(data), s_formatter);
            } catch (final ContentParserException e) {
                _exception = e;
                _publicationDate = null;
                _authors = null;
                _authorException = null;
                return;
            }
            _publicationDate = publicationDate;
            final List<AuthorData> list = new ArrayList<>(1);
            String author;
            try {
                author = s_authorParser.extract(data);
                list.add(LinkContentParserUtils.getAuthor(author));
            } catch (final ContentParserException e) {
                _exception = null;
                _authorException = e;
                _authors = null;
                return;
            }
            _authors = list;
            _exception = null;
            _authorException = null;
            return;
         }
        final String site = m.group(1);
        final String caas = m.group(2);

        // retrieve channel access token from site structure
        String stuctureJson = null;
        try {
            stuctureJson = getStructureJson(url, site);
        } catch (final IOException | NotGzipException e) {
            _exception = new ContentParserException("failed to get structure JSON data for " + url, e);
            _title = null;
            _subtitle = null;
            _publicationDate = null;
            _authors = null;
            _authorException = null;
            return;
        }
        String channelAccessToken = null;
        try {
            final JSONObject structure = new JSONObject(stuctureJson);
            final JSONObject siteInfo = structure.getJSONObject("siteInfo");
            final JSONObject base = siteInfo.getJSONObject("base");
            final JSONObject properties = base.getJSONObject("properties");
            final JSONArray channelAccessTokens = properties.getJSONArray("channelAccessTokens");
            channelAccessToken = channelAccessTokens.getJSONObject(0).getString("value");
        } catch (final JSONException e) {
            _exception = new ContentParserException("failed to parse structure JSON data for " + url + ". The JSON payload is \"" + stuctureJson + "\"", e);
            _title = null;
            _subtitle = null;
            _publicationDate = null;
            _authors = null;
            _authorException = null;
            return;
        }

        // retrieve article information
        String articleJson = null;
        try {
            articleJson = getJsonPayload(url, channelAccessToken, caas);
        } catch (final IOException | NotGzipException e) {
            _exception = new ContentParserException("failed to get article JSON data for " + url, e);
            _title = null;
            _subtitle = null;
            _publicationDate = null;
            _authors = null;
            _authorException = null;
            return;
        }

        JSONObject fields = null;
        String title;
        Optional<String> subtitle;
        LocalDate publicationDate;

        try {
            final JSONObject article = new JSONObject(articleJson);
            final JSONArray items = article.getJSONArray("items");
            final JSONObject firstItem = items.getJSONObject(0);
            fields = firstItem.getJSONObject("fields");
            title = fields.getString("title").trim();
            final String html = fields.getString("body");
            final Matcher m2 = s_subtitlePattern.matcher(html);
            if (m2.find()) {
                subtitle = Optional.of(HtmlHelper.cleanContent(m2.group(2)));
            } else {
                subtitle = Optional.empty();
            }
            final String pubDate = fields.getJSONObject("publish_date").getString("value");
            publicationDate = Instant.parse(pubDate).atZone(ZoneId.of("Europe/Paris")).toLocalDate();
        } catch (final JSONException e) {
            _exception = new ContentParserException("failed to parse date JSON data for " + url + ". The JSON payload is \""+ articleJson + "\"", e);
            _title = null;
            _subtitle = null;
            _publicationDate = null;
            _authors = null;
            _authorException = null;
            return;
        }
        _exception = null;
        _title = title;
        _subtitle = subtitle;
        _publicationDate = publicationDate;

        // retrieve author data
        final JSONArray authors = fields.getJSONArray("author");
        _authors = new ArrayList<>(authors.length());
        for (int i = 0; i < authors.length(); i++) {
            final String authorUrl = authors.getJSONObject(i).getJSONArray("links").getJSONObject(0).getString("href");
            try {
                _authors.add(getAuthor(authorUrl));
            } catch (final IOException | ContentParserException | JSONException | NotGzipException e) {
                _authorException = new ContentParserException("failed to read author JSON data for " + url, e);
                return;
            }
        }
        _authorException = null;
    }

    @Override
    public String getTitle() {
        if (_exception != null) {
            return "";
        }
        return _title;
    }

    @Override
    public Optional<String> getSubtitle() {
        if (_exception != null) {
            return Optional.empty();
        }
        return _subtitle;
    }

    private String getStructureJson(final String url,
                                    final String site) throws IOException, NotGzipException {
        final String urlJsonStructure = url.replaceFirst("/post/", "/")
                                           .replaceFirst("/[^/]*$", "/" + site + "/structure.json");
        return _retriever.getGzippedContent(urlJsonStructure, false);
    }

    private String getJsonPayload(final String url,
                                  final String channelAccessToken,
                                  final String caas) throws IOException, NotGzipException {
        final URI u = UriHelper.convertStringToUri(url);
        final String slug = Path.of(u.getPath()).getFileName().toString();
        final String jsonUrl = "https://blogs.oracle.com/content/published/api/v1.1/items?fields=ALL&orderBy=name%3Aasc&limit=1&q=((type%20eq%20%22Blog-Post%22)%20and%20(language%20eq%20%22en-US%22%20or%20translatable%20eq%20%22false%22)%20and%20(slug%20eq%20%22"
                               + slug
                               + "%22))&channelToken="
                               + channelAccessToken
                               + "&cb="
                               + caas;
        return _retriever.getGzippedContent(jsonUrl, false);
    }

    private AuthorData getAuthor(final String url) throws IOException, ContentParserException, NotGzipException {
        final String jsonPayload = _retriever.getGzippedContent(url, false);
        final JSONObject obj = new JSONObject(jsonPayload);
        final String name = obj.getString("name");
        return LinkContentParserUtils.getAuthor(name);
    }

    @Override
    public Optional<TemporalAccessor> getDate() throws ContentParserException {
        if (_exception != null) {
            return Optional.of(LocalDate.of(1970, 1, 1));
        }
        return Optional.of(_publicationDate);
    }

    @Override
    public List<AuthorData> getSureAuthors() throws ContentParserException {
        if (_exception != null) {
            return new ArrayList<>();
        }
        if (_authorException != null) {
            return new ArrayList<>();
        }
        return _authors;
    }

    @Override
    public List<ExtractedLinkData> getLinks() throws ContentParserException {
        final ExtractedLinkData linkData = new ExtractedLinkData(getTitle(),
                                                                 getSubtitle().isPresent() ? new String[] { getSubtitle().get() }
                                                                                           : new String[] { },
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

    private static String cleanUrl(final String url) {
        final String u = UrlHelper.removeQueryParameters(url, "source");
        if (u.startsWith("https://blogs.oracle.com/javamagazine/post/")) {
            return u;
        }
        return u.replaceFirst("https://blogs.oracle.com/javamagazine/", "https://blogs.oracle.com/javamagazine/post/");
    }
}
