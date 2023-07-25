package fr.mazure.homepagemanager.data.linkchecker.linkstatusanalyzer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.mazure.homepagemanager.data.internet.FullFetchedLinkData;
import fr.mazure.homepagemanager.data.internet.HeaderFetchedLinkData;
import fr.mazure.homepagemanager.utils.internet.HttpHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkStatus;

/**
 * Expected redirection data
 */
public class RedirectionData {

    private final List<RedirectionMatcher> _matchers;

    /**
     * constructor
     */
    public RedirectionData() {

        _matchers = new ArrayList<>();

        final Set<Integer> errorCodes = new HashSet<>();
        errorCodes.add(null);
        errorCodes.add(Integer.valueOf(400));
        errorCodes.add(Integer.valueOf(403));
        errorCodes.add(Integer.valueOf(404));
        errorCodes.add(Integer.valueOf(410));
        errorCodes.add(Integer.valueOf(500));
        errorCodes.add(Integer.valueOf(999));  // TODO handle fucking LinkedIn

        final Set<Integer> redirectionCodes = new HashSet<>();
        redirectionCodes.add(Integer.valueOf(301));
        redirectionCodes.add(Integer.valueOf(302));
        redirectionCodes.add(Integer.valueOf(303));
        redirectionCodes.add(Integer.valueOf(307));

        final RedirectionMatcher fromYoutubeChannelToCookiesConfiguration = new RedirectionMatcher("from Youtube channel to cookies configuration",
                                                                                                   Set.of(LinkStatus.OK));
        fromYoutubeChannelToCookiesConfiguration.add("\\Qhttps://www.youtube.com/channel/\\E" + RedirectionMatcher.ANY_STRING,
                                                     Set.of(Integer.valueOf(302)),
                                                     RedirectionMatcher.Multiplicity.ONE);
        fromYoutubeChannelToCookiesConfiguration.add("\\Qhttps://consent.youtube.com/m?continue=https%3A%2F%2Fwww.youtube.com%2Fchannel%2F\\E" + RedirectionMatcher.ANY_STRING,
                                                     Set.of(Integer.valueOf(302)),
                                                     RedirectionMatcher.Multiplicity.ONE);
        fromYoutubeChannelToCookiesConfiguration.add("\\Qhttps://consent.youtube.com/ml?continue=https://www.youtube.com/channel/\\E"  + RedirectionMatcher.ANY_STRING,
                                                     Set.of(Integer.valueOf(200)),
                                                     RedirectionMatcher.Multiplicity.ONE);
        fromYoutubeChannelToCookiesConfiguration.compile();
        _matchers.add(fromYoutubeChannelToCookiesConfiguration);

        final RedirectionMatcher fromYoutubeChannelToYoutubeChannel = new RedirectionMatcher("from Youtube channel to Youtube channel",
                                                                                             Set.of(LinkStatus.OK));
        fromYoutubeChannelToYoutubeChannel.add("\\Qhttps://www.youtube.com/channel/\\E" + RedirectionMatcher.ANY_STRING,
                                                    Set.of(Integer.valueOf(302)),
                                                    RedirectionMatcher.Multiplicity.ONE);
        fromYoutubeChannelToYoutubeChannel.add("\\Qhttps://consent.youtube.com/m?continue=https%3A%2F%2Fwww.youtube.com%2Fchannel%2F\\E" + RedirectionMatcher.ANY_STRING,
                                                    Set.of(Integer.valueOf(303)),
                                                    RedirectionMatcher.Multiplicity.ONE);
        fromYoutubeChannelToYoutubeChannel.add("\\Qhttps://www.youtube.com/channel/\\E"  + RedirectionMatcher.ANY_STRING,
                                                    Set.of(Integer.valueOf(200)),
                                                    RedirectionMatcher.Multiplicity.ONE);
        fromYoutubeChannelToYoutubeChannel.compile();
        _matchers.add(fromYoutubeChannelToYoutubeChannel);

        final RedirectionMatcher mediumAnalytics = new RedirectionMatcher("Medium analytics",
                                                                          Set.of(LinkStatus.OK,
                                                                                  LinkStatus.OBSOLETE));
        mediumAnalytics.add("\\Qhttps://\\E(?<site>[^/]+)/(?<article>.+)",
                            Set.of(Integer.valueOf(307)),
                            RedirectionMatcher.Multiplicity.ONE);
        mediumAnalytics.add("\\Qhttps://\\Emedium.com/m/global-identity-2\\?redirectUrl=https%3A%2F%2F\\k<site>%2F\\k<article>",
                            Set.of(Integer.valueOf(307)),
                            RedirectionMatcher.Multiplicity.ONE);
        mediumAnalytics.add("\\Qhttps://\\E\\k<site>/\\k<article>\\?gi=[0-9a-z]+",
                            Set.of(Integer.valueOf(200)),
                            RedirectionMatcher.Multiplicity.ONE);
        mediumAnalytics.compile();
        _matchers.add(mediumAnalytics);

        final RedirectionMatcher redirectionEndingInSuccess = new RedirectionMatcher("redirection ending in success (last URL should be used)",
                                                                                     Set.of());
        redirectionEndingInSuccess.add("https?://"  + RedirectionMatcher.ANY_STRING,
                                       redirectionCodes,
                                       RedirectionMatcher.Multiplicity.ONE_OR_MANY);
        redirectionEndingInSuccess.add("https?://"  + RedirectionMatcher.ANY_STRING,
                                       Set.of(Integer.valueOf(200)),
                                       RedirectionMatcher.Multiplicity.ONE);
        redirectionEndingInSuccess.compile();
        _matchers.add(redirectionEndingInSuccess);

        final RedirectionMatcher redirectionEndingInError = new RedirectionMatcher("redirection ending with an error code",
                                                                                   Set.of(LinkStatus.DEAD));
        redirectionEndingInError.add("https?://"  + RedirectionMatcher.ANY_STRING,
                                     redirectionCodes,
                                     RedirectionMatcher.Multiplicity.ONE_OR_MANY);
        redirectionEndingInError.add("https?://"  + RedirectionMatcher.ANY_STRING,
                                     errorCodes,
                                     RedirectionMatcher.Multiplicity.ONE);
        redirectionEndingInError.compile();
        _matchers.add(redirectionEndingInError);

        final RedirectionMatcher basicError = new RedirectionMatcher("direct failure",
                                                                     Set.of(LinkStatus.DEAD));
        basicError.add("https?://"  + RedirectionMatcher.ANY_STRING,
                       errorCodes,
                       RedirectionMatcher.Multiplicity.ONE);
        basicError.compile();
        _matchers.add(basicError);

        final RedirectionMatcher basicOk = new RedirectionMatcher("direct success",
                                                                  Set.of(LinkStatus.OK, LinkStatus.ZOMBIE, LinkStatus.OBSOLETE));
        basicOk.add("https?://" + RedirectionMatcher.ANY_STRING,
                    Set.of(Integer.valueOf(200)),
                    RedirectionMatcher.Multiplicity.ONE);
        basicOk.compile();
        _matchers.add(basicOk);
    }

    /**
     * Compute the possible statuses of the redirection chain
     *
     * @param effectiveData redirection chain
     * @return possible statuses
     */
    public Match getMatch(final FullFetchedLinkData effectiveData) {
        for (final RedirectionMatcher matcher: _matchers) {
            if (matcher.doesRedirectionMatch(effectiveData)) {
                return matchOf(matcher);
            }
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
