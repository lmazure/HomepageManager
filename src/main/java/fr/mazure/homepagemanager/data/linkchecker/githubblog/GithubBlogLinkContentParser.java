package fr.mazure.homepagemanager.data.linkchecker.githubblog;

import java.time.LocalDate;
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

import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.ExtractedLinkData;
import fr.mazure.homepagemanager.data.linkchecker.LinkContentParserUtils;
import fr.mazure.homepagemanager.data.linkchecker.LinkDataExtractor;
import fr.mazure.homepagemanager.data.linkchecker.TextParser;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkFormat;

/**
* Data extractor for Github blog
*/
public class GithubBlogLinkContentParser extends LinkDataExtractor {

    private final String _data;
    private boolean _dataIsLoaded;
    private String _title;
    private String _subtitle;
    private AuthorData _author;
    private LocalDate _publicationDate;

    private static final TextParser s_jsonParser
        = new TextParser("<script type=\"application/ld\\+json\" class=\"yoast-schema-graph\">",
                         "</script>",
                         "GitHub blog",
                         "JSON");

    private static final TextParser s_titleParser
        = new TextParser("<h1 class=\"h3-mktg lh-condensed mb-3 color-fg-default\">",
                         "</h1>",
                         "GitHub blog",
                         "title");

    private static final TextParser s_subtitleParser
        = new TextParser("<p class=\"f4-mktg\">",
                         "</p>",
                         "GitHub blog",
                         "subtitle");

    /**
     * @param url URL of the link
     * @param data retrieved link data
     */
    public GithubBlogLinkContentParser(final String url,
                                       final String data) {
        super(url);
        _data = data;
    }

    @Override
    public String getTitle() throws ContentParserException {
        loadData();
        return _title;
    }

    @Override
    public Optional<String> getSubtitle() throws ContentParserException {
        loadData();
        if (_subtitle.endsWith("…")) {
         // this is not a real subtitle, but, in fact, the beginning of the article
            return Optional.empty();
        }
        return Optional.of(_subtitle);
    }

    /**
     * @return publication date, empty if there is none
     * @throws ContentParserException Failure to extract the information
     */
    public LocalDate getPublicationDate() throws ContentParserException {
        loadData();
        return _publicationDate;
    }

    private void loadData() throws ContentParserException {
        if (_dataIsLoaded) {
            return;
        }
        _dataIsLoaded = true;

        final String json = s_jsonParser.extract(_data);
        final JSONObject payload = new JSONObject(json);
        JSONArray post;
        try {
            post = payload.getJSONArray("@graph");
        } catch (final JSONException e) {
            throw new ContentParserException("Failed to find \"@graph\" JSON object in GitHub Blog page", e);
        }
        JSONObject person = null;
        JSONObject webPage = null;
        for (int i=0; i < post.length(); i++) {
            final JSONObject o = post.getJSONObject(i);
            final String type = o.getString("@type");
            if (type.equals("Person")) {
                person = o;
            } else if (type.equals("WebPage")) {
                webPage = o;
            }
        }
        if (person == null) {
            throw new ContentParserException("Failed to find \"@Person\" JSON object in GitHub Blog page");
        }
        if (webPage == null) {
            throw new ContentParserException("Failed to find \"@WebPage\" JSON object in GitHub Blog page");
        }
        /* This does not work: ’ is replaced by '
        final String title = webPage.getString("name");
        if (title == null) {
            throw new ContentParserException("Failed to find \"name\" JSON object in GitHub Blog page");
        }
        if (!title.endsWith(" - The GitHub Blog")) {
            throw new ContentParserException("Title does not end with \" - The GitHub Blog\" in GitHub Blog page");
        }
        _title = HtmlHelper.unescape(title.replace(" - The GitHub Blog", ""));
        */
        /* this does not work, the subtitle in the JSON payload is sometimes incorrect
        final String subtitle = webPage.getString("description");
        if (subtitle == null) {
            throw new ContentParserException("Failed to find \"description\" JSON object in GitHub Blog page");
        }
        _subtitle = HtmlHelper.unescape(subtitle);
        */
        _title = HtmlHelper.cleanContent(s_titleParser.extract(_data));
        _subtitle = HtmlHelper.cleanContent(s_subtitleParser.extract(_data));
        final String authorName = person.getString("name");
        if (authorName == null) {
            throw new ContentParserException("Failed to find \"name\" JSON object in GitHub Blog page");
        }
        try {
            _author = LinkContentParserUtils.getAuthor(authorName);
        } catch (final ContentParserException e) {
            throw new ContentParserException("failed to parse author name", e);
        }
        final String datePublished  = webPage.getString("datePublished");
        if (datePublished == null) {
            throw new ContentParserException("Failed to find \"datePublished\" JSON object in GitHub Blog page");
        }
        _publicationDate = ZonedDateTime.parse(datePublished, DateTimeFormatter.ISO_DATE_TIME).toLocalDate();
    }

    @Override
    public Optional<TemporalAccessor> getDate() throws ContentParserException {
        return Optional.of(getPublicationDate());
    }

    @Override
    public List<AuthorData> getSureAuthors() throws ContentParserException {
        loadData();
        final List<AuthorData> list = new ArrayList<>(1);
        list.add(_author);
        return list;
    }

    @Override
    public List<ExtractedLinkData> getLinks() throws ContentParserException {
        final ExtractedLinkData linkData = new ExtractedLinkData(getTitle(),
                                                                 getSubtitle().isPresent() ? new String[] { getSubtitle().get() }
                                                                                           : new String[] {},
                                                                 getUrl().toString(),
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
