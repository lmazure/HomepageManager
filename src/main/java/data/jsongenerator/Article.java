package data.jsongenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Laurent
 *
 */
public class Article {

	private final File a_referringPage;
	private final Integer a_dateYear;
	private final Integer a_dateMonth;
	private final Integer a_dateDay;
	private final ArrayList<Author> a_authors;
	private final ArrayList<Link> a_links;
	
	/**
	 * @param page
	 * @param year
	 * @param month
	 * @param day
	 */
	public Article(final File page,
			       final Integer year,
			       final Integer month,
			       final Integer day) {
		a_referringPage = page;
		a_dateYear = year;
		a_dateMonth = month;
		a_dateDay = day;
		a_authors = new ArrayList<Author>();
		a_links = new ArrayList<Link>();
	}

	/**
	 * @return the referringPage
	 */
	public File getReferringPage() {
		return a_referringPage;
	}

	/**
	 * @return the dateYear
	 */
	public Integer getDateYear() {
		return a_dateYear;
	}

	/**
	 * @return the dateMonth
	 */
	public Integer getDateMonth() {
		return a_dateMonth;
	}

	/**
	 * @return the dateDay
	 */
	public Integer getDateDay() {
		return a_dateDay;
	}

	/**
	 * @param author
	 */
	public void addAuthor(final Author author) {
		a_authors.add(author);
	}
	
	/**
	 * @return
	 */
	public Author[] getAuthors() {
		Author a[] = a_authors.toArray(new Author[0]);
		Arrays.sort(a);
		return a;
	}
	
	/**
	 * @param link
	 */
	public void addLink(final Link link) {
		a_links.add(link);
	}
	
	/**
	 * @return
	 */
	public Link[] getLinks() {
		Link a[] = a_links.toArray(new Link[0]);
		Arrays.sort(a);
		return a;
	}
}
