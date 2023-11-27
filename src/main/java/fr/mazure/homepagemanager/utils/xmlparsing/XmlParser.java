package fr.mazure.homepagemanager.utils.xmlparsing;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
*
*/
public class XmlParser {

    /**
     * Extract article data from a ARTICLE node
     *
     * @param articleElement the node
     * @return the article data
     * @throws XmlParsingException failure to parse the node
     */
    public static ArticleData parseArticleElement(final Element articleElement) throws XmlParsingException {

        if (!XmlHelper.isOfType(articleElement, ElementType.ARTICLE)) {
            throw new XmlParsingException("parseArticleNode called with wrong node (" + articleElement.getTagName() + ")");
        }

        final List<Element> dateNodes =  XmlHelper.getChildrenByElementType(articleElement, ElementType.DATE);
        Optional<TemporalAccessor> date = Optional.empty();
        if (dateNodes.size() == 1) {
            final TemporalAccessor dt = XmlParser.parseDateElement(dateNodes.get(0));
            date = Optional.of(dt);
        } else if (dateNodes.size() > 1) {
            throw new XmlParsingException("Wrong number of DATE nodes (" + dateNodes.size() + ")");
        }

        final List<LinkData> links = new ArrayList<>();
        for (final Element linkNode: XmlHelper.getChildrenByElementType(articleElement, ElementType.X)) {
            links.add(XmlParser.parseXElement(linkNode));
        }

        final List<AuthorData> authors = new ArrayList<>();
        for (final Element authorNode: XmlHelper.getChildrenByElementType(articleElement, ElementType.AUTHOR)) {
            authors.add(XmlParser.parseAuthorElement(authorNode));
        }

        String comment;
        final List<Element> commentNodes = XmlHelper.getChildrenByElementType(articleElement, ElementType.COMMENT);
        if (commentNodes.size() == 1) {
            comment = commentNodes.get(0).getTextContent();
        } else {
            throw new XmlParsingException("Wrong number of COMMENT nodes (" + commentNodes.size() + ")");
        }

        return new ArticleData(date, authors, links, comment);
    }

    /**
     * Extract keyword data from a KEYWORD node
     *
     * @param keywordElement the node
     * @return the keyword data
     * @throws XmlParsingException failure to parse the node
     */
    public static KeywordData parseKeywordElement(final Element keywordElement) throws XmlParsingException {

        if (!XmlHelper.isOfType(keywordElement, ElementType.KEYWORD)) {
            throw new XmlParsingException("parseKeywordNode called with wrong node (" + keywordElement.getTagName() + ")");
        }

        final NodeList children =  keywordElement.getChildNodes();

        if (children.getLength() != 2) {
            throw new XmlParsingException("KEYWORD node should have two children");
        }
        if (!XmlHelper.isOfType(children.item(0), ElementType.KEYID)) {
            throw new XmlParsingException("first child of KEYWORD node must be a KEYID node");
        }
        if (!XmlHelper.isOfType(children.item(1), ElementType.KEYEDTEXT)) {
            throw new XmlParsingException("second child of KEYWORD node must be a KEYEDTEXT node");
        }
        final String keyId = children.item(0).getTextContent();
        final String keyText = children.item(1).getTextContent();

        final Element grandParent = (Element)keywordElement.getParentNode().getParentNode();
        final List<ArticleData> articles = new ArrayList<>();
        final List<LinkData> links = new ArrayList<>();
        if (XmlHelper.isOfType(grandParent, ElementType.ARTICLE)) {
            // the KEYWORD is in an article
            articles.add(parseArticleElement(grandParent));
        } else if (XmlHelper.isOfType(grandParent, ElementType.CLIST)) {
            // the KEYWORD is in the title of a list of keywords
            for (final Element itemNode: XmlHelper.getChildrenByElementType(grandParent, ElementType.ITEM)) {
                final NodeList child =  itemNode.getChildNodes();
                if (child.getLength() != 1) {
                    throw new XmlParsingException("ITEM should have single child node");
                }
                if ((child.item(0).getNodeType() != Node.ELEMENT_NODE) || !XmlHelper.isOfType(child.item(0), ElementType.X)) {
                    throw new XmlParsingException("ITEM should have an X child node");
                }
                links.add(XmlParser.parseXElement((Element)child.item(0)));
            }
        } else if (XmlHelper.isOfType(grandParent, ElementType.BLIST)) {
            // the KEYWORD is in the title of a list of articles
            final NodeList articleNodes = XmlHelper.getDescendantsByElementType(grandParent, ElementType.ARTICLE);
            for (int i = 0; i < articleNodes.getLength(); i++ ) {
                articles.add(XmlParser.parseArticleElement((Element)articleNodes.item(i)));
            }
        } else {
            throw new XmlParsingException("grandparent of KEYWORD node must be a BLIST, CLIST, or ARTICLE node (it is currently a " + grandParent.getTagName() + ")");
        }

        return new KeywordData(keyId, keyText, articles, links);
    }

