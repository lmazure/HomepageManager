package fr.mazure.homepagemanager.data.linkchecker.chromium;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever;
import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.ExtractedLinkData;
import fr.mazure.homepagemanager.data.linkchecker.LinkDataExtractor;
import fr.mazure.homepagemanager.data.linkchecker.TextParser;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkFormat;

/**
 * Data extractor from Chromium blog
 */
public class ChromiumBlogLinkContentParser extends LinkDataExtractor {

    private static final String s_sourceName = "Chromium Blog";

    private final String _data;

    private static final TextParser s_titleParser
        = new TextParser("<title>\\nChromium Blog: ",
                         "</title>",
                         s_sourceName,
                         "title");
    private static final TextParser s_dateParser
        = new TextParser("<span class='publishdate' itemprop='datePublished'>",
                         "</span>",
                         s_sourceName,
                         "date");
    private static final DateTimeFormatter s_formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, u", Locale.ENGLISH);

    /**
     * @param url URL of the link
     * @param data retrieved link data
     * @param retriever cache data retriever
     */
    public ChromiumBlogLinkContentParser(final String url,
                                        final String data,
                                        final CachedSiteDataRetriever retriever) {
        super(url, retriever);
        _data = data;
    }

    /**
     * @return title
     * @throws ContentParserException Failure to extract the information
     */
    @Override
    public String getTitle() throws ContentParserException {
        return HtmlHelper.cleanContent(s_titleParser.extract(_data));
    }

    @Override
    public Optional<String> getSubtitle() throws ContentParserException {
        return Optional.empty();
    }

    /**
     * @return publication date, empty if there is none
     * @throws ContentParserException Failure to extract the information
     */
    @Override
    public Optional<TemporalAccessor> getDate() throws ContentParserException {
        final String date = HtmlHelper.cleanContent(s_dateParser.extract(_data));
        try {
            return Optional.of(LocalDate.parse(date, s_formatter));
        } catch (final DateTimeParseException e) {
            throw new ContentParserException("Failed to parse date (" + date + ") in Chromium Blog page", e);
        }
    }

    @Override
    public List<AuthorData> getSureAuthors() throws ContentParserException {
        return new ArrayList<>();
    }

    @Override
    public Locale getLanguage() throws ContentParserException {
        return Locale.ENGLISH;
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
}
