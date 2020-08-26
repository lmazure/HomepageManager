package data.jsongenerator;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Optional;

import utils.xmlparsing.AuthorData;

public class Author extends AuthorData implements Comparable<Author> {

	private final SortingKey _sortingKey;
	private final ArrayList<Article> _articles;
    private final ArrayList<Link> _links;
	static private final Collator s_collator = Collator.getInstance();
	
	static public class SortingKey implements Comparable<SortingKey> {
		
		private final String _normalizedName;
		
		SortingKey(final Optional<String> namePrefix,
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
			
			_normalizedName = normalizedName;
		}
		
		@Override
		public int compareTo(final SortingKey o) {
			return s_collator.compare(_normalizedName, o._normalizedName);
		}

		@Override
		public String toString() {
			return _normalizedName;
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
	public Author(final Optional<String> namePrefix,
			      final Optional<String> firstName,
			      final Optional<String> middleName,
			      final Optional<String> lastName,
			      final Optional<String> nameSuffix,
			      final Optional<String> givenName) {
	    super(namePrefix, firstName, middleName, lastName, nameSuffix, givenName);
		_sortingKey = new SortingKey(namePrefix, firstName, middleName, lastName, nameSuffix, givenName);
		_articles = new ArrayList<Article>();
        _links = new ArrayList<Link>();
	}
	
	/**
	 * @return sorting key
	 */
	public SortingKey getSortingKey() {
		return _sortingKey;
	}

	/**
	 * @param article
	 */
	public void addArticle(final Article article) {
		_articles.add(article);
	}
	
	/**
	 * @return articles written by the author
	 */
	public Article[] getArticles() {
		return _articles.toArray(new Article[0]);
	}

    /**
     * @param link
     */
    public void addLink(final Link link) {
        _links.add(link);
    }
    
	   /**
     * @return links containing information about the author
     */
    public Link[] getLinks() {
        return _links.toArray(new Link[0]);
    }
    
	@Override
	public int compareTo(final Author o) {
		return getSortingKey().compareTo(o.getSortingKey());
	}
}
