package data.jsongenerator2;

import java.util.Arrays;
import java.util.HashMap;
/**
 * @author Laurent
 *
 */

public class AuthorFactory2 {

	private final HashMap<String,Author2> a_authors;
	
	/**
	 * 
	 */
	public AuthorFactory2() {
		a_authors = new HashMap<String, Author2>();
	}
	
	/**
	 * if the author already exists, returns it<br/>
	 * if the author does not exists, creates it and returns it
	 * 
	 * @param authorDto 
	 * @return author
	 */
	public Author2 buildAuthor(final ParserAuthorDto2 authorDto) {
		
		final String encodedName = computeEncodedName(authorDto);
		
		if (a_authors.containsKey(encodedName)) {
			return a_authors.get(encodedName);
		}
		
		final Author2 author = new Author2(authorDto.getNamePrefix(),
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
    public Author2 peekAuthor(final ParserAuthorDto2 authorDto) {
        
        final String encodedName = computeEncodedName(authorDto);
        
        if (a_authors.containsKey(encodedName)) {
            return a_authors.get(encodedName);
        }
        
        return null;
    }
	
	/**
	 * @return sorted list of all authors
	 */
	public Author2[] getAuthors() {
		final Author2 a[] = a_authors.values().toArray(new Author2[0]);		
		Arrays.sort(a);
		return a;
	}

    private String computeEncodedName(final ParserAuthorDto2 authorDto) {
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