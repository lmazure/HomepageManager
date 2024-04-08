package fr.mazure.homepagemanager.data.knowledge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import fr.mazure.homepagemanager.utils.internet.UriHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;

/**
 * Manage well known authors of Web sites
 */
public class WellKnownAuthorsOfLink {

    /**
     * Records of the well-known authors
     * @param compulsoryAuthors   compulsory authors
     * @param canHaveOtherAuthors can there be other authors?
     */
    public record KnownAuthors (List<AuthorData> compulsoryAuthors,
                                boolean canHaveOtherAuthors) {}

    private static final Map<String, KnownAuthors> s_knownUrls = new HashMap<>();

    static {
        s_knownUrls.put("automathssite.wordpress.com",    buildKnownAuthors(WellKnownAuthors.JASON_LAPERONNIE,  false));
        s_knownUrls.put("eljjdx.canalblog.com",           buildKnownAuthors(WellKnownAuthors.JEROME_COTTANCEAU, false));
        s_knownUrls.put("lexfridman.com",                 buildKnownAuthors(WellKnownAuthors.LEX_FRIDMAN,       true));
        s_knownUrls.put("mydeveloperplanet.com",          buildKnownAuthors(WellKnownAuthors.GUNTER_ROTSAERT,   false));
        s_knownUrls.put("mkyong.com",                     buildKnownAuthors(WellKnownAuthors.YONG_MOOK_KIM,     false));
        s_knownUrls.put("nipafx.dev",                     buildKnownAuthors(WellKnownAuthors.NICOLAI_PARLOG,    false));
        s_knownUrls.put("scienceetonnante.substack.com",  buildKnownAuthors(WellKnownAuthors.DAVID_LOUAPRE,     false));
        s_knownUrls.put("www.inspiredtester.com",         buildKnownAuthors(WellKnownAuthors.LEAH_STOCKLEY,     false));
        s_knownUrls.put("www.jwz.org",                    buildKnownAuthors(WellKnownAuthors.JAMIE_ZAWINSKI,    false));
        s_knownUrls.put("www.numberphile.com",            buildKnownAuthors(WellKnownAuthors.BRADY_HARAN,       true));
        s_knownUrls.put("simonwillison.net",              buildKnownAuthors(WellKnownAuthors.SIMON_WILLISON,    false));
        s_knownUrls.put("til.simonwillison.net",          buildKnownAuthors(WellKnownAuthors.SIMON_WILLISON,    false));
        s_knownUrls.put("til.simonwillison.net",          buildKnownAuthors(WellKnownAuthors.SIMON_WILLISON,    false));
    }

    /**
     * @param url site URL
     * @return well known authors of the site
     */
    public static Optional<KnownAuthors> getWellKnownAuthors(final String url) { // TODO should return a set instead of an optional
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

    private static KnownAuthors buildKnownAuthors(final AuthorData author,
                                                  final boolean canHaveOtherAuthors) {
        final List<AuthorData> list = new ArrayList<>(1);
        list.add(author);
        return new KnownAuthors(list, canHaveOtherAuthors);
    }
}
