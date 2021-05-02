package data.linkchecker;

import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import data.knowledge.WellKnownAuthors;
import data.knowledge.WellKnownAuthorsOfLink;
import utils.FileHelper;
import utils.HtmlHelper;
import utils.Logger;
import utils.xmlparsing.ArticleData;
import utils.xmlparsing.AuthorData;
import utils.xmlparsing.LinkFormat;
import utils.xmlparsing.LinkData;

public class LinkContentChecker {

    private final URL _url;
    private final LinkData _linkData;
    private final Optional<ArticleData> _articleData;
    private final File _file;
    private LinkContentParser _parser;

    public LinkContentChecker(final URL url,
                              final LinkData linkData,
                              final Optional<ArticleData> articleData,
                              final File file) {
        _url = url;
        _linkData = linkData;
        _articleData = articleData;
        _file = file;
    }

    public final List<LinkContentCheck> check() throws ContentParserException {
        final String content = FileHelper.slurpFile(_file);
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

    public final List<LinkContentCheck> check(final String data) throws ContentParserException {

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
            final LinkContentCheck check = checkLinkAuthors(data, _articleData.get().getAuthors());
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
                                                            _articleData.isPresent() ? _articleData.get().getDate()
                                                                                     : Optional.empty());
            if (check != null) {
                checks.add(check);
            }
        }

        return checks;
    }

    /**
     * @throws ContentParserException
     */
    @SuppressWarnings("static-method")
    protected LinkContentCheck checkGlobalData(@SuppressWarnings("unused") final String data) throws ContentParserException
    {
        return null;
    }

    /**
     * @throws ContentParserException
     */
    @SuppressWarnings("static-method")
    protected LinkContentCheck checkLinkTitle(final String data,
                                              final String title) throws ContentParserException
    {
        return checkTitle(data, title, "title");
    }

    /**
     * @throws ContentParserException
     */
    @SuppressWarnings("static-method")
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
     * @throws ContentParserException
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
     * @throws ContentParserException
     */
    @SuppressWarnings("static-method")
    protected LinkContentCheck checkLinkDuration(@SuppressWarnings("unused") final String data,
                                                 @SuppressWarnings("unused") final Duration duration) throws ContentParserException
    {
        return null;
    }

    /**
     * @throws ContentParserException
     */
    protected LinkContentCheck checkLinkLanguages(final String data,
                                                  final Locale[] languages) throws ContentParserException
    {
        if (_parser == null) {
            _parser = new LinkContentParser(data);
        }

        final Optional<Locale> language = _parser.getLanguage();

        if (language.isPresent() && !Arrays.asList(languages).contains(language.get())) {
            return new LinkContentCheck("language is \"" + language.get() + "\" but this one is unexpected");
        }

        return null;
    }

    /**
     * @throws ContentParserException
     */
    @SuppressWarnings("static-method")
    protected LinkContentCheck checkArticleDate(@SuppressWarnings("unused") final String data,
                                                @SuppressWarnings("unused") final Optional<TemporalAccessor> publicationDate,
                                                @SuppressWarnings("unused") final Optional<TemporalAccessor> creationDate) throws ContentParserException
    {
        return null;
    }

    private static LinkContentCheck checkTitle(final String data,
                                               final String expectedTitle,
                                               final String description) {
        final String d = HtmlHelper.cleanContent(data);
        if (!doesStringAppearInData(d, expectedTitle)) {
            String comment = "";
            final String realTitle = extractEffectiveTitleByIgnoringNonBreakingSpaces(d, expectedTitle);
            if (realTitle != null) {
                comment = " (this is a problem of non breaking space, the real " + description + " is \"" + realTitle + "\")";
            }
            return new LinkContentCheck(description + " \"" + expectedTitle + "\" does not appear in the page" + comment);
        }
        return null;

    }

    private static LinkContentCheck checkAuthor(final String data,
                                                final AuthorData author) {
        final String d = HtmlHelper.cleanContent(data);
        final String authorStr = author.getLastName().isPresent() ? author.getLastName().get()
                                                                  : author.getFirstName().isPresent() ? author.getFirstName().get()
                                                                                                      : author.getGivenName().get();
        if (!doesStringAppearInDataCaseIndependant(d, authorStr)) {
            return new LinkContentCheck("author \"" + authorStr + "\" does not appear in the page");
        }
        return null;
    }

    private static boolean doesStringAppearInDataCaseIndependant(final String data,
                                                                 final String str) {
        return doesStringAppearInData(data.toUpperCase(), str.toUpperCase());
    }

    private static boolean doesStringAppearInData(final String data,
                                                  final String str) {
        return (data.indexOf(str) >= 0);
    }

    private static String extractEffectiveTitleByIgnoringNonBreakingSpaces(final String data,
                                                                           final String str) {
        final String data2 = data.replaceAll("\u00A0", " ");
        final String str2 = str.replaceAll("\u00A0", " ");
        final int index = data2.indexOf(str2);
        if (index  >= 0) {
            return data.substring(index, index + str.length());
        }
        return null;
    }
}
