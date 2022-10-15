package data.linkchecker.wired;

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

public class WiredLinkContentChecker extends LinkContentChecker {

    private WiredLinkContentParser _parser;

    public WiredLinkContentChecker(final String url,
                                   final LinkData linkData,
                                   final Optional<ArticleData> articleData,
                                   final FileSection file) {
        super(url, linkData, articleData, file);
    }

    @Override
    protected LinkContentCheck checkGlobalData(final String data)
    {
        _parser = new WiredLinkContentParser(data, getUrl());

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
                                                final List<AuthorData> expectedAuthors) throws ContentParserException
    {
        final List<AuthorData> effectiveAuthor = _parser.getAuthors();

        return simpleCheckLinkAuthors(effectiveAuthor, expectedAuthors);
    }
}
