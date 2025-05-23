package fr.mazure.homepagemanager.data.linkchecker.medium;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;

import fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever;
import fr.mazure.homepagemanager.data.knowledge.WellKnownAuthors;
import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.ExtractedLinkData;
import fr.mazure.homepagemanager.data.linkchecker.LinkContentParserUtils;
import fr.mazure.homepagemanager.data.linkchecker.LinkDataExtractor;
import fr.mazure.homepagemanager.data.linkchecker.TextParser;
import fr.mazure.homepagemanager.utils.DateTimeHelper;
import fr.mazure.homepagemanager.utils.StringHelper;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;
import fr.mazure.homepagemanager.utils.internet.UriHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkFormat;

/**
 * Data extractor for Medium
 */
public class MediumLinkContentParser extends LinkDataExtractor {

    private static final String s_sourceName = "Medium";

    private final String _code;
    private boolean _dataIsLoaded;
    private String _title;
    private Optional<String> _subtitle;
    private List<AuthorData> _authors;
    private LocalDate _publicationDate;
    private Locale _locale;

    private static final TextParser s_jsonParser
        = new TextParser("<script>window.__APOLLO_STATE__ = ",
                         "</script>",
                         s_sourceName,
                         "JSON preloaded state");

    private static final TextParser s_jsonTitle
        = new TextParser("<h1 id=\"(?:\\p{XDigit}{4}|title)\" class=\"pw-post-title(?: \\p{Lower}{1,2})+\" data-testid=\"storyTitle\">",
                         "</h1>",
                         s_sourceName,
                         "title");

    private static final TextParser s_jsonSubtitle
        = new TextParser("<h2 id=\"(?:\\p{XDigit}{4}|subtitle)\" class=\"pw-subtitle-paragraph(?: \\p{Lower}{1,2})+\">",
                         "</h2>",
                         s_sourceName,
                         "subtitle");

    private static final TextParser s_netflixAuthors
        = new TextParser("<p id=\"\\p{XDigit}{4}\" class=\"pw-post-body-paragraph(?: \\p{Lower}{1,2})+\"><em class=\"\\p{Lower}{2}\">by ",
                         "</p>",
                        s_sourceName,
                        "authors");

    private static final TextParser s_microsoftDesignAuthors
        = new TextParser("<p id=\"\\p{XDigit}{4}\" class=\"pw-post-body-paragraph(?: \\p{Lower}{1,2})+\">By ",
                        "</p>",
                        s_sourceName,
                        "authors");

    /**
     * @param url URL of the link
     * @param data retrieved link data
     * @param retriever cache data retriever
     * @throws ContentParserException Failure to extract the information
     */
    public MediumLinkContentParser(final String url,
                                   final String data,
                                   final CachedSiteDataRetriever retriever) throws ContentParserException {
        super(url, retriever);
        _code = url.substring(url.lastIndexOf("-") + 1);
        loadData(data);
    }

    /**
     * Determine if the link is managed
     *
     * @param url link
     * @return true if the link is managed
     */
    public static boolean isUrlManaged(final String url) {

        if (!UriHelper.isValidUri(url)) {
            return false;
        }

        final String host = UriHelper.getHost(url);
        if (host == null) {
            return false;
        }
        return host.endsWith("medium.com") ||
               host.equals("pub.towardsai.net") ||
               host.equals("levelup.gitconnected.com") ||
               host.equals("towardsdatascience.com");
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
        return getCreationDate();
    }

