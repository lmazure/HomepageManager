package data.knowledge;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import utils.xmlparsing.AuthorData;

public class WellKnownAuthorsOfLink {

    private static final Map<String, WellKnownAuthors> s_knownUrls = Map.of(
        "http://eljjdx.canalblog.com/", buildWellKnownAuthors("Jérôme", "Cottanceau", false),
        "https://lexfridman.com/", buildWellKnownAuthors("Lex", "Fridman", true),
        "https://www.inspiredtester.com/", buildWellKnownAuthors("Leah", "Stockley", false),
        "https://www.jwz.org/", buildWellKnownAuthors("Jamie", "Zawinski", false),
        "https://www.numberphile.com/", buildWellKnownAuthors("Brady", "Haran", true));

    public static Optional<WellKnownAuthors> getWellKnownAuthors(final String url) {
        return s_knownUrls.containsKey(url) ? Optional.of(s_knownUrls.get(url))
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
