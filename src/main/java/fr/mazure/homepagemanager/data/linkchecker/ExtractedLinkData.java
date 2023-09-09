package fr.mazure.homepagemanager.data.linkchecker;

import java.time.Duration;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;
import java.util.Optional;

import fr.mazure.homepagemanager.utils.xmlparsing.LinkFormat;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkProtection;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkStatus;

/**
 * link data
 *
 * @param title tilte
 * @param subtitles subtitles
 * @param url URL
 * @param status status
 * @param protection protection
 * @param formats formats
 * @param languages languages
 * @param duration duration
 * @param publicationDate publication date
 *
 */
public record ExtractedLinkData (String title,
                                 String[] subtitles,
                                 String url,
                                 Optional<LinkStatus> status,
                                 Optional<LinkProtection> protection,
                                 LinkFormat[] formats,
                                 Locale[] languages,
                                 Optional<Duration> duration,
                                 Optional<TemporalAccessor> publicationDate) {
    // EMPTY
}
