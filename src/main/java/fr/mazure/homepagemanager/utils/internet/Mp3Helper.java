package fr.mazure.homepagemanager.utils.internet;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagException;

import fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever;
import fr.mazure.homepagemanager.data.dataretriever.FullFetchedLinkData;
import fr.mazure.homepagemanager.utils.ExitHelper;
import fr.mazure.homepagemanager.utils.FileHelper;
import fr.mazure.homepagemanager.utils.FileSection;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

/**
 * Helper to manage MP3 URLs
 */
public class Mp3Helper {

    private Duration _duration;
    /**
     * Determine the duration of an MP3 file
     *
     * @param mp3Url URL of the MP3 file
     * @param retriever data retriever
     *
     * @return the duration
     */
    public Duration getMp3Duration(final String mp3Url,
                                   final CachedSiteDataRetriever retriever) {
        retriever.retrieve(mp3Url, this::foobar, false);
        return _duration;
    }

    private void foobar(final FullFetchedLinkData data) {
        try {
            final File tempFile = File.createTempFile("temp", ".mp3");
            final FileSection fileSection = data.dataFileSection().get();
            FileHelper.writeFileSectionToFile(fileSection, tempFile);
            final MP3File mp3File = (MP3File) AudioFileIO.read(tempFile);
            double durationInSeconds = mp3File.getAudioHeader().getTrackLength();
            _duration = Duration.ofMillis((long)(durationInSeconds * 1000));
            tempFile.delete();
        } catch (final IOException e) {
            ExitHelper.exit("Failed to create temporary MP3 file", e);
        } catch (final InvalidAudioFrameException | ReadOnlyFileException | CannotReadException | TagException e) {
            ExitHelper.exit("Failed to parse temporary MP3 file", e);
        }
    }
}