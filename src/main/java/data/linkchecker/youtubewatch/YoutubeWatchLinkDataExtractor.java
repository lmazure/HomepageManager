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

import data.linkchecker.ContentParserException;
import data.linkchecker.ExtractedLinkData;
import utils.xmlparsing.AuthorData;
import utils.xmlparsing.LinkFormat;

public class YoutubeWatchLinkDataExtractor extends data.linkchecker.LinkDataExtractor {

    private final static Map<String, ChannelData> _channelData = Map.ofEntries(
            new AbstractMap.SimpleEntry<>("3Blue1Brown",
                                          new ChannelData(buildList(buildAuthor("Grant", "Sanderson")),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("AstronoGeek",
                                          new ChannelData(buildList(buildAuthor("Arnaud", "Thiry")),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Balade Mentale",
                                          new ChannelData(buildList(buildAuthor("Théo", "Drieu")),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Chat Sceptique",
                                          new ChannelData(buildList(buildAuthor("Nathan", "Uyttendaele")),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Dr. Becky",
                                          new ChannelData(buildList(buildAuthor("Becky", "Smethurst")),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Heu?reka",
                                          new ChannelData(buildList(buildAuthor("Gilles", "Mitteau")),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("J'm'énerve pas, j'explique",
                                          new ChannelData(buildList(buildAuthor("Bertrand", "Augustin")),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("History of the Earth",
                                          new ChannelData(buildList(buildAuthor("David", "Kelly"), buildAuthor("Pete", "Kelly"), buildAuthor("Kelly", "Battison")),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Le Réveilleur",
                                          new ChannelData(buildList(buildAuthor("Rodolphe", "Meyer")),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Mathologer",
                                          new ChannelData(buildList(buildAuthor("Burkard", "Polster")),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("monsieur bidouille",
                                          new ChannelData(buildList(buildAuthor("Dimitri", "Ferrière")),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Monsieur Phi",
                                          new ChannelData(buildList(buildAuthor("Thibaut", "Giraud")),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Passe-Science",
                                          new ChannelData(buildList(buildAuthor("Thomas", "Cabaret")),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Quadriviuum Tremens",
                                          new ChannelData(buildList(buildAuthor("Tristan", "Audam-Dabidin"), buildAuthor("Keshika", "Dabidin")),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Robert Miles",
                                          new ChannelData(buildList(buildAuthor("Robert", "Miles")),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Sabine Hossenfelder",
                                          new ChannelData(buildList(buildAuthor("Sabine", "Hossenfelder")),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("ScienceClic",
                                          new ChannelData(buildList(buildAuthor("Alessandro", "Roussel")),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("ScienceEtonnante",
                                          new ChannelData(buildList(buildAuthor("David", "Louapre")),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Stand-up Maths",
                                          new ChannelData(buildList(buildAuthor("Matt", "Parker")),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Science4All",
                                          new ChannelData(buildList(buildAuthor("Lê", "Nguyên Hoang")),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Thomaths",
                                          new ChannelData(buildList(buildAuthor("Alexander", "Thomas")),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Tom Scott",
                                          new ChannelData(buildList(buildAuthor("Tom", "Scott")),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Tric Trac",
                                          new ChannelData(buildList(buildAuthor("Guillaume", "Chifoumi"), buildAuthor("François", "Décamp")),
                                                          Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Veritasium",
                                          new ChannelData(buildList(buildAuthor("Derek", "Muller")),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Web Dev Simplified",
                                          new ChannelData(buildList(buildAuthor("Kyle", "Cook")),
                                                          Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("WonderWhy",
                                          new ChannelData(buildList(buildAuthorFromGivenName("WonderWhy")),
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
        return new ArrayList<>(0);
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

   private static class ChannelData {
       final List<AuthorData> _authors;
       final Locale _language;

       public ChannelData(final List<AuthorData> authors,
                          final Locale language) {
        _authors = authors;
        _language = language;
    }

    private List<AuthorData> getAuthors() {
        return _authors;
    }

    private Locale getLanguage() {
        return _language;
    }
   }
}