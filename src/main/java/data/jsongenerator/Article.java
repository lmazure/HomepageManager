package data.jsongenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import utils.xmlparsing.DateData;

public class Article {

	private final File a_referringPage;
	private final Optional<DateData> _dateData;
	private final ArrayList<Author> a_authors;
	private final ArrayList<Link> a_links;
	
	public Article(final File page,
			       final Optional<DateData> dateData) {
		a_referringPage = page;
		_dateData = dateData;
		a_authors = new ArrayList<Author>();
		a_links = new ArrayList<Link>();
	}

	/**
	 * @return the referringPage
	 */
	public File getReferringPage() {
		return a_referringPage;
	}

	public Optional<DateData> getDateData() {
		return _dateData;
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
