package data.jsongenerator;

import java.io.File;
import java.util.HashSet;
import java.util.Optional;

import utils.xmlparsing.DateData;

public class ArticleFactory {

	private final HashSet<Article> a_articles;
	
	/**
	 * 
	 */
	public ArticleFactory() {
		a_articles = new HashSet<Article>();
	}
	
	public Article buildArticle(final File page,
		       				    final Optional<DateData> dateData) {
		final Article article = new Article(page, dateData);
		a_articles.add(article);
		return article;
	}
	
	/**
	 * @return sorted list of articles
	 */
	public Article[] getArticles() {
		final Article a[] = a_articles.toArray(new Article[0]);
		return a;
	}
}
