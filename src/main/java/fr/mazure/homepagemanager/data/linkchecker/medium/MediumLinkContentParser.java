package fr.mazure.homepagemanager.data.linkchecker.medium;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;

import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.ExtractedLinkData;
import fr.mazure.homepagemanager.data.linkchecker.LinkContentParserUtils;
import fr.mazure.homepagemanager.data.linkchecker.LinkDataExtractor;
import fr.mazure.homepagemanager.data.linkchecker.TextParser;
import fr.mazure.homepagemanager.utils.StringHelper;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkFormat;

/**
 * Data extractor for Medium
 */
public class MediumLinkContentParser extends LinkDataExtractor {

    private final String _data;
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
                         "Medium",
                         "JSON preloaded state");

    private static final TextParser s_jsonTitle
        = new TextParser("<h1 id=\"(?:\\p{XDigit}{4}|title)\" class=\"pw-post-title(?: \\p{Lower}{1,2})+\" data-testid=\"storyTitle\">",
                         "</h1>",
                         "Medium",
                         "title");

    private static final TextParser s_jsonSubtitle
        = new TextParser("<h2 id=\"(?:\\p{XDigit}{4}|subtitle)\" class=\"pw-subtitle-paragraph(?: \\p{Lower}{1,2})+\">",
                         "</h2>",
                         "Medium",
                         "subtitle");

    private static final TextParser s_netflixAuthors
        = new TextParser("<p id=\"\\p{XDigit}{4}\" class=\"pw-post-body-paragraph(?: \\p{Lower}{1,2})+\"><em class=\"\\p{Lower}{2}\">by ",
                         "</p>",
                        "Medium",
                        "Netflix authors");

    /**
     * @param url URL of the link
     * @param data retrieved link data
     */
    public MediumLinkContentParser(final String url,
                                   final String data) {
        super(url);
        _data = data;
        _code = url.substring(url.lastIndexOf("-") + 1);
    }

    @Override
    public String getTitle() throws ContentParserException {
        loadData();
        return _title;
    }

    @Override
    public Optional<String> getSubtitle() throws ContentParserException {
        loadData();
        return _subtitle;
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
        final Instant instant = Instant.ofEpochMilli(firstPublishedAt.longValueExact());
        _publicationDate = LocalDate.ofInstant(instant, ZoneId.of("Europe/Paris"));
        /* does not work, the title is out of date in the JSON payload if the author modified it after the first publication
        String title;
        try {
            title = post.getString("title");
        } catch (final JSONException e) {
            throw new ContentParserException("Failed to find \"Post:" + _code +"/title\" JSON field in Medium page", e);
        }
        _title = title.replace("\n"," ");
        */
        _title = HtmlHelper.cleanContent(s_jsonTitle.extract(_data));
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
            final Optional<String> netflixAuthors = s_netflixAuthors.extractOptional(_data);
            if (netflixAuthors.isPresent()) {
                _authors = LinkContentParserUtils.getAuthors(HtmlHelper.cleanContent((netflixAuthors.get())));
            } else {
                _authors = new ArrayList<>();
            }
        } else {
            _authors = Collections.singletonList(LinkContentParserUtils.getAuthor(name));
        }

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
        final Optional<String> subtitle = s_jsonSubtitle.extractOptional(_data);
        _subtitle = subtitle.isPresent() ? Optional.of(HtmlHelper.cleanContent(subtitle.get()))
                                         : Optional.empty();
        _locale = StringHelper.guessLanguage(HtmlHelper.cleanContent(_data)).get(); //TODO handle the case where the language is not recognized
    }

    @Override
    public Optional<TemporalAccessor> getDate() throws ContentParserException {
        return Optional.of(getPublicationDate());
    }

    @Override
    public List<AuthorData> getSureAuthors() throws ContentParserException {
        loadData();
        return _authors;
    }

    @Override
    public List<ExtractedLinkData> getLinks() throws ContentParserException {
        final ExtractedLinkData linkData = new ExtractedLinkData(getTitle(),
                                                                 new String[] { },
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
    public Locale getLanguage() throws ContentParserException {
        loadData();
        return _locale;
    }
}
