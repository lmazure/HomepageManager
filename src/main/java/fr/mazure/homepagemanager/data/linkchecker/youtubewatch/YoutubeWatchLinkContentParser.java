package fr.mazure.homepagemanager.data.linkchecker.youtubewatch;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever;
import fr.mazure.homepagemanager.data.knowledge.WellKnownAuthors;
import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.ExtractedLinkData;
import fr.mazure.homepagemanager.data.linkchecker.LinkDataExtractor;
import fr.mazure.homepagemanager.data.linkchecker.TextParser;
import fr.mazure.homepagemanager.utils.DateTimeHelper;
import fr.mazure.homepagemanager.utils.StringHelper;
import fr.mazure.homepagemanager.utils.internet.JsonHelper;
import fr.mazure.homepagemanager.utils.internet.UrlHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkFormat;

/**
 * Data extractor for YouTube videos
 */
public class YoutubeWatchLinkContentParser extends LinkDataExtractor {

    private static final TextParser s_jsonParser
        = new TextParser("<script nonce=\"[-_a-zA-Z0-9]+\">var ytInitialPlayerResponse = ",
                         ";var [^<]+?</script>",
                         "YouTube",
                         "JSON");

    private final boolean _isPrivate;
    private final boolean _isPlayable;
    private final String _channel;
    private final String _title;
    private final String _description;
    private final Locale _language;
    private final Optional<Locale> _subtitlesLanguage;
    private final LocalDate _uploadDate;
    private final LocalDate _publishDate;
    private final LocalDate _startBroadcastDate;
    private final LocalDate _endBroadcastDate;
    private final Duration _minDuration;
    private final Duration _maxDuration;
    private final ContentParserException _exception;

