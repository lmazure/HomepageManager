package data.jsongenerator;

import java.util.Arrays;
import java.util.HashMap;
/**
 * @author Laurent
 *
 */

public class AuthorFactory {

	private final HashMap<String,Author> a_authors;
	
	/**
	 * 
	 */
	public AuthorFactory() {
		a_authors = new HashMap<String, Author>();
	}
	
	/**
	 * if the author already exists, returns it<br/>
	 * if the author does not exists, creates it and returns it
	 * 
	 * @param authorDto 
	 * @return author
	 */
	public Author buildAuthor(final ParserAuthorDto authorDto) {
		
		final String encodedName = computeEncodedName(authorDto);
		
		if (a_authors.containsKey(encodedName)) {
			return a_authors.get(encodedName);
		}
		
		final Author author = new Author(authorDto.getNamePrefix(),
		                                 authorDto.getFirstName(),
		                                 authorDto.getMiddleName(),
		                                 authorDto.getLastName(),
		                                 authorDto.getNameSuffix(),
		                                 authorDto.getGivenName());
		a_authors.put(encodedName, author);
		return author;
	}

	   /**
     * if the author already exists, returns it<br/>
     * if the author does not exists, returns null
     * 
     * @param authorDto 
     * @return author
     */
    public Author peekAuthor(final ParserAuthorDto authorDto) {
        
        final String encodedName = computeEncodedName(authorDto);
        
        if (a_authors.containsKey(encodedName)) {
            return a_authors.get(encodedName);
        }
        
        return null;
    }
	
	/**
	 * @return sorted list of all authors
	 */
	public Author[] getAuthors() {
		final Author a[] = a_authors.values().toArray(new Author[0]);		
		Arrays.sort(a);
		return a;
	}

    private String computeEncodedName(final ParserAuthorDto authorDto) {
        final String encodedName =
            ((authorDto.getLastName() == null)   ? "" : authorDto.getLastName())   + '\n' +
            ((authorDto.getGivenName() == null)  ? "" : authorDto.getGivenName())  + '\n' +
            ((authorDto.getFirstName() == null)  ? "" : authorDto.getFirstName())  + '\n' +
            ((authorDto.getMiddleName() == null) ? "" : authorDto.getMiddleName()) + '\n' +
            ((authorDto.getNamePrefix() == null) ? "" : authorDto.getNameSuffix()) + '\n' + 
            ((authorDto.getNameSuffix() == null) ? "" : authorDto.getNamePrefix());
        return encodedName;
    }
}