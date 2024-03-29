package fr.mazure.homepagemanager.data.knowledge;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import fr.mazure.homepagemanager.utils.internet.UriHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;

/**
 * Manage well known authors of Web sites
 */
public class WellKnownAuthorsOfLink {

    private static final Map<String, WellKnownAuthors> s_knownUrls = Map.of(
        "automathssite.wordpress.com",            buildWellKnownAuthors("Jason", "Lapeyronnie", null, false),
        "eljjdx.canalblog.com",                   buildWellKnownAuthors("Jérôme", "Cottanceau", "El Jj", false),
        "lexfridman.com",                         buildWellKnownAuthors("Lex", "Fridman", null, true),
        "mkyong.com",                             buildWellKnownAuthors("Yong", "Mook Kim", null, true),
        "nipafx.dev",                             buildWellKnownAuthors("Nicolai", "Parlog", null, true),
        "scienceetonnante.substack.com",          buildWellKnownAuthors("David", "Louapre", null, false),
        "www.inspiredtester.com",                 buildWellKnownAuthors("Leah", "Stockley", null, false),
        "www.jwz.org",                            buildWellKnownAuthors("Jamie", "Zawinski", null, false),
        "www.numberphile.com",                    buildWellKnownAuthors("Brady", "Haran", null, true));

    /**
     * @param url site URL
     * @return well known authors of the site
     */
    public static Optional<WellKnownAuthors> getWellKnownAuthors(final String url) { // TODO should return a set instead of an optional
        if (!UriHelper.isValidUri(url)) {
            return Optional.empty();
        }
        final String host = UriHelper.getHost(url);
        if (host == null) {
            return Optional.empty();
        }
        return s_knownUrls.containsKey(host) ? Optional.of(s_knownUrls.get(host))
                                             : Optional.empty();
    }

    private static WellKnownAuthors buildWellKnownAuthors(final String firstName,
                                                          final String lastName,
                                                          final String givenName,
                                                          final boolean canHaveOtherAuthors) {
        final AuthorData author = new AuthorData(Optional.empty(),
                                                 Optional.of(firstName),
                                                 Optional.empty(),
                                                 Optional.of(lastName),
                                                 Optional.empty(),
                                                 Optional.ofNullable(givenName));
        final List<AuthorData> list = new ArrayList<>(1);
        list.add(author);
        return new WellKnownAuthors(list, canHaveOtherAuthors);
    }
}
