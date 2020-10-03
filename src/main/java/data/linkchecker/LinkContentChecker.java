package data.linkchecker;

import java.io.File;
import java.time.Duration;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import utils.FileHelper;
import utils.xmlparsing.ArticleData;
import utils.xmlparsing.Format;
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
        return check(FileHelper.slurpFile(_file));
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

        if (_linkData.getDuration().isPresent()) {
            final LinkContentCheck check = checkLinkDuration(data, _linkData.getDuration().get());
            if (check != null) {
                checks.add(check);
            }
        }

        if (!Arrays.asList(_linkData.getFormats()).contains(Format.PDF))
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

        final Locale language = _parser.getLanguage();

        if (!Arrays.asList(languages).contains(language)) {
            return new LinkContentCheck("language is \"" + language + "\" but this one is unexpected");
        }

        return null;
    }

    protected LinkContentCheck checkArticleDate(final String data,
                                                final Optional<TemporalAccessor> publicationDate,
                                                final Optional<TemporalAccessor> creationDate)
    {
        return null;
    }
}
