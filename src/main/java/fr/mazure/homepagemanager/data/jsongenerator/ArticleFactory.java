package fr.mazure.homepagemanager.data.jsongenerator;

import java.io.File;
import java.time.temporal.TemporalAccessor;
import java.util.HashSet;
import java.util.Optional;

/**
 *
 */
public class ArticleFactory {

    private final HashSet<Article> _articles;

    /**
     *
     */
    public ArticleFactory() {
        _articles = new HashSet<>();
    }

    /**
     * @param page
     * @param date
     * @return
     */
    public Article buildArticle(final File page,
                                final Optional<TemporalAccessor> date) {
        final Article article = new Article(page, date);
        _articles.add(article);
        return article;
    }

    /**
     * @param url
     * @return
     */
    public Optional<Article> getArticle(final String url) {

        for (final Article a: _articles) { // TODO this is completely stupid, use a HashMap indexed on the URL to be much faster
            if (a.getLinks()[0].getUrl().equals(url)) {
                return Optional.of(a);
            }
        }

        return Optional.empty();
    }

    /**
     * @return sorted list of articles
     */
    public Article[] getArticles() {
        return _articles.toArray(new Article[0]);
    }
}
