package data.linkchecker;

import java.io.File;
import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;
import java.util.Optional;

import utils.xmlparsing.ArticleData;
import utils.xmlparsing.LinkData;

public class QuantaMagazineLinkContentChecker extends LinkContentChecker {

    private QuantaMagazineLinkContentParser _parser;

    public QuantaMagazineLinkContentChecker(final LinkData linkData,
                                        final Optional<ArticleData> articleData,
                                        final File file) {
        super(linkData, articleData, file);
    }

    @Override
    protected LinkContentCheck checkGlobalData(final String data)
    {
        _parser = new QuantaMagazineLinkContentParser(data);

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
            return new LinkContentCheck("Quanta Magazine article should have one subtitle");
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
        if ((languages.length != 1) && (languages[0] != Locale.FRENCH)) {
            return new LinkContentCheck("Quanta Magazine article should have the language set as English");
        }

        return null;
    }

    @Override
    protected LinkContentCheck checkArticleDate(final String data,
                                                final Optional<TemporalAccessor> publicationDate,
                                                final Optional<TemporalAccessor> creationDate) throws ContentParserException
    {
        if (publicationDate.isPresent()) {
            return new LinkContentCheck("Quanta Magazine article should have no publication date");
        }
        if (creationDate.isEmpty()) {
            return new LinkContentCheck("Quanta Magazine article should have a creation date");
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
}
