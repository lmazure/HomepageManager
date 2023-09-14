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
     * @param id
     */
    public Keyword(final String id) {
        _id = id;
        _links = new ArrayList<>();
        _articles = new ArrayList<>();
    }

    /**
     * @return
     */
    public String getId() {
        return _id;
    }

    /**
     * @param link
     */
    public void addLink(final Link link) {
        _links.add(link);
    }

    /**
     * @return
     */
    public Link[] getLinks() {
        final Link l[] = _links.toArray(new Link[0]);
        Arrays.sort(l);
        return l;
    }

    /**
     * @param article
     */
    public void addArticle(final Article article) {
        _articles.add(article);
    }

    /**
     * @return
     */
    public ArrayList<Article> getArticles() {
        return _articles;
    }
}
