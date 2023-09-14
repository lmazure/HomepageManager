package fr.mazure.homepagemanager.data.jsongenerator;

import java.io.File;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fr.mazure.homepagemanager.utils.Logger;
import fr.mazure.homepagemanager.utils.xmlparsing.ArticleData;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;
import fr.mazure.homepagemanager.utils.xmlparsing.ElementType;
import fr.mazure.homepagemanager.utils.xmlparsing.KeywordData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkData;
import fr.mazure.homepagemanager.utils.xmlparsing.XmlHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.XmlParser;
import fr.mazure.homepagemanager.utils.xmlparsing.XmlParsingException;

/**
 *
 */
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
     * @param keywordFactory
     */
    public Parser(final ArticleFactory articleFactory,
                  final LinkFactory linkFactory,
                  final AuthorFactory authorFactory,
                  final KeywordFactory keywordFactory) {
        _articleFactory = articleFactory;
        _authorFactory = authorFactory;
        _linkFactory = linkFactory;
        _keywordFactory = keywordFactory;
        _builder = XmlHelper.buildDocumentBuilder();
    }

    /**
     * @param file XML file
     * @throws Exception exception if any
     */
    public void parse(final File file) throws Exception {

        try {
            final Document document = _builder.parse(file);
            extractArticles(document, file);
            extractKeywords(document, file);
        } catch (final Exception e) {
            final String errorMessage = "Failed to parse the XML file (" + file + ")";
            Logger.log(Logger.Level.ERROR)
                  .appendln(errorMessage)
                  .append(e)
                  .submit();
            throw new Exception(errorMessage, e);
        }
    }

    /**
     * @param document
     * @param file
     * @throws XmlParsingException
     */
    private void extractArticles(final Document document,
                                 final File file) throws XmlParsingException {

        final Element racine = document.getDocumentElement();
        final NodeList list = XmlHelper.getDescendantsByElementType(racine, ElementType.ARTICLE);

        for (int i = 0; i < list.getLength(); i++) {

            final Element articleNode = (Element)list.item(i);

            final ArticleData articleData = XmlParser.parseArticleElement(articleNode);

            final Article article = _articleFactory.buildArticle(file, articleData.date());

            for (final LinkData element : articleData.links()) {
                final Link link = _linkFactory.newLink(element);
                article.addLink(link);
            }

            for (final AuthorData element : articleData.authors()) {
                final Author author = _authorFactory.buildAuthor(element);
                author.addArticle(article);
                article.addAuthor(author);
            }
        }
    }

    /**
     * @param document
     * @param file
     * @throws XmlParsingException
     */
    private void extractKeywords(final Document document,
                                 final File file) throws XmlParsingException {

        final Element racine = document.getDocumentElement();
        final NodeList list = XmlHelper.getDescendantsByElementType(racine, ElementType.KEYWORD);

        for (int i = 0; i < list.getLength(); i++) {

            final Element keywordNode = (Element)list.item(i);

            final KeywordData keywordData = XmlParser.parseKeywordElement(keywordNode);

            final Keyword keyword = _keywordFactory.newKeyword(keywordData.keyId());

            for (final ArticleData articleData : keywordData.article()) {
                final Optional<Article> article =  _articleFactory.getArticle(articleData.links().get(0).getUrl());
                if (article.isEmpty()) {
                    throw new XmlParsingException("Cannot retrieve article of KEYWORD");
                }
                keyword.addArticle(article.get());
            }

            for (final LinkData link : keywordData.links()) {
                final Link l = _linkFactory.newLink(link);
                keyword.addLink(l);
            }
        }
    }

       /**
     * @param file file conaining a list of persons
     * @throws Exception exception if any
     */
    public void parsePersonFile(final File file) throws Exception {

        try {
            final Document document = _builder.parse(file);
            extractPersonLinks(document, file);
        } catch (final Exception e) {
            final String errorMessage = "Failed to parse the XML person file (" + file + ")";
            Logger.log(Logger.Level.ERROR)
                  .appendln(errorMessage)
                  .append(e)
                  .submit();
            throw new Exception(errorMessage, e);
        }
    }

    /**
     * @param document
     * @param file
     * @throws XmlParsingException
     */
    private void extractPersonLinks(final Document document,
                                    final File file) throws XmlParsingException {

        final Element racine = document.getDocumentElement();
        final NodeList list = XmlHelper.getDescendantsByElementType(racine, ElementType.CLIST);

        for (int i = 0; i < list.getLength(); i++) {

            final Element clistNode = (Element)list.item(i);

            final Node titleNode =  clistNode.getFirstChild();
            if (!XmlHelper.isOfType(titleNode, ElementType.TITLE)) {
                throw new XmlParsingException("Unexpected XML structure (the first child of a CLIST node is not a TITLE node)");
            }

            final Node authorNode =  titleNode.getFirstChild();
            if (!XmlHelper.isOfType(authorNode, ElementType.AUTHOR)) {
                throw new XmlParsingException("Unexpected XML structure (the first child of the first child of a CLIST node is not a AUTHOR node)");
            }

            final AuthorData authorData = XmlParser.parseAuthorElement((Element)authorNode);

            final Author author = _authorFactory.peekAuthor(authorData);

            if (author == null)
             {
                continue; // TODO ne devrait jamais arriver ?
            }

            for (int j = 0; j < XmlHelper.getDescendantsByElementType(clistNode, ElementType.ITEM).getLength(); j++) {

                final Element linkNode = (Element)XmlHelper.getDescendantsByElementType(clistNode, ElementType.ITEM).item(j);
                if (linkNode.getChildNodes().getLength() != 1) {
                    throw new XmlParsingException("Illegal number of children nodes");
                }
                if (!XmlHelper.isOfType((Element)linkNode.getChildNodes().item(0), ElementType.X)) {
                    throw new XmlParsingException("Illegal child node");
                }
                final LinkData linkData = XmlParser.parseXElement((Element)linkNode.getChildNodes().item(0));
                final Link link = _linkFactory.newLink(linkData);

                author.addLink(link);
            }
        }
    }
}
