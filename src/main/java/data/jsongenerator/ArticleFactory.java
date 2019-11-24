package data.jsongenerator;

import java.io.File;
import java.util.HashSet;

/**
 * @author Laurent
 *
 */
public class ArticleFactory {

	private final HashSet<Article> a_articles;
	
	/**
	 * 
	 */
	public ArticleFactory() {
		a_articles = new HashSet<Article>();
	}
	
	/**
	 * @param page 
	 * @param year 
	 * @param month 
	 * @param day 
	 * @return
	 */
	public Article buildArticle(final File page,
		       				    final Integer year,
		       				    final Integer month,
		       				    final Integer day) {
		final Article article = new Article(page, year, month, day);
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
