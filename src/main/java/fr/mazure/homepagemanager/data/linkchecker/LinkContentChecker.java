package fr.mazure.homepagemanager.data.linkchecker;

import java.time.Duration;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import fr.mazure.homepagemanager.data.knowledge.WellKnownAuthors;
import fr.mazure.homepagemanager.data.knowledge.WellKnownAuthorsOfLink;
import fr.mazure.homepagemanager.data.violationcorrection.UpdateLinkLanguageCorrection;
import fr.mazure.homepagemanager.data.violationcorrection.UpdateLinkTitleCorrection;
import fr.mazure.homepagemanager.data.violationcorrection.ViolationCorrection;
import fr.mazure.homepagemanager.utils.FileSection;
import fr.mazure.homepagemanager.utils.Logger;
import fr.mazure.homepagemanager.utils.StringHelper;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.ArticleData;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkFormat;

/**
 * Base class for the link data checkers
 */
public class LinkContentChecker implements Checker {

    private final String _url;
    private final LinkData _linkData;
    private final Optional<ArticleData> _articleData;
    private final FileSection _file;
    private LinkContentParser _parser;

    /**
     * @param url URL of the link to check
     * @param linkData expected link data
     * @param articleData expected article data
     * @param file effective retrieved link data
     */
    public LinkContentChecker(final String url,
                              final LinkData linkData,
                              final Optional<ArticleData> articleData,
                              final FileSection file) {
        _url = url;
        _linkData = linkData;
        _articleData = articleData;
        _file = file;
    }

    /**
     * Perform the check
     *
     * @return List of violations
     * @throws ContentParserException Failure to extract the information
     */
    @Override
    public final List<LinkContentCheck> check() throws ContentParserException {
        final String content = HtmlHelper.slurpFile(_file);
        final Pattern p = Pattern.compile("</HTML>\\p{Space}*$", Pattern.CASE_INSENSITIVE);
        final Matcher m = p.matcher(content);

        if (!m.find()) {
            Logger.log(Logger.Level.WARN).append("File " + _file + " does not end with </HTML>");
        }

        try {
            return check(content);
        } catch (final ContentParserException e) {
            throw new ContentParserException("Failed to check data of \"" + _url + "\"", e);
        }
    }

    private final List<LinkContentCheck> check(final String data) throws ContentParserException {

        final List<LinkContentCheck> checks = new ArrayList<>();

        {
            final LinkContentCheck check = checkGlobalData(data);
            if (check != null) {
                checks.add(check);
                return checks;
            }
        }

        if (_articleData.isPresent()) { // check title only for articles
            final LinkContentCheck check = checkLinkTitle(data, _linkData.getTitle());
            if (check != null) {
                checks.add(check);
            }
        }

        if (_articleData.isPresent()) { // check subtitles only for articles
            final LinkContentCheck check = checkLinkSubtitles(data, _linkData.getSubtitles());
            if (check != null) {
                checks.add(check);
            }
        }

        if (_articleData.isPresent()) {
            final LinkContentCheck check = checkLinkAuthors(data, _articleData.get().authors());
            if (check != null) {
                checks.add(check);
            }
        }

        if (_linkData.getDuration().isPresent()) {
            final LinkContentCheck check = checkLinkDuration(data, _linkData.getDuration().get());
            if (check != null) {
                checks.add(check);
            }
        }

        if (!Arrays.asList(_linkData.getFormats()).contains(LinkFormat.PDF))
        {
            final LinkContentCheck check = checkLinkLanguages(data, _linkData.getLanguages());
            if (check != null) {
                checks.add(check);
            }

        }

        {
            final LinkContentCheck check = checkArticleDate(data,
                                                            _linkData.getPublicationDate(),
                                                            _articleData.isPresent() ? _articleData.get().date()
                                                                                     : Optional.empty());
            if (check != null) {
                checks.add(check);
            }
        }

        return checks;
    }

    /**
     * @throws ContentParserException Failure to extract the information
     */
    @SuppressWarnings("static-method")
    protected LinkContentCheck checkGlobalData(@SuppressWarnings("unused") final String data) throws ContentParserException
    {
        return null;
    }

