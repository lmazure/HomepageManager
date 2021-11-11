package data.linkchecker.oracleblogs;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.JSONArray;
import org.json.JSONObject;

import data.linkchecker.ContentParserException;
import data.linkchecker.LinkContentParserUtils;
import utils.ExitHelper;
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
    
    private static final int s_connectTimeout = 30000;
    private static final int s_readTimeout = 60000;
    private static final String s_userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv\", \"0.0) Gecko/20100101 Firefox/90.0";
    private static final SSLSocketFactory _sslSocketFactory = getDisabledPKIXCheck();

    private final String _title;
    private final Optional<String> _subtitle;
    private final LocalDate _publicationDate;
    private final List<AuthorData> _authors;
    private final ContentParserException _exception;
    private final ContentParserException _authorException;

    public OracleBlogsLinkContentParser(final String data,
                                        final URL url) {

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
        
        String stuctureJsonPayload = null;
        try {
            stuctureJsonPayload = getStructureJsonPayload(url, site);
        } catch (final IOException e) {
            _exception = new ContentParserException("failed to get structure JSON data for " + url, e);
            _title = null;
            _subtitle = null;
            _publicationDate = null;
            _authors = null;
            _authorException = null;
            return;
        }
        
            final JSONObject o = new JSONObject(stuctureJsonPayload);
            final JSONObject siteInfo = o.getJSONObject("siteInfo");
            final JSONObject base = siteInfo.getJSONObject("base");
            final JSONObject properties = base.getJSONObject("properties");
            final JSONArray channelAccessTokens = properties.getJSONArray("channelAccessTokens");
            final String channelAccessToken = channelAccessTokens.getJSONObject(0).getString("value");
        
        
        String jsonPayload = null;
        try {
            jsonPayload = getJsonPayload(url, channelAccessToken, caas);
        } catch (final IOException e) {
            // TODO je n'affiche pas la bonne URL
            _exception = new ContentParserException("failed to get JSON data for " + url, e);
            _title = null;
            _subtitle = null;
            _publicationDate = null;
            _authors = null;
            _authorException = null;
            return;
        }

        _exception = null;
        
        final JSONObject obj = new JSONObject(jsonPayload);
        final JSONArray items = obj.getJSONArray("items");
        final JSONObject firstItem = items.getJSONObject(0);
        final JSONObject fields = firstItem.getJSONObject("fields"); 
        _title = fields.getString("title");
        
        final String html = fields.getString("body");
        final Matcher m2 = s_subtitlePattern.matcher(html);
        if (m2.find()) {
            _subtitle = Optional.of(m2.group(1));
        } else {
            _subtitle = Optional.empty();
        }
        
        final String creationDate = fields.getJSONObject("publish_date").getString("value");
        _publicationDate = LocalDate.parse(creationDate, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        final JSONArray authors = fields.getJSONArray("author");
        _authors = new ArrayList<>(authors.length());
        ContentParserException authorException = null;
        for (int i = 0; i < authors.length(); i++) {
            final String authorUrl = authors.getJSONObject(i).getJSONArray("links").getJSONObject(0).getString("href");
            try {
                _authors.add(getAuthor(StringHelper.convertStringToUrl(authorUrl)));
            } catch (final IOException e) {
                authorException = new ContentParserException("failed to read author JSON data for " + url, e);
                break;
            } catch (final ContentParserException e) {
                authorException = e;
                break;
            }
        }
        _authorException = authorException;
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

    public List<AuthorData> getAuthor() throws ContentParserException {
        if (_exception != null) {
            throw _exception;
        }
        if (_authorException != null) {
            throw _authorException;
        }
        return _authors;
    }

    public static String getStructureJsonPayload(final URL url,
                                                 final String site) throws IOException {
        final String uu = url.toString();
        final String urlJsonStructure =uu.replaceFirst("/post/.*", "/" + site + "/structure.json");
        final URL u = StringHelper.convertStringToUrl(urlJsonStructure);
        return getJson(u);
    }


    public static String getJsonPayload(final URL url,
                                        final String channelAccessToken,
                                        final String caas) throws IOException {
        final String slug = Path.of(url.getPath()).getFileName().toString();
        final String jsonUrl = "https://blogs.oracle.com/content/published/api/v1.1/items?fields=ALL&orderBy=name:asc&limit=1&q=((type%20eq%20\"Blog-Post\")%20and%20(language%20eq%20\"en-US\"%20or%20translatable%20eq%20\"false\")%20and%20(slug%20eq%20\""
                               + slug
                               + "\"))&channelToken="
                               + channelAccessToken
                               + "&cb="
                               + caas;
        final URL u = StringHelper.convertStringToUrl(jsonUrl);
        return getJson(u);
    }

    public static AuthorData getAuthor(final URL url) throws IOException, ContentParserException {
        final String jsonPayload = getJson(url);
        final JSONObject obj = new JSONObject(jsonPayload);
        final String name = obj.getString("name");
        return LinkContentParserUtils.getAuthor(name);
    }

    public static String getJson(final URL url) throws IOException {
        try {
            final URLConnection connection = url.openConnection();
            final HttpURLConnection httpConnection = (HttpURLConnection)connection;
            if (url.getProtocol().equals("https")) {
                ((HttpsURLConnection)connection).setSSLSocketFactory(_sslSocketFactory);
            }
            connection.setConnectTimeout(s_connectTimeout);
            connection.setReadTimeout(s_readTimeout);
            httpConnection.setRequestMethod("GET");
            httpConnection.setRequestProperty("User-Agent", s_userAgent);
            httpConnection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            httpConnection.setRequestProperty("Accept-Language", "en");
            httpConnection.setRequestProperty("Accept-Encoding", "gzip");
            httpConnection.setRequestProperty("DNT", "1");
            httpConnection.setRequestProperty("Connection", "keep-alive");
            httpConnection.setRequestProperty("Upgrade-Insecure-Requests", "1");
            httpConnection.setRequestProperty("Sec-Fetch-Dest", "document");
            httpConnection.setRequestProperty("Sec-Fetch-Mode", "navigate");
            httpConnection.setRequestProperty("Sec-Fetch-Site", "same-origin");
            httpConnection.setRequestProperty("Pragma", "no-cache");
            httpConnection.setRequestProperty("Cache-Control", "no-cache");
            httpConnection.setRequestProperty("TE", "trailers");
            httpConnection.connect();
            try (final GZIPInputStream gzipReader = new GZIPInputStream(connection.getInputStream())) {
                final byte[] bytes = gzipReader.readAllBytes();
                return new String(bytes, StandardCharsets.UTF_8);
            }
        } catch (final IOException e) {
            throw new IOException("Failed to get JSON payload from " + url, e);
        }
    }

    private static SSLSocketFactory getDisabledPKIXCheck() {
        // Create a trust manager that does not validate certificate chains
        final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            @Override
            public void checkClientTrusted(final X509Certificate[] chain, final String authType) {
                // to nothing
            }
            @Override
            public void checkServerTrusted(final X509Certificate[] chain, final String authType) {
                // to nothing
            }
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        } };

        // Install the all-trusting trust manager
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (final NoSuchAlgorithmException | KeyManagementException e) {
            ExitHelper.exit(e);
        }

        // Create a SSL socket factory with our all-trusting manager
        assert(sslContext != null);
        return sslContext.getSocketFactory();
    }
}
