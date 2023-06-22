package fr.mazure.homepagemanager.data.linkchecker.youtubewatch;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.ExtractedLinkData;
import fr.mazure.homepagemanager.data.linkchecker.LinkDataExtractor;
import fr.mazure.homepagemanager.utils.StringHelper;
import fr.mazure.homepagemanager.utils.internet.UrlHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkFormat;

/**
* Data extractor for YouTube videos
 */
public class YoutubeWatchLinkContentParser extends LinkDataExtractor {

    private final String _data;
    private final boolean _isEscaped;
    private Boolean _isPlayable;
    private String _channel;
    private String _title;
    private String _description;
    private Locale _language;
    private Optional<Locale> _subtitlesLanguage;
    private LocalDate _uploadDate;
    private LocalDate _publishDate;
    private Duration _minDuration;
    private Duration _maxDuration;

    /**
     * @param url URL of the link
     * @param data retrieved link data
     * @throws ContentParserException Failure to extract the information
     */
    public YoutubeWatchLinkContentParser(final String url,
                                         final String data) throws ContentParserException {
        super(UrlHelper.removeQueryParameters(url, "app",
                                                   "feature",
                                                   "index",
                                                   "list",
                                                   "t"));
        _data = data;
        if (data.contains("ytInitialPlayerResponse =")) {
            _isEscaped = false;
        } else if (data.contains("window[\"ytInitialPlayerResponse\"] =")) {
            _isEscaped = true;
        } else if (data.contains("ytplayer.config = {")) {
            _isEscaped = true;
        } else {
            throw new ContentParserException("Failed to recognize the YouTube answer type");
        }
    }

    /**
     * @return is the video playable or blocked?
     */
    public boolean isPlayable() {
        if (_isPlayable == null) {
            _isPlayable = Boolean.valueOf(getPlayable());
        }

        return _isPlayable.booleanValue();
    }

    /**
     * @return channel of the video
     * @throws ContentParserException Failure to extract the information
     */
    public String getChannel() throws ContentParserException {
        if (_channel == null) {
            _channel = extractField("ownerChannelName");
        }

        return _channel;
    }

