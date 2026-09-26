package fr.mazure.homepagemanager.data.linkchecker.arminronacher;

import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever;
import fr.mazure.homepagemanager.data.dataretriever.SiteSlurper;
import fr.mazure.homepagemanager.data.knowledge.WellKnownAuthors;
import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.ExtractedLinkData;
import fr.mazure.homepagemanager.data.linkchecker.LinkDataExtractor;
import fr.mazure.homepagemanager.data.linkchecker.TextParser;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;
import fr.mazure.homepagemanager.utils.internet.UrlHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkFormat;

/**
 * Data extractor for lucumr.pocoo.org
 */
public class ArminRonacherLinkContentParser extends LinkDataExtractor {

    private static final String s_sourceName = "lucumr.pocoo.org";

    private static final TextParser s_titleParser
        = new TextParser("<title>",
                         "</title>",
                         s_sourceName,
                         "title");

    private static final TextParser s_dateParser
        = new TextParser("\\Q<meta property=\"article:published_time\" content=\"\\E",
                         "\\d{4}-\\d{2}-\\d{2}",
                         "T",
                         s_sourceName,
                         "date");

    private final String _title;
    private final TemporalAccessor _creationDate;
    private final List<AuthorData> _authors;
    private final List<ExtractedLinkData> _links;

    /**
     * @param url URL of the link
     * @param retriever cache data retriever
     * @throws ContentParserException Failure to extract the information
     */
    public ArminRonacherLinkContentParser(final String url,
                                          final CachedSiteDataRetriever retriever) throws ContentParserException {
        super(url, retriever);

        final SiteSlurper sluper = new SiteSlurper(getRetriever(), url);
        final String data = sluper.getContent();

        _title = HtmlHelper.cleanContent(s_titleParser.extract(data))
                           .replaceFirst(" \\| Armin Ronacher's Thoughts and Writings$", "");

        _creationDate = LocalDate.parse(s_dateParser.extract(data));

        _authors = Collections.singletonList(WellKnownAuthors.ARMIN_RONACHER);

        final ExtractedLinkData linkData = new ExtractedLinkData(_title,
                                                                 new String[] { },
                                                                 getUrl(),
                                                                 Optional.empty(),
                                                                 Optional.empty(),
                                                                 getFormats(),
                                                                 new Locale[] { getLanguage() },
                                                                 Optional.empty(),
                                                                 Optional.empty());
        final List<ExtractedLinkData> list = new ArrayList<>(1);
        list.add(linkData);
        _links = list;
    }

    /**
     * Determine if the link is managed
     *
     * @param url link
     * @return true if the link is managed
     */
    public static boolean isUrlManaged(final String url) {
        return UrlHelper.hasPrefix(url, "https://lucumr.pocoo.org/");
    }

    @Override
    public String getTitle() {
        return _title;
    }

    @Override
    public Optional<String> getSubtitle() {
        return Optional.empty();
    }

    @Override
    public LinkFormat[] getFormats() {
        return new LinkFormat[] { LinkFormat.HTML };
    }

    @Override
    public Optional<TemporalAccessor> getCreationDate() {
        return Optional.of(_creationDate);
    }

    @Override
    public Optional<TemporalAccessor> getPublicationDate() {
        return Optional.of(_creationDate);
    }

    @Override
    public List<AuthorData> getSureAuthors() {
        return _authors;
    }

    @Override
    public List<ExtractedLinkData> getLinks() {
        return _links;
    }

    @Override
    public Locale getLanguage() {
        return Locale.ENGLISH;
    }
}
