package fr.mazure.homepagemanager.data.jsongenerator;

import java.io.File;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

/**
 *
 */
public class Article {

    private final File _referringPage;
    private final Optional<TemporalAccessor> _date;
    private final ArrayList<Author> _authors;
    private final ArrayList<Link> _links;

    /**
     * Article constructor
     *
     * @param page the page containing the article
     * @param date the date of the article
     */
    public Article(final File page,
                   final Optional<TemporalAccessor> date) {
        _referringPage = page;
        _date = date;
        _authors = new ArrayList<>();
        _links = new ArrayList<>();
    }

    /**
     * @return the page containing the article
     */
    public File getReferringPage() {
        return _referringPage;
    }

    /**
     * @return the date of the article
     */
    public Optional<TemporalAccessor> getDateData() {
        return _date;
    }

    /**
     * Add an author to the article
     *
     * @param author the author to add
     */
    public void addAuthor(final Author author) {
        _authors.add(author);
    }

    /**
     * @return the list of authors
     */
    public Author[] getAuthors() {
        final Author a[] = _authors.toArray(new Author[0]);
        Arrays.sort(a);
        return a;
    }

    /**
     * Add a link to the article
     *
     * @param link the link to add
     */
    public void addLink(final Link link) {
        _links.add(link);
    }

    /**
     * @return the list of links
     */
    public Link[] getLinks() {
        final Link a[] = _links.toArray(new Link[0]);
        Arrays.sort(a);
        return a;
    }
}
