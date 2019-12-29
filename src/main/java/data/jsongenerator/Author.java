package data.jsongenerator;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Optional;

import utils.xmlparsing.AuthorData;

public class Author extends AuthorData implements Comparable<Author> {

	private final SortingKey a_sortingKey;
	private final ArrayList<Article> a_articles;
    private final ArrayList<Link> a_links;
	static private final Collator collator = Collator.getInstance();
	
	static public class SortingKey implements Comparable<SortingKey> {
		
		private final String a_normalizedName;
		
		SortingKey(
				final Optional<String> namePrefix,
				final Optional<String> firstName,
				final Optional<String> middleName,
				final Optional<String> lastName,
				final Optional<String> nameSuffix,
				final Optional<String> givenName) {
			
			String normalizedName = "";
			normalizedName = append(normalizedName, lastName);
			normalizedName = append(normalizedName, givenName);
			normalizedName = append(normalizedName, firstName);
			normalizedName = append(normalizedName, middleName);
			normalizedName = append(normalizedName, nameSuffix);
			normalizedName = append(normalizedName, namePrefix);
			
			a_normalizedName = normalizedName;
		}
		
		@Override
		public int compareTo(final SortingKey o) {
			return collator.compare(a_normalizedName, o.a_normalizedName);
		}

		@Override
		public String toString() {
			return a_normalizedName;
		}
		
		private String append(final String str, final Optional<String> app) {
			
			String s = str;
			
			if (s.length() > 0) {
				s += '!'; // to not use '\n', the collator will ignore it when comparing strings
			}
			
			if (app.isPresent()) {
				s += app.get();
			}
			
			return s;
		}
	}

	/**
	 * @param namePrefix 
	 * @param firstName 
	 * @param middleName 
	 * @param lastName 
	 * @param nameSuffix 
	 * @param givenName 
	 */
	public Author(
			final Optional<String> namePrefix,
			final Optional<String> firstName,
			final Optional<String> middleName,
			final Optional<String> lastName,
			final Optional<String> nameSuffix,
			final Optional<String> givenName) {
	    super(namePrefix, firstName, middleName, lastName, nameSuffix, givenName);
		a_sortingKey = new SortingKey(namePrefix, firstName, middleName, lastName, nameSuffix, givenName);
		a_articles = new ArrayList<Article>();
        a_links = new ArrayList<Link>();
	}
	
	/**
	 * @return sorting key
	 */
	public SortingKey getSortingKey() {
		return a_sortingKey;
	}

	/**
	 * @param article
	 */
	public void addArticle(final Article article) {
		a_articles.add(article);
	}
	
	/**
	 * @return articles written by the author
	 */
	public Article[] getArticles() {
		return a_articles.toArray(new Article[0]);
	}

    /**
     * @param link
     */
    public void addLink(final Link link) {
        a_links.add(link);
    }
    
	   /**
     * @return links containing information about the author
     */
    public Link[] getLinks() {
        return a_links.toArray(new Link[0]);
    }
    

	@Override
	public int compareTo(final Author o) {
		return getSortingKey().compareTo(o.getSortingKey());
	}
}
