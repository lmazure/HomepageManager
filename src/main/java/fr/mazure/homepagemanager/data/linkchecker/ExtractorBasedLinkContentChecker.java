package fr.mazure.homepagemanager.data.linkchecker;

import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever;
import fr.mazure.homepagemanager.data.violationcorrection.AddLinkSubtitleCorrection;
import fr.mazure.homepagemanager.data.violationcorrection.RemoveLinkSubtitleCorrection;
import fr.mazure.homepagemanager.data.violationcorrection.UpdateArticleDateCorrection;
import fr.mazure.homepagemanager.data.violationcorrection.UpdateLinkSubtitleCorrection;
import fr.mazure.homepagemanager.data.violationcorrection.UpdateLinkTitleCorrection;
import fr.mazure.homepagemanager.data.violationcorrection.ViolationCorrection;
import fr.mazure.homepagemanager.utils.DateTimeHelper;
import fr.mazure.homepagemanager.utils.FileSection;
import fr.mazure.homepagemanager.utils.StringHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.ArticleData;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkData;

/**
 *
 */
public class ExtractorBasedLinkContentChecker extends LinkContentChecker {

    private final LinkDataExtractorBuilder _extractorBuilder;
    private LinkDataExtractor _parser;
    private static final LinkDataExtractorCache s_cache = new LinkDataExtractorCache();

    /**
     * @param url URL of the link to check
     * @param linkData expected link data
     * @param articleData expected article data
     * @param file effective retrieved link data
     * @param extractorBuilder function that returns a link data extractor
     * @param retriever data retriever
     */
    public ExtractorBasedLinkContentChecker(final String url,
                                            final LinkData linkData,
                                            final Optional<ArticleData> articleData,
                                            final FileSection file,
                                            final LinkDataExtractorBuilder extractorBuilder,
                                            final CachedSiteDataRetriever retriever) {
        super(url, linkData, articleData, file, retriever);
        _extractorBuilder = extractorBuilder;
    }

    protected LinkDataExtractor getParser() {
        return _parser;
    }

    @Override
    protected LinkContentCheck checkGlobalData(final String data) throws ContentParserException
    {
        _parser = s_cache.query(getUrl());
        if (_parser == null) {
            _parser = _extractorBuilder.buildExtractor(getUrl(), data, getRetriever());
            s_cache.store(getUrl(), _parser);
        }
        return null;
    }

    @Override
    protected LinkContentCheck checkLinkTitle(final String data,
                                              final String title) throws ContentParserException
    {
        final String effectiveTitle = _parser.getTitle();

        final String diff = StringHelper.compareAndExplainDifference(title, effectiveTitle);
        if (diff != null) {
            return new LinkContentCheck("WrongTitle",
                                        "title \"" +
                                        title +
                                        "\" is not equal to the real title \"" +
                                        effectiveTitle +
                                          "\"\n" +
                                        diff,
                                        Optional.of(new UpdateLinkTitleCorrection(title, effectiveTitle, getUrl())));
        }

        return null;
    }

    @Override
    protected LinkContentCheck checkLinkSubtitles(final String data,
                                                  final String[] expectedSubtitles) throws ContentParserException
    {
        if (expectedSubtitles.length > 1) {
            return new LinkContentCheck("MultipleSubtitles",
                                        "More than one subtitle is not supported yet",
                                        Optional.empty());  // TODO implement support of several subtitles
        }

        final Optional<String> effectiveSubtitle = _parser.getSubtitle();

        if (effectiveSubtitle.isEmpty()) {
            if (expectedSubtitles.length != 0 ) {
                return new LinkContentCheck("WrongSubtitle",
                                            "subtitle \"" +
                                            expectedSubtitles[0] +
                                            "\" should not be present",
                                            Optional.of(new RemoveLinkSubtitleCorrection(expectedSubtitles[0], getUrl())));
            }
            return null;
        }

        if (expectedSubtitles.length == 0) {
            return new LinkContentCheck("MissingSubtitle",
                                        "the subtitle \"" +
                                        effectiveSubtitle.get() +
                                        "\" is missing",
                                        Optional.of(new AddLinkSubtitleCorrection(effectiveSubtitle.get(), getUrl())));
        }

        if (!expectedSubtitles[0].equals(effectiveSubtitle.get())) {
            return new LinkContentCheck("WrongSubtitle",
                                        "subtitle \"" +
                                        expectedSubtitles[0] +
                                        "\" is not equal to the real subtitle \"" +
                                        effectiveSubtitle.get() +
                                        "\"",
                                        Optional.of(new UpdateLinkSubtitleCorrection(expectedSubtitles[0], effectiveSubtitle.get(), getUrl())));
        }

        return null;
    }

