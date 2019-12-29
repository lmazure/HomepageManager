package data.jsongenerator2;

import java.io.File;
import java.util.HashSet;

/**
 * @author Laurent
 *
 */
public class ArticleFactory2 {

	private final HashSet<Article2> a_articles;
	
	/**
	 * 
	 */
	public ArticleFactory2() {
		a_articles = new HashSet<Article2>();
	}
	
	/**
	 * @param page 
	 * @param year 
	 * @param month 
	 * @param day 
	 * @return
	 */
	public Article2 buildArticle(final File page,
		       				    final Integer year,
		       				    final Integer month,
		       				    final Integer day) {
		final Article2 article = new Article2(page, year, month, day);
		a_articles.add(article);
		return article;
	}
	
	/**
	 * @return sorted list of articles
	 */
	public Article2[] getArticles() {
		final Article2 a[] = a_articles.toArray(new Article2[0]);
		return a;
	}
}
