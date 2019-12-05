package data.internet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import data.internet.SiteData.Status;
import utils.ExitHelper;
import utils.FileHelper;

public class SiteDataPersister {

    private final Path _path;
    static private final DateTimeFormatter _timestampFormatter = DateTimeFormatter.ofPattern("yyyyMMdd-hhmmss");
    static final private int s_file_buffer_size = 8192;
    static final private int s_max_content_size = 8 * 1024 * 1024;

    SiteDataPersister(final Path path) {
        _path = path;
    }
    
    void persist(final URL url,
                 final Instant timestamp,
                 final Status status,
                 final Optional<Integer> httpCode,
                 final Optional<Map<String, List<String>>> headers,
                 final Optional<InputStream> dataStream,
                 final Optional<String> error) {
        
        getOutputDirectory(url, timestamp).toFile().mkdirs();

        try (final PrintStream stream = new PrintStream(getStatusFile(url, timestamp).toFile())) {
            stream.println(status);            
        } catch (final FileNotFoundException e) {
            ExitHelper.exit(e);
        }
        
        if (httpCode.isPresent()) {
            try (final PrintStream stream = new PrintStream(getHttpCodeFile(url, timestamp).toFile())) {
                stream.println(httpCode.get());            
            } catch (final FileNotFoundException e) {
                ExitHelper.exit(e);
            }            
        }
        
        if (headers.isPresent()) {
            try (final PrintStream stream = new PrintStream(getHeadersFile(url, timestamp).toFile())) {
                Map<String, List<String>> heads = headers.get();
                for (final String head : heads.keySet()) {
                    stream.print(head);
                    for (final String value : heads.get(head)) {
                        stream.print('\t');
                        stream.print(value);
                    }
                    stream.println();
                }
            } catch (final FileNotFoundException e) {
                ExitHelper.exit(e);
            }            
        }
        
        if (dataStream.isPresent()) {
            try (final PrintStream stream = new PrintStream(getDataFile(url, timestamp).toFile())) {
                long size = 0L;
                final byte[] buffer = new byte[s_file_buffer_size];
                int length;
                while ((size <= s_max_content_size) && (length = dataStream.get().read(buffer)) > 0) {
                    stream.write(buffer, 0, length);
                    size += length;
                }
                if (size > s_max_content_size) {
                    System.out.println("retrieved content of " + url + " is truncated");
                }
            } catch (final IOException e) {
                ExitHelper.exit(e);
            }            
        }
        
        if (error.isPresent()) {
            try (final PrintStream stream = new PrintStream(getErrorFile(url, timestamp).toFile())) {
                stream.println(error.get());            
            } catch (final FileNotFoundException e) {
                ExitHelper.exit(e);
            }            
        }
    }

    private Path getStatusFile(final URL url,
                               final Instant timestamp) {
        return getOutputDirectory(url, timestamp).resolve("status");
    }

    private Path getHttpCodeFile(final URL url,
                                 final Instant timestamp) {
        return getOutputDirectory(url, timestamp).resolve("http code");
    }

    private Path getHeadersFile(final URL url,
                                final Instant timestamp) {
        return getOutputDirectory(url, timestamp).resolve("header");
    }

    Path getDataFile(final URL url, final Instant timestamp) {
        return getOutputDirectory(url, timestamp).resolve("data");
    }

    private Path getErrorFile(final URL url, final Instant timestamp) {
        return getOutputDirectory(url, timestamp).resolve("error");
    }

    private Path getOutputDirectory(final URL url, final Instant timestamp) {

        final LocalDateTime localizedTimestamp = LocalDateTime.ofInstant(timestamp, ZoneOffset.UTC);
        return _path.resolve(FileHelper.generateFileNameFromURL(url))
                .resolve(_timestampFormatter.format(localizedTimestamp));
    }

}
