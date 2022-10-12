package data.linkchecker.githubblog;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import data.linkchecker.ContentParserException;
import data.linkchecker.LinkContentParserUtils;
import data.linkchecker.TextParser;
import utils.xmlparsing.AuthorData;

public class GithubBlogLinkContentParser {

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

    public GithubBlogLinkContentParser(final String data) {
        _data = data;
    }

    public String getTitle() throws ContentParserException {
        loadData();
        return _title;
    }

    public String getSubtitle() throws ContentParserException {
        loadData();
        return _subtitle;
    }

    public AuthorData getAuthor() throws ContentParserException {
        loadData();
        return _author;
    }

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
        JSONObject article = null;
        JSONObject webPage = null;
        for (int i=0; i < post.length(); i++) {
            final JSONObject o = post.getJSONObject(i);
            final String type = o.getString("@type");
            if (type.equals("Article")) {
                article = o;
            } else if (type.equals("WebPage")) {
                webPage = o;
            }
        }
        if (article == null) {
            throw new ContentParserException("Failed to find \"@type\" JSON object in GitHub Blog page");
        }
        if (webPage == null) {
            throw new ContentParserException("Failed to find \"@WebPage\" JSON object in GitHub Blog page");
        }
        _title = article.getString("headline");
        if (_title == null) {
            throw new ContentParserException("Failed to find \"headline\" JSON object in GitHub Blog page");
        }
        _subtitle = webPage.getString("description");
        if (_subtitle == null) {
            throw new ContentParserException("Failed to find \"description\" JSON object in GitHub Blog page");
        }
        final JSONObject author = article.getJSONObject("author");
        if (author == null) {
            throw new ContentParserException("Failed to find \"author\" JSON object in GitHub Blog page");
        }
        final String authorName = author.getString("name");
        if (authorName == null) {
            throw new ContentParserException("Failed to find \"name\" JSON object in GitHub Blog page");
        }
        try {
            _author = LinkContentParserUtils.getAuthor(authorName);
        } catch (final ContentParserException e) {
            throw new ContentParserException("failed to parse author name", e);
        }
        final String datePublished  = article.getString("datePublished");
        if (datePublished == null) {
            throw new ContentParserException("Failed to find \"datePublished\" JSON object in GitHub Blog page");
        }
        _publicationDate = ZonedDateTime.parse(datePublished, DateTimeFormatter.ISO_DATE_TIME).toLocalDate();
    }
}
