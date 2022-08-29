package data.linkchecker.arstechnica;

import java.net.URL;
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

public class ArsTechnicaLinkContentChecker extends LinkContentChecker {

    private ArsTechnicaLinkContentParser _parser;

    public ArsTechnicaLinkContentChecker(final URL url,
                                         final LinkData linkData,
                                         final Optional<ArticleData> articleData,
                                         final FileSection file) {
        super(url, linkData, articleData, file);
    }

    @Override
    protected LinkContentCheck checkGlobalData(final String data)
    {
        _parser = new ArsTechnicaLinkContentParser(data);
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
        if (subtitles.length != 1) {
            return new LinkContentCheck("Ars Technica article should have one subtitle");
        }


        final String effectiveSubtitle = _parser.getSubtitle();

        if (!subtitles[0].equals(effectiveSubtitle)) {
            return new LinkContentCheck("subtitle \"" +
                                        subtitles[0] +
                                        "\" is not equal to the real subtitle \"" +
                                        effectiveSubtitle +
                                          "\"");
        }

        return null;
    }

    @Override
    protected LinkContentCheck checkLinkLanguages(final String data,
                                                  final Locale[] languages)
    {
        if ((languages.length != 1) || (languages[0] != Locale.ENGLISH)) {
            return new LinkContentCheck("Ars Technica article should have the language set as English");
        }

        return null;
    }

    @Override
    protected LinkContentCheck checkArticleDate(final String data,
                                                final Optional<TemporalAccessor> publicationDate,
                                                final Optional<TemporalAccessor> creationDate) throws ContentParserException
    {
        if (publicationDate.isPresent()) {
            return new LinkContentCheck("Ars Technica article should have no publication date");
        }
        if (creationDate.isEmpty()) {
            return new LinkContentCheck("Ars Technica article should have a creation date");
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
        if (authors.size() > 1) {
            return new LinkContentCheck("Ars Technica article should have at most one author");
        }

        final Optional<AuthorData> effectiveAuthor = _parser.getAuthor();
        if (effectiveAuthor.isPresent()) {
            if (authors.size() == 1) {
                if (!effectiveAuthor.get().equals(authors.get(0))) {
                    return new LinkContentCheck("The expected author (" +
                                                authors.get(0) +
                                                ") is not equal to the effective author (" +
                                                effectiveAuthor.get() +
                                                ")");
                }
                return null;
            }
            return new LinkContentCheck("No author is expected but there is one (" +
                                        effectiveAuthor.get() +
                                        ")");
        }
        if (authors.size() == 1) {
            return new LinkContentCheck("An author is expected (" +
                                        authors.get(0) +
                                        ") but there is none");
        }
        return null;
    }
}
