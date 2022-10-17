package data.linkchecker.oracleblogs;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import data.linkchecker.ContentParserException;
import data.linkchecker.LinkContentCheck;
import data.linkchecker.LinkContentChecker;
import utils.FileSection;
import utils.xmlparsing.ArticleData;
import utils.xmlparsing.AuthorData;
import utils.xmlparsing.LinkData;

public class OracleBlogsLinkContentChecker extends LinkContentChecker {

    private OracleBlogsLinkContentParser _parser;

    public OracleBlogsLinkContentChecker(final String url,
                                         final LinkData linkData,
                                         final Optional<ArticleData> articleData,
                                         final FileSection file) {
        super(url, linkData, articleData, file);
    }

    @Override
    protected LinkContentCheck checkGlobalData(final String data)
    {
        _parser = new OracleBlogsLinkContentParser(getUrl(), data);
        return null;
    }

    @Override
    protected LinkContentCheck checkLinkTitle(final String data,
                                              final String title) throws ContentParserException
    {
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
    protected LinkContentCheck checkLinkSubtitles(final String data,
                                                  final String[] subtitles) throws ContentParserException
    {
        if (subtitles.length > 1) {
            return new LinkContentCheck("Oracle Blogs article should have zero or one subtitle");
        }

        final Optional<String> effectiveSubtitle = _parser.getSubtitleInternal();

        if (effectiveSubtitle.isEmpty()) {
            if (subtitles.length != 0 ) {
                return new LinkContentCheck("subtitle \"" +
                                            subtitles[0] +
                                            "\" should not be present");
            }
            return null;
        }

        if (subtitles.length == 0) {
            return new LinkContentCheck("the subtitle \"" +
                                        effectiveSubtitle.get() +
                                        "\" is missing");
        }

        if (!subtitles[0].equals(effectiveSubtitle.get())) {
            return new LinkContentCheck("subtitle \"" +
                                        subtitles[0] +
                                        "\" is not equal to the real subtitle \"" +
                                        effectiveSubtitle.get() +
                                          "\"");
        }

        return null;
    }

    @Override
    protected LinkContentCheck checkLinkLanguages(final String data,
                                                  final Locale[] languages)
    {
        if ((languages.length != 1) || (languages[0] != Locale.ENGLISH)) {
            return new LinkContentCheck("Oracle Blogs article should have the language set as English");
        }

        return null;
    }

    @Override
    protected LinkContentCheck checkArticleDate(final String data,
                                                final Optional<TemporalAccessor> publicationDate,
                                                final Optional<TemporalAccessor> creationDate) throws ContentParserException
    {
        if (creationDate.isEmpty()) {
            return new LinkContentCheck("Oracle Blogs article should have a creation date");
        }
        final TemporalAccessor date =  publicationDate.isPresent() ? publicationDate.get()
                                                                   : creationDate.get();

        final TemporalAccessor d = _parser.getDate().get();
        final LocalDate effectiveDate = LocalDate.of(d.get(ChronoField.YEAR),
                                                     d.get(ChronoField.MONTH_OF_YEAR),
                                                     d.get(ChronoField.DAY_OF_MONTH));

        if (!date.equals(effectiveDate)) {
            return new LinkContentCheck("expected publication date " +
                                        date +
                                        " is not equal to the effective date " +
                                        effectiveDate);
        }

        return null;
    }

    @Override
    protected LinkContentCheck checkLinkAuthors(final String data,
                                                final List<AuthorData> authors) throws ContentParserException
    {
        final List<AuthorData> effectiveAuthor = _parser.getAuthors();

        if (effectiveAuthor.size() != authors.size()) {
            return new LinkContentCheck("The number of authors (" +
                                        authors.size() +
                                        ") is not equal to the effective number of authors (" +
                                        effectiveAuthor.size() +
                                        ")");
        }

        for (int i=0; i < authors.size(); i++) {
            if (!effectiveAuthor.get(i).equals(authors.get(i))) {
                return new LinkContentCheck("The expected author (" +
                                            authors.get(i) +
                                            ") is not equal to the effective author (" +
                                            effectiveAuthor.get(i) +
                                            ")");
            }
        }
        return null;
    }
}
