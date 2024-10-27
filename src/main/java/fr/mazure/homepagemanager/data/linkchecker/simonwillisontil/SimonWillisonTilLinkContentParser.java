package fr.mazure.homepagemanager.data.linkchecker.simonwillisontil;

import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever;
import fr.mazure.homepagemanager.data.knowledge.WellKnownAuthors;
import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.ExtractedLinkData;
import fr.mazure.homepagemanager.data.linkchecker.LinkDataExtractor;
import fr.mazure.homepagemanager.data.linkchecker.TextParser;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkFormat;

/**
 * Data extractor for simonwillison.net
 */
public class SimonWillisonTilLinkContentParser extends LinkDataExtractor {

    private static final String s_sourceName = "til.simonwillison.net";

    private static final TextParser s_titleParser
        = new TextParser("<title>",
                         "</title>",
                         s_sourceName,
                         "title");

    private static final TextParser s_dateParser
        = new TextParser("\\Q<p class=\"created\">Created \\E",
                         "T\\d\\d:\\d\\d:\\d\\d\\-\\d\\d:\\d\\d\\Q, updated \\E",
                         s_sourceName,
                         "date");

    private final String _data;

    /**
     * @param url URL of the link
     * @param data retrieved link data
     * @param retriever cache data retriever
     */
    public SimonWillisonTilLinkContentParser(final String url,
                                             final String data,
                                             final CachedSiteDataRetriever retriever) {
        super(url, retriever);
        _data = data;
    }

    /**
     * Determine if the link is managed
     *
     * @param url link
     * @return true if the link is managed
     */
    public static boolean isUrlManaged(final String url) {
        return url.startsWith("https://til.simonwillison.net/");
    }

    @Override
    public String getTitle() throws ContentParserException {
        return HtmlHelper.cleanContent(s_titleParser.extract(_data).replaceAll("\\Q| Simon Willison’s TILs\\E$", ""));
    }

    @Override
    public Optional<String> getSubtitle() throws ContentParserException {
        return Optional.empty();
    }

    @Override
    public Optional<TemporalAccessor> getDate() throws ContentParserException {
        return Optional.of(LocalDate.parse(s_dateParser.extract(_data)));
    }

    @Override
    public List<AuthorData> getSureAuthors() throws ContentParserException {
        return Collections.singletonList(WellKnownAuthors.SIMON_WILLISON);
    }

    @Override
    public List<ExtractedLinkData> getLinks() throws ContentParserException {
        final ExtractedLinkData linkData = new ExtractedLinkData(getTitle(),
                                                                 new String[] { },
                                                                 getUrl(),
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
        return Locale.ENGLISH;
    }
}