    @Override
    protected LinkContentCheck checkLinkLanguages(final String data,
                                                  final Locale[] languages) throws ContentParserException
    {
        if ((languages.length != 1) || (languages[0] != _parser.getLanguage())) {
            return new LinkContentCheck("WrongLanguage",
                                        "Article should have the language set as " + _parser.getLanguage(),
                                        Optional.empty());
        }

        return null;
    }

    @Override
    protected LinkContentCheck checkArticleDate(final String data,
                                                final Optional<TemporalAccessor> publicationDate,
                                                final Optional<TemporalAccessor> creationDate) throws ContentParserException
    {
        if (creationDate.isEmpty()) {
            return new LinkContentCheck("MissingCreationDate",
                                        "Article should have a creation date",
                                        Optional.empty());
        }
        final TemporalAccessor date =  publicationDate.isPresent() ? publicationDate.get()
                                                                   : creationDate.get();

        final LocalDate effectiveDate = DateTimeHelper.convertTemporalAccessorToLocalDate(_parser.getPublicationDate().get());

        if (!date.equals(effectiveDate)) {
            final Optional<ViolationCorrection> correction = Optional.of(new UpdateArticleDateCorrection(date, effectiveDate, getUrl()));
            return new LinkContentCheck("WrongDate",
                                        "The expected date " +
                                        date +
                                        " is not equal to the effective date " +
                                        effectiveDate,
                                        correction);
        }

        return null;
    }

    @Override
    protected LinkContentCheck checkArticleAuthors(final String data,
                                                   final List<AuthorData> expectedAuthors) throws ContentParserException
    {
        final List<AuthorData> effectiveAuthor = _parser.getSureAuthors();

        return simpleCheckLinkAuthors(effectiveAuthor, expectedAuthors);
    }

    @Override
    protected LinkContentCheck checkArticleLinks(final String data,
                                                 final List<LinkData> expectedLinks) throws ContentParserException
    {
        final List<ExtractedLinkData> effectiveLinks = _parser.getLinks();

        // if we are currently checking a link which is not the first one, we check nothing
        if ((expectedLinks.size() > 1) && !expectedLinks.get(0).getUrl().equals(getUrl())) {
	        return null;
        }

        if (effectiveLinks.size() != expectedLinks.size()) {
            final String expectedLinksMsg = expectedLinks.stream()
                                                         .map(LinkData::getUrl)
                                                         .collect(Collectors.joining(", "));
            final String effectiveLinksMsg = effectiveLinks.stream()
                                                           .map(ExtractedLinkData::url)
                                                           .collect(Collectors.joining(", "));
            return new LinkContentCheck("WrongLinkCount",
                                        "Expected " + expectedLinks.size() + " links (" + expectedLinksMsg + "), but got " + effectiveLinks.size() + " links (" + effectiveLinksMsg + ")",
                                        Optional.empty());
        }

        for (int i = 0; i < effectiveLinks.size(); i++) {
            if (!effectiveLinks.get(i).url().equals(expectedLinks.get(i).getUrl())) {
                return new LinkContentCheck("WrongLink",
                                            "Expected link number " + (i + 1) + " to be " + expectedLinks.get(i).getUrl() + ", but got " + effectiveLinks.get(i).url(),
                                            Optional.empty());
            }
        }

        return null;
    }

    @FunctionalInterface
    protected interface LinkDataExtractorBuilder {
        LinkDataExtractor buildExtractor(final String url, final String data, final CachedSiteDataRetriever retriever) throws ContentParserException;
    }
}
