package data.jsongenerator;

import java.io.File;
import java.time.temporal.TemporalAccessor;
import java.util.HashSet;
import java.util.Optional;

public class ArticleFactory {

    private final HashSet<Article> _articles;

    /**
     *
     */
    public ArticleFactory() {
        _articles = new HashSet<Article>();
    }

    public Article buildArticle(final File page,
                                   final Optional<TemporalAccessor> date) {
        final Article article = new Article(page, date);
        _articles.add(article);
        return article;
    }

    public Optional<Article> getArticle(final String url) {

        for (Article a: _articles) { // TODO this is completely stupid, use a HashMap indexed on the URL to be much faster
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
        final Article a[] = _articles.toArray(new Article[0]);
        return a;
    }
}
