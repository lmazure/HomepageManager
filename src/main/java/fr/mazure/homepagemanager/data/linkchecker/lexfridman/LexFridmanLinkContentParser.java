package fr.mazure.homepagemanager.data.linkchecker.lexfridman;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever;
import fr.mazure.homepagemanager.data.knowledge.WellKnownAuthors;
import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.ExtractedLinkData;
import fr.mazure.homepagemanager.data.linkchecker.LinkDataExtractor;
import fr.mazure.homepagemanager.data.linkchecker.TextParser;
import fr.mazure.homepagemanager.data.linkchecker.youtubewatch.YoutubeWatchLinkContentParser;
import fr.mazure.homepagemanager.utils.Logger;
import fr.mazure.homepagemanager.utils.Logger.Level;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkFormat;

/**
 *  Data extractor for Lex Fridman podcast
 */
public class LexFridmanLinkContentParser extends LinkDataExtractor {

    private final String _data;

    private static final TextParser s_titleParser
        = new TextParser("<h1 class=\"entry-title\">",
                         "</h1>",
                         "Lex Fridman podcast",
                         "title");
    private static final TextParser s_dateParser
        = new TextParser("<meta property=\"article:published_time\" content=\"",
                         "T",
                         "Lex Fridman podcast",
                         "date");
    private static final TextParser s_youtubeLinkParser
        = new TextParser("\"https://www.youtube.com/embed/",
                         "\"",
                         "Lex Fridman podcast",
                         "duration");

    private static final DateTimeFormatter s_dateformatter = DateTimeFormatter.ISO_LOCAL_DATE;

    private static final List<AuthorData> s_authors = new ArrayList<>(Arrays.asList(
            WellKnownAuthors.LEX_FRIDMAN
    ));

    /**
     * @param url URL of the link
     * @param data retrieved link data
     */
    public LexFridmanLinkContentParser(final String url,
                                       final String data) {
        super(url);
        _data = data;
    }

    /**
     * Determine if the link is managed
     *
     * @param url link
     * @return true if the link is managed
     */
    public static boolean isUrlManaged(final String url) {
        return url.startsWith("https://lexfridman.com/");
    }

    @Override
    public String getTitle() throws ContentParserException {
        return HtmlHelper.cleanContent(s_titleParser.extract(_data));
    }

    @Override
    public Optional<String> getSubtitle() throws ContentParserException {
        return Optional.empty();
    }

    @Override
    public Optional<TemporalAccessor> getDate() throws ContentParserException {
        return Optional.of(LocalDate.parse(s_dateParser.extract(_data), s_dateformatter));
    }

    @Override
    public Optional<Duration> getDuration() throws ContentParserException {
        return getOtherLink().map(d -> d.duration().orElse(null));
    }

    @Override
    public List<AuthorData> getSureAuthors() throws ContentParserException {
        return s_authors;
    }

    @Override
    public List<AuthorData> getProbableAuthors() {
        return new ArrayList<>(0);
    }

    @Override
    public List<AuthorData> getPossibleAuthors() {
        return new ArrayList<>(0);
    }

    private Optional<String> getYoutubeLink() {
        return s_youtubeLinkParser.extractOptional(_data).map(s -> "https://www.youtube.com/watch?v=" + s);
    }

    private Optional<ExtractedLinkData> getOtherLink() throws ContentParserException { // TODO we need to cache in memory and on disk

        // get YouTube link
        Optional<String> youtubeLink = getYoutubeLink();
        if (youtubeLink.isEmpty()) {
            return Optional.empty();
        }

        // get YouTube payload
        String payload = null;
        try {
            payload = SynchronousSiteDataRetriever.getContent(youtubeLink.get(), false);
        } catch (final IOException e) {
            Logger.log(Level.ERROR)
                  .append("Failed to get YouTube payload")
                  .append(e)
                  .submit();
            return Optional.empty();
        }

        // extract the link data
        final YoutubeWatchLinkContentParser parser = new YoutubeWatchLinkContentParser(youtubeLink.get(), payload);
        final ExtractedLinkData linkData = new ExtractedLinkData(parser.getTitle(),
                                                                 new String[] { },
                                                                 youtubeLink.get(),
                                                                 Optional.empty(),
                                                                 Optional.empty(),
                                                                 new LinkFormat[] { LinkFormat.MP4 },
                                                                 new Locale[] { parser.getLanguage() },
                                                                 parser.getDuration(),
                                                                 Optional.empty());
        return Optional.of(linkData);
    }

    @Override
    public List<ExtractedLinkData> getLinks() throws ContentParserException {
        final ExtractedLinkData linkData = new ExtractedLinkData(getTitle(),
                                                                 new String[] {},
                                                                 getUrl(),
                                                                 Optional.empty(),
                                                                 Optional.empty(),
                                                                 new LinkFormat[] { LinkFormat.MP3 },
                                                                 new Locale[] { getLanguage() },
                                                                 getDuration(),
                                                                 Optional.empty());
        final List<ExtractedLinkData> list = new ArrayList<>(2);
        list.add(linkData);
        final Optional<ExtractedLinkData> otherLink = getOtherLink();
        if (otherLink.isPresent()) {
            list.add(otherLink.get());
        }
        return list;
    }

    @Override
    public Locale getLanguage() {
        return Locale.ENGLISH;
    }
}
