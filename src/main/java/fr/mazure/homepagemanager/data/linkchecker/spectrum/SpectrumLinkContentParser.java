package fr.mazure.homepagemanager.data.linkchecker.spectrum;

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
import fr.mazure.homepagemanager.utils.DateTimeHelper;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;
import fr.mazure.homepagemanager.utils.internet.UrlHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkFormat;

/**
 * Data extractor for IEEE Spectrum articles
 */
public class SpectrumLinkContentParser extends LinkDataExtractor {

    private static final String s_sourceName = "IEEE Spectrum";

    private final String _data;

    private List<AuthorData> _authors;
    private Optional<TemporalAccessor> _publicationDate;

    private static final TextParser s_titleParser
        = new TextParser("<h1 class=\"widget__headline h1\">",
                         "</h1>",
                         s_sourceName,
                         "title");
    private static final TextParser s_subtitleParser
        = new TextParser("<h2 class=\"widget__subheadline-text h2\" data-type=\"text\">",
                         "</h2>",
                         s_sourceName,
                         "subtitle");
    private static final TextParser s_jsonParser
        = new TextParser("<script type=\"application/ld\\+json\">",
                         "</script>",
                         s_sourceName,
                        "JSON");

    /**
     * @param url URL of the link
     * @param data retrieved link data
     * @param retriever cache data retriever
     */
    public SpectrumLinkContentParser(final String url,
                                     final String data,
                                     final CachedSiteDataRetriever retriever) {
        super(UrlHelper.removeQueryParameters(url,"comments",
                                                  "comments-page"),
              retriever);
        _data = data;
        _authors = null;
        _publicationDate = null;
    }

    /**
     * Determine if the link is managed
     *
     * @param url link
     * @return true if the link is managed
     */
    public static boolean isUrlManaged(final String url) {
        return url.startsWith("https://spectrum.ieee.org/");
    }    @Override

    public String getTitle() throws ContentParserException {
        return HtmlHelper.cleanContent(s_titleParser.extract(_data));
    }

    @Override
    public Optional<String> getSubtitle() throws ContentParserException {
        return Optional.of(HtmlHelper.cleanContent(s_subtitleParser.extract(_data)));
    }

    @Override
    public Optional<TemporalAccessor> getDate() throws ContentParserException {
        if (_publicationDate != null) {
            return _publicationDate;
        }
        parseJson();
        return _publicationDate;
    }

    @Override
    public List<AuthorData> getSureAuthors() throws ContentParserException {
        if (_authors != null) {
            return _authors;
        }
        parseJson();
        return _authors;
    }

    @Override
    public List<AuthorData> getProbableAuthors() {
        return new ArrayList<>(0);
    }

    @Override
    public List<AuthorData> getPossibleAuthors() {
        return new ArrayList<>(0);
    }

    @Override
    public List<ExtractedLinkData> getLinks() throws ContentParserException {
        final ExtractedLinkData linkData = new ExtractedLinkData(getTitle(),
                                                                 new String[] { getSubtitle().get() },
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

    private void parseJson() throws ContentParserException {

        _authors = new ArrayList<>(1);

        final String json = s_jsonParser.extract(_data);
        final JSONObject payload = new JSONObject(json);
        final Object authorNode = payload.get("author");
        if (authorNode instanceof JSONArray auths) {
            for (int i = 0; i < auths.length(); i++) {
                final String name = auths.getJSONObject(i).getString("name");
                if (name.equals(s_sourceName)) {
                    continue;
                }
                final AuthorData author = LinkContentParserUtils.parseAuthorName(name);
                _authors.add(author);
            }
        } else if (authorNode instanceof JSONObject auth) {
            final String name = auth.getString("name");
            final AuthorData author = LinkContentParserUtils.parseAuthorName(name);
            _authors.add(author);
        }
        _publicationDate = Optional.of(DateTimeHelper.convertISO8601StringToDateTime(payload.getString("datePublished")));
    }
}
