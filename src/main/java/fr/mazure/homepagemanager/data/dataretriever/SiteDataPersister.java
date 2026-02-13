package fr.mazure.homepagemanager.data.dataretriever;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.zip.GZIPInputStream;

import fr.mazure.homepagemanager.utils.ExitHelper;
import fr.mazure.homepagemanager.utils.FileNameHelper;
import fr.mazure.homepagemanager.utils.FileSection;
import fr.mazure.homepagemanager.utils.Logger;
import fr.mazure.homepagemanager.utils.internet.HttpHelper;
import fr.mazure.homepagemanager.utils.internet.UriHelper;

/**
 *
 */
public class SiteDataPersister {

    private final Path _path;

    private static final int s_file_buffer_size = 8192;
    private static final long s_max_content_size = 512L * 1024L * 1024L;
    private static final Charset s_charset_utf8 = StandardCharsets.UTF_8;
    private static final String s_effectiveFileNamePrefix = "cache_";
    private static final String s_tempoFileNamePrefix = "temp_";

    /**
     * @param path directory where the persistence files should be written
     */
    public SiteDataPersister(final Path path) {
        _path = path;
    }

    /**
     * @param siteData link data
     * @param dataStream stream to download the HTTP payload
     * @param error error description, empty if no error
     */
    public void persist(final HeaderFetchedLinkData siteData,
                        final Optional<InputStream> dataStream,
                        final Optional<String> error) {

        getOutputDirectory(siteData.url()).toFile().mkdirs();

        final List<byte[]> byteArrays = new LinkedList<>();
        int sumOfSizes = 0;

        HeaderFetchedLinkData data = siteData;
        while (data != null) {
            final String dataString = buildSerializedHeaderString(data);
            final byte[] byteArray = dataString.getBytes(s_charset_utf8);
            byteArrays.add(byteArray);
            sumOfSizes += byteArray.length;
            data = data.previousRedirection();
        }

        final String dataErrorString = buildSerializedErrorString(error);
        final byte[] byteErrorArray = dataErrorString.getBytes(s_charset_utf8);
        sumOfSizes += byteErrorArray.length;

        final File tempoFile = getTempoPersistedFile(siteData.url());
        final File effectiveFile = getEffectivePersistedFile(siteData.url());
        try (final FileOutputStream fos = new FileOutputStream(tempoFile)) {
            final String siz = String.format("%9d\n", Integer.valueOf(sumOfSizes + 20));
            fos.write(siz.getBytes(s_charset_utf8));
            final String numberOfRedirections = String.format("%9d\n", Integer.valueOf(byteArrays.size()));
            fos.write(numberOfRedirections.getBytes(s_charset_utf8));
            for (final byte[] byteArray: byteArrays) {
                fos.write(byteArray);
            }
            fos.write(byteErrorArray);

            if (dataStream.isPresent()) {
                @SuppressWarnings("resource")
                final InputStream inputStream = isEncodedWithGzip(getHeadersOfLastRedirection(siteData)) ? new GZIPInputStream(dataStream.get())
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
                          .append(siteData.url())
                          .append(" is truncated")
                          .submit();
                }
            }
            fos.flush();
        } catch (final IOException e) {
            Logger.log(Logger.Level.ERROR)
                  .appendln("Error")
                  .append(e)
                  .append("while getting data from ")
                  .append(siteData.url())
                  .submit();
        }

