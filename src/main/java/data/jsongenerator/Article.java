package data.jsongenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import utils.xmlparsing.DateData;

public class Article {

	private final File a_referringPage;
	private final Optional<DateData> _dateData;
	private final ArrayList<Author> _authors;
	private final ArrayList<Link> _links;
	
	public Article(final File page,
			       final Optional<DateData> dateData) {
		a_referringPage = page;
		_dateData = dateData;
		_authors = new ArrayList<Author>();
		_links = new ArrayList<Link>();
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
		_authors.add(author);
	}
	
	/**
	 * @return
	 */
	public Author[] getAuthors() {
		Author a[] = _authors.toArray(new Author[0]);
		Arrays.sort(a);
		return a;
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
		Link a[] = _links.toArray(new Link[0]);
		Arrays.sort(a);
		return a;
	}
}
