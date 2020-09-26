package utils.xmlparsing;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import utils.XMLHelper;

public class XmlParser {

    public static ArticleData parseArticleNode(final Element articleNode) {

        if (!XMLHelper.isOfType(articleNode, NodeType.ARTICLE)) {
            throw new UnsupportedOperationException("parseArticleNode called with wrong node (" + articleNode.getTagName() + ")");
        }

        final List<Element> dateNodes =  getChildElements(articleNode, NodeType.DATE);
        Optional<TemporalAccessor> date = Optional.empty();
        if (dateNodes.size() == 1) {
            final TemporalAccessor dt = XmlParser.parseDateNode(dateNodes.get(0));
            date = Optional.of(dt);
        } else if (dateNodes.size() > 1) {
            throw new UnsupportedOperationException("Wrong number of DATE nodes (" + dateNodes.size() + ")");
        }

        final List<LinkData> links = new ArrayList<LinkData>();
        for (final Element linkNode: getChildElements(articleNode, NodeType.X)) {
            links.add(XmlParser.parseXNode(linkNode));
        }

        final List<AuthorData> authors = new ArrayList<AuthorData>();
        for (final Element authorNode: getChildElements(articleNode, NodeType.AUTHOR)) {
            authors.add(XmlParser.parseAuthorNode(authorNode));
        }

        return new ArticleData(date, authors, links);
    }


    public static KeywordData parseKeywordNode(final Element keywordNode) {

        if (!XMLHelper.isOfType(keywordNode, NodeType.KEYWORD)) {
            throw new UnsupportedOperationException("parseKeywordNode called with wrong node (" + keywordNode.getTagName() + ")");
        }

        final NodeList children =  keywordNode.getChildNodes();

        if (children.getLength() != 2) {
            throw new UnsupportedOperationException("KEYWORD node should have two children");
        }
        if (!XMLHelper.isOfType(children.item(0), NodeType.KEYID)) {
            throw new UnsupportedOperationException("first child of KEYWORD node must be a KEYID node");
        }
        if (!XMLHelper.isOfType(children.item(1), NodeType.KEYEDTEXT)) {
            throw new UnsupportedOperationException("second child of KEYWORD node must be a KEYEDTEXT node");
        }
        final String keyId = children.item(0).getTextContent();
        final String keyText = children.item(1).getTextContent();

        final Element grandParent = (Element)keywordNode.getParentNode().getParentNode();
        Optional<ArticleData> article = Optional.empty();
        final List<LinkData> links = new ArrayList<LinkData>();
        if (XMLHelper.isOfType(grandParent, NodeType.ARTICLE)) {
            article = Optional.of(parseArticleNode(grandParent));
        } else if (XMLHelper.isOfType(grandParent, NodeType.CLIST)) {
            for (final Element itemNode: getChildElements(grandParent, NodeType.ITEM)) {
                final NodeList child =  itemNode.getChildNodes();
                if (child.getLength() != 1) {
                    throw new UnsupportedOperationException("ITEM should have single child node");
                }
                if ((child.item(0).getNodeType() != Node.ELEMENT_NODE) || !XMLHelper.isOfType(child.item(0), NodeType.X)) {
                    throw new UnsupportedOperationException("ITEM should have an X child node");
                }
                links.add(XmlParser.parseXNode((Element)child.item(0)));
            }
        } else {
            throw new UnsupportedOperationException("grandparent of KEYWORD node must be a CLIST or ARTICLE node");
        }

        return new KeywordData(keyId, keyText, article, links);
    }

