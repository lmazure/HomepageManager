package data.internet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import data.internet.SiteData.Status;
import utils.ExitHelper;
import utils.FileHelper;

public class SiteDataPersister {

    private final Path _path;
    private static final int s_file_buffer_size = 8192;
    private static final int s_max_content_size = 8 * 1024 * 1024;

    public SiteDataPersister(final Path path) {
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
            stream.print(status);            
        } catch (final FileNotFoundException e) {
            ExitHelper.exit(e);
        }
        
        if (httpCode.isPresent()) {
            try (final PrintStream stream = new PrintStream(getHttpCodeFile(url, timestamp).toFile())) {
                stream.print(httpCode.get());            
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
                System.err.println("Error (" + e.toString() + ")while getting data from " + url);
            }            
        }
        
        if (error.isPresent()) {
            try (final PrintStream stream = new PrintStream(getErrorFile(url, timestamp).toFile())) {
                stream.print(error.get());            
            } catch (final FileNotFoundException e) {
                ExitHelper.exit(e);
            }            
        }
    }

    /**
     * @param url
     * @return the timestamps of the cached values (in reverse order, the first in the younger one)
     */
    List<Instant> getTimestampList(final URL url) {
        
        if (!Files.exists(getOutputDirectory(url))) {
            return new ArrayList<Instant>(0);
        }
        
        try {
            return Files.list(getOutputDirectory(url))
                        .filter(p -> p.toFile().isDirectory())
                        .map(p -> Instant.parse(p.getFileName().toString().replaceAll(";", ":")))
                        .sorted(Comparator.reverseOrder())
                        .collect(Collectors.toList());
        } catch (final IOException e) {
            ExitHelper.exit(e);
            return null;
        }
    }
 
    public SiteData retrieve(final URL url,
                             final Instant timestamp) {
        
        Status status;
        Optional<Integer> httpCode;
        Optional<Map<String, List<String>>> headers;
        Optional<File> dataFile;
        Optional<String> error;

        if (Files.exists(getStatusFile(url, timestamp))) {
            String str;
            try {
                str = Files.readString(getStatusFile(url, timestamp));
            } catch (final IOException e) {
                ExitHelper.exit(e);
                // unreachable
                str = null;
            }
            status = Status.valueOf(str);
        } else {
            ExitHelper.exit("status file " + getStatusFile(url, timestamp) + " does not exist");
            // unreachable
            status = Status.FAILURE;
        }

        if (Files.exists(getHttpCodeFile(url, timestamp))) {
            String str;
            try {
                str = Files.readString(getHttpCodeFile(url, timestamp));
            } catch (final IOException e) {
                ExitHelper.exit(e);
                // unreachable
                str = null;
            }
            Integer code;
            try {
                code = Integer.parseInt(str);
            } catch (final NumberFormatException e) {
                ExitHelper.exit(e);
                // unreachable
                code = null;
            }
            httpCode = Optional.of(code);
        } else {
            httpCode = Optional.empty();
        }

        if (Files.exists(getHeadersFile(url, timestamp))) {
            final Map<String, List<String>> map = new HashMap<String, List<String>>();
            try (Stream<String> lines = Files.lines(getHeadersFile(url, timestamp))) {
                lines.forEach(l -> {
                    final String[] s = l.split("\t");
                    final String header = s[0];
                    final List<String> list = new ArrayList<String>(s.length - 1);
                    for (int i = 1; i < s.length; i++) {
                        list.add(s[i]);
                    }
                    map.put(header, list);
                });
                headers = Optional.of(map);
            } catch (final IOException e) {
                ExitHelper.exit(e);
                // unreachable
                headers = Optional.empty();
            }
            dataFile = Optional.of(getDataFile(url, timestamp).toFile());
        } else {
            headers = Optional.empty();
        }

        if (Files.exists(getDataFile(url, timestamp))) {
            dataFile = Optional.of(getDataFile(url, timestamp).toFile());
        } else {
            dataFile = Optional.empty();
        }

        if (Files.exists(getErrorFile(url, timestamp))) {
            String str;
            try {
                str = Files.readString(getErrorFile(url, timestamp));
            } catch (final IOException e) {
                ExitHelper.exit(e);
                // unreachable
                str = null;
            }
            error = Optional.of(str);
        } else {
            error = Optional.empty();
        }
        
        return new SiteData(url, status, httpCode, headers, dataFile, error);
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

    Path getDataFile(final URL url,
                     final Instant timestamp) {
        return getOutputDirectory(url, timestamp).resolve("data");
    }

    private Path getErrorFile(final URL url,
                              final Instant timestamp) {
        return getOutputDirectory(url, timestamp).resolve("error");
    }

    private Path getOutputDirectory(final URL url,
                                    final Instant timestamp) {
        return getOutputDirectory(url).resolve(timestamp.toString().replaceAll(":", ";"));
    }

    private Path getOutputDirectory(final URL url) {
        return _path.resolve(url.getHost())
                    .resolve(FileHelper.generateFileNameFromURL(url));
    }
}
