package fr.mazure.homepagemanager.data.jsongenerator;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 */
public class Keyword {

    private final String _id;
    private final ArrayList<Link> _links;
    private final ArrayList<Article> _articles;

    /**
     * Keyword constructor
     *
     * @param id the key ID
     */
    public Keyword(final String id) {
        _id = id;
        _links = new ArrayList<>();
        _articles = new ArrayList<>();
    }

    /**
     * @return the key ID
     */
    public String getId() {
        return _id;
    }

    /**
     * Add a link to the keyword
     *
     * @param link the link to add
     */
    public void addLink(final Link link) {
        _links.add(link);
    }

    /**
     * @return the list of links
     */
    public Link[] getLinks() {
        final Link l[] = _links.toArray(new Link[0]);
        Arrays.sort(l);
        return l;
    }

    /**
     * Add an article to the keyword
     *
     * @param article the article to add
     */
    public void addArticle(final Article article) {
        _articles.add(article);
    }

    /**
     * Get the articles containing the keyword
     *
     * @return the list of articles
     */
    public ArrayList<Article> getArticles() {
        return _articles;
    }
}
