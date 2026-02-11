package fr.mazure.homepagemanager.data.jsongenerator;

import java.util.Arrays;
import java.util.HashMap;

import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;

/**
 *
 */
public class AuthorFactory {

    private final HashMap<String,Author> _authors;

    /**
     * AuthorFactory constructor
     */
    public AuthorFactory() {
        _authors = new HashMap<>();
    }

    /**
     * if the author already exists, returns it<br/>
     * if the author does not exists, creates it and returns it
     *
     * @param authorData the author data
     * @return the author
     */
    public Author buildAuthor(final AuthorData authorData) {

        final String encodedName = computeEncodedName(authorData);

        if (_authors.containsKey(encodedName)) {
            return _authors.get(encodedName);
        }

        final Author author = new Author(authorData.getNamePrefix(),
                                         authorData.getFirstName(),
                                         authorData.getMiddleName(),
                                         authorData.getLastName(),
                                         authorData.getNameSuffix(),
                                         authorData.getGivenName());
        _authors.put(encodedName, author);
        return author;
    }

    /**
     * if the author already exists, returns it<br/>
     * if the author does not exists, returns null
     *
     * @param authorData the author data
     * @return the author
     */
    public Author peekAuthor(final AuthorData authorData) {

        final String encodedName = computeEncodedName(authorData);

        if (_authors.containsKey(encodedName)) {
            return _authors.get(encodedName);
        }

        return null;
    }

    /**
     * @return sorted list of all authors
     */
    public Author[] getAuthors() {
        final Author a[] = _authors.values().toArray(new Author[0]);
        Arrays.sort(a);
        return a;
    }

    private static String computeEncodedName(final AuthorData authorData) {
        return authorData.getLastName().orElse("") + '\n' +
            authorData.getGivenName().orElse("") + '\n' +
            authorData.getFirstName().orElse("") + '\n' +
            authorData.getMiddleName().orElse("") + '\n' +
            authorData.getNamePrefix().orElse("") + '\n' +
            authorData.getNameSuffix().orElse("");
    }
}