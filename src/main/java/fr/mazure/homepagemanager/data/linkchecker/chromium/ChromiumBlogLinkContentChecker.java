package fr.mazure.homepagemanager.data.linkchecker.chromium;

import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;

import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.LinkContentCheck;
import fr.mazure.homepagemanager.data.linkchecker.LinkContentChecker;
import fr.mazure.homepagemanager.data.violationcorrection.UpdateArticleTitleCorrection;
import fr.mazure.homepagemanager.utils.FileSection;
import fr.mazure.homepagemanager.utils.xmlparsing.ArticleData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkData;

/**
*
*/
public class ChromiumBlogLinkContentChecker extends LinkContentChecker {

    private ChromiumBlogLinkContentParser _parser;

    /**
     * @param url URL of the link to check
     * @param linkData expected link data
     * @param articleData expected article data
     * @param file effective retrieved link data
     */
    public ChromiumBlogLinkContentChecker(final String url,
                                          final LinkData linkData,
                                          final Optional<ArticleData> articleData,
                                          final FileSection file) {
        super(url, linkData, articleData, file);
    }

    @Override
    protected LinkContentCheck checkGlobalData(final String data) {
        _parser = new ChromiumBlogLinkContentParser(data);

        return null;
    }

    @Override
    public LinkContentCheck checkLinkTitle(final String data,
                                           final String title) throws ContentParserException {

        final String effectiveTitle = _parser.getTitle();

        if (!title.equals(effectiveTitle)) {
            return new LinkContentCheck("WrongTitle",
                                        "title \"" +
                                        title +
                                        "\" is not equal to the real title \"" +
                                        effectiveTitle +
                                        "\"",
                                        Optional.of(new UpdateArticleTitleCorrection(title, effectiveTitle, getUrl())));
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
                                        "Medium link with no creation date",
                                        Optional.empty());
        }

        if (!(creationDate.get() instanceof LocalDate)) {
            return new LinkContentCheck("IncorrectCreationDate",
                                        "Date without month or day",
                                        Optional.empty());
        }

        final LocalDate expectedDate = (LocalDate)creationDate.get();
        final LocalDate effectivePublishDate = _parser.getPublicationDate();

        if (!expectedDate.equals(effectivePublishDate)) {
            return new LinkContentCheck("WrongCreationDate",
                                        "expected date " +
                                        expectedDate +
                                        " is not equal to the effective publish date " +
                                        effectivePublishDate,
                                        Optional.empty());
       }

       return null;
    }
}
