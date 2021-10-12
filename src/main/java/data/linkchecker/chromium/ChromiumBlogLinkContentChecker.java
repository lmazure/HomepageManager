package data.linkchecker.chromium;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;

import data.linkchecker.ContentParserException;
import data.linkchecker.LinkContentCheck;
import data.linkchecker.LinkContentChecker;
import utils.xmlparsing.ArticleData;
import utils.xmlparsing.LinkData;

public class ChromiumBlogLinkContentChecker extends LinkContentChecker {

    private ChromiumBlogLinkContentParser _parser;

    public ChromiumBlogLinkContentChecker(final URL url,
                                          final LinkData linkData,
                                          final Optional<ArticleData> articleData,
                                          final File file) {
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
            return new LinkContentCheck("title \"" +
                                        title +
                                        "\" is not equal to the real title \"" +
                                        effectiveTitle +
                                          "\"");
        }

        return null;
    }

    @Override
    protected LinkContentCheck checkArticleDate(final String data,
                                                final Optional<TemporalAccessor> publicationDate,
                                                final Optional<TemporalAccessor> creationDate) throws ContentParserException
    {

        if (creationDate.isEmpty()) {
            return new LinkContentCheck("Medium link with no creation date");
        }

        if (!(creationDate.get() instanceof LocalDate)) {
            return new LinkContentCheck("Date without month or day");
        }

        final LocalDate expectedDate = (LocalDate)creationDate.get();
        final LocalDate effectivePublishDate = _parser.getPublicationDate();

        if (!expectedDate.equals(effectivePublishDate)) {
            return new LinkContentCheck("expected date " +
                                        expectedDate +
                                        " is not equal to the effective publish date " +
                                        effectivePublishDate);
       }

       return null;
    }
}