        try {
            Files.move(tempoFile.toPath(), effectiveFile.toPath());
        } catch (final FileAlreadyExistsException _) {
            Logger.log(Logger.Level.WARN)
                  .append("Do not rename file into ")
                  .append(effectiveFile.toPath())
                  .append(", that one already exists")
                  .submit();
        } catch (final IOException e) {
            ExitHelper.exit(e);
        }
    }

    private static String buildSerializedHeaderString(final HeaderFetchedLinkData siteData) {

        final StringBuilder builder = new StringBuilder();

        final String url = siteData.url();
        builder.append(url + "\n");

        final Optional<Map<String, List<String>>> headers = siteData.headers();
        if (headers.isPresent()) {
            builder.append("present\n");
            final Map<String, List<String>> heads = headers.get();
            builder.append(heads.size()).append('\n');
            for (final Map.Entry<String, List<String>> entry : heads.entrySet()) {
                builder.append(entry.getKey());
                for (final String value : entry.getValue()) {
                    builder.append('\t').append(value);
                }
                builder.append('\n');
            }
        } else {
            builder.append("empty\n");
        }

        return builder.toString();
    }

    private static String buildSerializedErrorString(final Optional<String> error) {

        final StringBuilder builder = new StringBuilder();

        if (error.isPresent()) {
            builder.append("present\n");
            builder.append(error.get().lines().count()).append('\n');
            error.get().lines().forEach(l -> builder.append(l + "\n"));
        } else {
            builder.append("empty\n");
        }

        return builder.toString();
    }

    /**
     * @param url URL of the link to retrieve
     * @return link data
     */
    public FullFetchedLinkData retrieve(final String url) {

        final File file = getEffectivePersistedFile(url);

        if (!Files.exists(file.toPath())) {
            return null;
        }

        try (final FileInputStream fileInputStream = new FileInputStream(file);
             final BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream))) {

            final int size = Integer.parseInt(reader.readLine().trim());
            final int numberOfRedirections = Integer.parseInt(reader.readLine().trim());

            final List<HeaderFetchedLinkData> redirectionsDatas = new LinkedList<>();
            for (int i = 0; i < numberOfRedirections; i++) {
                final HeaderFetchedLinkData d = readOneRedirection(reader);
                redirectionsDatas.add(d);
            }

            Optional<String> error = Optional.empty();
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
                throw new IllegalStateException("File is corrupted (bad error presence)");
            }

            HeaderFetchedLinkData lastRedirectionData = null;
            for (int i = numberOfRedirections - 1; i >= 0; i--) {
                final HeaderFetchedLinkData d = redirectionsDatas.get(i);
                lastRedirectionData = new HeaderFetchedLinkData(d.url(), d.headers(), lastRedirectionData);
            }

            assert lastRedirectionData != null;
            assert lastRedirectionData.url().equals(url);

            return new FullFetchedLinkData(lastRedirectionData.url(),
                                           lastRedirectionData.headers(),
                                           Optional.of(new FileSection(file, size, file.length() - size)),
                                           error,
                                           lastRedirectionData.previousRedirection());
        } catch (final Exception e) {
            throw new IllegalStateException("Failure while reading " + file, e);
        }
    }

    private static HeaderFetchedLinkData readOneRedirection(final BufferedReader reader) throws IOException {

        final String url = reader.readLine();

        Optional<Map<String, List<String>>> headers;
        final String headersPresence = reader.readLine();
        if (headersPresence.equals("present")) {
            final int nbHeaders = Integer.parseInt(reader.readLine());
            final Map<String, List<String>> map = HashMap.newHashMap(nbHeaders);
            for (int i = 0; i < nbHeaders; i++) {
                final String[] lineParts = reader.readLine().split("\t");
                final String header = lineParts[0].equals("null") ? null : lineParts[0];
                final List<String> list = new ArrayList<>(Arrays.asList(lineParts).subList(1, lineParts.length));   
                map.put(header, list);
            }
            headers = Optional.of(map);
        } else if (headersPresence.equals("empty")) {
            headers = Optional.empty();
        } else {
            throw new IllegalStateException("File is corrupted (bad HTTP headers)");
        }

        return new HeaderFetchedLinkData(url, headers, null);
    }

    /**
     * @param url URL of the link to retrieve
     * @return file section containing the HTTP payload, empty if the retrieval failed
     */
    public FileSection getDataFileSection(final String url) {
        final File statusFile = getEffectivePersistedFile(url);
        try (final BufferedReader reader = new BufferedReader(new FileReader(statusFile))) {
            final int size = Integer.parseInt(reader.readLine().trim());
            return new FileSection(statusFile, size, statusFile.length() - size);
        } catch (final IOException e) {
            throw new IllegalStateException("Failure while reading " + statusFile, e);
        }
    }

    private File getTempoPersistedFile(final String url) {
        return getPersistedFile(url, s_tempoFileNamePrefix + UUID.randomUUID());
    }

    private File getEffectivePersistedFile(final String url) {
        return getPersistedFile(url, s_effectiveFileNamePrefix );
    }

    private File getPersistedFile(final String url,
                                   final String prefix) {
        return getOutputDirectory(url).resolve(FileNameHelper.generateFileNameFromURL(prefix, url))
                                      .toFile();
    }

    private Path getOutputDirectory(final String url) {
        final String host = UriHelper.isValidUri(url) ? UriHelper.getHost(url)
                                                      : "_invalid_URL";
        return _path.resolve(FileNameHelper.generateDirectoryNameFromHostName(host));
    }

    private static Optional<Map<String, List<String>>> getHeadersOfLastRedirection(final HeaderFetchedLinkData siteData) {
        HeaderFetchedLinkData s = siteData;
        while (s.previousRedirection() != null) {
            s = s.previousRedirection();
        }
        return s.headers();
    }

    private static boolean isEncodedWithGzip(final Optional<Map<String, List<String>>> headers) {

        if (!headers.isPresent()) {
            return false;
        }

        return HttpHelper.isEncodedWithGzip(headers.get());
    }
}
