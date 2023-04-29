package fr.mazure.homepagemanager.data.internet;

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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import fr.mazure.homepagemanager.utils.ExitHelper;
import fr.mazure.homepagemanager.utils.Logger;
import fr.mazure.homepagemanager.utils.internet.HttpHelper;
import fr.mazure.homepagemanager.utils.internet.UriHelper;
import fr.mazure.homepagemanager.utils.internet.UrlHelper;

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
     * @param persister data persister
     */
    public SynchronousSiteDataRetriever(final SiteDataPersister persister) {
        _persister = persister;
        _sslSocketFactory = getDisabledPKIXCheck();
    }

    /**
     * @param url URL of the link to retrieve
     * @param consumer
     *   - its first argument is always true since the data is always fresh
     *   - its second argument is the site data
     * @param doNotUseCookies if true, cookies will not be recorded and resend while following redirections
     */
    public void retrieve(final String url,
                         final BiConsumer<Boolean, FullFetchedLinkData> consumer,
                         final boolean doNotUseCookies) {
        try {
            retrieveInternal(url, url, new Stack<>(), consumer, 0, doNotUseCookies ? null : new CookieManager());
        } catch (final Throwable e) {
            throw new IllegalStateException("Exception while retrieving " + url, e);
        }
    }

    private void retrieveInternal(final String initialUrl,
                                  final String currentUrl,
                                  final Stack<HeaderFetchedLinkData> redirectionsData,
                                  final BiConsumer<Boolean, FullFetchedLinkData> consumer,
                                  final int depth,
                                  final CookieManager cookieManager) {

        Optional<InputStream> dataStream = Optional.empty();
        Optional<String> error = Optional.empty();
        HttpURLConnection httpConnection = null;

        try {
            httpConnection = httpConnect(currentUrl, cookieManager);
        } catch (final Exception e) {
            error = Optional.of("Failed to connect: " + e.toString());
            final HeaderFetchedLinkData redirectionData = new HeaderFetchedLinkData(currentUrl, Optional.empty(), null);
            redirectionsData.push(redirectionData);
        }

        if (httpConnection != null) {
            final Map<String, List<String>> headers = httpConnection.getHeaderFields();
            if (headers.size() == 0) {
                error = Optional.of("No headers");
                final HeaderFetchedLinkData redirectionData = new HeaderFetchedLinkData(currentUrl, Optional.empty(), null);
                redirectionsData.push(redirectionData);
            } else {
                final int responseCode = HttpHelper.getResponseCodeFromHeaders(headers);
                if (httpCodeIsRedirected(responseCode)) {
                    if (depth == s_maxNbRedirects) {
                        error = Optional.of("Too many redirects (" + s_maxNbRedirects + ") occurred while trying to load URL " + initialUrl);
                        final HeaderFetchedLinkData redirectionData = new HeaderFetchedLinkData(currentUrl, Optional.of(headers), null);
                        redirectionsData.push(redirectionData);
                    } else {
                        final HeaderFetchedLinkData redirectionData = new HeaderFetchedLinkData(currentUrl, Optional.of(headers), null);
                        redirectionsData.push(redirectionData);
                        final String location = HttpHelper.getLocationFromHeaders(headers);
                        final String redirectUrl = getRedirectionUrl(currentUrl, location);
                        storeCookies(currentUrl, cookieManager, httpConnection);
                        retrieveInternal(initialUrl, redirectUrl, redirectionsData, consumer, depth + 1, cookieManager);
                        return;
                    }
                } else {
                    try {
                        dataStream = Optional.of(httpConnection.getInputStream());
                    } catch (final IOException e) {
                        error = Optional.of("Failed to get input stream: " + e.toString());
                    }
                    final HeaderFetchedLinkData redirectionData = new HeaderFetchedLinkData(currentUrl, Optional.of(headers), null);
                    redirectionsData.push(redirectionData);
                }
            }
        }

        HeaderFetchedLinkData previous = null;
        do {
            final HeaderFetchedLinkData d = redirectionsData.pop();
            previous = new HeaderFetchedLinkData(d.url(), d.headers(), previous);
        } while (!redirectionsData.isEmpty());
        final Instant timestamp = Instant.now();
        _persister.persist(previous, dataStream, error, timestamp);
        final FullFetchedLinkData siteData = _persister.retrieve(initialUrl, timestamp);
        consumer.accept(Boolean.TRUE, siteData);
    }

    /**
     * get the content of a link whose payload is gzipped
     *
     * @param url URL of the link to retrieve
     * @param doNotUseCookies if true, cookies will not be recorded and resend while following redirections
     * @return payload
     * @throws IOException exception if the payload could not be retrieved
     * @throws NotGzipException
     */
    public String getGzippedContent(final String url,
                                    final boolean doNotUseCookies) throws IOException, NotGzipException {
        final HttpURLConnection httpConnection = httpConnect(url, doNotUseCookies ? null : new CookieManager());
        final Map<String, List<String>> headers = httpConnection.getHeaderFields();
        if (headers.size() == 0) {
            throw new IOException("No headers for " + url);
        }

        final int responseCode = HttpHelper.getResponseCodeFromHeaders(headers);
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("Received HTTP code " + responseCode + " for " + url);
        }

        final String encoding = (headers.get("Content-Encoding") != null) ? headers.get("Content-Encoding").get(0)
                                                                          : null;
        if (!"gzip".equals(encoding)) {
            try (final InputStream reader = httpConnection.getInputStream()) {
                final byte[] bytes = reader.readAllBytes();
                final String errorMessage = new String(bytes, StandardCharsets.UTF_8);
                throw new NotGzipException("\"Content-Encoding\" is not equal to \"gzip\" but to \"" +
                                           encoding +
                                           "\", error message is \"" +
                                           errorMessage +
                                           "\" for " +
                                           url);
            } catch (final IOException e) {
                throw new NotGzipException("\"Content-Encoding\" is not equal to \"gzip\" but to \"" +
                                           encoding +
                                           "\", error message is not retrievable for " +
                                           url,
                                           e);
            }
        }

        try (final InputStream gzipReader = new GZIPInputStream(httpConnection.getInputStream())) {
            final byte[] bytes = gzipReader.readAllBytes();
            return new String(bytes, StandardCharsets.UTF_8);
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
        applyCookies(urlString, cookieManager, connection);
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
        if (redirection.startsWith("http://") ||
            redirection.startsWith("https://")) {
            return redirection;
        }
        final URI uri = UriHelper.convertStringToUri(currentUrl);
        if (redirection.startsWith("/")) {
            final URI redirectUri = UriHelper.buildUri(uri.getScheme(), uri.getHost(), redirection);
            return redirectUri.toString();
        }
        String root = uri.getPath().replaceFirst("[^/]*$", "");
        if (root.isEmpty()) {
            root = "/";
        }
        final URI redirectUri = UriHelper.buildUri(uri.getScheme(), uri.getHost(), root + redirection);
        return redirectUri.toString();
    }

    private static void storeCookies(final String url,
                                     final CookieManager cookieManager,
                                     final HttpURLConnection connection) {

        if (cookieManager == null) return;

         final Map<String, List<String>> headerFields = connection.getHeaderFields();
         final List<String> cookies = headerFields.get("Set-Cookie");
         if (cookies != null) {
             for (final String cookie: cookies) {
                 List<HttpCookie> list = new LinkedList<>();
                 try {
                     list = HttpCookie.parse(cookie);
                 } catch (final IllegalArgumentException e) {
                     Logger.log(Logger.Level.ERROR)
                     .append(url)
                     .append(" has an invalid cookie value: \"")
                     .append(cookie)
                     .append("\" ")
                     .append(e)
                     .submit();
                 }
                 for (final HttpCookie c: list) {
                     cookieManager.getCookieStore().add(UriHelper.convertStringToUri(url), c);
                 }
             }
         }
     }

    private static void applyCookies(final String url,
                                     final CookieManager cookieManager,
                                     final URLConnection connection) {
        if (cookieManager == null) return;

        final String cookies = cookieManager.getCookieStore()
                                            .get(UriHelper.convertStringToUri(url))
                                            .stream()
                                            .map(h -> h.toString())
                                            .collect(Collectors.joining(";"));
        connection.setRequestProperty("Cookie", cookies);
    }

    private static boolean httpCodeIsRedirected(final int responseCode) {
        return responseCode == HttpURLConnection.HTTP_MOVED_PERM || // 301
               responseCode == HttpURLConnection.HTTP_MOVED_TEMP || // 302
               responseCode == HttpURLConnection.HTTP_SEE_OTHER  || // 303
               responseCode == 307                               || // 307
               responseCode == 308;                                 // 308
    }
}
