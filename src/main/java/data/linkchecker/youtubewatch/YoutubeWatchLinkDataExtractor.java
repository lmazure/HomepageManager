package data.linkchecker.youtubewatch;

import java.net.URL;
import java.nio.file.Path;
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

import data.linkchecker.ContentParserException;
import data.linkchecker.ExtractedLinkData;
import utils.xmlparsing.AuthorData;
import utils.xmlparsing.LinkFormat;

public class YoutubeWatchLinkDataExtractor extends data.linkchecker.LinkDataExtractor {

    private final static Map<String, ChannelData> _channelData = Map.ofEntries(
            new AbstractMap.SimpleEntry<>("3Blue1Brown",
                                          new ChannelData(buildList(buildAuthor("Grant", "Sanderson")),
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
            new AbstractMap.SimpleEntry<>("Balade Mentale",
                                          new ChannelData(buildList(buildAuthor("Théo", "Drieu")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("blackpenredpen",
                                          new ChannelData(buildList(buildAuthor("Steve", "Chow")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Chat Sceptique",
                                          new ChannelData(buildList(buildAuthor("Nathan", "Uyttendaele")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Dr. Becky",
                                          new ChannelData(buildList(buildAuthor("Becky", "Smethurst")),
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
            new AbstractMap.SimpleEntry<>("El Jj",
                                          new ChannelData(buildList(buildAuthor("Jérôme", "Cottanceau")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Heu?reka",
                                          new ChannelData(buildList(buildAuthor("Gilles", "Mitteau")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Holger Voormann",
                                          new ChannelData(buildList(buildAuthor("Holger", "Voormann")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
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
            new AbstractMap.SimpleEntry<>("Hygiène Mentale",
                                          new ChannelData(buildList(buildAuthor("Christophe", "Michel")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("History of the Earth",
                                          new ChannelData(buildList(buildAuthor("David", "Kelly"),
                                                                    buildAuthor("Pete", "Kelly"),
                                                                    buildAuthor("Kelly", "Battison")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Le Dessous des Cartes - ARTE",
                                          new ChannelData(buildList(buildAuthor("Émilie", "Aubry")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Le Réveilleur",
                                          new ChannelData(buildList(buildAuthor("Rodolphe", "Meyer")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Mathologer",
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
            new AbstractMap.SimpleEntry<>("monsieur bidouille",
                                          new ChannelData(buildList(buildAuthor("Dimitri", "Ferrière")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Monsieur Phi",
                                          new ChannelData(buildList(buildAuthor("Thibaut", "Giraud")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Nota Bonus",
                                          new ChannelData(buildList(buildAuthor("Benjamin", "Brillaud")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Passe-Science",
                                          new ChannelData(buildList(buildAuthor("Thomas", "Cabaret")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Philoxime",
                                          new ChannelData(buildList(buildAuthor("Maxime", "Lambrecht")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Primer",
                                          new ChannelData(buildList(buildAuthor("Justin", "Helps")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Quadriviuum Tremens",
                                          new ChannelData(buildList(buildAuthor("Tristan", "Audam-Dabidin"),
                                                                    buildAuthor("Keshika", "Dabidin")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Robert Miles",
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
            new AbstractMap.SimpleEntry<>("Science de comptoir",
                                          new ChannelData(buildList(buildAuthor("Valentine", "Delattre")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("ScienceEtonnante",
                                          new ChannelData(buildList(buildAuthor("David", "Louapre")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Scilabus",
                                          new ChannelData(buildList(buildAuthor("Viviane", "Lalande")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("singingbanana",
                                          new ChannelData(buildList(buildAuthor("James", "Grime")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Stand-up Maths",
                                          new ChannelData(buildList(buildAuthor("Matt", "Parker")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Science4All",
                                          new ChannelData(buildList(buildAuthor("Lê", "Nguyên Hoang")),
                                                          buildMatchingList(),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Thomaths",
                                          new ChannelData(buildList(buildAuthor("Alexander", "Thomas")),
                                                          buildMatchingList(),
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
            new AbstractMap.SimpleEntry<>("Web Dev Simplified",
                                          new ChannelData(buildList(buildAuthor("Kyle", "Cook")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("WonderWhy",
                                          new ChannelData(buildList(buildAuthorFromGivenName("WonderWhy")),
                                                          buildMatchingList(),
                                                          Locale.ENGLISH))
            );
    private final YoutubeWatchLinkContentParser _parser;

    public YoutubeWatchLinkDataExtractor(final URL url,
                                         final Path cacheDirectory) throws ContentParserException {
        super(url, cacheDirectory);
        _parser = new YoutubeWatchLinkContentParser(getContent());
    }

    @Override
    public Optional<TemporalAccessor> getDate() throws ContentParserException {
        return Optional.of(_parser.getPublishDate());
    }

    @Override
    public List<AuthorData> getSureAuthors() throws ContentParserException {
        final String channel = _parser.getChannel();
        if (_channelData.containsKey(channel)) {
            return _channelData.get(channel).getAuthors();
        }
        if (channel.equals("Java")) {
            final String title = _parser.getTitle();
            if (title.contains("Inside Java Newscast")) {
                return buildList(buildAuthor("Nicolai", "Parlog"));
            }
            if (title.contains("JEP Café")) {
                return buildList(buildAuthor("José", "Paumard"));
            }
        }
        if (channel.equals("Computerphile")) {
            final String description = _parser.getDescription();
            if (description.contains("Mike Pound")) {
                return buildList(buildAuthor("Mike", "Pound"));
            }
        }
        if (channel.equals("Periodic Videos")) {
            final String description = _parser.getDescription();
            final List<AuthorData> list = new ArrayList<>();
            if (description.contains("Poliakoff")) {
                list.add(buildAuthor("Martyn", "Poliakoff"));
            }
            if (description.contains("Barnes")) {
                list.add(buildAuthor("Neil", "Barnes"));
            }
            return list;
        }
        return new ArrayList<>(0);
    }

    @Override
    public List<AuthorData> getProbableAuthors() throws ContentParserException {
        final List<AuthorData> authors = new ArrayList<>();
        final String channel = _parser.getChannel();
        if (_channelData.containsKey(channel)) {
            for (MatchingAuthor match: _channelData.get(channel).getMatchingAuthors()) {
                final Matcher m = match.getPattern().matcher(_parser.getDescription());
                if (m.find()) {
                   authors.add(match.getAuthor());
                 }
            }
        }
        return authors;
    }

    @Override
    public List<AuthorData> getPossibleAuthors() throws ContentParserException  {
        final List<AuthorData> authors = new ArrayList<>();
        final String channel = _parser.getChannel();
        if (_channelData.containsKey(channel)) {
            for (MatchingAuthor match: _channelData.get(channel).getMatchingAuthors()) {
                final Matcher m = match.getPattern().matcher(_parser.getDescription());
                if (!m.find()) {
                   authors.add(match.getAuthor());
                 }
            }
        }
        return authors;
    }

    @Override
    public List<ExtractedLinkData> getLinks() throws ContentParserException {
        final String channel = _parser.getChannel();
        final Locale lang = (_channelData.containsKey(channel)) ? _channelData.get(channel).getLanguage()
                                                                : Locale.ENGLISH;
        final ExtractedLinkData linkData = new ExtractedLinkData(_parser.getTitle(),
                                                                 new String[0],
                                                                 getUrl().toString(),
                                                                 Optional.empty(),
                                                                 Optional.empty(),
                                                                 new LinkFormat[] { LinkFormat.MP4 },
                                                                 new Locale[] { lang },
                                                                 Optional.of(_parser.getMinDuration()),
                                                                 Optional.empty());
        final List<ExtractedLinkData> list = new ArrayList<>(1);
        list.add(linkData);
        return list;
    }

    private static List<AuthorData> buildList(final AuthorData ... authors) {
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

    private static AuthorData buildAuthorFromGivenName(final String givenName) {
        return new AuthorData(Optional.empty(),
                              Optional.empty(),
                              Optional.empty(),
                              Optional.empty(),
                              Optional.empty(),
                              Optional.of(givenName));
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