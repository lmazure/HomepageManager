package data.linkchecker.wired;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import data.linkchecker.ContentParserException;
import data.linkchecker.LinkContentCheck;
import data.linkchecker.LinkContentChecker;
import utils.xmlparsing.ArticleData;
import utils.xmlparsing.AuthorData;
import utils.xmlparsing.LinkData;

public class WiredLinkContentChecker extends LinkContentChecker {

    private WiredLinkContentParser _parser;

    public WiredLinkContentChecker(final URL url,
                                   final LinkData linkData,
                                   final Optional<ArticleData> articleData,
                                   final File file) {
        super(url, linkData, articleData, file);
    }

    @Override
    protected LinkContentCheck checkGlobalData(final String data)
    {
        _parser = new WiredLinkContentParser(data);

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
            return new LinkContentCheck("Wired article should have at most one subtitle");
        }

        final Optional<String> effectiveSubtitle = _parser.getSubtitle();

        if (effectiveSubtitle .isPresent()) {
            if (subtitles.length == 0) {
                return new LinkContentCheck("Article has no subtitle while there is effectively one (\"" +
                                            effectiveSubtitle.get() +
                                            "\")");
            }
            if (!subtitles[0].equals(effectiveSubtitle.get())) {
                return new LinkContentCheck("subtitle \"" +
                                            subtitles[0] +
                                            "\" is not equal to the real subtitle \"" +
                                            effectiveSubtitle.get() +
                                            "\"");
            }
        } else {
            if (subtitles.length == 1) {
                return new LinkContentCheck("Article has one subtitle while there is effectively none");
            }
        }

        return null;
    }

    @Override
    protected LinkContentCheck checkLinkLanguages(final String data,
                                                  final Locale[] languages)
    {
        if ((languages.length != 1) || (languages[0] != Locale.ENGLISH)) {
            return new LinkContentCheck("Wired article should have the language set as English");
        }

        return null;
    }

    @Override
    protected LinkContentCheck checkArticleDate(final String data,
                                                final Optional<TemporalAccessor> publicationDate,
                                                final Optional<TemporalAccessor> creationDate) throws ContentParserException
    {
        if (publicationDate.isPresent()) {
            return new LinkContentCheck("Wired article should have no publication date");
        }
        if (creationDate.isEmpty()) {
            return new LinkContentCheck("Wired article should have a creation date");
        }

        final LocalDate effectiveDate = _parser.getDate();

        if (!creationDate.get().equals(effectiveDate)) {
            return new LinkContentCheck("expected creation date " +
                                        creationDate.get() +
                                        " is not equal to the effective date " +
                                        effectiveDate);
        }

        return null;
    }

    @Override
    protected LinkContentCheck checkLinkAuthors(final String data,
                                                final List<AuthorData> authors) throws ContentParserException
    {
        if ((authors.size() < 1) || (authors.size() > 2)) {
            return new LinkContentCheck("Wired should have one or two authors");
        }

        final List<AuthorData> effectiveAuthor = _parser.getAuthors();
        if (effectiveAuthor.size() == 0) {
            return new LinkContentCheck(authors.size()  + " author(s) was(were) expected, but there is none");
        }

        if (!authors.get(0).equals(effectiveAuthor.get(0))) {
            return new LinkContentCheck("The first expected author (" +
                                        authors.get(0) +
                                        ") is not equal to the effective first author (" +
                                        effectiveAuthor.get(0) +
                                        ")");
        }

        if (authors.size() == 1) {
            if (effectiveAuthor.size() == 2) {
                return new LinkContentCheck("One author was expected, but there are effectively two authors");
            }
        } else { // authors.size() == 2
            if (effectiveAuthor.size() == 1) {
                return new LinkContentCheck("Two authors were expected, but there is effectively one author");
            }
            if (!authors.get(1).equals(effectiveAuthor.get(1))) {
                return new LinkContentCheck("The second expected author (" +
                                            authors.get(1) +
                                            ") is not equal to the effective second author (" +
                                            effectiveAuthor.get(1) +
                                            ")");
            }
        }

        return null;
    }
}
