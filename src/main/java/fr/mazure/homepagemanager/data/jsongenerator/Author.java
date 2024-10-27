package fr.mazure.homepagemanager.data.jsongenerator;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;

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

        @Override
        public int hashCode() {
            return Objects.hash(_normalizedName);
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final SortingKey other = (SortingKey) obj;
            return Objects.equals(_normalizedName, other._normalizedName);
        }

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
                builder.append(normalize(app.get()));
            }
        }

        private static String normalize(final String str) {
            final StringBuilder result = new StringBuilder(str.length());

            for (final char c : str.toCharArray()) {
                if (Character.isLetterOrDigit(c)) {
                    result.append(c);
                }
            }

            return result.toString();
        }
    }

    /**
     * @param namePrefix Name prefix
     * @param firstName First name
     * @param middleName Middle name
     * @param lastName Last name
     * @param nameSuffix Name suffix
     * @param givenName Given name
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
     * @param article add an article written by the author
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
     * @param link add a link containing information about the author
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
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public int compareTo(final Author o) {
        return getSortingKey().compareTo(o.getSortingKey());
    }
}