    public static LinkData parseXNode(final Element xNode) {

        if (!XMLHelper.isOfType(xNode, NodeType.X)) {
            throw new UnsupportedOperationException("parseXNode called with wrong node (" + xNode.getTagName() + ")");
        }

        final NodeList titleNodes = XMLHelper.getDescendantsByNodeType(xNode, NodeType.T);
        if (titleNodes.getLength() != 1) {
            throw new UnsupportedOperationException("Wrong number of T nodes (" + titleNodes.getLength() + ") in \"" + xNode.getTextContent() + "\"");
        }
        final String title = ((Element)titleNodes.item(0)).getTextContent();

        final NodeList subtitleNodes = XMLHelper.getDescendantsByNodeType(xNode,NodeType.ST);
        final String subtitles[] = new String[subtitleNodes.getLength()];
        for (int k = 0; k < subtitleNodes.getLength(); k++) {
            subtitles[k] = ((Element)subtitleNodes.item(k)).getTextContent();
        }

        final List<Element> urlNodes = getChildElements(xNode, NodeType.A);
        if (urlNodes.size() != 1) {
            throw new UnsupportedOperationException("Wrong number of A nodes (" + urlNodes.size() + ") in \"" + title + "\"");
        }
        final String url = urlNodes.get(0).getTextContent();

        final NodeList languageNodes = XMLHelper.getDescendantsByNodeType(xNode, NodeType.L);
        if (languageNodes.getLength() == 0) {
            throw new UnsupportedOperationException("Wrong number of L nodes (" + languageNodes.getLength() + ") in \"" + title + "\"");
        }
        final String languages[] = new String[languageNodes.getLength()];
        for (int k = 0; k < languageNodes.getLength(); k++) {
            languages[k] = ((Element)languageNodes.item(k)).getTextContent();
        }

        final NodeList formatNodes = XMLHelper.getDescendantsByNodeType(xNode, NodeType.F);
        if (formatNodes.getLength() == 0) {
            throw new UnsupportedOperationException("Wrong number of F nodes (" + formatNodes.getLength() + ") in \"" + title + "\"");
        }
        final String formats[] = new String[formatNodes.getLength()];
        for (int k = 0; k < formatNodes.getLength(); k++) {
            formats[k] = ((Element)formatNodes.item(k)).getTextContent();
        }

        final NodeList durationNodes =  XMLHelper.getDescendantsByNodeType(xNode, NodeType.DURATION);
        Optional<Duration> duration = Optional.empty();
        if (durationNodes.getLength() == 1) {
            duration = Optional.of(parseDurationNode((Element)durationNodes.item(0)));
        }  else if (durationNodes.getLength() > 1) {
            throw new UnsupportedOperationException("Wrong number of DURATION nodes (" + durationNodes.getLength() + ") in \"" + title + "\"");
        }

        final List<Element> dateNodes =  getChildElements(xNode, NodeType.DATE);
        Optional<TemporalAccessor> publicationDate = Optional.empty();
        if (dateNodes.size() == 1) {
            final TemporalAccessor dt = XmlParser.parseDateNode(dateNodes.get(0));
            publicationDate = Optional.of(dt);
        } else if (dateNodes.size() > 1) {
            throw new UnsupportedOperationException("Wrong number of DATE nodes (" + dateNodes.size() + ") in \"" + title + "\"");
        }

        final Attr statusAttribute = xNode.getAttributeNode("status");
        final Optional<String> status = (statusAttribute != null) ? Optional.of(statusAttribute.getValue())
                                                                  : Optional.empty();

        final Attr protectionAttribute = xNode.getAttributeNode("protection");
        final Optional<String> protection = (protectionAttribute != null) ? Optional.of(protectionAttribute.getValue())
                                                                          : Optional.empty();

        return new LinkData(title, subtitles, url, status, protection, formats, languages, duration, publicationDate);
    }

    public static AuthorData parseAuthorNode(final Element authorNode) {

        if (!XMLHelper.isOfType(authorNode, NodeType.AUTHOR)) {
            throw new UnsupportedOperationException("parseAuthorNode called with wrong node (" + authorNode.getTagName() + ")");
        }

        Optional<String> namePrefix = Optional.empty();
        final NodeList namePrefixList = XMLHelper.getDescendantsByNodeType(authorNode, NodeType.NAMEPREFIX);
        if (namePrefixList.getLength() == 1) {
            namePrefix = Optional.of(namePrefixList.item(0).getTextContent());
        } else if (namePrefixList.getLength() > 1) {
            throw new UnsupportedOperationException("Wrong number of NAMEPREFIX nodes (" + namePrefixList.getLength() + ") in string \"" + authorNode.getTextContent() + "\"");
        }

        Optional<String> firstName = Optional.empty();
        final NodeList firstNameList = XMLHelper.getDescendantsByNodeType(authorNode, NodeType.FIRSTNAME);
        if (firstNameList.getLength() == 1) {
            firstName = Optional.of(firstNameList.item(0).getTextContent());
        } else if (firstNameList.getLength() > 1) {
            throw new UnsupportedOperationException("Wrong number of FIRSTNAME nodes (" + firstNameList.getLength() + ") in string \"" + authorNode.getTextContent() + "\"");
        }

        Optional<String> middleName = Optional.empty();
        final NodeList middleNameList = XMLHelper.getDescendantsByNodeType(authorNode, NodeType.MIDDLENAME);
        if (middleNameList.getLength() == 1) {
            middleName = Optional.of(middleNameList.item(0).getTextContent());
        } else if (middleNameList.getLength() > 1) {
            throw new UnsupportedOperationException("Wrong number of MIDDLENAME nodes (" + middleNameList.getLength() + ") in string \"" + authorNode.getTextContent() + "\"");
        }

        Optional<String> lastName = Optional.empty();
        final NodeList lastNameList = XMLHelper.getDescendantsByNodeType(authorNode, NodeType.LASTNAME);
        if (lastNameList.getLength() == 1) {
            lastName = Optional.of(lastNameList.item(0).getTextContent());
        } else if (lastNameList.getLength() > 1) {
            throw new UnsupportedOperationException("Wrong number of LASTNAME nodes (" + lastNameList.getLength() + ") in string \"" + authorNode.getTextContent() + "\"");
        }

        Optional<String> nameSuffix = Optional.empty();
        final NodeList nameSuffixList = XMLHelper.getDescendantsByNodeType(authorNode, NodeType.NAMESUFFIX);
        if (nameSuffixList.getLength() == 1) {
            nameSuffix = Optional.of(nameSuffixList.item(0).getTextContent());
        } else if (nameSuffixList.getLength() > 1) {
            throw new UnsupportedOperationException("Wrong number of NAMESUFFIX nodes (" + nameSuffixList.getLength() + ") in string \"" + authorNode.getTextContent() + "\"");
        }

        Optional<String> givenName = Optional.empty();
        final NodeList givenNameList = XMLHelper.getDescendantsByNodeType(authorNode, NodeType.GIVENNAME);
        if (givenNameList.getLength() == 1) {
            givenName = Optional.of(givenNameList.item(0).getTextContent());
        } else if (givenNameList.getLength() > 1) {
            throw new UnsupportedOperationException("Wrong number of GIVENNAME nodes (" + givenNameList.getLength() + ") in string \"" + authorNode.getTextContent() + "\"");
        }

        return new AuthorData(namePrefix, firstName, middleName, lastName, nameSuffix, givenName);
    }

