package data.jsongenerator;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import utils.Logger;
import utils.XMLHelper;
import utils.xmlparsing.ArticleData;
import utils.xmlparsing.AuthorData;
import utils.xmlparsing.KeywordData;
import utils.xmlparsing.LinkData;
import utils.xmlparsing.ElementType;
import utils.xmlparsing.XmlParser;

public class Parser {

    private final DocumentBuilder _builder;
    private final ArticleFactory _articleFactory;
    private final LinkFactory _linkFactory;
    private final AuthorFactory _authorFactory;
    private final KeywordFactory _keywordFactory;

    /**
     * @param articleFactory
     * @param linkFactory
     * @param authorFactory
     */
    public Parser(final ArticleFactory articleFactory,
                  final LinkFactory linkFactory,
                  final AuthorFactory authorFactory,
                  final KeywordFactory keywordFactory) {
        _articleFactory = articleFactory;
        _authorFactory = authorFactory;
        _linkFactory = linkFactory;
        _keywordFactory = keywordFactory;
        _builder = XMLHelper.buildDocumentBuilder();
    }

    /**
     * @param file
     */
    public void parse(final File file) {

        try {
            final Document document = _builder.parse(file);
            extractArticles(document, file);
            extractKeywords(document, file);
        } catch (final SAXException se) {
            Logger.log(Logger.Level.ERROR)
                  .append("Failed to parse the XML file")
                  .append(se)
                  .submit();
        } catch (final IOException ioe) {
            Logger.log(Logger.Level.ERROR)
                  .append("Failed to read the XML file")
                  .append(ioe)
                  .submit();
        }
    }

    /**
     * @param document
     * @param file
     */
    private void extractArticles(final Document document,
                                 final File file) {

        final Element racine = document.getDocumentElement();
        final NodeList list = XMLHelper.getDescendantsByElementType(racine, ElementType.ARTICLE);

        for (int i = 0; i < list.getLength(); i++) {

            final Element articleNode = (Element)list.item(i);

            final ArticleData articleData = XmlParser.parseArticleElement(articleNode);

            final Article article = _articleFactory.buildArticle(file, articleData.getDate());

            for (int j = 0; j < articleData.getLinks().size(); j++) {
                final Link link = _linkFactory.newLink(article, articleData.getLinks().get(j));
                article.addLink(link);
            }

            for (int j = 0; j < articleData.getAuthors().size(); j++) {
                final Author author = _authorFactory.buildAuthor(articleData.getAuthors().get(j));
                author.addArticle(article);
                article.addAuthor(author);
            }
        }
    }

    /**
     * @param document
     * @param file
     */
    private void extractKeywords(final Document document,
                                 final File file) {

        final Element racine = document.getDocumentElement();
        final NodeList list = XMLHelper.getDescendantsByElementType(racine, ElementType.KEYWORD);

        for (int i = 0; i < list.getLength(); i++) {

            final Element keywordNode = (Element)list.item(i);

            final KeywordData keywordData = XmlParser.parseKeywordElement(keywordNode);

            final Keyword keyword = _keywordFactory.newKeyword(keywordData.getKeyId());

            if (keywordData.getArticle().isPresent()) {
                final Optional<Article> article =  _articleFactory.getArticle(keywordData.getArticle().get().getLinks().get(0).getUrl());
                if (article.isEmpty()) {
                    throw new UnsupportedOperationException("Cannot retrieve article of KEYWORD");
                }
                keyword.addArticle(article.get());
            }

            for (final LinkData link : keywordData.getLinks()) {
                final Link l = _linkFactory.newLink(null, link);
                keyword.addLink(l);
            }
        }
    }


       /**
     * @param file
     */
    public void parsePersonFile(final File file) {

        try {
            final Document document = _builder.parse(file);
            extractPersonLinks(document, file);
        } catch (final SAXException se) {
            Logger.log(Logger.Level.ERROR)
                  .append("Failed to parse the XML file")
                  .append(se)
                  .submit();
        } catch (final IOException ioe) {
            Logger.log(Logger.Level.ERROR)
                  .append("Failed to read the XML file")
                  .append(ioe)
                  .submit();
        }
    }

    /**
     * @param document
     * @param file
     */
    private void extractPersonLinks(final Document document,
                                    final File file) {

        final Element racine = document.getDocumentElement();
        final NodeList list = XMLHelper.getDescendantsByElementType(racine, ElementType.CLIST);

        for (int i = 0; i < list.getLength(); i++) {

            final Element clistNode = (Element)list.item(i);

            final Node titleNode =  clistNode.getFirstChild();
            if (!XMLHelper.isOfType(titleNode, ElementType.TITLE)) {
                throw new UnsupportedOperationException("Unexpected XML structure (the first child of a CLIST node is not a TITLE node)");
            }

            final Node authorNode =  titleNode.getFirstChild();
            if (!XMLHelper.isOfType(authorNode, ElementType.AUTHOR)) {
                throw new UnsupportedOperationException("Unexpected XML structure (the first child of the first child of a CLIST node is not a AUTHOR node)");
            }

            final AuthorData authorData = XmlParser.parseAuthorElement((Element)authorNode);

            final Author author = _authorFactory.peekAuthor(authorData);

            if (author == null) continue; // TODO ne devrait jamais arriver ?

            for (int j = 0; j < XMLHelper.getDescendantsByElementType(clistNode, ElementType.ITEM).getLength(); j++) {

                final Element linkNode = (Element)XMLHelper.getDescendantsByElementType(clistNode, ElementType.ITEM).item(j);
                if (linkNode.getChildNodes().getLength() != 1) {
                    throw new UnsupportedOperationException("Illegal number of children nodes");
                }
                if (!XMLHelper.isOfType((Element)linkNode.getChildNodes().item(0), ElementType.X)) {
                    throw new UnsupportedOperationException("Illegal child node");
                }
                final LinkData linkData = XmlParser.parseXElement((Element)linkNode.getChildNodes().item(0));
                final Link link = _linkFactory.newLink(null, linkData);

                author.addLink(link);
            }
        }
    }
}
