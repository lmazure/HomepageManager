package data.internet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import utils.ExitHelper;

public class SiteDataRetriever {

    private final Path _cachePath;
    static private final DateTimeFormatter _timestampFormatter = DateTimeFormatter.ofPattern("yyyyMMdd-hhmmss");
    final private static int s_connectTimeout = 30000;
    final private static int s_readTimeout = 60000;

    // pretend to be Firefox 44.0.2
    final private static String s_userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:44.0) Gecko/20100101 Firefox/44.0";

    private SSLSocketFactory _sslSocketFactory;
    final private static int s_file_buffer_size = 8192;
    final private static int s_max_content_size = 8 * 1024 * 1024;

    
    public SiteDataRetriever(final Path cachePath) {
        
        _cachePath = cachePath;
        _sslSocketFactory = getDisabledPKIXCheck();
    }
    
    public SiteDataRetrieval retrieve(final URL url) {
        
        URLConnection connection;        

        final Instant timestamp = Instant.now();
        
        final File dataFile = getDataFile(url, timestamp).toFile();
        final File errorFile = getErrorFile(url, timestamp).toFile();
        final HashMap<String,String> cookies = new HashMap<String, String>();
        
        try (final PrintStream outWriter = new PrintStream(dataFile);
             final PrintStream errorWriter = new PrintStream(errorFile)) {
                 
                 try {
                     connection = url.openConnection();
                     if ( url.getProtocol().equals("https") ) {
                         ((HttpsURLConnection)connection).setSSLSocketFactory( _sslSocketFactory );
                     }
                     connection.setConnectTimeout(s_connectTimeout);
                     connection.setReadTimeout(s_readTimeout);
                 } catch (final IOException e) {
                     errorWriter.println("failed to create the connection");
                     errorWriter.println(e);
                     final CompletableFuture<SiteData> future = new CompletableFuture<SiteData>();
                     future.complete(new SiteData(SiteData.Status.FAILURE, dataFile, errorFile));
                     return new SiteDataRetrieval(SiteDataRetrieval.Status.UP_TO_DATE,
                                                  new CompletableFuture<SiteData>(),
                                                  null);
                 }
                 
                 try {
                     final HttpsURLConnection httpConnection = (HttpsURLConnection)connection;
                     httpConnection.setRequestMethod("GET"); // en mettant HEAD, certains sites renvoient 403
                     httpConnection.setRequestProperty ( "User-agent", s_userAgent);
                     final String host = httpConnection.getURL().getHost();
                     final String cookie = cookies.get(host);
                     if (cookie != null) {
                         httpConnection.setRequestProperty( "Cookie", cookie);           
                     }
                     httpConnection.connect();
                     final int response = httpConnection.getResponseCode();
                     
                     if ( response == HttpURLConnection.HTTP_MOVED_PERM /* 301 */ ||
                          response == HttpURLConnection.HTTP_MOVED_TEMP /* 302 */ ||
                          response == HttpURLConnection.HTTP_SEE_OTHER  /* 303 */ ||
                          response == HttpURLConnection.HTTP_USE_PROXY  /* 305 */) {
                         
                             final String location = httpConnection.getHeaderField("Location");
                             if ( location==null ) {
                                 errorWriter.println("\"" +
                                         httpConnection.getURL().toString() +
                                         "\" response is "+
                                         response +
                                         " but header \"Location\" is not present");
                                 final CompletableFuture<SiteData> future = new CompletableFuture<SiteData>();
                                 future.complete(new SiteData(SiteData.Status.FAILURE, dataFile, errorFile));
                                 return new SiteDataRetrieval(SiteDataRetrieval.Status.UP_TO_DATE,
                                                              new CompletableFuture<SiteData>(),
                                                              null);
                             }
                             outWriter.println(response);
                                  for (int i = 1; httpConnection.getHeaderField(i) != null; i++) {
                                       final String key = httpConnection.getHeaderFieldKey(i);
                                       if ( (key!=null) && key.equalsIgnoreCase("set-cookie") )
                                       {
                                           final String receivedCookie = httpConnection.getHeaderField(key);
                                           final int index = receivedCookie.indexOf(";");
                                           if ( index>0 ) { // some servers sent incorrect cookies
                                               final String receivedCookieValue = receivedCookie.substring(0, index);
                                               cookies.put(host,receivedCookieValue);
                                           }
                                       }
                                   }
                                 //final URL redirectURL = new URL(httpConnection.getURL(),location);
                                 //final URLLivenessStatus s = internallyCheckURLLiveness(redirectURL,depth+1, cookies);
                                 //finalRedirection = s.getFinalRedirection();
                                 //status = s.getCode();
                                 //detailedMessage += " -- " + s.getExplanation();
                                 ExitHelper.exit("redirection is not implemented");
                                 
                     } else if ( response != HttpURLConnection.HTTP_OK      /* 200 */ &&
                                 response != HttpURLConnection.HTTP_CREATED /* 201 */) {
                         
                         errorWriter.println("page not found");
                         final CompletableFuture<SiteData> future = new CompletableFuture<SiteData>();
                         future.complete(new SiteData(SiteData.Status.FAILURE, dataFile, errorFile));
                         return new SiteDataRetrieval(SiteDataRetrieval.Status.UP_TO_DATE,
                                                      new CompletableFuture<SiteData>(),
                                                      null);                         
                     } else {
                         
                         try {
                             loadUrlContent(httpConnection, outWriter);
                         } catch (final SocketTimeoutException e) {
                             errorWriter.println("socket timed out during the retrieval of the URL content");
                             errorWriter.println(e);
                             final CompletableFuture<SiteData> future = new CompletableFuture<SiteData>();
                             future.complete(new SiteData(SiteData.Status.FAILURE, dataFile, errorFile));
                             return new SiteDataRetrieval(SiteDataRetrieval.Status.UP_TO_DATE,
                                                          new CompletableFuture<SiteData>(),
                                                          null);
                         } catch (final SocketException e) {
                             errorWriter.println("socket exception during the retrieval of the URL content");
                             errorWriter.println(e);
                             final CompletableFuture<SiteData> future = new CompletableFuture<SiteData>();
                             future.complete(new SiteData(SiteData.Status.FAILURE, dataFile, errorFile));
                             return new SiteDataRetrieval(SiteDataRetrieval.Status.UP_TO_DATE,
                                                          new CompletableFuture<SiteData>(),
                                                          null);
                         } catch (final IOException e) {
                             errorWriter.println("unexpected exception during the retrieval of the URL content");
                             errorWriter.println(e);
                             final CompletableFuture<SiteData> future = new CompletableFuture<SiteData>();
                             future.complete(new SiteData(SiteData.Status.FAILURE, dataFile, errorFile));
                             return new SiteDataRetrieval(SiteDataRetrieval.Status.UP_TO_DATE,
                                                          new CompletableFuture<SiteData>(),
                                                          null);
                         }
                     }
                     
                 } catch (final UnknownHostException e) {
                     errorWriter.println("unknown host");
                     errorWriter.println(e);
                     final CompletableFuture<SiteData> future = new CompletableFuture<SiteData>();
                     future.complete(new SiteData(SiteData.Status.FAILURE, dataFile, errorFile));
                     return new SiteDataRetrieval(SiteDataRetrieval.Status.UP_TO_DATE,
                                                  new CompletableFuture<SiteData>(),
                                                  null);
                 } catch (final SSLHandshakeException e) {
                     errorWriter.println("incorrect certificate");
                     errorWriter.println(e);
                     final CompletableFuture<SiteData> future = new CompletableFuture<SiteData>();
                     future.complete(new SiteData(SiteData.Status.FAILURE, dataFile, errorFile));
                     return new SiteDataRetrieval(SiteDataRetrieval.Status.UP_TO_DATE,
                                                  new CompletableFuture<SiteData>(),
                                                  null);
                 } catch (final SocketTimeoutException e) {
                     errorWriter.println("timeout");
                     errorWriter.println(e);
                     final CompletableFuture<SiteData> future = new CompletableFuture<SiteData>();
                     future.complete(new SiteData(SiteData.Status.FAILURE, dataFile, errorFile));
                     return new SiteDataRetrieval(SiteDataRetrieval.Status.UP_TO_DATE,
                                                  new CompletableFuture<SiteData>(),
                                                  null);
                 } catch (final IOException e) {
                     errorWriter.println("failed to connect");
                     errorWriter.println(e);
                     final CompletableFuture<SiteData> future = new CompletableFuture<SiteData>();
                     future.complete(new SiteData(SiteData.Status.FAILURE, dataFile, errorFile));
                     return new SiteDataRetrieval(SiteDataRetrieval.Status.UP_TO_DATE,
                                                  new CompletableFuture<SiteData>(),
                                                  null);
                 }
        } catch (final FileNotFoundException e1) {
            ExitHelper.exit(e1);
        }


         final CompletableFuture<SiteData> future = new CompletableFuture<SiteData>();
         future.complete(new SiteData(SiteData.Status.SUCCESS, dataFile, errorFile));
         return new SiteDataRetrieval(SiteDataRetrieval.Status.UP_TO_DATE,
                                      new CompletableFuture<SiteData>(),
                                      null);
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
        return _cachePath.resolve(url.toString())
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
        } catch (final NoSuchAlgorithmException e) {
            ExitHelper.exit(e);
        } catch (final KeyManagementException e) {
            ExitHelper.exit(e);
        }

        // Create a SSL socket factory with our all-trusting manager
        assert(sslContext != null);
        return sslContext.getSocketFactory();
    }
    
    static private void loadUrlContent(final URLConnection connection,
                                       final PrintStream dataStream) throws IOException {
        
        long size = 0L;

        try (final InputStream inputStream = connection.getInputStream()) {
            final byte[] buffer = new byte[s_file_buffer_size];
            int length;
            while (( size <= s_max_content_size ) &&
                   (length = inputStream.read(buffer)) > 0) {
                dataStream.write(buffer, 0, length);
                size += length;
            }
        }
    
        if ( size > s_max_content_size ) {
            System.out.println("retrieved content of "+ connection.getURL() + " is truncated");
        }
    }

}