    /**
     * Extract link data from an X node
     *
     * @param xElement the node
     * @return the link data
     * @throws XmlParsingException failure to parse the node
     */
    public static LinkData parseXElement(final Element xElement) throws XmlParsingException {

        if (!XmlHelper.isOfType(xElement, ElementType.X)) {
            throw new XmlParsingException("parseXNode called with wrong node (" + xElement.getTagName() + ")");
        }

        final NodeList titleNodes = XmlHelper.getDescendantsByElementType(xElement, ElementType.T);
        if (titleNodes.getLength() != 1) {
            throw new XmlParsingException("Wrong number of T nodes (" + titleNodes.getLength() + ") in \"" + xElement.getTextContent() + "\"");
        }
        final String title = ((Element)titleNodes.item(0)).getTextContent();

        final NodeList subtitleNodes = XmlHelper.getDescendantsByElementType(xElement,ElementType.ST);
        final String subtitles[] = new String[subtitleNodes.getLength()];
        for (int k = 0; k < subtitleNodes.getLength(); k++) {
            subtitles[k] = ((Element)subtitleNodes.item(k)).getTextContent();
        }

        final List<Element> urlNodes = XmlHelper.getChildrenByElementType(xElement, ElementType.A);
        if (urlNodes.size() != 1) {
            throw new XmlParsingException("Wrong number of A nodes (" + urlNodes.size() + ") in \"" + title + "\"");
        }
        final String url = urlNodes.get(0).getTextContent();

        final NodeList languageNodes = XmlHelper.getDescendantsByElementType(xElement, ElementType.L);
        if (languageNodes.getLength() == 0) {
            throw new XmlParsingException("Wrong number of L nodes (0) in \"" + title + "\"");
        }
        final Locale languages[] = new Locale[languageNodes.getLength()];
        for (int k = 0; k < languageNodes.getLength(); k++) {
            languages[k] = LinkData.parseLanguage(((Element)languageNodes.item(k)).getTextContent());
        }

        final List<Element> formatNodes = XmlHelper.getChildrenByElementType(xElement, ElementType.F);
        if (formatNodes.isEmpty()) {
            throw new XmlParsingException("Wrong number of F nodes (0) in \"" + title + "\"");
        }
        final LinkFormat formats[] = new LinkFormat[formatNodes.size()];
        for (int k = 0; k < formatNodes.size(); k++) {
            formats[k] = LinkData.parseFormat((formatNodes.get(k)).getTextContent());
        }

        final NodeList durationNodes =  XmlHelper.getDescendantsByElementType(xElement, ElementType.DURATION);
        Optional<Duration> duration = Optional.empty();
        if (durationNodes.getLength() == 1) {
            duration = Optional.of(parseDurationElement((Element)durationNodes.item(0)));
        } else if (durationNodes.getLength() > 1) {
            throw new XmlParsingException("Wrong number of DURATION nodes (" + durationNodes.getLength() + ") in \"" + title + "\"");
        }

        final List<Element> dateNodes =  XmlHelper.getChildrenByElementType(xElement, ElementType.DATE);
        Optional<TemporalAccessor> publicationDate = Optional.empty();
        if (dateNodes.size() == 1) {
            final TemporalAccessor dt = XmlParser.parseDateElement(dateNodes.get(0));
            publicationDate = Optional.of(dt);
        } else if (dateNodes.size() > 1) {
            throw new XmlParsingException("Wrong number of DATE nodes (" + dateNodes.size() + ") in \"" + title + "\"");
        }

        final Attr statusAttribute = xElement.getAttributeNode("status");
        final LinkStatus status = (statusAttribute != null) ? LinkData.parseStatus(statusAttribute.getValue())
                                                            : LinkStatus.OK;

        final Attr protectionAttribute = xElement.getAttributeNode("protection");
        final LinkProtection protection = (protectionAttribute != null) ? LinkData.parseProtection(protectionAttribute.getValue())
                                                                        : LinkProtection.NO_REQUIRED_REGISTRATION;

        final List<Element> feedNodes =  XmlHelper.getChildrenByElementType(xElement, ElementType.FEED);
        Optional<FeedData> feed = Optional.empty();
        if (feedNodes.size() == 1) {
            final FeedData data = XmlParser.parseFeedElement(feedNodes.get(0));
            feed = Optional.of(data);
        } else if (dateNodes.size() > 1) {
            throw new XmlParsingException("Wrong number of FEED nodes (" + feedNodes.size() + ") in \"" + title + "\"");
        }
        return new LinkData(title, subtitles, url, status, protection, formats, languages, duration, publicationDate, feed);
    }

