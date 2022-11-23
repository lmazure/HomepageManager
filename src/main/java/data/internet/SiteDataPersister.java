package data.internet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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

import utils.ExitHelper;
import utils.FileHelper;
import utils.FileSection;
import utils.Logger;
import utils.internet.UrlHelper;

/**
 * 
 *
 */
public class SiteDataPersister {

    private final Path _path;
    private static final int s_file_buffer_size = 8192;
    private static final int s_max_content_size = 8 * 1024 * 1024;

    private static final Charset UTF8_CHARSET = StandardCharsets.UTF_8;

    /**
     * @param path directory where the persistence files should be written
     */
    public SiteDataPersister(final Path path) {
        _path = path;
    }

    /**
     * @param siteData link data
     * @param dataStream stream to download the HTTP payload
     * @param timestamp timestamp of the visit
     */
    public void persist(final SiteDataDTO siteData,
                        final Optional<InputStream> dataStream,
                        final Instant timestamp) {
        persist(siteData.url(), siteData.status(), siteData.httpCode(), siteData.headers(), dataStream, siteData.error(), timestamp);
    }

    /**
     * @param url URL of the link to retrieve
     * @param status status SUCCESS/FAILURE
     * @param httpCode HTTP code, empty if the retrieval failed
     * @param headers HTTT header, empty if the retrieval failed
     * @param dataStream stream to download the HTTP payload
     * @param error error message describing why the information retrieval failed, empty if there is no error
     * @param timestamp timestamp of the visit
     */
    public void persist(final String url,
                        final SiteDataDTO.Status status,
                        final Optional<Integer> httpCode,
                        final Optional<Map<String, List<String>>> headers,
                        final Optional<InputStream> dataStream,
                        final Optional<String> error,
                        final Instant timestamp) {

        getOutputDirectory(url).toFile().mkdirs();

        final StringBuilder builder = new StringBuilder();

        builder.append(status).append('\n');
        if (httpCode.isPresent()) {
            builder.append("present\n");
            builder.append(httpCode.get()).append('\n');
        } else {
            builder.append("empty\n");
        }
        if (headers.isPresent()) {
            builder.append("present\n");
            final Map<String, List<String>> heads = headers.get();
            builder.append(heads.size()).append('\n');
            for (final String head : heads.keySet()) {
                builder.append(head);
                for (final String value : heads.get(head)) {
                    builder.append('\t').append(value);
                }
                builder.append('\n');
            }
        } else {
            builder.append("empty\n");
        }

        if (error.isPresent()) {
            builder.append("present\n");
            builder.append(error.get().lines().count()).append('\n');
            error.get().lines().forEach(l -> builder.append(l + "\n"));
        } else {
            builder.append("empty\n");
        }

        final String dataString = builder.toString();
        byte[] byteArrray = dataString.getBytes(UTF8_CHARSET);

        try (FileOutputStream fos = new FileOutputStream(getPersistedFile(url, timestamp))) {

            final String sz = String.format("%9d\n", Integer.valueOf(byteArrray.length + 10));
            fos.write(sz.getBytes(UTF8_CHARSET));
            fos.write(byteArrray);

            if (dataStream.isPresent()) {
                @SuppressWarnings("resource")
                final InputStream inputStream = isEncodedWithGzip(headers) ? new GZIPInputStream(dataStream.get())
                                                                           : dataStream.get();
                long size = 0L;
                final byte[] buffer = new byte[s_file_buffer_size];
                int length;
                while ((size <= s_max_content_size) && (length = inputStream.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                    size += length;
                }
                if (size > s_max_content_size) {
                    Logger.log(Logger.Level.WARN)
                          .append("retrieved content of ")
                          .append(url)
                          .append(" is truncated")
                          .submit();
                }
        }
        fos.flush();
        } catch (final IOException e) {
            Logger.log(Logger.Level.ERROR)
                  .append("Error (")
                  .append(e.toString())
                  .append(") while getting data from ")
                  .append(url.toString())
                  .submit();
        }
    }

    /**
     * @param url URL of the link to retrieve
     * @return timestamps of the cached visits (in reverse order, the first in the younger one)
     */
    public List<Instant> getTimestampList(final String url) {

        if (!Files.exists(getOutputDirectory(url))) {
            return new ArrayList<>(0);
        }

        try {
            return Files.list(getOutputDirectory(url))
                        .map(p -> Instant.parse(p.getFileName().toString().replaceAll(";", ":")))
                        .sorted(Comparator.reverseOrder())
                        .collect(Collectors.toList());
        } catch (final IOException e) {
            ExitHelper.exit(e);
            return null;
        }
    }

    /**
     * @param url URL of the link to retrieve
     * @param timestamp timestamp of the visit to retrieve
     * @return link data
     */
    public SiteData retrieve(final String url,
                             final Instant timestamp) {

        if (!Files.exists(getPersistedFile(url, timestamp).toPath())) {
            ExitHelper.exit("status file " + getPersistedFile(url, timestamp) + " does not exist");
        }

        SiteData.Status status = SiteData.Status.FAILURE;
        Optional<Integer> httpCode = Optional.empty();
        Optional<Map<String, List<String>>> headers = Optional.empty();
        Optional<String> error = Optional.empty();
        int size;

        final File statusFile = getPersistedFile(url, timestamp);
        try (final BufferedReader reader = new BufferedReader(new FileReader(statusFile))) {

            size = Integer.parseInt(reader.readLine().trim());

            status = SiteData.Status.valueOf(reader.readLine());

            final String httpCodePresence = reader.readLine();
            if (httpCodePresence.equals("present")) {
                httpCode = Optional.of(Integer.valueOf(Integer.parseInt(reader.readLine())));
            } else if (httpCodePresence.equals("empty")) {
                httpCode = Optional.empty();
            } else {
                throw new IllegalStateException("File " + statusFile + " is corrupted (bad HTTP code presence)");
            }

            final String headersPresence = reader.readLine();
            if (headersPresence.equals("present")) {
                final int nbHeaders = Integer.parseInt(reader.readLine());
                final Map<String, List<String>> map = new HashMap<>(nbHeaders);
                for (int i = 0; i < nbHeaders; i++) {
                    final String[] lineParts = reader.readLine().split("\t");
                    final String header = lineParts[0];
                    final List<String> list = new ArrayList<>(lineParts.length - 1);
                    for (int j = 1; j < lineParts.length; j++) {
                        list.add(lineParts[j]);
                    }
                    map.put(header, list);
                }
                headers = Optional.of(map);
            } else if (headersPresence.equals("empty")) {
                headers = Optional.empty();
            } else {
                throw new IllegalStateException("File " + statusFile + " is corrupted (bad HTTP headers)");
            }

            final String errorPresence = reader.readLine();
            if (errorPresence.equals("present")) {
                final int nbErrorLines = Integer.parseInt(reader.readLine());
                final StringBuilder strBuilder = new StringBuilder();
                for (int i = 0; i < nbErrorLines; i++) {
                    strBuilder.append(reader.readLine());
                }
                error = Optional.of(strBuilder.toString());
            } else if (errorPresence.equals("empty")) {
                error = Optional.empty();
            } else {
                throw new IllegalStateException("File " + statusFile + " is corrupted (bad error presence)");
            }

        } catch (final IOException e) {
            throw new IllegalStateException("Failure while reading " + statusFile, e);
        }

        final File dataFile = getPersistedFile(url, timestamp);
        final Optional<FileSection> dataFileSection = Optional.of(new FileSection(dataFile, size, dataFile.length() - size));

        return new SiteData(url, status, httpCode, headers, dataFileSection, error, null);
    }

    /**
     * @param url URL of the link to retrieve
     * @param timestamp timestamp of the visit to retrieve
     * @return file section containing the HTTP payload, empty if the retrieval failed
     */
    public FileSection getDataFileSection(final String url,
                                          final Instant timestamp) {
        final File statusFile = getPersistedFile(url, timestamp);
        try (final BufferedReader reader = new BufferedReader(new FileReader(statusFile))) {
            final int size = Integer.parseInt(reader.readLine().trim());
            return new FileSection(statusFile, size, statusFile.length() - size);
        } catch (final IOException e) {
            throw new IllegalStateException("Failure while reading " + statusFile, e);
        }
    }

    private File getPersistedFile(final String url,
                                  final Instant timestamp) {
        return getOutputDirectory(url).resolve(timestamp.toString().replaceAll(":", ";")).toFile();
    }

    private Path getOutputDirectory(final String url) {
        return _path.resolve(UrlHelper.getHost(url))
                    .resolve(FileHelper.generateFileNameFromURL(url));
    }

    private static boolean isEncodedWithGzip(final Optional<Map<String, List<String>>> headers) {

        if (!headers.isPresent()) {
            return false;
        }

        if (headers.get().containsKey("Content-Encoding")) {
            return headers.get().get("Content-Encoding").get(0).equals("gzip");
        }

        if (headers.get().containsKey("content-encoding")) {
            return headers.get().get("content-encoding").get(0).equals("gzip");
        }

        return false;
    }
}
