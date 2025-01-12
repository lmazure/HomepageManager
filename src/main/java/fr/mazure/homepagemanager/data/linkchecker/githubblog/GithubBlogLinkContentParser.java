package fr.mazure.homepagemanager.data.linkchecker.githubblog;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever;
import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.ExtractedLinkData;
import fr.mazure.homepagemanager.data.linkchecker.LinkContentParserUtils;
import fr.mazure.homepagemanager.data.linkchecker.LinkDataExtractor;
import fr.mazure.homepagemanager.data.linkchecker.TextParser;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;
import fr.mazure.homepagemanager.utils.internet.UrlHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkFormat;

/**
 * Data extractor for Github blog
 */
public class GithubBlogLinkContentParser extends LinkDataExtractor {

    private static final String s_sourceName = "GitHub blog";
    private static final AuthorData s_githubStaff = new AuthorData(Optional.empty(), Optional.of("GitHub"), Optional.empty(), Optional.of("Staff"), Optional.empty(), Optional.empty());

    private boolean _dataIsLoaded;
    private String _title;
    private Optional<String> _subtitle;
    private List<AuthorData> _authors;
    private Optional<TemporalAccessor> _publicationDate;

    private static final TextParser s_jsonParser
        = new TextParser("<script type=\"application/ld\\+json\" class=\"yoast-schema-graph\">",
                         "</script>",
                         s_sourceName,
                         "JSON");

    private static final TextParser s_titleParser
        = new TextParser("<h1 class=\"h3-mktg lh-condensed mb-3 color-fg-default\">",
                         "</h1>",
                         s_sourceName,
                         "title");

    private static final TextParser s_subtitleParser
        = new TextParser("<div class=\"f4-mktg\">\\n\\t\\t\\t\\t\\t<p>",
                         "</p>",
                         s_sourceName,
                         "subtitle");

    private static final TextParser s_authorParser
        = new TextParser("<meta name=\"author\" content=\"",
                         "\" />",
                         s_sourceName,
                         "author");
    /**
     * @param url URL of the link
     * @param retriever cache data retriever
     * @param data retrieved link data
     *
     * @throws ContentParserException Failure to extract the information
     */
    public GithubBlogLinkContentParser(final String url,
                                       final String data,
                                       final CachedSiteDataRetriever retriever) throws ContentParserException {
        super(url, retriever);

        loadData(data);
    }

    /**
     * Determine if the link is managed
     *
     * @param url link
     * @return true if the link is managed
     */
    public static boolean isUrlManaged(final String url) {
        return UrlHelper.hasPrefix(url, "https://github.blog/");
    }

    @Override
    public String getTitle() {
        return _title;
    }

    @Override
    public Optional<String> getSubtitle() {
        return _subtitle;
    }

    @Override
    public Optional<TemporalAccessor> getPublicationDate() {
        return _publicationDate;
    }

    private void loadData(final String data) throws ContentParserException {
        if (_dataIsLoaded) {
            return;
        }
        _dataIsLoaded = true;

        final String json = s_jsonParser.extract(data);
        final JSONObject payload = new JSONObject(json);
        JSONArray post;
        try {
            post = payload.getJSONArray("@graph");
        } catch (final JSONException e) {
            throw new ContentParserException("Failed to find \"@graph\" JSON object in GitHub Blog page", e);
        }
        JSONObject webPage = null;
        for (int i = 0; i < post.length(); i++) {
            final JSONObject o = post.getJSONObject(i);
            final String type = o.getString("@type");
            if (type.equals("WebPage")) {
                webPage = o;
                break;
            }
        }
        if (webPage == null) {
            throw new ContentParserException("Failed to find \"@WebPage\" JSON object in GitHub Blog page");
        }

        _title = HtmlHelper.cleanContent(s_titleParser.extract(data));
        _subtitle = Optional.of(HtmlHelper.cleanContent(s_subtitleParser.extract(data)));
        if (_subtitle.get().endsWith("…")) {
            // this is not a real subtitle, but, in fact, the beginning of the article
            _subtitle = Optional.empty();
           }
        final String authorString = s_authorParser.extract(data);
        _authors = LinkContentParserUtils.getAuthors(authorString);
        _authors.remove(s_githubStaff);
        final String datePublished  = webPage.getString("datePublished");
        if (datePublished == null) {
            throw new ContentParserException("Failed to find \"datePublished\" JSON object in GitHub Blog page");
        }
        _publicationDate = Optional.of(ZonedDateTime.parse(datePublished, DateTimeFormatter.ISO_DATE_TIME).toLocalDate());
    }

    @Override
    public Optional<TemporalAccessor> getCreationDate(){
        return _publicationDate;
    }

    @Override
    public List<AuthorData> getSureAuthors() {
        return _authors;
    }

    @Override
    public List<ExtractedLinkData> getLinks() {
        final ExtractedLinkData linkData = new ExtractedLinkData(getTitle(),
                                                                 getSubtitle().isPresent() ? new String[] { getSubtitle().get() }
                                                                                           : new String[] {},
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
