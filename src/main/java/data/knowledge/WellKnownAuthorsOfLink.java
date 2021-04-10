package data.knowledge;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import utils.xmlparsing.AuthorData;

public class WellKnownAuthorsOfLink {

    private static final Map<String, WellKnownAuthors> s_knownUrls = Map.of(
        "eljjdx.canalblog.com", buildWellKnownAuthors("Jérôme", "Cottanceau", false),
        "lexfridman.com", buildWellKnownAuthors("Lex", "Fridman", true),
        "www.inspiredtester.com", buildWellKnownAuthors("Leah", "Stockley", false),
        "www.jwz.org", buildWellKnownAuthors("Jamie", "Zawinski", false),
        "www.numberphile.com", buildWellKnownAuthors("Brady", "Haran", true));

    public static Optional<WellKnownAuthors> getWellKnownAuthors(final URL url) {
        final String host = url.getHost();
        return s_knownUrls.containsKey(host) ? Optional.of(s_knownUrls.get(host))
                                             : Optional.empty();
    }

    private static WellKnownAuthors buildWellKnownAuthors(final String firstName,
                                                          final String lastName,
                                                          final boolean canHaveOtherAuthors) {
        final AuthorData author = new AuthorData(Optional.empty(),
                                                 Optional.of(firstName),
                                                 Optional.empty(),
                                                 Optional.of(lastName),
                                                 Optional.empty(),
                                                 Optional.empty());
        final List<AuthorData> list = new ArrayList<>(1);
        list.add(author);
        return new WellKnownAuthors(list, canHaveOtherAuthors);
    }
}
