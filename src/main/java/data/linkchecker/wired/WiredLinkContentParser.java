package data.linkchecker.wired;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;

import data.linkchecker.ContentParserException;
import data.linkchecker.ExtractedLinkData;
import data.linkchecker.LinkContentParserUtils;
import data.linkchecker.LinkDataExtractor;
import data.linkchecker.TextParser;
import utils.HtmlHelper;
import utils.xmlparsing.AuthorData;
import utils.xmlparsing.LinkFormat;

public class WiredLinkContentParser extends LinkDataExtractor {

    private static final TextParser s_jsonParser
    = new TextParser("<script type=\"text/javascript\">window.__PRELOADED_STATE__ =",
                     "</script>",
                     "Wired",
                     "JSON");

    private final ContentParserException _exception;
    private final String _title;
    private final String _subtitle;
    private final LocalDate _publicationDate;
    private final List<AuthorData> _authors;

    public WiredLinkContentParser(final String url,
                                  final String data) {
        super(url);
        ContentParserException exception = null;
        String title = null;
        String subtitle = null;
        LocalDate publicationDate = null;
        List<AuthorData> authors = null;
        try {
            final String json = s_jsonParser.extract(data);
            final JSONObject payload = new JSONObject(json);
            title = HtmlHelper.cleanContent(payload.getJSONObject("transformed").getJSONObject("article").getJSONObject("headerProps").getString("dangerousHed"));
            subtitle = HtmlHelper.cleanContent(payload.getJSONObject("transformed").getJSONObject("article").getJSONObject("headerProps").getString("dangerousDek"));
            final String pubDate = payload.getJSONObject("transformed").getString("head.firstPublishDate");
            publicationDate = ZonedDateTime.parse(pubDate, DateTimeFormatter.ISO_DATE_TIME).toLocalDate();
            final JSONArray authorArray = payload.getJSONObject("transformed").getJSONArray("head.jsonld").getJSONObject(0).getJSONArray("author");
            authors = new ArrayList<>(authorArray.length());
            for (int i = 0; i < authorArray.length(); i++) {
                final String author = authorArray.getJSONObject(i).getString("name");
                if (!author.equals("WIRED Staff")) {
                    final String cleanedAuthor = author.replaceAll(", Ars Technica$", "");
                    authors.add(LinkContentParserUtils.getAuthor(cleanedAuthor));
                }
            }

        } catch (final ContentParserException e) {
            exception = e;
        }
        _title = title;
        _subtitle = subtitle;
        _publicationDate = publicationDate;
        _authors = authors;
        _exception = exception;
    }

    public String getTitle() throws ContentParserException {
        if (_exception != null) {
            throw _exception;
        }
        return _title;
    }

    public Optional<String> getSubtitle() throws ContentParserException {
        if (_exception != null) {
            throw _exception;
        }
        if (_subtitle.isEmpty()) {
            return Optional.empty();
        }
        if (_subtitle.endsWith(" […]")) {
            return Optional.empty();
        }
        return Optional.of(_subtitle);
    }

    private LocalDate getDateInternal() throws ContentParserException {
        if (_exception != null) {
            throw _exception;
        }
        return _publicationDate;
    }

    public List<AuthorData> getAuthors() throws ContentParserException {
        if (_exception != null) {
            throw _exception;
        }
        return _authors;
    }

    @Override
    public Optional<TemporalAccessor> getDate() throws ContentParserException {
        return Optional.of(getDateInternal());
    }

    @Override
    public List<AuthorData> getSureAuthors() throws ContentParserException {
        return getAuthors();
    }

    @Override
    public List<ExtractedLinkData> getLinks() throws ContentParserException {
        String subtitle = null;
        if (getSubtitle().isPresent()) {
            final String s = getSubtitle().get();
            if (!s.endsWith("[…]")) { // we ignore subtitles which are an extract of the article
                subtitle = s;
            }
        }
        final String[] subtitles = (subtitle != null) ? new String[]{ subtitle }
                                                      : new String[0];
        final ExtractedLinkData linkData = new ExtractedLinkData(getTitle(),
                                                                 subtitles,
                                                                 getUrl().toString(),
                                                                 Optional.empty(),
                                                                 Optional.empty(),
                                                                 new LinkFormat[] { LinkFormat.HTML },
                                                                 new Locale[] { Locale.ENGLISH },
                                                                 Optional.empty(),
                                                                 Optional.empty());
        final List<ExtractedLinkData> list = new ArrayList<>(1);
        list.add(linkData);
        return list;
    }
}
