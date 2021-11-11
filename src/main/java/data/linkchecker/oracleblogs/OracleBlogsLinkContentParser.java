package data.linkchecker.oracleblogs;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import data.internet.SynchronousSiteDataRetriever;
import data.linkchecker.ContentParserException;
import data.linkchecker.LinkContentParserUtils;
import utils.HtmlHelper;
import utils.StringHelper;
import utils.xmlparsing.AuthorData;

public class OracleBlogsLinkContentParser {

    private static final String s_htmlTemplate = """
            <html>
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

    private static final Pattern s_subtitlePattern = Pattern.compile("<h2>([^<]*)</h2>");
    
    private final String _title;
    private final Optional<String> _subtitle;
    private final LocalDate _publicationDate;
    private final List<AuthorData> _authors;
    private final ContentParserException _exception;
    private final ContentParserException _authorException;
    
    private SynchronousSiteDataRetriever _retriever;

    public OracleBlogsLinkContentParser(final String data,
                                        final URL url) {

        _retriever = new SynchronousSiteDataRetriever(null);

        // retrieve site and caas from initial HTML
        final Matcher m = s_htmlPattern.matcher(data);
        if (!m.find()) {
            _exception = new ContentParserException("HTML does not match " + s_htmlTemplate);
            _title = null;
            _subtitle = null;
            _publicationDate = null;
            _authors = null;
            _authorException = null;
            return;
         }
        final String site = m.group(1);
        final String caas = m.group(2);

        // retrieve channel access token from site structure
        String stuctureJson = null;
        try {
            stuctureJson = getStructureJson(url, site);
        } catch (final IOException e) {
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
            _exception = new ContentParserException("failed to parse structure JSON data for " + url + ". The JSON payload is \""+ stuctureJson + "\"", e);
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
        } catch (final IOException e) {
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
            title = fields.getString("title");
            final String html = fields.getString("body");
            final Matcher m2 = s_subtitlePattern.matcher(html);
            if (m2.find()) {
                subtitle = Optional.of(HtmlHelper.cleanContent(m2.group(1)));
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
                _authors.add(getAuthor(StringHelper.convertStringToUrl(authorUrl)));
            } catch (final IOException | ContentParserException | JSONException e) {
                _authorException = new ContentParserException("failed to read author JSON data for " + url, e);
                return;
            }
        }
        _authorException = null;
    }

    public String getTitle() throws ContentParserException {
        if (_exception != null) {
            throw _exception;
        }
        return _title;
    }

    public Optional<String> getSubtitle() throws ContentParserException {
        if (_exception != null) {
            throw _exception;
        }
        return _subtitle;
    }

    public LocalDate getDate() throws ContentParserException {
        if (_exception != null) {
            throw _exception;
        }
        return _publicationDate;
    }

    public List<AuthorData> getAuthors() throws ContentParserException {
        if (_exception != null) {
            throw _exception;
        }
        if (_authorException != null) {
            throw _authorException;
        }
        return _authors;
    }

    public String getStructureJson(final URL url,
                                   final String site) throws IOException {
        final String urlJsonStructure = url.toString()
                                           .replaceFirst("/post/", "/")
                                           .replaceFirst("/[^/]*$", "/" + site + "/structure.json");
        final URL u = StringHelper.convertStringToUrl(urlJsonStructure);
        return _retriever.getGzippedContent(u);
    }


    public String getJsonPayload(final URL url,
                                 final String channelAccessToken,
                                 final String caas) throws IOException {
        final String slug = Path.of(url.getPath()).getFileName().toString();
        final String jsonUrl = "https://blogs.oracle.com/content/published/api/v1.1/items?fields=ALL&orderBy=name%3Aasc&limit=1&q=((type%20eq%20%22Blog-Post%22)%20and%20(language%20eq%20%22en-US%22%20or%20translatable%20eq%20%22false%22)%20and%20(slug%20eq%20%22"
                               + slug
                               + "%22))&channelToken="
                               + channelAccessToken
                               + "&cb="
                               + caas;
        final URL u = StringHelper.convertStringToUrl(jsonUrl);
        return _retriever.getGzippedContent(u);
    }

    public AuthorData getAuthor(final URL url) throws IOException, ContentParserException {
        final String jsonPayload = _retriever.getGzippedContent(url);
        final JSONObject obj = new JSONObject(jsonPayload);
        final String name = obj.getString("name");
        return LinkContentParserUtils.getAuthor(name);
    }
}
