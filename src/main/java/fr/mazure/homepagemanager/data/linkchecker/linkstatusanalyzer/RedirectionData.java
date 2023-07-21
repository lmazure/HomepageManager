package fr.mazure.homepagemanager.data.linkchecker.linkstatusanalyzer;

import java.util.HashSet;
import java.util.Set;

import fr.mazure.homepagemanager.data.internet.FullFetchedLinkData;
import fr.mazure.homepagemanager.data.internet.HeaderFetchedLinkData;
import fr.mazure.homepagemanager.utils.internet.HttpHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkStatus;

/**
 * Expected redirection data
 */
public class RedirectionData {

    private final RedirectionMatcher _basicOk;
    private final RedirectionMatcher _basicError;
    private final RedirectionMatcher _fromGoogleChannelToCookiesConfiguration;

    /**
     * constructor
     */
    public RedirectionData() {
        _basicOk = new RedirectionMatcher("direct successful", Set.of(LinkStatus.OK, LinkStatus.ZOMBIE, LinkStatus.OBSOLETE));
        _basicOk.add("https?://(" + RedirectionMatcher.ANY_STRING + "/)*" + RedirectionMatcher.ANY_STRING + "/?", Set.of(Integer.valueOf(200)), RedirectionMatcher.Multiplicity.ONE);
        _basicOk.compile();

        final Set<Integer> basicErrorCodes = new HashSet<>();
        basicErrorCodes.add(null);
        basicErrorCodes.add(Integer.valueOf(400));
        basicErrorCodes.add(Integer.valueOf(403));
        basicErrorCodes.add(Integer.valueOf(404));
        basicErrorCodes.add(Integer.valueOf(500));
        basicErrorCodes.add(Integer.valueOf(999));  // TODO handle fucking LinkedIn
        _basicError = new RedirectionMatcher("direct failure", Set.of(LinkStatus.DEAD));
        _basicError.add("https?://(" + RedirectionMatcher.ANY_STRING + "/)*" + RedirectionMatcher.ANY_STRING + "/?", basicErrorCodes, RedirectionMatcher.Multiplicity.ONE);
        _basicError.compile();

        _fromGoogleChannelToCookiesConfiguration = new RedirectionMatcher("from Google channel to cookies configuration", Set.of(LinkStatus.OK, LinkStatus.OBSOLETE));
        _fromGoogleChannelToCookiesConfiguration.add("\\Qhttps://www.youtube.com/channel/\\E.*", Set.of(Integer.valueOf(302)), RedirectionMatcher.Multiplicity.ONE);
        _fromGoogleChannelToCookiesConfiguration.add("\\Qhttps://consent.youtube.com/m?continue=https%3A%2F%2Fwww.youtube.com%2Fchannel%2F\\E.*", Set.of(Integer.valueOf(302)), RedirectionMatcher.Multiplicity.ONE);
        _fromGoogleChannelToCookiesConfiguration.add("\\Qhttps://consent.youtube.com/ml?continue=https://www.youtube.com/channel/\\E.*", Set.of(Integer.valueOf(200)), RedirectionMatcher.Multiplicity.ONE);
        _fromGoogleChannelToCookiesConfiguration.compile();
    }

    /**
     * Compute the possible statuses of the redirection chain
     *
     * @param effectiveData redirection chain
     * @return possible statuses
     */
    public Match getMatch(final FullFetchedLinkData effectiveData) {
        if (_fromGoogleChannelToCookiesConfiguration.doesRedirectionMatch(effectiveData)) {
            return matchOf(_fromGoogleChannelToCookiesConfiguration);
        }
        if (_basicError.doesRedirectionMatch(effectiveData)) {
            return matchOf(_basicError);
        }
        if (_basicOk.doesRedirectionMatch(effectiveData)) {
            return matchOf(_basicError);
        }
        throw new UnsupportedOperationException(effectiveDataToString(effectiveData));
    }

    private static String effectiveDataToString(final FullFetchedLinkData effectiveData) {
        final StringBuilder builder = new StringBuilder();
        builder.append(effectiveData.url());
        builder.append("|");
        if (effectiveData.headers().isPresent()) {
            builder.append(HttpHelper.getResponseCodeFromHeaders(effectiveData.headers().get()));
        }
        builder.append("→");
        HeaderFetchedLinkData d = effectiveData.previousRedirection();
        while (d != null) {
            builder.append(d.url());
            builder.append("|");
            if (d.headers().isPresent()) {
                builder.append(HttpHelper.getResponseCodeFromHeaders(d.headers().get()));
            }
            builder.append("→");
            d = d.previousRedirection();
        }
        return builder.toString();
    }

    private static Match matchOf(final RedirectionMatcher matcher) {
        return new Match(matcher.getName(), matcher.getStatuses());
    }

    /**
     * Result of a redirection chain matching
     *
     * @param name Name of the matcher
     * @param statuses Possible statuses of the redirection chain
     *
     */
    public record Match(String name, Set<LinkStatus> statuses) {}
}
