package data.internet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
import java.util.zip.GZIPInputStream;

import data.internet.SiteData.Status;
import utils.ExitHelper;
import utils.FileHelper;
import utils.Logger;

public class SiteDataPersister {

    private final Path _path;
    private static final int s_file_buffer_size = 8192;
    private static final int s_max_content_size = 8 * 1024 * 1024;

    public SiteDataPersister(final Path path) {
        _path = path;
    }

    public void persist(final URL url,
                        final Instant timestamp,
                        final Status status,
                        final Optional<Integer> httpCode,
                        final Optional<Map<String, List<String>>> headers,
                        final Optional<InputStream> dataStream,
                        final Optional<String> error) {
        
        getOutputDirectory(url, timestamp).toFile().mkdirs();

        try (final PrintStream stream = new PrintStream(getStatusFile(url, timestamp).toFile())) {

            {
                stream.println(status);
            }

            {
                if (httpCode.isPresent()) {
                    stream.println("present");
                    stream.println(httpCode.get());
                } else {
                    stream.println("empty");                      
                }
            }

            {
                if (headers.isPresent()) {
                    stream.println("present");
                    final Map<String, List<String>> heads = headers.get();
                    stream.println(heads.size());
                    for (final String head : heads.keySet()) {
                        stream.print(head);
                        for (final String value : heads.get(head)) {
                            stream.print('\t');
                            stream.print(value);
                        }
                        stream.println();
                    }                
                } else {
                    stream.println("empty");                      
                }
            }

            {
                if (error.isPresent()) {
                    stream.println("present");      
                    stream.println(error.get().lines().count());
                    error.get().lines().forEach(l -> stream.println(l));
                } else {
                    stream.println("empty");                      
                }
            }

        } catch (final FileNotFoundException e) {
            ExitHelper.exit(e);
        }
                
        if (dataStream.isPresent()) {
            final boolean gzip = headers.isPresent() &&
             		             headers.get().containsKey("Content-Encoding") &&
             		             headers.get().get("Content-Encoding").get(0).equals("gzip");
    		try (final InputStream inputStream = gzip ? new GZIPInputStream(dataStream.get()) : dataStream.get();
    		     final PrintStream outputStream = new PrintStream(getDataFile(url, timestamp).toFile())) {
                long size = 0L;
                final byte[] buffer = new byte[s_file_buffer_size];
                int length;
                while ((size <= s_max_content_size) && (length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                    size += length;
                }
                if (size > s_max_content_size) {
                	Logger.log(Logger.Level.WARN)
          	              .append("retrieved content of ")
                          .append(url)
                          .append(" is truncated")
                          .submit();
                }
            } catch (final IOException e) {
            	Logger.log(Logger.Level.ERROR)
            	      .append("Error (")
                      .append(e.toString())
                      .append(") while getting data from ")
                      .append(url.toString())
                      .submit();
            }            
        }        
    }

    /**
     * @param url
     * @return the timestamps of the cached values (in reverse order, the first in the younger one)
     */
    public List<Instant> getTimestampList(final URL url) {
        
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
        
        if (!Files.exists(getStatusFile(url, timestamp))) {
            ExitHelper.exit("status file " + getStatusFile(url, timestamp) + " does not exist");
        }

        Status status = Status.FAILURE;
        Optional<Integer> httpCode = Optional.empty();
        Optional<Map<String, List<String>>> headers = Optional.empty();
        Optional<String> error = Optional.empty();

        try (final BufferedReader r = new BufferedReader(new FileReader(getStatusFile(url, timestamp).toFile()))) {

            {
                status = Status.valueOf(r.readLine());
            }
            
            {
                final String httpCodePresence = r.readLine();
                if (httpCodePresence.equals("present")) {
                    httpCode = Optional.of(Integer.parseInt(r.readLine()));
                } else if (httpCodePresence.equals("empty")) {
                    httpCode = Optional.empty();
                } else {
                    ExitHelper.exit("Corrupted file");
                }
            }

            {
                final String headersPresence = r.readLine();
                if (headersPresence.equals("present")) {
                    final int nbHeaders = Integer.parseInt(r.readLine());
                    final Map<String, List<String>> map = new HashMap<String, List<String>>(nbHeaders);
                    for (int i = 0; i < nbHeaders; i++) {
                        final String[] lineParts = r.readLine().split("\t");
                        final String header = lineParts[0];
                        final List<String> list = new ArrayList<String>(lineParts.length - 1);
                        for (int j = 1; j < lineParts.length; j++) {
                            list.add(lineParts[j]);
                        }
                        map.put(header, list);
                    }
                    headers = Optional.of(map);
                } else if (headersPresence.equals("empty")) {
                    headers = Optional.empty();
                } else {
                    ExitHelper.exit("Corrupted file");
                }
            }

            {
                final String errorPresence = r.readLine();
                if (errorPresence.equals("present")) {
                    final int nbErrorLines = Integer.parseInt(r.readLine());
                    final StringBuilder strBuilder = new StringBuilder();
                    for (int i = 0; i < nbErrorLines; i++) {
                        strBuilder.append(r.readLine());
                    }
                    error = Optional.of(strBuilder.toString());
                } else if (errorPresence.equals("empty")) {
                    error = Optional.empty();
                } else {
                    ExitHelper.exit("Corrupted file");
                }
            }

        } catch (final IOException e) {
            ExitHelper.exit(e);
        }

        final Optional<File> dataFile = Optional.of(getDataFile(url, timestamp).toFile());

        return new SiteData(url, status, httpCode, headers, dataFile, error);
    }
    
    private Path getStatusFile(final URL url,
                               final Instant timestamp) {
        return getOutputDirectory(url, timestamp).resolve("status");
    }

    Path getDataFile(final URL url,
                     final Instant timestamp) {
        return getOutputDirectory(url, timestamp).resolve("data");
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
