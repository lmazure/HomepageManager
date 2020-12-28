package data.linkchecker;

import java.io.File;
import java.time.Duration;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.FileHelper;
import utils.HtmlHelper;
import utils.Logger;
import utils.xmlparsing.ArticleData;
import utils.xmlparsing.LinkFormat;
import utils.xmlparsing.LinkData;

public class LinkContentChecker {

    private final LinkData _linkData;
    private final Optional<ArticleData> _articleData;
    private final File _file;
    private LinkContentParser _parser;

    public LinkContentChecker(final LinkData linkData,
                              final Optional<ArticleData> articleData,
                              final File file) {
        _linkData = linkData;
        _articleData = articleData;
        _file = file;
    }

    public final List<LinkContentCheck> check() {
        final String content = FileHelper.slurpFile(_file);
        final Pattern p = Pattern.compile("</HTML>\\p{Space}*$", Pattern.CASE_INSENSITIVE);
        final Matcher m = p.matcher(content);

        if (!m.find()) {
            Logger.log(Logger.Level.WARN).append("File " + _file + " does not end with </HTML>");
        }
        return check(content);
    }

    public final List<LinkContentCheck> check(final String data) {

        final List<LinkContentCheck> checks = new ArrayList<LinkContentCheck>();

        {
            final LinkContentCheck check = checkGlobalData(data);
            if (check != null) {
                checks.add(check);
                return checks;
            }
        }

        {
            final LinkContentCheck check = checkLinkTitle(data, _linkData.getTitle());
            if (check != null) {
                checks.add(check);
            }
        }

        {
            final LinkContentCheck check = checkLinkSubtitles(data, _linkData.getSubtitles());
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

        if (_articleData.isPresent() &&
            (_articleData.get().getDate().isPresent() || _linkData.getPublicationDate().isPresent())) {
            final LinkContentCheck check = checkArticleDate(data, _linkData.getPublicationDate(), _articleData.get().getDate());
            if (check != null) {
                checks.add(check);
            }
        }

        return checks;
    }

    protected LinkContentCheck checkGlobalData(final String data)
    {
        return null;
    }

    protected LinkContentCheck checkLinkTitle(final String data,
                                              final String title)
    {
        if (_articleData.isEmpty()) {
            return null;
        }

        return checkTitle(data, title, "title");
    }

    protected LinkContentCheck checkLinkSubtitles(final String data,
                                                  final String[] subtitles)
    {
        if (_articleData.isEmpty()) {
            return null;
        }

        for (final String subtitle: subtitles) {
            final LinkContentCheck check = checkTitle(data, subtitle, "subtitle");
            if (check != null) {
                return check;
            }
        }
        return null;
    }

    protected LinkContentCheck checkLinkDuration(final String data,
                                                 final Duration duration)
    {
        return null;
    }

    protected LinkContentCheck checkLinkLanguages(final String data,
                                                  final Locale[] languages)
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

    protected LinkContentCheck checkArticleDate(final String data,
                                                final Optional<TemporalAccessor> publicationDate,
                                                final Optional<TemporalAccessor> creationDate)
    {
        return null;
    }

    private LinkContentCheck checkTitle(final String data,
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

    private boolean doesStringAppearInData(final String data,
                                           final String str) {
        return (data.indexOf(str) >= 0);
    }

    private String extractEffectiveTitleByIgnoringNonBreakingSpaces(final String data,
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
