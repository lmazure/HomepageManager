package data.linkchecker.youtubewatch;

import java.net.URL;
import java.nio.file.Path;
import java.time.temporal.TemporalAccessor;
import java.util.AbstractMap;
import java.util.ArrayList;
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
            new AbstractMap.SimpleEntry<>("3Blue1Brown", new ChannelData(buildListFromOneAuthor("Grant", "Sanderson"), Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("AstronoGeek", new ChannelData(buildListFromOneAuthor("Arnaud", "Thiry"), Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("J'm'énerve pas, j'explique", new ChannelData(buildListFromOneAuthor("Bertrand", "Augustin"), Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("History of the Earth", new ChannelData(buildListFromThreeAuthors("David", "Kelly", "Pete", "Kelly", "Kelly", "Battison"), Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Le Réveilleur", new ChannelData(buildListFromOneAuthor("Rodolphe", "Meyer"), Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("monsieur bidouille", new ChannelData(buildListFromOneAuthor("Dimitri", "Ferrière"), Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Monsieur Phi", new ChannelData(buildListFromOneAuthor("Thibaut", "Giraud"), Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Passe-Science", new ChannelData(buildListFromOneAuthor("Thomas", "Cabaret"), Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Robert Miles", new ChannelData(buildListFromOneAuthor("Robert", "Miles"), Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Sabine Hossenfelder", new ChannelData(buildListFromOneAuthor("Sabine", "Hossenfelder"), Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("ScienceClic", new ChannelData(buildListFromOneAuthor("Alessandro", "Roussel"), Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Stand-up Maths", new ChannelData(buildListFromOneAuthor("Matt", "Parker"), Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Science4All", new ChannelData(buildListFromOneAuthor("Lê", "Nguyên Hoang"), Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Thomaths", new ChannelData(buildListFromOneAuthor("Alexander", "Thomas"), Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Tric Trac", new ChannelData(buildListFromTwoAuthors("Guillaume", "Chifoumi", "François", "Décamp"), Locale.FRENCH)),
            new AbstractMap.SimpleEntry<>("Veritasium", new ChannelData(buildListFromOneAuthor("Derek", "Muller"), Locale.ENGLISH)),
            new AbstractMap.SimpleEntry<>("Web Dev Simplified", new ChannelData(buildListFromOneAuthor("Kyle", "Cook"), Locale.ENGLISH))
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
    public List<AuthorData> getAuthors() throws ContentParserException {
        final String channel = _parser.getChannel();
        if (_channelData.containsKey(channel)) {
            return _channelData.get(channel).authors();
        }
        if (channel.equals("Java")) {
            final String title = _parser.getTitle();
            if (title.contains("Inside Java Newscast")) {
                return buildListFromOneAuthor("Nicolai", "Parlog");
            }
            if (title.contains("JEP Café")) {
                return buildListFromOneAuthor("José", "Paumard");
            }
        }
        return new ArrayList<>(0);
    }

    @Override
    public List<ExtractedLinkData> getLinks() throws ContentParserException {
        final String channel = _parser.getChannel();
        final Locale lang = (_channelData.containsKey(channel)) ? _channelData.get(channel).language()
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

    private static List<AuthorData> buildListFromOneAuthor(final String firstName,
                                                           final String lastName) {
        final List<AuthorData> list = new ArrayList<>(1);
        list.add(buildAuthor(firstName, lastName));
        return list;
    }

    private static List<AuthorData> buildListFromTwoAuthors(final String firstName1,
                                                            final String lastName1,
                                                            final String firstName2,
                                                            final String lastName2) {
        final List<AuthorData> list = buildListFromOneAuthor(firstName1, lastName1);
        list.add(buildAuthor(firstName2, lastName2));
        return list;
    }

    private static List<AuthorData> buildListFromThreeAuthors(final String firstName1,
                                                              final String lastName1,
                                                              final String firstName2,
                                                              final String lastName2,
                                                              final String firstName3,
                                                              final String lastName3) {
        final List<AuthorData> list = buildListFromOneAuthor(firstName1, lastName1);
        list.add(buildAuthor(firstName2, lastName2));
        list.add(buildAuthor(firstName3, lastName3));
        return list;
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

   private record ChannelData(List<AuthorData> authors,
                              Locale language) {
       // DO NOTHING
   }
}