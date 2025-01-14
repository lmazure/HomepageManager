package fr.mazure.homepagemanager.data.linkchecker.wired;

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

import fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever;
import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.ExtractedLinkData;
import fr.mazure.homepagemanager.data.linkchecker.LinkContentParserUtils;
import fr.mazure.homepagemanager.data.linkchecker.LinkDataExtractor;
import fr.mazure.homepagemanager.data.linkchecker.TextParser;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;
import fr.mazure.homepagemanager.utils.internet.JsonHelper;
import fr.mazure.homepagemanager.utils.internet.UrlHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkFormat;

/**
 * Data extractor for Wired articles
 */
public class WiredLinkContentParser extends LinkDataExtractor {

    private static final String s_sourceName = "Wired";

    private static final TextParser s_jsonParser
        = new TextParser("<script type=\"text/javascript\">window.__PRELOADED_STATE__ =",
                         "</script>",
                         s_sourceName,
                         "JSON");

    private final String _title;
    private final String _subtitle;
    private final LocalDate _publicationDate;
    private final List<AuthorData> _authors;

    /**
     * @param url URL of the link
     * @param data retrieved link data
     * @param retriever cache data retriever
     * @throws ContentParserException Failure to extract the information
     */
    public WiredLinkContentParser(final String url,
                                  final String data,
                                  final CachedSiteDataRetriever retriever) throws ContentParserException {
        super(url, retriever);
        String title = null;
        String subtitle = null;
        LocalDate publicationDate = null;
        List<AuthorData> authors = null;
        try {
            final String json = s_jsonParser.extract(data);
            final JSONObject payload = new JSONObject(json);
            title = HtmlHelper.cleanContent(JsonHelper.getAsText(payload, "transformed", "article", "headerProps", "dangerousHed"));
            subtitle = HtmlHelper.cleanContent(JsonHelper.getAsText(payload, "transformed", "article", "headerProps", "dangerousDek"));
            final String pubDate = JsonHelper.getAsText(payload, "transformed", "head.firstPublishDate");
            publicationDate = ZonedDateTime.parse(pubDate, DateTimeFormatter.ISO_DATE_TIME).toLocalDate();
            final JSONArray authorArray = JsonHelper.getAsArray(payload, "transformed", "head.jsonld").getJSONObject(0).getJSONArray("author");
            authors = new ArrayList<>(authorArray.length());
            for (int i = 0; i < authorArray.length(); i++) {
                final String author = authorArray.getJSONObject(i).getString("name");
                if (!author.equals("WIRED Staff") &&
                    !author.equals("WIRED Ideas")) {
                    final String cleanedAuthor = author.replaceAll(", Ars Technica$", "");
                    authors.add(LinkContentParserUtils.parseAuthorName(cleanedAuthor));
                }
            }
        } catch (final IllegalStateException e) {
            throw new ContentParserException("Unexpected JSON", e);
        }
        _title = title;
        _subtitle = subtitle;
        _publicationDate = publicationDate;
        _authors = authors;
    }

    /**
     * Determine if the link is managed
     *
     * @param url link
     * @return true if the link is managed
     */
    public static boolean isUrlManaged(final String url) {
          return UrlHelper.hasPrefix(url, "https://www.wired.com/");
    }

      @Override
    public String getTitle() {
        return _title;
    }

    @Override
    public Optional<String> getSubtitle() {
        if (_subtitle.isEmpty() || _subtitle.endsWith(" […]")) {
            return Optional.empty();
        }
        return Optional.of(_subtitle);
    }

    @Override
    public Optional<TemporalAccessor> getCreationDate() {
        return Optional.of(_publicationDate);
    }

    @Override
    public Optional<TemporalAccessor> getPublicationDate() {
        return getCreationDate();
    }

    @Override
    public List<AuthorData> getSureAuthors() {
        return _authors;
    }

    @Override
    public List<ExtractedLinkData> getLinks() {
        final String[] subtitles = getSubtitle().isPresent() ? new String[]{ getSubtitle().get() }
                                                             : new String[0];
        final ExtractedLinkData linkData = new ExtractedLinkData(getTitle(),
                                                                 subtitles,
                                                                 getUrl(),
                                                                 Optional.empty(),
                                                                 Optional.empty(),
                                                                 new LinkFormat[] { LinkFormat.HTML },
                                                                 new Locale[] { getLanguage() },
                                                                 Optional.empty(),
                                                                 Optional.empty());
        final List<ExtractedLinkData> list = new ArrayList<>(1);
        list.add(linkData);
        return list;
    }

    @Override
    public Locale getLanguage() {
        return Locale.ENGLISH;
    }
}