    /**
     * @param url URL of the link
     * @param data retrieved link data
     * @param retriever cache data retriever
     * @throws ContentParserException Failure to extract the information
     */
    public YoutubeWatchLinkContentParser(final String url,
                                         final String data,
                                         final CachedSiteDataRetriever retriever) throws ContentParserException {
        super(UrlHelper.removeQueryParameters(url, "app",
                                                   "embeds_referring_euri",
                                                   "embeds_referring_origin",
                                                   "feature",
                                                   "index",
                                                   "list",
                                                   "si",
                                                   "source_ve_path",
                                                   "t"),
              retriever);
        ContentParserException exception = null;
        String channel = null;
        String title = null;
        String description = null;
        Optional<Locale> subtitlesLanguage = Optional.empty();
        int minDuration = Integer.MAX_VALUE;
        int maxDuration = Integer.MIN_VALUE;
        LocalDate uploadDate = null;
        LocalDate publishDate = null;
        LocalDate startBroadcastDate = null;
		LocalDate endBroadcastDate = null;
        boolean isPrivate = false;
        boolean isPlayable = false;

        try {
            final String json = s_jsonParser.extract(data);
            final JSONObject payload = new JSONObject(json);
            if (!payload.has("videoDetails")) {
                isPrivate = true;
            } else {
                final JSONObject jsonObject = JsonHelper.getAsNode(payload, "videoDetails");
                channel = jsonObject.getString("author");
                title = jsonObject.getString("title");
                description = jsonObject.getString("shortDescription");
            }
            if (payload.has("captions")) {
                final JSONArray captions = JsonHelper.getAsArray(payload, "captions", "playerCaptionsTracklistRenderer", "captionTracks");
                String language = null;
                for (int i = 0; i < captions.length(); i++) {
                    final String lang = captions.getJSONObject(i).getJSONObject("name").getString("simpleText");
                    if (lang.endsWith(" (auto-generated)")) {
                        if (language != null) {
                            throw new ContentParserException("Subtitles autogenerated in two languages: \"" + language + "\" and \"" + lang + "\"");
                        }
                        language = lang;
                    }
                }
                if (language != null) {
                    subtitlesLanguage = switch (language) {
                        case "Bulgarian (auto-generated)" -> Optional.of(new Locale.Builder().setLanguage("bg").build());
                        case "Dutch (auto-generated)" -> Optional.of(new Locale.Builder().setLanguage("nl").build());
                        case "English (auto-generated)" -> Optional.of(Locale.ENGLISH);
                        case "French (auto-generated)" -> Optional.of(Locale.FRENCH);
                        case "German (auto-generated)" -> Optional.of(Locale.GERMAN);
                        case "Hindi (auto-generated)" -> Optional.of(new Locale.Builder().setLanguage("hi").build());
                        case "Korean (auto-generated)" -> Optional.of(Locale.KOREAN);
                        case "Persian (auto-generated)" -> Optional.of(new Locale.Builder().setLanguage("fa").build());
                        case "Portuguese (auto-generated)" -> Optional.of(new Locale.Builder().setLanguage("pt").build());
                        case "Vietnamese (auto-generated)" -> Optional.of(new Locale.Builder().setLanguage("vi").build());
                        default -> throw new ContentParserException("Unknown language for autogenerated subtitles: \"" + language + "\"");
                    };
                }
            }
            if (payload.has("streamingData")) {
                final JSONArray formats = JsonHelper.getAsArray(payload, "streamingData", "formats");
                for (int i = 0; i < formats.length(); i++) {
                    final JSONObject format = formats.getJSONObject(i);
                    final int duration = JsonHelper.getAsTextInt(format, "approxDurationMs");
                    if (duration < minDuration) {
                        minDuration = duration;
                    }
                    if (duration > maxDuration) {
                        maxDuration = duration;
                    }
                }
            }
            if (!isPrivate) {
                final JSONObject player = JsonHelper.getAsNode(payload, "microformat", "playerMicroformatRenderer");
                uploadDate = parseDateTimeString(JsonHelper.getAsText(player, "uploadDate"));
                publishDate = parseDateTimeString(JsonHelper.getAsText(player, "publishDate"));
                if (player.has("liveBroadcastDetails")) {
                    final JSONObject liveBroadcastDetails = JsonHelper.getAsNode(player, "liveBroadcastDetails");
                    startBroadcastDate = parseDateTimeString(JsonHelper.getAsText(liveBroadcastDetails, "startTimestamp"));
                    endBroadcastDate = parseDateTimeString(JsonHelper.getAsText(liveBroadcastDetails, "endTimestamp"));
                }
            }
            final String status = JsonHelper.getAsText(payload, "playabilityStatus", "status");
            isPlayable = status.equals("OK") ||
                         status.equals("LOGIN_REQUIRED"); // this is the status for sensible videos
        } catch (final IllegalStateException e) {
            exception = new ContentParserException("Unexpected JSON", e);
        } catch (final ContentParserException e) {
            exception = e;
        }
        _channel = channel;
        _title = title;
        _description = description;
        _subtitlesLanguage = subtitlesLanguage;
        final Optional<Locale> lang2 = (description != null) ? StringHelper.guessLanguage(description)
                                                             : Optional.empty();
        if (lang2.isPresent()) {
            _language = lang2.get();
        } else {
            final Optional<Locale> lang3 = (title != null) ? StringHelper.guessLanguage(title)
                                                           : Optional.empty();
            _language = lang3.isPresent() ? lang3.get() : Locale.ENGLISH;
        }
        _minDuration = Duration.ofMillis(minDuration);
        _maxDuration = Duration.ofMillis(maxDuration);
        _uploadDate = uploadDate;
        _publishDate = publishDate;
        _startBroadcastDate = startBroadcastDate;
        _endBroadcastDate = endBroadcastDate;
        _isPrivate = isPrivate;
        _isPlayable = isPlayable;

        _exception = exception;
    }

    /**
     * Determine if the link is managed
     *
     * @param url link
     * @return true if the link is managed
     */
    public static boolean isUrlManaged(final String url) {
        return url.startsWith("https://www.youtube.com/watch?");
    }

