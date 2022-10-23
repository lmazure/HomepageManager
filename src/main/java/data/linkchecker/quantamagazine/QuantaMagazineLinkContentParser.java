package data.linkchecker.quantamagazine;

import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import data.linkchecker.ContentParserException;
import data.linkchecker.ExtractedLinkData;
import data.linkchecker.LinkContentParserUtils;
import data.linkchecker.LinkDataExtractor;
import data.linkchecker.TextParser;
import utils.internet.HtmlHelper;
import utils.xmlparsing.AuthorData;
import utils.xmlparsing.LinkFormat;

public class QuantaMagazineLinkContentParser extends LinkDataExtractor {

    private final String _data;
    private static final TextParser s_titleParser
        = new TextParser("<div class=\"ml025 h3 noe mv0\">",
                         "</div>",
                         "QuantaMagazine",
                         "title");
    private static final TextParser s_subtitleParser
        = new TextParser("<div class=\"post__title__excerpt wysiwyg p italic mb1 mt025 pr2 o4 theme__text[- a-z]*\">",
                         "</div>",
                         "QuantaMagazine",
                         "subtitle");
    private static final TextParser s_dateParser
        = new TextParser(",\"date\":\"",
                         "[^\"]*",
                         "T[0-9][0-9]:[0-9][0-9]:[0-9][0-9]\",\"featured_media_image\":",
                         "QuantaMagazine",
                         "date");
    private static final Pattern s_authorPattern
        = Pattern.compile("<a (class=\"[^\"]+\" )?href=\"/authors/[^/]+/\"><span [^>]+>([^<]+)</span></a>");

    public QuantaMagazineLinkContentParser(final String url,
                                           final String data) {
        super(url);
        _data = data;
    }

    @Override
    public String getTitle() throws ContentParserException {
        return HtmlHelper.cleanContent(s_titleParser.extract(_data));
    }

    @Override
    public Optional<String> getSubtitle() throws ContentParserException {
        return Optional.of(HtmlHelper.cleanContent(s_subtitleParser.extract(_data)));
    }

    @Override
    public Optional<TemporalAccessor> getDate() throws ContentParserException {
        return Optional.of(LocalDate.parse(HtmlHelper.cleanContent(s_dateParser.extract(_data))));
    }

    @Override
    public List<AuthorData> getSureAuthors() throws ContentParserException {
        final List<AuthorData> authors = new ArrayList<>();
        final Matcher m = s_authorPattern.matcher(_data);
        while (m.find()) {
            authors.add(LinkContentParserUtils.getAuthor(m.group(2)));
        }
        if (authors.size() == 0) {
            throw new ContentParserException("Failed to find author in QuantaMagazine");
        }
        return authors;
    }

    @Override
    public List<ExtractedLinkData> getLinks() throws ContentParserException {
        final ExtractedLinkData linkData = new ExtractedLinkData(getTitle(),
                                                                 new String[] { getSubtitle().get() },
                                                                 getUrl().toString(),
                                                                 Optional.empty(),
                                                                 Optional.empty(),
                                                                 new LinkFormat[] { LinkFormat.HTML },
                                                                 new Locale[] {getLanguage() },
                                                                 Optional.empty(),
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
