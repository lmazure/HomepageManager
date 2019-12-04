package data.internet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import utils.ExitHelper;
import utils.FileHelper;

public class SynchronousSiteDataRetriever {

    private final Path _cachePath;
    static private final DateTimeFormatter _timestampFormatter = DateTimeFormatter.ofPattern("yyyyMMdd-hhmmss");
    final private static int s_connectTimeout = 30000;
    final private static int s_readTimeout = 60000;

    // pretend to be Firefox 44.0.2
    final private static String s_userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:44.0) Gecko/20100101 Firefox/44.0";

    private SSLSocketFactory _sslSocketFactory;
    final private static int s_file_buffer_size = 8192;
    final private static int s_max_content_size = 8 * 1024 * 1024;

    
    public SynchronousSiteDataRetriever(final Path cachePath) {
        _cachePath = cachePath;
        _sslSocketFactory = getDisabledPKIXCheck();
    }
    
    public void retrieve(final URL url,
                         final Consumer<SiteData> consumer) {

        final Instant timestamp = Instant.now();
        getOutputDirectory(url, timestamp).toFile().mkdirs();
        final File httpCodeFile = getHttpCodeFile(url, timestamp).toFile();
        final File headerFile = getHeaderFile(url, timestamp).toFile();
        final File dataFile = getDataFile(url, timestamp).toFile();
        final File errorFile = getErrorFile(url, timestamp).toFile();
        
        try (final PrintStream httpCodeWriter = new PrintStream(httpCodeFile);
             final PrintStream headerWriter = new PrintStream(headerFile);
             final PrintStream dataWriter = new PrintStream(dataFile);
             final PrintStream errorWriter = new PrintStream(errorFile)) {
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
                 final Map<String, List<String>> headers = connection.getHeaderFields();
                 writeUrlHeader(headers, headerWriter);
                 final int httpCode = httpConnection.getResponseCode();
                 writeHttpCodeHeader(httpCode, httpCodeWriter);
                 if ( httpCode != HttpURLConnection.HTTP_OK         /* 200 */ &&
                      httpCode != HttpURLConnection.HTTP_CREATED    /* 201 */ &&
                      httpCode != HttpURLConnection.HTTP_MOVED_PERM /* 301 */ &&
                      httpCode != HttpURLConnection.HTTP_MOVED_TEMP /* 302 */ &&
                      httpCode != HttpURLConnection.HTTP_SEE_OTHER  /* 303 */ &&
                      httpCode != HttpURLConnection.HTTP_USE_PROXY  /* 305 */ ) {
                     errorWriter.println("page not found");
                     consumer.accept(new SiteData(SiteData.Status.FAILURE, httpCode, headers, dataFile, errorFile));
                 }
                 writeUrlContent(httpConnection, dataWriter);
                 consumer.accept(new SiteData(SiteData.Status.SUCCESS, httpCode, headers, dataFile, errorFile));
             } catch (final IOException e) {
                 e.printStackTrace(errorWriter);
                 consumer.accept(new SiteData(SiteData.Status.FAILURE, -1, new HashMap<String, List<String>>(), dataFile, errorFile));
             }
        } catch (final FileNotFoundException e1) {
            ExitHelper.exit(e1);
        }
    }
    
    private Path getHttpCodeFile(final URL url,
                                 final Instant timestamp) {
        return getOutputDirectory(url, timestamp).resolve("http code");
    }

    private Path getHeaderFile(final URL url,
                               final Instant timestamp) {
        return getOutputDirectory(url, timestamp).resolve("header");
    }

    private Path getDataFile(final URL url,
                             final Instant timestamp) {
        return getOutputDirectory(url, timestamp).resolve("data");
    }
    
    private Path getErrorFile(final URL url,
                              final Instant timestamp) {
        return getOutputDirectory(url, timestamp).resolve("error");
    }
    
    private Path getOutputDirectory(final URL url,
                                    final Instant timestamp) {
        
        final LocalDateTime localizedTimestamp = LocalDateTime.ofInstant(timestamp, ZoneOffset.UTC);
        return _cachePath.resolve(FileHelper.generateFileNameFromURL(url))
                         .resolve(_timestampFormatter.format(localizedTimestamp));
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

    static private void writeUrlHeader(final Map<String, List<String>> headers,
                                       final PrintStream headerStream) {

        for (final String header : headers.keySet()) {
            headerStream.print(header);
            for (final String value : headers.get(header)) {
                headerStream.print('\t');
                headerStream.print(value);
            }
            headerStream.println();
        }
    }

    static private void writeHttpCodeHeader(final int httpCode,
                                            final PrintStream httpCodeStream) {
        httpCodeStream.println(httpCode);
    }

    static private void writeUrlContent(final URLConnection connection,
                                        final PrintStream dataStream) throws IOException {

        long size = 0L;
        try (final InputStream inputStream = connection.getInputStream()) {
            final byte[] buffer = new byte[s_file_buffer_size];
            int length;
            while ((size <= s_max_content_size) && (length = inputStream.read(buffer)) > 0) {
                dataStream.write(buffer, 0, length);
                size += length;
            }
        }
        if (size > s_max_content_size) {
            System.out.println("retrieved content of " + connection.getURL() + " is truncated");
        }
    }
}