    /**
     * Extract author data from a AUTHOR node
     *
     * @param authorElement the node
     * @return author data
     * @throws XmlParsingException failure to parse the node
     */
    public static AuthorData parseAuthorElement(final Element authorElement) throws XmlParsingException {

        if (!XmlHelper.isOfType(authorElement, ElementType.AUTHOR)) {
            throw new XmlParsingException("parseAuthorNode called with wrong node (" + authorElement.getTagName() + ")");
        }

        Optional<String> namePrefix = Optional.empty();
        final NodeList namePrefixList = XmlHelper.getDescendantsByElementType(authorElement, ElementType.NAMEPREFIX);
        if (namePrefixList.getLength() == 1) {
            namePrefix = Optional.of(namePrefixList.item(0).getTextContent());
        } else if (namePrefixList.getLength() > 1) {
            throw new XmlParsingException("Wrong number of NAMEPREFIX nodes (" + namePrefixList.getLength() + ") in string \"" + authorElement.getTextContent() + "\"");
        }

        Optional<String> firstName = Optional.empty();
        final NodeList firstNameList = XmlHelper.getDescendantsByElementType(authorElement, ElementType.FIRSTNAME);
        if (firstNameList.getLength() == 1) {
            firstName = Optional.of(firstNameList.item(0).getTextContent());
        } else if (firstNameList.getLength() > 1) {
            throw new XmlParsingException("Wrong number of FIRSTNAME nodes (" + firstNameList.getLength() + ") in string \"" + authorElement.getTextContent() + "\"");
        }

        Optional<String> middleName = Optional.empty();
        final NodeList middleNameList = XmlHelper.getDescendantsByElementType(authorElement, ElementType.MIDDLENAME);
        if (middleNameList.getLength() == 1) {
            middleName = Optional.of(middleNameList.item(0).getTextContent());
        } else if (middleNameList.getLength() > 1) {
            throw new XmlParsingException("Wrong number of MIDDLENAME nodes (" + middleNameList.getLength() + ") in string \"" + authorElement.getTextContent() + "\"");
        }

        Optional<String> lastName = Optional.empty();
        final NodeList lastNameList = XmlHelper.getDescendantsByElementType(authorElement, ElementType.LASTNAME);
        if (lastNameList.getLength() == 1) {
            lastName = Optional.of(lastNameList.item(0).getTextContent());
        } else if (lastNameList.getLength() > 1) {
            throw new XmlParsingException("Wrong number of LASTNAME nodes (" + lastNameList.getLength() + ") in string \"" + authorElement.getTextContent() + "\"");
        }

        Optional<String> nameSuffix = Optional.empty();
        final NodeList nameSuffixList = XmlHelper.getDescendantsByElementType(authorElement, ElementType.NAMESUFFIX);
        if (nameSuffixList.getLength() == 1) {
            nameSuffix = Optional.of(nameSuffixList.item(0).getTextContent());
        } else if (nameSuffixList.getLength() > 1) {
            throw new XmlParsingException("Wrong number of NAMESUFFIX nodes (" + nameSuffixList.getLength() + ") in string \"" + authorElement.getTextContent() + "\"");
        }

        Optional<String> givenName = Optional.empty();
        final NodeList givenNameList = XmlHelper.getDescendantsByElementType(authorElement, ElementType.GIVENNAME);
        if (givenNameList.getLength() == 1) {
            givenName = Optional.of(givenNameList.item(0).getTextContent());
        } else if (givenNameList.getLength() > 1) {
            throw new XmlParsingException("Wrong number of GIVENNAME nodes (" + givenNameList.getLength() + ") in string \"" + authorElement.getTextContent() + "\"");
        }

        return new AuthorData(namePrefix, firstName, middleName, lastName, nameSuffix, givenName);
    }

