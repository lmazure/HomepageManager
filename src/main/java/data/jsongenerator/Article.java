package data.jsongenerator;

import java.io.File;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class Article {

    private final File _referringPage;
    private final Optional<TemporalAccessor> _date;
    private final ArrayList<Author> _authors;
    private final ArrayList<Link> _links;

    public Article(final File page,
                   final Optional<TemporalAccessor> date) {
        _referringPage = page;
        _date = date;
        _authors = new ArrayList<Author>();
        _links = new ArrayList<Link>();
    }

    /**
     * @return the referringPage
     */
    public File getReferringPage() {
        return _referringPage;
    }

    public Optional<TemporalAccessor> getDateData() {
        return _date;
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
        final Link a[] = _links.toArray(new Link[0]);
        Arrays.sort(a);
        return a;
    }
}
