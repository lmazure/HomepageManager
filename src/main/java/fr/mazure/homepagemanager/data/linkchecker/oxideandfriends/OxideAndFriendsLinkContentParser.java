package fr.mazure.homepagemanager.data.linkchecker.oxideandfriends;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import fr.mazure.homepagemanager.data.knowledge.WellKnownAuthors;
import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.ExtractedLinkData;
import fr.mazure.homepagemanager.data.linkchecker.LinkDataExtractor;
import fr.mazure.homepagemanager.data.linkchecker.TextParser;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkFormat;

/**
 *  Data extractor for Oxide and Friends blog
 */
public class OxideAndFriendsLinkContentParser extends LinkDataExtractor {


    private final String _data;

    private static final TextParser s_titleParser
        = new TextParser("<h1 class=\"text-sans-3xl 800:text-sans-3xl 1000:text-sans-4xl text-default my-3\">",
                         "</h1>",
                         "Oxide and Friends",
                         "title");
    private static final TextParser s_dateParser
        = new TextParser("<div class=\"block uppercase text-mono-sm text-tertiary\"><span class=\"inline-block\">",
                         "</span>",
                         "Oxide and Friends",
                         "date");
    private static final TextParser s_durationParser
        = new TextParser("<span class=\"ml-2 inline-block\">",
                         "</span>",
                         "Oxide and Friends",
                         "duration");

    private static final DateTimeFormatter s_dateformatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH);

    private static final List<AuthorData> s_authors = new ArrayList<>(Arrays.asList(
        WellKnownAuthors.buildAuthor("Bryan", "Cantrill"),
        WellKnownAuthors.buildAuthor("Adam", "Leventhal")
    ));

    /**
     * @param url URL of the link
     * @param data retrieved link data
     */
    public OxideAndFriendsLinkContentParser(final String url,
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
        return url.startsWith("https://oxide.computer/podcasts/oxide-and-friends/");
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
        final String timeString = s_durationParser.extract(_data);
        final String[] parts = timeString.split(":");
        final int hours = Integer.parseInt(parts[0]);
        final int minutes = Integer.parseInt(parts[1]);
        final int seconds = Integer.parseInt(parts[2]);
        return Optional.of(Duration.ofHours(hours)
                                   .plusMinutes(minutes)
                                   .plusSeconds(seconds));
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
        final List<ExtractedLinkData> list = new ArrayList<>(1);
        list.add(linkData);
        return list;
    }

    @Override
    public Locale getLanguage() {
        return Locale.ENGLISH;
    }
}