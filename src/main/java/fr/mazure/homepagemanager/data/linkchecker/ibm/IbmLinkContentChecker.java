package fr.mazure.homepagemanager.data.linkchecker.ibm;

import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.LinkContentCheck;
import fr.mazure.homepagemanager.data.linkchecker.LinkContentChecker;
import fr.mazure.homepagemanager.data.violationcorrection.UpdateArticleDateCorrection;
import fr.mazure.homepagemanager.data.violationcorrection.UpdateLinkTitleCorrection;
import fr.mazure.homepagemanager.data.violationcorrection.ViolationCorrection;
import fr.mazure.homepagemanager.utils.DateTimeHelper;
import fr.mazure.homepagemanager.utils.FileSection;
import fr.mazure.homepagemanager.utils.xmlparsing.ArticleData;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkData;

/**
 *
 */
public class IbmLinkContentChecker extends LinkContentChecker {

    private IbmLinkContentParser _parser;
    private final String _url;

    /**
     * @param url URL of the link to check
     * @param linkData expected link data
     * @param articleData expected article data
     * @param file effective retrieved link data
     */
    public IbmLinkContentChecker(final String url,
                                 final LinkData linkData,
                                 final Optional<ArticleData> articleData,
                                 final FileSection file) {
        super(url, linkData, articleData, file);
        _url = url;
    }

    /**
     * Determine if the link is managed
     *
     * @param url link
     * @return true if the link is managed
     */
    public static boolean isUrlManaged(final String url) {
        return url.startsWith("https://developer.ibm.com/articles/") || url.startsWith("https://developer.ibm.com/tutorials/");
    }

    @Override
    protected LinkContentCheck checkGlobalData(final String data)
    {
        _parser = new IbmLinkContentParser(data, _url);
        if (_parser.articleIsLost()) {
            return new LinkContentCheck("LostArticle",
                                        "article is lost",
                                        Optional.empty());
        }

        return null;
    }

    @Override
    protected LinkContentCheck checkLinkTitle(final String data,
                                              final String title) throws ContentParserException
    {
        final String effectiveTitle = _parser.getTitle();

        if (!title.equals(effectiveTitle)) {
            return new LinkContentCheck("WrongTitle",
                                        "title \"" +
                                        title +
                                        "\" is not equal to the real title \"" +
                                        effectiveTitle +
                                        "\"",
                                        Optional.of(new UpdateLinkTitleCorrection(title, effectiveTitle, getUrl())));
        }

        return null;
    }

    @Override
    protected LinkContentCheck checkLinkSubtitles(final String data,
                                                  final String[] subtitles) throws ContentParserException
    {
        if (subtitles.length != 1) {
            return new LinkContentCheck("MissingSubtitle",
                                        "IBM article should have one subtitle",
                                        Optional.empty());
        }

        final String effectiveSubtitle = _parser.getSubtitle();

        if (!subtitles[0].equals(effectiveSubtitle)) {
            return new LinkContentCheck("WrongSubtitle",
                                        "subtitle \"" +
                                        subtitles[0] +
                                        "\" is not equal to the real subtitle \"" +
                                        effectiveSubtitle +
                                        "\"",
                                        Optional.empty());
        }

        return null;
    }

    @Override
    protected LinkContentCheck checkLinkLanguages(final String data,
                                                  final Locale[] languages)
    {
        if ((languages.length != 1) || (languages[0] != Locale.ENGLISH)) {
            return new LinkContentCheck("WrongLanguage",
                                        "IBM article should have the language set as English",
                                        Optional.empty());
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
                                        "IBM article should have a creation date",
                                        Optional.empty());
        }
        final TemporalAccessor date =  publicationDate.isPresent() ? publicationDate.get()
                                                                   : creationDate.get();

        final LocalDate effectiveDate = _parser.getDate();

        if (!date.equals(effectiveDate)) {
            final Optional<ViolationCorrection> correction = DateTimeHelper.convertTemporalAccessorToLocalDate(date)
                                                                           .map(dat -> new UpdateArticleDateCorrection(dat, effectiveDate, getUrl()));
            return new LinkContentCheck("WrongDate",
                                        "expected date " +
                                        date +
                                        " is not equal to the effective date " +
                                        effectiveDate,
                                        correction);
        }

        return null;
    }

    @Override
    protected LinkContentCheck checkLinkAuthors(final String data,
                                                final List<AuthorData> authors) throws ContentParserException
    {
        final List<AuthorData> effectiveAuthor = _parser.getAuthors();

        if (effectiveAuthor.size() != authors.size()) {
            return new LinkContentCheck("WrongAuthors",
                                        "The number of authors (" +
                                        authors.size() +
                                        ") is not equal to the effective number of authors (" +
                                        effectiveAuthor.size() +
                                        ")",
                                        Optional.empty());
        }

        for (int i=0; i < authors.size(); i++) {
            if (!effectiveAuthor.get(i).equals(authors.get(i))) {
                return new LinkContentCheck("WrongAuthors",
                                            "The expected author (" +
                                            authors.get(i) +
                                            ") is not equal to the effective author (" +
                                            effectiveAuthor.get(i) +
                                            ")",
                                            Optional.empty());
            }
        }
        return null;
    }
}