    static final LocalDate parseDateTimeString(final String str) throws ContentParserException {

        // case the date is formatted as YYYY-MM-DD
        if (str.length() == 10) {
            return LocalDate.parse(str);
        }

        // case the date is formatted as YYYY-MM-DDThh:mm:ss-XX:XX
        if (str.length() == 25) {
            return DateTimeHelper.convertISO8601StringToDateTime(str);
        }

        throw new ContentParserException("Unknown date format: \"" + str + "\"");
    }

    /**
     * @return is the video public or private?
     * @throws ContentParserException Failure to extract the information
     */
    public boolean isPrivate() throws ContentParserException {
        if (_exception != null) {
            throw _exception;
        }
        return _isPrivate;
    }

    /**
     * @return is the video playable or blocked?
     * @throws ContentParserException Failure to extract the information
     */
    public boolean isPlayable() throws ContentParserException {
        if (_exception != null) {
            throw _exception;
        }
        return _isPlayable;
    }

    /**
     * @return channel of the video
     * @throws ContentParserException Failure to extract the information
     */
    public String getChannel() throws ContentParserException {
        if (_exception != null) {
            throw _exception;
        }
        return _channel;
    }

    @Override
    public String getTitle() throws ContentParserException {
        if (_exception != null) {
            throw _exception;
        }
        return _title;
    }

    @Override
    public Optional<String> getSubtitle() {
        return Optional.empty();
    }

    /**
     * @return description of the video
     * @throws ContentParserException Failure to extract the information
     */
    public String getDescription() throws ContentParserException {
        if (_exception != null) {
            throw _exception;
        }
        return _description;
    }

    /**
     * @return upload date of the video
     * @throws ContentParserException Failure to extract the information
     */
    public LocalDate getUploadDateInternal() throws ContentParserException {
        if (_exception != null) {
            throw _exception;
        }
        return _uploadDate;
    }

    /**
     * @return publication date of the video
     * @throws ContentParserException Failure to extract the information
     */
    public LocalDate getPublishDateInternal() throws ContentParserException {
        if (_exception != null) {
            throw _exception;
        }
        return _publishDate;
    }

    /**
     * @return start date of the video broacast if it was a broacast, empty otherwise
     * @throws ContentParserException Failure to extract the information
     */
    public Optional<LocalDate> getStartBroadcastDateInternal() throws ContentParserException {
        if (_exception != null) {
            throw _exception;
        }
        return Optional.ofNullable(_startBroadcastDate);
    }

    /**
     * @return end date of the video broacast if it was a broacast, empty otherwise
     * @throws ContentParserException Failure to extract the information
     */
    public Optional<LocalDate> getEndBroadcastDateInternal() throws ContentParserException {
        if (_exception != null) {
            throw _exception;
        }
        return Optional.ofNullable(_endBroadcastDate);
    }
    /**
     * return the duration of the video (we return the min value)
     *
     * @return duration of the video
     * @throws ContentParserException Failure to extract the information
     */
    @Override
    public Optional<Duration> getDuration() throws ContentParserException {
        if (_exception != null) {
            throw _exception;
        }
        return Optional.of(getMinDuration());
    }

    /**
     * @return minimum duration of the video
     * @throws ContentParserException Failure to extract the information
     */
    public Duration getMinDuration() throws ContentParserException {
        if (_exception != null) {
            throw _exception;
        }
        return _minDuration;
    }

    /**
     * @return maximum duration of the video
     * @throws ContentParserException Failure to extract the information
     */
    public Duration getMaxDuration() throws ContentParserException {
        if (_exception != null) {
            throw _exception;
        }
        return _maxDuration;
    }

    @Override
    public Locale getLanguage() throws ContentParserException {
        if (_exception != null) {
            throw _exception;
        }
        return _language;
    }

