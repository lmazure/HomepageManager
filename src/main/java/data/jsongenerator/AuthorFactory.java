package data.jsongenerator;

import java.util.Arrays;
import java.util.HashMap;

import utils.xmlparsing.AuthorData;

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
	 * @param authorData 
	 * @return author
	 */
	public Author buildAuthor(final AuthorData authorData) {
		
		final String encodedName = computeEncodedName(authorData);
		
		if (a_authors.containsKey(encodedName)) {
			return a_authors.get(encodedName);
		}
		
		final Author author = new Author(authorData.getNamePrefix(),
		                                 authorData.getFirstName(),
		                                 authorData.getMiddleName(),
		                                 authorData.getLastName(),
		                                 authorData.getNameSuffix(),
		                                 authorData.getGivenName());
		a_authors.put(encodedName, author);
		return author;
	}

	   /**
     * if the author already exists, returns it<br/>
     * if the author does not exists, returns null
     * 
     * @param authorData 
     * @return author
     */
    public Author peekAuthor(final AuthorData authorData) {
        
        final String encodedName = computeEncodedName(authorData);
        
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

    private String computeEncodedName(final AuthorData authorData) {
        final String encodedName =
            authorData.getLastName().orElse("") + '\n' +
            authorData.getGivenName().orElse("") + '\n' +
            authorData.getFirstName().orElse("") + '\n' +
            authorData.getMiddleName().orElse("") + '\n' +
            authorData.getNamePrefix().orElse("") + '\n' + 
            authorData.getNameSuffix().orElse("");
        return encodedName;
    }
}