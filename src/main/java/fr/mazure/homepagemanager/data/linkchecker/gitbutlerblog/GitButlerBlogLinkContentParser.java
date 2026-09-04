package fr.mazure.homepagemanager.data.linkchecker.gitbutlerblog;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;

import fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever;
import fr.mazure.homepagemanager.data.dataretriever.SiteSlurper;
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
 * Data extractor for GitButler blog
 */
public class GitButlerBlogLinkContentParser extends LinkDataExtractor {

    private static final String s_sourceName = "GitButler blog";

    private final String _title;
    private final String _subtitle;
    private final Optional<TemporalAccessor> _creationDate;
    private final List<AuthorData> _authors;
    private final Locale _language;
    private final List<ExtractedLinkData> _links;

    private static final TextParser s_jsonParser
        = new TextParser("<script type=\"application/ld\\+json\">",
                         "</script>",
                         s_sourceName,
                         "JSON-LD");

    /**
     * Constructor
     * @param url URL of the link
     * @param retriever cache data retriever
     * @throws ContentParserException Failure to extract the information
     */
    public GitButlerBlogLinkContentParser(final String url,
                                          final CachedSiteDataRetriever retriever) throws ContentParserException {
        super(url, retriever);

        final SiteSlurper sluper = new SiteSlurper(getRetriever(), url);
        final String data = sluper.getContent();

        final String json = s_jsonParser.extract(data);
        final JSONObject payload;
        try {
            payload = new JSONObject(json);
        } catch (final JSONException e) {
            throw new ContentParserException("Failed to parse JSON-LD in GitButler blog page", e);
        }

        try {
            _title = payload.getString("headline");

            final String description = payload.getString("description");
            if (description == null || description.isEmpty()) {
                throw new ContentParserException("Missing description in GitButler blog JSON-LD");
            }
            _subtitle = HtmlHelper.cleanContent(description);

            final String datePublished = payload.getString("datePublished");
            _creationDate = Optional.of(ZonedDateTime.parse(datePublished, DateTimeFormatter.ISO_DATE_TIME).toLocalDate());

            final String authorName = payload.getJSONObject("author").getString("name");
            final List<AuthorData> authorList = new ArrayList<>(1);
            authorList.add(LinkContentParserUtils.parseAuthorName(authorName));
            _authors = authorList;

            final String inLanguage = payload.getString("inLanguage");
            _language = Locale.of(inLanguage.split("-")[0]);
        } catch (final JSONException e) {
            throw new ContentParserException("Failed to extract data from GitButler blog JSON-LD", e);
        }

        final ExtractedLinkData linkData = new ExtractedLinkData(_title,
                                                                 new String[] { _subtitle },
                                                                 url,
                                                                 Optional.empty(),
                                                                 Optional.empty(),
                                                                 getFormats(),
                                                                 new Locale[] { _language },
                                                                 Optional.empty(),
                                                                 Optional.empty());
        final List<ExtractedLinkData> linkList = new ArrayList<>(1);
        linkList.add(linkData);
        _links = linkList;
    }

    /**
     * Determine if the link is managed
     *
     * @param url link
     * @return true if the link is managed
     */
    public static boolean isUrlManaged(final String url) {
        return UrlHelper.hasPrefix(url, "https://blog.gitbutler.com/");
    }

    @Override
    public String getTitle() {
        return _title;
    }

    @Override
    public Optional<String> getSubtitle() {
        return Optional.of(_subtitle);
    }

    @Override
    public LinkFormat[] getFormats() {
        return new LinkFormat[] { LinkFormat.HTML };
    }

    @Override
    public Optional<TemporalAccessor> getCreationDate() {
        return _creationDate;
    }

    @Override
    public Optional<TemporalAccessor> getPublicationDate() {
        return _creationDate;
    }

    @Override
    public List<AuthorData> getSureAuthors() {
        return _authors;
    }

    @Override
    public List<AuthorData> getProbableAuthors() {
        return Collections.emptyList();
    }

    @Override
    public List<AuthorData> getPossibleAuthors() {
        return Collections.emptyList();
    }

    @Override
    public List<ExtractedLinkData> getLinks() {
        return _links;
    }

    @Override
    public Locale getLanguage() {
        return _language;
    }
}