    /**
     * @throws ContentParserException Failure to extract the information
     */
    protected LinkContentCheck checkLinkTitle(final String data,
                                              final String title) throws ContentParserException
    {
        return checkTitle(data, title, "title");
    }

    /**
     * @throws ContentParserException Failure to extract the information
     */
    protected LinkContentCheck checkLinkSubtitles(final String data,
                                                  final String[] subtitles) throws ContentParserException
    {
        for (final String subtitle: subtitles) {
            final LinkContentCheck check = checkTitle(data, subtitle, "subtitle");
            if (check != null) {
                return check;
            }
        }
        return null;
    }

    /**
     * @throws ContentParserException Failure to extract the information
     */
    protected LinkContentCheck checkLinkAuthors(final String data,
                                                final List<AuthorData> authors) throws ContentParserException
    {
        final Optional<WellKnownAuthors> wellKnownAuthors = WellKnownAuthorsOfLink.getWellKnownAuthors(_url);

        for (final AuthorData author: authors) {
            if (wellKnownAuthors.isPresent() &&
                wellKnownAuthors.get().getCompulsoryAuthors().contains(author)) {
                // well known author does not appear most of the time so we ignore them
                continue;
            }
            final LinkContentCheck check = checkAuthor(data, author);
            if (check != null) {
                return check;
            }
        }
        return null;
    }

    /**
     * @throws ContentParserException Failure to extract the information
     */
    @SuppressWarnings("static-method")
    protected LinkContentCheck checkLinkDuration(@SuppressWarnings("unused") final String data,
                                                 @SuppressWarnings("unused") final Duration duration) throws ContentParserException
    {
        return null;
    }

    /**
     * @throws ContentParserException Failure to extract the information
     */
    protected LinkContentCheck checkLinkLanguages(final String data,
                                                  final Locale[] expectedLanguages) throws ContentParserException
    {
        if (_parser == null) {
            _parser = new LinkContentParser(data);
        }

        final Optional<Locale> effectiveLanguage = _parser.getLanguage();

        if (effectiveLanguage.isPresent()) {
            return checkLinkLanguagesHelper(effectiveLanguage.get(), expectedLanguages);
        }

        return null;
    }

    /**
     * @throws ContentParserException Failure to extract the information
     */
    @SuppressWarnings("static-method")
    protected LinkContentCheck checkArticleDate(@SuppressWarnings("unused") final String data,
                                                @SuppressWarnings("unused") final Optional<TemporalAccessor> publicationDate,
                                                @SuppressWarnings("unused") final Optional<TemporalAccessor> creationDate) throws ContentParserException
    {
        return null;
    }

    protected static LinkContentCheck simpleCheckLinkAuthors(final List<AuthorData> effectiveAuthors,
                                                             final List<AuthorData> expectedAuthors)
    {
        final List<AuthorData> unexpectedAuthors = new ArrayList<>();
        for (final AuthorData author: effectiveAuthors) {
            if (!expectedAuthors.contains(author)) {
                unexpectedAuthors.add(author);
            }
        }

        final List<AuthorData> missingAuthors = new ArrayList<>();
        for (final AuthorData author: expectedAuthors) {
            if (!effectiveAuthors.contains(author)) {
                missingAuthors.add(author);
            }
        }

        if (!unexpectedAuthors.isEmpty() || !missingAuthors.isEmpty()) {
            final String message = "The list of effective authors is not the effective one."
                                   + "\nThe following authors are effectively present but are unexpected: " + unexpectedAuthors.stream().map(a-> a.toString()).collect(Collectors.joining(","))
                                   + "\nThe following authors are expected but are effectively missing: " + missingAuthors.stream().map(a-> a.toString()).collect(Collectors.joining(","));
            return new LinkContentCheck("WrongAuthors",
                                        message,
                                        Optional.empty());

        }

        for (int i = 0; i < expectedAuthors.size(); i++) {
            if (!expectedAuthors.get(i).equals(effectiveAuthors.get(i))) {
                final String message = "The list of effective authors is not ordered as the effective one."
                        + "\nexpected authors: " + expectedAuthors.stream().map(a-> a.toString()).collect(Collectors.joining(","))
                        + "\neffective authors: " + effectiveAuthors.stream().map(a-> a.toString()).collect(Collectors.joining(","));
                return new LinkContentCheck("WrongAuthors",
                                            message,
                                            Optional.empty());
            }
        }

        return null;
    }

