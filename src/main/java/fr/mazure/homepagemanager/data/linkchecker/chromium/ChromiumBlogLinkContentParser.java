package fr.mazure.homepagemanager.data.linkchecker.chromium;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever;
import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.ExtractedLinkData;
import fr.mazure.homepagemanager.data.linkchecker.LinkContentParserUtils;
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

    private final String _title;
    private final Optional<TemporalAccessor> _creationDate;
    private final List<AuthorData> _authors;
    private final List<ExtractedLinkData> _links;

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
    private static final TextParser s_authorParser
        = new TextParser("<span [^>]+>Posted by ",
                         "</span>",
                         s_sourceName,
                         "author");
    private static final DateTimeFormatter s_dateFormat = DateTimeFormatter.ofPattern("EEEE, MMMM d, u", Locale.ENGLISH);

    /**
     * @param url URL of the link
     * @param data retrieved link data
     * @param retriever cache data retriever
     * @throws ContentParserException Failure to extract the information
     */
    public ChromiumBlogLinkContentParser(final String url,
                                        final String data,
                                        final CachedSiteDataRetriever retriever) throws ContentParserException {
        super(url, retriever);

        _title = HtmlHelper.cleanContent(s_titleParser.extract(data));

        final String dateStr = HtmlHelper.cleanContent(s_dateParser.extract(data));
        try {
            _creationDate = Optional.of(LocalDate.parse(dateStr, s_dateFormat));
        } catch (final DateTimeParseException e) {
            throw new ContentParserException("Failed to parse date (" + dateStr + ") in Chromium Blog page", e);
        }

        final Optional<String> authorStr = s_authorParser.extractOptional(data);
        if (authorStr.isEmpty() || authorStr.get().equals("the Chrome DevTools team")) {
            _authors = Collections.emptyList();
        } else {
            final String author = HtmlHelper.cleanContent(authorStr.get()) // TODO this is a real nightmare
                                            .replace(", on behalf of Chrome's web platform security team", "")
                                            .replace("SYN, SYN-ACK and ACK (also known as ", "")
                                            .replaceAll(" of .*", "")
                                            .replace(" from the Chrome team", "")
                                            .replace(" - lazy Chrome engineers.", "")
                                            .replace(" - Director, Chrome Engineering", "")
                                            .replace(", technical lead for Chrome accessibility.", "")
                                            .replace(", Lite Pages Technical Lead", "")
                                            .replace(", Product Director Chrome Media", "")
                                            .replace(", Product Manager for Web Assembly, V8 and Web Capabilities", "")
                                            .replace(", Software Engineer working on Web Ecosystem Infrastructure", "")
                                            .replaceAll(", Product Manager(,| on) Chrome", "")
                                            .replace(", Chrome Product Manager", "")
                                            .replace(", Senior Product Manager,", "")
                                            .replace(", Web Platform PM", "")
                                            .replace(", Technical Program Manager", "")
                                            .replaceAll(", (|Chrome Extensions )Product Manager", "")
                                            .replace(", Developer Relations", "")
                                            .replaceAll(", Software Engineer(,| on) .*", "")
                                            .replace(", Software Engineer, Chrome on iOS", "")
                                            .replaceAll("[-,]( Chrome)? Software Engineers?", "")
                                            .replace(", Technical Lead for Lite Mode", "")
                                            .replaceAll(", (Chrome|Google) Security (Engineer|Team)", "")
                                            .replace(", Chrome Policy and Anti-Abuse Team", "")
                                            .replace(", Chrome Product & Policy", "")
                                            .replaceAll(", (Developer|Design) Advocate", "")
                                            .replace(", Resident Loader Coders", "")
                                            .replace(", JavaScript Janitor", "")
                                            .replace(", dreamer.", "")
                                            .replace(", “The Unbouncer”", "")
                                            .replace(", the Wizzy Web Warrior.", "")
                                            .replaceAll("\\)$", "");
            _authors = LinkContentParserUtils.getAuthors(author);
        }

        final ExtractedLinkData linkData = new ExtractedLinkData(_title,
                                                             new String[] { },
                                                             url,
                                                             Optional.empty(),
                                                             Optional.empty(),
                                                             new LinkFormat[] { LinkFormat.HTML },
                                                             new Locale[] { getLanguage() },
                                                             Optional.empty(),
                                                             Optional.empty());
        _links = new ArrayList<>(1);
        _links.add(linkData);
    }

    /**
     * @return title
     */
    @Override
    public String getTitle() {
        return _title;
    }

    @Override
    public Optional<String> getSubtitle() {
        return Optional.empty();
    }

    /**
     * @return publication date, empty if there is none
     */
    @Override
    public Optional<TemporalAccessor> getCreationDate() {
        return _creationDate;
    }

    @Override
    public Optional<TemporalAccessor> getPublicationDate() {
        return _creationDate;
    }

    @Override
    public List<AuthorData> getSureAuthors() {
        return _authors;
    }

    @Override
    public Locale getLanguage() {
        return Locale.ENGLISH;
    }

    @Override
    public List<ExtractedLinkData> getLinks() {
        return _links;
    }
}