    @Override
    public String getTitle() throws ContentParserException {
        if (_title == null) {
            _title = extractField("title");
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
        if (_description == null) {
            _description = extractField("shortDescription");
        }

        return _description;
    }

    /**
     * @return upload date of the video
     * @throws ContentParserException Failure to extract the information
     */
    public LocalDate getUploadDateInternal() throws ContentParserException {
        if (_uploadDate == null) {
            _uploadDate = extractDate("uploadDate");
        }

        return _uploadDate;
    }

    /**
     * @return publication date of the video
     * @throws ContentParserException Failure to extract the information
     */
    public LocalDate getPublishDateInternal() throws ContentParserException {
        if (_publishDate == null) {
            _publishDate = extractDate("publishDate");
        }

        return _publishDate;
    }

    /**
     * @return minimum duration of the video
     * @throws ContentParserException Failure to extract the information
     */
    public Duration getMinDuration() throws ContentParserException {
        if (_minDuration == null) {
            extractDuration();
        }

        return _minDuration;
    }

    /**
     * @return maximum duration of the video
     * @throws ContentParserException Failure to extract the information
     */
    public Duration getMaxDuration() throws ContentParserException {
        if (_maxDuration == null) {
            extractDuration();
        }

        return _maxDuration;
    }

    @Override
    public Locale getLanguage() throws ContentParserException {
        if (_language == null) {
            final Optional<Locale> lang = getSubtitlesLanguage();
            if (lang.isPresent()) {
                _language = lang.get();
            } else {
                final Optional<Locale> lang2 = StringHelper.guessLanguage(getDescription());
                if (lang2.isPresent()) {
                    _language = lang2.get();
                } else {
                    final Optional<Locale> lang3 = StringHelper.guessLanguage(getTitle());
                    _language = lang3.isPresent() ? lang3.get() : Locale.ENGLISH;
                }
            }
        }

        return _language;
    }

    /**
     * @return language of the subtitles of the video
     */
    public Optional<Locale> getSubtitlesLanguage() {
        final String prefix = _isEscaped ? "\\\"name\\\":{\\\"simpleText\\\":\\\""
                                         : "\"name\":{\"simpleText\":\"";
        final String postfix = _isEscaped ? " (auto-generated)\\\"}"
                                          : " (auto-generated)\"}";
        if (_subtitlesLanguage == null) {
            if (_data.contains(prefix + "French" + postfix)) {
                _subtitlesLanguage = Optional.of(Locale.FRENCH);
            } else if (_data.contains(prefix + "English" + postfix)) {
                _subtitlesLanguage = Optional.of(Locale.ENGLISH);
            } else {
                _subtitlesLanguage = Optional.empty();
            }
        }

        return _subtitlesLanguage;
    }

    private boolean getPlayable() {
        return _isEscaped ? _data.contains("\\\"playabilityStatus\\\":{\\\"status\\\":\\\"OK\\\"")
                          : _data.contains("\"playabilityStatus\":{\"status\":\"OK\"");
    }

    private String extractField(final String str) throws ContentParserException {
        String text = null;

        final Pattern p = Pattern.compile(_isEscaped ? ("\\\\\"" + str + "\\\\\":\\\\\"(.+?)(?<!\\\\\\\\)\\\\\"")
                                                     : (",\"" + str + "\":\"(.+?)(?<!\\\\)\""));
        final Matcher m = p.matcher(_data);
        while (m.find()) {
            final String t = m.group(1);
            if (text == null) {
                text = t;
            } else {
                if (!text.equals(t)) {
                    throw new ContentParserException("Found different " + str + " texts in YouTube watch page");
                }
            }
        }

        if (text == null) {
            throw new ContentParserException("Failed to extract " + str + " text from YouTube watch page ");
        }

        if (_isEscaped) {
            text = text.replaceAll(Pattern.quote("\\\\n"), "\n")
                       .replaceAll(Pattern.quote("\\\\u0026"),"&")
                       .replaceAll(Pattern.quote("\\\\u0090"),"\u0090")
                       .replaceAll(Pattern.quote("\\/"),"/")
                       .replaceAll(Pattern.quote("\\\\\\\""),"\"")
                       .replaceAll(Pattern.quote("\\\\\\'"),"'");
        } else {
            text = text.replaceAll(Pattern.quote("\\n"), "\n")
                       .replaceAll(Pattern.quote("\\u0026"),"&")
                       .replaceAll(Pattern.quote("\\/"),"/")
                       .replaceAll(Pattern.quote("\\\""),"\"")
                       .replaceAll(Pattern.quote("\\\'"),"'");
        }

        return text;
    }

    private LocalDate extractDate(final String str) throws ContentParserException {
        return LocalDate.parse(extractField(str));
    }

    private void extractDuration() throws ContentParserException {

        int minDuration = Integer.MAX_VALUE;
        int maxDuration = Integer.MIN_VALUE;

        final Pattern p = _isEscaped ? Pattern.compile("\\\\\"approxDurationMs\\\\\":\\\\\"(\\d+)\\\\\"")
                                     :  Pattern.compile("\"approxDurationMs\":\"(\\d+)\"");
        final Matcher m = p.matcher(_data);
        while (m.find()) {
            final int duration = Integer.parseInt(m.group(1));
            if (duration < minDuration) {
                minDuration = duration;
            }
            if (duration > maxDuration) {
                maxDuration = duration;
            }
        }

        if (minDuration == Integer.MAX_VALUE) {
            throw new ContentParserException("Failed to extract durations from YouTube watch page");
        }

        _minDuration = Duration.ofMillis(minDuration);
        _maxDuration = Duration.ofMillis(maxDuration);
    }

    private static final Map<String, ChannelData> _channelData = Map.ofEntries(
            new AbstractMap.SimpleEntry<>("3Blue1Brown",
                                          new ChannelData(buildList(buildAuthor("Grant", "Sanderson")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("ArjanCodes",
                                          new ChannelData(buildList(buildAuthor("Arjan", "Egges")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("AstronoGeek",
                                          new ChannelData(buildList(buildAuthor("Arnaud", "Thiry")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Aurelien_Sama",
                                          new ChannelData(buildList(buildAuthor("Aurélien", "Sama")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Aypierre",
                                          new ChannelData(buildList(buildAuthor("Aymeric", "Pierre")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("BABOR LELEFAN",
                                          new ChannelData(buildList(buildAuthor("Clément", "Berut")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Balade Mentale",
                                          new ChannelData(buildList(buildAuthor("Théo", "Drieu")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("blackpenredpen",
                                          new ChannelData(buildList(buildAuthor("Steve", "Chow")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("BLAST, Le souffle de l'info",
                                          new ChannelData(buildList(),
                                                          buildMatchingList(match("Moritz", buildAuthor("Paloma", "Moritz"))),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Chat Sceptique",
                                          new ChannelData(buildList(buildAuthor("Nathan", "Uyttendaele")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Clément Freze",
                                          new ChannelData(buildList(buildAuthor("Clément", "Freze")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Computerphile",
                                          new ChannelData(buildList(),
                                                          buildMatchingList(match("Altenkirch", buildAuthor("Thorsten", "Altenkirch")),
                                                                            match("Bagley ", buildAuthor("Steve", "Bagley")),
                                                                            match("Miles", buildAuthor("Robert", "Miles")),
                                                                            match("Mike Pound", buildAuthor("Mike", "Pound")),
                                                                            match("Laurence Tratt", buildAuthor("Laurence", "Tratt")),
                                                                            match("Kai Xu", buildAuthor("Kai", "Xu"))),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Continuous Delivery",
                                          new ChannelData(buildList(buildAuthor("Dave", "Farley")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("DeepSkyVideos",
                                          new ChannelData(buildList(),
                                                          buildMatchingList(match("Crowther", buildAuthor("Paul", "Crowther")),
                                                                            match("Gray", buildAuthor("Meghan", "Gray")),
                                                                            match("Merrifield", buildAuthor("Michael", "Merrifield"))),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("DirtyBiology",
                                          new ChannelData(buildList(buildAuthor("Léo", "Grasset")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Dr. Becky",
                                          new ChannelData(buildList(buildAuthor("Becky", "Smethurst")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Dr Peyam",
                                          new ChannelData(buildList(buildAuthor("Peyam", "Ryan", "Tabrizian")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("El Jj",
                                          new ChannelData(buildList(buildAuthor("Jérôme", "Cottanceau")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Fireship",
                                          new ChannelData(buildList(buildAuthor("Jeff", "Delaney")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("GOTO Conferences",
                                          new ChannelData(buildList(),
                                                          buildMatchingList(match("Dave Farley", buildAuthor("Dave", "Farley"))),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Heu?reka",
                                          new ChannelData(buildList(buildAuthor("Gilles", "Mitteau")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("History of the Earth",
                                          new ChannelData(buildList(buildAuthor("David", "Kelly"),
                                                                    buildAuthor("Pete", "Kelly"),
                                                                    buildAuthor("Kelly", "Battison")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Holger Voormann",
                                          new ChannelData(buildList(buildAuthor("Holger", "Voormann")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Homo Fabulus",
                                          new ChannelData(buildList(buildAuthor("Stéphane", "Debove")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Hygiène Mentale",
                                          new ChannelData(buildList(buildAuthor("Christophe", "Michel")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Jamy - Epicurieux",
                                          new ChannelData(buildList(buildAuthor("Jamy", "Gourmaud")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("J'm'énerve pas, j'explique",
                                          new ChannelData(buildList(buildAuthor("Bertrand", "Augustin")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Jon Perry - Genetics & Evolution Stated Casually",
                                          new ChannelData(buildList(buildAuthor("Jon", "Perry")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Kevin Fang",
                                          new ChannelData(buildList(buildAuthor("Kevin", "Fang")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Kyle Hill",
                                          new ChannelData(buildList(buildAuthor("Kyle", "Hill")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("La Tronche en Biais",
                                          new ChannelData(buildList(buildAuthor("Thomas", "Durand")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Le Dessous des Cartes - ARTE",
                                          new ChannelData(buildList(buildAuthor("Émilie", "Aubry")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Le Réveilleur",
                                          new ChannelData(buildList(buildAuthor("Rodolphe", "Meyer")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Linguisticae",
                                          new ChannelData(buildList(buildAuthor("Romain", "Filstroff")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Mathador",
                                          new ChannelData(buildList(buildAuthor("Franck", "Dunas")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Mathologer",
                                          new ChannelData(buildList(buildAuthor("Burkard", "Polster")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Mathologer 2",
                                          new ChannelData(buildList(buildAuthor("Burkard", "Polster")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Matt_Parker_2",
                                          new ChannelData(buildList(buildAuthor("Matt", "Parker")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Mickaël Launay",
                                          new ChannelData(buildList(buildAuthor("Mickaël", "Launay")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("minutephysics",
                                          new ChannelData(buildList(buildAuthor("Henry", "Reich")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Monsieur Bidouille",
                                          new ChannelData(buildList(buildAuthor("Dimitri", "Ferrière")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Monsieur Phi",
                                          new ChannelData(buildList(buildAuthor("Thibaut", "Giraud")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Numberphile",
                                          new ChannelData(buildList(),
                                                          buildMatchingList(match("Crawford", buildAuthor("Tom", "Crawford")),
                                                                            match("Eisenbud", buildAuthor("David", "Eisenbud")),
                                                                            match("Grime", buildAuthor("James", "Grime")),
                                                                            match("Sanderson", buildAuthor("Grant", "Sanderson")),
                                                                            match("Stoll", buildAuthor("Cliff", "Stoll")),
                                                                            match("Sloane", buildAuthor("Neil", "Sloane")),
                                                                            match("Sparks ", buildAuthor("Ben", "Sparks")),
                                                                            match("Woolley ", buildAuthor("Thomas", "Woolley"))),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Nota Bonus",
                                          new ChannelData(buildList(buildAuthor("Benjamin", "Brillaud")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Officiel DEFAKATOR",
                                          new ChannelData(buildList(buildAuthorFromGivenName("Defakator")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Osons Causer",
                                          new ChannelData(buildList(buildAuthor("Ludo", "Torbey")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Passe-Science",
                                          new ChannelData(buildList(buildAuthor("Thomas", "Cabaret")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("PBS Eons",
                                          new ChannelData(buildList(),
                                                          buildMatchingList(match("Barboza-Ramirez", buildAuthor("Michelle", "Barboza-Ramirez")),
                                                                            match("Moore", buildAuthor("Kallie", "Moore")),
                                                                            match("de Pastino", buildAuthor("Blake", "de Pastino"))),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Periodic Videos",
                                          new ChannelData(buildList(),
                                                          buildMatchingList(match("Poliakoff", buildAuthor("Martyn", "Poliakoff")),
                                                                            match("Barnes", buildAuthor("Neil", "Barnes"))),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("permaculture agroécologie etc...",
                                          new ChannelData(buildList(buildAuthor("Damien", "Dekarz")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Philoxime",
                                          new ChannelData(buildList(buildAuthor("Maxime", "Lambrecht")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Plainly Difficult",
                                          new ChannelData(buildList(buildAuthorFromFirstName("John")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Primer",
                                          new ChannelData(buildList(buildAuthor("Justin", "Helps")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Quadriviuum Tremens",
                                          new ChannelData(buildList(buildAuthor("Tristan", "Audam-Dabidin"),
                                                                    buildAuthor("Keshika", "Dabidin")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Robert Miles AI Safety",
                                          new ChannelData(buildList(buildAuthor("Robert", "Miles")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Sabine Hossenfelder",
                                          new ChannelData(buildList(buildAuthor("Sabine", "Hossenfelder")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("ScienceClic",
                                          new ChannelData(buildList(buildAuthor("Alessandro", "Roussel")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("ScienceClic Plus",
                                          new ChannelData(buildList(buildAuthor("Alessandro", "Roussel")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Science de comptoir",
                                          new ChannelData(buildList(buildAuthor("Valentine", "Delattre")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("ScienceEtonnante",
                                          new ChannelData(buildList(buildAuthor("David", "Louapre")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Science4All",
                                          new ChannelData(buildList(buildAuthor("Lê", "Nguyên Hoang")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("SciShow",
                                          new ChannelData(buildList(),
                                                          buildMatchingList(match("Hank", buildAuthor("Hank", "Green")),
                                                                            match("Stefan Chin", buildAuthor("Stefan", "Chin"))),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Scilabus",
                                          new ChannelData(buildList(buildAuthor("Viviane", "Lalande")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("singingbanana",
                                          new ChannelData(buildList(buildAuthor("James", "Grime")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Sixty Symbols",
                                          new ChannelData(buildList(),
                                                          buildMatchingList(match("Copeland", buildAuthor("Edmund", "Copeland")),
                                                                            match("Gray", buildAuthor("Meghan", "Gray")),
                                                                            match("Merrifield", buildAuthor("Michael", "Merrifield")),
                                                                            match("Moriarty", buildAuthor("Philip", "Moriarty")),
                                                                            match("Smethurst", buildAuthor("Becky", "Smethurst")),
                                                                            match("Unwin", buildAuthor("James", "Unwin"))),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Stand-up Maths",
                                          new ChannelData(buildList(buildAuthor("Matt", "Parker")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Stated Clearly",
                                          new ChannelData(buildList(buildAuthor("Jon", "Perry")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Steve Mould",
                                          new ChannelData(buildList(buildAuthor("Steve", "Mould")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("TechWorld with Nana",
                                          new ChannelData(buildList(buildAuthor("Nana", "Janashia")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Thomaths",
                                          new ChannelData(buildList(buildAuthor("Alexander", "Thomas")),
                                                          buildMatchingList(match("Eve", buildAuthor("Eve", "Grigy-Kissian"))),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Tom Scott",
                                          new ChannelData(buildList(buildAuthor("Tom", "Scott")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Tric Trac",
                                          new ChannelData(buildList(buildAuthor("Guillaume", "Chifoumi"),
                                                                    buildAuthor("François", "Décamp")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Veritasium",
                                          new ChannelData(buildList(buildAuthor("Derek", "Muller")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("VERY MATH TRIP - Manu Houdart",
                                          new ChannelData(buildList(buildAuthor("Manu", "Houdart")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Web Dev Simplified",
                                          new ChannelData(buildList(buildAuthor("Kyle", "Cook")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("WonderWhy",
                                          new ChannelData(buildList(buildAuthorFromGivenName("WonderWhy")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Yannic Kilcher",
                                          new ChannelData(buildList(buildAuthor("Yannic", "Kilcher")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Yosha - Echecs",
                                          new ChannelData(buildList(buildAuthor("Yosha", "Iglesias")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH))
            );

    @Override
    public Optional<TemporalAccessor> getDate() throws ContentParserException {
        return Optional.of(getPublishDateInternal());
    }

    @Override
    public List<AuthorData> getSureAuthors() throws ContentParserException {
        final String channel = getChannel();
        if (_channelData.containsKey(channel)) {
            return _channelData.get(channel).getAuthors();
        }
        if (channel.equals("Java")) {
            final String title = getTitle();
            if (title.contains("Inside Java Newscast")) {
                return buildList(buildAuthor("Nicolai", "Parlog"));
            }
            if (title.contains("JEP Café")) {
                return buildList(buildAuthor("José", "Paumard"));
            }
        }
        final List<AuthorData> list = new ArrayList<>();
        list.add(buildAuthorFromGivenName(channel));
        return list;
    }

    @Override
    public List<AuthorData> getProbableAuthors() throws ContentParserException {
        final List<AuthorData> authors = new ArrayList<>();
        final String channel = getChannel();
        if (_channelData.containsKey(channel)) {
            for (MatchingAuthor match: _channelData.get(channel).getMatchingAuthors()) {
                final Matcher m = match.getPattern().matcher(getDescription());
                if (m.find()) {
                   authors.add(match.getAuthor());
                 }
            }
        }
        return authors;
    }

    @Override
    public List<AuthorData> getPossibleAuthors() throws ContentParserException {
        final List<AuthorData> authors = new ArrayList<>();
        final String channel = getChannel();
        if (_channelData.containsKey(channel)) {
            for (MatchingAuthor match: _channelData.get(channel).getMatchingAuthors()) {
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
                                                                 getUrl().toString(),
                                                                 Optional.empty(),
                                                                 Optional.empty(),
                                                                 new LinkFormat[] { LinkFormat.MP4 },
                                                                 new Locale[] { lang },
                                                                 Optional.of(getMinDuration()),
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

    private static AuthorData buildAuthor(final String firstName,
                                          final String lastName) {
        return new AuthorData(Optional.empty(),
                              Optional.of(firstName),
                              Optional.empty(),
                              Optional.of(lastName),
                              Optional.empty(),
                              Optional.empty());
    }

    private static AuthorData buildAuthor(final String firstName,
                                          final String middleName,
                                          final String lastName) {
        return new AuthorData(Optional.empty(),
                              Optional.of(firstName),
                              Optional.of(middleName),
                              Optional.of(lastName),
                              Optional.empty(),
                              Optional.empty());
    }

    private static AuthorData buildAuthorFromGivenName(final String givenName) {
        return new AuthorData(Optional.empty(),
                              Optional.empty(),
                              Optional.empty(),
                              Optional.empty(),
                              Optional.empty(),
                              Optional.of(givenName));
    }

    private static AuthorData buildAuthorFromFirstName(final String firstName) {
        return new AuthorData(Optional.empty(),
                              Optional.of(firstName),
                              Optional.empty(),
                              Optional.empty(),
                              Optional.empty(),
                              Optional.empty());
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
