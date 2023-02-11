package fr.mazure.homepagemanager.utils.internet.youtube;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import fr.mazure.homepagemanager.utils.ExitHelper;

/**
 *
 */
public class CachedYoutubeApi {

    private final Path _cachePath;
    private final YoutubeApi _api;

    /**
     * @param applicationName
     * @param apiKey
     * @param referenceRegion
     * @param cachePath
     */
    public CachedYoutubeApi(final String applicationName,
                            final String apiKey,
                            final String referenceRegion,
                            final Path cachePath) {
        _cachePath = cachePath.resolve("youtube_api_cache");
        _api = new YoutubeApi(applicationName, apiKey, referenceRegion);
    }

    /**
     * @param videoId
     * @return
     */
    public YoutubeVideoDto getData(final String videoId) {
        if (getVideoOutputFile(videoId).toFile().exists()) {
            return deserialiseVideoDto(videoId);
        }
        final YoutubeVideoDto dto = _api.getData(videoId);
        serialiseVideoDto(videoId, dto);
        return dto;
    }

    /**
     * @param videoIds
     * @return
     */
    public List<YoutubeVideoDto> getData(final List<String> videoIds) {
        final List<YoutubeVideoDto> dtos = new ArrayList<>();
        for (String videoId: videoIds) {
            dtos.add(getData(videoId));
        }
        return dtos;
    }

    private void serialiseVideoDto(final String videoId,
                                   final YoutubeVideoDto dto) {
        getVideoOutputDir().toFile().mkdirs();
        try (final FileOutputStream file = new FileOutputStream(getVideoOutputFile(videoId).toFile());
             final ObjectOutputStream oos = new ObjectOutputStream(file)) {
            oos.writeObject(dto);
            oos.flush();
        } catch (final IOException e) {
            ExitHelper.exit(e);
        }
    }

    private YoutubeVideoDto deserialiseVideoDto(final String videoId) {
        try (final FileInputStream file = new FileInputStream(getVideoOutputFile(videoId).toFile());
             final ObjectInputStream ois = new ObjectInputStream(file)) {
            return (YoutubeVideoDto)ois.readObject();
           } catch (final IOException | ClassNotFoundException e) {
               ExitHelper.exit(e);
               // NOT REACHED
               return null;
           }
       }

    private Path getVideoOutputFile(final String videoId) {
        return getVideoOutputDir().resolve(videoId + ".ser");
    }

    private Path getVideoOutputDir() {
        return _cachePath.resolve("video");
    }
}
