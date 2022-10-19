package data.linkchecker;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import utils.FileSection;
import utils.StringHelper;
import utils.xmlparsing.ArticleData;
import utils.xmlparsing.AuthorData;
import utils.xmlparsing.LinkData;

public class ExtractorBasedLinkContentChecker extends LinkContentChecker {

    private final ThrowingLinkDataExtractor _extractorBuilder;
    private LinkDataExtractor _parser;

    public ExtractorBasedLinkContentChecker(final String url,
                                            final LinkData linkData,
                                            final Optional<ArticleData> articleData,
                                            final FileSection file,
                                            final ThrowingLinkDataExtractor extractorBuilder) {
        super(url, linkData, articleData, file);
        _extractorBuilder = extractorBuilder;
    }

    @Override
    protected LinkContentCheck checkGlobalData(final String data) throws ContentParserException
    {
        _parser = _extractorBuilder.apply(getUrl(), data);
        return null;
    }

    @Override
    protected LinkContentCheck checkLinkTitle(final String data,
                                              final String title) throws ContentParserException
    {
        final String effectiveTitle = _parser.getTitle();

        final String diff = StringHelper.compareAndExplainDifference(title, effectiveTitle);
        if (diff != null) {
            return new LinkContentCheck("title \"" +
                                        title +
                                        "\" is not equal to the real title \"" +
                                        effectiveTitle +
                                          "\"\n" +
                                        diff);
        }

        return null;
    }

    @Override
    protected LinkContentCheck checkLinkSubtitles(final String data,
                                                  final String[] expectedSubtitles) throws ContentParserException
    {
        if (expectedSubtitles.length > 1) {
            return new LinkContentCheck("More than one subtitle is not supported yet");  // TODO implement support of several subtitles
        }

        final Optional<String> effectiveSubtitle = _parser.getSubtitle();

        if (effectiveSubtitle.isEmpty()) {
            if (expectedSubtitles.length != 0 ) {
                return new LinkContentCheck("subtitle \"" +
                                            expectedSubtitles[0] +
                                            "\" should not be present");
            }
            return null;
        }

        if (expectedSubtitles.length == 0) {
            return new LinkContentCheck("the subtitle \"" +
                                        effectiveSubtitle.get() +
                                        "\" is missing");
        }

        if (!expectedSubtitles[0].equals(effectiveSubtitle.get())) {
            return new LinkContentCheck("subtitle \"" +
                                        expectedSubtitles[0] +
                                        "\" is not equal to the real subtitle \"" +
                                        effectiveSubtitle.get() +
                                          "\"");
        }

        return null;
    }

    @Override
    protected LinkContentCheck checkLinkLanguages(final String data,
                                                  final Locale[] languages) throws ContentParserException
    {
        if ((languages.length != 1) || (languages[0] != _parser.getLanguage())) {
            return new LinkContentCheck("Article should have the language set as " + _parser.getLanguage());
        }

        return null;
    }

    @Override
    protected LinkContentCheck checkArticleDate(final String data,
                                                final Optional<TemporalAccessor> publicationDate,
                                                final Optional<TemporalAccessor> creationDate) throws ContentParserException
    {
        if (creationDate.isEmpty()) {
            return new LinkContentCheck("Article should have a creation date");
        }
        final TemporalAccessor date =  publicationDate.isPresent() ? publicationDate.get()
                                                                   : creationDate.get();

        final TemporalAccessor d = _parser.getDate().get();
        final LocalDate effectiveDate = LocalDate.of(d.get(ChronoField.YEAR),
                                                     d.get(ChronoField.MONTH_OF_YEAR),
                                                     d.get(ChronoField.DAY_OF_MONTH));

        if (!date.equals(effectiveDate)) {
            return new LinkContentCheck("The expected publication date " +
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
        final List<AuthorData> effectiveAuthor = _parser.getSureAuthors();

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

    @FunctionalInterface
    protected interface ThrowingLinkDataExtractor {
        LinkDataExtractor apply(final String url, final String data) throws ContentParserException;
    }
}
