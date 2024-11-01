package fr.mazure.homepagemanager.data.linkchecker.youtubewatch;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever;
import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.ExtractorBasedLinkContentChecker;
import fr.mazure.homepagemanager.data.linkchecker.LinkContentCheck;
import fr.mazure.homepagemanager.data.violationcorrection.UpdateArticleDateCorrection;
import fr.mazure.homepagemanager.data.violationcorrection.UpdateLinkDateCorrection;
import fr.mazure.homepagemanager.data.violationcorrection.UpdateLinkDurationCorrection;
import fr.mazure.homepagemanager.data.violationcorrection.ViolationCorrection;
import fr.mazure.homepagemanager.utils.DateTimeHelper;
import fr.mazure.homepagemanager.utils.FileSection;
import fr.mazure.homepagemanager.utils.xmlparsing.ArticleData;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkData;

/**
 *
 */
public class YoutubeWatchLinkContentChecker extends ExtractorBasedLinkContentChecker {

    /**
     * @param url URL of the link to check
     * @param linkData expected link data
     * @param articleData expected article data
     * @param file effective retrieved link data
     * @param retriever data retriever
     */
    public YoutubeWatchLinkContentChecker(final String url,
                                          final LinkData linkData,
                                          final Optional<ArticleData> articleData,
                                          final FileSection file,
                                          final CachedSiteDataRetriever retriever) {
        super(url, linkData, articleData, file, (LinkDataExtractorBuilder)YoutubeWatchLinkContentParser::new, retriever);
    }

    /**
     * Determine if the link is managed
     *
     * @param url link
     * @return true if the link is managed
     */
    public static boolean isUrlManaged(final String url) {
        return YoutubeWatchLinkContentParser.isUrlManaged(url);
    }

    @Override
    protected LinkContentCheck checkGlobalData(final String data) throws ContentParserException {
        super.checkGlobalData(data);
        if (getYoutubeWatchLinkContentParser().isPrivate()) {
            return new LinkContentCheck("VideoNotPlayable",
                                        "video is private",
                                        Optional.empty());
        }
        if (!getYoutubeWatchLinkContentParser().isPlayable()) {
            return new LinkContentCheck("VideoNotPlayable",
                                        "video is not playable",
                                        Optional.empty());
        }

        return null;
    }

    @Override
    protected LinkContentCheck checkArticleAuthors(final String data,
                                                   final List<AuthorData> authors) throws ContentParserException
    {
        final List<AuthorData> missingAuthors = new ArrayList<>();
        for (final AuthorData author: getYoutubeWatchLinkContentParser().getSureAuthors()) {
            if (!authors.contains(author)) {
                missingAuthors.add(author);
            }
        }

        if (!missingAuthors.isEmpty()) {
            final String message = "The following authors are expected but are effectively missing: " + missingAuthors.stream().map(AuthorData::toString).collect(Collectors.joining(","));
            return new LinkContentCheck("WrongAuthors",
                                        message,
                                        Optional.empty());

        }

        return null;
    }

    @Override
    public LinkContentCheck checkLinkDuration(final String data,
                                              final Duration expectedDuration) throws ContentParserException {

        final Duration effectiveMinDuration = getYoutubeWatchLinkContentParser().getMinDuration().truncatedTo(ChronoUnit.SECONDS);
        final Duration effectiveMaxDuration = getYoutubeWatchLinkContentParser().getMaxDuration().truncatedTo(ChronoUnit.SECONDS).plusSeconds(1);

        if ((expectedDuration.compareTo(effectiveMinDuration) < 0) ||
            (expectedDuration.compareTo(effectiveMaxDuration) > 0)) {
            return new LinkContentCheck("WrongDuration",
                                        "expected duration " +
                                        expectedDuration +
                                        " is not in the real duration interval [" +
                                        effectiveMinDuration +
                                        "," +
                                        effectiveMaxDuration +
                                        "]",
                                        Optional.of(new UpdateLinkDurationCorrection(expectedDuration, effectiveMinDuration,getUrl())));
        }

        return null;
    }

    @Override
    protected LinkContentCheck checkArticleDate(final String data,
                                                final Optional<TemporalAccessor> publicationDate,
                                                final Optional<TemporalAccessor> creationDate) throws ContentParserException
    {
        if (creationDate.isEmpty() && publicationDate.isEmpty()) {
            return new LinkContentCheck("MissingDate",
                                        "YouTube link with neither a creation date nor a publication date",
                                        Optional.empty());
        }

        final LocalDate effectiveDate = DateTimeHelper.convertTemporalAccessorToLocalDate(getYoutubeWatchLinkContentParser().getDate().get()).get();

        if (publicationDate.isPresent()) {
            // publication date is present, we used it
            if (!(publicationDate.get() instanceof final LocalDate expectedDate)) {
                return new LinkContentCheck("IncorrectDate",
                                            "Date without month or day",
                                            Optional.empty());
            }
            if (!expectedDate.equals(effectiveDate)) {
                final ViolationCorrection correction = new UpdateLinkDateCorrection(expectedDate,
                                                                                    effectiveDate,
                                                                                    getUrl());
                return new LinkContentCheck("WrongDate",
                                            "expected publication date " +
                                            expectedDate +
                                            " is not equal to the effective date " +
                                            effectiveDate,
                                            Optional.of(correction));
           }
        } else {
            // publication date is absent, we used creation date instead
            if (!(creationDate.get() instanceof final LocalDate expectedDate)) {
                return new LinkContentCheck("IncorrectDate",
                                            "Date without month or day",
                                            Optional.empty());
            }
            if (!expectedDate.equals(effectiveDate)) {
                final ViolationCorrection correction = new UpdateArticleDateCorrection(expectedDate,
                                                                                       effectiveDate,
                                                                                       getUrl());
                return new LinkContentCheck("WrongDate",
                                            "expected creation date " +
                                            expectedDate +
                                            " is not equal to the effective date " +
                                            effectiveDate,
                                            Optional.of(correction));
           }
       }

       return null;
    }

    @Override
    protected LinkContentCheck checkLinkLanguages(final String data,
                                                  final Locale[] expectedLanguages) throws ContentParserException
    {
        final Locale effectiveLanguage = getYoutubeWatchLinkContentParser().getLanguage();

        return checkLinkLanguagesHelper(effectiveLanguage, expectedLanguages);
    }

    private YoutubeWatchLinkContentParser getYoutubeWatchLinkContentParser() {
        return (YoutubeWatchLinkContentParser)getParser();
    }
}
