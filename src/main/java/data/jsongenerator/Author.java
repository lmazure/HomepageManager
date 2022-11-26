package data.jsongenerator;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;

import utils.xmlparsing.AuthorData;

/**
*
*/
public class Author extends AuthorData implements Comparable<Author> {

    private final SortingKey _sortingKey;
    private final ArrayList<Article> _articles;
    private final ArrayList<Link> _links;

    /**
     *
     */
    public static class SortingKey implements Comparable<SortingKey> {

        private static final Collator s_collator = Collator.getInstance(Locale.UK);
        private final String _normalizedName;

        SortingKey(final Optional<String> namePrefix,
                   final Optional<String> firstName,
                   final Optional<String> middleName,
                   final Optional<String> lastName,
                   final Optional<String> nameSuffix,
                   final Optional<String> givenName) {

            final StringBuilder normalizedName = new StringBuilder(128);
            append(normalizedName, lastName);
            append(normalizedName, givenName);
            append(normalizedName, firstName);
            append(normalizedName, middleName);
            append(normalizedName, nameSuffix);
            append(normalizedName, namePrefix);

            _normalizedName = normalizedName.toString();
        }

        @Override
        public int compareTo(final SortingKey o) {
            return s_collator.compare(_normalizedName, o._normalizedName);
        }

        @Override
        public String toString() {
            return _normalizedName;
        }

        private static void append(final StringBuilder builder,
                                   final Optional<String> app) {

            if (builder.length() > 0) {
                builder.append('!'); // to not use '\n', the collator will ignore it when comparing strings
            }

            if (app.isPresent()) {
                builder.append(app.get());
            }
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
        _articles = new ArrayList<>();
        _links = new ArrayList<>();
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