    private void loadData(final String data) throws ContentParserException {
        if (_dataIsLoaded) {
            return;
        }
        _dataIsLoaded = true;


        /* does not work, the subtitle in the FSON payload is not the subtitle, but the first paragraph, whatever is is this one
        JSONObject previewContent;
        try {
            previewContent = post.getJSONObject("previewContent");
        } catch (final JSONException e) {
            throw new ContentParserException("Failed to find \"previewContent\" JSON object in Medium page", e);
        }
        String subtitle;
        try {
            subtitle = previewContent.getString("subtitle");
        } catch (final JSONException e) {
            throw new ContentParserException("Failed to find \"previewContent/subtitle\" JSON field in Medium page", e);
        }
        _subtitle = subtitle;
        */
        final Optional<String> subtitle = s_jsonSubtitle.extractOptional(data);
        _subtitle = subtitle.isPresent() ? Optional.of(HtmlHelper.cleanContent(subtitle.get()))
                                         : Optional.empty();

        final String json = s_jsonParser.extract(data);
        final JSONObject payload = new JSONObject(json);
        JSONObject post;
        try {
            post = payload.getJSONObject("Post:" + _code);
        } catch (final JSONException e) {
            throw new ContentParserException("Failed to find \"Post:" + _code +"\" JSON object in Medium page", e);
        }
        BigInteger firstPublishedAt;
        try {
            firstPublishedAt = post.getBigInteger("firstPublishedAt");
        } catch (final JSONException e) {
            throw new ContentParserException("Failed to find \"Post:" + _code +"/firstPublishedAt\" JSON integer in Medium page", e);
        }
        _publicationDate = DateTimeHelper.convertLongToDateTime(firstPublishedAt.longValueExact());
        /* does not work, the title is out of date in the JSON payload if the author modified it after the first publication
        String title;
        try {
            title = post.getString("title");
        } catch (final JSONException e) {
            throw new ContentParserException("Failed to find \"Post:" + _code +"/title\" JSON field in Medium page", e);
        }
        _title = title.replace("\n"," ");
        */
        _title = HtmlHelper.cleanContent(s_jsonTitle.extract(data));
        JSONObject creator;
        try {
            creator = post.getJSONObject("creator");
        } catch (final JSONException e) {
            throw new ContentParserException("Failed to find \"Post:" + _code +"/creator\" JSON object in Medium page", e);
        }
        String creatorCode;
        try {
            creatorCode = creator.getString("__ref");
        } catch (final JSONException e) {
            throw new ContentParserException("Failed to find \"Post:" + _code +"/creator/__ref\" JSON field in Medium page", e);
        }
        JSONObject user;
        try {
            user = payload.getJSONObject(creatorCode);
        } catch (final JSONException e) {
            throw new ContentParserException("Failed to find \"" + creatorCode +"\" JSON object in Medium page", e);
        }
        String name;
        try {
            name = user.getString("name");
        } catch (final JSONException e) {
            throw new ContentParserException("Failed to find \"" + creatorCode +"/name\" JSON field in Medium page", e);
        }
        if (name.equals("Netflix Technology Blog")) {
            final Optional<String> netflixAuthors = s_netflixAuthors.extractOptional(data);
            if (netflixAuthors.isPresent()) {
                _authors = LinkContentParserUtils.getAuthors(HtmlHelper.cleanContent((netflixAuthors.get())));
            } else {
                _authors = Collections.emptyList();
            }
        } else if (name.equals("Betable Engineering")) {
            if (_subtitle.isPresent() && _subtitle.get().endsWith(" Mike Malone")) {
                _authors = Collections.singletonList(WellKnownAuthors.buildAuthor("Mike", "Malone"));
            } else {
                _authors = Collections.emptyList();
            }
        } else if (name.equals("Microsoft Design")) {
            final Optional<String> microsoftDesignAuthors = s_microsoftDesignAuthors.extractOptional(data);
            if (microsoftDesignAuthors.isPresent()) {
                _authors = LinkContentParserUtils.getAuthors(HtmlHelper.cleanContent((microsoftDesignAuthors.get())));
            } else {
                _authors = Collections.emptyList();
            }
        } else {
            _authors = Collections.singletonList(LinkContentParserUtils.parseAuthorName(name));
        }

        _locale = StringHelper.guessLanguage(HtmlHelper.cleanContent(data)).get(); //TODO handle the case where the language is not recognized
    }

    @Override
    public Optional<TemporalAccessor> getCreationDate() {
        return Optional.of(_publicationDate);
    }

    @Override
    public List<AuthorData> getSureAuthors() {
        return _authors;
    }

    @Override
    public List<ExtractedLinkData> getLinks() {
        final ExtractedLinkData linkData = new ExtractedLinkData(getTitle(),
                                                                 getSubtitle().isPresent() ? new String[] { getSubtitle().get() }
                                                                                           : new String[] { },
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
        return _locale;
    }
}
