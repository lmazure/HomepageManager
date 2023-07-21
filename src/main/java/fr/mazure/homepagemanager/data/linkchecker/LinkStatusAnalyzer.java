package fr.mazure.homepagemanager.data.linkchecker;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.mazure.homepagemanager.data.internet.FullFetchedLinkData;
import fr.mazure.homepagemanager.data.internet.HeaderFetchedLinkData;
import fr.mazure.homepagemanager.data.internet.SynchronousSiteDataRetriever;
import fr.mazure.homepagemanager.data.linkchecker.linkstatusanalyzer.RedirectionData;
import fr.mazure.homepagemanager.utils.internet.HttpHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkStatus;

/**
 * Base class for all the link status analyzers (i.e. the classes deciding if the effective data matches the expected data)
 */
public class LinkStatusAnalyzer {

    private static RedirectionData _redirectionData = new RedirectionData();

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

    private static Set<LinkStatus> getPossibleStatuses(final FullFetchedLinkData effectiveData) {
        if (numberOfRedirections(effectiveData) == SynchronousSiteDataRetriever.getMaximumNumberOfRedirections()) {
            return Set.of(LinkStatus.DEAD);
        }
        return _redirectionData.getMatch(effectiveData).statuses();
    }

    private static int numberOfRedirections(final FullFetchedLinkData effectiveData) {
        int n = 0;
        HeaderFetchedLinkData d = effectiveData.previousRedirection();
        while (d != null) {
            n++;
            d = d.previousRedirection();
        }
        return n;
    }

}

