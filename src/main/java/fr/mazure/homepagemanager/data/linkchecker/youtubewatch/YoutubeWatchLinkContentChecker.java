package fr.mazure.homepagemanager.data.linkchecker.youtubewatch;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.ExtractorBasedLinkContentChecker;
import fr.mazure.homepagemanager.data.linkchecker.LinkContentCheck;
import fr.mazure.homepagemanager.utils.FileSection;
import fr.mazure.homepagemanager.utils.xmlparsing.ArticleData;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkData;

/**
*
*/
public class YoutubeWatchLinkContentChecker extends ExtractorBasedLinkContentChecker {

    private YoutubeWatchLinkContentParserNew _parser;

    /**
     * @param url URL of the link to check
     * @param linkData expected link data
     * @param articleData expected article data
     * @param file effective retrieved link data
     */
    public YoutubeWatchLinkContentChecker(final String url,
                                          final LinkData linkData,
                                          final Optional<ArticleData> articleData,
                                          final FileSection file) {
        super(url, linkData, articleData, file, (LinkDataExtractorBuilder)YoutubeWatchLinkContentParserNew::new);
    }

    @Override
    protected LinkContentCheck checkGlobalData(final String data) throws ContentParserException {
        super.checkGlobalData(data);
        _parser = (YoutubeWatchLinkContentParserNew)(getParser()); // TODO cleanup this crap
        if (!_parser.isPlayable()) {
            return new LinkContentCheck("VideoNotPlayable",
                                        "video is not playable",
                                        Optional.empty());
        }

        return null;
    }

    @Override
    protected LinkContentCheck checkLinkAuthors(final String data,
                                                final List<AuthorData> authors)
    {
        return null; //TODO We will have to check YT authors sometime in the future
    }

    @Override
    public LinkContentCheck checkLinkDuration(final String data,
                                              final Duration expectedDuration) throws ContentParserException {

        final Duration effectiveMinDuration = _parser.getMinDuration().truncatedTo(ChronoUnit.SECONDS);
        final Duration effectiveMaxDuration = _parser.getMaxDuration().truncatedTo(ChronoUnit.SECONDS).plusSeconds(1);

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
                                        Optional.empty());
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
                                        "YouTube link with neither creation date not publication date",
                                        Optional.empty());
        }

        final TemporalAccessor date = publicationDate.isPresent() ? publicationDate.get() : creationDate.get();

        if (!(date instanceof LocalDate)) {
            return new LinkContentCheck("IncorrectDate",
                                        "Date without month or day",
                                        Optional.empty());
       }

        final LocalDate expectedDate = (LocalDate)date;
        final LocalDate effectivePublishDate = _parser.getPublishDateInternal();
        final LocalDate effectiveUploadDate = _parser.getUploadDateInternal();

        if (!expectedDate.equals(effectivePublishDate)) {
            return new LinkContentCheck("WrongDate",
                                        "expected date " +
                                        expectedDate +
                                        " is not equal to the effective publish date " +
                                        effectivePublishDate,
                                        Optional.empty());
       }

       if (!expectedDate.equals(effectiveUploadDate)) {
            return new LinkContentCheck("WrongDate",
                                        "expected date " +
                                        expectedDate +
                                        " is not equal to the effective upload date " +
                                        effectivePublishDate,
                                        Optional.empty());
       }

       return null;
    }

    @Override
    protected LinkContentCheck checkLinkLanguages(final String data,
                                                  final Locale[] expectedLanguages) throws ContentParserException
    {
        final Locale effectiveLanguage = _parser.getLanguage();

        return checkLinkLanguagesHelper(effectiveLanguage, expectedLanguages);
    }
}