    public static TemporalAccessor parseDateNode(final Element dateNode) {

        if (!XMLHelper.isOfType(dateNode, NodeType.DATE)) {
            throw new UnsupportedOperationException("parseDateNode called with wrong node (" + dateNode.getTagName() + ")");
        }

        final NodeList yearNodes = XMLHelper.getDescendantsByNodeType(dateNode, NodeType.YEAR);
        if (yearNodes.getLength() == 1) {
            final int year = Integer.parseInt(yearNodes.item(0).getTextContent());
            final NodeList monthNodes = XMLHelper.getDescendantsByNodeType(dateNode, NodeType.MONTH);
            if (monthNodes.getLength() == 1) {
                final int month = Integer.parseInt(monthNodes.item(0).getTextContent());
                final NodeList dayNodes = XMLHelper.getDescendantsByNodeType(dateNode, NodeType.DAY);
                if (dayNodes.getLength() == 1) {
                    final int day = Integer.parseInt(dayNodes.item(0).getTextContent());
                    return LocalDate.of(year, month, day);
                } else if (dayNodes.getLength() > 1) {
                    throw new UnsupportedOperationException("Wrong number of DAY nodes (" + dayNodes.getLength() + ") in string \"" + dateNode.getTextContent() + "\"");
                }
                return YearMonth.of(year, month);
            } else if (monthNodes.getLength() > 1) {
                throw new UnsupportedOperationException("Wrong number of MONTH nodes (" + monthNodes.getLength() + ") in string \"" + dateNode.getTextContent() + "\"");
            }
            return Year.of(year);
        }
        throw new UnsupportedOperationException("Wrong number of YEAR nodes (" + yearNodes.getLength() + ") in string \"" + dateNode.getTextContent() + "\"");
    }

    private static Duration parseDurationNode(final Element durationNode) {

        if (!XMLHelper.isOfType(durationNode, NodeType.DURATION)) {
            throw new UnsupportedOperationException("parseDurationNode called with wrong node (" + durationNode.getTagName() + ")");
        }

        Duration duration;

        final NodeList secondsNodes = XMLHelper.getDescendantsByNodeType(durationNode, NodeType.SECOND);
        if (secondsNodes.getLength() == 1) {
            final long seconds = Long.parseLong(secondsNodes.item(0).getTextContent());
            duration = Duration.ofSeconds(seconds);
        } else {
            throw new UnsupportedOperationException("Wrong number of SECOND nodes (" + secondsNodes.getLength() + ") in string \"" + durationNode.getTextContent() + "\"");
        }

        final NodeList minutesNodes = XMLHelper.getDescendantsByNodeType(durationNode, NodeType.MINUTE);
        if (minutesNodes.getLength() == 1) {
            final long minutes = Long.parseLong(minutesNodes.item(0).getTextContent());
            duration = duration.plusMinutes(minutes);
        } else if (minutesNodes.getLength() > 1) {
            throw new UnsupportedOperationException("Wrong number of MINUTE nodes (" + minutesNodes.getLength() + ") in string \"" + durationNode.getTextContent() + "\"");
        }

        final NodeList hoursNodes = XMLHelper.getDescendantsByNodeType(durationNode, NodeType.HOUR);
        if (hoursNodes.getLength() == 1) {
            final long  hours = Long.parseLong(hoursNodes.item(0).getTextContent());
            duration = duration.plusHours(hours);
        } else if (hoursNodes.getLength() > 1) {
            throw new UnsupportedOperationException("Wrong number of HOUR nodes (" + hoursNodes.getLength() + ") in string \"" + durationNode.getTextContent() + "\"");
        }

        return duration;
    }

    private static List<Element> getChildElements(final Element element,
                                                  final NodeType type) {

        final List<Element> list = new ArrayList<Element>();

        final NodeList children =  element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeType() == Node.ELEMENT_NODE) {
                final Element child = (Element)children.item(i);
                if (XMLHelper.isOfType(child, type)) {
                    list.add(child);
                }
            }
        }

        return list;
    }
}
