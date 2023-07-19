package fr.mazure.homepagemanager.data.linkchecker;

import java.util.HashSet;
import java.util.Set;

import fr.mazure.homepagemanager.data.internet.FullFetchedLinkData;
import fr.mazure.homepagemanager.data.internet.HeaderFetchedLinkData;
import fr.mazure.homepagemanager.data.linkchecker.linkstatusanalyzer.RedirectionMatcher;
import fr.mazure.homepagemanager.utils.internet.HttpHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkStatus;

/**
 * Expected redirection data
 */
public class RedirectionData {

    private final RedirectionMatcher _basicOk;
    private final RedirectionMatcher _basicError;

    /**
     * constructor
     */
    public RedirectionData() {
        _basicOk = new RedirectionMatcher();
        _basicOk.add("https?://(" + RedirectionMatcher.ANY_STRING + "/)*" + RedirectionMatcher.ANY_STRING + "/?", Set.of(Integer.valueOf(200)), RedirectionMatcher.Multiplicity.ONE);
        _basicOk.compile();
        final Set<Integer> basicErrorCodes = new HashSet<>();
        basicErrorCodes.add(null);
        basicErrorCodes.add(Integer.valueOf(400));
        basicErrorCodes.add(Integer.valueOf(403));
        basicErrorCodes.add(Integer.valueOf(404));
        basicErrorCodes.add(Integer.valueOf(500));
        basicErrorCodes.add(Integer.valueOf(999));  // TODO handle fucking LinkedIn

        _basicError = new RedirectionMatcher();
        _basicError.add("https?://(" + RedirectionMatcher.ANY_STRING + "/)*" + RedirectionMatcher.ANY_STRING + "/?", basicErrorCodes, RedirectionMatcher.Multiplicity.ONE);
        _basicError.compile();
    }

    /**
     * Compute the possible statuses of the redirection chain
     * 
     * @param effectiveData redirection chain
     * @return possible statuses
     */
    public Set<LinkStatus> getPossibleStatuses(final FullFetchedLinkData effectiveData) {
        if (_basicOk.doesRedirectionMatch(effectiveData)) {
            return Set.of(LinkStatus.OK, LinkStatus.ZOMBIE, LinkStatus.OBSOLETE);
        }
        if (_basicError.doesRedirectionMatch(effectiveData)) {
            return Set.of(LinkStatus.DEAD);
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
    }}
