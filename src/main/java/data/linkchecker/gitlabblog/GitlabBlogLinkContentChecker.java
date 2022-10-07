package data.linkchecker.gitlabblog;

import java.time.LocalDate;
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

public class GitlabBlogLinkContentChecker extends LinkContentChecker {

    private GitlabBlogLinkContentParser _parser;

    public GitlabBlogLinkContentChecker(final String url,
                                        final LinkData linkData,
                                        final Optional<ArticleData> articleData,
                                        final FileSection file) {
        super(url, linkData, articleData, file);
    }

    @Override
    protected LinkContentCheck checkGlobalData(final String data)
    {
        _parser = new GitlabBlogLinkContentParser(data);

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
    protected LinkContentCheck checkLinkLanguages(final String data,
                                                  final Locale[] languages)
    {
        if ((languages.length != 1) || (languages[0] != Locale.ENGLISH)) {
            return new LinkContentCheck("GitLab blog should have the language set as English");
        }

        return null;
    }

    @Override
    protected LinkContentCheck checkArticleDate(final String data,
                                                final Optional<TemporalAccessor> publicationDate,
                                                final Optional<TemporalAccessor> creationDate) throws ContentParserException
    {
        if (publicationDate.isPresent()) {
            return new LinkContentCheck("GitLab blog should have no publication date");
        }
        if (creationDate.isEmpty()) {
            return new LinkContentCheck("GitLab blog should have a creation date");
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
        if ((authors.size() < 0) || (authors.size() > 2)) {
            return new LinkContentCheck("GitLab blog should have one or two authors");
        }

        final List<AuthorData> effectiveAuthor = _parser.getAuthors();
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
