package data.internet;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import utils.ExitHelper;

/**
 * Synchronous retrieving of site data
 */
public class SynchronousSiteDataRetriever {

    private final SiteDataPersister _persister;
    final private static int s_connectTimeout = 30000;
    final private static int s_readTimeout = 60000;

    // pretend to be Firefox 44.0.2
    final private static String s_userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:44.0) Gecko/20100101 Firefox/44.0";

    private SSLSocketFactory _sslSocketFactory;

    
    public SynchronousSiteDataRetriever(final SiteDataPersister persister) {
        _persister = persister;
        _sslSocketFactory = getDisabledPKIXCheck();
    }
    
    public void retrieve(final URL url,
                         final BiConsumer<Boolean, SiteData> consumer) {

        final Instant timestamp = Instant.now();
        Optional<Integer> httpCode = Optional.empty();
        Optional<Map<String, List<String>>> headers = Optional.empty();
        Optional<String> error = Optional.empty();
        
         try {
             final URLConnection connection = url.openConnection();
             final HttpURLConnection httpConnection = (HttpURLConnection)connection;
             if ( url.getProtocol().equals("https") ) {
                 ((HttpsURLConnection)connection).setSSLSocketFactory( _sslSocketFactory );
             }
             connection.setConnectTimeout(s_connectTimeout);
             connection.setReadTimeout(s_readTimeout);
             httpConnection.setRequestMethod("GET");
             httpConnection.setRequestProperty("User-agent", s_userAgent);
             httpConnection.connect();
             headers = Optional.of(connection.getHeaderFields());
             httpCode = Optional.of(httpConnection.getResponseCode());
             if (httpCode.get() != HttpURLConnection.HTTP_OK         /* 200 */ &&
                 httpCode.get() != HttpURLConnection.HTTP_CREATED    /* 201 */ &&
                 httpCode.get() != HttpURLConnection.HTTP_MOVED_PERM /* 301 */ &&
                 httpCode.get() != HttpURLConnection.HTTP_MOVED_TEMP /* 302 */ &&
                 httpCode.get() != HttpURLConnection.HTTP_SEE_OTHER  /* 303 */ &&
                 httpCode.get() != HttpURLConnection.HTTP_USE_PROXY  /* 305 */ ) {
                 error = Optional.of("page not found");
                 _persister.persist(url, timestamp, SiteData.Status.FAILURE, httpCode, headers, Optional.empty(), error);
                 consumer.accept(Boolean.TRUE, new SiteData(SiteData.Status.FAILURE, httpCode, headers, Optional.empty(), error));
                 return;
             }
             _persister.persist(url, timestamp, SiteData.Status.SUCCESS, httpCode, headers, Optional.of(connection.getInputStream()), error);
             final File dataFile = _persister.getDataFile(url, timestamp).toFile();
             consumer.accept(Boolean.TRUE, new SiteData(SiteData.Status.SUCCESS, httpCode, headers, Optional.of(dataFile), error));
         } catch (final IOException e) {
             error = Optional.of(e.toString());
             _persister.persist(url, timestamp, SiteData.Status.FAILURE, httpCode, headers, Optional.empty(), error);
             consumer.accept(Boolean.TRUE, new SiteData(SiteData.Status.FAILURE, httpCode, headers, Optional.empty(), error));
         }
    }
        
    static private SSLSocketFactory getDisabledPKIXCheck() {
        // Create a trust manager that does not validate certificate chains
        final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            @Override
            public void checkClientTrusted(final X509Certificate[] chain, final String authType ) {
            }
            @Override
            public void checkServerTrusted(final X509Certificate[] chain, final String authType ) {
            }
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        } };
        
        // Install the all-trusting trust manager
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance( "SSL" );
            sslContext.init( null, trustAllCerts, new java.security.SecureRandom() );
        } catch (final NoSuchAlgorithmException | KeyManagementException e) {
            ExitHelper.exit(e);
        }

        // Create a SSL socket factory with our all-trusting manager
        assert(sslContext != null);
        return sslContext.getSocketFactory();
    }
}
