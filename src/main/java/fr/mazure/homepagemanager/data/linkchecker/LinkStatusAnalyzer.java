package fr.mazure.homepagemanager.data.linkchecker;

import java.net.HttpURLConnection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.mazure.homepagemanager.data.internet.FullFetchedLinkData;
import fr.mazure.homepagemanager.data.internet.HeaderFetchedLinkData;
import fr.mazure.homepagemanager.data.linkchecker.linkstatusanalyzer.RedirectionMatcher;
import fr.mazure.homepagemanager.utils.internet.HttpHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkStatus;

/**
 * Base class for all the link status analyzers (i.e. the classes deciding if the effective data matches the expected data 
 */
public class LinkStatusAnalyzer {


    /**
     * @param expectedData data as expected in the XML file
     * @param effectiveData data as retrieved from internet
     * @return true if and only if effectiveData matches expectedData
     */
    public static boolean doesEffectiveDataMatchesExpectedData2(final LinkData expectedData,
                                                               final FullFetchedLinkData effectiveData) {

        if ((expectedData.getStatus() != LinkStatus.OK) && expectedData.getStatus() != LinkStatus.DEAD) {
            if (effectiveData.error().isPresent()) {
                return true;
            }
            if (!httpRequestIsSuccessful(effectiveData.headers().get())) {
                return true;
            }
            final HeaderFetchedLinkData lastRedirection = lastRedirection(effectiveData);
            if (lastRedirection == null) {
                return false;
            }
            if (!httpRequestIsSuccessful(lastRedirection.headers().get())) {
                return true;
            }
            return false;
        }

        final HeaderFetchedLinkData lastRedirection = lastRedirection(effectiveData);
        return (effectiveData.headers().isPresent() &&
                httpRequestIsSuccessful(effectiveData.headers().get()) &&
                ((lastRedirection == null) ||
                 lastRedirection.headers().isPresent() &&
                 httpRequestIsSuccessful(lastRedirection.headers().get())));
    }

    /**
     * @param expectedData data as expected in the XML file
     * @param effectiveData data as retrieved from internet
     * @return true if and only if effectiveData matches expectedData
     */
    public static boolean doesEffectiveDataMatchesExpectedData(final LinkData expectedData,
                                                               final FullFetchedLinkData effectiveData) {
        final Set<LinkStatus> expectedStatuses = getPossibleStatuses(effectiveData);
        return expectedStatuses.contains(expectedData.getStatus());
    }

    private static HeaderFetchedLinkData lastRedirection(final FullFetchedLinkData data) {
        HeaderFetchedLinkData d = data.previousRedirection();
        if (d == null) {
            return null;
        }
        while (d.previousRedirection() != null) {
            d = d.previousRedirection();
        }
        return d;
    }

    private static boolean httpRequestIsSuccessful(final Map<String, List<String>> headers) {
        final int code = HttpHelper.getResponseCodeFromHeaders(headers);
        return (code == HttpURLConnection.HTTP_OK) ||
               (code == HttpURLConnection.HTTP_MOVED_TEMP) ||
               (code == HttpURLConnection.HTTP_SEE_OTHER);
    }

    private static final RedirectionMatcher _basicOk;
    private static final RedirectionMatcher _basicError;
    
    static {
        _basicOk = new RedirectionMatcher();
        _basicOk.add(".*", Set.of(Integer.valueOf(200)), RedirectionMatcher.Multiplicity.ONE);
        _basicOk.compile();
        final Set<Integer> basicErrorCodes = new HashSet<>();
        basicErrorCodes.add(null);
        basicErrorCodes.add(Integer.valueOf(400));
        basicErrorCodes.add(Integer.valueOf(403));
        basicErrorCodes.add(Integer.valueOf(404));
        _basicError = new RedirectionMatcher();
        _basicError.add(".*", basicErrorCodes, RedirectionMatcher.Multiplicity.ONE);
        _basicError.compile();
    }
        
    private static Set<LinkStatus> getPossibleStatuses(final FullFetchedLinkData effectiveData) {
        if (_basicOk.doesRedirectionMatch(effectiveData)) {
            return Set.of(LinkStatus.OK, LinkStatus.ZOMBIE);
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
    }
}

