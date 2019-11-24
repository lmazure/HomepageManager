package data.jsongenerator;

import java.text.Collator;
import java.util.ArrayList;

/**
 * @author Laurent
 *
 */
public class Author implements Comparable<Author> {

	private final String a_namePrefix;
	private final String a_firstName;
	private final String a_middleName;
	private final String a_lastName;
	private final String a_nameSuffix;
	private final String a_givenName;
	private final SortingKey a_sortingKey;
	private final ArrayList<Article> a_articles;
    private final ArrayList<Link> a_links;
	static private final Collator collator = Collator.getInstance();
	
	/**
	 * @author Laurent
	 *
	 */
	static public class SortingKey implements Comparable<SortingKey> {
		
		private final String a_normalizedName;
		
		SortingKey(
				final String namePrefix,
				final String firstName,
				final String middleName,
				final String lastName,
				final String nameSuffix,
				final String givenName) {
			
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
		
		private String append(final String str, final String app) {
			
			String s = str;
			
			if (s.length() > 0) {
				s += '!'; // to not use '\n', the collator will ignore it when comparing strings
			}
			
			if (app != null) {
				s += app;
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
			final String namePrefix,
			final String firstName,
			final String middleName,
			final String lastName,
			final String nameSuffix,
			final String givenName) {
		a_namePrefix = namePrefix;
		a_firstName = firstName;
		a_middleName = middleName;
		a_lastName = lastName;
		a_nameSuffix = nameSuffix;
		a_givenName = givenName;
		a_sortingKey = new SortingKey(namePrefix, firstName, middleName, lastName, nameSuffix, givenName);
		a_articles = new ArrayList<Article>();
        a_links = new ArrayList<Link>();
	}
	
	/**
	 * @return name prefix
	 */
	public String getNamePrefix() {
		return a_namePrefix;
	}

	/**
	 * @return first name
	 */
	public String getFirstName() {
		return a_firstName;
	}

	/**
	 * @return middle name
	 */
	public String getMiddleName() {
		return a_middleName;
	}

	/**
	 * @return last name
	 */
	public String getLastName() {
		return a_lastName;
	}

	/**
	 * @return name suffix
	 */
	public String getNameSuffix() {
		return a_nameSuffix;
	}

	/**
	 * @return given name
	 */
	public String getGivenName() {
		return a_givenName;
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