    /**
     * Extract date data from a DATE node
     *
     * @param dateElement the node
     * @return the date data
     * @throws XmlParsingException failure to parse the node
     */
    public static TemporalAccessor parseDateElement(final Element dateElement) throws XmlParsingException {

        if (!XmlHelper.isOfType(dateElement, ElementType.DATE)) {
            throw new XmlParsingException("parseDateNode called with wrong node (" + dateElement.getTagName() + ")");
        }

        final NodeList yearNodes = XmlHelper.getDescendantsByElementType(dateElement, ElementType.YEAR);
        if (yearNodes.getLength() == 1) {
            final int year = Integer.parseInt(yearNodes.item(0).getTextContent());
            final NodeList monthNodes = XmlHelper.getDescendantsByElementType(dateElement, ElementType.MONTH);
            if (monthNodes.getLength() == 1) {
                final int month = Integer.parseInt(monthNodes.item(0).getTextContent());
                final NodeList dayNodes = XmlHelper.getDescendantsByElementType(dateElement, ElementType.DAY);
                if (dayNodes.getLength() == 1) {
                    final int day = Integer.parseInt(dayNodes.item(0).getTextContent());
                    return LocalDate.of(year, month, day);
                } else if (dayNodes.getLength() > 1) {
                    throw new XmlParsingException("Wrong number of DAY nodes (" + dayNodes.getLength() + ") in string \"" + dateElement.getTextContent() + "\"");
                }
                return YearMonth.of(year, month);
            }
            if (monthNodes.getLength() > 1) {
                throw new XmlParsingException("Wrong number of MONTH nodes (" + monthNodes.getLength() + ") in string \"" + dateElement.getTextContent() + "\"");
            }
            return Year.of(year);
        }
        throw new XmlParsingException("Wrong number of YEAR nodes (" + yearNodes.getLength() + ") in string \"" + dateElement.getTextContent() + "\"");
    }

    /**
     * Extract feed data from a FEED node
     *
     * @param feedElement the node
     * @return the feed data
     * @throws XmlParsingException failure to parse the node
     */
    public static FeedData parseFeedElement(final Element feedElement) throws XmlParsingException {

        if (!XmlHelper.isOfType(feedElement, ElementType.FEED)) {
            throw new XmlParsingException("parseFeedNode called with wrong node (" + feedElement.getTagName() + ")");
        }

        final List<Element> urlNodes = XmlHelper.getChildrenByElementType(feedElement, ElementType.A);
        if (urlNodes.size() != 1) {
            throw new XmlParsingException("Wrong number of A nodes (" + urlNodes.size() + ") in feed \"" + feedElement.getTextContent() + "\"");
        }
        final String url = urlNodes.get(0).getTextContent();

        final List<Element> formatNodes = XmlHelper.getChildrenByElementType(feedElement, ElementType.F);
        if (formatNodes.size() != 1) {
            throw new XmlParsingException("Wrong number of F nodes (0) in feed \"" + feedElement.getTextContent() + "\"");
        }
        final FeedFormat format = FeedData.parseFormat((formatNodes.get(0)).getTextContent());

        return new FeedData(url, format);
    }

    private static Duration parseDurationElement(final Element durationElement) throws XmlParsingException {

        if (!XmlHelper.isOfType(durationElement, ElementType.DURATION)) {
            throw new XmlParsingException("parseDurationNode called with wrong node (" + durationElement.getTagName() + ")");
        }

        Duration duration;

        final NodeList secondsNodes = XmlHelper.getDescendantsByElementType(durationElement, ElementType.SECOND);
        if (secondsNodes.getLength() != 1) {
            throw new XmlParsingException("Wrong number of SECOND nodes (" + secondsNodes.getLength() + ") in string \"" + durationElement.getTextContent() + "\"");
        }
        final long seconds = Long.parseLong(secondsNodes.item(0).getTextContent());
        duration = Duration.ofSeconds(seconds);

        final NodeList minutesNodes = XmlHelper.getDescendantsByElementType(durationElement, ElementType.MINUTE);
        if (minutesNodes.getLength() == 1) {
            final long minutes = Long.parseLong(minutesNodes.item(0).getTextContent());
            duration = duration.plusMinutes(minutes);
        } else if (minutesNodes.getLength() > 1) {
            throw new XmlParsingException("Wrong number of MINUTE nodes (" + minutesNodes.getLength() + ") in string \"" + durationElement.getTextContent() + "\"");
        }

        final NodeList hoursNodes = XmlHelper.getDescendantsByElementType(durationElement, ElementType.HOUR);
        if (hoursNodes.getLength() == 1) {
            final long hours = Long.parseLong(hoursNodes.item(0).getTextContent());
            duration = duration.plusHours(hours);
        } else if (hoursNodes.getLength() > 1) {
            throw new XmlParsingException("Wrong number of HOUR nodes (" + hoursNodes.getLength() + ") in string \"" + durationElement.getTextContent() + "\"");
        }

        return duration;
    }
}