    /**
     * @return language of the subtitles of the video
     * @throws ContentParserException Failure to extract the information
     */
    public Optional<Locale> getSubtitlesLanguage() throws ContentParserException {
        if (_exception != null) {
            throw _exception;
        }
        return _subtitlesLanguage;
    }

    private static final Map<String, ChannelData> _channelData = Map.ofEntries(
            new AbstractMap.SimpleEntry<>("1littlecoder",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Abdul Majed", "Raja")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("3Blue1Brown",
                                          new ChannelData(buildList(WellKnownAuthors.GRANT_SANDERSON),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("AI Coffee Break with Letitia",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Letitia", "Parcalabescu")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Ai Flux",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthorFromFirstName("Noah")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("AI Jason",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Jason", "Zhou")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Andrej Karpathy",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Andrej", "Karpathy")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("ApresLaBiere",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Jean-Lou", "Fourquet")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("ArjanCodes",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Arjan", "Egges")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("AstronoGeek",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Arnaud", "Thiry")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Aurelien_Sama",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Aurélien", "Sama")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Aypierre",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Aymeric", "Pierre")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("BABOR LELEFAN",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Clément", "Berut")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Balade Mentale",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Théo", "Drieu")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Beyond Fireship",
                                          new ChannelData(buildList(WellKnownAuthors.JEFF_DELANEY),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("blackpenredpen",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Steve", "Chow")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("BLAST, Le souffle de l'info",
                                          new ChannelData(buildList(),
                                                          buildMatchingList(match("Moritz", WellKnownAuthors.buildAuthor("Paloma", "Moritz"))),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Chat Sceptique",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Nathan", "Uyttendaele")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Carberra",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Ethan", "Henderson")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Clément Freze",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Clément", "Freze")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Computerphile",
                                          new ChannelData(buildList(),
                                                          buildMatchingList(match("Altenkirch", WellKnownAuthors.buildAuthor("Thorsten", "Altenkirch")),
                                                                            match("Bagley ", WellKnownAuthors.buildAuthor("Steve", "Bagley")),
                                                                            match("Brailsford", WellKnownAuthors.buildAuthor("David", "F.", "Brailsford")),
                                                                            match("Clegg", WellKnownAuthors.buildAuthor("Richard", "G.", "Clegg")),
                                                                            match("Miles", WellKnownAuthors.buildAuthor("Robert", "Miles")),
                                                                            match("Muller", WellKnownAuthors.buildAuthor("Tim", "Muller")),
                                                                            match("Mike Pound", WellKnownAuthors.buildAuthor("Mike", "Pound")),
                                                                            match("Lewis Stuart", WellKnownAuthors.buildAuthor("Lewis", "Stuart")),
                                                                            match("Laurence Tratt", WellKnownAuthors.buildAuthor("Laurence", "Tratt")),
                                                                            match("Kai Xu", WellKnownAuthors.buildAuthor("Kai", "Xu"))),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Continuous Delivery",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Dave", "Farley")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("DeepSkyVideos",
                                          new ChannelData(buildList(),
                                                          buildMatchingList(match("Crowther", WellKnownAuthors.buildAuthor("Paul", "Crowther")),
                                                                            match("Gray", WellKnownAuthors.buildAuthor("Meghan", "Gray")),
                                                                            match("Merrifield", WellKnownAuthors.MICHAEL_MERRIFIELD)),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("DEFAKATOR",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthorFromGivenName("Defakator")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("DirtyBiology",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Léo", "Grasset")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Domain of Science",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Dominic", "Walliman")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Dr. Becky",
                                          new ChannelData(buildList(WellKnownAuthors.BECKY_SMETHURST),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Dr Peyam",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Peyam", "Ryan", "Tabrizian")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("El Jj",
                                          new ChannelData(buildList(WellKnownAuthors.JEROME_COTTANCEAU),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Fireship",
                                          new ChannelData(buildList(WellKnownAuthors.JEFF_DELANEY),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("GOTO Conferences",
                                          new ChannelData(buildList(),
                                                          buildMatchingList(match("Dave Farley", WellKnownAuthors.buildAuthor("Dave", "Farley"))),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Greg Kamradt",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Greg", "Kamradt")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Heu?reka",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Gilles", "Mitteau")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("History of the Earth",
                                          new ChannelData(buildList(WellKnownAuthors.DAVID_KELLY,
                                                                    WellKnownAuthors.buildAuthor("Pete", "Kelly"),
                                                                    WellKnownAuthors.LEILA_BATTISON),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("History of the Universe",
                                          new ChannelData(buildList(WellKnownAuthors.DAVID_KELLY,
                                                                    WellKnownAuthors.LEILA_BATTISON),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Holger Voormann (howlger)",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Holger", "Voormann")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Homo Fabulus",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Stéphane", "Debove")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Hygiène Mentale",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Christophe", "Michel")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("IBM Technology",
                                          new ChannelData(buildList(),
                                                          buildMatchingList(match("Broker", WellKnownAuthors.buildAuthor("Carl", "Broker")),
                                                                            match("Clyburn", WellKnownAuthors.buildAuthor("Cedric", "Clyburn")),
                                                                            match("Keen", WellKnownAuthors.buildAuthor("Martin", "Keen"))),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Jamy - Epicurieux",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Jamy", "Gourmaud")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("J'm'énerve pas, j'explique",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Bertrand", "Augustin")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Jon Perry - Genetics & Evolution Stated Casually",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Jon", "Perry")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Kevin Fang",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Kevin", "Fang")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Kyle Hill",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Kyle", "Hill")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("La Chaine EV",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Jean-Christophe", "Gigniac"),
                                                                    WellKnownAuthors.buildAuthor("Alexandre", "Homberger")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("La Tronche en Biais",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Thomas", "C.", "Durand")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Le Dessous des Cartes - ARTE",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Émilie", "Aubry")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Le Réveilleur",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Rodolphe", "Meyer")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Le Sense Of Wonder",
                                          new ChannelData(buildList(),
                                                          buildMatchingList(match("", WellKnownAuthors.buildAuthor("Sébastien", "Carassou")),
                                                                            match("", WellKnownAuthors.buildAuthor("Étienne", "Ledolley"))),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Linguisticae",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Romain", "Filstroff")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Mathador",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Franck", "Dunas")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Mathologer",
                                          new ChannelData(buildList(WellKnownAuthors.BURKARD_POLSTER),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Mathologer 2",
                                          new ChannelData(buildList(WellKnownAuthors.BURKARD_POLSTER),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Matt_Parker_2",
                                          new ChannelData(buildList(WellKnownAuthors.MATT_PARKER),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Matt Williams",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Matt", "Williams")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Matthew Berman",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Matthew", "Berman")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("minutephysics",
                                          new ChannelData(buildList(),
                                                          buildMatchingList(match("", WellKnownAuthors.buildAuthor("Henry", "Reich"))),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Mickaël Launay (Micmaths)",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Mickaël", "Launay")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Michael Ryan Clarkson",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Michael", "Ryan", "Clarkson")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Monsieur Bidouille",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Dimitri", "Ferrière")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Monsieur Phi",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Thibaut", "Giraud")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Nota Bonus",
                                          new ChannelData(buildList(WellKnownAuthors.BENJAMIN_BRILLAUD),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Numberphile",
                                          new ChannelData(buildList(),
                                                          buildMatchingList(match("Crawford", WellKnownAuthors.buildAuthor("Tom", "Crawford")),
                                                                            match("Sautoy", WellKnownAuthors.buildAuthor("Marcus", "du Sautoy")),
                                                                            match("Eisenbud", WellKnownAuthors.buildAuthor("David", "Eisenbud")),
                                                                            match("Feng", WellKnownAuthors.buildAuthor("Tony", "Feng")),
                                                                            match("Frenkel", WellKnownAuthors.buildAuthor("Edward", "Frenkel")),
                                                                            match("Grime", WellKnownAuthors.JAMES_GRIME),
                                                                            match("Karagila", WellKnownAuthors.buildAuthor("Asaf", "Karagila")),
                                                                            match("Krieger", WellKnownAuthors.buildAuthor("Holly", "Krieger")),
                                                                            match("Lichtman", WellKnownAuthors.buildAuthor("Jared", "Duker", "Lichtman")),
                                                                            match("MacDonald", WellKnownAuthors.AYLIEAN_MACDONALD),
                                                                            match("Maclean", WellKnownAuthors.buildAuthor("Sophie", "Maclean")),
                                                                            match("Parker", WellKnownAuthors.MATT_PARKER),
                                                                            match("Grant", WellKnownAuthors.GRANT_SANDERSON),
                                                                            match("Segerman", WellKnownAuthors.buildAuthor("Henry", "Segerman")),
                                                                            match("Padilla", WellKnownAuthors.TONY_PADILLA),
                                                                            match("Stoll", WellKnownAuthors.buildAuthor("Cliff", "Stoll")),
                                                                            match("Sloane", WellKnownAuthors.buildAuthor("Neil", "Sloane")),
                                                                            match("Sparks", WellKnownAuthors.BEN_SPARKS),
                                                                            match("Woolley", WellKnownAuthors.buildAuthor("Thomas", "Woolley"))),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Numberphile2",
                                          new ChannelData(buildList(),
                                                          buildMatchingList(match("Parker", WellKnownAuthors.MATT_PARKER),
                                                                            match("MacDonald", WellKnownAuthors.AYLIEAN_MACDONALD)),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Osons Causer",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Ludo", "Torbey")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Passe-Science",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Thomas", "Cabaret")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("PBS Eons",
                                          new ChannelData(buildList(),
                                                          buildMatchingList(match("Barboza-Ramirez", WellKnownAuthors.buildAuthor("Michelle", "Barboza-Ramirez")),
                                                                            match("Moore", WellKnownAuthors.buildAuthor("Kallie", "Moore")),
                                                                            match("de Pastino", WellKnownAuthors.buildAuthor("Blake", "de Pastino"))),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Periodic Videos",
                                          new ChannelData(buildList(),
                                                          buildMatchingList(match("Poliakoff", WellKnownAuthors.buildAuthor("Martyn", "Poliakoff")),
                                                                            match("Barnes", WellKnownAuthors.buildAuthor("Neil", "Barnes"))),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("permaculture agroécologie etc...",
                                          new ChannelData(buildList(),
                                                          buildMatchingList(match("", WellKnownAuthors.buildAuthor("Damien", "Dekarz"))),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Philoxime",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Maxime", "Lambrecht")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Plainly Difficult",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthorFromFirstName("John")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Primer",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Justin", "Helps")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Python With Liu",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Liu", "Zuo Lin")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Quadriviuum Tremens",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Tristan", "Audam-Dabidin"),
                                                                    WellKnownAuthors.buildAuthor("Keshika", "Dabidin")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Rabbit Hole Syndrome",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Greg", "Richardson")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Robert Miles AI Safety",
                                          new ChannelData(buildList(WellKnownAuthors.ROBERT_MILES),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Robert Miles 2",
                                          new ChannelData(buildList(WellKnownAuthors.ROBERT_MILES),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Sabine Hossenfelder",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Sabine", "Hossenfelder")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Sam Witteveen",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Sam", "Witteveen")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("ScienceClic",
                                          new ChannelData(buildList(WellKnownAuthors.ALESSANDRO_ROUSSEL),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("ScienceClic Plus",
                                          new ChannelData(buildList(WellKnownAuthors.ALESSANDRO_ROUSSEL),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Science de comptoir",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Valentine", "Delattre")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("ScienceEtonnante",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("David", "Louapre")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Science4All",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Lê", "Nguyên Hoang")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("SciShow",
                                          new ChannelData(buildList(),
                                                          buildMatchingList(match("Hank", WellKnownAuthors.buildAuthor("Hank", "Green")),
                                                                            match("Elcock", WellKnownAuthors.buildAuthor("Jaida", "Elcock")),
                                                                            match("Reid", WellKnownAuthors.buildAuthor("Reid", "Reimers")),
                                                                            match("Geary", WellKnownAuthors.buildAuthor("Savannah", "Geary")),
                                                                            match("Stefan Chin", WellKnownAuthors.buildAuthor("Stefan", "Chin"))),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Scilabus",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Viviane", "Lalande")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("singingbanana",
                                          new ChannelData(buildList(WellKnownAuthors.JAMES_GRIME),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Sixty Symbols",
                                          new ChannelData(buildList(),
                                                          buildMatchingList(match("Chapman ", WellKnownAuthors.buildAuthor("Emma", "Chapman")),
                                                                            match("Copeland", WellKnownAuthors.buildAuthor("Edmund", "Copeland")),
                                                                            match("Gray", WellKnownAuthors.buildAuthor("Meghan", "Gray")),
                                                                            match("Merrifield", WellKnownAuthors.MICHAEL_MERRIFIELD),
                                                                            match("Moriarty", WellKnownAuthors.buildAuthor("Philip", "Moriarty")),
                                                                            match("Padilla", WellKnownAuthors.TONY_PADILLA),
                                                                            match("Smethurst", WellKnownAuthors.BECKY_SMETHURST),
                                                                            match("Unwin", WellKnownAuthors.buildAuthor("James", "Unwin"))),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Stand-up Maths",
                                          new ChannelData(buildList(WellKnownAuthors.MATT_PARKER),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Stated Clearly",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Jon", "Perry")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Steve Mould",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Steve", "Mould")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Super Data Science: ML & AI Podcast with Jon Krohn",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Jon", "Krohn")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("TechWorld with Nana",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Nana", "Janashia")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("ThePrimeTime",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Michael", "B.", "Paulson")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Thomaths",
                                          new ChannelData(buildList(),
                                                          buildMatchingList(match("", WellKnownAuthors.buildAuthor("Alexander", "Thomas")),
                                                                            match("Eve", WellKnownAuthors.buildAuthor("Eve", "Grigy-Kissian"))),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Tom Scott",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Tom", "Scott")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Tric Trac",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Guillaume", "Chifoumi"),
                                                                    WellKnownAuthors.buildAuthor("François", "Décamp")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Veritasium",
                                          new ChannelData(buildList(WellKnownAuthors.DEREK_MULLER),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("VERY MATH TRIP - Manu Houdart",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Manu", "Houdart")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Vous Avez Le Droit",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Sébastien", "Canévet")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Web Dev Simplified",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Kyle", "Cook")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Welch Labs",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Stephen", "Welch")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Wes Roth",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Wes", "Roth")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("WonderWhy",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthorFromGivenName("WonderWhy")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Yannic Kilcher",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Yannic", "Kilcher")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Yosha - Echecs",
                                          new ChannelData(buildList(WellKnownAuthors.buildAuthor("Yosha", "Iglesias")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH))
            );

    @Override
    public Optional<TemporalAccessor> getDate() throws ContentParserException {
        if (getStartBroadcastDateInternal().isPresent()) {
	        return Optional.of(getStartBroadcastDateInternal().get());
        }
        return Optional.of(getUploadDateInternal());
    }

    @Override
    public List<AuthorData> getSureAuthors() throws ContentParserException {
        final String channel = getChannel();
        if (_channelData.containsKey(channel)) {
            return _channelData.get(channel).getAuthors();
        }
        return Collections.emptyList();
    }

    @Override
    public List<AuthorData> getProbableAuthors() throws ContentParserException {
        final List<AuthorData> authors = new ArrayList<>();
        final String channel = getChannel();
        if (channel.equals("Java")) {
            final String title = getTitle();
            if (title.contains("Inside Java Newscast")) {
                return buildList(WellKnownAuthors.NICOLAI_PARLOG);
            }
            if (title.contains("JEP Café") || title.contains("JEP Cafe")) {
                return buildList(WellKnownAuthors.buildAuthor("José", "Paumard"));
            }
        }
        if (_channelData.containsKey(channel)) {
            for (final MatchingAuthor match: _channelData.get(channel).getMatchingAuthors()) {
                final Matcher m = match.getPattern().matcher(getDescription());
                if (m.find()) {
                   authors.add(match.getAuthor());
                 }
            }
        }
        if (authors.isEmpty() && getSureAuthors().isEmpty() && getPossibleAuthors().isEmpty()) {
            authors.add(WellKnownAuthors.buildAuthorFromGivenName(channel));
        }
        return authors;
    }

    @Override
    public List<AuthorData> getPossibleAuthors() throws ContentParserException {
        final List<AuthorData> authors = new ArrayList<>();
        final String channel = getChannel();
        if (channel.equals("Java")) {
            final String title = getTitle();
            if (title.contains("Inside Java Newscast")) {
                return buildList(WellKnownAuthors.buildAuthor("Ana-Maria", "Mihalceanu"),
                                 WellKnownAuthors.buildAuthor("Billy", "Korando"));
            }
        }
        if (_channelData.containsKey(channel)) {
            for (final MatchingAuthor match: _channelData.get(channel).getMatchingAuthors()) {
                final Matcher m = match.getPattern().matcher(getDescription());
                if (!m.find()) {
                   authors.add(match.getAuthor());
                 }
            }
        }
        return authors;
    }

    @Override
    public List<ExtractedLinkData> getLinks() throws ContentParserException {
        final String channel = getChannel();
        final Locale lang = (_channelData.containsKey(channel)) ? _channelData.get(channel).getLanguage()
                                                                : getLanguage();
        final ExtractedLinkData linkData = new ExtractedLinkData(getTitle(),
                                                                 new String[0],
                                                                 getUrl(),
                                                                 Optional.empty(),
                                                                 Optional.empty(),
                                                                 new LinkFormat[] { LinkFormat.MP4 },
                                                                 new Locale[] { lang },
                                                                 getDuration(),
                                                                 Optional.empty());
        final List<ExtractedLinkData> list = new ArrayList<>(1);
        list.add(linkData);
        return list;
    }

    private static List<AuthorData> buildList(final AuthorData ...authors) {
        return Arrays.asList(authors);
    }

    private static List<MatchingAuthor> buildMatchingList(final MatchingAuthor ...authors) {
        return Arrays.asList(authors);
    }

    private static MatchingAuthor match(final String regexp,
                                        final AuthorData author) {
        return new MatchingAuthor(Pattern.compile(regexp), author);
    }

    private static class ChannelData {
        private final List<AuthorData> _authors;
        private final List<MatchingAuthor> _matchingAuthors;
        private final Locale _language;

        public ChannelData(final List<AuthorData> authors,
                           final List<MatchingAuthor> matchingAuthors,
                           final Locale language) {
            _authors = authors;
            _matchingAuthors = matchingAuthors;
            _language = language;
        }

        private List<AuthorData> getAuthors() {
            return _authors;
        }

        private List<MatchingAuthor> getMatchingAuthors() {
            return _matchingAuthors;
        }

        private Locale getLanguage() {
            return _language;
        }
   }

    private static class MatchingAuthor {
        private final Pattern _pattern;
        private final AuthorData _author;

        public MatchingAuthor(final Pattern pattern,
                              final AuthorData author) {
            _pattern = pattern;
            _author = author;
        }

        private Pattern getPattern() {
            return _pattern;
        }

        private AuthorData getAuthor() {
            return _author;
        }
    }
}
