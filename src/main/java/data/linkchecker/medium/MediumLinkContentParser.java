package data.linkchecker.medium;

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

import data.linkchecker.ContentParserException;
import data.linkchecker.ExtractedLinkData;
import data.linkchecker.LinkContentParserUtils;
import data.linkchecker.LinkDataExtractor;
import data.linkchecker.TextParser;
import utils.xmlparsing.AuthorData;
import utils.xmlparsing.LinkFormat;

public class MediumLinkContentParser extends LinkDataExtractor {

    private final String _data;
    private final String _code;
    private boolean _dataIsLoaded;
    private String _title;
    private List<AuthorData> _authors;
    private LocalDate _publicationDate;

    private static final TextParser s_jsonParser
        = new TextParser("<script>window.__APOLLO_STATE__ = ",
                         "</script>",
                         "Medium",
                         "JSON preloaded state");

    public MediumLinkContentParser(final String url,
                                   final String data) {
        super(url);
        _data = data;
        _code = url.substring(url.lastIndexOf("-") + 1);
    }

    public String getTitle() throws ContentParserException {
        loadData();
        return _title;
    }

    public List<AuthorData> getAuthors() throws ContentParserException {
        loadData();
        return _authors;
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
        String title;
        try {
            title = post.getString("title");
        } catch (final JSONException e) {
            throw new ContentParserException("Failed to find \"Post:" + _code +"/title\" JSON field in Medium page", e);
        }
        _title = title.replace("\n"," ");
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
        _authors = Collections.singletonList(LinkContentParserUtils.getAuthor(name));
    }

    @Override
    public Optional<TemporalAccessor> getDate() throws ContentParserException {
        return Optional.of(getPublicationDate());
    }

    @Override
    public List<AuthorData> getSureAuthors() throws ContentParserException {
        return getAuthors();
    }

    @Override
    public List<AuthorData> getProbableAuthors() {
        return new ArrayList<>(0);
    }

    @Override
    public List<AuthorData> getPossibleAuthors()  {
        return new ArrayList<>(0);
    }

    @Override
    public List<ExtractedLinkData> getLinks() throws ContentParserException {
        final ExtractedLinkData linkData = new ExtractedLinkData(getTitle(),
                                                                 new String[] { },
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
