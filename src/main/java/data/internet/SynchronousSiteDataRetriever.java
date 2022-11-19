package data.internet;

import java.io.IOException;
import java.io.InputStream;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import utils.ExitHelper;
import utils.FileSection;
import utils.internet.UriHelper;
import utils.internet.UrlHelper;

/**
 * Synchronous retrieving of site data
 */
public class SynchronousSiteDataRetriever {

    private final SiteDataPersister _persister;
    private final SSLSocketFactory _sslSocketFactory;

    private static final int s_connectTimeout = 30000;
    private static final int s_readTimeout = 60000;
    private static final int s_maxNbRedirects = 40;

    private static final String s_userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv\", \"0.0) Gecko/20100101 Firefox/90.0";

    /**
     * @param persister
     */
    public SynchronousSiteDataRetriever(final SiteDataPersister persister) {
        _persister = persister;
        _sslSocketFactory = getDisabledPKIXCheck();
    }

    /**
     * @param url
     * @param consumer
     *   - its first argument is always true since the data is always fresh
     *   - its second argument is the site data
     * @param doNotUseCookies if true, cookies will not be recorded and resend while following redirections
     */
    public void retrieve(final String url,
                         final BiConsumer<Boolean, SiteData> consumer,
                         final boolean doNotUseCookies) {
        retrieveInternal(url, url, consumer, 0, doNotUseCookies ? null : new CookieManager());
    }

    private void retrieveInternal(final String initialUrl,
                                  final String currentUrl,
                                  final BiConsumer<Boolean, SiteData> consumer,
                                  final int depth,
                                  final CookieManager cookieManager) {
        Optional<Integer> httpCode = Optional.empty();
        Optional<Map<String, List<String>>> headers = Optional.empty();
        SiteData.Status status = SiteData.Status.SUCCESS;
        Optional<String> error = Optional.empty();
        Optional<InputStream> dataStream = Optional.empty();

        try {
            final HttpURLConnection httpConnection = httpConnect(currentUrl, cookieManager);
            headers = Optional.of(httpConnection.getHeaderFields());
            final int responseCode = httpConnection.getResponseCode();
            httpCode = Optional.of(Integer.valueOf(responseCode));
            if (responseCode == HttpURLConnection.HTTP_MOVED_PERM || // 301
                responseCode == HttpURLConnection.HTTP_MOVED_TEMP || // 302
                responseCode == HttpURLConnection.HTTP_SEE_OTHER || // 303
                responseCode == 307) {
                if (depth == s_maxNbRedirects) {
                    error = Optional.of("Too many redirects (" + s_maxNbRedirects + ") occurred while trying to load URL " + initialUrl);
                    status = SiteData.Status.FAILURE;
                } else {                    
                    final String location = httpConnection.getHeaderField("Location");
                    final String redirectUrl = getRedirectionUrl(currentUrl, location);
                    if (cookieManager != null) {
                        final Map<String, List<String>> headerFields = httpConnection.getHeaderFields();
                        final List<String> cookies = headerFields.get("Set-Cookie");
                        if (cookies != null) {
                            for (final String cookie: cookies) {
                                for (final HttpCookie c: HttpCookie.parse(cookie)) {
                                    cookieManager.getCookieStore().add(UriHelper.convertStringToUri(currentUrl), c);
                                }
                            }
                        }
                    }
                    retrieveInternal(initialUrl, redirectUrl, consumer, depth + 1, cookieManager);
                    return;
                }
            }
            if (responseCode != HttpURLConnection.HTTP_OK /* 200 */ &&
                responseCode != HttpURLConnection.HTTP_CREATED   /* 201 */) {
                error = Optional.of("page not found");
                status = SiteData.Status.FAILURE;
            }
            dataStream = Optional.of(httpConnection.getInputStream());
        } catch (final IOException e) {
            error = Optional.of(e.toString());
            status = SiteData.Status.FAILURE;
        }
        final Instant timestamp = Instant.now();
        _persister.persist(initialUrl, status, httpCode, headers, dataStream, error, timestamp);
        final FileSection dataFile = _persister.getDataFileSection(initialUrl, timestamp);
        final SiteData siteData = new SiteData(initialUrl, status, httpCode, headers, Optional.of(dataFile), error);
        consumer.accept(Boolean.TRUE, siteData);
    }

    /**
     * @param url
     * @param doNotUseCookies if true, cookies will not be recorded and resend while following redirections
     * @return
     * @throws IOException
     */
    public String getGzippedContent(final String url,
                                    final boolean doNotUseCookies) throws IOException {
        try {
            final HttpURLConnection httpConnection = httpConnect(url, doNotUseCookies ? null : new CookieManager());
            try (final GZIPInputStream gzipReader = new GZIPInputStream(httpConnection.getInputStream())) {
                final byte[] bytes = gzipReader.readAllBytes();
                return new String(bytes, StandardCharsets.UTF_8);
            }
        } catch (final IOException e) {
            throw new IOException("Failed to get gzipped payload from " + url, e);
        }
    }

    private HttpURLConnection httpConnect(final String urlString,
                                          final CookieManager cookieManager) throws IOException {
        final URL url = UrlHelper.convertStringToUrl(urlString);
        final URLConnection connection = url.openConnection();
        final HttpURLConnection httpConnection = (HttpURLConnection)connection;
        if (url.getProtocol().equals("https")) {
            ((HttpsURLConnection)connection).setSSLSocketFactory(_sslSocketFactory);
        }
        connection.setConnectTimeout(s_connectTimeout);
        connection.setReadTimeout(s_readTimeout);
        httpConnection.setInstanceFollowRedirects(false);
        if (cookieManager != null) {
            final String cookies = cookieManager.getCookieStore()
                                                .get(UriHelper.convertStringToUri(urlString))
                                                .stream()
                                                .map(h -> h.toString())
                                                .collect(Collectors.joining(";"));
            connection.setRequestProperty("Cookie", cookies);
        }
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
        return httpConnection;
    }

    private static SSLSocketFactory getDisabledPKIXCheck() {
        // Create a trust manager that does not validate certificate chains
        final TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
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
                }
            };

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

    private static String getRedirectionUrl(final String currentUrl,
                                            final String redirection) {
        if (redirection.startsWith("http://") || redirection.startsWith("https://")) {
            return redirection;
        }
        final URI uri = UriHelper.convertStringToUri(currentUrl);
        if (redirection.startsWith("/")) {
            final URI redirectUri = UriHelper.buildUri(uri.getScheme(), uri.getHost(), redirection);
            return redirectUri.toString();
        }
        final URI redirectUri = UriHelper.buildUri(uri.getScheme(), uri.getHost(), uri.getPath().replaceFirst("[^/]*$", "") + redirection);
        return redirectUri.toString();
    }
}
