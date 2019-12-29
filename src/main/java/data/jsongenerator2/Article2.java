package data.jsongenerator2;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Laurent
 *
 */
public class Article2 {

	private final File a_referringPage;
	private final Integer a_dateYear;
	private final Integer a_dateMonth;
	private final Integer a_dateDay;
	private final ArrayList<Author2> a_authors;
	private final ArrayList<Link2> a_links;
	
	/**
	 * @param page
	 * @param year
	 * @param month
	 * @param day
	 */
	public Article2(final File page,
			       final Integer year,
			       final Integer month,
			       final Integer day) {
		a_referringPage = page;
		a_dateYear = year;
		a_dateMonth = month;
		a_dateDay = day;
		a_authors = new ArrayList<Author2>();
		a_links = new ArrayList<Link2>();
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
	public void addAuthor(final Author2 author) {
		a_authors.add(author);
	}
	
	/**
	 * @return
	 */
	public Author2[] getAuthors() {
		Author2 a[] = a_authors.toArray(new Author2[0]);
		Arrays.sort(a);
		return a;
	}
	
	/**
	 * @param link
	 */
	public void addLink(final Link2 link) {
		a_links.add(link);
	}
	
	/**
	 * @return
	 */
	public Link2[] getLinks() {
		Link2 a[] = a_links.toArray(new Link2[0]);
		Arrays.sort(a);
		return a;
	}
}
