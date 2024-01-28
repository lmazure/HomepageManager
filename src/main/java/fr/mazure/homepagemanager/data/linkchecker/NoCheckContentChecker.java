package fr.mazure.homepagemanager.data.linkchecker;

import java.time.Duration;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import fr.mazure.homepagemanager.utils.FileSection;
import fr.mazure.homepagemanager.utils.xmlparsing.ArticleData;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkData;

/**
 * Dummy link data checkercthat cjheck nothing (i.e. it does not report any violation)
 */
public class NoCheckContentChecker extends LinkContentChecker {

    /**
     * @param url URL of the link to check
     * @param linkData expected link data
     * @param articleData expected article data
     * @param file effective retrieved link data
     */
    public NoCheckContentChecker(final String url,
                                 final LinkData linkData,
                                 final Optional<ArticleData> articleData,
                                 final FileSection file) {
        super(url, linkData, articleData, file);
    }

    @Override
    protected LinkContentCheck checkGlobalData(final String data)
    {
        return null;
    }

    @Override
    protected LinkContentCheck checkLinkTitle(final String data,
                                              final String title)
    {
        return null;
    }

    @Override
    protected LinkContentCheck checkLinkSubtitles(final String data,
                                                  final String[] subtitles)
    {
        return null;
    }

    @Override
    protected LinkContentCheck checkLinkAuthors(final String data,
                                                final List<AuthorData> authors) throws ContentParserException
    {
        return null;
    }

    @Override
    protected LinkContentCheck checkLinkDuration(final String data,
                                                 final Duration duration)
    {
        return null;
    }

    @Override
    protected LinkContentCheck checkLinkLanguages(final String data,
                                                  final Locale[] languages)
    {
        return null;
    }

    @Override
    protected LinkContentCheck checkArticleDate(final String data,
                                                final Optional<TemporalAccessor> publicationDate,
                                                final Optional<TemporalAccessor> creationDate)
    {
        return null;
    }
}