    private LinkContentCheck checkTitle(final String data,
                                        final String expectedTitle,
                                        final String description) {
        final String d = HtmlHelper.cleanContent(data);
        if (StringHelper.generalizedIndex(d, expectedTitle, false, false) < 0) {
            final int i1 = StringHelper.generalizedIndex(d, expectedTitle, true, false);
            if (i1 >= 0) {
                final String effectiveTitle = d.substring(i1, i1 + expectedTitle.length());
                return new LinkContentCheck("WrongTitle",
                                            description +
                                            " \"" +
                                            expectedTitle +
                                            "\" does not appear in the page, this is a problem of casing, the real title is \"" +
                                            effectiveTitle +
                                            "\"",
                                            Optional.of(new UpdateLinkTitleCorrection(expectedTitle, effectiveTitle, _url)));
            }
            final int i2 = StringHelper.generalizedIndex(d, expectedTitle, false, true);
            if (i2 >= 0) {
                final String effectiveTitle = d.substring(i2, i2 + expectedTitle.length());
                return new LinkContentCheck("WrongTitle",
                                            description +
                                            " \"" +
                                            expectedTitle +
                                            "\" does not appear in the page, this is a problem of space, the real title is \"" +
                                            effectiveTitle +
                                            "\"",
                                            Optional.of(new UpdateLinkTitleCorrection(expectedTitle, effectiveTitle, _url)));
            }
            final int i3 = StringHelper.generalizedIndex(d, expectedTitle, false, true);
            if (i3 >= 0) {
                final String effectiveTitle = d.substring(i3, i3 + expectedTitle.length());
                return new LinkContentCheck("WrongTitle",
                                            description +
                                            " \"" +
                                            expectedTitle +
                                            "\" does not appear in the page, this is a problem of casing and space, the real title is \"" +
                                            effectiveTitle +
                                            "\"",
                                            Optional.of(new UpdateLinkTitleCorrection(expectedTitle, effectiveTitle, _url)));
            }
            return new LinkContentCheck("WrongTitle",
                                        description +
                                        " \"" +
                                        expectedTitle +
                                        "\" does not appear in the page",
                                        Optional.empty());
        }
        return null;
    }

    private static LinkContentCheck checkAuthor(final String data,
                                                final AuthorData author) {
        final String d = HtmlHelper.cleanContent(data);
        final String authorStr = author.getLastName().isPresent() ? author.getLastName().get()
                                                                  : author.getFirstName().isPresent() ? author.getFirstName().get()
                                                                                                      : author.getGivenName().get();
        if (StringHelper.generalizedIndex(d, authorStr, true, false) < 0) {
            return new LinkContentCheck("WrongAuthors",
                                        "author \"" + authorStr + "\" does not appear in the page",
                                        Optional.empty());
        }
        return null;
    }

    protected String getUrl() {
        return _url;
    }

    protected LinkContentCheck checkLinkLanguagesHelper(final Locale effectiveLanguage,
                                                        final Locale[] expectedLanguages) {

        if (Arrays.asList(expectedLanguages).contains(effectiveLanguage)) {
            return null;
        }

        final Optional<ViolationCorrection> correction = (expectedLanguages.length == 1) ? Optional.of(new UpdateLinkLanguageCorrection(expectedLanguages[0], effectiveLanguage, getUrl()))
                                                                                         : Optional.empty();
        final String expectedLanguagesAsString = Arrays.stream(expectedLanguages).map(l -> l.toString()).collect(Collectors.joining(", "));
        return new LinkContentCheck("WrongLanguage",
                                    "language is \"" +
                                    effectiveLanguage +
                                    "\" but this one is unexpected, the expected languages are: " +
                                    expectedLanguagesAsString,
                                    correction);
    }
}
