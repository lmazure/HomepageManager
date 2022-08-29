package data.internet;

import java.io.IOException;
import java.net.HttpURLConnection;
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
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import utils.ExitHelper;
import utils.FileSection;

/**
 * Synchronous retrieving of site data
 */
public class SynchronousSiteDataRetriever {

    private final SiteDataPersister _persister;
    private final SSLSocketFactory _sslSocketFactory;
    private static final int s_connectTimeout = 30000;
    private static final int s_readTimeout = 60000;

    private static final String s_userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv\", \"0.0) Gecko/20100101 Firefox/90.0";


    public SynchronousSiteDataRetriever(final SiteDataPersister persister) {
        _persister = persister;
        _sslSocketFactory = getDisabledPKIXCheck();
    }

    /**
     * @param url
     * @param consumer
     * its first argument is always true since the data is always fresh
     * its second argument is the site data
     * @param maxAge maximum age in seconds
     */
    public void retrieve(final URL url,
                         final BiConsumer<Boolean, SiteData> consumer) {

        final Instant timestamp = Instant.now();
        Optional<Integer> httpCode = Optional.empty();
        Optional<Map<String, List<String>>> headers = Optional.empty();
        Optional<String> error = Optional.empty();

         try {
             final HttpURLConnection httpConnection = httpConnect(url);
             headers = Optional.of(httpConnection.getHeaderFields());
             final int responseCode = httpConnection.getResponseCode();
             httpCode = Optional.of(Integer.valueOf(responseCode));
             if (responseCode != HttpURLConnection.HTTP_OK         /* 200 */ && // TODO this cannot be right
                 responseCode != HttpURLConnection.HTTP_CREATED    /* 201 */) {
                 error = Optional.of("page not found");
                 _persister.persist(url, timestamp, SiteData.Status.FAILURE, httpCode, headers, Optional.empty(), error);
                 consumer.accept(Boolean.TRUE, new SiteData(url, SiteData.Status.FAILURE, httpCode, headers, Optional.empty(), error));
                 return;
             }
             _persister.persist(url, timestamp, SiteData.Status.SUCCESS, httpCode, headers, Optional.of(httpConnection.getInputStream()), error);
             final FileSection dataFile = _persister.getDataFileSection(url, timestamp);
             consumer.accept(Boolean.TRUE, new SiteData(url, SiteData.Status.SUCCESS, httpCode, headers, Optional.of(dataFile), error));
         } catch (final IOException e) {
             error = Optional.of(e.toString());
             _persister.persist(url, timestamp, SiteData.Status.FAILURE, httpCode, headers, Optional.empty(), error);
             consumer.accept(Boolean.TRUE, new SiteData(url, SiteData.Status.FAILURE, httpCode, headers, Optional.empty(), error));
         }
    }

    public String getGzippedContent(final URL url) throws IOException {
        try {
            final HttpURLConnection httpConnection = httpConnect(url);
            try (final GZIPInputStream gzipReader = new GZIPInputStream(httpConnection.getInputStream())) {
                final byte[] bytes = gzipReader.readAllBytes();
                return new String(bytes, StandardCharsets.UTF_8);
            }
        } catch (final IOException e) {
            throw new IOException("Failed to get gzipped payload from " + url, e);
        }
    }

    private HttpURLConnection httpConnect(final URL url) throws IOException {
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
        return httpConnection;
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
