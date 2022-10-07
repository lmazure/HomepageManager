package data.linkchecker;

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

import data.knowledge.WellKnownAuthors;
import data.knowledge.WellKnownAuthorsOfLink;
import utils.FileSection;
import utils.HtmlHelper;
import utils.Logger;
import utils.StringHelper;
import utils.xmlparsing.ArticleData;
import utils.xmlparsing.AuthorData;
import utils.xmlparsing.LinkData;
import utils.xmlparsing.LinkFormat;

public class LinkContentChecker {

    private final String _url;
    private final LinkData _linkData;
    private final Optional<ArticleData> _articleData;
    private final FileSection _file;
    private LinkContentParser _parser;

    public LinkContentChecker(final String url,
                              final LinkData linkData,
                              final Optional<ArticleData> articleData,
                              final FileSection file) {
        _url = url;
        _linkData = linkData;
        _articleData = articleData;
        _file = file;
    }

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

        if (language.isPresent() &&
            !Arrays.asList(languages).contains(language.get())) {
            final String languagesAsString = Arrays.stream(languages).map(l -> l.toString()).collect(Collectors.joining(", "));
            return new LinkContentCheck("language is \"" + language.get() + "\" but this one is unexpected, the expected languages are: " + languagesAsString);
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
                                   + "\nThe following authors are effectively present but are unexpected:" + unexpectedAuthors.stream().map(a-> a.toString()).collect(Collectors.joining(","))
                                   + "\nThe following authors are expected but are effectively missing:" + missingAuthors.stream().map(a-> a.toString()).collect(Collectors.joining(","));
            return new LinkContentCheck(message);

        }

        for (int i = 0; i < expectedAuthors.size(); i++) {
            if (!expectedAuthors.get(i).equals(effectiveAuthors.get(i))) {
                final String message = "The list of effective authors is not ordered as the effective one."
                        + "\nexpected authors:" + expectedAuthors.stream().map(a-> a.toString()).collect(Collectors.joining(","))
                        + "\neffective authors:" + effectiveAuthors.stream().map(a-> a.toString()).collect(Collectors.joining(","));
                return new LinkContentCheck(message);
            }
        }

        return null;
    }

    private static LinkContentCheck checkTitle(final String data,
                                               final String expectedTitle,
                                               final String description) {
        final String d = HtmlHelper.cleanContent(data);
        if (StringHelper.generalizedIndex(d, expectedTitle, false, false) < 0) {
            final int i1 = StringHelper.generalizedIndex(d, expectedTitle, true, false);
            if (i1 >= 0) {
                return new LinkContentCheck(description +
                                            " \"" +
                                            expectedTitle +
                                            "\" does not appear in the page, this is a problem of casing, the real title is \"" +
                                            d.substring(i1, i1 + expectedTitle.length()) +
                                            "\"");
            }
            final int i2 = StringHelper.generalizedIndex(d, expectedTitle, false, true);
            if (i2 >= 0) {
                return new LinkContentCheck(description +
                                            " \"" +
                                            expectedTitle +
                                            "\" does not appear in the page, this is a problem of space, the real title is \"" +
                                            d.substring(i2, i2 + expectedTitle.length()) +
                                            "\"");
            }
            final int i3 = StringHelper.generalizedIndex(d, expectedTitle, false, true);
            if (i3 >= 0) {
                return new LinkContentCheck(description +
                                            " \"" +
                                            expectedTitle +
                                            "\" does not appear in the page, this is a problem of casing and space, the real title is \"" +
                                            d.substring(i3, i3 + expectedTitle.length()) +
                                            "\"");
            }
            return new LinkContentCheck(description +
                                        " \"" +
                                        expectedTitle +
                                        "\" does not appear in the page");
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
            return new LinkContentCheck("author \"" + authorStr + "\" does not appear in the page");
        }
        return null;
    }
}
