package data.jsongenerator;

import java.util.ArrayList;
import java.util.Arrays;

public class Keyword {

    private final String _id;
    private final ArrayList<Link> _links;
    private final ArrayList<Article> _articles;

    public Keyword(final String id) {
        _id = id;
        _links = new ArrayList<>();
        _articles = new ArrayList<>();
    }

    public String getId() {
        return _id;
    }

    public void addLink(final Link link) {
        _links.add(link);
    }

    public Link[] getLinks() {
        final Link l[] = _links.toArray(new Link[0]);
        Arrays.sort(l);
        return l;
    }

    public void addArticle(final Article article) {
        _articles.add(article);
    }

    public ArrayList<Article> getArticles() {
        return _articles;
    }
}
